package idas.chox.data.services;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.EmailUpdateUser;
import idas.chox.core.services.EmailUpdateUserService;

public class EmailUpdateUserServiceImpl extends BaseDataService implements EmailUpdateUserService {

    private static final Logger LOG = LoggerFactory.getLogger(EmailUpdateUserServiceImpl.class);

    @Override
    public List<EmailUpdateUser> getECDUpdatePrivilegedUsers() {
        
        DetachedCriteria criteria = DetachedCriteria.forClass(EmailUpdateUser.class)
                .add(Restrictions.eq("active", true))
                .add(Restrictions.eq("ecdUpdate", true));
        return findByCriteria(criteria);
    }

    @Override
    public List<EmailUpdateUser> getReferenceNumberUpdatePrivilegedUsers() {
        
        DetachedCriteria criteria = DetachedCriteria.forClass(EmailUpdateUser.class)
                .add(Restrictions.eq("active", true))
                .add(Restrictions.eq("refUpdate", true));
        return findByCriteria(criteria);
    }

    @Override
    public List<EmailUpdateUser> getPenaltyChargeUpdatePrivilegedUsers() {

        DetachedCriteria criteria = DetachedCriteria.forClass(EmailUpdateUser.class)
                .add(Restrictions.eq("active", true))
                .add(Restrictions.eq("penaltyUpdate", true));
        return findByCriteria(criteria);
    
    }

    @Override
    public List<EmailUpdateUser> getECDBccReceiver() {

        DetachedCriteria criteria = DetachedCriteria.forClass(EmailUpdateUser.class)
                .add(Restrictions.eq("active", true))
                .add(Restrictions.eq("ecdBccReceiver", true));
        return findByCriteria(criteria);
    }
    
    @Override
    public List<EmailUpdateUser> getRefBccReceiver() {

        DetachedCriteria criteria = DetachedCriteria.forClass(EmailUpdateUser.class)
                .add(Restrictions.eq("active", true))
                .add(Restrictions.eq("refBccReceiver", true));
        return findByCriteria(criteria);
    }
    
    @Override
    public List<EmailUpdateUser> getPenaltyBccReceiver() {

        DetachedCriteria criteria = DetachedCriteria.forClass(EmailUpdateUser.class)
                .add(Restrictions.eq("active", true))
                .add(Restrictions.eq("penaltyBccReceiver", true));
        return findByCriteria(criteria);
    }
    
    @Override
    public List<EmailUpdateUser> getErrorMessageReceiver() {

        DetachedCriteria criteria = DetachedCriteria.forClass(EmailUpdateUser.class)
                .add(Restrictions.eq("active", true))
                .add(Restrictions.eq("errorMessageReciver", true));
        return findByCriteria(criteria);
    }

}
