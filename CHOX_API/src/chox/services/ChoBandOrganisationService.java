/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.ChoBandOrganisation;
import java.util.List;

public interface ChoBandOrganisationService {

    public boolean isChoBandOccupied(int bandId);
    public ChoBandOrganisation getObject(int id);
    public void updateObject(ChoBandOrganisation object);
    public boolean deleteChoBandOrganisationByBandId(int bandId);


}
