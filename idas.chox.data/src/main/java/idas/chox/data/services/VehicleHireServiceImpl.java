package idas.chox.data.services;

import idas.chox.core.model.VehicleHire;
import idas.chox.core.services.VehicleHireService;
import idas.chox.core.xmlValidation.ClaimResult;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class VehicleHireServiceImpl extends SecureDataService implements VehicleHireService {

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void saveVehicleHireForXMLUploader(final ClaimResult claimResult) {

        if ((claimResult.getClaim().getVehicleHire()) != null) {
            getHibernateTemplate().saveOrUpdate(claimResult.getClaim().getVehicleHire());

        }
    }

    public VehicleHire getVehicleHire(int id) {
        return (VehicleHire) get(VehicleHire.class, id);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void saveVehicleHire(VehicleHire vehicleHire) {
        save(vehicleHire);
    }
}
