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

public class BreBandOrganisationServiceImpl extends SecureDataService implements BreBandOrganisationService {

    public boolean isActiveChorganisationWithBand(int choOrgid) {

        boolean isExist = false;

        List<BreBandOrganisation> objects = new ArrayList<BreBandOrganisation>();

        try {

            List<BreBandOrganisation> brebandorganisations = getBreBandChorganisationsByChoOrgId(choOrgid);

            for (BreBandOrganisation obj : brebandorganisations) {

                objects.add(obj);

            }

        } catch (Throwable e) {
            e.printStackTrace();
        }

        if (objects.size() > 0) {
            isExist = true;
        }

        return isExist;
    }

    public boolean isActiveChorganisationWithBand(int choOrgid, int insurerId) {

        boolean isExist = false;

        List<BreBandOrganisation> objects = new ArrayList<BreBandOrganisation>();

        try {

            List<BreBandOrganisation> brebandorganisations = getBreBandChorganisationsByChoOrgId(choOrgid);

            for (BreBandOrganisation obj : brebandorganisations) {
                if (obj.getBreBand().getInsurer().getId() == insurerId) {
                    objects.add(obj);
                }
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }

        if (objects.size() > 0) {
            isExist = true;
        }

        return isExist;
    }

    public boolean isBreBandOccupied(int bandId) {

        boolean isExist = false;

        List<BreBandOrganisation> objects = new ArrayList<BreBandOrganisation>();

        try {

            objects = getBreBandChorganisationsByBreBandId(bandId);

        } catch (Throwable e) {
            e.printStackTrace();
        }

        if (objects.size() > 0) {
            isExist = true;
        }

        return isExist;
    }

    public List<BreBandOrganisation> getBreBandChorganisationsByChoOrgId(int choOrgid) {

        List<BreBandOrganisation> objects = new ArrayList<BreBandOrganisation>();

        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(BreBandOrganisation.class);
            criteria.add(Restrictions.eq("chorganisation.id", choOrgid));
            objects = findByCriteria(criteria);

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return objects;

    }

    public List<BreBandOrganisation> getBreBandChorganisationsByBreBandId(int bandId) {

        List<BreBandOrganisation> objects = new ArrayList<BreBandOrganisation>();

        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(BreBandOrganisation.class);
            criteria.add(Restrictions.eq("breBand.id", bandId));
            objects = findByCriteria(criteria);

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return objects;

    }

    public boolean deleteBreBandOrganisationByChorganisationId(int chorganisationId, int insurerId) {

        boolean bFlag = false;

        try {

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

            bFlag = true;

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return bFlag;
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

    public void deleteObject(BreBandOrganisation object) {

        try {
            delete(object);
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    public BreBandOrganisation getObject(int id) {
        return (BreBandOrganisation) get(BreBandOrganisation.class, id);
    }

    public void updateObject(BreBandOrganisation object) {
        save(object);
    }
}
