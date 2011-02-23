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
public class BreInvoiceApprovalDisputedData {

    private static final Logger LOG = LoggerFactory.getLogger(BreInvoiceApprovalDisputedData.class);

    private String headerName;
    private Integer noInvoicesUploaded;
    private Integer noInvoicesApprovedByBusinessRules;
    private Integer noInvoicesApprovedByBusinessRulesDisputed;
    private Integer perInvoicesApprovedByBusinessRulesDisputed;
    private Integer perInvoicesApprovesByBusinessRulesNotDisputedPaid15Days;
    private Integer perInvoicesApprovedByBusinessRulesNotDisputedPaid30Days;
    private Integer perInvoicesApprovedByBusinessRulesDisputedPaid15Days;
    private Integer perInvoicesApprovedByBusinessRulesDisputedPaid30Days;
    private Integer perInvoicesDisputedDueToHireCharge;
    private Integer perInvoicesDisputedDueToHireDuration;
    private Integer perInvoicesDisputedDueToLIabilityDispute;
    private Integer perInvoicesDisputedDueToLikeForLike;
    private Integer perInvoicesDisputedDueToQuantum;
    private Integer perInvoicesDisputedDueToRepairCost;
    private Integer perInvoicesDisputedDueToInvoiceAlreadyPaid;
    private Integer perInvoicesDisputedDueToUndisclosed;
    private Integer perInvoicesDisputedDueToOther;

    public static BreInvoiceApprovalDisputedData getObject(Map data) {

        BreInvoiceApprovalDisputedData result = new BreInvoiceApprovalDisputedData();
        result.setHeaderName((String) data.get("month_header".toLowerCase()));
        result.setNoInvoicesUploaded(((BigInteger) data.get("invoice_uploaded_current_total".toLowerCase())).intValue());
        result.setNoInvoicesApprovedByBusinessRules(((BigInteger) data.get("invoice_approved_by_bre_current".toLowerCase())).intValue());
        result.setNoInvoicesApprovedByBusinessRulesDisputed(((BigInteger) data.get("invoice_approved_by_bre_disputed_current".toLowerCase())).intValue());
        result.setPerInvoicesApprovedByBusinessRulesDisputed(((BigInteger) data.get("invoice_approved_by_bre_disputed_current".toLowerCase())).intValue());
        result.setPerInvoicesApprovesByBusinessRulesNotDisputedPaid15Days(((BigInteger) data.get("invoice_approved_by_bre_not_disputed_paid_within_15days_current".toLowerCase())).intValue());
        result.setPerInvoicesApprovedByBusinessRulesNotDisputedPaid30Days(((BigInteger) data.get("invoice_approved_by_bre_not_disputed_paid_within_30days_current".toLowerCase())).intValue());
        result.setPerInvoicesApprovedByBusinessRulesDisputedPaid15Days(((BigInteger) data.get("invoice_approved_by_bre_disputed_paid_within_15days_current".toLowerCase())).intValue());
        result.setPerInvoicesApprovedByBusinessRulesDisputedPaid30Days(((BigInteger) data.get("invoice_approved_by_bre_disputed_paid_within_30days_current".toLowerCase())).intValue());
        result.setPerInvoicesDisputedDueToHireCharge(((BigInteger) data.get("invoice_disputed_due_to_hire_charge_current".toLowerCase())).intValue());
        result.setPerInvoicesDisputedDueToHireDuration(((BigInteger) data.get("invoice_disputed_due_to_hire_duration_current".toLowerCase())).intValue());
        result.setPerInvoicesDisputedDueToLIabilityDispute(((BigInteger) data.get("invoice_disputed_due_to_liability_dispute_current".toLowerCase())).intValue());
        result.setPerInvoicesDisputedDueToLikeForLike(((BigInteger) data.get("invoice_disputed_due_to_like_for_like_current".toLowerCase())).intValue());
        result.setPerInvoicesDisputedDueToQuantum(((BigInteger) data.get("invoice_disputed_due_to_quantum_current".toLowerCase())).intValue());
        result.setPerInvoicesDisputedDueToRepairCost(((BigInteger) data.get("invoice_disputed_due_to_repair_cost_current".toLowerCase())).intValue());
        result.setPerInvoicesDisputedDueToInvoiceAlreadyPaid(((BigInteger) data.get("invoice_disputed_due_to_invoice_already_paid_current".toLowerCase())).intValue());
        result.setPerInvoicesDisputedDueToUndisclosed(((BigInteger) data.get("invoice_disputed_due_to_undisclosed_current".toLowerCase())).intValue());
        result.setPerInvoicesDisputedDueToOther(((BigInteger) data.get("invoice_disputed_due_to_other_current".toLowerCase())).intValue());

        LOG.debug("inside getObject BreInvoiceApprovalDisputedData");

        return result;
    }

