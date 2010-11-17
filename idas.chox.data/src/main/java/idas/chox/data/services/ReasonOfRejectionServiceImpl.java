package idas.chox.data.services;

import idas.chox.core.model.ReasonOfRejection;
import idas.chox.core.services.ReasonOfRejectionService;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class ReasonOfRejectionServiceImpl extends SecureDataService implements ReasonOfRejectionService {

    @Override
    public ReasonOfRejection getReasonOfRejection(int id) {
        return (ReasonOfRejection) get(ReasonOfRejection.class, id);
    }

    @Override
    public List<ReasonOfRejection> getAllReasonOfRejection() {
        DetachedCriteria criteria = DetachedCriteria.forClass(ReasonOfRejection.class);
        criteria.addOrder(Order.asc("id"));
        return findByCriteria(criteria);
    }

    @Override
    public int getgetInvoiceLiabilityDisputeReasonId() {
        DetachedCriteria criteria = DetachedCriteria.forClass(ReasonOfRejection.class);
        criteria.add(Restrictions.eq("name", "Liability Dispute")).add(Restrictions.eq("type", "Invoice"));
        ReasonOfRejection reason = (ReasonOfRejection)getByCriteria(criteria);
        return reason.getId();
    }
}
