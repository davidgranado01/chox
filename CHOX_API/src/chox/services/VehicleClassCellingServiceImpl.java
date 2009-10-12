package chox.services;

import chox.model.Insurer;
import chox.model.VehicleClassCelling;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class VehicleClassCellingServiceImpl extends SecureDataService implements VehicleClassCellingService {

    public List<VehicleClassCelling> getVehicleClassCellingByInsurer(Insurer insurer)
    {
        List<VehicleClassCelling> vehicleClassCellings = null;

        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(VehicleClassCelling.class);
            criteria.add(Restrictions.eq("insurer", insurer));
            criteria.addOrder(Order.asc("vehicleClass"));
            vehicleClassCellings = findByCriteria(criteria);

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return vehicleClassCellings;
    }

    public void updateObject(VehicleClassCelling object) {
        save(object);
    }

    public void deleteObject(VehicleClassCelling object){
        try {
            delete(object);
        } catch (Throwable e) {
           e.printStackTrace();
        }

    }

    public VehicleClassCelling getObject(int id) {
        return (VehicleClassCelling) get(VehicleClassCelling.class, id);
    }
    
}
