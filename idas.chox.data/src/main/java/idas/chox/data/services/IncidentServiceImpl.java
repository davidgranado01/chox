package idas.chox.data.services;

import idas.chox.core.model.Incident;
import idas.chox.core.services.IncidentService;
import idas.chox.core.xmlValidation.ClaimResult;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class IncidentServiceImpl extends SecureDataService implements IncidentService {

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    public void saveIncidentForXMLUploader(final ClaimResult claimResult) {

        Incident incident = claimResult.getClaim().getIncident();

        if (incident != null) {
            super.getHibernateTemplate().saveOrUpdate(incident);
        }
    }

    public Incident getIncident(int id) {
        return (Incident) get(Incident.class, id);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    public void saveIncident(Incident incident) {
        save(incident);
    }
}
