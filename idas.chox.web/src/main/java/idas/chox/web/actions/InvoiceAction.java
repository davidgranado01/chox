package idas.chox.web.actions;

import idas.chox.core.model.Invoice;
import idas.chox.service.security.ApplicationAccessibility;

public class InvoiceAction extends ClaimModelAction<Invoice> {

    @Override
    public Invoice loadModel() {

        Invoice invoice = claim.getInvoice();
        if (invoice != null) {
            return invoice;
        }
        return new Invoice();
    }

    @Override
    public String updateModel() {
        claim.setInvoice(model);

        return super.updateModel();
    }

    @Override
    String getTabName() {
        return ApplicationAccessibility.TAB_INVOICE_DETAIL;
    }
}
