package idas.chox.data.services;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.ReasonOfRejection;
import idas.chox.core.model.ReasonOfRejectionTemplate;
import idas.chox.core.services.ReasonOfRejectionService;
import idas.chox.core.services.ReasonOfRejectionTemplateService;
import idas.chox.core.util.DateHelper;

public class ReasonOfRejectionServiceImpl  extends SecureDataService implements ReasonOfRejectionService {

	ReasonOfRejectionTemplateService reasonOfRejectionTemplateService;
    
    @Override
    public ReasonOfRejection getReasonOfRejection(int id) {
        return (ReasonOfRejection) get(ReasonOfRejection.class, id);
    }

    @Override
    public List<ReasonOfRejection> getInsurerReasonsOfRejection(int insurerId, String type, ClaimType activeType, Boolean status,  Boolean restricted) {
        DetachedCriteria criteria = DetachedCriteria.forClass(ReasonOfRejection.class);
        if(type != null) {
            criteria.add(Restrictions.eq("type", type));
        }
        if(status != null && activeType != null) {
            criteria.add(Restrictions.eq(activeReasonOfRejectionClaimType(activeType), status));
        }
        if(restricted != null) {
            criteria.add(Restrictions.eq("restricted", restricted));
        }
        criteria.add(Restrictions.eq("insurer.id", insurerId));
        criteria.addOrder(Order.asc("rorName"));
        return findByCriteria(criteria);
    }

    @Override
    public int getInvoiceLiabilityDisputeReasonId(int insurerId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(ReasonOfRejection.class);
        criteria.add(Restrictions.eq("insurer.id", insurerId));
        criteria.add(Restrictions.eq("rorName", "Liability Dispute")).add(Restrictions.eq("type", "Invoice"));
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
            reasonOfRejection.setRorName(ror.getName());
            reasonOfRejection.setRestricted(ror.isRestricted());
            reasonOfRejection.setType(ror.getType());
            reasonOfRejection.setGtaActive(ror.isGtaActive());
            reasonOfRejection.setCollaborationActive(ror.isCollaborationActive());
            reasonOfRejection.setTpiActive(ror.isTpiActive());
            reasonOfRejection.setSubscriberActive(ror.isSubscriberActive());
            reasonOfRejection.setFixedFeeActive(ror.isFixedFeeActive());
            reasonOfRejection.setInsurerUploadActive(ror.isInsurerUploadActive());
            reasonOfRejection.setInsurerVsInsurerActive(ror.isInsurerVsInsurerActive());
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

    @Override
    public boolean isSubscriberClaimRejected(ReasonOfRejection reasonOfRejection) {
        if (reasonOfRejection.getRorName().equals("Subscriber - Indemnity Issues")
                || reasonOfRejection.getRorName().equals("Subscriber - Fraud Issues")) {
            return true;
        }
        
        return false;
    }

    private String activeReasonOfRejectionClaimType(ClaimType ct){
        if(ClaimType.isGTA(ct)) {
            return "gtaActive";
        }
        else if(ClaimType.isCollaborationProtocol(ct)) {
            return "collaborationActive";
        }
        else if(ClaimType.isInsurerUpload(ct)) {
            return "insurerUploadActive";
        }
        else if(ClaimType.isInsurerVsInsurer(ct)) {
            return "insurerVsInsurerActive";
        }
        else if(ClaimType.isTPI(ct)) {
            return "tpiActive";
        }
        else if(ClaimType.isSubscriber(ct)) {
            return "subscriberActive";
        } else if(ClaimType.isFixedFee(ct)) {
            return "fixedFeeActive";
        }
        return null;
    }
    
}
