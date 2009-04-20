/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.services;

import chox.model.ReasonOfRejection;
import chox.model.Chorganisation;
import chox.model.ClaimStatus;
import chox.model.IdLookupItem;
import chox.model.Insurer;
import chox.model.LineOfBusiness;
import chox.model.LookupItem;
import chox.model.VehicleClass;
import chox.model.WebUser;
import chox.model.WebUserRole;
import chox.model.WebUserUserRole;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Emmanuel
 */
public class LookupServiceImpl extends SecureDataService implements LookupService, Serializable {
    
    private WebUserUserRoleService webUserUserRoleService;
    
    public void setWebUserUserRoleService(WebUserUserRoleService webUserUserRoleService)
    {
        this.webUserUserRoleService = webUserUserRoleService;
    }
    
    public List getStatuses() {
        List items = new ArrayList<LookupItem>();
        for (String s : ClaimStatus.getStatus()) {
            items.add(new LookupItem(s, s));
        }
        return items;
    }

    public List getLineOfBusinesses() {

        DetachedCriteria criteria = DetachedCriteria.forClass(LineOfBusiness.class);
        return findByCriteria(criteria);
    }

    public List getAllActiveSuppliers() {
        DetachedCriteria criteria = DetachedCriteria.forClass(Chorganisation.class);
        criteria.add(Restrictions.eq("status", true));
        return findByCriteria(criteria);            
    }
    
    public List getSelectedUserAvailableRole(int orgTypeId, int webUserId){
        
        List items = new ArrayList<IdLookupItem>();
        
        List<WebUserRole> webUserroles = webUserUserRoleService.getWebUserroles(orgTypeId);
        List<WebUserUserRole> selectedWebUserroles = webUserUserRoleService.getUserRoleMapping(webUserId, null);
        
        List<Integer> selectedList = new ArrayList<Integer>();
        
        for (WebUserUserRole o : selectedWebUserroles) {
            selectedList.add(o.getWebUserRole().getId());
        }
        
        for (WebUserRole s : webUserroles) {
            
            if(!selectedList.contains(s.getId())){
                
                items.add(new IdLookupItem(s.getDescription(),s.getId()));
            }
        }
        
        return items;
    }
    
   
    
    public List getAllActiveInsurers() {
        DetachedCriteria criteria = DetachedCriteria.forClass(Insurer.class);
        criteria.add(Restrictions.eq("status", true));
        return findByCriteria(criteria);
    }
    
    public List getSuppliers() {
        
        WebUser currentUser = getCurrentUser();
                
        if(currentUser.isCHOXAdmin()){
            
            DetachedCriteria criteria = DetachedCriteria.forClass(Chorganisation.class);
            criteria.add(Restrictions.eq("status", true));
            return findByCriteria(criteria);            
            
        }else{
            return getSuppliers(currentUser.getInsurer().getId());
        }
    }

    public List getInsurers() {
        
        WebUser currentUser = getCurrentUser();
        if(currentUser.isCHOXAdmin()){
            DetachedCriteria criteria = DetachedCriteria.forClass(Insurer.class);
            criteria.add(Restrictions.eq("status", true));
            return findByCriteria(criteria);            
        }else{
            return getInsurers(currentUser.getChorganisation().getId());
        }
    }

    public List getVehicleClasses() {
        DetachedCriteria criteria = DetachedCriteria.forClass(VehicleClass.class).addOrder(Order.asc("name"));
        return findByCriteria(criteria);
    }
    
    public List getClaimRejectionReason(){
        
        DetachedCriteria criteria = DetachedCriteria.forClass(ReasonOfRejection.class).addOrder(Order.asc("id"));
        criteria.add(Restrictions.eq("type", "Claim"));
        return findByCriteria(criteria);

    }
    
    public List getInvoiceRejectionReason(){
        DetachedCriteria criteria = DetachedCriteria.forClass(ReasonOfRejection.class).addOrder(Order.asc("id"));
        criteria.add(Restrictions.eq("type", "Invoice"));
        return findByCriteria(criteria);
    }

    public List getSuppliers(Integer insurerId) {

        List<Chorganisation> results = new ArrayList<Chorganisation>();
        
        try {
            
            List result = new ArrayList();
            String query = "select a.id as value, a.name as text from chorganisation a inner join insurer_chorganisation b on a.id = b.chorganisation_id where a.status=true and b.insurer_id=:pInsurerId";
            Map extParameters = new HashMap();
            extParameters.put("pInsurerId", insurerId);
            result = externalQuery(query, extParameters, IdLookupItem.class);
            
            for(Object o : result){
                IdLookupItem data = (IdLookupItem) o;
                Chorganisation item = new Chorganisation();
                item.setId(data.getValue());
                item.setName(data.getText());
                results.add(item);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return results;

    }

    public List getInsurers(Integer choId) {
        
        List<Insurer> results = new ArrayList<Insurer>();
         
        
        try {
            List result = new ArrayList();
            String query = "select a.id as value, a.name as text from insurer a inner join insurer_chorganisation b on a.id = b.insurer_id where a.status=true and b.chorganisation_id=:pChorganisationId";
            Map extParameters = new HashMap();
            extParameters.put("pChorganisationId", choId);
            result = externalQuery(query, extParameters, IdLookupItem.class);
            
            for(Object o : result){
                IdLookupItem data = (IdLookupItem) o;
                Insurer item = new Insurer();
                item.setId(data.getValue());
                item.setName(data.getText());
                results.add(item);
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return results;

    }
    
    public List getNonProvisionReason(){
    
        List items = new ArrayList<LookupItem>();
        
        items.add(new LookupItem("Point Blank Refusal", "Point Blank Refusal"));
        items.add(new LookupItem("Faxed Garage", "Faxed Garage"));
        items.add(new LookupItem("Information Not Available/No System Access", "Info. Not Available/No System Access"));
        items.add(new LookupItem("Non Contactable/Ring Through", "Non Contactable/Ring Through"));
        items.add(new LookupItem("Update Obtained By Other Source", "Update Obtained By Other Source"));
        
        return items;
        
    }

}
