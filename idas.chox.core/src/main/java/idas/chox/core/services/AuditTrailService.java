package idas.chox.core.services;

import idas.chox.core.model.AuditTrail;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ReasonOfRejection;
import java.util.Date;
import java.util.List;

public interface AuditTrailService {

    AuditTrail getAuditTrail(int auditTrailId);

    Boolean logAuditLog(String newStatus, String oldStatus, Claim thisClaim);

    Boolean logAuditLogForce(String newStatus, String oldStatus, Claim thisClaim);

    Boolean logAuditLog(String newStatus, String oldStatus, Claim thisClaim, Integer secInteval);

    Boolean logAuditLog(String newStatus, Claim thisClaim, ReasonOfRejection claimReasonOfRejection, ReasonOfRejection invoiceReasonOfRejection);

    Boolean logAuditLog(String newStatus, Claim thisClaim, ReasonOfRejection claimReasonOfRejection, ReasonOfRejection invoiceReasonOfRejection, Integer secInteval);

    List<AuditTrail> getAuditTrailByClaim(int claimId);

    List<AuditTrail> getFullAuditTrailByClaim(int claimId, boolean descending);

    AuditTrail getLastChange(int claimId);

    double getTimeInvoiceWithCHO(int claimId);

    int getSubscriberClaimDays(int claimId);

    int getSubscriberClaimRejectedDays(int claimId, String cutOffTime);

    int getSubscriberClaimRejectedTimes(int claimId);
    
    int getClaimRejectedTimes(int claimId);

    String getStateBeforeRejection(int claimId);

    int getFixedFeeClaimDays(int claimId);

    int getFixedFeeClaimRejectedDays(int claimId);

    double getTimeInvoiceWithInsurer(int claimId);

    double getTimeAwaitingLiabilityResolution(int claimId);
    
    Boolean revertAuditEntry(int auditTrailId);

    Boolean hasRevertedEntries(int claimId);
    
    boolean isSubscriberClaimRejectedAndAgreed(int claimId);
    
    boolean hasBeenContestedInvoiceReferredToInsurer(int claimId);

    void deleteAllAuditEntriesByClaimId(int claimId);
    
    void revertAllAuditEntriesByClaimId(int claimId);
    
    AuditTrail getAuditTrailByTaskCreatedDate(int claimId, Date taskCreatedDate);

}
