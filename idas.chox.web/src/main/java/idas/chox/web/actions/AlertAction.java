package idas.chox.web.actions;

import idas.chox.core.model.ClaimStatus;
import idas.chox.core.services.ClaimService;
import idas.chox.service.ActionResponse;
import java.util.List;

public class AlertAction extends BaseAction {

    private ClaimService claimService;
    private Integer claimId;
    private String claimNumber;
    private String customerClaimRefNum;
    private List duplicatedClaims;
    private String actionResult;
    private List duplicatedSupplementaryInvoice;

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
}
