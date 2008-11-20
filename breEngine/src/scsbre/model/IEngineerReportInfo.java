package scsbre.model;

import java.math.BigDecimal;

public interface IEngineerReportInfo {

    public BigDecimal getEstimatedLabourAmount();

    public void setEstimatedLabourAmount(BigDecimal estimatedLabourAmount);

    public BigDecimal getEstimatedTotalRepairAmount();

    public void setEstimatedTotalRepairAmount(
            BigDecimal estimatedTotalRepairAmount);

    public int getEstimatedDaysUnderRepair();

    public void setEstimatedDaysUnderRepair(int estimatedDaysUnderRepair);
}