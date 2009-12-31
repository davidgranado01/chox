package idas.chox.data.services;

import idas.chox.core.model.Incident;
import idas.chox.core.model.Injury;
import idas.chox.core.services.InjuryService;
import idas.chox.core.xmlValidation.ClaimResult;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.DetachedCriteria;

public class InjuryServiceImpl extends SecureDataService implements InjuryService {

    public Injury getInjuryByIncident(Incident incident) {

        List injuries = new ArrayList<Injury>();
        Injury injury = null;

        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Injury.class).add(Restrictions.eq("incident", incident));
            injuries = findByCriteria(criteria);
            if (injuries.size() > 0) {
                injury = (Injury) injuries.get(0);
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }




        return injury;
    }

    public void saveObjectForXMLUploader(final ClaimResult claimResult) {

        if ((claimResult.getInjuries()) != null) {
            for (Integer i = 0; i < (claimResult.getInjuries()).size(); i++) {
                save(((claimResult.getInjuries()).get(i)));
            }
        }

    }

    public Injury getObject(int id) {
        return (Injury) get(Injury.class, id);
    }

    public void updateObject(Injury injury) {
        save(injury);
    }

    public Injury getObjectByIncidentId(Incident incident) {
        Injury injury = null;

        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Injury.class).add(Restrictions.eq("incident", incident));
            injury = (Injury) getByCriteria(criteria);

        } catch (Throwable e) {
            e.printStackTrace();
        }
        return injury;
    }
}
