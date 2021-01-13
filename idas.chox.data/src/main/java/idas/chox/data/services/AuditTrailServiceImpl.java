package idas.chox.data.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.AuditTrail;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Entity;
import idas.chox.core.model.ReasonOfRejection;
import idas.chox.core.services.AuditTrailService;
import idas.chox.core.util.DateHelper;
import java.util.HashMap;
import java.util.Map;

public class AuditTrailServiceImpl extends SecureDataService implements AuditTrailService {

    private static final Logger LOG = LoggerFactory.getLogger(AuditTrailServiceImpl.class);
    private static final Collection rejectedStatuses = Arrays.asList(new String[] {
                                        ClaimStatus.CLAIM_REJECTED,
                                        ClaimStatus.CLAIM_REJECTION_CONTESTED,
                                        ClaimStatus.SUBSCRIBER_CLAIM_REJECTED});
    
    @Override
    public AuditTrail getAuditTrail(int auditTrailId) {
        return (AuditTrail) get(AuditTrail.class, auditTrailId);
    }

    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public Boolean logAuditLog(String newStatus, String oldStatus, Claim thisClaim) {

        Boolean bFlag = false;

        if (!oldStatus.trim().equalsIgnoreCase(newStatus.trim())) {

            AuditTrail thisAuditTrail = new AuditTrail();
            thisAuditTrail.setClaim(thisClaim);
            thisAuditTrail.setNewStatus(newStatus);
            thisAuditTrail.setOriginalStatus(oldStatus);
            thisAuditTrail.setUpdateDate(DateHelper.getCurrentDateTime());
            thisAuditTrail.setUser(getCurrentUser());
            save(thisAuditTrail);
            bFlag = true;
        }
        return bFlag;

    }


    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public Boolean logAuditLogForce(String newStatus, String oldStatus, Claim thisClaim) {

        AuditTrail thisAuditTrail = new AuditTrail();
        thisAuditTrail.setClaim(thisClaim);
        thisAuditTrail.setNewStatus(newStatus);
        thisAuditTrail.setOriginalStatus(oldStatus);
        thisAuditTrail.setUpdateDate(DateHelper.getCurrentDateTime());
        thisAuditTrail.setUser(getCurrentUser());
        save(thisAuditTrail);

        return true;

    }


    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public AuditTrail getLastChange(int claimId) {
        AuditTrail auditTrail = null;

        DetachedCriteria criteria = DetachedCriteria.forClass(AuditTrail.class);
        criteria.add(Restrictions.eq("reverted", false));
        criteria.createCriteria("claim").add(Restrictions.eq("id", claimId));
        criteria.addOrder(Order.desc("id"));
        List<AuditTrail> auditTrailList = findByCriteria(criteria);
        if (auditTrailList.size() > 0) {
            auditTrail = auditTrailList.get(0);
        } else {
            LOG.warn("Cannot delete audit trail: No audit trail entries found for claim Id={}", claimId);
        }
        return auditTrail;
    }


    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public Boolean logAuditLog(String newStatus, String oldStatus, Claim thisClaim, Integer secInteval) {

        Boolean bFlag = false;

        if (!oldStatus.trim().equalsIgnoreCase(newStatus.trim())) {

            AuditTrail thisAuditTrail = new AuditTrail();
            thisAuditTrail.setClaim(thisClaim);
            thisAuditTrail.setNewStatus(newStatus);
            thisAuditTrail.setOriginalStatus(oldStatus);

            Date currentDate = DateHelper.getCurrentDateTime();
            currentDate.setTime(currentDate.getTime() + secInteval);

            thisAuditTrail.setUpdateDate(currentDate);
            thisAuditTrail.setUser(getCurrentUser());
            save(thisAuditTrail);
            bFlag = true;
        }
        return bFlag;

    }


    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public Boolean logAuditLog(String newStatus, Claim thisClaim, ReasonOfRejection claimReasonOfRejection, ReasonOfRejection invoiceReasonOfRejection) {

        Boolean bFlag = false;

        if (!thisClaim.getStatus().trim().equalsIgnoreCase(newStatus.trim())) {

            AuditTrail thisAuditTrail = new AuditTrail();
            thisAuditTrail.setClaim(thisClaim);
            thisAuditTrail.setNewStatus(newStatus);
            thisAuditTrail.setOriginalStatus(thisClaim.getStatus());
            thisAuditTrail.setUpdateDate(DateHelper.getCurrentDateTime());
            thisAuditTrail.setUser(getCurrentUser());

            if (claimReasonOfRejection != null) {
                thisAuditTrail.setClaimReasonOfRejection(claimReasonOfRejection);
            }

            if (invoiceReasonOfRejection != null) {
                thisAuditTrail.setInvoiceReasonOfRejection(invoiceReasonOfRejection);
            }

            save(thisAuditTrail);
            bFlag = true;
        }
        return bFlag;

    }


    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public Boolean logAuditLog(String newStatus, Claim thisClaim, ReasonOfRejection claimReasonOfRejection, ReasonOfRejection invoiceReasonOfRejection, Integer secInteval) {

        Boolean bFlag = false;

        if (!thisClaim.getStatus().trim().equalsIgnoreCase(newStatus.trim())) {

            Date currentDate = DateHelper.getCurrentDateTime();
            currentDate.setTime(currentDate.getTime() + secInteval);

            AuditTrail thisAuditTrail = new AuditTrail();
            thisAuditTrail.setClaim(thisClaim);
            thisAuditTrail.setNewStatus(newStatus);
            thisAuditTrail.setOriginalStatus(thisClaim.getStatus());
            thisAuditTrail.setUpdateDate(currentDate);
            thisAuditTrail.setUser(getCurrentUser());

            if (claimReasonOfRejection != null) {
                thisAuditTrail.setClaimReasonOfRejection(claimReasonOfRejection);
            }

            if (invoiceReasonOfRejection != null) {
                thisAuditTrail.setInvoiceReasonOfRejection(invoiceReasonOfRejection);
            }

            save(thisAuditTrail);
            bFlag = true;
        }
        return bFlag;

    }


