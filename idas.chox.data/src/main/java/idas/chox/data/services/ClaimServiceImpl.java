package idas.chox.data.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import idas.chox.core.model.AuditTrail;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.LiabilityStatus;
import idas.chox.core.model.Notification;
import idas.chox.core.model.NotificationType;
import idas.chox.core.model.ThirdParty;
import idas.chox.core.search.ClaimSearchCriteria;
import idas.chox.core.search.SearchResult;
import idas.chox.core.services.AuditTrailService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.util.RoleHelper;
import java.text.DecimalFormat;

public class ClaimServiceImpl extends SecureDataService implements ClaimService, Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(ClaimServiceImpl.class);
    private AuditTrailService auditTrailService;
    public static final String PENDING = "Pending";
    public static final String IN_PROGRESS = "InProgress";
    public static final String COMPLETE = "Complete";
    public static final String CANCELLED = "Cancelled";
    public static final String NEW_CLAIM = "1st Notification";

    public void setAuditTrailService(AuditTrailService auditTrailService) {
        this.auditTrailService = auditTrailService;
    }

    public ClaimServiceImpl() {
        super();
        return;
    }

    public Claim getClaim(int id) {
        return (Claim) get(Claim.class, id);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateClaim(Claim claim) {
        claim.setClaimNumber(claim.getClaimNumber().trim());
        save(claim);
        LOG.debug("Claim updated and saved.");
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateSaveLiabilityStatus(Claim claim) {
        save(claim);
    }

    public void save(Claim object) {
        object.updateLiabilityPayment();
        super.save(object);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Boolean revertClaim(int id) {
        Boolean result = false;
        AuditTrail auditTrail;
        if ((auditTrail = auditTrailService.getLastChange(id)) != null) {
            Claim claim = (Claim) get(Claim.class, id);
            claim.setPreviousStatus(claim.getStatus());
            claim.setStatus(auditTrail.getOriginalStatus());
            claim.setStatusModifiedDate(new Date());
            save(claim);
            LOG.debug("Claim status reverted and saved.");
            result = true;
        } else {
            LOG.warn("Could not revert claim status.");
        }

        return result;
    }

    public Long getECDCountByClaimId(int claimId) {
        String q = "select count(*) from HireMonitoringEcd where claim.id = '" + claimId + "'";
        return getCount(q);
    }

    public Long getClaimCountByClaimNumber(String claimNumber, int claimId) {
        String q = "select count(*) from Claim where claimNumber = '" + claimNumber + "' And id != '" + claimId + "'";
        return getCount(q);
    }

    public List getOtherClaimsByClaimNumber(String claimNumber, int claimId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
        criteria.add(Restrictions.eq("claimNumber", claimNumber));
        criteria.add(Restrictions.ne("id", claimId));
        List result = this.findByCriteria(criteria);
        return result;
    }

    public Integer getCountOfClaimByVRN(String strVRN, int claimId) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
        criteria.setProjection(Projections.rowCount());
        criteria.createCriteria("customer").add(Restrictions.like("vehicleRegistration", strVRN).ignoreCase());
        criteria.add(Expression.ne("id", claimId));
        List result = findByCriteria(criteria);
        Integer totalCount = (Integer) result.get(0);
        return totalCount;


    }

    // this method has been implemented for TPI claim as there is no claim id already exist in the database.
    // and it will still check if there is any claim which has customer with same vrn number in some other claim.
    // if same vrn exist (if the count more than 0) then rule no-21 will be failed.
    @Override
    public Integer getCountOfClaimByVRNforTPIClaim(String strVRN, Claim claim) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
        criteria.setProjection(Projections.rowCount());
        criteria.createCriteria("customer").add(Restrictions.like("vehicleRegistration", strVRN).ignoreCase());
        // at some point this method need to be removed and use the getCountOfClaimByVRN(String strVRN, int claimId) above method.
        // instead checking claim.getStatus()!=null should check the claim existence in the system. this change has to be added to the above mentioned method.
        // depricated hibernate method should be removed.
        if (claim.getStatus() != null) {
            criteria.add(Expression.ne("id", claim.getId()));
        }
        List result = findByCriteria(criteria);
        Integer totalCount = (Integer) result.get(0);
        return totalCount;


    }

    public Claim getClaimByCHOReferenceNumber(String sClaimReferenceNumber) {
        Claim claim = new Claim();
        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
        criteria.add(Restrictions.eq("choReference", sClaimReferenceNumber));
        claim = (Claim) getByCriteria(criteria);
        return claim;
    }

    public Boolean isCustomerClaimNumberExist(String strClaimNumber, int claimId, Boolean isClaimExit) {

        Boolean bFlag = false;

        if (!strClaimNumber.equalsIgnoreCase("")) {
            DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
            criteria.setProjection(Projections.rowCount());
            criteria.createCriteria("customer").add(Restrictions.like("claimReference", strClaimNumber).ignoreCase());
            if (isClaimExit) {
                criteria.add(Expression.ne("id", claimId));
            }
            List result = findByCriteria(criteria);

            Integer totalCount = (Integer) result.get(0);
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
                criteria.add(Expression.ne("id", claimId));
            }
            List result = findByCriteria(criteria);

            Integer totalCount = (Integer) result.get(0);
            bFlag = totalCount > 0;
        }

        return bFlag;
    }

    public SearchResult searchClaims(ClaimSearchCriteria searchCriteria) {
        //return searchClaims(searchCriteria, 0, Integer.MAX_VALUE, "", "");
        return searchClaims(searchCriteria, 0, Integer.MAX_VALUE, "created", "desc");
    }

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
            } else if (sort.equalsIgnoreCase("invoiceAmount")) {
                addSort(criteria, "iv.invoiceAmount", dir);
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

    public Integer countClaims(ClaimSearchCriteria searchCriteria) {

        Criteria criteria = buildSearchCriteria(searchCriteria);
        return countClaims(criteria);
    }

    public Boolean isClaimSupplierReferenceNumberExist(String sClaimReferenceNumber) {

        Boolean bFlag = false;

        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
        criteria.setProjection(Projections.rowCount());
        criteria.add(Restrictions.like("choReference", sClaimReferenceNumber.trim()).ignoreCase());
        List result = findByCriteria(criteria);

        Integer totalCount = (Integer) result.get(0);
        bFlag = totalCount > 0;

        return bFlag;
    }

    public Boolean isObjectExist(int WorkgroupId) {

        boolean isExist = false;



        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);

        criteria.add(Restrictions.eq("workgroup.id", WorkgroupId));

        if (findByCriteria(criteria).size() > 0) {
            isExist = true;
        }



        return isExist;

    }

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

    public boolean isOpenClaimByWorkgroupExist(int WorkgroupId) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
        criteria.add(Restrictions.eq("workgroup.id", WorkgroupId));

        for (String sStatus : ClaimStatus.getClosedStatus()) {
            criteria.add(Restrictions.ne("status", sStatus));
        }

        if (findByCriteria(criteria).size() > 0) {
            return true;
        }

        return false;

    }

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

    public boolean isOpenClaimByWorkgroupIdByUserExist(int insurerId, int WorkgroupId, int UserId) {

        boolean isExist = false;
        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
        criteria.add(Restrictions.eq("workgroup.id", WorkgroupId));
        criteria.add(Restrictions.eq("insurer.id", insurerId));

        if (UserId > 0) {
            criteria.add(Restrictions.eq("claimOwner.id", UserId));
        }

        for (String sStatus : ClaimStatus.getClosedStatus()) {
            criteria.add(Restrictions.ne("status", sStatus));
        }

        if (findByCriteria(criteria).size() > 0) {
            isExist = true;
        }

        return isExist;

    }

    public boolean isUserHasOpenClaim(int userId) {

        boolean isExist = false;

        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
        criteria.add(Restrictions.eq("claimOwner.id", userId));

        for (String sStatus : ClaimStatus.getClosedStatus()) {
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
        return (Integer) totalCountResult.get(0);
    }

    private Criteria buildSearchCriteria(ClaimSearchCriteria searchCriteria) {
        Criteria criteria = getSession().createCriteria(Claim.class).createAlias("this.invoice", "iv", CriteriaSpecification.LEFT_JOIN).createAlias("this.customer", "cs", CriteriaSpecification.LEFT_JOIN).createAlias("this.workgroup", "wg", CriteriaSpecification.LEFT_JOIN).createAlias("this.thirdParty", "tp", CriteriaSpecification.LEFT_JOIN).createAlias("this.vehicleHire", "vh", CriteriaSpecification.LEFT_JOIN).createAlias("this.chorganisation", "cho", CriteriaSpecification.LEFT_JOIN).createAlias("this.createdBy", "cb", CriteriaSpecification.LEFT_JOIN).createAlias("this.claimOwner", "co", CriteriaSpecification.LEFT_JOIN).createAlias("this.supplierClaimOwner", "sco", CriteriaSpecification.LEFT_JOIN).createAlias("this.hireMonitoringDetail", "hmd", CriteriaSpecification.LEFT_JOIN).createAlias("this.insurer", "ins", CriteriaSpecification.LEFT_JOIN);

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

        if (searchCriteria.getClaimOwnerId() > 0) {
            criteria.add(Restrictions.eq("claimOwner.id", searchCriteria.getClaimOwnerId()));
        } else if (searchCriteria.getClaimOwnerId() == ClaimSearchCriteria.CLAIM_OWNER_NOT_ASSIGNED) {
            criteria.add(Restrictions.isNull("claimOwner.id"));
        }

        if (searchCriteria.getSupplierClaimOwnerId() > 0) {
            criteria.add(Restrictions.eq("supplierClaimOwner.id", searchCriteria.getSupplierClaimOwnerId()));
        } else if (searchCriteria.getSupplierClaimOwnerId() == ClaimSearchCriteria.CLAIM_OWNER_NOT_ASSIGNED) {
            criteria.add(Restrictions.isNull("supplierClaimOwner.id"));
        }

        if (searchCriteria.getSupplierReference() != null && !searchCriteria.getSupplierReference().isEmpty()) {
            String sSupplierRef = searchCriteria.getSupplierReference();
            criteria.add(Restrictions.like("choReference", sSupplierRef).ignoreCase());
        }

        if (searchCriteria.getStatus() != null && !searchCriteria.getStatus().isEmpty()) {
            if (searchCriteria.getStatus().equals(ClaimSearchCriteria.STATUS_ACTIONS_FOR_HANDLERS)) {
                criteria.add(Restrictions.in("status", new Object[]{"ClaimUnacknowledgedRouted", "ClaimRejectionContested", "ClaimPending", "ClaimUpdatedByEngineer", "InvoiceReferredToClaimsHandler", "InvoiceEscalatedToHandler", "ContestedInvoiceReferredToInsurer", "InvoiceApprovedByBRE", "AwaitingInvoicePayment", "AwaitingLiabilityResolution"}));
            } else {
                criteria.add(Restrictions.eq("status", searchCriteria.getStatus()));
            }
        }

        if (searchCriteria.getStatusExcludeList() != null && !searchCriteria.getStatusExcludeList().isEmpty()) {
            criteria.add(Restrictions.not(Restrictions.in("status", searchCriteria.getStatusExcludeList())));

        }

        if (searchCriteria.getInsurerId() > 0) {
            criteria.add(Restrictions.eq("ins.id", searchCriteria.getInsurerId()));
        }

        if (searchCriteria.getSupplierId() > 0) {
            criteria.add(Restrictions.eq("cho.id", searchCriteria.getSupplierId()));
        }

        if (searchCriteria.getWorkgroupId() > 0) {
            criteria.add(Restrictions.eq("wg.id", searchCriteria.getWorkgroupId()));
        }

        if (searchCriteria.getIsAnomalies()) {

            Set anomaliesStatus = new HashSet();
            anomaliesStatus.add(ClaimStatus.CLAIM_REF_TO_ENG);
            anomaliesStatus.add(ClaimStatus.CLAIM_REFERRED_TO_FNOL);
            anomaliesStatus.add(ClaimStatus.CLAIM_REJECTION_CONTESTED);
            anomaliesStatus.add(ClaimStatus.CLAIM_PENDING);
            anomaliesStatus.add(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
            anomaliesStatus.add(ClaimStatus.CLAIM_REJECTED);
            anomaliesStatus.add(ClaimStatus.CLAIM_UPDATE_BY_ENG);
            anomaliesStatus.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);




            DetachedCriteria noti = DetachedCriteria.forClass(Notification.class).add(Restrictions.in("type", NotificationType.getInsurerNotificationTypes())).add(Restrictions.eq("isacknowledged", false)).setProjection(Projections.distinct(Projections.projectionList().add(Projections.property("claim"))));
            criteria.add(Subqueries.propertyIn("id", noti));
            criteria.add(Restrictions.in("status", anomaliesStatus));

        }

        if (searchCriteria.isLiabilityStatusUpdated()) {
            DetachedCriteria noti = DetachedCriteria.forClass(Notification.class).add(Restrictions.in("type", NotificationType.getChoNotificationTypes())).setProjection(Projections.distinct(Projections.projectionList().add(Projections.property("claim"))));
            criteria.add(Subqueries.propertyIn("id", noti));
        }

        if (searchCriteria.getIspenaltyChargeApplied()) {
            criteria.add(Restrictions.ne("status", ClaimStatus.INVOICE_PAYMENT_LOGGED));
            criteria.add(Restrictions.ne("status", ClaimStatus.CLAIM_CLOSED));
            criteria.add(Restrictions.ne("status", ClaimStatus.INVOICE_REJECTED_ACCEPTED));
            criteria.add(Restrictions.ne("status", ClaimStatus.INVOICE_PAYMENT_RECEIVED));
            criteria.add(Restrictions.ne("status", ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT));
            criteria.add(Restrictions.ge("iv.penaltyAlertQty", 0));
            criteria.add(Restrictions.sqlRestriction("extract(epoch from current_date- iv1_.created_date)/(3600*24) >(iv1_.penalty_alert_qty+1)*30"));

            Junction nonSplit = Restrictions.disjunction().add(Restrictions.isNull("liabilityStatus")).add(Restrictions.conjunction().add(Restrictions.ne("liabilityStatus", LiabilityStatus.LIABILITY_SPLIT)).add(Restrictions.ne("liabilityStatus", LiabilityStatus.PROCEED_WITHOUT_PREJUDICE)));

            Junction split = Restrictions.conjunction().add(Restrictions.sqlRestriction("extract(epoch from current_date - liability_agreed_date)/(3600*24) >(iv1_.penalty_alert_qty+1)*30")).add(Restrictions.disjunction().add(Restrictions.eq("liabilityStatus", LiabilityStatus.LIABILITY_SPLIT)).add(Restrictions.eq("liabilityStatus", LiabilityStatus.PROCEED_WITHOUT_PREJUDICE)));
            criteria.add(Restrictions.disjunction().add(nonSplit).add(split));

        }

        if (searchCriteria.getIsInterimPaymentMade()) {
            criteria.add(Restrictions.eq("iv.interimPaymentReceived", false));
        }

        if (searchCriteria.getLiabilityStatus() != null && searchCriteria.getLiabilityStatus().ordinal() > 0) {
            criteria.add(Restrictions.eq("liabilityStatus", searchCriteria.getLiabilityStatus()));
            LOG.debug("Liability Search Criteria: {}", searchCriteria.getLiabilityStatus());
        } else {
            LOG.debug("Liability Search Criteria not present: '{}'", searchCriteria.getLiabilityStatus());
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

        if (searchCriteria.getIsOpenClaim()) {
            criteria.add(Restrictions.ne("status", ClaimStatus.CLAIM_REJECTION_ACCEPTED));
            criteria.add(Restrictions.ne("status", ClaimStatus.INVOICE_REJECTED_ACCEPTED));
            criteria.add(Restrictions.ne("status", ClaimStatus.CLAIM_CLOSED));
            criteria.add(Restrictions.ne("status", ClaimStatus.INVOICE_PAYMENT_RECEIVED));
        }

        if (searchCriteria.getClaimUploadDateFrom() != null) {
            Date d = searchCriteria.getClaimUploadDateFrom();
            d.setHours(0);
            d.setMinutes(0);
            d.setSeconds(0);
            criteria.add(Expression.ge("createdDate", d));
        }

        if (searchCriteria.getClaimUploadDateTo() != null) {
            Date d = searchCriteria.getClaimUploadDateTo();
            d.setDate(d.getDate());
            d.setHours(23);
            d.setMinutes(59);
            d.setSeconds(59);
            criteria.add(Expression.le("createdDate", d));
        }

        if (searchCriteria.getStatusModifiedDateFrom() != null) {
            Date d = searchCriteria.getStatusModifiedDateFrom();
            d.setHours(0);
            d.setMinutes(0);
            d.setSeconds(0);
            criteria.add(Expression.ge("statusModifiedDate", d));
        }

        if (searchCriteria.getStatusModifiedDateTo() != null) {
            Date d = searchCriteria.getStatusModifiedDateTo();
            d.setDate(d.getDate());
            d.setHours(23);
            d.setMinutes(59);
            d.setSeconds(59);
            criteria.add(Expression.le("statusModifiedDate", d));
        }


        if (searchCriteria.getReviewRequiredDateFrom() != null || searchCriteria.getReviewRequiredDateTo() != null) {

            if (searchCriteria.getReviewRequiredDateFrom() != null) {

                Date d = searchCriteria.getReviewRequiredDateFrom();
                d.setHours(0);
                d.setMinutes(0);
                d.setSeconds(0);
                criteria.add(Expression.ge("hmd.nextReviewDate", d));

            }

            if (searchCriteria.getReviewRequiredDateTo() != null) {

                Date d = searchCriteria.getReviewRequiredDateTo();
                d.setDate(d.getDate());
                d.setHours(23);
                d.setMinutes(59);
                d.setSeconds(59);
                criteria.add(Expression.le("hmd.nextReviewDate", d));

            }

        }

        if (searchCriteria.getInvoiceUploadDateFrom() != null || searchCriteria.getInvoiceUploadDateTo() != null) {

            if (searchCriteria.getInvoiceUploadDateFrom() != null) {
                Date d = searchCriteria.getInvoiceUploadDateFrom();
                d.setHours(0);
                d.setMinutes(0);
                d.setSeconds(0);
                criteria.add(Expression.ge("iv.createdDate", d));
            }

            if (searchCriteria.getInvoiceUploadDateTo() != null) {
                Date d = searchCriteria.getInvoiceUploadDateTo();
                d.setDate(d.getDate());
                d.setHours(23);
                d.setMinutes(59);
                d.setSeconds(59);
                criteria.add(Expression.le("iv.createdDate", d));
            }

        }

        if (searchCriteria.getHireDateFrom() != null || searchCriteria.getHireDateTo() != null) {

            if (searchCriteria.getHireDateFrom() != null) {
                Date d = searchCriteria.getHireDateFrom();
                d.setHours(0);
                d.setMinutes(0);
                d.setSeconds(0);
                criteria.add(Expression.ge("vh.rentalStart", d)).add(Expression.le("vh.rentalEnd", d));
            }

            if (searchCriteria.getHireDateTo() != null) {
                Date d = searchCriteria.getHireDateTo();
                d.setDate(d.getDate());
                d.setHours(23);
                d.setMinutes(59);
                d.setSeconds(59);
                criteria.add(Expression.ge("vh.rentalStart", d)).add(Expression.le("vh.rentalEnd", d));
            }
        }

        if (searchCriteria.getLastModifiedDateFrom() != null || searchCriteria.getLastModifiedDateTo() != null) {

            if (searchCriteria.getLastModifiedDateFrom() != null) {
                Date d = searchCriteria.getLastModifiedDateFrom();
                d.setHours(0);
                d.setMinutes(0);
                d.setSeconds(0);
                criteria.add(Expression.ge("lastModifiedDate", d));
            }

            if (searchCriteria.getLastModifiedDateTo() != null) {
                Date d = searchCriteria.getLastModifiedDateTo();
                d.setDate(d.getDate());
                d.setHours(23);
                d.setMinutes(59);
                d.setSeconds(59);
                criteria.add(Expression.le("lastModifiedDate", d));
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
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        LOG.debug("Getting number of days claim was with CHO for review");
        double days = auditTrailService.getTimeInvoiceWithCHO(id);
        return doubleToTime(days);
    }

    @Override
    public String getDaysWithInsurerForReview(int id) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        LOG.debug("Getting number of days claim was with Insurer for review");
        double days = auditTrailService.getTimeInvoiceWithInsurer(id);
        return doubleToTime(days);
    }

    @Override
    public String getDaysAwaitingLiabilityResolution(int id) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
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

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Boolean switchClaim(int claimId) {
        Claim claim = (Claim) get(Claim.class, claimId);
        Insurer oldInsurer = claim.getInsurer();
        Insurer newInsurer = oldInsurer.getRelatedInsurer();
        LOG.debug("Switching claim with CHO reference '{}' to {}", claim.getChoReference(), newInsurer.getName());

        claim.setInsurer(newInsurer);
        claim.setClaimOwner(null);
        claim.setWorkgroup(null);
        claim.setPreviousStatus(claim.getStatus());
        claim.setStatusModifiedDate(new Date());
        claim.setLiabilityStatus(LiabilityStatus.LIABILITY_NULL);
        claim.setLiabilityAgreedDate(null);
        claim.setCreatedDate(new Date());



        if (newInsurer.isWorkgroupEnable()) {
            claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        } else {
            claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        }

        LOG.debug("Switching Claim Action : Claim has been updated");

        ThirdParty thirdParty = claim.getThirdParty();
        thirdParty.setInsurer(newInsurer);
        thirdParty.setInsurerBrand(newInsurer.getName());

        LOG.debug("Switching Claim Action : ThirdParty has been updated");

        Comment comment = Comment.New(0, "Claim switched from " + oldInsurer.getName() + " to " + newInsurer.getName());
        claim.addComment(comment);

        LOG.debug("Switching Claim Action : Comment has been updated");

        if (auditTrailService.logAuditLogForce(claim.getStatus(), claim.getPreviousStatus(), claim)) {
            LOG.debug("Switching Claim Action : AuditTrail has been updated");
        } else {
            LOG.debug("Switching Claim Action : AuditTrail has not been updated");
        }

        save(claim);

        LOG.debug("Switching Claim Action : Claim {} has been switched to {}", claimId, newInsurer);
        return true;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void saveClaimWithoutUpdatingLiabilityPayment(Claim claim) {
        super.save(claim);
    }
}
