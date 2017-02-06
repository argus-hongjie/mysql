/**
 * 
 */
//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.8-b130911.1802 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2017.01.16 à 09:53:33 AM CET 
//
package fr.argus.socle.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import static fr.argus.socle.util.Helper.unmarshallFromObject;

/**
 * @author mamadou.dansoko
 * Ticket construct for transport the original ticketQuery sons
 *
 * <p>Classe Java pour ticketType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="ticketType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idOCR" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idProduit" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="priority" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="modeApprovisionnement" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="bpindex" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idPere" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nomFichier" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="tailleFichier" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="cheminFichier" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dateDipo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
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
	    "idOCR",
	    "idProduit",
	    "id",
	    "priority",
	    "type",
	    "modeApprovisionnement",
	    "bpIndex",
	    "idPere",
	    "nomFichier",
	    "tailleFichier",
	    "cheminFichier",
	    "dateDipo",
	    "status"
	})
@XmlRootElement(name = "ticket")
public class Ticket {	   
    @XmlElement(name = "ocr",required = false)
    protected String idOCR;
    @XmlElement(name = "produit",required = false)
    protected String idProduit;
    @XmlElement(name = "id",required = true)
    protected String id;
    @XmlElement(name = "priority",required = false)
    protected String priority;
     @XmlElement(name = "type",required = false)
    protected String type;
     @XmlElement(name = "mode",required = false)
    protected String modeApprovisionnement;
     @XmlElement(name = "bpindex",required = false)
    protected String bpIndex;
     @XmlElement(name = "idpere",required = false)
    protected String idPere;
     @XmlElement(name = "nom",required = false)
    protected String nomFichier;
     @XmlElement(name = "taille",required = false)
    protected String tailleFichier;
     @XmlElement(name = "path",required = false)
    protected String cheminFichier;
    @XmlElement(name = "dispo",required = false)
    protected String dateDipo;
    @XmlElement(name = "status",required = false)
    protected String status;
   /**
	* Renvoi la transformation en format xml de l'objet
	* @return
	*/
    public String getContent() {		
 		return unmarshallFromObject(this.getClass(),this);
 	}
}
