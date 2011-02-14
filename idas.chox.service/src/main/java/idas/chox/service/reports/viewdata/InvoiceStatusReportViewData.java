/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
public class InvoiceStatusReportViewData {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceStatusReportViewData.class);
    private String headerNames;
    private Integer noOfInvoicesUploaded;
    private BigDecimal valOfInvoicesUploaded;
    private Integer noOfInvoicesPenaltyApplied;
    private BigDecimal valOfInvoicesPenaltyApplied;
    private Integer noOfInvoicesPaymentLogged;
    private BigDecimal valOfInvoicesPaymentLogged;
    private Integer noOfInvoicesPaymentReceived;
    private BigDecimal valOfInvoicesPaymentReceived;
    private Integer noOfInvoicesWithdrawn;
    private BigDecimal valOfInvoicesWithdrawn;
    private Integer noOfInvoicesAwaiting;
    private BigDecimal valOfInvoicesAwaiting;
    private Integer noOfInvoicesAwaitingLiability;
    private BigDecimal valOfInvoicesAwaitingLiability;
    private Integer noOfInvoicesCHOAwaiting;
    private BigDecimal valOfInvoicesCHOAwaiting;
    private Integer noOfInvoicesInsurerAwaiting;
    private BigDecimal valOfInvoicesInsurerAwaiting;
    private Integer noOfInvoicesApprovedByBusiness;
    private BigDecimal valOfInvoicesApprovedByBusiness;
    private Integer noOfInvoicesEscalatedToHandler;
    private BigDecimal valOfInvoicesEscalatedToHandler;
    private Integer noOfInvoicesEscalatedToEngineer;
    private BigDecimal valOfInvoicesEscalatedToEngineer;
    private Integer noOfInvoicesReferredToEngineer;
    private BigDecimal valOfInvoicesReferredToEngineer;
    private Integer noOfInvoicesReferedToHandler;
    private BigDecimal valOfInvoicesReferedToHandler;
    private Integer noOfInvoicesCHODispute;
    private BigDecimal valOfInvoicesCHODispute;
    private Integer noOfInvoicesApprovedAwaitingPay;
    private BigDecimal valOfInvoicesApprovedAwaitingPay;

    public static InvoiceStatusReportViewData getObject(Map data) {

        InvoiceStatusReportViewData result = new InvoiceStatusReportViewData();

        result.setHeaderNames((String) data.get("month_header".toLowerCase()));
        result.setNoOfInvoicesUploaded(((BigInteger) data.get("no_invoices_uploaded_current_month".toLowerCase())).intValue());
        result.setValOfInvoicesUploaded(((BigDecimal) data.get("val_invoices_uploaded_current_month".toLowerCase())));
        result.setNoOfInvoicesPenaltyApplied(((BigInteger) data.get("no_invoices_penalty_current_month".toLowerCase())).intValue());
        result.setValOfInvoicesPenaltyApplied(((BigDecimal) data.get("val_invoices_penalty_current_month".toLowerCase())));
        result.setNoOfInvoicesPaymentLogged(((BigInteger) data.get("no_invoices_payment_logged_current_month".toLowerCase())).intValue());
        result.setValOfInvoicesPaymentLogged(((BigDecimal) data.get("val_invoices_payment_logged_current_month".toLowerCase())));
        result.setNoOfInvoicesPaymentReceived(((BigInteger) data.get("no_invoices_payment_reconciled_current_month".toLowerCase())).intValue());
        result.setValOfInvoicesPaymentReceived(((BigDecimal) data.get("val_invoices_payment_reconciled_current_month".toLowerCase())));
        result.setNoOfInvoicesWithdrawn(((BigInteger) data.get("no_invoices_withdrawn_current_month".toLowerCase())).intValue());
        result.setValOfInvoicesWithdrawn(((BigDecimal) data.get("val_invoices_withdrawn_current_month".toLowerCase())));
        result.setNoOfInvoicesAwaiting(((BigInteger) data.get("no_invoices_awaiting_current_month".toLowerCase())).intValue());
        result.setValOfInvoicesAwaiting(((BigDecimal) data.get("val_invoices_awaiting_current_month".toLowerCase())));
        result.setNoOfInvoicesAwaitingLiability(((BigInteger) data.get("no_invoices_awaitingliability_current_month".toLowerCase())).intValue());
        result.setValOfInvoicesAwaitingLiability(((BigDecimal) data.get("val_invoices_awaitingliability_current_month".toLowerCase())));
        result.setNoOfInvoicesCHOAwaiting(((BigInteger) data.get("no_invoices_cho_awaiting_current_month".toLowerCase())).intValue());
        result.setValOfInvoicesCHOAwaiting(((BigDecimal) data.get("val_invoices_cho_awaiting_current_month".toLowerCase())));
        result.setNoOfInvoicesInsurerAwaiting(((BigInteger) data.get("no_invoices_insurer_awaiting_current_month".toLowerCase())).intValue());
        result.setValOfInvoicesInsurerAwaiting(((BigDecimal) data.get("val_invoices_insurer_awaiting_current_month".toLowerCase())));
        result.setNoOfInvoicesApprovedByBusiness(((BigInteger) data.get("no_invoices_approved_by_businessrules_current_month".toLowerCase())).intValue());
        result.setValOfInvoicesApprovedByBusiness(((BigDecimal) data.get("val_invoices_approved_by_businessrules_current_month".toLowerCase())));
        result.setNoOfInvoicesEscalatedToHandler(((BigInteger) data.get("no_invoices_escalated_to_handler_current_month".toLowerCase())).intValue());
        result.setValOfInvoicesEscalatedToHandler(((BigDecimal) data.get("val_invoices_escalated_to_handler_current_month".toLowerCase())));
        result.setNoOfInvoicesEscalatedToEngineer(((BigInteger) data.get("no_invoices_escalated_current_month".toLowerCase())).intValue());
        result.setValOfInvoicesEscalatedToEngineer(((BigDecimal) data.get("val_invoices_escalated_current_month".toLowerCase())));
        result.setNoOfInvoicesReferredToEngineer(((BigInteger) data.get("no_invoices_referred_to_engineer_current_month".toLowerCase())).intValue());
        result.setValOfInvoicesReferredToEngineer(((BigDecimal) data.get("val_invoices_referred_to_engineer_current_month".toLowerCase())));
        result.setNoOfInvoicesReferedToHandler(((BigInteger) data.get("no_invoices_referred_to_handler_current_month".toLowerCase())).intValue());
        result.setValOfInvoicesReferedToHandler(((BigDecimal) data.get("val_invoices_referred_to_handler_current_month".toLowerCase())));
        result.setNoOfInvoicesCHODispute(((BigInteger) data.get("no_invoices_cho_dispute_current_month".toLowerCase())).intValue());
        result.setValOfInvoicesCHODispute(((BigDecimal) data.get("val_invoices_cho_dispute_current_month".toLowerCase())));
        result.setNoOfInvoicesApprovedAwaitingPay(((BigInteger) data.get("no_invoices_awaiting_payment_current_month".toLowerCase())).intValue());
        result.setValOfInvoicesApprovedAwaitingPay(((BigDecimal) data.get("val_invoices_awaiting_payment_current_month".toLowerCase())));


        return result;

    }

    /**
     * @return the noOfInvoicesUploaded
     */
    public Integer getNoOfInvoicesUploaded() {
        return noOfInvoicesUploaded;
    }

    /**
     * @param noOfInvoicesUploaded the noOfInvoicesUploaded to set
     */
    public void setNoOfInvoicesUploaded(Integer noOfInvoicesUploaded) {
        this.noOfInvoicesUploaded = noOfInvoicesUploaded;
    }

    /**
     * @return the valOfInvoicesUploaded
     */
    public BigDecimal getValOfInvoicesUploaded() {
        return valOfInvoicesUploaded;
    }

    /**
     * @param valOfInvoicesUploaded the valOfInvoicesUploaded to set
     */
    public void setValOfInvoicesUploaded(BigDecimal valOfInvoicesUploaded) {
        this.valOfInvoicesUploaded = valOfInvoicesUploaded;
    }

    /**
     * @return the noOfInvoicesPenaltyApplied
     */
    public Integer getNoOfInvoicesPenaltyApplied() {
        return noOfInvoicesPenaltyApplied;
    }

    /**
     * @param noOfInvoicesPenaltyApplied the noOfInvoicesPenaltyApplied to set
     */
    public void setNoOfInvoicesPenaltyApplied(Integer noOfInvoicesPenaltyApplied) {
        this.noOfInvoicesPenaltyApplied = noOfInvoicesPenaltyApplied;
    }

    /**
     * @return the valOfInvoicesPenaltyApplied
     */
    public BigDecimal getValOfInvoicesPenaltyApplied() {
        return valOfInvoicesPenaltyApplied;
    }

    /**
     * @param valOfInvoicesPenaltyApplied the valOfInvoicesPenaltyApplied to set
     */
    public void setValOfInvoicesPenaltyApplied(BigDecimal valOfInvoicesPenaltyApplied) {
        this.valOfInvoicesPenaltyApplied = valOfInvoicesPenaltyApplied;
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
    public void setNoOfInvoicesPaymentLogged(Integer noOfInvoicesPaymentLogged) {
        this.noOfInvoicesPaymentLogged = noOfInvoicesPaymentLogged;
    }

    /**
     * @return the valOfInvoicesPaymentLogged
     */
    public BigDecimal getValOfInvoicesPaymentLogged() {
        return valOfInvoicesPaymentLogged;
    }

    /**
     * @param valOfInvoicesPaymentLogged the valOfInvoicesPaymentLogged to set
     */
    public void setValOfInvoicesPaymentLogged(BigDecimal valOfInvoicesPaymentLogged) {
        this.valOfInvoicesPaymentLogged = valOfInvoicesPaymentLogged;
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
    public void setNoOfInvoicesPaymentReceived(Integer noOfInvoicesPaymentReceived) {
        this.noOfInvoicesPaymentReceived = noOfInvoicesPaymentReceived;
    }

    /**
     * @return the valOfInvoicesPaymentReceived
     */
    public BigDecimal getValOfInvoicesPaymentReceived() {
        return valOfInvoicesPaymentReceived;
    }

    /**
     * @param valOfInvoicesPaymentReceived the valOfInvoicesPaymentReceived to set
     */
    public void setValOfInvoicesPaymentReceived(BigDecimal valOfInvoicesPaymentReceived) {
        this.valOfInvoicesPaymentReceived = valOfInvoicesPaymentReceived;
    }

    /**
     * @return the noOfInvoicesWithdrawn
     */
    public Integer getNoOfInvoicesWithdrawn() {
        return noOfInvoicesWithdrawn;
    }

    /**
     * @param noOfInvoicesWithdrawn the noOfInvoicesWithdrawn to set
     */
    public void setNoOfInvoicesWithdrawn(Integer noOfInvoicesWithdrawn) {
        this.noOfInvoicesWithdrawn = noOfInvoicesWithdrawn;
    }

    /**
     * @return the valOfInvoicesWithdrawn
     */
    public BigDecimal getValOfInvoicesWithdrawn() {
        return valOfInvoicesWithdrawn;
    }

    /**
     * @param valOfInvoicesWithdrawn the valOfInvoicesWithdrawn to set
     */
    public void setValOfInvoicesWithdrawn(BigDecimal valOfInvoicesWithdrawn) {
        this.valOfInvoicesWithdrawn = valOfInvoicesWithdrawn;
    }

    /**
     * @return the noOfInvoicesAwaiting
     */
    public Integer getNoOfInvoicesAwaiting() {
        return noOfInvoicesAwaiting;
    }

    /**
     * @param noOfInvoicesAwaiting the noOfInvoicesAwaiting to set
     */
    public void setNoOfInvoicesAwaiting(Integer noOfInvoicesAwaiting) {
        this.noOfInvoicesAwaiting = noOfInvoicesAwaiting;
    }

    /**
     * @return the valOfInvoicesAwaiting
     */
    public BigDecimal getValOfInvoicesAwaiting() {
        return valOfInvoicesAwaiting;
    }

    /**
     * @param valOfInvoicesAwaiting the valOfInvoicesAwaiting to set
     */
    public void setValOfInvoicesAwaiting(BigDecimal valOfInvoicesAwaiting) {
        this.valOfInvoicesAwaiting = valOfInvoicesAwaiting;
    }

    /**
     * @return the noOfInvoicesAwaitingLiability
     */
    public Integer getNoOfInvoicesAwaitingLiability() {
        return noOfInvoicesAwaitingLiability;
    }

    /**
     * @param noOfInvoicesAwaitingLiability the noOfInvoicesAwaitingLiability to set
     */
    public void setNoOfInvoicesAwaitingLiability(Integer noOfInvoicesAwaitingLiability) {
        this.noOfInvoicesAwaitingLiability = noOfInvoicesAwaitingLiability;
    }

    /**
     * @return the valOfInvoicesAwaitingLiability
     */
    public BigDecimal getValOfInvoicesAwaitingLiability() {
        return valOfInvoicesAwaitingLiability;
    }

    /**
     * @param valOfInvoicesAwaitingLiability the valOfInvoicesAwaitingLiability to set
     */
    public void setValOfInvoicesAwaitingLiability(BigDecimal valOfInvoicesAwaitingLiability) {
        this.valOfInvoicesAwaitingLiability = valOfInvoicesAwaitingLiability;
    }

    /**
     * @return the noOfInvoicesCHOAwaiting
     */
    public Integer getNoOfInvoicesCHOAwaiting() {
        return noOfInvoicesCHOAwaiting;
    }

    /**
     * @param noOfInvoicesCHOAwaiting the noOfInvoicesCHOAwaiting to set
     */
    public void setNoOfInvoicesCHOAwaiting(Integer noOfInvoicesCHOAwaiting) {
        this.noOfInvoicesCHOAwaiting = noOfInvoicesCHOAwaiting;
    }

    /**
     * @return the valOfInvoicesCHOAwaiting
     */
    public BigDecimal getValOfInvoicesCHOAwaiting() {
        return valOfInvoicesCHOAwaiting;
    }

    /**
     * @param valOfInvoicesCHOAwaiting the valOfInvoicesCHOAwaiting to set
     */
    public void setValOfInvoicesCHOAwaiting(BigDecimal valOfInvoicesCHOAwaiting) {
        this.valOfInvoicesCHOAwaiting = valOfInvoicesCHOAwaiting;
    }

    /**
     * @return the noOfInvoicesInsurerAwaiting
     */
    public Integer getNoOfInvoicesInsurerAwaiting() {
        return noOfInvoicesInsurerAwaiting;
    }

    /**
     * @param noOfInvoicesInsurerAwaiting the noOfInvoicesInsurerAwaiting to set
     */
    public void setNoOfInvoicesInsurerAwaiting(Integer noOfInvoicesInsurerAwaiting) {
        this.noOfInvoicesInsurerAwaiting = noOfInvoicesInsurerAwaiting;
    }

    /**
     * @return the valOfInvoicesInsurerAwaiting
     */
    public BigDecimal getValOfInvoicesInsurerAwaiting() {
        return valOfInvoicesInsurerAwaiting;
    }

    /**
     * @param valOfInvoicesInsurerAwaiting the valOfInvoicesInsurerAwaiting to set
     */
    public void setValOfInvoicesInsurerAwaiting(BigDecimal valOfInvoicesInsurerAwaiting) {
        this.valOfInvoicesInsurerAwaiting = valOfInvoicesInsurerAwaiting;
    }

    /**
     * @return the noOfInvoicesApprovedByBusiness
     */
    public Integer getNoOfInvoicesApprovedByBusiness() {
        return noOfInvoicesApprovedByBusiness;
    }

    /**
     * @param noOfInvoicesApprovedByBusiness the noOfInvoicesApprovedByBusiness to set
     */
    public void setNoOfInvoicesApprovedByBusiness(Integer noOfInvoicesApprovedByBusiness) {
        this.noOfInvoicesApprovedByBusiness = noOfInvoicesApprovedByBusiness;
    }

    /**
     * @return the valOfInvoicesApprovedByBusiness
     */
    public BigDecimal getValOfInvoicesApprovedByBusiness() {
        return valOfInvoicesApprovedByBusiness;
    }

    /**
     * @param valOfInvoicesApprovedByBusiness the valOfInvoicesApprovedByBusiness to set
     */
    public void setValOfInvoicesApprovedByBusiness(BigDecimal valOfInvoicesApprovedByBusiness) {
        this.valOfInvoicesApprovedByBusiness = valOfInvoicesApprovedByBusiness;
    }

    /**
     * @return the noOfInvoicesEscalatedToHandler
     */
    public Integer getNoOfInvoicesEscalatedToHandler() {
        return noOfInvoicesEscalatedToHandler;
    }

    /**
     * @param noOfInvoicesEscalatedToHandler the noOfInvoicesEscalatedToHandler to set
     */
    public void setNoOfInvoicesEscalatedToHandler(Integer noOfInvoicesEscalatedToHandler) {
        this.noOfInvoicesEscalatedToHandler = noOfInvoicesEscalatedToHandler;
    }

    /**
     * @return the valOfInvoicesEscalatedToHandler
     */
    public BigDecimal getValOfInvoicesEscalatedToHandler() {
        return valOfInvoicesEscalatedToHandler;
    }

    /**
     * @param valOfInvoicesEscalatedToHandler the valOfInvoicesEscalatedToHandler to set
     */
    public void setValOfInvoicesEscalatedToHandler(BigDecimal valOfInvoicesEscalatedToHandler) {
        this.valOfInvoicesEscalatedToHandler = valOfInvoicesEscalatedToHandler;
    }

    /**
     * @return the noOfInvoicesEscalatedToEngineer
     */
    public Integer getNoOfInvoicesEscalatedToEngineer() {
        return noOfInvoicesEscalatedToEngineer;
    }

    /**
     * @param noOfInvoicesEscalatedToEngineer the noOfInvoicesEscalatedToEngineer to set
     */
    public void setNoOfInvoicesEscalatedToEngineer(Integer noOfInvoicesEscalatedToEngineer) {
        this.noOfInvoicesEscalatedToEngineer = noOfInvoicesEscalatedToEngineer;
    }

    /**
     * @return the valOfInvoicesEscalatedToEngineer
     */
    public BigDecimal getValOfInvoicesEscalatedToEngineer() {
        return valOfInvoicesEscalatedToEngineer;
    }

    /**
     * @param valOfInvoicesEscalatedToEngineer the valOfInvoicesEscalatedToEngineer to set
     */
    public void setValOfInvoicesEscalatedToEngineer(BigDecimal valOfInvoicesEscalatedToEngineer) {
        this.valOfInvoicesEscalatedToEngineer = valOfInvoicesEscalatedToEngineer;
    }

    /**
     * @return the noOfInvoicesReferredToEngineer
     */
    public Integer getNoOfInvoicesReferredToEngineer() {
        return noOfInvoicesReferredToEngineer;
    }

    /**
     * @param noOfInvoicesReferredToEngineer the noOfInvoicesReferredToEngineer to set
     */
    public void setNoOfInvoicesReferredToEngineer(Integer noOfInvoicesReferredToEngineer) {
        this.noOfInvoicesReferredToEngineer = noOfInvoicesReferredToEngineer;
    }

    /**
     * @return the valOfInvoicesReferredToEngineer
     */
    public BigDecimal getValOfInvoicesReferredToEngineer() {
        return valOfInvoicesReferredToEngineer;
    }

    /**
     * @param valOfInvoicesReferredToEngineer the valOfInvoicesReferredToEngineer to set
     */
    public void setValOfInvoicesReferredToEngineer(BigDecimal valOfInvoicesReferredToEngineer) {
        this.valOfInvoicesReferredToEngineer = valOfInvoicesReferredToEngineer;
    }

    /**
     * @return the noOfInvoicesReferedToHandler
     */
    public Integer getNoOfInvoicesReferedToHandler() {
        return noOfInvoicesReferedToHandler;
    }

    /**
     * @param noOfInvoicesReferedToHandler the noOfInvoicesReferedToHandler to set
     */
    public void setNoOfInvoicesReferedToHandler(Integer noOfInvoicesReferedToHandler) {
        this.noOfInvoicesReferedToHandler = noOfInvoicesReferedToHandler;
    }

    /**
     * @return the valOfInvoicesReferedToHandler
     */
    public BigDecimal getValOfInvoicesReferedToHandler() {
        return valOfInvoicesReferedToHandler;
    }

    /**
     * @param valOfInvoicesReferedToHandler the valOfInvoicesReferedToHandler to set
     */
    public void setValOfInvoicesReferedToHandler(BigDecimal valOfInvoicesReferedToHandler) {
        this.valOfInvoicesReferedToHandler = valOfInvoicesReferedToHandler;
    }

    /**
     * @return the noOfInvoicesCHODispute
     */
    public Integer getNoOfInvoicesCHODispute() {
        return noOfInvoicesCHODispute;
    }

    /**
     * @param noOfInvoicesCHODispute the noOfInvoicesCHODispute to set
     */
    public void setNoOfInvoicesCHODispute(Integer noOfInvoicesCHODispute) {
        this.noOfInvoicesCHODispute = noOfInvoicesCHODispute;
    }

    /**
     * @return the valOfInvoicesCHODispute
     */
    public BigDecimal getValOfInvoicesCHODispute() {
        return valOfInvoicesCHODispute;
    }

    /**
     * @param valOfInvoicesCHODispute the valOfInvoicesCHODispute to set
     */
    public void setValOfInvoicesCHODispute(BigDecimal valOfInvoicesCHODispute) {
        this.valOfInvoicesCHODispute = valOfInvoicesCHODispute;
    }

    /**
     * @return the noOfInvoicesApprovedAwaitingPay
     */
    public Integer getNoOfInvoicesApprovedAwaitingPay() {
        return noOfInvoicesApprovedAwaitingPay;
    }

    /**
     * @param noOfInvoicesApprovedAwaitingPay the noOfInvoicesApprovedAwaitingPay to set
     */
    public void setNoOfInvoicesApprovedAwaitingPay(Integer noOfInvoicesApprovedAwaitingPay) {
        this.noOfInvoicesApprovedAwaitingPay = noOfInvoicesApprovedAwaitingPay;
    }

    /**
     * @return the valOfInvoicesApprovedAwaitingPay
     */
    public BigDecimal getValOfInvoicesApprovedAwaitingPay() {
        return valOfInvoicesApprovedAwaitingPay;
    }

    /**
     * @param valOfInvoicesApprovedAwaitingPay the valOfInvoicesApprovedAwaitingPay to set
     */
    public void setValOfInvoicesApprovedAwaitingPay(BigDecimal valOfInvoicesApprovedAwaitingPay) {
        this.valOfInvoicesApprovedAwaitingPay = valOfInvoicesApprovedAwaitingPay;
    }

    /**
     * @return the headerNames
     */
    public String getHeaderNames() {
        return headerNames;
    }

    /**
     * @param headerNames the headerNames to set
     */
    public void setHeaderNames(String headerNames) {
        this.headerNames = headerNames;
    }
}
