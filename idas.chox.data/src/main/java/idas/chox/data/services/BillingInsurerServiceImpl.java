package idas.chox.data.services;

import idas.chox.core.model.AuditTrail;
import idas.chox.core.model.Billing;
import idas.chox.core.model.BillingInsurer;
import idas.chox.core.model.BillingInsurerDetail;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Insurer;
import idas.chox.core.services.BillingInsurerService;

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

public class BillingInsurerServiceImpl extends SecureDataService implements BillingInsurerService {

    private static final Logger LOG = LoggerFactory.getLogger(BillingInsurerServiceImpl.class);

    /* (non-Javadoc)
     * @see idas.chox.data.services.BillingInsurerService#checkObject(java.lang.String, java.util.Date, java.util.Date)
     */
    @Override
    public Map checkObject(String scheduleName, Date dateFrom, Date dateTo, int insurerId, String triggerPoint) {
        Map checks = new HashMap();
        DetachedCriteria dc1 = DetachedCriteria.forClass(BillingInsurer.class).add(Restrictions.eq("scheduleName", scheduleName));
        List result1 = getHibernateTemplate().findByCriteria(dc1);
        if (result1 != null && result1.size() > 0) {
            checks.put("scheduleName", "Schedule name already exists.");
        }

        checkScheduleOverlap(checks, dateFrom, dateTo, insurerId, triggerPoint);
        return checks;
    }

    private String getShDtStr(Date date) {
        DateFormat overlap_literal_format = new SimpleDateFormat("yyyy-MM-dd");
        return overlap_literal_format.format(date);
    }

    public void checkScheduleOverlap(Map checkmap, Date dateFrom, Date dateTo, int insurerId, String triggerPoint) {

        StringBuilder sb = new StringBuilder();
        sb.append("select distinct");
        sb.append("(date_from,date_to) ");
        sb.append("overlaps ");
        sb.append("(DATE '");
        sb.append(getShDtStr(dateFrom));
        sb.append("',DATE '");
        sb.append(getShDtStr(dateTo));
        sb.append("') ");
        sb.append("from billing_insurer ");
        sb.append("where insurer_id = ");
        sb.append(insurerId);
        if (triggerPoint.equals("Manual Invoice Paid")) {
            sb.append(" and trigger_point = 'Manual Invoice Paid'");
        } else {
            sb.append(" and trigger_point != 'Manual Invoice Paid'");
        }

        String query = sb.toString();
        LOG.debug(query);

        List valList = getCurrentSession().createSQLQuery(query).list();
        for (Object object : valList) {
            LOG.debug("Object value: {}", ((Boolean) object).booleanValue());
            if (((Boolean) object).booleanValue()) {
                checkmap.put("dateTo", "From or To date overlaps existing schedule.");
                checkmap.put("dateFrom", "From or To date overlaps existing schedule.");
                break;
            }
        }
    }

    /* (non-Javadoc)
     * @see idas.chox.data.services.BillingInsurerService#getObject(int)
     */
    @Override
    public BillingInsurer getObject(int id) {
        LOG.debug("getting BillingInsurer instance with id: {}", id);
        try {
            BillingInsurer instance = (BillingInsurer) get(BillingInsurer.class, id);
            if (instance == null) {
                LOG.debug("getObject successful, no instance found");
            } else {
                LOG.debug("getObject successful, instance found");
            }
            return instance;
        } catch (RuntimeException re) {
            LOG.error("getObject failed: {}", re.getMessage());
            throw re;
        }

    }

    /* (non-Javadoc)
     * @see idas.chox.data.services.BillingInsurerService#updateObject(idas.chox.core.model.BillingInsurer)
     */
    @Override
    public Billing updateObject(BillingInsurer object) {
        LOG.debug("updateObject with id={}", object.getId());

        try {
            save(object);
            LOG.debug("no claims in list: {}", object.getBillingDetails().size());
            LOG.debug("updateObject sucessfull: {} ", object.getId());
        } catch (RuntimeException re) {
            LOG.error("updateObject failed: {}", re.getMessage());
            throw re;
        }
        return object;
    }

