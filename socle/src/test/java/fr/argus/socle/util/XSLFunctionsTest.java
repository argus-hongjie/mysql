package fr.argus.socle.util;

import static java.nio.file.Paths.get;
import static org.apache.commons.lang3.StringEscapeUtils.escapeXml;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.FileUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import fr.argus.socle.db.DBClient;
import jersey.repackaged.com.google.common.collect.ImmutableMap;

@RunWith(MockitoJUnitRunner.class)
public class XSLFunctionsTest {
	@Mock
	private DBClient dbClient;
	
	@InjectMocks
	private XSLFunctions xSLFunctions;
	
	@Test
	public void testTar() throws TransformerException, ParserConfigurationException, SAXException, IOException {
		Path srcA = Files.createTempFile("a.", ".txt");
		Path srcB = Files.createTempFile("b.", ".txt");
		Path destTar = srcB.getParent().resolve("def.tar");
		
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<test>"
				+ "	<files>"
				+ "		<file>" + srcA + "</file>"
				+ "		<file>" + srcB + "</file>"
				+ "	</files>"
				+ "	<dest>" + destTar + "</dest>"
				+ " <base-path>abc</base-path>"
				+ "</test>";
			
		String xsl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" xmlns:java=\"fr.argus.socle.util.XSLFunctions\" version=\"2.0\"> "
				+ "<xsl:template match=\"/\">"
				+ "	<xsl:variable name=\"tar\" select=\"//dest\" />"
				+ "	<xsl:variable name=\"base-path\" select=\"//base-path\" />"
				+ "	<xsl:variable name=\"files\" select=\"//file/string()\" />"
				+ "	<xsl:value-of select=\"java:createTar($tar, $base-path, $files)\" />"
				+ "</xsl:template>"
				+ "</xsl:stylesheet>";
     
		InputSource xmlSource = new InputSource(new StringReader(xml));
		StreamSource xslSource = new StreamSource(new ByteArrayInputStream(xsl.getBytes()));

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(false);
		DocumentBuilder docBuilder = dbf.newDocumentBuilder();
		org.w3c.dom.Document xfddDoc = docBuilder.parse(xmlSource);
		DOMSource xmlDomSource = new DOMSource(xfddDoc);
		
		TransformerFactory tFactory = new net.sf.saxon.TransformerFactoryImpl();
		Transformer transformer = tFactory.newTransformer(xslSource); 
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1"); 
		StringWriter output = new StringWriter();
		Result result = new StreamResult(output);
		transformer.transform(xmlDomSource, result);
		
		Assertions.assertThat(output.toString()).containsIgnoringCase(get("abc").resolve(srcA.getFileName()).toString());
		Assertions.assertThat(output.toString()).containsIgnoringCase(get("abc").resolve(srcB.getFileName()).toString());
		Assertions.assertThat(Files.isRegularFile(destTar)).isTrue();
		
		Files.deleteIfExists(srcA);
		Files.deleteIfExists(srcB);
		Files.deleteIfExists(destTar);
	}

	@Test
	public void testGetProperty() throws TransformerException, ParserConfigurationException, SAXException, IOException {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><test></test>";
			
		String xsl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" xmlns:java=\"fr.argus.socle.util.XSLFunctions\" version=\"2.0\"> "
				+ "<xsl:template match=\"/\">"
				+ "<xsl:value-of select=\"java:getProperty('JDBC.host',())\" />"
				+ "</xsl:template>"
				+ "</xsl:stylesheet>";
     
		InputSource xmlSource = new InputSource(new StringReader(xml));
		StreamSource xslSource = new StreamSource(new ByteArrayInputStream(xsl.getBytes()));

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(false);
		DocumentBuilder docBuilder = dbf.newDocumentBuilder();
		org.w3c.dom.Document xfddDoc = docBuilder.parse(xmlSource);
		DOMSource xmlDomSource = new DOMSource(xfddDoc);
		
		TransformerFactory tFactory = new net.sf.saxon.TransformerFactoryImpl();
		Transformer transformer = tFactory.newTransformer(xslSource); 
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1"); 
		StringWriter output = new StringWriter();
		Result result = new StreamResult(output);
		transformer.transform(xmlDomSource, result);
		
		Assertions.assertThat(output.toString()).isEqualTo(Helper.getProperty("jdbc.host"));
	}
	
