/**
 * 
 */
package fr.argus.socle.util;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.inst2xsd.Inst2Xsd;
import org.apache.xmlbeans.impl.inst2xsd.Inst2XsdOptions;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument;
/**
 * @author mamadou.dansoko
 * 
 */
public class XsdGen {
	/**
	 *  
	 * @param inputFile
	 * @return
	 * @throws XmlException
	 * @throws IOException
	 */
    public SchemaDocument generateSchema(File inputFile) throws XmlException, IOException {
        // Only 1 instance is required for now
        XmlObject[] xmlInstances = new XmlObject[1];
        xmlInstances[0] = XmlObject.Factory.parse(inputFile);  
        return inst2xsd(xmlInstances);
    }
    /**
     * 
     * @param is
     * @return
     * @throws XmlException
     * @throws IOException
     */
    public SchemaDocument generateSchema(InputStream is) throws XmlException, IOException {       
        XmlObject[] xmlInstances = new XmlObject[1];
        xmlInstances[0] = XmlObject.Factory.parse(is);  
        return inst2xsd(xmlInstances);
    }
    /**
     * 
     * @param input
     * @return
     * @throws XmlException
     * @throws IOException
     */
    public SchemaDocument generateSchema(String input) throws XmlException, IOException {      
        XmlObject[] xmlInstances = new XmlObject[1];
        xmlInstances[0] = XmlObject.Factory.parse(input); 
        return inst2xsd(xmlInstances);
    }
    /**
     * 
     * @param xmlInstances
     * @return
     * @throws IOException
     */
    private SchemaDocument inst2xsd(XmlObject[] xmlInstances) throws IOException {
        Inst2XsdOptions inst2XsdOptions = new Inst2XsdOptions();
        inst2XsdOptions.setDesign(Inst2XsdOptions.DESIGN_VENETIAN_BLIND);
        SchemaDocument[] schemaDocuments = Inst2Xsd.inst2xsd(xmlInstances, inst2XsdOptions);
        if (schemaDocuments != null && schemaDocuments.length > 0) {
            return schemaDocuments[0];
        }
  
        return null;
    }
}
