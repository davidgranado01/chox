package idas.chox.data.services;


import idas.chox.core.model.BillingInsurerDetail;
import idas.chox.core.services.BillingInsurerDetailService;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class BillingInsurerDetailServiceImpl extends SecureDataService implements BillingInsurerDetailService{
    private static final Log log = LogFactory.getLog(BillingChoDetailServiceImpl.class);

    public BillingInsurerDetail getObject(int id) {
        log.debug("getting  instance with id: " + id);
        try {
            BillingInsurerDetail instance = (BillingInsurerDetail) get(BillingInsurerDetail.class, id);
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

    public BillingInsurerDetail updateObject(BillingInsurerDetail object) {
        log.debug("updateObject with id " + object.getId());
        try {
            save(object);
            log.debug("updateObject sucessfull " + object.getId());
        } catch (RuntimeException re) {
            log.error("updateObject failed", re);
            throw re;
        }
        return object;
    }

    public void deleteObject(BillingInsurerDetail object) {
        try {
            delete(object);
            log.debug("deteteObject successful ");
        } catch (RuntimeException re) {
            log.error("deleteObject failed", re);
            throw re;
        }

    }

    public List<BillingInsurerDetail> getBillingInsurerDetails(final int id) {
        List<BillingInsurerDetail> list = new ArrayList<BillingInsurerDetail>();
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(BillingInsurerDetail.class);
            criteria.createCriteria("billing").add(Restrictions.eq("id", id));
            criteria.addOrder(Order.desc("id"));
            list = findByCriteria(criteria);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return list;
    }
}
