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
public class Error {

	private final static QName _ERROR_QNAME = new QName("", "ERROR");

	@XmlElementDecl(namespace = "", name = "ERROR")
	public JAXBElement<String> createERROR(String value) {
		return new JAXBElement<String>(_ERROR_QNAME, String.class, null, value);
	}

}
