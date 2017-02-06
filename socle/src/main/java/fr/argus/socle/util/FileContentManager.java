/**
 * 
 */
package fr.argus.socle.util;

import static fr.argus.socle.util.Helper.slash;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardDecryptionMaterial;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.apache.pdfbox.util.PDFTextStripper;
//import org.apache.pdfbox.text.PDFTextStripper;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import fr.argus.socle.model.Contenu;
import fr.argus.socle.model.Fichier;
import fr.argus.socle.model.Page;
import fr.argus.socle.model.ReceptionContent;
import fr.argus.socle.util.AdapterCDATA;
import fr.argus.socle.util.Constant;
import fr.argus.socle.util.Filename;
import fr.argus.socle.util.Helper;
import fr.argus.socle.ws.model.TicketQuery;
import fr.argus.socle.db.DBClient;

/**
 * @author mamadou.dansoko
 *
 */
public class FileContentManager {
   private static final Logger logger = LogManager.getLogger(FileContentManager.class);
   private PDFTextStripper pdfStripper;
   private PDDocument pdDoc;
   private String Text ;
   private String filePath;
   private String fileRealPath;
   private java.io.File file;
   private DBClient DBClient;
   private TicketQuery ticket;
   private Date dateCreation;
   private String taille;
  
   public FileContentManager() {
        
   }

/**
    * This method get PDF file content and put into Reception 
    * @param reception
    * @throws IOException
    */
   public void getPDFContent(ReceptionContent reception, String password)   
   {	 
       this.pdfStripper = null;
       this.pdDoc = null;       
       //file = new File(filePath);       
       Contenu contenu = new Contenu();
        
       try{  
    	   if(isValidPdf()){    		                 
		       pdfStripper = new PDFTextStripper();			    
		       pdDoc = PDDocument.load(file);
		       AccessPermission ap = pdDoc.getCurrentAccessPermission();
               if( ! ap.canExtractContent() )
               {            	
		           logger.error("Vous ne disposez de permission nécéssaire pour extraire le text du fichier.");
                   throw new IOException( "You do not have permission to extract text" );
               }
		       if (pdDoc.isEncrypted()) {
		           try {
		        	   //pdDoc.decrypt(password);
		        	   pdDoc.setAllSecurityToBeRemoved(true);		        	   		        	 
		        	   StandardDecryptionMaterial dm= new StandardDecryptionMaterial(password); 
		        	   pdDoc.openProtection(dm);
		        	   AccessPermission acp = new AccessPermission();
		               acp.setCanPrint(true);
		               acp.setCanExtractContent(true);
		               acp.setCanExtractForAccessibility(true);
		               StandardProtectionPolicy spp = new StandardProtectionPolicy("", "", ap);
		               pdDoc.protect(spp);
		               pdDoc.save(filePath);
		               pdDoc.close();

		               pdDoc = PDDocument.load(file);
		           }
		           catch (Exception e) {
		        	  DBClient.traceLoggerInfos(Helper.loggerGenerator(ticket,"Le fichier PDF : "+file.getName()+" "+ Arrays.toString(e.getStackTrace()), false));
		              logger.error("Le fichier PDF "+file.getName()+" est protégé par un mot de passe. Impossible de dévérouiller avec le mot de passe fourni ", e);
		           }
		       }
		      
		       int nbOfPages= pdDoc.getNumberOfPages();	 		       
		       for(int i = 1;i<= nbOfPages;i++){
		    	   pdfStripper.setStartPage(i);
			       pdfStripper.setEndPage(i);				      
			       Text =pdfStripper.getText(pdDoc);			       			     
			       Text = Helper.xmlEscape(Text);
			       Text = escapesCharaters(Text);	
			       Text = StringEscapeUtils.escapeHtml4(Text);			 
			       			      			      			       			      
			       Page page = new Page();
			       page.setNumero(String.valueOf(i));
			       page.setValue(Text);
			       contenu.getPAGES().add(page);		       
		       }
		       
    	   }else{
			 Page page = new Page();		          
		     contenu.getPAGES().add(page);	
    	   }
	       makeContenuFichier(reception, contenu);	
       } catch (IOException e) {    	 
    	   DBClient.traceLoggerInfos(Helper.loggerGenerator(ticket, "Le fichier PDF : "+file.getName()+" "+Arrays.toString(e.getStackTrace()), false));
    	   logger.error("Erreur Fichier PDF "+file.getName()+" non trouvé |ou la récupération du contenu du PDF -getPDFContent(): ", e);	    	  
		}
       finally {
			try {
				if(pdDoc != null){
					pdDoc.close();
				}
			} catch (IOException e) {
				DBClient.traceLoggerInfos(Helper.loggerGenerator(ticket,"Le fichier PDF : "+file.getName()+" "+ Arrays.toString(e.getStackTrace()), false));
				logger.error("Erreur de fermeture du fichier PDF-getPDFContent(): ", e);
			}
	   }            	     
    }
   /**
    * Transform XML file content to string
    * @param FPATH
    * @return
 * @throws Exception 
    */
	public void getXMLContent(ReceptionContent reception,byte[] bytes) {
		file = new File(filePath);	
		System.out.println("");
		try{		
        	Text = new String(bytes);	    		        
	        Text = Helper.removeInvalidXMLCharacters(Text);	        
	        Text = escapesCharaters(Text);	
	             	   	        
	        boolean isValidate = Helper.validate(Text);
	        if(!isValidate){
	        	AdapterCDATA adapCDATA = new AdapterCDATA();
	        	Text = removePrologAndNameSpace(Text);
	        	Text = adapCDATA.marshal(Text);
	        }else{
	        	Text = removePrologAndNameSpace(Text);
	        }
	        
	        Contenu contenu = new Contenu();	       
		    contenu.setValue(Text);
		    makeContenuFichier(reception, contenu);	   
		   
		} catch (Exception e) {
			DBClient.traceLoggerInfos(Helper.loggerGenerator(ticket, "Le fichier XML : "+file.getName()+" "+Arrays.toString(e.getStackTrace()), false));
			logger.error("Error survenue dans la methode getXMLContent(): ", e);		
		} 
	}

