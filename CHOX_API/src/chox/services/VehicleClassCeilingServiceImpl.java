package chox.services;

import chox.model.Insurer;
import chox.model.VehicleClassCeiling;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class VehicleClassCeilingServiceImpl extends SecureDataService implements VehicleClassCeilingService {

    public List<VehicleClassCeiling> getVehicleClassCeilingByInsurer(Insurer insurer)
    {
        List<VehicleClassCeiling> vehicleClassCeilings = null;

        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(VehicleClassCeiling.class);
            criteria.add(Restrictions.eq("insurer", insurer));
            criteria.addOrder(Order.asc("vehicleClass"));
            vehicleClassCeilings = findByCriteria(criteria);

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return vehicleClassCeilings;
    }

    public void updateObject(VehicleClassCeiling object) {
        save(object);
    }

    public void deleteObject(VehicleClassCeiling object){
        try {
            delete(object);
        } catch (Throwable e) {
           e.printStackTrace();
        }

    }

    public VehicleClassCeiling getObject(int id) {
        return (VehicleClassCeiling) get(VehicleClassCeiling.class, id);
    }
    
}
