package chox.xmlValidation.rules;

import chox.Util.DocumentHelper;
import chox.xmlValidation.model.BordereauResult;
import chox.xmlValidation.model.ClaimResult;
import chox.xmlValidation.model.status.ClaimParseStatus;
import com.filesystemsoftware.utils.XMLUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.transform.dom.DOMSource;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.apache.struts2.ServletActionContext;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.SAXParseException;

public class BordereauVersionValidation{
    
    public static String W3C_XML_SCHEMA_NS_URI = "http://www.w3.org/2001/XMLSchema";
    public static String V_FILE_VERSION_ERROR = "Incorrect version";
    public static String V_SCHEMA_FILE = "xml-schema.xsd";
    
    public BordereauResult validate(
            File file, 
            String fileName, 
            BordereauResult bordereauResult){
        
        try {
            
            Document document = DocumentHelper.getDocumentFromFile(file);
            Element root = document.getDocumentElement();
            List<ClaimResult> claimElements = new ArrayList<ClaimResult>();
            
            if (root != null && root.getTagName().equals("chox")) {
                
                List<Element> rentals = XMLUtils.getElements(document, root, "rental");
                
                if(rentals!=null && rentals.size()>0){
                    
                    for(Element e : rentals){
                        
                        ClaimResult claimResult = new ClaimResult();
                        claimResult.setCheckDataValid(true);
                        claimResult.setDataValid(true);
                        claimResult.setValid(true);
                        
                        boolean isvalid = true;
                        
                        claimResult.setElement(e);
                        
                        if(!doElementValidation(e)){
                            isvalid = false;
                            claimResult.setClaimParseStatus(ClaimParseStatus.invalidSchema);
                        }
                        
                        claimResult.setValid(isvalid);
                        claimElements.add(claimResult);
                    }
                    
                }

            }else{
                bordereauResult.setValid(false);
                bordereauResult.addMessage(V_FILE_VERSION_ERROR);
            }
            bordereauResult.setClaimResult(claimElements);
            
        } catch (Throwable t) {
            bordereauResult.setValid(false);
            bordereauResult.addMessage("Parsing Error:" + ", Error Description: " + t.getLocalizedMessage());
        }
        
        return bordereauResult;
    }
    
    private boolean doElementValidation(Element element) throws SAXException, IOException{
        
        boolean bFlag = true;
        
        try{
            
            // String xmlValidationTemplate = "C:/Project Workplace/Greefinch/Sherwood/choxidas/trunk/CHOX_WEB/web/excelTemplate/"+V_SCHEMA_FILE;
            // this.getClass().getResource();
            String xmlValidationTemplate = ServletActionContext.getServletContext().getRealPath("/excelTemplate/" + V_SCHEMA_FILE);
            
            SchemaFactory factory = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(new File(xmlValidationTemplate)));
            Validator validator = schema.newValidator();
            validator.validate(new DOMSource(element));
            
        }catch (SAXParseException ex) {
            bFlag = false;
        }catch (SAXException ex) {
            bFlag = false;
        }
        
        return bFlag;
    }
   
    
}
