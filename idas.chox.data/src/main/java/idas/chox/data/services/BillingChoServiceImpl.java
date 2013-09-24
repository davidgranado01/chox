package idas.chox.data.services;

import idas.chox.core.model.AuditTrail;
import idas.chox.core.model.BillingCho;
import idas.chox.core.model.BillingChoDetail;
import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.services.BillingChoService;
import idas.chox.core.util.DateHelper;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

public class BillingChoServiceImpl extends SecureDataService implements BillingChoService {

    private static final Logger LOG = LoggerFactory.getLogger(BillingChoServiceImpl.class);

    @Override
    public Map checkObject(String scheduleName, Date dateFrom, Date dateTo, int choId) {
        Map checks = new HashMap();
        DetachedCriteria dc1 = DetachedCriteria.forClass(BillingCho.class).add(Restrictions.eq("scheduleName", scheduleName));
        List result1 = getHibernateTemplate().findByCriteria(dc1);
        if (result1 != null && result1.size() > 0) {
            checks.put("scheduleName", "Schedule name already exists.");
        }


        checkScheduleOverlap(checks, dateFrom, dateTo, choId);
        return checks;
    }

    private String getShDtStr(Date date) {
        DateFormat overlap_literal_format = new SimpleDateFormat("yyyy-MM-dd");
        return overlap_literal_format.format(date);
    }

    public void checkScheduleOverlap(Map checkmap, Date dateFrom, Date dateTo, int choId) {

        StringBuilder sb = new StringBuilder();
        sb.append("select distinct");
        sb.append("(date_from,date_to) ");
        sb.append("overlaps ");
        sb.append("(date(:pDateFrom)");
        sb.append(",date(:pDateTo)) ");
        sb.append("from billing_cho ");
        sb.append("where cho_id = :pChoId");

        String query = sb.toString();
        LOG.debug("checkScheduleOverlap query is: {}", query);

        Map extParameters = new HashMap();
        extParameters.put("pDateFrom", DateHelper.getDBDateFormat().format(dateFrom));
        extParameters.put("pDateTo", DateHelper.getDBDateFormat().format(dateTo));
        extParameters.put("pChoId", choId);
        
        List valList = externalQuery(query, extParameters);
        for (Object object : valList) {
            Map data = (Map) object;
            if ((Boolean) (data.get("overlaps"))) {
                checkmap.put("dateTo", "From or To date overlaps existing schedule.");
                checkmap.put("dateFrom", "From or To date overlaps existing schedule.");
                break;
            }
        }
    }

    /* (non-Javadoc)
     * @see idas.chox.data.services.BillingChoService#getObject(int)
     */
    @Override
    public BillingCho getObject(int id) {
        LOG.debug("getting BillingCho instance with id: {}", id);
        try {
            BillingCho instance = (BillingCho) get(BillingCho.class, id);
            if (instance == null) {
                LOG.debug("getObject successful, no instance found");
            } else {
                LOG.debug("getObject successful, instance found");
            }
            return instance;
        } catch (RuntimeException re) {
            LOG.error("getObject failed with exception: {}", re.getMessage());
            throw re;
        }

    }

    /* (non-Javadoc)
     * @see idas.chox.data.services.BillingChoService#updateObject(idas.chox.core.model.BillingCho)
     */
    @Override
    public BillingCho updateObject(BillingCho object) {
        LOG.debug("updateObject with id {}", object.getId());
        try {
            save(object);
            LOG.debug("updateObject sucessfull: {}", object.getId());
        } catch (RuntimeException re) {
            LOG.error("updateObject failed with exception: {}", re.getMessage());
            throw re;
        }
        return object;
    }

