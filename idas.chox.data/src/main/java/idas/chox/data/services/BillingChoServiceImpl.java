package idas.chox.data.services;

import java.math.BigDecimal;
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
import org.hibernate.criterion.Subqueries;
import org.springframework.orm.hibernate4.HibernateCallback;

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
            LOG.debug("Checking billing band '{}'", band.getBandName());
            List<ChoBillingBandMapping> choBillingBandMappings = billingBandMappingService.getChoBillingBandMappings(cho.getId(), band.getId());
            
            DetachedCriteria criteria;
            DetachedCriteria billingChoDetailCriteria = DetachedCriteria.forClass(BillingChoDetail.class)
                                                                .createAlias("claim", "c", org.hibernate.sql.JoinType.LEFT_OUTER_JOIN)
                                                                .add(Restrictions.eq("c.chorganisation", cho))
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
            
            for (ChoBillingBandMapping choBillingBandMapping : choBillingBandMappings) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Checking mapping to '{}'", choBillingBandMapping.getInsurer().getName());
                    for (ClaimType c : ClaimType.getClaimTypeList(choBillingBandMapping.getClaimType(), band.isExcludeSupplementary())) {
                        LOG.debug("Looking for claim type: {}", c.toString());   
                    }
                }
                // Determine Claims for each mapping
                if (band.getTriggerStatus().equals(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO) && !band.isExcludeSupplementary()) {
                    // If trigger point is Accepeted Claims and we are billing for supplementaries, then bill at AwaitingInvoiceData (first audit trail entry)
                    criteria = DetachedCriteria.forClass(AuditTrail.class, "at")
                                    .createAlias("claim", "claim", org.hibernate.sql.JoinType.LEFT_OUTER_JOIN)
                                    .add(Restrictions.eq("claim.chorganisation", cho))
                                    .add(Restrictions.eq("claim.insurer", choBillingBandMapping.getInsurer()))
                                    .add(Restrictions.in("claim.claimType", ClaimType.getClaimTypeList(choBillingBandMapping.getClaimType(), band.isExcludeSupplementary())))
                                    .add(Property.forName("claim.id").notIn(billingChoDetailCriteria))
                                    .add(Restrictions.between("at.updateDate", from, to))
                                    .add(Restrictions.eq("at.reverted", Boolean.FALSE))
                                    .add(Restrictions.disjunction().add(Restrictions.eq("at.newStatus", band.getTriggerStatus()))
                                            .add(Restrictions.conjunction().add(Restrictions.eq("at.newStatus", ClaimStatus.CLAIM_AWAITING_INVOICE_DATA))
                                                                            .add(Restrictions.eq("at.originalStatus", ""))
                                                                            .add(Restrictions.in("claim.claimType", ClaimType.getSupplementaryInvoiceTypes()))))
                                    .setProjection(Projections.projectionList().add(Projections.property("at.claim")));
                } else {
                    criteria = DetachedCriteria.forClass(AuditTrail.class, "at")
                                    .createAlias("claim", "claim", org.hibernate.sql.JoinType.LEFT_OUTER_JOIN)
                                    .add(Restrictions.eq("claim.chorganisation", cho))
                                    .add(Restrictions.eq("claim.insurer", choBillingBandMapping.getInsurer()))
                                    .add(Restrictions.in("claim.claimType", ClaimType.getClaimTypeList(choBillingBandMapping.getClaimType(), band.isExcludeSupplementary())))
                                    .add(Property.forName("claim.id").notIn(billingChoDetailCriteria))
                                    .add(Restrictions.between("at.updateDate", from, to))
                                    .add(Restrictions.eq("at.reverted", Boolean.FALSE))
                                    .add(Restrictions.eq("at.newStatus", band.getTriggerStatus()))
                                    .setProjection(Projections.projectionList().add(Projections.property("at.claim")));
                }
                List<Claim> claims = findByCriteria(criteria);
                LOG.debug("Found {} claims", claims.size());

                if (band.isExcludeSupplementary()) {
                    // Add Supplementary Claims whose original claim is closed and no other supplementary billed
                    DetachedCriteria closedOriginalClaims = DetachedCriteria.forClass(Claim.class, "b")
                                    .add(Restrictions.eq("b.chorganisation", cho))
                                    .add(Restrictions.eq("b.insurer", choBillingBandMapping.getInsurer()))
                                    .add(Restrictions.eq("b.claimType", ClaimType.getOriginalSupplementaryClaimType(choBillingBandMapping.getClaimType())))
                                    .add(Property.forName("b.customer").eqProperty("c2.customer"))
                                    .add(Property.forName("b.id").notIn(billingChoDetailCriteria))
                                    .add(Restrictions.in("b.status", ClaimStatus.getClosedUnpaidStatus()))
                                    .setProjection(Projections.projectionList().add(Projections.property("b.id")));
                    
                    DetachedCriteria otherSupplemetaries;
                                        
                    if (band.getTriggerStatus().equals(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO)) {
                        otherSupplemetaries = DetachedCriteria.forClass(AuditTrail.class, "at")
                                    .createAlias("claim", "c", org.hibernate.sql.JoinType.LEFT_OUTER_JOIN)
                                    .add(Restrictions.eq("c.chorganisation", cho))
                                    .add(Restrictions.eq("c.insurer", choBillingBandMapping.getInsurer()))
                                    .add(Restrictions.eq("c.claimType", ClaimType.getSupplementaryClaimType(choBillingBandMapping.getClaimType())))
                                    .add(Restrictions.not(Restrictions.in("c2.status", ClaimStatus.getClosedUnpaidStatus())))
                                    .add(Property.forName("c.customer").eqProperty("c2.customer"))
                                    .add(Restrictions.le("at.updateDate", to))
//                                    .add(Restrictions.between("at.updateDate", from, to))
                                    .add(Restrictions.eq("at.reverted", Boolean.FALSE))
                                    .add(Restrictions.eq("at.newStatus", ClaimStatus.CLAIM_AWAITING_INVOICE_DATA))
                                    .add(Restrictions.or(Property.forName("c.id").in(billingChoDetailCriteria),
                                                         Restrictions.ltProperty("at.updateDate", "at2.updateDate")))
                                    .setProjection(Projections.projectionList().add(Projections.property("c.id")));

                        criteria = DetachedCriteria.forClass(AuditTrail.class, "at2")
                                    .createAlias("claim", "c2", org.hibernate.sql.JoinType.LEFT_OUTER_JOIN)
                                    .add(Restrictions.eq("c2.chorganisation", cho))
                                    .add(Restrictions.eq("c2.insurer", choBillingBandMapping.getInsurer()))
                                    .add(Restrictions.eq("c2.claimType", ClaimType.getSupplementaryClaimType(choBillingBandMapping.getClaimType())))
                                    .add(Restrictions.not(Restrictions.in("c2.status", ClaimStatus.getClosedUnpaidStatus())))
                                    .add(Property.forName("c2.id").notIn(billingChoDetailCriteria))
                                    .add(Restrictions.le("at2.updateDate", to))
//                                    .add(Restrictions.between("at2.updateDate", from, to))
                                    .add(Restrictions.eq("at2.reverted", Boolean.FALSE))
                                    .add(Restrictions.eq("at2.newStatus", ClaimStatus.CLAIM_AWAITING_INVOICE_DATA))
                                    .add(Subqueries.exists(closedOriginalClaims)) // original claim closed and not billed
                                    .add(Subqueries.notExists(otherSupplemetaries))  // no other supplementary at trigger point before the one selected or billed
                                    .setProjection(Projections.projectionList().add(Projections.property("claim")));
                    } else {
                        otherSupplemetaries = DetachedCriteria.forClass(AuditTrail.class, "at")
                                    .createAlias("claim", "c", org.hibernate.sql.JoinType.LEFT_OUTER_JOIN)
                                    .add(Restrictions.eq("c.chorganisation", cho))
                                    .add(Restrictions.eq("c.insurer", choBillingBandMapping.getInsurer()))
                                    .add(Restrictions.eq("c.claimType", ClaimType.getSupplementaryClaimType(choBillingBandMapping.getClaimType())))
                                    .add(Property.forName("c.customer").eqProperty("c2.customer"))
                                    .add(Restrictions.between("at.updateDate", from, to))
                                    .add(Restrictions.eq("at.reverted", Boolean.FALSE))
                                    .add(Restrictions.eq("at.newStatus", band.getTriggerStatus()))
                                    .add(Restrictions.or(Property.forName("c.id").in(billingChoDetailCriteria),
                                                         Restrictions.ltProperty("at.updateDate", "at2.updateDate")))
                                    .setProjection(Projections.projectionList().add(Projections.property("c.id")));
                        criteria = DetachedCriteria.forClass(AuditTrail.class, "at2")
                                    .createAlias("claim", "c2", org.hibernate.sql.JoinType.LEFT_OUTER_JOIN)
                                    .add(Restrictions.eq("c2.chorganisation", cho))
                                    .add(Restrictions.eq("c2.insurer", choBillingBandMapping.getInsurer()))
                                    .add(Restrictions.eq("c2.claimType", ClaimType.getSupplementaryClaimType(choBillingBandMapping.getClaimType())))
                                    .add(Property.forName("c2.id").notIn(billingChoDetailCriteria))
                                    .add(Restrictions.le("at2.updateDate", to))
//                                    .add(Restrictions.between("at2.updateDate", from, to))
                                    .add(Restrictions.eq("at2.reverted", Boolean.FALSE))
                                    .add(Restrictions.eq("at2.newStatus", band.getTriggerStatus()))
                                    .add(Subqueries.exists(closedOriginalClaims)) // original claim closed and not billed
                                    .add(Subqueries.notExists(otherSupplemetaries))  // no other supplementary at trigger point before the one selected or billed
                                    .setProjection(Projections.projectionList().add(Projections.property("claim")));
                    }

                    List<Claim> suppClaims = findByCriteria(criteria);
                    LOG.debug("Found {} supplementary claims with closed original invoice", suppClaims.size());
                    if (suppClaims.size() > 0) {
                        if (LOG.isDebugEnabled()) {
                            for (Claim c : suppClaims) {
                                LOG.debug("Supplementary claim '{}' for {} will be added", c.getChoReference(), c.getInsurer().getName());
                            }
                        }
                        claims.addAll(suppClaims);
                    }
                }
                                
                // Now create a BillingInsurerDetail entry for each claim
                for (Claim claim : claims) {
                    BillingChoDetail billingChoDetail = new BillingChoDetail();
                    billingChoDetail.setClaim(claim);
                    billingChoDetail.setTriggerPoint(triggerPoint);
                    try {
                        Date triggerDate;
                        if (band.isExcludeSupplementary() && ClaimType.isSupplementaryInvoice(claim.getClaimType()) && !ClaimType.isOriginalSupplementaryInvoice(claim.getClaimType()) && band.getTriggerStatus().equals(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO)) {
                            triggerDate= getTriggerDate(claim, ClaimStatus.CLAIM_AWAITING_INVOICE_DATA, null, null);
                        } else if (band.isExcludeSupplementary() && ClaimType.isSupplementaryInvoice(claim.getClaimType()) && !ClaimType.isOriginalSupplementaryInvoice(claim.getClaimType())) {
                            triggerDate= getTriggerDate(claim, band.getTriggerStatus(), null, null);
                        } else if (ClaimType.isSupplementaryInvoice(claim.getClaimType())  && !ClaimType.isOriginalSupplementaryInvoice(claim.getClaimType()) && band.getTriggerStatus().equals(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO)) {
                            triggerDate= getTriggerDate(claim, ClaimStatus.CLAIM_AWAITING_INVOICE_DATA, from, to);
                        } else {
                            triggerDate= getTriggerDate(claim, band.getTriggerStatus(), from, to);
                        }
                        
                        if (triggerDate.after(to)) {
                            LOG.warn("Trigger date {} for claim {} as outside to-date of '{}'", new Object[]{triggerDate.toString(), claim.getChoReference(), to.toString()});
                        }
                        billingChoDetail.setTriggerDate(triggerDate);
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
                    && (from == null || auditEntry.getCreatedDate().after(from)) && (to == null || auditEntry.getCreatedDate().before(to))) {
                return auditEntry.getCreatedDate();
            }
     
        }
        throw new Exception("Cannot find trigger date for claim '" + claim.getChoReference() + "' [id=" + claim.getId() + "] for status '" 
                + triggerStatus + "{' between " + (from == null ? "(null)" : from.toString()) + " and " + (to == null ? "(null)" : to.toString() + "."));
    }

    /* (non-Javadoc)
     * @see idas.chox.data.services.BillingChoService#getScheduleDetailList(int)
     */
    @Override
    public Set<BillingChoDetail> getScheduleDetailList(final int id) {
        return (Set<BillingChoDetail>) getHibernateTemplate().execute(new HibernateCallback() {

            @Override
            public Object doInHibernate(Session session) throws HibernateException {
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
