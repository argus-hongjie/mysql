package fr.argus.socle.util;

import static com.google.common.base.Strings.emptyToNull;
import static java.nio.file.Files.isDirectory;
import static java.nio.file.Paths.get;
import static java.util.Arrays.copyOfRange;
import static java.util.Arrays.stream;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
import static org.apache.commons.lang3.StringUtils.stripStart;
import static org.assertj.core.util.Arrays.isNullOrEmpty;

import static fr.argus.socle.util.Constant.PREFIX_TICKET_STATUS_FILE_ID;
import static fr.argus.socle.util.Constant.PROCESS_TRADEXPRESS;
import static fr.argus.socle.util.Constant.remoteDir;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.imageio.ImageIO;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.io.IOUtils;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.omg.CORBA.SystemException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import fr.argus.socle.db.DBClient;
import fr.argus.socle.db.mapper.service.ResultSetRowMapper;
import fr.argus.socle.model.Livraison;
import fr.argus.socle.model.ProcessTradExpress;

import fr.argus.socle.model.StatusManager;
import fr.argus.socle.queue.TicketQueueManager;
import fr.argus.socle.ws.model.Alive;
import fr.argus.socle.ws.model.File;
import fr.argus.socle.ws.model.LogsQuery;
import fr.argus.socle.ws.model.ReturnAutoTest;
import fr.argus.socle.ws.model.Stop;
import fr.argus.socle.ws.model.TicketQuery;


/**
 * © @author mamadou.dansoko July 2016
 */
public class Helper {

	private static final Logger logger = LogManager.getLogger(Helper.class);	
	public static final DateFormat	SDF	= new SimpleDateFormat("dd/MM/yyyy");
	private static final DateFormat	SDF_FORMAT_ANNEE = new SimpleDateFormat("yyyy");
	private static final String PADDING = "00000000000000000000";	
	public static final char slash = '/';
	private static byte[] MAGIC = { 'P', 'K', 0x3, 0x4 };

	public static String dateToString(Date date) {
		return SDF.format(date);
	}

