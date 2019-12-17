package idas.chox.service.bre.rules;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Invoice;
import idas.chox.core.services.BankHolidayService;
import idas.chox.core.util.CalcHelper;
import idas.chox.core.util.DateHelper;
import idas.chox.service.bre.util.InvoiceCalcHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GtaAmendTotalLossCHOUnroadworthyCheck implements IBusinessRule {

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

        if (claim.getBreBand().isTotalLossChoUnroadworthyCheck() && ClaimType.isGTA_WideDef(claim.getClaimType())
                && claim.getVehicleHire() != null && claim.getVehicleHire().getHireStart() != null && claim.getVehicleHire().getHireStart().after(firstJuly2019)
                && claim.isManagingRepair()
                && claim.getHireMonitoringDetail() != null && claim.getHireMonitoringDetail().getWhoIsSendingPav().equals("CHO")
                && claim.getCustomer() != null && claim.getCustomer().getIsUsable() != null && !claim.getCustomer().getIsUsable()
                && allDatesPresent(claim) == true) {


            boolean success = true;

            // Calculate The number of working days between Date of Policyholder Contact and 'Date Engineer Instructed'
            int noDays1 = DateHelper.getNumberOfWorkingDaysBetween(claim.getPolicyHolderContactDate(), claim.getHireMonitoringDetail().getInspectionBookedDate());
            int noHolidays = bankHolidayService.getNoHolidaysBetween(claim.getPolicyHolderContactDate(), claim.getHireMonitoringDetail().getInspectionBookedDate());
            noDays1 -= noHolidays;

            // Calculate The number of working days between Date Engineer Instructed and Inspected Date
            int noDays2 = DateHelper.getNumberOfWorkingDaysBetween(claim.getHireMonitoringDetail().getInspectionBookedDate(), claim.getHireMonitoringDetail().getInspectionDate());
            noHolidays = bankHolidayService.getNoHolidaysBetween(claim.getHireMonitoringDetail().getInspectionBookedDate(), claim.getHireMonitoringDetail().getInspectionDate());
            noDays2 -= noHolidays;

            // Calculate The number of working days between hire start date and ‘Date Repair Authorised/TL Identified’
            int noDays3 = DateHelper.getNumberOfWorkingDaysBetween(claim.getVehicleHire().getHireStart(), claim.getHireMonitoringDetail().getRepairAuthorisedDate());
            noHolidays = bankHolidayService.getNoHolidaysBetween(claim.getVehicleHire().getHireStart(), claim.getHireMonitoringDetail().getRepairAuthorisedDate());
            noDays3 -= noHolidays;

            // Calculate The number of working days between Date of Inspection and the Date Engineers Report Sent
            int noDays4 = DateHelper.getNumberOfWorkingDaysBetween(claim.getHireMonitoringDetail().getInspectionDate(), claim.getHireMonitoringDetail().getEngineersReportSentDate());
            noHolidays = bankHolidayService.getNoHolidaysBetween(claim.getHireMonitoringDetail().getInspectionDate(), claim.getHireMonitoringDetail().getEngineersReportSentDate());
            noDays4 -= noHolidays;

            // Calculate The number of working days between Date Total Loss Cheque Received and Date Off-Hired
            int noDays5 = DateHelper.getNumberOfWorkingDaysBetween(claim.getHireMonitoringDetail().getTotalLossOfferCheckReceivedDate(), claim.getVehicleHire().getHireEnd());
            noHolidays = bankHolidayService.getNoHolidaysBetween(claim.getHireMonitoringDetail().getTotalLossOfferCheckReceivedDate(), claim.getVehicleHire().getHireEnd());
            noDays5 -= noHolidays;

            // Calculate The number of working days between Hire Start Date and Date Off-Hired
            int noDays6 = DateHelper.getNumberOfWorkingDaysBetween(claim.getVehicleHire().getHireStart(), claim.getVehicleHire().getHireEnd());
            noHolidays = bankHolidayService.getNoHolidaysBetween(claim.getVehicleHire().getHireStart(), claim.getVehicleHire().getHireEnd());
            noDays6 -= noHolidays;

            if (noDays1 > claim.getBreBand().getTimeToInstructEngineer4()
                    || noDays2 > claim.getBreBand().getTimeToInspect4()
                    || noDays3 > claim.getBreBand().getTimeToAuthoriseRepair4()
                    || noDays4 > claim.getBreBand().getTimeToSubmittEngineersReport4()
                    || noDays5 > claim.getBreBand().getTimeToOffHire4()
                    || noDays6 > claim.getBreBand().getTotalAllowableDays4()) {
                success = false;
                narrative = "The hire period for a total loss exceeds the number of days allowed where the CHO is sending PAV and the vehicle is not roadworthy.";
            }

            res.setResult(success ? RuleEvaluationResult.RULE_PASSED : RuleEvaluationResult.RULE_FAILED);


        } else {

            narrative = "";
            res.setResult(RuleEvaluationResult.RULE_SKIPPED);

        }

        return res;

    }

    private boolean allDatesPresent(Claim claim){
        if ( claim.getHireMonitoringDetail().getTotalLossOfferCheckReceivedDate() != null && claim.getHireMonitoringDetail().getInspectionDate() != null
                && claim.getPolicyHolderContactDate() != null && claim.getHireMonitoringDetail().getInspectionBookedDate() != null
                && claim.getVehicleHire().getHireStart()!= null && claim.getHireMonitoringDetail().getRepairAuthorisedDate() != null
                && claim.getHireMonitoringDetail().getEngineersReportSentDate() != null && claim.getVehicleHire().getHireEnd() != null){
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
        return "104";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        if (ClaimType.isInsurerUpload(claimType)) {
            return ClaimStatus.MANUAL_INVOICE_CONTESTED;
        }
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }

}
