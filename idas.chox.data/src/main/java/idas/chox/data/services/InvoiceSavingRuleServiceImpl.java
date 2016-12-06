package idas.chox.data.services;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.Invoice;
import idas.chox.core.model.InvoiceSavingRule;
import idas.chox.core.services.InvoiceSavingRuleService;

/**
 *
 * @author john
 */
public class InvoiceSavingRuleServiceImpl  extends SecureDataService implements InvoiceSavingRuleService {
    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void saveInvoiceSavingRule(InvoiceSavingRule object) {
        save(object);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void deleteInvoiceSavingRule(InvoiceSavingRule object) {
        delete(object);
    }

    @Override
    public List<InvoiceSavingRule> getInvoiceSavingRules(Invoice invoice) {
        DetachedCriteria criteria = DetachedCriteria.forClass(InvoiceSavingRule.class);
        criteria.createCriteria("invoice").add(Restrictions.eq("id", invoice.getId()));
        return findByCriteria(criteria);
    }

}
