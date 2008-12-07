/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.Util.DateHelper;
import chox.model.Attachment;
import java.util.List;
import java.util.ArrayList;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;

public class AttachmentServiceImpl extends DataService implements AttachmentService{

    public Boolean saveObj(Attachment attachment){
        
        Boolean bFlag = false;
        
        getCurrentSession().beginTransaction();
        
        try{
            getCurrentSession().saveOrUpdate(attachment);
            bFlag = true;
        } catch (Exception e) {
            getCurrentSession().getTransaction().rollback();
        }finally{
            getCurrentSession().getTransaction().commit();
        }

        return bFlag;
    }
    
    public List<Attachment> getAttachmentByClaimId(int claimId) {

        List attachments = new ArrayList<Attachment>();

        try {
            Criteria criteria = getCurrentSession().createCriteria(Attachment.class);
            criteria.createCriteria("claim").add(Restrictions.eq("id", claimId));
            attachments = criteria.list();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return attachments;
    }    
           
    public Attachment getObject(int id) {
        return (Attachment) getCurrentSession().get(Attachment.class, id);
    }
}
