/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package idas.chox.service.reports.viewdata;

import java.math.BigInteger;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author rajareddydodda
 */
public class BreInvoiceApprovalDisputeCumulativeData {

    private static final Logger LOG = LoggerFactory.getLogger(BreInvoiceApprovalDisputeCumulativeData.class);

    private String headerNameCumm;
    private Integer noInvoicesUploadedCumm;
    private Integer noInvoicesApprovedByBusinessRulesCumm;
    private Integer noInvoicesApprovedByBusinessRulesDisputedCumm;
    private Integer perInvoicesApprovedByBusinessRulesDisputedCumm;
    private Integer perInvoicesApprovesByBusinessRulesNotDisputedPaid15DaysCumm;
    private Integer perInvoicesApprovedByBusinessRulesNotDisputedPaid30DaysCumm;
    private Integer perInvoicesApprovedByBusinessRulesDisputedPaid15DaysCumm;
    private Integer perInvoicesApprovedByBusinessRulesDisputedPaid30DaysCumm;
    private Integer perInvoicesDisputedDueToHireChargeCumm;
    private Integer perInvoicesDisputedDueToHireDurationCumm;
    private Integer perInvoicesDisputedDueToLIabilityDisputeCumm;
    private Integer perInvoicesDisputedDueToLikeForLikeCumm;
    private Integer perInvoicesDisputedDueToQuantumCumm;
    private Integer perInvoicesDisputedDueToRepairCostCumm;
    private Integer perInvoicesDisputedDueToInvoiceAlreadyPaidCumm;
    private Integer perInvoicesDisputedDueToUndisclosedCumm;
    private Integer perInvoicesDisputedDueToOtherCumm;

    public static BreInvoiceApprovalDisputeCumulativeData getObject(Map data) {

        BreInvoiceApprovalDisputeCumulativeData result = new BreInvoiceApprovalDisputeCumulativeData();
        result.setHeaderNameCumm((String) data.get("month_header".toLowerCase()));
        result.setNoInvoicesUploadedCumm(((BigInteger) data.get("invoice_uploaded_total".toLowerCase())).intValue());
        result.setNoInvoicesApprovedByBusinessRulesCumm(((BigInteger) data.get("invoice_approved_by_bre_total".toLowerCase())).intValue());
      
        result.setNoInvoicesApprovedByBusinessRulesDisputedCumm(((BigInteger) data.get("invoice_approved_by_bre_disputed_total".toLowerCase())).intValue());
        result.setPerInvoicesApprovedByBusinessRulesDisputedCumm(((BigInteger) data.get("invoice_approved_by_bre_disputed_total".toLowerCase())).intValue());
        result.setPerInvoicesApprovesByBusinessRulesNotDisputedPaid15DaysCumm(((BigInteger) data.get("invoice_approved_by_bre_not_disputed_paid_within_15days_total".toLowerCase())).intValue());
        result.setPerInvoicesApprovedByBusinessRulesNotDisputedPaid30DaysCumm(((BigInteger) data.get("invoice_approved_by_bre_not_disputed_paid_within_30days_total".toLowerCase())).intValue());
        result.setPerInvoicesApprovedByBusinessRulesDisputedPaid15DaysCumm(((BigInteger) data.get("invoice_approved_by_bre_disputed_paid_within_15days_total".toLowerCase())).intValue());
        result.setPerInvoicesApprovedByBusinessRulesDisputedPaid30DaysCumm(((BigInteger) data.get("invoice_approved_by_bre_disputed_paid_within_30days_total".toLowerCase())).intValue());
        result.setPerInvoicesDisputedDueToHireChargeCumm(((BigInteger) data.get("invoice_disputed_due_to_hire_charge_total".toLowerCase())).intValue());
        result.setPerInvoicesDisputedDueToHireDurationCumm(((BigInteger) data.get("invoice_disputed_due_to_hire_duration_total".toLowerCase())).intValue());
        result.setPerInvoicesDisputedDueToLIabilityDisputeCumm(((BigInteger) data.get("invoice_disputed_due_to_liability_dispute_total".toLowerCase())).intValue());
        result.setPerInvoicesDisputedDueToLikeForLikeCumm(((BigInteger) data.get("invoice_disputed_due_to_like_for_like_total".toLowerCase())).intValue());
        result.setPerInvoicesDisputedDueToQuantumCumm(((BigInteger) data.get("invoice_disputed_due_to_quantum_total".toLowerCase())).intValue());
        result.setPerInvoicesDisputedDueToRepairCostCumm(((BigInteger) data.get("invoice_disputed_due_to_repair_cost_total".toLowerCase())).intValue());
        result.setPerInvoicesDisputedDueToInvoiceAlreadyPaidCumm(((BigInteger) data.get("invoice_disputed_due_to_invoice_already_paid_total".toLowerCase())).intValue());
        result.setPerInvoicesDisputedDueToUndisclosedCumm(((BigInteger) data.get("invoice_disputed_due_to_undisclosed_total".toLowerCase())).intValue());
        result.setPerInvoicesDisputedDueToOtherCumm(((BigInteger) data.get("invoice_disputed_due_to_other_total".toLowerCase())).intValue());

        
        return result;
    }