    @Override
    public List<AuditTrail> getAuditTrailByClaim(int claimId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(AuditTrail.class);
        criteria.add(Restrictions.eq("reverted", false));
        criteria.createCriteria("claim").add(Restrictions.eq("id", claimId));
        criteria.addOrder(Order.desc("updateDate"));
        criteria.addOrder(Order.desc("id"));
        return findByCriteria(criteria);

    }

    private List<AuditTrail> getReconstructedAuditTrailByClaim(int claimId) {
        List<AuditTrail> auditTrail = getFullAuditTrailByClaim(claimId, false);
        List<AuditTrail> results = new ArrayList<>();
        
        for (AuditTrail trail : auditTrail) {
            results.add(trail);
            if (trail.getReverted() && !trail.getOriginalStatus().isEmpty()) {
                AuditTrail newEntry = new AuditTrail();
                newEntry.setOriginalStatus(trail.getNewStatus());
                newEntry.setNewStatus(trail.getOriginalStatus());
                newEntry.setUpdateDate(trail.getLastModifiedDate());
                results.add(newEntry);
            }
        }
        
        Collections.sort(results, AuditTrail.UPDATECOMPARATOR);
        
        return results;
    }
    
    @Override
    public List<AuditTrail> getFullAuditTrailByClaim(int claimId, boolean descending) {
        DetachedCriteria criteria = DetachedCriteria.forClass(AuditTrail.class);
        criteria.createCriteria("claim").add(Restrictions.eq("id", claimId));
        if (descending) {
            criteria.addOrder(Order.desc("updateDate"));
            criteria.addOrder(Order.desc("id"));
       }
        else {
            criteria.addOrder(Order.asc("updateDate"));
            criteria.addOrder(Order.asc("id"));
        }
        return findByCriteria(criteria);

    }


    @Override
    public double getTimeInvoiceWithInsurer(int claimId) {
        return timeClaimInStatus(claimId, ClaimStatus.getInvoiceWithInsurerStatusList());
    }


