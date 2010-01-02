package idas.chox.core.services;

import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.core.model.Incident;
import idas.chox.core.model.Witness;

public interface WitnessService {

    public void saveWitnessForXMLUploader(final ClaimResult claimResult);

    public Witness getWitness(int witnessId);

    public void saveWitness(Witness Witness);

    public Witness getWitnessByIncident(Incident incident);
}
