package chox.services;

import chox.model.*;
import chox.xmlValidation.model.ClaimResult;

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
