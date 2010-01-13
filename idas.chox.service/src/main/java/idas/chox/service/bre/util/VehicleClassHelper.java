package idas.chox.service.bre.util;

import idas.chox.core.model.VehicleClass;

public class VehicleClassHelper {

    public static boolean isVehicleClassValid(VehicleClass vehicleClass) {

        if (vehicleClass != null) {
            if (!vehicleClass.getName().toUpperCase().equalsIgnoreCase("UNATTACHED")) {
                return true;
            }
        }
        return false;
    }
}