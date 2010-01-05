package idas.chox.service.xml.validations;

import idas.chox.core.util.XMLUtils;
import idas.chox.core.xmlValidation.BordereauResult;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.xml.transform.dom.DOMSource;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.springframework.core.io.ClassPathResource;

public class BordereauSchemaValidation {

    public static String W3C_XML_SCHEMA_NS_URI = "http://www.w3.org/2001/XMLSchema";
    public static String V_SCHEMA_ERROR = "Incorrect schema";

    private String schemaFile;

    public void validate(Document document, BordereauResult bordereauResult) {
        try {
           
            Element root = document.getDocumentElement();

            if (root != null && root.getTagName().equals("chox")) {

                List<Element> elements = XMLUtils.getElements(document, root, "rental");

                if (elements != null && elements.size() > 0) {
                    for (Element e : elements) {
                        if (!isValidSchema(e)) {
                            bordereauResult.setValid(false);
                            bordereauResult.addMessage(V_SCHEMA_ERROR);
                            return;
                        }
                    }
                }
            } else {
                bordereauResult.setValid(false);
                bordereauResult.addMessage(V_SCHEMA_ERROR);
            }

        } catch (Exception ex) {
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
