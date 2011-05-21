package idas.chox.core.util;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.*;
import java.io.File;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;

public class DocumentHelper {
    
    public static Document getDocumentFromFile(File file) throws ParserConfigurationException, SAXException, IOException {

        Document doc = null;

            if (file.isFile()) {
                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
                docBuilderFactory.setNamespaceAware(true);
                DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                doc = docBuilder.parse(file);
                doc.getDocumentElement().normalize();
            } 

            return doc;
        }

    public static Document getDocumentFromStream(InputStream stream) throws ParserConfigurationException, SAXException, IOException {

        Document doc = null;
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        docBuilderFactory.setNamespaceAware(true);
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        doc = docBuilder.parse(stream);
        doc.getDocumentElement().normalize();
        return doc;
    }

    
}
