package idas.chox.data.services;

import idas.chox.core.model.Billing;
import idas.chox.core.model.BillingInsurer;
import idas.chox.core.model.BillingInsurerDetail;
import idas.chox.core.model.Claim;
import idas.chox.core.model.Insurer;
import idas.chox.core.services.BillingInsurerService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class BillingInsurerServiceImpl extends SecureDataService implements BillingInsurerService {
	private static final Log log = LogFactory.getLog(BillingInsurerServiceImpl.class);
	
	/* (non-Javadoc)
	 * @see idas.chox.data.services.BillingInsurerService#checkObject(java.lang.String, java.util.Date, java.util.Date)
	 */
	public Map checkObject(String scheduleName,Date dateFrom,Date dateTo){
		Map checks = new HashMap();
		DetachedCriteria dc1 = DetachedCriteria.forClass(BillingInsurer.class).add(Restrictions.eq("scheduleName", scheduleName));
		List result1 =  getHibernateTemplate().findByCriteria(dc1);
        if ( result1 != null && result1.size() > 0) {
        	checks.put("scheduleName", "Schedule name already exists.");
        } 
             
        DetachedCriteria dc2 = DetachedCriteria.forClass(BillingInsurer.class).add(Restrictions.ge("dateTo", dateFrom));       
        List result2 =  getHibernateTemplate().findByCriteria(dc2);
        if ( result2 != null && result2.size() > 0) {
        	for (Object object : result2) {
				log.debug(((Billing)object).getScheduleName() + " schedule name ");
			}
        	checks.put("dateFrom", "From date overlaps existing schedule.");
        }
        
        DetachedCriteria dc3 = DetachedCriteria.forClass(BillingInsurer.class).add(Restrictions.ge("dateTo", dateTo));       
        List result3 =  getHibernateTemplate().findByCriteria(dc3);
        if ( result3 != null && result3.size() > 0) {
        	checks.put("dateTo", "To date overlaps existing schedule.");
        }
        
		return checks;
	}
	
	
	/* (non-Javadoc)
	 * @see idas.chox.data.services.BillingInsurerService#getObject(int)
	 */
	public BillingInsurer getObject(int id) {
		log.debug("getting BillingInsurer instance with id: " + id);
		try{
			BillingInsurer instance = (BillingInsurer)get(BillingInsurer.class,id);
			if (instance==null) {
                log.debug("getObject successful, no instance found");
            }
            else {
                log.debug("getObject successful, instance found");
            }
            return instance;
		}catch(RuntimeException re){
			log.error("getObject failed", re);
            throw re;
		}
		
	}

	
	/* (non-Javadoc)
	 * @see idas.chox.data.services.BillingInsurerService#updateObject(idas.chox.core.model.BillingInsurer)
	 */
        @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Billing updateObject(BillingInsurer object) {
		log.debug("updateObject with id " + object.getId());

		try{
			save(object);
                        log.debug("claims in list "+object.getBillingDetails().size());
			log.debug("updateObject sucessfull " + object.getId());
		}catch(RuntimeException re){
			log.error("updateObject failed",re);
			throw re;
		}
		return object;
	}

	
	/* (non-Javadoc)
	 * @see idas.chox.data.services.BillingInsurerService#getBillingInsurers()
	 */
	public List<BillingInsurer> getBillingInsurers() {
		List<BillingInsurer> list = new ArrayList<BillingInsurer>();
		
		try {    
            DetachedCriteria criteria = DetachedCriteria.forClass(BillingInsurer.class);
            criteria.addOrder(Order.desc("dateTo"));
            list = findByCriteria(criteria);        
        } catch (Throwable e) {
           e.printStackTrace();
        }
        log.debug("getBillingInsurers "+ list.size());
		return list;
	}

	
	/* (non-Javadoc)
	 * @see idas.chox.data.services.BillingInsurerService#deteteObject(idas.chox.core.model.BillingInsurer)
	 */
        @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void deteteObject(BillingInsurer object) {
		try{
			delete(object);
			log.debug("deteteObject successful " + object.getScheduleName());
		}catch(RuntimeException re){
			log.error("deleteObject failed",re);
			throw re;
		}
		
	}
	
	/* (non-Javadoc)
	 * @see idas.chox.data.services.BillingInsurerService#findClaimsBetween(java.util.Date, java.util.Date)
	 */
	public List findClaimsBetween(Date from,Date to){
		DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
		
		criteria.add(Expression.ge("createdDate",from));
		criteria.add(Expression.le("createdDate", to));
		
		return findByCriteria(criteria);
	}

                public List findClaimsforSchedule(Date from,Date to, Insurer insurer){
            DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
            	criteria.add(Expression.ge("createdDate",from));
		criteria.add(Expression.le("createdDate", to));
             criteria.add(Restrictions.eq("insurer", insurer));
            return findByCriteria(criteria);
        }
	
	/* (non-Javadoc)
	 * @see idas.chox.data.services.BillingInsurerService#getScheduleDetailList(int)
	 */
	public Set<BillingInsurerDetail> getScheduleDetailList(final int id){
		return (Set<BillingInsurerDetail>)getHibernateTemplate().execute(new HibernateCallback() {			
			
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
				log.debug("get schedule list");
				BillingInsurer schedule = (BillingInsurer) session.get(BillingInsurer.class, id);
				//log.debug("size in dao"+schedule.getBillingInsurerDetails().size());
				return schedule.getBillingDetails();
			}
		});
	}

}
