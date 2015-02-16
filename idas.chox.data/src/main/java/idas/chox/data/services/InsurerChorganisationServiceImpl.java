package idas.chox.data.services;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.InsurerChorganisation;
import idas.chox.core.services.InsurerChorganisationService;

public class InsurerChorganisationServiceImpl extends SecureDataService implements InsurerChorganisationService {


    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void deleteInsurerChorganisation(InsurerChorganisation object) {
        delete(object);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
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

        return (InsurerChorganisation) getByCriteria(criteria);
    }

    @Override
    public boolean isMapped(int insurerId, int chorganisationId) {
        boolean bFlag = false;
        InsurerChorganisation object = getInsurerChorganisation(insurerId, chorganisationId);
        if (object != null) {
            bFlag = true;
        }
        return bFlag;
    }
}
