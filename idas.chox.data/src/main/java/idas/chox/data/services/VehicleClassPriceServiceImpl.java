package idas.chox.data.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.model.VehicleClass;
import idas.chox.core.model.VehicleClassPrice;
import idas.chox.core.services.VehicleClassPriceService;
import idas.chox.core.services.BreBandService;
import idas.chox.core.services.VehicleClassPriceSpecialRateService;

/**
 *
 * @author John
 */
public class VehicleClassPriceServiceImpl extends SecureDataService implements VehicleClassPriceService {

    private static final Logger LOG = LoggerFactory.getLogger(VehicleClassPriceServiceImpl.class);
    private BreBandService breBandService;
    private VehicleClassPriceSpecialRateService vehicleClassPriceSpecialRateService;

    public void setBreBandServiceService(BreBandService breBandService) {
        this.breBandService = breBandService;
    }

    public void setVehicleClassPriceSpecialRateService(VehicleClassPriceSpecialRateService vehicleClassPriceSpecialRateService) {
        this.vehicleClassPriceSpecialRateService = vehicleClassPriceSpecialRateService;
    }

    @Override
    public BigDecimal getPrice(VehicleClass vehicleClass, Date startDate, int insId, int choId) {

        if (breBandService.isSupplierRatesActivated(choId, insId)) {
            return vehicleClassPriceSpecialRateService.getPrice(vehicleClass, startDate, insId, choId);
        } else {


            DetachedCriteria criteria = DetachedCriteria.forClass(VehicleClassPrice.class);
            criteria.add(Restrictions.eq("vehicleClass.id", vehicleClass.getId()));
            criteria.add(Restrictions.le("startDate", startDate));
            criteria.addOrder(Order.desc("startDate"));
            criteria.addOrder(Order.desc("age"));

            List<VehicleClassPrice> vehicleClassPrices = null;
            try {
                vehicleClassPrices = this.findByCriteria(criteria);
            } catch (Exception ex) {
                LOG.error("Exception caught getting vehicle class price: {}", ex.getMessage());
                if (ex.getCause() != null) {
                    LOG.error("Caused by: {}", ex.getCause().getMessage());
                }
            }
            if (vehicleClassPrices == null || vehicleClassPrices.isEmpty()) {
                LOG.info("No vehicle prices found for class '{}' with start date '{}': returnin 0.0", vehicleClass.getName(), startDate);
                throw new IllegalArgumentException("No vehicle class price found for class '" + vehicleClass.getName() + "'");
            }
            return ((VehicleClassPrice) vehicleClassPrices.get(0)).getPrice();
        }
    }

    @Override
    public BigDecimal getPrice(VehicleClass vehicleClass, Date startDate, BigDecimal age, int insId, int choId) {

        if (breBandService.isSupplierRatesActivated(choId, insId)) {
            return vehicleClassPriceSpecialRateService.getPrice(vehicleClass, startDate, age, insId, choId);
        } else {

            DetachedCriteria criteria = DetachedCriteria.forClass(VehicleClassPrice.class);
            criteria.add(Restrictions.eq("vehicleClass.id", vehicleClass.getId()));
            criteria.add(Restrictions.le("startDate", startDate));
            criteria.add(Restrictions.ge("age", age));
            criteria.addOrder(Order.desc("startDate"));
            criteria.addOrder(Order.asc("age"));

            List<VehicleClassPrice> vehicleClassPrices = null;
            try {
                vehicleClassPrices = this.findByCriteria(criteria);
            } catch (Exception ex) {
                LOG.error("Exception caught getting vehicle class price: {}", ex.getMessage());
                if (ex.getCause() != null) {
                    LOG.error("Caused by: {}", ex.getCause().getMessage());
                }
            }
            if (vehicleClassPrices == null || vehicleClassPrices.isEmpty()) {
                LOG.info("No vehicle prices found for class '{}' with start date '{}': returning 0.0", vehicleClass.getName(), startDate);
                throw new IllegalArgumentException("No vehicle class price found for class '" + vehicleClass.getName() + "' at age " + age.setScale(2, BigDecimal.ROUND_HALF_UP));
            }
            LOG.debug("Returning price={} (from start date '{}' and age=" + age.setScale(2, BigDecimal.ROUND_HALF_UP).toString(), ((VehicleClassPrice) vehicleClassPrices.get(0)).getPrice(), ((VehicleClassPrice) vehicleClassPrices.get(0)).getStartDate());
            return ((VehicleClassPrice) vehicleClassPrices.get(0)).getPrice();
        }
    }
}
/* not being used by any class (if it is used please provide cho id and insurer id as method parameter)
@Override
public List<VehicleClassPrice> getPrices(VehicleClass vehicleClass, Date startDate) {
// LOG.debug("Getting price for vehicle class {} on date {}", vehicleClass.getName(), startDate);
DetachedCriteria criteria = DetachedCriteria.forClass(VehicleClassPrice.class);
criteria.createCriteria("vehicleClass").add(Restrictions.eq("id", vehicleClass.getId()));
//        criteria.add(Restrictions.eq("vehicleClass", vehicleClass.getId()));
criteria.add(Restrictions.le("startDate", startDate));
criteria.addOrder(Order.desc("startDate"));
criteria.addOrder(Order.asc("age"));

List<VehicleClassPrice> vehicleClassPrices = null;
try {
vehicleClassPrices = this.findByCriteria(criteria);
} catch (Exception ex) {
LOG.error("Exception caught getting vehicle class price: {}", ex.getMessage());
if (ex.getCause() != null) {
LOG.error("Caused by: {}", ex.getCause().getMessage());
}
}
if (vehicleClassPrices == null || vehicleClassPrices.isEmpty()) {
LOG.info("No vehicle prices found for class '{}' with start date '{}': returnin 0.0", vehicleClass.getName(), startDate);
throw new IllegalArgumentException("No vehicle class price found for class '" + vehicleClass.getName() + "'");
//            return BigDecimal.ZERO;
}
// LOG.debug("Found {} prices:", vehicleClassPrices.size());
//        for (Object vehicleClassPrice : vehicleClassPrices) {
//            LOG.debug("Date: {}, Price: {}", ((VehicleClassPrice)vehicleClassPrice).getStartDate().toString(), ((VehicleClassPrice)vehicleClassPrice).getPrice().toString());
//        }
// LOG.debug("Returning price={} (from start date '{}'", ((VehicleClassPrice)vehicleClassPrices.get(0)).getPrice(), ((VehicleClassPrice)vehicleClassPrices.get(0)).getStartDate());
return vehicleClassPrices;
} */