	/**
	 * Remove xml prolog and name space
	 * @param Text
	 * @return
	 */
	private String removePrologAndNameSpace(String Text) {
		Text = Text.replaceAll("(<\\?[^<]*\\?>)?", ""); /* remove preamble */
		Text = Text.replaceAll("xmlns.*?(\"|\').*?(\"|\')", ""); /* remove xmlns declaration */
		return Text;
	}

	/**
	 * this method escape all characters interpreted 
	 */
	private String escapesCharaters(String Text) {
		Text = Text.replaceAll("\n","\\\n");
		Text = Text.replaceAll("\t","\\t");
		Text = Text.replaceAll("\b","\\b");
		Text = Text.replaceAll("\"","\\\"");		        
		Text = Text.replaceAll("&amp;","\\x3B");	          
		Text = Text.replaceAll("\f","\\f");
		Text = Text.replaceAll("\r","\\r");		     
		Text = Text.replaceAll("'","\\x27");
		Text = Text.replaceAll("&","\\x26");
		Text = Text.replaceAll(";","\\x3B");
		Text = Text.replaceAll(",","\\x2C");
		Text = Text.replaceAll("´","\\xB4");
		Text = Text.replaceAll("’", "\\x27");
		Text = Text.replaceAll("[^\\x20-\\x7e]", "");
		return Text;
	}
		
