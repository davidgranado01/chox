package scsbre.tests.sample;

import java.math.BigDecimal;

import scsbre.model.IEngineerReportInfo;

public class EngineerReportInfo implements IEngineerReportInfo {

    private BigDecimal estimatedLabourAmount;
    private BigDecimal estimatedTotalRepairAmount;
    private int estimatedDaysUnderRepair;

    /* (non-Javadoc)
     * @see scsbre.model.IEngineerReportInfo#getEstimatedLabourAmount()
     */
    public BigDecimal getEstimatedLabourAmount() {
        return estimatedLabourAmount;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IEngineerReportInfo#setEstimatedLabourAmount(java.math.BigDecimal)
     */

    public void setEstimatedLabourAmount(BigDecimal estimatedLabourAmount) {
        this.estimatedLabourAmount = estimatedLabourAmount;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IEngineerReportInfo#getEstimatedTotalRepairAmount()
     */

    public BigDecimal getEstimatedTotalRepairAmount() {
        return estimatedTotalRepairAmount;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IEngineerReportInfo#setEstimatedTotalRepairAmount(java.math.BigDecimal)
     */

    public void setEstimatedTotalRepairAmount(BigDecimal estimatedTotalRepairAmount) {
        this.estimatedTotalRepairAmount = estimatedTotalRepairAmount;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IEngineerReportInfo#getEstimatedDaysUnderRepair()
     */

    public int getEstimatedDaysUnderRepair() {
        return estimatedDaysUnderRepair;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IEngineerReportInfo#setEstimatedDaysUnderRepair(int)
     */

    public void setEstimatedDaysUnderRepair(int estimatedDaysUnderRepair) {
        this.estimatedDaysUnderRepair = estimatedDaysUnderRepair;
    }
}
