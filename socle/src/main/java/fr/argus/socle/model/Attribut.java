package fr.argus.socle.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

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
	@XmlRootElement(name = "ATTRIBUT")
	public class Attribut {
		
		@XmlAttribute(name = "NOM")
		protected String name;
		
		@XmlAttribute(name = "VALEUR")
		protected String valeur;
		
		/**
		 * Renvoi la transformation en format xml de l'objet
		 * @return
		 */
		public String getContent() {		
			return Helper.unmarshallFromObject(this.getClass(),this);
		}
		

}
