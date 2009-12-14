/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.core.services;

import idas.chox.core.model.Injury;
import idas.chox.core.model.Solicitor;
import idas.chox.core.xmlValidation.ClaimResult;

public interface SolicitorService {

    public void saveObjectForXMLUploader(final ClaimResult claimResult);

    public Solicitor getObject(int id);

    public void updateObject(Solicitor solicitor);

    public Solicitor getSolicitorByInjury(Injury injury);
}