    /**
     * @return the headerName
     */
    public String getHeaderName() {
        return headerName;
    }

    /**
     * @param headerName the headerName to set
     */
    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    /**
     * @return the noInvoicesUploaded
     */
    public Integer getNoInvoicesUploaded() {
        return noInvoicesUploaded;
    }

    /**
     * @param noInvoicesUploaded the noInvoicesUploaded to set
     */
    public void setNoInvoicesUploaded(Integer noInvoicesUploaded) {
        this.noInvoicesUploaded = noInvoicesUploaded;
    }

    /**
     * @return the noInvoicesApprovedByBusinessRules
     */
    public Integer getNoInvoicesApprovedByBusinessRules() {
        return noInvoicesApprovedByBusinessRules;
    }

    /**
     * @param noInvoicesApprovedByBusinessRules the noInvoicesApprovedByBusinessRules to set
     */
    public void setNoInvoicesApprovedByBusinessRules(Integer noInvoicesApprovedByBusinessRules) {
        this.noInvoicesApprovedByBusinessRules = noInvoicesApprovedByBusinessRules;
    }

    /**
     * @return the noInvoicesApprovedByBusinessRulesDisputed
     */
    public Integer getNoInvoicesApprovedByBusinessRulesDisputed() {
        return noInvoicesApprovedByBusinessRulesDisputed;
    }

    /**
     * @param noInvoicesApprovedByBusinessRulesDisputed the noInvoicesApprovedByBusinessRulesDisputed to set
     */
    public void setNoInvoicesApprovedByBusinessRulesDisputed(Integer noInvoicesApprovedByBusinessRulesDisputed) {
        this.noInvoicesApprovedByBusinessRulesDisputed = noInvoicesApprovedByBusinessRulesDisputed;
    }

    /**
     * @return the perInvoicesApprovedByBusinessRulesDisputed
     */
    public Integer getPerInvoicesApprovedByBusinessRulesDisputed() {
        return perInvoicesApprovedByBusinessRulesDisputed;
    }

    /**
     * @param perInvoicesApprovedByBusinessRulesDisputed the perInvoicesApprovedByBusinessRulesDisputed to set
     */
    public void setPerInvoicesApprovedByBusinessRulesDisputed(Integer perInvoicesApprovedByBusinessRulesDisputed) {
        this.perInvoicesApprovedByBusinessRulesDisputed = perInvoicesApprovedByBusinessRulesDisputed;
    }

    /**
     * @return the perInvoicesApprovesByBusinessRulesNotDisputedPaid15Days
     */
    public Integer getPerInvoicesApprovesByBusinessRulesNotDisputedPaid15Days() {
        return perInvoicesApprovesByBusinessRulesNotDisputedPaid15Days;
    }

    /**
     * @param perInvoicesApprovesByBusinessRulesNotDisputedPaid15Days the perInvoicesApprovesByBusinessRulesNotDisputedPaid15Days to set
     */
    public void setPerInvoicesApprovesByBusinessRulesNotDisputedPaid15Days(Integer perInvoicesApprovesByBusinessRulesNotDisputedPaid15Days) {
        this.perInvoicesApprovesByBusinessRulesNotDisputedPaid15Days = perInvoicesApprovesByBusinessRulesNotDisputedPaid15Days;
    }

