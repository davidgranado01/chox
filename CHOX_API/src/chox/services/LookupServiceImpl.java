/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.services;

import chox.model.ChoBand;
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

    /*
    private WebUserUserRoleService webUserUserRoleService;
    
    public void setWebUserUserRoleService(WebUserUserRoleService webUserUserRoleService)
    {
        this.webUserUserRoleService = webUserUserRoleService;
    }
    */
    
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    public List getStatuses() {
        List items = new ArrayList<LookupItem>();
        for (String s : ClaimStatus.getStatus()) {
            items.add(new LookupItem(s, s));
        }
        return items;
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
    
    public List getInsurerChoBand(int insurerId){
        DetachedCriteria criteria = DetachedCriteria.forClass(ChoBand.class).addOrder(Order.asc("id"));
        criteria.add(Restrictions.eq("insurer.id", insurerId));
        return findByCriteria(criteria);
    }
    
    public List getInvoiceRejectionReason(){
        DetachedCriteria criteria = DetachedCriteria.forClass(ReasonOfRejection.class).addOrder(Order.asc("id"));
        criteria.add(Restrictions.eq("type", "Invoice"));
        return findByCriteria(criteria);
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

    /*
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
                items.add(new IdLookupItem(s.getId(), s.getDescription()));
            }
        }
        
        return items;
    }
    */
    
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // LINE OF BUSINESS LIST
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    public List getLineOfBusinesses() {
        DetachedCriteria criteria = DetachedCriteria.forClass(LineOfBusiness.class);
        criteria.add(Restrictions.eq("active", true));
        criteria.addOrder(Order.asc("name"));  
        return findByCriteria(criteria);
    }

    public List getAllLineOfBusinesses() {
        DetachedCriteria criteria = DetachedCriteria.forClass(LineOfBusiness.class);
        criteria.addOrder(Order.asc("name"));  
        return findByCriteria(criteria);
    }
    
    public List getLineOfBusinessesByInsurerId(int insurerId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(LineOfBusiness.class);
        criteria.add(Restrictions.eq("active", true));
        criteria.add(Restrictions.eq("insurer.id", insurerId));
        criteria.addOrder(Order.asc("name"));  
        return findByCriteria(criteria);
    }
    
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // SUPPLIERS / CREDIT HIRES
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
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
    
    public List getSuppliers(Integer insurerId) {

        List<Chorganisation> results = new ArrayList<Chorganisation>();

        try {

            List result = new ArrayList();

            System.out.println("getSuppliers - START - 0");
            
            StringBuffer sb = new StringBuffer();
            sb.append("select a.id as id, a.name as name from chorganisation ");
            sb.append("a inner join insurer_chorganisation b on a.id = b.chorganisation_id and b.status=true ");
            sb.append("where a.status=true and b.insurer_id=:pInsurerId");
            
            System.out.println("getSuppliers - START - 1");
            
            Map extParameters = new HashMap();
            extParameters.put("pInsurerId", insurerId);
            result = externalQuery(sb.toString(), extParameters, IdLookupItem.class);
            
            System.out.println("getSuppliers - START - 2");
            
            for(Object o : result){
                IdLookupItem data = (IdLookupItem) o;
                Chorganisation item = new Chorganisation();
                item.setId(data.getId());
                item.setName(data.getName());
                results.add(item);
            }
            
            System.out.println("getSuppliers - START - 3");
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return results;
    }

    public List getAllSuppliers() {
        DetachedCriteria criteria = DetachedCriteria.forClass(Chorganisation.class);
        return findByCriteria(criteria);            
    }    
    
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // INSURERS
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    public List getAllInsurers() {
        DetachedCriteria criteria = DetachedCriteria.forClass(Insurer.class);
        return findByCriteria(criteria);
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
    
    public List getInsurers(Integer choId) {
        
        List<Insurer> results = new ArrayList<Insurer>();
         
        try {
            
            List result = new ArrayList();
            
            StringBuffer sb = new StringBuffer();
            sb.append("select a.id as id, a.name as name from insurer ");
            sb.append("a inner join insurer_chorganisation b on a.id = b.insurer_id and b.status=true ");       
            sb.append("where a.status=true and b.chorganisation_id=:pChorganisationId");
            
            Map extParameters = new HashMap();
            extParameters.put("pChorganisationId", choId);
            
            result = externalQuery(sb.toString(), extParameters, IdLookupItem.class);
            
            for(Object o : result){
                IdLookupItem data = (IdLookupItem) o;
                Insurer item = new Insurer();
                item.setId(data.getId());
                item.setName(data.getName());
                results.add(item);
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return results;

    }

}
