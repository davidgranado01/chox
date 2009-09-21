package chox.xmlValidation.rules.enginee;

import chox.model.Invoice;
import chox.services.ChoBandService;
import chox.services.ChorganisationService;
import chox.services.ClaimService;
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

public class InvoiceRepairValidation extends SecureDataService implements rulesInterface{
    
    protected static String sectionName = "Invoice Repair";
    private DataValidationParameter dataValidationParameter;
    private ClaimService claimService;
    private ChorganisationService chorganisationService;
    private ChoBandService choBandService;
    private ClaimResult claimResult;
    private Element element;

    public void setElement(Element element) { this.element = element; }
    public void setChoBandService(ChoBandService choBandService) { this.choBandService = choBandService; }
    public void setChorganisationService(ChorganisationService chorganisationService) { this.chorganisationService = chorganisationService; }
    public void setClaimResult(ClaimResult claimResult) { this.claimResult = claimResult; }
    public void setClaimService(ClaimService claimService) { this.claimService = claimService; }
    public void setDataValidationParameter(DataValidationParameter dataValidationParameter) { this.dataValidationParameter = dataValidationParameter; }
    
    public InvoiceRepairValidation(
            ClaimResult claimResult, 
            DataValidationParameter dataValidationParameter, 
            ClaimService claimService, 
            ChorganisationService chorganisationService, 
            ChoBandService choBandService){
        
            setClaimResult(claimResult);
            setDataValidationParameter(dataValidationParameter);
            setClaimService(claimService);
            setChorganisationService(chorganisationService);
            setChoBandService(choBandService);    
    }
    
    public ClaimResult execute() throws DOMException, XPathExpressionException{
        Element root = XMLUtils.getElement(claimResult.getElement(), "invoice");
        this.element = XMLUtils.getElement(root, "repair");
        
        if(validate()){
            process();
        }
        
        doPrintResult(false);
        return this.claimResult;
    }
    
    private boolean validate() throws DOMException, XPathExpressionException{
        
        boolean isAllowToReadData = false;
        
        if(claimResult.getClaimParseStatus().equals(ClaimParseStatus.newInvoice)){
            
            isAllowToReadData = true;

            this.claimResult = NodeHelper.nodeValidate(sectionName, "net", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "vat", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "gross", this.element, claimResult, dataValidationParameter);

            if(!this.claimResult.isCheckDataValid()){
                isAllowToReadData = false;
                this.claimResult.setValid(false);
                this.claimResult.setDataValid(false);                 
            }
        }
        
        return isAllowToReadData;
    }
    
    private void process(){ 
        this.claimResult.getClaim().getInvoice().setRepairGross(XmlHelper.getBigDecimalFromNode(this.element, "gross"));
        this.claimResult.getClaim().getInvoice().setRepairNet(XmlHelper.getBigDecimalFromNode(this.element, "net"));
        this.claimResult.getClaim().getInvoice().setRepairVat(XmlHelper.getBigDecimalFromNode(this.element, "vat"));
    }
    
    private void doPrintResult(boolean isAllowed){    
        if(isAllowed){
            
            System.out.println("-------");
            System.out.println(sectionName + "| Status :"+this.claimResult.isDataValid());
            
            if(claimResult.getClaim().getInvoice()!=null){
                System.out.println(sectionName + "|Claim Data >getRepairGross :"+claimResult.getClaim().getInvoice().getRepairGross());
                System.out.println(sectionName + "|Claim Data >getRepairNet :"+claimResult.getClaim().getInvoice().getRepairNet());
                System.out.println(sectionName + "|Claim Data >getRepairVat :"+claimResult.getClaim().getInvoice().getRepairVat());
            }else{
                System.out.println(sectionName + "| NO INCIDENT OBJECT HAVE FOUND!!");
            }              
        } 
    } 

}