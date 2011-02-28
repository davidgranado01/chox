package idas.chox.service.reports.viewdata;

import java.math.BigDecimal;
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
    private double perInvoicesApprovedByBusinessRulesDisputedCumm =0.0;
    private double perInvoicesApprovesByBusinessRulesNotDisputedPaid15DaysCumm =0.0;
    private double perInvoicesApprovedByBusinessRulesNotDisputedPaid30DaysCumm =0.0;
    private double perInvoicesApprovedByBusinessRulesDisputedPaid15DaysCumm=0.0 ;
    private double perInvoicesApprovedByBusinessRulesDisputedPaid30DaysCumm=0.0;
    private double perInvoicesDisputedDueToHireChargeCumm=0.0;
    private double perInvoicesDisputedDueToHireDurationCumm =0.0;
    private double perInvoicesDisputedDueToLIabilityDisputeCumm =0.0;
    private double perInvoicesDisputedDueToLikeForLikeCumm =0.0;
    private double perInvoicesDisputedDueToQuantumCumm=0.0;
    private double perInvoicesDisputedDueToRepairCostCumm =0.0;
    private double perInvoicesDisputedDueToInvoiceAlreadyPaidCumm =0.0;
    private double perInvoicesDisputedDueToUndisclosedCumm =0.0;
    private double perInvoicesDisputedDueToOtherCumm =0.0;

    public static BreInvoiceApprovalDisputeCumulativeData getObject(Map data) {

        BreInvoiceApprovalDisputeCumulativeData result = new BreInvoiceApprovalDisputeCumulativeData();
        result.setHeaderNameCumm((String) data.get("month_header".toLowerCase()));
        result.setNoInvoicesUploadedCumm(((BigInteger) data.get("invoice_uploaded_total".toLowerCase())).intValue());
        result.setNoInvoicesApprovedByBusinessRulesCumm(((BigInteger) data.get("invoice_approved_by_bre_total".toLowerCase())).intValue());
        result.setNoInvoicesApprovedByBusinessRulesDisputedCumm(((BigInteger) data.get("invoice_approved_by_bre_disputed_total".toLowerCase())).intValue());


        if(((BigInteger) data.get("invoice_uploaded_total".toLowerCase())).intValue() != 0){


            result.setPerInvoicesApprovedByBusinessRulesDisputedCumm((getDoubleValue(data.get("invoice_approved_by_bre_disputed_total")) * 1.0) / ((BigInteger) data.get("invoice_uploaded_total".toLowerCase())).intValue());
            result.setPerInvoicesApprovesByBusinessRulesNotDisputedPaid15DaysCumm((getDoubleValue(data.get("invoice_approved_by_bre_not_disputed_paid_within_15days_total")) * 1.0) / ((BigInteger) data.get("invoice_uploaded_total".toLowerCase())).intValue());
            result.setPerInvoicesApprovedByBusinessRulesNotDisputedPaid30DaysCumm((getDoubleValue(data.get("invoice_approved_by_bre_not_disputed_paid_within_30days_total")) * 1.0) / ((BigInteger) data.get("invoice_uploaded_total".toLowerCase())).intValue());
            result.setPerInvoicesApprovedByBusinessRulesDisputedPaid15DaysCumm((getDoubleValue(data.get("invoice_approved_by_bre_disputed_paid_within_15days_total")) * 1.0) / ((BigInteger) data.get("invoice_uploaded_total".toLowerCase())).intValue());
            result.setPerInvoicesApprovedByBusinessRulesDisputedPaid30DaysCumm((getDoubleValue(data.get("invoice_approved_by_bre_disputed_paid_within_30days_total")) * 1.0) / ((BigInteger) data.get("invoice_uploaded_total".toLowerCase())).intValue());
        }else{



        }
        if(((BigInteger) data.get("invoice_approved_by_bre_disputed_total".toLowerCase())).intValue() !=0){
        result.setPerInvoicesDisputedDueToHireChargeCumm((getDoubleValue(data.get("invoice_disputed_due_to_hire_charge_total")) * 1.0) / ((BigInteger) data.get("invoice_approved_by_bre_disputed_total".toLowerCase())).intValue());
        result.setPerInvoicesDisputedDueToHireDurationCumm((getDoubleValue(data.get("invoice_disputed_due_to_hire_duration_total")) * 1.0) / ((BigInteger) data.get("invoice_approved_by_bre_disputed_total".toLowerCase())).intValue());
        result.setPerInvoicesDisputedDueToLIabilityDisputeCumm((getDoubleValue(data.get("invoice_disputed_due_to_liability_dispute_total")) * 1.0) / ((BigInteger) data.get("invoice_approved_by_bre_disputed_total".toLowerCase())).intValue());
        result.setPerInvoicesDisputedDueToLikeForLikeCumm((getDoubleValue(data.get("invoice_disputed_due_to_like_for_like_total")) * 1.0) / ((BigInteger) data.get("invoice_approved_by_bre_disputed_total".toLowerCase())).intValue());
        result.setPerInvoicesDisputedDueToQuantumCumm((getDoubleValue(data.get("invoice_disputed_due_to_quantum_total")) * 1.0) / ((BigInteger) data.get("invoice_approved_by_bre_disputed_total".toLowerCase())).intValue());
        result.setPerInvoicesDisputedDueToRepairCostCumm((getDoubleValue(data.get("invoice_disputed_due_to_repair_cost_total")) * 1.0) / ((BigInteger) data.get("invoice_approved_by_bre_disputed_total".toLowerCase())).intValue());
        result.setPerInvoicesDisputedDueToInvoiceAlreadyPaidCumm((getDoubleValue(data.get("invoice_disputed_due_to_invoice_already_paid_total")) * 1.0) / ((BigInteger) data.get("invoice_approved_by_bre_disputed_total".toLowerCase())).intValue());
        result.setPerInvoicesDisputedDueToUndisclosedCumm((getDoubleValue(data.get("invoice_disputed_due_to_undisclosed_total")) * 1.0) / ((BigInteger) data.get("invoice_approved_by_bre_disputed_total".toLowerCase())).intValue());
        result.setPerInvoicesDisputedDueToOtherCumm((getDoubleValue(data.get("invoice_disputed_due_to_other_total")) * 1.0) / ((BigInteger) data.get("invoice_approved_by_bre_disputed_total".toLowerCase())).intValue());
        }else{




        }

        
        return result;
    }





     private static double getDoubleValue(Object v) {
        LOG.debug("inside getDoubleValue method found class for the sent value is: {}", v.getClass());

        if (v.getClass().equals(BigDecimal.class)) {
            LOG.debug("return value {}", ((BigDecimal) v).doubleValue());
            return ((BigDecimal) v).doubleValue();
        } else if (v.getClass().equals(Integer.class)) {
            return (Integer) v;
        } else if (v.getClass().equals(BigInteger.class)) {
            LOG.debug("return value {}", ((BigInteger) v).intValue());
            return ((BigInteger) v).intValue();
        } else {
            LOG.debug("no class match found returning 0");
            return 0;
        }
    }

     private Integer getIntegerValue(Object v) {
        LOG.debug("inside IntegerValue method found class for the sent value is: {}", v.getClass());
        if (v.getClass().equals(Integer.class)) {
            return (Integer) v;
        } else if (v.getClass().equals(BigInteger.class)) {
            LOG.debug("return value {}", ((BigInteger) v).intValue());
            return ((BigInteger) v).intValue();
        } else {
            LOG.debug("no class match found returning 0");
            return 0;
        }
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
    public double getPerInvoicesApprovedByBusinessRulesDisputedCumm() {
        return perInvoicesApprovedByBusinessRulesDisputedCumm;
    }

    /**
     * @param perInvoicesApprovedByBusinessRulesDisputedCumm the perInvoicesApprovedByBusinessRulesDisputedCumm to set
     */
    public void setPerInvoicesApprovedByBusinessRulesDisputedCumm(double perInvoicesApprovedByBusinessRulesDisputedCumm) {
        this.perInvoicesApprovedByBusinessRulesDisputedCumm = perInvoicesApprovedByBusinessRulesDisputedCumm;
    }

    /**
     * @return the perInvoicesApprovesByBusinessRulesNotDisputedPaid15DaysCumm
     */
    public double getPerInvoicesApprovesByBusinessRulesNotDisputedPaid15DaysCumm() {
        return perInvoicesApprovesByBusinessRulesNotDisputedPaid15DaysCumm;
    }

    /**
     * @param perInvoicesApprovesByBusinessRulesNotDisputedPaid15DaysCumm the perInvoicesApprovesByBusinessRulesNotDisputedPaid15DaysCumm to set
     */
    public void setPerInvoicesApprovesByBusinessRulesNotDisputedPaid15DaysCumm(double perInvoicesApprovesByBusinessRulesNotDisputedPaid15DaysCumm) {
        this.perInvoicesApprovesByBusinessRulesNotDisputedPaid15DaysCumm = perInvoicesApprovesByBusinessRulesNotDisputedPaid15DaysCumm;
    }

    /**
     * @return the perInvoicesApprovedByBusinessRulesNotDisputedPaid30DaysCumm
     */
    public double getPerInvoicesApprovedByBusinessRulesNotDisputedPaid30DaysCumm() {
        return perInvoicesApprovedByBusinessRulesNotDisputedPaid30DaysCumm;
    }

    /**
     * @param perInvoicesApprovedByBusinessRulesNotDisputedPaid30DaysCumm the perInvoicesApprovedByBusinessRulesNotDisputedPaid30DaysCumm to set
     */
    public void setPerInvoicesApprovedByBusinessRulesNotDisputedPaid30DaysCumm(double perInvoicesApprovedByBusinessRulesNotDisputedPaid30DaysCumm) {
        this.perInvoicesApprovedByBusinessRulesNotDisputedPaid30DaysCumm = perInvoicesApprovedByBusinessRulesNotDisputedPaid30DaysCumm;
    }

    /**
     * @return the perInvoicesApprovedByBusinessRulesDisputedPaid15DaysCumm
     */
    public double getPerInvoicesApprovedByBusinessRulesDisputedPaid15DaysCumm() {
        return perInvoicesApprovedByBusinessRulesDisputedPaid15DaysCumm;
    }

    /**
     * @param perInvoicesApprovedByBusinessRulesDisputedPaid15DaysCumm the perInvoicesApprovedByBusinessRulesDisputedPaid15DaysCumm to set
     */
    public void setPerInvoicesApprovedByBusinessRulesDisputedPaid15DaysCumm(double perInvoicesApprovedByBusinessRulesDisputedPaid15DaysCumm) {
        this.perInvoicesApprovedByBusinessRulesDisputedPaid15DaysCumm = perInvoicesApprovedByBusinessRulesDisputedPaid15DaysCumm;
    }

    /**
     * @return the perInvoicesApprovedByBusinessRulesDisputedPaid30DaysCumm
     */
    public double getPerInvoicesApprovedByBusinessRulesDisputedPaid30DaysCumm() {
        return perInvoicesApprovedByBusinessRulesDisputedPaid30DaysCumm;
    }

    /**
     * @param perInvoicesApprovedByBusinessRulesDisputedPaid30DaysCumm the perInvoicesApprovedByBusinessRulesDisputedPaid30DaysCumm to set
     */
    public void setPerInvoicesApprovedByBusinessRulesDisputedPaid30DaysCumm(double perInvoicesApprovedByBusinessRulesDisputedPaid30DaysCumm) {
        this.perInvoicesApprovedByBusinessRulesDisputedPaid30DaysCumm = perInvoicesApprovedByBusinessRulesDisputedPaid30DaysCumm;
    }

    /**
     * @return the perInvoicesDisputedDueToHireChargeCumm
     */
    public double getPerInvoicesDisputedDueToHireChargeCumm() {
        return perInvoicesDisputedDueToHireChargeCumm;
    }

    /**
     * @param perInvoicesDisputedDueToHireChargeCumm the perInvoicesDisputedDueToHireChargeCumm to set
     */
    public void setPerInvoicesDisputedDueToHireChargeCumm(double perInvoicesDisputedDueToHireChargeCumm) {
        this.perInvoicesDisputedDueToHireChargeCumm = perInvoicesDisputedDueToHireChargeCumm;
    }

    /**
     * @return the perInvoicesDisputedDueToHireDurationCumm
     */
    public double getPerInvoicesDisputedDueToHireDurationCumm() {
        return perInvoicesDisputedDueToHireDurationCumm;
    }

    /**
     * @param perInvoicesDisputedDueToHireDurationCumm the perInvoicesDisputedDueToHireDurationCumm to set
     */
    public void setPerInvoicesDisputedDueToHireDurationCumm(double perInvoicesDisputedDueToHireDurationCumm) {
        this.perInvoicesDisputedDueToHireDurationCumm = perInvoicesDisputedDueToHireDurationCumm;
    }

    /**
     * @return the perInvoicesDisputedDueToLIabilityDisputeCumm
     */
    public double getPerInvoicesDisputedDueToLIabilityDisputeCumm() {
        return perInvoicesDisputedDueToLIabilityDisputeCumm;
    }

    /**
     * @param perInvoicesDisputedDueToLIabilityDisputeCumm the perInvoicesDisputedDueToLIabilityDisputeCumm to set
     */
    public void setPerInvoicesDisputedDueToLIabilityDisputeCumm(double perInvoicesDisputedDueToLIabilityDisputeCumm) {
        this.perInvoicesDisputedDueToLIabilityDisputeCumm = perInvoicesDisputedDueToLIabilityDisputeCumm;
    }

    /**
     * @return the perInvoicesDisputedDueToLikeForLikeCumm
     */
    public double getPerInvoicesDisputedDueToLikeForLikeCumm() {
        return perInvoicesDisputedDueToLikeForLikeCumm;
    }

    /**
     * @param perInvoicesDisputedDueToLikeForLikeCumm the perInvoicesDisputedDueToLikeForLikeCumm to set
     */
    public void setPerInvoicesDisputedDueToLikeForLikeCumm(double perInvoicesDisputedDueToLikeForLikeCumm) {
        this.perInvoicesDisputedDueToLikeForLikeCumm = perInvoicesDisputedDueToLikeForLikeCumm;
    }

    /**
     * @return the perInvoicesDisputedDueToQuantumCumm
     */
    public double getPerInvoicesDisputedDueToQuantumCumm() {
        return perInvoicesDisputedDueToQuantumCumm;
    }

    /**
     * @param perInvoicesDisputedDueToQuantumCumm the perInvoicesDisputedDueToQuantumCumm to set
     */
    public void setPerInvoicesDisputedDueToQuantumCumm(double perInvoicesDisputedDueToQuantumCumm) {
        this.perInvoicesDisputedDueToQuantumCumm = perInvoicesDisputedDueToQuantumCumm;
    }

    /**
     * @return the perInvoicesDisputedDueToRepairCostCumm
     */
    public double getPerInvoicesDisputedDueToRepairCostCumm() {
        return perInvoicesDisputedDueToRepairCostCumm;
    }

    /**
     * @param perInvoicesDisputedDueToRepairCostCumm the perInvoicesDisputedDueToRepairCostCumm to set
     */
    public void setPerInvoicesDisputedDueToRepairCostCumm(double perInvoicesDisputedDueToRepairCostCumm) {
        this.perInvoicesDisputedDueToRepairCostCumm = perInvoicesDisputedDueToRepairCostCumm;
    }

    /**
     * @return the perInvoicesDisputedDueToInvoiceAlreadyPaidCumm
     */
    public double getPerInvoicesDisputedDueToInvoiceAlreadyPaidCumm() {
        return perInvoicesDisputedDueToInvoiceAlreadyPaidCumm;
    }

    /**
     * @param perInvoicesDisputedDueToInvoiceAlreadyPaidCumm the perInvoicesDisputedDueToInvoiceAlreadyPaidCumm to set
     */
    public void setPerInvoicesDisputedDueToInvoiceAlreadyPaidCumm(double perInvoicesDisputedDueToInvoiceAlreadyPaidCumm) {
        this.perInvoicesDisputedDueToInvoiceAlreadyPaidCumm = perInvoicesDisputedDueToInvoiceAlreadyPaidCumm;
    }

    /**
     * @return the perInvoicesDisputedDueToUndisclosedCumm
     */
    public double getPerInvoicesDisputedDueToUndisclosedCumm() {
        return perInvoicesDisputedDueToUndisclosedCumm;
    }

    /**
     * @param perInvoicesDisputedDueToUndisclosedCumm the perInvoicesDisputedDueToUndisclosedCumm to set
     */
    public void setPerInvoicesDisputedDueToUndisclosedCumm(double perInvoicesDisputedDueToUndisclosedCumm) {
        this.perInvoicesDisputedDueToUndisclosedCumm = perInvoicesDisputedDueToUndisclosedCumm;
    }

    /**
     * @return the perInvoicesDisputedDueToOtherCumm
     */
    public double getPerInvoicesDisputedDueToOtherCumm() {
        return perInvoicesDisputedDueToOtherCumm;
    }

    /**
     * @param perInvoicesDisputedDueToOtherCumm the perInvoicesDisputedDueToOtherCumm to set
     */
    public void setPerInvoicesDisputedDueToOtherCumm(double perInvoicesDisputedDueToOtherCumm) {
        this.perInvoicesDisputedDueToOtherCumm = perInvoicesDisputedDueToOtherCumm;
    }

  

}