	public static Date stringToDate(String stringDate) {
		Date date = new Date();
		try {
			date = SDF.parse(stringDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static String yearOfDate(Date date){
		return SDF_FORMAT_ANNEE.format(date);
	}

	/**
	 * Récupérer la valeur d'une propriété depuis le fichier properties.
	 * 
	 * @param key
	 *            nom de la propriété.
	 * @return la valeur de la propriété.
	 */
	public static String getProperty(String key) {

		return PropertyHandler.getInstance().getProperty(key);

	}

	/**
	 * Cette méthode permet de mapper la ligne courante du resultset en Objet.
	 * 
	 * @param rowMapper
	 *            type du mapper.
	 * @param rs
	 *            le resultSet.
	 * @return L'objet qui représente la ligne courante du resultset.
	 */
	public static Object getResultSetRowObject(ResultSetRowMapper<?> rowMapper, ResultSet rs) {
		Object result = null;
		try {
			result = rowMapper.mapRow(rs);
		} catch (SQLException e) {
			logger.error("Erreur lors du mapping du resultset :" + e.getMessage());
		}
		return result;

	}

	/**
	 * Déterminer le type de reqête depuis la valeur du paramètre query.
	 * 
	 * @param query
	 *            paramètre de la requête http.
	 * @return type de la requête http.
	 */
	public static QueryType getQueryType(Object query) {
		QueryType result = null;
		if (query == null)
			return null;
		if (query instanceof Alive)
			result = QueryType.ALIVE;
		else if (query instanceof LogsQuery)
			result = QueryType.LOGS;
		else if (query instanceof Stop)
			result = QueryType.STOP;
		else if (query instanceof ReturnAutoTest)
			result = QueryType.RETURN_AUTO_TEST;

		return result;
	}

	/**
	 * Retourne le nom de la machine.
	 * 
	 * @return le nom de la machine
	 */
	public static String getComputerName() {
		Map<String, String> env = System.getenv();
		if (env.containsKey(Constant.COMPUTERNAME))
			return env.get(Constant.COMPUTERNAME);
		else if (env.containsKey(Constant.HOSTNAME))
			return env.get(Constant.HOSTNAME);
		else
			return Constant.UNKNOWN_COMPUTER;
	}

	/**
	 * récupérer la liste des fichiers depuis un répertoire.
	 * 
	 * @param dir
	 *            répertoire à parcourir.
	 * @return liste de fichiers.
	 * @throws IOException
	 */
	public static List<Path> listFilesFromDir(String banetteIn) throws IOException {
		Path dir = Paths.get(banetteIn);
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
			return stream(stream.spliterator(), false).filter(path -> !isDirectory(path)).collect(toList());
		} catch (DirectoryIteratorException ex) {
			throw ex;
		}
	}

	/**
	 * créer un ticket depuis un fichier de la banette d'entrée.
	 * 
	 * @param file
	 *            fichier à traiter.
	 * @return ticket créé.
	 */
	public static TicketQuery makeTicketFromFile(Path filePath) {
		TicketQuery ticket = new TicketQuery();
		File file = new File();
		fr.argus.socle.ws.model.Files files = new fr.argus.socle.ws.model.Files();
		file.setValue(filePath.toFile().getAbsolutePath());
		file.setNomFichier(filePath.toFile().getName());
		files.getFILE().add(file);
		ticket.setFiles(files);
		ticket.setPriority(1);
		ticket.setType(Helper.getProperty("banette.name"));
		return ticket;
	}

	/**
	 * ajouter un ticket à la pile à traiter.
	 * 
	 * @param ticket
	 *            ticket à empiler.
	 */
	public static void addTicketToQueue(TicketQuery ticket) {
		TicketQueueManager.getInstance().addTicket(ticket);
	}

	/**
	 * récupérer le login de la session.
	 * 
	 * @return le nom d'utilisateur de la session.
	 */
	public static String getLogin() {
		return System.getProperty(Constant.USERNAME);
	}

	/**
	 * @param targetClass
	 * @param string
	 * @return
	 */
	public static Object unmarshallFromString(Class<?> targetClass, String xmlString) {
		Object result = null;
		try {
			JAXBContext context = JAXBContext.newInstance(targetClass);
			Unmarshaller unmarshaller = context.createUnmarshaller();		
			StringReader reader = new StringReader(xmlString);
			result = unmarshaller.unmarshal(reader);
		} catch (JAXBException e) {
			logger.error("Erreur de de serialisation du xml : " + xmlString + " :\n" + e.getMessage());
		}
		return result;
	}

	/**
	 * Transforme un objet en xml
	 * @param targetClas
	 * @return
	 */
	public static String unmarshallFromObject(Class<?> targetClas,Object object) {
		StringWriter writer = new StringWriter();
		try {
			JAXBContext context = JAXBContext.newInstance(targetClas);
			Marshaller m = context.createMarshaller();
			//m.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");
			m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
			m.marshal(object, writer);
		} catch (JAXBException e) {
			logger.error("Erreur de serialisation de la classe : " + targetClas.getName() + " :\n"+e.getMessage());
			return null;
		}
		return writer.toString();
	}
	
	/**
	 * 
	 * @param targetClas
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public static ByteArrayOutputStream getXMLSource(Class<?> targetClas,Object object) throws Exception {
		JAXBContext context;

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();

		try {
			context = JAXBContext.newInstance(targetClas);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(object, outStream);
		} catch (JAXBException e) {

			e.printStackTrace();
		}
		return outStream;

	}
	/**
	 * 
	 * @param to
	 * @param from
	 * @param subject
	 * @param text
	 * @param smtpHost
	 */
	public static void sendEmail(String to,  String from, String subject, String text, String smtpHost) {
		try {
			Properties properties = new Properties();
			properties.put(Constant.SMTPHOST, smtpHost);
			Session emailSession = Session.getDefaultInstance(properties);

			Message emailMessage = new MimeMessage(emailSession);
			emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			emailMessage.setFrom(new InternetAddress(from));
			emailMessage.setSubject(subject);
			emailMessage.setText(text);

			emailSession.setDebug(true);

			Transport.send(emailMessage);
		} catch (AddressException e) {
			logger.error("Erreur sur l'adresse mail :"+e.getMessage());
		} catch (MessagingException e) {
			logger.error("Erreur sur l'envoi du mail :"+e.getMessage());
		}
	}

	/**
	 * @param filePath
	 * @return
	 */
	public static String getFileNameFromPath(String filePath) {
		String fileName="";
		try {
			Path p = Paths.get(filePath);
			fileName = p.getFileName().toString();
		} catch (Exception e) {
			logger.error("Erreur de récupération du nom de fichier depuis son chemin :"+e.getMessage());
		}
		return fileName;
		
	}
	/**
	 * Permet de créer une balise xml 
	 * @param value
	 * @param qname
	 * @return
	 */
	public static JAXBElement<String> xmlElementFactory(String value, String qname) {
		return new JAXBElement<String>(new QName("", qname), String.class, null, value);
	}
	/**
	 * Convert date to string date format
	 * @param date
	 * @param format
	 * @return
	 */
	public static String convertDateToString(Date date, String format){		
		String timeStamp = new SimpleDateFormat(format).format(date);
		return timeStamp;
		
	}
	/**
	 * Renvoie la date actuelle sous de forme de string 
	 * @param format
	 * @return
	 */
	public static String getCurrentDateToString(String format){
		LocalDateTime date = LocalDateTime.now();		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		String text = date.format(formatter);
		return text;
	}
	
	/**
	 * Renvoie la date actuelle 
	 * @param format
	 * @return
	 */
	public static Timestamp getCurrentDate(String format){
		LocalDateTime date = LocalDateTime.now();		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		String date_s = date.format(formatter);		
		Timestamp parsedDate = null;
		parsedDate = Timestamp.valueOf(LocalDateTime.parse(date_s));
		
		return parsedDate;
	}
	
	/**
     * Removes all invalid Unicode characters that are not suitable to be used either
     * in markup or text inside XML Documents.
     * 
     * Based on these recommendations
     * http://www.w3.org/TR/2000/REC-xml-20001006#NT-Char
     * http://cse-mjmcl.cse.bris.ac.uk/blog/2007/02/14/1171465494443.html
     *
     * @param s The resultant String stripped of the offending characters!
     * @return
     */
	public static String removeInvalidXMLCharacters(String content) {
		StringBuilder out = new StringBuilder();

        int codePoint;
        int i = 0;

        while (i < content.length())
        {
            // This is the unicode code of the character.
            codePoint = content.codePointAt(i);
            if ((codePoint == 0x9) ||
                    (codePoint == 0xA) ||
                    (codePoint == 0xD) ||
                    ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                    ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                    ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)))
            {
                out.append(Character.toChars(codePoint));
            }
            i += Character.charCount(codePoint);
        }
        
        return out.toString().replaceAll("&lt;", "<").replaceAll("&gt;", ">");
	}
	/**
	 * Remove all XML MArkup in the string
	 * @param s
	 * @return
	 */
	public static String removeXMLMarkups(String s){
        StringBuffer out = new StringBuffer();
        char[] allCharacters = s.toCharArray();
        for (char c : allCharacters){
            if ((c == '\'') || (c == '<') || (c == '>') || (c == '&') || (c == '\"')){
                continue;
            }
            else{
                out.append(c);
            }
        }
        return out.toString();
	}
	
