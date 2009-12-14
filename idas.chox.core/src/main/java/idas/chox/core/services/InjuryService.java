/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.core.services;

import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.core.model.Incident;
import idas.chox.core.model.Injury;

public interface InjuryService {

    public void saveObjectForXMLUploader(final ClaimResult claimResult);

    public Injury getObject(int id);

    public void updateObject(Injury injury);

    public Injury getInjuryByIncident(Incident incident);

    public Injury getObjectByIncidentId(Incident incident);
}
