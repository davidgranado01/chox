package idas.chox.core.services;

import java.util.List;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ProtocolVehicleClassCeiling;
import idas.chox.core.model.VehicleClass;

public interface ProtocolVehicleClassCeilingService {

    List<VehicleClass> getAvailableProtocolVehicleClassCeilingByBreBand(int breBandId);

    List<ProtocolVehicleClassCeiling> getSelectedProtocolVehicleClassCeilingByBreBand(int breBandId);

    void saveProtocolVehicleClassCeiling(ProtocolVehicleClassCeiling protocolVehicleClassCeiling);

    void deleteProtocolVehicleClassCeiling(ProtocolVehicleClassCeiling protocolVehicleClassCeiling);

    ProtocolVehicleClassCeiling getProtocolVehicleClassCeiling(int protocolVehicleClassCeilingId);
    
    ProtocolVehicleClassCeiling getProtocolVehicleClassCeilingByVehicleClass(int vehicleClassId, int breBandId);
    
    ProtocolVehicleClassCeiling getProtocolVechileClassCeilingForClaim(Claim claim);
}
