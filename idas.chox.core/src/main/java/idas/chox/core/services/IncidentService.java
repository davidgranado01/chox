package idas.chox.core.services;

import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.core.model.Incident;

public interface IncidentService {

    public void saveIncidentForXMLUploader(final ClaimResult claimResult);

    public void saveIncident(Incident incident);

    public Incident getIncident(int incidentId);
}
