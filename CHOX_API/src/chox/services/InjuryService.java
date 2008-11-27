/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.Injury;
import chox.model.XMLParseResult;

public interface InjuryService {
    XMLParseResult saveInjuryForXMLUploader(XMLParseResult xmlParseResult);
    
   public Injury getInjury(int id);
   public void updateInjury(Injury injury);
}