	/**
	 * Check if string contains XML MArkups
	 * @param s
	 * @return
	 */
	public static boolean containsXMLMarkups(String s){
        boolean flag = false;
        char[] allCharacters = s.toCharArray();
        for (char c : allCharacters){
            if ((c == '\'') || (c == '<') || (c == '>') || (c == '&') || (c == '\"')){
            	flag = true;
                return flag;
            }           
        }
        return flag;
	 }

	/**
	 * Escape XML markups
	 * @param s
	 */
	public static String xmlEscape(String s){
		s = s.replaceAll("<", "&#60;");
		s = s.replaceAll(">", "&#62;");
		s = s.replaceAll("'", "&#39;");
		s = s.replaceAll("\"", "&#34;");
		s = s.replaceAll("&", "&#38;");
		return s;
	}
	 /**
	   * Checks if a string contains illegal characters with respect to the XML
	   * specification.
	   *
	   * @param text the text to be checked
	   *
	   * @return true if illegal chars contained, otherwise false
	   */
	  public static boolean containsIllegalChars(String text) {
	    int size        = text.length();
	    boolean illegal = false;
	    for (int i = 0; i < size; i++) {
	      char c = text.charAt(i);
	      if ((c < 32) && (c != '\t') && (c != '\n') && (c != '\r')) {
	        illegal = true;
	        break;
	      }
	    }
	    return illegal;
	  }
	  
	  /**
	   * Cleans strings of illegal characters with respect to the XML
	   * specification.
	   *
	   * @param text string to be cleaned
	   *
	   * @return the cleaned string
	   */
	  public static String purgeString(String text) {
	    char[] block        = null;
	    StringBuffer buffer = new StringBuffer();
	    int i;
	    int last            = 0;
	    int size            = text.length();
	    for (i = 0; i < size; i++) {
	      char c = text.charAt(i);
	      if ((c < 32) && (c != '\t') && (c != '\n') && (c != '\r')) {
	        // remove character
	        if (block == null) {
	          block = text.toCharArray();
	        }
	        buffer.append(block, last, i - last);
	        last = i + 1;
	      }
	    }
	    if (last == 0) {
	      return text;
	    }
	    if (last < size) {
	      if (block == null) {
	        block = text.toCharArray();
	      }
	      buffer.append(block, last, i - last);
	    }
	    return buffer.toString();
	  }

   /**
    * File copy process
    * @param in
    * @param out
    * @throws Exception
    */
   public static void fileCopy(String in, String out) throws Exception {
	 Path path = Paths.get(out);
	 Path realPath = path.getParent();
	 if(Files.notExists(realPath, LinkOption.NOFOLLOW_LINKS)){
		 Files.createDirectories(realPath, new FileAttribute[]{});
	 }
	 
	 FileInputStream fis = new FileInputStream(in);
  	 FileChannel sourceChannel = fis.getChannel();
  	 java.io.File fileOut =new java.io.File(out);
  	 FileOutputStream fos = new FileOutputStream(fileOut);

  	 FileChannel destinationChannel = fos.getChannel();
  	
  	 long tranfert=sourceChannel.size();
  	 long debut=0;
  	
  	 while(tranfert>0){
  		 long trans=sourceChannel.transferTo(debut, 1024*1024*10, destinationChannel);
  		 debut+=trans;
  		 tranfert-=trans;
  	 }
  	
  	 sourceChannel.close();
  	 destinationChannel.close();
  	 fis.close();
  	 fos.close();
  	
   }

