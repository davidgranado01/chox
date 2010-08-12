package idas.chox.core.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class BillingCho extends Billing implements Serializable {

    private static final long serialVersionUID = 3749953848026124782L;
    private Chorganisation cho;
    private BigDecimal chargeRate;
    private int numberInvoicesSubmitted;
    private BigDecimal fixedTransactionFee;
    private boolean fixedTransaction;

    public BigDecimal getChargeRate() {
        return chargeRate;
    }

    public void setChargeRate(BigDecimal chargeRate) {
        this.chargeRate = chargeRate;
    }

    public int getNumberInvoicesSubmitted() {
        return numberInvoicesSubmitted;
    }

    public void setNumberInvoicesSubmitted(int numberInvoicesSubmitted) {
        this.numberInvoicesSubmitted = numberInvoicesSubmitted;
    }

    public void setCho(Chorganisation cho) {
	this.cho = cho;
    }

    public Chorganisation getCho() {
	return cho;
    }

    public boolean isFixedTransaction() {
        return fixedTransaction;
    }

    public void setFixedTransaction(boolean fixedTransaction) {
        this.fixedTransaction = fixedTransaction;
    }

    public BigDecimal getFixedTransactionFee() {
        return fixedTransactionFee;
    }

    public void setFixedTransactionFee(BigDecimal fixedTransactionFee) {
        this.fixedTransactionFee = fixedTransactionFee;
    }
}