    /**
     * @return the perInvoicesApprovedByBusinessRulesNotDisputedPaid30Days
     */
    public Integer getPerInvoicesApprovedByBusinessRulesNotDisputedPaid30Days() {
        return perInvoicesApprovedByBusinessRulesNotDisputedPaid30Days;
    }

    /**
     * @param perInvoicesApprovedByBusinessRulesNotDisputedPaid30Days the perInvoicesApprovedByBusinessRulesNotDisputedPaid30Days to set
     */
    public void setPerInvoicesApprovedByBusinessRulesNotDisputedPaid30Days(Integer perInvoicesApprovedByBusinessRulesNotDisputedPaid30Days) {
        this.perInvoicesApprovedByBusinessRulesNotDisputedPaid30Days = perInvoicesApprovedByBusinessRulesNotDisputedPaid30Days;
    }

    /**
     * @return the perInvoicesApprovedByBusinessRulesDisputedPaid15Days
     */
    public Integer getPerInvoicesApprovedByBusinessRulesDisputedPaid15Days() {
        return perInvoicesApprovedByBusinessRulesDisputedPaid15Days;
    }

    /**
     * @param perInvoicesApprovedByBusinessRulesDisputedPaid15Days the perInvoicesApprovedByBusinessRulesDisputedPaid15Days to set
     */
    public void setPerInvoicesApprovedByBusinessRulesDisputedPaid15Days(Integer perInvoicesApprovedByBusinessRulesDisputedPaid15Days) {
        this.perInvoicesApprovedByBusinessRulesDisputedPaid15Days = perInvoicesApprovedByBusinessRulesDisputedPaid15Days;
    }

    /**
     * @return the perInvoicesApprovedByBusinessRulesDisputedPaid30Days
     */
    public Integer getPerInvoicesApprovedByBusinessRulesDisputedPaid30Days() {
        return perInvoicesApprovedByBusinessRulesDisputedPaid30Days;
    }

    /**
     * @param perInvoicesApprovedByBusinessRulesDisputedPaid30Days the perInvoicesApprovedByBusinessRulesDisputedPaid30Days to set
     */
    public void setPerInvoicesApprovedByBusinessRulesDisputedPaid30Days(Integer perInvoicesApprovedByBusinessRulesDisputedPaid30Days) {
        this.perInvoicesApprovedByBusinessRulesDisputedPaid30Days = perInvoicesApprovedByBusinessRulesDisputedPaid30Days;
    }

    /**
     * @return the perInvoicesDisputedDueToHireCharge
     */
    public Integer getPerInvoicesDisputedDueToHireCharge() {
        return perInvoicesDisputedDueToHireCharge;
    }

    /**
     * @param perInvoicesDisputedDueToHireCharge the perInvoicesDisputedDueToHireCharge to set
     */
    public void setPerInvoicesDisputedDueToHireCharge(Integer perInvoicesDisputedDueToHireCharge) {
        this.perInvoicesDisputedDueToHireCharge = perInvoicesDisputedDueToHireCharge;
    }

    /**
     * @return the perInvoicesDisputedDueToHireDuration
     */
    public Integer getPerInvoicesDisputedDueToHireDuration() {
        return perInvoicesDisputedDueToHireDuration;
    }

    /**
     * @param perInvoicesDisputedDueToHireDuration the perInvoicesDisputedDueToHireDuration to set
     */
    public void setPerInvoicesDisputedDueToHireDuration(Integer perInvoicesDisputedDueToHireDuration) {
        this.perInvoicesDisputedDueToHireDuration = perInvoicesDisputedDueToHireDuration;
    }

    /**
     * @return the perInvoicesDisputedDueToLIabilityDispute
     */
    public Integer getPerInvoicesDisputedDueToLIabilityDispute() {
        return perInvoicesDisputedDueToLIabilityDispute;
    }

