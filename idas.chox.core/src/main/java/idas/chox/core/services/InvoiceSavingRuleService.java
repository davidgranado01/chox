package idas.chox.core.services;

import java.util.List;
import idas.chox.core.model.Invoice;
import idas.chox.core.model.InvoiceSavingRule;

/**
 *
 * @author john
 */
public interface InvoiceSavingRuleService {
    public void saveInvoiceSavingRule(InvoiceSavingRule object);   
    public void deleteInvoiceSavingRule(InvoiceSavingRule object);   
    public List<InvoiceSavingRule> getInvoiceSavingRules(Invoice invoice);   
}
