package idas.chox.core.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class BillingInsurer extends Billing implements Serializable {

    private static final long serialVersionUID = -7471266743453470103L;
    private Insurer insurer;
    private BigDecimal benefitValue;
    private BigDecimal benefitShare;
    private BigDecimal fixedTransactionFee;
    private boolean fixedTransaction;

    public BigDecimal getBenefitShare() {
        return benefitShare;
    }

    public void setBenefitShare(BigDecimal benefitShare) {
        this.benefitShare = benefitShare;
    }

    public BigDecimal getBenefitValue() {
        return benefitValue;
    }

    public void setBenefitValue(BigDecimal benefitValue) {
        this.benefitValue = benefitValue;
    }

    public BigDecimal getFixedTransactionFee() {
        return fixedTransactionFee;
    }

    public void setFixedTransactionFee(BigDecimal fixedTransactionFee) {
        this.fixedTransactionFee = fixedTransactionFee;
    }

    public boolean isFixedTransaction() {
        return fixedTransaction;
    }

    public void setFixedTransaction(boolean fixedTransaction) {
        this.fixedTransaction = fixedTransaction;
    }

    public void setInsurer(Insurer insurer) {
        this.insurer = insurer;
    }

    public Insurer getInsurer() {
        return insurer;
    }
}
