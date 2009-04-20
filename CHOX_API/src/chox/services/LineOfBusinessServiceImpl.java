/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.LineOfBusiness;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class LineOfBusinessServiceImpl extends SecureDataService implements LineOfBusinessService {

    public boolean isLineOfBusinessExist(int insurerId, String lineOfBusinessName){
        
        boolean isExist = false;
        
        try {
            
            DetachedCriteria criteria = DetachedCriteria.forClass(LineOfBusiness.class);

            criteria.add(Restrictions.eq("insurer.id", insurerId));
            criteria.add(Restrictions.eq("name", lineOfBusinessName.trim()));
            
            if(findByCriteria(criteria).size()>0){
                isExist = true;
            }
            
        } catch (Throwable e) {
            e.printStackTrace();
        }
         
        return isExist;
    }
    
    public List<LineOfBusiness> getInsurerLineOfBusiness(int insurerId) {

        List<LineOfBusiness> lineofbusiness = new ArrayList<LineOfBusiness>();
        
        try {
            
            DetachedCriteria criteria = DetachedCriteria.forClass(LineOfBusiness.class);
            
            if(insurerId>0){
                criteria.add(Restrictions.eq("insurer.id", insurerId));
                
            }
            
            lineofbusiness = findByCriteria(criteria);
            
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return lineofbusiness;
    } 
    
    public boolean DeleteObject(LineOfBusiness object){
        
        boolean bFlag = false;
        
        try {
            
            
            delete(object);
            
            bFlag = true;
            
        } catch (Throwable e) {
           e.printStackTrace();
        }
        
        return bFlag;
    }
    
    public LineOfBusiness getObject(int id) {        
        return (LineOfBusiness) get(LineOfBusiness.class, id);
    }
    
    public boolean updateObject(LineOfBusiness object) {
        
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