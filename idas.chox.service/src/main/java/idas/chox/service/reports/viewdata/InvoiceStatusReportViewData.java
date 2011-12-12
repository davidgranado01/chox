package idas.chox.service.reports.viewdata;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvoiceStatusReportViewData {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceStatusReportViewData.class);
    private String headerNames;
    private Integer noOfInvoicesUploaded;
    private BigDecimal valOfInvoicesUploaded;
    private BigDecimal valOfInterimPaymentUploaded;
    private Integer noOfInvoicesPenaltyApplied;
    private BigDecimal valOfInvoicesPenaltyApplied;
    private Integer noOfInvoicesPaymentLogged;
    private BigDecimal valOfInvoicesPaymentLogged;
    private BigDecimal valOfInterimPaymentPaymentLogged;
    private Integer noOfInvoicesPaymentReceived;
    private BigDecimal valOfInvoicesPaymentReceived;
    private BigDecimal valOfInterimPaymentPaymentReceived;
    private Integer noOfInvoicesWithdrawn;
    private BigDecimal valOfInvoicesWithdrawn;
    private BigDecimal valOfInterimPaymentWithdrawn;
    private Integer noOfInvoicesAwaiting;
    private BigDecimal valOfInvoicesAwaiting;
    private BigDecimal valOfInterimPaymentAwaiting;
    private Integer noOfInvoicesAwaitingLiability;
    private BigDecimal valOfInvoicesAwaitingLiability;
    private BigDecimal valOfInterimPaymentAwaitingLiability;
    private Integer noOfInvoicesCHOAwaiting;
    private BigDecimal valOfInvoicesCHOAwaiting;
    private BigDecimal valOfInterimPaymentCHOAwaiting;
    private Integer noOfInvoicesInsurerAwaiting;
    private BigDecimal valOfInvoicesInsurerAwaiting;
    private BigDecimal valOfInterimPaymentInsurerAwaiting;
    private Integer noOfInvoicesApprovedByBusiness;
    private BigDecimal valOfInvoicesApprovedByBusiness;
    private BigDecimal valOfInterimPaymentApprovedByBusiness;
    private Integer noOfInvoicesEscalatedToHandler;
    private BigDecimal valOfInvoicesEscalatedToHandler;
    private BigDecimal valOfInterimPaymentEscalatedToHandler;
    private Integer noOfInvoicesEscalatedToEngineer;
    private BigDecimal valOfInvoicesEscalatedToEngineer;
    private BigDecimal valOfInterimPaymentEscalatedToEngineer;
    private Integer noOfInvoicesReferredToEngineer;
    private BigDecimal valOfInvoicesReferredToEngineer;
    private BigDecimal valOfInterimPaymentReferredToEngineer;
    private Integer noOfInvoicesReferedToHandler;
    private BigDecimal valOfInvoicesReferedToHandler;
    private BigDecimal valOfInterimPaymentReferedToHandler;
    private Integer noOfInvoicesUnassigned;
    private BigDecimal valOfInvoicesUnassigned;
    private BigDecimal valOfInterimPaymentUnassigned;
    private Integer noOfInvoicesCHODispute;
    private BigDecimal valOfInvoicesCHODispute;
    private BigDecimal valOfInterimPaymentCHODispute;
    private Integer noOfInvoicesApprovedAwaitingPay;
    private BigDecimal valOfInvoicesApprovedAwaitingPay;
    private BigDecimal valOfInterimPaymentApprovedAwaitingPay;

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
        result.setNoOfInvoicesUnassigned(((BigInteger) data.get("no_invoices_unassigned_current_month".toLowerCase())).intValue());
        result.setValOfInvoicesUnassigned(((BigDecimal) data.get("val_invoices_unassigned_current_month".toLowerCase())));
        result.setNoOfInvoicesCHODispute(((BigInteger) data.get("no_invoices_cho_dispute_current_month".toLowerCase())).intValue());
        result.setValOfInvoicesCHODispute(((BigDecimal) data.get("val_invoices_cho_dispute_current_month".toLowerCase())));
        result.setNoOfInvoicesApprovedAwaitingPay(((BigInteger) data.get("no_invoices_awaiting_payment_current_month".toLowerCase())).intValue());
        result.setValOfInvoicesApprovedAwaitingPay(((BigDecimal) data.get("val_invoices_awaiting_payment_current_month".toLowerCase())));
        result.setValOfInvoicesApprovedAwaitingPay(((BigDecimal) data.get("val_invoices_awaiting_payment_current_month".toLowerCase())));

        result.setValOfInterimPaymentUploaded(((BigDecimal) data.get("val_interim_payment_invoices_uploaded_current_month".toLowerCase())));
        result.setValOfInterimPaymentPaymentLogged(((BigDecimal) data.get("val_interim_payment_payment_logged_current_month".toLowerCase())));
        result.setValOfInterimPaymentPaymentReceived(((BigDecimal) data.get("val_interim_payment_reconciled_current_month".toLowerCase())));
        result.setValOfInterimPaymentWithdrawn(((BigDecimal) data.get("val_interim_payment_withdrawn_current_month".toLowerCase())));
        result.setValOfInterimPaymentAwaiting(((BigDecimal) data.get("val_interim_payment_awaiting_current_month".toLowerCase())));
        result.setValOfInterimPaymentAwaitingLiability(((BigDecimal) data.get("val_interim_payment_awaitingliability_current_month".toLowerCase())));
        result.setValOfInterimPaymentCHOAwaiting(((BigDecimal) data.get("val_interim_payment_cho_awaiting_current_month".toLowerCase())));
        result.setValOfInterimPaymentInsurerAwaiting(((BigDecimal) data.get("val_interim_payment_insurer_awaiting_current_month".toLowerCase())));
        result.setValOfInterimPaymentApprovedByBusiness(((BigDecimal) data.get("val_interim_payment_approved_by_businessrules_current_month".toLowerCase())));
        result.setValOfInterimPaymentEscalatedToHandler(((BigDecimal) data.get("val_interim_payment_escalated_to_handler_current_month".toLowerCase())));
        result.setValOfInterimPaymentEscalatedToEngineer(((BigDecimal) data.get("val_interim_payment_escalated_current_month".toLowerCase())));
        result.setValOfInterimPaymentReferredToEngineer(((BigDecimal) data.get("val_interim_payment_referred_to_engineer_current_month".toLowerCase())));
        result.setValOfInterimPaymentReferedToHandler(((BigDecimal) data.get("val_interim_payment_referred_to_handler_current_month".toLowerCase())));
        result.setValOfInterimPaymentCHODispute(((BigDecimal) data.get("val_interim_payment_cho_dispute_current_month".toLowerCase())));
        result.setValOfInterimPaymentUnassigned(((BigDecimal) data.get("val_interim_payment_unassigned_current_month".toLowerCase())));
        result.setValOfInterimPaymentApprovedAwaitingPay(((BigDecimal) data.get("val_interim_payment_awaiting_payment_current_month".toLowerCase())));

        return result;

    }

    public String getHeaderNames() {
        return headerNames;
    }

    public void setHeaderNames(String headerNames) {
        this.headerNames = headerNames.substring(0, 1).toUpperCase() + (headerNames.substring(1)).toLowerCase();
    }

    public Integer getNoOfInvoicesApprovedAwaitingPay() {
        return noOfInvoicesApprovedAwaitingPay;
    }

    public void setNoOfInvoicesApprovedAwaitingPay(Integer noOfInvoicesApprovedAwaitingPay) {
        this.noOfInvoicesApprovedAwaitingPay = noOfInvoicesApprovedAwaitingPay;
    }

    public Integer getNoOfInvoicesApprovedByBusiness() {
        return noOfInvoicesApprovedByBusiness;
    }

    public void setNoOfInvoicesApprovedByBusiness(Integer noOfInvoicesApprovedByBusiness) {
        this.noOfInvoicesApprovedByBusiness = noOfInvoicesApprovedByBusiness;
    }

    public Integer getNoOfInvoicesAwaiting() {
        return noOfInvoicesAwaiting;
    }

    public void setNoOfInvoicesAwaiting(Integer noOfInvoicesAwaiting) {
        this.noOfInvoicesAwaiting = noOfInvoicesAwaiting;
    }

    public Integer getNoOfInvoicesAwaitingLiability() {
        return noOfInvoicesAwaitingLiability;
    }

    public void setNoOfInvoicesAwaitingLiability(Integer noOfInvoicesAwaitingLiability) {
        this.noOfInvoicesAwaitingLiability = noOfInvoicesAwaitingLiability;
    }

    public Integer getNoOfInvoicesCHOAwaiting() {
        return noOfInvoicesCHOAwaiting;
    }

    public void setNoOfInvoicesCHOAwaiting(Integer noOfInvoicesCHOAwaiting) {
        this.noOfInvoicesCHOAwaiting = noOfInvoicesCHOAwaiting;
    }

    public Integer getNoOfInvoicesCHODispute() {
        return noOfInvoicesCHODispute;
    }

    public void setNoOfInvoicesCHODispute(Integer noOfInvoicesCHODispute) {
        this.noOfInvoicesCHODispute = noOfInvoicesCHODispute;
    }

    public Integer getNoOfInvoicesEscalatedToEngineer() {
        return noOfInvoicesEscalatedToEngineer;
    }

    public void setNoOfInvoicesEscalatedToEngineer(Integer noOfInvoicesEscalatedToEngineer) {
        this.noOfInvoicesEscalatedToEngineer = noOfInvoicesEscalatedToEngineer;
    }

    public Integer getNoOfInvoicesEscalatedToHandler() {
        return noOfInvoicesEscalatedToHandler;
    }

    public void setNoOfInvoicesEscalatedToHandler(Integer noOfInvoicesEscalatedToHandler) {
        this.noOfInvoicesEscalatedToHandler = noOfInvoicesEscalatedToHandler;
    }

    public Integer getNoOfInvoicesInsurerAwaiting() {
        return noOfInvoicesInsurerAwaiting;
    }

    public void setNoOfInvoicesInsurerAwaiting(Integer noOfInvoicesInsurerAwaiting) {
        this.noOfInvoicesInsurerAwaiting = noOfInvoicesInsurerAwaiting;
    }

    public Integer getNoOfInvoicesPaymentLogged() {
        return noOfInvoicesPaymentLogged;
    }

    public void setNoOfInvoicesPaymentLogged(Integer noOfInvoicesPaymentLogged) {
        this.noOfInvoicesPaymentLogged = noOfInvoicesPaymentLogged;
    }

    public Integer getNoOfInvoicesPaymentReceived() {
        return noOfInvoicesPaymentReceived;
    }

    public void setNoOfInvoicesPaymentReceived(Integer noOfInvoicesPaymentReceived) {
        this.noOfInvoicesPaymentReceived = noOfInvoicesPaymentReceived;
    }

    public Integer getNoOfInvoicesPenaltyApplied() {
        return noOfInvoicesPenaltyApplied;
    }

    public void setNoOfInvoicesPenaltyApplied(Integer noOfInvoicesPenaltyApplied) {
        this.noOfInvoicesPenaltyApplied = noOfInvoicesPenaltyApplied;
    }

    public Integer getNoOfInvoicesReferedToHandler() {
        return noOfInvoicesReferedToHandler;
    }

    public void setNoOfInvoicesReferedToHandler(Integer noOfInvoicesReferedToHandler) {
        this.noOfInvoicesReferedToHandler = noOfInvoicesReferedToHandler;
    }

    public Integer getNoOfInvoicesReferredToEngineer() {
        return noOfInvoicesReferredToEngineer;
    }

    public void setNoOfInvoicesReferredToEngineer(Integer noOfInvoicesReferredToEngineer) {
        this.noOfInvoicesReferredToEngineer = noOfInvoicesReferredToEngineer;
    }

    public Integer getNoOfInvoicesUnassigned() {
        return noOfInvoicesUnassigned;
    }

    public void setNoOfInvoicesUnassigned(Integer noOfInvoicesUnassigned) {
        this.noOfInvoicesUnassigned = noOfInvoicesUnassigned;
    }

    public Integer getNoOfInvoicesUploaded() {
        return noOfInvoicesUploaded;
    }

    public void setNoOfInvoicesUploaded(Integer noOfInvoicesUploaded) {
        this.noOfInvoicesUploaded = noOfInvoicesUploaded;
    }

    public Integer getNoOfInvoicesWithdrawn() {
        return noOfInvoicesWithdrawn;
    }

    public void setNoOfInvoicesWithdrawn(Integer noOfInvoicesWithdrawn) {
        this.noOfInvoicesWithdrawn = noOfInvoicesWithdrawn;
    }

    public BigDecimal getValOfInvoicesApprovedAwaitingPay() {
        return valOfInvoicesApprovedAwaitingPay;
    }

    public void setValOfInvoicesApprovedAwaitingPay(BigDecimal valOfInvoicesApprovedAwaitingPay) {
        this.valOfInvoicesApprovedAwaitingPay = valOfInvoicesApprovedAwaitingPay;
    }

    public BigDecimal getValOfInvoicesApprovedByBusiness() {
        return valOfInvoicesApprovedByBusiness;
    }

    public void setValOfInvoicesApprovedByBusiness(BigDecimal valOfInvoicesApprovedByBusiness) {
        this.valOfInvoicesApprovedByBusiness = valOfInvoicesApprovedByBusiness;
    }

    public BigDecimal getValOfInvoicesAwaiting() {
        return valOfInvoicesAwaiting;
    }

    public void setValOfInvoicesAwaiting(BigDecimal valOfInvoicesAwaiting) {
        this.valOfInvoicesAwaiting = valOfInvoicesAwaiting;
    }

    public BigDecimal getValOfInvoicesAwaitingLiability() {
        return valOfInvoicesAwaitingLiability;
    }

    public void setValOfInvoicesAwaitingLiability(BigDecimal valOfInvoicesAwaitingLiability) {
        this.valOfInvoicesAwaitingLiability = valOfInvoicesAwaitingLiability;
    }

    public BigDecimal getValOfInvoicesCHOAwaiting() {
        return valOfInvoicesCHOAwaiting;
    }

    public void setValOfInvoicesCHOAwaiting(BigDecimal valOfInvoicesCHOAwaiting) {
        this.valOfInvoicesCHOAwaiting = valOfInvoicesCHOAwaiting;
    }

    public BigDecimal getValOfInvoicesCHODispute() {
        return valOfInvoicesCHODispute;
    }

    public void setValOfInvoicesCHODispute(BigDecimal valOfInvoicesCHODispute) {
        this.valOfInvoicesCHODispute = valOfInvoicesCHODispute;
    }

    public BigDecimal getValOfInvoicesEscalatedToEngineer() {
        return valOfInvoicesEscalatedToEngineer;
    }

    public void setValOfInvoicesEscalatedToEngineer(BigDecimal valOfInvoicesEscalatedToEngineer) {
        this.valOfInvoicesEscalatedToEngineer = valOfInvoicesEscalatedToEngineer;
    }

    public BigDecimal getValOfInvoicesEscalatedToHandler() {
        return valOfInvoicesEscalatedToHandler;
    }

    public void setValOfInvoicesEscalatedToHandler(BigDecimal valOfInvoicesEscalatedToHandler) {
        this.valOfInvoicesEscalatedToHandler = valOfInvoicesEscalatedToHandler;
    }

    public BigDecimal getValOfInvoicesInsurerAwaiting() {
        return valOfInvoicesInsurerAwaiting;
    }

    public void setValOfInvoicesInsurerAwaiting(BigDecimal valOfInvoicesInsurerAwaiting) {
        this.valOfInvoicesInsurerAwaiting = valOfInvoicesInsurerAwaiting;
    }

    public BigDecimal getValOfInvoicesPaymentLogged() {
        return valOfInvoicesPaymentLogged;
    }

    public void setValOfInvoicesPaymentLogged(BigDecimal valOfInvoicesPaymentLogged) {
        this.valOfInvoicesPaymentLogged = valOfInvoicesPaymentLogged;
    }

    public BigDecimal getValOfInvoicesPaymentReceived() {
        return valOfInvoicesPaymentReceived;
    }

    public void setValOfInvoicesPaymentReceived(BigDecimal valOfInvoicesPaymentReceived) {
        this.valOfInvoicesPaymentReceived = valOfInvoicesPaymentReceived;
    }

    public BigDecimal getValOfInvoicesPenaltyApplied() {
        return valOfInvoicesPenaltyApplied;
    }

    public void setValOfInvoicesPenaltyApplied(BigDecimal valOfInvoicesPenaltyApplied) {
        this.valOfInvoicesPenaltyApplied = valOfInvoicesPenaltyApplied;
    }

    public BigDecimal getValOfInvoicesReferedToHandler() {
        return valOfInvoicesReferedToHandler;
    }

    public void setValOfInvoicesReferedToHandler(BigDecimal valOfInvoicesReferedToHandler) {
        this.valOfInvoicesReferedToHandler = valOfInvoicesReferedToHandler;
    }

    public BigDecimal getValOfInvoicesReferredToEngineer() {
        return valOfInvoicesReferredToEngineer;
    }

    public void setValOfInvoicesReferredToEngineer(BigDecimal valOfInvoicesReferredToEngineer) {
        this.valOfInvoicesReferredToEngineer = valOfInvoicesReferredToEngineer;
    }

    public BigDecimal getValOfInvoicesUnassigned() {
        return valOfInvoicesUnassigned;
    }

    public void setValOfInvoicesUnassigned(BigDecimal valOfInvoicesUnassigned) {
        this.valOfInvoicesUnassigned = valOfInvoicesUnassigned;
    }

    public BigDecimal getValOfInvoicesUploaded() {
        return valOfInvoicesUploaded;
    }

    public void setValOfInvoicesUploaded(BigDecimal valOfInvoicesUploaded) {
        this.valOfInvoicesUploaded = valOfInvoicesUploaded;
    }

    public BigDecimal getValOfInvoicesWithdrawn() {
        return valOfInvoicesWithdrawn;
    }

    public void setValOfInvoicesWithdrawn(BigDecimal valOfInvoicesWithdrawn) {
        this.valOfInvoicesWithdrawn = valOfInvoicesWithdrawn;
    }

    public BigDecimal getValOfInterimPaymentApprovedAwaitingPay() {
        return valOfInterimPaymentApprovedAwaitingPay;
    }

    public void setValOfInterimPaymentApprovedAwaitingPay(BigDecimal valOfInterimPaymentApprovedAwaitingPay) {
        this.valOfInterimPaymentApprovedAwaitingPay = valOfInterimPaymentApprovedAwaitingPay;
    }

    public BigDecimal getValOfInterimPaymentApprovedByBusiness() {
        return valOfInterimPaymentApprovedByBusiness;
    }

    public void setValOfInterimPaymentApprovedByBusiness(BigDecimal valOfInterimPaymentApprovedByBusiness) {
        this.valOfInterimPaymentApprovedByBusiness = valOfInterimPaymentApprovedByBusiness;
    }

    public BigDecimal getValOfInterimPaymentAwaiting() {
        return valOfInterimPaymentAwaiting;
    }

    public void setValOfInterimPaymentAwaiting(BigDecimal valOfInterimPaymentAwaiting) {
        this.valOfInterimPaymentAwaiting = valOfInterimPaymentAwaiting;
    }

    public BigDecimal getValOfInterimPaymentCHOAwaiting() {
        return valOfInterimPaymentCHOAwaiting;
    }

    public void setValOfInterimPaymentCHOAwaiting(BigDecimal valOfInterimPaymentCHOAwaiting) {
        this.valOfInterimPaymentCHOAwaiting = valOfInterimPaymentCHOAwaiting;
    }

    public BigDecimal getValOfInterimPaymentCHODispute() {
        return valOfInterimPaymentCHODispute;
    }

    public void setValOfInterimPaymentCHODispute(BigDecimal valOfInterimPaymentCHODispute) {
        this.valOfInterimPaymentCHODispute = valOfInterimPaymentCHODispute;
    }

    public BigDecimal getValOfInterimPaymentEscalatedToEngineer() {
        return valOfInterimPaymentEscalatedToEngineer;
    }

    public void setValOfInterimPaymentEscalatedToEngineer(BigDecimal valOfInterimPaymentEscalatedToEngineer) {
        this.valOfInterimPaymentEscalatedToEngineer = valOfInterimPaymentEscalatedToEngineer;
    }

    public BigDecimal getValOfInterimPaymentEscalatedToHandler() {
        return valOfInterimPaymentEscalatedToHandler;
    }

    public void setValOfInterimPaymentEscalatedToHandler(BigDecimal valOfInterimPaymentEscalatedToHandler) {
        this.valOfInterimPaymentEscalatedToHandler = valOfInterimPaymentEscalatedToHandler;
    }

    public BigDecimal getValOfInterimPaymentInsurerAwaiting() {
        return valOfInterimPaymentInsurerAwaiting;
    }

    public void setValOfInterimPaymentInsurerAwaiting(BigDecimal valOfInterimPaymentInsurerAwaiting) {
        this.valOfInterimPaymentInsurerAwaiting = valOfInterimPaymentInsurerAwaiting;
    }

    public BigDecimal getValOfInterimPaymentPaymentLogged() {
        return valOfInterimPaymentPaymentLogged;
    }

    public void setValOfInterimPaymentPaymentLogged(BigDecimal valOfInterimPaymentPaymentLogged) {
        this.valOfInterimPaymentPaymentLogged = valOfInterimPaymentPaymentLogged;
    }

    public BigDecimal getValOfInterimPaymentPaymentReceived() {
        return valOfInterimPaymentPaymentReceived;
    }

    public void setValOfInterimPaymentPaymentReceived(BigDecimal valOfInterimPaymentPaymentReceived) {
        this.valOfInterimPaymentPaymentReceived = valOfInterimPaymentPaymentReceived;
    }

    public BigDecimal getValOfInterimPaymentReferedToHandler() {
        return valOfInterimPaymentReferedToHandler;
    }

    public void setValOfInterimPaymentReferedToHandler(BigDecimal valOfInterimPaymentReferedToHandler) {
        this.valOfInterimPaymentReferedToHandler = valOfInterimPaymentReferedToHandler;
    }

    public BigDecimal getValOfInterimPaymentReferredToEngineer() {
        return valOfInterimPaymentReferredToEngineer;
    }

    public void setValOfInterimPaymentReferredToEngineer(BigDecimal valOfInterimPaymentReferredToEngineer) {
        this.valOfInterimPaymentReferredToEngineer = valOfInterimPaymentReferredToEngineer;
    }

    public BigDecimal getValOfInterimPaymentUnassigned() {
        return valOfInterimPaymentUnassigned;
    }

    public void setValOfInterimPaymentUnassigned(BigDecimal valOfInterimPaymentUnassigned) {
        this.valOfInterimPaymentUnassigned = valOfInterimPaymentUnassigned;
    }

    public BigDecimal getValOfInterimPaymentUploaded() {
        return valOfInterimPaymentUploaded;
    }

    public void setValOfInterimPaymentUploaded(BigDecimal valOfInterimPaymentUploaded) {
        this.valOfInterimPaymentUploaded = valOfInterimPaymentUploaded;
    }

    public BigDecimal getValOfInterimPaymentWithdrawn() {
        return valOfInterimPaymentWithdrawn;
    }

    public void setValOfInterimPaymentWithdrawn(BigDecimal valOfInterimPaymentWithdrawn) {
        this.valOfInterimPaymentWithdrawn = valOfInterimPaymentWithdrawn;
    }

    public BigDecimal getValOfInterimPaymentAwaitingLiability() {
        return valOfInterimPaymentAwaitingLiability;
    }

    public void setValOfInterimPaymentAwaitingLiability(BigDecimal valOfInterimPaymentAwaitingLiability) {
        this.valOfInterimPaymentAwaitingLiability = valOfInterimPaymentAwaitingLiability;
    }
}
