package idas.chox.data.services;

import idas.chox.core.model.VehicleHire;
import idas.chox.core.services.VehicleHireService;
import idas.chox.core.xmlValidation.ClaimResult;

public class VehicleHireServiceImpl extends SecureDataService implements VehicleHireService {

    public void saveObjectForXMLUploader(final ClaimResult claimResult) {

        if ((claimResult.getClaim().getVehicleHire()) != null) {
            getHibernateTemplate().saveOrUpdate(claimResult.getClaim().getVehicleHire());

        }
    }

    public VehicleHire getObject(int id) {
        return (VehicleHire) get(VehicleHire.class, id);
    }

    
    public void updateObject(VehicleHire vehicleHire) {

        save(vehicleHire);
    }
}
