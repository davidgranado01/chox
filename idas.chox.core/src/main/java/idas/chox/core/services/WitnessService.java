package idas.chox.core.services;

import idas.chox.core.model.Incident;
import idas.chox.core.model.Witness;

public interface WitnessService {

    Witness getWitness(int witnessId);

    void saveWitness(Witness Witness);

    Witness getWitnessByIncident(Incident incident);
}
