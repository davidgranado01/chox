package idas.chox.data.services;

import idas.chox.core.model.Insurer;
import idas.chox.core.model.ReasonOfRejection;
import idas.chox.core.model.ReasonOfRejectionTemplate;
import idas.chox.core.services.ReasonOfRejectionService;
import idas.chox.core.services.ReasonOfRejectionTemplateService;
import idas.chox.core.util.DateHelper;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class ReasonOfRejectionServiceImpl  extends SecureDataService implements ReasonOfRejectionService {

	ReasonOfRejectionTemplateService reasonOfRejectionTemplateService;
    
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
        criteria.addOrder(Order.asc("name"));
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

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void createDefaultRecord(Insurer insurer) {
        List<ReasonOfRejectionTemplate> listOfRorTemplate = reasonOfRejectionTemplateService.getReasonOfRejectionTemplates();
        for(ReasonOfRejectionTemplate ror : listOfRorTemplate){
            ReasonOfRejection reasonOfRejection = new ReasonOfRejection();
            reasonOfRejection.setCreatedBy(ror.getCreatedBy());
            reasonOfRejection.setCreatedDate(DateHelper.getCurrentDate());
            reasonOfRejection.setDescription(ror.getDescription());
            reasonOfRejection.setInsurer(insurer);
            reasonOfRejection.setLastModifiedBy(ror.getLastModifiedBy());
            reasonOfRejection.setLastModifiedDate(DateHelper.getCurrentDate());
            reasonOfRejection.setName(ror.getName());
            reasonOfRejection.setRestricted(ror.isRestricted());
            reasonOfRejection.setStatus(ror.isStatus());
            reasonOfRejection.setType(ror.getType());
            reasonOfRejection.setVersion(0);
            saveReasonOfRejection(reasonOfRejection);
        }
    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void saveReasonOfRejection(ReasonOfRejection reasonOfRejection) {
        save(reasonOfRejection);
    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteReasonOfRejection(ReasonOfRejection reasonOfRejection) {
        delete(reasonOfRejection);
    }

    public void setReasonOfRejectionTemplateService(
            ReasonOfRejectionTemplateService reasonOfRejectionTemplateService) {
        this.reasonOfRejectionTemplateService = reasonOfRejectionTemplateService;
    }

}
