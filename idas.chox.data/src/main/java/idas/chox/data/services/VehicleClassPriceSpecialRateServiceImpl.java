/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
import idas.chox.core.model.VehicleClassPriceSpecialRate;
import idas.chox.core.services.VehicleClassPriceSpecialRateService;

/**
 *
 * @author seeni
 */
public class VehicleClassPriceSpecialRateServiceImpl extends SecureDataService implements VehicleClassPriceSpecialRateService {

    private static final Logger LOG = LoggerFactory.getLogger(VehicleClassPriceSpecialRateServiceImpl.class);

    @Override
    public BigDecimal getPrice(VehicleClass vehicleClass, Date startDate, BigDecimal age, int insId, int choId) {

        if (startDate == null) {
            startDate = new Date();
            LOG.debug("No start date provided - using todays date");
        }
        LOG.debug(" generating query for  v.class name '{}' with start date '{}'", vehicleClass.getName(), startDate);
        LOG.debug(" and insurer id  '{}' with cho id '{}'", insId, choId);
        LOG.debug(" and age  '{}' ", age);
        DetachedCriteria criteria = DetachedCriteria.forClass(VehicleClassPriceSpecialRate.class);
        criteria.add(Restrictions.eq("vehicleClass.id", vehicleClass.getId()));
        criteria.add(Restrictions.eq("insurer.id", insId));
        criteria.add(Restrictions.eq("chorganisation.id", choId));
        criteria.add(Restrictions.le("startDate", startDate));
        criteria.add(Restrictions.ge("age", age));
        criteria.addOrder(Order.desc("startDate"));
        criteria.addOrder(Order.asc("age"));

        List<VehicleClassPriceSpecialRate> vehicleClassPricesSpecialRate = null;
        try {
            vehicleClassPricesSpecialRate = this.findByCriteria(criteria);
        } catch (Exception ex) {
            LOG.debug("Exception caught getting vehicle class special price: {}", ex.getMessage());
            if (ex.getCause() != null) {
                LOG.debug("Caused by: {}", ex.getCause().getMessage());
            }
        }
        if (vehicleClassPricesSpecialRate == null || vehicleClassPricesSpecialRate.isEmpty()) {
            LOG.debug("No vehicle special prices found for class '{}' with start date '{}': returning 0.0", vehicleClass.getName(), startDate);
            throw new IllegalArgumentException("No vehicle class special price found for class '" + vehicleClass.getName() + "' at age " + age.setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        LOG.debug("Returning special price={} (from start date '{}' and age=" + age.setScale(2, BigDecimal.ROUND_HALF_UP).toString(), ((VehicleClassPriceSpecialRate) vehicleClassPricesSpecialRate.get(0)).getPrice(), ((VehicleClassPriceSpecialRate) vehicleClassPricesSpecialRate.get(0)).getStartDate());
        return ((VehicleClassPriceSpecialRate) vehicleClassPricesSpecialRate.get(0)).getPrice();

    }

    @Override
    public BigDecimal getPrice(VehicleClass vehicleClass, Date startDate, int insId, int choId) {

        DetachedCriteria criteria = DetachedCriteria.forClass(VehicleClassPriceSpecialRate.class);
        criteria.add(Restrictions.eq("vehicleClass.id", vehicleClass.getId()));
        criteria.add(Restrictions.eq("insurer.id", insId));
        criteria.add(Restrictions.eq("chorganisation.id", choId));
        criteria.add(Restrictions.le("startDate", startDate));
        criteria.addOrder(Order.desc("startDate"));
        criteria.addOrder(Order.asc("age"));

        List<VehicleClassPriceSpecialRate> vehicleClassPricesSpecialRate = null;
        try {
            vehicleClassPricesSpecialRate = this.findByCriteria(criteria);
        } catch (Exception ex) {
            LOG.debug("Exception caught getting vehicle class price: {}", ex.getMessage());
            if (ex.getCause() != null) {
                LOG.debug("Caused by: {}", ex.getCause().getMessage());
            }
        }
        if (vehicleClassPricesSpecialRate == null || vehicleClassPricesSpecialRate.isEmpty()) {
            LOG.debug("No vehicle special prices found for class '{}' with start date '{}': returnin 0.0", vehicleClass.getName(), startDate);
            throw new IllegalArgumentException("No vehicle class price found for class '" + vehicleClass.getName() + "'");
        }
        return ((VehicleClassPriceSpecialRate) vehicleClassPricesSpecialRate.get(0)).getPrice();
    }
}
