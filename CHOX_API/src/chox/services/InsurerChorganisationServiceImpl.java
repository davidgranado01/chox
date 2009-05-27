/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.services;

import chox.model.InsurerChorganisation;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class InsurerChorganisationServiceImpl extends SecureDataService implements InsurerChorganisationService {
    
    // **********************
    // DEFINE SERVIES
    // **********************
    private ChoBandOrganisationService choBandOrganisationService;

    public void setChoBandOrganisationService(ChoBandOrganisationService choBandOrganisationService) {
        this.choBandOrganisationService = choBandOrganisationService;
    }
    
    
    // **********************
    // STANDARD FUNCTION
    // **********************
    
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
    
    public void deleteObject(InsurerChorganisation object) {        
        delete(object);
    }
    
    public InsurerChorganisation getObject(int id) {        
        return (InsurerChorganisation) get(InsurerChorganisation.class, id);
    }
    
    // getInsurerChorganisationObject
    public InsurerChorganisation getObject(int insurerId, int chorganisationId) {

        InsurerChorganisation object = new InsurerChorganisation();
        
        try {
            
            DetachedCriteria criteria = DetachedCriteria.forClass(InsurerChorganisation.class);
            criteria.add(Restrictions.eq("insurer.id", insurerId));
            criteria.add(Restrictions.eq("chorganisation.id", chorganisationId));
            criteria.add(Restrictions.eq("status", true));
            object = (InsurerChorganisation) getByCriteria(criteria);
            
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return object;
    }        
    
    public boolean triggerStatus(InsurerChorganisation object) {
        
        boolean bFlag = false;
        
        try {
            
            object.setStatus(!object.isStatus());
            save(object);
            
            bFlag = true;
            
            if(!object.isStatus()){
                choBandOrganisationService.deleteChoBandOrganisationByChorganisationId(object.getChorganisation().getId(), object.getInsurer().getId());
            }

        } catch (Throwable e) {
            bFlag = false;
            e.printStackTrace();
        }    

        return bFlag;
    }   

    // **********************
    // OTHER FUNCTION
    // **********************
    
    public List<InsurerChorganisation> getObjects(Integer insurerId, Integer chorganisationId) {

        List<InsurerChorganisation> objects = new ArrayList<InsurerChorganisation>();
        
        try {
            
            DetachedCriteria criteria = DetachedCriteria.forClass(InsurerChorganisation.class);
            
            if(insurerId!=null && insurerId>0){
                criteria.add(Restrictions.eq("insurer.id", insurerId));
            }
            
            if(chorganisationId!=null && chorganisationId>0){
                criteria.add(Restrictions.eq("chorganisation.id", chorganisationId));
            }
            criteria.add(Restrictions.eq("status", true));
            objects = findByCriteria(criteria);
            
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return objects;
    }


    // OBJECT EXIST AND STATUS IS INACTIVE = TRUE
    public boolean isInactiveObjectExist(int insurerId, int chorganisationId){
        
        boolean bFlag = false;
        
        try {

            InsurerChorganisation object = getObject(insurerId, chorganisationId);
            
            if(object!=null){
                if(!object.isStatus()){
                    bFlag = true;
                }
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }
        
        return bFlag;
    }

    // OBJECT EXIST AND STATUS IS ACTIVE = TRUE
    public boolean isActiveObjectExist(int insurerId, int chorganisationId){
         
        boolean bFlag = false;
        
        try {

            InsurerChorganisation object = getObject(insurerId, chorganisationId);
            
            if(object!=null){
                bFlag = object.isStatus();
            }
            
        } catch (Throwable e) {
            e.printStackTrace();
        }
        
        return bFlag;
    }
    
    
}
