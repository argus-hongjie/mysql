/**
 * 
 */
package fr.argus.socle.util;

import java.util.logging.Logger;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
/**
 * @author mamadou.dansoko
 *
 */
public class SFTPManager {
  private static final Logger LOGGER = Logger.getLogger(SFTPManager.class.getName());

  private SFTPManager() {
  }

  public static void upload(String login,String password,String hostname,String from,String to,String remoteFile){
	  
	try {
 		java.util.Properties config = new java.util.Properties();
	    config.put("StrictHostKeyChecking", "no");

	    JSch ssh = new JSch();
	    Session session;
		FileInputStream fis = new FileInputStream(new File(from));
		session = ssh.getSession(login, hostname, 22);
		session.setConfig(config);
	    session.setPassword(password);
	    session.connect();
	    Channel channel = session.openChannel("sftp");
        channel.connect();
 
        ChannelSftp sftp = (ChannelSftp) channel;
        sftp.cd(to);       
        sftp.put(fis, remoteFile);
       
        channel.disconnect();
        session.disconnect();
 
	} catch (JSchException e) {
		LOGGER.warning(e.getMessage());	
	} catch (FileNotFoundException e) {
		LOGGER.warning(e.getMessage());	
	} catch (SftpException e) {
		LOGGER.warning(e.getMessage());	
	}
      
  }
 
}
