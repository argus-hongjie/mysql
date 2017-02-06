package fr.argus.socle.util;

/**
 * © @author mamadou.dansoko
 */
public class Constant {

	public static final String PROPERTY_FILE_PATH = "config.xml";
	public static final String LOG4J_FILE_PATH = "log4j.xml";
	public static final String FOP_XCONF_FILE_PATH = "fop.xconf";
	
	public static final String BASE_URI = "http://{0}/socle";
	public static final String WS_PACKAGE = "fr.argus.socle.ws";
	
	public static final String COMPUTERNAME = "COMPUTERNAME";
	public static final String HOSTNAME = "HOSTNAME";
	public static final String UNKNOWN_COMPUTER = "Unknown Computer";
	public static final String USERNAME = "user.name";
	public static final String SMTPHOST = "mail.smtp.host";
	
	public static final String FORMAT_YYYYMMDD_HH_MM_SS = "yyyyMMdd HHmmss";
	public static final String FORMAT_YYYYMMDD = "yyyyMMdd";
	public static final String FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static final String FORMAT_YYYY_MM_DD_T_HH_MM_SS ="yyyy-MM-dd'T'HH:mm:ss";
	public static final String FORMAT_DDMMYYYY = "ddMMyyyy";
	public static final String ENCODER_TYPE_LATIN1 ="ISO-8859-1";
	public static final String ENCODER_TYPE_UTF_8 ="UTF-8";
	public static final String ENCODER_TYPE_WINDOWS = "windows-1253";
	// XML 1.0
    // #x9 | #xA | #xD | [#x20-#xD7FF] | [#xE000-#xFFFD] | [#x10000-#x10FFFF]	
	public static final String INVALID_XML_CHARACTERS = "[^\u0009\r\n\u0020-\uD7FF\uE000-\uFFFD\ud800\udc00-\udbff\udfff]";
	public static final String PDF = "pdf";
	public static final String XML = "xml";
	public static final String ZIP = "zip";
	public static final String[] FICHIER_INFOS_ZIP =  new String [] {"info.xml","index.xml"};
	public static final String[] IMG = new String [] {"jpg","jpeg","tif"};
	public static final Integer ID_TICKET_INEXISTANT = -1;
	public static final String STATUS_CONTENU = "Original";
	public static final String FTP = "FTP";
	public static final String WS = "WS";
	public static final String PREMIERE_PAGE ="PREMIERE_PAGE";
	public static final String AUCUNE = "AUCUNE";
	public static final String PAGE = "page";
	public static final String COUVERTURE = "couverture";
	public static final String COUVERTURE_INDISPONIBLE ="couverture indisponible";
	public static final String REPERTTOIRE_TMP_GENRATION_FICHIERS_IMG = "pages";
	public static final String PREFIX_PAGE_IMAGE_NAME ="page_";
	
	public static final String PREFIX_TICKET_STATUS_FILE_ID ="status_id_";
	public static final String remoteDir ="/tmp/";
	public static final String root ="status_manager";
	public static final String PROCESS_TRADEXPRESS = "PROCESS_TRADEXPRESS_STATUS";
}
