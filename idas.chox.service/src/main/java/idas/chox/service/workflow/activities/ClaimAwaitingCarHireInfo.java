package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.security.SecurityInfoProvider;
import java.util.List;
import org.springframework.security.AccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClaimAwaitingCarHireInfo extends BaseActivity {

    private static final Logger LOG = LoggerFactory.getLogger(ClaimAwaitingCarHireInfo.class);
    private StringBuffer sb = new StringBuffer(550);
    private StringBuffer sb1 = new StringBuffer(150);

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);

        // VALIDATE HIRE MORNITORING ECD, MUST HAVE AT LEAST ONE ECD
        if (claim.getCustomer() == null || claim.getCustomer().getInitialECD() == null) {
            if (claim.getHireMonitoringEcds().isEmpty()) {
                sb1.append("* You need to provide an Estimated Completion Date (ECD) in order to proceed this claim.");
            }
        }

        if (!isHireMonitoringLabourDetailExist(claim)) {
            sb1.append("* In order to progress the claim, entries in either 'Labour Hours' or 'Total Labour Cost' fields are required,.if this information cannot be provided please select the reason why using the 'Labour Information Non-Provision Reason' drop down box.");
        }
        if (claim.getHireMonitoringDetail() != null && !claim.getHireMonitoringDetail().isIsRepairOnlyCheck()) {
            // Do nothing now
        } else if (claim.getHireMonitoringDetail() == null) {
            sb1.append("* For hires that involved a repair the following fields are required: .");
            sb1.append("'Date Repair Authorised' .");
            sb1.append("'Repair Completion Date' .");
            throw new Exception(sb1.toString());
        }
        if (sb1.length() > 0) {
            LOG.debug("throwing validation exception error {} for claim {}", sb1, claim.getChoReference());
            throw new Exception(sb1.toString());
        }
        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        if (!securityInfoProvider.isInRoleOf("ROLE_CHO") && !securityInfoProvider.getIsCHOXAdmin()) {
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

            if (claim.getHireMonitoringDetail().getLabourCost() == null && claim.getHireMonitoringDetail().getLabourHour() == null && nonProvisionReason.length() == 0 && !claim.getHireMonitoringDetail().isIsTotalLostCheck() && !isXmlActivityProcessing()) {
                return false;
                /*
                 *  If this activity is processed via XML upload stage and no information provided in either of labour hour or cost or non provision reason is empty, set Non Provision Reson as "Information Not Available/No System Access".
                 */
            } else if (isXmlActivityProcessing() && nonProvisionReason.length() == 0) {
                if (claim.getHireMonitoringDetail().getLabourCost() == null) {
                    claim.getHireMonitoringDetail().setNonProvisionReason("Information Not Available/No System Access");
                } else if (claim.getHireMonitoringDetail().getLabourHour() == null) {
                    claim.getHireMonitoringDetail().setNonProvisionReason("Information Not Available/No System Access");
                }
            }
        }
        return true;
    }

    public boolean isRequiredFieldPresentWhenTotalLossUnChecked(Claim claim) {
        boolean result = true;
        sb.append(sb1.toString());
        sb.append("* For hires that involved a repair the following fields are required: .");
        if (claim.getHireMonitoringDetail().getRepairAuthorisedDate() == null) {
            sb.append("'Date Repair Authorised' .");
            result = false;
        }
        if (claim.getHireMonitoringDetail().getRepairCompletionDate() == null) {
            sb.append("'Repair Completion Date' .");
            result = false;
        }
        return result;
    }

    public boolean isRequiredFieldPresentWhenTotalLossChecked(Claim claim) {
        boolean result = true;
        sb.append(sb1.toString());
        sb.append("* For hires that involved a total loss the the following fields are required: .");
        if (claim.getHireMonitoringDetail().getTotalLossOfferMadeDate() == null) {
            sb.append("'Date Total Loss Offer Made' .");
            result = false;
        }
        return result;
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
    }
}
