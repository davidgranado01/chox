/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.InsurerAllias;
import java.util.List;

public interface InsurerAlliasService{
    public InsurerAllias getInsurerByAlliasName(String s);
    public List<InsurerAllias> getInsurerAllias(int insurerId);
    public InsurerAllias getObject(int id);
    public boolean DeleteObject(InsurerAllias object);
    public boolean updateObject(InsurerAllias object);
    public boolean isInsurerAlliasExist(int insurerId, String AlliasName);
}
