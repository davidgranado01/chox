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
import java.math.BigDecimal;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.*;

public class InvoiceSupplierValidation extends SecureDataService implements rulesInterface{
    
    protected static String sectionName = "Invoice Supplier";
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
    
    public InvoiceSupplierValidation(
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
        this.element = XMLUtils.getElement(root, "supplier");
        
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

            this.claimResult = NodeHelper.nodeValidate(sectionName, "excess-collected", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "vat-collected", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "handling-invoice-no", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "handling-invoice-amount", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "claim-invoice-no", this.element, claimResult, dataValidationParameter);

            if(!this.claimResult.isCheckDataValid()){
                isAllowToReadData = false;
                this.claimResult.setValid(false);
                this.claimResult.setDataValid(false);                 
            }
        }
        
        return isAllowToReadData;
    }
    
    private void process(){ 
        this.claimResult.getClaim().getInvoice().setHandlingInvoiceNo(XmlHelper.getNodeValue(this.element, "handling-invoice-no"));
        this.claimResult.getClaim().getInvoice().setClaimsHandlingInvoiceAmount(XmlHelper.getBigDecimalFromNode(this.element, "handling-invoice-amount"));
        this.claimResult.getClaim().getInvoice().setClaimInvoiceNo(XmlHelper.getNodeValue(this.element, "claim-invoice-no"));
        this.claimResult.getClaim().getInvoice().setExcessAmountCollected(XmlHelper.getBigDecimalFromNode(this.element, "excess-collected"));
        this.claimResult.getClaim().getInvoice().setVatAmountCollected(XmlHelper.getBigDecimalFromNode(this.element, "vat-collected"));
    }
    
    private void doPrintResult(boolean isAllowed){    
        
        if(isAllowed){
            
            System.out.println("-------");
            System.out.println(sectionName + "| Status :"+this.claimResult.isDataValid());
            
            if(claimResult.getClaim().getInvoice()!=null){
                System.out.println(sectionName + "|Claim Data >getHandlingInvoiceNo :"+claimResult.getClaim().getInvoice().getHandlingInvoiceNo());
                System.out.println(sectionName + "|Claim Data >getClaimsHandlingInvoiceAmount :"+claimResult.getClaim().getInvoice().getClaimsHandlingInvoiceAmount());
                System.out.println(sectionName + "|Claim Data >getClaimInvoiceNo :"+claimResult.getClaim().getInvoice().getClaimInvoiceNo());
                System.out.println(sectionName + "|Claim Data >getExcessAmountCollected :"+claimResult.getClaim().getInvoice().getExcessAmountCollected());
                System.out.println(sectionName + "|Claim Data >getVatAmountCollected :"+claimResult.getClaim().getInvoice().getVatAmountCollected());
            }else{
                System.out.println(sectionName + "| NO INCIDENT OBJECT HAVE FOUND!!");
            }  
            
        } 
    } 

}