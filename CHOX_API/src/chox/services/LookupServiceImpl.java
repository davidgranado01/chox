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

    public List getSuppliers() {
        WebUser currentUser = getCurrentUser();
        return getSuppliers(currentUser.getInsurer().getId());
        // DetachedCriteria criteria = DetachedCriteria.forClass(Chorganisation.class);
        // return findByCriteria(criteria);
    }

    public List getInsurers() {
        
        WebUser currentUser = getCurrentUser();
        return getInsurers(currentUser.getChorganisation().getId());
        /*
        DetachedCriteria criteria = DetachedCriteria.forClass(Insurer.class);
        return findByCriteria(criteria);
        */
         
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
            String query = "select a.id as value, a.name as text from chorganisation a inner join insurer_chorganisation b on a.id = b.chorganisation_id where b.insurer_id=:pInsurerId";
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
            String query = "select a.id as value, a.name as text from insurer a inner join insurer_chorganisation b on a.id = b.insurer_id where b.chorganisation_id=:pChorganisationId";
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
