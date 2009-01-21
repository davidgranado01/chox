package scsbre.tests.sample;

import java.math.BigDecimal;

import scsbre.model.ICHOBandInfo;

public class CHOBandInfo implements ICHOBandInfo {

    private BigDecimal hireNetCeiling;
    private int hireDayCeiling;
    private BigDecimal maxRepairValue;
    private int weekendBufferDays;
    private int takeVehicleOutDays;
    private int engineerInspectionDelayDays;
    private int offerMadeDays;
    private int receiptOfFinalStatementChequeDays;
    private int inspectionDelayDays;
    private BigDecimal hireRateChargeTolerance;
    private int isMobileDayAllowance;
    private int isNotMobileDayAllowance;
    private int takeVehicleToGarageDaysMobile;
    private int takeVehicleToGarageDaysNonMobile;


    /* (non-Javadoc)
     * @see scsbre.model.ICHOBandInfo#getHireDayCeiling()
     */
    public int getHireDayCeiling() {
        return hireDayCeiling;
    }
    /* (non-Javadoc)
     * @see scsbre.model.ICHOBandInfo#setHireDayCeiling(int)
     */

    public void setHireDayCeiling(int hireDayCeiling) {
        this.hireDayCeiling = hireDayCeiling;
    }
    /* (non-Javadoc)
     * @see scsbre.model.ICHOBandInfo#getMaxRepairValue()
     */

    public BigDecimal getMaxRepairValue() {
        return maxRepairValue;
    }
    /* (non-Javadoc)
     * @see scsbre.model.ICHOBandInfo#setMaxRepairValue(java.math.BigDecimal)
     */

    public void setMaxRepairValue(BigDecimal maxRepairValue) {
        this.maxRepairValue = maxRepairValue;
    }
    /* (non-Javadoc)
     * @see scsbre.model.ICHOBandInfo#getWeekendBufferDays()
     */

    public int getWeekendBufferDays() {
        return weekendBufferDays;
    }
    /* (non-Javadoc)
     * @see scsbre.model.ICHOBandInfo#setWeekendBufferDays(int)
     */

    public void setWeekendBufferDays(int weekendBufferDays) {
        this.weekendBufferDays = weekendBufferDays;
    }
    /* (non-Javadoc)
     * @see scsbre.model.ICHOBandInfo#getTakeVehicleOutDays()
     */

    public int getTakeVehicleOutDays() {
        return takeVehicleOutDays;
    }
    /* (non-Javadoc)
     * @see scsbre.model.ICHOBandInfo#setTakeVehicleOutDays(int)
     */

    public void setTakeVehicleOutDays(int takeVehicleOutDays) {
        this.takeVehicleOutDays = takeVehicleOutDays;
    }
    /* (non-Javadoc)
     * @see scsbre.model.ICHOBandInfo#getEngineerInspectionDelayDays()
     */

    public int getEngineerInspectionDelayDays() {
        return engineerInspectionDelayDays;
    }
    /* (non-Javadoc)
     * @see scsbre.model.ICHOBandInfo#setEngineerInspectionDelayDays(int)
     */

    public void setEngineerInspectionDelayDays(int engineerInspectionDelayDays) {
        this.engineerInspectionDelayDays = engineerInspectionDelayDays;
    }
    /* (non-Javadoc)
     * @see scsbre.model.ICHOBandInfo#getOfferMadeDays()
     */

    public int getOfferMadeDays() {
        return offerMadeDays;
    }
    /* (non-Javadoc)
     * @see scsbre.model.ICHOBandInfo#setOfferMadeDays(int)
     */

    public void setOfferMadeDays(int offerMadeDays) {
        this.offerMadeDays = offerMadeDays;
    }
    /* (non-Javadoc)
     * @see scsbre.model.ICHOBandInfo#getReceiptOfFinalStatementChequeDays()
     */

    public int getReceiptOfFinalStatementChequeDays() {
        return receiptOfFinalStatementChequeDays;
    }
    /* (non-Javadoc)
     * @see scsbre.model.ICHOBandInfo#setReceiptOfFinalStatementChequeDays(int)
     */

