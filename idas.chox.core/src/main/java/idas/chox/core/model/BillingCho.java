package idas.chox.core.model;

import java.io.Serializable;

public class BillingCho extends Billing implements Serializable {

    private static final long serialVersionUID = 3749953848026124782L;
    private Chorganisation cho;
    private int numberInvoicesSubmitted;
    private int numberPaymentsReceived;

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

    public int getNumberPaymentsReceived() {
        return numberPaymentsReceived;
    }

    public void setNumberPaymentsReceived(int numberPaymentsReceived) {
        this.numberPaymentsReceived = numberPaymentsReceived;
    }
}
