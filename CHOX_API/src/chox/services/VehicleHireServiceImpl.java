package chox.services;

import chox.model.*;

public class VehicleHireServiceImpl extends SecureDataService implements VehicleHireService {

    public void saveObjectForXMLUploader(final XMLParseResult xmlParseResult) {

        if ((xmlParseResult.getClaim().getVehicleHire()) != null) {
            getHibernateTemplate().saveOrUpdate(xmlParseResult.getClaim().getVehicleHire());

        }
    }

    public VehicleHire getObject(int id) {
        return (VehicleHire) get(VehicleHire.class, id);
    }

    public void updateObject(VehicleHire vehicleHire) {

        save(vehicleHire);
    }
}
