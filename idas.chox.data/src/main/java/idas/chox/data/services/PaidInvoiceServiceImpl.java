package idas.chox.data.services;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import idas.chox.core.model.PaidInvoiceEntry;
import idas.chox.core.services.PaidInvoiceService;

/**
 *
 * @author john
 */
public class PaidInvoiceServiceImpl extends SecureDataService implements PaidInvoiceService {

    @Override
    public List<PaidInvoiceEntry> getPaidInvoiceEntries(String insurerName) {
        DetachedCriteria criteria = DetachedCriteria.forClass(PaidInvoiceEntry.class);
        criteria.add(Restrictions.eq("insurerName", insurerName));
        return findByCriteria(criteria);
    }
    
}
