/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.Util.DateHelper;
import chox.model.AuditTrail;
import chox.model.Claim;
import chox.model.WebUser;

public class AuditTrailServiceImpl extends DataService implements AuditTrailService{
    
    public Boolean logAuditLog(String newStatus,String oldStatus, Claim thisClaim){
       
        Boolean bFlag = false;
        
        if(!oldStatus.trim().equalsIgnoreCase(newStatus.trim())){
        
            AuditTrail thisAuditTrail = new AuditTrail();
            thisAuditTrail.setClaim(thisClaim);
            thisAuditTrail.setNewStatus(newStatus);
            thisAuditTrail.setOriginalStatus(oldStatus);
            thisAuditTrail.setUpdateDate(DateHelper.getCurrentTimeStamp());
            //thisAuditTrail.setLastModifiedDate(DateHelper.getCurrentTimeStamp());
            //thisAuditTrail.setCreatedDate(DateHelper.getCurrentTimeStamp());
            thisAuditTrail.setUser(getCurrentUser());
            //thisAuditTrail.setCreatedBy(getCurrentUser());
            //thisAuditTrail.setLastModifiedBy(getCurrentUser());
            save(thisAuditTrail);
            bFlag = true;
        }
        return bFlag;
               
    } 
    
    public Boolean logAuditLog(String newStatus, Claim thisClaim){
       
        Boolean bFlag = false;
        
        if(!thisClaim.getStatus().trim().equalsIgnoreCase(newStatus.trim())){
        
            AuditTrail thisAuditTrail = new AuditTrail();
            thisAuditTrail.setClaim(thisClaim);
            thisAuditTrail.setNewStatus(newStatus);
            thisAuditTrail.setOriginalStatus(thisClaim.getStatus());
            thisAuditTrail.setUpdateDate(DateHelper.getCurrentTimeStamp());
            //thisAuditTrail.setLastModifiedDate(DateHelper.getCurrentTimeStamp());
            //thisAuditTrail.setCreatedDate(DateHelper.getCurrentTimeStamp());
            thisAuditTrail.setUser(getCurrentUser());
            //thisAuditTrail.setCreatedBy(getCurrentUser());
            //thisAuditTrail.setLastModifiedBy(getCurrentUser());
            save(thisAuditTrail);
            bFlag = true;
        }
        return bFlag;
               
    } 
        
    public AuditTrail getObject(int id) {
        return (AuditTrail) get(AuditTrail.class, id);
    }    
}
