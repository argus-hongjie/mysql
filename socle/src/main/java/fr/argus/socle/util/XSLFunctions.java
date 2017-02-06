package fr.argus.socle.util;

import static com.diffplug.common.base.Errors.rethrow;
import static com.google.common.collect.ImmutableMap.of;
import static com.google.common.collect.Lists.partition;
import static fr.argus.socle.util.Helper.coalesce;
import static fr.argus.socle.util.Helper.defaultIfNullOrException;
import static fr.argus.socle.util.Helper.runXslOnXml;
import static fr.argus.socle.util.Helper.validXmlByXsd;
import static fr.argus.socle.util.PropertyHandler.getInstance;
import static fr.argus.socle.util.Tar.addEntry;
import static fr.argus.socle.util.Tar.addFile;
import static fr.argus.socle.util.Tar.create;
import static java.net.URI.create;
import static java.nio.file.FileSystems.newFileSystem;
import static java.nio.file.Files.copy;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.move;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static java.util.Optional.empty;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.io.FileUtils.deleteQuietly;
import static org.apache.commons.io.FileUtils.readFileToString;
import static org.apache.commons.io.FileUtils.writeStringToFile;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.validator.routines.checkdigit.EAN13CheckDigit;

import fr.argus.socle.db.DBClient;
import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;

/**
 * @author dalomadi
 *
 */
@Log4j2
public class XSLFunctions {
	private static final String ALGO = "MD5";
	static DBClient dbClient =  new DBClient();
	
	public XSLFunctions(){}
	/**
	 * Hash MD5 generator
	 * @param xml
	 * @return
	 */
	public static String generateHashByMD5(String xml){
		String hash = null;
		try {
			MessageDigest md =  MessageDigest.getInstance(ALGO);
			md.reset();
			md.update(xml.getBytes(),0,xml.length());
			final byte[] resultByte = md.digest();			
			hash =  new BigInteger(1,resultByte).toString(32);
		} catch (NoSuchAlgorithmException e) {
			log.error("Erreur de generation du hash : ",e);		
		}
		return hash;
	}
	
	/**
	 * Make URL parameters for the web service link Cover page  
	 * @param fichier
	 */
	public static String generateParameters(String url,String nomFichier,String dateDispo) {
		StringBuffer parameters = new StringBuffer();
		parameters.append(url).append("?");
		String identPublication = nomFichier.substring(0, 3);		
		SimpleDateFormat sdf = new SimpleDateFormat(Constant.FORMAT_YYYY_MM_DD_T_HH_MM_SS);
		SimpleDateFormat output = new SimpleDateFormat(Constant.FORMAT_DDMMYYYY);
		Date d = null;
		try {
			d = sdf.parse(dateDispo);
		} catch (ParseException e) {			
			log.error("Erreur de convertion de format de la date de mise à disposition du ticket "+nomFichier+" ",e.getMessage());			
		}
		String datePublication = output.format(d);
		parameters.append("Source=").append(identPublication).append("&Date=").append(datePublication).append("&PageNumber=1");
		return parameters.toString();
	}
	
	/** apply xsl transformation on xml
	 * @param xml
	 * @param xslName
	 * @param paramPairs null take as empty=(), supply by pair of key and value, like (K1, V1, K2, V2...)
	 * @return
	 */
	public static String runXslTransform(String xml, String xsl, String[] paramPairs) {
		if (paramPairs==null) paramPairs = new String[]{};
		if (paramPairs.length / 2 == 1) {
			log.error("params must be supplied by pairs");		
			return null;
		}
		StringWriter xmlOutput = new StringWriter();
		Map<String, String> paramMap = partition(asList(paramPairs), 2).stream().collect(toMap(list->list.get(0), list->list.get(1)));
		runXslOnXml(StringEscapeUtils.unescapeXml(xml), paramMap, xsl.getBytes(), emptyMap(), new StreamResult(xmlOutput));
		return xmlOutput.toString();
	}
	
	public static String cleanXml(String xml){
		return 	Matcher.quoteReplacement(Helper.removeInvalidXMLCharacters(xml));
	}
	
	/**
	 * Create new tar from files, create parent dirs if not existed.
	 */
	public static List<String> createTar(String tar, String parentPathInTar, String[] files) {
		return coalesce(e->log.error("Erreur de  generation du tar : ", e), rethrow().wrap(()->create(tar, parentPathInTar, files)));
	}
	
	/**
	 * Add a entry to tar, create the tar if not existed, create parent dirs if not existed.
	 */
	public static String addEntryToTar(String tar, String parentPathInTar, String entryName, String entryData) {
		return coalesce(e->log.error("Erreur de addEntryToTar : ", e), rethrow().wrap(()->addEntry(tar, parentPathInTar, entryName, entryData.getBytes())));
	}
	
