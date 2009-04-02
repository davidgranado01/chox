/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.web.report.viewdata;

import chox.web.report.ReportHelper;
import java.math.BigDecimal;
import java.util.Map;

/**
 *
 * @author Carlson
 */
public class OverviewSummaryReportByOrg {
    
    private int orgId;
    private String orgName;
    private boolean isIns = true;

    private Integer total_no_claims_num;
    private Integer total_no_invoice_num;
    private BigDecimal total_no_claims_val;
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
    
    public static OverviewSummaryReportByOrg getObject(Map data, boolean isIns) {
        
        OverviewSummaryReportByOrg result = new OverviewSummaryReportByOrg();
        
        result.setIsIns(isIns);
        result.setOrgId(ReportHelper.getIntegerValue(data.get("org_id".toLowerCase())));
        result.setOrgName(data.get("org_name").toString());
        
        // TOTAL NO. CLAIMS
        result.setTotal_no_claims_num(ReportHelper.getIntegerValue(data.get("total_no_claims_num".toLowerCase())));
        result.setTotal_no_invoice_num(ReportHelper.getIntegerValue(data.get("total_no_invoice_num".toLowerCase())));
        
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

    
}
