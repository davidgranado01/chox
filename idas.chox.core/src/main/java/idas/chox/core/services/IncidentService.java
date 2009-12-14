/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.core.services;

import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.core.model.Incident;

public interface IncidentService {

    public void saveObjectForXMLUploader(final ClaimResult claimResult);

    public void updateObject(Incident incident);

    public Incident getObject(int id);
}