    /**
     * @return the headerNameCumm
     */
    public String getHeaderNameCumm() {
        return headerNameCumm;
    }

    /**
     * @param headerNameCumm the headerNameCumm to set
     */
    public void setHeaderNameCumm(String headerNameCumm) {
        this.headerNameCumm = headerNameCumm;
    }

    /**
     * @return the noInvoicesUploadedCumm
     */
    public Integer getNoInvoicesUploadedCumm() {
        return noInvoicesUploadedCumm;
    }

    /**
     * @param noInvoicesUploadedCumm the noInvoicesUploadedCumm to set
     */
    public void setNoInvoicesUploadedCumm(Integer noInvoicesUploadedCumm) {
        this.noInvoicesUploadedCumm = noInvoicesUploadedCumm;
    }

    /**
     * @return the noInvoicesApprovedByBusinessRulesCumm
     */
    public Integer getNoInvoicesApprovedByBusinessRulesCumm() {
        return noInvoicesApprovedByBusinessRulesCumm;
    }

    /**
     * @param noInvoicesApprovedByBusinessRulesCumm the noInvoicesApprovedByBusinessRulesCumm to set
     */
    public void setNoInvoicesApprovedByBusinessRulesCumm(Integer noInvoicesApprovedByBusinessRulesCumm) {
        this.noInvoicesApprovedByBusinessRulesCumm = noInvoicesApprovedByBusinessRulesCumm;
    }

    /**
     * @return the noInvoicesApprovedByBusinessRulesDisputedCumm
     */
    public Integer getNoInvoicesApprovedByBusinessRulesDisputedCumm() {
        return noInvoicesApprovedByBusinessRulesDisputedCumm;
    }

    /**
     * @param noInvoicesApprovedByBusinessRulesDisputedCumm the noInvoicesApprovedByBusinessRulesDisputedCumm to set
     */
    public void setNoInvoicesApprovedByBusinessRulesDisputedCumm(Integer noInvoicesApprovedByBusinessRulesDisputedCumm) {
        this.noInvoicesApprovedByBusinessRulesDisputedCumm = noInvoicesApprovedByBusinessRulesDisputedCumm;
    }

    /**
     * @return the perInvoicesApprovedByBusinessRulesDisputedCumm
     */
    public Integer getPerInvoicesApprovedByBusinessRulesDisputedCumm() {
        return perInvoicesApprovedByBusinessRulesDisputedCumm;
    }

    /**
     * @param perInvoicesApprovedByBusinessRulesDisputedCumm the perInvoicesApprovedByBusinessRulesDisputedCumm to set
     */
    public void setPerInvoicesApprovedByBusinessRulesDisputedCumm(Integer perInvoicesApprovedByBusinessRulesDisputedCumm) {
        this.perInvoicesApprovedByBusinessRulesDisputedCumm = perInvoicesApprovedByBusinessRulesDisputedCumm;
    }

    /**
     * @return the perInvoicesApprovesByBusinessRulesNotDisputedPaid15DaysCumm
     */
    public Integer getPerInvoicesApprovesByBusinessRulesNotDisputedPaid15DaysCumm() {
        return perInvoicesApprovesByBusinessRulesNotDisputedPaid15DaysCumm;
    }

    /**
     * @param perInvoicesApprovesByBusinessRulesNotDisputedPaid15DaysCumm the perInvoicesApprovesByBusinessRulesNotDisputedPaid15DaysCumm to set
     */
    public void setPerInvoicesApprovesByBusinessRulesNotDisputedPaid15DaysCumm(Integer perInvoicesApprovesByBusinessRulesNotDisputedPaid15DaysCumm) {
        this.perInvoicesApprovesByBusinessRulesNotDisputedPaid15DaysCumm = perInvoicesApprovesByBusinessRulesNotDisputedPaid15DaysCumm;
    }

