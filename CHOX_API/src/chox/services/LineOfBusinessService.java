/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.Insurer;
import chox.model.LineOfBusiness;
import java.util.List;

public interface LineOfBusinessService {
    
    public List<LineOfBusiness> getInsurerLineOfBusiness(int insurerId);
    public LineOfBusiness getObject(int id);
    public boolean DeleteObject(LineOfBusiness object);
    public boolean updateObject(LineOfBusiness object);
    public boolean isLineOfBusinessExist(int insurerId, String lineOfBusinessName);
    public void createDefaultRecord(Insurer insurer);
    
}
