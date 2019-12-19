package idas.chox.service.bre.rules;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.services.BankHolidayService;
import idas.chox.core.util.DateHelper;

public class GtaAmendTotalLossOwnInsurerRoadworthy implements IBusinessRule {

    BankHolidayService bankHolidayService;
    private String narrative = "";

    public void setBankHolidayService(BankHolidayService bankHolidayService) {
        this.bankHolidayService = bankHolidayService;
    }

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());

        Date firstJuly2019 = new Date();
        try {
            firstJuly2019 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse("2019-06-30 23:59:59.999");
        } catch (ParseException ex) {
            ; // Not reached
        }

        if (claim.getBreBand().isTotalLossOwnRoadworthyCheck() && !ClaimType.isCollaborationProtocol(claim.getClaimType())
                && !ClaimType.isSubscriber(claim.getClaimType()) && !ClaimType.isFixedFee(claim.getClaimType())
                && claim.getVehicleHire() != null && claim.getVehicleHire().getHireStart() != null && claim.getVehicleHire().getHireStart().after(firstJuly2019)
                && claim.getHireMonitoringDetail() != null && claim.getHireMonitoringDetail().getWhoIsSendingPav() != null  && claim.getHireMonitoringDetail().getWhoIsSendingPav().equals("Customers Own Insurer")
                && claim.getCustomer() != null && claim.getCustomer().getIsUsable() != null && claim.getCustomer().getIsUsable()
                && !claim.isManagingRepair() && claim.getHireMonitoringDetail() != null
                && claim.getHireMonitoringDetail().getRepairAuthorisedDate() != null && claim.getVehicleHire().getHireEnd() != null) {

            boolean success = true;

            // Calculate The number of working days between hire start date and ‘Date Repair Authorised/TL Identified’
            int noDays1 = DateHelper.getNumberOfWorkingDaysBetween(claim.getVehicleHire().getHireStart(), claim.getHireMonitoringDetail().getRepairAuthorisedDate());
            int noHolidays = bankHolidayService.getNoHolidaysBetween(claim.getVehicleHire().getHireStart(), claim.getHireMonitoringDetail().getRepairAuthorisedDate());
            noDays1 -= noHolidays;

            // Calculate The number of working days between the Date Repair Authorised/TL Identified’ and Hire-end date
            int noDays2 = DateHelper.getNumberOfWorkingDaysBetween(claim.getHireMonitoringDetail().getRepairAuthorisedDate(), claim.getVehicleHire().getHireEnd());
            noHolidays = bankHolidayService.getNoHolidaysBetween(claim.getHireMonitoringDetail().getRepairAuthorisedDate(), claim.getVehicleHire().getHireEnd());
            noDays2 -= noHolidays;

            if (noDays1 > claim.getBreBand().getTimeToAuthoriseRepair1() || noDays2 > claim.getBreBand().getTimeToOffHire1()) {
                success = false;
                narrative = "The hire period for a total loss exceeds the number of days allowed where the claimants own insurer is dealing and the vehicle is roadworthy.";
            }

            res.setResult(success ? RuleEvaluationResult.RULE_PASSED : RuleEvaluationResult.RULE_FAILED);

        } else {

            narrative = "";
            res.setResult(RuleEvaluationResult.RULE_SKIPPED);

        }

        return res;
    }

    @Override
    public String getNarrative() {
        return narrative;
    }

    @Override
    public String getRuleId() {
        return "101";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}