   /**
    * Process logs
    * @param ticket
    * @return
    */
	public static Logs loggerGenerator(TicketQuery ticket,String message,boolean isLogStaus) {
		Logs logs = new Logs();
		logs.setDateDebut(getCurrentDateToString(Constant.FORMAT_YYYY_MM_DD_HH_MM_SS));
		if(!isLogStaus){
			logs.setDateFin(getCurrentDateToString(Constant.FORMAT_YYYY_MM_DD_HH_MM_SS));
		}
		logs.setLogin(Helper.getLogin());
		logs.setMachine(Helper.getComputerName());
		logs.setModule(Helper.getProperty("module.name"));
		logs.setIdOcr(String.valueOf(ticket.getIdOCR()));
		logs.setIdProduit(String.valueOf(ticket.getIdProduit()));
		message = message != null ? message : "";
		if(message.length() > 2000){
			message = message.substring(0, 2000);
		}
		logs.setDescription(message);
		return logs;
	}
	/**
	 * 
	 * @param message
	 * @param isLogStaus
	 * @return
	 */
	public static Logs loggerGenerator(String message,boolean isLogStaus) {
		Logs logs = new Logs();
		logs.setDateDebut(getCurrentDateToString(Constant.FORMAT_YYYY_MM_DD_HH_MM_SS));
		if(!isLogStaus){
			logs.setDateFin(getCurrentDateToString(Constant.FORMAT_YYYY_MM_DD_HH_MM_SS));
		}
		logs.setLogin(Helper.getLogin());
		logs.setMachine(getComputerName());
		logs.setModule(getProperty("module.name"));
		logs.setIdOcr("");
		logs.setIdProduit("");
		message = message != null ? message : "";
		if(message.length() > 2000){
			message = message.substring(0, 2000);
		}
		logs.setDescription(message);
		return logs;
	}
	
