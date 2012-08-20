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
import idas.chox.data.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

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
                    claim.setPreviousStatus(auditTrail.getOriginalStatus());
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

        if (searchCriteria.getIsWorkgroupCheck() && !searchCriteria.isIsManual()) {
            if (RoleHelper.isWorkgroupValidationEnabledUser(getCurrentUser())) {
                criteria.add(Restrictions.sqlRestriction("workgroup_id in (select workgroup_id from web_user_workgroup where user_id =" + getCurrentUser().getId() + ")"));
            }
        }

        if (searchCriteria.getIsWorkgroupCheck() && searchCriteria.isIsManual()) {
            if (RoleHelper.isManualWorkgroupValidationEnabledUser(getCurrentUser())) {
                criteria.add(Restrictions.sqlRestriction("workgroup_id in (select workgroup_id from web_user_workgroup where user_id =" + getCurrentUser().getId() + ")"));
            }
        }

        if (searchCriteria.getIsOwnerShipCheck() && getCurrentUser().isAnInsurer() & !searchCriteria.isIsManual()) {
            if (RoleHelper.isOwnershipValidationEnabledUser(getCurrentUser())) {
                criteria.add(Restrictions.eq("claimOwner.id", getCurrentUser().getId()));
            }
        }

        if (searchCriteria.getIsOwnerShipCheck() && getCurrentUser().isAnInsurer() & searchCriteria.isIsManual()) {
            if (RoleHelper.isManualOwnershipValidationEnabledUser(getCurrentUser())) {
                criteria.add(Restrictions.eq("claimOwner.id", getCurrentUser().getId()));
            }
        }

        if (searchCriteria.getIsSupplierOwnerShipCheck() && getCurrentUser().isCHO()) {
            if (RoleHelper.isOwnershipValidationEnabledUser(getCurrentUser())) {
                criteria.add(Restrictions.or(Restrictions.eq("supplierClaimOwner.id", getCurrentUser().getId()),
                        Restrictions.isNull("supplierClaimOwner.id")));
            }
        }

        if (searchCriteria.getClaimOwnerIds() != null && !searchCriteria.getClaimOwnerIds().isEmpty()) {
            ArrayList<Integer> ClaimOwnerIds = new ArrayList<Integer>();

            ClaimOwnerIds.addAll(searchCriteria.getClaimOwnerIds());
            criteria.add(Restrictions.in("claimOwner.id", ClaimOwnerIds.toArray()));
        }

        if (searchCriteria.getSupplierClaimOwnerIds() != null && !searchCriteria.getSupplierClaimOwnerIds().isEmpty()) {
            ArrayList<Integer> supplierClaimOwnerIds = new ArrayList<Integer>();

            if (searchCriteria.getSupplierClaimOwnerIds().contains(ClaimSearchCriteria.CLAIM_OWNER_NOT_ASSIGNED)) {
                // Remove -9 value from selected supplierClaimOwnerIds as we are adding null restriction.
                searchCriteria.getSupplierClaimOwnerIds().remove(ClaimSearchCriteria.CLAIM_OWNER_NOT_ASSIGNED);
                // If multiple SupplierClaimOwner selected with CLAIM_OWNER_NOT_ASSIGNED then use criteria OR condition.
                if (searchCriteria.getSupplierClaimOwnerIds().size() > 0) {
                    supplierClaimOwnerIds.addAll(searchCriteria.getSupplierClaimOwnerIds());
                    criteria.add(Restrictions.or(Restrictions.in("supplierClaimOwner.id", supplierClaimOwnerIds.toArray()),
                            Restrictions.isNull("supplierClaimOwner.id")));
                } else { // If only CLAIM_OWNER_NOT_ASSIGNED selected just add null restriction.
                    criteria.add(Restrictions.isNull("supplierClaimOwner.id"));
                }
            } else {
                supplierClaimOwnerIds.addAll(searchCriteria.getSupplierClaimOwnerIds());
                criteria.add(Restrictions.in("supplierClaimOwner.id", supplierClaimOwnerIds.toArray()));
            }
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
            criteria.add(Restrictions.ne("status", ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT));
            criteria.add(Restrictions.ne("status", ClaimStatus.INVOICE_PAYMENT_LOGGED));
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
            criteria.add(Restrictions.ne("status", ClaimStatus.INVOICE_PAYMENT_LOGGED));
            
            criteria.add(Restrictions.disjunction()
                    .add(Restrictions.sqlRestriction("(current_date - iv1_.created_date::Date) >= " + getCurrentUser().getInsurer().getDaysBeforeEscalated()))
                    .add(Restrictions.sqlRestriction("{alias}.id in (select temp.id from (select count(a.claim_id) as nr, a.claim_id as id from audit_trail a " +
                    "where a.claim_id = {alias}.id " +
                    "and a.new_status = 'ContestedInvoiceReferredToInsurer' " +
                    "and a.reverted = false " +
                    "group by a.claim_id ) as temp where nr >= " + getCurrentUser().getInsurer().getTimesInStatusContested() + ")" )));
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
            if (!ClaimType.isInsurerVsInsurer(claimType) && l != null && (l.equals(LiabilityStatus.LIABILITY_SPLIT) || (l.equals(LiabilityStatus.PROCEED_WITHOUT_PREJUDICE)))) {
                BigDecimal ttp = invoice.getFullTotalToPay();
                BigDecimal insper = claim.getPercentageLiabilityAccepted();
                invoice.setTotalToPay(ttp.multiply(insper).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
//                BigDecimal ofttp = invoice.getOriginalFullTotalToPay();
//                invoice.setOriginalTotalToPay(ofttp.multiply(insper).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
                LOG.debug("liability updated " + invoice.getTotalToPay());
            } else if (!ClaimType.isInsurerVsInsurer(claimType) && l != null && l.equals(LiabilityStatus.LIABILITY_REPUDIATED)) {
                invoice.setTotalToPay(BigDecimal.ZERO);
//                invoice.setOriginalTotalToPay(BigDecimal.ZERO);
            } else {
                invoice.setTotalToPay(invoice.getFullTotalToPay());
                LOG.debug("liablity not updated");
            }
        }
    }
    
    @Override
    public int getDaysSinceInvoiceUploadToEscalate(Integer claimId) {
        Claim claim = (Claim) this.getClaim(claimId);
        
        if(isClaimInClosedStatus(claim)){
            return 0;
        }

        Date createdDate = claim.getInvoice().getCreatedDate();
        Date currentDate = DateHelper.getCurrentDate();  
        return DateHelper.getNumberOfDaysBetween(createdDate, currentDate) + 1;
    }

    @Override
    public int getNumberOfTimesContestedWithCHOtoEscalate(Integer claimId) {
        Claim claim = (Claim) this.getClaim(claimId);

        if(isClaimInClosedStatus(claim)){
            return 0;
        }
        
        Criteria criteria = getSession().createCriteria(AuditTrail.class);
        criteria.add(Restrictions.eq("newStatus", ClaimStatus.CONTESTED_INVOICE_REF_TO_INS));
        criteria.add(Restrictions.eq("reverted", false));
        criteria.add(Restrictions.eq("claim.id", claimId));
        return countClaims(criteria).intValue();
    }
    
    private boolean isClaimInClosedStatus(Claim claim) {
        for (String status : ClaimStatus.getCompletedStatus(true)) {
            if (claim.getStatus().equalsIgnoreCase(status))
                return true;
        }
        DetachedCriteria auditTrail = DetachedCriteria.forClass(AuditTrail.class, "aut");
        auditTrail.add(Restrictions.eq("aut.newStatus",ClaimStatus.INVOICE_PAYMENT_LOGGED));
        auditTrail.add(Restrictions.eq("aut.reverted", false));
        auditTrail.add(Restrictions.eq("aut.claim.id", claim.getId()));
        List result = getHibernateTemplate().findByCriteria(auditTrail);
        // in case the claim was in status 'invoice payment logged' we return 0
        // and don't display it in information panel
        if (result.size() > 0)
            return true;
        return false;
    }
    
    @Override
    public int getNoOfRejectedClaims(Integer reasonOfRejectionId) {
        Criteria criteria = getSession().createCriteria(Claim.class);
        criteria.add(Restrictions.eq("reasonOfRejection.id", reasonOfRejectionId));
        return countClaims(criteria).intValue();
    }

    @Override
    public String getOverlappingHire(Claim claim) {
        /* returns the insurer claim nuber of any claim found with an overlappinh hire period
         * to the argument claim, otherwise null
         */
        String insurerClaimNumber = null;
        
        if (claim.getVehicleHire() != null && claim.getVehicleHire().getVehicleRegistration()!=null
                && claim.getVehicleHire().getVehicleRegistration().length() > 0
                && !claim.getVehicleHire().getVehicleRegistration().equals("NK1")) {
            DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class)
                                .createAlias("this.vehicleHire", "vh", CriteriaSpecification.LEFT_JOIN)
                                .createAlias("this.customer", "cust", CriteriaSpecification.LEFT_JOIN);

            criteria.add(Restrictions.ne("choReference", claim.getChoReference()));
            // Ignore blank customer claim numbers (i.e. they should not prevent an overlap match) - bug#1887
            if (claim.getCustomer().getClaimReference() != null && claim.getCustomer().getClaimReference().length() > 0)
                criteria.add(Restrictions.ne("cust.claimReference", claim.getCustomer().getClaimReference()));
            criteria.add(Restrictions.eq("insurer.id", claim.getInsurer().getId()));
            criteria.add(Restrictions.eq("vh.vehicleRegistration", claim.getVehicleHire().getVehicleRegistration()));
            criteria.add(Restrictions.disjunction().add(Restrictions.between("vh.rentalStart", claim.getVehicleHire().getHireStart(), claim.getVehicleHire().getHireEnd()))
                    .add(Restrictions.between("vh.rentalEnd", claim.getVehicleHire().getHireStart(), claim.getVehicleHire().getHireEnd())));
 
            LOG.debug("Overlapping query is: {}", criteria.toString());
            List<Claim> claims = findByCriteria(criteria);
            if (claims.size() > 0) {
                insurerClaimNumber = claims.get(0).getClaimNumber();
            }

        } else {
            LOG.debug("NO overlapping hire query ran");
        }
        return insurerClaimNumber;
    }

        
    @Override
    public List<ExcelClaim> getExcelClaims(List<Integer> ids) {
        List<ExcelClaim> results = new ArrayList<ExcelClaim>(ids.size());
        StringBuilder sb = new StringBuilder();
        sb.append("select")
            .append(" c.status, c.claim_type, c.cho_reference, cho.name as chorg_name, w.name as workgroup_name, c.status_modified_date, c.indeminty_amount,")
            .append(" c.liability_status, c.percentage_liability_accepted, c.percentage_liability_cho, c.managing_repair, c.policy_holder_contact_date,")
            .append(" c.credit_agreement_date, c.gta_notice_date, c.claim_number, wu.last_name || ' ' || wu.first_name as claim_owner, cust.title as customer_title,")
            .append(" cust.first_name as customer_first_name, cust.last_name as customer_last_name, cust.address1 as customer_address1, cust.address2 as customer_address2,")
            .append(" cust.address3 as customer_address3, cust.address4 as customer_address4, cust.address5 as customer_address5, cust.postcode as customer_postcode,")
            .append(" cust.telephone_day as customer_telephone_day, cust.telephone_evening as customer_telephone_evening, cust.email as customer_email,")
            .append(" cust.insurer_name as customer_insurer_name, cust.policy_number as customer_policy_number, cust.claim_reference as customer_claim_reference,")
            .append(" cust.comprehensive as customer_comprehensive, cust.vehicle_manufacturer as customer_vehicle_manufacturer, cust.vehicle_model as customer_vehicle_model,")
            .append(" cust.vehicle_registration as customer_vehicle_registration, cust.vehicle_year as customer_vehicle_year, cust_vc.name as customer_vehicle_class,")
            .append(" cust.location as customer_location, cust.hpi_vehicle_manufacturer as customer_hpi_vehicle_manufacturer, cust.hpi_vehicle_model as customer_hpi_vehicle_model,")
            .append(" cust.hpi_vehicle_year as customer_hpi_vehicle_year, cust.hpi_first_registration as customer_hpi_vehicle_first_registration,")
            .append(" cust.hpi_vehicle_capacity as customer_hpi_vehicle_capacity, cust.hpi_vehicle_doorplan as customer_hpi_vehicle_doorplan,")
            .append(" cust.hpi_vehicle_transmission as customer_hpi_vehicle_transmission, cust.access_other_vehicle as customer_access_other_vehicle,")
            .append(" cust.other_vehicle_used as customer_other_vehicle_used, cust.other_vehicle as customer_other_vehicle, cust.courtesy_car as customer_courtesy_car,")
            .append(" cust.specific_vehicle as customer_specific_vehicle, cust.specific_vehicle_reason as customer_specific_vehicle_reason,")
            .append(" cust.vehicle_type_required as customer_vehicle_type_required, cust.special_requirements as customer_special_requirements,")
            .append(" cust.average_daily_mileage as customer_average_daily_mileage, cust.damage as customer_damage, cust.is_usable as customer_is_usable,")
            .append(" cust.is_total_loss as customer_is_total_loss, cust.initial_ecd as customer_initial_ecd,")
            .append(" tp.title as tp_title, tp.first_name as tp_first_name, tp.last_name as tp_last_name,")
            .append(" tp.address1 as tp_address1, tp.address2 as tp_address2, tp.address3 as tp_address3, tp.address4 as tp_address4, tp.address5 as tp_address5, ")
            .append(" tp.postcode as tp_postcode, tp.telephone_day as tp_telephone_day, tp.telephone_evening as tp_telephone_evening, tp.email as tp_email,")
            .append(" tp_insurer.name as tp_insurer_name, tp.policy_number as tp_policy_number, ")
            .append(" tp.vehicle_manufacturer as tp_vehicle_manufacturer, tp.vehicle_model as tp_vehicle_model,")
            .append(" tp.vehicle_registration as tp_vehicle_registration, tp_vc.name as tp_vehicle_class,")
            .append(" inc. date as incident_date, inc.location as incident_location, inc.is_police_involved as incident_is_police_involved, inc.incident_description as incident_description,")
            .append(" wit.name as witness_name, wit.address1 as witness_address1, wit.address2 as witness_address2, wit.address3 as witness_address3,")
            .append(" wit.address4 as witness_address4, wit.address5 as witness_address5, wit.postcode as witness_postcode, wit.telephone_evening as witness_telephone_evening,")
            .append(" wit.telephone_day as witness_telephone_day, wit.email as witness_email,")
            .append(" inj.name as injury_name, inj.address1 as injury_address1, inj.address2 as injury_address2, inj.address3 as injury_address3, inj.address4 as injury_address4,")
            .append(" inj.address5 as injury_address5, inj.postcode as injury_postcode, inj.email as injury_email, inj.telephone_day as injury_telephone_day, ")
            .append(" inj.telephone_evening as injury_telephone_evening, inj.solicitor_name as injury_solicitor_name, inj.solicitor_address1 as injury_solicitor_address1,")
            .append(" inj.solicitor_address2 as injury_solicitor_address2, inj.solicitor_address3 as injury_solicitor_address3, inj.solicitor_address4 as injury_solicitor_address4,")
            .append(" inj.solicitor_address5 as injury_solicitor_address5, inj.solicitor_postcode as injury_solicitor_postcode, inj.solicitor_telephone as injury_solicitor_telephone,")
            .append(" inj.solicitor_email as injury_solicitor_email, er.labour_amount as er_labour_amount, er.total_amount as er_repair_amount, er.days as er_days,")
            .append(" er.is_usable as er_is_usable, er.name as er_name, er.company as er_company, er.address1 as er_address1, er.address2 as er_address2, er.address3 as er_address3,")
            .append(" er.address4 as er_address4, er.address5 as er_address5, er.postcode as er_postcode, er.telephone as er_telephone, er.email as er_email,")
            .append(" vh.vehicle_manufacturer as vh_vehicle_manufacturer, vh.vehicle_model as vh_vehicle_model, vh.vehicle_registration as vh_vehicle_registration,")
            .append(" vh_vc.name as vh_vehicle_class_name, vh.rental_start as vh_rental_start, vh.rental_end as vh_rental_end, vh.days as vh_days,")
            .append(" vh.collection_reason as vh_collection_reason, vh.hpi_vehicle_manufacturer as vh_hpi_vehicle_manufacturer, vh.hpi_vehicle_model as vh_hpi_vehicle_model,")
            .append(" vh.hpi_vehicle_year as vh_hpi_vehicle_year, vh.hpi_first_registration as vh_hpi_vehicle_first_registration,")
            .append(" vh.hpi_vehicle_capacity as vh_hpi_vehicle_capacity, vh.hpi_vehicle_doorplan as vh_hpi_vehicle_doorplan,")
            .append(" vh.hpi_vehicle_transmission as vh_hpi_vehicle_transmission, hmd.name_of_repairer as hmd_name_of_repairer, hmd.repair_book_in_date as hmd_repair_book_in_date,")
            .append(" hmd.repair_authorised_date as hmd_repair_authorised_date, hmd.repair_commenced_date as hmd_repair_commenced_date,")
            .append(" hmd.inspection_booked_date as hmd_inspection_booked_date, hmd.inspection_date as hmd_inspection_date, hmd.name_of_ime as hmd_name_of_ime,")
            .append(" hmd.repair_completion_date as hmd_repair_completion_date, hmd.is_total_lost_check as hmd_is_total_lost_check, hmd.total_loss_offer_made as hmd_total_loss_offer_made,")
            .append(" hmd.total_loss_offer_accepted as hmd_total_loss_offer_accepted, hmd.total_loss_check_issued as hmd_total_loss_check_issued,")
            .append(" hmd.total_loss_check_received as hmd_total_loss_check_received, hmd.labour_rate as hmd_labour_rate, hmd.labour_hour as hmd_labour_hour,")
            .append(" hmd.labour_cost as hmd_labour_cost, hmd.non_provision_reason as hmd_non_provision_reason, hmd.next_review_date as hmd_next_review_date")
            .append(" from claim c")
            .append("     join chorganisation cho on (c.chorganisation_id = cho.id)")
            .append("     left outer join workgroup w on (c.workgroup_id = w.id)")
            .append("     left outer join web_user wu on (c.claim_owner_id = wu.id)")
            .append("     left outer join customer cust on (c.customer_id = cust.id)")
            .append("     left outer join vehicle_class cust_vc on (cust.vehicle_class_id = cust_vc.id)")
            .append("     left outer join third_party tp on (c.third_party_id = tp.id)")
            .append("     left outer join vehicle_class tp_vc on (cust.vehicle_class_id = tp_vc.id)")
            .append("     left outer join insurer tp_insurer on (tp.insurer_id = tp_insurer.id)")
            .append("     left outer join incident inc on (c.incident_id = inc.id)")
            .append("     left outer join witness wit on (inc.id = wit.incident_id)")
            .append("     left outer join injury inj on (inc.id = inj.incident_id)")
            .append("     left outer join engineer_report er on (c.engineer_report_id = er.id)")
            .append("     left outer join vehicle_hire vh on (c.vehicle_hire_id = vh.id)")
            .append("     left outer join vehicle_class vh_vc on (vh.vehicle_class_id = vh_vc.id)")
            .append("     left outer join hire_monitoring_detail hmd on (c.hire_monitoring_detail_id = hmd.id)")
//            .append(" where c.id in ( :claimIds ) ");
            .append(" where c.id in (");

        boolean first = true;
        for (Integer id : ids) {
            if (!first)
                sb.append(", ").append(id.toString());
            else {
                sb.append(id.toString());
                first = false;
            }
        }            
        sb.append(") ");
//        sb.append(" order by ?");

//        Map paramMap = new HashMap();
//        paramMap.put("claimIds", ids);

        LOG.debug("Querying for claim details...\n{}", sb.toString());
//        List result = this.externalQuery(sb.toString(), paramMap);
        List result = this.externalQuery(sb.toString());
        LOG.debug("Got details - building data objects");

        for(Object obj : result)
            results.add(new ExcelClaim((Map)obj));

        LOG.debug("Returning results.");
        return results;
    }
    
    @Override
    public List<ExcelInvoice> getExcelInvoices(List<Integer> ids) {
        List<ExcelInvoice> results = new ArrayList<ExcelInvoice>(ids.size());
        StringBuilder sb = new StringBuilder();
        sb.append("select ")
            .append(" c.status as claimstatus, c.cho_reference as choreference, tp.claim_reference as thirdpartyclaimreference,")
            .append(" i.created_date as createddate, i.auto_penalty_start as autopenaltystart, i.miscellaneous_fee as miscellaneousfee,")
            .append(" i.automatic_fee as automaticfee, i.automatic_qty as automaticqty, i.additional_driver_fee as additionaldriverfee,")
            .append(" i.additional_driver_qty as additionaldriverqty, i.sat_nav_fee as satnavfee, i.sat_nav_qty as satnavqty,")
            .append(" i.estate_fee as estatefee, i.estate_qty as estateqty, i.baby_seat_fee as babyseatfee, i.baby_seat_qty as babyseatqty,")
            .append(" i.tow_bars_fee as towbarsfee, i.tow_bars_qty as towbarsqty, i.non_standard_insurance_premium_fee as nonstandardinsurancepremiumfee,")
            .append(" i.non_standard_insurance_premium_qty as nonstandardinsurancepremiumqty, i.cover_note_required as covernoterequired,")
            .append(" i.admin_fee as adminfee, i.admin_qty as adminqty, i.roof_rack_fee as roofrackfee, i.roof_rack_qty as roofrackqty,")
            .append(" i.dual_control_fee as dualcontrolfee, i.dual_control_qty as dualcontrolqty, i.delivery_collection_fee as deliverycollectionfee,")
            .append(" i.delivery_collection_qty as deliverycollectionqty, i.excess_amount_collected as excessamountcollected,")
            .append(" i.vat_amount_collected as vatamountcollected, i.handling_invoice_no as handlinginvoiceno,")
            .append(" i.claims_handling_invoice_amount as  claimshandlinginvoiceamount, i.claim_invoice_no as claiminvoiceno,")
            .append(" i.hire_rate_charged_per_day as hireratechargedperday, i.hire_net as hirenet, i.hire_vat as hirevat,")
            .append(" i.hire_gross as hiregross, i.repair_net as repairnet, i.repair_vat as repairvat, i.repair_gross as repairgross,")
            .append(" i.engineer_fee_net as engineerfeenet, i.engineer_fee_vat as engineerfeevat, i.engineer_fee_gross as engineerfeegross,")
            .append(" i.total_loss_net as totallossfeenet, i.total_loss_vat as totallossfeevat, i.total_loss_gross as totallossfeegross,")
            .append(" i.storage_recovery_net as storagerecoverynet, i.storage_recovery_vat as storagerecoveryvat, i.storage_recovery_gross as storagerecoverygross,")
            .append(" i.deduction_for_claims_handling_fee as deductionforclaimshandlingfee, i.hire_penalty_charge as hirepenaltycharge,")
            .append(" i.hire_penalty_percentage as hirepenaltypercentage, i.repair_penalty_charge as repairpenaltycharge,")
            .append(" i.repair_penalty_percentage as repairpenaltypercentage, i.total_penalty_charge as totalpenaltycharge,")
            .append(" i.total_net as totalnet, i.total_vat as totalvat, i.total_gross as totalgross, i.discount as discount,")
            .append(" i.insurer_discount as insurerdiscount, i.full_total_to_pay as fulltotaltopay, io.full_total_to_pay as original_fulltotaltopay,")
            .append(" i.total_to_pay as totaltopay, io.total_to_pay as original_totaltopay, i.interim_payment_made as interimpaymentmade,")
            .append(" i.interim_payment_received as interimpaymentreceived, i.date_invoiced as dateinvoiced, i.hire_gross_paid as hiregrosspaid,")
            .append(" i.repair_gross_paid as repairgrosspaid, i.engineer_fee_gross_paid as engineerfeegrosspaid, i.total_loss_fee_gross_paid as totallossfeegrosspaid,")
            .append(" i.storage_recovery_gross_paid as storagerecoverygrosspaid, i.hire_penalty_charge_paid as hirepenaltychargepaid,")
            .append(" i.repair_penalty_charge_paid as repairpenaltychargepaid, i.claim_handler_charge_paid as claimhandlerchargepaid,")
            .append(" i.deduction_claim_handler_fee_paid as deductionclaimhandlerfeepaid, i.cho_discount_fee_paid as chodiscountfeepaid,")
            .append(" i.insurer_discount_fee_paid as insurerdiscountfeepaid, i.final_payment as finalpayment")
            .append(" from claim c")
            .append(" left outer join third_party tp on (c.third_party_id = tp.id)")
            .append(" join invoice i on (c.invoice_id = i.id)")
            .append(" join invoice_original io on (i.invoice_original_id = io.id)")

//            .append(" where c.id in ( :claimIds )");
            .append(" where c.id in (");

        boolean first = true;
        for (Integer id : ids) {
            if (!first)
                sb.append(", ").append(id.toString());
            else {
                sb.append(id.toString());
                first = false;
            }
        }            
        sb.append(") ");
        sb.append(" order by createddate");

//        Map paramMap = new HashMap();
//        paramMap.put("claimIds", ids);

        LOG.debug("Querying for invoice details...\n{}", sb.toString());
//        List result = this.externalQuery(sb.toString(), paramMap);
        List result = this.externalQuery(sb.toString());
        LOG.debug("Got invoice details - building data objects");

        for(Object obj : result)
            results.add(new ExcelInvoice((Map)obj, this.getCurrentUser().isCHO()));

        LOG.debug("Returning results.");

        return results;
    }
    
    @Override
    public List<ExcelHistory> getExcelHistory(List<Integer> ids) {
        StringBuilder sb = new StringBuilder();
        sb.append("select ")
            .append(" c.cho_reference as choreference, h.process_date as processdate,")
            .append(" h.rule_id as ruleid, h.type as type, h.narrative as narrative,")
            .append(" h.is_public as ispublic")
            .append(" from claim c")
            .append(" join invoice i on (c.invoice_id = i.id)")
            .append(" join history h on (c.id = h.claim_id)")
            .append(" where h.type != 'INFO'")

//            .append(" and c.id in ( :claimIds )");
            .append(" and c.id in (");

        boolean first = true;
        for (Integer id : ids) {
            if (!first)
                sb.append(", ").append(id.toString());
            else {
                sb.append(id.toString());
                first = false;
            }
        }            
        sb.append(") ");
        sb.append(" order by choreference, processdate, ruleid");

//        Map paramMap = new HashMap();
//        paramMap.put("claimIds", ids);

        LOG.debug("Querying for BRE history details...\n{}", sb.toString());
//        List result = this.externalQuery(sb.toString(), paramMap);
        List result = this.externalQuery(sb.toString());
        LOG.debug("Got BRE history details - building data objects");

        List<ExcelHistory> results = new ArrayList<ExcelHistory>(result.size());
        for(Object obj : result) {
            ExcelHistory history = new ExcelHistory((Map)obj);
            if (!getCurrentUser().isCHO() ||  history.isVisibleToCHO())
                results.add(history);
        }

        return results;
    }
    
    @Override
    public List<ExcelComment> getExcelComments(List<Integer> ids) {
        StringBuilder sb = new StringBuilder();
        sb.append("select ")
            .append("     c.cho_reference as choreference, n.created_date as createddate,")
            .append("     wu.first_name || ' ' || wu.last_name as createdby, ")
            .append("     n.comment as comment, n.visibility_type as visibilitytype")
            .append(" from claim c")
            .append(" join comment n on (c.id = n.claim_id)")
            .append(" left outer join web_user wu on (n.created_by = wu.id)")
            .append(" where n.reverted = false")

//            .append(" and c.id in ( :claimIds )");
            .append(" and c.id in (");

        boolean first = true;
        for (Integer id : ids) {
            if (!first)
                sb.append(", ").append(id.toString());
            else {
                sb.append(id.toString());
                first = false;
            }
        }            
        sb.append(") ");
        sb.append(" order by  choreference, createddate");

//        Map paramMap = new HashMap();
//        paramMap.put("claimIds", ids);

        LOG.debug("Querying for BRE history details...\n{}", sb.toString());
//        List result = this.externalQuery(sb.toString(), paramMap);
        List result = this.externalQuery(sb.toString());
        LOG.debug("Got BRE history details - building data objects");

        List<ExcelComment> results = new ArrayList<ExcelComment>(result.size());
        for(Object obj : result) {
            ExcelComment comment = new ExcelComment((Map)obj);
            if ((getCurrentUser().isCHO() && comment.getVisibilityType() == 1)
                    || (getCurrentUser().isAnInsurer() && comment.getVisibilityType() == 2))
                continue;

            results.add(new ExcelComment((Map)obj));
        }

        
        return results;
    }
    
    @Override
    public List<ExcelClaimCycle> getExcelClaimCycle(List<Integer> ids) {
        StringBuilder sb = new StringBuilder();
        sb.append("select ")
            .append(" c.cho_reference as choreference, a.created_date as modifieddate,")
            .append(" case when ins.name is not null then wu.first_name || ' ' || wu.last_name || ' (' || ins.name || ')'")
            .append("       else case when cho.name is not null then wu.first_name || ' ' || wu.last_name || ' (' || cho.name || ')'")
            .append("            else wu.first_name || ' ' || wu.last_name end end as modifiedby,")
            .append(" a.new_status as status, a.reverted as reverted")
            .append(" from claim c")
            .append(" join audit_trail a on (c.id = a.claim_id)")
            .append(" left outer join web_user wu on (a.created_by = wu.id)")
            .append(" left outer join insurer ins on (wu.insurer_id = ins.id)")
            .append(" left outer join chorganisation cho on (wu.chorganisation_id = cho.id)")
//            .append(" where c.id in ( :claimIds )");
            .append(" where c.id in (");

        boolean first = true;
        for (Integer id : ids) {
            if (!first)
                sb.append(", ").append(id.toString());
            else {
                sb.append(id.toString());
                first = false;
            }
        }            
        sb.append(") ");
        sb.append(" order by  choreference, modifieddate");

//        Map paramMap = new HashMap();
//        paramMap.put("claimIds", ids);

        LOG.debug("Querying for claim cycle details...\n{}", sb.toString());
//        List result = this.externalQuery(sb.toString(), paramMap);
        List result = this.externalQuery(sb.toString());
        LOG.debug("Got claim cycle details - building data objects");

        List<ExcelClaimCycle> results = new ArrayList<ExcelClaimCycle>(result.size());
        for(Object obj : result)
            results.add(new ExcelClaimCycle((Map)obj));

        return results;
    }
    
}
