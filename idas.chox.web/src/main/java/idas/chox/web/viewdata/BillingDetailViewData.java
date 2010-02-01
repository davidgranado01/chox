/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package idas.chox.web.viewdata;

import idas.chox.core.model.BillingDetail;
import idas.chox.core.util.DateHelper;
import java.math.BigDecimal;
import org.apache.log4j.Logger;

/**
 *
 * @author abrar
 */
public class BillingDetailViewData {
    private static final Logger log = Logger.getLogger(BillingDetailViewData.class);
    private int id;
    private String scheduleName;
    private String claimReferenceId;
    private BigDecimal itemAmount;
    private BigDecimal amountReceived;
    private String receivedDate;
    private String comment;
    private boolean reconciled;

    public BillingDetailViewData(BillingDetail record) {
        this.id = record.getId();
        this.scheduleName = record.getBilling().getScheduleName();
        this.claimReferenceId = record.getClaim().getClaimNumber();
        this.itemAmount = record.getBillAmount();
        this.amountReceived = record.getAmountReceived();
        this.receivedDate = record.getReceivedDate() == null ? "":DateHelper.LocalDateTimeFormat.format(record.getReceivedDate());
        this.comment = record.getComment();
        this.reconciled = record.isReconciled();
    }


    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
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
}
