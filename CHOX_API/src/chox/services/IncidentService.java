/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.Incident;
import chox.xmlValidation.model.ClaimResult;

public interface IncidentService {
    public void saveObjectForXMLUploader(final ClaimResult claimResult);
    public void updateObject(Incident incident);
    public Incident getObject(int id);
}