    public void setReceiptOfFinalStatementChequeDays(
            int receiptOfFinalStatementChequeDays) {
        this.receiptOfFinalStatementChequeDays = receiptOfFinalStatementChequeDays;
    }
    /* (non-Javadoc)
     * @see scsbre.model.ICHOBandInfo#getInspectionDelayDays()
     */

    public int getInspectionDelayDays() {
        return inspectionDelayDays;
    }
    /* (non-Javadoc)
     * @see scsbre.model.ICHOBandInfo#setInspectionDelayDays(int)
     */

    public void setInspectionDelayDays(int inspectionDelayDays) {
        this.inspectionDelayDays = inspectionDelayDays;
    }
    /* (non-Javadoc)
     * @see scsbre.model.ICHOBandInfo#getHireRateChargeTolerance()
     */

    public BigDecimal getHireRateChargeTolerance() {
        return hireRateChargeTolerance;
    }
    /* (non-Javadoc)
     * @see scsbre.model.ICHOBandInfo#setHireRateChargeTolerance(java.math.BigDecimal)
     */

    public void setHireRateChargeTolerance(BigDecimal hireRateChargeTolerance) {
        this.hireRateChargeTolerance = hireRateChargeTolerance;
    }
    /* (non-Javadoc)
     * @see scsbre.model.ICHOBandInfo#getIsMobileDayAllowance()
     */

    public int getIsMobileDayAllowance() {
        return isMobileDayAllowance;
    }
    /* (non-Javadoc)
     * @see scsbre.model.ICHOBandInfo#setIsMobileDayAllowance(int)
     */

    public void setIsMobileDayAllowance(int isMobileDayAllowance) {
        this.isMobileDayAllowance = isMobileDayAllowance;
    }
    /* (non-Javadoc)
     * @see scsbre.model.ICHOBandInfo#getIsNotMobileDayAllowance()
     */

    public int getIsNotMobileDayAllowance() {
        return isNotMobileDayAllowance;
    }
    /* (non-Javadoc)
     * @see scsbre.model.ICHOBandInfo#setIsNotMobileDayAllowance(int)
     */

    public void setIsNotMobileDayAllowance(int isNotMobileDayAllowance) {
        this.isNotMobileDayAllowance = isNotMobileDayAllowance;
    }
    /* (non-Javadoc)
     * @see scsbre.model.ICHOBandInfo#getTakeVehicleToGarageDaysMobile()
     */

    public int getTakeVehicleToGarageDaysMobile() {
        return takeVehicleToGarageDaysMobile;
    }
    /* (non-Javadoc)
     * @see scsbre.model.ICHOBandInfo#setTakeVehicleToGarageDaysMobile(int)
     */

    public void setTakeVehicleToGarageDaysMobile(
            int takeVehicleToGarageDaysMobile) {
        this.takeVehicleToGarageDaysMobile = takeVehicleToGarageDaysMobile;
    }
    /* (non-Javadoc)
     * @see scsbre.model.ICHOBandInfo#getTakeVehicleToGarageDaysNonMobile()
     */

    public int getTakeVehicleToGarageDaysNonMobile() {
        return takeVehicleToGarageDaysNonMobile;
    }
    /* (non-Javadoc)
     * @see scsbre.model.ICHOBandInfo#setTakeVehicleToGarageDaysNonMobile(int)
     */

    public void setTakeVehicleToGarageDaysNonMobile(
            int takeVehicleToGarageDaysNonMobile) {
        this.takeVehicleToGarageDaysNonMobile = takeVehicleToGarageDaysNonMobile;
    }
    /* (non-Javadoc)
     * @see scsbre.model.ICHOBandInfo#getHireNetCeiling()
     */

    public BigDecimal getHireNetCeiling() {
        return hireNetCeiling;
    }
    /* (non-Javadoc)
     * @see scsbre.model.ICHOBandInfo#setHireNetCeiling(java.math.BigDecimal)
     */

    public void setHireNetCeiling(BigDecimal hireNetCeiling) {
        this.hireNetCeiling = hireNetCeiling;
    }
}
