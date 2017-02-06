/**
 * 
 */
package fr.argus.socle.util;

import static fr.argus.socle.util.Constant.ID_TICKET_INEXISTANT;
import static fr.argus.socle.util.Helper.defaultIfNullOrException;
import static fr.argus.socle.util.Helper.fastChannelCopy;
import static fr.argus.socle.util.Helper.fileCreationDate;
import static fr.argus.socle.util.Helper.slash;
import static fr.argus.socle.util.Signalements.PF_IMPORT_PIVOT_ERREUR_ARCHIVE_CORROMPUE;
import static java.lang.Integer.parseInt;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.argus.socle.db.DBClient;
import fr.argus.socle.model.File;
import fr.argus.socle.model.Reception;
import fr.argus.socle.model.ReceptionContent;
import fr.argus.socle.model.VariableContenu;
import fr.argus.socle.ws.model.TicketQuery;

/**
 * @author mamadou.dansoko
 *
 */
public class UtilShare {
	 private static final Logger logger = LogManager.getLogger(UtilShare.class);
	 //private final static char slash = java.io.File.separatorChar;
	/**Get text values from the file (pdf,xml)
	 * @param reception
	 * @param pathFile
	 * @param FPATH
	 * @param realPath
	 * @throws Exception 
	 */
	public static void getTextContentsFromFile(ReceptionContent reception,java.io.File temp,String realPath,byte[] bytes,DBClient DBClient,TicketQuery ticket,Date dateCreation,String taille){		
		String filename="";
		try {
			String FPATH =  temp.getCanonicalPath() != null ? temp.getCanonicalPath() : "";			
			Filename myFile = new Filename(FPATH, slash, '.');	
			filename = myFile.filename();
			FileContentManager pdfManager = new FileContentManager();
			pdfManager.setFilePath(FPATH);
			pdfManager.setFileRealPath(realPath);
			pdfManager.setFile(temp);
			pdfManager.setDBClient(DBClient);
			pdfManager.setTicket(ticket);
			pdfManager.setDateCreation(dateCreation);
			pdfManager.setTaille(taille);
			if(Constant.PDF.equals(myFile.extension())){
				pdfManager.getPDFContent(reception, ticket.getPassword());  
			}else if(Constant.XML.equals(myFile.extension())){
				pdfManager.getXMLContent(reception,bytes);			
			}else if(Arrays.asList(Constant.IMG).contains(myFile.extension())){
				if(!Helper.isValidImage(temp)){
					DBClient.traceLoggerInfos(Helper.loggerGenerator(ticket, "L'image "+myFile.filename()+" n'est pas ouvrable ", false));
					logger.error("L'image "+myFile.filename()+" n'est pas ouvrable ");
				}
				pdfManager.getIMAGEContent(reception);
			}
		} catch (Exception e) {
			DBClient.traceLoggerInfos(Helper.loggerGenerator(ticket, "Le fichier "+filename+" "+Arrays.toString(e.getStackTrace()), false));
			logger.error("Erreur le fichier introuvable : ", e);			
		}
		
	}
	
