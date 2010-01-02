package idas.chox.data.services;

import idas.chox.core.model.VehicleHire;
import idas.chox.core.services.VehicleHireService;
import idas.chox.core.xmlValidation.ClaimResult;

public class VehicleHireServiceImpl extends SecureDataService implements VehicleHireService {

    public void saveVehicleHireForXMLUploader(final ClaimResult claimResult) {

        if ((claimResult.getClaim().getVehicleHire()) != null) {
            getHibernateTemplate().saveOrUpdate(claimResult.getClaim().getVehicleHire());

        }
    }

    public VehicleHire getVehicleHire(int id) {
        return (VehicleHire) get(VehicleHire.class, id);
    }

    public void saveVehicleHire(VehicleHire vehicleHire) {
        save(vehicleHire);
    }
}
