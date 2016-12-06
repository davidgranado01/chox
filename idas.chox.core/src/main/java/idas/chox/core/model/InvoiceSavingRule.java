package idas.chox.core.model;

import java.io.Serializable;

/**
 *
 * @author john
 */
public class InvoiceSavingRule extends Entity implements Serializable {
    private static int SAVING_0_25 = 1;
    private static int SAVING_25_50 = 2;
    private static int SAVING_50_100 = 3;
    private Invoice invoice;
    private String invoiceSavingRule;
    private int savingGroup;

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public String getInvoiceSavingRule() {
        return invoiceSavingRule;
    }

    public void setInvoiceSavingRule(String invoiceSavingRule) {
        this.invoiceSavingRule = invoiceSavingRule;
    }

    public int getSavingGroup() {
        return savingGroup;
    }

    public void setSavingGroup(int savingGroup) {
        this.savingGroup = savingGroup;
    }

}
