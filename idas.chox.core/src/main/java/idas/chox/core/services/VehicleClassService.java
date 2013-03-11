package idas.chox.core.services;

import java.util.List;
import org.w3c.dom.Element;
import idas.chox.core.model.VehicleClass;

public interface VehicleClassService {

    VehicleClass getVehicleClassByName(String vehicleClassName);

    List getAllVehicleClass();

    VehicleClass getVehicleClassByNodeName(Element thisElement, String nodeName);

    VehicleClass getVehicleClass(int vehicleClassId);
}
