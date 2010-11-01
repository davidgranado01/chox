package idas.chox.service.reports.viewdata;

import idas.chox.service.reports.ReportHelper;
import java.math.BigDecimal;
import java.util.Map;

public class OverviewSummaryReportByOrg {

    private int orgId;
    private String orgName;
    private boolean isIns = true;
    private Integer total_no_claims_num;
    private BigDecimal total_no_claims_val;
    private Integer total_no_invoice_num;
    private BigDecimal total_no_invoice_val;
    private Integer total_no_accepted_claims_num;
    private BigDecimal total_no_accepted_claims_val;
    private BigDecimal total_no_accepted_claims_per;
    private Integer total_no_rejected_claims_num;
    private BigDecimal total_no_rejected_claims_val;
    private BigDecimal total_no_rejected_claims_per;
    private Integer total_no_approved_invoice_num;
    private BigDecimal total_no_approved_invoice_val;
    private BigDecimal total_no_approved_invoice_per;
    private Integer total_no_rejected_invoice_num;
    private BigDecimal total_no_rejected_invoice_val;
    private BigDecimal total_no_rejected_invoice_per;
    private Integer average_claim_cycle_day;
    private Integer average_hire_duration_day;
    private BigDecimal average_invoice_val;
    private BigDecimal average_penalty_val;
    private Integer average_invoice_cycle_day;
    private BigDecimal amount_saved_val;
    private BigDecimal credit_repair_no_invoice_val;
//    private BigDecimal credit_repair_no_invoice_per;
    private Integer credit_repair_no_invoice_num;
    private BigDecimal credit_repair_no_paid_invoice_val;
//    private BigDecimal credit_repair_no_paid_invoice_per;
    private Integer credit_repair_no_paid_invoice_num;
    private BigDecimal s_class_no_invoice_val;
//    private BigDecimal s_class_no_invoice_per;
    private Integer s_class_no_invoice_num;
    private BigDecimal s_class_no_paid_invoice_val;
//    private BigDecimal s_class_no_paid_invoice_per;
    private Integer s_class_no_paid_invoice_num;
    private BigDecimal p_class_no_invoice_val;
//    private BigDecimal p_class_no_invoice_per;
    private Integer p_class_no_invoice_num;
    private BigDecimal p_class_no_paid_invoice_val;
//    private BigDecimal p_class_no_paid_invoice_per;
    private Integer p_class_no_paid_invoice_num;
    private BigDecimal mv_class_no_invoice_val;
//    private BigDecimal mv_class_no_invoice_per;
    private Integer mv_class_no_invoice_num;
    private BigDecimal mv_class_no_paid_invoice_val;
//    private BigDecimal mv_class_no_paid_invoice_per;
    private Integer mv_class_no_paid_invoice_num;
    private BigDecimal m_class_no_invoice_val;
//    private BigDecimal m_class_no_invoice_per;
    private Integer m_class_no_invoice_num;
    private BigDecimal m_class_no_paid_invoice_val;
//    private BigDecimal m_class_no_paid_invoice_per;
    private Integer m_class_no_paid_invoice_num;
    private BigDecimal sp_class_no_invoice_val;
//    private BigDecimal sp_class_no_invoice_per;
    private Integer sp_class_no_invoice_num;
    private BigDecimal sp_class_no_paid_invoice_val;
//    private BigDecimal sp_class_no_paid_invoice_per;
    private Integer sp_class_no_paid_invoice_num;
    private BigDecimal other_class_no_invoice_val;
//    private BigDecimal other_class_no_invoice_per;
    private Integer other_class_no_invoice_num;
    private BigDecimal other_class_no_paid_invoice_val;
//    private BigDecimal other_class_no_paid_invoice_per;
    private Integer other_class_no_paid_invoice_num;

