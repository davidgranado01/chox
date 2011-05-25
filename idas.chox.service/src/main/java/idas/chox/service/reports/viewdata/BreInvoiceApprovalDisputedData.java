package idas.chox.service.reports.viewdata;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
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
    private BigDecimal perInvoicesApprovedByBusinessRulesDisputed = BigDecimal.ZERO;
    private BigDecimal perInvoicesApprovesByBusinessRulesNotDisputedPaid15Days = BigDecimal.ZERO;
    private BigDecimal perInvoicesApprovedByBusinessRulesNotDisputedPaid30Days = BigDecimal.ZERO;
    private BigDecimal perInvoicesApprovedByBusinessRulesDisputedPaid15Days = BigDecimal.ZERO;
    private BigDecimal perInvoicesApprovedByBusinessRulesDisputedPaid30Days = BigDecimal.ZERO;
    private BigDecimal perInvoicesDisputedDueToHireCharge = BigDecimal.ZERO;
    private BigDecimal perInvoicesDisputedDueToHireDuration = BigDecimal.ZERO;
    private BigDecimal perInvoicesDisputedDueToLIabilityDispute = BigDecimal.ZERO;
    private BigDecimal perInvoicesDisputedDueToLikeForLike = BigDecimal.ZERO;
    private BigDecimal perInvoicesDisputedDueToQuantum = BigDecimal.ZERO;
    private BigDecimal perInvoicesDisputedDueToRepairCost = BigDecimal.ZERO;
    private BigDecimal perInvoicesDisputedDueToInvoiceAlreadyPaid = BigDecimal.ZERO;
    private BigDecimal perInvoicesDisputedDueToUndisclosed = BigDecimal.ZERO;
    private BigDecimal perInvoicesDisputedDueToOther = BigDecimal.ZERO;


    public static BreInvoiceApprovalDisputedData getObject(Map data) {

        BreInvoiceApprovalDisputedData result = new BreInvoiceApprovalDisputedData();
        result.setHeaderName((String) data.get("month_header".toLowerCase()));
        LOG.debug("Column: {}", result.getHeaderName());
        result.setNoInvoicesUploaded(((BigInteger) data.get("invoice_uploaded_current_total".toLowerCase())).intValue());
        LOG.debug("NoInvoicesApprovedUploaded: {}", result.getNoInvoicesUploaded().intValue());
        result.setNoInvoicesApprovedByBusinessRules(((BigInteger) data.get("invoice_approved_by_bre_current".toLowerCase())).intValue());
        LOG.debug("NoInvoicesApprovedByBusinessRules: {}", result.getNoInvoicesApprovedByBusinessRules().intValue());
        result.setNoInvoicesApprovedByBusinessRulesDisputed(((BigInteger) data.get("invoice_approved_by_bre_disputed_current".toLowerCase())).intValue());
        LOG.debug("NoInvoicesApprovedByBusinessRulesDisputed: {}", result.getNoInvoicesApprovedByBusinessRulesDisputed().intValue());
LOG.debug(" number not disputed paid within 15 days: {}", data.get("invoice_approved_by_bre_not_disputed_paid_within_15days_current"));
LOG.debug(" number not disputed paid within 30 days: {}", data.get("invoice_approved_by_bre_not_disputed_paid_within_30days_current"));
LOG.debug(" number disputed paid within 15 days: {}", data.get("invoice_approved_by_bre_disputed_paid_within_15days_current"));
LOG.debug(" number disputed paid within 30 days: {}", data.get("invoice_approved_by_bre_disputed_paid_within_30days_current"));
        if(result.getNoInvoicesApprovedByBusinessRules().intValue() != 0){
            result.setPerInvoicesApprovedByBusinessRulesDisputed(new BigDecimal((result.getNoInvoicesApprovedByBusinessRulesDisputed() * 1.0) / result.getNoInvoicesApprovedByBusinessRules()).setScale(4, RoundingMode.HALF_UP));
            if (result.getNoInvoicesApprovedByBusinessRules() - result.getNoInvoicesApprovedByBusinessRulesDisputed() != 0) {
                result.setPerInvoicesApprovesByBusinessRulesNotDisputedPaid15Days(new BigDecimal(getIntegerValue(data.get("invoice_approved_by_bre_not_disputed_paid_within_15days_current")) * 1.0 / (result.getNoInvoicesApprovedByBusinessRules() - result.getNoInvoicesApprovedByBusinessRulesDisputed())).setScale(4, RoundingMode.HALF_UP));
                result.setPerInvoicesApprovedByBusinessRulesNotDisputedPaid30Days(new BigDecimal(getIntegerValue(data.get("invoice_approved_by_bre_not_disputed_paid_within_30days_current")) * 1.0 / (result.getNoInvoicesApprovedByBusinessRules() - result.getNoInvoicesApprovedByBusinessRulesDisputed())).setScale(4, RoundingMode.HALF_UP));
            }
            if (result.getNoInvoicesApprovedByBusinessRulesDisputed().intValue() != 0) {
                result.setPerInvoicesApprovedByBusinessRulesDisputedPaid15Days(new BigDecimal(getIntegerValue(data.get("invoice_approved_by_bre_disputed_paid_within_15days_current")) * 1.0 / result.getNoInvoicesApprovedByBusinessRulesDisputed()).setScale(4, RoundingMode.HALF_UP));
                result.setPerInvoicesApprovedByBusinessRulesDisputedPaid30Days(new BigDecimal(getIntegerValue(data.get("invoice_approved_by_bre_disputed_paid_within_30days_current")) * 1.0 / result.getNoInvoicesApprovedByBusinessRulesDisputed()).setScale(4, RoundingMode.HALF_UP));
            }
        }
        if(result.getNoInvoicesApprovedByBusinessRulesDisputed().intValue() !=0){
            result.setPerInvoicesDisputedDueToHireCharge(new BigDecimal(getIntegerValue(data.get("invoice_disputed_due_to_hire_charge_current")) * 1.0 / result.getNoInvoicesApprovedByBusinessRulesDisputed()).setScale(4, RoundingMode.HALF_UP));
            result.setPerInvoicesDisputedDueToHireDuration(new BigDecimal(getIntegerValue(data.get("invoice_disputed_due_to_hire_duration_current")) * 1.0 / result.getNoInvoicesApprovedByBusinessRulesDisputed()).setScale(4, RoundingMode.HALF_UP));
            result.setPerInvoicesDisputedDueToLIabilityDispute(new BigDecimal(getIntegerValue(data.get("invoice_disputed_due_to_liability_dispute_current")) * 1.0 / result.getNoInvoicesApprovedByBusinessRulesDisputed()).setScale(4, RoundingMode.HALF_UP));
            result.setPerInvoicesDisputedDueToLikeForLike(new BigDecimal(getIntegerValue(data.get("invoice_disputed_due_to_like_for_like_current")) * 1.0 / result.getNoInvoicesApprovedByBusinessRulesDisputed()).setScale(4, RoundingMode.HALF_UP));
            result.setPerInvoicesDisputedDueToQuantum(new BigDecimal(getIntegerValue(data.get("invoice_disputed_due_to_quantum_current")) * 1.0 / result.getNoInvoicesApprovedByBusinessRulesDisputed()).setScale(4, RoundingMode.HALF_UP));
            result.setPerInvoicesDisputedDueToRepairCost(new BigDecimal(getIntegerValue(data.get("invoice_disputed_due_to_repair_cost_current")) * 1.0 / result.getNoInvoicesApprovedByBusinessRulesDisputed()).setScale(4, RoundingMode.HALF_UP));
            result.setPerInvoicesDisputedDueToInvoiceAlreadyPaid(new BigDecimal(getIntegerValue(data.get("invoice_disputed_due_to_invoice_already_paid_current")) * 1.0 / result.getNoInvoicesApprovedByBusinessRulesDisputed()).setScale(4, RoundingMode.HALF_UP));
            result.setPerInvoicesDisputedDueToUndisclosed(new BigDecimal(getIntegerValue(data.get("invoice_disputed_due_to_undisclosed_current")) * 1.0 / result.getNoInvoicesApprovedByBusinessRulesDisputed()).setScale(4, RoundingMode.HALF_UP));
            result.setPerInvoicesDisputedDueToOther(new BigDecimal(getIntegerValue(data.get("invoice_disputed_due_to_other_current")) * 1.0 / result.getNoInvoicesApprovedByBusinessRulesDisputed()).setScale(4, RoundingMode.HALF_UP));
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

     private static Integer getIntegerValue(Object v) {
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
    public BigDecimal getPerInvoicesApprovedByBusinessRulesDisputed() {
        return perInvoicesApprovedByBusinessRulesDisputed;
    }

    /**
     * @param perInvoicesApprovedByBusinessRulesDisputed the perInvoicesApprovedByBusinessRulesDisputed to set
     */
    public void setPerInvoicesApprovedByBusinessRulesDisputed(BigDecimal perInvoicesApprovedByBusinessRulesDisputed) {

        this.perInvoicesApprovedByBusinessRulesDisputed = perInvoicesApprovedByBusinessRulesDisputed;

        LOG.debug("perInvoicesApprovedByBusinessRulesDisputed  :{}",perInvoicesApprovedByBusinessRulesDisputed);
    }

    /**
     * @return the perInvoicesApprovesByBusinessRulesNotDisputedPaid15Days
     */
    public BigDecimal getPerInvoicesApprovesByBusinessRulesNotDisputedPaid15Days() {
        return perInvoicesApprovesByBusinessRulesNotDisputedPaid15Days;
    }

    /**
     * @param perInvoicesApprovesByBusinessRulesNotDisputedPaid15Days the perInvoicesApprovesByBusinessRulesNotDisputedPaid15Days to set
     */
    public void setPerInvoicesApprovesByBusinessRulesNotDisputedPaid15Days(BigDecimal perInvoicesApprovesByBusinessRulesNotDisputedPaid15Days) {
        this.perInvoicesApprovesByBusinessRulesNotDisputedPaid15Days = perInvoicesApprovesByBusinessRulesNotDisputedPaid15Days;
    }

    /**
     * @return the perInvoicesApprovedByBusinessRulesNotDisputedPaid30Days
     */
    public BigDecimal getPerInvoicesApprovedByBusinessRulesNotDisputedPaid30Days() {
        return perInvoicesApprovedByBusinessRulesNotDisputedPaid30Days;
    }

    /**
     * @param perInvoicesApprovedByBusinessRulesNotDisputedPaid30Days the perInvoicesApprovedByBusinessRulesNotDisputedPaid30Days to set
     */
    public void setPerInvoicesApprovedByBusinessRulesNotDisputedPaid30Days(BigDecimal perInvoicesApprovedByBusinessRulesNotDisputedPaid30Days) {
        this.perInvoicesApprovedByBusinessRulesNotDisputedPaid30Days = perInvoicesApprovedByBusinessRulesNotDisputedPaid30Days;
    }

    /**
     * @return the perInvoicesApprovedByBusinessRulesDisputedPaid15Days
     */
    public BigDecimal getPerInvoicesApprovedByBusinessRulesDisputedPaid15Days() {
        return perInvoicesApprovedByBusinessRulesDisputedPaid15Days;
    }

    /**
     * @param perInvoicesApprovedByBusinessRulesDisputedPaid15Days the perInvoicesApprovedByBusinessRulesDisputedPaid15Days to set
     */
    public void setPerInvoicesApprovedByBusinessRulesDisputedPaid15Days(BigDecimal perInvoicesApprovedByBusinessRulesDisputedPaid15Days) {
        this.perInvoicesApprovedByBusinessRulesDisputedPaid15Days = perInvoicesApprovedByBusinessRulesDisputedPaid15Days;
    }

    /**
     * @return the perInvoicesApprovedByBusinessRulesDisputedPaid30Days
     */
    public BigDecimal getPerInvoicesApprovedByBusinessRulesDisputedPaid30Days() {
        return perInvoicesApprovedByBusinessRulesDisputedPaid30Days;
    }

    /**
     * @param perInvoicesApprovedByBusinessRulesDisputedPaid30Days the perInvoicesApprovedByBusinessRulesDisputedPaid30Days to set
     */
    public void setPerInvoicesApprovedByBusinessRulesDisputedPaid30Days(BigDecimal perInvoicesApprovedByBusinessRulesDisputedPaid30Days) {
        this.perInvoicesApprovedByBusinessRulesDisputedPaid30Days = perInvoicesApprovedByBusinessRulesDisputedPaid30Days;
    }

    /**
     * @return the perInvoicesDisputedDueToHireCharge
     */
    public BigDecimal getPerInvoicesDisputedDueToHireCharge() {
        return perInvoicesDisputedDueToHireCharge;
    }

    /**
     * @param perInvoicesDisputedDueToHireCharge the perInvoicesDisputedDueToHireCharge to set
     */
    public void setPerInvoicesDisputedDueToHireCharge(BigDecimal perInvoicesDisputedDueToHireCharge) {
        this.perInvoicesDisputedDueToHireCharge = perInvoicesDisputedDueToHireCharge;
    }

    /**
     * @return the perInvoicesDisputedDueToHireDuration
     */
    public BigDecimal getPerInvoicesDisputedDueToHireDuration() {
        return perInvoicesDisputedDueToHireDuration;
    }

    /**
     * @param perInvoicesDisputedDueToHireDuration the perInvoicesDisputedDueToHireDuration to set
     */
    public void setPerInvoicesDisputedDueToHireDuration(BigDecimal perInvoicesDisputedDueToHireDuration) {
        this.perInvoicesDisputedDueToHireDuration = perInvoicesDisputedDueToHireDuration;
    }

    /**
     * @return the perInvoicesDisputedDueToLIabilityDispute
     */
    public BigDecimal getPerInvoicesDisputedDueToLIabilityDispute() {
        return perInvoicesDisputedDueToLIabilityDispute;
    }

    /**
     * @param perInvoicesDisputedDueToLIabilityDispute the perInvoicesDisputedDueToLIabilityDispute to set
     */
    public void setPerInvoicesDisputedDueToLIabilityDispute(BigDecimal perInvoicesDisputedDueToLIabilityDispute) {
        this.perInvoicesDisputedDueToLIabilityDispute = perInvoicesDisputedDueToLIabilityDispute;
    }

    /**
     * @return the perInvoicesDisputedDueToLikeForLike
     */
    public BigDecimal getPerInvoicesDisputedDueToLikeForLike() {
        return perInvoicesDisputedDueToLikeForLike;
    }

    /**
     * @param perInvoicesDisputedDueToLikeForLike the perInvoicesDisputedDueToLikeForLike to set
     */
    public void setPerInvoicesDisputedDueToLikeForLike(BigDecimal perInvoicesDisputedDueToLikeForLike) {
        this.perInvoicesDisputedDueToLikeForLike = perInvoicesDisputedDueToLikeForLike;
    }

    /**
     * @return the perInvoicesDisputedDueToQuantum
     */
    public BigDecimal getPerInvoicesDisputedDueToQuantum() {
        return perInvoicesDisputedDueToQuantum;
    }

    /**
     * @param perInvoicesDisputedDueToQuantum the perInvoicesDisputedDueToQuantum to set
     */
    public void setPerInvoicesDisputedDueToQuantum(BigDecimal perInvoicesDisputedDueToQuantum) {
        this.perInvoicesDisputedDueToQuantum = perInvoicesDisputedDueToQuantum;
    }

    /**
     * @return the perInvoicesDisputedDueToRepairCost
     */
    public BigDecimal getPerInvoicesDisputedDueToRepairCost() {
        return perInvoicesDisputedDueToRepairCost;
    }

    /**
     * @param perInvoicesDisputedDueToRepairCost the perInvoicesDisputedDueToRepairCost to set
     */
    public void setPerInvoicesDisputedDueToRepairCost(BigDecimal perInvoicesDisputedDueToRepairCost) {
        this.perInvoicesDisputedDueToRepairCost = perInvoicesDisputedDueToRepairCost;
    }

    /**
     * @return the perInvoicesDisputedDueToInvoiceAlreadyPaid
     */
    public BigDecimal getPerInvoicesDisputedDueToInvoiceAlreadyPaid() {
        return perInvoicesDisputedDueToInvoiceAlreadyPaid;
    }

    /**
     * @param perInvoicesDisputedDueToInvoiceAlreadyPaid the perInvoicesDisputedDueToInvoiceAlreadyPaid to set
     */
    public void setPerInvoicesDisputedDueToInvoiceAlreadyPaid(BigDecimal perInvoicesDisputedDueToInvoiceAlreadyPaid) {
        this.perInvoicesDisputedDueToInvoiceAlreadyPaid = perInvoicesDisputedDueToInvoiceAlreadyPaid;
    }

    /**
     * @return the perInvoicesDisputedDueToUndisclosed
     */
    public BigDecimal getPerInvoicesDisputedDueToUndisclosed() {
        return perInvoicesDisputedDueToUndisclosed;
    }

    /**
     * @param perInvoicesDisputedDueToUndisclosed the perInvoicesDisputedDueToUndisclosed to set
     */
    public void setPerInvoicesDisputedDueToUndisclosed(BigDecimal perInvoicesDisputedDueToUndisclosed) {
        this.perInvoicesDisputedDueToUndisclosed = perInvoicesDisputedDueToUndisclosed;
    }

    /**
     * @return the perInvoicesDisputedDueToOther
     */
    public BigDecimal getPerInvoicesDisputedDueToOther() {
        return perInvoicesDisputedDueToOther;
    }

    /**
     * @param perInvoicesDisputedDueToOther the perInvoicesDisputedDueToOther to set
     */
    public void setPerInvoicesDisputedDueToOther(BigDecimal perInvoicesDisputedDueToOther) {
        this.perInvoicesDisputedDueToOther = perInvoicesDisputedDueToOther;
    }

   


}
