package idas.chox.web.viewdata;


import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.LoggerFactory;

import idas.chox.core.model.BillingDetail;
import idas.chox.core.util.DateHelper;

/**
 *
 * @author abrar
 */
public class BillingDetailViewData {
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(BillingDetailViewData.class);
    private int billingDetailId;
    private String scheduleName;
    private String claimReferenceId;
    private BigDecimal itemAmount;
    private BigDecimal amountReceived;
    private String receivedDate;
    private String triggerDate;
    private String triggerPoint;
    private String comment;
    private boolean reconciled;

    public BillingDetailViewData(BillingDetail record) {
        this.billingDetailId = record.getId();
        this.scheduleName = record.getBilling().getScheduleName();
        this.claimReferenceId = record.getClaim().getClaimNumber();
        this.itemAmount = record.getGrossBillAmount();
        this.amountReceived = record.getAmountReceived();
        this.receivedDate = record.getReceivedDate()== null ? "":DateHelper.getEXTDateTimeFormat().format(record.getReceivedDate());
        this.triggerDate = record.getTriggerDate()== null ? "":DateHelper.getEXTDateTimeFormat().format(record.getTriggerDate());
        this.triggerPoint = record.getTriggerPoint();
        this.comment = record.getComment() == null || record.getComment().equals("null") ? "" : record.getComment();
        this.reconciled = record.isReconciled();
    }

    public static List<Map<String, String>> mapListFromJsonString(String json) throws ParseException{
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, String>> ll = null;
        try {
            ll = mapper.readValue(json, new TypeReference<List<Map<String, String>>>(){});
        } catch (IOException ex) {
            LOG.error("Error converting json string '{}' to object: {}", json, ex.getMessage());

        }
        return ll;
    }


    public BillingDetailViewData() {}

    /**
     * @return the id
     */
    public int getBillingDetailId() {
        return billingDetailId;
    }

    /**
     * @param id the id to set
     */
    public void setBillingDetailId(int id) {
        this.billingDetailId = id;
    }

    /**
     * @return the scheduleName
     */
    public String getScheduleName() {
        return scheduleName;
    }

    /**
     * @param scheduleName the scheduleName to set
     */
    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    /**
     * @return the claimReferenceId
     */
    public String getClaimReferenceId() {
        return claimReferenceId;
    }

    /**
     * @param claimReferenceId the claimReferenceId to set
     */
    public void setClaimReferenceId(String claimReferenceId) {
        this.claimReferenceId = claimReferenceId;
    }

    /**
     * @return the itemAmount
     */
    public BigDecimal getItemAmount() {
        return itemAmount;
    }

    /**
     * @param itemAmount the itemAmount to set
     */
    public void setItemAmount(BigDecimal itemAmount) {
        this.itemAmount = itemAmount;
    }

    /**
     * @return the amountReceived
     */
    public BigDecimal getAmountReceived() {
        return amountReceived;
    }

    /**
     * @param amountReceived the amountReceived to set
     */
    public void setAmountReceived(BigDecimal amountReceived) {
        this.amountReceived = amountReceived;
    }

    /**
     * @return the triggerDate
     */
    public String getTriggerDate() {
        return triggerDate;
    }

    /**
     * @param triggerDate the triggerDate to set
     */
    public void setTriggerDate(String triggerDate) {
        this.triggerDate = triggerDate;
    }

    /**
     * @return the receivedDate
     */
    public String getReceivedDate() {
        return receivedDate;
    }

    /**
     * @param receivedDate the receivedDate to set
     */
    public void setReceivedDate(String receivedDate) {
        this.receivedDate = receivedDate;
    }
    /**
     * @return the reconciled
     */
    public boolean isReconciled() {
        return reconciled;
    }

    /**
     * @param reconciled the reconciled to set
     */
    public void setReconciled(boolean reconciled) {
        this.reconciled = reconciled;
    }

    /**
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTriggerPoint() {
        return triggerPoint;
    }
}
