/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.ChoBand;
import java.util.List;

public interface ChoBandService {
    ChoBand getChoBandByChorganisationId(int Id);
    ChoBand getChoBandByChorganisationIdAndInsurerId(int orgId, int insurerId);
    public ChoBand getDummyChoBand();
    public List<ChoBand> getInsurerChoBand();
    public List<ChoBand> getInsurerChoBand(int insurerId);
    public ChoBand getObject(int id);
    public void updateObject(ChoBand object);
    public boolean deleteObject(ChoBand object);
    public boolean isChoBandOccupied(ChoBand object);
    public boolean isChoBandNameExist(ChoBand object);
}
