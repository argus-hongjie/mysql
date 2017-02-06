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
public class Alive {

	private final static QName _ALIVE_QNAME = new QName("", "ALIVE");

	@XmlElementDecl(namespace = "", name = "ALIVE")
	public JAXBElement<String> createALIVE(String value) {
		return new JAXBElement<String>(_ALIVE_QNAME, String.class, null, value);
	}

}
