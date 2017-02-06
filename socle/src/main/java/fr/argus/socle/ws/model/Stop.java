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
public class Stop {

	private final static QName _STOP_QNAME = new QName("", "STOP");

	@XmlElementDecl(namespace = "", name = "SOTP")
	public JAXBElement<String> createSTOP(String value) {
		return new JAXBElement<String>(_STOP_QNAME, String.class, null, value);
	}

}
