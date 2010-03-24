package idas.chox.data.services;

import idas.chox.core.model.BillingChoDetail;
import idas.chox.core.services.BillingChoDetailService;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class BillingChoDetailServiceImpl extends SecureDataService implements BillingChoDetailService {

    private static final Log log = LogFactory.getLog(BillingChoDetailServiceImpl.class);

    public BillingChoDetail getObject(int id) {
        log.debug("getting  instance with id: " + id);
        try {
            BillingChoDetail instance = (BillingChoDetail) get(BillingChoDetail.class, id);
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

    public BillingChoDetail updateObject(BillingChoDetail object) {
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

    public void deleteObject(BillingChoDetail object) {
        try {
            delete(object);
            log.debug("deteteObject successful ");
        } catch (RuntimeException re) {
            log.error("deleteObject failed", re);
            throw re;
        }

    }

    public List<BillingChoDetail> getBillingChoDetails(final int id) {
        List<BillingChoDetail> list = new ArrayList<BillingChoDetail>();
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(BillingChoDetail.class);
            criteria.createCriteria("billing").add(Restrictions.eq("id", id));
            criteria.addOrder(Order.desc("id"));
            list = findByCriteria(criteria);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return list;
    }

    public List sumPaymentAmount(int billingChoId) {
        Criteria criteria = getSession().createCriteria(BillingChoDetail.class);
        criteria.createCriteria("billing").add(Restrictions.eq("id", billingChoId));

        ProjectionList projList = Projections.projectionList();
        projList.add(Projections.sum("amountReceived"));
        criteria.setProjection(projList);
        return criteria.list();
    }
}
