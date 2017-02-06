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
public class TicketResponse {

	private final static QName _TICKET_QNAME = new QName("", "TICKET");

	@XmlElementDecl(namespace = "", name = "TICKET")
	public JAXBElement<String> createTICKET(String value) {
		return new JAXBElement<String>(_TICKET_QNAME, String.class, null, value);
	}

}
