/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.Solicitor;
import chox.model.Injury;

public interface SolicitorService {
    public void saveObjectForXMLUploader(final ClaimResult claimResult);
    public Solicitor getObject(int id);
    public void updateObject(Solicitor solicitor);
    public Solicitor getSolicitorByInjury(Injury injury);
}
