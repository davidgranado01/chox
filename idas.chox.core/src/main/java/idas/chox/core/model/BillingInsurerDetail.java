package idas.chox.core.model;

public class BillingInsurerDetail extends BillingDetail {

    @Override
    public BillingInsurer getBilling() {
        return (BillingInsurer) super.getBilling();
    }
}