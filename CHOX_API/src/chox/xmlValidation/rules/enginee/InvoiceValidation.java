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
import java.math.BigDecimal;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.*;

public class InvoiceValidation extends SecureDataService implements rulesInterface{
    
    protected static String sectionName = "Invoice";
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
    
    public InvoiceValidation(
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
        
        this.element = XMLUtils.getElement(claimResult.getElement(), "invoice");
        
        if(validate()){
            process();
        }
        
        doPrintResult(false);
        return claimResult;
    }
    
    private boolean validate() throws DOMException, XPathExpressionException{
        
        boolean isAllowToReadData = false;
        
        if(claimResult.getClaimParseStatus().equals(ClaimParseStatus.newInvoice)){
            
            isAllowToReadData = true;
            
            this.claimResult.setCheckDataValid(true);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "less-handling-fee", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "net", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "vat", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "gross", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "less-discount", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "total-to-pay", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "date-invoiced", this.element, claimResult, dataValidationParameter);
            isAllowToReadData = this.claimResult.isCheckDataValid();
            
        }
        
        return isAllowToReadData;
    }
    
    private void process() throws DOMException, XPathExpressionException{ 
        
        Invoice invoice = new Invoice();
        
        invoice.setTotalGross(XmlHelper.getBigDecimalFromNode(this.element, "gross"));
        invoice.setTotalNet(XmlHelper.getBigDecimalFromNode(this.element, "net"));
        invoice.setTotalVat(XmlHelper.getBigDecimalFromNode(this.element, "vat"));
        invoice.setTotalToPay(XmlHelper.getBigDecimalFromNode(this.element, "total-to-pay"));
        invoice.setDiscount(XmlHelper.getBigDecimalFromNode(this.element, "less-discount"));
        invoice.setDeductionForClaimsHandlingFee(XmlHelper.getBigDecimalFromNode(this.element, "less-handling-fee"));
        invoice.setDateInvoiced(XmlHelper.getTimeStampFromNode(this.element, "date-invoiced"));
        invoice.setPenaltyAlertQty(0);
        invoice.setPenaltyCharge(BigDecimal.ZERO);

        // PRE-DEFINED
        invoice.setHireGross(BigDecimal.ZERO);
        invoice.setHireNet(BigDecimal.ZERO);
        invoice.setHireVat(BigDecimal.ZERO);
        invoice.setHireRateChargedPerDay(BigDecimal.ZERO);
        invoice.setHandlingInvoiceNo("");
        invoice.setClaimsHandlingInvoiceAmount(BigDecimal.ZERO);
        invoice.setClaimInvoiceNo("");
        invoice.setExcessAmountCollected(BigDecimal.ZERO);
        invoice.setVatAmountCollected(BigDecimal.ZERO);
        invoice.setRepairGross(BigDecimal.ZERO);
        invoice.setRepairNet(BigDecimal.ZERO);
        invoice.setRepairVat(BigDecimal.ZERO);
        invoice.setStorageRecoveryGross(BigDecimal.ZERO);
        invoice.setStorageRecoveryNet(BigDecimal.ZERO);
        invoice.setStorageRecoveryVat(BigDecimal.ZERO);
        invoice.setEngineerFeeGross(BigDecimal.ZERO);
        invoice.setEngineerFeeNet(BigDecimal.ZERO);
        invoice.setEngineerFeeVat(BigDecimal.ZERO);

        this.claimResult.getClaim().setInvoice(invoice);
        
        // VEHICLE
        InvoiceVehiclesValidation vehiclesVal = new InvoiceVehiclesValidation(claimResult, dataValidationParameter, claimService, chorganisationService, choBandService);
        this.claimResult = vehiclesVal.execute();
        
        // SUPPLIER
        InvoiceSupplierValidation supplierVal = new InvoiceSupplierValidation(claimResult, dataValidationParameter, claimService, chorganisationService, choBandService);
        this.claimResult = supplierVal.execute();
        
        // REPAIR
        InvoiceRepairValidation repairVal = new InvoiceRepairValidation(claimResult, dataValidationParameter, claimService, chorganisationService, choBandService);
        this.claimResult = repairVal.execute();        
        
        // STORAGE RECOVERY
        InvoiceStorageRecoveryValidation storageeRecVal = new InvoiceStorageRecoveryValidation(claimResult, dataValidationParameter, claimService, chorganisationService, choBandService);
        this.claimResult = storageeRecVal.execute();

        // ENGINEERING FEE
        InvoiceEngineeringFee engFeeVal = new InvoiceEngineeringFee(claimResult, dataValidationParameter, claimService, chorganisationService, choBandService);
        this.claimResult = engFeeVal.execute();
        
        InvoiceExtraValidation extraValidation = new InvoiceExtraValidation(claimResult, dataValidationParameter, claimService, chorganisationService, choBandService);
        this.claimResult = extraValidation.execute();
        
    }

    private void doPrintResult(boolean isAllowed){

        if(isAllowed){
            
            System.out.println("-------");
            System.out.println(sectionName + "| Status :"+this.claimResult.isDataValid());
            
            if(claimResult.getClaim().getInvoice()!=null){
                System.out.println(sectionName + "| getTotalGross :"+claimResult.getClaim().getInvoice().getTotalGross());
                System.out.println(sectionName + "| getTotalNet :"+claimResult.getClaim().getInvoice().getTotalNet());
                System.out.println(sectionName + "| getTotalVat :"+claimResult.getClaim().getInvoice().getTotalVat());
                System.out.println(sectionName + "| getTotalToPay :"+claimResult.getClaim().getInvoice().getTotalToPay());
                System.out.println(sectionName + "| getDiscount :"+claimResult.getClaim().getInvoice().getDiscount());
                System.out.println(sectionName + "| getDeductionForClaimsHandlingFee :"+claimResult.getClaim().getInvoice().getDeductionForClaimsHandlingFee());
                System.out.println(sectionName + "| getDateInvoiced :"+claimResult.getClaim().getInvoice().getDateInvoiced());
                System.out.println(sectionName + "| getPenaltyAlertQty :"+claimResult.getClaim().getInvoice().getPenaltyAlertQty());
                System.out.println(sectionName + "| getPenaltyCharge :"+claimResult.getClaim().getInvoice().getPenaltyCharge());
            }else{
                System.out.println(sectionName + "| NO INVOICE OBJECT HAVE FOUND!!");
            }
        }
          
    } 

}