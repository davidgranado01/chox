/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.services;

import chox.model.ThirdParty;
import chox.model.XMLParseResult;

public interface ThirdPartyService {

    XMLParseResult saveThirdPartyForXMLUploader(XMLParseResult xmlParseResult);

    public ThirdParty getObject(int id);

    public void updateObject(ThirdParty thirdParty);
}