	/**
	 * Retrieve information on the archive
	 * @param reception
	 * @param pathZip
	 * @throws Exception 
	 * @throws IOException 
	 */
	public static void zipFilesInfos (Reception reception, String pathZip,ReceptionContent receptionContent,DBClient DBClient,TicketQuery ticket,VariableContenu varContenu){
		ZipFile zipFile = null;
		Path path  = Paths.get(pathZip);
		Path dir= path.getParent();
		String chemin_abso_archive = dir.toString();
		
		try {			
			zipFile = new ZipFile(pathZip);
		
			if(zipFile!=null){
			    Enumeration<? extends ZipEntry> entries = zipFile.entries();
			    Filename fnameInToZip = new Filename(zipFile.getName(), slash, '.');
			    chemin_abso_archive = fnameInToZip.filename();//chemin_abso_archive+slash+fnameInToZip.filename()
			    			   
			    while(entries.hasMoreElements()){
			        ZipEntry entry = entries.nextElement();	
			        if(entry.isDirectory()){			        	
			        	continue;
			        }
			        if(entry!=null){			        	
						File file = new File();  
						long time = 0l;
						if(entry.getCreationTime() !=null){
							time = entry.getCreationTime().toMillis();
						}else{
							time = entry.getTime();
						}
						Date data = new Date(time);
						file.setDateCreation(data);
						//file.setValue(entry.g);
						String taille = String.valueOf(entry.getSize());
						file.setTaille(taille);
									
						String relativePath = Helper.makeRelativePath(entry.getName());
						String endOfAbsolutePath = relativePath;
						if(relativePath.isEmpty()){
							relativePath = chemin_abso_archive;
						}					
						Path pathAbsolute = Paths.get(chemin_abso_archive+slash+entry.getName());
					    Path pathBase = Paths.get(chemin_abso_archive);
					    Path pathRelative = pathBase.relativize(pathAbsolute);				    
					    file.setNom(pathRelative.getFileName().toString());
						file.setEmplacementArchive(dir.toString()+endOfAbsolutePath);	
						if(endOfAbsolutePath!=null && !endOfAbsolutePath.isEmpty()){
							endOfAbsolutePath = slash+endOfAbsolutePath;
						}
						file.setEmplacementDansLivraison(relativePath);
						String poitnMontage =  Helper.getProperty("point.montage"); 
						file.setEmplacementTemporaire(Helper.makeFileHashPath(reception.getDateReception(), ticket.getIdLivraison(),poitnMontage));
					
						reception.getFILES().getFILE().add(file);	
												
						Filename infos = new Filename(entry.getName(), slash, '.');
						String tmpDir = System.getProperty("java.io.tmpdir");
						java.io.File temp = new java.io.File(tmpDir, infos.fullFilename());						
						//temp.deleteOnExit();
												
						InputStream is = zipFile.getInputStream(entry);
										        
						int bufferSize = (int) entry.getSize();
						
			            // Create a readable file channel         
			            
			            OutputStream os = new FileOutputStream(temp);
			           
			            ReadableByteChannel inChannel = Channels.newChannel(is);
			            WritableByteChannel outChannel = Channels.newChannel(os);
			            
			            byte[] bytes = fastChannelCopy( inChannel, outChannel,bufferSize);			            
			            if(bytes == null ){			            
			            	bytes = new byte[bufferSize];
			            }
			            
			            os.close();
			            inChannel.close();
			            outChannel.close();
			            is.close();	
											
						getTextContentsFromFile(receptionContent, temp,chemin_abso_archive+slash+entry.getName(),bytes,DBClient,ticket,data,taille);
						
						Files.deleteIfExists(temp.toPath());
			        }		
			    }			    
			}
		} catch (Exception e) {
			Integer idLivraisonNumerique = defaultIfNullOrException(ID_TICKET_INEXISTANT, () -> parseInt(ticket.getIdLivraison())); 		
			DBClient.updateStatus(idLivraisonNumerique, PF_IMPORT_PIVOT_ERREUR_ARCHIVE_CORROMPUE.getCode()); 
			String fichier = ticket.getFiles().getFILE()!= null ?ticket.getFiles().getFILE().get(0).getNomFichier() : "";
			DBClient.traceLoggerInfos(Helper.loggerGenerator(ticket,fichier+" "+ Arrays.toString(e.getStackTrace()), false));
			logger.error("Erreur de traitement du fichier "+fichier, e);	
			varContenu.setCodeSignalement(PF_IMPORT_PIVOT_ERREUR_ARCHIVE_CORROMPUE.getCode());
			varContenu.setLibelleSignalement(PF_IMPORT_PIVOT_ERREUR_ARCHIVE_CORROMPUE.name());
		}finally {
	        try {
	            if (zipFile != null) {
	            	zipFile.close();
	            	zipFile = null;
	            }
	        } catch (IOException e) {
	        	logger.error("Erreur de fermeture du fichier zip "+pathZip+" :", e);
	        	DBClient.traceLoggerInfos(Helper.loggerGenerator(ticket,"Erreur de fermeture du fichier zip "+pathZip+" "+ Arrays.toString(e.getStackTrace()), false));
	        	varContenu.setCodeSignalement(Signalements.PF_ERREUR_FERMETURE_ARCHIVE.getCode());
	        	varContenu.setLibelleSignalement(Signalements.PF_ERREUR_FERMETURE_ARCHIVE.name());
	        }
		}
	}
	
	/**
	 * Files informations Treatment (unzip file type)
	 * @param pathZip
	 * @throws Exception 
	 */
	public static void simpleFileInfos(Reception reception,String pathFile,ReceptionContent receptionContent,DBClient DBClient,TicketQuery ticket) throws Exception {
		java.io.File fileTmp = new java.io.File(pathFile);	
									
		File file = new File();		
		Path path = Paths.get(pathFile);				
				
		Date data = fileCreationDate(pathFile);	
		file.setDateCreation(data);		
		String taille = String.valueOf(fileTmp.length());
		file.setTaille(taille);
		file.setNom(fileTmp.getName());			
		String relativePath  = Helper.makeRelativePath(fileTmp.getPath());		
		file.setEmplacementArchive(relativePath);
		file.setEmplacementDansLivraison(path.getParent().toString());
		String poitnMontage = Helper.getProperty("point.montage"); 
		file.setEmplacementTemporaire(Helper.makeFileHashPath(reception.getDateReception(), ticket.getIdLivraison(),poitnMontage));
				
		reception.getFILES().getFILE().add(file);
		getTextContentsFromFile(receptionContent, fileTmp,pathFile,null,DBClient,ticket,file.getDateCreation(),taille);
	}
}
