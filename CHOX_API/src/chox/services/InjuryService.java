/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.Incident;
import chox.model.Injury;
import chox.model.XMLParseResult;

public interface InjuryService {
   public void saveObjectForXMLUploader(XMLParseResult xmlParseResult);    
   public Injury getObject(int id);
   public void updateObject(Injury injury);
   public Injury getInjuryByIncident(Incident incident);
   public Injury getObjectByIncidentId(Incident incident);
}
