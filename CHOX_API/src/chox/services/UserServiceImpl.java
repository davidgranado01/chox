package chox.services;

import chox.Util.RoleHelper;
import chox.model.WebUser;
import chox.model.WebUserRole;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class UserServiceImpl extends DataService implements UserService {

    public UserServiceImpl() {
    }

    public WebUser findByEmail(String email) {

        DetachedCriteria criteria = DetachedCriteria.forClass(WebUser.class).add(Restrictions.eq("email", email).ignoreCase());
        criteria.add(Restrictions.eq("status", true));
        WebUser result = (WebUser) getByCriteria(criteria);
        return result;
    }

    public boolean isEmailExist(String email){
        
        boolean bFlag = true;
        
        DetachedCriteria criteria = DetachedCriteria.forClass(WebUser.class).add(Restrictions.eq("email", email).ignoreCase());
        WebUser result = (WebUser) getByCriteria(criteria);
        
        if(result==null){
            bFlag = false;
        }
        
        return bFlag;
    }
    
    public boolean isEmailExist(String email, int userId){
        
        boolean bFlag = true;
        
        DetachedCriteria criteria = DetachedCriteria.forClass(WebUser.class).add(Restrictions.eq("email", email).ignoreCase());
        criteria.add(Restrictions.ne("id", userId));
        WebUser result = (WebUser) getByCriteria(criteria);
        
        if(result==null){
            bFlag = false;
        }
        
        return bFlag;
    }
    
    public void persist(WebUser user, String emailId) {
        this.save(user);
    }

    public WebUser loadUserByUsername(String s) {
        WebUser u = findByEmail(s);
        return u;
    }

    public WebUser getObject(int id) {
        UserCacheManager cacheManager = UserCacheManager.getInstance();
        WebUser user = cacheManager.getUserFromCache(id);
        if (user == null) {
            user = (WebUser) get(WebUser.class, id);
            cacheManager.putUserToCache(user);
        }

        return user;
    }

    public WebUser getLatestObject(int id) {
        UserCacheManager cacheManager = UserCacheManager.getInstance();
        WebUser user = (WebUser) get(WebUser.class, id);
        cacheManager.putUserToCache(user);
        return user;
    }

    public Long getNumChoActiveUser(Integer choId) {
        String q = "select count(*) from WebUser where status = true and chorganisation.id = " + choId.toString();
        return getCount(q);
    }

    public Long getNumInsActiveUser(Integer insId) {
        String q = "select count(*) from WebUser where status = true and insurer.id = " + insId.toString();
        return getCount(q);
    } 
    
    public List<WebUser> getUsers(){
        
        List<WebUser> users = new ArrayList<WebUser>();
        
        try {
            
            DetachedCriteria criteria = DetachedCriteria.forClass(WebUser.class);
            criteria.addOrder(Order.asc("email"));      
            users = findByCriteria(criteria);
        
        } catch (Throwable e) {
           e.printStackTrace();
        }    
        
        return users;
    } 

    public List<WebUser> getClaimHanldersByInsurerWorkgroup(int insurerId, int selectedWorkgroupId, boolean workgroupEnable){

        List<WebUser> users = new ArrayList<WebUser>();

        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(WebUser.class);
            criteria.add(Restrictions.eq("insurer.id", insurerId));
            criteria.add(Restrictions.eq("status", true));
            criteria.addOrder(Order.asc("firstName"));
            users = findByCriteria(criteria);

        } catch (Throwable e) {
           e.printStackTrace();
        }
        
        List<WebUser> claimHandlers = new ArrayList<WebUser>();

        if(!workgroupEnable){
            
            claimHandlers = users;
            
        }else{
            
            for(WebUser wu : users){
                if(RoleHelper.isCheckSelectedRoleExist(wu.getRoles(), WebUserRole.ROLE_CH) && wu.getWorkgroupIds().contains(selectedWorkgroupId)){
                    claimHandlers.add(wu);
                }
            }
        }
        
        return claimHandlers;
    }

    public List<WebUser> getClaimHanldersByInsurer(int insurerId, boolean workgroupEnable){

        List<WebUser> users = new ArrayList<WebUser>();

        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(WebUser.class);
            criteria.add(Restrictions.eq("insurer.id", insurerId));
            criteria.add(Restrictions.eq("status", true));
            criteria.addOrder(Order.asc("firstName"));
            users = findByCriteria(criteria);

        } catch (Throwable e) {
           e.printStackTrace();
        }

        List<WebUser> claimHandlers = new ArrayList<WebUser>();

        if(!workgroupEnable){

            claimHandlers = users;

        }else{

            for(WebUser wu : users){
                if(RoleHelper.isCheckSelectedRoleExist(wu.getRoles(), WebUserRole.ROLE_CH)){
                    claimHandlers.add(wu);
                }
            }
        }

        return claimHandlers;
    }
    
    public List<WebUser> getUsers(int orgTypeId, int orgId){
        
        List<WebUser> users = new ArrayList<WebUser>();
        
        try {
            
            DetachedCriteria criteria = DetachedCriteria.forClass(WebUser.class);

            if(orgId>0){
                
                if(orgTypeId==2){
                    criteria.add(Restrictions.eq("insurer.id", orgId));
                }else if(orgTypeId==3){
                    criteria.add(Restrictions.eq("chorganisation.id", orgId));
                }
                
            }
            
            criteria.addOrder(Order.asc("email")); 
            
            List<WebUser> userData = findByCriteria(criteria);

            for(WebUser h : userData){
                if(h.getOrganisationType()==(Integer.valueOf(orgTypeId))){
                    users.add(h);
                }
            }
            
        } catch (Throwable e) {
           e.printStackTrace();
        }    
        
        return users;
    } 
    
    public WebUser getUsers(int id){
        
        return (WebUser) get(WebUser.class, id);
    } 
    
    public boolean updateObject(WebUser object) {
        boolean bFlag = false;
        try {
            object.setEmail(object.getEmail().toLowerCase());
            save(object);
            bFlag = true;
            
        } catch (Throwable e) {
            bFlag = false;
            e.printStackTrace();
        }    

        return bFlag;
    }
    
}
