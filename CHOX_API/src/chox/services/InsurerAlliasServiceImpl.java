/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.services;

import chox.model.InsurerAllias;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class InsurerAlliasServiceImpl extends SecureDataService implements InsurerAlliasService {

    public InsurerAllias getInsurerByAlliasName(String s) {

        InsurerAllias insurerallias = new InsurerAllias();
        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(InsurerAllias.class);
            criteria.add(Restrictions.eq("alliasName", s));
            insurerallias = (InsurerAllias) getByCriteria(criteria);

        } catch (Throwable e) {
            e.printStackTrace();
        }
        return insurerallias;
    }
    
    public boolean isInsurerAlliasExist(int insurerId, String AlliasName){
        
        boolean bFlag = true;
        
        InsurerAllias insurerallias = new InsurerAllias();
        
        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(InsurerAllias.class);
            criteria.add(Restrictions.eq("alliasName", AlliasName.trim()));
            criteria.add(Restrictions.eq("insurer.id", insurerId));
            
            if(getByCriteria(criteria)==null){
                bFlag = false;
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }
        
        return bFlag;
    }
    
    public List<InsurerAllias> getInsurerAllias(int insurerId) {

        List<InsurerAllias> insurerallias = new ArrayList<InsurerAllias>();
        
        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(InsurerAllias.class);
            
            if(insurerId>0){
                criteria.add(Restrictions.eq("insurer.id", insurerId));
            }
            
            criteria.addOrder(Order.asc("alliasName"));  
            
            insurerallias = findByCriteria(criteria);
            
        } catch (Throwable e) {
            e.printStackTrace();
        }
        
        return insurerallias;
    } 
    
    public boolean DeleteObject(InsurerAllias object){
        
        boolean bFlag = false;
        
        try {
            
            
            delete(object);
            
            bFlag = true;
            
        } catch (Throwable e) {
           e.printStackTrace();
        }
        
        return bFlag;
    }
    
    public InsurerAllias getObject(int id) {        
        return (InsurerAllias) get(InsurerAllias.class, id);
    }
    
    public boolean updateObject(InsurerAllias object) {
        
        boolean bFlag = false;
        
        try {
            
            save(object);
            bFlag = true;

        } catch (Throwable e) {
            bFlag = false;
            e.printStackTrace();
        }    

        return bFlag;
    }       
}
