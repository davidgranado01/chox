/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.xmlValidation.rules;

import chox.xmlValidation.result.ParseResult;
import com.filesystemsoftware.utils.XMLUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.String;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
/**
 *
 * @author Carlson
 */
public class xmlVersionValidation {
    
    public static String V_FILE_VERSION_ERROR = "Incorrect version";
    
    public ParseResult validate(File file, String fileName, ParseResult parseResult) throws FileNotFoundException, IOException, SAXException {
    
        boolean bFlag = true;
        
        try {
            
            Element xmlTemplate = getXMLTemplate();
            NodeList nodelist = xmlTemplate.getChildNodes();  
            Map xmlTemplateMap = getNodeName(nodelist);
            
            List<Element> elements = getRootList(file);
            
            for(Element e : elements){
                
                NodeList elementNodeList = e.getChildNodes();
                Map elementNodeListMap = getNodeName(elementNodeList);
                
                if(!elementNodeListMap.equals(xmlTemplateMap)){
                    bFlag = false;
                    
                    if(parseResult.getMessage()!=null){
                        parseResult.getMessage().add(V_FILE_VERSION_ERROR);
                    }else{
                        List<String> errMsg = new ArrayList<String>();
                        errMsg.add(V_FILE_VERSION_ERROR);
                        parseResult.setMessage(errMsg);
                    }
                    
                    break;
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
        
        parseResult.setStatus(bFlag);
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
    
}