    @Override
    public double getTimeInvoiceWithCHO(int claimId) {
        return timeClaimInStatus(claimId, ClaimStatus.getInvoiceWithCHOStatusList());
    }


    @Override
    public double getTimeAwaitingLiabilityResolution(int claimId) {
        return timeClaimInStatus(claimId, ClaimStatus.getAwaitingLiabilityStatusList());
    }


    @Override
    public int getSubscriberClaimDays(int claimId, boolean ignoreBankHolidays) {
        
        return daysInStatuses(claimId, Arrays.asList(new String[] {ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED,
                                           ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED,
                                           ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED,
                                           ClaimStatus.CLAIM_PENDING,
                                           ClaimStatus.CLAIM_REFERRED_TO_FNOL,
                                           ClaimStatus.CLAIM_REF_TO_ENG,
                                           ClaimStatus.CLAIM_UPDATE_BY_ENG,
                                           ClaimStatus.CLAIM_REJECTION_CONTESTED}), ignoreBankHolidays);
    }

    @Override
    public int getFixedFeeClaimDays(int claimId, boolean ignoreBankHolidays) {
        
        return getSubscriberClaimDays(claimId, ignoreBankHolidays);
    }

    private int daysInStatuses(int claimId, Collection<String> statuses, boolean ignoreBankHolidays) {
        LOG.debug("Calculating days claim {} in statuses '{}'", claimId, statuses);
        int days = 0;
        
        List<AuditTrail> auditTrail = getReconstructedAuditTrailByClaim(claimId);

        Date dateInStatus = null;
        int lastDayCounted = 0;
        for (AuditTrail trail : auditTrail) {
            LOG.debug("Start: Processed entry {} -> {} @ {} : days so far={}",
                    new Object[] {trail.getOriginalStatus(), trail.getNewStatus(),
                                  trail.getUpdateDate(), days});
            if (dateInStatus == null && statuses.contains(trail.getNewStatus())) {
                LOG.debug("Starting timer for status: {}", trail.getNewStatus());
                dateInStatus = trail.getUpdateDate();
            }
            else if (dateInStatus != null && !statuses.contains(trail.getNewStatus())) {

                // Determine no days claim was in status
                
                Calendar cal = Calendar.getInstance();
                cal.setTime(dateInStatus);
                int dayInStatus = cal.get(Calendar.DAY_OF_YEAR);

                cal.setTime(trail.getUpdateDate());
                int dayOutStatus = cal.get(Calendar.DAY_OF_YEAR);
                LOG.debug("dayInStatus={}, dayOutStatus={}, lastDayCounted={}", new Object[] {dayInStatus, dayOutStatus, lastDayCounted});
                if (!(lastDayCounted == dayInStatus && dayInStatus == dayOutStatus)) {
                    days += DateHelper.getNumberOfDaysBetween(dateInStatus, trail.getUpdateDate()) + 1;
                }
                if (ignoreBankHolidays) {
                    days -= getNoBankHolidays(dateInStatus, trail.getUpdateDate());
                }
                lastDayCounted = dayOutStatus;
                dateInStatus = null;   
            }
            LOG.debug("End: Processed entry {} -> {} @ {} : days so far={}",
                    new Object[] {trail.getOriginalStatus(), trail.getNewStatus(),
                                  trail.getUpdateDate(), days});
        }
        
        if (dateInStatus != null) {
            // We must currently be in the status, so count days until now()
            LOG.debug("Adding days from last status change {} until now: {}", dateInStatus, DateHelper.getNumberOfDaysBetween(dateInStatus, new Date()));
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateInStatus);
            int dayInStatus=cal.get(Calendar.DAY_OF_YEAR);
            if (lastDayCounted != dayInStatus) {
                days += DateHelper.getNumberOfDaysBetween(dateInStatus, new Date()) + 1;
            }
            else {
                days += DateHelper.getNumberOfDaysBetween(dateInStatus, new Date());
            }
            if (ignoreBankHolidays) {
                days -= getNoBankHolidays(dateInStatus, new Date());
            }
        }

        LOG.debug("Claim {} in statuses for {} days", claimId, days);