	/**
	 * XML Validator schema
	 * @param xsdPath
	 * @param xmlPath
	 * @return
	 */
	public static boolean XSDValidatorXMLSchema(String xsdPath, String xmlPath){
		try{
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema scheama = factory.newSchema(new java.io.File(xsdPath));
			Validator validator =  scheama.newValidator();
			validator.validate(new StreamSource(new java.io.File(xmlPath)));
		}catch(IOException e){
			return false;
		} catch (SAXException e) {
			return false;
		}
		return true;
	}
	/**
	 * XML Schema validator 
	 * @param xsdPath
	 * @param xmlPath
	 * @return
	 */
	public static boolean XSDValidatorXMLSchema(java.io.File xsdPath, java.io.File xmlPath){
		try{
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema scheama = factory.newSchema(xsdPath);
			Validator validator =  scheama.newValidator();
			validator.validate(new StreamSource(xmlPath));
		}catch(IOException e){
			return false;
		} catch (SAXException e) {
			return false;
		}
		return true;
	}
	/**
	 * XSD Content generator from XML
	 * @param xmlFilename
	 * @return
	 */
	public static String XSDContentGeneratorFormXml(String xmlFilename){
		StringWriter writer = new StringWriter();
		try {
            XsdGen xmlBeans = new XsdGen();
            SchemaDocument schemaDocument = xmlBeans.generateSchema(new java.io.File(xmlFilename));
            schemaDocument.save(writer, new XmlOptions().setSavePrettyPrint());
            writer.close();
    
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
		return writer.toString();
	}
	/**
	 * XSD File generator from XML file
	 * @param xmlFilename
	 * @return
	 */
	public static java.io.File XSDGeneratorFormXml(String xmlFilename){
		java.io.File writer = new java.io.File(xmlFilename);
		try {
            XsdGen xmlBeans = new XsdGen();
            SchemaDocument schemaDocument = xmlBeans.generateSchema(new java.io.File(xmlFilename));
            schemaDocument.save(writer, new XmlOptions().setSavePrettyPrint());
           
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
		return writer;
	}
	
	/**
	   * This is parsing code
	   * 
	   * @param xml The input argument to check.
	   * @throws SAXException
	   *             If the input xml is invalid
	   * 
	   * @throws SystemException
	   *             Thrown if the input string cannot be read
	   */
	  public static boolean validate(String xml) {
	    try {
	      doValidate(xml);
	    } catch (IOException | SAXException ioe) {	    	
	    	return false;
	    }
	    return true;
	  }
	  /**
	   * Validate xml Well formatted
	   * @param xml
	   * @throws SAXException
	   * @throws IOException
	   */
	  private static void doValidate(String xml) throws SAXException, IOException {
	    XMLReader parser = XMLReaderFactory.createXMLReader();
	    parser.setContentHandler(new DefaultHandler());
	    InputSource source = new InputSource(new ByteArrayInputStream(xml.getBytes()));
	    parser.parse(source);
	  }
	  /**
	   * Verify if image can be opened
	   * @param name
	   * @return
	   */
	  public static boolean isValidImage(java.io.File file){
		 boolean valid = true;
		 try {
		    Image image = ImageIO.read(file);
		    if (image == null) {
		        valid = false;		        
		    }
		 } catch(IOException ex) {
		    valid = false;		
		 }
		 return valid;
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
		public static String makeStringFromXmlContent(byte[] bytes) throws SQLException
			{
			String str ="";
			try{
				str = new String(bytes, Constant.ENCODER_TYPE_UTF_8);
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
				transformer.setOutputProperty(OutputKeys.ENCODING, Constant.ENCODER_TYPE_UTF_8);
				transformer.transform(new DOMSource(doc), new StreamResult(sw));
				str=sw.toString();	      
			    outStream1.close();
		    
			} catch (ParserConfigurationException pce) {				
				logger.error("Error de parsing de configuration: ", pce);
			} catch (SAXException se) {				
				logger.error("Error de parsing xml: ", se);
			} catch (TransformerConfigurationException tce) {				
				logger.error("Error de transformation de configuration: ", tce);
			} catch (TransformerException e) {				
				logger.error("Error de transfromation de xml en string: ", e);
			} catch (Exception ex) {				
				logger.error("Error survenue dans la methode getXMLContent(): ", ex);		
			}	
			return str;
		}

		/**
		 * 
		 * @param resource
		 * @param livraison
		 * @return le résultat de l'éxecution de la requête xsl resource sur le contenu de la livraison 
		 * @throws Exception
		 */
	public static String runXslRequestOnXmlContent(byte[] resource, Livraison livraison,boolean isParam) throws Exception {

		StringReader livraisonContent = new StringReader(livraison.getContent());
		
		InputSource inputXml = new InputSource(livraisonContent);
		InputStream resourceContent = new ByteArrayInputStream(resource);
		StringWriter output = new StringWriter();
		try {
			TransformerFactory tFactory = TransformerFactory.newInstance();  
			Transformer transformer = tFactory.newTransformer(new StreamSource(resourceContent)); 
			if(isParam){
				transformer.setParameter("parameter", livraison.getContent());
			}
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");                
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(false);
			DocumentBuilder docBuilder = dbf.newDocumentBuilder();
			org.w3c.dom.Document xfddDoc = docBuilder.parse(inputXml);
			transformer.transform(new DOMSource(xfddDoc), new StreamResult(output));
		}catch(TransformerConfigurationException tce){
				logger.info("Erreur lors de la configuration de la transformation de l'xml "+ inputXml +" : ", tce.getMessage());
			}
			catch(ParserConfigurationException pce){
				logger.info("Erreur sur le parse : ", pce.getMessage());
			}
			catch(SAXException se){
				logger.info("Erreur sax : ", se.getMessage());
			}
			catch(TransformerException te){
				logger.info("Erreur lors de la transformation de l'xml : ", te.getMessage());
			}
			catch(IOException ioe ){
				logger.info("Erreur d'entrée sortie : ", ioe.getMessage());
			}
		return output.toString();
	}
	
	/**
	 * Web service parameters generator for cover page 
	 * @param resource
	 * @param url
	 * @param filename
	 * @param datePublication
	 * @return
	 * @throws Exception
	 */
	public static String generateParametersWebserviceByXslFunction(byte[] resource, String url,String filename,String datePublication) throws Exception {

		StringReader livraisonContent = new StringReader("<doc/>");
		
		InputSource inputXml = new InputSource(livraisonContent);
		InputStream resourceContent = new ByteArrayInputStream(resource);
		StringWriter output = new StringWriter();
		try {
			TransformerFactory tFactory = TransformerFactory.newInstance();  
			Transformer transformer = tFactory.newTransformer(new StreamSource(resourceContent)); 			
			transformer.setParameter("url", url);
			transformer.setParameter("filename", filename);
			transformer.setParameter("date", datePublication);			
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");                
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(false);
			DocumentBuilder docBuilder = dbf.newDocumentBuilder();
			org.w3c.dom.Document xfddDoc = docBuilder.parse(inputXml);
			transformer.transform(new DOMSource(xfddDoc), new StreamResult(output));
		}catch(TransformerConfigurationException tce){
				logger.info("Erreur lors de la configuration de la transformation de l'xml "+ inputXml +" : ", tce.getMessage());
			}
			catch(ParserConfigurationException pce){
				logger.info("Erreur sur le parse : ", pce.getMessage());
			}
			catch(SAXException se){
				logger.info("Erreur sax : ", se.getMessage());
			}
			catch(TransformerException te){
				logger.info("Erreur lors de la transformation de l'xml : ", te.getMessage());
			}
			catch(IOException ioe ){
				logger.info("Erreur d'entrée sortie : ", ioe.getMessage());
			}
		return output.toString();
	}	
	
	 /**
	  * Method : generate the hash of temporary file path 
	  * @param dateReception
	  * @param identifiant
	  * @return
	  */
	 public static String makeFileHashPath(String dateReception,String identifiant, String poitnMontage){		
		 StringBuffer sb =  new StringBuffer();		
		 sb.append(poitnMontage);
		 sb.append(slash);
		 if(dateReception!=null && !dateReception.isEmpty() && dateReception.length()>=8){			 
			 sb.append(dateReception.substring(0, 4)).append(slash)
			 .append(dateReception.substring(4, 6)).append(slash)
			 .append(dateReception.substring(6, 8)).append(slash);
		 }
		 String idenTmp = PADDING.concat(identifiant);
		 int beginIndex = idenTmp.length()-3;
		 if(idenTmp != null && !idenTmp.isEmpty()){
			 sb.append(idenTmp.substring(beginIndex)).append(slash)
			 .append(identifiant).append(slash);
		 }
		 return sb.toString();
	 }
	 
   /**
	* Make the relative path 
	* @param entry
	*/
	public static String makeRelativePath(String entry) {
		StringBuffer relativePath = new StringBuffer("");
		String [] tmp = new String[0];		
		tmp = entry.split("[\\\\|/]");		
		for(int i=0; i<tmp.length-1;i++){
			relativePath.append(tmp[i]+slash);
		}
		
		return relativePath.toString();
	}		 		
		 	
	 /**
		 * File fast copy 
		 * @param src
		 * @param dest
		 * @throws IOException
		 */
	public static  byte[] fastChannelCopy(final ReadableByteChannel src, final WritableByteChannel dest,int sizeOf) throws IOException {
	    final ByteBuffer buffer = ByteBuffer.allocateDirect(sizeOf);
	    while (src.read(buffer) != -1) {	      
	      buffer.flip();// prepare the buffer to be drained	      
	      dest.write(buffer);// write to the channel, may block	     
	      buffer.compact(); // If partial transfer, shift remainder down // If buffer is empty, same as doing clear()
	    }
	
	    byte[] bytes = new byte[sizeOf];
	    buffer.get(bytes);
	    buffer.flip();// EOF will leave buffer in fill state
	    
	    // make sure the buffer is fully drained.
	    while (buffer.hasRemaining()) {
	      dest.write(buffer);
	    }
	   	   
	    return bytes;
	}
	
	 /**
	  * Method check if the stream is an archive.
	  * @param in the input stream.
	  * @return
	  */
	 public static boolean isZipStream(InputStream in){		 
		 if (!in.markSupported()){
			 in = new BufferedInputStream(in);
		 }
		 boolean isZip = true;
		 try {
			 in.mark(MAGIC.length);
			 for (int i = 0; i < MAGIC.length; i++) {
				 if (MAGIC[i] != (byte) in.read()) {
					 isZip = false;
					 break;
				 }
			 }
			 in.reset();
		 } catch (IOException e) {
			 logger.error("Erreur de vérification du flux de l'archive - isZipStream() : ", e);
			 isZip = false;
		 }
		 return isZip;
	 }
	 
	 /**
	  * Check i=f the file is ZIP.
	  * @param f
	  * @return
	  */
	 public static boolean isZipFile(String f) {
		 boolean isZip = true;
		 byte[] buffer = new byte[MAGIC.length];
		 try {
			 RandomAccessFile raf = new RandomAccessFile(f, "r");
			 raf.readFully(buffer);
			 for (int i = 0; i < MAGIC.length; i++) {
				 if (buffer[i] != MAGIC[i]) {
					 isZip = false;
					 break;
				 }
			 }
			 raf.close();
		 } catch (Throwable e) {
			 logger.error("Erreur de vérification du fichier de l'archive - isZipFile() : ", e);
			 isZip = false;
		 }
		 return isZip;
	 }
	 
	 /**
		 * Generate the XML file that content list of id Livraison
		 * @param ids
		 * @param fichier
		 */
		public static void generatetXmlIdLivraisonFile(String ids, String fichier,String libelleRoot,String libelleChild){
			try {
				Element root = new Element(libelleRoot); 
				org.jdom2.Document document = new org.jdom2.Document(root);		
				Element ident = new Element(libelleChild);
				ident.setText(ids);
				root.addContent(ident);
			
				XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());		
				sortie.output(document, new FileWriter(new java.io.File(fichier)));
				
			} catch (FileNotFoundException e) {
				logger.error("Erreur fichier introuvable :"+e.getMessage());
			} catch (IOException e) {
				logger.error("Erreur de génération du fichier xml contenant la liste des ids livraison :"+e.getMessage());
			}
			
		}
		
		/**
		 * Generate XML ticket for sous lots		
		 * @param fichier
		 * @param libelleRoot
		 * @param ticket
		 */
		public static void generatetTcketSousLot(String fichier,String xmlSource){
			try {								
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			    DocumentBuilder builder = factory.newDocumentBuilder();
			    Document doc = builder.parse(new InputSource(new StringReader(xmlSource)));

			    // Write the parsed document to an xml file
			    TransformerFactory transformerFactory = TransformerFactory.newInstance();
			    Transformer transformer = transformerFactory.newTransformer();
			    DOMSource source = new DOMSource(doc);

			    StreamResult result =  new StreamResult(new java.io.File(fichier));
			    transformer.transform(source, result);
				
			} catch (FileNotFoundException e) {
				logger.error("FileNotFoundException | Erreur fichier introuvable :"+e.getMessage());
			} catch (IOException e) {
				logger.error("IOException | Erreur de génération du fichier ticket contenant les infos pour les process tradepress :"+e.getMessage());
			} catch (ParserConfigurationException e) {
				logger.error("ParserConfigurationException | Erreur de génération du fichier ticket contenant les infos pour les process tradepress :"+e.getMessage());
			} catch (SAXException e) {
				logger.error("SAXException | Erreur de génération du fichier ticket contenant les infos pour les process tradepress :"+e.getMessage());
			} catch (TransformerConfigurationException e) {
				logger.error("TransformerConfigurationException | Erreur de génération du fichier ticket contenant les infos pour les process tradepress :"+e.getMessage());
			} catch (TransformerException e) {
				logger.error("TransformerException | Erreur de génération du fichier ticket contenant les infos pour les process tradepress :"+e.getMessage());
			}
			
		}
		
		/**
		 * TRADEXPRESS Process executor in SSH command connection
		 * @param filename
		 * @param pathXML
		 * @param processTradExpress
		 * @param remoteDir
		 */

		public static void executeCommand(String filename, String pathXML,ProcessTradExpress processTradExpress,String remoteDir) {
		 
			String ipMachine = processTradExpress.getIp();
			String command = processTradExpress.getCommand()+" "+remoteDir+filename;
			String login = processTradExpress.getLogin();
			String password = processTradExpress.getPassword();

			SFTPManager.upload(login, password, ipMachine, pathXML, remoteDir,filename);
	  
			try {			
				SSHFunctions.SSHClient(ipMachine,command,login,password);
			} catch (Exception e) {
				logger.error("Erreur lors de l'ancement du process TradExpress : ", e.getMessage());
			}
		}
		
	
		
		@SafeVarargs
		public static <T> T firstOrDefault(T[] ts, T... defaultValue) {
			return !isNullOrEmpty(ts) ? ts[0] : !isNullOrEmpty(defaultValue) ? defaultValue[0]: null;
		}
		
		/** Get value from next supplier if this result is null or Exception
		 * @param exLogger
		 * @param suppliers
		 * @return
		 */
		@SafeVarargs
		public static <T> T coalesce(Consumer<Exception> exLogger, Supplier<T>... suppliers){
			if (isNullOrEmpty(suppliers)) return null;
	        try{
	        	return  of(suppliers[0].get()).get();
	        }catch (Exception e) {
	        	if (exLogger!=null) exLogger.accept(e);
	            return coalesce(exLogger, copyOfRange(suppliers, 1, suppliers.length));
	        }
		 }
		
		/**
		 * @param defaultValue
		 * @param supplier
		 * @param exLogger
		 */
		@SuppressWarnings("unchecked")
		@SafeVarargs
		public static <T> T defaultIfNullOrException(T defaultValue, Supplier<T> supplier, Consumer<Exception>...exLogger){
			return coalesce(firstOrDefault(exLogger), ArrayUtils.toArray(supplier, ()->defaultValue));
		}

		 /**
		  * Only for String
		 * @param defaultValue
		 * @param supplier
		 * @return if null or empty or NullPointerException, then return defaultValue, else return it
		 */
		@SafeVarargs
		public static String defaultIfEmptyOrNullOrException(String defaultValue, Supplier<String> supplier, Consumer<Exception>...exLogger){
			return defaultIfNullOrException(defaultValue, ()-> emptyToNull(supplier.get()), exLogger);
		 }
		
		public static Supplier<Builder<String, String>> outputKeysBuilder = ()->ImmutableMap.<String, String>builder()
				.put(OutputKeys.OMIT_XML_DECLARATION, "yes")
				.put(OutputKeys.ENCODING, "ISO-8859-1");  
		
		public static <T> Stream<T> streamOf(Optional<T> ot) {
            return ot.map(Stream::of).orElseGet(Stream::empty); 
		}
		
	/**
	 * @param xml
	 * @param params
	 * @param xsl
	 * @param outputKeys
	 * @param result
	 */
	public static void runXslOnXml(String xml, Map<String, ?> params, byte[] xsl, Map<String, String> outputKeys, Result result) {

		InputSource xmlSource = new InputSource(new StringReader(xml));
		StreamSource xslSource = new StreamSource(new ByteArrayInputStream(xsl));

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(false);
			DocumentBuilder docBuilder = dbf.newDocumentBuilder();
			org.w3c.dom.Document xfddDoc = docBuilder.parse(xmlSource);
			DOMSource xmlDomSource = new DOMSource(xfddDoc);
			
			TransformerFactory tFactory = new net.sf.saxon.TransformerFactoryImpl();
			Transformer transformer = tFactory.newTransformer(xslSource); 
			outputKeys.entrySet().forEach(key->transformer.setOutputProperty(key.getKey(), key.getValue()));
			params.entrySet().forEach(entry->transformer.setParameter(entry.getKey(), entry.getValue()));

			transformer.transform(xmlDomSource, result);
		}
		catch(TransformerConfigurationException tce){
			logger.error("Erreur lors de la configuration de la transformation de l'xml "+ xmlSource +" : ", tce.getMessage());
		}
		catch(ParserConfigurationException pce){
			logger.error("Erreur sur le parse : ", pce.getMessage());
		}
		catch(SAXException se){
			logger.error("Erreur sax : ", se.getMessage());
		}
		catch(TransformerException te){
			logger.error("Erreur lors de la transformation de l'xml : ", te.getMessage());
		}
		catch(IOException ioe ){
			logger.error("Erreur d'entrée sortie : ", ioe.getMessage());
		}

	}
	
	public static String getPath(String...parts) {
		if (isNullOrEmpty(parts)) return "";
		return stream(parts).skip(1).map(part->stripStart(part, "/\\").trim()).map(Paths::get).reduce(get(parts[0].trim().replaceAll("[/\\\\]+", "/")), (result, part)->result.resolve(part)).toString();
	}
	
	public static boolean validXmlByXsd(String xml, byte[] xsd){
		StreamSource xmlSource = new StreamSource(IOUtils.toInputStream(xml));
		StreamSource xslSource = new StreamSource(new ByteArrayInputStream(xsd));
		try{
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema scheama = factory.newSchema(xslSource);
			Validator validator =  scheama.newValidator();
			validator.validate(xmlSource);
		}catch(IOException e){
			return false;
		} catch (SAXException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * Manager tradexpress status by module
	 * @param ticket
	 * @param etape
	 * @param dbClient
	 */
	public static void statusTradExpressManager(TicketQuery ticket,String etape,DBClient dbClient,String idPere){		
		String filename = PREFIX_TICKET_STATUS_FILE_ID+ticket.getIdLivraison()+".xml";
		String tmpDir = System.getProperty("java.io.tmpdir");
		java.io.File temp = new java.io.File(tmpDir, filename);
		String pathXML = getFileName(temp);
		StatusManager status = StatusManager.builder().id(ticket.getIdLivraison()).bpindex(ticket.getBpIndex()).moduleName(getProperty("module.name")).status(etape).idPere(idPere).build();
		generatetTcketSousLot(pathXML,status.getContent());
		ProcessTradExpress processTradExpress = dbClient.getProcessTradExpress(PROCESS_TRADEXPRESS,getProperty("module.name")); 
		executeCommand(filename, pathXML,processTradExpress,remoteDir);
		deleteTemporaryFile(temp);
	}
	
	
	/**
	 * @param filename
	 * @param tmpDir
	 * @param idTicket
	 * @return
	 */
	public static String generateFileTicket(String filename,String tmpDir) {		
		java.io.File temp = new java.io.File(tmpDir, filename);						
		//temp.deleteOnExit();
		String pathXML = "";
			
		try {
			pathXML = temp.getCanonicalPath();
		} catch (IOException e1) {
			logger.error("Erreur lors de récupération du nom de fichier paramètre du process TradExpress : ", e1.getMessage());
		}
		return pathXML;
	}
	/**
	 * 
	 * @param temp
	 * @return
	 */
	public static String getFileName(java.io.File temp) {
		String pathXML="";
		try {
			pathXML = temp.getCanonicalPath();
		} catch (Exception e) {
			logger.error("Erreur lors de récupération du nom de fichier paramètre du process TradExpress : ", e.getMessage());
		}
		return pathXML;
	}
	/**
	 * @param temp
	 */
	public static void deleteTemporaryFile(java.io.File temp) {
		try {
			FileUtils.deleteQuietly(temp);			
		} catch (Exception e) {
			logger.error("Erreur de suppression de fichier temporaire : "+temp.toPath(), e.getMessage());
		}
	}
	
	/**
	 * Get fiel creation date
	 * @param pathFile
	 * @return
	 * @throws IOException 
	 */
	public static Date fileCreationDate(String pathFile) throws IOException {
		Path path = Paths.get(pathFile);
		Date data =null;	
		BasicFileAttributes attr = java.nio.file.Files.readAttributes(path,BasicFileAttributes.class);
		if(attr!=null)  data = new Date(attr.creationTime().toMillis());		
		return data;
	}
}
