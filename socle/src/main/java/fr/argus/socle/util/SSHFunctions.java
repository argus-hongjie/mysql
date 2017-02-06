package fr.argus.socle.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.StreamGobbler;

/**
 * 
 * @author nassima.djerrah
 *
 */

public class SSHFunctions {
	
	private static final Logger logger = LogManager.getLogger(SSHFunctions.class);
	
	public SSHFunctions(){
		
	}
	
	/**
	 * 
	 * @param serverIp : Adresse IP de la machine distante
	 * @param command : Commande a exécuter sur la machine distante
	 * @param usernameString : Login de la session sur la machine distante 
	 * @param password : Mot-de-passe de la session sur la machine distante
	 * @throws IOException
	 */
	public static void SSHClient(String serverIp,String command, String usernameString,String password) throws IOException{
        System.out.println("Debut de la fonction SSH "+command+" ");
        logger.info("Debut de la fonction SSH "+command+" "+command);
        try
        {
            Connection conn = new Connection(serverIp);
            conn.connect();
            boolean isAuthenticated = conn.authenticateWithPassword(usernameString, password);
            if (isAuthenticated == false){
                logger.error("Authentification échouée sur le server "+ serverIp);
            }else{
	            ch.ethz.ssh2.Session sess = conn.openSession();
	            sess.execCommand(command);  
	            InputStream stdout = new StreamGobbler(sess.getStdout());
	            BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
	            while (true)
	            {
	                String line = br.readLine();
	                if (line == null)
	                    break;
	                System.out.println(line);
	                logger.info("La sortie de la commande "+ command + " est " +line);
	            }
	            logger.info("Le code de sortie esr : "+sess.getExitStatus());
	            br.close();
	            sess.close();
	            conn.close();
            }
        }
        catch (IOException e)
        {
        	logger.error("Erreur de connexion au serveur "+ serverIp +" : ",e.getMessage());
        }
    }

}
