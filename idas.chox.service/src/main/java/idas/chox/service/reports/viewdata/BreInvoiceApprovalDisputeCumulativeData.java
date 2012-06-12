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
public class BreInvoiceApprovalDisputeCumulativeData {

    private static final Logger LOG = LoggerFactory.getLogger(BreInvoiceApprovalDisputeCumulativeData.class);

    private String headerNameCumm;
    private Integer noInvoicesUploadedCumm;
    private Integer noInvoicesApprovedByBusinessRulesCumm;
    private Integer noInvoicesApprovedByBusinessRulesDisputedCumm;
    private BigDecimal perInvoicesApprovedByBusinessRulesDisputedCumm = BigDecimal.ZERO;
    private BigDecimal perInvoicesApprovesByBusinessRulesNotDisputedPaid15DaysCumm = BigDecimal.ZERO;
    private BigDecimal perInvoicesApprovedByBusinessRulesNotDisputedPaid30DaysCumm = BigDecimal.ZERO;
    private BigDecimal perInvoicesApprovedByBusinessRulesDisputedPaid15DaysCumm = BigDecimal.ZERO;
    private BigDecimal perInvoicesApprovedByBusinessRulesDisputedPaid30DaysCumm = BigDecimal.ZERO;
    
    private Map<Integer, BigDecimal> disputedApprovalReasonsMap;

