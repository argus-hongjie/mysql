package fr.argus.socle.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;

import fr.argus.socle.util.DateAdapter;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *       &lt;attribute name="nom" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="date_creation" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *       &lt;attribute name="taille" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="emplacement_archive" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="emplacement_temporaire" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="emplacement_dans_livraison" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="id_contenu" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "value" })
public class File {

	@XmlValue
	protected String value;
	@XmlAttribute(name = "nom")
	protected String nom;
	@XmlAttribute(name = "date_creation")
	@XmlSchemaType(name = "dateTime")
	@XmlJavaTypeAdapter(DateAdapter.class)
	protected Date dateCreation;
	@XmlAttribute(name = "taille")
	protected String taille;
	@XmlAttribute(name = "emplacement_archive")
	protected String emplacementArchive;
	@XmlAttribute(name = "emplacement_temporaire")
	protected String emplacementTemporaire;
	@XmlAttribute(name = "emplacement_dans_livraison")
	protected String emplacementDansLivraison;
	@XmlAttribute(name = "id_contenu")
	protected String idContenu;

	/**
	 * Obtient la valeur de la propri�t� value.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getValue() {
		return value;
	}

	/**
	 * D�finit la valeur de la propri�t� value.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Obtient la valeur de la propri�t� nom.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * D�finit la valeur de la propri�t� nom.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setNom(String value) {
		this.nom = value;
	}

	/**
	 * Obtient la valeur de la propri�t� dateCreation.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public Date getDateCreation() {
		return dateCreation;
	}

	/**
	 * D�finit la valeur de la propri�t� dateCreation.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setDateCreation(Date value) {
		this.dateCreation = value;
	}

	/**
	 * Obtient la valeur de la propri�t� taille.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTaille() {
		return taille;
	}

	/**
	 * D�finit la valeur de la propri�t� taille.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTaille(String value) {
		this.taille = value;
	}

	/**
	 * Obtient la valeur de la propri�t� emplacementArchive.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getEmplacementArchive() {
		return emplacementArchive;
	}

	/**
	 * D�finit la valeur de la propri�t� emplacementArchive.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setEmplacementArchive(String value) {
		this.emplacementArchive = value;
	}

	/**
	 * Obtient la valeur de la propri�t� emplacementTemporaire.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getEmplacementTemporaire() {
		return emplacementTemporaire;
	}

	/**
	 * D�finit la valeur de la propri�t� emplacementTemporaire.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setEmplacementTemporaire(String value) {
		this.emplacementTemporaire = value;
	}

	/**
	 * Obtient la valeur de la propri�t� emplacementDansLivraison.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getEmplacementDansLivraison() {
		return emplacementDansLivraison;
	}

	/**
	 * D�finit la valeur de la propri�t� emplacementDansLivraison.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setEmplacementDansLivraison(String value) {
		this.emplacementDansLivraison = value;
	}

	/**
	 * Obtient la valeur de la propri�t� idContenu.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getIdContenu() {
		return idContenu;
	}

	/**
	 * D�finit la valeur de la propri�t� idContenu.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setIdContenu(String value) {
		this.idContenu = value;
	}

}
