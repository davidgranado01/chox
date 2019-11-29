package idas.chox.service.bre.rules;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.services.BankHolidayService;
import idas.chox.core.util.DateHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TotalLossPeriodWhereCHODealingAtFaultInsurerSendingPAVAndVehicleRoadworthyCheck implements IBusinessRule {

    BankHolidayService bankHolidayService;
    private String narrative = "";

    public void setBankHolidayService(BankHolidayService bankHolidayService) {
        this.bankHolidayService = bankHolidayService;
    }

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(true);    //CONFIRM??
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());


        Date firstJuly2019 = new Date();
        try {
            firstJuly2019 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse("2019-06-30 23:59:59.999");
        } catch (ParseException ex) {
            ; // Not reached
        }

        if (claim.getBreBand().isTotalLossChoAtFaultRoadworthyCheck()
                && claim.getVehicleHire() != null && claim.getVehicleHire().getHireStart() != null && claim.getVehicleHire().getHireStart().after(firstJuly2019)
                && claim.isManagingRepair()
                && claim.getHireMonitoringDetail() != null && claim.getHireMonitoringDetail().getWhoIsSendingPav().equals("At Fault Insurer")
                && claim.getCustomer() != null && claim.getCustomer().getIsUsable() != null && claim.getCustomer().getIsUsable()) {


            boolean success = true;

            // Calculate The number of working days between Hire Start Date and Date Repair Authorised/TL Identified
            int noDays1 = DateHelper.getNumberOfWorkingDaysBetween(claim.getVehicleHire().getHireStart(), claim.getHireMonitoringDetail().getRepairAuthorisedDate());
            int noHolidays = bankHolidayService.getNoHolidaysBetween(claim.getVehicleHire().getHireStart(), claim.getHireMonitoringDetail().getRepairAuthorisedDate());
            noDays1 -= noHolidays;

            // Calculate The number of working days between the Date Repair Authorised/TL Identified’ and Date Engineers Report Sent
            int noDays2 = DateHelper.getNumberOfWorkingDaysBetween(claim.getHireMonitoringDetail().getRepairAuthorisedDate(), claim.getHireMonitoringDetail().getEngineersReportSentDate());
            noHolidays = bankHolidayService.getNoHolidaysBetween(claim.getHireMonitoringDetail().getRepairAuthorisedDate(), claim.getHireMonitoringDetail().getEngineersReportSentDate());
            noDays2 -= noHolidays;

            // Calculate The number of working days between the Date Repair Authorised/TL Identified’ and Hire End Date
            int noDays3 = DateHelper.getNumberOfWorkingDaysBetween(claim.getHireMonitoringDetail().getRepairAuthorisedDate(), claim.getVehicleHire().getHireEnd());
            noHolidays = bankHolidayService.getNoHolidaysBetween(claim.getHireMonitoringDetail().getRepairAuthorisedDate(), claim.getVehicleHire().getHireEnd());
            noDays3 -= noHolidays;

            // Calculate The number of working days between the Hire Start Date and Hire End Date
            int noDays4 = DateHelper.getNumberOfWorkingDaysBetween(claim.getVehicleHire().getHireStart(), claim.getVehicleHire().getHireEnd());
            noHolidays = bankHolidayService.getNoHolidaysBetween(claim.getVehicleHire().getHireStart(), claim.getVehicleHire().getHireEnd());
            noDays4 -= noHolidays;


            if (noDays1 > claim.getBreBand().getTimeToAuthoriseRepair5()
                    || noDays2 > claim.getBreBand().getTimeToSubmittEngineersReport5()
                    || noDays3 > claim.getBreBand().getTimeToOffHire5()
                    || noDays4 > claim.getBreBand().getTotalAllowableDays5()) {
                success = false;
                narrative = "The hire period for a total loss exceeds the number of days allowed where the at-fault insurer is sending PAV and the vehicle is roadworthy.";
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
        return "105";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        if (ClaimType.isInsurerUpload(claimType)) {
            return ClaimStatus.MANUAL_INVOICE_CONTESTED;
        }
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }

}
