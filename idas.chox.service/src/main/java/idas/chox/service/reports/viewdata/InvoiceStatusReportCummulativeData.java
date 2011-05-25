/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package idas.chox.service.reports.viewdata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

/**
 *
 * @author rajareddydodda
 */
public class InvoiceStatusReportCummulativeData {



    private static final Logger LOG = LoggerFactory.getLogger(InvoiceStatusReportViewData.class);

private String headerName;
private Integer noOfInvoicesUploadedCumm;
private BigDecimal valOfInvoicesUploadedCumm;
private Integer noOfInvoicesPenaltyAppliedCumm;
private BigDecimal valOfInvoicesPenaltyAppliedCumm;
private Integer noOfInvoicesPaymentLoggedCumm;
private BigDecimal valOfInvoicesPaymentLoggedCumm;
private Integer noOfInvoicesPaymentReceivedCumm;
private BigDecimal valOfInvoicesPaymentReceivedCumm;
private Integer noOfInvoicesWithdrawnCumm;
private BigDecimal valOfInvoicesWithdrawnCumm;
private Integer noOfInvoicesAwaitingCumm;
private BigDecimal valOfInvoicesAwaitingCumm;
private Integer noOfInvoicesAwaitingLiabilityCumm;
private BigDecimal valOfInvoicesAwaitingLiabilityCumm;
private Integer noOfInvoicesCHOAwaitingCumm;
private BigDecimal valOfInvoicesCHOAwaitingCumm;
private Integer noOfInvoicesInsurerAwaitingCumm;
private BigDecimal valOfInvoicesInsurerAwaitingCumm;
private Integer noOfInvoicesApprovedByBusinessCumm;
private BigDecimal valOfInvoicesApprovedByBusinessCumm;
private Integer noOfInvoicesEscalatedToHandlerCumm;
private BigDecimal valOfInvoicesEscalatedToHandlerCumm;
private Integer noOfInvoicesEscalatedToEngineerCumm;
private BigDecimal valOfInvoicesEscalatedToEngineerCumm;
private Integer noOfInvoicesReferredToEngineerCumm;
private BigDecimal valOfInvoicesReferredToEngineerCumm;
private Integer noOfInvoicesReferedToHandlerCumm;
private BigDecimal valOfInvoicesReferedToHandlerCumm;
private Integer noOfInvoicesUnassignedCumm;
private BigDecimal valOfInvoicesUnassignedCumm;
private Integer  noOfInvoicesCHODisputeCumm;
private BigDecimal valOfInvoicesCHODisputeCumm;
private Integer  noOfInvoicesApprovedAwaitingPayCumm;
private BigDecimal valOfInvoicesApprovedAwaitingPayCumm;




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
        

