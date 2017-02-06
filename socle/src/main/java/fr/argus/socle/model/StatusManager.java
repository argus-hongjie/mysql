//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.8-b130911.1802 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2017.01.20 à 10:15:08 AM CET 
//
/**
 * 
 */
package fr.argus.socle.model;


import static fr.argus.socle.util.Helper.unmarshallFromObject;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mamadou.dansoko
 *
 * <p>Classe Java pour status_managerType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="status_managerType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="bpindex" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="module_name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idPere" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "id",
    "bpindex",
    "status",
    "moduleName",
    "idPere"
})
@XmlRootElement(name = "status_manager")
public class StatusManager {
	@XmlElement(required = true)
    protected String id;
    @XmlElement(required = true)
    protected String bpindex;
    @XmlElement(required = true)
    protected String status;
    @XmlElement(name = "module_name", required = true)
    protected String moduleName;
    @XmlElement(name = "idPere")
    private String idPere;
    
    /**
   	* Renvoi la transformation en format xml de l'objet
   	* @return
   	*/
    public String getContent() {		
		return unmarshallFromObject(this.getClass(),this);
	}
}
