package idas.chox.service.bre.util;

import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.util.CalcHelper;

public final class ClaimCalcHelper {

    private static final Logger LOG = LoggerFactory.getLogger(ClaimCalcHelper.class);
    private Claim claim;
    private ExtrasCalcHelper exCalcHelper;

    private ClaimCalcHelper() {
    }

    public static ClaimCalcHelper getInstance(Claim c) {
        ClaimCalcHelper cc = new ClaimCalcHelper();
        cc.claim = c;
        cc.exCalcHelper = ExtrasCalcHelper.getInstance(c.getInvoice());
        return cc;
    }

    public ExtrasCalcHelper getExCalcHelper() {
        return exCalcHelper;
    }

    /*
     * ------------- helper calc methods ---------------------------
     */
    public int getHireDuration() {
        if (claim.getVehicleHire() == null || claim.getVehicleHire().getRentalStart() == null) {
            return 0;
        }
        Date hireStart = claim.getVehicleHire().getRentalStart();
        Date initialEcd = claim.getLatestHireMonitoringEcd();
        LOG.debug("Hire duration period from {} to {}", hireStart, initialEcd);
        int hireDuration = CalcHelper.getDaysBetweenDates(hireStart, initialEcd);
        LOG.debug("Hire duration is {}", hireDuration);
        return hireDuration;
    }

    
    public BigDecimal getDailyHireRateCharged() {
        BigDecimal hireNetMinusExtras = claim.getInvoice().getHireNet().subtract(exCalcHelper.getTotalExtras());
        BigDecimal dailyHireRatecharged;
        try {
            dailyHireRatecharged = hireNetMinusExtras.divide(new BigDecimal(claim.getVehicleHire().getDays()), 4, BigDecimal.ROUND_HALF_UP);
        } catch (Exception ex) {
            LOG.info("Exception thrown calculating daily hire rate charged: {}", ex.getMessage());
            dailyHireRatecharged = hireNetMinusExtras;
        }
        return dailyHireRatecharged;
    }


    public int getAllowedDays() {
        int allowedDays = 0;

        // Basecamp: S8019
        // allowedDays += claim.getBreBand().getWeekendBufferDays();
        LOG.debug("Adding to allowable days: TakeVehicleOutDays={}", claim.getBreBand().getTakeVehicleOutDays());
        allowedDays += claim.getBreBand().getTakeVehicleOutDays();
        
        if (claim.getCustomer().getIsUsable() != null && claim.getCustomer().getIsUsable() ) {
            LOG.debug("Adding to allowable days: EngineerInspectionDelayDaysMobile={}", claim.getBreBand().getEngineerInspectionDelayDaysMobile());
            allowedDays += claim.getBreBand().getEngineerInspectionDelayDaysMobile();
        } else {
            LOG.debug("Adding to allowable days: EngineerInspectionDelayDaysNonMobile={}", claim.getBreBand().getEngineerInspectionDelayDaysNonMobile());
            allowedDays += claim.getBreBand().getEngineerInspectionDelayDaysNonMobile();
        }

        //if (claim.getCustomerVehicleDamage().getInitialECD() == null) //no ecd
        if (claim.getLatestHireMonitoringEcd() == null) { //no ecd
            LOG.debug("No ECD.");
            if (claim.getCustomer().getIsUsable() != null && claim.getCustomer().getIsUsable()) {
                LOG.debug("Adding to allowable days: TakeVehicleToGarageDaysMobile={}", claim.getBreBand().getTakeVehicleToGarageDaysMobile());
                allowedDays += claim.getBreBand().getTakeVehicleToGarageDaysMobile();
                LOG.debug("Adding to allowable days: IsMobileDayAllowance={}", claim.getBreBand().getIsMobileDayAllowance());
                allowedDays += claim.getBreBand().getIsMobileDayAllowance();
            } else {
                LOG.debug("Adding to allowable days: TakeVehicleToGarageDaysNonMobile={}", claim.getBreBand().getTakeVehicleToGarageDaysNonMobile());
                allowedDays += claim.getBreBand().getTakeVehicleToGarageDaysNonMobile();
                LOG.debug("Adding to allowable days: IsNotMobileDayAllowance={}", claim.getBreBand().getIsNotMobileDayAllowance());
                allowedDays += claim.getBreBand().getIsNotMobileDayAllowance();
            }
            int weekendBuffer = getWeekendBuffer(allowedDays);

            LOG.debug("Adding to allowable days: weekendBuffer={}", weekendBuffer);
            allowedDays += weekendBuffer;
        } else { //we have an ecd
            LOG.debug("ECD found: adding hire duration");

            if (claim.getCustomer().getIsUsable() != null && claim.getCustomer().getIsUsable()) {
                LOG.debug("Adding to allowable days: TakeVehicleToGarageDaysMobile={}", claim.getBreBand().getTakeVehicleToGarageDaysMobile());
                allowedDays += claim.getBreBand().getTakeVehicleToGarageDaysMobile();
            } else {
                LOG.debug("Adding to allowable days: TakeVehicleToGarageDaysNonMobile={}", claim.getBreBand().getTakeVehicleToGarageDaysNonMobile());
                allowedDays += claim.getBreBand().getTakeVehicleToGarageDaysNonMobile();
            }
            int hireDuration = getHireDuration() + 1;
            int weekendBuffer = getWeekendBuffer(allowedDays + (hireDuration % 7));
            LOG.debug("Adding to allowable days: hireDuration={}, weekendBuffer={}", hireDuration, weekendBuffer);
            allowedDays += hireDuration + weekendBuffer;
        }

        return allowedDays;
    }

    
    
