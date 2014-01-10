package idas.chox.service.dashboard;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DashBoardViewData {

    private static final Logger LOG = LoggerFactory.getLogger(DashBoardViewData.class);
    private static final Logger logger = LoggerFactory.getLogger(DashBoardViewData.class);

    private Integer noOfClaimNotificationsSubmitted;
    private Integer noOfClaimNotificationsAccepted;
    private Integer noOfClaimNotificationsRejectionsAccepted;
    private Integer noOfClaimsAwaitingToBeProcessed;
    private Integer noOfClaimNotificationsClosed;
    private Integer noOfInvoicesSubmitted;
    private BigDecimal valueOfInvoicesSubmitted;
    private Integer noOfInvoicesAccepted;
    private BigDecimal valueOfInvoicesAccepted;
    private Integer noOfInvoicesRejected;
    private BigDecimal valueOfInvoicesRejected;
    private Integer noOfInvoicesPending;
    private BigDecimal valueOfInvoicesPending;
    private Integer noOfInvoicesAwaitingLiabilityResolution;
    private BigDecimal valueOfInvoicesAwaitingLiabilityResolution;
    private Integer noOfInvoicesClosed;
    private BigDecimal valueOfInvoicesClosed;
    private Integer noOfInvoicesPaymentLogged;
    private BigDecimal valueOfInvoicesPaymentLogged;
    private Integer noOfInvoicesPaymentReceived;
    private BigDecimal valueOfInvoicesPaymentReceived;
    private BigDecimal totalValueOfPenaltyChargesApplied;
    private BigDecimal totalValueOfPenaltyChargesPaid;
    private BigDecimal avgInvoicePaymentTime;
    private BigDecimal avgManualInvoicePaymentTime;
    private Integer noOfInsurerClaimsSubmitted;
    private Integer noOfManualInvoicesSubmitted;
    private BigDecimal valueOfManualInvoicesSubmitted;
    private Integer noOfManualInvoicesAccepted;
    private BigDecimal valueOfManualInvoicesAccepted;
    private Integer noOfManualInvoicesAwaitingLiabilityResolution;
    private BigDecimal valueOfManualInvoicesAwaitingLiabilityResolution;
    private Integer noOfManualInvoicesPaid;
    private BigDecimal valueOfManualInvoicesPaid;
    private Integer noOfManualInvoicesClosed;
    private BigDecimal valueOfManualInvoicesClosed;
    private Integer noOfInvoicesAwaitingLitigationOutcome;
    private BigDecimal valueOfInvoicesAwaitingLitigationOutcome;
    private BigDecimal valueOfManualInvoicesPenaltyChargesPaid;

    public static DashBoardViewData getObject(Map data) {

        Iterator iterator = data.keySet().iterator();

            while (iterator.hasNext()) {

            String key = (String) iterator.next();

            logger.debug("key " + key + " value " + data.get(key));

         }
        
        DashBoardViewData viewData = new DashBoardViewData();
        viewData.setNoOfClaimNotificationsSubmitted(getIntegerValue(data.get("n_ClaimNotificationsSubmitted".toLowerCase())));
        viewData.setNoOfClaimNotificationsAccepted(getIntegerValue(data.get("n_ClaimNotificationsAccepted".toLowerCase())));
        viewData.setNoOfClaimNotificationsRejectionsAccepted(getIntegerValue(data.get("n_ClaimNotificationsRejectionsAccepted".toLowerCase())));
        viewData.setNoOfClaimsAwaitingToBeProcessed(getIntegerValue(data.get("n_ClaimsAwaitingToBeProcessed".toLowerCase())));
        viewData.setNoOfClaimNotificationsClosed(getIntegerValue(data.get("n_ClaimNotificationsClosed".toLowerCase())));
        viewData.setNoOfInvoicesSubmitted(getIntegerValue(data.get("n_InvoicesSubmitted".toLowerCase())));
        viewData.setValueOfInvoicesSubmitted(getDecimalValue(data.get("v_InvoicesSubmitted".toLowerCase())));
        viewData.setNoOfInvoicesAccepted(getIntegerValue(data.get("n_InvoicesAccepted".toLowerCase())));
        viewData.setValueOfInvoicesAccepted(getDecimalValue(data.get("v_InvoicesAccepted".toLowerCase())));
        viewData.setNoOfInvoicesRejected(getIntegerValue(data.get("n_InvoicesRejected".toLowerCase())));
        viewData.setValueOfInvoicesRejected(getDecimalValue(data.get("v_InvoicesRejected".toLowerCase())));
        viewData.setNoOfInvoicesPending(getIntegerValue(data.get("n_InvoicesPending".toLowerCase())));
        viewData.setValueOfInvoicesPending(getDecimalValue(data.get("v_InvoicesPending".toLowerCase())));
        viewData.setNoOfInvoicesAwaitingLiabilityResolution(getIntegerValue(data.get("n_InvoicesAwaitingLiabilityResolution".toLowerCase())));
        viewData.setValueOfInvoicesAwaitingLiabilityResolution(getDecimalValue(data.get("v_InvoicesAwaitingLiabilityResolution".toLowerCase())));
        viewData.setNoOfInvoicesClosed(getIntegerValue(data.get("n_InvoicesClosed".toLowerCase())));
        viewData.setValueOfInvoicesClosed(getDecimalValue(data.get("v_InvoicesClosed".toLowerCase())));
        viewData.setNoOfInvoicesPaymentLogged(getIntegerValue(data.get("n_InvoicesPaymentLogged".toLowerCase())));
        viewData.setValueOfInvoicesPaymentLogged(getDecimalValue(data.get("v_InvoicesPaymentLogged".toLowerCase())));
        viewData.setNoOfInvoicesPaymentReceived(getIntegerValue(data.get("n_InvoicesPaymentReceived".toLowerCase())));
        viewData.setValueOfInvoicesPaymentReceived(getDecimalValue(data.get("v_InvoicesPaymentReceived".toLowerCase())));
        viewData.setTotalValueOfPenaltyChargesApplied(getDecimalValue(data.get("v_PenaltyChargesApplied".toLowerCase())));
        viewData.setTotalValueOfPenaltyChargesPaid(getDecimalValue(data.get("v_PenaltyChargesPaid".toLowerCase())));
        viewData.setAvgInvoicePaymentTime(getDecimalValue(data.get("Avg_InvPaymentTime".toLowerCase())).setScale(2, RoundingMode.HALF_UP));
        viewData.setAvgManualInvoicePaymentTime(getDecimalValue(data.get("Avg_ManualInvPaymentTime".toLowerCase())).setScale(2, RoundingMode.HALF_UP));
        viewData.setNoOfInsurerClaimsSubmitted(getIntegerValue(data.get("n_insurer_ClaimsSubmitted".toLowerCase())));
        viewData.setNoOfManualInvoicesSubmitted(getIntegerValue(data.get("n_manual_InvoicesSubmitted".toLowerCase())));
        viewData.setValueOfManualInvoicesSubmitted(getDecimalValue(data.get("v_manual_InvoicesSubmitted".toLowerCase())));
        viewData.setNoOfManualInvoicesAccepted(getIntegerValue(data.get("n_manual_InvoicesAccepted".toLowerCase())));
        viewData.setValueOfManualInvoicesAccepted(getDecimalValue(data.get("v_manual_InvoicesAccepted".toLowerCase())));
        viewData.setNoOfManualInvoicesPaid(getIntegerValue(data.get("n_manual_InvoicesPaid".toLowerCase())));
        viewData.setValueOfManualInvoicesPaid(getDecimalValue(data.get("v_manual_InvoicesPaid".toLowerCase())));
        viewData.setNoOfManualInvoicesClosed(getIntegerValue(data.get("n_manual_InvoicesClosed".toLowerCase())));
        viewData.setValueOfManualInvoicesClosed(getDecimalValue(data.get("v_manual_InvoicesClosed".toLowerCase())));
        viewData.setNoOfManualInvoicesAwaitingLiabilityResolution(getIntegerValue(data.get("n_manual_InvoicesAwaitingLiabilityResolution".toLowerCase())));
        viewData.setValueOfManualInvoicesAwaitingLiabilityResolution(getDecimalValue(data.get("v_manual_InvoicesAwaitingLiabilityResolution".toLowerCase())));
        viewData.setNoOfInvoicesAwaitingLitigationOutcome(getIntegerValue(data.get("n_InvoicesAwaitingLitigationOutcome".toLowerCase())));
        viewData.setValueOfInvoicesAwaitingLitigationOutcome(getDecimalValue(data.get("v_InvoicesAwaitingLitigationOutcome".toLowerCase())));
        viewData.setValueOfManualInvoicesPenaltyChargesPaid(getDecimalValue(data.get("v_manual_InvoicesPenaltyChargesPaid".toLowerCase())));
        return viewData; 
    }

    private static Integer getIntegerValue(Object v) {

        if (v != null) {

            if (v.getClass().equals(Integer.class)) {
                return (Integer) v;
            } else if (v.getClass().equals(BigInteger.class)) {
                return ((BigInteger) v).intValue();
            } else {
                return 0;
            }

        } else {
            return 0;
        }
    }

    private static BigDecimal getDecimalValue(Object v) {

        if (v != null) {
            return (BigDecimal) v;
        } else {
            return new BigDecimal("0.00");
        }

    }

    public BigDecimal getAvgInvoicePaymentTime() {
        return avgInvoicePaymentTime;
    }

    private void setAvgInvoicePaymentTime(BigDecimal avgInvoicePaymentTime) {
        this.avgInvoicePaymentTime = avgInvoicePaymentTime;
    }

    public BigDecimal getAvgManualInvoicePaymentTime() {
        return avgManualInvoicePaymentTime;
    }

    private void setAvgManualInvoicePaymentTime(BigDecimal avgManualInvoicePaymentTime) {
        this.avgManualInvoicePaymentTime = avgManualInvoicePaymentTime;
    }

    /**
     * @return the noOfClaimNotificationsSubmitted
     */
    public Integer getNoOfClaimNotificationsSubmitted() {
        return noOfClaimNotificationsSubmitted;
    }

    /**
     * @param noOfClaimNotificationsSubmitted the noOfClaimNotificationsSubmitted to set
     */
    private void setNoOfClaimNotificationsSubmitted(Integer noOfClaimNotificationsSubmitted) {
        this.noOfClaimNotificationsSubmitted = noOfClaimNotificationsSubmitted;
    }

    /**
     * @return the noOfClaimNotificationsAccepted
     */
    public Integer getNoOfClaimNotificationsAccepted() {
        return noOfClaimNotificationsAccepted;
    }

    /**
     * @param noOfClaimNotificationsAccepted the noOfClaimNotificationsAccepted to set
     */
    private void setNoOfClaimNotificationsAccepted(Integer noOfClaimNotificationsAccepted) {
        this.noOfClaimNotificationsAccepted = noOfClaimNotificationsAccepted;
    }

    /**
     * @return the noOfClaimNotificationsRejectionsAccepted
     */
    public Integer getNoOfClaimNotificationsRejectionsAccepted() {
        return noOfClaimNotificationsRejectionsAccepted;
    }

    /**
     * @param noOfClaimNotificationsRejectionsAccepted the noOfClaimNotificationsRejectionsAccepted to set
     */
    private void setNoOfClaimNotificationsRejectionsAccepted(Integer noOfClaimNotificationsRejectionsAccepted) {
        this.noOfClaimNotificationsRejectionsAccepted = noOfClaimNotificationsRejectionsAccepted;
    }

    /**
     * @return the noOfClaimsAwaitingToBeProcessed
     */
    public Integer getNoOfClaimsAwaitingToBeProcessed() {
        return noOfClaimsAwaitingToBeProcessed;
    }

    /**
     * @param noOfClaimsAwaitingToBeProcessed the noOfClaimsAwaitingToBeProcessed to set
     */
    private void setNoOfClaimsAwaitingToBeProcessed(Integer noOfClaimsAwaitingToBeProcessed) {
        this.noOfClaimsAwaitingToBeProcessed = noOfClaimsAwaitingToBeProcessed;
    }

    /**
     * @return the noOfClaimNotificationsClosed
     */
    public Integer getNoOfClaimNotificationsClosed() {
        return noOfClaimNotificationsClosed;
    }

    /**
     * @param noOfClaimNotificationsClosed the noOfClaimNotificationsClosed to set
     */
    private void setNoOfClaimNotificationsClosed(Integer noOfClaimNotificationsClosed) {
        this.noOfClaimNotificationsClosed = noOfClaimNotificationsClosed;
    }

    /**
     * @return the noOfInvoicesSubmitted
     */
    public Integer getNoOfInvoicesSubmitted() {
        return noOfInvoicesSubmitted;
    }

    /**
     * @param noOfInvoicesSubmitted the noOfInvoicesSubmitted to set
     */
    private void setNoOfInvoicesSubmitted(Integer noOfInvoicesSubmitted) {
        this.noOfInvoicesSubmitted = noOfInvoicesSubmitted;
    }

    /**
     * @return the noOfInvoicesAccepted
     */
    public Integer getNoOfInvoicesAccepted() {
        return noOfInvoicesAccepted;
    }

    /**
     * @param noOfInvoicesAccepted the noOfInvoicesAccepted to set
     */
    private void setNoOfInvoicesAccepted(Integer noOfInvoicesAccepted) {
        this.noOfInvoicesAccepted = noOfInvoicesAccepted;
    }

    /**
     * @return the valueOfInvoicesAccepted
     */
    public BigDecimal getValueOfInvoicesAccepted() {
        return valueOfInvoicesAccepted;
    }

    /**
     * @param valueOfInvoicesAccepted the valueOfInvoicesAccepted to set
     */
    private void setValueOfInvoicesAccepted(BigDecimal valueOfInvoicesAccepted) {
        this.valueOfInvoicesAccepted = valueOfInvoicesAccepted;
    }

    /**
     * @return the noOfInvoicesRejected
     */
    public Integer getNoOfInvoicesRejected() {
        return noOfInvoicesRejected;
    }

    /**
     * @param noOfInvoicesRejected the noOfInvoicesRejected to set
     */
    private void setNoOfInvoicesRejected(Integer noOfInvoicesRejected) {
        this.noOfInvoicesRejected = noOfInvoicesRejected;
    }

    /**
     * @return the valueOfInvoicesRejected
     */
    public BigDecimal getValueOfInvoicesRejected() {
        return valueOfInvoicesRejected;
    }

    /**
     * @param valueOfInvoicesRejected the valueOfInvoicesRejected to set
     */
    private void setValueOfInvoicesRejected(BigDecimal valueOfInvoicesRejected) {
        this.valueOfInvoicesRejected = valueOfInvoicesRejected;
    }

    /**
     * @return the noOfInvoicesPending
     */
    public Integer getNoOfInvoicesPending() {
        return noOfInvoicesPending;
    }

    /**
     * @param noOfInvoicesPending the noOfInvoicesPending to set
     */
    private void setNoOfInvoicesPending(Integer noOfInvoicesPending) {
        this.noOfInvoicesPending = noOfInvoicesPending;
    }

    /**
     * @return the valueOfInvoicesPending
     */
    public BigDecimal getValueOfInvoicesPending() {
        return valueOfInvoicesPending;
    }

    /**
     * @param valueOfInvoicesPending the valueOfInvoicesPending to set
     */
    private void setValueOfInvoicesPending(BigDecimal valueOfInvoicesPending) {
        this.valueOfInvoicesPending = valueOfInvoicesPending;
    }

    /**
     * @return the noOfInvoicesAwaitingLiabilityResolution
     */
    public Integer getNoOfInvoicesAwaitingLiabilityResolution() {
        return noOfInvoicesAwaitingLiabilityResolution;
    }

    /**
     * @param noOfInvoicesAwaitingLiabilityResolution the noOfInvoicesAwaitingLiabilityResolution to set
     */
    private void setNoOfInvoicesAwaitingLiabilityResolution(Integer noOfInvoicesAwaitingLiabilityResolution) {
        this.noOfInvoicesAwaitingLiabilityResolution = noOfInvoicesAwaitingLiabilityResolution;
    }

    /**
     * @return the valueOfInvoicesAwaitingLiabilityResolution
     */
    public BigDecimal getValueOfInvoicesAwaitingLiabilityResolution() {
        return valueOfInvoicesAwaitingLiabilityResolution;
    }

    /**
     * @param valueOfInvoicesAwaitingLiabilityResolution the valueOfInvoicesAwaitingLiabilityResolution to set
     */
    private void setValueOfInvoicesAwaitingLiabilityResolution(BigDecimal valueOfInvoicesAwaitingLiabilityResolution) {
        this.valueOfInvoicesAwaitingLiabilityResolution = valueOfInvoicesAwaitingLiabilityResolution;
    }

    /**
     * @return the noOfInvoicesClosed
     */
    public Integer getNoOfInvoicesClosed() {
        return noOfInvoicesClosed;
    }

    /**
     * @param noOfInvoicesClosed the noOfInvoicesClosed to set
     */
    private void setNoOfInvoicesClosed(Integer noOfInvoicesClosed) {
        this.noOfInvoicesClosed = noOfInvoicesClosed;
    }

    /**
     * @return the valueOfInvoicesClosed
     */
    public BigDecimal getValueOfInvoicesClosed() {
        return valueOfInvoicesClosed;
    }

    /**
     * @param valueOfInvoicesClosed the valueOfInvoicesClosed to set
     */
    private void setValueOfInvoicesClosed(BigDecimal valueOfInvoicesClosed) {
        this.valueOfInvoicesClosed = valueOfInvoicesClosed;
    }

    /**
     * @return the noOfInvoicesPaymentLogged
     */
    public Integer getNoOfInvoicesPaymentLogged() {
        return noOfInvoicesPaymentLogged;
    }

    /**
     * @param noOfInvoicesPaymentLogged the noOfInvoicesPaymentLogged to set
     */
    private void setNoOfInvoicesPaymentLogged(Integer noOfInvoicesPaymentLogged) {
        this.noOfInvoicesPaymentLogged = noOfInvoicesPaymentLogged;
    }

    /**
     * @return the valueOfInvoicesPaymentLogged
     */
    public BigDecimal getValueOfInvoicesPaymentLogged() {
        return valueOfInvoicesPaymentLogged;
    }

    /**
     * @param valueOfInvoicesPaymentLogged the valueOfInvoicesPaymentLogged to set
     */
    private void setValueOfInvoicesPaymentLogged(BigDecimal valueOfInvoicesPaymentLogged) {
        this.valueOfInvoicesPaymentLogged = valueOfInvoicesPaymentLogged;
    }

    /**
     * @return the noOfInvoicesPaymentReceived
     */
    public Integer getNoOfInvoicesPaymentReceived() {
        return noOfInvoicesPaymentReceived;
    }

    /**
     * @param noOfInvoicesPaymentReceived the noOfInvoicesPaymentReceived to set
     */
    private void setNoOfInvoicesPaymentReceived(Integer noOfInvoicesPaymentReceived) {
        this.noOfInvoicesPaymentReceived = noOfInvoicesPaymentReceived;
    }

    /**
     * @return the valueOfInvoicesPaymentReceived
     */
    public BigDecimal getValueOfInvoicesPaymentReceived() {
        return valueOfInvoicesPaymentReceived;
    }

    /**
     * @param valueOfInvoicesPaymentReceived the valueOfInvoicesPaymentReceived to set
     */
    private void setValueOfInvoicesPaymentReceived(BigDecimal valueOfInvoicesPaymentReceived) {
        this.valueOfInvoicesPaymentReceived = valueOfInvoicesPaymentReceived;
    }

    /**
     * @return the totalValueOfPenaltyChargesApplied
     */
    public BigDecimal getTotalValueOfPenaltyChargesApplied() {
        return totalValueOfPenaltyChargesApplied;
    }

    /**
     * @param totalValueOfPenaltyChargesApplied the totalValueOfPenaltyChargesApplied to set
     */
    private void setTotalValueOfPenaltyChargesApplied(BigDecimal totalValueOfPenaltyChargesApplied) {
        this.totalValueOfPenaltyChargesApplied = totalValueOfPenaltyChargesApplied;
    }

    /**
     * @return the totalValueOfPenaltyChargesPaid
     */
    public BigDecimal getTotalValueOfPenaltyChargesPaid() {
        return totalValueOfPenaltyChargesPaid;
    }

    /**
     * @param totalValueOfPenaltyChargesPaid the totalValueOfPenaltyChargesPaid to set
     */
    private void setTotalValueOfPenaltyChargesPaid(BigDecimal totalValueOfPenaltyChargesPaid) {
        this.totalValueOfPenaltyChargesPaid = totalValueOfPenaltyChargesPaid;
    }


    /**
     * @return the valueOfInvoicesSubmitted
     */
    public BigDecimal getValueOfInvoicesSubmitted() {
        return valueOfInvoicesSubmitted;
    }

    /**
     * @param valueOfInvoicesSubmitted the valueOfInvoicesSubmitted to set
     */
    private void setValueOfInvoicesSubmitted(BigDecimal valueOfInvoicesSubmitted) {
        this.valueOfInvoicesSubmitted = valueOfInvoicesSubmitted;
    }

    public Integer getNoOfManualInvoicesPaid() {
        return noOfManualInvoicesPaid;
    }

    private void setNoOfManualInvoicesPaid(Integer noOfManualInvoicesPaid) {
        this.noOfManualInvoicesPaid = noOfManualInvoicesPaid;
    }

    public Integer getNoOfManualInvoicesAccepted() {
        return noOfManualInvoicesAccepted;
    }

    private void setNoOfManualInvoicesAccepted(Integer noOfManualInvoicesAccepted) {
        this.noOfManualInvoicesAccepted = noOfManualInvoicesAccepted;
    }

    public Integer getNoOfManualInvoicesSubmitted() {
        return noOfManualInvoicesSubmitted;
    }

    private void setNoOfManualInvoicesSubmitted(Integer noOfManualInvoicesSubmitted) {
        this.noOfManualInvoicesSubmitted = noOfManualInvoicesSubmitted;
    }

    public Integer getNoOfInsurerClaimsSubmitted() {
        return noOfInsurerClaimsSubmitted;
    }

    private void setNoOfInsurerClaimsSubmitted(Integer noOfInsurerClaimsSubmitted) {
        this.noOfInsurerClaimsSubmitted = noOfInsurerClaimsSubmitted;
    }

    public BigDecimal getValueOfManualInvoicesAccepted() {
        return valueOfManualInvoicesAccepted;
    }

    private void setValueOfManualInvoicesAccepted(BigDecimal valueOfManualInvoicesAccepted) {
        this.valueOfManualInvoicesAccepted = valueOfManualInvoicesAccepted;
    }

    public BigDecimal getValueOfManualInvoicesPaid() {
        return valueOfManualInvoicesPaid;
    }

    private void setValueOfManualInvoicesPaid(BigDecimal valueOfManualInvoicesPaid) {
        this.valueOfManualInvoicesPaid = valueOfManualInvoicesPaid;
    }

    public BigDecimal getValueOfManualInvoicesSubmitted() {
        return valueOfManualInvoicesSubmitted;
    }

    private void setValueOfManualInvoicesSubmitted(BigDecimal valueOfManualInvoicesSubmitted) {
        this.valueOfManualInvoicesSubmitted = valueOfManualInvoicesSubmitted;
    }

    public Integer getNoOfManualInvoicesClosed() {
        return noOfManualInvoicesClosed;
    }

    private void setNoOfManualInvoicesClosed(Integer noOfManualInvoicesClosed) {
        this.noOfManualInvoicesClosed = noOfManualInvoicesClosed;
    }

    public BigDecimal getValueOfManualInvoicesClosed() {
        return valueOfManualInvoicesClosed;
    }

    private void setValueOfManualInvoicesClosed(BigDecimal valueOfManualInvoicesClosed) {
        this.valueOfManualInvoicesClosed = valueOfManualInvoicesClosed;
    }

    public Integer getNoOfManualInvoicesAwaitingLiabilityResolution() {
        return noOfManualInvoicesAwaitingLiabilityResolution;
    }

    private void setNoOfManualInvoicesAwaitingLiabilityResolution(Integer noOfManualInvoicesAwaitingLiabilityResolution) {
        this.noOfManualInvoicesAwaitingLiabilityResolution = noOfManualInvoicesAwaitingLiabilityResolution;
    }

    public BigDecimal getValueOfManualInvoicesAwaitingLiabilityResolution() {
        return valueOfManualInvoicesAwaitingLiabilityResolution;
    }

    private void setValueOfManualInvoicesAwaitingLiabilityResolution(BigDecimal valueOfManualInvoicesAwaitingLiabilityResolution) {
        this.valueOfManualInvoicesAwaitingLiabilityResolution = valueOfManualInvoicesAwaitingLiabilityResolution;
    }

    public Integer getNoOfInvoicesAwaitingLitigationOutcome() {
        return noOfInvoicesAwaitingLitigationOutcome;
    }

    private void setNoOfInvoicesAwaitingLitigationOutcome(Integer noOfInvoicesAwaitingLitigationOutcome) {
        this.noOfInvoicesAwaitingLitigationOutcome = noOfInvoicesAwaitingLitigationOutcome;
    }

    public BigDecimal getValueOfInvoicesAwaitingLitigationOutcome() {
        return valueOfInvoicesAwaitingLitigationOutcome;
    }

    private void setValueOfInvoicesAwaitingLitigationOutcome(BigDecimal valueOfInvoicesAwaitingLitigationOutcome) {
        this.valueOfInvoicesAwaitingLitigationOutcome = valueOfInvoicesAwaitingLitigationOutcome;
    }

    private void setValueOfManualInvoicesPenaltyChargesPaid(BigDecimal valueOfManualInvoicesPenaltyChargesPaid) {
        this.valueOfManualInvoicesPenaltyChargesPaid = valueOfManualInvoicesPenaltyChargesPaid;
    }

    public BigDecimal getValueOfManualInvoicesPenaltyChargesPaid() {
        return valueOfManualInvoicesPenaltyChargesPaid;
    }
}
