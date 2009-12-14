/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.core.services;

import idas.chox.core.model.Claim;
import org.w3c.dom.Element;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.VehicleClassCeiling;
import java.util.List;

public interface InsurerService {

    public boolean isInsurerNameExist(String s);

    Insurer getInsurerByName(String s);

    Insurer getInsurerByNodeName(Element thisElement, String nodeName);

    public Insurer getObject(int id);

    public List<Insurer> getInsurers();

    public Insurer updateObject(Insurer object);

    public VehicleClassCeiling getVechileClassCeilingForClaim(Claim claim);
}
