/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.data.services;

import idas.chox.core.model.AuditTrail;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ReasonOfRejection;
import idas.chox.core.services.AuditTrailService;
import idas.chox.core.util.DateHelper;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class AuditTrailServiceImpl extends SecureDataService implements AuditTrailService {

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
            thisAuditTrail.setUpdateDate(DateHelper.getCurrentTimeStamp());
            thisAuditTrail.setUser(getCurrentUser());
            save(thisAuditTrail);
            bFlag = true;
        }
        return bFlag;

    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Boolean logAuditLog(String newStatus, String oldStatus, Claim thisClaim, Integer secInteval) {

        Boolean bFlag = false;

        if (!oldStatus.trim().equalsIgnoreCase(newStatus.trim())) {

            AuditTrail thisAuditTrail = new AuditTrail();
            thisAuditTrail.setClaim(thisClaim);
            thisAuditTrail.setNewStatus(newStatus);
            thisAuditTrail.setOriginalStatus(oldStatus);

            Timestamp currentDate = DateHelper.getCurrentTimeStamp();
            int sec = DateHelper.getCurrentTimeStamp().getSeconds();
            currentDate.setSeconds(sec + secInteval);

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
            thisAuditTrail.setUpdateDate(DateHelper.getCurrentTimeStamp());
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

            Timestamp currentDate = DateHelper.getCurrentTimeStamp();
            int sec = DateHelper.getCurrentTimeStamp().getSeconds();
            currentDate.setSeconds(sec + secInteval);

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
        criteria.addOrder(Order.asc("updateDate"));
        return findByCriteria(criteria);

    }
}
