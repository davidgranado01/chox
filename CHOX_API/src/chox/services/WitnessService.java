/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.Witness;
import chox.model.XMLParseResult;

public interface WitnessService {
    XMLParseResult saveWitnessForXMLUploader(XMLParseResult xmlParseResult);
    public Witness getObject(int id);
    public void updateObject(Witness Witness);
}
