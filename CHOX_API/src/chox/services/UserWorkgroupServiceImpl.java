package chox.services;

import chox.model.UserWorkgroup;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class UserWorkgroupServiceImpl extends SecureDataService implements UserWorkgroupService {
    
    public List<UserWorkgroup> getObjects(int userId) {
        
        List<UserWorkgroup> objects = new ArrayList<UserWorkgroup>();
        
        try {
            
            DetachedCriteria criteria = DetachedCriteria.forClass(UserWorkgroup.class);
            
            if(userId>0){
                criteria.add(Restrictions.eq("webUser.id", userId));
            }
            
            criteria.addOrder(Order.asc("name"));
            
            objects = findByCriteria(criteria);
            
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return objects;
    }

    public UserWorkgroup getObject(int id) {
        return (UserWorkgroup) get(UserWorkgroup.class, id);
    }

    public boolean DeleteObject(UserWorkgroup object) {
        
        boolean bFlag = false;
        
        try {
            
            delete(object);
            
            bFlag = true;
            
        } catch (Throwable e) {
           e.printStackTrace();
        }
        
        return bFlag;
    }
    
    public boolean isObjectExist(int WorkgroupId){
                
        boolean isExist = false;
        
        try {
            
            DetachedCriteria criteria = DetachedCriteria.forClass(UserWorkgroup.class);

            criteria.add(Restrictions.eq("workgroup.id", WorkgroupId));
            
            if(findByCriteria(criteria).size()>0){
                isExist = true;
            }
            
        } catch (Throwable e) {
            e.printStackTrace();
        }
         
        return isExist;      
    }
}
