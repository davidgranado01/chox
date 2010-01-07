/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.data.services;

import idas.chox.core.model.BreBandOrganisation;
import idas.chox.core.services.BreBandOrganisationService;
import org.hibernate.criterion.Restrictions;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class BreBandOrganisationServiceImpl extends SecureDataService implements BreBandOrganisationService {
    
    public boolean isBreBandOccupied(int breBandId) {

        boolean isExist = false;

        List<BreBandOrganisation> objects = new ArrayList<BreBandOrganisation>();
        objects = getBreBandChorganisationsByBreBandId(breBandId);

        if (objects.size() > 0) {
            isExist = true;
        }

        return isExist;
    }

    public List<BreBandOrganisation> getBreBandChorganisationsByChoOrgId(int choOrgid) {
        System.out.println(">>>>>>>>>>> 02.1.1:"+choOrgid);
        DetachedCriteria criteria = DetachedCriteria.forClass(BreBandOrganisation.class);
        System.out.println(">>>>>>>>>>> 02.1.2");
        criteria.add(Restrictions.eq("chorganisation.id", choOrgid));
        return findByCriteria(criteria);
    }

    public List<BreBandOrganisation> getBreBandChorganisationsByBreBandId(int bandId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(BreBandOrganisation.class);
        criteria.add(Restrictions.eq("breBand.id", bandId));
        return findByCriteria(criteria);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteBreBandOrganisationByChorganisationId(int chorganisationId, int insurerId) {

        List<BreBandOrganisation> objects = new ArrayList<BreBandOrganisation>();
        DetachedCriteria criteria = DetachedCriteria.forClass(BreBandOrganisation.class);
        criteria.add(Restrictions.eq("chorganisation.id", chorganisationId));
        objects = findByCriteria(criteria);

        int iCount = 0;
        for (BreBandOrganisation object : objects) {
            if (object.getBreBand().getInsurer().getId() == insurerId) {
                iCount++;
                delete(object);
            }
        }
    }

    public boolean deleteBreBandOrganisationByBandId(int bandId) {

        boolean bFlag = false;

        try {

            List<BreBandOrganisation> objects = new ArrayList<BreBandOrganisation>();
            DetachedCriteria criteria = DetachedCriteria.forClass(BreBandOrganisation.class);
            criteria.add(Restrictions.eq("breBand.id", bandId));
            objects = findByCriteria(criteria);

            for (BreBandOrganisation object : objects) {
                delete(object);
            }

            bFlag = true;

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return bFlag;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteBreBandOrganisation(BreBandOrganisation breBandOrganisation) {
        delete(breBandOrganisation);
    }

    public BreBandOrganisation getBreBandOrganisation(int id) {
        return (BreBandOrganisation) get(BreBandOrganisation.class, id);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void saveBreBandOrganisation(BreBandOrganisation breBandOrganisation) {
        save(breBandOrganisation);
    }
}
