package idas.chox.data.services;

import idas.chox.core.model.Incident;
import idas.chox.core.services.IncidentService;
import idas.chox.core.xmlValidation.ClaimResult;

public class IncidentServiceImpl extends SecureDataService implements IncidentService {

    public void saveObjectForXMLUploader(final ClaimResult claimResult) {

        Incident incident = claimResult.getClaim().getIncident();

        if (incident != null) {
            super.getHibernateTemplate().saveOrUpdate(incident);
        }
    }

    public Incident getObject(int id) {
        return (Incident) get(Incident.class, id);
    }

    public void updateObject(Incident incident) {
        save(incident);
    }
}
