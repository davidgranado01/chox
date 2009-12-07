/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.Insurer;
import chox.model.InsurerAlias;
import java.util.List;

public interface InsurerAliasService{
    public InsurerAlias getInsurerByAliasName(String s);
    public List<InsurerAlias> getInsurerAlias(int insurerId);
    public InsurerAlias getObject(int id);
    public boolean DeleteObject(InsurerAlias object);
    public boolean updateObject(InsurerAlias object);
    public boolean isInsurerAliasExist(int insurerId, String AliasName);
    public void createDefaultRecord(Insurer insurer);
}
