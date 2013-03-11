package idas.chox.core.services;

import java.util.List;

import idas.chox.core.model.VehicleClass;
import idas.chox.core.model.VehicleClassCeiling;

public interface VehicleClassCeilingService {

    List<VehicleClass> getAvailableVehicleClassCeilingByInsurer(int insurerId);

    List<VehicleClassCeiling> getSelectedVehicleClassCeilingByInsurer(int insurerId);

    void saveVehicleClassCeiling(VehicleClassCeiling vehicleClassCeiling);

    void deleteVehicleClassCeiling(VehicleClassCeiling vehicleClassCeiling);

    VehicleClassCeiling getVehicleClassCeiling(int vehicleClassCeilingId);
}
