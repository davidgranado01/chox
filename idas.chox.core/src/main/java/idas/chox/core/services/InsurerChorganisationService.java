/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.core.services;

import idas.chox.core.model.InsurerChorganisation;
import java.util.List;

public interface InsurerChorganisationService {

    // ACTION
    public boolean updateObject(InsurerChorganisation object);

    public void deleteObject(InsurerChorganisation object);

    public boolean triggerStatus(InsurerChorganisation object);

    // RETREIVE - OBJECT
    public InsurerChorganisation getObject(int insurerId, int chorganisationId);

    public InsurerChorganisation getObject(int id);

    // RETREIVE - LISTING
    public List<InsurerChorganisation> getObjects(Integer insurerId, Integer chorganisationId);

    // VALIDATION
    public boolean isInactiveObjectExist(int insurerId, int chorganisationId);

    public boolean isActiveObjectExist(int insurerId, int chorganisationId);
}
