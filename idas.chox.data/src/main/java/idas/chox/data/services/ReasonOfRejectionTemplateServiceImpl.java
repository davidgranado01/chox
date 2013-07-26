package idas.chox.data.services;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import idas.chox.core.model.ReasonOfRejectionTemplate;
import idas.chox.core.services.ReasonOfRejectionTemplateService;

public class ReasonOfRejectionTemplateServiceImpl extends SecureDataService implements ReasonOfRejectionTemplateService {

    @Override
    public ReasonOfRejectionTemplate getReasonOfRejectionTemplate(int id) {
        return (ReasonOfRejectionTemplate) get(ReasonOfRejectionTemplate.class, id);
    }

    @Override
    public List<ReasonOfRejectionTemplate> getAllReasonOfRejectionTemplate(String type, Boolean status, Boolean restricted) {
        DetachedCriteria criteria = DetachedCriteria.forClass(ReasonOfRejectionTemplate.class);
        if (type != null) {
            criteria.add(Restrictions.eq("type", type));
        }
        if (status != null) {
            criteria.add(Restrictions.eq("status", status));
        }
        if (restricted != null) {
            criteria.add(Restrictions.eq("restricted", restricted));
        }
        criteria.addOrder(Order.asc("id"));
        return findByCriteria(criteria);
    }

    @Override
    public int getDefaultInvoiceLiabilityDisputeReasonId() {
        DetachedCriteria criteria = DetachedCriteria.forClass(ReasonOfRejectionTemplate.class);
        criteria.add(Restrictions.eq("name", "Liability Dispute")).add(Restrictions.eq("type", "Invoice"));
        ReasonOfRejectionTemplate reason = (ReasonOfRejectionTemplate)getByCriteria(criteria);
        return reason.getId();
    }

    @Override
    public List<ReasonOfRejectionTemplate> getReasonOfRejectionTemplates() {

        DetachedCriteria criteria = DetachedCriteria.forClass(ReasonOfRejectionTemplate.class);
        criteria.addOrder(Order.asc("name"));
        return findByCriteria(criteria);
    }
}
