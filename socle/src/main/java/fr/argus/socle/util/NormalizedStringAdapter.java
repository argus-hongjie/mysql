/**
 * 
 */
package fr.argus.socle.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author mamadou.dansoko
 *
 */
public class NormalizedStringAdapter extends XmlAdapter<String, String>{

	public String marshal(String text) throws Exception {		
		return text.trim();
	}

	public String unmarshal(String v) throws Exception {		
		return v.trim();
	}
}
