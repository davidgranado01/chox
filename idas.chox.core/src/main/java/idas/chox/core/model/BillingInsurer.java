package idas.chox.core.model;

import java.io.Serializable;

public class BillingInsurer extends Billing implements Serializable {

    private static final long serialVersionUID = -7471266743453470103L;
    private Insurer insurer;


    public void setInsurer(Insurer insurer) {
        this.insurer = insurer;
    }

    public Insurer getInsurer() {
        return insurer;
    }
}
