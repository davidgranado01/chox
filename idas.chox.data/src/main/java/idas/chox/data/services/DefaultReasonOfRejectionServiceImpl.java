package idas.chox.data.services;

import idas.chox.core.model.DefaultReasonOfRejection;
import idas.chox.core.services.DefaultReasonOfRejectionService;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class DefaultReasonOfRejectionServiceImpl extends SecureDataService implements DefaultReasonOfRejectionService {

    @Override
    public DefaultReasonOfRejection getDefaultReasonOfRejection(int id) {
        return (DefaultReasonOfRejection) get(DefaultReasonOfRejection.class, id);
    }

    @Override
    public List<DefaultReasonOfRejection> getAllDefaultReasonOfRejection(String type, Boolean status, Boolean restricted) {
        DetachedCriteria criteria = DetachedCriteria.forClass(DefaultReasonOfRejection.class);
        if(type != null)
        	criteria.add(Restrictions.eq("type", type));
        if(status != null)
        	criteria.add(Restrictions.eq("status", status));
        if(restricted != null)
        	criteria.add(Restrictions.eq("restricted", restricted));
        criteria.addOrder(Order.asc("id"));
        return findByCriteria(criteria);
    }

    @Override
    public int getDefaultInvoiceLiabilityDisputeReasonId() {
        DetachedCriteria criteria = DetachedCriteria.forClass(DefaultReasonOfRejection.class);
        criteria.add(Restrictions.eq("name", "Liability Dispute")).add(Restrictions.eq("type", "Invoice"));
        DefaultReasonOfRejection reason = (DefaultReasonOfRejection)getByCriteria(criteria);
        return reason.getId();
    }
}
