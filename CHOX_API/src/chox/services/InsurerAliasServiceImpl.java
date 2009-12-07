/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.services;

import chox.model.Insurer;
import chox.model.InsurerAlias;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;


public class InsurerAliasServiceImpl extends SecureDataService implements InsurerAliasService {
    
    public void createDefaultRecord(Insurer insurer){
        InsurerAlias object = new InsurerAlias();
        object.setInsurer(insurer);
        object.setAliasName(insurer.getName());
        updateObject(object);
    }
    
    public InsurerAlias getInsurerByAliasName(String s) {

        InsurerAlias object = new InsurerAlias();
        
        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(InsurerAlias.class);
            criteria.add(Restrictions.eq("aliasName", s));
            object = (InsurerAlias) getByCriteria(criteria);

        } catch (Throwable e) {
            e.printStackTrace();
        }
        return object;
    }
    
    public boolean isInsurerAliasExist(int insurerId, String AliasName){
        
        boolean bFlag = true;
        
        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(InsurerAlias.class);
            criteria.add(Restrictions.eq("aliasName", AliasName.trim()));
            criteria.add(Restrictions.eq("insurer.id", insurerId));
            
            if(getByCriteria(criteria)==null){
                bFlag = false;
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }
        
        return bFlag;
    }
    
    public List<InsurerAlias> getInsurerAlias(int insurerId) {

        List<InsurerAlias> objects = new ArrayList<InsurerAlias>();
        
        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(InsurerAlias.class);
            
            if(insurerId>0){
                criteria.add(Restrictions.eq("insurer.id", insurerId));
            }
            
            criteria.addOrder(Order.asc("aliasName"));  
            
            objects = findByCriteria(criteria);
            
        } catch (Throwable e) {
            e.printStackTrace();
        }
        
        return objects;
    } 
    
    public boolean DeleteObject(InsurerAlias object){
        
        boolean bFlag = false;
        
        try {
            
            
            delete(object);
            
            bFlag = true;
            
        } catch (Throwable e) {
           e.printStackTrace();
        }
        
        return bFlag;
    }
    
    public InsurerAlias getObject(int id) {
        return (InsurerAlias) get(InsurerAlias.class, id);
    }
    
    public boolean updateObject(InsurerAlias object) {
        
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
