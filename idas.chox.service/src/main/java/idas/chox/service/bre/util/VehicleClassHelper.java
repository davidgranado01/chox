package idas.chox.service.bre.util;

import idas.chox.core.model.VehicleClass;

public class VehicleClassHelper {

    public static boolean isVehicleClassValid(VehicleClass vehicleClass) {
        // Previously a vehicle class of 'Un/ATTACHED' was also consider to be
        // invalid. This was changed under bug#3083
        return vehicleClass != null;
    }
}