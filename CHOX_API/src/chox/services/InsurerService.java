/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.Claim;
import org.w3c.dom.Element;
import chox.model.Insurer;
import chox.model.VehicleClassCelling;
import java.util.List;

public interface InsurerService {
    public boolean isInsurerNameExist(String s);
    Insurer getInsurerByName(String s);
    Insurer getInsurerByNodeName(Element thisElement, String nodeName);
    public Insurer getObject(int id);
    public List<Insurer> getInsurers();
    public Insurer updateObject(Insurer object);
    public VehicleClassCelling getVechileClassCellingForClaim(Claim claim);
}
