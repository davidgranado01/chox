package idas.chox.service.xml.validations;

import idas.chox.core.xmlValidation.*;
import idas.chox.core.util.DocumentHelper;
import java.io.File;
import java.io.IOException;
import org.springframework.core.io.ClassPathResource;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.ChorganisationService;
import org.w3c.dom.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataValidationParameter {

    private static final Logger LOG = LoggerFactory.getLogger(DataValidationParameter.class);
    private String validateFile;
    private String reservaValidateFile;
    private ChorganisationService chorganisationService;
    private SecurityInfoProvider securityInfoProvider;

    public void setReservaValidateFile(String reservaValidateFile) {
        this.reservaValidateFile = reservaValidateFile;
    }

    private Element getDataValidationRootElement() throws IOException {
        String templateFilePath = "";
        if (securityInfoProvider.getIsCHO()) {
            if (chorganisationService.getChorganisation(securityInfoProvider.getCurrentUser().getChorganisation().getId()).isThirdPartyIntervention()) {
                templateFilePath = reservaValidateFile;
            } else {
                templateFilePath = validateFile;
            }
        }
        File file = new ClassPathResource(templateFilePath).getFile();
        Document doc = DocumentHelper.getDocumentFromFile(file);
        return doc.getDocumentElement();
    }

    public NodeRuleModel getValidationElementByField(String nodeName) {

        NodeRuleModel ruleModel = new NodeRuleModel();

        try {

            Element rootElement = getDataValidationRootElement();
            Node fieldNode = rootElement.getElementsByTagName(nodeName).item(0);
            ruleModel.setNodeName(nodeName);
            ruleModel.setDataType(fieldNode.getChildNodes().item(3).getTextContent());
            ruleModel.setDataMandatory(fieldNode.getChildNodes().item(5).getTextContent());
            ruleModel.setRegExp(fieldNode.getChildNodes().item(7).getTextContent());
            ruleModel.setNodeDesc(fieldNode.getChildNodes().item(1).getTextContent());

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

    public void setSecurityInfoProvider(SecurityInfoProvider securityInfoProvider) {
        this.securityInfoProvider = securityInfoProvider;
    }

     public void setChorganisationService(ChorganisationService chorganisationService) {
        this.chorganisationService = chorganisationService;
    }
}
