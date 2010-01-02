package idas.chox.core.services;

import idas.chox.core.model.VehicleClass;
import java.util.List;
import org.w3c.dom.*;

public interface VehicleClassService {

    public VehicleClass getVehicleClassByName(String vehicleClassName);

    public List getAllVehicleClass();

    public VehicleClass getVehicleClassByNodeName(Element thisElement, String nodeName);

    public VehicleClass getVehicleClass(int vehicleClassId);
}
