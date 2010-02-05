package idas.chox.data.services;

import idas.chox.core.model.AuditTrail;
import idas.chox.core.model.BillingCho;
import idas.chox.core.model.BillingChoDetail;
import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.Claim;
import idas.chox.core.services.BillingChoService;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class BillingChoServiceImpl extends SecureDataService implements BillingChoService {

    private static final Log log = LogFactory.getLog(BillingChoServiceImpl.class);
    

    public Map checkObject(String scheduleName, Date dateFrom, Date dateTo) {
        Map checks = new HashMap();
        DetachedCriteria dc1 = DetachedCriteria.forClass(BillingCho.class).add(Restrictions.eq("scheduleName", scheduleName));
        List result1 = getHibernateTemplate().findByCriteria(dc1);
        if (result1 != null && result1.size() > 0) {
            checks.put("scheduleName", "Schedule name already exists.");
        }


        checkScheduleOverlap(checks, dateFrom, dateTo);
        return checks;
    }

    private String getShDtStr(Date date) {
        DateFormat overlap_literal_format = new SimpleDateFormat("yyyy-MM-dd");
        return overlap_literal_format.format(date);
    }

    public void checkScheduleOverlap(Map checkmap, Date dateFrom, Date dateTo) {

            StringBuffer sb = new StringBuffer();
            sb.append("select distinct");
                sb.append("(date_from,date_to) ");
                sb.append("overlaps ");
                sb.append("(DATE '"+getShDtStr(dateFrom)+"',DATE '"+getShDtStr(dateTo)+"') ");
                sb.append("from billing_cho ");

            String query = sb.toString();
            log.debug(query);

            List valList  = getCurrentSession().createSQLQuery(query).list();
            for (Object object : valList) {
                log.debug(((Boolean)object).booleanValue());
                if ( ((Boolean)object).booleanValue() ){
                    checkmap.put("dateTo", "From or To date overlaps existing schedule.");
                    checkmap.put("dateFrom", "From or To date overlaps existing schedule.");
                    break;
                }
            }
    }
    /* (non-Javadoc)
     * @see idas.chox.data.services.BillingChoService#getObject(int)
     */

    public BillingCho getObject(int id) {
        log.debug("getting BillingCho instance with id: " + id);
        try {
            BillingCho instance = (BillingCho) get(BillingCho.class, id);
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

    /* (non-Javadoc)
     * @see idas.chox.data.services.BillingChoService#updateObject(idas.chox.core.model.BillingCho)
     */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public BillingCho updateObject(BillingCho object) {
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

    /* (non-Javadoc)
     * @see idas.chox.data.services.BillingChoService#getBillingChos()
     */
    @SuppressWarnings("unchecked")
    public List getBillingChos() {
        List list = new ArrayList<BillingCho>();
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(BillingCho.class);
            criteria.addOrder(Order.desc("dateTo"));
            list = findByCriteria(criteria);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return list;
    }

    public List searchBills(String choReference,String claimNumber){
        List list = new ArrayList<BillingCho>();

        try {
            log.debug("Cho Ref" + choReference);
            log.debug("Claim Number " + claimNumber);

            DetachedCriteria criteria = DetachedCriteria.forClass(BillingCho.class);
            DetachedCriteria dc = criteria.createCriteria("billingDetails").createCriteria("claim");
            if (claimNumber != null && !claimNumber.equals("")) {
                Criterion c2 = Restrictions.eq("claimNumber", claimNumber);
                dc.add(c2);
            }


            if (choReference != null && !choReference.equals("")) {
                Criterion c1 = Restrictions.eq("choReference", choReference);
                dc.add(c1);
            }


            list = findByCriteria(criteria);
        } catch (RuntimeException e) {
            throw e;
        }

        return list;
    }

    /* (non-Javadoc)
     * @see idas.chox.data.services.BillingChoService#deteteObject(idas.chox.core.model.BillingCho)
     */
    
    public void deteteObject(BillingCho object) {
        try {
            delete(object);
            log.debug("deteteObject successful " + object.getScheduleName());
        } catch (RuntimeException re) {
            log.error("deleteObject failed", re);
            throw re;
        }

    }

    /* (non-Javadoc)
     * @see idas.chox.data.services.BillingChoService#findClaimsBetween(java.util.Date, java.util.Date)
     */
    public List findClaimsBetween(Date from, Date to) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);

        criteria.add(Expression.ge("createdDate", from));
        criteria.add(Expression.le("createdDate", to));

        return findByCriteria(criteria);
    }

    public List findClaimsforSchedule(Date from, Date to, Chorganisation cho) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
        criteria.add(Expression.ge("createdDate", from));
        criteria.add(Expression.le("createdDate", to));
        criteria.add(Restrictions.eq("chorganisation", cho));
        return findByCriteria(criteria);
    }

    public List findInvoiceforSchedule(Date from,Date to,Chorganisation cho){
        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
        criteria.createCriteria("invoice").add(Restrictions.between("createdDate", from, to));        
        criteria.add(Restrictions.eq("chorganisation", cho));
        List claimsList = findByCriteria(criteria);
        logger.debug("claims list size " + claimsList.size());
        List returnList = new ArrayList();
        for (Iterator claimsItor = claimsList.iterator(); claimsItor.hasNext();) {
            Claim claim = (Claim)claimsItor.next();
            DetachedCriteria dc = DetachedCriteria.forClass(AuditTrail.class);
            criteria.createCriteria("claim").add(Restrictions.eq("id", claim.getId()));
            Criterion c1 = Restrictions.eq("originalStatus", "InvoicePaymentLogged");
            Criterion c2 = Restrictions.eq("newStatus", "InvoicePaymentLogged");
            dc.add(Restrictions.or(c1, c2));
            List l = findByCriteria(dc);
            if ( l.size() > 0 ){
                returnList.add(claim);
            }
        }
        logger.debug("return list size " + returnList.size());
        return returnList;
    }


/*
    public List findInvoiceforSchedule(Date from,Date to,Chorganisation cho){
        String query1 =
            "select claim.* from claim,invoice where claim.id = invoice.id and " +
            "invoice.created_date between date '"

        sb.append("select ");
            sb.append("claim.id ");
        sb.append("from ");
            sb.append("claim,invoice");
            String sql = "    " 
                    "";
  
         claim.id from  claim,invoice where
claim.id = invoice.id and invoice.created_date between date '2009-10-01' and '2009-10-15' and
 exists (select * from audit_trail where claim.id = audit_trail.claim_id and original_status='InvoicePaymentLogged' )

union

select claim.id from  claim,invoice where
claim.id = invoice.id and invoice.created_date between date '2009-10-01' and '2009-10-15' and
 exists (select * from audit_trail where claim.id = audit_trail.claim_id and new_status='InvoicePaymentLogged' )
         *
  
        return null;
    }
       */
    /* (non-Javadoc)
     * @see idas.chox.data.services.BillingChoService#getScheduleDetailList(int)
     */
    public Set<BillingChoDetail> getScheduleDetailList(final int id) {
        return (Set<BillingChoDetail>) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException,
                    SQLException {
                log.debug("get schedule list");
                BillingCho schedule = (BillingCho) session.get(BillingCho.class, id);
                //log.debug("size in dao"+schedule.getBillingChoDetails().size());
                return schedule.getBillingDetails();
            }
        });
    }
}
