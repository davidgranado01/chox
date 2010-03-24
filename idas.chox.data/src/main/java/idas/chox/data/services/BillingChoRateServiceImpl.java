/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.data.services;

import idas.chox.core.model.BillingChoRate;
import idas.chox.core.services.BillingChoRateService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author abrar
 */
public class BillingChoRateServiceImpl extends SecureDataService implements BillingChoRateService {

    private static final Log log = LogFactory.getLog(BillingChoRateServiceImpl.class);

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
        } catch (Throwable e) {
            e.printStackTrace();
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
            log.debug("############################################### List "+ list.size());
            billingChoRate = (BillingChoRate)list.get(0);
            log.debug("##################" + billingChoRate.getId() + " " + billingChoRate.getFee() );


            fee = billingChoRate.getFee();
        } catch (Exception e) {
            log.error(e);
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
    public BillingChoRate getObject(int id) {
        log.debug("getting BillingChoRate instance with id: " + id);
        try {
            BillingChoRate instance = (BillingChoRate) get(BillingChoRate.class, id);
            if (instance == null) {
                log.debug("getObject successful, no instance found");
            } else {
                log.debug("getObject successful, instance found");
            }
            return instance;
        } catch (RuntimeException re) {
            log.error("getObject failed", re);
            throw re;
        }

    }
}
