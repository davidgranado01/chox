package chox.xmlValidation.rules.enginee;

import chox.model.EngineerReport;
import chox.services.ChoBandService;
import chox.services.ChorganisationService;
import chox.services.ClaimService;
import chox.services.SecureDataService;
import chox.xmlValidation.model.ClaimResult;
import chox.xmlValidation.model.status.ClaimParseStatus;
import chox.xmlValidation.rules.DataValidationParameter;
import chox.xmlValidation.rules.Util.NodeHelper;
import chox.xmlValidation.rules.Util.XmlHelper;
import chox.xmlValidation.rules.rulesInterface;
import com.filesystemsoftware.utils.XMLUtils;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.*;

public class ClaimEngineeringReportValidation extends SecureDataService implements rulesInterface{
    
    protected static String sectionName = "Engineering Report";
    private DataValidationParameter dataValidationParameter;
    private ClaimResult claimResult;
    private Element element;

    public void setClaimResult(ClaimResult claimResult) { this.claimResult = claimResult; }
    public void setDataValidationParameter(DataValidationParameter dataValidationParameter) { this.dataValidationParameter = dataValidationParameter; }
    
    public ClaimEngineeringReportValidation(
            ClaimResult claimResult, 
            DataValidationParameter dataValidationParameter){
        
            setClaimResult(claimResult);
            setDataValidationParameter(dataValidationParameter);  
    }
    
    public ClaimResult execute() throws DOMException, XPathExpressionException{
 
        this.element = XMLUtils.getElement(claimResult.getElement(), "engineer-report");
        
        if(validate()){
            process();
        }
        
        doPrintResult(false);
        return claimResult;
    }
    
    private boolean validate() throws DOMException, XPathExpressionException{
        
        boolean isAllowToReadData = false;
        
        // AND ONLY FOR NEW CLAIM, EXISTING CLAIM, AND NEW INVOICE
        if(claimResult.getClaimParseStatus().equals(ClaimParseStatus.newClaim) || 
            claimResult.getClaimParseStatus().equals(ClaimParseStatus.existClaim) ||
            claimResult.getClaimParseStatus().equals(ClaimParseStatus.newInvoice)){
            
            isAllowToReadData = true;
            this.claimResult.setCheckDataValid(true);
            
            this.claimResult = NodeHelper.nodeValidate(sectionName, "labour-amount", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "total-amount", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "days", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "usable", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "name", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "company", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "address1", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "address2", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "address3", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "address4", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "address5", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "postcode", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "telephone", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "email", this.element, claimResult, dataValidationParameter);

            isAllowToReadData = this.claimResult.isCheckDataValid();
        }
        
        return isAllowToReadData;
    }
    
    private void process(){
        
        if (XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "labour-amount"))
            || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "total-amount"))
            || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "days"))
            || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "name"))
            || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "company"))
            || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "address1"))
            || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "address2"))
            || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "address3"))
            || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "address4"))
            || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "address5"))
            || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "postcode"))
            || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "telephone"))
            || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "email"))
            || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "usable"))
        ){
            
            if(this.claimResult.getClaim().getEngineerReport() == null){
               this.claimResult.getClaim().setEngineerReport(new EngineerReport()); 
            } 
            
            this.claimResult.getClaim().getEngineerReport().setDays(XmlHelper.getIntegerFromNode(this.element, "days"));
            this.claimResult.getClaim().getEngineerReport().setLabourAmount(XmlHelper.getBigDecimalFromNode(this.element, "labour-amount"));
            this.claimResult.getClaim().getEngineerReport().setTotalAmount(XmlHelper.getBigDecimalFromNode(this.element, "total-amount"));
            this.claimResult.getClaim().getEngineerReport().setName(XmlHelper.getNodeValue(this.element, "name"));
            this.claimResult.getClaim().getEngineerReport().setCompany(XmlHelper.getNodeValue(this.element, "company"));
            this.claimResult.getClaim().getEngineerReport().setAddress1(XmlHelper.getNodeValue(this.element, "address1"));
            this.claimResult.getClaim().getEngineerReport().setAddress2(XmlHelper.getNodeValue(this.element, "address2"));
            this.claimResult.getClaim().getEngineerReport().setAddress3(XmlHelper.getNodeValue(this.element, "address3"));
            this.claimResult.getClaim().getEngineerReport().setAddress4(XmlHelper.getNodeValue(this.element, "address4"));
            this.claimResult.getClaim().getEngineerReport().setAddress5(XmlHelper.getNodeValue(this.element, "address5"));
            this.claimResult.getClaim().getEngineerReport().setPostcode(XmlHelper.getNodeValue(this.element, "postcode"));
            this.claimResult.getClaim().getEngineerReport().setTelephone(XmlHelper.getNodeValue(this.element, "telephone"));
            this.claimResult.getClaim().getEngineerReport().setEmail(XmlHelper.getEmailAddressFromNode(this.element, "email"));
            this.claimResult.getClaim().getEngineerReport().setIsUsable(XmlHelper.getBooleanFromNode(this.element, "usable"));
        } 
    
    }

    private void doPrintResult(boolean isAllowed){
    
        if(isAllowed){
            
            System.out.println("-------");
            System.out.println(sectionName + "| Status :"+this.claimResult.isDataValid());
            
            if(this.claimResult.getClaim().getEngineerReport()!=null){
                
                System.out.println(sectionName + "| getDays :"+this.claimResult.getClaim().getEngineerReport().getDays());
                System.out.println(sectionName + "| getLabourAmount :"+this.claimResult.getClaim().getEngineerReport().getLabourAmount());
                System.out.println(sectionName + "| getTotalAmount :"+this.claimResult.getClaim().getEngineerReport().getTotalAmount());
                System.out.println(sectionName + "| getName :"+this.claimResult.getClaim().getEngineerReport().getName());
                System.out.println(sectionName + "| getCompany :"+this.claimResult.getClaim().getEngineerReport().getCompany());
                System.out.println(sectionName + "| getAddress1 :"+this.claimResult.getClaim().getEngineerReport().getAddress1());
                System.out.println(sectionName + "| getAddress2 :"+this.claimResult.getClaim().getEngineerReport().getAddress2());
                System.out.println(sectionName + "| getAddress3 :"+this.claimResult.getClaim().getEngineerReport().getAddress3());
                System.out.println(sectionName + "| getAddress4 :"+this.claimResult.getClaim().getEngineerReport().getAddress4());
                System.out.println(sectionName + "| getAddress5 :"+this.claimResult.getClaim().getEngineerReport().getAddress5());
                System.out.println(sectionName + "| getPostcode :"+this.claimResult.getClaim().getEngineerReport().getPostcode());
                System.out.println(sectionName + "| getTelephone :"+this.claimResult.getClaim().getEngineerReport().getTelephone());
                System.out.println(sectionName + "| getEmail :"+this.claimResult.getClaim().getEngineerReport().getEmail());
                System.out.println(sectionName + "| isIsUsable :"+this.claimResult.getClaim().getEngineerReport().isIsUsable());
                
            }else{
                System.out.println(sectionName + "| NO ENGINEER REPORT OBJECT HAVE FOUND!!");
            }
            
        }
          
    } 

}