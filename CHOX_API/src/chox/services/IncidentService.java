/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.Incident;
import chox.model.XMLParseResult;

public interface IncidentService {
    public void saveObjectForXMLUploader(final XMLParseResult xmlParseResult);
    public void updateObject(Incident incident);
    public Incident getObject(int id);
}
