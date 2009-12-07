/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.BreBand;
import chox.model.Insurer;
import java.util.List;

public interface BreBandService {

    public BreBand getObject(int id);
    public void updateObject(BreBand object);
    public boolean deleteObject(BreBand object);
    public List<BreBand> getInsurerBreBand(int insurerId);
    BreBand getBreBandByChorganisationIdAndInsurerId(int orgId, int insurerId);
    public boolean isBreBandOccupied(BreBand object);
    public boolean isBreBandNameExist(BreBand object);
    public void createDefaultRecord(Insurer insurer);
}