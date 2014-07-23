package idas.chox.core.util;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.*;
import java.io.File;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public class DocumentHelper {
    private static final Logger LOG = LoggerFactory.getLogger(DocumentHelper.class);
    
    public static Document getDocumentFromFile(File file) throws ParserConfigurationException, SAXException, IOException {

        Document doc = null;

        if (file.isFile()) {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            try {
                dbf.setNamespaceAware(true);
                LOG.debug("Disabling XXE Processing in toDocument(File)....");
                dbf.setExpandEntityReferences(false);
                // Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-general-entities
                // Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-general-entities
                String FEATURE = "http://xml.org/sax/features/external-general-entities";
                dbf.setFeature(FEATURE, false);

                // Xerces 2 only - http://xerces.apache.org/xerces2-j/features.html#disallow-doctype-decl
                FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
                dbf.setFeature(FEATURE, true);
                DocumentBuilder db = dbf.newDocumentBuilder();
                doc = db.parse(file);
                doc.getDocumentElement().normalize();
            } catch (ParserConfigurationException e) {
                // This should catch a failed setFeature feature
                LOG.error("ParserConfigurationException was thrown. The feature is probably not supported by your XML processor: {}\n", e.getMessage(), e);
                throw e;
            } catch (SAXException e) {
                // On Apache, this should be thrown when disallowing DOCTYPE
                LOG.error("A DOCTYPE was passed into the XML document");
                throw e;
            } catch (IOException e) {
                // XXE that points to a file that doesn't exist
                LOG.error("IOException occurred, XXE may still possible: " + e.getMessage());
                throw e;
            }
        }

        return doc;
    }

    public static Document getDocumentFromStream(InputStream stream) throws ParserConfigurationException, SAXException, IOException {

        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            dbf.setNamespaceAware(true);
            LOG.info("Disabling XXE Processing in toDocument(File)....");
            dbf.setExpandEntityReferences(false);
            // Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-general-entities
            // Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-general-entities
            String FEATURE = "http://xml.org/sax/features/external-general-entities";
            dbf.setFeature(FEATURE, false);

            // Xerces 2 only - http://xerces.apache.org/xerces2-j/features.html#disallow-doctype-decl
            FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
            dbf.setFeature(FEATURE, true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(stream);
            doc.getDocumentElement().normalize();
        } catch (ParserConfigurationException e) {
            // This should catch a failed setFeature feature
            LOG.error("ParserConfigurationException was thrown. The feature is probably not supported by your XML processor: {}\n", e.getMessage(), e);
            throw e;
        } catch (SAXException e) {
            // On Apache, this should be thrown when disallowing DOCTYPE
            LOG.error("A DOCTYPE was passed into the XML document");
            throw e;
        } catch (IOException e) {
            // XXE that points to a file that doesn't exist
            LOG.error("IOException occurred, XXE may still possible: " + e.getMessage());
            throw e;
        }

        return doc;
    }
    
}
