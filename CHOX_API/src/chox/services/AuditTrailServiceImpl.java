/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.Util.DateHelper;
import chox.model.AuditTrail;
import chox.model.Claim;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class AuditTrailServiceImpl extends SecureDataService implements AuditTrailService{
    
    public Boolean logAuditLog(String newStatus, String oldStatus, Claim thisClaim){
       
        Boolean bFlag = false;
        
        if(!oldStatus.trim().equalsIgnoreCase(newStatus.trim())){
        
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
    
    public Boolean logAuditLog(String newStatus, Claim thisClaim, Integer claimReasonOfRejection, Integer invoiceReasonOfRejection){
       
        Boolean bFlag = false;
        
        if(!thisClaim.getStatus().trim().equalsIgnoreCase(newStatus.trim())){
        
            AuditTrail thisAuditTrail = new AuditTrail();
            thisAuditTrail.setClaim(thisClaim);
            thisAuditTrail.setNewStatus(newStatus);
            thisAuditTrail.setOriginalStatus(thisClaim.getStatus());
            thisAuditTrail.setUpdateDate(DateHelper.getCurrentTimeStamp());
            thisAuditTrail.setUser(getCurrentUser());
            
            if(claimReasonOfRejection!=null){
                thisAuditTrail.setClaimReasonOfRejection(claimReasonOfRejection);
            }
            
            if(invoiceReasonOfRejection!=null){
                thisAuditTrail.setInvoiceReasonOfRejection(invoiceReasonOfRejection);
            }
            
            save(thisAuditTrail);
            bFlag = true;
        }
        return bFlag;
               
    } 
        
    public AuditTrail getObject(int id) {
        return (AuditTrail) get(AuditTrail.class, id);
    }  
    
    public List<AuditTrail> getAuditTrailByClaim(int claimId) {

        List auditTrails = new ArrayList<AuditTrail>();
        
        try {
            
            DetachedCriteria criteria = DetachedCriteria.forClass(AuditTrail.class);
            criteria.createCriteria("claim").add(Restrictions.eq("id", claimId));
            criteria.addOrder(Order.asc("updateDate"));
            auditTrails = findByCriteria(criteria);
        
        } catch (Throwable e) {
           e.printStackTrace();
        }    
        
        return auditTrails;

    }
    
}
