package chox.xmlValidation.rules;

import chox.Util.DocumentHelper;
import chox.xmlValidation.result.ParseResult;
import chox.xmlValidation.xmlInterface.fileValidationInterface;
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


import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.apache.log4j.xml.SAXErrorHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

public class xmlVersionValidation implements fileValidationInterface{
    
    public static String W3C_XML_SCHEMA_NS_URI = "http://www.w3.org/2001/XMLSchema";
    public static String V_FILE_VERSION_ERROR = "Incorrect version";
    public static String V_SCHEMA_FILE = "C:/Project Workplace/Greefinch/Sherwood/testXML/xml-schema.xsd";
    private boolean status = true;
    
    public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException, DOMException, XPathExpressionException {
        
        String xmlFile = "C:/Project Workplace/Greefinch/Sherwood/testXML/DemoDataXMLTest-01.xml";
        
        try{

            ParseResult parseResult = new ParseResult();
            File file = new File(xmlFile);
            
            xmlVersionValidation ctrl = new xmlVersionValidation();
            ctrl.doValidate(file, file.getName(), parseResult);

        }catch (Exception ex) {
            
            System.out.println("<<<< NOT VALID >>>>");
            System.out.println(ex.getMessage());
            
        }

        
    }
    
    public ParseResult doValidate(File file, String fileName, ParseResult parseResult){
        
        try {
            
            List<Element> rentals = new ArrayList<Element>();
            
            DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = parser.parse(file);
            document.getDocumentElement().normalize();
            Element root = document.getDocumentElement();
        
            if (root != null && root.getTagName().equals("chox")) {
                
                rentals = XMLUtils.getElements(document, root, "rental");
                
                if(rentals!=null && rentals.size()>0){
                    
                    for(Element e : rentals){
                        System.out.println(">>> "+doSubValidation(e));
                    }

                }
            
            }else{
                parseResult.setStatus(false);
                parseResult.addMessage("Incorrect file");
            }
            
        } catch (Throwable t) {
            parseResult.setStatus(false);
            parseResult.addMessage("Parsing Error:" + ", Error Description: " + t.getLocalizedMessage());
        }
        
        return parseResult;
    }
    
    private boolean doSubValidation(Element element) throws SAXException, IOException{
        
        boolean bFlag = true;
        
        try{

            SchemaFactory factory = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(new File(V_SCHEMA_FILE)));
            Validator validator = schema.newValidator();
            validator.validate(new DOMSource(element));
            
        }catch (SAXParseException ex) {
            bFlag = false;
            System.out.println(ex.getMessage());
        }catch (SAXException ex) {
            bFlag = false;
            System.out.println(ex.getMessage());
        }
        
        return bFlag;
    }
    
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
    
    public String getRuleId() {
        return "F0001";
    }
    
}
