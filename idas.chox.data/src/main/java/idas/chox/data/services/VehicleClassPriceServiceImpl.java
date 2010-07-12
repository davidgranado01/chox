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

/**
 *
 * @author John
 */
public class VehicleClassPriceServiceImpl extends SecureDataService implements VehicleClassPriceService {
    private static final Logger LOG = LoggerFactory.getLogger(VehicleClassPriceServiceImpl.class);

    @Override
    public BigDecimal getPrice(VehicleClass vehicleClass, Date startDate) {
        LOG.debug("Getting price for vehicle class {} on date {}", vehicleClass.getName(), startDate);
        DetachedCriteria criteria = DetachedCriteria.forClass(VehicleClassPrice.class);
        criteria.createCriteria("vehicleClass").add(Restrictions.eq("id", vehicleClass.getId()));
//        criteria.add(Restrictions.eq("vehicleClass", vehicleClass.getId()));
        criteria.add(Restrictions.le("startDate", startDate));
        criteria.addOrder(Order.desc("startDate"));

        List vehicleClassPrices = null;
        try {
            vehicleClassPrices = this.findByCriteria(criteria);
        } catch (Exception ex) {
            LOG.error("Exception caught getting vehicle class price: {}", ex.getMessage());
            if (ex.getCause() != null)
                LOG.error("Caused by: {}", ex.getCause().getMessage());
        }
        if (vehicleClassPrices == null || vehicleClassPrices.size() == 0) {
            LOG.error("No vehicle prices found for class '{}' with start date '{}': returnin 0.0", vehicleClass.getName(), startDate);
            throw new IllegalArgumentException("No vehicle class price found for class '" + vehicleClass.getName() + "'");
//            return BigDecimal.ZERO;
        }
        LOG.debug("Found {} prices:", vehicleClassPrices.size());
//        for (Object vehicleClassPrice : vehicleClassPrices) {
//            LOG.debug("Date: {}, Price: {}", ((VehicleClassPrice)vehicleClassPrice).getStartDate().toString(), ((VehicleClassPrice)vehicleClassPrice).getPrice().toString());
//        }
        LOG.debug("Returning price={} (from start date '{}'", ((VehicleClassPrice)vehicleClassPrices.get(0)).getPrice(), ((VehicleClassPrice)vehicleClassPrices.get(0)).getStartDate());
        return ((VehicleClassPrice)vehicleClassPrices.get(0)).getPrice();
    }

}
