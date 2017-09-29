package idas.chox.service.reports.viewdata;

import idas.chox.core.model.ReasonOfRejection;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
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
    private Map<Integer, BigDecimal> disputedApprovalReasonsMap;

    public static BreInvoiceApprovalDisputedData getObject(Map data, List<ReasonOfRejection> reasonsOfRejection) {

        BreInvoiceApprovalDisputedData result = new BreInvoiceApprovalDisputedData();
        result.setHeaderName((String) data.get("month_header".toLowerCase()));
        LOG.debug("Column: {}", result.getHeaderName());
        result.setNoInvoicesUploaded(((BigInteger) data.get("invoice_uploaded_current_total".toLowerCase())).intValue());
        LOG.debug("NoInvoicesApprovedUploaded: {}", result.getNoInvoicesUploaded());
        result.setNoInvoicesApprovedByBusinessRules(((BigInteger) data.get("invoice_approved_by_bre_current".toLowerCase())).intValue());
        LOG.debug("NoInvoicesApprovedByBusinessRules: {}", result.getNoInvoicesApprovedByBusinessRules());
        result.setNoInvoicesApprovedByBusinessRulesDisputed(((BigInteger) data.get("invoice_approved_by_bre_disputed_current".toLowerCase())).intValue());
        LOG.debug("NoInvoicesApprovedByBusinessRulesDisputed: {}", result.getNoInvoicesApprovedByBusinessRulesDisputed());
        LOG.debug(" number not disputed paid within 15 days: {}", data.get("invoice_approved_by_bre_not_disputed_paid_within_15days_current"));
        LOG.debug(" number not disputed paid within 30 days: {}", data.get("invoice_approved_by_bre_not_disputed_paid_within_30days_current"));
        LOG.debug(" number disputed paid within 15 days: {}", data.get("invoice_approved_by_bre_disputed_paid_within_15days_current"));
        LOG.debug(" number disputed paid within 30 days: {}", data.get("invoice_approved_by_bre_disputed_paid_within_30days_current"));
        if(result.getNoInvoicesApprovedByBusinessRules() != 0){
            result.setPerInvoicesApprovedByBusinessRulesDisputed(new BigDecimal((result.getNoInvoicesApprovedByBusinessRulesDisputed() * 1.0) / result.getNoInvoicesApprovedByBusinessRules()).setScale(4, RoundingMode.HALF_UP));
            if (result.getNoInvoicesApprovedByBusinessRules() - result.getNoInvoicesApprovedByBusinessRulesDisputed() != 0) {
                result.setPerInvoicesApprovesByBusinessRulesNotDisputedPaid15Days(new BigDecimal(getIntegerValue(data.get("invoice_approved_by_bre_not_disputed_paid_within_15days_current")) * 1.0 / (result.getNoInvoicesApprovedByBusinessRules() - result.getNoInvoicesApprovedByBusinessRulesDisputed())).setScale(4, RoundingMode.HALF_UP));
                result.setPerInvoicesApprovedByBusinessRulesNotDisputedPaid30Days(new BigDecimal(getIntegerValue(data.get("invoice_approved_by_bre_not_disputed_paid_within_30days_current")) * 1.0 / (result.getNoInvoicesApprovedByBusinessRules() - result.getNoInvoicesApprovedByBusinessRulesDisputed())).setScale(4, RoundingMode.HALF_UP));
            }
            if (result.getNoInvoicesApprovedByBusinessRulesDisputed() != 0) {
                result.setPerInvoicesApprovedByBusinessRulesDisputedPaid15Days(new BigDecimal(getIntegerValue(data.get("invoice_approved_by_bre_disputed_paid_within_15days_current")) * 1.0 / result.getNoInvoicesApprovedByBusinessRulesDisputed()).setScale(4, RoundingMode.HALF_UP));
                result.setPerInvoicesApprovedByBusinessRulesDisputedPaid30Days(new BigDecimal(getIntegerValue(data.get("invoice_approved_by_bre_disputed_paid_within_30days_current")) * 1.0 / result.getNoInvoicesApprovedByBusinessRulesDisputed()).setScale(4, RoundingMode.HALF_UP));
            }
        }
        if(result.getNoInvoicesApprovedByBusinessRulesDisputed() !=0){
            Map<Integer, BigDecimal> drorMap = new HashMap<>();
            reasonsOfRejection.forEach((ror) -> {
                drorMap.put(ror.getId(), new BigDecimal(getIntegerValue(data.get("invoice_disputed_due_to_" + ror.getRorName().toLowerCase())) * 1.0 / result.getNoInvoicesApprovedByBusinessRulesDisputed()).setScale(4, RoundingMode.HALF_UP));
            });
            result.setDisputedApprovalReasonsMap(drorMap);
        } else {
            Map<Integer, BigDecimal> drorMap = new HashMap<>();
            reasonsOfRejection.forEach((ror) -> {
                drorMap.put(ror.getId(), (BigDecimal.ZERO));
            });
            result.setDisputedApprovalReasonsMap(drorMap);
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
        StringBuilder s=new StringBuilder(tgt.toLowerCase());
        s.setCharAt(0,Character.toUpperCase(s.charAt(0)));

        String ns=s.toString();

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

    public Map<Integer, BigDecimal> getDisputedApprovalReasonsMap() {
        return disputedApprovalReasonsMap;
    }

    public void setDisputedApprovalReasonsMap(
            Map<Integer, BigDecimal> disputedApprovalReasonsMap) {
        this.disputedApprovalReasonsMap = disputedApprovalReasonsMap;
    }

}
