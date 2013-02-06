package idas.chox.service.workflow.activities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.Invoice;
import idas.chox.core.model.LiabilityStatus;
import idas.chox.core.model.ThirdParty;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.AuditTrailService;
import idas.chox.core.services.CommentService;
import idas.chox.core.services.InsurerService;
import idas.chox.core.services.NotificationService;
import idas.chox.core.services.TaskService;

public class SwitchClaimToMultipleInsurer extends BaseActivity {

    private static final Logger LOG = LoggerFactory.getLogger(SwitchClaimToMultipleInsurer.class);
    private int insId;
    private String policyNumber;
    private InsurerService insurerService;
    private AuditTrailService auditTrailService;
    private CommentService commentService;
    private TaskService taskService;
    private NotificationService notificationService;
    private Insurer newInsurer;

    public String getPolicyNumber() {
        return policyNumber;
    }
    
    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }
    
    public int getInsId() {
        return insId;
    }
    
    public void setInsId(int insId) {
        this.insId = insId;
    }
    
    public void setAuditTrailService(AuditTrailService auditTrailService) {
        this.auditTrailService = auditTrailService;
    }
    
    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }
    
    public void setInsurerService(InsurerService insurerService) {
        this.insurerService = insurerService;
    }
    
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }
    
    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);

        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        if (!securityInfoProvider.getIsCHOXAdmin() && !securityInfoProvider.isInRoleOf(WebUserRole.ROLE_CHO)) {
            throw new AccessDeniedException("Not in correct role to switch claim.");
        }

        LOG.debug("insurer id is  '{}' ", insId);
        LOG.debug("insurer service class is {}", insurerService.toString());
        newInsurer = insurerService.getInsurer(insId);
        if (newInsurer == null) {
            LOG.error("user trying to Switching claim {} with invalid insurer id {}", claim.getChoReference(), insId);
            throw new AccessDeniedException("Cannot switch claim as provided insurer id is not valid.");
        }
        if (ClaimType.isSubscriber(claim.getClaimType())) {
            // Make sure the new Insurer accepts subscriber claims
            if (!newInsurer.isAllowSubscriberClaims()) {
                LOG.error("The selected Insurer '{}' does not allow Subscriber claims.", newInsurer.getName());
                throw new Exception("The selected Insurer does not allow Subscriber claims.");
            }
        }
        else if (ClaimType.isFixedFee(claim.getClaimType())) {
            // Make sure the new Insurer accepts fixed fee claims
            if (!newInsurer.isAllowFixedFeeClaims()) {
                LOG.error("The selected Insurer '{}' does not allow Fixed Fee claims.", newInsurer.getName());
                throw new Exception("The selected Insurer does not allow Fixed Fee claims.");
            }
        }
    }
    
    @Override
    protected void doProcess(Claim claim) {

        LOG.debug("Switching claim with CHO reference '{}' to {}", claim.getChoReference(), newInsurer.getName());

        claim.setInsurer(newInsurer);
        claim.setClaimOwner(null);
        claim.setWorkgroup(null);
        claim.setPreviousStatus(null);
        claim.setLiabilityStatus(LiabilityStatus.LIABILITY_NULL);
        claim.setPercentageLiabilityCho(BigDecimal.ZERO);
        claim.setPercentageLiabilityAccepted(BigDecimal.ZERO);
        claim.setLiabilityAgreedDate(null);
        claim.setCreatedDate(new Date());

        LOG.debug("Switching Claim : Claim details has been updated");

        notificationService.removeAllNotifications(claim.getId());
        LOG.debug("Switching Claim : Claim notifications have been removed");

        // update Third party
        ThirdParty thirdParty = claim.getThirdParty();
        thirdParty.setInsurer(newInsurer);
        thirdParty.setInsurerBrand(newInsurer.getName());
        if(!thirdParty.getPolicyNumber().equalsIgnoreCase(policyNumber)){
            thirdParty.setPolicyNumber(policyNumber);
        }
        LOG.debug("Switching Claim: ThirdParty has been updated");
        // revert all Audits entries
        auditTrailService.revertAllAuditEntriesByClaimId(claim.getId());
        // revert all Comments entries
        commentService.deleteAllCommentsByClaimId(claim.getId());
        // delete all Tasks entries
        taskService.deleteAllTasksByClaimId(claim.getId());
        
        if (claim.getInvoice() != null) {
            LOG.debug("This claim has invoice and will be deleted as switching the claim to another insurer");
            Invoice oldInvoice = claim.getInvoice();
            claim.setInvoice(null);
            LOG.debug("claim invoice set to null");
            getDataService().delete(oldInvoice);
            LOG.warn("claim invoice deleted");
        }
        LOG.debug("Switching Claim: claim details has been updated");
    }

    @Override
    protected void afterProcess(Claim claim) throws Exception {
        /*
         * Rather than calling super.afterProcess(), we'll process the next activity (NewClaim) ourselves.
         * This prevents the claim being saved and the transaction logged
         */
        claim.setStatus(null);
        if (getChainActivity() != null) {
            LOG.debug("Processing next chain activity.");
            getChainActivity().setWorkflowContext(getProcessContext());
            getChainActivity().processInBatch(claim);
        }
    }

    
    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        expectingStatuses.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        expectingStatuses.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED);
        expectingStatuses.add(ClaimStatus.CLAIM_PENDING);
        expectingStatuses.add(ClaimStatus.CLAIM_REJECTED);
        expectingStatuses.add(ClaimStatus.SUBSCRIBER_CLAIM_REJECTED);
        expectingStatuses.add(ClaimStatus.CLAIM_REJECTION_CONTESTED);
        expectingStatuses.add(ClaimStatus.CLAIM_REFERRED_TO_FNOL);
        expectingStatuses.add(ClaimStatus.CLAIM_UPDATE_BY_ENG);
        expectingStatuses.add(ClaimStatus.CLAIM_REJECTION_ACCEPTED);
        expectingStatuses.add(ClaimStatus.CLAIM_CLOSED);
        expectingStatuses.add(ClaimStatus.CLAIM_REF_TO_ENG);
        expectingStatuses.add(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        expectingStatuses.add(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA);

 
    }


    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

}
