package chox.xmlValidation.rules;

import chox.Util.DocumentHelper;
import chox.xmlValidation.model.BordereauResult;
import chox.xmlValidation.model.ClaimResult;
import chox.xmlValidation.model.status.ClaimParseStatus;
import com.filesystemsoftware.utils.XMLUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.xpath.XPathExpressionException;
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
    
    
    /*
    public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException, DOMException, XPathExpressionException {
        
        String xmlFile = "C:/Project Workplace/Greefinch/Sherwood/testXML/DemoDataXMLTest-01.xml";
        
        try{

            BordereauResult parseResult = new BordereauResult();
            File file = new File(xmlFile);
            
            BordereauVersionValidattion ctrl = new BordereauVersionValidattion();
            ctrl.validate(file, file.getName(), parseResult);

        }catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        
    }
    */
    
    /*
    public ParseResult validate(File file, String fileName, ParseResult parseResult){
        
        try {
            
            SchemaFactory factory = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(new File(V_SCHEMA_FILE)));
            Validator validator = schema.newValidator();
            
            DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = parser.parse(file);
            validator.validate(new DOMSource(document));
            
        }catch (SAXParseException ex) {
            parseResult.setStatus(false);
            parseResult.addMessage("Parsing Error:" + ", Error Description: " + ex.getLocalizedMessage());
        } catch (SAXException e) {
            parseResult.setStatus(false);
            Exception x = e.getException();
            parseResult.addMessage("Parsing Error:" + ", Error Description: " + ((x == null) ? e : x).getLocalizedMessage());
        } catch (Throwable t) {
            parseResult.setStatus(false);
            parseResult.addMessage("Parsing Error:" + ", Error Description: " + t.getLocalizedMessage());
        }
        
        return parseResult;
    }
    */
    
    /*
    public ParseResult validate(File file, String fileName, ParseResult parseResult){
        
        try {
            
            Element xmlTemplate = getXMLTemplate();
            NodeList nodelist = xmlTemplate.getChildNodes();  
            Map xmlTemplateMap = getNodeName(nodelist);
            
            List<Element> elements = getRootList(file);
            
            for(Element e : elements){
                
                NodeList elementNodeList = e.getChildNodes();
                Map elementNodeListMap = getNodeName(elementNodeList);
                
                if(!elementNodeListMap.equals(xmlTemplateMap)){
                    parseResult.setStatus(false);
                    parseResult.addMessage(V_FILE_VERSION_ERROR);
                    break;
                }
            }
            
        } catch (SAXException e) {
            parseResult.setStatus(false);
            Exception x = e.getException();
            parseResult.addMessage("Parsing Error:" + ", Error Description: " + ((x == null) ? e : x).getLocalizedMessage());
        } catch (Throwable t) {
            parseResult.setStatus(false);
            parseResult.addMessage("Parsing Error:" + ", Error Description: " + t.getLocalizedMessage());
        }

        return parseResult;
    }
    
    public Element getXMLTemplate() throws ParserConfigurationException, SAXException, IOException, DOMException, DOMException, DOMException, XPathExpressionException{
        
        File templateFile = new File("C:/Project Workplace/Greefinch/Sherwood/testXML/Demo Data XML.xml");

        List<Element> elements = getRootList(templateFile);
        Element element = null;
        
        if (elements != null && elements.size()>0) {
            element = elements.get(0);
        }

        return element;
    }
    
    private List<Element> getRootList(File file) throws SAXException, IOException, ParserConfigurationException, DOMException, XPathExpressionException{
        
        List<Element> claims = new ArrayList<Element>();
        
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(file);

        doc.getDocumentElement().normalize();
        Element root = doc.getDocumentElement();

        doc = docBuilder.parse(file);

        doc.getDocumentElement().normalize();
        root = doc.getDocumentElement();

        if (root != null && root.getTagName().equals("chox")) {
            claims = XMLUtils.getElements(doc, root, "rental");
        }

        return claims;
    }
    
    private Map getNodeName(NodeList nodelist){
        
        Map map = new HashMap();
        
        for(int i = 0 ; i<nodelist.getLength() ; i++) {
            
            Node node = nodelist.item(i);
            
            if(node.getNodeType()==1){
                
                String nodeName = node.getNodeName();
                Map childMap = null;
                                
                if(node.hasChildNodes() && node.getChildNodes().getLength()>1){
                    childMap = getNodeName(node.getChildNodes()); 
                }
                
                map.put(nodeName, childMap);
            }
        }
        
        return map;
    }
    */

   
    
}
