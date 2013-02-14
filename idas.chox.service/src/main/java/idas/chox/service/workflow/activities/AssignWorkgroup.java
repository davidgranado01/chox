package idas.chox.service.workflow.activities;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Workgroup;
import idas.chox.core.security.SecurityInfoProvider;

public class AssignWorkgroup extends BaseActivity {
    private static final Logger LOG = LoggerFactory.getLogger(AssignWorkgroup.class);

    private int workgroupId;
    private Workgroup workgroup;

    @Override
    protected void validate(Claim claim) throws Exception {

        try {
            super.validate(claim);
            LOG.debug("AssignWorkgroup Activity validation: workgroupId='{}'", workgroupId);
            if (workgroupId <= 0) {
                throw new Exception("Invalid workgroup id.");
            } else {
                workgroup = (Workgroup) this.getWorkflowContext().getDataService().get(Workgroup.class, workgroupId);
                LOG.debug("validate assign workgroup " + workgroup.getName());
                if (workgroup == null) {
                    throw new Exception("An attempt to assign work group failed due to invalid workgroup provided");
                }
                // Check workgroup belongs to the Insurer
                if (workgroup.getInsurer().getId().intValue() != claim.getInsurer().getId().intValue()) {
                    throw new AccessDeniedException("Workgroup does not belong to Insurer");
                }
            }
            SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
            if (!securityInfoProvider.isInRoleOf("ROLE_INS_CR") && !securityInfoProvider.isInRoleOf("ROLE_INS_MNG")
                    && !securityInfoProvider.getIsCHOXAdmin() && !securityInfoProvider.isInRoleOf("ROLE_INS_COM")) {
                throw new AccessDeniedException("Not in correct role to assign workgroup.");
            }

        } catch (Exception e) {
            LOG.error("Exception thrown: {}", e.getMessage());
            throw e;
        }

    }

    @Override
    protected void beforeProcess(Claim claim) throws Exception {
        try {
            claim.setWorkgroup(workgroup);
        } catch (Exception e) {
            LOG.error("Exception thrown: {}", e.getMessage());
            throw e;
        }

    }

    @Override
    protected void doProcess(Claim claim) throws Exception {

        if (claim.getStatus().equals(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED)) {
            try {
                if (claim.getInsurer().isClaimOwnershipEnable()) {
                    claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED);
                } else {
                    claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
                }
            } catch (Exception e) {
                LOG.error("Exception thrown: {}", e.getMessage());
                throw e;

            }
        } 

    }

    public void setWorkgroupId(int workgroupId) {
        this.workgroupId = workgroupId;
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        expectingStatuses.add(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        expectingStatuses.add(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA);
        expectingStatuses.add(ClaimStatus.AWAITING_INVOICE_PAYMENT);
        expectingStatuses.add(ClaimStatus.AWAITING_LIABILITY_RESOLUTION);
        expectingStatuses.add(ClaimStatus.AWAITING_LITIGATION_OUTCOME);
        expectingStatuses.add(ClaimStatus.CLAIM_PENDING);
        expectingStatuses.add(ClaimStatus.CLAIM_REF_TO_ENG);
        expectingStatuses.add(ClaimStatus.CLAIM_REFERRED_TO_FNOL);
        expectingStatuses.add(ClaimStatus.CLAIM_REJECTED);
        expectingStatuses.add(ClaimStatus.CLAIM_REJECTION_CONTESTED);
        expectingStatuses.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        expectingStatuses.add(ClaimStatus.CLAIM_UPDATE_BY_ENG);
        expectingStatuses.add(ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO);
        expectingStatuses.add(ClaimStatus.CONTESTED_INVOICE_REF_TO_INS);
        expectingStatuses.add(ClaimStatus.INVOICE_APPROVED_BY_BRE);
        expectingStatuses.add(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT);
        expectingStatuses.add(ClaimStatus.INVOICE_ESCALATED);
        expectingStatuses.add(ClaimStatus.INVOICE_ESCALATED_TO_CH);
        expectingStatuses.add(ClaimStatus.INVOICE_PAYMENT_LOGGED);
        expectingStatuses.add(ClaimStatus.INVOICE_REF_TO_CH);
        expectingStatuses.add(ClaimStatus.INVOICE_REF_TO_ENG);
    }
}
