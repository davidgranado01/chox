package idas.chox.core.services;

import idas.chox.core.model.Incident;
import idas.chox.core.model.Witness;

public interface WitnessService {

    public Witness getWitness(int witnessId);

    public void saveWitness(Witness Witness);

    public Witness getWitnessByIncident(Incident incident);
}
