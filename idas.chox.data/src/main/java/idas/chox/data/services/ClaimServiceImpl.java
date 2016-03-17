package idas.chox.data.services;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
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
import idas.chox.core.enums.FinalReviewMapping;
import idas.chox.core.model.Attachment;
import idas.chox.core.model.AuditTrail;
import idas.chox.core.model.BreBand;
import idas.chox.core.model.BreBandOrganisation;
import idas.chox.core.model.BrePenaltyBand;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimAuditReview;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Comment;
import idas.chox.core.model.HireMonitoringDetail;
import idas.chox.core.model.HireMonitoringEcd;
import idas.chox.core.model.History;
import idas.chox.core.model.Invoice;
import idas.chox.core.model.LiabilityStatus;
import idas.chox.core.model.Notification;
import idas.chox.core.model.QueuedTicket;
import idas.chox.core.model.Task;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.search.ClaimSearchCriteria;
import idas.chox.core.search.SearchResult;
import idas.chox.core.services.AuditTrailService;
import idas.chox.core.services.BreBandService;
import idas.chox.core.services.BrePenaltyBandService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.CommentService;
import idas.chox.core.services.InsurerDiscountService;
import idas.chox.core.services.NotificationService;
import idas.chox.core.services.TaskService;
import idas.chox.core.services.UserService;
import idas.chox.core.util.DateHelper;
import idas.chox.core.util.RoleHelper;
import idas.chox.data.events.ChoxEvent;
import idas.chox.data.notifications.LiabilityStatusUpdatedNotification;
import idas.chox.data.notifications.NotificationType;

public class ClaimServiceImpl extends SecureDataService implements ClaimService, Serializable {

    public static final String PENDING = "Pending";
    public static final String IN_PROGRESS = "InProgress";
    public static final String COMPLETE = "Complete";
    public static final String CANCELLED = "Cancelled";
    public static final String NEW_CLAIM = "1st Notification";

    private static final Logger LOG = LoggerFactory.getLogger(ClaimServiceImpl.class);
    private AuditTrailService auditTrailService;
    private CommentService commentService;
    private TaskService taskService;
    private UserService userService;
    private NotificationService notificationService;
    private boolean enableActivityMonitor;
    private int activityMonitorRequestInterval;
    private EventService eventService;
    private BreBandService breBandService;
    private BrePenaltyBandService brePenaltyBandService;
    private InsurerDiscountService insurerDiscountService;

    @Override
    public int getActivityMonitorRequestInterval() {
        return activityMonitorRequestInterval;
    }

    public void setActivityMonitorRequestInterval(int activityMonitorRequestInterval) {
        this.activityMonitorRequestInterval = activityMonitorRequestInterval;
    }

    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    public void setBreBandService(BreBandService breBandService) {
        this.breBandService = breBandService;
    }

    public void setBrePenaltyBandService(BrePenaltyBandService brePenaltyBandService) {
        this.brePenaltyBandService = brePenaltyBandService;
    }

