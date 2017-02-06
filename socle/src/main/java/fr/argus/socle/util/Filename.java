/**
 * 
 */
package fr.argus.socle.util;

/**
 * @author mamadou.dansoko
 *
 */
public class Filename {
	 /**
	 * @return the fullPath
	 */
	public String getFullPath() {
		return fullPath;
	}

	/**
	 * @param fullPath the fullPath to set
	 */
	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	private String fullPath;
	 private char pathSeparator, extensionSeparator;

	 public Filename(String str, char sep, char ext) {
	    fullPath = str;
	    pathSeparator = sep;
	    extensionSeparator = ext;
	 }

	 public String extension() {
	    int dot = fullPath.lastIndexOf(extensionSeparator);
	    return fullPath.substring(dot + 1);
	 }

	 public String filename() { // gets filename without extension
	    int dot = fullPath.lastIndexOf(extensionSeparator);
	    int sep = fullPath.lastIndexOf(pathSeparator);
	    return fullPath.substring(sep + 1, dot);
	 }
	 
	 public String fullFilename() { // gets filename without extension	   
	    return filename()+"."+extension();
	 }

	 public String path() {
	    int sep = fullPath.lastIndexOf(pathSeparator);
	    if(sep != -1){
	    	return fullPath.substring(0, sep);
	    }else{
	    	return "";
	    }
	 }
}
