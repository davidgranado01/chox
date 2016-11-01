package idas.chox.data.services;

import java.math.BigDecimal;
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
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import idas.chox.core.model.AuditTrail;
import idas.chox.core.model.BillingCho;
import idas.chox.core.model.BillingChoDetail;
import idas.chox.core.model.ChoBillingBand;
import idas.chox.core.model.ChoBillingBandMapping;
import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.services.AuditTrailService;
import idas.chox.core.services.BillingBandMappingService;
import idas.chox.core.services.BillingBandService;
import idas.chox.core.services.BillingChoService;
import idas.chox.core.util.CalcHelper;
import idas.chox.core.util.DateHelper;


public class BillingChoServiceImpl extends SecureDataService implements BillingChoService {

    private static final Logger LOG = LoggerFactory.getLogger(BillingChoServiceImpl.class);
    private BillingBandService billingBandService;
    private BillingBandMappingService billingBandMappingService;
    private AuditTrailService auditTrailService;
    
    public void setAuditTrailService(AuditTrailService auditTrailService) {
        this.auditTrailService = auditTrailService;
    }

    public void setBillingBandService(BillingBandService billingBandService) {
        this.billingBandService = billingBandService;
    }

    public void setBillingBandMappingService(BillingBandMappingService billingBandMappingService) {
        this.billingBandMappingService = billingBandMappingService;
    }

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
        List list = new ArrayList<>();
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
        List list = new ArrayList<>();

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

    @Override
    public List<BillingChoDetail> findClaimsforSchedule(Date from, Date to, Chorganisation cho) {
        List<BillingChoDetail> results = new ArrayList<>();
        
        // Get Insurer Billing Bands
        List<ChoBillingBand> choBillingBands = billingBandService.getChoBillingBands(cho.getId());
        
        // For each Band, get CHOs mapped to band
        for(ChoBillingBand band : choBillingBands) {
            List<ChoBillingBandMapping> choBillingBandMappings = billingBandMappingService.getChoBillingBandMappings(cho.getId(), band.getId());
            String triggerPoint;
            
            DetachedCriteria criteria;
            DetachedCriteria auditCriteria;
            DetachedCriteria billingChoDetailCriteria = DetachedCriteria.forClass(BillingChoDetail.class)
                                                                .setProjection(Property.forName("claim.id"));
             
            switch (band.getTriggerStatus()) {
                case "ManualInvoicePaid":
                    auditCriteria = DetachedCriteria.forClass(AuditTrail.class)
                            .add(Restrictions.between("updateDate", from, to))
                            .add(Restrictions.eq("newStatus", ClaimStatus.MANUAL_INVOICE_PAID))
                            .add(Restrictions.eq("reverted", Boolean.FALSE))
                            .setProjection(Property.forName("claim.id"));
                    triggerPoint = "Manual Invoice Paid";
                    break;
                case "PaymentReceived":
                    auditCriteria = DetachedCriteria.forClass(AuditTrail.class)
                            .add(Restrictions.between("updateDate", from, to))
                            .add(Restrictions.eq("newStatus", ClaimStatus.INVOICE_PAYMENT_RECEIVED))
                            .add(Restrictions.eq("reverted", Boolean.FALSE))
                            .setProjection(Property.forName("claim.id"));
                    triggerPoint = "Payment Received";
                    break;
                case "AwaitingCarHireInfo":
                    auditCriteria = DetachedCriteria.forClass(AuditTrail.class)
                            .add(Restrictions.between("updateDate", from, to))
                            .add(Restrictions.eq("newStatus", ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO))
                            .add(Restrictions.eq("reverted", Boolean.FALSE))
                            .setProjection(Property.forName("claim.id"));
                    triggerPoint = "Accepted Claims";
                    break;
                case "InvoicePaymentLogged":
                    auditCriteria = DetachedCriteria.forClass(AuditTrail.class)
                            .add(Restrictions.between("updateDate", from, to))
                            .add(Restrictions.eq("newStatus", ClaimStatus.INVOICE_PAYMENT_LOGGED))
                            .add(Restrictions.eq("reverted", Boolean.FALSE))
                            .setProjection(Property.forName("claim.id"));
                    triggerPoint = "Invoice Payment Logged";
                    break;
                default:
                    // unknown trigger point
                    LOG.error("Unknown trigger point: {}", band.getTriggerStatus());
                    return results;
            }
            
            for (ChoBillingBandMapping choBillingBandMapping : choBillingBandMappings) {
                // Determine Claims for each mapping
                criteria = DetachedCriteria.forClass(Claim.class)
//                                    .setProjection(Projections.distinct(Projections.projectionList().add(Projections.property("id"))))
                                    .add(Restrictions.eq("insurer", choBillingBandMapping.getInsurer()))
                                    .add(Restrictions.eq("chorganisation", cho))
                                    .add(Restrictions.in("claimType", ClaimType.getClaimTypeList(choBillingBandMapping.getClaimType(), band.isExcludeSupplementary())))
                                    .add(Property.forName("id").in(auditCriteria))
                                    .add(Property.forName("id").notIn(billingChoDetailCriteria));
                List<Claim> claims = findByCriteria(criteria);
                                
                // Now create a BillingInsurerDetail entry for each claim
                for (Claim claim : claims) {
                    BillingChoDetail billingChoDetail = new BillingChoDetail();
                    billingChoDetail.setClaim(claim);
                    billingChoDetail.setTriggerPoint(triggerPoint);
                    try {
                        billingChoDetail.setTriggerDate(getTriggerDate(claim, band.getTriggerStatus(), from, to));
                    } catch (Exception ex) {
                        LOG.error("Cannot set trigger date: {}", ex.getMessage());
                    }
                    billingChoDetail.setBillAmount(band.getCostPerClaim());
                    billingChoDetail.setVatOnBillAmount(band.getCostPerClaim().multiply(CalcHelper.getVatRate(to)).setScale(2, BigDecimal.ROUND_HALF_UP));
                    billingChoDetail.setGrossBillAmount(band.getCostPerClaim().add(billingChoDetail.getVatOnBillAmount()));
                    billingChoDetail.setAmountReceived(BigDecimal.ZERO);
                    
                    results.add(billingChoDetail);
                }
            }
        }
        
        return results;
    }

    private Date getTriggerDate(Claim claim, String triggerStatus, Date from, Date to) throws Exception {
        Date result;
        List<AuditTrail> auditTrail = auditTrailService.getAuditTrailByClaim(claim.getId());
        
        for (AuditTrail auditEntry : auditTrail) {
            if (auditEntry.getNewStatus().equals(triggerStatus) && !auditEntry.getReverted()
                    && auditEntry.getCreatedDate().after(from) && auditEntry.getCreatedDate().before(to)) {
                return auditEntry.getCreatedDate();
            }
     
        }
        throw new Exception("Cannot find trigger date for claim '" + claim.getChoReference() + "' [id=" + claim.getId() + "] for status '" 
                + triggerStatus + "{' between " + from.toString() + " and " + to.toString() + ".");
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
        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class).add(Restrictions.eq("chorganisation", cho));
        criteria.createCriteria("invoice").add(Restrictions.between("createdDate", dateFrom, dateTo));
        int numberOfInvoicesSubmitted = findByCriteria(criteria).size();

        LOG.debug("Number of invoices submitted: {}", numberOfInvoicesSubmitted);
        return numberOfInvoicesSubmitted;
    }
}
