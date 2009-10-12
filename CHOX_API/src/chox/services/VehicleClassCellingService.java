package chox.services;

import chox.model.Insurer;
import chox.model.VehicleClassCelling;
import java.util.List;

public interface VehicleClassCellingService {

    public List<VehicleClassCelling> getVehicleClassCellingByInsurer(Insurer insurer);
    public void updateObject(VehicleClassCelling object);
    public void deleteObject(VehicleClassCelling object);
    public VehicleClassCelling getObject(int id);
}
