package idas.chox.web.actions;

import idas.chox.core.model.Claim;
import idas.chox.core.model.InvoiceOriginal;
import idas.chox.service.security.ApplicationAccessibility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvoiceOriginalAction extends ClaimModelAction<InvoiceOriginal> {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceAction.class);

    @Override
    public InvoiceOriginal loadModel() {

        LOG.debug("InvoiceOriginal LoadModel is called");

        InvoiceOriginal invoiceOriginal = claim.getInvoice_original();
        if (invoiceOriginal != null) {
            LOG.debug("InvoiceOriginal LoadModel is not null returning exsisting one");
            return invoiceOriginal;
        }
        LOG.debug("InvoiceOriginal LoadModel is null returning new one");
        return new InvoiceOriginal();
    }

   // @Override
    public String updateModel(Claim claim) {
        claim.setInvoice_original(model);
        LOG.debug("InvoiceOriginal is set in claim");
        return SUCCESS;
    }

    @Override
    String getTabName() {
        return ApplicationAccessibility.TAB_INVOICE_DETAIL;
    }
}
