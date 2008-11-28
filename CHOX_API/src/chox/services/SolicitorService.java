/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.Solicitor;
import chox.model.XMLParseResult;

public interface SolicitorService {
    XMLParseResult saveSolicitorForXMLUploader(XMLParseResult xmlParseResult);
    public Solicitor getObject(int id);
    public void updateObject(Solicitor solicitor);
}
