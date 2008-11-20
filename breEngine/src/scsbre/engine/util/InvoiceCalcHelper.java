package scsbre.engine.util;

import java.math.BigDecimal;

import scsbre.model.IInvoiceInfo;

public class InvoiceCalcHelper {

    private IInvoiceInfo invoice;
    private BigDecimal vatRate;

    private InvoiceCalcHelper(IInvoiceInfo i, BigDecimal vatRate) {

        invoice = i;
        this.vatRate = vatRate;
    }

    public static InvoiceCalcHelper Create(IInvoiceInfo i, BigDecimal vatRate) {
        InvoiceCalcHelper calc = new InvoiceCalcHelper(i, vatRate);
        return calc;
    }

    /* calculation methods */
    public BigDecimal getCalculatedHireVat() {
        return invoice.getHireNet().multiply(vatRate);
    }

    public BigDecimal getCalculatedHireGross() {
        return getCalculatedHireVat().add(invoice.getHireNet());
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
        return getCalculatedTotalGross().add(invoice.getDiscount());
    }
}
