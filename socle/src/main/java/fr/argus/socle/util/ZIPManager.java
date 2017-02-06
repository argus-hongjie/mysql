/**
 * 
 */
package fr.argus.socle.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author mamadou.dansoko
 *
 */
public class ZIPManager {
	private ZipOutputStream zipOutputStream;
	private String path;
    private String name;
    private final String FILE_EXTENSION = ".zip";
    
    public ZIPManager(String path, String name) throws FileNotFoundException {
        this.path = path;
        this.name = name;
        constructNewStream();
    }
    /**
     * Create ZIP file 
     * @param inputFileName
     * @param fileName
     * @throws IOException
     */
    public void addEntry(ZipEntry entry,InputStream is ) throws IOException {
    	
    	try{
	    	zipOutputStream.putNextEntry(entry);
	    	byte[] readBuffer = new byte[2048];
	        int amountRead; 
	
	        while ((amountRead = is.read(readBuffer)) > 0) {
	        	 zipOutputStream.write(readBuffer, 0, amountRead);      
	        }
	        zipOutputStream.closeEntry();
	        is.close();
	     }
         catch(IOException e) {
            
         }
    }
    /**
     * Close stream method
     * @throws IOException
     */
    public void closeStream() throws IOException {    	
        zipOutputStream.close();
    }
    /**
     * New Stream output builder
     * @throws FileNotFoundException
     */
    private void constructNewStream() throws FileNotFoundException {    	
        zipOutputStream = new ZipOutputStream(new FileOutputStream(new File(path, constructCurrentPartName())));       
    }
    /**
     * ZIP file name builder
     * @return
     */
    private String constructCurrentPartName() {       
        StringBuilder partNameBuilder = new StringBuilder(name);          
        partNameBuilder.append(FILE_EXTENSION);
        return partNameBuilder.toString();
    }
         
}
