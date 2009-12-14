/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.core.services;

import idas.chox.core.model.VehicleClass;
import java.util.List;
import org.w3c.dom.*;

public interface VehicleClassService {

    public VehicleClass getVehicleClassByName(String s);

    public List getAllVehicleClass();

    public VehicleClass getVehicleClassByNodeName(Element thisElement, String nodeName);

    public VehicleClass getObject(int id);
}
