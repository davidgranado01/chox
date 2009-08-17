/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.ReasonOfDelay;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class ReasonOfDelayServiceImpl extends SecureDataService implements ReasonOfDelayService{

    public ReasonOfDelay getObject(int id) {
        return (ReasonOfDelay) get(ReasonOfDelay.class, id);
    }

    public List<ReasonOfDelay> getReasonOfDelay() {
        
        List<ReasonOfDelay> objects = new ArrayList<ReasonOfDelay>();
        
        try {
            
            DetachedCriteria criteria = DetachedCriteria.forClass(ReasonOfDelay.class);
            criteria.add(Restrictions.eq("status", true));
            criteria.addOrder(Order.asc("id"));  
            objects = findByCriteria(criteria);
        
        } catch (Throwable e) {
           e.printStackTrace();
        }    
        
        return objects;
    }

    public List<ReasonOfDelay> getAllReasonOfDelay() {
        
        List<ReasonOfDelay> objects = new ArrayList<ReasonOfDelay>();
        
        try {
            
            DetachedCriteria criteria = DetachedCriteria.forClass(ReasonOfDelay.class);
            criteria.addOrder(Order.asc("id"));  
            objects = findByCriteria(criteria);
        
        } catch (Throwable e) {
           e.printStackTrace();
        }    
        
        return objects;
    }

}
