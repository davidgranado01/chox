package idas.chox.core.services;

import idas.chox.core.model.Insurer;
import idas.chox.core.model.VehicleClassCeiling;
import java.util.List;

public interface VehicleClassCeilingService {

    public List<VehicleClassCeiling> getVehicleClassCeilingByInsurer(Insurer insurer);

    public void updateObject(VehicleClassCeiling object);

    public void deleteObject(VehicleClassCeiling object);

    public VehicleClassCeiling getObject(int id);
}
