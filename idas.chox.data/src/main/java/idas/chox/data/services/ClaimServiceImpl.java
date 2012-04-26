package idas.chox.data.services;

import idas.chox.core.common.OrganisationType;
import idas.chox.core.model.*;
import idas.chox.core.search.ClaimSearchCriteria;
import idas.chox.core.search.SearchResult;
import idas.chox.core.services.AuditTrailService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.CommentService;
import idas.chox.core.util.DateHelper;
import idas.chox.core.util.RoleHelper;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


public class ClaimServiceImpl extends SecureDataService implements ClaimService, Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(ClaimServiceImpl.class);
    private AuditTrailService auditTrailService;
    private CommentService commentService;
    public static final String PENDING = "Pending";
    public static final String IN_PROGRESS = "InProgress";
    public static final String COMPLETE = "Complete";
    public static final String CANCELLED = "Cancelled";
    public static final String NEW_CLAIM = "1st Notification";

    public void setAuditTrailService(AuditTrailService auditTrailService) {
        this.auditTrailService = auditTrailService;
    }

    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }

    public ClaimServiceImpl() {
        super();
    }

    @Override
    public Claim getClaim(int id) {
        return (Claim) get(Claim.class, id);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public void updateClaim(Claim claim) {
        save(claim);
        LOG.debug("Claim updated and saved.");
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public void updateSaveLiabilityStatus(Claim claim) {
        save(claim);
    }

    public void save(Claim object) {
        updateLiabilityPayment(object);
        super.save(object);
    }

    /*
     * Use below method in model driven action where checkVersion validation
     * faild, but model is updated by struts(eg. new value from ui set to model
     * properties) before checkVersion validation done. This method will evict
     * the dirty model from hibernate session (to avoid persisting dirty object
     * to DB by hibernate) and return the model which is loaded from the DB.
     */
    @Override
    public Claim updateClaimWithInvalidSessionVersion(Claim claim) {
        evict(claim);
        return (Claim) getSession().load(Claim.class, claim.getId());
    }
    
//    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public Boolean revertClaim(int id) {
        Boolean result = false;
        AuditTrail auditTrail;
        if ((auditTrail = auditTrailService.getLastChange(id)) != null) {
            Claim claim = (Claim) get(Claim.class, id);


            if (ClaimStatus.SUBSCRIBER_CLAIM_REJECTED.equals(auditTrail.getOriginalStatus()) && !ClaimType.isSubscriber(claim.getClaimType())) {
                LOG.warn("Cannot revert non-subscriber claim back to 'SubscriberClaimRejected'");
            } else {
                claim.setStatus(auditTrail.getOriginalStatus());
                if (claim.getStatus().equals(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA) && claim.getInvoice() != null) {
                    LOG.debug("This claim has invoice and will be deleted as reverting the status");
                    Invoice oldInvoice = claim.getInvoice();
                    claim.setInvoice(null);
                    LOG.debug("claim invoice set to null");
                    delete(oldInvoice);
                    LOG.debug("claim invoice deleted");
                }
                /*
                 *  This fix is for BUG#1306 Reverting from 'PaymentReceived' should take into account the interim payment status
                 */
                if (claim.getStatus().equals(ClaimStatus.INVOICE_PAYMENT_LOGGED)) {
                    claim.getInvoice().setTotalToPay(claim.getInvoice().getFullTotalToPay());
                    
                    if (claim.getInvoice().isInterimPaymentReceivedFullAndFinal())
                        claim.getInvoice().setInterimPaymentReceivedFullAndFinal(false);
                }
                if (claim.getStatus().equals(ClaimStatus.AWAITING_INVOICE_PAYMENT)) {
                    claim.getInvoice().setHireGrossPaid(BigDecimal.ZERO);
                    claim.getInvoice().setRepairGrossPaid(BigDecimal.ZERO);
                    claim.getInvoice().setEngineerFeeGrossPaid(BigDecimal.ZERO);
                    claim.getInvoice().setTotalLossFeeGrossPaid(BigDecimal.ZERO);
                    claim.getInvoice().setStorageRecoveryGrossPaid(BigDecimal.ZERO);
                    claim.getInvoice().setHirePenaltyChargePaid(BigDecimal.ZERO);
                    claim.getInvoice().setRepairPenaltyChargePaid(BigDecimal.ZERO);
                    claim.getInvoice().setFinalPayment(null);
                }

                auditTrailService.revertAuditEntry(auditTrail.getId());
                LOG.debug("Audit entry reverted and saved - saving claim");
                save(claim);
                flush();
                // Now we need to set the correct status modified date (bug#1029) - to do this, we need to get the
                // last (not reverted!) audit trail entry again
                if ((auditTrail = auditTrailService.getLastChange(id)) != null) {
                    LOG.debug("Claim status reverted and saved - updating statusModifiedDate to '{}'", auditTrail.getCreatedDate());
                    claim.setStatusModifiedDate(auditTrail.getCreatedDate());
                    super.save(claim);
                    LOG.debug("Claim status modified date saved.");
                }
                result = true;
                if (ClaimType.isSubscriber(claim.getClaimType()) && claim.getStatus().equals(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO)) {
                    result = revertClaim(id);
                }
            }
        } else {
            LOG.warn("Could not revert claim status.");
        }

        return result;
    }

    @Override
    public Long getECDCountByClaimId(int claimId) {
        String q = "select count(*) from HireMonitoringEcd where claim.id = '" + claimId + "'";
        return getCount(q);
    }

    @Override
    public Long getClaimCountByClaimNumber(String claimNumber, int claimId) {
        String q = "select count(*) from Claim where claimNumber = '" + claimNumber + "' And id != '" + claimId + "'";
        return getCount(q);
    }

    @Override
    public List getClaimsByCustomerClaimRef(String customerClaimRef, int choId) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
        criteria.createCriteria("customer").add(Restrictions.like("claimReference", customerClaimRef).ignoreCase());
        criteria.add(Restrictions.eq("chorganisation.id", choId));
        criteria.addOrder(Order.asc("createdDate"));
        return findByCriteria(criteria);
    }

    @Override
    public List getOtherClaimsByClaimNumber(String claimNumber, int claimId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
        criteria.add(Restrictions.eq("claimNumber", claimNumber));
        criteria.add(Restrictions.ne("id", claimId));
        List result = this.findByCriteria(criteria);
        return result;
    }

    @Override
    public List getDuplicateSupplementaryInvoiceClaims(String customerClaimRef, int claimId) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
        criteria.createCriteria("customer").add(Restrictions.like("claimReference", customerClaimRef).ignoreCase());
        criteria.add(Restrictions.in("claimType", ClaimType.getAllSupplementaryInvoiceTypes()));

