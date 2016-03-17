package idas.chox.web.actions;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.services.ClaimService;
import idas.chox.service.ActionResponse;


public class AlertAction extends BaseAction {
    private static final Logger LOG = LoggerFactory.getLogger(AlertAction.class);

    private ClaimService claimService;
    private Integer claimId;
    private String claimNumber;
    private String customerClaimNumber;
    private String choReference;
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
        if ((getIsInsurer() && claimService.getClaim(sessionClaimId).getInsurer().isSupervisorEnable())
                || (getIsCHO() && claimService.getClaim(sessionClaimId).getChorganisation().isSupervisorEnable())) {

            Integer daysBeforeEscalatedRetriction;
            Integer timesInStatusContestedRetriction;
            
            if (getIsInsurer()) {
                daysBeforeEscalatedRetriction = getAuthenticatedUser().getInsurer().getDaysBeforeEscalated();
                timesInStatusContestedRetriction = getAuthenticatedUser().getInsurer().getTimesInStatusContested();
            } else {
                daysBeforeEscalatedRetriction = getAuthenticatedUser().getChorganisation().getDaysBeforeEscalated();
                timesInStatusContestedRetriction = getAuthenticatedUser().getChorganisation().getTimesInStatusContested();
            }
            
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

        try {
            if (!claimNumber.isEmpty() && claimId != 0) {
                if (claimService == null) {
                    LOG.error("No Claim Service in AlertAction.isClaimNumberDuplicated: claimId={}, claimNumber='{}'",
                            new Object[]{claimId, claimNumber});
                    this.getActionResponse().AssignResult(ActionResponse.RESULT_TYPE_YESNO, "Unable to validate whether claim number is already associated with another claim(s). Do you wish to continue?");
                } else if (claimService.getClaimCountByClaimNumber(claimNumber, claimId) > 0) {
                    this.getActionResponse().AssignResult(ActionResponse.RESULT_TYPE_YESNO, "The claim number you have supplied is already associated with another claim(s). Do you wish to continue?");
                }
            }
        } catch (Exception ex) {
            LOG.warn("Exception thrown: claimId={}, claimNumber='{}', claimService={}",
                    new Object[]{claimId, claimNumber, claimService, ex});
            this.getActionResponse().AssignResult(ActionResponse.RESULT_TYPE_YESNO, "Unable to validate whether claim number is already associated with another claim(s). Do you wish to continue?");
        }

        return SUCCESS;
    }
    
    public String isCustomerClaimNumberDuplicated() {
        try {
            if (!customerClaimNumber.isEmpty() && claimId != 0) {
                if (claimService == null) {
                    LOG.error("No Claim Service in AlertAction.isCustomerClaimNumberDuplicated: claimId={}, claimNumber='{}'",
                            new Object[]{claimId, customerClaimNumber});
                    this.getActionResponse().AssignResult(ActionResponse.RESULT_TYPE_YESNO, "Unable to validate whether customer claim number is already associated with another claim(s). Do you wish to continue?");
                } else if (claimService.isCustomerClaimNumberExist(customerClaimNumber, claimId, Boolean.TRUE)) {
                    this.getActionResponse().AssignResult(ActionResponse.RESULT_TYPE_YESNO, "The customer claim number you have supplied is already associated with another claim(s). Do you wish to continue?");
                }
            }
        } catch (Exception ex) {
            LOG.warn("Exception thrown: claimId={}, customerClaimNumber='{}', claimService={}",
                    new Object[]{claimId, customerClaimNumber, claimService, ex});
            this.getActionResponse().AssignResult(ActionResponse.RESULT_TYPE_YESNO, "Unable to validate whether customer claim number is already associated with another claim(s). Do you wish to continue?");
        }

        return SUCCESS;
    }
    
    public String isSupplierReferenceNumberDuplicated() {
        try {
            if (!choReference.isEmpty() && claimId != 0) {
                if (claimService == null) {
                    LOG.error("No Claim Service in AlertAction.isSupplierReferenceNumberDuplicated: claimId={}, claimNumber='{}'",
                            new Object[]{claimId, claimNumber});
                    this.getActionResponse().AssignResult(ActionResponse.RESULT_TYPE_YESNO, "Unable to validate whether supplier reference number is already associated with another claim(s). Do you wish to continue?");
                } else {
                    int choId = claimService.getClaim(claimId).getChorganisation().getId();
                    if (claimService.isClaimSupplierReferenceNumberExistForCho(choReference, choId)) {
                        this.getActionResponse().AssignResult(ActionResponse.RESULT_TYPE_MESSAGE, "The Supplier Reference number you have supplied is already associated with another claim for this CHO.");
                    } else if (claimService.isClaimSupplierReferenceNumberExist(choReference)) {
                        this.getActionResponse().AssignResult(ActionResponse.RESULT_TYPE_YESNO, "The Supplier Reference number you have supplied is already associated with a claim for another CHO. Do you wish to continue?");
                    }
                }
            }
        } catch (Exception ex) {
            LOG.warn("Exception thrown: claimId={}, customerClaimNumber='{}', claimService={}",
                    new Object[]{claimId, choReference, claimService, ex});
            this.getActionResponse().AssignResult(ActionResponse.RESULT_TYPE_YESNO, "Unable to validate whether the Supplier Reference number is already associated with another claim(s). Do you wish to continue?");
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

    public String getCustomerClaimNumber() {
        return customerClaimNumber;
    }

    public void setCustomerClaimNumber(String customerClaimNumber) {
        this.customerClaimNumber = customerClaimNumber;
    }

    public String getCustomerClaimRefNum() {
        return customerClaimRefNum;
    }

    public void setCustomerClaimRefNum(String customerClaimRefNum) {
        this.customerClaimRefNum = customerClaimRefNum;
    }

    public String getChoReference() {
        return choReference;
    }

    public void setChoReference(String choReference) {
        this.choReference = choReference;
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
