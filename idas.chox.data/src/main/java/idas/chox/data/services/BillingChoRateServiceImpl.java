package idas.chox.data.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import idas.chox.core.model.BillingChoRate;
import idas.chox.core.services.BillingChoRateService;

/**
 *
 * @author abrar
 */
public class BillingChoRateServiceImpl extends SecureDataService implements BillingChoRateService {

    private static final Logger LOG = LoggerFactory.getLogger(BillingChoRateServiceImpl.class);

    @Override
    public BigDecimal getRate(int volume) {
        BillingChoRate billingChoRate;
        BigDecimal fee = BigDecimal.ZERO;

        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(BillingChoRate.class);
            criteria.add(Restrictions.ge("minVolume", volume));
            Criterion maxVolume = Restrictions.ge("maxVolume", volume);
            Criterion isNull = Restrictions.isNull("maxVolume");
            LogicalExpression orExp = Restrictions.or(maxVolume, isNull);
            criteria.add(orExp);

            billingChoRate = (BillingChoRate) getByCriteria(criteria);
            fee = billingChoRate.getFee();
        } catch (Exception e) {
            LOG.error("Exception thrown: {}", e.getMessage());
        }

        return fee;
    }

    @Override
    public BigDecimal getRateForCho(int cho_organisation_id, int volume) {
        BillingChoRate billingChoRate;
        BigDecimal fee = BigDecimal.ZERO;

        try {


            DetachedCriteria criteria = DetachedCriteria.forClass(BillingChoRate.class);
            criteria.createCriteria("chorganisation").add(Restrictions.eq("id", cho_organisation_id));
            Criterion minVolume = Restrictions.le("minVolume", volume);
            Criterion maxVolume = Restrictions.ge("maxVolume", volume);
            Criterion isNull = Restrictions.isNull("maxVolume");

            LogicalExpression and1 = Restrictions.and(minVolume, maxVolume);
            LogicalExpression and2 = Restrictions.and(minVolume, isNull);

            LogicalExpression or = Restrictions.or(and1, and2);
            criteria.add(or);
            
            List  list =  findByCriteria(criteria);
            LOG.debug("List "+ list.size());
            if(list.isEmpty()){
                return new BigDecimal(0);
            }
            billingChoRate = (BillingChoRate)list.get(0);
            LOG.debug("Rate for {} id {}",billingChoRate.getId(), billingChoRate.getFee());
            fee = billingChoRate.getFee();
        } catch (Exception e) {
            LOG.error("Exception thrown: {}", e.getMessage());
            throw new RuntimeException(e);
        }

        return fee;
    }



    @Override
    public List<BillingChoRate> getBillingChoRates() {
        List<BillingChoRate> billingChoRate = new ArrayList<BillingChoRate>();

        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(BillingChoRate.class);
            criteria.addOrder(Order.asc("id"));
//            criteria.addOrder(Order.asc("min_value"));

            billingChoRate = findByCriteria(criteria);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return billingChoRate;
    }

    @Override
    public List<BillingChoRate> getBillingChoRates(int cho_organisation_id) {
        List<BillingChoRate> billingChoRate = new ArrayList<BillingChoRate>();

        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(BillingChoRate.class);
            criteria.add(Restrictions.eq("choorganisation", cho_organisation_id));


            billingChoRate = findByCriteria(criteria);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return billingChoRate;
    }

    /* (non-Javadoc)
     * @see idas.chox.data.services.BillingChoService#getObject(int)
     */
    @Override
    public BillingChoRate getObject(int id) {
        LOG.debug("getting BillingChoRate instance with id: {}", id);
        try {
            BillingChoRate instance = (BillingChoRate) get(BillingChoRate.class, id);
            if (instance == null) {
                LOG.debug("getObject successful, no instance found");
            } else {
                LOG.debug("getObject successful, instance found");
            }
            return instance;
        } catch (RuntimeException re) {
            LOG.error("RuntimeException thrown in getObject: {}", re.getMessage());
            throw re;
        }

    }
}
