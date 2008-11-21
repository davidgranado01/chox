package scsbre.model;

import java.math.BigDecimal;

public interface IEngineerReportInfo {

    public BigDecimal getEstimatedLabourAmount();

    public BigDecimal getEstimatedTotalRepairAmount();

    public int getEstimatedDaysUnderRepair();
}