    public int getNumberDayOfLabourCostWorthy() {

        int iLabourCostAverageRateDay = getLabourCostAverageRateDay();
        int iDayBufferForEngineeringProcess = getDayBufferForEngineeringProcess();
        int iWeekedBuffer = getWeekendBuffer(iLabourCostAverageRateDay + iDayBufferForEngineeringProcess);

        LOG.debug("iLabourCostAverageRateDay: {}", iLabourCostAverageRateDay);
        LOG.debug("iDayBufferForEngineeringProcess: {}", iDayBufferForEngineeringProcess);
        LOG.debug("iWeekedBuffer: {}", iWeekedBuffer);

        return iLabourCostAverageRateDay + iWeekedBuffer + iDayBufferForEngineeringProcess;
    }

    public int getWeekendBuffer(int days) {

        int iWeekendBufferDay = 0;

        if (days < 5) {
            iWeekendBufferDay = 0;
        } else if (days >= 5 && days < 12) {
            iWeekendBufferDay = 2;
        } else if (days >= 12 && days < 19) {
            iWeekendBufferDay = 4;
        } else if (days >= 19 && days < 26) {
            iWeekendBufferDay = 6;
        } else if (days >= 26 && days < 33) {
            iWeekendBufferDay = 8;
        } else if (days >= 40 && days < 47) {
            iWeekendBufferDay = 10;
        } else if (days >= 47 && days < 54) {
            iWeekendBufferDay = 12;
        } else if (days >= 54 && days < 61) {
            iWeekendBufferDay = 14;
        } else if (days >= 61 && days < 68) {
            iWeekendBufferDay = 16;
        } else if (days >= 68 && days < 75) {
            iWeekendBufferDay = 18;
        } else if (days >= 75 && days < 82) {
            iWeekendBufferDay = 20;
        } else if (days >= 82 && days < 89) {
            iWeekendBufferDay = 22;
        } else if (days >= 89 && days < 96) {
            iWeekendBufferDay = 24;
        } else if (days >= 96 && days < 103) {
            iWeekendBufferDay = 26;
        } else if (days >= 103 && days < 110) {
            iWeekendBufferDay = 28;
        } else if (days >= 110 && days < 117) {
            iWeekendBufferDay = 30;
        } else if (days >= 117 && days < 124) {
            iWeekendBufferDay = 32;
        } else if (days >= 124 && days < 131) {
            iWeekendBufferDay = 34;
        } else if (days >= 131 && days < 138) {
            iWeekendBufferDay = 36;
        } else if (days >= 138 && days < 145) {
            iWeekendBufferDay = 38;
        } else if (days >= 145 && days < 152) {
            iWeekendBufferDay = 40;
        } else if (days >= 152 && days < 159) {
            iWeekendBufferDay = 42;
        } else if (days >= 159 && days < 166) {
            iWeekendBufferDay = 44;
        } else if (days >= 166 && days < 173) {
            iWeekendBufferDay = 46;
        } else if (days >= 173 && days < 180) {
            iWeekendBufferDay = 48;
        } else if (days >= 180 && days < 187) {
            iWeekendBufferDay = 50;
        } else if (days >= 187 && days < 194) {
            iWeekendBufferDay = 52;
        } else if (days >= 194 && days < 201) {
            iWeekendBufferDay = 54;
        } else if (days >= 201 && days < 208) {
            iWeekendBufferDay = 56;
        } else if (days >= 208 && days < 215) {
            iWeekendBufferDay = 58;
        } else if (days >= 215 && days < 222) {
            iWeekendBufferDay = 60;
        }

        LOG.debug("Weekend buffer is {}", iWeekendBufferDay);
        return iWeekendBufferDay;
    }

