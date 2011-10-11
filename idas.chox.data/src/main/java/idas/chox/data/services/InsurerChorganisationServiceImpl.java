package idas.chox.data.services;

import idas.chox.core.model.InsurerChorganisation;
import idas.chox.core.services.BreBandOrganisationService;
import idas.chox.core.services.InsurerChorganisationService;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class InsurerChorganisationServiceImpl extends SecureDataService implements InsurerChorganisationService {

    private BreBandOrganisationService choBandOrganisationService;

    public void setChoBandOrganisationService(BreBandOrganisationService choBandOrganisationService) {
        this.choBandOrganisationService = choBandOrganisationService;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public void deleteInsurerChorganisation(InsurerChorganisation object) {
        delete(object);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public void saveInsurerChorganisation(InsurerChorganisation object) {
        save(object);
    }

    @Override
    public List<InsurerChorganisation> getInsurerChorganisations(Integer insurerId, Integer chorganisationId) {

        DetachedCriteria criteria = DetachedCriteria.forClass(InsurerChorganisation.class);

        if (insurerId != null && insurerId > 0) {
            criteria.add(Restrictions.eq("insurer.id", insurerId));
        }

        if (chorganisationId != null && chorganisationId > 0) {
            criteria.add(Restrictions.eq("chorganisation.id", chorganisationId));
        }

        criteria.add(Restrictions.eq("status", true));
        return findByCriteria(criteria);

    }

    @Override
    public List<InsurerChorganisation> getTpiActivatedInsurerChorganisations(Integer insurerId, Integer chorganisationId) {

        DetachedCriteria criteria = DetachedCriteria.forClass(InsurerChorganisation.class);

        if (insurerId != null && insurerId > 0) {
            criteria.add(Restrictions.eq("insurer.id", insurerId));
        }

        if (chorganisationId != null && chorganisationId > 0) {
            criteria.add(Restrictions.eq("chorganisation.id", chorganisationId));
        }

        criteria.add(Restrictions.eq("status", true));
        criteria.add(Restrictions.eq("thirdPartyInterventionActivated", true));
        return findByCriteria(criteria);

    }

    @Override
    public InsurerChorganisation getInsurerChorganisation(int insurerChorganisationId) {
        return (InsurerChorganisation) get(InsurerChorganisation.class, insurerChorganisationId);
    }

    @Override
    public InsurerChorganisation getInsurerChorganisation(int insurerId, int chorganisationId) {

        DetachedCriteria criteria = DetachedCriteria.forClass(InsurerChorganisation.class);
        criteria.add(Restrictions.eq("insurer.id", insurerId));
        criteria.add(Restrictions.eq("chorganisation.id", chorganisationId));
        criteria.add(Restrictions.eq("status", true));

        return (InsurerChorganisation) getByCriteria(criteria);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public boolean triggerStatus(InsurerChorganisation object) {

        boolean bFlag = false;


        object.setStatus(!object.isStatus());
        save(object);

        bFlag = true;

        if (!object.isStatus()) {
            choBandOrganisationService.deleteBreBandOrganisationByChorganisationId(object.getChorganisation().getId(), object.getInsurer().getId());
        }

        return bFlag;
    }

    @Override
    public boolean isInactiveObjectExist(int insurerId, int chorganisationId) {
        boolean bFlag = false;
        InsurerChorganisation object = getInsurerChorganisation(insurerId, chorganisationId);
        if (object != null) {
            if (!object.isStatus()) {
                bFlag = true;
            }
        }
        return bFlag;
    }

    @Override
    public boolean isActiveObjectExist(int insurerId, int chorganisationId) {
        boolean bFlag = false;
        InsurerChorganisation object = getInsurerChorganisation(insurerId, chorganisationId);
        if (object != null) {
            bFlag = object.isStatus();
        }
        return bFlag;
    }
}
