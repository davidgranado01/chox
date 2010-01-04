/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.data.services;

import idas.chox.core.model.Attachment;
import idas.chox.core.services.AttachmentService;
import java.util.List;
import java.util.ArrayList;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class AttachmentServiceImpl extends SecureDataService implements AttachmentService {

    public List<Attachment> getAttachmentsByClaim(int claimId) {
        List attachments = new ArrayList<Attachment>();
        DetachedCriteria criteria = DetachedCriteria.forClass(Attachment.class);
        criteria.createCriteria("claim").add(Restrictions.eq("id", claimId));
        List result = findByCriteria(criteria);
        attachments = result;
        return attachments;
    }

    public Attachment getAttachment(int AttachmentId) {
        return (Attachment) get(Attachment.class, AttachmentId);
    }
}
