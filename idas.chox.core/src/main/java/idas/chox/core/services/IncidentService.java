package idas.chox.core.services;

import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.core.model.Incident;

public interface IncidentService {

    void saveIncidentForXMLUploader(final ClaimResult claimResult);

    void saveIncident(Incident incident);

    Incident getIncident(int incidentId);
}
