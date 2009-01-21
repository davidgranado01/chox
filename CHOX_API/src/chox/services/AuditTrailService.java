/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.AuditTrail;
import java.util.List;

public interface AuditTrailService {
    public Boolean saveObj(AuditTrail obj);
    AuditTrail getObject(int id);
    public Boolean logAuditLog(String newStatus, int claimId);
}
