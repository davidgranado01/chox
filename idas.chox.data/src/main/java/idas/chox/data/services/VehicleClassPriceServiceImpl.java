package idas.chox.data.services;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import idas.chox.core.search.SearchResult;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.VehicleClass;
import idas.chox.core.model.VehicleClassPrice;
import idas.chox.core.model.ClaimType;
import idas.chox.core.services.VehicleClassPriceService;
import idas.chox.core.services.BreBandService;
import idas.chox.core.services.VehicleClassPriceSpecialRateService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author John
 */
public class VehicleClassPriceServiceImpl extends SecureDataService implements VehicleClassPriceService {

    private static final Logger LOG = LoggerFactory.getLogger(VehicleClassPriceServiceImpl.class);
    private BreBandService breBandService;
    private VehicleClassPriceSpecialRateService vehicleClassPriceSpecialRateService;

    public void setBreBandService(BreBandService breBandService) {
        this.breBandService = breBandService;
    }

    public void setVehicleClassPriceSpecialRateService(VehicleClassPriceSpecialRateService vehicleClassPriceSpecialRateService) {
        this.vehicleClassPriceSpecialRateService = vehicleClassPriceSpecialRateService;
    }

    @Override
    public BigDecimal getPrice(ClaimType claimType, VehicleClass vehicleClass, Date startDate, int insId, int choId) throws Exception {

        if (ClaimType.isSubscriber(claimType) || ClaimType.isFixedFee(claimType)
                || ClaimType.isCollaborationProtocol(claimType) || breBandService.isSupplierRatesActivated(choId, insId)) {
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
                LOG.debug("No vehicle prices found for class '{}' with start date '{}' and insurerId={}, choId={}",
                        new Object[] {vehicleClass.getName(), startDate, insId, choId});
                throw new Exception("No rate found for vehicle class '" + vehicleClass.getName() + "'");
            }
            return ((VehicleClassPrice) vehicleClassPrices.get(0)).getPrice();
        }
    }

    @Override
    public BigDecimal getPrice(ClaimType claimType, VehicleClass vehicleClass, Date startDate, BigDecimal age, int insId, int choId) throws Exception {

        if (ClaimType.isSubscriber(claimType) || ClaimType.isFixedFee(claimType)
                || ClaimType.isCollaborationProtocol(claimType) || breBandService.isSupplierRatesActivated(choId, insId)) {
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
                LOG.debug("No rate found for vehicle class '{}' with start date '{}' and insurerId={}, choId={}",
                        new Object[] {vehicleClass.getName(), startDate, insId, choId});
                throw new Exception(MessageFormat.format("No rate found for vehicle class ''{0}'' at age {1}", vehicleClass.getName(), age.setScale(2, BigDecimal.ROUND_HALF_UP)));
            }
            BigDecimal price = ((VehicleClassPrice) vehicleClassPrices.get(0)).getPrice();

            LOG.debug("Returning price={} for vehicle class '{}', with start date '{}', age={}, insId={}, choId={})",
                    new Object[]{vehicleClass.getName(), price,
                            ((VehicleClassPrice) vehicleClassPrices.get(0)).getStartDate(),
                            age.setScale(2, BigDecimal.ROUND_HALF_UP).toString(),
                            insId, choId
                    });
            return price;
        }
    }

    @Override
    public SearchResult getVehicleClassPriceRatesPagination(int start, int limit, String sort, String dir) {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(VehicleClassPrice.class);

        Integer totalCount = totalCount(criteria);

        criteria.setFirstResult(start);
        criteria.setMaxResults(limit);
        if (!sort.isEmpty() && !dir.isEmpty()) {
            if (sort.equalsIgnoreCase("startDate")) {
                addSort(criteria, "startDate", dir);
            } else if (sort.equalsIgnoreCase("createdDate")) {
                addSort(criteria, "createdDate", dir);
            }
        } else {
            criteria.addOrder(Order.desc("startDate"));
        }

        List<VehicleClassPrice> vehicleClassPrices = criteria.list();

        return new SearchResult(vehicleClassPrices, totalCount, null);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value = "transactionManager")
    @Override
    public void deleteVehicleClassPriceRate(int id) throws Exception {
        if (id > 0) {
            try {
                DetachedCriteria mapping = DetachedCriteria.forClass(VehicleClassPrice.class);
                mapping.add(Restrictions.eq("id", id));

                VehicleClassPrice vehicleClassPrice = (VehicleClassPrice) getByCriteria(mapping);
                delete(vehicleClassPrice);
            } catch (Exception ex) {
                LOG.warn("Exception thrown removing rate: {}", ex.getMessage(), ex);
                throw new Exception("An error occured removing the rate - please try again");
            }
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value = "transactionManager")
    public void saveGTARates(List<VehicleClassPrice> gtaRates) {
        saveCollections(gtaRates);
    }

}
