package idas.chox.data.services;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

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

import idas.chox.core.common.OrganisationType;
import idas.chox.core.model.AuditTrail;
import idas.chox.core.model.BreBand;
import idas.chox.core.model.BreBandOrganisation;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Comment;
import idas.chox.core.model.HireMonitoringEcd;
import idas.chox.core.model.History;
import idas.chox.core.model.Invoice;
import idas.chox.core.model.LiabilityStatus;
import idas.chox.core.model.Notification;
import idas.chox.core.model.QueuedTicket;
import idas.chox.data.notifications.NotificationType;
import idas.chox.core.search.ClaimSearchCriteria;
import idas.chox.core.search.SearchResult;
import idas.chox.core.services.AuditTrailService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.CommentService;
import idas.chox.core.services.NotificationService;
import idas.chox.core.services.UserService;
import idas.chox.core.util.DateHelper;
import idas.chox.core.util.RoleHelper;
import java.util.Map;

public class ClaimServiceImpl extends SecureDataService implements ClaimService, Serializable {
    public static final String PENDING = "Pending";
    public static final String IN_PROGRESS = "InProgress";
    public static final String COMPLETE = "Complete";
    public static final String CANCELLED = "Cancelled";
    public static final String NEW_CLAIM = "1st Notification";

    private static final Logger LOG = LoggerFactory.getLogger(ClaimServiceImpl.class);
    private AuditTrailService auditTrailService;
    private CommentService commentService;
    private UserService userService;
    private NotificationService notificationService;
    private boolean enableActivityMonitor;
    private int activityMonitorRequestInterval;
    private static final Set anomaliesStatus = new HashSet(9);

