/**
 * 
 */
package fr.argus.socle.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import fr.argus.socle.util.DateAdapter;
import fr.argus.socle.util.Helper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @author mamadou.dansoko
 *
 */
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
 *         &lt;element name="CONTENU">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="PAGE">
 *                     &lt;complexType>
 *                       &lt;simpleContent>
 *                         &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                           &lt;attribute name="numero" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                         &lt;/extension>
 *                       &lt;/simpleContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="nom" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="id_livraison" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
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
@XmlType(name = "", propOrder = {
    "contenu"
})
@XmlRootElement(name = "FICHIER")
public class Fichier {

    @XmlElement(name = "CONTENU", required = true)
    protected Contenu contenu;
    @XmlAttribute(name = "nom")
    protected String nom;
    @XmlAttribute(name = "id_livraison_contenu")
    protected String idLivraison;
    @XmlAttribute(name = "type")
    protected String type;
	@XmlAttribute(name = "date_creation")
	@XmlSchemaType(name = "dateTime")
	@XmlJavaTypeAdapter(DateAdapter.class)
	protected Date dateCreation;
	@XmlAttribute(name = "taille")
	protected String taille;
	@XmlAttribute(name = "type_contenu")
	protected String typeContenu;
	@XmlAttribute(name = "status")
	protected String status;

    /**
     * Obtient la valeur de la propriété contenu.
     * 
     * @return
     *     possible object is
     *     {@link Contenu }
     *     
     */
    public Contenu getCONTENU() {
        return contenu;
    }

    /**
     * Définit la valeur de la propriété contenu.
     * 
     * @param value
     *     allowed object is
     *     {@link Contenu }
     *     
     */
    public void setCONTENU(Contenu value) {
        this.contenu = value;
    }

	/**
     * Renvoie la transfoamtion en formation xml de l'objet
     * @return
     */
    public String getContent() {		
		return Helper.unmarshallFromObject(this.getClass(),this);
	}
}