    public int getLabourCostAverageRateDay() {
        BigDecimal bLabourCost;

        if (claim.getHireMonitoringDetail() == null) {
            LOG.error("HireMonitoringDetail is null for claim '{}'. Returning LabourCostAverageRateDay=0", claim.getChoReference());
            bLabourCost = BigDecimal.ZERO;
        } else {
            bLabourCost = MathHelper.getNotNullDecimalValue(claim.getHireMonitoringDetail().getLabourCost());
        }

        if (bLabourCost.compareTo(BigDecimal.ZERO) < 1) {
            LOG.debug("Labour cost (from HireMonitoringDetail) is zero - calculating new labour cost.");
            bLabourCost = getNewLabourCost();
        }
        LOG.debug("LabourCost is {}", bLabourCost);

        BigDecimal bAverageLabourHoursPerHireDay = new BigDecimal(claim.getBreBand().getAverageLabourHoursPerHireDay());
        BigDecimal bAverageLabourRate;
        if (claim.getCustomer() != null && claim.getCustomer().getVehicleClass() != null)
            bAverageLabourRate = new BigDecimal(claim.getBreBand().getAverageLabourRate(claim.getCustomer().getVehicleClass().getName()));
        else
            bAverageLabourRate = new BigDecimal(claim.getBreBand().getAverageLabourRateStandard());

        BigDecimal bLabourCostAverageRateDay = BigDecimal.ZERO;

        LOG.debug("AverageLabourHoursPerHireDay={}, AverageLabourRate={}", bAverageLabourHoursPerHireDay, bAverageLabourRate);
        if (bLabourCost.doubleValue() > 0
                && bAverageLabourRate.doubleValue() > 0
                && bAverageLabourHoursPerHireDay.doubleValue() > 0) {
            bLabourCostAverageRateDay = (bLabourCost.divide(bAverageLabourRate, 2, BigDecimal.ROUND_HALF_UP)).divide(bAverageLabourHoursPerHireDay, 2, BigDecimal.ROUND_HALF_UP);
            LOG.debug("Calculating LabourCostAverageRateDay as LabourCost/AverageLabourRate/AverageLabourHoursPerHireDay = {}", bLabourCostAverageRateDay);
        } else {
            LOG.debug("LabourCostAverageRateDay not calculated (={})", bLabourCostAverageRateDay);
        }
        return MathHelper.getIntegerFromDecimalRoundUp(bLabourCostAverageRateDay);

    }

    public BigDecimal getNewLabourCost() {

        BigDecimal bLabourCost = BigDecimal.ZERO;
        BigDecimal bLabourRate = BigDecimal.ZERO;
        BigDecimal iLabourHour = BigDecimal.ZERO;

        if (claim.getHireMonitoringDetail() != null) {

            bLabourCost = MathHelper.getNotNullDecimalValue(claim.getHireMonitoringDetail().getLabourCost());
            bLabourRate = MathHelper.getNotNullDecimalValue(claim.getHireMonitoringDetail().getLabourRate());
            iLabourHour = MathHelper.getNotNullDecimalValue(claim.getHireMonitoringDetail().getLabourHour());

        }

        LOG.debug("In getNewLabourCost(): LabourCost={}", bLabourCost);
        LOG.debug("In getNewLabourCost(): LabourRate={}", bLabourRate);
        LOG.debug("In getNewLabourCost(): LabourHour={}", iLabourHour);

        if ((bLabourCost.compareTo(BigDecimal.ZERO) < 1) && (iLabourHour.compareTo(BigDecimal.ZERO) > 0)) {

            if (bLabourRate.compareTo(BigDecimal.ZERO) < 1) {
                int labourRate;
                
                if (claim.getCustomer() != null && claim.getCustomer().getVehicleClass() != null)
                    labourRate = claim.getBreBand().getAverageLabourRate(claim.getCustomer().getVehicleClass().getName());
                else
                    labourRate = claim.getBreBand().getAverageLabourRateStandard();
                
                bLabourCost = new BigDecimal(labourRate).multiply(iLabourHour);
                LOG.debug("LabourCost={} ({}*LabourHour)", bLabourCost, labourRate);
            } else {
                bLabourCost = bLabourRate.multiply(iLabourHour);
                LOG.debug("LabourCost={} (LabourRate*LabourHour)", bLabourCost);
            }
        } else {
            LOG.debug("No LabourCost or LabourHours provided, ");
        }

        return bLabourCost;
    }

    public int getDayBufferForEngineeringProcess() {

        int iDays = 0;

        if (claim.getLatestHireMonitoringEcd() == null) {
            if (claim.getCustomer().getIsUsable() != null && claim.getCustomer().getIsUsable()) {
                iDays += claim.getBreBand().getTakeVehicleToGarageDaysMobile();
            } else {
                iDays += claim.getBreBand().getTakeVehicleToGarageDaysNonMobile();
            }
        } else {
            if (claim.getCustomer().getIsUsable() != null && claim.getCustomer().getIsUsable()) {
                iDays += claim.getBreBand().getTakeVehicleToGarageDaysMobile();
            } else {
                iDays += claim.getBreBand().getTakeVehicleToGarageDaysNonMobile();
            }
        }

        iDays += claim.getBreBand().getTakeVehicleOutDays();

        if (claim.getCustomer().getIsUsable() != null && claim.getCustomer().getIsUsable()) {
            iDays += claim.getBreBand().getEngineerInspectionDelayDaysMobile();
        } else {
            iDays += claim.getBreBand().getEngineerInspectionDelayDaysNonMobile();
        }
        return iDays;
    }
}