    static {
            anomaliesStatus.add(ClaimStatus.CLAIM_REF_TO_ENG);
            anomaliesStatus.add(ClaimStatus.CLAIM_REFERRED_TO_FNOL);
            anomaliesStatus.add(ClaimStatus.CLAIM_REJECTION_CONTESTED);
            anomaliesStatus.add(ClaimStatus.CLAIM_PENDING);
            anomaliesStatus.add(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
            anomaliesStatus.add(ClaimStatus.CLAIM_REJECTED);
            anomaliesStatus.add(ClaimStatus.SUBSCRIBER_CLAIM_REJECTED);
            anomaliesStatus.add(ClaimStatus.CLAIM_UPDATE_BY_ENG);
            anomaliesStatus.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
    }

    @Override
    public int getActivityMonitorRequestInterval() {
        return activityMonitorRequestInterval;
    }

    public void setActivityMonitorRequestInterval(int activityMonitorRequestInterval) {
        this.activityMonitorRequestInterval = activityMonitorRequestInterval;
    }

    @Override
    public boolean isEnableActivityMonitor() {
        return enableActivityMonitor;
    }

    public void setEnableActivityMonitor(boolean enableActivityMonitor) {
        this.enableActivityMonitor = enableActivityMonitor;
    }

    public void setAuditTrailService(AuditTrailService auditTrailService) {
        this.auditTrailService = auditTrailService;
    }

    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }

    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
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
    public void checkRepairBookedInDateAnomaly(Claim claim) {
        try {
            LOG.debug("Adding hire monitoring detail anomalies - claim version={}, hmd version={}", claim.getVersion(), claim.getHireMonitoringDetail().getVersion());
            notificationService.checkForAnomalies(claim, NotificationType.RepairBookedInDateAnomalousNotification.getType());
            LOG.debug("Hire monitoring detail anomalies added - claim version={}, hmd version={}", claim.getVersion(), claim.getHireMonitoringDetail().getVersion());
        } catch (Exception ex) {
            LOG.error("Exception thrown adding notifications of type '{}' to claim={}: {}", new Object[]{
                        NotificationType.RepairBookedInDateAnomalousNotification.getType(), claim.getId(), ex.getMessage()});
        }
   }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public void checkTotalLossAnomaly(Claim claim) {
        try {
            LOG.debug("Adding total loss anomaly - claim version={}, hmd version={}", claim.getVersion(), claim.getHireMonitoringDetail().getVersion());
            notificationService.checkForAnomalies(claim, NotificationType.TotalLossAnomalousNotification.getType());
            LOG.debug("Hire total loss anomaly added - claim version={}, hmd version={}", claim.getVersion(), claim.getHireMonitoringDetail().getVersion());
        } catch (Exception ex) {
            LOG.error("Exception thrown adding notifications of type '{}' to claim={}: {}", new Object[]{
                        NotificationType.TotalLossAnomalousNotification.getType(), claim.getId(), ex.getMessage()});
        }
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
        LOG.debug("Evicting claim={} with version={}", claim.getId(), claim.getVersion());
        evict(claim);
        claim = (Claim) getSession().load(Claim.class, claim.getId());
        LOG.debug("Loaded new claim={} with version={}", claim.getId(), claim.getVersion());
        return claim;
    }
    

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
                    List<History> histories = claim.getHistories();
                    for(History history : histories) {
                        delete(history);
                    }
                    histories.clear();
                    LOG.debug("claim invoice and BRE history deleted");
                }
                /*
                 *  This fix is for BUG#1306 Reverting from 'PaymentReceived' should take into account the interim payment status
                 */
                if (claim.getStatus().equals(ClaimStatus.INVOICE_PAYMENT_LOGGED)) {
                    claim.getInvoice().setTotalToPay(claim.getInvoice().getFullTotalToPay());
                    
                    if (claim.getInvoice().isInterimPaymentReceivedFullAndFinal()) {
                        claim.getInvoice().setInterimPaymentReceivedFullAndFinal(false);
                    }
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

                /*
                 *  To-do item 7.2.2 - If the claim is moved out of either one of these 
                 *  closed states('ClaimClosed','InvoiceRejectionAccepted') then the 'Total To Pay' value
                 *  should revert back to the previous value.
                 */
                if (claim.getInvoice() != null
                        && (auditTrail.getNewStatus().equals(ClaimStatus.CLAIM_CLOSED)
                        || auditTrail.getNewStatus().equals(ClaimStatus.INVOICE_REJECTED_ACCEPTED))) {
                    claim.getInvoice().setTotalToPay(auditTrail.getPreviousTotalToPay());
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
                if (ClaimType.isSubscriber(claim.getClaimType()) && claim.getStatus().equals(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO)
                        && claim.getPreviousStatus().equals(ClaimStatus.SUBSCRIBER_CLAIM_REJECTED)) {
                    result = revertClaim(id);
                }
            }
        } else {
            LOG.warn("Could not revert claim status.");
        }

        return result;
    }

    @Override
    public int getECDCountByClaimId(int claimId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(HireMonitoringEcd.class);
        criteria.setProjection(Projections.rowCount());
        criteria.createCriteria("claim").add(Restrictions.eq("id", claimId));
        List result = findByCriteria(criteria);

        return ((Long) result.get(0)).intValue();
    }

    @Override
    public int getClaimCountByClaimNumber(String claimNumber, int claimId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
        criteria.setProjection(Projections.rowCount());
        criteria.add(Restrictions.eq("claimNumber", claimNumber));
        criteria.add(Restrictions.ne("id", claimId));

        List result = findByCriteria(criteria);
        
        return ((Long) result.get(0)).intValue();
    }

    @Override
    public List getCHOClaimsByCustomerClaimRef(String customerClaimRef, int choId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
        criteria.createCriteria("customer").add(Restrictions.like("claimReference", customerClaimRef).ignoreCase());
        criteria.add(Restrictions.eq("chorganisation.id", choId));
        criteria.addOrder(Order.asc("createdDate"));
        return findByCriteria(criteria);
    }

    @Override
    public List getInsurerClaimsByCustomerClaimRef(String customerClaimRef, int insId) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
        criteria.createCriteria("customer").add(Restrictions.like("claimReference", customerClaimRef).ignoreCase());
        criteria.add(Restrictions.eq("insurer.id", insId));
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
        return searchClaims(searchCriteria, 0, Integer.MAX_VALUE, "created", "desc");
    }

    @Override
    public SearchResult searchClaims(ClaimSearchCriteria searchCriteria, int start, int limit, String sort, String dir) {
        Criteria criteria = buildSearchCriteria(searchCriteria);
        Integer totalCount = totalCount(criteria);
        LOG.debug("Searching with criteria: {}", searchCriteria.toString());

        if (sort != null && !sort.isEmpty() && dir != null && !dir.isEmpty()) {
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
            } else if (sort.equalsIgnoreCase("noAttachments")) {
                addSort(criteria, "noAttachments", dir);
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
        if(searchCriteria == null){
            return 0;
        }

        Criteria criteria = buildSearchCriteria(searchCriteria);
        return totalCount(criteria);
    }

    @Override
    public Boolean isClaimSupplierReferenceNumberExist(String sClaimReferenceNumber) {
        if (sClaimReferenceNumber == null || sClaimReferenceNumber.isEmpty()) {
            return false;
        }
        
        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
        criteria.setProjection(Projections.rowCount());
        criteria.add(Restrictions.like("choReference", sClaimReferenceNumber.trim()).ignoreCase());
        List result = findByCriteria(criteria);

        Integer totalCount = ((Long) result.get(0)).intValue();

        return totalCount > 0;
    }

    @Override
    public Boolean isClaimSupplierReferenceNumberExistForCho(String sClaimReferenceNumber, int choId) {
        if (sClaimReferenceNumber == null || sClaimReferenceNumber.isEmpty()) {
            return false;
        }
        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
        criteria.setProjection(Projections.rowCount());
        criteria.add(Restrictions.like("choReference", sClaimReferenceNumber.trim()).ignoreCase());
        criteria.add(Restrictions.eq("chorganisation.id", choId));
        List result = findByCriteria(criteria);

        Integer totalCount = ((Long) result.get(0)).intValue();

        return totalCount > 0;
    }

    @Override
    public Boolean isObjectExist(int workgroupId) {
        boolean isExist = false;

        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);

        criteria.add(Restrictions.eq("workgroup.id", workgroupId));

        if (findByCriteria(criteria).size() > 0) {
            isExist = true;
        }

        return isExist;
    }

    @Override
    public boolean isOpenClaimByWorkgroupsByStatusExist(int insurerId, Set workgroupIds, String status) {
        boolean isExist = false;

        if (workgroupIds.size() > 0) {
            Iterator itr = workgroupIds.iterator();
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

    private boolean isOpenClaimByWorkgroupIdByStatusExist(int insurerId, Integer workgroupId, String status) {
        boolean isExist = false;

        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
        criteria.add(Restrictions.eq("insurer.id", insurerId));
        criteria.add(Restrictions.eq("workgroup.id", workgroupId));
        criteria.add(Restrictions.eq("status", status));

        if (findByCriteria(criteria).size() > 0) {
            isExist = true;
        }

        return isExist;
    }

    @Override
    public boolean isOpenClaimByWorkgroupExist(int workgroupId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
        criteria.add(Restrictions.eq("workgroup.id", workgroupId));

        for (String sStatus : ClaimStatus.getInsurerClosedStatus(true)) {
            criteria.add(Restrictions.ne("status", sStatus));
        }

        if (findByCriteria(criteria).size() > 0) {
            return true;
        }

        return false;

    }

    @Override
    public boolean isOpenClaimByWorkgroupsByUserExist(int insurerId, Set workgroupIds, int userId) {
        boolean isExist = false;

        if (workgroupIds.size() > 0) {
            Iterator itr = workgroupIds.iterator();
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
    public boolean isOpenClaimByWorkgroupIdByUserExist(int insurerId, int workgroupId, int UserId) {
        boolean isExist = false;
        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
        criteria.add(Restrictions.eq("workgroup.id", workgroupId));
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
                handlersActionStatus.addAll(ClaimStatus.getHandlerOutstandingStatusList());
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
            DetachedCriteria inSubclause = DetachedCriteria.forClass(Notification.class).add(Restrictions.in("type", NotificationType.getInsurerNotificationTypes())).add(Restrictions.eq("acknowledged", false)).add(Restrictions.eq("deleted", false)).setProjection(Projections.property("claim"));
            DetachedCriteria in = DetachedCriteria.forClass(Notification.class).add(Restrictions.in("type", NotificationType.getInsurerNotificationTypes())).add(Restrictions.eq("acknowledged", false)).setProjection(Property.forName("claim"));
            criteria.add(Subqueries.propertyIn("id", inSubclause));
            criteria.add(Restrictions.in("status", anomaliesStatus));
        }

        if (searchCriteria.isLiabilityStatusUpdated()) {
            DetachedCriteria noti = DetachedCriteria.forClass(Notification.class).add(Restrictions.in("type", NotificationType.getChoNotificationTypes())).add(Restrictions.eq("deleted", false)).setProjection(Projections.projectionList().add(Projections.property("claim")));
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
            criteria.add(Restrictions.ge("iv.penaltyBand", 0));
            criteria.add(Restrictions.sqlRestriction("(current_date - iv1_.auto_penalty_start::Date) >= (iv1_.penalty_band)"));
            criteria.add(Restrictions.disjunction()
                                    .add(Restrictions.eq("autoPenaltyChargeEnabled", Boolean.FALSE))
                                    .add(Restrictions.conjunction()
                                        .add(Restrictions.eq("autoPenaltyChargeEnabled", Boolean.TRUE))
                                        .add(Restrictions.eq("cho.autoPenaltyChargeEnabled", Boolean.FALSE))));

            if (!OrganisationType.CHO.equals(getCurrentUser().getOrganisationType())) {
                LOG.warn("Error in search criteria: only CHO can filter for penalty charges");
            } else {
                // Get the id's of the BRE Bands mapped to this CHO
                DetachedCriteria bCriteria = DetachedCriteria.forClass(BreBandOrganisation.class, "brebandorganisation")
                        .createAlias("brebandorganisation.chorganisation", "cho", CriteriaSpecification.LEFT_JOIN)
                        .add(Restrictions.eq("cho.id", getCurrentUser().getChorganisation().getId()));
                bCriteria.setProjection(Projections.property("brebandorganisation.breBand.id"));

                // Get the insurers from the BRE Band which don't allow penalty charges to be added
                DetachedCriteria pCriteria = DetachedCriteria.forClass(BreBand.class, "breband")
                        .add(Restrictions.disjunction()
                            .add(Restrictions.conjunction()
                                .add(Restrictions.eq("breband.allowGTAPenaltyCharges", Boolean.FALSE))
                                .add(Restrictions.in("this.claimType", Arrays.asList(ClaimType.GTA, ClaimType.GTA_ORIGINAL_INVOICE, ClaimType.GTA_SUPPLEMENTARY_INVOICE))))
                            .add(Restrictions.conjunction()
                                .add(Restrictions.eq("breband.allowSubscriberPenaltyCharges", Boolean.FALSE))
                                .add(Restrictions.in("this.claimType", Arrays.asList(ClaimType.SUBSCRIBER, ClaimType.SUBSCRIBER_ORIGINAL_INVOICE, ClaimType.SUBSCRIBER_SUPPLEMENTARY_INVOICE))))
                            .add(Restrictions.conjunction()
                                .add(Restrictions.eq("breband.allowFixedFeePenaltyCharges", Boolean.FALSE))
                                .add(Restrictions.in("this.claimType", Arrays.asList(ClaimType.FIXED_FEE, ClaimType.FIXED_FEE_ORIGINAL_INVOICE, ClaimType.FIXED_FEE_SUPPLEMENTARY_INVOICE))))
                            .add(Restrictions.conjunction()
                                .add(Restrictions.eq("breband.allowTPIPenaltyCharges", Boolean.FALSE))
                                .add(Restrictions.in("this.claimType", Arrays.asList(ClaimType.TPI))))
                            .add(Restrictions.conjunction()
                                .add(Restrictions.eq("breband.allowInsurervsInsurerPenaltyCharges", Boolean.FALSE))
                                .add(Restrictions.in("this.claimType", Arrays.asList(ClaimType.INSURER_VS_INSURER, ClaimType.INSURER_VS_INSURER_ORIGINAL_INVOICE, ClaimType.INSURER_VS_INSURER_SUPPLEMENTARY_INVOICE))))
                            .add(Restrictions.conjunction()
                                .add(Restrictions.eq("breband.allowManualInvoicePenaltyCharges", Boolean.FALSE))
                                .add(Restrictions.in("this.claimType", Arrays.asList(ClaimType.INSURER_INVOICE)))))
                        .add(Restrictions.in("breband.id", bCriteria.getExecutableCriteria(getSession()).list()))
                        .setProjection(Projections.property("breband.insurer"));

                // Make sure we retrieve no claims for insurers who don't allow penalty charges to be added
                criteria.add(Property.forName("this.insurer").notIn(pCriteria));
            }
        }
        
        if (searchCriteria.isEscalatedToSupervisor()) {
            criteria.add(Restrictions.ne("status", ClaimStatus.INVOICE_PAYMENT_LOGGED));
            
            if (getCurrentUser().getInsurer().getDaysBeforeEscalated() != null && getCurrentUser().getInsurer().getTimesInStatusContested() != null) {
                criteria.add(Restrictions.disjunction()
                    .add(Restrictions.sqlRestriction("(current_date - iv1_.created_date::Date) >= " + getCurrentUser().getInsurer().getDaysBeforeEscalated()))
                    .add(Restrictions.sqlRestriction("{alias}.id in (select temp.id from (select count(a.claim_id) as nr, a.claim_id as id from audit_trail a " +
                    "where a.claim_id = {alias}.id " +
                    "and a.new_status = 'ContestedInvoiceReferredToInsurer' " +
                    "and a.reverted = false " +
                    "group by a.claim_id ) as temp where nr >= " + getCurrentUser().getInsurer().getTimesInStatusContested() + ")" )));
            } else if (getCurrentUser().getInsurer().getDaysBeforeEscalated() != null) {
                criteria.add(Restrictions.sqlRestriction("(current_date - iv1_.created_date::Date) >= " + getCurrentUser().getInsurer().getDaysBeforeEscalated()));
            } else if (getCurrentUser().getInsurer().getTimesInStatusContested() != null) {
                criteria.add(Restrictions.sqlRestriction("{alias}.id in (select temp.id from (select count(a.claim_id) as nr, a.claim_id as id from audit_trail a " +
                    "where a.claim_id = {alias}.id " +
                    "and a.new_status = 'ContestedInvoiceReferredToInsurer' " +
                    "and a.reverted = false " +
                    "group by a.claim_id ) as temp where nr >= " + getCurrentUser().getInsurer().getTimesInStatusContested() + ")" ));
            } else {
                // Supervisor activated but no details given - therefore queue should be empty
                criteria.add(Restrictions.eq("status", "NoSuchStatus"));
            }
        }

        if (searchCriteria.getIsInterimPaymentMade()) {
            criteria.add(Restrictions.gtProperty("iv.interimPaymentMade", "iv.interimPaymentReceived"));
        }

        if (searchCriteria.getLiabilityStatuses() != null && !searchCriteria.getLiabilityStatuses().isEmpty()) {
            criteria.add(Restrictions.in("liabilityStatus", searchCriteria.getLiabilityStatuses().toArray()));
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
            } 
            if (searchCriteria.getClaimTypes().contains(ClaimType.INSURER_UPLOAD)) {
                ClaimTypes.addAll(Arrays.asList(ClaimType.INSURER_INVOICE, ClaimType.INSURER_CLAIM, ClaimType.INSURER_ORIGINAL_INVOICE, ClaimType.INSURER_SUPPLEMENTARY_INVOICE));
            } 
            if (searchCriteria.getClaimTypes().contains(ClaimType.INSURER_VS_INSURER)) {
                ClaimTypes.addAll(Arrays.asList(ClaimType.INSURER_VS_INSURER,ClaimType.INSURER_VS_INSURER_ORIGINAL_INVOICE,ClaimType.INSURER_VS_INSURER_SUPPLEMENTARY_INVOICE));
            } 
            if (searchCriteria.getClaimTypes().contains(ClaimType.SUBSCRIBER)) {
                ClaimTypes.addAll(Arrays.asList(ClaimType.SUBSCRIBER,ClaimType.SUBSCRIBER_ORIGINAL_INVOICE,ClaimType.SUBSCRIBER_SUPPLEMENTARY_INVOICE));
            } 
            if (searchCriteria.getClaimTypes().contains(ClaimType.FIXED_FEE)) {
                ClaimTypes.addAll(Arrays.asList(ClaimType.FIXED_FEE,ClaimType.FIXED_FEE_ORIGINAL_INVOICE,ClaimType.FIXED_FEE_SUPPLEMENTARY_INVOICE));
            } 
            if (searchCriteria.getClaimTypes().contains(ClaimType.TPI)) {
                ClaimTypes.addAll(Arrays.asList(ClaimType.TPI));
            }
            criteria.add(Restrictions.in("claimType", ClaimTypes.toArray()));
        }

        if (searchCriteria.isIsSupplementaryInvoiceOnly()) {
            criteria.add(Restrictions.in("claimType", ClaimType.getAllSupplementaryInvoiceTypes()));
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

        if (searchCriteria.getRentalStartDate() != null || searchCriteria.getRentalEndDate() != null) {
            if (searchCriteria.getRentalStartDate() != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(searchCriteria.getRentalStartDate());
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                criteria.add(Restrictions.ge("vh.rentalStart", cal.getTime()));
            }
            
            if (searchCriteria.getRentalEndDate() != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(searchCriteria.getRentalEndDate());
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                criteria.add(Restrictions.le("vh.rentalStart", cal.getTime()));
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
        
        /*
         * Final Review
         */
        // First, check if both flags are non-null (NB. must be CHOX Admin and both flags must be equal!
        if (searchCriteria.isFinalReviewCho() != null && searchCriteria.isFinalReviewIns() != null) {
            if (searchCriteria.isFinalReviewIns()) {
                criteria.add(Restrictions.disjunction().add(Restrictions.eq("finalReviewCho", searchCriteria.isFinalReviewCho()))
                    .add(Restrictions.eq("finalReviewIns", searchCriteria.isFinalReviewIns())));
            }
            else {
                criteria.add(Restrictions.conjunction().add(Restrictions.eq("finalReviewCho", searchCriteria.isFinalReviewCho()))
                    .add(Restrictions.eq("finalReviewIns", searchCriteria.isFinalReviewIns())));
            }
        } else if (searchCriteria.isFinalReviewCho() != null) {
            criteria.add(Restrictions.eq("finalReviewCho", searchCriteria.isFinalReviewCho().booleanValue()));
        } else if (searchCriteria.isFinalReviewIns() != null) {
            criteria.add(Restrictions.eq("finalReviewIns", searchCriteria.isFinalReviewIns().booleanValue()));
        }
        return criteria;
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

        if (claimAge > (DateHelper.SUBSCRIBER_SLA_DAYS + claim.getSlaExtDays()) || (claimAge == (DateHelper.SUBSCRIBER_SLA_DAYS + claim.getSlaExtDays()) && !DateHelper.isBefore3pm())) {
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
                Comment comment = Comment.newComment(0, claim.getInsurer().getName() + " failed to respond to the Subscriber notification within the 5 day SLA, claim taken down Subscriber route.");
                if (claim.getSlaExtDays() > 0) {
                    comment = Comment.newComment(0, claim.getInsurer().getName() + " failed to respond to the Subscriber notification within the 5 day SLA + "+claim.getSlaExtDays()+" day extension, claim taken down Subscriber route.");
                }
                comment.setRaisedBy(userService.getWebUser(999));
                claim.addComment(comment);
                save(claim);
                LOG.debug("Comment added and claim saved.");
            }
        }

        LOG.debug("Returning claim age of {}", claimAge);
        return claimAge;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int getFixedFeeClaimDays(int id) {
        int claimAge = -1;
        LOG.debug("Getting days of fixed-fee claim with id={}", id);
        Claim claim = (Claim) get(Claim.class, id);

        if (claim != null && ClaimType.isFixedFee(claim.getClaimType())) {
            claimAge = auditTrailService.getFixedFeeClaimDays(id);
        }

        if (claimAge > (DateHelper.FIXED_FEE_SLA_DAYS + claim.getSlaExtDays()) || (claimAge == (DateHelper.FIXED_FEE_SLA_DAYS + claim.getSlaExtDays()) && !DateHelper.isBefore3pm())) {
            boolean addComment = true;
            List<Comment> comments = commentService.getCommentByClaimId(claim.getId());
            for (Comment comment : comments) {
                if (comment.getComment().endsWith("claim taken down Fixed Fee route.")) {
                    addComment = false;
                    LOG.debug("Comment already added - skipping");
                    break;
                }
            }
            if (addComment) {
                Comment comment = Comment.newComment(0, claim.getInsurer().getName() + " failed to respond to the Fixed Fee notification within the 14 day SLA, claim taken down Fixed Fee route.");
                if (claim.getSlaExtDays() > 0) {
                    comment = Comment.newComment(0, claim.getInsurer().getName() + " failed to respond to the Fixed Fee notification within the 14 day SLA + "+claim.getSlaExtDays()+" day extension, claim taken down Fixed Fee route.");
                }
                comment.setRaisedBy(userService.getWebUser(999));
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
    public int getFixedFeeClaimRejectedDays(int claimId) {
        int claimAge = -1;
        LOG.debug("Getting days until fixed fee claim rejected with id={}", claimId);
        Claim claim = (Claim) get(Claim.class, claimId);

        if (claim != null && ClaimType.isFixedFee(claim.getClaimType())) {
            claimAge = auditTrailService.getFixedFeeClaimRejectedDays(claimId);
        }

        LOG.debug("Days until fixed fee claim ({}) rejected: {}", claimId, claimAge);

        return claimAge;
    }

    
    @Override
    public int getSubscriberClaimRejects(int claimId) {
        
        LOG.debug("Getting number of times subscriber claim (with id={}) rejected", claimId);
        int subscriberClaimRejects = auditTrailService.getSubscriberClaimRejectedTimes(claimId);
        LOG.debug("Number of times subscriber claim ({}) rejected: {}", claimId, subscriberClaimRejects);

        return subscriberClaimRejects;
    }

    @Override
    public int getClaimRejects(int claimId) {
        
        LOG.debug("Getting number of times claim (with id={}) rejected", claimId);
        int claimRejects = auditTrailService.getClaimRejectedTimes(claimId);
        LOG.debug("Number of times claim ({}) rejected: {}", claimId, claimRejects);

        return claimRejects;
    }

    
    private int updateChoReferenceNumber(String oldReference, String newReference, Integer choId) {
        Claim claim = getClaimByChoIdAndCHOReferenceNumber(choId, oldReference);
        if (claim != null) {
            Claim newClaim = getClaimByChoIdAndCHOReferenceNumber(choId, newReference);
            if (newClaim == null) {
                try {
                    claim.setChoReference(newReference);
                    claim.addComment(Comment.newComment(0, "Supplier Reference updated from '" + oldReference + "' to '" + newReference + "'."));
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

        LiabilityStatus liabilityStatus = claim.getLiabilityStatus();
        Invoice invoice = claim.getInvoice();
        ClaimType claimType = claim.getClaimType();
        if (invoice != null) { 
            // When excluding some 'Claim Type' Please exclude it from applyAutoPenaltyCharge Stored Procedure as well.
            if (!ClaimType.isInsurerVsInsurer(claimType) 
                    && !ClaimType.isSubscriber(claimType)
                    && !ClaimType.isFixedFee(claimType) 
                    && liabilityStatus != null 
                    && (liabilityStatus.equals(LiabilityStatus.LIABILITY_SPLIT)
                            || (liabilityStatus.equals(LiabilityStatus.PROCEED_WITHOUT_PREJUDICE)))) {
                BigDecimal ttp = invoice.getFullTotalToPay();
                BigDecimal insper = claim.getPercentageLiabilityAccepted();
                invoice.setTotalToPay(ttp.multiply(insper).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
                LOG.debug("liability updated " + invoice.getTotalToPay());
            } else if (!ClaimType.isInsurerVsInsurer(claimType) 
                    && !ClaimType.isSubscriber(claimType)
                    && !ClaimType.isFixedFee(claimType) 
                    && liabilityStatus != null
                    && liabilityStatus.equals(LiabilityStatus.LIABILITY_REPUDIATED)) {
                invoice.setTotalToPay(BigDecimal.ZERO);
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
        Claim claim = this.getClaim(claimId);

        if(isClaimInClosedStatus(claim)){
            return 0;
        }
        
        Criteria criteria = getSession().createCriteria(AuditTrail.class);
        criteria.add(Restrictions.eq("newStatus", ClaimStatus.CONTESTED_INVOICE_REF_TO_INS));
        criteria.add(Restrictions.eq("reverted", false));
        criteria.add(Restrictions.eq("claim.id", claimId));
        return totalCount(criteria).intValue();
    }
    
    private boolean isClaimInClosedStatus(Claim claim) {
        for (String status : ClaimStatus.getCompletedStatus(true)) {
            if (claim.getStatus().equalsIgnoreCase(status)) {
                return true;
            }
        }
        DetachedCriteria auditTrail = DetachedCriteria.forClass(AuditTrail.class, "aut");
        auditTrail.add(Restrictions.eq("aut.newStatus",ClaimStatus.INVOICE_PAYMENT_LOGGED));
        auditTrail.add(Restrictions.eq("aut.reverted", false));
        auditTrail.add(Restrictions.eq("aut.claim.id", claim.getId()));
        List result = getHibernateTemplate().findByCriteria(auditTrail);
        // in case the claim was in status 'invoice payment logged' we return 0
        // and don't display it in information panel
        if (result.size() > 0) {
            return true;
        }
        return false;
    }
    
    @Override
    public int getNoOfRejectedClaims(Integer reasonOfRejectionId) {
        Criteria criteria = getSession().createCriteria(Claim.class);
        criteria.add(Restrictions.eq("reasonOfRejection.id", reasonOfRejectionId));
        return totalCount(criteria).intValue();
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
            if (claim.getCustomer().getClaimReference() != null && claim.getCustomer().getClaimReference().length() > 0) {
                        criteria.add(Restrictions.ne("cust.claimReference", claim.getCustomer().getClaimReference()));
            }
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

}
