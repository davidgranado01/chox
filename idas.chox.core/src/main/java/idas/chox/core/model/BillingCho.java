package idas.chox.core.model;

import java.io.Serializable;

public class BillingCho extends Billing implements Serializable {

    private static final long serialVersionUID = 3749953848026124782L;
    private Chorganisation cho;

    public void setCho(Chorganisation cho) {
	this.cho = cho;
    }

    public Chorganisation getCho() {
	return cho;
    }
}
