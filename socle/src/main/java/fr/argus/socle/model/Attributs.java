package fr.argus.socle.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
	    "attribut"
	})
@XmlRootElement(name = "ATTRIBUTS")
public class Attributs {
	
	@XmlElement(name = "ATTRIBUT")
	protected List<Attribut> attribut;
	


	public List<Attribut> getAttributs() {
		if(attribut == null){
			attribut = new ArrayList<Attribut>();
		}
		return attribut;
	}



	public void setAttributs(List<Attribut> attribut) {
		this.attribut = attribut;
	}




}
