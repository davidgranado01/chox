/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.Incident;
import chox.model.Witness;
import chox.model.XMLParseResult;import chox.xmlValidation.model.ClaimResult;
public interface WitnessService {
    public void saveObjectForXMLUploader(final ClaimResult claimResult);
    public Witness getObject(int id);
    public void updateObject(Witness Witness);
    public Witness getWitnessByIncident(Incident incident);
}
