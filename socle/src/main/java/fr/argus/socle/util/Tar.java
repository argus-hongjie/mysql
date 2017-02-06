package fr.argus.socle.util;

import static com.diffplug.common.base.Errors.rethrow;
import static fr.argus.socle.util.Helper.firstOrDefault;
import static java.nio.file.Files.isRegularFile;
import static java.nio.file.Files.move;
import static java.nio.file.Files.newInputStream;
import static java.nio.file.Files.newOutputStream;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.compress.archivers.tar.TarArchiveOutputStream.BIGNUMBER_STAR;
import static org.apache.commons.compress.archivers.tar.TarArchiveOutputStream.LONGFILE_GNU;
import static org.apache.commons.compress.utils.IOUtils.copy;
import static org.apache.commons.io.FileUtils.readFileToByteArray;
import static org.apache.logging.log4j.util.Strings.isBlank;
import static org.assertj.core.util.Arrays.array;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.tika.io.IOUtils;

import lombok.Cleanup;


public class Tar {

	/** Add a entry to tar, create the tar if not existed, create parent dirs if not existed.
	 * @param dest
	 * @param parentPathInTar
	 * @param entryName
	 * @param entryData
	 * @return
	 * @throws Exception
	 */
	public static String addEntry(String dest, String parentPathInTar, String entryName, byte[] entryData) throws Exception {
		List<String> result = tarCreator.apply(dest, rethrow().wrapFunction(outputStream->{
			if (isRegularFile(get(dest))) {
				@Cleanup TarArchiveInputStream tis = (TarArchiveInputStream) new ArchiveStreamFactory().createArchiveInputStream(new BufferedInputStream(Files.newInputStream(get(dest))));
		        TarArchiveEntry entry;
		        Path newEntryName = get(parentPathInTar).resolve(entryName);
		        while ( ( entry = tis.getNextTarEntry()) != null && !newEntryName.equals(get(entry.getName()))) addEntry(outputStream, get(""), entry.getName(), tis, false);
			}
			
			return Arrays.asList(addEntry(outputStream, get(parentPathInTar), entryName, new BufferedInputStream( new ByteArrayInputStream(entryData))));
		}));
		return result.size() > 0 ? result.get(0) : null;
	}
	
	
	/** Add a file to tar, create the tar if not existed, create parent dirs if not existed.
	 * @param dest
	 * @param parentPathInTar
	 * @param entryName
	 * @param regularFilePath
	 * @return
	 * @throws Exception
	 */
	public static String addFile(String dest, String parentPathInTar, String entryName, String regularFilePath) throws Exception {
		return addEntry(dest, parentPathInTar, isBlank(entryName) ? regularFilePath : entryName, readFileToByteArray(new File(regularFilePath)));
	}

	/** Create new tar from files, create parent dirs if not existed.
	 * @param dest
	 * @param parentPathInTar
	 * @param files
	 * @return
	 * @throws Exception
	 */
	public static List<String> create(String dest, String parentPathInTar, String... files) throws Exception {
		return tarCreator.apply(dest, outputStream->stream(ofNullable(files).orElse(array())).flatMap(rethrow().wrapFunction(file->create(outputStream, get(parentPathInTar), new File(file)).stream())).collect(toList()));
	}
	
	private static BiFunction<String, Function<TarArchiveOutputStream, List<String>>, List<String>> tarCreator = (dest, consumer) -> rethrow().get(()->{
		Path tempPath = get(dest+".tmp");
		Path destPath = get(dest);
		
		Optional.ofNullable(tempPath.getParent()).map(rethrow().wrapFunction(Files::createDirectories));
		TarArchiveOutputStream outputStream = new TarArchiveOutputStream( new BufferedOutputStream(newOutputStream(tempPath)));
		outputStream.setLongFileMode(LONGFILE_GNU);
		outputStream.setBigNumberMode(BIGNUMBER_STAR);
		List<String> newEntryName = consumer.apply(outputStream);
		outputStream.flush();
		outputStream.finish(); 
		outputStream.close();
		
		move(tempPath, destPath, REPLACE_EXISTING);
		return newEntryName;
    });

	private static List<String> create(ArchiveOutputStream outputStream, Path parentPathInTar, File file) throws Exception {
		List<String> result = new ArrayList<String>();
		if (!file.isDirectory()) result.add(addEntry(outputStream, parentPathInTar, file.getName(), new BufferedInputStream(newInputStream(file.toPath()))));
		else {
			File[] files = file.listFiles();
			if (files != null) {
				if (files.length < 1) result.add(addEntry(outputStream, parentPathInTar, file.getName(), null));
				for (File f : files) result.addAll(create(outputStream, parentPathInTar.resolve(file.getName()), f));
			}
		}
		return result;
	}
	
	private static String addEntry(ArchiveOutputStream outputStream, Path parentPathInTar, String entryName, InputStream inputStream, Boolean...closeInputStream) throws Exception {
		boolean closeIS = firstOrDefault(closeInputStream, true);
		String fullEntryName = parentPathInTar.resolve(entryName).toString()+ (inputStream==null ? "/" : "");
		TarArchiveEntry entry = new TarArchiveEntry(fullEntryName);
		if (inputStream!=null) entry.setSize(inputStream.available());
		outputStream.putArchiveEntry(entry);
		if (inputStream!=null) {
			copy(inputStream, outputStream);
			if (closeIS) inputStream.close();
		}
		outputStream.closeArchiveEntry();
		return closeIS ? fullEntryName : null;
	}
	
	public static byte[] readFileFromTar(String tarFile, String filePathInTar) throws IOException, ArchiveException {
		Path tarPath = Paths.get(tarFile);
		if(!Files.isRegularFile(tarPath)) throw new IOException(tarFile+" is not a existing file");
        
        @Cleanup TarArchiveInputStream tis= (TarArchiveInputStream) new ArchiveStreamFactory().createArchiveInputStream(new BufferedInputStream(Files.newInputStream(tarPath))) ;     
        TarArchiveEntry entry;
        byte[] result = null;
        while ( ( entry = tis.getNextTarEntry()) != null ) {
            if(!entry.isDirectory() && Paths.get(entry.getName()).equals(Paths.get(filePathInTar))) {
            	result = IOUtils.toByteArray(tis);
            }
        }
        return result;
	}

}