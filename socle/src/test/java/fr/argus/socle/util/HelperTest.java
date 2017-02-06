package fr.argus.socle.util;

import static fr.argus.socle.util.Helper.coalesce;
import static fr.argus.socle.util.Helper.defaultIfEmptyOrNullOrException;
import static fr.argus.socle.util.Helper.defaultIfNullOrException;
import static fr.argus.socle.util.Helper.firstOrDefault;
import static fr.argus.socle.util.Helper.getProperty;
import static java.nio.file.Files.readAllBytes;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.emptyList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Collections;
import java.util.function.Supplier;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Arrays;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import fr.argus.socle.model.Ticket;

/**
 * @author hongjie.zhang
 *
 */
public class HelperTest {
	
    @Test
    public void testFirst() throws Exception {
    	Object result = firstOrDefault(null, Arrays.array());
    	assertThat(result).isNull();
    }
    
    @Test
    public void testFirst2() throws Exception {
    	Object result = firstOrDefault(Arrays.array());
    	assertThat(result).isNull();
    }
    
    @Test
    public void testCoalesce_suppliers_empty() throws Exception {
    	Object result = coalesce(null);
    	assertThat(result).isNull();
    }
    
    @Test
    public void testCoalesce_suppliers_null() throws Exception {
    	Object result = coalesce(null, (Supplier<Object>)null);
    	assertThat(result).isNull();
    }
    
    @Test
    public void testCoalesce_suppliers_elements_nulls() throws Exception {
    	Object result = coalesce(null, null, null);
    	assertThat(result).isNull();
    }
    
    @Test
    public void get_with_default_NPE() throws Exception {
        assertThat(
            defaultIfNullOrException(1, () -> {
                throw new NullPointerException();
            })
        ).isEqualTo(1);
    }

    @Test
    public void get_with_default_null() throws Exception {
        assertThat(
                defaultIfNullOrException(emptyList(), () -> null)
        ).isEqualTo(emptyList());
    }

    @Test
    public void get_with_default_normal() throws Exception {
        assertThat(
                defaultIfNullOrException(1, () -> 15)
        ).isEqualTo(15);
    }

    @Test
    public void empty_to_default_empty() throws Exception {
        assertThat(
        		defaultIfEmptyOrNullOrException("default", () -> "")
        ).isEqualTo("default");
    }
    
    @Test
    public void empty_to_default_other_exception() throws Exception {
        assertThat(
    		defaultIfEmptyOrNullOrException("default", () -> (10/0)+"")
        ).isEqualTo("default");
    }

    @Test
    public void should_makeStringFromXmlContent() throws SQLException {
    	String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
    			+ "<ns0:DEMANDE_POINTAGE xmlns:ns0=\"http://rs2i.fr/Argus/Bulletinage/XSD/DEMANDE_POINTAGE\">"
    			+"    <ns0:id_source>617261</ns0:id_source>"
    			+"    <ns0:titre_source>OUEST FRANCE</ns0:titre_source>"
    			+"    <ns0:id_demande_pointage>1022270</ns0:id_demande_pointage>"
    			+"    <ns0:code_barre>0023940202402</ns0:code_barre>"
    			+"    <ns0:utilisateur/>"
    			+"    <ns0:feuille_pointage>20161209/0023940202402.pdf</ns0:feuille_pointage>"
    			+"    <ns0:date_parution>2016-12-09</ns0:date_parution>"
    			+"    <ns0:date_faciale>09/12/2016</ns0:date_faciale>"
    			+"    <ns0:numero_publication>5341654</ns0:numero_publication>"
    			+"    <ns0:nb_pages_publication>6546</ns0:nb_pages_publication>"
    			+"    <ns0:message_pointage/>"
    			+"    <ns0:origine>ESB</ns0:origine>"
    			+"    <ns0:a_scanner>false</ns0:a_scanner>"
    			+"    <ns0:rescan>false</ns0:rescan>"
    			+"    <ns0:traitement_id>3</ns0:traitement_id>"
    			+"    <ns0:traitement_label>TraitementPDFLineaire</ns0:traitement_label>"
    			+"</ns0:DEMANDE_POINTAGE>";
    	String result = Helper.makeStringFromXmlContent(xml.getBytes());
    	assertThat(result).isEqualTo("<ns0:DEMANDE_POINTAGE xmlns:ns0=\"http://rs2i.fr/Argus/Bulletinage/XSD/DEMANDE_POINTAGE\">    <ns0:id_source>617261</ns0:id_source>    <ns0:titre_source>OUEST FRANCE</ns0:titre_source>    <ns0:id_demande_pointage>1022270</ns0:id_demande_pointage>    <ns0:code_barre>0023940202402</ns0:code_barre>    <ns0:utilisateur/>    <ns0:feuille_pointage>20161209/0023940202402.pdf</ns0:feuille_pointage>    <ns0:date_parution>2016-12-09</ns0:date_parution>    <ns0:date_faciale>09/12/2016</ns0:date_faciale>    <ns0:numero_publication>5341654</ns0:numero_publication>    <ns0:nb_pages_publication>6546</ns0:nb_pages_publication>    <ns0:message_pointage/>    <ns0:origine>ESB</ns0:origine>    <ns0:a_scanner>false</ns0:a_scanner>    <ns0:rescan>false</ns0:rescan>    <ns0:traitement_id>3</ns0:traitement_id>    <ns0:traitement_label>TraitementPDFLineaire</ns0:traitement_label></ns0:DEMANDE_POINTAGE>");
    }
    
