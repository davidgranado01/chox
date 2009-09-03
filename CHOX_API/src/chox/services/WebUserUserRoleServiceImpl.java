package chox.services;

import chox.model.IdLookupItem;
import chox.model.WebUser;
import chox.model.WebUserRole;
import chox.model.WebUserUserRole;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class WebUserUserRoleServiceImpl extends SecureDataService implements WebUserUserRoleService {

    public WebUserUserRoleServiceImpl() {
    }
    
    protected UserService userService;
    protected WebUserUserRoleService webUserUserRoleService;
    
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }
    
    public void setWebUserRoleService(WebUserUserRoleService webUserUserRoleService)
    {
        this.webUserUserRoleService = webUserUserRoleService;
    }
    
    public WebUserUserRole getObject(int id) {        
        return (WebUserUserRole) get(WebUserUserRole.class, id);
    }
    
    public String getUserroleName(int id){
        DetachedCriteria criteria = DetachedCriteria.forClass(WebUserRole.class);  
        criteria.add(Restrictions.eq("id", id));
        WebUserRole object = (WebUserRole) getByCriteria(criteria);
        return object.getName();
    }
    
    public List<WebUserUserRole> getUserRoleMapping(Integer webUserId, Integer webUserRoleId){
        
        List<WebUserUserRole> webUserUserRoles = new ArrayList<WebUserUserRole>();
        
        try {
            
            DetachedCriteria criteria = DetachedCriteria.forClass(WebUserUserRole.class);  
            
            if(webUserId!=null && webUserId>0){
                criteria.add(Restrictions.eq("webUser.id", webUserId));
            }
            
            if(webUserRoleId!=null && webUserRoleId>0){
                criteria.add(Restrictions.eq("webUserRole.id", webUserRoleId));
            }            
            
            webUserUserRoles = findByCriteria(criteria);
        
        } catch (Throwable e) {
           e.printStackTrace();
        }    
        
        return webUserUserRoles;
    }
    
    public void updateObject(WebUserUserRole object) {
        try {
            save(object);
        } catch (Throwable e) {
           e.printStackTrace();
        }
    }
    
    public boolean addNewUserRole(int webUserId, int webUserRoleId){
        
        boolean bFlag = false;
        
        try {
            
            WebUserUserRole webUserUserRole = new WebUserUserRole();
            webUserUserRole.setActive(true);
            webUserUserRole.setWebUser(userService.getObject(webUserId));
            webUserUserRole.setWebUserRole(getWebUserRole(webUserRoleId));
            updateObject(webUserUserRole);
            bFlag = true;
            
        } catch (Throwable e) {
           e.printStackTrace();  
        }
        
        return bFlag;

    }
    
    public boolean addBaseNewUserRole(int webUserId, int typeId){
        
        boolean bFlag = false;
        
        try {
            
            WebUserUserRole webUserUserRole = new WebUserUserRole();
            webUserUserRole.setActive(true);
            webUserUserRole.setWebUser(userService.getObject(webUserId));
            webUserUserRole.setWebUserRole(getUserOrgBaseRoleId(typeId));

            updateObject(webUserUserRole);
            bFlag = true;
            
        } catch (Throwable e) {
           e.printStackTrace();
           
        }         
        
        return bFlag;

    }

    private WebUserRole getUserOrgBaseRoleId(int orgTypeId) {
       
        String webUserRoleName = "-";

        switch(orgTypeId){
            case 1: webUserRoleName = WebUserRole.ROLE_CHOX; break;
            case 2: webUserRoleName = WebUserRole.ROLE_INS; break;
            case 3: webUserRoleName = WebUserRole.ROLE_CHO; break;
        }

        return getWebUserRole(webUserRoleName);
        
    }

    public boolean DeleteObject(WebUserUserRole object){
        
        boolean bFlag = false;
        
        try {
            
            
            delete(object);
            
            bFlag = true;
            
        } catch (Throwable e) {
           e.printStackTrace();
        }
        
        return bFlag;
    }
    
    private WebUserRole getWebUserRole(String roleName){
        
        WebUserRole webUserRole = new WebUserRole();
        
        DetachedCriteria criteria = DetachedCriteria.forClass(WebUserRole.class);  
        criteria.add(Restrictions.eq("name", roleName));
        webUserRole = (WebUserRole) getByCriteria(criteria);
        
        return webUserRole;
    }
    
    private WebUserRole getWebUserRole(int webUserRoleId){
        
        WebUserRole webUserRole = new WebUserRole();
        
        DetachedCriteria criteria = DetachedCriteria.forClass(WebUserRole.class);  
        criteria.add(Restrictions.eq("id", webUserRoleId));
        webUserRole = (WebUserRole) getByCriteria(criteria);
        
        return webUserRole;
    } 
    
    public List<WebUserRole> getWebUserroles(int orgTypeId){
    
        List<WebUserRole> webUserRoles = new ArrayList<WebUserRole>();
        
        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(WebUserRole.class); 

            criteria.add(Restrictions.eq("typeId", orgTypeId));
            
            criteria.add(Restrictions.ne("name", WebUserRole.ROLE_CHO));
            criteria.add(Restrictions.ne("name", WebUserRole.ROLE_INS));
            criteria.add(Restrictions.ne("name", WebUserRole.ROLE_CHOX));
            
            webUserRoles = findByCriteria(criteria);
        
        } catch (Throwable e) {
           e.printStackTrace();
        }
        
        return webUserRoles;
        
    }

    public List getWebUserrolesLookupItem(int orgTypeId){
     
        List<WebUserRole> webUserroles = getWebUserroles(orgTypeId);
        List items = new ArrayList<IdLookupItem>();
        
        for (WebUserRole s : webUserroles) {
            items.add(new IdLookupItem(s.getId(), s.getDescription()));
        }        
        
        return items;
    }
    
    public List getSelectedUserAvailableRoleLookupItem(int orgTypeId, Integer webUserId){

        List items = new ArrayList<IdLookupItem>();
        List<WebUserRole> webUserroles = getWebUserroles(orgTypeId);
        List<WebUserUserRole> selectedWebUserroles = getUserRoleMapping(webUserId, null);
        List<Integer> selectedList = new ArrayList<Integer>();

        for (WebUserUserRole o : selectedWebUserroles) {
            selectedList.add(o.getWebUserRole().getId());
        }

        for (WebUserRole s : webUserroles) {
            if(!selectedList.contains(s.getId())){
                items.add(new IdLookupItem(s.getId(), s.getDescription()));
            }
        }

        return items;
    }
    
    public boolean isClaimHandlerRole(int roleId){
        
        boolean isClaimHandler = false;
        WebUserRole webUserRole = new WebUserRole();
        
        DetachedCriteria criteria = DetachedCriteria.forClass(WebUserRole.class);  
        criteria.add(Restrictions.eq("id", roleId));
        criteria.add(Restrictions.eq("name", WebUserRole.ROLE_CH));
        webUserRole = (WebUserRole) getByCriteria(criteria);
        
        if(webUserRole!=null){
            isClaimHandler = true;
        }
        
        return isClaimHandler;
    }
    
}