	@Test
	public void testGetConstant() throws TransformerException, ParserConfigurationException, SAXException, IOException {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><test></test>";
			
		String xsl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" xmlns:java=\"fr.argus.socle.util.XSLFunctions\" version=\"2.0\"> "
				+ "<xsl:template match=\"/\">"
				+ "<xsl:value-of select=\"java:getConstant('rePERTTOIRE_tmp_GENRATION_FICHIERS_IMG',())\" />"
				+ "</xsl:template>"
				+ "</xsl:stylesheet>";
     
		InputSource xmlSource = new InputSource(new StringReader(xml));
		StreamSource xslSource = new StreamSource(new ByteArrayInputStream(xsl.getBytes()));

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(false);
		DocumentBuilder docBuilder = dbf.newDocumentBuilder();
		org.w3c.dom.Document xfddDoc = docBuilder.parse(xmlSource);
		DOMSource xmlDomSource = new DOMSource(xfddDoc);
		
		TransformerFactory tFactory = new net.sf.saxon.TransformerFactoryImpl();
		Transformer transformer = tFactory.newTransformer(xslSource); 
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1"); 
		StringWriter output = new StringWriter();
		Result result = new StreamResult(output);
		transformer.transform(xmlDomSource, result);
		
		Assertions.assertThat(output.toString()).isEqualTo(Constant.REPERTTOIRE_TMP_GENRATION_FICHIERS_IMG);
	}
	
	@Test
	public void testSaveToFile() throws TransformerException, ParserConfigurationException, SAXException, IOException {
		Path parent = Files.createTempDirectory("test");
		Path file = parent.resolve("not").resolve("exsited").resolve("parent").resolve("file.txt");
		
		XSLFunctions.saveToFile("test", file.toString());
		
		Assertions.assertThat(Files.isRegularFile(file));
		FileUtils.deleteQuietly(parent.toFile());
	}
	
	@Test
	public void testMoveFile() throws TransformerException, ParserConfigurationException, SAXException, IOException {
		Path parent = Files.createTempDirectory("test");
		Path src = Files.createTempFile(parent, "test.", ".txt");
		Path dest = parent.resolve("not").resolve("exsited").resolve("parent").resolve("file.txt");
		
		XSLFunctions.moveFile(src.toString() , dest.toString());
		
		Assertions.assertThat(Files.isRegularFile(dest));
		FileUtils.deleteQuietly(parent.toFile());
	}
	
	@Test
	public void testCopyile() throws TransformerException, ParserConfigurationException, SAXException, IOException {
		Path parent = Files.createTempDirectory("test");
		Path src = Files.createTempFile(parent, "test.", ".txt");
		Path dest = parent.resolve("not").resolve("exsited").resolve("parent").resolve("file.txt");
		
		XSLFunctions.copyFile(src.toString() , dest.toString());
		
		Assertions.assertThat(Files.isRegularFile(dest));
		FileUtils.deleteQuietly(parent.toFile());
	}
	
	@Test
	public void testRunXslTransform() throws TransformerException, ParserConfigurationException, SAXException, IOException {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><test>test value</test>";
		String xsl2 = "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"2.0\">"
				+ "<xsl:template match=\"/\">"
				+ "		<xsl:value-of select=\"/test\" />"
				+ "</xsl:template>"
				+ "</xsl:stylesheet>";
		String xsl = "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" xmlns:java=\"fr.argus.socle.util.XSLFunctions\" version=\"2.0\" xmlns:saxon=\"http://saxon.sf.net/\"> "
				+ "<xsl:param name=\"map1\" />"
				+ "<xsl:output name=\"default\" indent=\"yes\"/>"
				+ "<xsl:template match=\"/\">"
				+ "<xsl:value-of select=\"java:runXslTransform(saxon:serialize(/, 'default'), '"+ escapeXml(xsl2)+"', ())\" />"
				+ "</xsl:template>"
				+ "</xsl:stylesheet>";
		
		InputSource xmlSource = new InputSource(new StringReader(xml));
		StreamSource xslSource = new StreamSource(new ByteArrayInputStream(xsl.getBytes()));

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(false);
		DocumentBuilder docBuilder = dbf.newDocumentBuilder();
		org.w3c.dom.Document xfddDoc = docBuilder.parse(xmlSource);
		DOMSource xmlDomSource = new DOMSource(xfddDoc);
		
		TransformerFactory tFactory = new net.sf.saxon.TransformerFactoryImpl();
		Transformer transformer = tFactory.newTransformer(xslSource); 
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1"); 
		
		Map<String, String> map =  ImmutableMap.of("k1", "v1", "k2", "v2");
		transformer.setParameter("map1", map);
		StringWriter output = new StringWriter();
		Result result = new StreamResult(output);
		transformer.transform(xmlDomSource, result);
		
		assertThat(output.toString()).endsWith("test value");
	}

	@Test
	public void testIsEAN13(){
		Assertions.assertThat(XSLFunctions.isEAN13("9958221202424")).isTrue();
		Assertions.assertThat(XSLFunctions.isEAN13("9958221202422")).isFalse();
	}
}