    /**
     * @param perInvoicesDisputedDueToLIabilityDispute the perInvoicesDisputedDueToLIabilityDispute to set
     */
    public void setPerInvoicesDisputedDueToLIabilityDispute(Integer perInvoicesDisputedDueToLIabilityDispute) {
        this.perInvoicesDisputedDueToLIabilityDispute = perInvoicesDisputedDueToLIabilityDispute;
    }

    /**
     * @return the perInvoicesDisputedDueToLikeForLike
     */
    public Integer getPerInvoicesDisputedDueToLikeForLike() {
        return perInvoicesDisputedDueToLikeForLike;
    }

    /**
     * @param perInvoicesDisputedDueToLikeForLike the perInvoicesDisputedDueToLikeForLike to set
     */
    public void setPerInvoicesDisputedDueToLikeForLike(Integer perInvoicesDisputedDueToLikeForLike) {
        this.perInvoicesDisputedDueToLikeForLike = perInvoicesDisputedDueToLikeForLike;
    }

    /**
     * @return the perInvoicesDisputedDueToQuantum
     */
    public Integer getPerInvoicesDisputedDueToQuantum() {
        return perInvoicesDisputedDueToQuantum;
    }

    /**
     * @param perInvoicesDisputedDueToQuantum the perInvoicesDisputedDueToQuantum to set
     */
    public void setPerInvoicesDisputedDueToQuantum(Integer perInvoicesDisputedDueToQuantum) {
        this.perInvoicesDisputedDueToQuantum = perInvoicesDisputedDueToQuantum;
    }

    /**
     * @return the perInvoicesDisputedDueToRepairCost
     */
    public Integer getPerInvoicesDisputedDueToRepairCost() {
        return perInvoicesDisputedDueToRepairCost;
    }

    /**
     * @param perInvoicesDisputedDueToRepairCost the perInvoicesDisputedDueToRepairCost to set
     */
    public void setPerInvoicesDisputedDueToRepairCost(Integer perInvoicesDisputedDueToRepairCost) {
        this.perInvoicesDisputedDueToRepairCost = perInvoicesDisputedDueToRepairCost;
    }

    /**
     * @return the perInvoicesDisputedDueToInvoiceAlreadyPaid
     */
    public Integer getPerInvoicesDisputedDueToInvoiceAlreadyPaid() {
        return perInvoicesDisputedDueToInvoiceAlreadyPaid;
    }

    /**
     * @param perInvoicesDisputedDueToInvoiceAlreadyPaid the perInvoicesDisputedDueToInvoiceAlreadyPaid to set
     */
    public void setPerInvoicesDisputedDueToInvoiceAlreadyPaid(Integer perInvoicesDisputedDueToInvoiceAlreadyPaid) {
        this.perInvoicesDisputedDueToInvoiceAlreadyPaid = perInvoicesDisputedDueToInvoiceAlreadyPaid;
    }

    /**
     * @return the perInvoicesDisputedDueToUndisclosed
     */
    public Integer getPerInvoicesDisputedDueToUndisclosed() {
        return perInvoicesDisputedDueToUndisclosed;
    }

    /**
     * @param perInvoicesDisputedDueToUndisclosed the perInvoicesDisputedDueToUndisclosed to set
     */
    public void setPerInvoicesDisputedDueToUndisclosed(Integer perInvoicesDisputedDueToUndisclosed) {
        this.perInvoicesDisputedDueToUndisclosed = perInvoicesDisputedDueToUndisclosed;
    }

    /**
     * @return the perInvoicesDisputedDueToOther
     */
    public Integer getPerInvoicesDisputedDueToOther() {
        return perInvoicesDisputedDueToOther;
    }

    /**
     * @param perInvoicesDisputedDueToOther the perInvoicesDisputedDueToOther to set
     */
    public void setPerInvoicesDisputedDueToOther(Integer perInvoicesDisputedDueToOther) {
        this.perInvoicesDisputedDueToOther = perInvoicesDisputedDueToOther;
    }




}