    /**
     * @return the perInvoicesApprovedByBusinessRulesNotDisputedPaid30DaysCumm
     */
    public Integer getPerInvoicesApprovedByBusinessRulesNotDisputedPaid30DaysCumm() {
        return perInvoicesApprovedByBusinessRulesNotDisputedPaid30DaysCumm;
    }

    /**
     * @param perInvoicesApprovedByBusinessRulesNotDisputedPaid30DaysCumm the perInvoicesApprovedByBusinessRulesNotDisputedPaid30DaysCumm to set
     */
    public void setPerInvoicesApprovedByBusinessRulesNotDisputedPaid30DaysCumm(Integer perInvoicesApprovedByBusinessRulesNotDisputedPaid30DaysCumm) {
        this.perInvoicesApprovedByBusinessRulesNotDisputedPaid30DaysCumm = perInvoicesApprovedByBusinessRulesNotDisputedPaid30DaysCumm;
    }

    /**
     * @return the perInvoicesApprovedByBusinessRulesDisputedPaid15DaysCumm
     */
    public Integer getPerInvoicesApprovedByBusinessRulesDisputedPaid15DaysCumm() {
        return perInvoicesApprovedByBusinessRulesDisputedPaid15DaysCumm;
    }

    /**
     * @param perInvoicesApprovedByBusinessRulesDisputedPaid15DaysCumm the perInvoicesApprovedByBusinessRulesDisputedPaid15DaysCumm to set
     */
    public void setPerInvoicesApprovedByBusinessRulesDisputedPaid15DaysCumm(Integer perInvoicesApprovedByBusinessRulesDisputedPaid15DaysCumm) {
        this.perInvoicesApprovedByBusinessRulesDisputedPaid15DaysCumm = perInvoicesApprovedByBusinessRulesDisputedPaid15DaysCumm;
    }

    /**
     * @return the perInvoicesApprovedByBusinessRulesDisputedPaid30DaysCumm
     */
    public Integer getPerInvoicesApprovedByBusinessRulesDisputedPaid30DaysCumm() {
        return perInvoicesApprovedByBusinessRulesDisputedPaid30DaysCumm;
    }

    /**
     * @param perInvoicesApprovedByBusinessRulesDisputedPaid30DaysCumm the perInvoicesApprovedByBusinessRulesDisputedPaid30DaysCumm to set
     */
    public void setPerInvoicesApprovedByBusinessRulesDisputedPaid30DaysCumm(Integer perInvoicesApprovedByBusinessRulesDisputedPaid30DaysCumm) {
        this.perInvoicesApprovedByBusinessRulesDisputedPaid30DaysCumm = perInvoicesApprovedByBusinessRulesDisputedPaid30DaysCumm;
    }

    /**
     * @return the perInvoicesDisputedDueToHireChargeCumm
     */
    public Integer getPerInvoicesDisputedDueToHireChargeCumm() {
        return perInvoicesDisputedDueToHireChargeCumm;
    }

    /**
     * @param perInvoicesDisputedDueToHireChargeCumm the perInvoicesDisputedDueToHireChargeCumm to set
     */
    public void setPerInvoicesDisputedDueToHireChargeCumm(Integer perInvoicesDisputedDueToHireChargeCumm) {
        this.perInvoicesDisputedDueToHireChargeCumm = perInvoicesDisputedDueToHireChargeCumm;
    }

    /**
     * @return the perInvoicesDisputedDueToHireDurationCumm
     */
    public Integer getPerInvoicesDisputedDueToHireDurationCumm() {
        return perInvoicesDisputedDueToHireDurationCumm;
    }

    /**
     * @param perInvoicesDisputedDueToHireDurationCumm the perInvoicesDisputedDueToHireDurationCumm to set
     */
    public void setPerInvoicesDisputedDueToHireDurationCumm(Integer perInvoicesDisputedDueToHireDurationCumm) {
        this.perInvoicesDisputedDueToHireDurationCumm = perInvoicesDisputedDueToHireDurationCumm;
    }

    /**
     * @return the perInvoicesDisputedDueToLIabilityDisputeCumm
     */
    public Integer getPerInvoicesDisputedDueToLIabilityDisputeCumm() {
        return perInvoicesDisputedDueToLIabilityDisputeCumm;
    }

