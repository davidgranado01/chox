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

    public List<AuditTrail> getFullAuditTrailByClaim(int claimId);

    public AuditTrail getLastChange(int claimId);

    public double getTimeInvoiceWithCHO(int claimId);

    public double getTimeInvoiceWithInsurer(int claimId);

    public double getTimeAwaitingLiabilityResolution(int claimId);
    
    public Boolean revertAuditEntry(int auditTrailId);

}
