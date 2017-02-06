/**
 * 
 */
package fr.argus.socle.util;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author mamadou.dansoko
 *
 */
public class FTPDownloader {
	private  final Logger logger = LogManager.getLogger(FTPDownloader.class);	
    private String toPathSource;
    private String toPathTarget;
    private FTPClient ftpClient = null;
    private String host;
    private String user;
    private String pass;
    
    /**
     * 
     * @param host
     * @param user
     * @param pass
     * @param toPathSource
     * @param toPathTarget
     */
    public FTPDownloader(String host, String user, String pass,String toPathSource, String toPathTarget) {      
	   this.host = host;
       this.user = user;
       this.pass = pass;
       this.toPathSource = toPathSource;
       this.toPathTarget = toPathTarget;	  
    }	  

    /**
     * 
     */
	public FTPDownloader() {
		
	}


	/**
	 * Téléchargement de fichier via ftp
	 * @param fileName
	 * @param remotePath
	 * @param downloadPath
	 */
	public void ftpDownloadFile(){	    	      
		try {	        	
        	ftpClient = new FTPClient();
 	        ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
 	        int reply;
 	        
        	ftpClient.connect(host);
        	reply = ftpClient.getReplyCode();
        	if (!FTPReply.isPositiveCompletion(reply)) {
        		ftpClient.disconnect();   
        		throw new IOException("Exception in connecting to FTP Server");
            }            
            boolean isLoginOk =ftpClient.login(user, pass);  
            if(!isLoginOk){
            	logger.info("L'authentification de l'utilisateur a échouée.");	            	
            }
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
 	      	            
           Path path = Paths.get(toPathSource);
           Path realPath = path.getParent();
    	   if(Files.notExists(realPath, LinkOption.NOFOLLOW_LINKS)){
    		   logger.info(toPathSource+": Aucun fichier ou répertoire de ce type "); 	    		  
    	   }else{ 	
    		   
	            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(toPathSource));
	            boolean success = ftpClient.retrieveFile(toPathTarget, outputStream);
	            outputStream.close();
 	    	   
	            if (success) {	                
	            	logger.info("Le fichier #"+toPathTarget+" a été téléchargé avec succès.");
	            }else{
	            	logger.info("Le téléchargement du fichier #"+toPathTarget+" a échoué.");
	            }
    	   }
           

        } catch (IOException ex) {
        	
        	logger.error("Erreur config client FTP: " , ex.getMessage());	     
        }  finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {	            	
            	logger.error("Error in closing connection to FTP Server : " , ex.getMessage());
            }
        }
	}
	

	/**
	 * @return the ftpClient
	 */
	public FTPClient getFtpClient() {
		return ftpClient;
	}

	/**
	 * @param ftpClient the ftpClient to set
	 */
	public void setFtpClient(FTPClient ftpClient) {
		this.ftpClient = ftpClient;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the pass
	 */
	public String getPass() {
		return pass;
	}

	/**
	 * @param pass the pass to set
	 */
	public void setPass(String pass) {
		this.pass = pass;
	}

	/**
	 * @return the logger
	 */
	public Logger getLogger() {
		return logger;
	}

	/**
	 * @return the toPathSource
	 */
	public String getToPathSource() {
		return toPathSource;
	}

	/**
	 * @return the toPathTarget
	 */
	public String getToPathTarget() {
		return toPathTarget;
	}	
	
}