    public static OverviewSummaryReportByOrg getObject(Map data, boolean isIns) {

        OverviewSummaryReportByOrg result = new OverviewSummaryReportByOrg();

        result.setIsIns(isIns);
        result.setOrgId(ReportHelper.getIntegerValue(data.get("org_id".toLowerCase())));
        result.setOrgName(data.get("org_name").toString());

        // TOTAL NO. CLAIMS
        result.setTotal_no_claims_num(ReportHelper.getIntegerValue(data.get("total_no_claims_num".toLowerCase())));
        result.setTotal_no_invoice_num(ReportHelper.getIntegerValue(data.get("total_no_invoice_num".toLowerCase())));
        result.setTotal_no_invoice_val(ReportHelper.getBigDecimalValue(data.get("total_no_invoice_val".toLowerCase())));

        result.setTotal_no_claims_val(ReportHelper.getBigDecimalValue(data.get("total_no_claims_val".toLowerCase())));

        // TOTAL NO. ACCPETED CLAIMS
        result.setTotal_no_accepted_claims_num(ReportHelper.getIntegerValue(data.get("total_no_accepted_claims_num".toLowerCase())));
        result.setTotal_no_accepted_claims_per(ReportHelper.getBigDecimalValue(data.get("total_no_accepted_claims_per".toLowerCase())));
        result.setTotal_no_accepted_claims_val(ReportHelper.getBigDecimalValue(data.get("total_no_accepted_claims_val".toLowerCase())));

        // TOTAL NO. REJECTED CLAIMS
        result.setTotal_no_rejected_claims_num(ReportHelper.getIntegerValue(data.get("total_no_rejected_claims_num".toLowerCase())));
        result.setTotal_no_rejected_claims_per(ReportHelper.getBigDecimalValue(data.get("total_no_rejected_claims_per".toLowerCase())));
        result.setTotal_no_rejected_claims_val(ReportHelper.getBigDecimalValue(data.get("total_no_rejected_claims_val".toLowerCase())));

        // TOTAL NO. APPRIVED INVOICES
        result.setTotal_no_approved_invoice_num(ReportHelper.getIntegerValue(data.get("total_no_approved_invoice_num".toLowerCase())));
        result.setTotal_no_approved_invoice_per(ReportHelper.getBigDecimalValue(data.get("total_no_approved_invoice_per".toLowerCase())));
        result.setTotal_no_approved_invoice_val(ReportHelper.getBigDecimalValue(data.get("total_no_approved_invoice_val".toLowerCase())));

        // TOTAL NO. REJECTED INVOICES
        result.setTotal_no_rejected_invoice_num(ReportHelper.getIntegerValue(data.get("total_no_rejected_invoice_num".toLowerCase())));
        result.setTotal_no_rejected_invoice_per(ReportHelper.getBigDecimalValue(data.get("total_no_rejected_invoice_per".toLowerCase())));
        result.setTotal_no_rejected_invoice_val(ReportHelper.getBigDecimalValue(data.get("total_no_rejected_invoice_val".toLowerCase())));

        // AVERAGE INFORMATION
        result.setAverage_claim_cycle_day(ReportHelper.getIntegerValue(data.get("average_claim_cycle_day".toLowerCase())));
        result.setAverage_hire_duration_day(ReportHelper.getIntegerValue(data.get("average_hire_duration_day".toLowerCase())));
        result.setAverage_invoice_val(ReportHelper.getBigDecimalValue(data.get("average_invoice_val".toLowerCase())));
        result.setAverage_penalty_val(ReportHelper.getBigDecimalValue(data.get("average_penalty_val".toLowerCase())));
        result.setAverage_invoice_cycle_day(ReportHelper.getIntegerValue(data.get("average_invoice_cycle_day".toLowerCase())));

        // Amount Saved
        result.setAmount_saved_val(ReportHelper.getBigDecimalValue(data.get("amount_saved_val".toLowerCase())));

        // Credir Repair
        result.setCredit_repair_no_invoice_num(ReportHelper.getIntegerValue(data.get("total_no_creditrepair_invoice_num".toLowerCase())));
        result.setCredit_repair_no_invoice_val(ReportHelper.getBigDecimalValue(data.get("total_no_creditrepair_invoice_val".toLowerCase())));
//        result.setCredit_repair_no_invoice_per(ReportHelper.getBigDecimalValue(data.get("total_no_creditrepair_invoice_per".toLowerCase())));

        result.setCredit_repair_no_paid_invoice_num(ReportHelper.getIntegerValue(data.get("total_no_creditrepair_paid_invoice_num".toLowerCase())));
        result.setCredit_repair_no_paid_invoice_val(ReportHelper.getBigDecimalValue(data.get("total_no_creditrepair_paid_invoice_val".toLowerCase())));
//        result.setCredit_repair_no_paid_invoice_per(ReportHelper.getBigDecimalValue(data.get("total_no_creditrepair_paid_invoice_per".toLowerCase())));

        // Totals by Vehicle Class
        result.setS_class_no_invoice_num(ReportHelper.getIntegerValue(data.get("total_s_class_invoice_num".toLowerCase())));
        result.setS_class_no_invoice_val(ReportHelper.getBigDecimalValue(data.get("total_s_class_invoice_val".toLowerCase())));
//        result.setS_class_no_invoice_per(ReportHelper.getBigDecimalValue(data.get("total_s_class_invoice_per".toLowerCase())));

        result.setS_class_no_paid_invoice_num(ReportHelper.getIntegerValue(data.get("total_s_class_paid_invoice_num".toLowerCase())));
        result.setS_class_no_paid_invoice_val(ReportHelper.getBigDecimalValue(data.get("total_s_class_paid_invoice_val".toLowerCase())));
//        result.setS_class_no_paid_invoice_per(ReportHelper.getBigDecimalValue(data.get("total_s_class_paid_invoice_per".toLowerCase())));

        result.setP_class_no_invoice_num(ReportHelper.getIntegerValue(data.get("total_p_class_invoice_num".toLowerCase())));
        result.setP_class_no_invoice_val(ReportHelper.getBigDecimalValue(data.get("total_p_class_invoice_val".toLowerCase())));
//        result.setS_class_no_invoice_per(ReportHelper.getBigDecimalValue(data.get("total_p_class_invoice_per".toLowerCase())));

        result.setP_class_no_paid_invoice_num(ReportHelper.getIntegerValue(data.get("total_p_class_paid_invoice_num".toLowerCase())));
        result.setP_class_no_paid_invoice_val(ReportHelper.getBigDecimalValue(data.get("total_p_class_paid_invoice_val".toLowerCase())));
//        result.setS_class_no_paid_invoice_per(ReportHelper.getBigDecimalValue(data.get("total_p_class_paid_invoice_per".toLowerCase())));

        result.setMv_class_no_invoice_num(ReportHelper.getIntegerValue(data.get("total_mv_class_invoice_num".toLowerCase())));
        result.setMv_class_no_invoice_val(ReportHelper.getBigDecimalValue(data.get("total_mv_class_invoice_val".toLowerCase())));
//        result.setS_class_no_invoice_per(ReportHelper.getBigDecimalValue(data.get("total_mv_class_invoice_per".toLowerCase())));

        result.setMv_class_no_paid_invoice_num(ReportHelper.getIntegerValue(data.get("total_mv_class_paid_invoice_num".toLowerCase())));
        result.setMv_class_no_paid_invoice_val(ReportHelper.getBigDecimalValue(data.get("total_mv_class_paid_invoice_val".toLowerCase())));
//        result.setS_class_no_paid_invoice_per(ReportHelper.getBigDecimalValue(data.get("total_mv_class_paid_invoice_per".toLowerCase())));

        result.setM_class_no_paid_invoice_num(ReportHelper.getIntegerValue(data.get("total_m_class_paid_invoice_num".toLowerCase())));
        result.setM_class_no_paid_invoice_val(ReportHelper.getBigDecimalValue(data.get("total_m_class_paid_invoice_val".toLowerCase())));
//        result.setS_class_no_paid_invoice_per(ReportHelper.getBigDecimalValue(data.get("total_m_class_paid_invoice_per".toLowerCase())));

        result.setM_class_no_invoice_num(ReportHelper.getIntegerValue(data.get("total_m_class_invoice_num".toLowerCase())));
        result.setM_class_no_invoice_val(ReportHelper.getBigDecimalValue(data.get("total_m_class_invoice_val".toLowerCase())));
//        result.setS_class_no_invoice_per(ReportHelper.getBigDecimalValue(data.get("total_m_class_invoice_per".toLowerCase())));

        result.setSp_class_no_paid_invoice_num(ReportHelper.getIntegerValue(data.get("total_sp_class_paid_invoice_num".toLowerCase())));
        result.setSp_class_no_paid_invoice_val(ReportHelper.getBigDecimalValue(data.get("total_sp_class_paid_invoice_val".toLowerCase())));
//        result.setS_class_no_paid_invoice_per(ReportHelper.getBigDecimalValue(data.get("total_sp_class_paid_invoice_per".toLowerCase())));

        result.setSp_class_no_invoice_num(ReportHelper.getIntegerValue(data.get("total_sp_class_invoice_num".toLowerCase())));
        result.setSp_class_no_invoice_val(ReportHelper.getBigDecimalValue(data.get("total_sp_class_invoice_val".toLowerCase())));
//        result.setS_class_no_invoice_per(ReportHelper.getBigDecimalValue(data.get("total_sp_class_invoice_per".toLowerCase())));

        result.setOther_class_no_paid_invoice_num(ReportHelper.getIntegerValue(data.get("total_other_class_paid_invoice_num".toLowerCase())));
        result.setOther_class_no_paid_invoice_val(ReportHelper.getBigDecimalValue(data.get("total_other_class_paid_invoice_val".toLowerCase())));
//        result.setS_class_no_paid_invoice_per(ReportHelper.getBigDecimalValue(data.get("total_other_class_paid_invoice_per".toLowerCase())));

        result.setOther_class_no_invoice_num(ReportHelper.getIntegerValue(data.get("total_other_class_invoice_num".toLowerCase())));
        result.setOther_class_no_invoice_val(ReportHelper.getBigDecimalValue(data.get("total_other_class_invoice_val".toLowerCase())));
//        result.setS_class_no_invoice_per(ReportHelper.getBigDecimalValue(data.get("total_other_class_invoice_per".toLowerCase())));

        return result;
    }

