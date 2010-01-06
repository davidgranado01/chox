package chox.xmlValidation.rules.enginee;

import chox.xmlValidation.rules.*;
import chox.Util.DateHelper;
import chox.xmlValidation.rules.Util.XmlHelper;
import org.w3c.dom.*;
import com.filesystemsoftware.utils.XMLUtils;
import chox.model.*;
import chox.services.ChoBandService;
import chox.services.ChorganisationService;
import chox.services.ClaimService;
import chox.services.SecureDataService;
import chox.services.ClaimResult;
import chox.xmlValidation.model.status.ClaimParseStatus;
import chox.xmlValidation.rules.Util.NodeHelper;
import java.math.BigDecimal;
import java.sql.Timestamp;
import javax.xml.xpath.XPathExpressionException;

public class ClaimHeaderValidation extends SecureDataService implements rulesInterface{
    
    protected static String sectionName = "Claim Header";
    private DataValidationParameter dataValidationParameter;
    private ClaimService claimService;
    private ChorganisationService chorganisationService;
    private ChoBandService choBandService;
    private ClaimResult claimResult;
    private Element element;

    // PAGE PARAMETERS
    Boolean managingRepair;
    Timestamp firstContactDate;
    Timestamp creditAgreementDate;
    Timestamp gtaNoticeDate;
    String choReferenceNumber;
    boolean isUpdateManagingRepair = false;
        
    public void setChoBandService(ChoBandService choBandService) { this.choBandService = choBandService; }
    public void setChorganisationService(ChorganisationService chorganisationService) { this.chorganisationService = chorganisationService; }
    public void setClaimResult(ClaimResult claimResult) { this.claimResult = claimResult; }
    public void setClaimService(ClaimService claimService) { this.claimService = claimService; }
    public void setDataValidationParameter(DataValidationParameter dataValidationParameter) { this.dataValidationParameter = dataValidationParameter; }
    
    public ClaimHeaderValidation(ClaimResult claimResult, DataValidationParameter dataValidationParameter, 
            ClaimService claimService, ChorganisationService chorganisationService, ChoBandService choBandService){
            setClaimResult(claimResult);
            setDataValidationParameter(dataValidationParameter);
            setClaimService(claimService);
            setChorganisationService(chorganisationService);
            setChoBandService(choBandService);    
    }
    
    public ClaimResult execute() throws DOMException, XPathExpressionException, Exception{

        this.element = XMLUtils.getElement(claimResult.getElement(), "supplier");
        
        validate();
        process();
        doPrintResult(false);
        
        return claimResult;
    }
    
    private void validate() throws DOMException, XPathExpressionException, Exception{
        
        this.claimResult.setCheckDataValid(true);
        
        this.claimResult = NodeHelper.nodeValidate(sectionName, "first-contact", claimResult.getElement(), claimResult, dataValidationParameter);
        this.claimResult = NodeHelper.nodeValidate(sectionName, "managing-repair", claimResult.getElement(), claimResult, dataValidationParameter);
        this.claimResult = NodeHelper.nodeValidate(sectionName, "agreement-signed", claimResult.getElement(), claimResult, dataValidationParameter);
        this.claimResult = NodeHelper.nodeValidate(sectionName, "gta-notice", claimResult.getElement(), claimResult, dataValidationParameter);
        this.claimResult = NodeHelper.nodeValidate(sectionName, "rental-status", claimResult.getElement(), claimResult, dataValidationParameter);
        this.claimResult = NodeHelper.nodeValidate(sectionName, "supplier-name", this.element, claimResult, dataValidationParameter);
        this.claimResult = NodeHelper.nodeValidate(sectionName, "supplier-reference", this.element, claimResult, dataValidationParameter);
        
        if(NodeHelper.nodeValidateBoolean(sectionName, "first-contact", claimResult.getElement(), dataValidationParameter)){
            firstContactDate = XmlHelper.getTimeStampFromNode(claimResult.getElement(), "first-contact");
        }
        
        if(NodeHelper.nodeValidateBoolean(sectionName, "managing-repair", claimResult.getElement(),  dataValidationParameter)){
            managingRepair = XmlHelper.getBooleanFromNode(claimResult.getElement(), "managing-repair");
            
            if(!XMLUtils.getElementValue(claimResult.getElement(), "managing-repair").equalsIgnoreCase("")
                    && XMLUtils.getElementValue(claimResult.getElement(), "managing-repair")!=null){
                isUpdateManagingRepair = true;
            }

        }
        
        if(NodeHelper.nodeValidateBoolean(sectionName, "agreement-signed", claimResult.getElement(),  dataValidationParameter)){
            creditAgreementDate = XmlHelper.getTimeStampFromNode(claimResult.getElement(), "agreement-signed");
        }
        
        if(NodeHelper.nodeValidateBoolean(sectionName, "supplier-reference", claimResult.getElement(),  dataValidationParameter)){
            choReferenceNumber = XmlHelper.getNodeValue(this.element, "supplier-reference");
        }
        
        if(NodeHelper.nodeValidateBoolean(sectionName, "gta-notice", claimResult.getElement(), dataValidationParameter)){
            gtaNoticeDate = XmlHelper.getTimeStampFromNode(claimResult.getElement(), "gta-notice");
        }
        
        if(gtaNoticeDate==null){
            gtaNoticeDate = DateHelper.getCurrentTimeStamp();
        }
    }
    
