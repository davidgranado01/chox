package idas.chox.core.services;

import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.core.model.VehicleHire;

public interface VehicleHireService {

    public void saveVehicleHireForXMLUploader(final ClaimResult claimResult);

    public VehicleHire getVehicleHire(int vehicleHireId);

    public void saveVehicleHire(VehicleHire vehicleHire);
}
