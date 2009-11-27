package chox.services;

import chox.Util.RoleHelper;
import chox.model.ChoBand;
import chox.model.ReasonOfRejection;
import chox.model.Chorganisation;
import chox.model.ClaimStatus;
import chox.model.IdLookupItem;
import chox.model.Insurer;
import chox.model.LookupItem;
import chox.model.ReasonOfDelay;
import chox.model.UserWorkgroup;
import chox.model.VehicleClass;
import chox.model.WebUser;
import chox.model.Workgroup;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class LookupServiceImpl extends SecureDataService implements LookupService, Serializable {
    
    public List getStatuses() {
        List items = new ArrayList<LookupItem>();
        for (String s : ClaimStatus.getStatus()) {
            items.add(new LookupItem(s, s));
        }
        return items;
    }

    public List getVehicleClasses() {
        DetachedCriteria criteria = DetachedCriteria.forClass(VehicleClass.class).addOrder(Order.asc("name"));
        return findByCriteria(criteria,true);
    }
    
    public List getClaimRejectionReason(){
        DetachedCriteria criteria = DetachedCriteria.forClass(ReasonOfRejection.class).addOrder(Order.asc("id"));
        criteria.add(Restrictions.eq("type", "Claim"));
        return findByCriteria(criteria,true);
    }
    
    public List getInsurerChoBand(int insurerId){
        DetachedCriteria criteria = DetachedCriteria.forClass(ChoBand.class).addOrder(Order.asc("id"));
        criteria.add(Restrictions.eq("insurer.id", insurerId));
         return findByCriteria(criteria,true);
    }
    
    public List getInvoiceRejectionReason(){
        DetachedCriteria criteria = DetachedCriteria.forClass(ReasonOfRejection.class).addOrder(Order.asc("id"));
        criteria.add(Restrictions.eq("type", "Invoice"));
         return findByCriteria(criteria,true);
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
    
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // LINE OF REASON OF DELAY
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    public List getReasonOfDelay() {
        DetachedCriteria criteria = DetachedCriteria.forClass(ReasonOfDelay.class);
        criteria.add(Restrictions.eq("status", true));
        criteria.addOrder(Order.asc("id"));  
         return findByCriteria(criteria,true);
    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // WORKGROUP LIST
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    public List getWorkgroups(WebUser user, boolean isActiveOnly){
        
        List workgroups = new ArrayList();

        if(RoleHelper.isChoxAdmin(user)){
            workgroups = getAllWorkgroup(isActiveOnly);
        }else{
            
            if(RoleHelper.isInsurerUser(user)){
                
                if(RoleHelper.isGlobalFilterByWorkgroup(user)){
                    workgroups = getWorkgroupsByUserId(user.getId(), isActiveOnly);
                }else{
                    workgroups = getWorkgroupsByInsurerId(user.getInsurer().getId(), isActiveOnly);
                }
            }
        }
        
        return workgroups;

    }

    private List getAllWorkgroup(boolean isActiveOnly){

        DetachedCriteria criteria = DetachedCriteria.forClass(Workgroup.class);

        if(isActiveOnly){
            criteria.add(Restrictions.eq("status", true));
        }

        criteria.addOrder(Order.asc("name"));
        return findByCriteria(criteria,true);
            
    }

    public List getWorkgroupsByInsurerId(int insurerId, boolean isActiveOnly) {
        
        DetachedCriteria criteria = DetachedCriteria.forClass(Workgroup.class);

        if(isActiveOnly){
            criteria.add(Restrictions.eq("status", true));
        }

        if(insurerId>0){
            criteria.add(Restrictions.eq("insurer.id", insurerId));
        }
        
        criteria.addOrder(Order.asc("name"));
        return findByCriteria(criteria,true);
    }

    private List getWorkgroupsByUserId(int userId, boolean isActiveOnly){

        List workgroups = new ArrayList();

        DetachedCriteria criteria = DetachedCriteria.forClass(UserWorkgroup.class);
        criteria.add(Restrictions.eq("user.id", userId));

        List result = findByCriteria(criteria,true);

        for (Object o : result) {
            
            UserWorkgroup a = (UserWorkgroup) o;

            if(isActiveOnly){
                if(a.getWorkgroup().isStatus()){
                    workgroups.add((Workgroup)a.getWorkgroup());
                }
            }else{
                workgroups.add((Workgroup)a.getWorkgroup());
            }
        }

        return workgroups;
    }
    
    public List getNotMyWorkgroups(WebUser user, boolean isActiveOnly){

        List allWorkgroupsByIns = getWorkgroupsByInsurerId(user.getInsurer().getId(), isActiveOnly);
        Set workgroupIds = user.getWorkgroupIds();

        List workgroups = new ArrayList();

        for (Object o : allWorkgroupsByIns) {
            
            Workgroup wg = (Workgroup) o;

            if(!workgroupIds.contains(wg.getId())){
                workgroups.add(wg);
            }

        }
        
        return workgroups;
        
    }


    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // SUPPLIERS / CREDIT HIRES
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    public List getSuppliers() {
        
        WebUser currentUser = getCurrentUser();
        
        if(currentUser.isCHOXAdmin()){
            DetachedCriteria criteria = DetachedCriteria.forClass(Chorganisation.class);
            criteria.add(Restrictions.eq("status", true));
            return findByCriteria(criteria,true);
            
        }else{
            return getSuppliers(currentUser.getInsurer().getId());
        }
    }
    
    public List getSuppliers(Integer insurerId) {

        List<Chorganisation> results = new ArrayList<Chorganisation>();

        try {

            List result = new ArrayList();
            
            StringBuffer sb = new StringBuffer();
            sb.append("select a.id as id, a.name as name from chorganisation ");
            sb.append("a inner join insurer_chorganisation b on a.id = b.chorganisation_id and b.status=true ");
            sb.append("where a.status=true and b.insurer_id=:pInsurerId");
            
            Map extParameters = new HashMap();
            extParameters.put("pInsurerId", insurerId);
            result = externalQuery(sb.toString(), extParameters, IdLookupItem.class);
            
            for(Object o : result){
                IdLookupItem data = (IdLookupItem) o;
                Chorganisation item = new Chorganisation();
                item.setId(data.getId());
                item.setName(data.getName());
                results.add(item);
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return results;
    }

    public List getAllSuppliers() {
        DetachedCriteria criteria = DetachedCriteria.forClass(Chorganisation.class);
        return findByCriteria(criteria,true);
    }    
    
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // INSURERS
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    public List getAllInsurers() {
        DetachedCriteria criteria = DetachedCriteria.forClass(Insurer.class);
        return findByCriteria(criteria,true);
    }
    
    public List getInsurers() {
        
        WebUser currentUser = getCurrentUser();
        
        if(currentUser.isCHOXAdmin()){
            
            DetachedCriteria criteria = DetachedCriteria.forClass(Insurer.class);
            criteria.add(Restrictions.eq("status", true));
            return findByCriteria(criteria,true);
            
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
