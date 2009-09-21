package scsbre.model;

import java.math.BigDecimal;

public interface ICHOBandInfo {

    public int getHireDayCeiling();    

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

    //public BigDecimal getHireNetCeiling();

    //public BigDecimal getMaxRepairValue();

    public int getAverageLabourRate();
    public int getAverageLabourHoursPerHireDay();
}