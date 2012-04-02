package idas.chox.data.services;

import idas.chox.core.model.ReasonOfRejection;
import idas.chox.core.services.ReasonOfRejectionService;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class ReasonOfRejectionServiceImpl  extends SecureDataService implements ReasonOfRejectionService {

	
    @Override
    public ReasonOfRejection getReasonOfRejection(int id) {
        return (ReasonOfRejection) get(ReasonOfRejection.class, id);
    }

    @Override
    public List<ReasonOfRejection> getInsurerReasonsOfRejection(int insurerId, String type, Boolean status, Boolean restricted) {
        DetachedCriteria criteria = DetachedCriteria.forClass(ReasonOfRejection.class);
        if(type != null)
        	criteria.add(Restrictions.eq("type", type));
        if(status != null)
        	criteria.add(Restrictions.eq("status", status));
        if(restricted != null)
        	criteria.add(Restrictions.eq("restricted", restricted));
        criteria.add(Restrictions.eq("insurer.id", insurerId));
        criteria.addOrder(Order.asc("id"));
        return findByCriteria(criteria);
    }

    @Override
    public int getInvoiceLiabilityDisputeReasonId(int insurerId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(ReasonOfRejection.class);
        criteria.add(Restrictions.eq("insurer.id", insurerId));
        criteria.add(Restrictions.eq("name", "Liability Dispute")).add(Restrictions.eq("type", "Invoice"));
        ReasonOfRejection reason = (ReasonOfRejection)getByCriteria(criteria);
        return reason.getId();
    }

}