    private void process(){
       
        Claim claim = new Claim();
        
        if(claimService.isClaimSupplierReferenceNumberExist(choReferenceNumber)){
            
            claim = claimService.getClaimByCHOReferenceNumber(choReferenceNumber);
             
            if(claim.getInvoice() != null){

                claimResult.setClaimParseStatus(ClaimParseStatus.existInvoice);
                claimResult.setValid(false);
                
            }else{
                
                if(claim.getStatus().equalsIgnoreCase(ClaimStatus.AWAITING_INVOICE_DATA)){
                    
                    claimResult.setClaimParseStatus(ClaimParseStatus.newInvoice);
                    ChoBand choBand = choBandService.getChoBandByChorganisationIdAndInsurerId(claim.getChorganisation().getId(), claim.getInsurer().getId());
                    claim.setChoband(choBand);

                    if(isUpdateManagingRepair && managingRepair!=null){
                        claim.setManagingRepair(managingRepair);
                    }
                    
                }else if(claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_CLOSED) ||
                    claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_PENDING) ||
                    claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_REJECTION_ACCEPTED)){
                    
                    // NOT EDITABNLE CLAIM
                    claimResult.setClaimParseStatus(ClaimParseStatus.ClaimNotEditable);
                    claimResult.setValid(false);
                    
                }else{
                    // EDITABLE CLAIM
                    claimResult.setClaimParseStatus(ClaimParseStatus.existClaim);
                }
            }
            
       }else{

            claimResult.setClaimParseStatus(ClaimParseStatus.newClaim);

            if(managingRepair!=null){
                claim.setManagingRepair(managingRepair);
            }
            
            claim.setPolicyHolderContactDate(firstContactDate);
            claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
            claim.setChoReference(choReferenceNumber);
            claim.setCreditAgreementDate(creditAgreementDate);
            claim.setGtaNoticeDate(gtaNoticeDate);
            claim.setIndemnityAmount(new BigDecimal("0.00"));
            claim.setPercentageLiabilityAccepted(new BigDecimal("0.00"));
            claim.setChorganisation(chorganisationService.getCurrentCHOrganisation());

        }

        claimResult.setClaim(claim);
    }
    
    private void doPrintResult(boolean isAllowed){
        
        if(isAllowed){
            
            System.out.println("-STARTS------");
            System.out.println(sectionName + "| " + XmlHelper.getNodeValue(this.element, "supplier-name"));
            
            if(this.claimResult.getClaim()!=null){

                System.out.println(sectionName + "| getManagingRepair :"+this.claimResult.getClaim().getManagingRepair());
                System.out.println(sectionName + "| getPolicyHolderContactDate :"+this.claimResult.getClaim().getPolicyHolderContactDate());
                System.out.println(sectionName + "| getStatus :"+this.claimResult.getClaim().getStatus());
                System.out.println(sectionName + "| getChoReference :"+this.claimResult.getClaim().getChoReference());
                System.out.println(sectionName + "| getCreditAgreementDate :"+this.claimResult.getClaim().getCreditAgreementDate());
                System.out.println(sectionName + "| getGtaNoticeDate :"+this.claimResult.getClaim().getGtaNoticeDate());
                System.out.println(sectionName + "| getIndemnityAmount :"+this.claimResult.getClaim().getIndemnityAmount());
                System.out.println(sectionName + "| getPercentageLiabilityAccepted :"+this.claimResult.getClaim().getPercentageLiabilityAccepted());  
            }
            
            System.out.println("-END------");
        }   
    }
    
}
