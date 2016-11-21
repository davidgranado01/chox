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
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.orm.hibernate3.HibernateCallback;

import idas.chox.core.model.AuditTrail;
import idas.chox.core.model.Billing;
import idas.chox.core.model.BillingInsurer;
import idas.chox.core.model.BillingInsurerDetail;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.InsurerBillingBand;
import idas.chox.core.model.InsurerBillingBandMapping;
import idas.chox.core.services.AuditTrailService;
import idas.chox.core.services.BillingBandService;
import idas.chox.core.services.BillingBandMappingService;
import idas.chox.core.services.BillingInsurerService;
import idas.chox.core.util.CalcHelper;
import idas.chox.core.util.DateHelper;


public class BillingInsurerServiceImpl extends SecureDataService implements BillingInsurerService {

    private static final Logger LOG = LoggerFactory.getLogger(BillingInsurerServiceImpl.class);
    private BillingBandService billingBandService;
    private BillingBandMappingService billingBandMappingService;
    private AuditTrailService auditTrailService;

    public void setBillingBandService(BillingBandService billingBandService) {
        this.billingBandService = billingBandService;
    }

    public void setBillingBandMappingService(BillingBandMappingService billingBandMappingService) {
        this.billingBandMappingService = billingBandMappingService;
    }

    public void setAuditTrailService(AuditTrailService auditTrailService) {
        this.auditTrailService = auditTrailService;
    }
    
    /* (non-Javadoc)
     * @see idas.chox.data.services.BillingInsurerService#checkObject(java.lang.String, java.util.Date, java.util.Date)
     */
    @Override
    public Map checkObject(String scheduleName, Date dateFrom, Date dateTo, int insurerId) {
        Map checks = new HashMap();
        DetachedCriteria dc1 = DetachedCriteria.forClass(BillingInsurer.class).add(Restrictions.eq("scheduleName", scheduleName));
        List result1 = getHibernateTemplate().findByCriteria(dc1);
        if (result1 != null && result1.size() > 0) {
            checks.put("scheduleName", "Schedule name already exists.");
        }

        checkScheduleOverlap(checks, dateFrom, dateTo, insurerId);
        return checks;
    }

    private String getShDtStr(Date date) {
        DateFormat overlap_literal_format = new SimpleDateFormat("yyyy-MM-dd");
        return overlap_literal_format.format(date);
    }

