package idas.chox.service.xml.validations;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.transform.dom.DOMSource;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.springframework.core.io.ClassPathResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.util.XMLUtils;
import idas.chox.core.xmlValidation.BordereauResult;

public class BordereauSchemaValidation {
    private static final Logger LOG = LoggerFactory.getLogger(BordereauSchemaValidation.class);
    private static final String CURRENT_MACROVERSION = "2.5";

    public static String W3C_XML_SCHEMA_NS_URI = "http://www.w3.org/2001/XMLSchema";
    public static String V_SCHEMA_ERROR = "Incorrect schema";
    public static String V_MACRO_VERSION_ERROR = "Incorrect macro version";

    private String schemaFile;

    public void validate(Document document, BordereauResult bordereauResult) {
        try {
           
            Element root = document.getDocumentElement();

            if (root != null && root.getTagName().equals("chox")) {

              String macroversion = XMLUtils.getElementValue(root, "macroversion");
              if (macroversion == null) {
                  bordereauResult.setValid(false);
                  bordereauResult.addMessage(V_MACRO_VERSION_ERROR + ": no macro version defined");
              }
              else if (!macroversion.equals(CURRENT_MACROVERSION)) {
                  bordereauResult.setValid(false);
                  bordereauResult.addMessage(V_MACRO_VERSION_ERROR + ": expecting version " + CURRENT_MACROVERSION + " but found " + macroversion + ".");
                  bordereauResult.addMessage("Please contact support.");
              }
              else {
                List<Element> elements = XMLUtils.getElements(document, root, "rental");

                if (elements != null && elements.size() > 0) {
                    for (Element e : elements) {
                        LOG.debug("Validating schema element: {}", e.getNodeName());
                        if (!isValidSchema(e)) {
                            LOG.debug("Element not valid: {}={}", e.getNodeName(), e.getNodeValue());
                            bordereauResult.setValid(false);
                            bordereauResult.addMessage(V_SCHEMA_ERROR);
                            return;
                        }
                    }
                }
              }
            } else {
                bordereauResult.setValid(false);
                bordereauResult.addMessage(V_SCHEMA_ERROR);
            }

        } catch (Exception ex) {
            LOG.debug("Parse error: {}", ex.getLocalizedMessage());
            bordereauResult.setValid(false);
            bordereauResult.addMessage("Parsing Error:" + ", Error Description: " + ex.getLocalizedMessage());
        }
    }

    private boolean isValidSchema(Element element) throws SAXException, IOException {

        boolean bFlag = true;

        try {
            File xmlValidationTemplate = new ClassPathResource(schemaFile).getFile();
            SchemaFactory factory = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(xmlValidationTemplate));
            Validator validator = schema.newValidator();
            validator.validate(new DOMSource(element));
        } catch (Exception ex) {
            LOG.debug("Exception thrown validating schema: {}", ex.getMessage());
            bFlag = false;
        }
        return bFlag;
    }

    /**
     * @param schemaFile the schemaFile to set
     */
    public void setSchemaFile(String schemaFile) {
        this.schemaFile = schemaFile;
    }
}
