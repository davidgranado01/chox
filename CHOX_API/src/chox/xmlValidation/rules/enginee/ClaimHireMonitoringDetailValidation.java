package chox.xmlValidation.rules.enginee;

import chox.model.HireMonitoringDetail;
import chox.services.SecureDataService;
import chox.services.ClaimResult;
import chox.xmlValidation.model.status.ClaimParseStatus;
import chox.xmlValidation.rules.DataValidationParameter;
import chox.xmlValidation.rules.Util.NodeHelper;
import chox.xmlValidation.rules.Util.XmlHelper;
import chox.xmlValidation.rules.rulesInterface;
import com.filesystemsoftware.utils.XMLUtils;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.*;

public class ClaimHireMonitoringDetailValidation extends SecureDataService implements rulesInterface{
    
    protected static String sectionName = "Hire Monitoring Detail";
    private DataValidationParameter dataValidationParameter;
    private ClaimResult claimResult;
    private Element element;

    public void setClaimResult(ClaimResult claimResult) { this.claimResult = claimResult; }
    public void setDataValidationParameter(DataValidationParameter dataValidationParameter) { this.dataValidationParameter = dataValidationParameter; }
    
    public ClaimHireMonitoringDetailValidation(
            ClaimResult claimResult, 
            DataValidationParameter dataValidationParameter){
        
            setClaimResult(claimResult);
            setDataValidationParameter(dataValidationParameter);  
    }
    
    public ClaimResult execute() throws DOMException, XPathExpressionException{
 
        this.element = XMLUtils.getElement(claimResult.getElement(), "repair-details");
        
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
            
            this.claimResult = NodeHelper.nodeValidate(sectionName, "repairer", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "inspection-booked-date", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "inspection-date", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "repair-book-in-date", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "repair-complete-date", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "name-ime", this.element, claimResult, dataValidationParameter);
        
            if(!this.claimResult.isCheckDataValid()){
                isAllowToReadData = false;
                this.claimResult.setValid(false);
                this.claimResult.setDataValid(false); 
            }
        }
        
        return isAllowToReadData;
    }
    
    private void process(){
    
        HireMonitoringDetail hireMonitoringdtl = new HireMonitoringDetail();
        boolean isNotEmpty = false;
        
        if(this.claimResult.getClaim().getHireMonitoringDetail() != null){
            hireMonitoringdtl = this.claimResult.getClaim().getHireMonitoringDetail();
        }

        if(XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "repairer"))){
            isNotEmpty = true;
            hireMonitoringdtl.setNameOfRepairer(XmlHelper.getNodeValue(this.element, "repairer"));
        }

        if(XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "inspection-booked-date"))){
            isNotEmpty = true;
            hireMonitoringdtl.setInspectionBookedDate(XmlHelper.getTimeStampFromNode(this.element, "inspection-booked-date"));
        }

        if(XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "inspection-date"))){
            isNotEmpty = true;
            hireMonitoringdtl.setInspectionDate(XmlHelper.getTimeStampFromNode(this.element, "inspection-date"));
        }

        if(XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "repair-book-in-date"))){
            isNotEmpty = true;
            hireMonitoringdtl.setRepairBookInDate(XmlHelper.getTimeStampFromNode(this.element, "repair-book-in-date"));
        }

        if(XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "repair-complete-date"))){
            isNotEmpty = true;
            hireMonitoringdtl.setRepairCompletionDate(XmlHelper.getTimeStampFromNode(this.element, "repair-complete-date"));
        }

        if(XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "name-ime"))){
            isNotEmpty = true;
            hireMonitoringdtl.setNameOfIme(XmlHelper.getNodeValue(this.element, "name-ime"));
        }
        
        if(isNotEmpty){
            this.claimResult.getClaim().setHireMonitoringDetail(hireMonitoringdtl);
        }
    }

    private void doPrintResult(boolean isAllowed){
    
        if(isAllowed){
            
            System.out.println("-------");
            System.out.println(sectionName + "| Status :"+this.claimResult.isDataValid());
            
            if(this.claimResult.getClaim().getHireMonitoringDetail() != null){
                
                System.out.println(sectionName + "| getNameOfRepairer :"+this.claimResult.getClaim().getHireMonitoringDetail().getNameOfRepairer());
                System.out.println(sectionName + "| getInspectionBookedDate :"+this.claimResult.getClaim().getHireMonitoringDetail().getInspectionBookedDate());
                System.out.println(sectionName + "| getInspectionDate :"+this.claimResult.getClaim().getHireMonitoringDetail().getInspectionDate());
                System.out.println(sectionName + "| getRepairBookInDate :"+this.claimResult.getClaim().getHireMonitoringDetail().getRepairBookInDate());
                System.out.println(sectionName + "| getRepairCompletionDate :"+this.claimResult.getClaim().getHireMonitoringDetail().getRepairCompletionDate());
                System.out.println(sectionName + "| getNameOfIme :"+this.claimResult.getClaim().getHireMonitoringDetail().getNameOfIme());

            }else{
                System.out.println(sectionName + "| NO HIRE MONITORING OBJECT HAVE FOUND!!");
            }
        }
    } 

}