/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.dashboard;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

public class DashBoardViewData {
    private Integer noOfClaimNotificationsSubmitted;
    private Integer noOfClaimNotificationsAccepted;
    private Integer noOfClaimNotificationsAcceptedAccumulative;
    private Integer noOfClaimNotificationsRejected;
    private Integer noOfClaimNotificationsRejectedAccumulative;
    private Integer noOfClaimNotificationsPending;
    private Integer noOfClaimNotificationsClosed;
    private Integer noOfInvoicesSubmitted;
    private BigDecimal valueOfInvoicesSubmitted;
    private Integer noOfInvoicesAccepted;
    private BigDecimal valueOfInvoicesAccepted;
    private Integer noOfInvoicesRejected;
    private BigDecimal valueOfInvoicesRejected;
    private Integer noOfInvoicesPending;
    private BigDecimal valueOfInvoicesPending;
    private Integer noOfInvoicesClosed;
    private BigDecimal valueOfInvoicesClosed;
    private Integer noOfInvoicesPaymentLogged;
    private BigDecimal valueOfInvoicesPaymentLogged;    
    private Integer noOfInvoicesPaymentReceived;
    private BigDecimal valueOfInvoicesPaymentReceived;        
    private BigDecimal totalValueOfPenaltyChargesApplied;
    
    
    public static DashBoardViewData getObject(Map data)
    {
        DashBoardViewData viewData = new DashBoardViewData();
        viewData.noOfClaimNotificationsSubmitted = getIntegerValue(data.get("n_ClaimNotificationsSubmitted".toLowerCase()));
        viewData.noOfClaimNotificationsAccepted = getIntegerValue(data.get("n_ClaimNotificationsAccepted".toLowerCase()));
        viewData.noOfClaimNotificationsRejectedAccumulative = getIntegerValue(data.get("n_ClaimNotificationsRejectedAcc".toLowerCase()));
        viewData.noOfClaimNotificationsAcceptedAccumulative = getIntegerValue(data.get("n_ClaimNotificationsAcceptedAcc".toLowerCase()));
        viewData.noOfClaimNotificationsRejected = getIntegerValue(data.get("n_ClaimNotificationsRejected".toLowerCase()));
        viewData.noOfClaimNotificationsPending = getIntegerValue(data.get("n_ClaimNotificationsPending".toLowerCase()));
        viewData.noOfClaimNotificationsClosed = getIntegerValue(data.get("n_ClaimNotificationsClosed".toLowerCase()));
        viewData.noOfInvoicesSubmitted = getIntegerValue(data.get("n_InvoicesSubmitted".toLowerCase()));
        viewData.valueOfInvoicesSubmitted = getDecimalValue(data.get("v_InvoicesSubmitted".toLowerCase()));
        viewData.noOfInvoicesAccepted = getIntegerValue(data.get("n_InvoicesAccepted".toLowerCase()));
        viewData.valueOfInvoicesAccepted = getDecimalValue(data.get("v_InvoicesAccepted".toLowerCase()));
        viewData.noOfInvoicesRejected = getIntegerValue(data.get("n_InvoicesRejected".toLowerCase()));
        viewData.valueOfInvoicesRejected = getDecimalValue(data.get("v_InvoicesRejected".toLowerCase()));
        viewData.noOfInvoicesPending = getIntegerValue(data.get("n_InvoicesPending".toLowerCase()));
        viewData.valueOfInvoicesPending = getDecimalValue(data.get("v_InvoicesPending".toLowerCase()));
        viewData.noOfInvoicesClosed = getIntegerValue(data.get("n_InvoicesClosed".toLowerCase()));
        viewData.valueOfInvoicesClosed = getDecimalValue(data.get("v_InvoicesClosed".toLowerCase()));
        viewData.noOfInvoicesPaymentLogged = getIntegerValue(data.get("n_InvoicesPaymentLogged".toLowerCase()));
        viewData.valueOfInvoicesPaymentLogged = getDecimalValue(data.get("v_InvoicesPaymentLogged".toLowerCase()));        
        viewData.noOfInvoicesPaymentReceived = getIntegerValue(data.get("n_InvoicesPaymentReceived".toLowerCase()));
        viewData.valueOfInvoicesPaymentReceived = getDecimalValue(data.get("v_InvoicesPaymentReceived".toLowerCase()));           
        viewData.totalValueOfPenaltyChargesApplied = getDecimalValue(data.get("v_PenaltyChargesApplied".toLowerCase()));
        return viewData;
    }
    
