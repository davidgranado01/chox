package idas.chox.data.services;


import idas.chox.core.model.BillingInsurerDetail;
import idas.chox.core.services.BillingInsurerDetailService;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class BillingInsurerDetailServiceImpl extends SecureDataService implements BillingInsurerDetailService{
    private static final Logger LOG = LoggerFactory.getLogger(BillingInsurerDetailServiceImpl.class);

    @Override
    public BillingInsurerDetail getObject(int id) {
        LOG.debug("getting  instance with id: " + id);
        try {
            BillingInsurerDetail instance = (BillingInsurerDetail) get(BillingInsurerDetail.class, id);
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
    public BillingInsurerDetail updateObject(BillingInsurerDetail object) {
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
    public void deleteObject(BillingInsurerDetail object) {
        try {
            delete(object);
            LOG.debug("deteteObject successful ");
        } catch (RuntimeException re) {
            LOG.error("deleteObject failed", re);
            throw re;
        }

    }

    @Override
    public List<BillingInsurerDetail> getBillingInsurerDetails(final int id) {
        List<BillingInsurerDetail> list = new ArrayList<BillingInsurerDetail>();
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(BillingInsurerDetail.class);
            criteria.createCriteria("billing").add(Restrictions.eq("id", id));
            criteria.addOrder(Order.desc("id"));
            list = findByCriteria(criteria);
        } catch (Exception ex) {
            LOG.error("Exception generating Invoice Summary Report: {}", ex.getMessage());
            if (ex.getCause() != null) {
                LOG.error("    Caused by: {}", ex.getCause().getMessage());
            }
        }
        return list;
    }

    @Override
    	public List sumPaymentAmount(int billingInsurerId){
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(BillingInsurerDetail.class);
		criteria.createCriteria("billing").add(Restrictions.eq("id", billingInsurerId));

		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.sum("amountReceived"));
		criteria.setProjection(projList);
		return criteria.list();
	}
}
