/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.services;

import chox.model.InsurerChorganisation;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class InsurerChorganisationServiceImpl extends SecureDataService implements InsurerChorganisationService {
    
    public List<InsurerChorganisation> getInsurerChorganisationByInsurer(int insurerId) {

        List<InsurerChorganisation> objects = new ArrayList<InsurerChorganisation>();
        
        try {
            
            DetachedCriteria criteria = DetachedCriteria.forClass(InsurerChorganisation.class);
            criteria.add(Restrictions.eq("insurer.id", insurerId));
            criteria.add(Restrictions.eq("status", true));
            objects = findByCriteria(criteria);
            
        } catch (Throwable e) {
            e.printStackTrace();
        }
        
        return objects;
    } 
    
    public List<InsurerChorganisation> getInsurerChorganisationByChorganisation(int chorganisationId) {

        List<InsurerChorganisation> objects = new ArrayList<InsurerChorganisation>();
        
        try {
            
            DetachedCriteria criteria = DetachedCriteria.forClass(InsurerChorganisation.class);
            criteria.add(Restrictions.eq("chorganisation.id", chorganisationId));
            criteria.add(Restrictions.eq("status", true));
            objects = findByCriteria(criteria);
            
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return objects;
    } 
    
    public InsurerChorganisation getInsurerChorganisationObject(int insurerId, int chorganisationId) {

        InsurerChorganisation object = new InsurerChorganisation();
        
        try {
            
            DetachedCriteria criteria = DetachedCriteria.forClass(InsurerChorganisation.class);
            criteria.add(Restrictions.eq("insurer.id", insurerId));
            criteria.add(Restrictions.eq("chorganisation.id", chorganisationId));
            object = (InsurerChorganisation) getByCriteria(criteria);
            
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return object;
    } 
    
    public List<InsurerChorganisation> getInsurerChorganisation(int insurerId, int chorganisationId) {

        List<InsurerChorganisation> objects = new ArrayList<InsurerChorganisation>();
        
        try {
            
            DetachedCriteria criteria = DetachedCriteria.forClass(InsurerChorganisation.class);
            criteria.add(Restrictions.eq("insurer.id", insurerId));
            criteria.add(Restrictions.eq("chorganisation.id", chorganisationId));
            objects = findByCriteria(criteria);
            
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return objects;
    } 
    
    public boolean isInactiveInsurerChorganisationExist(int insurerId, int chorganisationId){
        
        boolean bFlag = true;
        
        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(InsurerChorganisation.class);
            criteria.add(Restrictions.eq("insurer.id", insurerId));
            criteria.add(Restrictions.eq("chorganisation.id", chorganisationId));
            criteria.add(Restrictions.eq("status", false));
            
            if(getByCriteria(criteria)==null){
                bFlag = false;
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }
        
        return bFlag;
    }    

    public boolean isActiveInsurerChorganisationExist(int insurerId, int chorganisationId){
        
        boolean bFlag = true;
        
        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(InsurerChorganisation.class);
            criteria.add(Restrictions.eq("insurer.id", insurerId));
            criteria.add(Restrictions.eq("chorganisation.id", chorganisationId));
            criteria.add(Restrictions.eq("status", true));
            
            if(getByCriteria(criteria)==null){
                bFlag = false;
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }
        
        return bFlag;
    }    
    
    
    /*
    public boolean DeleteObject(InsurerChorganisation object){
        
        boolean bFlag = false;
        
        try {
            
            
            delete(object);
            
            bFlag = true;
            
        } catch (Throwable e) {
           e.printStackTrace();
        }
        
        return bFlag;
    }
    */
    public boolean triggerStatus(InsurerChorganisation object) {
        
        boolean bFlag = false;
        
        try {
            
            object.setStatus(!object.isStatus());
            
            save(object);
            bFlag = true;

        } catch (Throwable e) {
            bFlag = false;
            e.printStackTrace();
        }    

        return bFlag;
    }   
    
    public InsurerChorganisation getObject(int id) {        
        return (InsurerChorganisation) get(InsurerChorganisation.class, id);
    }
    
    public boolean updateObject(InsurerChorganisation object) {
        
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
