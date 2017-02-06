package fr.argus.socle.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import fr.argus.socle.util.Helper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name= "LIVRAISON")
@XmlType(name = "", propOrder = {
	    "attributs","fichiers","reception"
})
public class Livraison {

	@XmlAttribute(name = "id_livraison_numerique")
	protected Integer idLivraisonNumerique;
	
	@XmlAttribute(name = "id_pere")
	protected Integer idPere;
	
	@XmlAttribute(name = "id_source_appro")
	protected Integer idSourceAppro;
	
	@XmlAttribute(name = "bpIndex")
	protected String bpIndex;
	
	@XmlAttribute(name = "clef_index_pivot")
	protected String clefIndexPivot;
	
	@XmlElement(name = "RECEPTION")
	protected Reception reception;
	
	@XmlElement(name = "FICHIERS", required = true)
    protected Fichiers fichiers;
	
	
	@XmlElement(name = "ATTRIBUTS")
	protected Attributs attributs;

	/**
	 * Renvoi la transformation en format xml de l'objet
	 * @return
	 */
	public String getContent() {		
		return Helper.unmarshallFromObject(this.getClass(),this);
	}
	
	
}
