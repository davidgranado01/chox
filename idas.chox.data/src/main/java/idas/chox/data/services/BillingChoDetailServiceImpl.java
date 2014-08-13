package idas.chox.data.services;

import idas.chox.core.model.BillingChoDetail;
import idas.chox.core.services.BillingChoDetailService;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BillingChoDetailServiceImpl extends SecureDataService implements BillingChoDetailService {
    private static final Logger LOG = LoggerFactory.getLogger(BillingChoDetailServiceImpl.class);


    @Override
    public BillingChoDetail getObject(int id) {
        LOG.debug("getting  instance with id: " + id);
        try {
            BillingChoDetail instance = (BillingChoDetail) get(BillingChoDetail.class, id);
            if (instance == null) {
                LOG.debug("getObject successful, no instance found");
            } else {
                LOG.debug("getObject successful, instance found");
            }
            return instance;
        } catch (RuntimeException re) {
            LOG.error("getObject failed", re);
            throw re;
        }

    }

    @Override
    public BillingChoDetail updateObject(BillingChoDetail object) {
        LOG.debug("updateObject with id " + object.getId());
        try {
            save(object);
            LOG.debug("updateObject sucessfull " + object.getId());
        } catch (RuntimeException re) {
            LOG.error("updateObject failed", re);
            throw re;
        }
        return object;
    }

    @Override
    public void deleteObject(BillingChoDetail object) {
        try {
            delete(object);
            LOG.debug("deteteObject successful ");
        } catch (RuntimeException re) {
            LOG.error("deleteObject failed", re);
            throw re;
        }

    }

    @Override
    public List<BillingChoDetail> getBillingChoDetails(final int id) {
        List<BillingChoDetail> list = new ArrayList<BillingChoDetail>();
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(BillingChoDetail.class);
            criteria.createCriteria("billing").add(Restrictions.eq("id", id));
            criteria.addOrder(Order.desc("id"));
            list = findByCriteria(criteria);
        } catch (Exception e) {
            LOG.error("Exception thrown: {}\n", e.getMessage(), e);
        }
        return list;
    }

    @Override
    public List sumPaymentAmount(int billingChoId) {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(BillingChoDetail.class);
        criteria.createCriteria("billing").add(Restrictions.eq("id", billingChoId));

        ProjectionList projList = Projections.projectionList();
        projList.add(Projections.sum("amountReceived"));
        criteria.setProjection(projList);
        return criteria.list();
    }
}