    /**
     * @param perInvoicesDisputedDueToLIabilityDisputeCumm the perInvoicesDisputedDueToLIabilityDisputeCumm to set
     */
    public void setPerInvoicesDisputedDueToLIabilityDisputeCumm(Integer perInvoicesDisputedDueToLIabilityDisputeCumm) {
        this.perInvoicesDisputedDueToLIabilityDisputeCumm = perInvoicesDisputedDueToLIabilityDisputeCumm;
    }

    /**
     * @return the perInvoicesDisputedDueToLikeForLikeCumm
     */
    public Integer getPerInvoicesDisputedDueToLikeForLikeCumm() {
        return perInvoicesDisputedDueToLikeForLikeCumm;
    }

    /**
     * @param perInvoicesDisputedDueToLikeForLikeCumm the perInvoicesDisputedDueToLikeForLikeCumm to set
     */
    public void setPerInvoicesDisputedDueToLikeForLikeCumm(Integer perInvoicesDisputedDueToLikeForLikeCumm) {
        this.perInvoicesDisputedDueToLikeForLikeCumm = perInvoicesDisputedDueToLikeForLikeCumm;
    }

    /**
     * @return the perInvoicesDisputedDueToQuantumCumm
     */
    public Integer getPerInvoicesDisputedDueToQuantumCumm() {
        return perInvoicesDisputedDueToQuantumCumm;
    }

    /**
     * @param perInvoicesDisputedDueToQuantumCumm the perInvoicesDisputedDueToQuantumCumm to set
     */
    public void setPerInvoicesDisputedDueToQuantumCumm(Integer perInvoicesDisputedDueToQuantumCumm) {
        this.perInvoicesDisputedDueToQuantumCumm = perInvoicesDisputedDueToQuantumCumm;
    }

    /**
     * @return the perInvoicesDisputedDueToRepairCostCumm
     */
    public Integer getPerInvoicesDisputedDueToRepairCostCumm() {
        return perInvoicesDisputedDueToRepairCostCumm;
    }

    /**
     * @param perInvoicesDisputedDueToRepairCostCumm the perInvoicesDisputedDueToRepairCostCumm to set
     */
    public void setPerInvoicesDisputedDueToRepairCostCumm(Integer perInvoicesDisputedDueToRepairCostCumm) {
        this.perInvoicesDisputedDueToRepairCostCumm = perInvoicesDisputedDueToRepairCostCumm;
    }

    /**
     * @return the perInvoicesDisputedDueToInvoiceAlreadyPaidCumm
     */
    public Integer getPerInvoicesDisputedDueToInvoiceAlreadyPaidCumm() {
        return perInvoicesDisputedDueToInvoiceAlreadyPaidCumm;
    }

    /**
     * @param perInvoicesDisputedDueToInvoiceAlreadyPaidCumm the perInvoicesDisputedDueToInvoiceAlreadyPaidCumm to set
     */
    public void setPerInvoicesDisputedDueToInvoiceAlreadyPaidCumm(Integer perInvoicesDisputedDueToInvoiceAlreadyPaidCumm) {
        this.perInvoicesDisputedDueToInvoiceAlreadyPaidCumm = perInvoicesDisputedDueToInvoiceAlreadyPaidCumm;
    }

    /**
     * @return the perInvoicesDisputedDueToUndisclosedCumm
     */
    public Integer getPerInvoicesDisputedDueToUndisclosedCumm() {
        return perInvoicesDisputedDueToUndisclosedCumm;
    }

    /**
     * @param perInvoicesDisputedDueToUndisclosedCumm the perInvoicesDisputedDueToUndisclosedCumm to set
     */
    public void setPerInvoicesDisputedDueToUndisclosedCumm(Integer perInvoicesDisputedDueToUndisclosedCumm) {
        this.perInvoicesDisputedDueToUndisclosedCumm = perInvoicesDisputedDueToUndisclosedCumm;
    }

    /**
     * @return the perInvoicesDisputedDueToOtherCumm
     */
    public Integer getPerInvoicesDisputedDueToOtherCumm() {
        return perInvoicesDisputedDueToOtherCumm;
    }

    /**
     * @param perInvoicesDisputedDueToOtherCumm the perInvoicesDisputedDueToOtherCumm to set
     */
    public void setPerInvoicesDisputedDueToOtherCumm(Integer perInvoicesDisputedDueToOtherCumm) {
        this.perInvoicesDisputedDueToOtherCumm = perInvoicesDisputedDueToOtherCumm;
    }


}
