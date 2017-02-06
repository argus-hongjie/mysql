/**
 * 
 */
package fr.argus.socle.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.xerces.dom.ElementNSImpl;

public class NodeAdapter extends XmlAdapter<Object, Object>{

	public Object marshal(Object text) throws Exception {		
		 if (text == null) {
			   return null;
			  }
		 return W3CHelper.parseDocument("<VALEUR>" + ((String) text).trim() + "</VALEUR>").getDocumentElement();
	}

	public Object unmarshal(Object contentObj) {
		ElementNSImpl node = (ElementNSImpl) contentObj;
		return W3CHelper.nodeToString(node.getFirstChild()).trim();
	} 
}
