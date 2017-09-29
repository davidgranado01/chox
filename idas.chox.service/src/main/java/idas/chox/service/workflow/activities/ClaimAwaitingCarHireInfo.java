package idas.chox.service.workflow.activities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.HireMonitoringDetail;

public class ClaimAwaitingCarHireInfo extends BaseActivity {
    private static final Logger LOG = LoggerFactory.getLogger(ClaimAwaitingCarHireInfo.class);

    @Override
    protected void beforeProcess(Claim claim) {
        if (claim.getHireMonitoringDetail() != null && claim.getCustomer() != null && claim.getCustomer().getIsTotalLoss() != null) {
            if (claim.getCustomer().getIsTotalLoss() != claim.getHireMonitoringDetail().isIsTotalLostCheck()) {
                claim.getHireMonitoringDetail().setIsTotalLostCheck(claim.getCustomer().getIsTotalLoss());
            }
        }
    }
    
    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
        StringBuilder sb = new StringBuilder();

        // VALIDATE HIRE MORNITORING ECD, MUST HAVE AT LEAST ONE ECD (initial or added)
        if (!ClaimType.isInsurerUpload(claim.getClaimType()) && (claim.getCustomer() == null || claim.getCustomer().getInitialECD() == null) && claim.getHireMonitoringEcds().isEmpty()) {
                sb.append("* You need to provide an Estimated Completion Date (ECD) in order to proceed this claim.\n");
        }
        

        if (!ClaimType.isInsurerUpload(claim.getClaimType()) && !isHireMonitoringLabourDetailCorrect(claim)) {
            sb.append("* In order to progress the claim, entries in either 'Labour Hours' or 'Total Labour Cost' fields are required. If this information cannot be provided please select the reason why using the 'Labour Information Non-Provision Reason' drop down box.");
        }

        if (sb.length() > 0) {
            LOG.debug("throwing validation exception error {} for claim {}", sb, claim.getChoReference());
            throw new Exception(sb.toString());
        }

    }


    @Override
    protected void doProcess(Claim claim) {
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA);
    }

    
    private boolean isHireMonitoringLabourDetailCorrect(Claim claim) {

        if (claim.getHireMonitoringDetail() == null) {
            if (isXmlActivityProcessing()) {
                HireMonitoringDetail hireMonitoringdtl = new HireMonitoringDetail();
                hireMonitoringdtl.setNonProvisionReason("Information Not Available/No System Access");
                claim.setHireMonitoringDetail(hireMonitoringdtl);
                return true;
            } else {
                return false;
            }

        }

        String nonProvisionReason = "";
        if (claim.getHireMonitoringDetail().getNonProvisionReason() != null) {
            nonProvisionReason = claim.getHireMonitoringDetail().getNonProvisionReason().trim();
        }

        /*
         * If there is no labour cost or labour hours or non provision reason....
         */
        if (claim.getHireMonitoringDetail().getLabourCost() == null && claim.getHireMonitoringDetail().getLabourHour() == null
                && nonProvisionReason.length() == 0) {
            if (isXmlActivityProcessing()) {
            /*
             *  ...and this is off-hired (processed via XML upload), then we
             *  need to set the non-provision reason
             */
                claim.getHireMonitoringDetail().setNonProvisionReason("Information Not Available/No System Access");
            }
            else if (!claim.getHireMonitoringDetail().isIsTotalLostCheck()) {
                // else if this isn't a total loss check, then return an error
                return false;
            }
        }

        return true;
    }
    
}
