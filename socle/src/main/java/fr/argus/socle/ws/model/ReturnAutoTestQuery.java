/**
 * 
 */
package fr.argus.socle.ws.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * © @author Mongi MIRAOUI 12 mai 2016
 */

@XmlRegistry
public class ReturnAutoTestQuery {

	private final static QName _RETURN_AUTO_TEST_QNAME = new QName("", "RETURN_AUTO_TEST");

	@XmlElementDecl(namespace = "", name = "RETURN_AUTO_TEST")
	public JAXBElement<String> createReturnAutoTest(String value) {
		return new JAXBElement<String>(_RETURN_AUTO_TEST_QNAME, String.class, null, value);
	}

}
