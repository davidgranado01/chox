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
}
