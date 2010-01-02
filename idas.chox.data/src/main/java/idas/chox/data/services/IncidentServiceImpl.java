package idas.chox.data.services;

import idas.chox.core.model.Incident;
import idas.chox.core.services.IncidentService;
import idas.chox.core.xmlValidation.ClaimResult;

public class IncidentServiceImpl extends SecureDataService implements IncidentService {

    public void saveIncidentForXMLUploader(final ClaimResult claimResult) {

        Incident incident = claimResult.getClaim().getIncident();

        if (incident != null) {
            super.getHibernateTemplate().saveOrUpdate(incident);
        }
    }

    public Incident getIncident(int id) {
        return (Incident) get(Incident.class, id);
    }

    public void saveIncident(Incident incident) {
        save(incident);
    }
}
