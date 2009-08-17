/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.Attachment;
import chox.model.Bordereau;
import java.util.List;
import java.util.ArrayList;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class BordereauServiceImpl extends DataService implements BordereauService{

    public Boolean saveObj(Bordereau obj){
        
        Boolean bFlag = false; 
        
        try{
           save(obj);
            bFlag = true;
        } catch (Exception e) {    
            e.printStackTrace();
        }
        return bFlag;
    }
    
    public Bordereau getObject(int id) {
        return (Bordereau) get(Bordereau.class, id);
    }
    
    public Bordereau getObject(String fileName) {
        
        Bordereau bordereau = new Bordereau();
        
        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(Bordereau.class);
            criteria.add(Restrictions.eq("fileName", fileName));
            bordereau = (Bordereau) getByCriteria(criteria);

        } catch (Throwable e) {
           e.printStackTrace();
        }
        
        return bordereau;
    }  
    
    public boolean deleteObject(String fileName){
        
        boolean bFlag = false;
        Bordereau  object = getObject(fileName);
        if(object!=null){
            bFlag = true;
            delete(object);
        }
        
        return bFlag;
    }
    
}
