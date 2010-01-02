package idas.chox.data.services;

import idas.chox.core.model.Insurer;
import idas.chox.core.model.VehicleClassCeiling;
import idas.chox.core.services.VehicleClassCeilingService;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class VehicleClassCeilingServiceImpl extends SecureDataService implements VehicleClassCeilingService {

    public List<VehicleClassCeiling> getVehicleClassCeilingByInsurer(Insurer insurer) {
        List<VehicleClassCeiling> vehicleClassCeilings = null;



        DetachedCriteria criteria = DetachedCriteria.forClass(VehicleClassCeiling.class);
        criteria.add(Restrictions.eq("insurer", insurer));
        criteria.addOrder(Order.asc("vehicleClass"));
        vehicleClassCeilings = findByCriteria(criteria);


        return vehicleClassCeilings;
    }

    public void saveVehicleClassCeiling(VehicleClassCeiling object) {
        save(object);
    }

    public void deleteVehicleClassCeiling(VehicleClassCeiling object) {

        delete(object);

    }

    public VehicleClassCeiling getVehicleClassCeiling(int id) {
        return (VehicleClassCeiling) get(VehicleClassCeiling.class, id);
    }
}
