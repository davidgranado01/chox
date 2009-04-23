/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.InsurerChorganisation;
import java.util.List;

public interface InsurerChorganisationService{
    
    public InsurerChorganisation getObject(int id);
    // public boolean DeleteObject(InsurerChorganisation object);
    public boolean triggerStatus(InsurerChorganisation object);
    public boolean updateObject(InsurerChorganisation object);
    public boolean isActiveInsurerChorganisationExist(int insurerId, int chorganisationId);
    public boolean isInactiveInsurerChorganisationExist(int insurerId, int chorganisationId);
    public List<InsurerChorganisation> getInsurerChorganisationByInsurer(int insurerId);
    public List<InsurerChorganisation> getInsurerChorganisationByChorganisation(int chorganisationId);
    public List<InsurerChorganisation> getInsurerChorganisation(int insurerId, int chorganisationId);
    public InsurerChorganisation getInsurerChorganisationObject(int insurerId, int chorganisationId);
    
}