    /**
     * mamadou.dansoko test
     */
    @Test
    public void generatetTcketSousLot(){
		try {
			String fichier = "/tmp/ticket.xml";
			Ticket ticket = Ticket.builder().bpIndex("1254")
					.cheminFichier("C:/path/")
					.dateDipo("10/01/2017").id("11")
					.idOCR("0").idPere("854").idProduit("0")
					.modeApprovisionnement("ftp")
					.nomFichier("tst.txt").priority("1")
					.type("txt")
					.tailleFichier("1254856").build();
			
			String xmlSource = ticket.getContent();
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder builder = factory.newDocumentBuilder();
		    Document doc = builder.parse(new InputSource(new StringReader(xmlSource)));

		    // Write the parsed document to an xml file
		    TransformerFactory transformerFactory = TransformerFactory.newInstance();
		    Transformer transformer = transformerFactory.newTransformer();
		    DOMSource source = new DOMSource(doc);

		    StreamResult result =  new StreamResult(new java.io.File(fichier));
		    transformer.transform(source, result);
			
		    FileUtils.deleteQuietly(new java.io.File(fichier));
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException | Erreur fichier introuvable :"+e.getMessage());
		} catch (IOException e) {
			System.out.println("IOException | Erreur de génération du fichier ticket contenant les infos pour les process tradepress :"+e.getMessage());
		} catch (ParserConfigurationException e) {
			System.out.println("ParserConfigurationException | Erreur de génération du fichier ticket contenant les infos pour les process tradepress :"+e.getMessage());
		} catch (SAXException e) {
			System.out.println("SAXException | Erreur de génération du fichier ticket contenant les infos pour les process tradepress :"+e.getMessage());
		} catch (TransformerConfigurationException e) {
			System.out.println("TransformerConfigurationException | Erreur de génération du fichier ticket contenant les infos pour les process tradepress :"+e.getMessage());
		} catch (TransformerException e) {
			System.out.println("TransformerException | Erreur de génération du fichier ticket contenant les infos pour les process tradepress :"+e.getMessage());
		}
    }
    
    @Test
    public void should_makeStringFromXmlContent_of_file() throws SQLException, IOException, URISyntaxException {
    	String result = Helper.makeStringFromXmlContent(readAllBytes(Paths.get(getClass().getResource("9958221202424.xml").toURI())));
    	assertThat(result.replaceAll("[\n\r\t ]+", " ")).isEqualTo("<publication> <identification id_publication=\"9958221202424\"/> <source_magazine smg_date_parution=\"\" smg_express=\"non\" smg_fichier_source=\"5_MAJEUR-20161101-092715.zip\" smg_nb_articles=\"1\" smg_nb_pages=\"76\" smg_priorite=\"_PRIORITE_INCONNUE\" smg_provenance=\"Journaux.fr\" smg_titre=\"5 MAJEUR\"/> <extraction> <info_extraction xtrac_duree=\"1\" xtrac_heure_debut=\"2016/11/03-02:36:56\" xtrac_valide=\"oui\"/> </extraction> <verification> <info_verification vrif_duree=\"5\" vrif_heure_debut=\"2016/11/03-02:37:30\"/> </verification> <impression_publication> <info_impression_publication ipb_duree=\"4\" ipb_heure_debut=\"2016/11/03-02:39:37\"/> </impression_publication> </publication>");
    }

	@Test
	public void testRunXslOnXml() throws TransformerException, ParserConfigurationException, SAXException, IOException {		
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><test>abc</test>";
			
		String xsl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" xmlns:java=\"java.lang.String\" version=\"2.0\"> "
				+ "<xsl:template match=\"/\">"
				+ "	<xsl:value-of select=\"java:toUpperCase(/test)\" />"
				+ "</xsl:template>"
				+ "</xsl:stylesheet>";

		StringWriter output = new StringWriter();
		Helper.runXslOnXml(xml, Collections.emptyMap(), xsl.getBytes(), Helper.outputKeysBuilder.get().build(), new StreamResult(output));
		
		Assertions.assertThat(output.toString()).isEqualTo("ABC");
	}
    
	@Test
	public void testGetPath() {
		assertThat(Helper.getPath(Arrays.array("////ab////", "\\\\cd ", " ef ", "[my dir]", "gh/mn/"))).isEqualTo(Paths.get("/ab/cd/ef/[my dir]/gh/mn").toString());
	}
    
	@Test
	public void should_generate_XSD_from_Xml() throws SQLException, IOException, URISyntaxException {
		String xmlPath = Paths.get(getClass().getResource("9958221202424_002.xml").toURI()).toString();
		
		String xsd = Helper.XSDContentGeneratorFormXml(xmlPath);
		
		assertThat(xsd).isXmlEqualToContentOf(new File(getClass().getResource("xmlChema.xsd").toURI()));
	}
	
	@Test
	public void test(){
		String statusNom = "PRESSE_ERREUR_DEFAUT";
		String code = Signalements.valueOf(statusNom).getCode();
		assertThat(code).isEqualTo("201");
	}
}
