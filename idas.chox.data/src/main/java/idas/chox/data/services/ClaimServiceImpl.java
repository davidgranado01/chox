package idas.chox.data.services;

import idas.chox.core.common.OrganisationType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
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
import idas.chox.core.model.BreBand;
import idas.chox.core.model.BreBandOrganisation;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Comment;
import idas.chox.core.model.Invoice;
import idas.chox.core.model.LiabilityStatus;
import idas.chox.core.model.Notification;
import idas.chox.core.model.NotificationType;
import idas.chox.core.search.ClaimSearchCriteria;
import idas.chox.core.search.SearchResult;
import idas.chox.core.services.AuditTrailService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.util.RoleHelper;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;
import org.apache.http.impl.cookie.DateUtils;
import org.hibernate.criterion.Property;

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

    @Override
    public Claim getClaim(int id) {
        return (Claim) get(Claim.class, id);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public void updateClaim(Claim claim) {
        claim.setClaimNumber(claim.getClaimNumber().trim());
        save(claim);
        LOG.debug("Claim updated and saved.");
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public void updateSaveLiabilityStatus(Claim claim) {
        save(claim);
    }

    public void save(Claim object) {
        object.updateLiabilityPayment();
        super.save(object);
    }

//    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public Boolean revertClaim(int id) {
        Boolean result = false;
        AuditTrail auditTrail;
        if ((auditTrail = auditTrailService.getLastChange(id)) != null) {
            Claim claim = (Claim) get(Claim.class, id);
          
            if (ClaimStatus.INVOICE_PAYMENT_LOGGED.equals(claim.getStatus())) {
                // Log note
                Comment comment = Comment.New(0, "The claim was marked as 'Invoice Payment Logged' on " + DateUtils.formatDate(auditTrail.getUpdateDate()) + ", however the CHO has not received the payment. Please check the payment details in your claim system.");
                claim.addComment(comment);
            }
            claim.setStatus(auditTrail.getOriginalStatus());
//            claim.setPreviousStatus(claim.getAvailableStatus()); - not needed (done by interceptor)
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
            if(claim.getStatus().equals(ClaimStatus.INVOICE_PAYMENT_LOGGED) && claim.getInvoice().getInterimPaymentReceivedFullAndFinal()!=null && claim.getInvoice().getInterimPaymentReceivedFullAndFinal()){
               claim.getInvoice().setInterimPaymentReceived(false);
               claim.getInvoice().setInterimPaymentReceivedFullAndFinal(false);
               claim.getInvoice().setTotalToPay(claim.getInvoice().getFullTotalToPay());
            }
            if (claim.getStatus().equals(ClaimStatus.AWAITING_INVOICE_PAYMENT)) {
                claim.getInvoice().setHireGrossPaid(BigDecimal.ZERO);
                claim.getInvoice().setRepairGrossPaid(BigDecimal.ZERO);
                claim.getInvoice().setEngineerFeeGrossPaid(BigDecimal.ZERO);
                claim.getInvoice().setTotalLossFeeGrossPaid(BigDecimal.ZERO);
                claim.getInvoice().setStorageRecoveryGrossPaid(BigDecimal.ZERO);
                claim.getInvoice().setHirePenaltyChargePaid(BigDecimal.ZERO);
                claim.getInvoice().setRepairPenaltyChargePaid(BigDecimal.ZERO);
                claim.getInvoice().setTotalPaid(BigDecimal.ZERO);
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
        Claim claim = new Claim();
        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
        criteria.add(Restrictions.eq("choReference", sClaimReferenceNumber.trim()).ignoreCase());
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

        Boolean bFlag = false;

        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
        criteria.setProjection(Projections.rowCount());
        criteria.add(Restrictions.like("choReference", sClaimReferenceNumber.trim()).ignoreCase());
        List result = findByCriteria(criteria);

        Integer totalCount = ((Long) result.get(0)).intValue();
        bFlag = totalCount > 0;

        return bFlag;
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
        Criteria criteria = getSession().createCriteria(Claim.class).createAlias("this.invoice", "iv", CriteriaSpecification.LEFT_JOIN)
                .createAlias("this.customer", "cs", CriteriaSpecification.LEFT_JOIN)
                .createAlias("this.workgroup", "wg", CriteriaSpecification.LEFT_JOIN)
                .createAlias("this.thirdParty", "tp", CriteriaSpecification.LEFT_JOIN)
                .createAlias("this.vehicleHire", "vh", CriteriaSpecification.LEFT_JOIN)
                .createAlias("this.chorganisation", "cho", CriteriaSpecification.LEFT_JOIN)
                .createAlias("this.createdBy", "cb", CriteriaSpecification.LEFT_JOIN)
                .createAlias("this.claimOwner", "co", CriteriaSpecification.LEFT_JOIN)
//                .createAlias("this.choband", "choband", CriteriaSpecification.LEFT_JOIN)
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

            DetachedCriteria noti = DetachedCriteria.forClass(Notification.class).add(Restrictions.in("type", NotificationType.getInsurerNotificationTypes())).add(Restrictions.eq("isacknowledged", false)).setProjection(Projections.projectionList().add(Projections.property("claim")));
            criteria.add(Subqueries.propertyIn("id", noti));
            criteria.add(Restrictions.in("status", anomaliesStatus));

        }

        if (searchCriteria.isLiabilityStatusUpdated()) {
            DetachedCriteria noti = DetachedCriteria.forClass(Notification.class).add(Restrictions.in("type", NotificationType.getChoNotificationTypes())).setProjection(Projections.projectionList().add(Projections.property("claim")));
            criteria.add(Subqueries.propertyIn("id", noti));
        }

        if (searchCriteria.getIsPenaltyChargeApplied()) {
            criteria.add(Restrictions.ne("status", ClaimStatus.INVOICE_PAYMENT_LOGGED));
            criteria.add(Restrictions.ne("status", ClaimStatus.CLAIM_CLOSED));
            criteria.add(Restrictions.ne("status", ClaimStatus.INVOICE_REJECTED_ACCEPTED));
            criteria.add(Restrictions.ne("status", ClaimStatus.INVOICE_PAYMENT_RECEIVED));
            criteria.add(Restrictions.ne("status", ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT));
            criteria.add(Restrictions.ne("status", ClaimStatus.MANUAL_INVOICE_APPROVED));
            criteria.add(Restrictions.ne("status", ClaimStatus.MANUAL_INVOICE_PAID));
            criteria.add(Restrictions.ne("status", ClaimStatus.MANUAL_INVOICE_REJECTED));
            criteria.add(Restrictions.ge("iv.penaltyAlertQty", 0));
            criteria.add(Restrictions.sqlRestriction("extract(epoch from current_date- iv1_.created_date)/(3600*24) >(iv1_.penalty_alert_qty+1)*30"));
            criteria.add(Restrictions.disjunction()
                        .add(Restrictions.eq("autoPenaltyChargeEnabled", Boolean.FALSE))
                        .add(Restrictions.conjunction()
                            .add(Restrictions.eq("autoPenaltyChargeEnabled", Boolean.TRUE))
                            .add(Restrictions.eq("cho.autoPenaltyChargeEnabled", Boolean.FALSE))));

            if (!OrganisationType.CHO.equals(getCurrentUser().getOrganisationType())) {
                LOG.warn("Error in search criteria: only CHO can filter for penalty charges");
            }
            else {
                LOG.debug("Supplier Id={}", getCurrentUser().getChorganisation().getId());
                // Get the id's of the BRE Bands mapped to this CHO
                DetachedCriteria bCriteria =  DetachedCriteria.forClass(BreBandOrganisation.class, "brebandorganisation")
                    .createAlias("brebandorganisation.chorganisation", "cho", CriteriaSpecification.LEFT_JOIN)
                    .add(Restrictions.eq("cho.id", getCurrentUser().getChorganisation().getId()));
                bCriteria.setProjection( Projections.property("brebandorganisation.breBand.id") );

                // Get the insurers from the BRE Band which don't allow penalty charges to be added
                DetachedCriteria pCriteria =  DetachedCriteria.forClass(BreBand.class, "breband")
                    .add(Restrictions.eq("breband.allowPenaltyCharges", Boolean.FALSE))
                    .add(Restrictions.in("breband.id", bCriteria.getExecutableCriteria(getSession()).list() ))
                    .setProjection( Projections.property("breband.insurer") );

                // Make sure we retrieve no claims for insurers who don't allow penalty charges to be added
                criteria.add(Property.forName("this.insurer").notIn( pCriteria ) );
            }
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
            for (String status : ClaimStatus.getCompletedStatus(true)) {
                criteria.add(Restrictions.ne("status", status));
            }
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
}
