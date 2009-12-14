package idas.chox.core.xmlValidation;

import idas.chox.core.util.DocumentHelper;
import java.io.File;
import java.io.IOException;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.*;

public class DataValidationParameter {

    protected String XMLDataValidateFile= "DataValidationTemplate.xml";
    protected Document XMLDataValidateDocument;
    protected Element mainRoot;
    
    public DataValidationParameter() throws IOException{
        
        // String xmlValidationTemplate = "c:/Greenfinch/Projects/CHOX/idaschox/trunk/CHOX_WEB/web/excelTemplate/"+XMLDataValidateFile;
        // String xmlValidationTemplate = "c:/Project Workplace/Greefinch/Sherwood/choxidas/trunk/CHOX_WEB/web/excelTemplate/"+XMLDataValidateFile;
        String path = "/excelTemplate/DataValidationTemplate.xml";
        File file = new ClassPathResource(path).getFile();
        //String xmlValidationTemplate = ServletActionContext.getServletContext().getRealPath("/excelTemplate/" + XMLDataValidateFile);
        this.XMLDataValidateDocument = DocumentHelper.getDocumentFromFile(file);
        this.mainRoot = XMLDataValidateDocument.getDocumentElement();
        
    }
    
    public NodeRuleModel getValidationElementByField(String nodeName){
        
        NodeRuleModel ruleModel = new NodeRuleModel();
        
        try{
            
            Node fieldNode = this.mainRoot.getElementsByTagName(nodeName).item(0);
            ruleModel.setNodeName(nodeName);
            ruleModel.setDataType(fieldNode.getChildNodes().item(3).getTextContent());
            ruleModel.setDataMandatory(fieldNode.getChildNodes().item(5).getTextContent());
            ruleModel.setRegExp(fieldNode.getChildNodes().item(7).getTextContent());
            ruleModel.setNodeDesc(fieldNode.getChildNodes().item(1).getTextContent());
            
        }catch(Exception ex){
            
        }
        
        return ruleModel;
    }
}
