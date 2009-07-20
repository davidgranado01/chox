/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.services;

import chox.model.ThirdParty;
import chox.xmlValidation.model.ClaimResult;

public interface ThirdPartyService {
    public void saveObjectForXMLUploader(final ClaimResult claimResult);
    public ThirdParty getObject(int id);
    public void updateObject(ThirdParty thirdParty);
}