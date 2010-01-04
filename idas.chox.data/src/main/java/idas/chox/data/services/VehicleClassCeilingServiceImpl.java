package idas.chox.data.services;

import idas.chox.core.model.VehicleClass;
import idas.chox.core.model.VehicleClassCeiling;
import idas.chox.core.services.VehicleClassCeilingService;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class VehicleClassCeilingServiceImpl extends SecureDataService implements VehicleClassCeilingService {

    public List<VehicleClass> getAvailableVehicleClassCeilingByInsurer(int insurerId) {

        // GET ALL VEHICLE CLASS
        DetachedCriteria vehicleClassCirteria = DetachedCriteria.forClass(VehicleClass.class);


        // GET ALL  VEHICLE CLASS ASSIGNED TO INSURER
        DetachedCriteria vehicleClassCeilingCirteria = DetachedCriteria.forClass(VehicleClassCeiling.class);
        vehicleClassCeilingCirteria.add(Restrictions.eq("insurer.id", insurerId));
        vehicleClassCeilingCirteria.setProjection(Property.forName("vehicleClass.id"));

        vehicleClassCirteria.addOrder(Order.asc("name"));

        // FILTERED BY ASSIGNED  VEHICLE CLASS
        vehicleClassCirteria.add(Property.forName("id").notIn(vehicleClassCeilingCirteria));

        // RETURN SEARCH RESULT
        return findByCriteria(vehicleClassCirteria);

    }

    public List<VehicleClassCeiling> getSelectedVehicleClassCeilingByInsurer(int insurerId) {
        DetachedCriteria vehicleClassCeilingCirteria = DetachedCriteria.forClass(VehicleClassCeiling.class);
        vehicleClassCeilingCirteria.add(Restrictions.eq("insurer.id", insurerId));
        vehicleClassCeilingCirteria.addOrder(Order.asc("vehicleClass"));
        return findByCriteria(vehicleClassCeilingCirteria);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void saveVehicleClassCeiling(VehicleClassCeiling object) {
        save(object);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteVehicleClassCeiling(VehicleClassCeiling object) {
        delete(object);
    }

    public VehicleClassCeiling getVehicleClassCeiling(int id) {
        return (VehicleClassCeiling) get(VehicleClassCeiling.class, id);
    }
}
