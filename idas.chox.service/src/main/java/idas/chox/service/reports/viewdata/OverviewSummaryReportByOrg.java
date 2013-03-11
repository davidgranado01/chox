package idas.chox.service.reports.viewdata;

import java.math.BigDecimal;
import java.util.Map;

import idas.chox.service.reports.ReportHelper;

public class OverviewSummaryReportByOrg {

    private int orgId;
    private String orgName;
    private boolean isIns = true;
    private Integer totalClaims;
    private BigDecimal totalClaimsValue;
    private Integer totalInvoices;
    private BigDecimal totalnvoicesValue;
    private Integer totalAcceptedClaims;
    private BigDecimal totalAcceptedClaimsValue;
    private BigDecimal totalAcceptedClaimsPer;
    private Integer totalRejectedClaims;
    private BigDecimal totalRejectedClaimsValue;
    private BigDecimal totalRejectedClaimsPer;
    private Integer totalApprovedInvoices;
    private BigDecimal totalApprovedInvoicesValue;
    private BigDecimal totalApprovedInvoicesPer;
    private Integer totalRejectedInvoices;
    private BigDecimal totalRejectedInvoicesValue;
    private BigDecimal totalRejectedInvoicesPer;
    private Integer averageClaimCycleDay;
    private Integer averageHireDurationDay;
    private BigDecimal averageInvoiceValue;
    private BigDecimal averageHireValue;
    private BigDecimal averagePenaltyValue;
    private Integer averageInvoicesCycleDay;
    private BigDecimal amountSavedValue;
    private BigDecimal creditRepairInvoicesValue;
    private Integer creditRepairInvoices;
    private BigDecimal creditRepairPaidInvoicesValue;
    private Integer creditRepairPaidInvoices;
    private BigDecimal sClassInvoicesValue;
    private Integer sClassInvoices;
    private BigDecimal sClassPaidInvoicesValue;
    private Integer sClassPaidInvoices;
    private BigDecimal pClassInvoicesValue;
    private Integer pClassInvoices;
    private BigDecimal pClassPaidInvoicesValue;
    private Integer pClassPaidInvoices;
    private BigDecimal mvClassInvoicesValue;
    private Integer mvClassInvoices;
    private BigDecimal mvClassPaidInvoicesValue;
    private Integer mvClassPaidInvoices;
    private BigDecimal mClassInvoicesValue;
    private Integer mClassInvoices;
    private BigDecimal mClassPaidInvoicesValue;
    private Integer mClassPaidInvoices;
    private BigDecimal spClassInvoicesValue;
    private Integer spClassInvoices;
    private BigDecimal spClassPaidInvoicesValue;
    private Integer spClassPaidInvoices;
    private BigDecimal otherClassInvoicesValue;
    private Integer otherClassInvoices;
    private BigDecimal otherClassPaidInvoicesValue;
    private Integer otherClassPaidInvoices;

