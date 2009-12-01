package chox.services;

import chox.model.Insurer;
import chox.model.VehicleClassCeiling;
import java.util.List;

public interface VehicleClassCeilingService {

    public List<VehicleClassCeiling> getVehicleClassCeilingByInsurer(Insurer insurer);
    public void updateObject(VehicleClassCeiling object);
    public void deleteObject(VehicleClassCeiling object);
    public VehicleClassCeiling getObject(int id);
}
