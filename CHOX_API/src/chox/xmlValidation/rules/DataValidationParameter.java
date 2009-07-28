package chox.xmlValidation.rules;

import chox.Util.DocumentHelper;
import chox.xmlValidation.model.NodeRuleModel;
import org.apache.struts2.ServletActionContext;
import java.io.File;
import org.w3c.dom.*;

public class DataValidationParameter {

    protected String XMLDataValidateFile= "DataValidationTemplate.xml";
    protected Document XMLDataValidateDocument;
    protected Element mainRoot;
    
    public DataValidationParameter(){
        //String xmlValidationTemplate = ServletActionContext.getServletContext().getRealPath("/excelTemplate/" + XMLDataValidateFile);
        String xmlValidationTemplate = "c:/Greenfinch/Projects/CHOX/idaschox/trunk/CHOX_WEB/web/excelTemplate/"+XMLDataValidateFile;
        this.XMLDataValidateDocument = DocumentHelper.getDocumentFromFile(new File(xmlValidationTemplate));
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
