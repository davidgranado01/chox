package idas.chox.service.reports.viewdata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

public class InvoiceStatusReportCummulativeData {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceStatusReportViewData.class);
    private String headerName;
    private Integer noOfInvoicesUploadedCumm;
    private BigDecimal valOfInvoicesUploadedCumm;
    private BigDecimal valOfInterimPaymentUploadedCumm;
    private Integer noOfInvoicesPenaltyAppliedCumm;
    private BigDecimal valOfInvoicesPenaltyAppliedCumm;
    private Integer noOfInvoicesPaymentLoggedCumm;
    private BigDecimal valOfInvoicesPaymentLoggedCumm;
    private BigDecimal valOfInterimPaymentPaymentLoggedCumm;
    private Integer noOfInvoicesPaymentReceivedCumm;
    private BigDecimal valOfInvoicesPaymentReceivedCumm;
    private BigDecimal valOfInterimPaymentPaymentReceivedCumm;
    private Integer noOfInvoicesWithdrawnCumm;
    private BigDecimal valOfInvoicesWithdrawnCumm;
    private BigDecimal valOfInterimPaymentWithdrawnCumm;
    private Integer noOfInvoicesAwaitingCumm;
    private BigDecimal valOfInvoicesAwaitingCumm;
    private BigDecimal valOfInterimPaymentAwaitingCumm;
    private Integer noOfInvoicesAwaitingLiabilityCumm;
    private BigDecimal valOfInvoicesAwaitingLiabilityCumm;
    private BigDecimal valOfInterimPaymentAwaitingLiabilityCumm;
    private Integer noOfInvoicesCHOAwaitingCumm;
    private BigDecimal valOfInvoicesCHOAwaitingCumm;
    private BigDecimal valOfInterimPaymentCHOAwaitingCumm;
    private Integer noOfInvoicesInsurerAwaitingCumm;
    private BigDecimal valOfInvoicesInsurerAwaitingCumm;
    private BigDecimal valOfInterimPaymentInsurerAwaitingCumm;
    private Integer noOfInvoicesApprovedByBusinessCumm;
    private BigDecimal valOfInvoicesApprovedByBusinessCumm;
    private BigDecimal valOfInterimPaymentApprovedByBusinessCumm;
    private Integer noOfInvoicesEscalatedToHandlerCumm;
    private BigDecimal valOfInvoicesEscalatedToHandlerCumm;
    private BigDecimal valOfInterimPaymentEscalatedToHandlerCumm;
    private Integer noOfInvoicesEscalatedToEngineerCumm;
    private BigDecimal valOfInvoicesEscalatedToEngineerCumm;
    private BigDecimal valOfInterimPaymentEscalatedToEngineerCumm;
    private Integer noOfInvoicesReferredToEngineerCumm;
    private BigDecimal valOfInvoicesReferredToEngineerCumm;
    private BigDecimal valOfInterimPaymentReferredToEngineerCumm;
    private Integer noOfInvoicesReferedToHandlerCumm;
    private BigDecimal valOfInvoicesReferedToHandlerCumm;
    private BigDecimal valOfInterimPaymentReferedToHandlerCumm;
    private Integer noOfInvoicesUnassignedCumm;
    private BigDecimal valOfInvoicesUnassignedCumm;
    private BigDecimal valOfInterimPaymentUnassignedCumm;
    private Integer noOfInvoicesCHODisputeCumm;
    private BigDecimal valOfInvoicesCHODisputeCumm;
    private BigDecimal valOfInterimPaymentCHODisputeCumm;
    private Integer noOfInvoicesApprovedAwaitingPayCumm;
    private BigDecimal valOfInvoicesApprovedAwaitingPayCumm;
    private BigDecimal valOfInterimPaymentApprovedAwaitingPayCumm;
    private Integer noOfManualInvoicesPaidCumm;
    private BigDecimal valOfManualInvoicesPaidCumm;
    private Integer noOfManualInvoicesApprovedCumm;
    private BigDecimal valOfManualInvoicesApprovedCumm;
    private Integer noOfManualInvoicesRejectedCumm;
    private BigDecimal valOfManualInvoicesRejectedCumm;
    private Integer noOfManualInvoicesContestedCumm;
    private BigDecimal valOfManualInvoicesContestedCumm;
    private Integer noOfInvoicesInLitigationStatusCumm;
    private BigDecimal valOfInvoicesInLitigationStatusCumm;
    private BigDecimal valOfInterimPaymentInLitigationStatusCumm;

    public static InvoiceStatusReportCummulativeData getObject(Map data) {

        InvoiceStatusReportCummulativeData result = new InvoiceStatusReportCummulativeData();

        result.setHeaderName((String) data.get("month_header".toLowerCase()));
        result.setNoOfInvoicesUploadedCumm(((BigInteger) data.get("no_invoices_uploaded_total".toLowerCase())).intValue());
        result.setValOfInvoicesUploadedCumm(((BigDecimal) data.get("val_invoices_uploaded_total".toLowerCase())));
        result.setNoOfInvoicesPenaltyAppliedCumm(((BigInteger) data.get("no_invoices_penalty_total".toLowerCase())).intValue());
        result.setValOfInvoicesPenaltyAppliedCumm(((BigDecimal) data.get("val_invoices_penalty_total".toLowerCase())));
        result.setNoOfInvoicesPaymentLoggedCumm(((BigInteger) data.get("no_invoices_payment_logged_total".toLowerCase())).intValue());
        result.setValOfInvoicesPaymentLoggedCumm(((BigDecimal) data.get("val_invoices_payment_logged_total".toLowerCase())));
        result.setNoOfInvoicesPaymentReceivedCumm(((BigInteger) data.get("no_invoices_payment_reconciled_total".toLowerCase())).intValue());
        result.setValOfInvoicesPaymentReceivedCumm(((BigDecimal) data.get("val_invoices_payment_reconciled_total".toLowerCase())));
        result.setNoOfInvoicesWithdrawnCumm(((BigInteger) data.get("no_invoices_withdrawn_total".toLowerCase())).intValue());
        result.setValOfInvoicesWithdrawnCumm(((BigDecimal) data.get("val_invoices_withdrawn_total".toLowerCase())));
        result.setNoOfInvoicesAwaitingCumm(((BigInteger) data.get("no_invoices_awaiting_total".toLowerCase())).intValue());
        result.setValOfInvoicesAwaitingCumm(((BigDecimal) data.get("val_invoices_awaiting_total".toLowerCase())));
        result.setNoOfInvoicesAwaitingLiabilityCumm(((BigInteger) data.get("no_invoices_awaitingliability_total".toLowerCase())).intValue());
        result.setValOfInvoicesAwaitingLiabilityCumm(((BigDecimal) data.get("val_invoices_awaitingliability_total".toLowerCase())));
        result.setNoOfInvoicesCHOAwaitingCumm(((BigInteger) data.get("no_invoices_cho_awaiting_total".toLowerCase())).intValue());
        result.setValOfInvoicesCHOAwaitingCumm(((BigDecimal) data.get("val_invoices_cho_awaiting_total".toLowerCase())));
        result.setNoOfInvoicesInsurerAwaitingCumm(((BigInteger) data.get("no_invoices_insurer_awaiting_total".toLowerCase())).intValue());
        result.setValOfInvoicesInsurerAwaitingCumm(((BigDecimal) data.get("val_invoices_insurer_awaiting_total".toLowerCase())));
        result.setNoOfInvoicesApprovedByBusinessCumm(((BigInteger) data.get("no_invoices_approved_by_businessrules_total".toLowerCase())).intValue());
        result.setValOfInvoicesApprovedByBusinessCumm(((BigDecimal) data.get("val_invoices_approved_by_businessrules_total".toLowerCase())));
        result.setNoOfInvoicesEscalatedToHandlerCumm(((BigInteger) data.get("no_invoices_escalated_to_handler_total".toLowerCase())).intValue());
        result.setValOfInvoicesEscalatedToHandlerCumm(((BigDecimal) data.get("val_invoices_escalated_to_handler_total".toLowerCase())));
        result.setNoOfInvoicesEscalatedToEngineerCumm(((BigInteger) data.get("no_invoices_escalated_total".toLowerCase())).intValue());
        result.setValOfInvoicesEscalatedToEngineerCumm(((BigDecimal) data.get("val_invoices_escalated_total".toLowerCase())));
        result.setNoOfInvoicesReferredToEngineerCumm(((BigInteger) data.get("no_invoices_referred_to_engineer_total".toLowerCase())).intValue());
        result.setValOfInvoicesReferredToEngineerCumm(((BigDecimal) data.get("val_invoices_referred_to_engineer_total".toLowerCase())));
        result.setNoOfInvoicesReferedToHandlerCumm(((BigInteger) data.get("no_invoices_referred_to_handler_total".toLowerCase())).intValue());
        result.setValOfInvoicesReferedToHandlerCumm(((BigDecimal) data.get("val_invoices_referred_to_handler_total".toLowerCase())));
        result.setNoOfInvoicesUnassignedCumm(((BigInteger) data.get("no_invoices_unassigned_total".toLowerCase())).intValue());
        result.setValOfInvoicesUnassignedCumm(((BigDecimal) data.get("val_invoices_unassigned_total".toLowerCase())));
        result.setNoOfInvoicesCHODisputeCumm(((BigInteger) data.get("no_invoices_cho_dispute_total".toLowerCase())).intValue());
        result.setValOfInvoicesCHODisputeCumm(((BigDecimal) data.get("val_invoices_cho_dispute_total".toLowerCase())));
        result.setNoOfInvoicesApprovedAwaitingPayCumm(((BigInteger) data.get("no_invoices_awaiting_payment_total".toLowerCase())).intValue());
        result.setValOfInvoicesApprovedAwaitingPayCumm(((BigDecimal) data.get("val_invoices_awaiting_payment_total".toLowerCase())));
        
        result.setValOfInterimPaymentUploadedCumm(((BigDecimal) data.get("val_interim_payment_invoices_uploaded_total".toLowerCase())));
        result.setValOfInterimPaymentPaymentLoggedCumm(((BigDecimal) data.get("val_interim_payment_payment_logged_total".toLowerCase())));
        result.setValOfInterimPaymentPaymentReceivedCumm(((BigDecimal) data.get("val_interim_payment_reconciled_total".toLowerCase())));
        result.setValOfInterimPaymentWithdrawnCumm(((BigDecimal) data.get("val_interim_payment_withdrawn_total".toLowerCase())));
        result.setValOfInterimPaymentAwaitingCumm(((BigDecimal) data.get("val_interim_payment_awaiting_total".toLowerCase())));
        result.setValOfInterimPaymentAwaitingLiabilityCumm(((BigDecimal) data.get("val_interim_payment_awaitingliability_total".toLowerCase())));
        result.setValOfInterimPaymentCHOAwaitingCumm(((BigDecimal) data.get("val_interim_payment_cho_awaiting_total".toLowerCase())));
        result.setValOfInterimPaymentInsurerAwaitingCumm(((BigDecimal) data.get("val_interim_payment_insurer_awaiting_total".toLowerCase())));
        result.setValOfInterimPaymentApprovedByBusinessCumm(((BigDecimal) data.get("val_interim_payment_approved_by_businessrules_total".toLowerCase())));
        result.setValOfInterimPaymentEscalatedToHandlerCumm(((BigDecimal) data.get("val_interim_payment_escalated_to_handler_total".toLowerCase())));
        result.setValOfInterimPaymentEscalatedToEngineerCumm(((BigDecimal) data.get("val_interim_payment_escalated_total".toLowerCase())));
        result.setValOfInterimPaymentReferredToEngineerCumm(((BigDecimal) data.get("val_interim_payment_referred_to_engineer_total".toLowerCase())));
        result.setValOfInterimPaymentReferedToHandlerCumm(((BigDecimal) data.get("val_interim_payment_referred_to_handler_total".toLowerCase())));
        result.setValOfInterimPaymentCHODisputeCumm(((BigDecimal) data.get("val_interim_payment_cho_dispute_total".toLowerCase())));
        result.setValOfInterimPaymentUnassignedCumm(((BigDecimal) data.get("val_interim_payment_unassigned_total".toLowerCase())));
        result.setValOfInterimPaymentApprovedAwaitingPayCumm(((BigDecimal) data.get("val_interim_payment_awaiting_payment_total".toLowerCase())));
        
        result.setNoOfManualInvoicesPaidCumm(((BigInteger) data.get("no_manual_invoices_paid_total".toLowerCase())).intValue());
        result.setValOfManualInvoicesPaidCumm((BigDecimal) data.get("val_manual_invoices_paid_total".toLowerCase()));
        result.setNoOfManualInvoicesApprovedCumm(((BigInteger) data.get("no_manual_invoices_approved_total".toLowerCase())).intValue());
        result.setValOfManualInvoicesApprovedCumm((BigDecimal) data.get("val_manual_invoices_approved_total".toLowerCase()));
        result.setNoOfManualInvoicesRejectedCumm(((BigInteger) data.get("no_manual_invoices_rejected_total".toLowerCase())).intValue());
        result.setValOfManualInvoicesRejectedCumm((BigDecimal) data.get("val_manual_invoices_rejected_total".toLowerCase()));
        result.setNoOfManualInvoicesContestedCumm(((BigInteger) data.get("no_manual_invoices_contested_total".toLowerCase())).intValue());
        result.setValOfManualInvoicesContestedCumm((BigDecimal) data.get("val_manual_invoices_contested_total".toLowerCase()));
        
        result.setNoOfInvoicesInLitigationStatusCumm(((BigInteger) data.get("no_invoices_in_litigation_status_total".toLowerCase())).intValue());
        result.setValOfInvoicesInLitigationStatusCumm(((BigDecimal) data.get("val_invoices_in_litigation_status_total".toLowerCase())));
        result.setValOfInterimPaymentInLitigationStatusCumm(((BigDecimal) data.get("val_interim_payment_invoices_in_litigation_status_total".toLowerCase())));

        return result;

    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public Integer getNoOfInvoicesApprovedAwaitingPayCumm() {
        return noOfInvoicesApprovedAwaitingPayCumm;
    }

    public void setNoOfInvoicesApprovedAwaitingPayCumm(Integer noOfInvoicesApprovedAwaitingPayCumm) {
        this.noOfInvoicesApprovedAwaitingPayCumm = noOfInvoicesApprovedAwaitingPayCumm;
    }

    public Integer getNoOfInvoicesApprovedByBusinessCumm() {
        return noOfInvoicesApprovedByBusinessCumm;
    }

    public void setNoOfInvoicesApprovedByBusinessCumm(Integer noOfInvoicesApprovedByBusinessCumm) {
        this.noOfInvoicesApprovedByBusinessCumm = noOfInvoicesApprovedByBusinessCumm;
    }

    public Integer getNoOfInvoicesAwaitingCumm() {
        return noOfInvoicesAwaitingCumm;
    }

    public void setNoOfInvoicesAwaitingCumm(Integer noOfInvoicesAwaitingCumm) {
        this.noOfInvoicesAwaitingCumm = noOfInvoicesAwaitingCumm;
    }

    public Integer getNoOfInvoicesAwaitingLiabilityCumm() {
        return noOfInvoicesAwaitingLiabilityCumm;
    }

    public void setNoOfInvoicesAwaitingLiabilityCumm(Integer noOfInvoicesAwaitingLiabilityCumm) {
        this.noOfInvoicesAwaitingLiabilityCumm = noOfInvoicesAwaitingLiabilityCumm;
    }

    public Integer getNoOfInvoicesCHOAwaitingCumm() {
        return noOfInvoicesCHOAwaitingCumm;
    }

    public void setNoOfInvoicesCHOAwaitingCumm(Integer noOfInvoicesCHOAwaitingCumm) {
        this.noOfInvoicesCHOAwaitingCumm = noOfInvoicesCHOAwaitingCumm;
    }

    public Integer getNoOfInvoicesCHODisputeCumm() {
        return noOfInvoicesCHODisputeCumm;
    }

    public void setNoOfInvoicesCHODisputeCumm(Integer noOfInvoicesCHODisputeCumm) {
        this.noOfInvoicesCHODisputeCumm = noOfInvoicesCHODisputeCumm;
    }

    public Integer getNoOfInvoicesEscalatedToEngineerCumm() {
        return noOfInvoicesEscalatedToEngineerCumm;
    }

    public void setNoOfInvoicesEscalatedToEngineerCumm(Integer noOfInvoicesEscalatedToEngineerCumm) {
        this.noOfInvoicesEscalatedToEngineerCumm = noOfInvoicesEscalatedToEngineerCumm;
    }

    public Integer getNoOfInvoicesEscalatedToHandlerCumm() {
        return noOfInvoicesEscalatedToHandlerCumm;
    }

    public void setNoOfInvoicesEscalatedToHandlerCumm(Integer noOfInvoicesEscalatedToHandlerCumm) {
        this.noOfInvoicesEscalatedToHandlerCumm = noOfInvoicesEscalatedToHandlerCumm;
    }

    public Integer getNoOfInvoicesInsurerAwaitingCumm() {
        return noOfInvoicesInsurerAwaitingCumm;
    }

    public void setNoOfInvoicesInsurerAwaitingCumm(Integer noOfInvoicesInsurerAwaitingCumm) {
        this.noOfInvoicesInsurerAwaitingCumm = noOfInvoicesInsurerAwaitingCumm;
    }

    public Integer getNoOfInvoicesPaymentLoggedCumm() {
        return noOfInvoicesPaymentLoggedCumm;
    }

    public void setNoOfInvoicesPaymentLoggedCumm(Integer noOfInvoicesPaymentLoggedCumm) {
        this.noOfInvoicesPaymentLoggedCumm = noOfInvoicesPaymentLoggedCumm;
    }

    public Integer getNoOfInvoicesPaymentReceivedCumm() {
        return noOfInvoicesPaymentReceivedCumm;
    }

    public void setNoOfInvoicesPaymentReceivedCumm(Integer noOfInvoicesPaymentReceivedCumm) {
        this.noOfInvoicesPaymentReceivedCumm = noOfInvoicesPaymentReceivedCumm;
    }

    public Integer getNoOfInvoicesPenaltyAppliedCumm() {
        return noOfInvoicesPenaltyAppliedCumm;
    }

    public void setNoOfInvoicesPenaltyAppliedCumm(Integer noOfInvoicesPenaltyAppliedCumm) {
        this.noOfInvoicesPenaltyAppliedCumm = noOfInvoicesPenaltyAppliedCumm;
    }

    public Integer getNoOfInvoicesReferedToHandlerCumm() {
        return noOfInvoicesReferedToHandlerCumm;
    }

    public void setNoOfInvoicesReferedToHandlerCumm(Integer noOfInvoicesReferedToHandlerCumm) {
        this.noOfInvoicesReferedToHandlerCumm = noOfInvoicesReferedToHandlerCumm;
    }

    public Integer getNoOfInvoicesReferredToEngineerCumm() {
        return noOfInvoicesReferredToEngineerCumm;
    }

    public void setNoOfInvoicesReferredToEngineerCumm(Integer noOfInvoicesReferredToEngineerCumm) {
        this.noOfInvoicesReferredToEngineerCumm = noOfInvoicesReferredToEngineerCumm;
    }

    public Integer getNoOfInvoicesUnassignedCumm() {
        return noOfInvoicesUnassignedCumm;
    }

    public void setNoOfInvoicesUnassignedCumm(Integer noOfInvoicesUnassignedCumm) {
        this.noOfInvoicesUnassignedCumm = noOfInvoicesUnassignedCumm;
    }

    public Integer getNoOfInvoicesUploadedCumm() {
        return noOfInvoicesUploadedCumm;
    }

    public void setNoOfInvoicesUploadedCumm(Integer noOfInvoicesUploadedCumm) {
        this.noOfInvoicesUploadedCumm = noOfInvoicesUploadedCumm;
    }

    public Integer getNoOfInvoicesWithdrawnCumm() {
        return noOfInvoicesWithdrawnCumm;
    }

    public void setNoOfInvoicesWithdrawnCumm(Integer noOfInvoicesWithdrawnCumm) {
        this.noOfInvoicesWithdrawnCumm = noOfInvoicesWithdrawnCumm;
    }

    public BigDecimal getValOfInvoicesApprovedAwaitingPayCumm() {
        return valOfInvoicesApprovedAwaitingPayCumm;
    }

    public void setValOfInvoicesApprovedAwaitingPayCumm(BigDecimal valOfInvoicesApprovedAwaitingPayCumm) {
        this.valOfInvoicesApprovedAwaitingPayCumm = valOfInvoicesApprovedAwaitingPayCumm;
    }

    public BigDecimal getValOfInvoicesApprovedByBusinessCumm() {
        return valOfInvoicesApprovedByBusinessCumm;
    }

    public void setValOfInvoicesApprovedByBusinessCumm(BigDecimal valOfInvoicesApprovedByBusinessCumm) {
        this.valOfInvoicesApprovedByBusinessCumm = valOfInvoicesApprovedByBusinessCumm;
    }

    public BigDecimal getValOfInvoicesAwaitingCumm() {
        return valOfInvoicesAwaitingCumm;
    }

    public void setValOfInvoicesAwaitingCumm(BigDecimal valOfInvoicesAwaitingCumm) {
        this.valOfInvoicesAwaitingCumm = valOfInvoicesAwaitingCumm;
    }

    public BigDecimal getValOfInvoicesAwaitingLiabilityCumm() {
        return valOfInvoicesAwaitingLiabilityCumm;
    }

    public void setValOfInvoicesAwaitingLiabilityCumm(BigDecimal valOfInvoicesAwaitingLiabilityCumm) {
        this.valOfInvoicesAwaitingLiabilityCumm = valOfInvoicesAwaitingLiabilityCumm;
    }

    public BigDecimal getValOfInvoicesCHOAwaitingCumm() {
        return valOfInvoicesCHOAwaitingCumm;
    }

    public void setValOfInvoicesCHOAwaitingCumm(BigDecimal valOfInvoicesCHOAwaitingCumm) {
        this.valOfInvoicesCHOAwaitingCumm = valOfInvoicesCHOAwaitingCumm;
    }

    public BigDecimal getValOfInvoicesCHODisputeCumm() {
        return valOfInvoicesCHODisputeCumm;
    }

    public void setValOfInvoicesCHODisputeCumm(BigDecimal valOfInvoicesCHODisputeCumm) {
        this.valOfInvoicesCHODisputeCumm = valOfInvoicesCHODisputeCumm;
    }

    public BigDecimal getValOfInvoicesEscalatedToEngineerCumm() {
        return valOfInvoicesEscalatedToEngineerCumm;
    }

    public void setValOfInvoicesEscalatedToEngineerCumm(BigDecimal valOfInvoicesEscalatedToEngineerCumm) {
        this.valOfInvoicesEscalatedToEngineerCumm = valOfInvoicesEscalatedToEngineerCumm;
    }

    public BigDecimal getValOfInvoicesEscalatedToHandlerCumm() {
        return valOfInvoicesEscalatedToHandlerCumm;
    }

    public void setValOfInvoicesEscalatedToHandlerCumm(BigDecimal valOfInvoicesEscalatedToHandlerCumm) {
        this.valOfInvoicesEscalatedToHandlerCumm = valOfInvoicesEscalatedToHandlerCumm;
    }

    public BigDecimal getValOfInvoicesInsurerAwaitingCumm() {
        return valOfInvoicesInsurerAwaitingCumm;
    }

    public void setValOfInvoicesInsurerAwaitingCumm(BigDecimal valOfInvoicesInsurerAwaitingCumm) {
        this.valOfInvoicesInsurerAwaitingCumm = valOfInvoicesInsurerAwaitingCumm;
    }

    public BigDecimal getValOfInvoicesPaymentLoggedCumm() {
        return valOfInvoicesPaymentLoggedCumm;
    }

    public void setValOfInvoicesPaymentLoggedCumm(BigDecimal valOfInvoicesPaymentLoggedCumm) {
        this.valOfInvoicesPaymentLoggedCumm = valOfInvoicesPaymentLoggedCumm;
    }

    public BigDecimal getValOfInvoicesPaymentReceivedCumm() {
        return valOfInvoicesPaymentReceivedCumm;
    }

    public void setValOfInvoicesPaymentReceivedCumm(BigDecimal valOfInvoicesPaymentReceivedCumm) {
        this.valOfInvoicesPaymentReceivedCumm = valOfInvoicesPaymentReceivedCumm;
    }

    public BigDecimal getValOfInvoicesPenaltyAppliedCumm() {
        return valOfInvoicesPenaltyAppliedCumm;
    }

    public void setValOfInvoicesPenaltyAppliedCumm(BigDecimal valOfInvoicesPenaltyAppliedCumm) {
        this.valOfInvoicesPenaltyAppliedCumm = valOfInvoicesPenaltyAppliedCumm;
    }

    public BigDecimal getValOfInvoicesReferedToHandlerCumm() {
        return valOfInvoicesReferedToHandlerCumm;
    }

    public void setValOfInvoicesReferedToHandlerCumm(BigDecimal valOfInvoicesReferedToHandlerCumm) {
        this.valOfInvoicesReferedToHandlerCumm = valOfInvoicesReferedToHandlerCumm;
    }

    public BigDecimal getValOfInvoicesReferredToEngineerCumm() {
        return valOfInvoicesReferredToEngineerCumm;
    }

    public void setValOfInvoicesReferredToEngineerCumm(BigDecimal valOfInvoicesReferredToEngineerCumm) {
        this.valOfInvoicesReferredToEngineerCumm = valOfInvoicesReferredToEngineerCumm;
    }

    public BigDecimal getValOfInvoicesUnassignedCumm() {
        return valOfInvoicesUnassignedCumm;
    }

    public void setValOfInvoicesUnassignedCumm(BigDecimal valOfInvoicesUnassignedCumm) {
        this.valOfInvoicesUnassignedCumm = valOfInvoicesUnassignedCumm;
    }

    public BigDecimal getValOfInvoicesUploadedCumm() {
        return valOfInvoicesUploadedCumm;
    }

    public void setValOfInvoicesUploadedCumm(BigDecimal valOfInvoicesUploadedCumm) {
        this.valOfInvoicesUploadedCumm = valOfInvoicesUploadedCumm;
    }

    public BigDecimal getValOfInvoicesWithdrawnCumm() {
        return valOfInvoicesWithdrawnCumm;
    }

    public void setValOfInvoicesWithdrawnCumm(BigDecimal valOfInvoicesWithdrawnCumm) {
        this.valOfInvoicesWithdrawnCumm = valOfInvoicesWithdrawnCumm;
    }

    public BigDecimal getValOfInterimPaymentApprovedAwaitingPayCumm() {
        return valOfInterimPaymentApprovedAwaitingPayCumm;
    }

    public void setValOfInterimPaymentApprovedAwaitingPayCumm(BigDecimal valOfInterimPaymentApprovedAwaitingPayCumm) {
        this.valOfInterimPaymentApprovedAwaitingPayCumm = valOfInterimPaymentApprovedAwaitingPayCumm;
    }

    public BigDecimal getValOfInterimPaymentApprovedByBusinessCumm() {
        return valOfInterimPaymentApprovedByBusinessCumm;
    }

    public void setValOfInterimPaymentApprovedByBusinessCumm(BigDecimal valOfInterimPaymentApprovedByBusinessCumm) {
        this.valOfInterimPaymentApprovedByBusinessCumm = valOfInterimPaymentApprovedByBusinessCumm;
    }

    public BigDecimal getValOfInterimPaymentAwaitingCumm() {
        return valOfInterimPaymentAwaitingCumm;
    }

    public void setValOfInterimPaymentAwaitingCumm(BigDecimal valOfInterimPaymentAwaitingCumm) {
        this.valOfInterimPaymentAwaitingCumm = valOfInterimPaymentAwaitingCumm;
    }

    public BigDecimal getValOfInterimPaymentCHOAwaitingCumm() {
        return valOfInterimPaymentCHOAwaitingCumm;
    }

    public void setValOfInterimPaymentCHOAwaitingCumm(BigDecimal valOfInterimPaymentCHOAwaitingCumm) {
        this.valOfInterimPaymentCHOAwaitingCumm = valOfInterimPaymentCHOAwaitingCumm;
    }

    public BigDecimal getValOfInterimPaymentCHODisputeCumm() {
        return valOfInterimPaymentCHODisputeCumm;
    }

    public void setValOfInterimPaymentCHODisputeCumm(BigDecimal valOfInterimPaymentCHODisputeCumm) {
        this.valOfInterimPaymentCHODisputeCumm = valOfInterimPaymentCHODisputeCumm;
    }

    public BigDecimal getValOfInterimPaymentEscalatedToEngineerCumm() {
        return valOfInterimPaymentEscalatedToEngineerCumm;
    }

    public void setValOfInterimPaymentEscalatedToEngineerCumm(BigDecimal valOfInterimPaymentEscalatedToEngineerCumm) {
        this.valOfInterimPaymentEscalatedToEngineerCumm = valOfInterimPaymentEscalatedToEngineerCumm;
    }

    public BigDecimal getValOfInterimPaymentEscalatedToHandlerCumm() {
        return valOfInterimPaymentEscalatedToHandlerCumm;
    }

    public void setValOfInterimPaymentEscalatedToHandlerCumm(BigDecimal valOfInterimPaymentEscalatedToHandlerCumm) {
        this.valOfInterimPaymentEscalatedToHandlerCumm = valOfInterimPaymentEscalatedToHandlerCumm;
    }

    public BigDecimal getValOfInterimPaymentInsurerAwaitingCumm() {
        return valOfInterimPaymentInsurerAwaitingCumm;
    }

    public void setValOfInterimPaymentInsurerAwaitingCumm(BigDecimal valOfInterimPaymentInsurerAwaitingCumm) {
        this.valOfInterimPaymentInsurerAwaitingCumm = valOfInterimPaymentInsurerAwaitingCumm;
    }

    public BigDecimal getValOfInterimPaymentPaymentLoggedCumm() {
        return valOfInterimPaymentPaymentLoggedCumm;
    }

    public void setValOfInterimPaymentPaymentLoggedCumm(BigDecimal valOfInterimPaymentPaymentLoggedCumm) {
        this.valOfInterimPaymentPaymentLoggedCumm = valOfInterimPaymentPaymentLoggedCumm;
    }

    public BigDecimal getValOfInterimPaymentPaymentReceivedCumm() {
        return valOfInterimPaymentPaymentReceivedCumm;
    }

    public void setValOfInterimPaymentPaymentReceivedCumm(BigDecimal valOfInterimPaymentPaymentReceivedCumm) {
        this.valOfInterimPaymentPaymentReceivedCumm = valOfInterimPaymentPaymentReceivedCumm;
    }

    public BigDecimal getValOfInterimPaymentReferedToHandlerCumm() {
        return valOfInterimPaymentReferedToHandlerCumm;
    }

    public void setValOfInterimPaymentReferedToHandlerCumm(BigDecimal valOfInterimPaymentReferedToHandlerCumm) {
        this.valOfInterimPaymentReferedToHandlerCumm = valOfInterimPaymentReferedToHandlerCumm;
    }

    public BigDecimal getValOfInterimPaymentReferredToEngineerCumm() {
        return valOfInterimPaymentReferredToEngineerCumm;
    }

    public void setValOfInterimPaymentReferredToEngineerCumm(BigDecimal valOfInterimPaymentReferredToEngineerCumm) {
        this.valOfInterimPaymentReferredToEngineerCumm = valOfInterimPaymentReferredToEngineerCumm;
    }

    public BigDecimal getValOfInterimPaymentUnassignedCumm() {
        return valOfInterimPaymentUnassignedCumm;
    }

    public void setValOfInterimPaymentUnassignedCumm(BigDecimal valOfInterimPaymentUnassignedCumm) {
        this.valOfInterimPaymentUnassignedCumm = valOfInterimPaymentUnassignedCumm;
    }

    public BigDecimal getValOfInterimPaymentUploadedCumm() {
        return valOfInterimPaymentUploadedCumm;
    }

    public void setValOfInterimPaymentUploadedCumm(BigDecimal valOfInterimPaymentUploadedCumm) {
        this.valOfInterimPaymentUploadedCumm = valOfInterimPaymentUploadedCumm;
    }

    public BigDecimal getValOfInterimPaymentWithdrawnCumm() {
        return valOfInterimPaymentWithdrawnCumm;
    }

    public void setValOfInterimPaymentWithdrawnCumm(BigDecimal valOfInterimPaymentWithdrawnCumm) {
        this.valOfInterimPaymentWithdrawnCumm = valOfInterimPaymentWithdrawnCumm;
    }

    public BigDecimal getValOfInterimPaymentAwaitingLiabilityCumm() {
        return valOfInterimPaymentAwaitingLiabilityCumm;
    }

    public void setValOfInterimPaymentAwaitingLiabilityCumm(BigDecimal valOfInterimPaymentAwaitingLiabilityCumm) {
        this.valOfInterimPaymentAwaitingLiabilityCumm = valOfInterimPaymentAwaitingLiabilityCumm;
    }

    public Integer getNoOfManualInvoicesApprovedCumm() {
        return noOfManualInvoicesApprovedCumm;
    }

    public void setNoOfManualInvoicesApprovedCumm(Integer noOfManualInvoicesApprovedCumm) {
        this.noOfManualInvoicesApprovedCumm = noOfManualInvoicesApprovedCumm;
    }

    public Integer getNoOfManualInvoicesPaidCumm() {
        return noOfManualInvoicesPaidCumm;
    }

    public void setNoOfManualInvoicesPaidCumm(Integer noOfManualInvoicesPaidCumm) {
        this.noOfManualInvoicesPaidCumm = noOfManualInvoicesPaidCumm;
    }

    public Integer getNoOfManualInvoicesRejectedCumm() {
        return noOfManualInvoicesRejectedCumm;
    }

    public void setNoOfManualInvoicesRejectedCumm(Integer noOfManualInvoicesRejectedCumm) {
        this.noOfManualInvoicesRejectedCumm = noOfManualInvoicesRejectedCumm;
    }

    public BigDecimal getValOfManualInvoicesApprovedCumm() {
        return valOfManualInvoicesApprovedCumm;
    }

    public void setValOfManualInvoicesApprovedCumm(BigDecimal valOfManualInvoicesApprovedCumm) {
        this.valOfManualInvoicesApprovedCumm = valOfManualInvoicesApprovedCumm;
    }

    public BigDecimal getValOfManualInvoicesPaidCumm() {
        return valOfManualInvoicesPaidCumm;
    }

    public void setValOfManualInvoicesPaidCumm(BigDecimal valOfManualInvoicesPaidCumm) {
        this.valOfManualInvoicesPaidCumm = valOfManualInvoicesPaidCumm;
    }

    public BigDecimal getValOfManualInvoicesRejectedCumm() {
        return valOfManualInvoicesRejectedCumm;
    }

    public void setValOfManualInvoicesRejectedCumm(BigDecimal valOfManualInvoicesRejectedCumm) {
        this.valOfManualInvoicesRejectedCumm = valOfManualInvoicesRejectedCumm;
    }

    public Integer getNoOfManualInvoicesContestedCumm() {
        return noOfManualInvoicesContestedCumm;
    }

    public void setNoOfManualInvoicesContestedCumm(Integer noOfManualInvoicesContestedCumm) {
        this.noOfManualInvoicesContestedCumm = noOfManualInvoicesContestedCumm;
    }

    public BigDecimal getValOfManualInvoicesContestedCumm() {
        return valOfManualInvoicesContestedCumm;
    }

    public void setValOfManualInvoicesContestedCumm(BigDecimal valOfManualInvoicesContestedCumm) {
        this.valOfManualInvoicesContestedCumm = valOfManualInvoicesContestedCumm;
    }

    public Integer getNoOfInvoicesInLitigationStatusCumm() {
        return noOfInvoicesInLitigationStatusCumm;
    }

    public void setNoOfInvoicesInLitigationStatusCumm(Integer noOfInvoicesInLitigationStatusCumm) {
        this.noOfInvoicesInLitigationStatusCumm = noOfInvoicesInLitigationStatusCumm;
    }

    public BigDecimal getValOfInterimPaymentInLitigationStatusCumm() {
        return valOfInterimPaymentInLitigationStatusCumm;
    }

    public void setValOfInterimPaymentInLitigationStatusCumm(BigDecimal valOfInterimPaymentInLitigationStatusCumm) {
        this.valOfInterimPaymentInLitigationStatusCumm = valOfInterimPaymentInLitigationStatusCumm;
    }

    public BigDecimal getValOfInvoicesInLitigationStatusCumm() {
        return valOfInvoicesInLitigationStatusCumm;
    }

    public void setValOfInvoicesInLitigationStatusCumm(BigDecimal valOfInvoicesInLitigationStatusCumm) {
        this.valOfInvoicesInLitigationStatusCumm = valOfInvoicesInLitigationStatusCumm;
    }
}
