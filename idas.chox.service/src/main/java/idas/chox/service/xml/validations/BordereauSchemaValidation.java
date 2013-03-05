package idas.chox.service.xml.validations;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import idas.chox.core.model.Bordereau;
import idas.chox.core.model.WebUser;
import idas.chox.core.util.XMLUtils;

public class BordereauSchemaValidation {
    private static final Logger LOG = LoggerFactory.getLogger(BordereauSchemaValidation.class);
    private static final String[] VALID_MACROVERSIONS = {"2.8","2.9"};
    public static String W3C_XML_SCHEMA_NS_URI = "http://www.w3.org/2001/XMLSchema";
    public static String V_SCHEMA_ERROR = "Incorrect schema";
    public static String V_MACRO_VERSION_ERROR = "Incorrect macro version";

    private String schemaFile;

    public void validate(Document document, Bordereau bordereau, WebUser currentUser) {
        try {
           
            Element root = document.getDocumentElement();

            if (root != null && root.getTagName().equals("chox")) {

              String macroversion = XMLUtils.getElementValue(root, "macroversion");
              if (macroversion == null) {
                  bordereau.setValid(false);
                  bordereau.setMessage(V_MACRO_VERSION_ERROR + ": no macro version defined. ");
              }
              else if (!Arrays.asList(VALID_MACROVERSIONS).contains(macroversion)) {
                  bordereau.setValid(false);
                  bordereau.setMessage(V_MACRO_VERSION_ERROR + ": valid versions are : " + Arrays.toString(VALID_MACROVERSIONS) + " but found " + macroversion + ". Please contact support.");
              }
              else {
                List<Element> elements = XMLUtils.getElements(document, root, "rental");

                if (elements != null && elements.size() > 0) {
                    for (Element e : elements) {
                        LOG.debug("Validating schema element: {}", e.getNodeName());
                        if (!isValidSchema(e)) {
                            LOG.debug("Element not valid: {}={}", e.getNodeName(), e.getNodeValue());
                            bordereau.setValid(false);
                            bordereau.setMessage(V_SCHEMA_ERROR);
                            return;
                        }
                    }
                }
              }
            } else {
                bordereau.setValid(false);
                bordereau.setMessage(V_SCHEMA_ERROR);
            }

        } catch (Exception ex) {
            LOG.debug("Parse error: {}", ex.getLocalizedMessage());
            bordereau.setValid(false);
            bordereau.setMessage("Parsing Error:" + ", Error Description: " + ex.getLocalizedMessage());
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
            LOG.error("Exception thrown validating schema: {}", ex.getMessage());
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