    private static Integer getIntegerValue(Object v) {
        
        if(v!=null){
        
            if (v.getClass().equals(Integer.class)) {
                return (Integer) v;
            } else if (v.getClass().equals(BigInteger.class)) {
                return ((BigInteger) v).intValue();
            } else {
                return 0;
            }
            
        }else{
            return 0;
        }
    }
    
    private static BigDecimal getDecimalValue(Object v) {
        
        if(v!=null){
            return (BigDecimal) v;        
        }else{
            return new BigDecimal("0.00");
        }
        
    }

    public Integer getNoOfClaimNotificationsSubmitted() {        
        return noOfClaimNotificationsSubmitted;
    }

    public void setNoOfClaimNotificationsSubmitted(Integer noOfClaimNotificationsSubmitted) {
        this.noOfClaimNotificationsSubmitted = noOfClaimNotificationsSubmitted;
    }

    public Integer getNoOfClaimNotificationsAccepted() {
        return noOfClaimNotificationsAccepted;
    }

    public void setNoOfClaimNotificationsAccepted(Integer noOfClaimNotificationsAccepted) {
        this.noOfClaimNotificationsAccepted = noOfClaimNotificationsAccepted;
    }

    public Integer getNoOfClaimNotificationsRejected() {
        return noOfClaimNotificationsRejected;
    }

    public void setNoOfClaimNotificationsRejected(Integer noOfClaimNotificationsRejected) {
        this.noOfClaimNotificationsRejected = noOfClaimNotificationsRejected;
    }

    public Integer getNoOfClaimNotificationsPending() {
        return noOfClaimNotificationsPending;
    }

    public void setNoOfClaimNotificationsPending(Integer noOfClaimNotificationsPending) {
        this.noOfClaimNotificationsPending = noOfClaimNotificationsPending;
    }

    public Integer getNoOfInvoicesSubmitted() {
        return noOfInvoicesSubmitted;
    }

    public void setNoOfInvoicesSubmitted(Integer noOfInvoicesSubmitted) {
        this.noOfInvoicesSubmitted = noOfInvoicesSubmitted;
    }

    public BigDecimal getValueOfInvoicesSubmitted() {
        return valueOfInvoicesSubmitted;
    }

    public void setValueOfInvoicesSubmitted(BigDecimal valueOfInvoicesSubmitted) {
        this.valueOfInvoicesSubmitted = valueOfInvoicesSubmitted;
    }

    public Integer getNoOfInvoicesAccepted() {
        return noOfInvoicesAccepted;
    }

    public void setNoOfInvoicesAccepted(Integer noOfInvoicesAccepted) {
        this.noOfInvoicesAccepted = noOfInvoicesAccepted;
    }

    public BigDecimal getValueOfInvoicesAccepted() {
        return valueOfInvoicesAccepted;
    }

    public void setValueOfInvoicesAccepted(BigDecimal valueOfInvoicesAccepted) {
        this.valueOfInvoicesAccepted = valueOfInvoicesAccepted;
    }

    public Integer getNoOfInvoicesRejected() {
        return noOfInvoicesRejected;
    }

    public void setNoOfInvoicesRejected(Integer noOfInvoicesRejected) {
        this.noOfInvoicesRejected = noOfInvoicesRejected;
    }

    public BigDecimal getValueOfInvoicesRejected() {
        return valueOfInvoicesRejected;
    }

    public void setValueOfInvoicesRejected(BigDecimal valueOfInvoicesRejected) {
        this.valueOfInvoicesRejected = valueOfInvoicesRejected;
    }

