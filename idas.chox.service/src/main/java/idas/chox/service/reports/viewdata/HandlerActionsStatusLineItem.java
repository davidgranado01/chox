package idas.chox.service.reports.viewdata;

import java.math.BigInteger;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandlerActionsStatusLineItem {

    private static final Logger LOG = LoggerFactory.getLogger(HandlerActionsStatusLineItem.class);
    private String workgroup;
    private String name;
    private Integer id;
    private Integer countClaimUnacknowledgedRouted ;
    private Integer countClaimPending ;
    private Integer countClaimRejectionContested ;
    private Integer countClaimUpdatedByEngineer ;
    private Integer countInvoiceEscalatedToHandler ;
    private Integer countContestedInvoiceReferredToInsurer ;
    private Integer countInvoiceApprovedByBRE ;
    private Integer countAwaitingLiabilityResolution ;
    private Integer countAwaitingInvoicePayment ;
    private Integer countManualInvoiceBREApproved;
    private Integer countManualInvoiceBRERejected;
    private Integer countManualInvoiceContested;

    public static HandlerActionsStatusLineItem getObject(Map<String, Object> data) {
        HandlerActionsStatusLineItem result = new HandlerActionsStatusLineItem();
        if (data.get("workgroup") == null)
            result.setWorkgroup("");
        else
            result.setWorkgroup(data.get("workgroup").toString());
        if (data.get("name") == null) {
            result.setName("");
            result.setId(0);
        } else {
            result.setName(data.get("name").toString());
            result.setId((Integer)data.get("id"));
        }
        
//        LOG.debug("Creating stats for: {}", result.getName());
//        LOG.debug("Creating stats for ID : {}", result.getId());

        return result;
    }

    public void updateObject(Map<String, Object> data, boolean isInsurerInvoiceUploadEnabled) {
        this.setCountClaimUnacknowledgedRouted(getIntegerValue(data.get("countClaimUnacknowledgedRouted".toLowerCase())));
        this.setCountClaimPending(getIntegerValue(data.get("countClaimPending".toLowerCase())));
        this.setCountClaimRejectionContested(getIntegerValue(data.get("countClaimRejectionContested".toLowerCase())));
        this.setCountClaimUpdatedByEngineer(getIntegerValue(data.get("countClaimUpdatedByEngineer".toLowerCase())));
        this.setCountInvoiceEscalatedToHandler(getIntegerValue(data.get("countInvoiceEscalatedToHandler".toLowerCase())));
        this.setCountContestedInvoiceReferredToInsurer(getIntegerValue(data.get("countContestedInvoiceReferredToInsurer".toLowerCase())));
        this.setCountInvoiceApprovedByBRE(getIntegerValue(data.get("countInvoiceApprovedByBRE".toLowerCase())));
        this.setCountAwaitingLiabilityResolution(getIntegerValue(data.get("countAwaitingLiabilityResolution".toLowerCase())));
        this.setCountAwaitingInvoicePayment(getIntegerValue(data.get("countAwaitingInvoicePayment".toLowerCase())));
        if (isInsurerInvoiceUploadEnabled) {
            this.setCountManualInvoiceBREApproved(getIntegerValue(data.get("countManualInvoiceBREApproved".toLowerCase())));
            this.setCountManualInvoiceBRERejected(getIntegerValue(data.get("countManualInvoiceBRERejected".toLowerCase())));
            this.setCountManualInvoiceContested(getIntegerValue(data.get("countManualInvoiceContested".toLowerCase())));
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

    public String getWorkgroup() {
        return workgroup;
    }

    public void setWorkgroup(String workgroup) {
        this.workgroup = workgroup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCountClaimUnacknowledgedRouted() {
        return countClaimUnacknowledgedRouted;
    }

    public Integer getCountClaimPending() {
        return countClaimPending;
    }

    public Integer getCountClaimRejectionContested() {
        return countClaimRejectionContested;
    }

    public Integer getCountClaimUpdatedByEngineer() {
        return countClaimUpdatedByEngineer;
    }

    public Integer getCountInvoiceEscalatedToHandler() {
        return countInvoiceEscalatedToHandler;
    }

    public Integer getCountContestedInvoiceReferredToInsurer() {
        return countContestedInvoiceReferredToInsurer;
    }

    public Integer getCountInvoiceApprovedByBRE() {
        return countInvoiceApprovedByBRE;
    }

    public Integer getCountAwaitingLiabilityResolution() {
        return countAwaitingLiabilityResolution;
    }

    public Integer getCountAwaitingInvoicePayment() {
        return countAwaitingInvoicePayment;
    }

    public void setCountClaimUnacknowledgedRouted(
            Integer countClaimUnacknowledgedRouted) {
        this.countClaimUnacknowledgedRouted = countClaimUnacknowledgedRouted;
    }

    public void setCountClaimPending(Integer countClaimPending) {
        this.countClaimPending = countClaimPending;
    }

    public void setCountClaimRejectionContested(Integer countClaimRejectionContested) {
        this.countClaimRejectionContested = countClaimRejectionContested;
    }

    public void setCountClaimUpdatedByEngineer(Integer countClaimUpdatedByEngineer) {
        this.countClaimUpdatedByEngineer = countClaimUpdatedByEngineer;
    }

    public void setCountInvoiceEscalatedToHandler(
            Integer countInvoiceEscalatedToHandler) {
        this.countInvoiceEscalatedToHandler = countInvoiceEscalatedToHandler;
    }

    public void setCountContestedInvoiceReferredToInsurer(
            Integer countContestedInvoiceReferredToInsurer) {
        this.countContestedInvoiceReferredToInsurer = countContestedInvoiceReferredToInsurer;
    }

    public void setCountInvoiceApprovedByBRE(Integer countInvoiceApprovedByBRE) {
        this.countInvoiceApprovedByBRE = countInvoiceApprovedByBRE;
    }

    public void setCountAwaitingLiabilityResolution(
            Integer countAwaitingLiabilityResolution) {
        this.countAwaitingLiabilityResolution = countAwaitingLiabilityResolution;
    }

    public void setCountAwaitingInvoicePayment(Integer countAwaitingInvoicePayment) {
        this.countAwaitingInvoicePayment = countAwaitingInvoicePayment;
    }

    public Integer getCountManualInvoiceBREApproved() {
        return countManualInvoiceBREApproved;
    }

    public void setCountManualInvoiceBREApproved(Integer countManualInvoiceBREApproved) {
        this.countManualInvoiceBREApproved = countManualInvoiceBREApproved;
    }

    public Integer getCountManualInvoiceBRERejected() {
        return countManualInvoiceBRERejected;
    }

    public void setCountManualInvoiceBRERejected(Integer countManualInvoiceBRERejected) {
        this.countManualInvoiceBRERejected = countManualInvoiceBRERejected;
    }

    public Integer getCountManualInvoiceContested() {
        return countManualInvoiceContested;
    }

    public void setCountManualInvoiceContested(Integer countManualInvoiceContested) {
        this.countManualInvoiceContested = countManualInvoiceContested;
    }
}
