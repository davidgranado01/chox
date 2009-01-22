/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.Attachment;
import java.util.List;
import java.util.ArrayList;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class AttachmentServiceImpl extends DataService implements AttachmentService{

    public Boolean saveObj(Attachment attachment){
        
        Boolean bFlag = false; 
        
        try{
           save(attachment);
            bFlag = true;
        } catch (Exception e) {    
            e.printStackTrace();
        }
        return bFlag;
    }
    
    public Boolean deleteAttachment(Attachment att){
        
        Boolean bFlag = false;                
        try{
            delete(att);
            bFlag = true;
        } catch (Exception e) {
            e.printStackTrace();           
        }        
        return bFlag;
    }
    
    public List<Attachment> getAttachmentByClaimId(int claimId) {

        List attachments = new ArrayList<Attachment>();

        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Attachment.class);
            criteria.createCriteria("claim").add(Restrictions.eq("id", claimId));
            List result = findByCriteria(criteria);
            attachments = result;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return attachments;
    }    
           
    public Attachment getObject(int id) {
        return (Attachment) get(Attachment.class, id);
    }
}
