package scsbre.model;

import java.math.BigDecimal;

public interface ICHOBandInfo {

    public int getHireDayCeiling();

    public BigDecimal getMaxRepairValue();

    public int getWeekendBufferDays();

    public int getTakeVehicleOutDays();

    public int getEngineerInspectionDelayDays();

    public int getOfferMadeDays();

    public int getReceiptOfFinalStatementChequeDays();

    public int getInspectionDelayDays();

    public BigDecimal getHireRateChargeTolerance();

    public int getIsMobileDayAllowance();

    public int getIsNotMobileDayAllowance();

    public int getTakeVehicleToGarageDaysMobile();

    public int getTakeVehicleToGarageDaysNonMobile();

    public BigDecimal getHireNetCeiling();

    public int getAverageLabourRate();
    public int getAverageLabourHoursPerHireDay();
}