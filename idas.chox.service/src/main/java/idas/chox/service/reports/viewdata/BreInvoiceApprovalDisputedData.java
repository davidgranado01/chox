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
public class BreInvoiceApprovalDisputedData {

    private static final Logger LOG = LoggerFactory.getLogger(BreInvoiceApprovalDisputedData.class);

    private String headerName;
    private Integer noInvoicesUploaded;
    private Integer noInvoicesApprovedByBusinessRules;
    private Integer noInvoicesApprovedByBusinessRulesDisputed;
    private double perInvoicesApprovedByBusinessRulesDisputed =0.0;
    private double perInvoicesApprovesByBusinessRulesNotDisputedPaid15Days=0.0;
    private double perInvoicesApprovedByBusinessRulesNotDisputedPaid30Days=0.0;
    private double perInvoicesApprovedByBusinessRulesDisputedPaid15Days =0.0;
    private double perInvoicesApprovedByBusinessRulesDisputedPaid30Days =0.0;
    private double perInvoicesDisputedDueToHireCharge =0.0;
    private double perInvoicesDisputedDueToHireDuration =0.0;
    private double perInvoicesDisputedDueToLIabilityDispute =0.0;
    private double perInvoicesDisputedDueToLikeForLike =0.0;
    private double perInvoicesDisputedDueToQuantum =0.0;
    private double perInvoicesDisputedDueToRepairCost=0.0 ;
    private double perInvoicesDisputedDueToInvoiceAlreadyPaid=0.0;
    private double perInvoicesDisputedDueToUndisclosed=0.0;
    private double perInvoicesDisputedDueToOther=0.0;


    public static BreInvoiceApprovalDisputedData getObject(Map data) {

        BreInvoiceApprovalDisputedData result = new BreInvoiceApprovalDisputedData();
        result.setHeaderName((String) data.get("month_header".toLowerCase()));
        result.setNoInvoicesUploaded(((BigInteger) data.get("invoice_uploaded_current_total".toLowerCase())).intValue());
        result.setNoInvoicesApprovedByBusinessRules(((BigInteger) data.get("invoice_approved_by_bre_current".toLowerCase())).intValue());
        result.setNoInvoicesApprovedByBusinessRulesDisputed(((BigInteger) data.get("invoice_approved_by_bre_disputed_current".toLowerCase())).intValue());
        
        if(((BigInteger) data.get("invoice_uploaded_current_total".toLowerCase())).intValue() !=0){


            result.setPerInvoicesApprovedByBusinessRulesDisputed((getDoubleValue(data.get("invoice_approved_by_bre_disputed_current")) * 1.0) / ((BigInteger) data.get("invoice_uploaded_current_total".toLowerCase())).intValue());
            result.setPerInvoicesApprovesByBusinessRulesNotDisputedPaid15Days((getDoubleValue(data.get("invoice_approved_by_bre_not_disputed_paid_within_15days_current")) * 1.0) / ((BigInteger) data.get("invoice_uploaded_current_total".toLowerCase())).intValue());
            result.setPerInvoicesApprovedByBusinessRulesNotDisputedPaid30Days((getDoubleValue(data.get("invoice_approved_by_bre_not_disputed_paid_within_30days_current")) * 1.0) / ((BigInteger) data.get("invoice_uploaded_current_total".toLowerCase())).intValue());
            result.setPerInvoicesApprovedByBusinessRulesDisputedPaid15Days((getDoubleValue(data.get("invoice_approved_by_bre_disputed_paid_within_15days_current")) * 1.0) / ((BigInteger) data.get("invoice_uploaded_current_total".toLowerCase())).intValue());
            result.setPerInvoicesApprovedByBusinessRulesDisputedPaid30Days((getDoubleValue(data.get("invoice_approved_by_bre_disputed_paid_within_30days_current")) * 1.0) / ((BigInteger) data.get("invoice_uploaded_current_total".toLowerCase())).intValue());
        }
        if(((BigInteger) data.get("invoice_approved_by_bre_disputed_current".toLowerCase())).intValue() !=0){
            result.setPerInvoicesDisputedDueToHireCharge((getDoubleValue(data.get("invoice_disputed_due_to_hire_charge_current")) * 1.0) / ((BigInteger) data.get("invoice_approved_by_bre_disputed_current".toLowerCase())).intValue());
            result.setPerInvoicesDisputedDueToHireDuration((getDoubleValue(data.get("invoice_disputed_due_to_hire_duration_current")) * 1.0) / ((BigInteger) data.get("invoice_approved_by_bre_disputed_current".toLowerCase())).intValue());
            result.setPerInvoicesDisputedDueToLIabilityDispute((getDoubleValue(data.get("invoice_disputed_due_to_liability_dispute_current")) * 1.0) / ((BigInteger) data.get("invoice_approved_by_bre_disputed_current".toLowerCase())).intValue());
            result.setPerInvoicesDisputedDueToLikeForLike((getDoubleValue(data.get("invoice_disputed_due_to_like_for_like_current")) * 1.0) / ((BigInteger) data.get("invoice_approved_by_bre_disputed_current".toLowerCase())).intValue());
            result.setPerInvoicesDisputedDueToQuantum((getDoubleValue(data.get("invoice_disputed_due_to_quantum_current")) * 1.0) / ((BigInteger) data.get("invoice_approved_by_bre_disputed_current".toLowerCase())).intValue());
            result.setPerInvoicesDisputedDueToRepairCost((getDoubleValue(data.get("invoice_disputed_due_to_repair_cost_current")) * 1.0) / ((BigInteger) data.get("invoice_approved_by_bre_disputed_current".toLowerCase())).intValue());
            result.setPerInvoicesDisputedDueToInvoiceAlreadyPaid((getDoubleValue(data.get("invoice_disputed_due_to_invoice_already_paid_current")) * 1.0) / ((BigInteger) data.get("invoice_approved_by_bre_disputed_current".toLowerCase())).intValue());
            result.setPerInvoicesDisputedDueToUndisclosed((getDoubleValue(data.get("invoice_disputed_due_to_undisclosed_current")) * 1.0) / ((BigInteger) data.get("invoice_approved_by_bre_disputed_current".toLowerCase())).intValue());
            result.setPerInvoicesDisputedDueToOther((getDoubleValue(data.get("invoice_disputed_due_to_other_current")) * 1.0) / ((BigInteger) data.get("invoice_approved_by_bre_disputed_current".toLowerCase())).intValue());
        }
        LOG.debug("inside getObject BreInvoiceApprovalDisputedData");


        return result;
    }


