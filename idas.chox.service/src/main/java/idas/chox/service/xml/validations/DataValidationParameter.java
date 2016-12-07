package idas.chox.service.xml.validations;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.util.DocumentHelper;
import idas.chox.core.util.XMLUtils;
import idas.chox.core.util.XmlHelper;
import idas.chox.core.xmlValidation.NodeRuleModel;


public class DataValidationParameter {

    private static final Logger LOG = LoggerFactory.getLogger(DataValidationParameter.class);
    private String validateFile;
    private Document doc;
    private final Map<String, NodeRuleModel> validationMap = new HashMap<>(170);


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
        
        Element fieldElement = XMLUtils.getElement(doc.getDocumentElement(), nodeName);
        
        if (fieldElement != null) {
            ruleModel = new NodeRuleModel();
            ruleModel.setNodeName(nodeName);
            if (fieldElement.getElementsByTagName("data-description") != null) {
                ruleModel.setNodeDesc(XmlHelper.getNodeValue(fieldElement, "data-description"));
            }
            
            if (fieldElement.getElementsByTagName("data-type") != null) {
                ruleModel.setDataType(XmlHelper.getNodeValue(fieldElement, "data-type"));
            }
            
            if (fieldElement.getElementsByTagName("data-length") != null) {
                ruleModel.setLength(XmlHelper.getNodeValue(fieldElement, "data-length"));
            }
            
            if (fieldElement.getElementsByTagName("reg-exp") != null) {
                ruleModel.setRegExp(XmlHelper.getNodeValue(fieldElement, "reg-exp"));
            }
            
            if (fieldElement.getElementsByTagName("data-mandatory-newClaim") != null) {
                ruleModel.setNewClaimDataMandatory(XmlHelper.getNodeValue(fieldElement, "data-mandatory-newClaim"));
            }
            
            if (fieldElement.getElementsByTagName("data-mandatory-newCollaborationClaim") != null) {
                ruleModel.setNewCollaborationClaimDataMandatory(XmlHelper.getNodeValue(fieldElement, "data-mandatory-newCollaborationClaim"));
            }
            
            if (fieldElement.getElementsByTagName("data-mandatory-newInsurerInvoice") != null) {
                ruleModel.setInsurerInvoiceDataMandatory(XmlHelper.getNodeValue(fieldElement, "data-mandatory-newInsurerInvoice"));
            }
            
            if (fieldElement.getElementsByTagName("data-mandatory-existingClaim") != null) {
                ruleModel.setExistingClaimDataMandatory(XmlHelper.getNodeValue(fieldElement, "data-mandatory-existingClaim"));
            }
            
            if (fieldElement.getElementsByTagName("data-mandatory-existingCollaborationClaim") != null) {
                ruleModel.setExistingCollaborationClaimDataMandatory(XmlHelper.getNodeValue(fieldElement, "data-mandatory-existingCollaborationClaim"));
            }
            
            if (fieldElement.getElementsByTagName("data-mandatory-newInvoice") != null) {
                ruleModel.setNewInvoiceDataMandatory(XmlHelper.getNodeValue(fieldElement, "data-mandatory-newInvoice"));
            }
            
            if (fieldElement.getElementsByTagName("data-mandatory-existingInvoice") != null) {
                ruleModel.setExistingInvoiceDataMandatory(XmlHelper.getNodeValue(fieldElement, "data-mandatory-existingInvoice"));
            }
            
            if (fieldElement.getElementsByTagName("data-mandatory-tpiIntervention") != null) {
                ruleModel.setTpiInterventionDataMandatory(XmlHelper.getNodeValue(fieldElement, "data-mandatory-tpiIntervention"));
            }
            
            if (fieldElement.getElementsByTagName("data-mandatory-offHired") != null) {
                ruleModel.setOffHiredDataMandatory(XmlHelper.getNodeValue(fieldElement, "data-mandatory-offHired"));
            }
            
            if (fieldElement.getElementsByTagName("data-mandatory-hireMonitoring") != null) {
                ruleModel.setHireMonitoringDataMandatory(XmlHelper.getNodeValue(fieldElement, "data-mandatory-hireMonitoring"));
            }
            
            if (fieldElement.getElementsByTagName("data-mandatory-newSupplementaryInvoice") != null) {
                ruleModel.setNewSupplementaryInvoiceMandatory(XmlHelper.getNodeValue(fieldElement, "data-mandatory-newSupplementaryInvoice"));
            }
            
            if (fieldElement.getElementsByTagName("data-mandatory-newSubscriberClaim") != null) {
                ruleModel.setNewSubscriberClaimDataMandatory(XmlHelper.getNodeValue(fieldElement, "data-mandatory-newSubscriberClaim"));
            }
            
            if (fieldElement.getElementsByTagName("data-mandatory-existingSubscriberClaim") != null) {
                ruleModel.setExistingSubscriberClaimDataMandatory(XmlHelper.getNodeValue(fieldElement, "data-mandatory-existingSubscriberClaim"));
            }
            
            if (fieldElement.getElementsByTagName("data-mandatory-newFixedFeeClaim") != null) {
                ruleModel.setNewFixedFeeClaimDataMandatory(XmlHelper.getNodeValue(fieldElement, "data-mandatory-newFixedFeeClaim"));
            }
            
            if (fieldElement.getElementsByTagName("data-mandatory-existingFixedFeeClaim") != null) {
                ruleModel.setExistingFixedFeeClaimDataMandatory(XmlHelper.getNodeValue(fieldElement, "data-mandatory-existingFixedFeeClaim"));
            }
            
            if (fieldElement.getElementsByTagName("data-mandatory-newInsurerClaim") != null) {
                ruleModel.setInsurerClaimDataMandatory(XmlHelper.getNodeValue(fieldElement, "data-mandatory-newInsurerClaim"));
            }
            
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