        return days;
   }

    private int getNoBankHolidays(Date startDate, Date endDate) {
        Map<String, Object> extParameters = new HashMap();
        extParameters.put("pStartDate", startDate);
        extParameters.put("pEndDate", endDate);
        int noBankHolidays =  externalQueryCount("select count(*) from bank_holidays where bank_holiday >= :pStartDate and bank_holiday <= :pEndDate", extParameters);
        LOG.debug("Returning {} bank holidays between '{}' and '{}'", new Object[]{noBankHolidays, startDate, endDate});
        return noBankHolidays;
    }


    private double timeClaimInStatus(int claimId, List<String> statuses) {
        long noDays = 0;
        List<AuditTrail> auditTrail = getAuditTrailByClaim(claimId);

        // First calculate the time in its current status, if the current status is one we are interested in
        if (statuses.contains(auditTrail.get(0).getNewStatus())) {
            LOG.debug("Claim is referred to CHO and was done so on {} (time={})", auditTrail.get(0).getUpdateDate(), auditTrail.get(0).getUpdateDate().getTime());
            noDays += (new Date()).getTime() - auditTrail.get(0).getUpdateDate().getTime();
            LOG.debug("Claim has been in {} for {} days", auditTrail.get(0).getNewStatus(), noDays / (24 * 60 * 60 * 1000));
        }

        // Now add any periods when it was previous states of interest
        long time = -1;
        for (AuditTrail trail : auditTrail) {
            LOG.debug("Checking trail: status {} to {}", trail.getOriginalStatus(), trail.getNewStatus());
            if (time > 0) {
                if (statuses.contains(trail.getNewStatus())) {
                    LOG.debug("Claim put in state at {}, time={}", trail.getUpdateDate(), trail.getUpdateDate().getTime());
                    long timeInStatus = time - trail.getUpdateDate().getTime();
                    LOG.debug("Claim was in {} for {} days", trail.getNewStatus(), timeInStatus / (24 * 60 * 60 * 1000));
                    noDays += timeInStatus;
                } else {
                    LOG.debug("Error - claim in wrong status: {}", trail.getNewStatus());
                }
                time = -1;
            }
            if (statuses.contains(trail.getOriginalStatus())) {
                time = trail.getUpdateDate().getTime();
                LOG.debug("Found when changed out of state: {}, time={}", trail.getUpdateDate(), time);
            }
        }

        return noDays / (24 * 60 * 60 * 1000.0);
    }


    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    public Boolean revertAuditEntry(int auditTrailId) {
        LOG.debug("Reverting id={}", auditTrailId);
        AuditTrail auditTrail = (AuditTrail) get(AuditTrail.class, auditTrailId);
        auditTrail.setReverted(true);
        this.save(auditTrail);
        LOG.debug("Claim reverted - audit entry {} reverted", auditTrailId);
        return Boolean.TRUE;
    }


    @Override
    public Boolean hasRevertedEntries(int claimId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(AuditTrail.class);
        criteria.add(Restrictions.eq("reverted", true));
        criteria.createCriteria("claim").add(Restrictions.eq("id", claimId));
        List<Object> entries = findByCriteria(criteria);
        return (entries == null ? false : (entries.size() > 0));
    }


    @Override
    public void deleteAllAuditEntriesByClaimId(int claimId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(AuditTrail.class);
        criteria.createCriteria("claim").add(Restrictions.eq("id", claimId));
        List<Entity> entries = findByCriteria(criteria);
        if (entries.size() > 0) {
            this.deleteAll(entries);
        }
    }

    @Override
    public String getStateBeforeRejection(int claimId) {
        
        
        List<AuditTrail> auditTrail = getFullAuditTrailByClaim(claimId, true);
        for (AuditTrail trail : auditTrail) {
            if (!trail.getReverted() && !rejectedStatuses.contains(trail.getOriginalStatus())) {
                return trail.getOriginalStatus();
            }
        }
        
        return null;
    }

    @Override
    public boolean isSubscriberClaimRejectedAndAgreed(int claimId) {
        List<AuditTrail> auditTrail = getFullAuditTrailByClaim(claimId, true);
        for (AuditTrail trail : auditTrail) {
            if (!trail.getReverted() && ClaimStatus.SUBSCRIBER_CLAIM_REJECTED.equals(trail.getOriginalStatus())
                && ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO.equals(trail.getNewStatus())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int getSubscriberClaimRejectedDays(int claimId, String cutOffTime, boolean ignoreBankHolidays) {
        int days = daysInStatuses(claimId, Arrays.asList(new String[] {ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED,
                                           ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED,
                                           ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED,
                                           ClaimStatus.CLAIM_PENDING,
                                           ClaimStatus.CLAIM_REFERRED_TO_FNOL,
                                           ClaimStatus.CLAIM_REF_TO_ENG,
                                           ClaimStatus.CLAIM_UPDATE_BY_ENG,
                                           ClaimStatus.CLAIM_REJECTION_CONTESTED}), ignoreBankHolidays);
        
        // Now if the claim was rejected AFTER 3pm and it
        // was uploaded on a different day then we need to add another day
        if (isSubscriberClaimRejectedAfterCutOff(claimId, cutOffTime)) {
            days += 1;
        }
        return days;
    }

    @Override
    public int getFixedFeeClaimRejectedDays(int claimId, boolean ignoreBankHolidays) {
        int days = daysInStatuses(claimId, Arrays.asList(new String[] {ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED,
                                           ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED,
                                           ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED,
                                           ClaimStatus.CLAIM_PENDING,
                                           ClaimStatus.CLAIM_REFERRED_TO_FNOL,
                                           ClaimStatus.CLAIM_REF_TO_ENG,
                                           ClaimStatus.CLAIM_UPDATE_BY_ENG,
                                           ClaimStatus.CLAIM_REJECTION_CONTESTED}), ignoreBankHolidays);
        
        // Now if the claim was rejected AFTER 3pm and it
        // was uploaded on a different day then we need to add another day
        if (isFixedFeeClaimRejectedAfter3pm(claimId)) {
            days += 1;
        }
        return days;
    }

    @Override
    public int getSubscriberClaimRejectedTimes(int claimId) {
        int noTimesRejected = 0;
        List<AuditTrail> auditTrail = getFullAuditTrailByClaim(claimId, false);
        for (AuditTrail trail : auditTrail) {
            if (!trail.getReverted() && ClaimStatus.SUBSCRIBER_CLAIM_REJECTED.equals(trail.getNewStatus())) {
                noTimesRejected++;
            }
        }
        
        return noTimesRejected;
    }

    @Override
    public int getClaimRejectedTimes(int claimId) {
        int noTimesRejected = 0;
        List<AuditTrail> auditTrail = getFullAuditTrailByClaim(claimId, false);
        for (AuditTrail trail : auditTrail) {
            if (!trail.getReverted() && ClaimStatus.CLAIM_REJECTED.equals(trail.getNewStatus())) {
                noTimesRejected++;
            }
        }
        
        return noTimesRejected;
    }

    private boolean isSubscriberClaimRejectedAfterCutOff(int claimId, String cutOffTime) {
        Scanner in = new Scanner(cutOffTime).useDelimiter(":");
        int cutOffHour = in.nextInt();
        int cutOffMinute = in.nextInt();
        
        List<AuditTrail> auditTrail = getFullAuditTrailByClaim(claimId, false);
        Calendar cal = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        Date uploadDate = null;
        for (AuditTrail trail : auditTrail) {
            cal.setTime(trail.getUpdateDate());
            if (trail.getOriginalStatus().isEmpty()) {
                uploadDate = trail.getUpdateDate();
                cal2.setTime(trail.getUpdateDate());
            }
            if (!trail.getReverted() && ClaimStatus.SUBSCRIBER_CLAIM_REJECTED.equals(trail.getNewStatus())
                && (cal.get(Calendar.HOUR_OF_DAY) > cutOffHour || (cal.get(Calendar.HOUR_OF_DAY) == cutOffHour && cal.get(Calendar.MINUTE) >= cutOffMinute))) {
                return !(DateHelper.setStartOfDay(uploadDate).equals(DateHelper.setStartOfDay(trail.getUpdateDate()))
                        && (cal2.get(Calendar.HOUR_OF_DAY) > cutOffHour || (cal2.get(Calendar.HOUR_OF_DAY) == cutOffHour && cal2.get(Calendar.MINUTE) >= cutOffMinute)));
            }
        }
        
        return false;
    }

        
    private boolean isFixedFeeClaimRejectedAfter3pm(int claimId) {
        List<AuditTrail> auditTrail = getFullAuditTrailByClaim(claimId, false);
        Calendar cal = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        Date uploadDate = null;
        for (AuditTrail trail : auditTrail) {
            cal.setTime(trail.getUpdateDate());
            if (trail.getOriginalStatus().isEmpty()) {
                uploadDate = trail.getUpdateDate();
                cal2.setTime(trail.getUpdateDate());
            }
            if (!trail.getReverted() && ClaimStatus.CLAIM_REJECTED.equals(trail.getNewStatus())
                && cal.get(Calendar.HOUR_OF_DAY) >= 15) {
                return !(DateHelper.setStartOfDay(uploadDate).equals(DateHelper.setStartOfDay(trail.getUpdateDate()))
                        && cal2.get(Calendar.HOUR_OF_DAY) >= 15);
            }
        }
        
        return false;
    }

    @Override
    public void revertAllAuditEntriesByClaimId(int claimId) {
        try {
            // get audit entries where it has not been reverted earlier.
            List<AuditTrail> revertAuditTrails = getAuditTrailByClaim(claimId);
            for (AuditTrail revertAudit : revertAuditTrails) {
                revertAudit.setReverted(true);
                LOG.debug("audit entry {} reverted", revertAudit.getId());
            }
            this.saveCollections(revertAuditTrails);
        } catch (Exception ex) {
            LOG.error("Exception thrown when reverting all auditTrail entries", ex);
        }
    }
    
    @Override
    public AuditTrail getAuditTrailByTaskCreatedDate(int claimId, Date taskCreatedDate) {

        DetachedCriteria criteria = DetachedCriteria.forClass(AuditTrail.class);
        criteria.createCriteria("claim").add(Restrictions.eq("id", claimId));

        DetachedCriteria auditCreatedDateCriteria = DetachedCriteria.forClass(AuditTrail.class);
        auditCreatedDateCriteria.setProjection(Projections.max("createdDate"));
        auditCreatedDateCriteria.createCriteria("claim").add(Restrictions.eq("id", claimId));
        auditCreatedDateCriteria.add(Restrictions.disjunction()
                .add(Restrictions.conjunction()
                .add(Restrictions.le("createdDate", taskCreatedDate))
                .add(Restrictions.eq("reverted", false)))
                .add(Restrictions.conjunction()
                .add(Restrictions.le("createdDate", taskCreatedDate))
                .add(Restrictions.eq("reverted", true))
                .add(Restrictions.gt("lastModifiedDate", taskCreatedDate))));

        criteria.add(Subqueries.propertyEq("createdDate", auditCreatedDateCriteria));

        List<AuditTrail> auditTrails = findByCriteria(criteria);
        if (auditTrails == null || auditTrails.isEmpty()) {
            LOG.warn("No auditTrail entry returned when retriving AuditTrail By Task CreatedDate: claimId='{}', task created date='{}'", claimId, taskCreatedDate);
            return null;
        }
        return auditTrails.get(0);
    }

    @Override
    public boolean hasBeenContestedInvoiceReferredToInsurer(int claimId) {
        List<AuditTrail> auditTrail = getFullAuditTrailByClaim(claimId, true);
        for (AuditTrail trail : auditTrail) {
            if (!trail.getReverted() && ClaimStatus.CONTESTED_INVOICE_REF_TO_INS.equals(trail.getNewStatus())) {
                return true;
            }
        }

        return false;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void saveOrUpdateAuditTrail(AuditTrail auditTrail) {
        this.save(auditTrail);
    }
}
