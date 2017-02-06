package fr.argus.socle.ws.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java pour anonymous complex type.
 * 
 * <p>
 * Le fragment de schéma suivant indique le contenu attendu figurant dans cette
 * classe.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="START" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="END" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DURATION" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="STATE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "start", "end", "duration", "state" })
@XmlRootElement(name = "RETURN_AUTO_TEST")
public class ReturnAutoTest {

	@XmlElement(name = "START", required = true)
	@XmlSchemaType(name = "dateTime")
	protected String start;
	@XmlElement(name = "END", required = true)
	@XmlSchemaType(name = "dateTime")
	protected String end;
	@XmlElement(name = "DURATION")
	protected Long duration;
	@XmlElement(name = "STATE", required = true)
	protected String state;

	/**
	 * Obtient la valeur de la propriété start.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSTART() {
		return start;
	}

	/**
	 * Définit la valeur de la propriété start.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setSTART(String value) {
		this.start = value;
	}

	/**
	 * Obtient la valeur de la propriété end.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getEND() {
		return end;
	}

	/**
	 * Définit la valeur de la propriété end.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setEND(String value) {
		this.end = value;
	}

	/**
	 * Obtient la valeur de la propriété duration.
	 * 
	 */
	public Long getDURATION() {
		return duration;
	}

	/**
	 * Définit la valeur de la propriété duration.
	 * 
	 */
	public void setDURATION(Long value) {
		this.duration = value;
	}

	/**
	 * Obtient la valeur de la propriété state.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSTATE() {
		return state;
	}

	/**
	 * Définit la valeur de la propriété state.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setSTATE(String value) {
		this.state = value;
	}

}
