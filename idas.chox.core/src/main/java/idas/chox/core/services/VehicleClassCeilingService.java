package idas.chox.core.services;

import java.util.List;

import idas.chox.core.model.VehicleClass;
import idas.chox.core.model.VehicleClassCeiling;

public interface VehicleClassCeilingService {

    public List<VehicleClass> getAvailableVehicleClassCeilingByInsurer(int insurerId);

    public List<VehicleClassCeiling> getSelectedVehicleClassCeilingByInsurer(int insurerId);

    public void saveVehicleClassCeiling(VehicleClassCeiling vehicleClassCeiling);

    public void deleteVehicleClassCeiling(VehicleClassCeiling vehicleClassCeiling);

    public VehicleClassCeiling getVehicleClassCeiling(int vehicleClassCeilingId);
}
