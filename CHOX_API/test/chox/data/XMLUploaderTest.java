/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.data;

import org.junit.Test;
import java.io.File;
import org.w3c.dom.*;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import com.filesystemsoftware.utils.XMLUtils;
import com.filesystemsoftware.utils.Logger;
import chox.model.*;
import chox.services.XmlProcessController;

public class XMLUploaderTest {
    
    public XMLUploaderTest(){
    }
    
    @Test
    public void XMLUploader() {
    
        try {
            
            // TEST CLAIM
            String sXMLPath1 = "C:/Users/Carlson/Desktop/TESTCASEFILE/TEST_CASE_CLAIM1.xml";
            String sUpdateType = "C";
            Boolean isAllowPartialUpload = true;

            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File(sXMLPath1));

            doc.getDocumentElement().normalize();
            Element root = doc.getDocumentElement();
            
            // TEST INVOICE            
            //String sXMLPath2 = "C:/Project Workplace/Greefinch/choxida/CHOX_STAGE_2_SUBMISSION.xml";
            String sXMLPath2 = "C:/Users/Carlson/Desktop/CHOX/Bord Test 9.xml";
            sUpdateType = "A";
            doc = docBuilder.parse(new File(sXMLPath2));

            doc.getDocumentElement().normalize();
            root = doc.getDocumentElement();
            
            if (root != null && root.getTagName().equals("chox")) {

                ArrayList<XMLParseResult> xmlParseResults = new ArrayList<XMLParseResult>();
                ArrayList<Element> rentalElements = XMLUtils.getElements(doc, root, "rental");

                int count = 0;
                for (Element re : rentalElements) {

                    try {
                        count++;
                        /*
                        XmlProcessController thisCtrl = new XmlProcessController();
                        XMLParseResult xmlParseResult = new XMLParseResult();
                        xmlParseResult = thisCtrl.xmlSchemaValidateProcess(xmlParseResult, doc, re, sUpdateType, isAllowPartialUpload);
                        xmlParseResults.add(xmlParseResult);
                        */
                    } catch (Exception e) {
                        Logger.err.println("Error loading record " + count);
                        throw e;
                    }
                }
            }
            

        } catch (SAXParseException err) {
            System.out.println("** Parsing error" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId());
            System.out.println(" " + err.getMessage());
        } catch (SAXException e) {
            Exception x = e.getException();
            ((x == null) ? e : x).printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