        return result;

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
        this.headerName = headerName;
    }





    /**
     * @return the noOfInvoicesUploadedCumm
     */
    public Integer getNoOfInvoicesUploadedCumm() {
        return noOfInvoicesUploadedCumm;
    }

    /**
     * @param noOfInvoicesUploadedCumm the noOfInvoicesUploadedCumm to set
     */
    public void setNoOfInvoicesUploadedCumm(Integer noOfInvoicesUploadedCumm) {
        this.noOfInvoicesUploadedCumm = noOfInvoicesUploadedCumm;
    }

    /**
     * @return the valOfInvoicesUploadedCumm
     */
    public BigDecimal getValOfInvoicesUploadedCumm() {
        return valOfInvoicesUploadedCumm;
    }

    /**
     * @param valOfInvoicesUploadedCumm the valOfInvoicesUploadedCumm to set
     */
    public void setValOfInvoicesUploadedCumm(BigDecimal valOfInvoicesUploadedCumm) {

        if(valOfInvoicesUploadedCumm == null)
        this.valOfInvoicesUploadedCumm = new BigDecimal(0.00);
        else
        this.valOfInvoicesUploadedCumm = valOfInvoicesUploadedCumm;
    }

    /**
     * @return the noOfInvoicesPenaltyAppliedCumm
     */
    public Integer getNoOfInvoicesPenaltyAppliedCumm() {
        return noOfInvoicesPenaltyAppliedCumm;
    }

    /**
     * @param noOfInvoicesPenaltyAppliedCumm the noOfInvoicesPenaltyAppliedCumm to set
     */
    public void setNoOfInvoicesPenaltyAppliedCumm(Integer noOfInvoicesPenaltyAppliedCumm) {
        
        this.noOfInvoicesPenaltyAppliedCumm = noOfInvoicesPenaltyAppliedCumm;
    }

    /**
     * @return the valOfInvoicesPenaltyAppliedCumm
     */
    public BigDecimal getValOfInvoicesPenaltyAppliedCumm() {
        return valOfInvoicesPenaltyAppliedCumm;
    }

    /**
     * @param valOfInvoicesPenaltyAppliedCumm the valOfInvoicesPenaltyAppliedCumm to set
     */
    public void setValOfInvoicesPenaltyAppliedCumm(BigDecimal valOfInvoicesPenaltyAppliedCumm) {


        if(valOfInvoicesPenaltyAppliedCumm == null)
        this.valOfInvoicesPenaltyAppliedCumm = new BigDecimal(0.00);
        else
        this.valOfInvoicesPenaltyAppliedCumm = valOfInvoicesPenaltyAppliedCumm;
        
    }

    /**
     * @return the noOfInvoicesPaymentLoggedCumm
     */
    public Integer getNoOfInvoicesPaymentLoggedCumm() {
        return noOfInvoicesPaymentLoggedCumm;
    }

    /**
     * @param noOfInvoicesPaymentLoggedCumm the noOfInvoicesPaymentLoggedCumm to set
     */
    public void setNoOfInvoicesPaymentLoggedCumm(Integer noOfInvoicesPaymentLoggedCumm) {
        this.noOfInvoicesPaymentLoggedCumm = noOfInvoicesPaymentLoggedCumm;
    }

    /**
     * @return the valOfInvoicesPaymentLoggedCumm
     */
    public BigDecimal getValOfInvoicesPaymentLoggedCumm() {
        return valOfInvoicesPaymentLoggedCumm;
    }

    /**
     * @param valOfInvoicesPaymentLoggedCumm the valOfInvoicesPaymentLoggedCumm to set
     */
    public void setValOfInvoicesPaymentLoggedCumm(BigDecimal valOfInvoicesPaymentLoggedCumm) {


        if(valOfInvoicesPaymentLoggedCumm == null)
        this.valOfInvoicesPaymentLoggedCumm = new BigDecimal(0.00);
        else
        this.valOfInvoicesPaymentLoggedCumm = valOfInvoicesPaymentLoggedCumm;
        
    }

    /**
     * @return the noOfInvoicesPaymentReceivedCumm
     */
    public Integer getNoOfInvoicesPaymentReceivedCumm() {
        return noOfInvoicesPaymentReceivedCumm;
    }

    /**
     * @param noOfInvoicesPaymentReceivedCumm the noOfInvoicesPaymentReceivedCumm to set
     */
    public void setNoOfInvoicesPaymentReceivedCumm(Integer noOfInvoicesPaymentReceivedCumm) {
        this.noOfInvoicesPaymentReceivedCumm = noOfInvoicesPaymentReceivedCumm;
    }

    /**
     * @return the valOfInvoicesPaymentReceivedCumm
     */
    public BigDecimal getValOfInvoicesPaymentReceivedCumm() {
        return valOfInvoicesPaymentReceivedCumm;
    }

    /**
     * @param valOfInvoicesPaymentReceivedCumm the valOfInvoicesPaymentReceivedCumm to set
     */
    public void setValOfInvoicesPaymentReceivedCumm(BigDecimal valOfInvoicesPaymentReceivedCumm) {


        if(valOfInvoicesPaymentReceivedCumm == null)
        this.valOfInvoicesPaymentReceivedCumm = new BigDecimal(0.00);
        else
        this.valOfInvoicesPaymentReceivedCumm = valOfInvoicesPaymentReceivedCumm;
        
    }

    /**
     * @return the noOfInvoicesWithdrawnCumm
     */
    public Integer getNoOfInvoicesWithdrawnCumm() {
        return noOfInvoicesWithdrawnCumm;
    }

    /**
     * @param noOfInvoicesWithdrawnCumm the noOfInvoicesWithdrawnCumm to set
     */
    public void setNoOfInvoicesWithdrawnCumm(Integer noOfInvoicesWithdrawnCumm) {
        this.noOfInvoicesWithdrawnCumm = noOfInvoicesWithdrawnCumm;
    }

    /**
     * @return the valOfInvoicesWithdrawnCumm
     */
    public BigDecimal getValOfInvoicesWithdrawnCumm() {
        return valOfInvoicesWithdrawnCumm;
    }

    /**
     * @param valOfInvoicesWithdrawnCumm the valOfInvoicesWithdrawnCumm to set
     */
    public void setValOfInvoicesWithdrawnCumm(BigDecimal valOfInvoicesWithdrawnCumm) {
        if(valOfInvoicesWithdrawnCumm == null)
        this.valOfInvoicesWithdrawnCumm = new BigDecimal(0.00);
        else
        this.valOfInvoicesWithdrawnCumm = valOfInvoicesWithdrawnCumm;
    }

    /**
     * @return the noOfInvoicesAwaitingCumm
     */
    public Integer getNoOfInvoicesAwaitingCumm() {
        return noOfInvoicesAwaitingCumm;
    }

    /**
     * @param noOfInvoicesAwaitingCumm the noOfInvoicesAwaitingCumm to set
     */
    public void setNoOfInvoicesAwaitingCumm(Integer noOfInvoicesAwaitingCumm) {
        this.noOfInvoicesAwaitingCumm = noOfInvoicesAwaitingCumm;
    }

    /**
     * @return the valOfInvoicesAwaitingCumm
     */
    public BigDecimal getValOfInvoicesAwaitingCumm() {
        return valOfInvoicesAwaitingCumm;
    }

    /**
     * @param valOfInvoicesAwaitingCumm the valOfInvoicesAwaitingCumm to set
     */
    public void setValOfInvoicesAwaitingCumm(BigDecimal valOfInvoicesAwaitingCumm) {

        
        if(valOfInvoicesAwaitingCumm == null)
        this.valOfInvoicesAwaitingCumm = new BigDecimal(0.00);
        else
        this.valOfInvoicesAwaitingCumm = valOfInvoicesAwaitingCumm;
    }

    /**
     * @return the noOfInvoicesAwaitingLiabilityCumm
     */
    public Integer getNoOfInvoicesAwaitingLiabilityCumm() {
        return noOfInvoicesAwaitingLiabilityCumm;
    }

    /**
     * @param noOfInvoicesAwaitingLiabilityCumm the noOfInvoicesAwaitingLiabilityCumm to set
     */
    public void setNoOfInvoicesAwaitingLiabilityCumm(Integer noOfInvoicesAwaitingLiabilityCumm) {
        this.noOfInvoicesAwaitingLiabilityCumm = noOfInvoicesAwaitingLiabilityCumm;
    }

    /**
     * @return the valOfInvoicesAwaitingLiabilityCumm
     */
    public BigDecimal getValOfInvoicesAwaitingLiabilityCumm() {
        return valOfInvoicesAwaitingLiabilityCumm;
    }

    /**
     * @param valOfInvoicesAwaitingLiabilityCumm the valOfInvoicesAwaitingLiabilityCumm to set
     */
    public void setValOfInvoicesAwaitingLiabilityCumm(BigDecimal valOfInvoicesAwaitingLiabilityCumm) {


        if(valOfInvoicesAwaitingLiabilityCumm == null)
        this.valOfInvoicesAwaitingLiabilityCumm = new BigDecimal(0.00);
        else
        this.valOfInvoicesAwaitingLiabilityCumm = valOfInvoicesAwaitingLiabilityCumm;
        
    }

    /**
     * @return the noOfInvoicesCHOAwaitingCumm
     */
    public Integer getNoOfInvoicesCHOAwaitingCumm() {
        return noOfInvoicesCHOAwaitingCumm;
    }

    /**
     * @param noOfInvoicesCHOAwaitingCumm the noOfInvoicesCHOAwaitingCumm to set
     */
    public void setNoOfInvoicesCHOAwaitingCumm(Integer noOfInvoicesCHOAwaitingCumm) {
        this.noOfInvoicesCHOAwaitingCumm = noOfInvoicesCHOAwaitingCumm;
    }

    /**
     * @return the valOfInvoicesCHOAwaitingCumm
     */
    public BigDecimal getValOfInvoicesCHOAwaitingCumm() {
        return valOfInvoicesCHOAwaitingCumm;
    }

    /**
     * @param valOfInvoicesCHOAwaitingCumm the valOfInvoicesCHOAwaitingCumm to set
     */
    public void setValOfInvoicesCHOAwaitingCumm(BigDecimal valOfInvoicesCHOAwaitingCumm) {

        if(valOfInvoicesCHOAwaitingCumm == null)
        this.valOfInvoicesCHOAwaitingCumm = new BigDecimal(0.00);
        else
        this.valOfInvoicesCHOAwaitingCumm = valOfInvoicesCHOAwaitingCumm;
        
    }

    /**
     * @return the noOfInvoicesInsurerAwaitingCumm
     */
    public Integer getNoOfInvoicesInsurerAwaitingCumm() {
        return noOfInvoicesInsurerAwaitingCumm;
    }

    /**
     * @param noOfInvoicesInsurerAwaitingCumm the noOfInvoicesInsurerAwaitingCumm to set
     */
    public void setNoOfInvoicesInsurerAwaitingCumm(Integer noOfInvoicesInsurerAwaitingCumm) {
        this.noOfInvoicesInsurerAwaitingCumm = noOfInvoicesInsurerAwaitingCumm;
    }

    /**
     * @return the valOfInvoicesInsurerAwaitingCumm
     */
    public BigDecimal getValOfInvoicesInsurerAwaitingCumm() {
        return valOfInvoicesInsurerAwaitingCumm;
    }

    /**
     * @param valOfInvoicesInsurerAwaitingCumm the valOfInvoicesInsurerAwaitingCumm to set
     */
    public void setValOfInvoicesInsurerAwaitingCumm(BigDecimal valOfInvoicesInsurerAwaitingCumm) {

        if(valOfInvoicesInsurerAwaitingCumm == null)
        this.valOfInvoicesInsurerAwaitingCumm = new BigDecimal(0.00);
        else
        this.valOfInvoicesInsurerAwaitingCumm = valOfInvoicesInsurerAwaitingCumm;
    }

    /**
     * @return the noOfInvoicesApprovedByBusinessCumm
     */
    public Integer getNoOfInvoicesApprovedByBusinessCumm() {
        return noOfInvoicesApprovedByBusinessCumm;
    }

    /**
     * @param noOfInvoicesApprovedByBusinessCumm the noOfInvoicesApprovedByBusinessCumm to set
     */
    public void setNoOfInvoicesApprovedByBusinessCumm(Integer noOfInvoicesApprovedByBusinessCumm) {
        this.noOfInvoicesApprovedByBusinessCumm = noOfInvoicesApprovedByBusinessCumm;
    }

    /**
     * @return the valOfInvoicesApprovedByBusinessCumm
     */
    public BigDecimal getValOfInvoicesApprovedByBusinessCumm() {
        return valOfInvoicesApprovedByBusinessCumm;
    }

    /**
     * @param valOfInvoicesApprovedByBusinessCumm the valOfInvoicesApprovedByBusinessCumm to set
     */
    public void setValOfInvoicesApprovedByBusinessCumm(BigDecimal valOfInvoicesApprovedByBusinessCumm) {
        if(valOfInvoicesApprovedByBusinessCumm == null)
        this.valOfInvoicesApprovedByBusinessCumm = new BigDecimal(0.00);
        else
        this.valOfInvoicesApprovedByBusinessCumm = valOfInvoicesApprovedByBusinessCumm;
    }

    /**
     * @return the noOfInvoicesEscalatedToHandlerCumm
     */
    public Integer getNoOfInvoicesEscalatedToHandlerCumm() {
        return noOfInvoicesEscalatedToHandlerCumm;
    }

    /**
     * @param noOfInvoicesEscalatedToHandlerCumm the noOfInvoicesEscalatedToHandlerCumm to set
     */
    public void setNoOfInvoicesEscalatedToHandlerCumm(Integer noOfInvoicesEscalatedToHandlerCumm) {
        this.noOfInvoicesEscalatedToHandlerCumm = noOfInvoicesEscalatedToHandlerCumm;
    }

    /**
     * @return the valOfInvoicesEscalatedToHandlerCumm
     */
    public BigDecimal getValOfInvoicesEscalatedToHandlerCumm() {
        return valOfInvoicesEscalatedToHandlerCumm;
    }

    /**
     * @param valOfInvoicesEscalatedToHandlerCumm the valOfInvoicesEscalatedToHandlerCumm to set
     */
    public void setValOfInvoicesEscalatedToHandlerCumm(BigDecimal valOfInvoicesEscalatedToHandlerCumm) {

        if(valOfInvoicesEscalatedToHandlerCumm == null)
        this.valOfInvoicesEscalatedToHandlerCumm = new BigDecimal(0.00);
        else
        this.valOfInvoicesEscalatedToHandlerCumm = valOfInvoicesEscalatedToHandlerCumm;
        
    }

    /**
     * @return the noOfInvoicesEscalatedToEngineerCumm
     */
    public Integer getNoOfInvoicesEscalatedToEngineerCumm() {
        return noOfInvoicesEscalatedToEngineerCumm;
    }

    /**
     * @param noOfInvoicesEscalatedToEngineerCumm the noOfInvoicesEscalatedToEngineerCumm to set
     */
    public void setNoOfInvoicesEscalatedToEngineerCumm(Integer noOfInvoicesEscalatedToEngineerCumm) {
        this.noOfInvoicesEscalatedToEngineerCumm = noOfInvoicesEscalatedToEngineerCumm;
    }

    /**
     * @return the valOfInvoicesEscalatedToEngineerCumm
     */
    public BigDecimal getValOfInvoicesEscalatedToEngineerCumm() {
        return valOfInvoicesEscalatedToEngineerCumm;
    }

    /**
     * @param valOfInvoicesEscalatedToEngineerCumm the valOfInvoicesEscalatedToEngineerCumm to set
     */
    public void setValOfInvoicesEscalatedToEngineerCumm(BigDecimal valOfInvoicesEscalatedToEngineerCumm) {

        if(valOfInvoicesEscalatedToEngineerCumm == null)
        this.valOfInvoicesEscalatedToEngineerCumm = new BigDecimal(0.00);
        else
        this.valOfInvoicesEscalatedToEngineerCumm = valOfInvoicesEscalatedToEngineerCumm;
        
    }

    /**
     * @return the noOfInvoicesReferredToEngineerCumm
     */
    public Integer getNoOfInvoicesReferredToEngineerCumm() {
        return noOfInvoicesReferredToEngineerCumm;
    }

    /**
     * @param noOfInvoicesReferredToEngineerCumm the noOfInvoicesReferredToEngineerCumm to set
     */
    public void setNoOfInvoicesReferredToEngineerCumm(Integer noOfInvoicesReferredToEngineerCumm) {
        this.noOfInvoicesReferredToEngineerCumm = noOfInvoicesReferredToEngineerCumm;
    }

    /**
     * @return the valOfInvoicesReferredToEngineerCumm
     */
    public BigDecimal getValOfInvoicesReferredToEngineerCumm() {
        return valOfInvoicesReferredToEngineerCumm;
    }

    /**
     * @param valOfInvoicesReferredToEngineerCumm the valOfInvoicesReferredToEngineerCumm to set
     */
    public void setValOfInvoicesReferredToEngineerCumm(BigDecimal valOfInvoicesReferredToEngineerCumm) {

        if(valOfInvoicesReferredToEngineerCumm == null)
        this.valOfInvoicesReferredToEngineerCumm = new BigDecimal(0.00);
        else
        this.valOfInvoicesReferredToEngineerCumm = valOfInvoicesReferredToEngineerCumm;
        
    }

    /**
     * @return the noOfInvoicesReferedToHandlerCumm
     */
    public Integer getNoOfInvoicesReferedToHandlerCumm() {
        return noOfInvoicesReferedToHandlerCumm;
    }

    /**
     * @param noOfInvoicesReferedToHandlerCumm the noOfInvoicesReferedToHandlerCumm to set
     */
    public void setNoOfInvoicesReferedToHandlerCumm(Integer noOfInvoicesReferedToHandlerCumm) {
        this.noOfInvoicesReferedToHandlerCumm = noOfInvoicesReferedToHandlerCumm;
    }

    /**
     * @return the valOfInvoicesReferedToHandlerCumm
     */
    public BigDecimal getValOfInvoicesReferedToHandlerCumm() {
        return valOfInvoicesReferedToHandlerCumm;
    }

    /**
     * @param valOfInvoicesReferedToHandlerCumm the valOfInvoicesReferedToHandlerCumm to set
     */
    public void setValOfInvoicesReferedToHandlerCumm(BigDecimal valOfInvoicesReferedToHandlerCumm) {
        if(valOfInvoicesReferedToHandlerCumm == null)
        this.valOfInvoicesReferedToHandlerCumm = new BigDecimal(0.00);
        else
        this.valOfInvoicesReferedToHandlerCumm = valOfInvoicesReferedToHandlerCumm;
        
    }

    /**
     * @return the noOfInvoicesCHODisputeCumm
     */
    public Integer getNoOfInvoicesCHODisputeCumm() {
        return noOfInvoicesCHODisputeCumm;
    }

    /**
     * @param noOfInvoicesCHODisputeCumm the noOfInvoicesCHODisputeCumm to set
     */
    public void setNoOfInvoicesCHODisputeCumm(Integer noOfInvoicesCHODisputeCumm) {
        this.noOfInvoicesCHODisputeCumm = noOfInvoicesCHODisputeCumm;
    }

    /**
     * @return the valOfInvoicesCHODisputeCumm
     */
    public BigDecimal getValOfInvoicesCHODisputeCumm() {
        return valOfInvoicesCHODisputeCumm;
    }

    /**
     * @param valOfInvoicesCHODisputeCumm the valOfInvoicesCHODisputeCumm to set
     */
    public void setValOfInvoicesCHODisputeCumm(BigDecimal valOfInvoicesCHODisputeCumm) {

        if(valOfInvoicesCHODisputeCumm == null)
        this.valOfInvoicesCHODisputeCumm = new BigDecimal(0.00);
        else
        this.valOfInvoicesCHODisputeCumm = valOfInvoicesCHODisputeCumm;
        
    }

    /**
     * @return the noOfInvoicesApprovedAwaitingPayCumm
     */
    public Integer getNoOfInvoicesApprovedAwaitingPayCumm() {
        return noOfInvoicesApprovedAwaitingPayCumm;
    }

    /**
     * @param noOfInvoicesApprovedAwaitingPayCumm the noOfInvoicesApprovedAwaitingPayCumm to set
     */
    public void setNoOfInvoicesApprovedAwaitingPayCumm(Integer noOfInvoicesApprovedAwaitingPayCumm) {
        this.noOfInvoicesApprovedAwaitingPayCumm = noOfInvoicesApprovedAwaitingPayCumm;
    }

    /**
     * @return the valOfInvoicesApprovedAwaitingPayCumm
     */
    public BigDecimal getValOfInvoicesApprovedAwaitingPayCumm() {
        return valOfInvoicesApprovedAwaitingPayCumm;
    }

    /**
     * @param valOfInvoicesApprovedAwaitingPayCumm the valOfInvoicesApprovedAwaitingPayCumm to set
     */
    public void setValOfInvoicesApprovedAwaitingPayCumm(BigDecimal valOfInvoicesApprovedAwaitingPayCumm) {


        if(valOfInvoicesApprovedAwaitingPayCumm == null)
        this.valOfInvoicesApprovedAwaitingPayCumm = new BigDecimal(0.00);
        else
        this.valOfInvoicesApprovedAwaitingPayCumm = valOfInvoicesApprovedAwaitingPayCumm;
        
    }

    public Integer getNoOfInvoicesUnassignedCumm() {
        return noOfInvoicesUnassignedCumm;
    }

    public void setNoOfInvoicesUnassignedCumm(Integer noOfInvoicesUnassignedCumm) {
        this.noOfInvoicesUnassignedCumm = noOfInvoicesUnassignedCumm;
    }

    public BigDecimal getValOfInvoicesUnassignedCumm() {
        return valOfInvoicesUnassignedCumm;
    }

    public void setValOfInvoicesUnassignedCumm(BigDecimal valOfInvoicesUnassignedCumm) {
        this.valOfInvoicesUnassignedCumm = valOfInvoicesUnassignedCumm;
    }

}
