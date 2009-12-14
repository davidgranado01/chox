/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.core.services;

import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.core.model.Incident;
import idas.chox.core.model.Witness;

public interface WitnessService {

    public void saveObjectForXMLUploader(final ClaimResult claimResult);

    public Witness getObject(int id);

    public void updateObject(Witness Witness);

    public Witness getWitnessByIncident(Incident incident);
}
