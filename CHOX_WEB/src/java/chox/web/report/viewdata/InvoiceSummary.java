/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.report.viewdata;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

/**
 *
 * @author Emmanuel
 */
public class InvoiceSummary {

    private String choName;
    private BigInteger noInvoiceSubmitted;
    private BigDecimal totalInvoiceValue;
    private BigInteger noInvoicesPaid;
    private BigDecimal valueOfPaidInvoices;
    private BigDecimal averageInvoiceValue;
    private BigInteger noInvoiceAwaitingPayment;
    private BigDecimal invoiceAwaitingPaymentValue;
    private BigInteger noInvoicePending;
    private BigDecimal invoicePendingValue;
    private BigInteger noInvoiceWithdrawn;
    private BigDecimal invoiceWithdrawnValue;

    public static InvoiceSummary getObject(Map data) {
        InvoiceSummary result = new InvoiceSummary();
        result.setChoName((String)data.get("name".toLowerCase()));
        result.setNoInvoiceSubmitted((BigInteger)data.get("noInvoiceSubmitted".toLowerCase()));
        result.setTotalInvoiceValue((BigDecimal)data.get("totalInvoiceValue".toLowerCase()));
        result.setNoInvoicesPaid((BigInteger)data.get("noInvoicesPaid".toLowerCase()));
        result.setValueOfPaidInvoices((BigDecimal)data.get("valueOfPaidInvoices".toLowerCase()));
        //result.setAverageInvoiceValue((BigDecimal)data.get("averageInvoiceValue".toLowerCase()));
        result.setAverageInvoiceValue(BigDecimal.ZERO);
        result.setNoInvoiceAwaitingPayment((BigInteger)data.get("noInvoiceAwaitingPayment".toLowerCase()));
        result.setInvoiceAwaitingPaymentValue((BigDecimal) data.get("invoiceAwaitingPaymentValue".toLowerCase()));
        result.setNoInvoicePending((BigInteger)data.get("noInvoicePending".toLowerCase()));
        result.setInvoicePendingValue((BigDecimal) data.get("invoicePendingValue".toLowerCase()));
        result.setNoInvoiceWithdrawn((BigInteger)data.get("noInvoiceWithdrawn".toLowerCase()));
        result.setInvoiceWithdrawnValue((BigDecimal) data.get("invoiceWithdrawnValue".toLowerCase()));

        return result;
    }

    public BigInteger getNoInvoiceSubmitted() {
        return noInvoiceSubmitted;
    }

    public void setNoInvoiceSubmitted(BigInteger noInvoiceSubmitted) {
        this.noInvoiceSubmitted = noInvoiceSubmitted;
    }

    public BigDecimal getTotalInvoiceValue() {
        return totalInvoiceValue;
    }

    public void setTotalInvoiceValue(BigDecimal totalInvoiceValue) {
        this.totalInvoiceValue = totalInvoiceValue;
    }

    public BigInteger getNoInvoicesPaid() {
        return noInvoicesPaid;
    }

    public void setNoInvoicesPaid(BigInteger noInvoicesPaid) {
        this.noInvoicesPaid = noInvoicesPaid;
    }

    public BigDecimal getValueOfPaidInvoices() {
        return valueOfPaidInvoices;
    }

    public void setValueOfPaidInvoices(BigDecimal valueOfPaidInvoices) {
        this.valueOfPaidInvoices = valueOfPaidInvoices;
    }

    public BigDecimal getAverageInvoiceValue() {
        return averageInvoiceValue;
    }

    public void setAverageInvoiceValue(BigDecimal averageInvoiceValue) {
        this.averageInvoiceValue = averageInvoiceValue;
    }

    public BigInteger getNoInvoiceAwaitingPayment() {
        return noInvoiceAwaitingPayment;
    }

    public void setNoInvoiceAwaitingPayment(BigInteger noInvoiceAwaitingPayment) {
        this.noInvoiceAwaitingPayment = noInvoiceAwaitingPayment;
    }

    public BigDecimal getInvoiceAwaitingPaymentValue() {
        return invoiceAwaitingPaymentValue;
    }

    public void setInvoiceAwaitingPaymentValue(BigDecimal invoiceAwaitingPaymentValue) {
        this.invoiceAwaitingPaymentValue = invoiceAwaitingPaymentValue;
    }

    public BigInteger getNoInvoicePending() {
        return noInvoicePending;
    }

    public void setNoInvoicePending(BigInteger noInvoicePending) {
        this.noInvoicePending = noInvoicePending;
    }

    public BigDecimal getInvoicePendingValue() {
        return invoicePendingValue;
    }

    public void setInvoicePendingValue(BigDecimal invoicePendingValue) {
        this.invoicePendingValue = invoicePendingValue;
    }

    public BigInteger getNoInvoiceWithdrawn() {
        return noInvoiceWithdrawn;
    }

    public void setNoInvoiceWithdrawn(BigInteger noInvoiceWithdrawn) {
        this.noInvoiceWithdrawn = noInvoiceWithdrawn;
    }

    public BigDecimal getInvoiceWithdrawnValue() {
        return invoiceWithdrawnValue;
    }

    public void setInvoiceWithdrawnValue(BigDecimal invoiceWithdrawnValue) {
        this.invoiceWithdrawnValue = invoiceWithdrawnValue;
    }

    public String getChoName() {
        return choName;
    }

    public void setChoName(String choName) {
        this.choName = choName;
    }
}
