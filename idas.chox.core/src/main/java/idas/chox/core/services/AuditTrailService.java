package idas.chox.core.services;

import idas.chox.core.model.AuditTrail;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ReasonOfRejection;
import java.util.List;

public interface AuditTrailService {

    public AuditTrail getAuditTrail(int auditTrailId);

    public Boolean logAuditLog(String newStatus, String oldStatus, Claim thisClaim);

    public Boolean logAuditLogForce(String newStatus, String oldStatus, Claim thisClaim);

    public Boolean logAuditLog(String newStatus, String oldStatus, Claim thisClaim, Integer secInteval);

    public Boolean logAuditLog(String newStatus, Claim thisClaim, ReasonOfRejection claimReasonOfRejection, ReasonOfRejection invoiceReasonOfRejection);

    public Boolean logAuditLog(String newStatus, Claim thisClaim, ReasonOfRejection claimReasonOfRejection, ReasonOfRejection invoiceReasonOfRejection, Integer secInteval);

    public List<AuditTrail> getAuditTrailByClaim(int claimId);

    public List<AuditTrail> getFullAuditTrailByClaim(int claimId, boolean descending);

    public AuditTrail getLastChange(int claimId);

    public double getTimeInvoiceWithCHO(int claimId);

    public int getSubscriberClaimDays(int claimId);

    public int getSubscriberClaimRejectedDays(int claimId);

    public int getSubscriberClaimRejectedTimes(int claimId);
    public int getClaimRejectedTimes(int claimId);

    public String getStateBeforeRejection(int claimId);

    public int getFixedFeeClaimDays(int claimId);

    public int getFixedFeeClaimRejectedDays(int claimId);

    public double getTimeInvoiceWithInsurer(int claimId);

    public double getTimeAwaitingLiabilityResolution(int claimId);
    
    public Boolean revertAuditEntry(int auditTrailId);

    public Boolean hasRevertedEntries(int claimId);
    
    public boolean isSubscriberClaimRejectedAndAgreed(int claimId);
        
    public void deleteAllAuditEntriesByClaimId(int claimId);
    
    public void revertAllAuditEntriesByClaimId(int claimId);

}