    public static OverviewSummaryReportByOrg getObject(Map data, boolean isIns) {

        OverviewSummaryReportByOrg result = new OverviewSummaryReportByOrg();

        result.setIsIns(isIns);
        result.setOrgId(ReportHelper.getIntegerValue(data.get("org_id".toLowerCase())));
        result.setOrgName(data.get("org_name").toString());

        // TOTAL NO. CLAIMS
        result.setTotalClaims(ReportHelper.getIntegerValue(data.get("total_no_claims_num".toLowerCase())));
        result.setTotalInvoices(ReportHelper.getIntegerValue(data.get("total_no_invoice_num".toLowerCase())));
        result.setTotalnvoicesValue(ReportHelper.getBigDecimalValue(data.get("total_no_invoice_val".toLowerCase())));

        result.setTotalClaimsValue(ReportHelper.getBigDecimalValue(data.get("total_no_claims_val".toLowerCase())));

        // TOTAL NO. ACCPETED CLAIMS
        result.setTotalAcceptedClaims(ReportHelper.getIntegerValue(data.get("total_no_accepted_claims_num".toLowerCase())));
        result.setTotalAcceptedClaimsPer(ReportHelper.getBigDecimalValue(data.get("total_no_accepted_claims_per".toLowerCase())));
        result.setTotalAcceptedClaimsValue(ReportHelper.getBigDecimalValue(data.get("total_no_accepted_claims_val".toLowerCase())));

        // TOTAL NO. REJECTED CLAIMS
        result.setTotalRejectedClaims(ReportHelper.getIntegerValue(data.get("total_no_rejected_claims_num".toLowerCase())));
        result.setTotalRejectedClaimsPer(ReportHelper.getBigDecimalValue(data.get("total_no_rejected_claims_per".toLowerCase())));
        result.setTotalRejectedClaimsValue(ReportHelper.getBigDecimalValue(data.get("total_no_rejected_claims_val".toLowerCase())));

        // TOTAL NO. APPRIVED INVOICES
        result.setTotalApprovedInvoices(ReportHelper.getIntegerValue(data.get("total_no_approved_invoice_num".toLowerCase())));
        result.setTotalApprovedInvoicesPer(ReportHelper.getBigDecimalValue(data.get("total_no_approved_invoice_per".toLowerCase())));
        result.setTotalApprovedInvoicesValue(ReportHelper.getBigDecimalValue(data.get("total_no_approved_invoice_val".toLowerCase())));

        // TOTAL NO. REJECTED INVOICES
        result.setTotalRejectedInvoices(ReportHelper.getIntegerValue(data.get("total_no_rejected_invoice_num".toLowerCase())));
        result.setTotalRejectedInvoicesPer(ReportHelper.getBigDecimalValue(data.get("total_no_rejected_invoice_per".toLowerCase())));
        result.setTotalRejectedInvoicesValue(ReportHelper.getBigDecimalValue(data.get("total_no_rejected_invoice_val".toLowerCase())));

        // AVERAGE INFORMATION
        result.setAverageClaimCycleDay(ReportHelper.getIntegerValue(data.get("average_claim_cycle_day".toLowerCase())));
        result.setAverageHireDurationDay(ReportHelper.getIntegerValue(data.get("average_hire_duration_day".toLowerCase())));
        result.setAverageHireValue(ReportHelper.getBigDecimalValue(data.get("average_hire_val".toLowerCase())));
        result.setAverageInvoiceValue(ReportHelper.getBigDecimalValue(data.get("average_invoice_val".toLowerCase())));
        result.setAveragePenaltyValue(ReportHelper.getBigDecimalValue(data.get("average_penalty_val".toLowerCase())));
        result.setAverageInvoicesCycleDay(ReportHelper.getIntegerValue(data.get("average_invoice_cycle_day".toLowerCase())));

        // Amount Saved
        result.setAmountSavedValue(ReportHelper.getBigDecimalValue(data.get("amount_saved_val".toLowerCase())));

        // Credir Repair
        result.setCreditRepairInvoices(ReportHelper.getIntegerValue(data.get("total_no_creditrepair_invoice_num".toLowerCase())));
        result.setCreditRepairInvoicesValue(ReportHelper.getBigDecimalValue(data.get("total_no_creditrepair_invoice_val".toLowerCase())));

        result.setCreditRepairPaidInvoices(ReportHelper.getIntegerValue(data.get("total_no_creditrepair_paid_invoice_num".toLowerCase())));
        result.setCreditRepairPaidInvoicesValue(ReportHelper.getBigDecimalValue(data.get("total_no_creditrepair_paid_invoice_val".toLowerCase())));

        // Totals by Vehicle Class
        result.setSClassInvoices(ReportHelper.getIntegerValue(data.get("total_s_class_invoice_num".toLowerCase())));
        result.setSClassInvoicesValue(ReportHelper.getBigDecimalValue(data.get("total_s_class_invoice_val".toLowerCase())));

        result.setSClassPaidInvoices(ReportHelper.getIntegerValue(data.get("total_s_class_paid_invoice_num".toLowerCase())));
        result.setSClassPaidInvoicesValue(ReportHelper.getBigDecimalValue(data.get("total_s_class_paid_invoice_val".toLowerCase())));

        result.setPClassInvoices(ReportHelper.getIntegerValue(data.get("total_p_class_invoice_num".toLowerCase())));
        result.setPClassInvoicesValue(ReportHelper.getBigDecimalValue(data.get("total_p_class_invoice_val".toLowerCase())));

        result.setPClassPaidInvoices(ReportHelper.getIntegerValue(data.get("total_p_class_paid_invoice_num".toLowerCase())));
        result.setPClassPaidInvoicesValue(ReportHelper.getBigDecimalValue(data.get("total_p_class_paid_invoice_val".toLowerCase())));

        result.setMvClassInvoices(ReportHelper.getIntegerValue(data.get("total_mv_class_invoice_num".toLowerCase())));
        result.setMvClassInvoicesValue(ReportHelper.getBigDecimalValue(data.get("total_mv_class_invoice_val".toLowerCase())));

        result.setMvClassPaidInvoices(ReportHelper.getIntegerValue(data.get("total_mv_class_paid_invoice_num".toLowerCase())));
        result.setMvClassPaidInvoicesValue(ReportHelper.getBigDecimalValue(data.get("total_mv_class_paid_invoice_val".toLowerCase())));

        result.setMClassPaidInvoices(ReportHelper.getIntegerValue(data.get("total_m_class_paid_invoice_num".toLowerCase())));
        result.setMClassPaidInvoicesValue(ReportHelper.getBigDecimalValue(data.get("total_m_class_paid_invoice_val".toLowerCase())));

        result.setMClassInvoices(ReportHelper.getIntegerValue(data.get("total_m_class_invoice_num".toLowerCase())));
        result.setMClassInvoicesValue(ReportHelper.getBigDecimalValue(data.get("total_m_class_invoice_val".toLowerCase())));

        result.setSpClassPaidInvoices(ReportHelper.getIntegerValue(data.get("total_sp_class_paid_invoice_num".toLowerCase())));
        result.setSpClassPaidInvoicesValue(ReportHelper.getBigDecimalValue(data.get("total_sp_class_paid_invoice_val".toLowerCase())));

        result.setSpClassInvoices(ReportHelper.getIntegerValue(data.get("total_sp_class_invoice_num".toLowerCase())));
        result.setSpClassInvoicesValue(ReportHelper.getBigDecimalValue(data.get("total_sp_class_invoice_val".toLowerCase())));

        result.setOtherClassPaidInvoices(ReportHelper.getIntegerValue(data.get("total_other_class_paid_invoice_num".toLowerCase())));
        result.setOtherClassPaidInvoicesValue(ReportHelper.getBigDecimalValue(data.get("total_other_class_paid_invoice_val".toLowerCase())));

        result.setOtherClassInvoices(ReportHelper.getIntegerValue(data.get("total_other_class_invoice_num".toLowerCase())));
        result.setOtherClassInvoicesValue(ReportHelper.getBigDecimalValue(data.get("total_other_class_invoice_val".toLowerCase())));

        return result;
    }

