package idas.chox.service.xml.validations;

import idas.chox.core.xmlValidation.*;
import idas.chox.core.util.DocumentHelper;
import java.io.File;
import java.io.IOException;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.*;

public class DataValidationParameter {

    private String validateFile;
    
    private Element getDataValidationRootElement()throws IOException{

        File file = new ClassPathResource(validateFile).getFile();
        Document doc = DocumentHelper.getDocumentFromFile(file);
        return doc.getDocumentElement();
    }
    
    public NodeRuleModel getValidationElementByField(String nodeName){
        
        NodeRuleModel ruleModel = new NodeRuleModel();
        
        try{

            Element rootElement = getDataValidationRootElement();
            Node fieldNode = rootElement.getElementsByTagName(nodeName).item(0);
            ruleModel.setNodeName(nodeName);
            ruleModel.setDataType(fieldNode.getChildNodes().item(3).getTextContent());
            ruleModel.setDataMandatory(fieldNode.getChildNodes().item(5).getTextContent());
            ruleModel.setRegExp(fieldNode.getChildNodes().item(7).getTextContent());
            ruleModel.setNodeDesc(fieldNode.getChildNodes().item(1).getTextContent());
            
        }catch(Exception ex){
            
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
