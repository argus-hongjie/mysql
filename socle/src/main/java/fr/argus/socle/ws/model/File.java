package fr.argus.socle.ws.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

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
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "value" })
public class File {

	@XmlValue
	protected String value;
	
	@XmlAttribute(name = "mode_appro")
	protected String modeApprovisionnement;

	@XmlAttribute(name = "date_mise_a_disposition")
	protected String dateMiseADisposition;
	
	@XmlAttribute(name = "taille")
	protected String tailleFichier;
	
	@XmlAttribute(name = "nom")
	protected String nomFichier;
	
	
	
	

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
	 * @return the modeApprovisionnement
	 */
	public String getModeApprovisionnement() {
		return modeApprovisionnement;
	}

	/**
	 * @param modeApprovisionnement the modeApprovisionnement to set
	 */
	public void setModeApprovisionnement(String modeApprovisionnement) {
		this.modeApprovisionnement = modeApprovisionnement;
	}

	/**
	 * @return the dateMiseADisposition
	 */
	public String getDateMiseADisposition() {
		return dateMiseADisposition;
	}

	/**
	 * @param dateMiseADisposition the dateMiseADisposition to set
	 */
	public void setDateMiseADisposition(String dateMiseADisposition) {
		this.dateMiseADisposition = dateMiseADisposition;
	}

	/**
	 * @return the tailleFichier
	 */
	public String getTailleFichier() {
		return tailleFichier;
	}

	/**
	 * @param tailleFichier the tailleFichier to set
	 */
	public void setTailleFichier(String tailleFichier) {
		this.tailleFichier = tailleFichier;
	}

	/**
	 * @return the nomFichier
	 */
	public String getNomFichier() {
		return nomFichier;
	}

	/**
	 * @param nomFichier the nomFichier to set
	 */
	public void setNomFichier(String nomFichier) {
		this.nomFichier = nomFichier;
	}
	
	

	
	
}
