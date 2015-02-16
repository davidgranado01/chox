package idas.chox.data.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.DetachedCriteria;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.BreBandOrganisation;
import idas.chox.core.services.BreBandOrganisationService;

public class BreBandOrganisationServiceImpl extends SecureDataService implements BreBandOrganisationService {
    private static final Logger LOG = LoggerFactory.getLogger(BreBandOrganisationServiceImpl.class);

    
    @Override
    public boolean isBreBandOccupied(int breBandId) {

        boolean isExist = false;

        List<BreBandOrganisation> objects;
        objects = getBreBandChorganisationsByBreBandId(breBandId);

        if (objects.size() > 0) {
            isExist = true;
        }

        return isExist;
    }

    
    @Override
    public List<BreBandOrganisation> getBreBandChorganisationsByChoOrgId(int choOrgid) {
        DetachedCriteria criteria = DetachedCriteria.forClass(BreBandOrganisation.class);
        criteria.add(Restrictions.eq("chorganisation.id", choOrgid));
        return findByCriteria(criteria);
    }

    
    @Override
    public List<BreBandOrganisation> getBreBandChorganisationsByBreBandId(int bandId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(BreBandOrganisation.class);
        criteria.add(Restrictions.eq("breBand.id", bandId));
        return findByCriteria(criteria);
    }

    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void deleteBreBandOrganisationByChorganisationId(int chorganisationId, int insurerId) {

        List<BreBandOrganisation> objects;
        DetachedCriteria criteria = DetachedCriteria.forClass(BreBandOrganisation.class);
        criteria.add(Restrictions.eq("chorganisation.id", chorganisationId));
        objects = findByCriteria(criteria);

        for (BreBandOrganisation object : objects) {
            if (object.getBreBand().getInsurer().getId() == insurerId) {
                delete(object);
            }
        }
    }

    
    @Override
    public boolean deleteBreBandOrganisationByBandId(int bandId) {

        boolean bFlag = false;

        try {
            List<BreBandOrganisation> objects;
            DetachedCriteria criteria = DetachedCriteria.forClass(BreBandOrganisation.class);
            criteria.add(Restrictions.eq("breBand.id", bandId));
            objects = findByCriteria(criteria);

            for (BreBandOrganisation object : objects) {
                delete(object);
            }

            bFlag = true;

        } catch (Exception ex) {
            LOG.error("Error Deleting BRE Band Orb by band id={}: {}", bandId, ex.getMessage());
        }

        return bFlag;
    }

    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void deleteBreBandOrganisation(BreBandOrganisation breBandOrganisation) {
        delete(breBandOrganisation);
    }

    
    @Override
    public BreBandOrganisation getBreBandOrganisation(int id) {
        return (BreBandOrganisation) get(BreBandOrganisation.class, id);
    }

    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void saveBreBandOrganisation(BreBandOrganisation breBandOrganisation) {
        save(breBandOrganisation);
    }
}
