/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.data.services;

import idas.chox.core.model.AttachmentType;
import idas.chox.core.services.AttachmentTypeService;
import java.util.List;
import java.util.ArrayList;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class AttachmentTypeServiceImpl extends SecureDataService implements AttachmentTypeService {

    public List<String> getAttachmentTypeCode() {

        List<String> slist = new ArrayList<String>();

        DetachedCriteria criteria = DetachedCriteria.forClass(AttachmentType.class);
        criteria.add(Restrictions.eq("status", true));

        for (Object obj : findByCriteria(criteria)) {
            slist.add(((AttachmentType) obj).getCode());
        }

        return slist;
    }

    public AttachmentType getAttachmentType(String code) {
        DetachedCriteria criteria = DetachedCriteria.forClass(AttachmentType.class);
        criteria.add(Restrictions.eq("code", code));
        return (AttachmentType) getByCriteria(criteria);
    }

    public List<AttachmentType> getAllAttachmentType() {
        DetachedCriteria criteria = DetachedCriteria.forClass(AttachmentType.class);
        criteria.addOrder(Order.asc("code"));
        return findByCriteria(criteria);
    }
}
