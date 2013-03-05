package idas.chox.web.actions;

import java.util.List;
import java.util.Map;

import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Insurer;
import idas.chox.core.services.ClaimService;
import idas.chox.service.ActionResponse;

import com.idaschox.services.chox.Claim;

public class AlertAction extends BaseAction {

    private ClaimService claimService;
    private Integer claimId;
    private String claimNumber;
    private String customerClaimRefNum;
    private List duplicatedClaims;
    private String actionResult;
    private List duplicatedSupplementaryInvoice;
    private int numberOfTimesContestedWithCHOtoEscalate;
    private int daysSinceInvoiceUploadToEscalate;

    public List getDuplicatedSupplementaryInvoice() {
        return duplicatedSupplementaryInvoice;
    }

    public String getDuplicatedSupplementaryInvoiceAlert() {
        duplicatedSupplementaryInvoice = claimService.getDuplicateSupplementaryInvoiceClaims(customerClaimRefNum, claimId);
        return SUCCESS;
    }

    public String getDuplicatedClaimAlert() {
        duplicatedClaims = claimService.getOtherClaimsByClaimNumber(claimNumber, claimId);
        return SUCCESS;
    }
    
    public String getAwaitingLitigationOutcomeAlert() {
        if (claimService.getClaim(claimId).getStatus().equalsIgnoreCase(ClaimStatus.AWAITING_LITIGATION_OUTCOME)) {
            return SUCCESS;
        }
        return "empty";
    }
    
    public String getClaimEscalatedToSupervisorAlert() {
        Map<String, Object> sessionClaim =  (Map<String, Object>) getSession().get(Claim.class.getSimpleName());
        int sessionClaimId =  (Integer) sessionClaim.get("id");
        Insurer insurer = claimService.getClaim(sessionClaimId).getInsurer();
        if (insurer.isSupervisorEnable()){
            Integer daysBeforeEscalatedRetriction = insurer.getDaysBeforeEscalated();
            Integer timesInStatusContestedRetriction = insurer.getTimesInStatusContested();
            
            
            
            if (daysBeforeEscalatedRetriction != null) {
                daysSinceInvoiceUploadToEscalate = claimService.getDaysSinceInvoiceUploadToEscalate(sessionClaimId);
                daysSinceInvoiceUploadToEscalate = daysSinceInvoiceUploadToEscalate >= daysBeforeEscalatedRetriction ? daysSinceInvoiceUploadToEscalate : 0;
            } else {
                daysSinceInvoiceUploadToEscalate = 0;
            }
            if (timesInStatusContestedRetriction != null) {
                numberOfTimesContestedWithCHOtoEscalate = claimService.getNumberOfTimesContestedWithCHOtoEscalate(sessionClaimId);
                numberOfTimesContestedWithCHOtoEscalate = numberOfTimesContestedWithCHOtoEscalate >= timesInStatusContestedRetriction ? numberOfTimesContestedWithCHOtoEscalate : 0;
            } else {
                numberOfTimesContestedWithCHOtoEscalate = 0;
            }
            //if claim does not match the insurers restriction in that case we set it to 0 and don't display it in alert panel
            
            if (numberOfTimesContestedWithCHOtoEscalate > 0 || daysSinceInvoiceUploadToEscalate > 0) {
                return SUCCESS;
            }
        }
        return "empty";
    }

    public String isClaimNumberDuplicated() {

        if (!claimNumber.isEmpty()) {
            if (claimService.getClaimCountByClaimNumber(claimNumber, claimId) > 0) {
                this.getActionResponse().AssignResult(ActionResponse.RESULT_TYPE_YESNO, "The claim number you have supplied is already associated with another claim(s). Do you wish to continue?");
            }
        }

        return SUCCESS;
    }
    
    public List getOtherDuplicatedClaims() {
        return duplicatedClaims;
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    public Integer getClaimId() {
        return claimId;
    }

    public void setClaimId(Integer claimId) {
        this.claimId = claimId;
    }

    public String getClaimNumber() {
        return claimNumber;
    }

    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }

    public String getCustomerClaimRefNum() {
        return customerClaimRefNum;
    }

    public void setCustomerClaimRefNum(String customerClaimRefNum) {
        this.customerClaimRefNum = customerClaimRefNum;
    }

    @Override
    public String getActionResult() {
        return actionResult;
    }

    public int getNumberOfTimesContestedWithCHOtoEscalate() {
        return numberOfTimesContestedWithCHOtoEscalate;
    }

    public int getDaysSinceInvoiceUploadToEscalate() {
        return daysSinceInvoiceUploadToEscalate;
    }

}