    public static BreInvoiceApprovalDisputeCumulativeData getObject(Map data, List<ReasonOfRejection> reasonsOfRejection) {

        BreInvoiceApprovalDisputeCumulativeData result = new BreInvoiceApprovalDisputeCumulativeData();
        result.setHeaderNameCumm((String) data.get("month_header".toLowerCase()));
        result.setNoInvoicesUploadedCumm(((BigInteger) data.get("invoice_uploaded_total".toLowerCase())).intValue());
        result.setNoInvoicesApprovedByBusinessRulesCumm(((BigInteger) data.get("invoice_approved_by_bre_total".toLowerCase())).intValue());
        result.setNoInvoicesApprovedByBusinessRulesDisputedCumm(((BigInteger) data.get("invoice_approved_by_bre_disputed_total".toLowerCase())).intValue());


        if(result.getNoInvoicesApprovedByBusinessRulesCumm().intValue() != 0){
            result.setPerInvoicesApprovedByBusinessRulesDisputedCumm(new BigDecimal(getIntegerValue(data.get("invoice_approved_by_bre_disputed_total")) * 1.0 / result.getNoInvoicesApprovedByBusinessRulesCumm()).setScale(2, RoundingMode.HALF_UP));
            if (result.getNoInvoicesApprovedByBusinessRulesCumm() - result.getNoInvoicesApprovedByBusinessRulesDisputedCumm() != 0) {
                result.setPerInvoicesApprovesByBusinessRulesNotDisputedPaid15DaysCumm(new BigDecimal(getIntegerValue(data.get("invoice_approved_by_bre_not_disputed_paid_within_15days_total")) * 1.0 / (result.getNoInvoicesApprovedByBusinessRulesCumm() - result.getNoInvoicesApprovedByBusinessRulesDisputedCumm())).setScale(2, RoundingMode.HALF_UP));
                result.setPerInvoicesApprovedByBusinessRulesNotDisputedPaid30DaysCumm(new BigDecimal(getIntegerValue(data.get("invoice_approved_by_bre_not_disputed_paid_within_30days_total")) * 1.0 / (result.getNoInvoicesApprovedByBusinessRulesCumm() - result.getNoInvoicesApprovedByBusinessRulesDisputedCumm())).setScale(2, RoundingMode.HALF_UP));
            }
            if (result.getNoInvoicesApprovedByBusinessRulesDisputedCumm().intValue() != 0) {
                result.setPerInvoicesApprovedByBusinessRulesDisputedPaid15DaysCumm(new BigDecimal(getIntegerValue(data.get("invoice_approved_by_bre_disputed_paid_within_15days_total")) * 1.0 / result.getNoInvoicesApprovedByBusinessRulesDisputedCumm()).setScale(2, RoundingMode.HALF_UP));
                result.setPerInvoicesApprovedByBusinessRulesDisputedPaid30DaysCumm(new BigDecimal(getIntegerValue(data.get("invoice_approved_by_bre_disputed_paid_within_30days_total")) * 1.0 / result.getNoInvoicesApprovedByBusinessRulesDisputedCumm()).setScale(2, RoundingMode.HALF_UP));
            }
        }

        if (result.getNoInvoicesApprovedByBusinessRulesDisputedCumm().intValue() != 0) {
            Map<Integer, BigDecimal> drorMap = new HashMap<Integer, BigDecimal>();
            for (ReasonOfRejection ror : reasonsOfRejection) {
                drorMap.put(ror.getId(), new BigDecimal(getIntegerValue(data.get("invoice_disputed_due_to_" + ror.getName().toLowerCase())) * 1.0 / result.getNoInvoicesApprovedByBusinessRulesDisputedCumm()).setScale(2, RoundingMode.HALF_UP));
            }
            result.setDisputedApprovalReasonsMap(drorMap);
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
    public BigDecimal getPerInvoicesApprovedByBusinessRulesDisputedCumm() {
        return perInvoicesApprovedByBusinessRulesDisputedCumm;
    }

    /**
     * @param perInvoicesApprovedByBusinessRulesDisputedCumm the perInvoicesApprovedByBusinessRulesDisputedCumm to set
     */
    public void setPerInvoicesApprovedByBusinessRulesDisputedCumm(BigDecimal perInvoicesApprovedByBusinessRulesDisputedCumm) {
        this.perInvoicesApprovedByBusinessRulesDisputedCumm = perInvoicesApprovedByBusinessRulesDisputedCumm;
    }

    /**
     * @return the perInvoicesApprovesByBusinessRulesNotDisputedPaid15DaysCumm
     */
    public BigDecimal getPerInvoicesApprovesByBusinessRulesNotDisputedPaid15DaysCumm() {
        return perInvoicesApprovesByBusinessRulesNotDisputedPaid15DaysCumm;
    }

    /**
     * @param perInvoicesApprovesByBusinessRulesNotDisputedPaid15DaysCumm the perInvoicesApprovesByBusinessRulesNotDisputedPaid15DaysCumm to set
     */
    public void setPerInvoicesApprovesByBusinessRulesNotDisputedPaid15DaysCumm(BigDecimal perInvoicesApprovesByBusinessRulesNotDisputedPaid15DaysCumm) {
        this.perInvoicesApprovesByBusinessRulesNotDisputedPaid15DaysCumm = perInvoicesApprovesByBusinessRulesNotDisputedPaid15DaysCumm;
    }

    /**
     * @return the perInvoicesApprovedByBusinessRulesNotDisputedPaid30DaysCumm
     */
    public BigDecimal getPerInvoicesApprovedByBusinessRulesNotDisputedPaid30DaysCumm() {
        return perInvoicesApprovedByBusinessRulesNotDisputedPaid30DaysCumm;
    }

    /**
     * @param perInvoicesApprovedByBusinessRulesNotDisputedPaid30DaysCumm the perInvoicesApprovedByBusinessRulesNotDisputedPaid30DaysCumm to set
     */
    public void setPerInvoicesApprovedByBusinessRulesNotDisputedPaid30DaysCumm(BigDecimal perInvoicesApprovedByBusinessRulesNotDisputedPaid30DaysCumm) {
        this.perInvoicesApprovedByBusinessRulesNotDisputedPaid30DaysCumm = perInvoicesApprovedByBusinessRulesNotDisputedPaid30DaysCumm;
    }

    /**
     * @return the perInvoicesApprovedByBusinessRulesDisputedPaid15DaysCumm
     */
    public BigDecimal getPerInvoicesApprovedByBusinessRulesDisputedPaid15DaysCumm() {
        return perInvoicesApprovedByBusinessRulesDisputedPaid15DaysCumm;
    }

    /**
     * @param perInvoicesApprovedByBusinessRulesDisputedPaid15DaysCumm the perInvoicesApprovedByBusinessRulesDisputedPaid15DaysCumm to set
     */
    public void setPerInvoicesApprovedByBusinessRulesDisputedPaid15DaysCumm(BigDecimal perInvoicesApprovedByBusinessRulesDisputedPaid15DaysCumm) {
        this.perInvoicesApprovedByBusinessRulesDisputedPaid15DaysCumm = perInvoicesApprovedByBusinessRulesDisputedPaid15DaysCumm;
    }

    /**
     * @return the perInvoicesApprovedByBusinessRulesDisputedPaid30DaysCumm
     */
    public BigDecimal getPerInvoicesApprovedByBusinessRulesDisputedPaid30DaysCumm() {
        return perInvoicesApprovedByBusinessRulesDisputedPaid30DaysCumm;
    }

    /**
     * @param perInvoicesApprovedByBusinessRulesDisputedPaid30DaysCumm the perInvoicesApprovedByBusinessRulesDisputedPaid30DaysCumm to set
     */
    public void setPerInvoicesApprovedByBusinessRulesDisputedPaid30DaysCumm(BigDecimal perInvoicesApprovedByBusinessRulesDisputedPaid30DaysCumm) {
        this.perInvoicesApprovedByBusinessRulesDisputedPaid30DaysCumm = perInvoicesApprovedByBusinessRulesDisputedPaid30DaysCumm;
    }

    public Map<Integer, BigDecimal> getDisputedApprovalReasonsMap() {
        return disputedApprovalReasonsMap;
    }

    public void setDisputedApprovalReasonsMap(
            Map<Integer, BigDecimal> disputedApprovalReasonsMap) {
        this.disputedApprovalReasonsMap = disputedApprovalReasonsMap;
    }
}
