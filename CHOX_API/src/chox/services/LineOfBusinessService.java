/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.LineOfBusiness;
import java.util.List;

public interface LineOfBusinessService {
    
    public List<LineOfBusiness> getInsurerLineOfBusiness(int insurerId);
    public LineOfBusiness getObject(int id);
    public boolean DeleteObject(LineOfBusiness object);
    public boolean updateObject(LineOfBusiness object);
    
}
