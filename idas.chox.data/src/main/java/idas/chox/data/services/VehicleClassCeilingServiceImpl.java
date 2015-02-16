package idas.chox.data.services;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.VehicleClass;
import idas.chox.core.model.VehicleClassCeiling;
import idas.chox.core.services.VehicleClassCeilingService;

public class VehicleClassCeilingServiceImpl extends SecureDataService implements VehicleClassCeilingService {

    @Override
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

    @Override
    public List<VehicleClassCeiling> getSelectedVehicleClassCeilingByInsurer(int insurerId) {
        DetachedCriteria vehicleClassCeilingCirteria = DetachedCriteria.forClass(VehicleClassCeiling.class);
        vehicleClassCeilingCirteria.add(Restrictions.eq("insurer.id", insurerId));
        vehicleClassCeilingCirteria.addOrder(Order.asc("vehicleClass"));
        return findByCriteria(vehicleClassCeilingCirteria);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void saveVehicleClassCeiling(VehicleClassCeiling vehicleClassCeiling) {
        save(vehicleClassCeiling);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void deleteVehicleClassCeiling(VehicleClassCeiling vehicleClassCeiling) {
        delete(vehicleClassCeiling);
    }

    @Override
    public VehicleClassCeiling getVehicleClassCeiling(int vehicleClassCeilingId) {
        return (VehicleClassCeiling) get(VehicleClassCeiling.class, vehicleClassCeilingId);
    }
}
