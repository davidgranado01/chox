package scsbre.model;

import java.math.BigDecimal;

public interface ICHOBandInfo {

    public int getHireDayCeiling();

    public void setHireDayCeiling(int hireDayCeiling);

    public BigDecimal getMaxRepairValue();

    public void setMaxRepairValue(BigDecimal maxRepairValue);

    public int getWeekendBufferDays();

    public void setWeekendBufferDays(int weekendBufferDays);

    public int getTakeVehicleOutDays();

    public void setTakeVehicleOutDays(int takeVehicleOutDays);

    public int getEngineerInspectionDelayDays();

    public void setEngineerInspectionDelayDays(int engineerInspectionDelayDays);

    public int getOfferMadeDays();

    public void setOfferMadeDays(int offerMadeDays);

    public int getReceiptOfFinalStatementChequeDays();

    public void setReceiptOfFinalStatementChequeDays(
            int receiptOfFinalStatementChequeDays);

    public int getInspectionDelayDays();

    public void setInspectionDelayDays(int inspectionDelayDays);

    public BigDecimal getHireRateChargeTolerance();

    public void setHireRateChargeTolerance(BigDecimal hireRateChargeTolerance);

    public int getIsMobileDayAllowance();

    public void setIsMobileDayAllowance(int isMobileDayAllowance);

    public int getIsNotMobileDayAllowance();

    public void setIsNotMobileDayAllowance(int isNotMobileDayAllowance);

    public int getTakeVehicleToGarageDaysMobile();

    public void setTakeVehicleToGarageDaysMobile(
            int takeVehicleToGarageDaysMobile);

    public int getTakeVehicleToGarageDaysNonMobile();

    public void setTakeVehicleToGarageDaysNonMobile(
            int takeVehicleToGarageDaysNonMobile);

    public BigDecimal getHireNetCeiling();

    public void setHireNetCeiling(BigDecimal hireNetCeiling);
}