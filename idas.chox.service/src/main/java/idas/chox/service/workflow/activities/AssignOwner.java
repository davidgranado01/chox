package idas.chox.service.workflow.activities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Comment;
import idas.chox.core.model.LiabilityStatus;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.Workgroup;

public class AssignOwner extends BaseActivity {

    private static final Logger LOG = LoggerFactory.getLogger(AssignOwner.class);

    private int oasWorkgroupId;
    private int claimOwnerId;
    private WebUser claimOwner;
    private Workgroup workgroup;
    private boolean workgroupsEnabled;
    private boolean ownershipEnabled;

    // <editor-fold defaultstate="collapsed" desc="Parameter Getters">
    public WebUser getClaimOwner() {
        return claimOwner;

    }

    public Workgroup getWorkgroup() {
        return workgroup;
    }

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
        if (claim.getClaimType() == ClaimType.INSURER_INVOICE) {
            workgroupsEnabled = claim.getInsurer().isEnableManualInvoiceWorkgroups();
            ownershipEnabled = claim.getInsurer().isEnableManualInvoiceOwnership();
        } else {
            workgroupsEnabled = claim.getInsurer().isWorkgroupEnable();
            ownershipEnabled = claim.getInsurer().isClaimOwnershipEnable();
        }

        if (!workgroupsEnabled && !ownershipEnabled)  {
            throw new Exception("Both workgroup and ownership are disabled.");
        }

        // moved out of the first 'if' of this method to maintain backward compatibility
        if (claim.getClaimType() == ClaimType.INSURER_INVOICE &&
                !workgroupsEnabled && claim.getWorkgroup() != null && oasWorkgroupId > 0) {
            workgroupsEnabled = true;
        }

        if (workgroupsEnabled && oasWorkgroupId <= 0) {
            LOG.error("No workgroup specified for claim '{}' ({}): workgroupId={}", new Object[]{claim.getChoReference(), claim.getId(), oasWorkgroupId});
            throw new Exception("No workgroup specified");
        } else if (workgroupsEnabled) {
            workgroup = (Workgroup) getDataService().get(Workgroup.class, oasWorkgroupId);
            if (workgroup == null) {
                LOG.error("No such workgroup with id={}", oasWorkgroupId);
                throw new Exception("No such workgroup.");
            }
            // Check workgroup belongs to the Insurer
            if (workgroup.getInsurer().getId().intValue() != claim.getInsurer().getId().intValue()) {
                throw new AccessDeniedException("Workgroup does not belong to Insurer");
            }
        }

