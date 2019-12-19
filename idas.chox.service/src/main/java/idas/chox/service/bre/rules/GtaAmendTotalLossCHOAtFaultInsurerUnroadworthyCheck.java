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

public class GtaAmendTotalLossCHOAtFaultInsurerUnroadworthyCheck implements IBusinessRule {

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

        if (claim.getBreBand().isTotalLossChoAtFaultUnroadworthyCheck() && ClaimType.isGTA_WideDef(claim.getClaimType())
                && claim.getVehicleHire() != null && claim.getVehicleHire().getHireStart() != null && claim.getVehicleHire().getHireStart().after(firstJuly2019)
                && claim.isManagingRepair()
                && claim.getHireMonitoringDetail() != null && claim.getHireMonitoringDetail().getWhoIsSendingPav() != null && claim.getHireMonitoringDetail().getWhoIsSendingPav().equals("At Fault Insurer")
                && claim.getCustomer() != null && claim.getCustomer().getIsUsable() != null && !claim.getCustomer().getIsUsable()
                && allDatesPresent(claim) == true) {


            boolean success = true;

            // Calculate The number of working days between the Incident Date and Date Engineer Instructed
            int noDays1 = DateHelper.getNumberOfWorkingDaysBetween(claim.getIncident().getDate(), claim.getHireMonitoringDetail().getInspectionBookedDate());
            int noHolidays = bankHolidayService.getNoHolidaysBetween(claim.getIncident().getDate(), claim.getHireMonitoringDetail().getInspectionBookedDate());
            noDays1 -= noHolidays;

            // Calculate The number of working days between the Date Engineer Instructed and Inspected Date
            int noDays2 = DateHelper.getNumberOfWorkingDaysBetween(claim.getHireMonitoringDetail().getInspectionBookedDate(), claim.getHireMonitoringDetail().getInspectionDate());
            noHolidays = bankHolidayService.getNoHolidaysBetween(claim.getHireMonitoringDetail().getInspectionBookedDate(), claim.getHireMonitoringDetail().getInspectionDate());
            noDays2 -= noHolidays;

            // Calculate The number of working days between the Hire Start Date and Date Repair Authorised/TL Identified
            int noDays3 = DateHelper.getNumberOfWorkingDaysBetween(claim.getVehicleHire().getHireStart(), claim.getHireMonitoringDetail().getRepairAuthorisedDate());
            noHolidays = bankHolidayService.getNoHolidaysBetween(claim.getVehicleHire().getHireStart(), claim.getHireMonitoringDetail().getRepairAuthorisedDate());
            noDays3 -= noHolidays;

            // Calculate The number of working days between the Date Repair Authorised/TL Identified Date and Date Engineers Report Sent
            int noDays4 = DateHelper.getNumberOfWorkingDaysBetween(claim.getHireMonitoringDetail().getRepairAuthorisedDate(), claim.getHireMonitoringDetail().getEngineersReportSentDate());
            noHolidays = bankHolidayService.getNoHolidaysBetween(claim.getHireMonitoringDetail().getRepairAuthorisedDate(), claim.getHireMonitoringDetail().getEngineersReportSentDate());
            noDays4 -= noHolidays;

            // Calculate The number of working days between the Date Total Loss Cheque Received and Date Off-Hired
            int noDays5 = DateHelper.getNumberOfWorkingDaysBetween(claim.getHireMonitoringDetail().getTotalLossOfferCheckReceivedDate(), claim.getVehicleHire().getHireEnd());
            noHolidays = bankHolidayService.getNoHolidaysBetween(claim.getHireMonitoringDetail().getTotalLossOfferCheckReceivedDate(), claim.getVehicleHire().getHireEnd());
            noDays5 -= noHolidays;

            // Calculate The number of working days between Hire Start Date and Date Off-Hired
            int noDays6 = DateHelper.getNumberOfWorkingDaysBetween(claim.getVehicleHire().getHireStart(), claim.getVehicleHire().getHireEnd());
            noHolidays = bankHolidayService.getNoHolidaysBetween(claim.getVehicleHire().getHireStart(), claim.getVehicleHire().getHireEnd());
            noDays6 -= noHolidays;

            if (noDays1 > claim.getBreBand().getTimeToInstructEngineer6()
                    || noDays2 > claim.getBreBand().getTimeToInspect6()
                    || noDays3 > claim.getBreBand().getTimeToAuthoriseRepair6()
                    || noDays4 > claim.getBreBand().getTimeToSubmittEngineersReport6()
                    || noDays5 > claim.getBreBand().getTimeToOffHire6()
                    || noDays6 > claim.getBreBand().getTotalAllowableDays6()) {
                success = false;
                narrative = "The hire period for a total loss exceeds the number of days allowed where the at-fault insurer is sending PAV and the vehicle is not roadworthy.";
            }

            res.setResult(success ? RuleEvaluationResult.RULE_PASSED : RuleEvaluationResult.RULE_FAILED);


        } else {

            narrative = "";
            res.setResult(RuleEvaluationResult.RULE_SKIPPED);

        }

        return res;

    }

    private boolean allDatesPresent(Claim claim){
        if (claim.getIncident().getDate() != null && claim.getHireMonitoringDetail().getInspectionBookedDate() != null
                && claim.getHireMonitoringDetail().getInspectionDate() != null && claim.getVehicleHire().getHireStart()!= null
                && claim.getHireMonitoringDetail().getRepairAuthorisedDate() != null && claim.getHireMonitoringDetail().getEngineersReportSentDate() != null
                && claim.getHireMonitoringDetail().getTotalLossOfferCheckReceivedDate()!= null && claim.getVehicleHire().getHireEnd() != null){
            return true;
        }
        return false;
    }

    @Override
    public String getNarrative() {
        return narrative;
    }

    @Override
    public String getRuleId() {
        return "106";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        if (ClaimType.isInsurerUpload(claimType)) {
            return ClaimStatus.MANUAL_INVOICE_CONTESTED;
        }
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }

}
