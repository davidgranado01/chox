package chox.xmlValidation.rules.enginee;

import chox.model.Invoice;
import chox.services.BreBandService;
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

public class InvoiceStorageRecoveryValidation extends SecureDataService implements rulesInterface{
    
    protected static String sectionName = "Invoice Storage Recovery";
    private DataValidationParameter dataValidationParameter;
    private ClaimService claimService;
    private ChorganisationService chorganisationService;
    private BreBandService breBandService;
    private ClaimResult claimResult;
    private Element element;

    public void setElement(Element element) { this.element = element; }
    public void setBreBandService(BreBandService breBandService) { this.breBandService = breBandService; }
    public void setChorganisationService(ChorganisationService chorganisationService) { this.chorganisationService = chorganisationService; }
    public void setClaimResult(ClaimResult claimResult) { this.claimResult = claimResult; }
    public void setClaimService(ClaimService claimService) { this.claimService = claimService; }
    public void setDataValidationParameter(DataValidationParameter dataValidationParameter) { this.dataValidationParameter = dataValidationParameter; }
    
    public InvoiceStorageRecoveryValidation(
            ClaimResult claimResult, 
            DataValidationParameter dataValidationParameter, 
            ClaimService claimService, 
            ChorganisationService chorganisationService, 
            BreBandService breBandService){
        
            setClaimResult(claimResult);
            setDataValidationParameter(dataValidationParameter);
            setClaimService(claimService);
            setChorganisationService(chorganisationService);
            setBreBandService(breBandService);
    }
    
    public ClaimResult execute() throws DOMException, XPathExpressionException, Exception{
        
        Element root = XMLUtils.getElement(claimResult.getElement(), "invoice");
        this.element = XMLUtils.getElement(root, "storage-recovery");
        
        if(validate()){
            process();
        }
        
        doPrintResult(false);
        return this.claimResult;
    }
    
    private boolean validate() throws DOMException, XPathExpressionException, Exception{
        
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
        this.claimResult.getClaim().getInvoice().setStorageRecoveryGross(XmlHelper.getBigDecimalFromNode(this.element, "gross"));
        this.claimResult.getClaim().getInvoice().setStorageRecoveryNet(XmlHelper.getBigDecimalFromNode(this.element, "net"));
        this.claimResult.getClaim().getInvoice().setStorageRecoveryVat(XmlHelper.getBigDecimalFromNode(this.element, "vat"));
    }
    
    private void doPrintResult(boolean isAllowed){ 
        
        if(isAllowed){
            
            System.out.println("-------");
            System.out.println(sectionName + "| Status :"+this.claimResult.isDataValid());
            
            if(claimResult.getClaim().getInvoice()!=null){
                
                System.out.println(sectionName + "|Claim Data >getStorageRecoveryGross :"+claimResult.getClaim().getInvoice().getStorageRecoveryGross());
                System.out.println(sectionName + "|Claim Data >getStorageRecoveryNet :"+claimResult.getClaim().getInvoice().getStorageRecoveryNet());
                System.out.println(sectionName + "|Claim Data >getStorageRecoveryVat :"+claimResult.getClaim().getInvoice().getStorageRecoveryVat());
                
            }else{
                System.out.println(sectionName + "| NO INCIDENT OBJECT HAVE FOUND!!");
            }             
        } 
    } 

}