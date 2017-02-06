package fr.argus.socle.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import fr.argus.socle.util.Helper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * Classe Java pour anonymous complex type.
 * 
 * <p>
 * Le fragment de sch�ma suivant indique le contenu attendu figurant dans cette
 * classe.
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
 *                   &lt;element name="FILE" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;simpleContent>
 *                         &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                           &lt;attribute name="nom" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="date_creation" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *                           &lt;attribute name="taille" type="{http://www.w3.org/2001/XMLSchema}string" /
 *                           &lt;attribute name="emplacement_archive" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="emplacement_temporaire" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="emplacement_dans_livraison" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="id_contenu" type="{http://www.w3.org/2001/XMLSchema}string" />
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
 *       &lt;attribute name="date_parution" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *       &lt;attribute name="date_faciale" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *       &lt;attribute name="date_reception" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *       &lt;attribute name="date_mise_a_dispo" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="mode" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="mode_approvisionnement" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="taille" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="bpIndex" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="cheminLivraisonHash" type="{http://www.w3.org/2001/XMLSchema}string" />
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
@XmlType(name = "", propOrder = { "attributs","files" })
@XmlRootElement(name = "RECEPTION")
public class Reception {
	@XmlElement(name = "ATTRIBUTS")
	protected Attributs attributs;
	@XmlElement(name = "FILES", required = true)
	protected Files files;
	@XmlAttribute(name = "nom")
	protected String nom;
	@XmlAttribute(name = "date_parution")
	protected String dateParution;
	@XmlAttribute(name = "date_faciale")
	protected String dateFaciale;
	@XmlAttribute(name = "date_reception")
	protected String dateReception;
	@XmlAttribute(name = "date_mise_a_dispo")
	protected String dateMiseADispo;
	@XmlAttribute(name = "type")
	protected String type;
	@XmlAttribute(name = "mode")
	protected String mode;
	@XmlAttribute(name = "mode_approvisionnement")
	protected String modeApprovisionnement;
	@XmlAttribute(name = "taille")
	protected String taille;
	@XmlAttribute(name = "bpIndex")
    protected String bpIndex;
	@XmlAttribute(name = "cheminLivraisonHash")
	protected String cheminLivraisonHash;
	@XmlAttribute(name = "cheminLivraisonArchive")
	protected String cheminLivraisonArchive;
	@XmlTransient
	protected Integer id;

	/**
	 * Obtient la valeur de la propri�t� files.
	 * 
	 * @return possible object is {@link Reception.Files }
	 * 
	 */
	public Files getFILES() {
		return files;
	}

	/**
	 * D�finit la valeur de la propri�t� files.
	 * 
	 * @param value
	 *            allowed object is {@link Reception.Files }
	 * 
	 */
	public void setFILES(Files value) {
		this.files = value;
	}

	/**
	 * Renvoi la transformation en format xml de l'objet
	 * @return
	 */
	public String getContent() {		
		return Helper.unmarshallFromObject(this.getClass(),this);
	}

}
