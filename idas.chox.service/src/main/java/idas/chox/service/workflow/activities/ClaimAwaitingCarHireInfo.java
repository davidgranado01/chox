package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.security.SecurityInfoProvider;
import java.util.List;
import org.springframework.security.AccessDeniedException;

public class ClaimAwaitingCarHireInfo extends BaseActivity {

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);

        // VALIDATE HIRE MORNITORING ECD, MUST HAVE AT LEAST ONE ECD
        if (claim.getCustomer() == null || claim.getCustomer().getInitialECD() == null) {
            if (claim.getHireMonitoringEcds().size() == 0) {
                throw new Exception("Error : You need to provide an Estimated Completion Date (ECD) to submit this claim.");
            }
        }

        // LABOUR HOUR OR TOTAL LABOUT COSE MUST EXIST
        if (!isHireMonitoringLabourDetailExist(claim)) {
            throw new Exception("Error : In order to progress the claim, entries in either 'Labour Hours' or 'Total Labour Cost' fields are required, if this information cannot be provided please select the reason why using the 'Labour Information Non-Provision Reason' drop down box.");
        }
        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        if (!securityInfoProvider.isInRoleOf("ROLE_CHO")  && !securityInfoProvider.getIsCHOXAdmin()) {
            throw new AccessDeniedException("Not in correct role to add car hire info.");
        }

    }

    @Override
    protected void doProcess(Claim claim) {
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA);
    }

    public boolean isHireMonitoringLabourDetailExist(Claim claim) {

        if (claim.getHireMonitoringDetail() == null) {

            return false;

        } else {

            String nonProvisionReason = "";
            if (claim.getHireMonitoringDetail().getNonProvisionReason() != null) {
                nonProvisionReason = claim.getHireMonitoringDetail().getNonProvisionReason().trim();
            }

            if (claim.getHireMonitoringDetail().getLabourCost() == null && claim.getHireMonitoringDetail().getLabourHour() == null && nonProvisionReason.length() == 0 && !claim.getHireMonitoringDetail().isIsTotalLostCheck()) {
                return false;
            }

        }

        return true;
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
    }
}