	/**
	 * @param bytes
	 * @throws UnsupportedEncodingException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerConfigurationException
	 * @throws TransformerException
	 */
	public String makeStringFromXmlContent(byte[] bytes) throws SQLException
		{
		String str ="";
		try{
			//str = new String(bytes, Constant.ENCODER_TYPE_LATIN1);
			InputStream outStream1 = new ByteArrayInputStream(bytes);				
			DocumentBuilderFactory dFactory= DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder= dFactory.newDocumentBuilder();
			Document doc= dBuilder.parse(outStream1);			 
			StringWriter sw = new StringWriter();
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();	       
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "no");
			transformer.setOutputProperty(OutputKeys.ENCODING, Constant.ENCODER_TYPE_LATIN1);
			transformer.transform(new DOMSource(doc), new StreamResult(sw));
			str=sw.toString();	      
		    outStream1.close();
	    
		} catch (ParserConfigurationException pce) {	
			DBClient.traceLoggerInfos(Helper.loggerGenerator(ticket,"Le fichier PDF : "+file.getName()+" "+ Arrays.toString(pce.getStackTrace()), false));
			logger.error("Error de parsing de configuration: ", pce);
		} catch (SAXException se) {
			DBClient.traceLoggerInfos(Helper.loggerGenerator(ticket, "Le fichier PDF : "+file.getName()+" "+Arrays.toString(se.getStackTrace()), false));
			logger.error("Error de parsing xml: ", se);
		} catch (TransformerConfigurationException tce) {
			DBClient.traceLoggerInfos(Helper.loggerGenerator(ticket, "Le fichier PDF : "+file.getName()+" "+Arrays.toString(tce.getStackTrace()), false));
			logger.error("Error de transformation de configuration: ", tce);
		} catch (TransformerException e) {
			DBClient.traceLoggerInfos(Helper.loggerGenerator(ticket, "Le fichier PDF : "+file.getName()+" "+Arrays.toString(e.getStackTrace()), false));
			logger.error("Error de transfromation de xml en string: ", e);
		} catch (Exception ex) {
			DBClient.traceLoggerInfos(Helper.loggerGenerator(ticket, "Le fichier PDF : "+file.getName()+" "+Arrays.toString(ex.getStackTrace()), false));
			logger.error("Error survenue dans la methode getXMLContent(): ", ex);		
		}	
		return str;
	}

	
	/**
	 * @param reception
	 * @param contenu
	 */
	private void makeContenuFichier(ReceptionContent reception, Contenu contenu) {
		Fichier fichier = new Fichier();
		Filename fname = new Filename(fileRealPath,slash, '.');
		fichier.setNom(fname.fullFilename());
		fichier.setCONTENU(contenu);
		fichier.setType(fname.extension());
		fichier.setDateCreation(dateCreation);
		fichier.setTaille(taille);
		
		reception.getFICHIERS().getFICHIER().add(fichier);
	}
	
   /**
    * Get file image informations
    * @param reception
    */
	public void getIMAGEContent(ReceptionContent reception){
		Contenu contenu = new Contenu();	       	    
	    makeContenuFichier(reception, contenu);
	}
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
	/**
	 * @return the fileRealPath
	 */
	public String getFileRealPath() {
		return fileRealPath;
	}
	/**
	 * @param fileRealPath the fileRealPath to set
	 */
	public void setFileRealPath(String fileRealPath) {
		this.fileRealPath = fileRealPath;
	}
    
	/**
	 * Check if PDF file is valid
	 * All validate PDF file must be begin by this word %PDF-
	 * @return
	 */
	private boolean isValidPdf(){
		boolean isValid = false;
	    try(Scanner input = new Scanner(new FileReader(file))){		    	
		    while (input.hasNextLine()) {
		        final String checkline = input.nextLine();
		        if(checkline.contains("%PDF-")) { 		
		        	isValid = true;
		            return isValid;
		        }  
		    }
		    input.close();
	    }catch (FileNotFoundException e) {
	    	DBClient.traceLoggerInfos(Helper.loggerGenerator(ticket, "Le fichier PDF : "+file.getName()+" "+Arrays.toString(e.getStackTrace()), false));
			logger.error("Erreur fichier "+file.getName()+" introuvable-isValidPdf(): ", e);
		}
		return isValid;
	}
	
	  /**
	 * @return the file
	 */
	public java.io.File getFile() {
		return file;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(java.io.File file) {
		this.file = file;
	}

	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @return the dBClient
	 */
	public DBClient getDBClient() {
		return DBClient;
	}

	/**
	 * @param dBClient the dBClient to set
	 */
	public void setDBClient(DBClient dBClient) {
		DBClient = dBClient;
	}

	/**
	 * @return the ticket
	 */
	public TicketQuery getTicket() {
		return ticket;
	}

	/**
	 * @param ticket the ticket to set
	 */
	public void setTicket(TicketQuery ticket) {
		this.ticket = ticket;
	}
	/**
	 * File date creation 
	 * @return
	 */
	public Date getDateCreation() {
			return dateCreation;
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}
	/**
	 * File size
	 * @return
	 */
	public String getTaille() {
		return taille;
	}

	public void setTaille(String taille) {
		this.taille = taille;
	}

}
