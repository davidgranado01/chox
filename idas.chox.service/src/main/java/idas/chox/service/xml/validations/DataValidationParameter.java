package idas.chox.service.xml.validations;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.util.DocumentHelper;
import idas.chox.core.xmlValidation.NodeRuleModel;


public class DataValidationParameter {

    private static final Logger LOG = LoggerFactory.getLogger(DataValidationParameter.class);
    private String validateFile;
    private Document doc;
    private Map<String, NodeRuleModel> validationMap = new HashMap<String, NodeRuleModel>(170);


    private NodeRuleModel getDataValidationElement(String nodeName) throws IOException {
        if (validationMap.containsKey(nodeName)) {
            return validationMap.get(nodeName);
        }
        
        
        NodeRuleModel ruleModel = null;

        if (doc == null) {
            File file = new ClassPathResource(validateFile).getFile();
            try {
                LOG.debug("Reading data validation file....");
                doc = DocumentHelper.getDocumentFromFile(file);
                if (doc == null) {
                    LOG.error("Could not create document from file : {}", file.getAbsolutePath());
                    throw new IOException("Could not create document from file " + file.getAbsolutePath());
                }
            } catch (Exception ex) {
                LOG.error("Exception thrown in DataValidationParameter file while writing to document : {}", ex.getMessage());
                throw new IOException("Exception creating  document from file '" + file.getAbsolutePath() + "' : " + ex.getMessage(), ex);
            }
        }
        
        Node fieldNode = doc.getDocumentElement().getElementsByTagName(nodeName).item(0);
        
        if (fieldNode != null) {
            ruleModel = new NodeRuleModel();
            ruleModel.setNodeName(nodeName);
            ruleModel.setNodeDesc(fieldNode.getChildNodes().item(1).getTextContent());
            ruleModel.setDataType(fieldNode.getChildNodes().item(3).getTextContent());
            ruleModel.setNewClaimDataMandatory(fieldNode.getChildNodes().item(5).getTextContent());
            ruleModel.setInsurerInvoiceDataMandatory(fieldNode.getChildNodes().item(7).getTextContent());
            ruleModel.setExistingClaimDataMandatory(fieldNode.getChildNodes().item(9).getTextContent());
            ruleModel.setNewInvoiceDataMandatory(fieldNode.getChildNodes().item(11).getTextContent());
            ruleModel.setExistingInvoiceDataMandatory(fieldNode.getChildNodes().item(13).getTextContent());
            ruleModel.setTpiInterventionDataMandatory(fieldNode.getChildNodes().item(15).getTextContent());
            ruleModel.setOffHiredDataMandatory(fieldNode.getChildNodes().item(17).getTextContent());
            ruleModel.setHireMonitoringDataMandatory(fieldNode.getChildNodes().item(19).getTextContent());
            ruleModel.setNewSupplementaryInvoiceMandatory(fieldNode.getChildNodes().item(21).getTextContent());
            ruleModel.setNewSubscriberClaimDataMandatory(fieldNode.getChildNodes().item(23).getTextContent());
            ruleModel.setExistingSubscriberClaimDataMandatory(fieldNode.getChildNodes().item(25).getTextContent());
            ruleModel.setNewFixedFeeClaimDataMandatory(fieldNode.getChildNodes().item(27).getTextContent());
            ruleModel.setExistingFixedFeeClaimDataMandatory(fieldNode.getChildNodes().item(29).getTextContent());
            ruleModel.setLength(fieldNode.getChildNodes().item(31).getTextContent());
            ruleModel.setRegExp(fieldNode.getChildNodes().item(33).getTextContent());
            ruleModel.setInsurerClaimDataMandatory(fieldNode.getChildNodes().item(35).getTextContent());

            validationMap.put(nodeName, ruleModel);
            LOG.debug("Added validation for field: {}", nodeName);
        }
        return ruleModel;
    }

    public NodeRuleModel getValidationElementByField(String nodeName) {
        NodeRuleModel ruleModel = null;
        
        try {
            ruleModel = getDataValidationElement(nodeName);

        } catch (Exception ex) {
            LOG.error("Exception thrown getting field validation element '{}': ", nodeName, ex);
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
