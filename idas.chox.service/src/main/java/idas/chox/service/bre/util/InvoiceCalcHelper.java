package idas.chox.service.bre.util;

import idas.chox.core.util.CalcHelper;
import idas.chox.core.model.Invoice;
import java.math.BigDecimal;
import java.util.Date;


public final class InvoiceCalcHelper {

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
        return invoice.getHireNet().multiply(vatRate).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getCalculatedHireVat(Date date) {
        return invoice.getHireNet().multiply(CalcHelper.getVatRate(date)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getCalculatedHireGross() {
        return getCalculatedHireVat().add(invoice.getHireNet()).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getCalculatedTotalLossVat() {
        return invoice.getTotalLossFeeNet().multiply(vatRate).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getCalculatedStorageRecoveryVat() {
        return invoice.getStorageRecoveryNet().multiply(vatRate).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getCalculatedEngineerFeeVat() {
        return invoice.getEngineerFeeNet().multiply(vatRate).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getCalculatedTotalLossGross() {
        return getCalculatedTotalLossVat().add(invoice.getTotalLossFeeNet()).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getCalculatedRepairVat(Date date) {
        return invoice.getRepairNet().multiply(CalcHelper.getVatRate(date)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getCalculatedRepairVat() {
        return invoice.getRepairNet().multiply(vatRate).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getCalculatedRepairGross() {
        return getCalculatedRepairVat().add(invoice.getRepairNet()).setScale(2, BigDecimal.ROUND_HALF_UP);
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
        return getCalculatedTotalNet().multiply(vatRate).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getCalculatedTotalGross() {
        return getCalculatedTotalNet().add(getCalculatedTotalVat()).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getCalculatedTotalToPay() {
        BigDecimal charges = invoice.getDiscount().add(invoice.getHirePenaltyCharge()).add(invoice.getRepairPenaltyCharge()).add(invoice.getInsurerDiscount()).add(invoice.getGtaDiscount());
        return getCalculatedTotalGross().add(charges).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
