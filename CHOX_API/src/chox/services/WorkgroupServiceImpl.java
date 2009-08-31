package chox.services;

import chox.model.Workgroup;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class WorkgroupServiceImpl extends SecureDataService implements WorkgroupService {

    protected UserWorkgroupService userWorkgroupService;
    protected ClaimService claimService;
    
    public void setUserWorkgroupService(UserWorkgroupService userWorkgroupService) {
        this.userWorkgroupService = userWorkgroupService;
    }
    
    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }
    
    public List<Workgroup> getObjects(int insurerId) {
        
        List<Workgroup> objects = new ArrayList<Workgroup>();
        
        try {
            
            DetachedCriteria criteria = DetachedCriteria.forClass(Workgroup.class);
            
            if(insurerId>0){
                criteria.add(Restrictions.eq("insurer.id", insurerId));
            }
            
            criteria.addOrder(Order.asc("name"));
            
            objects = findByCriteria(criteria);
            
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return objects;
    }

    public Workgroup getObject(int id) {
        return (Workgroup) get(Workgroup.class, id);
    }

    public boolean DeleteObject(Workgroup object) {
        
        boolean bFlag = false;
        
        try {
            
            delete(object);
            
            bFlag = true;
            
        } catch (Throwable e) {
           e.printStackTrace();
        }
        
        return bFlag;
    } 
    
    public void updateObject(Workgroup object) {
        save(object);
    }    
    
    public boolean isWorkgroupExist(int insurerId, String workgroupName){
        
        boolean isExist = false;
        
        try {
            
            DetachedCriteria criteria = DetachedCriteria.forClass(Workgroup.class);

            criteria.add(Restrictions.eq("insurer.id", insurerId));
            criteria.add(Restrictions.eq("name", workgroupName.trim()));
            
            if(findByCriteria(criteria).size()>0){
                isExist = true;
            }
            
        } catch (Throwable e) {
            e.printStackTrace();
        }
         
        return isExist;
        
    }
    
    public boolean isWorkgroupDeletable(int workgroupId){
        
        boolean isExist = false;
        
        try {
            
            if(!this.userWorkgroupService.isObjectExist(workgroupId) && !this.claimService.isObjectExist(workgroupId)){
                isExist = true;
            }
            
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return isExist;        
    }
}
