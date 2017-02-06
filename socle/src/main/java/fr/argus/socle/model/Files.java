package fr.argus.socle.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

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
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="FILE" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="nom" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="date_creation" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *                 &lt;attribute name="taille" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="emplacement_archive" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="emplacement_temporaire" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="emplacement_dans_livraison" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="id_contenu" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "file" })
public class Files {

	@XmlElement(name = "FILE")
	protected List<File> file;

	/**
	 * Gets the value of the file property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the file property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getFILE().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link Reception.Files.File }
	 * 
	 * 
	 */
	public List<File> getFILE() {
		if (file == null) {
			file = new ArrayList<File>();
		}
		return this.file;
	}

}