//        criteria.add(Restrictions.eq("supplementaryInvoicedClaim", true));
        criteria.add(Restrictions.ne("id", claimId));
        if (getSecurityInfoProvider().getIsCHO()) {
            criteria.add(Restrictions.eq("chorganisation.id", getSecurityInfoProvider().getCurrentUser().getChorganisation().getId()));
        } else if (getSecurityInfoProvider().getIsINS()) {
            criteria.add(Restrictions.eq("insurer.id", getSecurityInfoProvider().getCurrentUser().getInsurer().getId()));
        }

        criteria.addOrder(Order.asc("createdDate"));
        return findByCriteria(criteria);
    }

    @Override
    public Integer getCountOfClaimByVRN(String strVRN, int claimId) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
        criteria.setProjection(Projections.rowCount());
        criteria.createCriteria("customer").add(Restrictions.like("vehicleRegistration", strVRN).ignoreCase());
        criteria.add(Restrictions.ne("id", claimId));
        List result = findByCriteria(criteria);
        return ((Long) result.get(0)).intValue();


    }

    // this method has been implemented for TPI claim as there is no claim id already exist in the database.
    // and it will still check if there is any claim which has customer with same vrn number in some other claim.
    // if same vrn exist (if the count more than 0) then rule no-21 will get failed.
    @Override
    public Integer getCountOfClaimByVRNforNewClaim(String strVRN, Claim claim) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
        criteria.setProjection(Projections.rowCount());
        criteria.createCriteria("customer").add(Restrictions.like("vehicleRegistration", strVRN).ignoreCase());
        // at some point this method need to be removed and use the getCountOfClaimByVRN(String strVRN, int claimId) above method.
        // instead checking claim.getAvailableStatus()!=null should check the claim existence in the system. this change has to be added to the above mentioned method.
        // depricated hibernate method should be removed.
        if (claim.getStatus() != null) {
            criteria.add(Restrictions.ne("id", claim.getId()));
        }
        List result = findByCriteria(criteria);
        return ((Long) result.get(0)).intValue();


    }

    @Override
    public Claim getClaimByCHOReferenceNumber(String sClaimReferenceNumber) {
        Claim claim;
        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
        criteria.add(Restrictions.eq("choReference", sClaimReferenceNumber.trim()).ignoreCase());
        claim = (Claim) getByCriteria(criteria);
        return claim;
    }
    
    @Override
    public Claim getClaimByChoIdAndCHOReferenceNumber(Integer choId, String sClaimReferenceNumber) {
        Claim claim;
        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
        criteria.add(Restrictions.eq("choReference", sClaimReferenceNumber.trim()).ignoreCase());
        criteria.add(Restrictions.eq("chorganisation.id", choId));
        claim = (Claim) getByCriteria(criteria);
        return claim;
    }

    @Override
    public Boolean isCustomerClaimNumberExist(String strClaimNumber, int claimId, Boolean isClaimExit) {

        Boolean bFlag = false;

        if (!strClaimNumber.equalsIgnoreCase("")) {
            DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
            criteria.setProjection(Projections.rowCount());
            criteria.createCriteria("customer").add(Restrictions.like("claimReference", strClaimNumber).ignoreCase());
            if (isClaimExit) {
                criteria.add(Restrictions.ne("id", claimId));
            }
            List result = findByCriteria(criteria);

            Integer totalCount = ((Long) result.get(0)).intValue();
            bFlag = totalCount > 0;
        }

        return bFlag;

    }

    @Override
    public Boolean isThirdPartyClaimNumberExist(String strClaimNumber, int claimId, Boolean isClaimExit) {

        Boolean bFlag = false;

        if (!strClaimNumber.equalsIgnoreCase("")) {
            DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
            criteria.setProjection(Projections.rowCount());
            criteria.createCriteria("thirdParty").add(Restrictions.like("claimReference", strClaimNumber).ignoreCase());
            if (isClaimExit) {
                criteria.add(Restrictions.ne("id", claimId));
            }
            List result = findByCriteria(criteria);

            Integer totalCount = ((Long) result.get(0)).intValue();
            bFlag = totalCount > 0;
        }

        return bFlag;
    }

    @Override
    public SearchResult searchClaims(ClaimSearchCriteria searchCriteria) {
        //return searchClaims(searchCriteria, 0, Integer.MAX_VALUE, "", "");
        return searchClaims(searchCriteria, 0, Integer.MAX_VALUE, "created", "desc");
    }

    @Override
    public SearchResult searchClaims(ClaimSearchCriteria searchCriteria, int start, int limit, String sort, String dir) {
        Criteria criteria = buildSearchCriteria(searchCriteria);
        Integer totalCount = countClaims(criteria);
        LOG.debug("Searching with criteria: {}", searchCriteria.toString());

        if (!sort.isEmpty() && !dir.isEmpty()) {
            if (sort.equalsIgnoreCase("supplierReference")) {
                addSort(criteria, "choReference", dir);
            } else if (sort.equalsIgnoreCase("vehicleRegistration")) {
                addSort(criteria, "tp.vehicleRegistration", dir);
                addSort(criteria, "choReference", dir);
            } else if (sort.equalsIgnoreCase("claimNumber")) {
                addSort(criteria, "claimNumber", dir);
                addSort(criteria, "choReference", dir);
            } else if (sort.equalsIgnoreCase("claimType")) {
                addSort(criteria, "claimType", dir);
                addSort(criteria, "choReference", dir);
            } else if (sort.equalsIgnoreCase("policyNumber")) {
                addSort(criteria, "tp.policyNumber", dir);
                addSort(criteria, "choReference", dir);
            } else if (sort.equalsIgnoreCase("invoiceAmount")) {
                addSort(criteria, "iv.totalToPay", dir);
                addSort(criteria, "choReference", dir);
            } else if (sort.equalsIgnoreCase("createdDate")) {
                addSort(criteria, "createdDate", dir);
                addSort(criteria, "choReference", dir);
            } else if (sort.equalsIgnoreCase("status")) {
                addSort(criteria, "status", dir);
                addSort(criteria, "choReference", dir);
            } else if (sort.equalsIgnoreCase("statusModifiedDate")) {
                addSort(criteria, "statusModifiedDate", dir);
                addSort(criteria, "choReference", dir);
            } else if (sort.equalsIgnoreCase("workgroup")) {
                addSort(criteria, "wg.name", dir);
                addSort(criteria, "choReference", dir);
            } else if (sort.equalsIgnoreCase("cho")) {
                addSort(criteria, "cho.name", dir);
                addSort(criteria, "choReference", dir);
            } else if (sort.equalsIgnoreCase("insurer")) {
                addSort(criteria, "ins.name", dir);
                addSort(criteria, "choReference", dir);
            } else if (sort.equalsIgnoreCase("reviewDate")) {
                addSort(criteria, "hmd.nextReviewDate", dir);
                addSort(criteria, "choReference", dir);
            } else if (sort.equalsIgnoreCase("invoiceUploadDate")) {
                addSort(criteria, "iv.createdDate", dir);
                addSort(criteria, "choReference", dir);
            } else if (sort.equalsIgnoreCase("ownerName")) {
                addSort(criteria, "co.firstName", dir);
                addSort(criteria, "co.lastName", dir);
                addSort(criteria, "choReference", dir);
            } else if (sort.equalsIgnoreCase("choOwnerName")) {
                addSort(criteria, "sco.firstName", dir);
                addSort(criteria, "sco.lastName", dir);
                addSort(criteria, "choReference", dir);
            } else if (sort.equalsIgnoreCase("createdBy")) {
                addSort(criteria, "cb.firstName", dir);
                addSort(criteria, "cb.lastName", dir);
                addSort(criteria, "choReference", dir);
            } else {
                addSort(criteria, "lastModifiedDate", dir);
                addSort(criteria, "choReference", dir);
            }
        }

        criteria.setFirstResult(start);
        criteria.setMaxResults(limit);
        
        criteria.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<HashMap> resultMap = criteria.list();

        List claims = new ArrayList<Claim>();

        for (HashMap m : resultMap) {
            claims.add(m.get("this"));
        }

        LOG.debug("Returning search result - {} claims found (totalCount={})", claims.size(), totalCount);
        return new SearchResult(claims, totalCount);
    }

    @Override
    public Integer countClaims(ClaimSearchCriteria searchCriteria) {

        Criteria criteria = buildSearchCriteria(searchCriteria);
        return countClaims(criteria);
    }

    @Override
    public Boolean isClaimSupplierReferenceNumberExist(String sClaimReferenceNumber) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
        criteria.setProjection(Projections.rowCount());
        criteria.add(Restrictions.like("choReference", sClaimReferenceNumber.trim()).ignoreCase());
        List result = findByCriteria(criteria);

        Integer totalCount = ((Long) result.get(0)).intValue();

        return totalCount > 0;
    }

    @Override
    public Boolean isObjectExist(int WorkgroupId) {

        boolean isExist = false;

        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);

        criteria.add(Restrictions.eq("workgroup.id", WorkgroupId));

        if (findByCriteria(criteria).size() > 0) {
            isExist = true;
        }

        return isExist;
    }

    @Override
    public boolean isOpenClaimByWorkgroupsByStatusExist(int insurerId, Set WorkgroupIds, String status) {

        boolean isExist = false;

        if (WorkgroupIds.size() > 0) {
            Iterator itr = WorkgroupIds.iterator();
            while (itr.hasNext()) {

                int workgroupId = (Integer) itr.next();
                if (isOpenClaimByWorkgroupIdByStatusExist(insurerId, workgroupId, status)) {
                    isExist = true;
                    break;
                }
            }
        }

        return isExist;

    }

    private boolean isOpenClaimByWorkgroupIdByStatusExist(int insurerId, Integer WorkgroupId, String status) {

        boolean isExist = false;

        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
        criteria.add(Restrictions.eq("insurer.id", insurerId));
        criteria.add(Restrictions.eq("workgroup.id", WorkgroupId));
        criteria.add(Restrictions.eq("status", status));

        if (findByCriteria(criteria).size() > 0) {
            isExist = true;
        }

        return isExist;
    }

    @Override
    public boolean isOpenClaimByWorkgroupExist(int WorkgroupId) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
        criteria.add(Restrictions.eq("workgroup.id", WorkgroupId));

        for (String sStatus : ClaimStatus.getInsurerClosedStatus(true)) {
            criteria.add(Restrictions.ne("status", sStatus));
        }

        if (findByCriteria(criteria).size() > 0) {
            return true;
        }

        return false;

    }

    @Override
    public boolean isOpenClaimByWorkgroupsByUserExist(int insurerId, Set WorkgroupIds, int userId) {

        boolean isExist = false;

        if (WorkgroupIds.size() > 0) {
            Iterator itr = WorkgroupIds.iterator();
            while (itr.hasNext()) {

                int workgroupId = (Integer) itr.next();
                if (isOpenClaimByWorkgroupIdByUserExist(insurerId, workgroupId, userId)) {

                    isExist = true;
                    break;
                }
            }
        }

        return isExist;

    }

    @Override
    public boolean isOpenClaimByWorkgroupIdByUserExist(int insurerId, int WorkgroupId, int UserId) {

        boolean isExist = false;
        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
        criteria.add(Restrictions.eq("workgroup.id", WorkgroupId));
        criteria.add(Restrictions.eq("insurer.id", insurerId));

        if (UserId > 0) {
            criteria.add(Restrictions.eq("claimOwner.id", UserId));
        }

        for (String sStatus : ClaimStatus.getInsurerClosedStatus(true)) {
            criteria.add(Restrictions.ne("status", sStatus));
        }

        if (findByCriteria(criteria).size() > 0) {
            isExist = true;
        }

        return isExist;

    }

    @Override
    public boolean isUserHasOpenClaim(int userId) {

        boolean isExist = false;

        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
        criteria.add(Restrictions.eq("claimOwner.id", userId));

        for (String sStatus : ClaimStatus.getInsurerClosedStatus(false)) {
            criteria.add(Restrictions.ne("status", sStatus));
        }

        if (findByCriteria(criteria).size() > 0) {
            isExist = true;
        }

        return isExist;

    }

    private Integer countClaims(Criteria criteria) {
        criteria.setProjection(Projections.rowCount());
        List totalCountResult = criteria.list();
        criteria.setProjection(null);

        return ((Long) totalCountResult.get(0)).intValue();
    }

    private Criteria buildSearchCriteria(ClaimSearchCriteria searchCriteria) {
        Criteria criteria = getSession().createCriteria(Claim.class)
            .createAlias("this.invoice", "iv", CriteriaSpecification.LEFT_JOIN)
            .createAlias("this.customer", "cs", CriteriaSpecification.LEFT_JOIN)
            .createAlias("this.workgroup", "wg", CriteriaSpecification.LEFT_JOIN)
            .createAlias("this.thirdParty", "tp", CriteriaSpecification.LEFT_JOIN)
            .createAlias("this.vehicleHire", "vh", CriteriaSpecification.LEFT_JOIN)
            .createAlias("this.chorganisation", "cho", CriteriaSpecification.LEFT_JOIN)
            .createAlias("this.createdBy", "cb", CriteriaSpecification.LEFT_JOIN)
            .createAlias("this.claimOwner", "co", CriteriaSpecification.LEFT_JOIN)
            .createAlias("this.supplierClaimOwner", "sco", CriteriaSpecification.LEFT_JOIN)
            .createAlias("this.hireMonitoringDetail", "hmd", CriteriaSpecification.LEFT_JOIN)
            .createAlias("this.insurer", "ins", CriteriaSpecification.LEFT_JOIN);

        if (searchCriteria.getIsWorkgroupCheck()) {
            if (RoleHelper.isWorkgroupValidationEnabledUser(getCurrentUser())) {
                criteria.add(Restrictions.sqlRestriction("workgroup_id in (select workgroup_id from web_user_workgroup where user_id =" + getCurrentUser().getId() + ")"));
            }
        }

        if (searchCriteria.getIsOwnerShipCheck() && getCurrentUser().isAnInsurer()) {
            if (RoleHelper.isOwnershipValidationEnabledUser(getCurrentUser())) {
                criteria.add(Restrictions.eq("claimOwner.id", getCurrentUser().getId()));
            }
        }

        if (searchCriteria.getIsSupplierOwnerShipCheck() && !getCurrentUser().isAnInsurer() && !getCurrentUser().isCHOXAdmin()) {
            if (RoleHelper.isOwnershipValidationEnabledUser(getCurrentUser())) {
                criteria.add(Restrictions.or(Restrictions.eq("supplierClaimOwner.id", getCurrentUser().getId()),
                        Restrictions.isNull("supplierClaimOwner.id")));
            }
        }

        if (searchCriteria.getClaimOwnerIds() != null && !searchCriteria.getClaimOwnerIds().isEmpty()) {
            ArrayList<Integer> ClaimOwnerIds = new ArrayList<Integer>();
            if (searchCriteria.getClaimOwnerIds().contains(ClaimSearchCriteria.CLAIM_OWNER_NOT_ASSIGNED)) {
                ClaimOwnerIds.add(null);
//               criteria.add(Restrictions.isNull("claimOwner.id")); 
            }
            ClaimOwnerIds.addAll(searchCriteria.getClaimOwnerIds());
            criteria.add(Restrictions.in("claimOwner.id", ClaimOwnerIds.toArray()));
        }
        
        if (searchCriteria.getSupplierClaimOwnerIds() != null && !searchCriteria.getSupplierClaimOwnerIds().isEmpty()) {
            ArrayList<Integer> supplierClaimOwnerIds = new ArrayList<Integer>();

            if (searchCriteria.getSupplierClaimOwnerIds().contains(ClaimSearchCriteria.CLAIM_OWNER_NOT_ASSIGNED)) {
                supplierClaimOwnerIds.add(null);
//               criteria.add(Restrictions.isNull("supplierClaimOwner.id")); 
            }
            supplierClaimOwnerIds.addAll(searchCriteria.getSupplierClaimOwnerIds());
            criteria.add(Restrictions.in("supplierClaimOwner.id", supplierClaimOwnerIds.toArray()));
        }

        if (searchCriteria.getSupplierReference() != null && !searchCriteria.getSupplierReference().isEmpty()) {
            String sSupplierRef = searchCriteria.getSupplierReference();
            criteria.add(Restrictions.like("choReference", sSupplierRef).ignoreCase());
        }

        if (searchCriteria.getStatuses() != null && !searchCriteria.getStatuses().isEmpty()) {
            if (searchCriteria.getStatuses().contains(ClaimSearchCriteria.STATUS_ACTIONS_FOR_HANDLERS)) {
                ArrayList<String> handlersActionStatus = new ArrayList<String>();
                handlersActionStatus.addAll(Arrays.asList("ClaimUnacknowledgedRouted", "ClaimRejectionContested", "ClaimPending", "ClaimUpdatedByEngineer", "InvoiceReferredToClaimsHandler", "InvoiceEscalatedToHandler", "ContestedInvoiceReferredToInsurer", "InvoiceApprovedByBRE", "AwaitingInvoicePayment", "AwaitingLiabilityResolution"));
                if (searchCriteria.getStatuses().size() > 1) {
                    handlersActionStatus.addAll(searchCriteria.getStatuses());
                }
                criteria.add(Restrictions.in("status", handlersActionStatus.toArray()));
            } else {
                criteria.add(Restrictions.in("status", searchCriteria.getStatuses().toArray()));
            }
        }

        if (searchCriteria.getStatusExcludeList() != null && !searchCriteria.getStatusExcludeList().isEmpty()) {
            criteria.add(Restrictions.not(Restrictions.in("status", searchCriteria.getStatusExcludeList())));

        }

        if (searchCriteria.getInsurerIds() != null && !searchCriteria.getInsurerIds().isEmpty()) {
            criteria.add(Restrictions.in("ins.id", searchCriteria.getInsurerIds().toArray()));
        }

        if (searchCriteria.getSupplierIds() != null && !searchCriteria.getSupplierIds().isEmpty()) {
            criteria.add(Restrictions.in("cho.id", searchCriteria.getSupplierIds().toArray()));
        }

        if (searchCriteria.getWorkgroupIds() != null && !searchCriteria.getWorkgroupIds().isEmpty()) {
            criteria.add(Restrictions.in("wg.id", searchCriteria.getWorkgroupIds().toArray()));
        }

        if (searchCriteria.getIsAnomalies()) {

            Set anomaliesStatus = new HashSet();
            anomaliesStatus.add(ClaimStatus.CLAIM_REF_TO_ENG);
            anomaliesStatus.add(ClaimStatus.CLAIM_REFERRED_TO_FNOL);
            anomaliesStatus.add(ClaimStatus.CLAIM_REJECTION_CONTESTED);
            anomaliesStatus.add(ClaimStatus.CLAIM_PENDING);
            anomaliesStatus.add(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
            anomaliesStatus.add(ClaimStatus.CLAIM_REJECTED);
            anomaliesStatus.add(ClaimStatus.SUBSCRIBER_CLAIM_REJECTED);
            anomaliesStatus.add(ClaimStatus.CLAIM_UPDATE_BY_ENG);
            anomaliesStatus.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);

            DetachedCriteria noti = DetachedCriteria.forClass(Notification.class).add(Restrictions.in("type", NotificationType.getInsurerNotificationTypes())).add(Restrictions.eq("isacknowledged", false)).setProjection(Projections.projectionList().add(Projections.property("claim")));
            criteria.add(Subqueries.propertyIn("id", noti));
            criteria.add(Restrictions.in("status", anomaliesStatus));

        }

        if (searchCriteria.isLiabilityStatusUpdated()) {
            DetachedCriteria noti = DetachedCriteria.forClass(Notification.class).add(Restrictions.in("type", NotificationType.getChoNotificationTypes())).setProjection(Projections.projectionList().add(Projections.property("claim")));
            criteria.add(Subqueries.propertyIn("id", noti));
        }

        if (searchCriteria.isPenaltyChargesAppliedOnly()) {
            criteria.add(Restrictions.gt("iv.totalPenaltyCharge", BigDecimal.ZERO));
        }

        if (searchCriteria.getIsPenaltyChargeApplied()) {
            criteria.add(Restrictions.ne("status", ClaimStatus.INVOICE_PAYMENT_LOGGED));
            criteria.add(Restrictions.ne("status", ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT));
            criteria.add(Restrictions.ne("status", ClaimStatus.MANUAL_INVOICE_APPROVED));
            criteria.add(Restrictions.ne("status", ClaimStatus.MANUAL_INVOICE_REJECTED));
            criteria.add(Restrictions.ne("status", ClaimStatus.MANUAL_INVOICE_CONTESTED));
// The below statuses can be removed as they are covered by the the 'showOpenClaimsOnly' flag
// However, we'll keep them in for now as this will be faster
            criteria.add(Restrictions.ne("status", ClaimStatus.CLAIM_CLOSED));
            criteria.add(Restrictions.ne("status", ClaimStatus.INVOICE_REJECTED_ACCEPTED));
            criteria.add(Restrictions.ne("status", ClaimStatus.INVOICE_PAYMENT_RECEIVED));
            criteria.add(Restrictions.ne("status", ClaimStatus.MANUAL_INVOICE_PAID));
            criteria.add(Restrictions.ge("iv.penaltyAlertQty", 0));
            criteria.add(Restrictions.sqlRestriction("(current_date - iv1_.auto_penalty_start::Date) >= (iv1_.penalty_alert_qty+1)*30"));
            criteria.add(Restrictions.disjunction().add(Restrictions.eq("autoPenaltyChargeEnabled", Boolean.FALSE)).add(Restrictions.conjunction().add(Restrictions.eq("autoPenaltyChargeEnabled", Boolean.TRUE)).add(Restrictions.eq("cho.autoPenaltyChargeEnabled", Boolean.FALSE))));

            if (!OrganisationType.CHO.equals(getCurrentUser().getOrganisationType())) {
                LOG.warn("Error in search criteria: only CHO can filter for penalty charges");
            } else {
                LOG.debug("Supplier Id={}", getCurrentUser().getChorganisation().getId());
                // Get the id's of the BRE Bands mapped to this CHO
                DetachedCriteria bCriteria = DetachedCriteria.forClass(BreBandOrganisation.class, "brebandorganisation").createAlias("brebandorganisation.chorganisation", "cho", CriteriaSpecification.LEFT_JOIN).add(Restrictions.eq("cho.id", getCurrentUser().getChorganisation().getId()));
                bCriteria.setProjection(Projections.property("brebandorganisation.breBand.id"));

                // Get the insurers from the BRE Band which don't allow penalty charges to be added
                DetachedCriteria pCriteria = DetachedCriteria.forClass(BreBand.class, "breband").add(Restrictions.eq("breband.allowPenaltyCharges", Boolean.FALSE)).add(Restrictions.in("breband.id", bCriteria.getExecutableCriteria(getSession()).list())).setProjection(Projections.property("breband.insurer"));

                // Make sure we retrieve no claims for insurers who don't allow penalty charges to be added
                criteria.add(Property.forName("this.insurer").notIn(pCriteria));
            }
        }
        
        
        if (searchCriteria.isEscalatedToSupervisor()) {
            DetachedCriteria auditTrail = DetachedCriteria.forClass(AuditTrail.class, "aut");
            auditTrail.add(Restrictions.eq("aut.newStatus", ClaimStatus.INVOICE_PAYMENT_LOGGED));
            auditTrail.add(Restrictions.eq("aut.reverted", false));
            auditTrail.add(Restrictions.eqProperty("aut.claim.id", "this.id"));
            auditTrail.setProjection(Property.forName("aut.claim.id"));

            DetachedCriteria innerQuery = DetachedCriteria.forClass(Claim.class, "cl1");
            innerQuery.add(Restrictions.sqlRestriction("id in (select temp.id from (select count(c.id) as nr, c.id as id from claim c, audit_trail a "
                                    + "where c.id = a.claim_id and a.new_status = 'ContestedInvoiceReferredToInsurer' and c.insurer_id = "
                                    + getCurrentUser().getInsurer().getId()
                                    + " and a.reverted = false group by c.id ) as temp where nr >= "
                                    + getCurrentUser().getInsurer().getTimesInStatusContested() + ")"));
            innerQuery.setProjection(Property.forName("cl1.id"));
            
            criteria.add(Restrictions.disjunction()
                    .add(Restrictions.conjunction()
                            .add(Restrictions.sqlRestriction("(current_date - iv1_.created_date::Date) >= " + getCurrentUser().getInsurer().getDaysBeforeEscalated()))
                            .add(Property.forName("this.id").notIn(auditTrail)))
                    .add(Property.forName("this.id").in(innerQuery)));
        }

        if (searchCriteria.getIsInterimPaymentMade()) {
            criteria.add(Restrictions.gtProperty("iv.interimPaymentMade", "iv.interimPaymentReceived"));
        }

        if (searchCriteria.getLiabilityStatuses() != null && !searchCriteria.getLiabilityStatuses().isEmpty()) {
            criteria.add(Restrictions.in("liabilityStatus", searchCriteria.getLiabilityStatuses().toArray()));
            LOG.debug("Liability Search Criteria: {}", Arrays.toString(searchCriteria.getLiabilityStatuses().toArray()));
        } else {
            LOG.debug("Liability Search Criteria not present");
        }

        if (searchCriteria.getInvoiceNumber() != null && !searchCriteria.getInvoiceNumber().isEmpty()) {
            String sSearchInvoiceNumber = searchCriteria.getInvoiceNumber();
            criteria.add(Restrictions.like("iv.claimInvoiceNo", sSearchInvoiceNumber).ignoreCase());
        }

        if (searchCriteria.getClaimNumber() != null && !searchCriteria.getClaimNumber().isEmpty()) {
            String sSearchClaimNumber = searchCriteria.getClaimNumber();
            criteria.add(Restrictions.like("claimNumber", sSearchClaimNumber).ignoreCase());
        }

        if (searchCriteria.getCustomerVrn() != null && !searchCriteria.getCustomerVrn().isEmpty()) {
            String sCustomerVrn = searchCriteria.getCustomerVrn().replaceAll(" ", "");
            criteria.add(Restrictions.like("cs.vehicleRegistration", sCustomerVrn).ignoreCase());
        }

        if (searchCriteria.getThirdPartyVrn() != null && !searchCriteria.getThirdPartyVrn().isEmpty()) {
            String sThirdPartyVrn = searchCriteria.getThirdPartyVrn().replaceAll(" ", "");
            criteria.add(Restrictions.like("tp.vehicleRegistration", sThirdPartyVrn).ignoreCase());
        }

        if (searchCriteria.isShowOpenClaimsOnly()) {
            for (String status : ClaimStatus.getCompletedStatus(true)) {
                criteria.add(Restrictions.ne("status", status));
            }
        }

        if (searchCriteria.getClaimTypes() != null && !searchCriteria.getClaimTypes().isEmpty()) {
            ArrayList<ClaimType> ClaimTypes = new ArrayList<ClaimType>();
            if (searchCriteria.getClaimTypes().contains(ClaimType.GTA)) {
                ClaimTypes.addAll(Arrays.asList(ClaimType.GTA, ClaimType.GTA_ORIGINAL_INVOICE,ClaimType.GTA_SUPPLEMENTARY_INVOICE));
//                criteria.add(Restrictions.in("claimType", new ClaimType[]{ClaimType.GTA, ClaimType.GTA_ORIGINAL_INVOICE,
//                            ClaimType.GTA_SUPPLEMENTARY_INVOICE}));
            } 
            if (searchCriteria.getClaimTypes().contains(ClaimType.INSURER_UPLOAD)) {
                ClaimTypes.addAll(Arrays.asList(ClaimType.INSURER_UPLOAD));
//                criteria.add(Restrictions.in("claimType", new ClaimType[]{ClaimType.INSURER_UPLOAD}));
            } 
            if (searchCriteria.getClaimTypes().contains(ClaimType.INSURER_VS_INSURER)) {
                ClaimTypes.addAll(Arrays.asList(ClaimType.INSURER_VS_INSURER,ClaimType.INSURER_VS_INSURER_ORIGINAL_INVOICE,ClaimType.INSURER_VS_INSURER_SUPPLEMENTARY_INVOICE));
//                criteria.add(Restrictions.in("claimType", new ClaimType[]{ClaimType.INSURER_VS_INSURER,
//                            ClaimType.INSURER_VS_INSURER_ORIGINAL_INVOICE,
//                            ClaimType.INSURER_VS_INSURER_SUPPLEMENTARY_INVOICE}));
            } 
            if (searchCriteria.getClaimTypes().contains(ClaimType.SUBSCRIBER)) {
                ClaimTypes.addAll(Arrays.asList(ClaimType.SUBSCRIBER,ClaimType.SUBSCRIBER_ORIGINAL_INVOICE,ClaimType.SUBSCRIBER_SUPPLEMENTARY_INVOICE));
//                criteria.add(Restrictions.in("claimType", new ClaimType[]{ClaimType.SUBSCRIBER,
//                            ClaimType.SUBSCRIBER_ORIGINAL_INVOICE,
//                            ClaimType.SUBSCRIBER_SUPPLEMENTARY_INVOICE}));
            } 
            if (searchCriteria.getClaimTypes().contains(ClaimType.TPI)) {
                ClaimTypes.addAll(Arrays.asList(ClaimType.TPI));
//                criteria.add(Restrictions.in("claimType", new ClaimType[]{ClaimType.TPI}));
            }
            criteria.add(Restrictions.in("claimType", ClaimTypes.toArray()));
        }

        if (searchCriteria.isIsSupplementaryInvoiceOnly()) {
            criteria.add(Restrictions.in("claimType", ClaimType.getAllSupplementaryInvoiceTypes()));
//            criteria.add(Restrictions.eq("supplementaryInvoicedClaim", true));
        }

        if (searchCriteria.getClaimUploadDateFrom() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(searchCriteria.getClaimUploadDateFrom());
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            criteria.add(Restrictions.ge("createdDate", cal.getTime()));
        }

        if (searchCriteria.getClaimUploadDateTo() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(searchCriteria.getClaimUploadDateTo());
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            criteria.add(Restrictions.le("createdDate", cal.getTime()));
        }

        if (searchCriteria.getStatusModifiedDateFrom() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(searchCriteria.getStatusModifiedDateFrom());
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            criteria.add(Restrictions.ge("statusModifiedDate", cal.getTime()));
        }

        if (searchCriteria.getStatusModifiedDateTo() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(searchCriteria.getStatusModifiedDateTo());
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            criteria.add(Restrictions.le("statusModifiedDate", cal.getTime()));
        }


        if (searchCriteria.getReviewRequiredDateFrom() != null || searchCriteria.getReviewRequiredDateTo() != null) {

            if (searchCriteria.getReviewRequiredDateFrom() != null) {

                Calendar cal = Calendar.getInstance();
                cal.setTime(searchCriteria.getReviewRequiredDateFrom());
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                criteria.add(Restrictions.ge("hmd.nextReviewDate", cal.getTime()));

            }

            if (searchCriteria.getReviewRequiredDateTo() != null) {

                Calendar cal = Calendar.getInstance();
                cal.setTime(searchCriteria.getReviewRequiredDateTo());
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                criteria.add(Restrictions.le("hmd.nextReviewDate", cal.getTime()));

            }

        }

        if (searchCriteria.getInvoiceUploadDateFrom() != null || searchCriteria.getInvoiceUploadDateTo() != null) {

            if (searchCriteria.getInvoiceUploadDateFrom() != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(searchCriteria.getInvoiceUploadDateFrom());
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                criteria.add(Restrictions.ge("iv.createdDate", cal.getTime()));
            }

            if (searchCriteria.getInvoiceUploadDateTo() != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(searchCriteria.getInvoiceUploadDateTo());
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                criteria.add(Restrictions.le("iv.createdDate", cal.getTime()));
            }

        }

        if (searchCriteria.getHireDateFrom() != null || searchCriteria.getHireDateTo() != null) {

            if (searchCriteria.getHireDateFrom() != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(searchCriteria.getHireDateFrom());
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                criteria.add(Restrictions.ge("vh.rentalStart", cal.getTime())).add(Restrictions.le("vh.rentalEnd", cal.getTime()));
            }

            if (searchCriteria.getHireDateTo() != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(searchCriteria.getHireDateTo());
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                criteria.add(Restrictions.ge("vh.rentalStart", cal.getTime())).add(Restrictions.le("vh.rentalEnd", cal.getTime()));
            }
        }

        if (searchCriteria.getLastModifiedDateFrom() != null || searchCriteria.getLastModifiedDateTo() != null) {

            if (searchCriteria.getLastModifiedDateFrom() != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(searchCriteria.getLastModifiedDateFrom());
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                criteria.add(Restrictions.ge("lastModifiedDate", cal.getTime()));
            }

            if (searchCriteria.getLastModifiedDateTo() != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(searchCriteria.getLastModifiedDateTo());
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                criteria.add(Restrictions.le("lastModifiedDate", cal.getTime()));
            }
        }
        return criteria;
    }

    private void addSort(Criteria criteria, String sort, String dir) {
        if (dir.equalsIgnoreCase("desc")) {
            criteria.addOrder(Order.desc(sort));
        } else {
            criteria.addOrder(Order.asc(sort));
        }
    }

    @Override
    public String getDaysWithCHOForReview(int id) {
        LOG.debug("Getting number of days claim was with CHO for review");
        double days = auditTrailService.getTimeInvoiceWithCHO(id);
        return doubleToTime(days);
    }

    @Override
    public String getDaysWithInsurerForReview(int id) {
        LOG.debug("Getting number of days claim was with Insurer for review");
        double days = auditTrailService.getTimeInvoiceWithInsurer(id);
        return doubleToTime(days);
    }

    @Override
    public String getDaysAwaitingLiabilityResolution(int id) {
        LOG.debug("Getting number of days claim was awaiting liability resolution");
        double days = auditTrailService.getTimeAwaitingLiabilityResolution(id);
        return doubleToTime(days);
    }
    
    private String doubleToTime(double days) {
        String time = "";

        if (days >= 1.0) {
            if (days < 2.0) {
                time = Integer.toString((int) days) + " day ";
            } else {
                time = Integer.toString((int) days) + " days ";
            }
            days -= (int) days;
        }
        int hours = (int) (days * 24.0);
        if (hours > 1) {
            time += Integer.toString(hours) + " hours";
        } else if (hours > 0) {
            time += Integer.toString(hours) + " hour";
        }

        if (time.length() == 0) {
            time = "-";
        }

        return time;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void saveClaimWithoutUpdatingLiabilityPayment(Claim claim) {
        super.save(claim);
    }

    /*
     *  Please make sure you handle null check on returned claim when using this below method.
     */
    @Override
    public Claim getOriginalSupplementaryInvoicedClaim(String customerClaimRef) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
        criteria.createCriteria("customer").add(Restrictions.like("claimReference", customerClaimRef).ignoreCase());
        criteria.add(Restrictions.in("claimType", ClaimType.getOriginalSupplementaryInvoiceTypes()));
        if (getSecurityInfoProvider().getIsCHO()) {
            criteria.add(Restrictions.eq("chorganisation.id", getSecurityInfoProvider().getCurrentUser().getChorganisation().getId()));
        } else if (getSecurityInfoProvider().getIsINS()) {
            criteria.add(Restrictions.eq("insurer.id", getSecurityInfoProvider().getCurrentUser().getInsurer().getId()));
        }
        List<Claim> claims = findByCriteria(criteria);
        if (claims.size() > 0) {
            return claims.get(0);
        } else {
            return null;
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int getSubscriberClaimDays(int id) {
        int claimAge = -1;
        LOG.debug("Getting days of subscriber claim with id={}", id);
        Claim claim = (Claim) get(Claim.class, id);

        if (claim != null && ClaimType.isSubscriber(claim.getClaimType())) {
            claimAge = auditTrailService.getSubscriberClaimDays(id);
        }

        if (claimAge > 5 || (claimAge == 5 && !DateHelper.isBefore3pm())) {
            boolean addComment = true;
            List<Comment> comments = commentService.getCommentByClaimId(claim.getId());
            for (Comment comment : comments) {
                if (comment.getComment().endsWith("claim taken down Subscriber route.")) {
                    addComment = false;
                    LOG.debug("Comment already added - skipping");
                    break;
                }
            }
            if (addComment) {
                Comment comment = Comment.New(0, claim.getInsurer().getName() + " failed to respond to the Subscriber notification within the 5 day SLA, claim taken down Subscriber route.");
                claim.addComment(comment);
                save(claim);
                LOG.debug("Comment added and claim saved.");
            }
        }

        LOG.debug("Returning claim age of {}", claimAge);
        return claimAge;
    }

    @Override
    public boolean isSubscriberClaimRejectedAndAgreed(int claimId) {
        Claim claim = getClaim(claimId);

        if (ClaimType.isSubscriber(claim.getClaimType())) {
            return auditTrailService.isSubscriberClaimRejectedAndAgreed(claimId);
        }

        return false;
    }

    @Override
    public int getSubscriberClaimRejectedDays(int claimId) {
        int claimAge = -1;
        LOG.debug("Getting days until subscriber claim rejected with id={}", claimId);
        Claim claim = (Claim) get(Claim.class, claimId);

        if (claim != null && ClaimType.isSubscriber(claim.getClaimType())) {
            claimAge = auditTrailService.getSubscriberClaimRejectedDays(claimId);
        }

        LOG.debug("Days until subscriber claim ({}) rejected: {}", claimId, claimAge);

        return claimAge;
    }

    @Override
//    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public boolean updateAutomaticPenaltyCharge(Claim claim) {
        LOG.debug("Updating penalty charges: claim.isAutoPenaltyChargeEnabled()={}, claim.getChorganisation().isAutoPenaltyChargeEnabled()={}, "
                + "!ClaimStatus.isInPenaltyChargeExclusionStatus(claim.getStatus())={}, claim.getInvoice()={}, "
                + "calculatePenaltyAlertQty(claim.getInvoice())={}, claim.getInvoice().getPenaltyAlertQty()={}, "
                + "calculatePenaltyAlertQty(claim.getInvoice())={}",
                new Object[]{claim.isAutoPenaltyChargeEnabled(), claim.getChorganisation().isAutoPenaltyChargeEnabled(),
                    !ClaimStatus.isInPenaltyChargeExclusionStatus(claim.getStatus()),
                    claim.getInvoice(), calculatePenaltyAlertQty(claim.getInvoice()),
                    claim.getInvoice().getPenaltyAlertQty(),
                    calculatePenaltyAlertQty(claim.getInvoice())});

        if (claim.isAutoPenaltyChargeEnabled()
                && claim.getChorganisation().isAutoPenaltyChargeEnabled()
                && !ClaimStatus.isInPenaltyChargeExclusionStatus(claim.getStatus())
                && claim.getInvoice() != null
                && claim.getInvoice().getInvoicedDays() > 30) {
//                && claim.getInvoice().getPenaltyAlertQty() < calculatePenaltyAlertQty(claim.getInvoice())) {

            try {
                LOG.debug("Calling stored procedure to update penalty charges...");
                callApplyAutoPenaltyCharge(999, claim.getId());
                // The Claim / Invoice may have been modified in the above call.
                // We therefore need to clear these objects from the cache
                // First clear the query/session cache
                evict(claim.getInvoice());
                evict(claim);
                // Then the second-level cache (if activated)
                getCurrentSession().getSessionFactory().evict(Claim.class, claim.getId());
                getCurrentSession().getSessionFactory().evict(Invoice.class, claim.getInvoice().getId());
                LOG.debug("Auto penalty charge applied to claim: {}", claim.getChoReference());
                return true;
            } catch (Exception ex) {
                LOG.error("Exception thrown while updating auto penalty charge store procedure for claim '{}'", claim.getChoReference(), ex);
                return false;
            }
        }
        return false;
    }

    @Override
    public int calculatePenaltyAlertQty(Invoice inv) {
        long dateDiff = inv.getInvoicedDays();
        return (int) (dateDiff / 30);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updatePenaltyStartDate(Claim claim, Date autoPenaltyStart) {

        Invoice inv = claim.getInvoice();
        inv.setFullTotalToPay(inv.getFullTotalToPay().subtract(inv.getHirePenaltyCharge()).subtract(inv.getRepairPenaltyCharge()));
        inv.setAutoPenaltyStart(autoPenaltyStart);
        inv.setHirePenaltyPercentage(null);
        inv.setRepairPenaltyPercentage(null);
        inv.setHirePenaltyCharge(BigDecimal.ZERO);
        inv.setRepairPenaltyCharge(BigDecimal.ZERO);
        inv.setPenaltyAlertQty(0);
        inv.setAutoPenaltyAlertQty(0);
        inv.setHirePenaltyChargeAppliedDate(null);
        inv.setRepairPenaltyChargeAppliedDate(null);
        if (inv.getTotalPenaltyCharge() != null && inv.getTotalPenaltyCharge().compareTo(BigDecimal.ZERO) > 0) {
            Comment comment = Comment.New(0, "Penalty charges have been removed from the invoice as the date from which penalty charges are calculated has been manually updated.");
            claim.addComment(comment);
        }
        inv.setTotalPenaltyCharge(BigDecimal.ZERO);


        updateClaim(claim);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public boolean setPenaltyStartToDateInvoiced(String choReference) {
        Claim claim = getClaimByCHOReferenceNumber(choReference);
        if (claim != null && claim.getInvoice() != null) {
            updatePenaltyStartDate(claim, claim.getInvoice().getDateInvoiced());
            updateAutomaticPenaltyCharge(claim);
            return true;
        }
        
        return false;
    }
    
    private int updateChoReferenceNumber(String oldReference, String newReference, Integer choId) {
        Claim claim = getClaimByChoIdAndCHOReferenceNumber(choId, oldReference);
        if (claim != null) {
            Claim newClaim = getClaimByChoIdAndCHOReferenceNumber(choId, newReference);
            if (newClaim == null) {
                try {
                    claim.setChoReference(newReference);
                    claim.addComment(Comment.New(0, "Supplier Reference updated from '" + oldReference + "' to '" + newReference + "'."));
                    updateClaim(claim);
                    LOG.debug("Claim with reference number " + oldReference + " updated with new Cho reference number: " + newReference);
                    return 0;
                } catch (Exception ex) {
                    LOG.error("Cannot update claim with reference number " + oldReference + " to new Cho reference number: " + newReference, ex);
                    return 9;
                }
            } else {
                return 1;
            }
        } else {
            Claim newClaim = getClaimByChoIdAndCHOReferenceNumber(choId, newReference);
            if (newClaim != null) {
                return 3;
            }
        }

        return 2;
    }

    @Override
    public List<QueuedTicket> getQueuedTicket() {
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(QueuedTicket.class);
            criteria.addOrder(Order.asc("sender"));
            List<QueuedTicket> queuedTickets = findByCriteria(criteria);
            return queuedTickets;
        } catch (Exception ex) {
            LOG.error("Exception thrown while getting queuedTickets ", ex);
            return new ArrayList<QueuedTicket>();
        }
    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int updateReservationToTicket(String oldReference, String newReference, Integer choId, String sender) {
        int result = updateChoReferenceNumber(oldReference, newReference, choId);
        if (result == 2) {
            addChoRefToQueuedTicket(oldReference, newReference, sender);
            return result;
        } else {
            return result;
        }
    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int updateQueuedTicket(QueuedTicket queuedTicket, Integer choId) {

        int result = updateChoReferenceNumber(queuedTicket.getOldReference(), queuedTicket.getNewReference(), choId);
        if (result != 2) {
            removeQueuedTicket(queuedTicket);
            return result;
        } else {
            return result;
        }
    }

    private void addChoRefToQueuedTicket(String oldReference, String newReference, String sender) {
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(QueuedTicket.class);
            criteria.add(Restrictions.like("sender", sender).ignoreCase());
            criteria.add(Restrictions.like("oldReference", oldReference).ignoreCase());
            criteria.add(Restrictions.like("newReference", newReference).ignoreCase());
            List<QueuedTicket> queuedTickets = findByCriteria(criteria);
            if (queuedTickets.size() <= 0) {
                QueuedTicket queuedTicket = new QueuedTicket();
                queuedTicket.setOldReference(oldReference);
                queuedTicket.setNewReference(newReference);
                queuedTicket.setSender(sender);
                queuedTicket.setCreatedDate(new Date());
                save(queuedTicket);
            } else {
                LOG.debug("queuedTicket already exists for sender:{} with old_cho_ref:{} and new_cho_ref:{}", new Object[]{sender, oldReference, newReference});
            }

        } catch (Exception ex) {
            LOG.error("Exception thrown while saving queuedTicket: sender:{} old_cho_ref:{} new_cho_ref:{}", new Object[]{sender, oldReference, newReference}, ex);
        }
    }

    private void removeQueuedTicket(QueuedTicket queuedTicket) {
        try {
            delete(queuedTicket);
        } catch (Exception ex) {
            LOG.error("Exception thrown while deleting QueuedTicket: sender:{} old_cho_ref:{} new_cho_ref:{}", new Object[]{queuedTicket.getSender(), queuedTicket.getOldReference(), queuedTicket.getNewReference()}, ex);
        }
    }
    
    @Override
    public void updateLiabilityPayment(Claim claim) {

        LiabilityStatus l = claim.getLiabilityStatus();
        Invoice invoice = claim.getInvoice();
        ClaimType claimType = claim.getClaimType();
        if (invoice != null) {
            if (!claim.getClaimType().isInsurerVsInsurer(claimType) && l != null && (l.equals(LiabilityStatus.LIABILITY_SPLIT) || (l.equals(LiabilityStatus.PROCEED_WITHOUT_PREJUDICE)))) {
                BigDecimal ttp = invoice.getFullTotalToPay();
                BigDecimal insper = claim.getPercentageLiabilityAccepted();
                invoice.setTotalToPay(ttp.multiply(insper).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
                BigDecimal ofttp = invoice.getOriginalFullTotalToPay();
                invoice.setOriginalTotalToPay(ofttp.multiply(insper).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
                LOG.debug("liability updated " + invoice.getTotalToPay());
            } else if (!ClaimType.isInsurerVsInsurer(claimType) && l != null && l.equals(LiabilityStatus.LIABILITY_REPUDIATED)) {
                invoice.setTotalToPay(BigDecimal.ZERO);
                invoice.setOriginalTotalToPay(BigDecimal.ZERO);
            } else {
                invoice.setTotalToPay(invoice.getFullTotalToPay());
                LOG.debug("liablity not updated");
            }
        }
    }
}
