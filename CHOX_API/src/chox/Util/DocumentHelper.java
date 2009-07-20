/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.Util;

import org.w3c.dom.*;
import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

public class DocumentHelper {
    
    public static Document getDocumentFromFile(File file){
        
        Document doc = null;
            
        try {
            
            if(file.isFile()){
                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
                docBuilderFactory.setNamespaceAware(true); 
                DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                doc = docBuilder.parse(file);
                doc.getDocumentElement().normalize();
            }
            
        } catch (Throwable t) {
            t.printStackTrace();
        }
        
        return doc;
    }
    
}
