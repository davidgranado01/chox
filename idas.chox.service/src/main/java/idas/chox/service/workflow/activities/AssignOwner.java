package idas.chox.service.workflow.activities;


import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Comment;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.Workgroup;

public class AssignOwner extends BaseActivity {

    private int oasWorkgroupId;
    private int claimOwnerId;
    private WebUser claimOwner;
    private Workgroup workgroup;
    private boolean workgroupsEnabled;

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

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
        workgroupsEnabled = claim.getInsurer().isWorkgroupEnable();

        if (workgroupsEnabled && oasWorkgroupId <= 0) {
            throw new Exception("Invalid workgroup id. workgroupId : " + oasWorkgroupId);
        } else if (workgroupsEnabled) {
            workgroup = (Workgroup) getDataService().get(Workgroup.class, oasWorkgroupId);
            if (workgroup == null) {
                throw new Exception("Invalid workgroup id. workgroup is null.");
            }
            // Check workgroup belongs to the Insurer
            if (workgroup.getInsurer().getId().intValue() != claim.getInsurer().getId().intValue()) {
                throw new AccessDeniedException("Workgroup does not belong to Insurer");
            }
        }

        if (claimOwnerId <= 0) {
            throw new Exception("Invalid user id. id : " + claimOwnerId);
        } else {
            claimOwner = (WebUser) getDataService().get(WebUser.class, claimOwnerId);
            if (claimOwner == null) {
                throw new Exception("Invalid user id. claimOwner is null");
            }
            // Check user belongs to the Insurer
            if (claimOwner.getInsurer().getId().intValue() != claim.getInsurer().getId().intValue()) {
                throw new AccessDeniedException("The selected Claim Owner does not belong to the Insurer of the claim.");
            }
        }

    }

    @Override
    protected void doProcess(Claim claim) throws Exception {
        claim.setClaimOwner(claimOwner);
        if (workgroupsEnabled) {
            claim.setWorkgroup(workgroup);
            if (ClaimType.isTPI(claim.getClaimType()) && workgroup.isStpExcluded() && claim.getInvoice() != null) {
                claim.getInvoice().setPaymentTeam(false);
            } else if (ClaimType.isTPI(claim.getClaimType()) && !workgroup.isStpExcluded()
                    && claim.getInvoice() != null && claim.getBreBand().isPaymentTeamActive()
                    && claim.getInsurer().isTpiPaymentsTeamEnable()) {
                claim.getInvoice().setPaymentTeam(true);
            }
        }
        if (!ClaimType.isTPI(claim.getClaimType())) {
            claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        } else if (ClaimStatus.INVOICE_APPROVED_BY_BRE.equals(claim.getTpiClaimStatus())
                && claim.getInvoice().isPaymentTeam()) {
            claim.setStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT);
        } else {
            claim.setStatus(claim.getTpiClaimStatus());
        }
        if (claimOwner.getTelephone() != null && claimOwner.getTelephone().length() > 0) {
            Comment comment = Comment.newComment(0, "Insurer Claims Handler is '" + claimOwner.getFullName() + "' (contact number: " + claimOwner.getTelephone() + ").");
            claim.addComment(comment);
        }
    }

    public int getOasWorkgroupId() {
        return oasWorkgroupId;
    }

    public void setOasWorkgroupId(int oasWorkgroupId) {
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
