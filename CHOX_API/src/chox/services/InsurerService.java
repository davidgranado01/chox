/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import org.w3c.dom.Element;
import chox.model.Insurer;
import java.util.List;

public interface InsurerService {
    
    
    Insurer getInsurerByName(String s);
    Insurer getInsurerByNodeName(Element thisElement, String nodeName);
    public Insurer getObject(int id);
    public List<Insurer> getInsurers();
    public void updateObject(Insurer object);
}
