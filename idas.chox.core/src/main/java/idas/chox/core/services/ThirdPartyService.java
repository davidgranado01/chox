/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.core.services;

import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.core.model.ThirdParty;

public interface ThirdPartyService {

    public void saveObjectForXMLUploader(final ClaimResult claimResult);

    public ThirdParty getObject(int id);

    public void updateObject(ThirdParty thirdParty);
}
