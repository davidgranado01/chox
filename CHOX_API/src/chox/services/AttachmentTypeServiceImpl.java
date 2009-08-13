/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.AttachmentType;
import java.util.List;
import java.util.ArrayList;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class AttachmentTypeServiceImpl extends SecureDataService implements AttachmentTypeService{

    public List<AttachmentType> getAttachmentType(){
        
        List<AttachmentType> list = new ArrayList<AttachmentType>();
        
        try {
            
            DetachedCriteria criteria = DetachedCriteria.forClass(AttachmentType.class);
            criteria.add(Restrictions.eq("status", true));
            list = findByCriteria(criteria);
                
        } catch (Throwable e) {
            e.printStackTrace();
        }
        
        return list;
    }
    
    public List<String> getAttachmentTypeCode(){
        
        List<String> slist = new ArrayList<String>();
        
        try {
            
            DetachedCriteria criteria = DetachedCriteria.forClass(AttachmentType.class);
            criteria.add(Restrictions.eq("status", true));            

            for(Object obj : findByCriteria(criteria)){
                slist.add(((AttachmentType)obj).getCode());
            }
             
        } catch (Throwable e) {
            e.printStackTrace();
        }
        
        return slist;
    }
    
    public AttachmentType getAttachmentType(String code){
        
        AttachmentType attachmentType = new AttachmentType();
        
        try {
            
            DetachedCriteria criteria = DetachedCriteria.forClass(AttachmentType.class);
            criteria.add(Restrictions.eq("code", code));
            attachmentType = (AttachmentType) getByCriteria(criteria);
             
        } catch (Throwable e) {
            e.printStackTrace();
        }
        
        return attachmentType;
        
    }
    
    public List<AttachmentType> getAllAttachmentType() {
        
        List<AttachmentType> list = new ArrayList<AttachmentType>();
        
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(AttachmentType.class);
            criteria.addOrder(Order.asc("code"));
            list = findByCriteria(criteria);   
        } catch (Throwable e) {
            e.printStackTrace();
        }
        
        return list;
    }           
}
