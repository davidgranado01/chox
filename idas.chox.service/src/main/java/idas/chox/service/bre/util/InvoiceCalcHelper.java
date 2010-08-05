package idas.chox.service.bre.util;

import idas.chox.core.model.Invoice;
import java.math.BigDecimal;


public class InvoiceCalcHelper {

    private Invoice invoice;
    private BigDecimal vatRate;

    private InvoiceCalcHelper(Invoice i) {

        invoice = i;
        this.vatRate = CalcHelper.VAT_RATE;
    }

    public static InvoiceCalcHelper getInstance(Invoice i) {
        InvoiceCalcHelper calc = new InvoiceCalcHelper(i);
        return calc;
    }

    /* calculation methods */
    public BigDecimal getCalculatedHireVat() {
        return invoice.getHireNet().multiply(vatRate);
    }

    public BigDecimal getCalculatedHireGross() {
        return getCalculatedHireVat().add(invoice.getHireNet());
    }

    public BigDecimal getCalculatedTotalLossVat() {
        return invoice.getTotalLossFeeNet().multiply(vatRate);
    }

    public BigDecimal getCalculatedTotalLossGross() {
        return getCalculatedTotalLossVat().add(invoice.getTotalLossFeeNet());
    }

    public BigDecimal getCalculatedRepairVat() {
        return invoice.getRepairNet().multiply(vatRate);
    }

    public BigDecimal getCalculatedRepairGross() {
        return getCalculatedRepairVat().add(invoice.getRepairNet());
    }

    public BigDecimal getCalculatedTotalNet() {
        
        BigDecimal totalNet = BigDecimal.ZERO;

        totalNet = totalNet.add(invoice.getHireNet());
        totalNet = totalNet.add(invoice.getRepairNet());
        totalNet = totalNet.add(invoice.getEngineerFeeNet());
        totalNet = totalNet.add(invoice.getTotalLossFeeNet());
        totalNet = totalNet.add(invoice.getStorageRecoveryNet());
        totalNet = totalNet.add(invoice.getDeductionForClaimsHandlingFee());

        return totalNet;
    }

    public BigDecimal getCalculatedTotalVat() {
        return getCalculatedTotalNet().multiply(vatRate);
    }

    public BigDecimal getCalculatedTotalGross() {
        return getCalculatedTotalNet().add(getCalculatedTotalVat());
    }

    public BigDecimal getCalculatedTotalToPay() {
        BigDecimal charges = invoice.getDiscount().add(invoice.getHirePenaltyCharge()).add(invoice.getRepairPenaltyCharge());
        return getCalculatedTotalGross().add(charges);
    }
}
