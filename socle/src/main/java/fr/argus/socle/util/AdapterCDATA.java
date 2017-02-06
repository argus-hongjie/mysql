/**
 * 
 */
package fr.argus.socle.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;
/**
 * @author mamadou.dansoko
 *
 */
public class AdapterCDATA extends XmlAdapter<String, String>{

	@Override
	public String marshal(String v) throws Exception {
		return "<![CDATA[" + v + "]]>";
	}

	@Override
	public String unmarshal(String v) throws Exception {
		return v;
	}

}