        if (ownershipEnabled && claimOwnerId <= 0) {
            LOG.error("No owner specified for claim '{}' ({}) with workgroupId={}: claimOwnerId={}", new Object[]{claim.getChoReference(), claim.getId(), oasWorkgroupId, claimOwnerId});
            throw new Exception("No owner specified");
        } else if (ownershipEnabled) {
            claimOwner = (WebUser) getDataService().get(WebUser.class, claimOwnerId);
            if (claimOwner == null) {
                LOG.error("No such user with id={}", claimOwnerId);
                throw new Exception("No such Claim Owner");
            }
            // Check user belongs to the Insurer
            if (claimOwner.getInsurer().getId().intValue() != claim.getInsurer().getId().intValue()) {
                throw new AccessDeniedException("The selected Claim Owner does not belong to the Insurer of the claim.");
            }

            //check user belongs to workgroup, if any - no error message needed because it is a common user error)
            if (workgroupsEnabled && workgroup != null){
                if (! isUserInWorkgroup(workgroup.getId(), claimOwnerId) ){
                    //LOG.warn("Web user {} does not belong to workgroup={}", workgroup.getId(), claimOwnerId);
                    throw new AccessDeniedException("The selected Claim Owner does not belong to the selected Workgroup.");
                }
            }

        }
    }

    @Override
    protected void doProcess(Claim claim) throws Exception {
        String oldOwnerName = null;
        if (claim.getClaimOwner() != null) {
            oldOwnerName = claim.getClaimOwner().getFullName();
        }

        if (ownershipEnabled) {
            claim.setClaimOwner(claimOwner);
        }

        if (workgroupsEnabled) {
            claim.setWorkgroup(workgroup);
            if (workgroup.isStpExcluded() && claim.getInvoice() != null && claim.getInvoice().isPaymentTeam()) {
                claim.getInvoice().setPaymentTeam(false);
            } else if (!workgroup.isStpExcluded()
                    && claim.getInvoice() != null && claim.getBreBand().isPaymentTeamActive()
                    && ((claim.getInsurer().isTpiPaymentsTeamEnable() && ClaimType.isTPI(claim.getClaimType()))
                    || (claim.getInsurer().isInsurerManualPaymentsTeamEnable() && ClaimType.isInsurerUpload(claim.getClaimType()))
                    || (claim.getInsurer().isFixedFeePaymentsTeamEnable() && ClaimType.isFixedFee(claim.getClaimType()))
                    || (claim.getInsurer().isSubscriberPaymentsTeamEnable() && ClaimType.isSubscriber(claim.getClaimType()))
                    || (claim.getInsurer().isGtaPaymentsTeamEnable() && ClaimType.isGTA(claim.getClaimType()))
                    || (claim.getInsurer().isInsurerVsInsurerPaymentsTeamEnable() && ClaimType.isInsurerVsInsurer(claim.getClaimType()))
                    || (claim.getInsurer().isCollaborationPaymentsTeamEnable() && ClaimType.isCollaborationProtocol(claim.getClaimType())))) {
                claim.getInvoice().setPaymentTeam(true);
            }
        }
        if (oldOwnerName == null) { // need to update status
            if (claim.getStatus().equals(ClaimStatus.MANUAL_INVOICE_UNASSIGNED)) {
                if (claim.isManualInvoiceApproved() && claim.getInvoice().isPaymentTeam()) {
                    claim.setPreviousStatus(claim.getStatus());
                    claim.setStatus(ClaimStatus.MANUAL_INVOICE_APPROVED);
                    setCurrentStatus(claim.getStatus());
                    logTransaction(claim, claim.getPreviousStatus(), claim.getStatus(), -50);
                    // move claim to next status
                    claim.setPreviousStatus(getCurrentStatus());

                    if (claim.getLiabilityStatus() == LiabilityStatus.LIABILITY_NULL
                            || claim.getLiabilityStatus() == LiabilityStatus.LIABILITY_UNKNOWN
                            || claim.getLiabilityStatus() == LiabilityStatus.LIABILITY_DISPUTED
                            || claim.getLiabilityStatus() == LiabilityStatus.LIABILITY_REPUDIATED) {
                        claim.setStatus(ClaimStatus.AWAITING_LIABILITY_RESOLUTION);
                    } else {
                        claim.setStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT);
                    }
                } else if (claim.isManualInvoiceApproved()) {
                    claim.setStatus(ClaimStatus.MANUAL_INVOICE_APPROVED);
                } else {
                    claim.setStatus(ClaimStatus.MANUAL_INVOICE_REJECTED);
                }
            } else if (ClaimType.isInsurerUpload(claim.getClaimType())) {
                if (claim.getStatus().equals(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED)
                        || claim.getStatus().equals(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED)) {
                    claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
                }
            } else if (!ClaimType.isTPI(claim.getClaimType())) {
                claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
            } else if (ClaimStatus.INVOICE_APPROVED_BY_BRE.equals(claim.getTpiClaimStatus())
                    && claim.getInvoice().isPaymentTeam()) {
                if (!ClaimType.isInsurerVsInsurer(claim.getClaimType())
                        && (claim.getLiabilityStatus() == LiabilityStatus.LIABILITY_NULL
                        || claim.getLiabilityStatus() == LiabilityStatus.LIABILITY_UNKNOWN
                        || claim.getLiabilityStatus() == LiabilityStatus.LIABILITY_DISPUTED
                        || claim.getLiabilityStatus() == LiabilityStatus.LIABILITY_REPUDIATED)) {
                    claim.setStatus(ClaimStatus.AWAITING_LIABILITY_RESOLUTION);
                } else {
                    claim.setStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT);
                }
            } else {
                claim.setStatus(claim.getTpiClaimStatus());
            }
        }

        if (ownershipEnabled) {
            Comment comment = null;
            if (oldOwnerName == null && claimOwner.getTelephone() != null && claimOwner.getTelephone().length() > 0) {
                comment = Comment.newComment(0, "Insurer Claims Handler is '" + claimOwner.getFullName() + "' (contact number: " + claimOwner.getTelephone() + ").", true);
            } else if (oldOwnerName == null && (claimOwner.getTelephone() == null || claimOwner.getTelephone().length() == 0)) {
                comment = Comment.newComment(0, "Insurer Claims Handler is '" + claimOwner.getFullName() + "'.", true);
            } else if (oldOwnerName != null && !oldOwnerName.equals(claimOwner.getFullName()) && claimOwner.getTelephone() != null && claimOwner.getTelephone().length() > 0) {
                comment = Comment.newComment(0, "Insurer Claims Handler changed from '" + oldOwnerName + "' to '" + claimOwner.getFullName() + "' (contact number: " + claimOwner.getTelephone() + ")", true);
            } else if (oldOwnerName != null && oldOwnerName.equals(claimOwner.getFullName()) && workgroupsEnabled) {
                comment = Comment.newComment(0, "Insurer Claims Handler workgroup changed to '" + workgroup + "'", true);
            } else if (oldOwnerName != null && !oldOwnerName.equals(claimOwner.getFullName())) {
                comment = Comment.newComment(0, "Insurer Claims Handler changed from '" + oldOwnerName + "' to '" + claimOwner.getFullName() + "'", true);
            } else {
                LOG.error("This should never be reached: oldOwnerName='{}', new owner id='{}' (telephone='{}', workgroupsEnabled={})",
                        new Object[]{oldOwnerName, claimOwner.getId(), claimOwner.getTelephone(), workgroupsEnabled});
            }
            claim.addComment(comment);
        }
    }

    public int getOasWorkgroupId() {
        return oasWorkgroupId;
    }

    public void setOasWorkgroupId(int oasWorkgroupId) {
        this.oasWorkgroupId = oasWorkgroupId;
    }

    public void setWorkgroupId(int oasWorkgroupId) {
        this.oasWorkgroupId = oasWorkgroupId;
    }

    public int getClaimOwnerId() {
        return claimOwnerId;
    }

    public void setClaimOwnerId(int claimOwnerId) {
        this.claimOwnerId = claimOwnerId;
    }

    public void setClaimOwnerIdField(int claimOwnerIdField) {
        this.claimOwnerId = claimOwnerIdField;
    }

    public void setWorkgroupIdField(int workgroupIdField) {
        this.oasWorkgroupId = workgroupIdField;
    }
}