	/**
	 *  Add a file to tar, create the tar if not existed, create parent dirs if not existed.
	 */
	public static String addFileToTar(String tar, String parentPathInTar, String entryName, String regularFilePath) {
		return coalesce(e->log.error("Erreur de addFileToTar : ", e), rethrow().wrap(()->addFile(tar, parentPathInTar, entryName, regularFilePath)));
	}
	
	public static String concatPath(String[] parts) {
		return Helper.getPath(parts);
	}
	
	public static boolean fileExisted(String filePath) {
		return Files.isRegularFile(Paths.get(filePath));
	}
	
	public static boolean fileInTar(String tar, String entryPath, String regularFilePath) {
		return defaultIfNullOrException(false, rethrow().wrap(()->{
			byte[] tarEntryContent = Tar.readFileFromTar(tar, entryPath);
			byte[] fileContent = FileUtils.readFileToByteArray(new File(regularFilePath));
			return Arrays.equals(tarEntryContent, fileContent);
		}));
	}
	
	public static boolean entryInTar(String tar, String entryPath, String entryData) {
		return defaultIfNullOrException(false, rethrow().wrap(()->{
			byte[] tarEntryContent = Tar.readFileFromTar(tar, entryPath);
			byte[] fileContent = entryData.getBytes();
			return Arrays.equals(tarEntryContent, fileContent);
		}));
	}
	/** move or rename file, create parent dirs of to if not existed
	 * @param from
	 * @param to
	 * @return
	 */
	public static void moveFile(String from, String to) {
		coalesce(e->log.error("Erreur de deplacer du ficher : ", e),  rethrow().wrap(()->{
			createDirectories(get(to).getParent());
			return move(get(from), get(to), REPLACE_EXISTING).toString();
		}));
	}
	
	/**  Copy file, create parent dirs of to if not existed
	 * @param from
	 * @param to
	 * @return
	 */
	public static String copyFile(String from, String to) {
		return coalesce(e->log.error("Erreur de copie du ficher : ", e),  rethrow().wrap(()->{
			createDirectories(get(to).getParent());
			return copy(get(from), get(to), REPLACE_EXISTING).toString();
		}));
	}
	
	/** Save data to file, create parents if not existed.
	 * @param data
	 * @param filePath
	 * @throws IOException
	 */
	public static void saveToFile(String data, String filePath) {
		coalesce(e->log.error("Erreur de saveToFile : ", e), rethrow().wrap(()-> {
			writeStringToFile(new File(filePath), data); 
			return empty();
		}));
	}
	
	/**
	 * Deletes a file, never throwing an exception. If file is a directory, delete it and all sub-directories. 
	 * @param filePath
	 */
	public static void deleteFileOrDir(String filePath){
		deleteQuietly(new File(filePath));
	}
	
	public static String readFile(String filePath) {
		return coalesce(e->log.error("Erreur de readFile : ", e), rethrow().wrap(()->readFileToString(new File(filePath))));
	}

	public static String readFileFromZip(String zip, String filePathInZip) {
		return coalesce(e->log.error("Erreur de readFileFromZip : ", e), rethrow().wrap(()->{
			URI uri = create("jar:" + get(zip).toUri());
			@Cleanup FileSystem fs = newFileSystem(uri, of("create", "false"));
		    Path nf = fs.getPath(filePathInZip);
		    return readFileToString(nf.toFile());
		}));
	}
	
	public static String getProperty(String key, String defaultValue) {
		return defaultIfNullOrException(defaultValue, ()->getInstance().getProperty(key));
	}
	
	/** Get the value of field from  fr.argus.socle.util.Constant.
	 * @param key
	 * @return
	 */
	public static String getConstant(String key, String defaultValue) {
		return defaultIfNullOrException(defaultValue, ()->getInstance().getConstantIgnoreCase(key));
	}
	
	/** Get value of "key.computername" firstly, if not existed, then get value of "key", if not existed, return defaultValue, ignore case for "key.computername" or "key" 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getResource(String key, String defaultValue) {
		return dbClient.getBestResourceIgnoreCase(key, defaultValue);
	}
	
	/** Get value for the key in order, 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getValue(String key, String defaultValue) {
		return coalesce(null, 
			()->getResource(key, null),
			()->getProperty(key, null),
			()->getInstance().getEnvIgnoreCase(key),
			()->getConstant(key, null),
			()->defaultValue);
	}
	
	public static boolean isEAN13(String code) {
		return new EAN13CheckDigit().isValid(code);
	}
	
	public static boolean xmlValid(String xml, String xsd) {
		return validXmlByXsd(xml, xsd.getBytes());
	}
}