    public void checkScheduleOverlap(Map checkmap, Date dateFrom, Date dateTo, int insurerId) {

        StringBuilder sb = new StringBuilder();
        sb.append("select distinct");
        sb.append("(date_from,date_to) ");
        sb.append("overlaps ");
        sb.append("(date(:pDateFrom)");
        sb.append(",date(:pDateTo)) ");
        sb.append("from billing_insurer ");
        sb.append("where insurer_id = :pInsurerId ");
        
        String query = sb.toString();
        
        Map extParameters = new HashMap();
        extParameters.put("pDateFrom", DateHelper.getDBDateFormat().format(dateFrom));
        extParameters.put("pDateTo", DateHelper.getDBDateFormat().format(dateTo));
        extParameters.put("pInsurerId", insurerId);
        
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
        List<BillingInsurer> list = new ArrayList<>();

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
        List list = new ArrayList<>();

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
    public List<BillingInsurerDetail> findClaimsforSchedule(Date from, Date to, Insurer insurer) {
        List<BillingInsurerDetail> results = new ArrayList<>();
        LOG.debug("Finding claims for '{}' from {} to {}", new Object[]{insurer.getName(), from.toString(), to.toString()});
        // Get Insurer Billing Bands
        List<InsurerBillingBand> insurerBillingBands = billingBandService.getInsurerBillingBands(insurer.getId());

        // For each Band, get CHOs mapped to band
        for(InsurerBillingBand band : insurerBillingBands) {
            LOG.debug("Checking billing band '{}'", band.getBandName());
            List<InsurerBillingBandMapping> insurerBillingBandMappings = billingBandMappingService.getInsurerBillingBandMappings(insurer.getId(), band.getId());
            DetachedCriteria criteria;
            DetachedCriteria billingInsurerDetailCriteria = DetachedCriteria.forClass(BillingInsurerDetail.class)
                                                                .createAlias("claim", "c", CriteriaSpecification.LEFT_JOIN)
                                                                .add(Restrictions.eq("c.insurer", insurer))
                                                                .setProjection(Property.forName("c.id"));
            String triggerPoint;
            switch (band.getTriggerStatus()) {
                case "ManualInvoicePaid":
                    triggerPoint = "Manual Invoice Paid";
                    break;
                case "PaymentReceived":
                    triggerPoint = "Payment Received";
                    break;
                case "AwaitingCarHireInfo":
                    triggerPoint = "Accepted Claims";
                    break;
                case "InvoicePaymentLogged":
                    triggerPoint = "Invoice Payment Logged";
                    break;
                default:
                    triggerPoint = "unknown";
                    break;
            }

            for (InsurerBillingBandMapping insurerBillingBandMapping : insurerBillingBandMappings) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Checking mapping to '{}'", insurerBillingBandMapping.getChorganisation().getName());
                    for (ClaimType c : ClaimType.getClaimTypeList(insurerBillingBandMapping.getClaimType(), band.isExcludeSupplementary())) {
                        LOG.debug("Looking for claim type: {}", c.toString());   
                    }
                }
                criteria = DetachedCriteria.forClass(AuditTrail.class, "at")
                                    .createAlias("claim", "claim", CriteriaSpecification.LEFT_JOIN)
                                    .add(Restrictions.eq("claim.insurer", insurer))
                                    .add(Restrictions.eq("claim.chorganisation", insurerBillingBandMapping.getChorganisation()))
                                    .add(Restrictions.in("claim.claimType", ClaimType.getClaimTypeList(insurerBillingBandMapping.getClaimType(), band.isExcludeSupplementary())))
                                    .add(Property.forName("claim.id").notIn(billingInsurerDetailCriteria))
                                    .add(Restrictions.between("at.updateDate", from, to))
                                    .add(Restrictions.eq("at.reverted", Boolean.FALSE))
                                    .add(Restrictions.eq("at.newStatus", band.getTriggerStatus()))
                                    .setProjection(Projections.projectionList().add(Projections.property("at.claim")));
                List<Claim> claims = findByCriteria(criteria);
                LOG.debug("Found {} claims", claims.size());

                if (band.isExcludeSupplementary()) {
                    // Add Supplementary Claims whose original claim is closed and no other supplementary billed
                    DetachedCriteria closedOriginalClaims = DetachedCriteria.forClass(Claim.class, "b")
                                    .add(Restrictions.eq("b.insurer", insurer))
                                    .add(Restrictions.eq("b.chorganisation", insurerBillingBandMapping.getChorganisation()))
                                    .add(Restrictions.in("b.claimType", ClaimType.getClaimTypeList(insurerBillingBandMapping.getClaimType(), band.isExcludeSupplementary())))
                                    .add(Property.forName("b.customer").eqProperty("c2.customer"))
                                    .add(Property.forName("b.id").notIn(billingInsurerDetailCriteria))
                                    .add(Restrictions.in("b.status", ClaimStatus.getClosedUnpaidStatus()))
                                    .setProjection(Projections.projectionList().add(Projections.property("b.id")));
                    
                    DetachedCriteria otherSupplemetaries = DetachedCriteria.forClass(AuditTrail.class, "at")
                                    .createAlias("claim", "c", CriteriaSpecification.LEFT_JOIN)
                                    .add(Restrictions.eq("c.insurer", insurer))
                                    .add(Restrictions.eq("c.chorganisation", insurerBillingBandMapping.getChorganisation()))
                                    .add(Restrictions.eq("c.claimType", ClaimType.getSupplementaryClaimType(insurerBillingBandMapping.getClaimType())))
                                    .add(Property.forName("c.customer").eqProperty("c2.customer"))
                                    .add(Restrictions.between("at.updateDate", from, to))
                                    .add(Restrictions.eq("at.reverted", Boolean.FALSE))
                                    .add(Restrictions.eq("at.newStatus", band.getTriggerStatus()))
                                    .add(Restrictions.or(Property.forName("c.id").in(billingInsurerDetailCriteria),
                                                         Restrictions.ltProperty("at.updateDate", "at2.updateDate")))
                                    .setProjection(Projections.projectionList().add(Projections.property("c.id")));
                    
                    criteria = DetachedCriteria.forClass(AuditTrail.class, "at2")
                                    .createAlias("claim", "c2", CriteriaSpecification.LEFT_JOIN)
                                    .add(Restrictions.eq("c2.insurer", insurer))
                                    .add(Restrictions.eq("c2.chorganisation", insurerBillingBandMapping.getChorganisation()))
                                    .add(Restrictions.eq("c2.claimType", ClaimType.getSupplementaryClaimType(insurerBillingBandMapping.getClaimType())))
                                    .add(Property.forName("c2.id").notIn(billingInsurerDetailCriteria))
                                    .add(Restrictions.between("at2.updateDate", from, to))
                                    .add(Restrictions.eq("at2.reverted", Boolean.FALSE))
                                    .add(Restrictions.eq("at2.newStatus", band.getTriggerStatus()))
                                    .add(Subqueries.exists(closedOriginalClaims)) // original claim closed and not billed
                                    .add(Subqueries.notExists(otherSupplemetaries))  // no other supplementary at trigger point before the one selected or billed
                                    .setProjection(Projections.projectionList().add(Projections.property("claim")));

                    List<Claim> suppClaims = findByCriteria(criteria);
                    LOG.debug("Found {} supplementary claims with closed original invoice", suppClaims.size());
                    if (suppClaims.size() > 0) {
                        claims.addAll(suppClaims);
                    }
                }

                // Now create a BillingInsurerDetail entry for each claim
                for (Claim claim : claims) {
                    BillingInsurerDetail billingInsurerDetail = new BillingInsurerDetail();
                    billingInsurerDetail.setClaim(claim);
                    billingInsurerDetail.setTriggerPoint(triggerPoint);
                    try {
                        billingInsurerDetail.setTriggerDate(getTriggerDate(claim, band.getTriggerStatus(), from, to));
                    } catch (Exception ex) {
                        LOG.error("Cannot set trigger date: {}", ex.getMessage());
                    }
                    billingInsurerDetail.setBillAmount(band.getCostPerClaim());
                    billingInsurerDetail.setVatOnBillAmount(band.getCostPerClaim().multiply(CalcHelper.getVatRate(to)).setScale(2, BigDecimal.ROUND_HALF_UP));
                    billingInsurerDetail.setGrossBillAmount(band.getCostPerClaim().add(billingInsurerDetail.getVatOnBillAmount()));
                    billingInsurerDetail.setAmountReceived(BigDecimal.ZERO);
                    
                    results.add(billingInsurerDetail);
                }
            }
        }
        
        LOG.debug("Return total of {} claims", results.size());
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
                return schedule.getBillingDetails();
            }
        });
    }
}