    public Integer getAverageClaimCycleDay() {
        return averageClaimCycleDay;
    }

    public void setAverageClaimCycleDay(Integer averageClaimCycleDay) {
        this.averageClaimCycleDay = averageClaimCycleDay;
    }

    public Integer getAverageHireDurationDay() {
        return averageHireDurationDay;
    }

    public void setAverageHireDurationDay(Integer average_hire_duration_day) {
        this.averageHireDurationDay = average_hire_duration_day;
    }

    public BigDecimal getAverageInvoiceValue() {
        return averageInvoiceValue;
    }

    public void setAverageInvoiceValue(BigDecimal averageInvoiceValue) {
        this.averageInvoiceValue = averageInvoiceValue;
    }

    public BigDecimal getAveragePenaltyValue() {
        return averagePenaltyValue;
    }

    public void setAveragePenaltyValue(BigDecimal averagePenaltyValue) {
        this.averagePenaltyValue = averagePenaltyValue;
    }

    public boolean isIsIns() {
        return isIns;
    }

    public void setIsIns(boolean isIns) {
        this.isIns = isIns;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Integer getTotalAcceptedClaims() {
        return totalAcceptedClaims;
    }

    public void setTotalAcceptedClaims(Integer totalAcceptedClaims) {
        this.totalAcceptedClaims = totalAcceptedClaims;
    }

    public BigDecimal getTotalAcceptedClaimsPer() {
        return totalAcceptedClaimsPer;
    }

    public void setTotalAcceptedClaimsPer(BigDecimal totalAcceptedClaimsPer) {
        this.totalAcceptedClaimsPer = totalAcceptedClaimsPer;
    }

    public BigDecimal getTotalAcceptedClaimsValue() {
        return totalAcceptedClaimsValue;
    }

    public void setTotalAcceptedClaimsValue(BigDecimal totalAcceptedClaimsValue) {
        this.totalAcceptedClaimsValue = totalAcceptedClaimsValue;
    }

    public Integer getTotalApprovedInvoices() {
        return totalApprovedInvoices;
    }

    public void setTotalApprovedInvoices(Integer totalApprovedInvoices) {
        this.totalApprovedInvoices = totalApprovedInvoices;
    }

    public BigDecimal getTotalApprovedInvoicesPer() {
        return totalApprovedInvoicesPer;
    }

    public void setTotalApprovedInvoicesPer(BigDecimal totalApprovedInvoicesPer) {
        this.totalApprovedInvoicesPer = totalApprovedInvoicesPer;
    }

    public BigDecimal getTotalApprovedInvoicesValue() {
        return totalApprovedInvoicesValue;
    }

    public void setTotalApprovedInvoicesValue(BigDecimal totalApprovedInvoicesValue) {
        this.totalApprovedInvoicesValue = totalApprovedInvoicesValue;
    }

    public Integer getTotalClaims() {
        return totalClaims;
    }

    public void setTotalClaims(Integer totalClaims) {
        this.totalClaims = totalClaims;
    }

    public BigDecimal getTotalClaimsValue() {
        return totalClaimsValue;
    }

    public void setTotalClaimsValue(BigDecimal totalClaimsValue) {
        this.totalClaimsValue = totalClaimsValue;
    }

    public Integer getTotalRejectedClaims() {
        return totalRejectedClaims;
    }

    public void setTotalRejectedClaims(Integer totalRejectedClaims) {
        this.totalRejectedClaims = totalRejectedClaims;
    }

    public BigDecimal getTotalRejectedClaimsPer() {
        return totalRejectedClaimsPer;
    }

    public void setTotalRejectedClaimsPer(BigDecimal totalRejectedClaimsPer) {
        this.totalRejectedClaimsPer = totalRejectedClaimsPer;
    }

    public BigDecimal getTotalRejectedClaimsValue() {
        return totalRejectedClaimsValue;
    }

    public void setTotalRejectedClaimsValue(BigDecimal totalRejectedClaimsValue) {
        this.totalRejectedClaimsValue = totalRejectedClaimsValue;
    }

    public Integer getTotalRejectedInvoices() {
        return totalRejectedInvoices;
    }

    public void setTotalRejectedInvoices(Integer totalRejectedInvoices) {
        this.totalRejectedInvoices = totalRejectedInvoices;
    }

    public BigDecimal getTotalRejectedInvoicesPer() {
        return totalRejectedInvoicesPer;
    }

    public void setTotalRejectedInvoicesPer(BigDecimal totalRejectedInvoicesPer) {
        this.totalRejectedInvoicesPer = totalRejectedInvoicesPer;
    }

    public BigDecimal getTotalRejectedInvoicesValue() {
        return totalRejectedInvoicesValue;
    }

    public void setTotalRejectedInvoicesValue(BigDecimal totalRejectedInvoicesValue) {
        this.totalRejectedInvoicesValue = totalRejectedInvoicesValue;
    }

    public Integer getTotalInvoices() {
        return totalInvoices;
    }

    public void setTotalInvoices(Integer totalInvoices) {
        this.totalInvoices = totalInvoices;
    }

    public Integer getAverageInvoicesCycleDay() {
        return averageInvoicesCycleDay;
    }

    public void setAverageInvoicesCycleDay(Integer averageInvoicesCycleDay) {
        this.averageInvoicesCycleDay = averageInvoicesCycleDay;
    }

    public BigDecimal getTotalnvoicesValue() {
        return totalnvoicesValue;
    }

    public void setTotalnvoicesValue(BigDecimal totalnvoicesValue) {
        this.totalnvoicesValue = totalnvoicesValue;
    }

    public BigDecimal getAmountSavedValue() {
        return amountSavedValue;
    }

    public void setAmountSavedValue(BigDecimal amountSavedValue) {
        this.amountSavedValue = amountSavedValue;
    }

    public Integer getCreditRepairInvoices() {
        return creditRepairInvoices;
    }

    public void setCreditRepairInvoices(Integer creditRepairInvoices) {
        this.creditRepairInvoices = creditRepairInvoices;
    }

    public BigDecimal getCreditRepairInvoicesValue() {
        return creditRepairInvoicesValue;
    }

    public void setCreditRepairInvoicesValue(BigDecimal creditRepairInvoicesValue) {
        this.creditRepairInvoicesValue = creditRepairInvoicesValue;
    }

    public Integer getCreditRepairPaidInvoices() {
        return creditRepairPaidInvoices;
    }

    public void setCreditRepairPaidInvoices(Integer creditRepairPaidInvoices) {
        this.creditRepairPaidInvoices = creditRepairPaidInvoices;
    }

    public BigDecimal getCreditRepairPaidInvoicesValue() {
        return creditRepairPaidInvoicesValue;
    }

    public void setCreditRepairPaidInvoicesValue(BigDecimal creditRepairPaidInvoicesValue) {
        this.creditRepairPaidInvoicesValue = creditRepairPaidInvoicesValue;
    }

    public Integer getMClassInvoices() {
        return mClassInvoices;
    }

    public void setMClassInvoices(Integer mClassInvoices) {
        this.mClassInvoices = mClassInvoices;
    }

    public BigDecimal getMClassInvoicesValue() {
        return mClassInvoicesValue;
    }

    public void setMClassInvoicesValue(BigDecimal mClassInvoicesValue) {
        this.mClassInvoicesValue = mClassInvoicesValue;
    }

    public Integer getMClassPaidInvoices() {
        return mClassPaidInvoices;
    }

    public void setMClassPaidInvoices(Integer mClassPaidInvoices) {
        this.mClassPaidInvoices = mClassPaidInvoices;
    }

    public BigDecimal getMClassPaidInvoicesValue() {
        return mClassPaidInvoicesValue;
    }

    public void setMClassPaidInvoicesValue(BigDecimal mClassPaidInvoicesValue) {
        this.mClassPaidInvoicesValue = mClassPaidInvoicesValue;
    }

    public Integer getMvClassInvoices() {
        return mvClassInvoices;
    }

    public void setMvClassInvoices(Integer mvClassInvoices) {
        this.mvClassInvoices = mvClassInvoices;
    }

    public BigDecimal getMvClassInvoicesValue() {
        return mvClassInvoicesValue;
    }

    public void setMvClassInvoicesValue(BigDecimal mvClassInvoicesValue) {
        this.mvClassInvoicesValue = mvClassInvoicesValue;
    }

    public Integer getMvClassPaidInvoices() {
        return mvClassPaidInvoices;
    }

    public void setMvClassPaidInvoices(Integer mvClassPaidInvoices) {
        this.mvClassPaidInvoices = mvClassPaidInvoices;
    }

    public BigDecimal getMvClassPaidInvoicesValue() {
        return mvClassPaidInvoicesValue;
    }

    public void setMvClassPaidInvoicesValue(BigDecimal mvClassPaidInvoicesValue) {
        this.mvClassPaidInvoicesValue = mvClassPaidInvoicesValue;
    }

    public Integer getOtherClassInvoices() {
        return otherClassInvoices;
    }

    public void setOtherClassInvoices(Integer otherClassInvoices) {
        this.otherClassInvoices = otherClassInvoices;
    }

    public BigDecimal getOtherClassInvoicesValue() {
        return otherClassInvoicesValue;
    }

    public void setOtherClassInvoicesValue(BigDecimal otherClassInvoicesValue) {
        this.otherClassInvoicesValue = otherClassInvoicesValue;
    }

    public Integer getOtherClassPaidInvoices() {
        return otherClassPaidInvoices;
    }

    public void setOtherClassPaidInvoices(Integer otherClassPaidInvoices) {
        this.otherClassPaidInvoices = otherClassPaidInvoices;
    }

    public BigDecimal getOtherClassPaidInvoicesValue() {
        return otherClassPaidInvoicesValue;
    }

    public void setOtherClassPaidInvoicesValue(BigDecimal otherClassPaidInvoicesValue) {
        this.otherClassPaidInvoicesValue = otherClassPaidInvoicesValue;
    }

    public Integer getPClassInvoices() {
        return pClassInvoices;
    }

    public void setPClassInvoices(Integer pClassInvoices) {
        this.pClassInvoices = pClassInvoices;
    }

    public BigDecimal getPClassInvoicesValue() {
        return pClassInvoicesValue;
    }

    public void setPClassInvoicesValue(BigDecimal pClassInvoicesValue) {
        this.pClassInvoicesValue = pClassInvoicesValue;
    }

    public Integer getPClassPaidInvoices() {
        return pClassPaidInvoices;
    }

    public void setPClassPaidInvoices(Integer pClassPaidInvoices) {
        this.pClassPaidInvoices = pClassPaidInvoices;
    }

    public BigDecimal getPClassPaidInvoicesValue() {
        return pClassPaidInvoicesValue;
    }

    public void setPClassPaidInvoicesValue(BigDecimal pClassPaidInvoicesValue) {
        this.pClassPaidInvoicesValue = pClassPaidInvoicesValue;
    }

    public Integer getSClassInvoices() {
        return sClassInvoices;
    }

    public void setSClassInvoices(Integer sClassInvoices) {
        this.sClassInvoices = sClassInvoices;
    }

    public BigDecimal getSClassInvoicesValue() {
        return sClassInvoicesValue;
    }

    public void setSClassInvoicesValue(BigDecimal sClassInvoicesValue) {
        this.sClassInvoicesValue = sClassInvoicesValue;
    }

    public Integer getS_class_no_paid_invoice_num() {
        return sClassPaidInvoices;
    }

    public void setSClassPaidInvoices(Integer sClassPaidInvoices) {
        this.sClassPaidInvoices = sClassPaidInvoices;
    }

    public BigDecimal getSClassPaidInvoicesValue() {
        return sClassPaidInvoicesValue;
    }

    public void setSClassPaidInvoicesValue(BigDecimal sClassPaidInvoicesValue) {
        this.sClassPaidInvoicesValue = sClassPaidInvoicesValue;
    }

    public Integer getSpClassInvoices() {
        return spClassInvoices;
    }

    public void setSpClassInvoices(Integer spClassInvoices) {
        this.spClassInvoices = spClassInvoices;
    }

    public BigDecimal getSpClassInvoicesValue() {
        return spClassInvoicesValue;
    }

    public void setSpClassInvoicesValue(BigDecimal spClassInvoicesValue) {
        this.spClassInvoicesValue = spClassInvoicesValue;
    }

    public Integer getSpClassPaidInvoices() {
        return spClassPaidInvoices;
    }

    public void setSpClassPaidInvoices(Integer spClassPaidInvoices) {
        this.spClassPaidInvoices = spClassPaidInvoices;
    }

    public BigDecimal getSpClassPaidInvoicesValue() {
        return spClassPaidInvoicesValue;
    }

    public void setSpClassPaidInvoicesValue(BigDecimal spClassPaidInvoicesValue) {
        this.spClassPaidInvoicesValue = spClassPaidInvoicesValue;
    }

    public BigDecimal getAverageHireValue() {
        return averageHireValue;
    }

    public void setAverageHireValue(BigDecimal averageHireValue) {
        this.averageHireValue = averageHireValue;
    }
}