    /* (non-Javadoc)
     * @see idas.chox.data.services.BillingInsurerService#getBillingInsurers()
     */
    @Override
    public List<BillingInsurer> getBillingInsurers() {
        List<BillingInsurer> list = new ArrayList<BillingInsurer>();

        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(BillingInsurer.class);
            criteria.addOrder(Order.desc("dateTo"));
            list = findByCriteria(criteria);
        } catch (Exception e) {
            LOG.error("Error getting Bolling Insurers: {}", e.getMessage());
        }
        LOG.debug("getBillingInsurers: {} ", list.size());
        return list;
    }

    @Override
    public List searchBills(String choReference, String claimNumber) {
        List list = new ArrayList<BillingInsurer>();

        try {
            LOG.debug("Cho Ref: {}, Claim Number: {}", choReference, claimNumber);

            DetachedCriteria criteria = DetachedCriteria.forClass(BillingInsurer.class);
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
     * @see idas.chox.data.services.BillingInsurerService#deteteObject(idas.chox.core.model.BillingInsurer)
     */
    @Override
    public void deleteObject(BillingInsurer object) {
        try {
            delete(object);
            LOG.debug("deteteObject successful: {} ", object.getScheduleName());
        } catch (RuntimeException re) {
            LOG.error("deleteObject failed: {}", re.getMessage());
            throw re;
        }

    }

    @Override
    public List<Claim> findClaimsforSchedule(Date from, Date to, Insurer insurer,
                        boolean excludeSupplmntInv, String triggerPoint) {
        DetachedCriteria criteria;
        DetachedCriteria auditCriteria;
        
        if (triggerPoint.equals("Manual Invoice Paid")) {
            auditCriteria = DetachedCriteria.forClass(AuditTrail.class)
                .add(Restrictions.between("updateDate", from, to))
                .add(Restrictions.eq("newStatus", ClaimStatus.MANUAL_INVOICE_PAID))
                .add(Restrictions.eq("reverted", Boolean.FALSE))
                .setProjection(Property.forName("claim.id"));
        } else if (triggerPoint.equals("Payment Received")) {
            auditCriteria = DetachedCriteria.forClass(AuditTrail.class)
                .add(Restrictions.between("updateDate", from, to))
                .add(Restrictions.eq("newStatus", ClaimStatus.INVOICE_PAYMENT_RECEIVED))
                .add(Restrictions.eq("reverted", Boolean.FALSE))
                .setProjection(Property.forName("claim.id"));
        } else { // trigger_point = 'Invoice Payment Logged'
            auditCriteria = DetachedCriteria.forClass(AuditTrail.class)
                .add(Restrictions.between("updateDate", from, to))
                .add(Restrictions.eq("newStatus", ClaimStatus.INVOICE_PAYMENT_LOGGED))
                .add(Restrictions.eq("reverted", Boolean.FALSE))
                .setProjection(Property.forName("claim.id"));
        }

        DetachedCriteria billingInsurerDetailCriteria = DetachedCriteria.forClass(BillingInsurerDetail.class)
                .setProjection(Property.forName("claim.id"));
        
        if(excludeSupplmntInv) {
             criteria = DetachedCriteria.forClass(Claim.class)
                .setProjection(Projections.distinct(Projections.projectionList().add(Projections.property("id"))))
                .add(Restrictions.eq("insurer", insurer))
                .add(Property.forName("id").in(auditCriteria))
                .add(Property.forName("id").notIn(billingInsurerDetailCriteria))
                .add(Restrictions.not(Restrictions.in("claimType", ClaimType.getSupplementaryInvoiceTypes())));
//                .add(Restrictions.disjunction()
//                     .add(Restrictions.eq("supplementaryInvoicedClaim", Boolean.FALSE))
//                     .add(Restrictions.conjunction()
//                        .add(Restrictions.eq("supplementaryInvoicedClaim", Boolean.TRUE))
//                        .add(Restrictions.eq("originalSupplementaryInvoicedClaim", Boolean.TRUE))));
        } else {
              criteria = DetachedCriteria.forClass(Claim.class)
                .setProjection(Projections.distinct(Projections.projectionList().add(Projections.property("id"))))
                .add(Restrictions.eq("insurer", insurer))
                .add(Property.forName("id").in(auditCriteria))
                .add(Property.forName("id").notIn(billingInsurerDetailCriteria));
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
     * @see idas.chox.data.services.BillingInsurerService#getScheduleDetailList(int)
     */
    @Override
    public Set<BillingInsurerDetail> getScheduleDetailList(final int id) {
        return (Set<BillingInsurerDetail>) getHibernateTemplate().execute(new HibernateCallback() {

            @Override
            public Object doInHibernate(Session session) throws HibernateException,
                    SQLException {
                LOG.debug("get schedule list");
                BillingInsurer schedule = (BillingInsurer) session.get(BillingInsurer.class, id);
                //log.debug("size in dao"+schedule.getBillingInsurerDetails().size());
                return schedule.getBillingDetails();
            }
        });
    }
}