    public Integer getAverage_claim_cycle_day() {
        return average_claim_cycle_day;
    }

    public void setAverage_claim_cycle_day(Integer average_claim_cycle_day) {
        this.average_claim_cycle_day = average_claim_cycle_day;
    }

    public Integer getAverage_hire_duration_day() {
        return average_hire_duration_day;
    }

    public void setAverage_hire_duration_day(Integer average_hire_duration_day) {
        this.average_hire_duration_day = average_hire_duration_day;
    }

    public BigDecimal getAverage_invoice_val() {
        return average_invoice_val;
    }

    public void setAverage_invoice_val(BigDecimal average_invoice_val) {
        this.average_invoice_val = average_invoice_val;
    }

    public BigDecimal getAverage_penalty_val() {
        return average_penalty_val;
    }

    public void setAverage_penalty_val(BigDecimal average_penalty_val) {
        this.average_penalty_val = average_penalty_val;
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

    public Integer getTotal_no_accepted_claims_num() {
        return total_no_accepted_claims_num;
    }

    public void setTotal_no_accepted_claims_num(Integer total_no_accepted_claims_num) {
        this.total_no_accepted_claims_num = total_no_accepted_claims_num;
    }

    public BigDecimal getTotal_no_accepted_claims_per() {
        return total_no_accepted_claims_per;
    }

    public void setTotal_no_accepted_claims_per(BigDecimal total_no_accepted_claims_per) {
        this.total_no_accepted_claims_per = total_no_accepted_claims_per;
    }

    public BigDecimal getTotal_no_accepted_claims_val() {
        return total_no_accepted_claims_val;
    }

    public void setTotal_no_accepted_claims_val(BigDecimal total_no_accepted_claims_val) {
        this.total_no_accepted_claims_val = total_no_accepted_claims_val;
    }

    public Integer getTotal_no_approved_invoice_num() {
        return total_no_approved_invoice_num;
    }

    public void setTotal_no_approved_invoice_num(Integer total_no_approved_invoice_num) {
        this.total_no_approved_invoice_num = total_no_approved_invoice_num;
    }

    public BigDecimal getTotal_no_approved_invoice_per() {
        return total_no_approved_invoice_per;
    }

    public void setTotal_no_approved_invoice_per(BigDecimal total_no_approved_invoice_per) {
        this.total_no_approved_invoice_per = total_no_approved_invoice_per;
    }

    public BigDecimal getTotal_no_approved_invoice_val() {
        return total_no_approved_invoice_val;
    }

    public void setTotal_no_approved_invoice_val(BigDecimal total_no_approved_invoice_val) {
        this.total_no_approved_invoice_val = total_no_approved_invoice_val;
    }

    public Integer getTotal_no_claims_num() {
        return total_no_claims_num;
    }

    public void setTotal_no_claims_num(Integer total_no_claims_num) {
        this.total_no_claims_num = total_no_claims_num;
    }

    public BigDecimal getTotal_no_claims_val() {
        return total_no_claims_val;
    }

    public void setTotal_no_claims_val(BigDecimal total_no_claims_val) {
        this.total_no_claims_val = total_no_claims_val;
    }

    public Integer getTotal_no_rejected_claims_num() {
        return total_no_rejected_claims_num;
    }

    public void setTotal_no_rejected_claims_num(Integer total_no_rejected_claims_num) {
        this.total_no_rejected_claims_num = total_no_rejected_claims_num;
    }

    public BigDecimal getTotal_no_rejected_claims_per() {
        return total_no_rejected_claims_per;
    }

    public void setTotal_no_rejected_claims_per(BigDecimal total_no_rejected_claims_per) {
        this.total_no_rejected_claims_per = total_no_rejected_claims_per;
    }

    public BigDecimal getTotal_no_rejected_claims_val() {
        return total_no_rejected_claims_val;
    }

    public void setTotal_no_rejected_claims_val(BigDecimal total_no_rejected_claims_val) {
        this.total_no_rejected_claims_val = total_no_rejected_claims_val;
    }

    public Integer getTotal_no_rejected_invoice_num() {
        return total_no_rejected_invoice_num;
    }

    public void setTotal_no_rejected_invoice_num(Integer total_no_rejected_invoice_num) {
        this.total_no_rejected_invoice_num = total_no_rejected_invoice_num;
    }

    public BigDecimal getTotal_no_rejected_invoice_per() {
        return total_no_rejected_invoice_per;
    }

    public void setTotal_no_rejected_invoice_per(BigDecimal total_no_rejected_invoice_per) {
        this.total_no_rejected_invoice_per = total_no_rejected_invoice_per;
    }

    public BigDecimal getTotal_no_rejected_invoice_val() {
        return total_no_rejected_invoice_val;
    }

    public void setTotal_no_rejected_invoice_val(BigDecimal total_no_rejected_invoice_val) {
        this.total_no_rejected_invoice_val = total_no_rejected_invoice_val;
    }

    public Integer getTotal_no_invoice_num() {
        return total_no_invoice_num;
    }

    public void setTotal_no_invoice_num(Integer total_no_invoice_num) {
        this.total_no_invoice_num = total_no_invoice_num;
    }

    public Integer getAverage_invoice_cycle_day() {
        return average_invoice_cycle_day;
    }

    public void setAverage_invoice_cycle_day(Integer average_invoice_cycle_day) {
        this.average_invoice_cycle_day = average_invoice_cycle_day;
    }

    public BigDecimal getTotal_no_invoice_val() {
        return total_no_invoice_val;
    }

    public void setTotal_no_invoice_val(BigDecimal total_no_invoice_val) {
        this.total_no_invoice_val = total_no_invoice_val;
    }

    public BigDecimal getAmount_saved_val() {
        return amount_saved_val;
    }

    public void setAmount_saved_val(BigDecimal amount_saved_val) {
        this.amount_saved_val = amount_saved_val;
    }

    public Integer getCredit_repair_no_invoice_num() {
        return credit_repair_no_invoice_num;
    }

    public void setCredit_repair_no_invoice_num(Integer credit_repair_no_invoice_num) {
        this.credit_repair_no_invoice_num = credit_repair_no_invoice_num;
    }

    public BigDecimal getCredit_repair_no_invoice_val() {
        return credit_repair_no_invoice_val;
    }

    public void setCredit_repair_no_invoice_val(BigDecimal credit_repair_no_invoice_val) {
        this.credit_repair_no_invoice_val = credit_repair_no_invoice_val;
    }

    public Integer getCredit_repair_no_paid_invoice_num() {
        return credit_repair_no_paid_invoice_num;
    }

    public void setCredit_repair_no_paid_invoice_num(Integer credit_repair_no_paid_invoice_num) {
        this.credit_repair_no_paid_invoice_num = credit_repair_no_paid_invoice_num;
    }

    public BigDecimal getCredit_repair_no_paid_invoice_val() {
        return credit_repair_no_paid_invoice_val;
    }

    public void setCredit_repair_no_paid_invoice_val(BigDecimal credit_repair_no_paid_invoice_val) {
        this.credit_repair_no_paid_invoice_val = credit_repair_no_paid_invoice_val;
    }

    public Integer getM_class_no_invoice_num() {
        return m_class_no_invoice_num;
    }

    public void setM_class_no_invoice_num(Integer m_class_no_invoice_num) {
        this.m_class_no_invoice_num = m_class_no_invoice_num;
    }

    public BigDecimal getM_class_no_invoice_val() {
        return m_class_no_invoice_val;
    }

    public void setM_class_no_invoice_val(BigDecimal m_class_no_invoice_val) {
        this.m_class_no_invoice_val = m_class_no_invoice_val;
    }

    public Integer getM_class_no_paid_invoice_num() {
        return m_class_no_paid_invoice_num;
    }

    public void setM_class_no_paid_invoice_num(Integer m_class_no_paid_invoice_num) {
        this.m_class_no_paid_invoice_num = m_class_no_paid_invoice_num;
    }

    public BigDecimal getM_class_no_paid_invoice_val() {
        return m_class_no_paid_invoice_val;
    }

    public void setM_class_no_paid_invoice_val(BigDecimal m_class_no_paid_invoice_val) {
        this.m_class_no_paid_invoice_val = m_class_no_paid_invoice_val;
    }

    public Integer getMv_class_no_invoice_num() {
        return mv_class_no_invoice_num;
    }

    public void setMv_class_no_invoice_num(Integer mv_class_no_invoice_num) {
        this.mv_class_no_invoice_num = mv_class_no_invoice_num;
    }


    public BigDecimal getMv_class_no_invoice_val() {
        return mv_class_no_invoice_val;
    }

    public void setMv_class_no_invoice_val(BigDecimal mv_class_no_invoice_val) {
        this.mv_class_no_invoice_val = mv_class_no_invoice_val;
    }

    public Integer getMv_class_no_paid_invoice_num() {
        return mv_class_no_paid_invoice_num;
    }

    public void setMv_class_no_paid_invoice_num(Integer mv_class_no_paid_invoice_num) {
        this.mv_class_no_paid_invoice_num = mv_class_no_paid_invoice_num;
    }

    public BigDecimal getMv_class_no_paid_invoice_val() {
        return mv_class_no_paid_invoice_val;
    }

    public void setMv_class_no_paid_invoice_val(BigDecimal mv_class_no_paid_invoice_val) {
        this.mv_class_no_paid_invoice_val = mv_class_no_paid_invoice_val;
    }

    public Integer getOther_class_no_invoice_num() {
        return other_class_no_invoice_num;
    }

    public void setOther_class_no_invoice_num(Integer other_class_no_invoice_num) {
        this.other_class_no_invoice_num = other_class_no_invoice_num;
    }

    public BigDecimal getOther_class_no_invoice_val() {
        return other_class_no_invoice_val;
    }

    public void setOther_class_no_invoice_val(BigDecimal other_class_no_invoice_val) {
        this.other_class_no_invoice_val = other_class_no_invoice_val;
    }

    public Integer getOther_class_no_paid_invoice_num() {
        return other_class_no_paid_invoice_num;
    }

    public void setOther_class_no_paid_invoice_num(Integer other_class_no_paid_invoice_num) {
        this.other_class_no_paid_invoice_num = other_class_no_paid_invoice_num;
    }

    public BigDecimal getOther_class_no_paid_invoice_val() {
        return other_class_no_paid_invoice_val;
    }

    public void setOther_class_no_paid_invoice_val(BigDecimal other_class_no_paid_invoice_val) {
        this.other_class_no_paid_invoice_val = other_class_no_paid_invoice_val;
    }

    public Integer getP_class_no_invoice_num() {
        return p_class_no_invoice_num;
    }

    public void setP_class_no_invoice_num(Integer p_class_no_invoice_num) {
        this.p_class_no_invoice_num = p_class_no_invoice_num;
    }

    public BigDecimal getP_class_no_invoice_val() {
        return p_class_no_invoice_val;
    }

    public void setP_class_no_invoice_val(BigDecimal p_class_no_invoice_val) {
        this.p_class_no_invoice_val = p_class_no_invoice_val;
    }

    public Integer getP_class_no_paid_invoice_num() {
        return p_class_no_paid_invoice_num;
    }

    public void setP_class_no_paid_invoice_num(Integer p_class_no_paid_invoice_num) {
        this.p_class_no_paid_invoice_num = p_class_no_paid_invoice_num;
    }

    public BigDecimal getP_class_no_paid_invoice_val() {
        return p_class_no_paid_invoice_val;
    }

    public void setP_class_no_paid_invoice_val(BigDecimal p_class_no_paid_invoice_val) {
        this.p_class_no_paid_invoice_val = p_class_no_paid_invoice_val;
    }

    public Integer getS_class_no_invoice_num() {
        return s_class_no_invoice_num;
    }

    public void setS_class_no_invoice_num(Integer s_class_no_invoice_num) {
        this.s_class_no_invoice_num = s_class_no_invoice_num;
    }

    public BigDecimal getS_class_no_invoice_val() {
        return s_class_no_invoice_val;
    }

    public void setS_class_no_invoice_val(BigDecimal s_class_no_invoice_val) {
        this.s_class_no_invoice_val = s_class_no_invoice_val;
    }

    public Integer getS_class_no_paid_invoice_num() {
        return s_class_no_paid_invoice_num;
    }

    public void setS_class_no_paid_invoice_num(Integer s_class_no_paid_invoice_num) {
        this.s_class_no_paid_invoice_num = s_class_no_paid_invoice_num;
    }

    public BigDecimal getS_class_no_paid_invoice_val() {
        return s_class_no_paid_invoice_val;
    }

    public void setS_class_no_paid_invoice_val(BigDecimal s_class_no_paid_invoice_val) {
        this.s_class_no_paid_invoice_val = s_class_no_paid_invoice_val;
    }

    public Integer getSp_class_no_invoice_num() {
        return sp_class_no_invoice_num;
    }

    public void setSp_class_no_invoice_num(Integer sp_class_no_invoice_num) {
        this.sp_class_no_invoice_num = sp_class_no_invoice_num;
    }

    public BigDecimal getSp_class_no_invoice_val() {
        return sp_class_no_invoice_val;
    }

    public void setSp_class_no_invoice_val(BigDecimal sp_class_no_invoice_val) {
        this.sp_class_no_invoice_val = sp_class_no_invoice_val;
    }

    public Integer getSp_class_no_paid_invoice_num() {
        return sp_class_no_paid_invoice_num;
    }

    public void setSp_class_no_paid_invoice_num(Integer sp_class_no_paid_invoice_num) {
        this.sp_class_no_paid_invoice_num = sp_class_no_paid_invoice_num;
    }

    public BigDecimal getSp_class_no_paid_invoice_val() {
        return sp_class_no_paid_invoice_val;
    }

    public void setSp_class_no_paid_invoice_val(BigDecimal sp_class_no_paid_invoice_val) {
        this.sp_class_no_paid_invoice_val = sp_class_no_paid_invoice_val;
    }
}
