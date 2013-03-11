package idas.chox.core.services;

import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.core.model.VehicleHire;

public interface VehicleHireService {

    void saveVehicleHireForXMLUploader(final ClaimResult claimResult);

    VehicleHire getVehicleHire(int vehicleHireId);

    void saveVehicleHire(VehicleHire vehicleHire);
}
