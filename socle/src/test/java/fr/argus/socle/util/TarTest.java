package fr.argus.socle.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class TarTest {

	@Test
	public void testCreanteOrAppend_create() throws Exception {
		Path dir = Files.createTempDirectory("dir");
		Path tmp1 = Files.createFile(dir.resolve("a.txt"));
		FileUtils.writeStringToFile(tmp1.toFile(), "test string");
		Path destPath = dir.getParent().resolve("dest.tar");
		
		Tar.addFile(destPath.toString(), "def", "aaa.txt", tmp1.toString());
		
	    TarArchiveInputStream tis = null;
	    try {
	        tis = new TarArchiveInputStream(new FileInputStream(destPath.toString()));
	        TarArchiveEntry entry = tis.getNextTarEntry();
	        tis.close();
	        tis = null;
	        assertNotNull(entry);
	        assertEquals("def/aaa.txt", entry.getName());
	        assertTrue(entry.isFile());
	    } finally {
	        if (tis != null) tis.close();
	        FileUtils.deleteDirectory(dir.toFile());
	        Files.deleteIfExists(Paths.get(destPath.toString()));
	    }
	}
	
	@Test
	public void testCreanteOrAppend_append() throws Exception {
		Path dir = Files.createTempDirectory("dir");
		Path tmp1 = Files.createFile(dir.resolve("a.txt"));
		Path tmp2 = Files.createFile(dir.resolve("b.txt"));
		FileUtils.writeStringToFile(tmp1.toFile(), "test string");
		FileUtils.writeStringToFile(tmp2.toFile(), "test string 2");
		Path destPath = dir.getParent().resolve("dest.tar");
		
		Tar.addFile(destPath.toString(), "def", "aaa.txt", tmp1.toString());
		Tar.addFile(destPath.toString(), "def", "bbb.txt", tmp2.toString());
		
	    TarArchiveInputStream tis = null;
	    try {
	        tis = new TarArchiveInputStream(new FileInputStream(destPath.toString()));
	        TarArchiveEntry entry = tis.getNextTarEntry();
	        TarArchiveEntry entry2 = tis.getNextTarEntry();
	        tis.close();
	        tis = null;
	        assertNotNull(entry);
	        assertNotNull(entry2);
	        assertTrue(entry.isFile());
	        assertTrue(entry2.isFile());
	        assertEquals("def/aaa.txt", entry.getName());
	        assertEquals("def/bbb.txt", entry2.getName());
	    } finally {
	        if (tis != null) tis.close();
	        FileUtils.deleteDirectory(dir.toFile());
	        Files.deleteIfExists(Paths.get(destPath.toString()));
	    }
	}
	
	@Test
	public void testCreate_files_without_basePath() throws Exception {
		Path dir = Files.createTempDirectory("dir");
		Path tmp1 = Files.createFile(dir.resolve("a.txt"));
		Path tmp2 = Files.createFile(dir.resolve("b.txt"));
		Path destPath = Files.createTempFile("dest.",".tar");
		
		Tar.create(destPath.toString(), "", tmp1.toString(), tmp2.toString());
		
	    TarArchiveInputStream tis = null;
	    try {
	        tis = new TarArchiveInputStream(new FileInputStream(destPath.toString()));
	        TarArchiveEntry entry = tis.getNextTarEntry();
	        tis.close();
	        tis = null;
	        assertNotNull(entry);
	        assertTrue( entry.getName().endsWith(".txt"));
	        assertTrue(entry.isFile());
	    } finally {
	        if (tis != null) tis.close();
	        FileUtils.deleteDirectory(dir.toFile());
	        Files.deleteIfExists(destPath);
	    }
	}
	
	@Test
	public void testCreate_files_with_basePath() throws Exception {
		Path dir = Files.createTempDirectory("dir");
		Path tmp1 = Files.createFile(dir.resolve("a.txt"));
		Path tmp2 = Files.createFile(dir.resolve("b.txt"));
		Path destPath = Files.createTempFile("dest.",".tar");
		
		Tar.create(destPath.toString(), "def", tmp1.toString(), tmp2.toString());
		
	    TarArchiveInputStream tis = null;
	    try {
	        tis = new TarArchiveInputStream(new FileInputStream(destPath.toString()));
	        TarArchiveEntry entry = tis.getNextTarEntry();
	        tis.close();
	        tis = null;
	        assertNotNull(entry);
	        assertEquals("def/a.txt", entry.getName());
	        assertTrue(entry.isFile());
	    } finally {
	        if (tis != null) tis.close();
	        FileUtils.deleteDirectory(dir.toFile());
	        Files.deleteIfExists(destPath);
	    }
	}
	
	@Test
	public void testCreate_from_dir() throws Exception {
		Path dir = Files.createTempDirectory("dir");
		Files.createFile(dir.resolve("a.txt"));
		Files.createFile(dir.resolve("b.txt"));
		Path destPath = Files.createTempFile("dest.",".tar");
		
		Tar.create(destPath.toString(), "", dir.toString());
		
	    TarArchiveInputStream tis = null;
	    try {
	        tis = new TarArchiveInputStream(new FileInputStream(destPath.toFile()));
	        TarArchiveEntry entry = tis.getNextTarEntry();
	        tis.close();
	        tis = null;
	        assertNotNull(entry);
	        assertTrue(entry.getName().startsWith("dir") && entry.getName().endsWith(".txt"));
	        assertTrue(entry.isFile());
	    } finally {
	        if (tis != null) tis.close();
	        FileUtils.deleteDirectory(dir.toFile());
	        Files.deleteIfExists(destPath);
	    }
	}

	@Test
	public void testCreate_from_empty_dir() throws Exception {
		Path dir = Files.createTempDirectory("dir");
		Path destPath = Files.createTempFile("dest.",".tar");
		
		Tar.create(destPath.toString(), "", dir.toString());
		
	    TarArchiveInputStream tis = null;
	    try {
	        tis = new TarArchiveInputStream(new FileInputStream(destPath.toFile()));
	        TarArchiveEntry entry = tis.getNextTarEntry();
	        tis.close();
	        tis = null;
	        assertNotNull(entry);
	        assertTrue(entry.getName().startsWith("dir"));
	        assertTrue(entry.isDirectory());
	    } finally {
	        if (tis != null) tis.close();
	        Files.deleteIfExists(dir);
	        Files.deleteIfExists(destPath);
	    }
	}
	
	@Test
	public void testCreate_from_empty_dir_with_basePath() throws Exception {
		Path dir = Files.createTempDirectory("dir");
		Path destPath = Files.createTempFile("dest.",".tar");
		
		Tar.create(destPath.toString(), "def", dir.toString());
		
	    TarArchiveInputStream tis = null;
	    try {
	        tis = new TarArchiveInputStream(new FileInputStream(destPath.toFile()));
	        TarArchiveEntry entry = tis.getNextTarEntry();
	        tis.close();
	        tis = null;
	        assertNotNull(entry);
	        assertTrue(entry.getName().startsWith("def/dir"));
	        assertTrue(entry.isDirectory());
	    } finally {
	        if (tis != null) tis.close();
	        Files.deleteIfExists(dir);
	        Files.deleteIfExists(destPath);
	    }
	}
}