    public void setInsurerDiscountService(InsurerDiscountService insurerDiscountService) {
        this.insurerDiscountService = insurerDiscountService;
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

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    public ClaimServiceImpl() {
        super();
    }

    @Override
    public Claim getClaim(int id) {
        return (Claim) get(Claim.class, id);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value = "transactionManager")
    @Override
    public void updateClaim(Claim claim) {
        save(claim);
        LOG.debug("Claim updated and saved.");
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value = "transactionManager")
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

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value = "transactionManager")
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

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value = "transactionManager")
    protected void save(Claim object) {
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
        claim = (Claim) getSessionFactory().getCurrentSession().load(Claim.class, claim.getId());
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
                
                if ((claim.getStatus().equals(ClaimStatus.INVOICE_PAYMENT_RECEIVED)
                        || claim.getStatus().equals(ClaimStatus.MANUAL_INVOICE_PAID)) && claim.getClaimAuditReview() != null) {
                    LOG.debug("This claim has auditReview and will be deleted as reverting the status");
                    ClaimAuditReview oldClaimAuditReview = claim.getClaimAuditReview();
                    claim.setClaimAuditReview(null);
                    delete(oldClaimAuditReview);
                    LOG.debug("auditReview deleted!!!");
                }
                
                claim.setStatus(auditTrail.getOriginalStatus());
                
                if (claim.getStatus().equals(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA) && claim.getInvoice() != null) {
                    LOG.debug("This claim has invoice and will be deleted as reverting the status");
                    Invoice oldInvoice = claim.getInvoice();
                    claim.setInvoice(null);
                    LOG.debug("claim invoice set to null");
                    delete(oldInvoice);
                    List<History> histories = claim.getHistories();
                    for (History history : histories) {
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

    private Claim getClaimByVehicleHireId(Integer vehicleHireId) {
        Claim claim;
        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
        criteria.add(Restrictions.eq("vehicleHire.id", vehicleHireId));
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

        Integer start = searchCriteria.getStart();
        Integer limit = searchCriteria.getLimit();
        String sort = searchCriteria.getSort();
        String dir = searchCriteria.getDir();

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
            } else if (sort.equalsIgnoreCase("remainingSlaDays")) {
                addSort(criteria, "remainingSlaDaysInt", dir);
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

        List<Claim> claims = new ArrayList<>();

        for (HashMap m : resultMap) {
            claims.add((Claim) m.get("this"));
        }

        LOG.debug("Returning search result - {} claims found (totalCount={})", claims.size(), totalCount);
        return new SearchResult(claims, totalCount, null);
    }

    @Override
    public Integer countClaims(ClaimSearchCriteria searchCriteria) {
        if (searchCriteria == null) {
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
        criteria.add(Restrictions.eq("choReference", sClaimReferenceNumber.trim()).ignoreCase());
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
        criteria.add(Restrictions.eq("choReference", sClaimReferenceNumber.trim()).ignoreCase());
        criteria.add(Restrictions.eq("chorganisation.id", choId));
        List result = findByCriteria(criteria);

        Integer totalCount = ((Long) result.get(0)).intValue();

        return totalCount > 0;
    }

    @Override
    public Boolean isClaimSupplierReferenceNumberExistForChoExternal(String sClaimReferenceNumber, int choId) {
        if (sClaimReferenceNumber == null || sClaimReferenceNumber.isEmpty()) {
            return false;
        }
        Map extParameters = new HashMap();
        extParameters.put("pChoRef", sClaimReferenceNumber);
        extParameters.put("pChoId", choId);
        int totalCount = externalQueryCount("select id from claim where cho_reference = :pChoRef and chorganisation_id = :pChoId", extParameters);
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

        return findByCriteria(criteria).size() > 0;

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
    public boolean isUserHasOpenClaim(int userId, boolean isInsurer) {
        boolean isExist = false;

        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);

        if (isInsurer) {
            criteria.add(Restrictions.eq("claimOwner.id", userId));

            for (String sStatus : ClaimStatus.getInsurerClosedStatus(true)) {
                criteria.add(Restrictions.ne("status", sStatus));
            }

        }
        else {
            criteria.add(Restrictions.eq("supplierClaimOwner.id", userId));

            for (String sStatus : ClaimStatus.getCompletedStatus(false)) {
                criteria.add(Restrictions.ne("status", sStatus));
            }
        }
       
        if (findByCriteria(criteria).size() > 0) {
            isExist = true;
        }

        return isExist;

    }

    private Criteria buildSearchCriteria(ClaimSearchCriteria searchCriteria) {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Claim.class)
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
                .createAlias("this.claimAuditReview", "ar", CriteriaSpecification.LEFT_JOIN)
                .createAlias("this.insurer", "ins", CriteriaSpecification.LEFT_JOIN);

        // For filter's workgroup check we need to set the restriction param to the searchCriteria, so that this restriction will be populated to the search panel when queue is clicked.
        if (searchCriteria.isWorkgroupCheck() && !searchCriteria.isManual()) {
            if (RoleHelper.isWorkgroupValidationEnabledUser(getCurrentUser())) {
                searchCriteria.setWorkgroupIds(getCurrentUser().getWorkgroupIds());
            }
        }

        // For filter's workgroup check we need to set the restriction param to the searchCriteria, so that this restriction will be populated to the search panel when queue is clicked.
        if (searchCriteria.isWorkgroupCheck() && searchCriteria.isManual()) {
            if (RoleHelper.isManualWorkgroupValidationEnabledUser(getCurrentUser())) {
                searchCriteria.setWorkgroupIds(getCurrentUser().getWorkgroupIds());
            }
        }

        // For filter's ownership check we need to set the restriction param to the searchCriteria, so that this restriction will be populated to the search panel when queue is clicked.
        if (searchCriteria.isOwnerShipCheck() && getCurrentUser().isAnInsurer() & !searchCriteria.isManual()) {
            if (RoleHelper.isOwnershipValidationEnabledUser(getCurrentUser())) {
                searchCriteria.setClaimOwnerIds(new HashSet<>(Arrays.asList(getCurrentUser().getId())));
            }
        }

        // For filter's ownership check we need to set the restriction param to the searchCriteria, so that this restriction will be populated to the search panel when queue is clicked.
        if (searchCriteria.isOwnerShipCheck() && getCurrentUser().isAnInsurer() & searchCriteria.isManual()) {
            if (RoleHelper.isManualOwnershipValidationEnabledUser(getCurrentUser())) {
                searchCriteria.setClaimOwnerIds(new HashSet<>(Arrays.asList(getCurrentUser().getId())));
            }
        }

        // For filter's supplier Owner check, we need to set the restriction param to the searchCriteria, so that this restriction will be populated to the search panel when queue is clicked.
        if (searchCriteria.isSupplierOwnerShipCheck() && getCurrentUser().isCHO()) {
            if (RoleHelper.isOwnershipValidationEnabledUser(getCurrentUser())) {
                searchCriteria.setSupplierClaimOwnerIds(new HashSet<>(Arrays.asList(getCurrentUser().getId(), ClaimSearchCriteria.CLAIM_OWNER_NOT_ASSIGNED)));
            }
        }

        /*
         * Payments Team Filter
         */
        if (searchCriteria.getApprovedInvoiceOwnershipSearchParamIds() != null && !searchCriteria.getApprovedInvoiceOwnershipSearchParamIds().isEmpty()) {
            Criterion claimsHandlerOnlyClaims = Restrictions.eq("id", -1);
            Criterion paymentTeamOnlyClaims = Restrictions.eq("id", -1);

            for (Integer restrictionId : searchCriteria.getApprovedInvoiceOwnershipSearchParamIds()) {
                // NB. For CHOX Admin, we also need to check the insurer config flag...
                if (restrictionId == 1) {
                    if (RoleHelper.isChoxAdmin(getCurrentUser())) {
                        claimsHandlerOnlyClaims = Restrictions.eq("iv.paymentTeam", Boolean.FALSE);
                    } else {
                        claimsHandlerOnlyClaims = Restrictions.eq("iv.paymentTeam", Boolean.FALSE);
                    }
                } else if (restrictionId == 2) {
                    if (RoleHelper.isChoxAdmin(getCurrentUser())) {
                        paymentTeamOnlyClaims = Restrictions.conjunction().add(Restrictions.eq("iv.paymentTeam", Boolean.TRUE))
                                .add(Restrictions.disjunction()
                                        .add(Restrictions.conjunction().add(Restrictions.eq("ins.gtaPaymentsTeamEnable", Boolean.TRUE))
                                                .add(Restrictions.in("this.claimType", Arrays.asList(ClaimType.GTA, ClaimType.GTA_ORIGINAL_INVOICE, ClaimType.GTA_SUPPLEMENTARY_INVOICE))))
                                        .add(Restrictions.conjunction().add(Restrictions.eq("ins.subscriberPaymentsTeamEnable", Boolean.TRUE))
                                                .add(Restrictions.in("this.claimType", Arrays.asList(ClaimType.SUBSCRIBER, ClaimType.SUBSCRIBER_ORIGINAL_INVOICE, ClaimType.SUBSCRIBER_SUPPLEMENTARY_INVOICE))))
                                        .add(Restrictions.conjunction().add(Restrictions.eq("ins.fixedFeePaymentsTeamEnable", Boolean.TRUE))
                                                .add(Restrictions.in("this.claimType", Arrays.asList(ClaimType.FIXED_FEE, ClaimType.FIXED_FEE_ORIGINAL_INVOICE, ClaimType.FIXED_FEE_SUPPLEMENTARY_INVOICE))))
                                        .add(Restrictions.conjunction().add(Restrictions.eq("ins.insurerVsInsurerPaymentsTeamEnable", Boolean.TRUE))
                                                .add(Restrictions.in("this.claimType", Arrays.asList(ClaimType.INSURER_VS_INSURER, ClaimType.INSURER_VS_INSURER_ORIGINAL_INVOICE, ClaimType.INSURER_VS_INSURER_SUPPLEMENTARY_INVOICE))))
                                        .add(Restrictions.conjunction().add(Restrictions.eq("ins.collaborationPaymentsTeamEnable", Boolean.TRUE))
                                                .add(Restrictions.in("this.claimType", Arrays.asList(ClaimType.COLLABORATION_PROTOCOL, ClaimType.COLLABORATION_PROTOCOL_ORIGINAL_INVOICE, ClaimType.COLLABORATION_PROTOCOL_SUPPLEMENTARY_INVOICE))))
                                        .add(Restrictions.conjunction().add(Restrictions.eq("ins.insurerManualPaymentsTeamEnable", Boolean.TRUE))
                                                .add(Restrictions.in("this.claimType", Arrays.asList(ClaimType.INSURER_CLAIM, ClaimType.INSURER_INVOICE, ClaimType.INSURER_ORIGINAL_INVOICE, ClaimType.INSURER_SUPPLEMENTARY_INVOICE, ClaimType.INSURER_UPLOAD))))
                                        .add(Restrictions.conjunction().add(Restrictions.eq("ins.tpiPaymentsTeamEnable", Boolean.TRUE))
                                                .add(Restrictions.eq("this.claimType", ClaimType.TPI))));
                    } else {
                        paymentTeamOnlyClaims = Restrictions.eq("iv.paymentTeam", Boolean.TRUE);
                    }
                }
            }
            criteria.add(Restrictions.disjunction()
                    .add(claimsHandlerOnlyClaims)
                    .add(paymentTeamOnlyClaims));
        }

        /*
         * Payment Dispute Filter
         */
        // NB. For CHOX Admin, we also need to check the insurer config flag...
        if (searchCriteria.getPaymentDisputeValue() > 0) {
            Criterion paymentDisputeRestriction;
            if (searchCriteria.getPaymentDisputeValue() == 2) {
                if (RoleHelper.isChoxAdmin(getCurrentUser())) {
                    paymentDisputeRestriction = Restrictions.disjunction().add(Restrictions.eq("paymentDispute", Boolean.FALSE)).add(Restrictions.eq("ins.paymentDisputesEnable", Boolean.FALSE));
                } else {
                    paymentDisputeRestriction = Restrictions.eq("paymentDispute", Boolean.FALSE);
                }
            } else {
                if (RoleHelper.isChoxAdmin(getCurrentUser())) {
                    paymentDisputeRestriction = Restrictions.conjunction().add(Restrictions.eq("paymentDispute", Boolean.TRUE)).add(Restrictions.eq("ins.paymentDisputesEnable", Boolean.TRUE));
                } else {
                    paymentDisputeRestriction = Restrictions.eq("paymentDispute", Boolean.TRUE);
                }
            }
            criteria.add(paymentDisputeRestriction);
        }
                
        if (searchCriteria.getClaimAuditValue() > 0) {
            if (searchCriteria.getClaimAuditValue() == 1) {
                criteria.add(Restrictions.conjunction()
                        .add(Restrictions.isNotNull("claimAuditReview"))
                        .add(Restrictions.eq("ar.claimAuditReviewCompleted", Boolean.FALSE)));
            } else if (searchCriteria.getClaimAuditValue() == 2) {
                criteria.add(Restrictions.conjunction()
                        .add(Restrictions.isNotNull("claimAuditReview"))
                        .add(Restrictions.eq("ar.claimAuditReviewCompleted", Boolean.TRUE)));
            }
        }

        if (searchCriteria.getHireAndRepairSearchParamIds() != null && !searchCriteria.getHireAndRepairSearchParamIds().isEmpty()) {

            /* The SQL Query for the below criteria is:
             OR ((iv.id is null AND ((hmd.id is null and c.managing_repair = false) OR (hmd.id is not null and hmd.is_repair_only_check = false and c.managing_repair = false))) OR (iv.id is not null AND (iv.hire_net > 0 and iv.repair_net = 0)))
             OR ((iv.id is null AND hmd.id is not null and hmd.is_repair_only_check = true) OR (iv.id is not null AND (iv.repair_net > 0 and iv.hire_net <= 37)))
             OR ((iv.id is null AND ((hmd.id is null and c.managing_repair = true) OR (hmd.id is not null and hmd.is_repair_only_check = false and c.managing_repair = true))) OR (iv.id is not null AND (iv.hire_net > 37 and iv.repair_net > 0)))
             OR (iv.id is not null AND iv.hire_net = 0 AND iv.repair_net = 0)
             */
            Criterion hireOnlyClaims = Restrictions.eq("id", -1);
            Criterion repairOnlyClaims = Restrictions.eq("id", -1);
            Criterion hireAndRepairOnlyClaims = Restrictions.eq("id", -1);
            Criterion noHireAndNoRepair = Restrictions.eq("id", -1);

            for (Integer restrictionId : searchCriteria.getHireAndRepairSearchParamIds()) {
                if (restrictionId == 1) {

                    hireOnlyClaims = Restrictions.disjunction()
                            .add(Restrictions.conjunction()
                                    .add(Restrictions.isNull("iv.id"))
                                    .add(Restrictions.disjunction()
                                            .add(Restrictions.conjunction()
                                                    .add(Restrictions.isNull("hmd.id"))
                                                    .add(Restrictions.eq("this.managingRepair", false)))
                                            .add(Restrictions.conjunction()
                                                    .add(Restrictions.isNotNull("hmd.id"))
                                                    .add(Restrictions.eq("hmd.isRepairOnlyCheck", false))
                                                    .add(Restrictions.eq("this.managingRepair", false)))))
                            .add(Restrictions.conjunction()
                                    .add(Restrictions.isNotNull("iv.id"))
                                    .add(Restrictions.conjunction()
                                            .add(Restrictions.gt("iv.hireNet", BigDecimal.ZERO))
                                            .add(Restrictions.eq("iv.repairNet", BigDecimal.ZERO))));

                } else if (restrictionId == 2) {

                    repairOnlyClaims = Restrictions.disjunction()
                            .add(Restrictions.conjunction()
                                    .add(Restrictions.isNull("iv.id"))
                                    .add(Restrictions.isNotNull("hmd.id"))
                                    .add(Restrictions.eq("hmd.isRepairOnlyCheck", true)))
                            .add(Restrictions.conjunction()
                                    .add(Restrictions.isNotNull("iv.id"))
                                    .add(Restrictions.conjunction()
                                            .add(Restrictions.le("iv.hireNet", new BigDecimal(37)))
                                            .add(Restrictions.gt("iv.repairNet", BigDecimal.ZERO))));

                } else if (restrictionId == 3) {

                    hireAndRepairOnlyClaims = Restrictions.disjunction()
                            .add(Restrictions.conjunction()
                                    .add(Restrictions.isNull("iv.id"))
                                    .add(Restrictions.disjunction()
                                            .add(Restrictions.conjunction()
                                                    .add(Restrictions.isNull("hmd.id"))
                                                    .add(Restrictions.eq("this.managingRepair", true)))
                                            .add(Restrictions.conjunction()
                                                    .add(Restrictions.isNotNull("hmd.id"))
                                                    .add(Restrictions.eq("hmd.isRepairOnlyCheck", false))
                                                    .add(Restrictions.eq("this.managingRepair", true)))))
                            .add(Restrictions.conjunction()
                                    .add(Restrictions.isNotNull("iv.id"))
                                    .add(Restrictions.conjunction()
                                            .add(Restrictions.gt("iv.hireNet", new BigDecimal(37)))
                                            .add(Restrictions.gt("iv.repairNet", BigDecimal.ZERO))));

                } else if (restrictionId == 4) {

                    noHireAndNoRepair = Restrictions.conjunction()
                            .add(Restrictions.isNotNull("iv.id"))
                            .add(Restrictions.eq("iv.hireNet", BigDecimal.ZERO))
                            .add(Restrictions.eq("iv.repairNet", BigDecimal.ZERO));

                }
            }
            criteria.add(Restrictions.disjunction()
                    .add(hireOnlyClaims)
                    .add(repairOnlyClaims)
                    .add(hireAndRepairOnlyClaims)
                    .add(noHireAndNoRepair));
        }

        if (searchCriteria.getClaimOwnerIds() != null && !searchCriteria.getClaimOwnerIds().isEmpty()) {
            criteria.add(Restrictions.in("claimOwner.id", searchCriteria.getClaimOwnerIds().toArray()));
        }

        if (searchCriteria.getSupplierClaimOwnerIds() != null && !searchCriteria.getSupplierClaimOwnerIds().isEmpty()) {
            ArrayList<Integer> supplierClaimOwnerIds = new ArrayList<>();

            if (searchCriteria.getSupplierClaimOwnerIds().contains(ClaimSearchCriteria.CLAIM_OWNER_NOT_ASSIGNED)) {
                // Remove -9 value from selected supplierClaimOwnerIds as we are adding null restriction.
//                searchCriteria.getSupplierClaimOwnerIds().remove(ClaimSearchCriteria.CLAIM_OWNER_NOT_ASSIGNED);
                supplierClaimOwnerIds.addAll(searchCriteria.getSupplierClaimOwnerIds());
                supplierClaimOwnerIds.remove((Integer) ClaimSearchCriteria.CLAIM_OWNER_NOT_ASSIGNED);
                // If multiple SupplierClaimOwner selected with CLAIM_OWNER_NOT_ASSIGNED then use criteria OR condition.
                if (supplierClaimOwnerIds.size() > 0) {

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
                ArrayList<String> handlersActionStatus = new ArrayList<>();
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

        if (searchCriteria.isAnomalies()) {
            DetachedCriteria inSubclause = DetachedCriteria.forClass(Notification.class).add(Restrictions.in("type", NotificationType.getInsurerNotificationTypes())).add(Restrictions.eq("acknowledged", false)).add(Restrictions.eq("deleted", false)).setProjection(Projections.property("claim"));
            criteria.add(Subqueries.propertyIn("id", inSubclause));
        }

        if (searchCriteria.isLiabilityStatusUpdated()) {
            DetachedCriteria noti = DetachedCriteria.forClass(Notification.class).add(Restrictions.in("type", NotificationType.getChoNotificationTypes())).add(Restrictions.eq("deleted", false)).setProjection(Projections.projectionList().add(Projections.property("claim")));
            criteria.add(Subqueries.propertyIn("id", noti));
        }

        if (searchCriteria.getCaseWithClientsSolicitor() != null) {
            if (searchCriteria.getCaseWithClientsSolicitor()) {
                criteria.add(Restrictions.eq("caseWithClientsSolicitor", Boolean.TRUE));
            } else {
                criteria.add(Restrictions.eq("caseWithClientsSolicitor", Boolean.FALSE));
            }
        }

        if (searchCriteria.isPenaltyChargesAppliedOnly()) {
            criteria.add(Restrictions.gt("iv.totalPenaltyCharge", BigDecimal.ZERO));
        }

        if (searchCriteria.isPenaltyChargeApplied()) {
            criteria.add(Restrictions.ge("iv.penaltyBand", 0));
            criteria.add(Restrictions.sqlRestriction("(current_date - iv1_.auto_penalty_start::Date) >= (iv1_.penalty_band)"));
            criteria.add(Restrictions.disjunction()
                    .add(Restrictions.eq("autoPenaltyChargeEnabled", Boolean.FALSE))
                    .add(Restrictions.conjunction()
                            .add(Restrictions.eq("autoPenaltyChargeEnabled", Boolean.TRUE))
                            .add(Restrictions.eq("cho.autoPenaltyChargeEnabled", Boolean.FALSE))));

            if (OrganisationType.INS.equals(getCurrentUser().getOrganisationType())) {
                // Restrict to Manual claims
                criteria.add(Restrictions.in("this.claimType", Arrays.asList(ClaimType.INSURER_CLAIM, ClaimType.INSURER_INVOICE, ClaimType.INSURER_UPLOAD, ClaimType.INSURER_ORIGINAL_INVOICE, ClaimType.INSURER_SUPPLEMENTARY_INVOICE)));
                // Don't show claims for CHOs that do not allow penalty charges (from BRE band)
                DetachedCriteria bCriteria = DetachedCriteria.forClass(BreBandOrganisation.class, "bbo")
                        .createAlias("bbo.breBand", "bb", CriteriaSpecification.LEFT_JOIN)
                        .createAlias("bb.insurer", "ins2", CriteriaSpecification.LEFT_JOIN)
                        .add(Restrictions.eq("ins2.id", getCurrentUser().getInsurer().getId()))
                        .add(Restrictions.eq("bb.allowManualInvoicePenaltyCharges", Boolean.FALSE));

                bCriteria.setProjection(Projections.property("bbo.chorganisation"));
                criteria.add(Property.forName("this.chorganisation").notIn(bCriteria));
                
            } else if (OrganisationType.CHO.equals(getCurrentUser().getOrganisationType())) {
                criteria.add(Restrictions.ne("status", ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT));
                criteria.add(Restrictions.ne("status", ClaimStatus.INVOICE_PAYMENT_LOGGED));
                // Get the id's of the BRE Bands mapped to this CHO
                DetachedCriteria bCriteria = DetachedCriteria.forClass(BreBandOrganisation.class, "brebandorganisation")
                        .createAlias("brebandorganisation.chorganisation", "cho", CriteriaSpecification.LEFT_JOIN)
                        .add(Restrictions.eq("cho.id", getCurrentUser().getChorganisation().getId()));
                bCriteria.setProjection(Projections.property("brebandorganisation.breBand.id"));

                // Get the insurers from the BRE Band which don't allow penalty charges to be added
                DetachedCriteria pCriteria = DetachedCriteria.forClass(BreBand.class, "breband")
                        .add(Restrictions.disjunction()
                                .add(Restrictions.conjunction()
                                        .add(Restrictions.disjunction()
                                                .add(Restrictions.eq("breband.allowGTAPenaltyCharges", Boolean.FALSE))
                                                .add(Restrictions.conjunction()
                                                        .add(Restrictions.eq("breband.allowGTAAutoPenaltyCharges", Boolean.TRUE))
                                                        .add(Restrictions.eq("this.autoPenaltyChargeEnabled", Boolean.TRUE))))
                                        .add(Restrictions.in("this.claimType", Arrays.asList(ClaimType.GTA, ClaimType.GTA_ORIGINAL_INVOICE, ClaimType.GTA_SUPPLEMENTARY_INVOICE))))
                                .add(Restrictions.conjunction()
                                        .add(Restrictions.disjunction()
                                                .add(Restrictions.eq("breband.allowSubscriberPenaltyCharges", Boolean.FALSE))
                                                .add(Restrictions.conjunction()
                                                        .add(Restrictions.eq("breband.allowSubscriberAutoPenaltyCharges", Boolean.TRUE))
                                                        .add(Restrictions.eq("this.autoPenaltyChargeEnabled", Boolean.TRUE))))
                                        .add(Restrictions.in("this.claimType", Arrays.asList(ClaimType.SUBSCRIBER, ClaimType.SUBSCRIBER_ORIGINAL_INVOICE, ClaimType.SUBSCRIBER_SUPPLEMENTARY_INVOICE))))
                                .add(Restrictions.conjunction()
                                        .add(Restrictions.disjunction()
                                                .add(Restrictions.eq("breband.allowFixedFeePenaltyCharges", Boolean.FALSE))
                                                .add(Restrictions.conjunction()
                                                        .add(Restrictions.eq("breband.allowFixedFeeAutoPenaltyCharges", Boolean.TRUE))
                                                        .add(Restrictions.eq("this.autoPenaltyChargeEnabled", Boolean.TRUE))))
                                        .add(Restrictions.in("this.claimType", Arrays.asList(ClaimType.FIXED_FEE, ClaimType.FIXED_FEE_ORIGINAL_INVOICE, ClaimType.FIXED_FEE_SUPPLEMENTARY_INVOICE))))
                                .add(Restrictions.conjunction()
                                        .add(Restrictions.disjunction()
                                                .add(Restrictions.eq("breband.allowCollaborationProtocolPenaltyCharges", Boolean.FALSE))
                                                .add(Restrictions.conjunction()
                                                        .add(Restrictions.eq("breband.allowCollaborationProtocolAutoPenaltyCharges", Boolean.TRUE))
                                                        .add(Restrictions.eq("this.autoPenaltyChargeEnabled", Boolean.TRUE))))
                                        .add(Restrictions.in("this.claimType", Arrays.asList(ClaimType.COLLABORATION_PROTOCOL, ClaimType.COLLABORATION_PROTOCOL_ORIGINAL_INVOICE, ClaimType.COLLABORATION_PROTOCOL_SUPPLEMENTARY_INVOICE))))
                                .add(Restrictions.conjunction()
                                        .add(Restrictions.disjunction()
                                                .add(Restrictions.eq("breband.allowTPIPenaltyCharges", Boolean.FALSE))
                                                .add(Restrictions.conjunction()
                                                        .add(Restrictions.eq("breband.allowTPIAutoPenaltyCharges", Boolean.TRUE))
                                                        .add(Restrictions.eq("this.autoPenaltyChargeEnabled", Boolean.TRUE))))
                                        .add(Restrictions.in("this.claimType", Arrays.asList(ClaimType.TPI))))
                                .add(Restrictions.conjunction()
                                        .add(Restrictions.disjunction()
                                                .add(Restrictions.eq("breband.allowInsurervsInsurerPenaltyCharges", Boolean.FALSE))
                                                .add(Restrictions.conjunction()
                                                        .add(Restrictions.eq("breband.allowInsurervsInsurerAutoPenaltyCharges", Boolean.TRUE))
                                                        .add(Restrictions.eq("this.autoPenaltyChargeEnabled", Boolean.TRUE))))
                                        .add(Restrictions.in("this.claimType", Arrays.asList(ClaimType.INSURER_VS_INSURER, ClaimType.INSURER_VS_INSURER_ORIGINAL_INVOICE, ClaimType.INSURER_VS_INSURER_SUPPLEMENTARY_INVOICE))))
                                .add(Restrictions.conjunction()
                                        .add(Restrictions.eq("breband.allowManualInvoicePenaltyCharges", Boolean.FALSE))
                                        .add(Restrictions.in("this.claimType", Arrays.asList(ClaimType.INSURER_INVOICE)))))
                        .add(Restrictions.in("breband.id", bCriteria.getExecutableCriteria(getSessionFactory().getCurrentSession()).list()))
                        .setProjection(Projections.property("breband.insurer"));

                // Make sure we retrieve no claims for insurers who don't allow penalty charges to be added
                criteria.add(Property.forName("this.insurer").notIn(pCriteria));
            } else {
                LOG.warn("Error in search criteria: only CHO and Insurer with manual invoices can filter for penalty charges");
            }
        }

        if (searchCriteria.isEscalatedToSupervisor()) {
            criteria.add(Restrictions.ne("status", ClaimStatus.INVOICE_PAYMENT_LOGGED));

            if (getCurrentUser().isAnInsurer()) {
                if (getCurrentUser().getInsurer().getDaysBeforeEscalated() != null && getCurrentUser().getInsurer().getTimesInStatusContested() != null) {
                    criteria.add(Restrictions.disjunction()
                            .add(Restrictions.sqlRestriction("(current_date - iv1_.created_date::Date) >= " + getCurrentUser().getInsurer().getDaysBeforeEscalated()))
                            .add(Restrictions.sqlRestriction("{alias}.id in (select temp.id from (select count(a.claim_id) as nr, a.claim_id as id from audit_trail a "
                                            + "where a.claim_id = {alias}.id "
                                            + "and a.new_status = 'ContestedInvoiceReferredToInsurer' "
                                            + "and a.reverted = false "
                                            + "group by a.claim_id ) as temp where nr >= " + getCurrentUser().getInsurer().getTimesInStatusContested() + ")")));
                } else if (getCurrentUser().getInsurer().getDaysBeforeEscalated() != null) {
                    criteria.add(Restrictions.sqlRestriction("(current_date - iv1_.created_date::Date) >= " + getCurrentUser().getInsurer().getDaysBeforeEscalated()));
                } else if (getCurrentUser().getInsurer().getTimesInStatusContested() != null) {
                    criteria.add(Restrictions.sqlRestriction("{alias}.id in (select temp.id from (select count(a.claim_id) as nr, a.claim_id as id from audit_trail a "
                            + "where a.claim_id = {alias}.id "
                            + "and a.new_status = 'ContestedInvoiceReferredToInsurer' "
                            + "and a.reverted = false "
                            + "group by a.claim_id ) as temp where nr >= " + getCurrentUser().getInsurer().getTimesInStatusContested() + ")"));
                } else {
                    // Supervisor activated but no details given - therefore queue should be empty
                    criteria.add(Restrictions.eq("status", "NoSuchStatus"));
                }
            } else if (getCurrentUser().isCHO()) {
                if (getCurrentUser().getChorganisation().getDaysBeforeEscalated() != null && getCurrentUser().getChorganisation().getTimesInStatusContested() != null) {
                    criteria.add(Restrictions.disjunction()
                            .add(Restrictions.sqlRestriction("(current_date - iv1_.created_date::Date) >= " + getCurrentUser().getChorganisation().getDaysBeforeEscalated()))
                            .add(Restrictions.sqlRestriction("{alias}.id in (select temp.id from (select count(a.claim_id) as nr, a.claim_id as id from audit_trail a "
                                            + "where a.claim_id = {alias}.id "
                                            + "and a.new_status = 'ContestedInvoiceReferredToInsurer' "
                                            + "and a.reverted = false "
                                            + "group by a.claim_id ) as temp where nr >= " + getCurrentUser().getChorganisation().getTimesInStatusContested() + ")")));
                } else if (getCurrentUser().getChorganisation().getDaysBeforeEscalated() != null) {
                    criteria.add(Restrictions.sqlRestriction("(current_date - iv1_.created_date::Date) >= " + getCurrentUser().getChorganisation().getDaysBeforeEscalated()));
                } else if (getCurrentUser().getChorganisation().getTimesInStatusContested() != null) {
                    criteria.add(Restrictions.sqlRestriction("{alias}.id in (select temp.id from (select count(a.claim_id) as nr, a.claim_id as id from audit_trail a "
                            + "where a.claim_id = {alias}.id "
                            + "and a.new_status = 'ContestedInvoiceReferredToInsurer' "
                            + "and a.reverted = false "
                            + "group by a.claim_id ) as temp where nr >= " + getCurrentUser().getChorganisation().getTimesInStatusContested() + ")"));
                } else {
                    // Supervisor activated but no details given - therefore queue should be empty
                    criteria.add(Restrictions.eq("status", "NoSuchStatus"));
                }
            }
        }

        if (searchCriteria.isInterimPaymentMade()) {
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
            ArrayList<ClaimType> ClaimTypes = new ArrayList<>();
            if (searchCriteria.getClaimTypes().contains(ClaimType.GTA)) {
                ClaimTypes.addAll(Arrays.asList(ClaimType.GTA, ClaimType.GTA_ORIGINAL_INVOICE, ClaimType.GTA_SUPPLEMENTARY_INVOICE));
            }
            if (searchCriteria.getClaimTypes().contains(ClaimType.INSURER_UPLOAD)) {
                ClaimTypes.addAll(Arrays.asList(ClaimType.INSURER_INVOICE, ClaimType.INSURER_CLAIM, ClaimType.INSURER_ORIGINAL_INVOICE, ClaimType.INSURER_SUPPLEMENTARY_INVOICE));
            }
            if (searchCriteria.getClaimTypes().contains(ClaimType.INSURER_VS_INSURER)) {
                ClaimTypes.addAll(Arrays.asList(ClaimType.INSURER_VS_INSURER, ClaimType.INSURER_VS_INSURER_ORIGINAL_INVOICE, ClaimType.INSURER_VS_INSURER_SUPPLEMENTARY_INVOICE));
            }
            if (searchCriteria.getClaimTypes().contains(ClaimType.SUBSCRIBER)) {
                ClaimTypes.addAll(Arrays.asList(ClaimType.SUBSCRIBER, ClaimType.SUBSCRIBER_ORIGINAL_INVOICE, ClaimType.SUBSCRIBER_SUPPLEMENTARY_INVOICE));
            }
            if (searchCriteria.getClaimTypes().contains(ClaimType.FIXED_FEE)) {
                ClaimTypes.addAll(Arrays.asList(ClaimType.FIXED_FEE, ClaimType.FIXED_FEE_ORIGINAL_INVOICE, ClaimType.FIXED_FEE_SUPPLEMENTARY_INVOICE));
            }
            if (searchCriteria.getClaimTypes().contains(ClaimType.COLLABORATION_PROTOCOL)) {
                ClaimTypes.addAll(Arrays.asList(ClaimType.COLLABORATION_PROTOCOL, ClaimType.COLLABORATION_PROTOCOL_ORIGINAL_INVOICE, ClaimType.COLLABORATION_PROTOCOL_SUPPLEMENTARY_INVOICE));
            }
            if (searchCriteria.getClaimTypes().contains(ClaimType.TPI)) {
                ClaimTypes.addAll(Arrays.asList(ClaimType.TPI));
            }
            criteria.add(Restrictions.in("claimType", ClaimTypes.toArray()));
        }

        if (searchCriteria.isSupplementaryInvoiceOnly()) {
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

        if (searchCriteria.getFinalReviewValue() != FinalReviewMapping.CHECK_NOT_REQUIRED.getValue()) {
            if (getCurrentUser().isCHO()) {
                if (searchCriteria.getFinalReviewValue() == FinalReviewMapping.CHO_TRUE.getValue()) {
                    criteria.add(Restrictions.eq("finalReviewCho", true));
                } else if (searchCriteria.getFinalReviewValue() == FinalReviewMapping.CHO_FALSE.getValue()) {
                    criteria.add(Restrictions.eq("finalReviewCho", false));
                }
            } else if (getCurrentUser().isAnInsurer()) {
                if (searchCriteria.getFinalReviewValue() == FinalReviewMapping.INS_TRUE.getValue()) {
                    criteria.add(Restrictions.eq("finalReviewIns", true));
                } else if (searchCriteria.getFinalReviewValue() == FinalReviewMapping.INS_FALSE.getValue()) {
                    criteria.add(Restrictions.eq("finalReviewIns", false));
                }
            } else if (getCurrentUser().isCHOXAdmin()) {
                if (searchCriteria.getFinalReviewValue() == FinalReviewMapping.CHO_TRUE.getValue()) {
                    criteria.add(Restrictions.eq("finalReviewCho", true));
                } else if (searchCriteria.getFinalReviewValue() == FinalReviewMapping.CHO_FALSE.getValue()) {
                    criteria.add(Restrictions.eq("finalReviewCho", false));
                } else if (searchCriteria.getFinalReviewValue() == FinalReviewMapping.INS_TRUE.getValue()) {
                    criteria.add(Restrictions.eq("finalReviewIns", true));
                } else if (searchCriteria.getFinalReviewValue() == FinalReviewMapping.INS_FALSE.getValue()) {
                    criteria.add(Restrictions.eq("finalReviewIns", false));
                } else if (searchCriteria.getFinalReviewValue() == FinalReviewMapping.CHO_OR_INS_TRUE.getValue()) {
                    criteria.add(Restrictions.disjunction()
                            .add(Restrictions.eq("finalReviewCho", true))
                            .add(Restrictions.eq("finalReviewIns", true)));
                } else if (searchCriteria.getFinalReviewValue() == FinalReviewMapping.CHO_AND_INS_TRUE.getValue()) {
                    criteria.add(Restrictions.conjunction()
                            .add(Restrictions.eq("finalReviewCho", true))
                            .add(Restrictions.eq("finalReviewIns", true)));
                } else if (searchCriteria.getFinalReviewValue() == FinalReviewMapping.CHO_AND_INS_FALSE.getValue()) {
                    criteria.add(Restrictions.eq("finalReviewCho", false));
                    criteria.add(Restrictions.eq("finalReviewIns", false));
                }
            }
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
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value = "transactionManager")
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
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value = "transactionManager")
    public int getSubscriberClaimDays(int id) {
        int claimAge = -1;
        LOG.debug("Getting days of subscriber claim with id={}", id);
        Claim claim = (Claim) get(Claim.class, id);
        if (claim == null) {
            LOG.error("Cannot get Subscriber days for claim with id={}: no such claim", id);
            return claimAge;
        }
        if (ClaimType.isSubscriber(claim.getClaimType())) {
            BreBand breBand = breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
            int subscriberSlaDays = breBand.getSubscriberSlaDays();
            String subscriberCutOffTime = breBand.getSubscriberTimeCutOff();
            claimAge = auditTrailService.getSubscriberClaimDays(id, breBand.isPauseSubscriberSlaClock());
            LOG.debug("claimAge={}, subscriberSlaDays={}, subscriberCutOffTime={}, SlaExtDays={}",
                    new Object[]{claimAge, subscriberSlaDays, subscriberCutOffTime, claim.getSlaExtDays()});

            if (subscriberSlaDays != 0 && (claimAge > (subscriberSlaDays + claim.getSlaExtDays()) || (claimAge == (subscriberSlaDays + claim.getSlaExtDays()) && !DateHelper.isBeforeCutOffTime(subscriberCutOffTime)))) {
                boolean addComment = true;
                List<Comment> comments = commentService.getCommentByClaimId(claim.getId());
                for (Comment comment : comments) {
                    if (comment.getComment().endsWith("claim taken down Subscriber route.") && !comment.isReverted()) {
                        addComment = false;
                        LOG.debug("Comment already added - skipping");
                        break;
                    }
                }
                if (addComment) {
                    Comment comment = Comment.newComment(0, claim.getInsurer().getName() + " failed to respond to the Subscriber notification within the " + subscriberSlaDays + " day SLA, claim taken down Subscriber route.");
                    if (claim.getSlaExtDays() > 0) {
                        comment = Comment.newComment(0, claim.getInsurer().getName() + " failed to respond to the Subscriber notification within the " + subscriberSlaDays + " day SLA + " + claim.getSlaExtDays() + " day extension, claim taken down Subscriber route.");
                    }
                    comment.setRaisedBy(userService.getWebUser(999));
                    claim.addComment(comment);
                    save(claim);
                    LOG.debug("Comment added and claim saved.");
                }
            }
        }

        LOG.debug("Returning claim age of {}", claimAge);
        return claimAge;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value = "transactionManager")
    public String stopClaimChase(String choRef) {
        StringBuilder result = new StringBuilder();

        Claim claim = this.getClaimByCHOReferenceNumber(choRef);

        if (claim == null) {
            result.append("Claim with CHO ref ").append(choRef).append(" does not exist");
        } else if (claim.isTotalLossChase()) {
            claim.setTotalLossChase(false);
            save(claim);
            result.append("Chase tasked stopped for claim with CHO ref ").append(choRef);
        } else {
            result.append("Chase tasked already stopped for claim with CHO ref ").append(choRef);
        }
        return result.toString();
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value = "transactionManager")
    public int getFixedFeeClaimDays(int id) {
        int claimAge = -1;
        LOG.debug("Getting days of fixed-fee claim with id={}", id);
        Claim claim = (Claim) get(Claim.class, id);
        if (claim == null) {
            LOG.error("Cannot get Fixed Fee days for claim with id={}: no such claim", id);
            return claimAge;
        } else if (ClaimType.isFixedFee(claim.getClaimType())) {

            BreBand breBand = breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
            int fixedFeeSlaDays = breBand.getFixedFeeSlaDays();
            String fixedFeeCutOffTime = breBand.getFixedFeeTimeCutOff();

            claimAge = auditTrailService.getFixedFeeClaimDays(id, breBand.isPauseFixedFeeSlaClock());

            if (fixedFeeSlaDays != 0 && (claimAge > (fixedFeeSlaDays + claim.getSlaExtDays()) || (claimAge == (fixedFeeSlaDays + claim.getSlaExtDays()) && !DateHelper.isBeforeCutOffTime(fixedFeeCutOffTime)))) {
                boolean addComment = true;
                List<Comment> comments = commentService.getCommentByClaimId(claim.getId());
                for (Comment comment : comments) {
                    if (comment.getComment().endsWith("claim taken down Fixed Fee route.") && !comment.isReverted()) {
                        addComment = false;
                        LOG.debug("Comment already added - skipping");
                        break;
                    }
                }
                if (addComment) {
                    Comment comment = Comment.newComment(0, claim.getInsurer().getName() + " failed to respond to the Fixed Fee notification within the " + fixedFeeSlaDays + " day SLA, claim taken down Fixed Fee route.");
                    if (claim.getSlaExtDays() > 0) {
                        comment = Comment.newComment(0, claim.getInsurer().getName() + " failed to respond to the Fixed Fee notification within the " + fixedFeeSlaDays + " day SLA + " + claim.getSlaExtDays() + " day extension, claim taken down Fixed Fee route.");
                    }
                    comment.setRaisedBy(userService.getWebUser(999));
                    claim.addComment(comment);
                    save(claim);
                    LOG.debug("Comment added and claim saved.");
                }
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
        if (claim == null) {
            LOG.error("Cannot get Subscriber Claim Rejected days for claim with id={}: no such claim", claimId);
            return claimAge;
        }
        BreBand breBand = breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
        String subscriberCutOffTime = breBand.getSubscriberTimeCutOff();

        if (ClaimType.isSubscriber(claim.getClaimType())) {
            claimAge = auditTrailService.getSubscriberClaimRejectedDays(claimId, subscriberCutOffTime, breBand.isPauseSubscriberSlaClock());
        }

        LOG.debug("Days until subscriber claim ({}) rejected: {}", claimId, claimAge);

        return claimAge;
    }

    @Override
    public int getFixedFeeClaimRejectedDays(int claimId) {
        int claimAge = -1;
        LOG.debug("Getting days until fixed fee claim rejected with id={}", claimId);
        Claim claim = (Claim) get(Claim.class, claimId);
        
        if (claim == null) {
            LOG.error("Cannot get Fixed Fee  Claim Rejected days for claim with id={}: no such claim", claimId);
            return claimAge;
        }
        BreBand breBand = breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());

        if (ClaimType.isFixedFee(claim.getClaimType())) {
            claimAge = auditTrailService.getFixedFeeClaimRejectedDays(claimId, breBand.isPauseFixedFeeSlaClock());
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
                // Check for linked CHO
                boolean exists = false;
                if (claim.getChorganisation().getLinkedCho() != null) {
                    exists = isClaimSupplierReferenceNumberExistForChoExternal(newReference, claim.getChorganisation().getLinkedCho().getId());
                }
                if (!exists) {
                    try {
                        claim.setChoReference(newReference);
                        claim.addComment(Comment.newComment(0, "Supplier Reference updated from '" + oldReference + "' to '" + newReference + "'."));
                        updateClaim(claim);
                        LOG.debug("Claim with reference number " + oldReference + " updated with new Cho reference number: " + newReference);
                        // Generate Event
                        eventService.generate(claim, ChoxEvent.CHO_REFERENCE_NO_UPDATED_EVENT, oldReference);
                        return 0;
                    } catch (Exception ex) {
                        LOG.error("Cannot update claim with reference number " + oldReference + " to new Cho reference number: " + newReference, ex);
                        return 9;
                    }
                } else {
                    return 4;
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
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value = "transactionManager")
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
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value = "transactionManager")
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

        Invoice invoice = claim.getInvoice();
        if (invoice != null) {
            LiabilityStatus liabilityStatus = claim.getLiabilityStatus();
            ClaimType claimType = claim.getClaimType();
            // When excluding some 'Claim Type' Please exclude it from applyAutoPenaltyCharge Stored Procedure as well.
            if (!ClaimType.isInsurerVsInsurer(claimType)
                    && !ClaimType.isSubscriber(claimType)
                    && !ClaimType.isFixedFee(claimType)
                    && !ClaimType.isCollaborationProtocol(claimType)
                    && liabilityStatus != null
                    && (liabilityStatus.equals(LiabilityStatus.LIABILITY_SPLIT)
                    || (liabilityStatus.equals(LiabilityStatus.PROCEED_WITHOUT_PREJUDICE)))) {
                BigDecimal ttp = invoice.getFullTotalToPay();
                BigDecimal insper = claim.getPercentageLiabilityAccepted();
                invoice.setTotalToPay(ttp.multiply(insper).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
                LOG.debug("liability updated: {}", invoice.getTotalToPay());
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

        if (isClaimInInsurerClosedStatus(claim)) {
            return 0;
        }

        Date createdDate = claim.getInvoice().getCreatedDate();
        Date currentDate = DateHelper.getCurrentDate();
        return DateHelper.getNumberOfDaysBetween(createdDate, currentDate) + 1;
    }

    @Override
    public int getNumberOfTimesContestedWithCHOtoEscalate(Integer claimId) {
        Claim claim = this.getClaim(claimId);

        if (isClaimInInsurerClosedStatus(claim)) {
            return 0;
        }

        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(AuditTrail.class);
        criteria.add(Restrictions.eq("newStatus", ClaimStatus.CONTESTED_INVOICE_REF_TO_INS));
        criteria.add(Restrictions.eq("reverted", false));
        criteria.add(Restrictions.eq("claim.id", claimId));
        return totalCount(criteria);
    }

    private boolean isClaimInInsurerClosedStatus(Claim claim) {
        for (String status : ClaimStatus.getInsurerClosedStatus(true)) {
            if (claim.getStatus().equalsIgnoreCase(status)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int getNoOfRejectedClaims(Integer reasonOfRejectionId) {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Claim.class);
        criteria.add(Restrictions.eq("reasonOfRejection.id", reasonOfRejectionId));
        return totalCount(criteria);
    }

    @Override
    public String getOverlappingHire(Claim claim) {
        /* returns the insurer claim nuber of any claim found with an overlappinh hire period
         * to the argument claim, otherwise null
         */
        String insurerClaimNumber = null;

        if (claim.getVehicleHire() != null && claim.getVehicleHire().getVehicleRegistration() != null
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

    @Override
    public int createChaseTask(Claim claim) {
        int returnStatus = 1;

        Task task = new Task();
        task.setClaim(claim);
        task.setComplete(Boolean.FALSE);
        task.setDescription("Total Loss pack has been uploaded and requires review.");
        task.setDueDate(DateHelper.addDay(new Date(), 1));
        task.setInsurer(false);
        task.setRaisedBy(userService.findByUserName("system"));
        task.setType("IMS TL Chase Task");
        task.setVisibility(3);
        try {
            taskService.createNewTask(task);
        } catch (Exception ex) {
            LOG.debug("Exception thrown creating chase task on claim with choref '{}'", claim.getChoReference());
            returnStatus = 0;
        }

        return returnStatus;
    }

    @Override
    public List<Claim> getTotalLossChaseClaims() {
        // Return claims where totalLossChase is true and there has been no total loss task created in the past 7 days
        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class, "cl");
        criteria.add(Restrictions.eq("totalLossChase", Boolean.TRUE));
        criteria.add(Restrictions.ne("status", ClaimStatus.CLAIM_CLOSED));
        criteria.add(Restrictions.ne("status", ClaimStatus.CLAIM_REJECTION_ACCEPTED));
        criteria.add(Restrictions.ne("status", ClaimStatus.INVOICE_REJECTED_ACCEPTED));
        criteria.add(Restrictions.ne("status", ClaimStatus.INVOICE_PAYMENT_LOGGED));
        criteria.add(Restrictions.ne("status", ClaimStatus.INVOICE_PAYMENT_RECEIVED));

        Date lastWeek = DateHelper.addDay(new Date(), -6);
        lastWeek = DateHelper.removeTime(lastWeek);

        // Check no total loss upload task in past 7 days
        DetachedCriteria subQuery = DetachedCriteria.forClass(Attachment.class, "a");
        subQuery.add(Restrictions.eq("category", "Total Loss Pack"))
                .add(Restrictions.ge("createdDate", lastWeek))
                .add(Restrictions.ilike("remarks", "Total Loss pack has been uploaded for review%"))
                .add(Restrictions.eqProperty("cl.id", "a.claim.id"));
        subQuery.setProjection(Projections.id());

        criteria.add(Subqueries.notExists(subQuery));

        // Check task hasn't been added in previous 7 days
        DetachedCriteria subQuery2 = DetachedCriteria.forClass(Task.class, "t");
        subQuery2.add(Restrictions.eq("type", "IMS TL Chase Task"))
                .add(Restrictions.gt("createdDate", lastWeek))
                .add(Restrictions.eqProperty("cl.id", "t.claim.id"));
        subQuery2.setProjection(Projections.id());

        criteria.add(Subqueries.notExists(subQuery2));

        return (List<Claim>) findByCriteria(criteria);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value = "transactionManager")
    public void setTotalLoss(Claim claim, boolean isTotalLoss) {
        if (claim.getCustomer().getIsTotalLoss() != null
                && claim.getCustomer().getIsTotalLoss() == isTotalLoss) {
            return;
        }
        HireMonitoringDetail hireMonDetail = claim.getHireMonitoringDetail();
        if (hireMonDetail == null) {
            hireMonDetail = new HireMonitoringDetail();
            claim.setHireMonitoringDetail(hireMonDetail);
        }
        hireMonDetail.setIsTotalLostCheck(isTotalLoss);
        hireMonDetail.setIsTotalLostCheckLastModified(new Date());
        if (claim.getCustomer().getIsTotalLossOriginal() == null) {
            claim.getCustomer().setIsTotalLossOriginal(claim.getCustomer().getIsTotalLoss());
        }
        claim.getCustomer().setIsTotalLoss(isTotalLoss);
        checkTotalLossAnomaly(claim);
        eventService.generate(claim, ChoxEvent.TOTAL_LOSS_UPDATE_EVENT);
    }

    @Override
    public boolean setLiability(Claim claim, LiabilityStatus liabilityStatus) {
        boolean updated = false;

        if (liabilityStatus != null && !claim.getLiabilityStatus().equals(liabilityStatus)) {

            String note;
            if (claim.getLiabilityStatus() == LiabilityStatus.LIABILITY_NULL) {
                note = new StringBuilder().append("Liability status changed to '").append(liabilityStatus).append("'").toString();
            } else {
                note = new StringBuilder().append("Liability status changed from '").append(claim.getLiabilityStatus()).append("' to '").append(liabilityStatus).append("'").toString();
            }
            claim.setLiability(liabilityStatus);
            Comment comment = Comment.newComment(0, note);
            comment.setClaim(claim);
            claim.addComment(comment);
            notificationService.addNotification(claim, new LiabilityStatusUpdatedNotification(liabilityStatus));
            updated = true;
        }

        return updated;
    }

//    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    private boolean updateAutomaticPenaltyCharge(Claim claim) {
        LOG.debug("Updating penalty charges: claim.isAutoPenaltyChargeEnabled()={}, claim.getChorganisation().isAutoPenaltyChargeEnabled()={}, "
                + "!ClaimStatus.isInPenaltyChargeExclusionStatus(claim.getStatus())={}, claim.getInvoice()={}",
                new Object[]{claim.isAutoPenaltyChargeEnabled(), claim.getChorganisation().isAutoPenaltyChargeEnabled(),
                    !ClaimStatus.isInPenaltyChargeExclusionStatus(claim.getStatus()),
                    claim.getInvoice()});
        
        // Set Claim BRE band
        BreBand choBand = breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
        claim.setBreBand(choBand);
        Date hireStart = (claim.getVehicleHire() != null && claim.getVehicleHire().getHireStart() != null) ? claim.getVehicleHire().getHireStart()
                                                : (claim.getInvoice() != null && claim.getInvoice().getDateInvoiced() != null) ? claim.getInvoice().getDateInvoiced() : new Date();
        BrePenaltyBand brePenaltyBand = brePenaltyBandService.getBrePenaltyBand(claim, hireStart);

        if (claim.isAutoPenaltyChargeEnabled()
                && brePenaltyBand != null
                && claim.getChorganisation().isAutoPenaltyChargeEnabled()
                && !ClaimStatus.isInPenaltyChargeExclusionStatus(claim.getStatus())
                && claim.getInvoice() != null
                && !brePenaltyBand.isUseCommercialDay1()
                && ((claim.getInvoice().getInvoicedDays() > brePenaltyBand.getHirePeriodStartDay1() && brePenaltyBand.getHirePeriodStartDay1() > 0)
                    || (claim.getInvoice().getInvoicedDays() > brePenaltyBand.getRepairPeriodStartDay1() && brePenaltyBand.getRepairPeriodStartDay1() > 0))) {

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

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value = "transactionManager")
    private void updatePenaltyStartDate(Claim claim, Date autoPenaltyStart) {

        Invoice inv = claim.getInvoice();
        inv.setFullTotalToPay(inv.getFullTotalToPay().subtract(inv.getHirePenaltyCharge()).subtract(inv.getRepairPenaltyCharge()));
        inv.setAutoPenaltyStart(autoPenaltyStart);
        inv.setHirePenaltyPercentage(null);
        inv.setRepairPenaltyPercentage(null);
        inv.setHirePenaltyCharge(BigDecimal.ZERO);
        inv.setRepairPenaltyCharge(BigDecimal.ZERO);
        setInitialPenaltyBand(claim);
        inv.setHirePenaltyChargeAppliedDate(null);
        inv.setRepairPenaltyChargeAppliedDate(null);
        if (inv.getTotalPenaltyCharge() != null && inv.getTotalPenaltyCharge().compareTo(BigDecimal.ZERO) > 0) {
            Comment comment = Comment.newComment(0, "Penalty charges have been removed from the invoice as the date from which penalty charges are calculated has been manually updated.");
            claim.addComment(comment);
        }
        inv.setTotalPenaltyCharge(BigDecimal.ZERO);
        insurerDiscountService.applyInsurerDiscounts(claim, userService.findByUserName("system"), true);
        updateLiabilityPayment(claim);
        updateClaim(claim);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value = "transactionManager")
    @Override
    public void setInitialPenaltyBand(Claim claim) {
        Date hireStart = (claim.getVehicleHire() != null && claim.getVehicleHire().getHireStart() != null) ? claim.getVehicleHire().getHireStart()
                : (claim.getInvoice() != null && claim.getInvoice().getDateInvoiced() != null) ? claim.getInvoice().getDateInvoiced() : new Date();
        BrePenaltyBand brePenaltyBand = brePenaltyBandService.getBrePenaltyBand(claim, hireStart);

        if (brePenaltyBand != null) {
            int days = claim.getInvoice().getInvoicedDays();

            if (days >= brePenaltyBand.getHirePeriodStartDay3() && brePenaltyBand.getHirePeriodStartDay3() > 0) {
                claim.getInvoice().setPenaltyBand(brePenaltyBand.getHirePeriodStartDay3());
                if (brePenaltyBand.isUseCommercialDay3()) {
                    claim.setAutoPenaltyChargeEnabled(false);
                }
            } else if (days >= brePenaltyBand.getRepairPeriodStartDay3() && brePenaltyBand.getRepairPeriodStartDay3() > 0) {
                claim.getInvoice().setPenaltyBand(brePenaltyBand.getRepairPeriodStartDay3());
                if (brePenaltyBand.isUseCommercialDay3()) {
                    claim.setAutoPenaltyChargeEnabled(false);
                }
            } else if (days >= brePenaltyBand.getHirePeriodStartDay2() && brePenaltyBand.getHirePeriodStartDay2() > 0) {
                claim.getInvoice().setPenaltyBand(brePenaltyBand.getHirePeriodStartDay2());
                if (brePenaltyBand.isUseCommercialDay2()) {
                    claim.setAutoPenaltyChargeEnabled(false);
                }
            } else if (days >= brePenaltyBand.getRepairPeriodStartDay2() && brePenaltyBand.getRepairPeriodStartDay2() > 0) {
                claim.getInvoice().setPenaltyBand(brePenaltyBand.getRepairPeriodStartDay2());
                if (brePenaltyBand.isUseCommercialDay2()) {
                    claim.setAutoPenaltyChargeEnabled(false);
                }
            } else if (brePenaltyBand.getHirePeriodStartDay1() > 0) {
                claim.getInvoice().setPenaltyBand(brePenaltyBand.getHirePeriodStartDay1());
                if (brePenaltyBand.isUseCommercialDay1()) {
                    claim.setAutoPenaltyChargeEnabled(false);
                }
            } else if (brePenaltyBand.getRepairPeriodStartDay1() > 0) {
                claim.getInvoice().setPenaltyBand(brePenaltyBand.getRepairPeriodStartDay1());
                if (brePenaltyBand.isUseCommercialDay1()) {
                    claim.setAutoPenaltyChargeEnabled(false);
                }
            } else {
                claim.getInvoice().setPenaltyBand(-1);
                claim.setAutoPenaltyChargeEnabled(false);
            }
            LOG.debug("Days={}, setting initial penalty band to {} for claim {}", new Object[]{days, claim.getInvoice().getPenaltyBand(), claim.getChoReference()});
        } else {
            claim.getInvoice().setPenaltyBand(-1);
            LOG.debug("No initial penalty band to set for claim {}", claim.getChoReference());
        }
    }


    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value = "transactionManager")
    private void setNextPenaltyBand(Claim claim) {
        Date hireStart = (claim.getVehicleHire() != null && claim.getVehicleHire().getHireStart() != null) ? claim.getVehicleHire().getHireStart()
                : (claim.getInvoice() != null && claim.getInvoice().getDateInvoiced() != null) ? claim.getInvoice().getDateInvoiced() : new Date();
        BrePenaltyBand brePenaltyBand = brePenaltyBandService.getBrePenaltyBand(claim, hireStart);

        if (brePenaltyBand != null) {
//        Date penaltyStartDate = claim.getInvoice().getAutoPenaltyStart();
//        long days = TimeUnit.DAYS.convert((new Date()).getTime() - penaltyStartDate.getTime(), TimeUnit.MILLISECONDS);
            int days = claim.getInvoice().getInvoicedDays();

            if (days > brePenaltyBand.getHirePeriodStartDay2() && days < brePenaltyBand.getHirePeriodStartDay3() && brePenaltyBand.getHirePeriodStartDay2() > 0 && brePenaltyBand.getHirePeriodStartDay3() > 0) {
                claim.getInvoice().setPenaltyBand(brePenaltyBand.getHirePeriodStartDay3());
                if (brePenaltyBand.isUseCommercialDay3()) {
                    claim.setAutoPenaltyChargeEnabled(false);
                }
            } else if (days > brePenaltyBand.getHirePeriodStartDay2() && days < brePenaltyBand.getHirePeriodStartDay3() && brePenaltyBand.getHirePeriodStartDay2() > 0 && brePenaltyBand.getHirePeriodStartDay3() <= 0) {
                claim.getInvoice().setPenaltyBand(brePenaltyBand.getRepairPeriodStartDay3());
                if (brePenaltyBand.isUseCommercialDay3()) {
                    claim.setAutoPenaltyChargeEnabled(false);
                }
            } else if (days > brePenaltyBand.getHirePeriodStartDay1() && days < brePenaltyBand.getHirePeriodStartDay2() && brePenaltyBand.getHirePeriodStartDay1() > 0 && brePenaltyBand.getHirePeriodStartDay2() > 0) {
                claim.getInvoice().setPenaltyBand(brePenaltyBand.getHirePeriodStartDay2());
                if (brePenaltyBand.isUseCommercialDay2()) {
                    claim.setAutoPenaltyChargeEnabled(false);
                }
            } else if (days > brePenaltyBand.getHirePeriodStartDay1() && days < brePenaltyBand.getHirePeriodStartDay2() && brePenaltyBand.getHirePeriodStartDay1() > 0 && brePenaltyBand.getHirePeriodStartDay2() <= 0) {
                claim.getInvoice().setPenaltyBand(brePenaltyBand.getRepairPeriodStartDay2());
                if (brePenaltyBand.isUseCommercialDay2()) {
                    claim.setAutoPenaltyChargeEnabled(false);
                }
            } else {
                claim.getInvoice().setPenaltyBand(-1);
                claim.setAutoPenaltyChargeEnabled(false);
            }
            LOG.debug("Days={}, setting next penalty band to {} for claim {}", new Object[]{days, claim.getInvoice().getPenaltyBand(), claim.getChoReference()});
        } else {
            claim.getInvoice().setPenaltyBand(-1);
            LOG.debug("No next penalty band to set for claim {}", claim.getChoReference());
        }

    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value = "transactionManager")
    public boolean setPenaltyStartToDateInvoiced(String choReference) {
        Claim claim = getClaimByCHOReferenceNumber(choReference);
        if (claim != null && claim.getInvoice() != null) {
            updatePenaltyStartDate(claim, claim.getInvoice().getDateInvoiced());
            updateAutomaticPenaltyCharge(claim);
            insurerDiscountService.applyInsurerDiscounts(claim, null, false);
            updateLiabilityPayment(claim);
            return true;
        }

        return false;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value = "transactionManager")
    public Map applyPenaltyCharge(Claim claim, Boolean isPenaltyAlertNotUsed, BigDecimal hirePenaltyChargeAmount,
            String hirePenaltyPercentage, BigDecimal repairPenaltyChargeAmount, String repairPenaltyPercentage) {
        Map resultMap = new HashMap();
        try {

            Invoice invoice = claim.getInvoice();
            BigDecimal newTotalAmountToPay = invoice.getFullTotalToPay().subtract(invoice.getHirePenaltyCharge())
                    .subtract(invoice.getRepairPenaltyCharge()).add(hirePenaltyChargeAmount).add(repairPenaltyChargeAmount);
            if (hirePenaltyChargeAmount.compareTo(BigDecimal.ZERO) > 0 && (hirePenaltyPercentage == null || hirePenaltyPercentage.length() == 0)) {
                resultMap.put("error", "You must supply a value for 'Hire Penalty Percentage'.");
                return resultMap;
            }
            if (repairPenaltyChargeAmount.compareTo(BigDecimal.ZERO) > 0 && (repairPenaltyPercentage == null || repairPenaltyPercentage.length() == 0)) {
                resultMap.put("error", "You must supply a value for 'Repair Penalty Percentage'.");
                return resultMap;
            }
            if (hirePenaltyChargeAmount.compareTo(invoice.getHirePenaltyCharge()) != 0) {
                invoice.setHirePenaltyChargeAppliedDate(DateHelper.getCurrentDateTime());
            }
            if (repairPenaltyChargeAmount.compareTo(invoice.getRepairPenaltyCharge()) != 0) {
                invoice.setRepairPenaltyChargeAppliedDate(DateHelper.getCurrentDateTime());
            }
            invoice.setFullTotalToPay(newTotalAmountToPay);
            invoice.setHirePenaltyCharge(hirePenaltyChargeAmount);
            invoice.setHirePenaltyPercentage(hirePenaltyPercentage+"%");
            invoice.setRepairPenaltyCharge(repairPenaltyChargeAmount);
            invoice.setRepairPenaltyPercentage(repairPenaltyPercentage+"%");
            invoice.setTotalPenaltyCharge(hirePenaltyChargeAmount.add(repairPenaltyChargeAmount));

            // As we are applying a manual penalty charge, we need to de-activate auto-penalty charges on this claim
            //  - should only be active in case of a manual invoice
// Currently a message is displayed advising the user to disable automatic penalty charges, so we won't fo it automatically (for now)
//            if (ClaimType.isInsurerUpload(claim.getClaimType()) && claim.isAutoPenaltyChargeEnabled()) {
//                claim.setAutoPenaltyChargeEnabled(false);
//            }
            
            insurerDiscountService.applyInsurerDiscounts(claim, userService.findByUserName("system"), true);
            updateLiabilityPayment(claim);

            if ((isPenaltyAlertNotUsed != null && isPenaltyAlertNotUsed)
                    || (claim.getChorganisation().isAutoPenaltyChargeEnabled() && claim.isAutoPenaltyChargeEnabled())) {
                setNextPenaltyBand(claim);
            }
            LOG.debug("Hire penalty %: '{}', Repair penalty %: '{}'", hirePenaltyPercentage, repairPenaltyPercentage);
            updateClaim(claim);

        } catch (Exception ex) {
            LOG.error("Exception thrown applying penalty charges to claim '{}': ", claim.getChoReference(), ex);
            resultMap.put("error", "An internal error occurred applying penalty charges to this claim. Please contact CHOX support.");
        }

        return resultMap;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value = "transactionManager")
    public Map adjustAutoPenaltyCharge(Claim claim, Date autoPenaltyStart, boolean isCHO) {
        Map resultMap = new HashMap();
        if (autoPenaltyStart != null) {

            Date invoiceCreationDate = claim.getInvoice().getCreatedDate();
            Date penaltyStartDate = claim.getInvoice().getAutoPenaltyStart();
            // Set both times to 00:00:00
            if (invoiceCreationDate != null) {
                invoiceCreationDate = DateHelper.setStartOfDay(invoiceCreationDate);
            }
            if (penaltyStartDate != null) {
                penaltyStartDate = DateHelper.setStartOfDay(penaltyStartDate);
            }
            // For CHO, the autoPenaltyStartDate must be AFTER the invoice creation date
            LOG.debug("autoPenaltyStart={}, penaltyStartDate={}, invoiceCreationDate={}", new Object[]{autoPenaltyStart, penaltyStartDate, invoiceCreationDate});
            if (isCHO && autoPenaltyStart.compareTo(penaltyStartDate) != 0 && autoPenaltyStart.compareTo(invoiceCreationDate) < 0) {
                LOG.warn("Attempt (by CHO) to set penalty-start date ({}) to before invoice upload date ({}).", autoPenaltyStart, invoiceCreationDate);
                resultMap.put("error", "The 'Penalty Charge Start Date' cannot be set to before the invoice was uploaded and has not been saved.");
                return resultMap;
            }
            updateClaim(claim);
//            if (autoPenaltyStart.compareTo(penaltyStartDate) != 0) {
                // The date has been changed
                updatePenaltyStartDate(claim, autoPenaltyStart);
//            }
            if (updateAutomaticPenaltyCharge(claim)) {
                LOG.debug("Auto Penalty charges updated for claim '{}'", claim.getChoReference());
                // Invoice details may have changed  so we need to reload the claim
                claim = getClaim(claim.getId());
                resultMap.put("claim", claim);
            } else {
                LOG.debug("Auto Penalty charges not updated for claim '{}'", claim.getChoReference());
            }
        }

        return resultMap;
    }

    @Override
    public boolean canShowPenaltyChargeAlert(Claim claim, boolean isCHO) {
        boolean result = false;
        boolean allowPenaltyCharges = true;

        Invoice invoice = claim.getInvoice();
        // Set Claim BRE band
        BreBand choBand = breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
        claim.setBreBand(choBand);
        if (claim.getBreBand() == null) {
            LOG.error("No BRE Band for claim '{}'", claim.getChoReference());
            allowPenaltyCharges = false;
        } else if (!claim.getBreBand().isAllowPenaltyCharges(claim.getClaimType())) {
            allowPenaltyCharges = false;
        }
        if (allowPenaltyCharges && invoice != null
                && !ClaimStatus.isInPenaltyChargeExclusionStatus(claim.getStatus())
                && invoice.getPenaltyBand() > -1
                && ((isCHO && (!claim.getChorganisation().isAutoPenaltyChargeEnabled()
                                || (claim.getChorganisation().isAutoPenaltyChargeEnabled()
                                    && !claim.isAutoPenaltyChargeEnabled())))
                    || !isCHO && ClaimType.isInsurerUpload(claim.getClaimType()))
            ) {
            result = invoice.getInvoicedDays() > invoice.getPenaltyBand();
        }
//        LOG.debug("canShowPenaltyChargeAlert returning {}: invoicedDays={}, band={}, allowPenaltyCharges={}, isCHO={}",
//                new Object[]{result, invoice.getInvoicedDays(), invoice.getPenaltyBand(), allowPenaltyCharges, isCHO});
        return result;
    }

    /*
     *  Calculate the penalty amount for the given claim.
     */
    @Override
    public BigDecimal calculateHirePenaltyChargeVal(Claim claim) {

        return (getHirePenaltyPercentageVal(claim).divide(new BigDecimal(100)).multiply(claim.getInvoice().getHireGross()))
                .setScale(2, RoundingMode.HALF_UP);

    }

    @Override
    public BigDecimal calculateRepairPenaltyChargeVal(Claim claim) {

        return (getRepairPenaltyPercentageVal(claim).divide(new BigDecimal(100)).multiply(claim.getInvoice().getRepairGross()))
                .setScale(2, RoundingMode.HALF_UP);

    }

    /*
     *  Calculate the penalty amount using the provided penalty percentage for the given claim.
     */
    @Override
    public BigDecimal calculateHirePenaltyChargeVal(Claim claim, String percentage) {
        Date hireStart = (claim.getVehicleHire() != null && claim.getVehicleHire().getHireStart() != null) ? claim.getVehicleHire().getHireStart()
                : (claim.getInvoice() != null && claim.getInvoice().getDateInvoiced() != null) ? claim.getInvoice().getDateInvoiced() : new Date();
        BrePenaltyBand brePenaltyBand = brePenaltyBandService.getBrePenaltyBand(claim, hireStart);

        if (brePenaltyBand != null) {
            if (brePenaltyBand.getHireDay1().toString().equals(percentage)) {
                return (brePenaltyBand.getHireDay1().divide(new BigDecimal(100)).multiply(claim.getInvoice().getHireGross()))
                        .setScale(2, RoundingMode.HALF_UP);
            } else if (brePenaltyBand.getHireDay2().toString().equals(percentage)) {
                return (brePenaltyBand.getHireDay2().divide(new BigDecimal(100)).multiply(claim.getInvoice().getHireGross()))
                        .setScale(2, RoundingMode.HALF_UP);
            } else if (brePenaltyBand.getHireDay3().toString().equals(percentage)) {
                return (brePenaltyBand.getHireDay3().divide(new BigDecimal(100)).multiply(claim.getInvoice().getHireGross()))
                        .setScale(2, RoundingMode.HALF_UP);
            }
        }
        
        return BigDecimal.ZERO.setScale(2);
    }

    @Override
    public BigDecimal calculateRepairPenaltyChargeVal(Claim claim, String percentage) {

        Date hireStart = (claim.getVehicleHire() != null && claim.getVehicleHire().getHireStart() != null) ? claim.getVehicleHire().getHireStart()
                : (claim.getInvoice() != null && claim.getInvoice().getDateInvoiced() != null) ? claim.getInvoice().getDateInvoiced() : new Date();
        BrePenaltyBand brePenaltyBand = brePenaltyBandService.getBrePenaltyBand(claim, hireStart);
        
        if (brePenaltyBand != null) {
            if (brePenaltyBand.getRepairDay1().toString().equals(percentage)) {
                return (brePenaltyBand.getRepairDay1().divide(new BigDecimal(100)).multiply(claim.getInvoice().getRepairGross()))
                        .setScale(2, RoundingMode.HALF_UP);
            } else if (brePenaltyBand.getRepairDay2().toString().equals(percentage)) {
                return (brePenaltyBand.getRepairDay2().divide(new BigDecimal(100)).multiply(claim.getInvoice().getRepairGross()))
                        .setScale(2, RoundingMode.HALF_UP);
            } else if (brePenaltyBand.getRepairDay3().toString().equals(percentage)) {
                return (brePenaltyBand.getRepairDay3().divide(new BigDecimal(100)).multiply(claim.getInvoice().getRepairGross()))
                        .setScale(2, RoundingMode.HALF_UP);
            }
        }
        
        return BigDecimal.ZERO.setScale(2);
    }

    /*
     *  Returns the BigDecimal value for the mapped string type penalty percentage.
     */
    @Override
    public BigDecimal getHirePenaltyPercentageVal(Claim claim) {

        Invoice inv = claim.getInvoice();
        Date hireStart = (claim.getVehicleHire() != null && claim.getVehicleHire().getHireStart() != null) ? claim.getVehicleHire().getHireStart()
                : (claim.getInvoice() != null && claim.getInvoice().getDateInvoiced() != null) ? claim.getInvoice().getDateInvoiced() : new Date();
        BrePenaltyBand brePenaltyBand = brePenaltyBandService.getBrePenaltyBand(claim, hireStart);

        if (brePenaltyBand != null && inv.getHireNet().compareTo(BigDecimal.ZERO) == 1) {
            int dateDiff = inv.getInvoicedDays();
            if (dateDiff <= brePenaltyBand.getHirePeriodStartDay1() || brePenaltyBand.getHirePeriodStartDay1() <= 0) {
                return BigDecimal.ZERO.setScale(2);
            } else if (dateDiff <= brePenaltyBand.getHirePeriodStartDay2() && brePenaltyBand.getHirePeriodStartDay1() > 0) {
                return brePenaltyBand.getHireDay1();
            } else if (dateDiff <= brePenaltyBand.getHirePeriodStartDay3() && brePenaltyBand.getHirePeriodStartDay2() > 0) {
                return brePenaltyBand.getHireDay2();
            } else if (dateDiff > brePenaltyBand.getHirePeriodStartDay3() && brePenaltyBand.getHirePeriodStartDay3() > 0) {
                return brePenaltyBand.getHireDay3();
            }
        }
        return BigDecimal.ZERO.setScale(2);
    }

    @Override
    public BigDecimal getRepairPenaltyPercentageVal(Claim claim) {

        Invoice inv = claim.getInvoice();
        Date hireStart = (claim.getVehicleHire() != null && claim.getVehicleHire().getHireStart() != null) ? claim.getVehicleHire().getHireStart()
                : (claim.getInvoice() != null && claim.getInvoice().getDateInvoiced() != null) ? claim.getInvoice().getDateInvoiced() : new Date();
        BrePenaltyBand brePenaltyBand = brePenaltyBandService.getBrePenaltyBand(claim, hireStart);

        if (brePenaltyBand != null && inv.getRepairNet().compareTo(BigDecimal.ZERO) == 1) {
            int dateDiff = inv.getInvoicedDays();
            if (dateDiff <= brePenaltyBand.getRepairPeriodStartDay1() || brePenaltyBand.getRepairPeriodStartDay1() <= 0) {
                return BigDecimal.ZERO.setScale(2);
            } else if (dateDiff <= brePenaltyBand.getRepairPeriodStartDay2() && brePenaltyBand.getRepairPeriodStartDay1() > 0) {
                return brePenaltyBand.getRepairDay1();
            } else if (dateDiff <= brePenaltyBand.getRepairPeriodStartDay3() && brePenaltyBand.getRepairPeriodStartDay2() > 0) {
                return brePenaltyBand.getRepairDay2();
            } else if (dateDiff > brePenaltyBand.getRepairPeriodStartDay3() && brePenaltyBand.getRepairPeriodStartDay3() > 0) {
                return brePenaltyBand.getRepairDay3();
            }
        }
        return BigDecimal.ZERO.setScale(2);
    }

    @Override
    public boolean addOnHireTask(Claim claim) {
        BreBand breBand = breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
        if (breBand.isAllowOnHireAutomatedTasks() && claim.getInsurer().isTaskManagementEnable()) {
            Task onHireTask = new Task();
            onHireTask.setClaim(claim);
            onHireTask.setComplete(Boolean.FALSE);
            onHireTask.setDescription("The Hire Start date has been added to this claim, please review.");
            onHireTask.setDueDate(DateHelper.addDay(new Date(), 1));
            onHireTask.setInsurer(Boolean.TRUE);
            onHireTask.setRaisedBy(userService.findByUserName("system"));
            onHireTask.setType("On Hire");
            onHireTask.setVisibility(2);
            onHireTask.setVisibilityRole(WebUserRole.ROLE_INS_CH);

            try {
                taskService.createNewTask(onHireTask);
            } catch (IllegalArgumentException ex) {
                LOG.error("IllegalArgumentException thrown creating On Hire Task for claim Id '{}': {}", claim.getId(), ex.getMessage());
                return false;
            } catch (Exception ex) {
                LOG.error("Exception thrown creating On Hire Task for claim Id '{}': {}", claim.getId(), ex.getMessage());
                return false;
            }
        }
        return true;
    }
}
