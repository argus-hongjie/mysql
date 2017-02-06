package fr.argus.socle.ws.model;

import static fr.argus.socle.util.Helper.unmarshallFromObject;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>Classe Java pour anonymous complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="FILES">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="FILE" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="id_OCR" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="id_coupure" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="priority" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="modeApprovisionnement" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="bpIndex" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "files" })
@XmlRootElement(name = "TICKET")
public class TicketQuery implements Comparable<TicketQuery> {

	@XmlElement(name = "FILES", required = true)
    protected Files files;
    @XmlAttribute(name = "id_OCR")
    protected Integer idOCR;
    @XmlAttribute(name = "id_coupure")
    protected Integer idProduit;
    @XmlAttribute(name = "id")
    protected Integer id;
    @XmlAttribute(name = "priority")
    protected Integer priority;
    @XmlAttribute(name = "type")
    protected String type;
    @XmlAttribute(name = "modeApprovisionnement")
    protected String modeApprovisionnement;
    @XmlAttribute(name = "bpIndex")
    protected String bpIndex;
    @XmlAttribute(name = "idLivraison")
    protected String idLivraison;
    @XmlAttribute(name = "password")
    protected String password;
    @XmlAttribute(name = "idPere")
    protected Integer idPere;

	/**
	 * @return
	 */
	public String getContent() {		
		return unmarshallFromObject(this.getClass(),this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(TicketQuery o) {

		return o.getId() == this.getId() ? 0 : this.getPriority() - o.getPriority();
	}

}