    /* (non-Javadoc)
     * @see idas.chox.data.services.BillingChoService#getBillingChos()
     */
    @Override
    public List getBillingChos() {
        List list = new ArrayList<BillingCho>();
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(BillingCho.class);
            criteria.addOrder(Order.desc("dateTo"));
            list = findByCriteria(criteria);
        } catch (Exception e) {
            LOG.error("Error getting billing CHOs: {}", e.getMessage());
        }
        return list;
    }

    @Override
    public List searchBills(String choReference, String claimNumber) {
        List list = new ArrayList<BillingCho>();

        try {
            LOG.debug("Cho Ref: {}", choReference);
            LOG.debug("Claim Number: {}", claimNumber);

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
    @Override
    public void deteteObject(BillingCho object) {
        try {
            delete(object);
            LOG.debug("deteteObject successful for schedule: {}", object.getScheduleName());
        } catch (RuntimeException re) {
            LOG.error("deleteObject failed with exception: {}", re.getMessage());
            throw re;
        }

    }

    /* (non-Javadoc)
     * @see idas.chox.data.services.BillingChoService#findClaimsBetween(java.util.Date, java.util.Date)
     */
//    public List findClaimsBetween(Date from, Date to) {
//        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
//        criteria.add(Expression.ge("createdDate", from));
//        criteria.add(Expression.le("createdDate", to));
//        return findByCriteria(criteria);
//    }
    @Override
    public List<Claim> findClaimsforSchedule(Date from, Date to, Chorganisation cho, boolean excludeSupplmntInv) {
        DetachedCriteria auditCriteria = DetachedCriteria.forClass(AuditTrail.class)
                .add(Restrictions.between("updateDate", from, to))
                .add(Restrictions.eq("newStatus", ClaimStatus.INVOICE_PAYMENT_RECEIVED))
                .add(Restrictions.eq("reverted", Boolean.FALSE))
                .setProjection(Property.forName("claim.id"));

        DetachedCriteria billingChoDetailCriteria = DetachedCriteria.forClass(BillingChoDetail.class)
                .setProjection(Property.forName("claim.id"));
        
        DetachedCriteria criteria = null;
        if(excludeSupplmntInv){
            
             criteria = DetachedCriteria.forClass(Claim.class)
                .setProjection(Projections.distinct(Projections.projectionList().add(Projections.property("id"))))
                .add(Restrictions.eq("chorganisation", cho))
                .add(Property.forName("id").in(auditCriteria))
                .add(Property.forName("id").notIn(billingChoDetailCriteria))
                .add(Restrictions.not(Restrictions.in("claimType", ClaimType.getSupplementaryInvoiceTypes())));
//                .add(Restrictions.disjunction()
//                     .add(Restrictions.eq("supplementaryInvoicedClaim", Boolean.FALSE))
//                     .add(Restrictions.conjunction()
//                        .add(Restrictions.eq("supplementaryInvoicedClaim", Boolean.TRUE))
//                        .add(Restrictions.eq("originalSupplementaryInvoicedClaim", Boolean.TRUE))));
             
        }else{
            
              criteria = DetachedCriteria.forClass(Claim.class)
                .setProjection(Projections.distinct(Projections.projectionList()
                .add(Projections.property("id")))).add(Restrictions.eq("chorganisation", cho))
                .add(Property.forName("id").in(auditCriteria)).add(Property.forName("id").notIn(billingChoDetailCriteria));
        }


        List<Integer> claimIds = findByCriteria(criteria);

        LOG.debug("Found {} claim IDs matching schedule.", claimIds.size());
        if (!claimIds.isEmpty()) {
            DetachedCriteria criteria2 = DetachedCriteria.forClass(Claim.class).add(Property.forName("id").in(claimIds));

            return findByCriteria(criteria2);

        } else {
            return new ArrayList<Claim>();
        }
    }

    /* (non-Javadoc)
     * @see idas.chox.data.services.BillingChoService#getScheduleDetailList(int)
     */
    @Override
    public Set<BillingChoDetail> getScheduleDetailList(final int id) {
        return (Set<BillingChoDetail>) getHibernateTemplate().execute(new HibernateCallback() {

            @Override
            public Object doInHibernate(Session session) throws HibernateException,
                    SQLException {
                LOG.debug("get schedule list");
                BillingCho schedule = (BillingCho) session.get(BillingCho.class, id);
                //log.debug("size in dao"+schedule.getBillingChoDetails().size());
                return schedule.getBillingDetails();
            }
        });
    }

    @Override
    public int getNumberInvoicesSubmitted(Date dateFrom, Date dateTo, Chorganisation cho) {
        /*        DetachedCriteria invoiceCriteria = DetachedCriteria.forClass(Invoice.class)
        .add(Restrictions.between("created_date", dateFrom, dateTo))
        .setProjection(Property.forName("claim.invoice_id"));
        
        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class)
        .add(Restrictions.eq("chorganisation", cho))
        .add(Property.forName("invoice_id").in(invoiceCriteria));
         */
        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class).add(Restrictions.eq("chorganisation", cho));
        criteria.createCriteria("invoice").add(Restrictions.between("createdDate", dateFrom, dateTo));
        int numberOfInvoicesSubmitted = findByCriteria(criteria).size();

        LOG.debug("Number of invoices submitted: {}", numberOfInvoicesSubmitted);
        return numberOfInvoicesSubmitted;
    }
}
