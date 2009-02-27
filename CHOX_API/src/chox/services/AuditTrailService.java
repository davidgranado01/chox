/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.AuditTrail;
import chox.model.Claim;
import chox.model.WebUser;
import java.util.List;

public interface AuditTrailService {
    AuditTrail getObject(int id);
    public Boolean logAuditLog(String newStatus,String oldStatus, Claim thisClaim);
    public Boolean logAuditLog(String newStatus, Claim thisClaim, Integer claimReasonOfRejection, Integer invoiceReasonOfRejection);
    public List<AuditTrail> getAuditTrailByClaim(int claimId);
}