    public Integer getNoOfInvoicesPending() {
        return noOfInvoicesPending;
    }

    public void setNoOfInvoicesPending(Integer noOfInvoicesPending) {
        this.noOfInvoicesPending = noOfInvoicesPending;
    }

    public BigDecimal getValueOfInvoicesPending() {
        return valueOfInvoicesPending;
    }

    public void setValueOfInvoicesPending(BigDecimal valueOfInvoicesPending) {
        this.valueOfInvoicesPending = valueOfInvoicesPending;
    }

    public Integer getNoOfInvoicesClosed() {
        return noOfInvoicesClosed;
    }

    public void setNoOfInvoicesClosed(Integer noOfInvoicesClosed) {
        this.noOfInvoicesClosed = noOfInvoicesClosed;
    }

    public BigDecimal getValueOfInvoicesClosed() {
        return valueOfInvoicesClosed;
    }

    public void setValueOfInvoicesClosed(BigDecimal valueOfInvoicesClosed) {
        this.valueOfInvoicesClosed = valueOfInvoicesClosed;
    }

    public BigDecimal getTotalValueOfPenaltyChargesApplied() {
        return totalValueOfPenaltyChargesApplied;
    }

    public void setTotalValueOfPenaltyChargesApplied(BigDecimal totalValueOfPenaltyChargesApplied) {
        this.totalValueOfPenaltyChargesApplied = totalValueOfPenaltyChargesApplied;
    }

    public Integer getNoOfInvoicesPaymentLogged() {
        return noOfInvoicesPaymentLogged;
    }

    public void setNoOfInvoicesPaymentLogged(Integer noOfInvoicesPaymentLogged) {
        this.noOfInvoicesPaymentLogged = noOfInvoicesPaymentLogged;
    }

    public BigDecimal getValueOfInvoicesPaymentLogged() {
        return valueOfInvoicesPaymentLogged;
    }

    public void setValueOfInvoicesPaymentLogged(BigDecimal valueOfInvoicesPaymentLogged) {
        this.valueOfInvoicesPaymentLogged = valueOfInvoicesPaymentLogged;
    }

    public Integer getNoOfClaimNotificationsClosed() {
        return noOfClaimNotificationsClosed;
    }

    public void setNoOfClaimNotificationsClosed(Integer noOfClaimNotificationsClosed) {
        this.noOfClaimNotificationsClosed = noOfClaimNotificationsClosed;
    }

    public Integer getNoOfClaimNotificationsAcceptedAccumulative() {
        return noOfClaimNotificationsAcceptedAccumulative;
    }

    public void setNoOfClaimNotificationsAcceptedAccumulative(Integer noOfClaimNotificationsAcceptedAccumulative) {
        this.noOfClaimNotificationsAcceptedAccumulative = noOfClaimNotificationsAcceptedAccumulative;
    }

    public Integer getNoOfClaimNotificationsRejectedAccumulative() {
        return noOfClaimNotificationsRejectedAccumulative;
    }

    public void setNoOfClaimNotificationsRejectedAccumulative(Integer noOfClaimNotificationsRejectedAccumulative) {
        this.noOfClaimNotificationsRejectedAccumulative = noOfClaimNotificationsRejectedAccumulative;
    }

    public Integer getNoOfInvoicesPaymentReceived() {
        return noOfInvoicesPaymentReceived;
    }

    public void setNoOfInvoicesPaymentReceived(Integer noOfInvoicesPaymentReceived) {
        this.noOfInvoicesPaymentReceived = noOfInvoicesPaymentReceived;
    }

    public BigDecimal getValueOfInvoicesPaymentReceived() {
        return valueOfInvoicesPaymentReceived;
    }

    public void setValueOfInvoicesPaymentReceived(BigDecimal valueOfInvoicesPaymentReceived) {
        this.valueOfInvoicesPaymentReceived = valueOfInvoicesPaymentReceived;
    }
    
    
}
