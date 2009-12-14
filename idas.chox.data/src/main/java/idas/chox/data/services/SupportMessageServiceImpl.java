package idas.chox.data.services;

import idas.chox.core.model.SupportMessage;
import idas.chox.core.services.SupportMessageService;
import java.io.Serializable;

/*
import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.DetachedCriteria;
 */
public class SupportMessageServiceImpl extends SecureDataService implements SupportMessageService, Serializable {

    public SupportMessage getObject(int id) {
        return (SupportMessage) get(SupportMessage.class, id);
    }

    public void updateObject(SupportMessage object) {
        save(object);
    }
}
