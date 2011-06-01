package idas.chox.service.xml.validations;

import idas.chox.core.xmlValidation.*;
import idas.chox.core.util.DocumentHelper;
import java.io.File;
import java.io.IOException;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataValidationParameter {

    private static final Logger LOG = LoggerFactory.getLogger(DataValidationParameter.class);
    private String validateFile;

    private Element getDataValidationRootElement() throws IOException {

        File file = new ClassPathResource(validateFile).getFile();
        Document doc = null;
        try {
            doc = DocumentHelper.getDocumentFromFile(file);
            if (doc == null) {
                LOG.error("Could not create document from file : {}", file.getAbsolutePath());
                throw new IOException("Could not create document from file " + file.getAbsolutePath());
            }
        } catch (Exception ex) {
            LOG.error("Exception thrown in DataValidationParameter file while writing to document : {}", ex.getMessage());
            throw new IOException("Exception creating  document from file '" + file.getAbsolutePath() + "' : " + ex.getMessage());
        }
        return doc.getDocumentElement();
    }

    public NodeRuleModel getValidationElementByField(String nodeName) {

        NodeRuleModel ruleModel = new NodeRuleModel();

        try {

            Element rootElement = getDataValidationRootElement();
            Node fieldNode = rootElement.getElementsByTagName(nodeName).item(0);
            ruleModel.setNodeName(nodeName);
            ruleModel.setNodeDesc(fieldNode.getChildNodes().item(1).getTextContent());
            LOG.debug("total childs {}", fieldNode.getChildNodes().getLength());
            LOG.debug("item (1) {}", fieldNode.getChildNodes().item(1).getTextContent());
            ruleModel.setDataType(fieldNode.getChildNodes().item(3).getTextContent());
            LOG.debug("item 3 {}", fieldNode.getChildNodes().item(3).getTextContent());
            ruleModel.setNewClaimDataMandatory(fieldNode.getChildNodes().item(5).getTextContent());
            LOG.debug("item 5 : {}", fieldNode.getChildNodes().item(5).getTextContent());
            ruleModel.setExistingClaimDataMandatory(fieldNode.getChildNodes().item(7).getTextContent());
            LOG.debug("item 7: {}", fieldNode.getChildNodes().item(7).getTextContent());
            ruleModel.setNewInvoiceDataMandatory(fieldNode.getChildNodes().item(9).getTextContent());
            LOG.debug("item 9: {}", fieldNode.getChildNodes().item(9).getTextContent());
            ruleModel.setExistingInvoiceDataMandatory(fieldNode.getChildNodes().item(11).getTextContent());
            LOG.debug("item 11: {}", fieldNode.getChildNodes().item(11).getTextContent());
            ruleModel.setTpiInterventionDataMandatory(fieldNode.getChildNodes().item(13).getTextContent());
            LOG.debug("item 13: {}", fieldNode.getChildNodes().item(13).getTextContent());
            ruleModel.setHireMoniteringDataMandatory(fieldNode.getChildNodes().item(15).getTextContent());
            LOG.debug("item 15: {}", fieldNode.getChildNodes().item(15).getTextContent());
            ruleModel.setLength(fieldNode.getChildNodes().item(17).getTextContent());
            LOG.debug("item 17: {}", fieldNode.getChildNodes().item(17).getTextContent());
            ruleModel.setRegExp(fieldNode.getChildNodes().item(19).getTextContent());
            LOG.debug("item 19: {}", fieldNode.getChildNodes().item(19).getTextContent());

        } catch (Exception ex) {
            LOG.debug("Exception thrown getting field validation element '{}': {}", nodeName, ex.getMessage());
        }

        return ruleModel;
    }

    /**
     * @param validateFile the validateFile to set
     */
    public void setValidateFile(String validateFile) {
        this.validateFile = validateFile;
    }
}
