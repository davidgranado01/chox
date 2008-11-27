/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.Incident;
import chox.model.XMLParseResult;

public interface IncidentService {
    XMLParseResult saveIncidentForXMLUploader(XMLParseResult xmlParseResult);
    public void updateIncident(Incident incident);
    public Incident getIncident(int id);
}