    /**
     * @return the noInvoicesUploaded
     */
    public Integer getNoInvoicesUploaded() {
        return noInvoicesUploaded;
    }

    /**
     * @param aNoInvoicesUploaded the noInvoicesUploaded to set
     */
    public void setNoInvoicesUploaded(Integer aNoInvoicesUploaded) {
        noInvoicesUploaded = aNoInvoicesUploaded;
    }

    /**
     * @return the noInvoicesApprovedByBusinessRulesDisputed
     */
    public Integer getNoInvoicesApprovedByBusinessRulesDisputed() {
        return noInvoicesApprovedByBusinessRulesDisputed;
    }

    /**
     * @param aNoInvoicesApprovedByBusinessRulesDisputed the noInvoicesApprovedByBusinessRulesDisputed to set
     */
    public void setNoInvoicesApprovedByBusinessRulesDisputed(Integer aNoInvoicesApprovedByBusinessRulesDisputed) {
        noInvoicesApprovedByBusinessRulesDisputed = aNoInvoicesApprovedByBusinessRulesDisputed;
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

        String tgt = headerName;
        StringBuffer s=new StringBuffer(tgt.toLowerCase());
        s.setCharAt(0,Character.toUpperCase(s.charAt(0)));

        String ns=new String(s);

        LOG.debug("header value"+ns);
        this.headerName = ns;
        
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
     * @return the perInvoicesApprovedByBusinessRulesDisputed
     */
    public double getPerInvoicesApprovedByBusinessRulesDisputed() {
        return perInvoicesApprovedByBusinessRulesDisputed;
    }

    /**
     * @param perInvoicesApprovedByBusinessRulesDisputed the perInvoicesApprovedByBusinessRulesDisputed to set
     */
    public void setPerInvoicesApprovedByBusinessRulesDisputed(double perInvoicesApprovedByBusinessRulesDisputed) {

        this.perInvoicesApprovedByBusinessRulesDisputed = perInvoicesApprovedByBusinessRulesDisputed;

        LOG.debug("perInvoicesApprovedByBusinessRulesDisputed  :{}",perInvoicesApprovedByBusinessRulesDisputed);
    }

    /**
     * @return the perInvoicesApprovesByBusinessRulesNotDisputedPaid15Days
     */
    public double getPerInvoicesApprovesByBusinessRulesNotDisputedPaid15Days() {
        return perInvoicesApprovesByBusinessRulesNotDisputedPaid15Days;
    }

    /**
     * @param perInvoicesApprovesByBusinessRulesNotDisputedPaid15Days the perInvoicesApprovesByBusinessRulesNotDisputedPaid15Days to set
     */
    public void setPerInvoicesApprovesByBusinessRulesNotDisputedPaid15Days(double perInvoicesApprovesByBusinessRulesNotDisputedPaid15Days) {
        this.perInvoicesApprovesByBusinessRulesNotDisputedPaid15Days = perInvoicesApprovesByBusinessRulesNotDisputedPaid15Days;
    }

    /**
     * @return the perInvoicesApprovedByBusinessRulesNotDisputedPaid30Days
     */
    public double getPerInvoicesApprovedByBusinessRulesNotDisputedPaid30Days() {
        return perInvoicesApprovedByBusinessRulesNotDisputedPaid30Days;
    }

    /**
     * @param perInvoicesApprovedByBusinessRulesNotDisputedPaid30Days the perInvoicesApprovedByBusinessRulesNotDisputedPaid30Days to set
     */
    public void setPerInvoicesApprovedByBusinessRulesNotDisputedPaid30Days(double perInvoicesApprovedByBusinessRulesNotDisputedPaid30Days) {
        this.perInvoicesApprovedByBusinessRulesNotDisputedPaid30Days = perInvoicesApprovedByBusinessRulesNotDisputedPaid30Days;
    }

    /**
     * @return the perInvoicesApprovedByBusinessRulesDisputedPaid15Days
     */
    public double getPerInvoicesApprovedByBusinessRulesDisputedPaid15Days() {
        return perInvoicesApprovedByBusinessRulesDisputedPaid15Days;
    }

    /**
     * @param perInvoicesApprovedByBusinessRulesDisputedPaid15Days the perInvoicesApprovedByBusinessRulesDisputedPaid15Days to set
     */
    public void setPerInvoicesApprovedByBusinessRulesDisputedPaid15Days(double perInvoicesApprovedByBusinessRulesDisputedPaid15Days) {
        this.perInvoicesApprovedByBusinessRulesDisputedPaid15Days = perInvoicesApprovedByBusinessRulesDisputedPaid15Days;
    }

    /**
     * @return the perInvoicesApprovedByBusinessRulesDisputedPaid30Days
     */
    public double getPerInvoicesApprovedByBusinessRulesDisputedPaid30Days() {
        return perInvoicesApprovedByBusinessRulesDisputedPaid30Days;
    }

    /**
     * @param perInvoicesApprovedByBusinessRulesDisputedPaid30Days the perInvoicesApprovedByBusinessRulesDisputedPaid30Days to set
     */
    public void setPerInvoicesApprovedByBusinessRulesDisputedPaid30Days(double perInvoicesApprovedByBusinessRulesDisputedPaid30Days) {
        this.perInvoicesApprovedByBusinessRulesDisputedPaid30Days = perInvoicesApprovedByBusinessRulesDisputedPaid30Days;
    }

    /**
     * @return the perInvoicesDisputedDueToHireCharge
     */
    public double getPerInvoicesDisputedDueToHireCharge() {
        return perInvoicesDisputedDueToHireCharge;
    }

    /**
     * @param perInvoicesDisputedDueToHireCharge the perInvoicesDisputedDueToHireCharge to set
     */
    public void setPerInvoicesDisputedDueToHireCharge(double perInvoicesDisputedDueToHireCharge) {
        this.perInvoicesDisputedDueToHireCharge = perInvoicesDisputedDueToHireCharge;
    }

    /**
     * @return the perInvoicesDisputedDueToHireDuration
     */
    public double getPerInvoicesDisputedDueToHireDuration() {
        return perInvoicesDisputedDueToHireDuration;
    }

    /**
     * @param perInvoicesDisputedDueToHireDuration the perInvoicesDisputedDueToHireDuration to set
     */
    public void setPerInvoicesDisputedDueToHireDuration(double perInvoicesDisputedDueToHireDuration) {
        this.perInvoicesDisputedDueToHireDuration = perInvoicesDisputedDueToHireDuration;
    }

    /**
     * @return the perInvoicesDisputedDueToLIabilityDispute
     */
    public double getPerInvoicesDisputedDueToLIabilityDispute() {
        return perInvoicesDisputedDueToLIabilityDispute;
    }

    /**
     * @param perInvoicesDisputedDueToLIabilityDispute the perInvoicesDisputedDueToLIabilityDispute to set
     */
    public void setPerInvoicesDisputedDueToLIabilityDispute(double perInvoicesDisputedDueToLIabilityDispute) {
        this.perInvoicesDisputedDueToLIabilityDispute = perInvoicesDisputedDueToLIabilityDispute;
    }

    /**
     * @return the perInvoicesDisputedDueToLikeForLike
     */
    public double getPerInvoicesDisputedDueToLikeForLike() {
        return perInvoicesDisputedDueToLikeForLike;
    }

    /**
     * @param perInvoicesDisputedDueToLikeForLike the perInvoicesDisputedDueToLikeForLike to set
     */
    public void setPerInvoicesDisputedDueToLikeForLike(double perInvoicesDisputedDueToLikeForLike) {
        this.perInvoicesDisputedDueToLikeForLike = perInvoicesDisputedDueToLikeForLike;
    }

    /**
     * @return the perInvoicesDisputedDueToQuantum
     */
    public double getPerInvoicesDisputedDueToQuantum() {
        return perInvoicesDisputedDueToQuantum;
    }

    /**
     * @param perInvoicesDisputedDueToQuantum the perInvoicesDisputedDueToQuantum to set
     */
    public void setPerInvoicesDisputedDueToQuantum(double perInvoicesDisputedDueToQuantum) {
        this.perInvoicesDisputedDueToQuantum = perInvoicesDisputedDueToQuantum;
    }

    /**
     * @return the perInvoicesDisputedDueToRepairCost
     */
    public double getPerInvoicesDisputedDueToRepairCost() {
        return perInvoicesDisputedDueToRepairCost;
    }

    /**
     * @param perInvoicesDisputedDueToRepairCost the perInvoicesDisputedDueToRepairCost to set
     */
    public void setPerInvoicesDisputedDueToRepairCost(double perInvoicesDisputedDueToRepairCost) {
        this.perInvoicesDisputedDueToRepairCost = perInvoicesDisputedDueToRepairCost;
    }

    /**
     * @return the perInvoicesDisputedDueToInvoiceAlreadyPaid
     */
    public double getPerInvoicesDisputedDueToInvoiceAlreadyPaid() {
        return perInvoicesDisputedDueToInvoiceAlreadyPaid;
    }

    /**
     * @param perInvoicesDisputedDueToInvoiceAlreadyPaid the perInvoicesDisputedDueToInvoiceAlreadyPaid to set
     */
    public void setPerInvoicesDisputedDueToInvoiceAlreadyPaid(double perInvoicesDisputedDueToInvoiceAlreadyPaid) {
        this.perInvoicesDisputedDueToInvoiceAlreadyPaid = perInvoicesDisputedDueToInvoiceAlreadyPaid;
    }

    /**
     * @return the perInvoicesDisputedDueToUndisclosed
     */
    public double getPerInvoicesDisputedDueToUndisclosed() {
        return perInvoicesDisputedDueToUndisclosed;
    }

    /**
     * @param perInvoicesDisputedDueToUndisclosed the perInvoicesDisputedDueToUndisclosed to set
     */
    public void setPerInvoicesDisputedDueToUndisclosed(double perInvoicesDisputedDueToUndisclosed) {
        this.perInvoicesDisputedDueToUndisclosed = perInvoicesDisputedDueToUndisclosed;
    }

    /**
     * @return the perInvoicesDisputedDueToOther
     */
    public double getPerInvoicesDisputedDueToOther() {
        return perInvoicesDisputedDueToOther;
    }

    /**
     * @param perInvoicesDisputedDueToOther the perInvoicesDisputedDueToOther to set
     */
    public void setPerInvoicesDisputedDueToOther(double perInvoicesDisputedDueToOther) {
        this.perInvoicesDisputedDueToOther = perInvoicesDisputedDueToOther;
    }

   


}
