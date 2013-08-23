package idas.chox.service.workflow.activities;

import org.springframework.security.access.AccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Comment;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.Workgroup;

public class AssignManualInvoiceOwner extends BaseActivity {

    private static final Logger LOG = LoggerFactory.getLogger(AssignManualInvoiceOwner.class);
    private int oasWorkgroupId;
    private int claimOwnerId;
    private WebUser claimOwner;
    private Workgroup workgroup;
    private boolean workgroupsEnabled;
    private boolean ownershipEnabled;
    private int workgroupId;

    // <editor-fold defaultstate="collapsed" desc="Parameter Getters">
    public WebUser getClaimOwner() {
        return claimOwner;
    }

    public Workgroup getWorkgroup() {
        return workgroup;
    }

    public boolean isWorkgroupsEnabled() {
        return workgroupsEnabled;
    }

    public boolean isOwnershipEnabled() {
        return ownershipEnabled;
    }

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
        if (claim.getInsurer().isEnableManualInvoiceWorkgroups() && claim.getInsurer().isWorkgroupEnable()) {
            workgroupsEnabled = true;
        }
        if (claim.getInsurer().isEnableManualInvoiceOwnership() && claim.getInsurer().isClaimOwnershipEnable()) {
            ownershipEnabled = true;
        }

        if (workgroupsEnabled && oasWorkgroupId <= 0) {
            throw new Exception("Invalid workgroup id. workgroupId : " + oasWorkgroupId);
        } else if (workgroupsEnabled) {
            workgroup = (Workgroup) getDataService().get(Workgroup.class, oasWorkgroupId);
            if (workgroup == null) {
                throw new Exception("Invalid workgroup id. workgroup is null.");
            }
        }

        if (ownershipEnabled && claimOwnerId <= 0) {
            throw new Exception("Invalid user id. id : " + claimOwnerId);
        } else if (ownershipEnabled) {
            claimOwner = (WebUser) getDataService().get(WebUser.class, claimOwnerId);
            if (claimOwner == null) {
                throw new Exception("Invalid user id. claimOwner is null");
            }
            // Check if user belongs to the Insurer
            if (claimOwner.getInsurer().getId().intValue() != claim.getInsurer().getId().intValue()) {
                throw new AccessDeniedException("The selected Claim Owner does not belong to the Insurer of the claim.");
            }
        }

    }

    @Override
    protected void doProcess(Claim claim) throws Exception {
        boolean updateOnly = false;

        if (!claim.getStatus().equals(ClaimStatus.MANUAL_INVOICE_UNASSIGNED)) {
            updateOnly = true;
        }
        LOG.debug("update only {}.", updateOnly);


        WebUser oldClaimOwner = null;
        String oldClaimOwnerName = "-";
        if (claim.getClaimOwner() != null) {
            oldClaimOwner = claim.getClaimOwner();
            oldClaimOwnerName = oldClaimOwner.getFullName();
            LOG.debug("Old Claim Owner full name", oldClaimOwnerName);
        } else {
            LOG.debug("Old Claim Owner is null");
        }

        if (ownershipEnabled) {
            claim.setClaimOwner(claimOwner);
            LOG.debug("Changed(Current) Claim Owner full name", claim.getClaimOwner().getFullName());
        } else {
            LOG.debug("Claim Ownership is not enabled for this insurer.");
        }

        if (workgroupsEnabled) {
            claim.setWorkgroup(workgroup);
        } else {
            LOG.debug("Workgroup is not enabled for this insurer.");
        }

        if (!updateOnly) {
            if (claim.isManualInvoiceApproved()) {
                claim.setStatus(ClaimStatus.MANUAL_INVOICE_APPROVED);
            } else {
                claim.setStatus(ClaimStatus.MANUAL_INVOICE_REJECTED);
            }
        } else if (claim.getClaimType() == ClaimType.INSURER_CLAIM && (claim.getStatus().equals(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED)
                || claim.getStatus().equals(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED))) {
            claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        }


        if (ownershipEnabled && updateOnly && !claimOwner.equals(oldClaimOwner) && claimOwner.getTelephone() != null && claimOwner.getTelephone().length() > 0) {
            LOG.debug("Adding Comment for the change of insurer Claim owner");
            String noteMsg = "Insurer Claims Handler changed from '" + oldClaimOwnerName + "' to '" + claimOwner.getFullName() + "' (contact number: " + claimOwner.getTelephone() + ").";
            Comment comment = Comment.newComment(0, noteMsg);
            claim.addComment(comment);
        } else if (ownershipEnabled && updateOnly && !claimOwner.equals(oldClaimOwner)) {
            LOG.debug("Adding Comment for the change of insurer Claim owner");
            String noteMsg = "Insurer Claims Handler changed from '" + oldClaimOwnerName + "' to '" + claimOwner.getFullName() + "'.";
            Comment comment = Comment.newComment(0, noteMsg);
            claim.addComment(comment);
        } else if (!updateOnly && ownershipEnabled && claimOwner.getTelephone() != null && claimOwner.getTelephone().length() > 0) {
            LOG.debug("Adding Comment for the new insurer Claim owner");
            Comment comment = Comment.newComment(0, "Insurer Claims Handler is '" + claimOwner.getFullName() + "' (contact number: " + claimOwner.getTelephone() + ").");
            claim.addComment(comment);
        } else {
            LOG.debug("Adding Comment for the new insurer Claim owner");
            Comment comment = Comment.newComment(0, "Insurer Claims Handler is '" + claimOwner.getFullName() + "'.");
            claim.addComment(comment);
        }
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

    public int getOasWorkgroupId() {
        return oasWorkgroupId;
    }

    public void setOasWorkgroupId(int oasWorkgroupId) {
        this.oasWorkgroupId = oasWorkgroupId;
    }

    public int getWorkgroupId() {
        return workgroupId;
    }

    public void setWorkgroupId(int workgroupId) {
        this.oasWorkgroupId = workgroupId;
    }
}
