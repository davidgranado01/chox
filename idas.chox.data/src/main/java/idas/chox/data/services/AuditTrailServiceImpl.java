/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.data.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.model.AuditTrail;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ReasonOfRejection;
import idas.chox.core.services.AuditTrailService;
import idas.chox.core.util.DateHelper;
import java.util.Date;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class AuditTrailServiceImpl extends SecureDataService implements AuditTrailService {
    private static final Logger LOG = LoggerFactory.getLogger(AuditTrailServiceImpl.class);

    public AuditTrail getAuditTrail(int auditTrailId) {
        return (AuditTrail) get(AuditTrail.class, auditTrailId);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
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

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public AuditTrail getLastChange(int claimId) {
        AuditTrail auditTrail = null;

        DetachedCriteria criteria = DetachedCriteria.forClass(AuditTrail.class);
        criteria.createCriteria("claim").add(Restrictions.eq("id", claimId));
        criteria.addOrder(Order.desc("id"));
        List<AuditTrail> auditTrailList = findByCriteria(criteria);
        if (auditTrailList.size() > 1) {
            auditTrail = auditTrailList.get(0);
        }
        else
            LOG.warn("Cannot delete audit trail: No audit trail entries found for claim Id={}", claimId);
        return auditTrail;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
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

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
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

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
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

    public List<AuditTrail> getAuditTrailByClaim(int claimId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(AuditTrail.class);
        criteria.createCriteria("claim").add(Restrictions.eq("id", claimId));
        criteria.addOrder(Order.desc("updateDate"));
        return findByCriteria(criteria);

    }

    @Override
    public double getDaysInContestedInvoiceReferredToCHO(int claimId) {
        long noDays = 0;
        // Get the number of days the claim was in the 'ContestedInvoiceReferredToCHO' state.
        LOG.debug("Getting number of days in ContestedInvoiceReferredToCHO");

        List<AuditTrail> auditTrail = getAuditTrailByClaim(claimId);

        // If the current status is 'ContestedInvoiceReferredToCHO', need to take the
        // difference between the day it was put into this state and the current date
        if (ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO.equals(auditTrail.get(0).getNewStatus())) {
            LOG.debug("Claim is referred to CHO and was done so on {} (time={})", auditTrail.get(0).getUpdateDate(), auditTrail.get(0).getUpdateDate().getTime());
            noDays += (new Date()).getTime() - auditTrail.get(0).getUpdateDate().getTime();
            LOG.debug("Claim has been in {} for {} days", ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO, noDays/(24*60*60*1000));
        }

        // Now add any periods when it was previously in this state
        long time = -1;
        for (AuditTrail trail : auditTrail) {
            LOG.debug("Checking trail: status {} to {}", trail.getOriginalStatus(), trail.getNewStatus());
            if (time > 0) {
                if (ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO.equals(trail.getNewStatus())) {
                    LOG.debug("Claim put in state at {}, time={}", trail.getUpdateDate(), trail.getUpdateDate().getTime());
                    long timeInStatus = time - trail.getUpdateDate().getTime();
                    LOG.debug("Claim was in {} for {} days", ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO, timeInStatus/(24*60*60*1000));
                    noDays += timeInStatus;
                }
                else
                    LOG.debug("Error - claim in wrong status: {}", trail.getNewStatus());
                time = -1;
            }
            if (ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO.equals(trail.getOriginalStatus())) {
                time = trail.getUpdateDate().getTime();
                LOG.debug("Found when changed out of state: {}, time={}", trail.getUpdateDate(), time);
            }

        }
        return noDays/(24*60*60*1000.0);
    }
}
