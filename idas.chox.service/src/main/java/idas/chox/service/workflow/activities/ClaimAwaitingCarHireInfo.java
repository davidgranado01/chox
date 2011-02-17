package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.security.SecurityInfoProvider;
import java.util.List;
import org.springframework.security.AccessDeniedException;

public class ClaimAwaitingCarHireInfo extends BaseActivity {

    private StringBuffer sb = new StringBuffer();

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);

        // VALIDATE HIRE MORNITORING ECD, MUST HAVE AT LEAST ONE ECD
        if (claim.getCustomer() == null || claim.getCustomer().getInitialECD() == null) {
            if (claim.getHireMonitoringEcds().isEmpty()) {
                sb.append("* You need to provide an Estimated Completion Date (ECD) to submit this claim.");
               // sb.append("\n.");

//                throw new Exception("Error : You need to provide an Estimated Completion Date (ECD) to submit this claim.");
            }
        }

        // LABOUR HOUR OR TOTAL LABOUT COSE MUST EXIST
        if (!isHireMonitoringLabourDetailExist(claim)) {
            sb.append("* In order to progress the claim, entries in either 'Labour Hours' or 'Total Labour Cost' fields are required,.if this information cannot be provided please select the reason why using the 'Labour Information Non-Provision Reason' drop down box.");
            //sb.append("\n.");
           // throw new Exception();
        }
        if (!claim.getHireMonitoringDetail().isIsRepairOnlyCheck()) {
            if (claim.getHireMonitoringDetail().isIsTotalLostCheck()) {
                if (!isRequiredFieldPresentWhenTotalLossChecked(claim)) {
                    throw new Exception(sb.toString());
                }
            } else {
                if(!isRequiredFieldPresentWhenTotalLossUnChecked(claim)){
                    throw new Exception(sb.toString());
                }
            }
        }
        if(sb.toString().equals("")||sb.toString()==null){
            throw new Exception(sb.toString());
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

            if (claim.getHireMonitoringDetail().getLabourCost() == null && claim.getHireMonitoringDetail().getLabourHour() == null && nonProvisionReason.length() == 0 && !claim.getHireMonitoringDetail().isIsTotalLostCheck()) {
                return false;
            }

        }

        return true;
    }

    public boolean isRequiredFieldPresentWhenTotalLossUnChecked(Claim claim) {
        boolean result = true;
        sb.append("* For Hires That Involved A Repair The Following Fields Are Required.");
        if (claim.getHireMonitoringDetail().getInspectionBookedDate() == null) {
            sb.append("'Inspection Booked Date'.");
            result = false;
        }
        if (claim.getHireMonitoringDetail().getInspectionDate() == null) {
            sb.append("'Inspection Date'.");
            result = false;
        }
        if (claim.getHireMonitoringDetail().getRepairAuthorisedDate() == null) {
            sb.append("'Date Repair Authorised'.");
            result = false;
        }
        if (claim.getHireMonitoringDetail().getRepairBookInDate() == null) {
            sb.append("'Repair Book In Dates'.");
            result = false;
        }
        if (claim.getHireMonitoringDetail().getRepairCommencedDate() == null) {
                sb.append("'Date Repair Commenced'.");
            result = false;
        }
        if (claim.getHireMonitoringDetail().getRepairCompletionDate() == null) {
            sb.append("'Repair Completion Date'.");
            result = false;
        }
        return result;
    }

    public boolean isRequiredFieldPresentWhenTotalLossChecked(Claim claim) {
        boolean result = true;
           sb.append("* For Hires That Involved A Total Loss The Following Fields Are Required.");
        if (claim.getHireMonitoringDetail().getTotalLossOfferMadeDate() == null) {
            sb.append("'Date Total Loss Offer Made'.");
            result = false;
        }
        if (claim.getHireMonitoringDetail().getTotalLossOfferAcceptedDate() == null) {
            sb.append("'Date Total Loss Offer Accepted'.");
            result = false;
        }
        if (claim.getHireMonitoringDetail().getTotalLossOfferCheckIssuedDate() == null) {
            sb.append("'Date Total Loss Cheque Issued'.");
            result = false;
        }
        if (claim.getHireMonitoringDetail().getTotalLossOfferCheckReceivedDate() == null) {
            sb.append("'Repair Book In Dates'.");
            result = false;
        }

        return result;
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
    }
}
