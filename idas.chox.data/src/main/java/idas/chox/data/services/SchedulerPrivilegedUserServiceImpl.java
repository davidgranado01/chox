package idas.chox.data.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.EmailUpdateUser;
import idas.chox.core.services.SchedulerPrivilegedUserService;

public class SchedulerPrivilegedUserServiceImpl extends BaseDataService implements SchedulerPrivilegedUserService {

    private static final Logger LOG = LoggerFactory.getLogger(SchedulerPrivilegedUserServiceImpl.class);

    @Override
    public List<EmailUpdateUser> getECDUpdatePrivilegedUsers() {
        List<EmailUpdateUser> emailUpdateUsers = null;
        String query = "select * from email_update_user where ecd_update =:ecdUpdate";
        Map extParameters = new HashMap();
        extParameters.put("ecdUpdate", true);
        try {
            List result = externalQuery(query, extParameters);
            emailUpdateUsers = convertListToEmailUpdateUser(result);
            LOG.debug("emailUpdateUsers users: {}", emailUpdateUsers.size());
        } catch (Exception ex) {
            LOG.error("exception while getting ecdUpdateEmail users: ", ex);
        }
        return emailUpdateUsers;
    }

    @Override
    public List<EmailUpdateUser> getReferenceNumberUpdatePrivilegedUsers() {
        List<EmailUpdateUser> emailUpdateUsers = null;
        String query = "select * from email_update_user where ref_update =:refUpdate";
        Map extParameters = new HashMap();
        extParameters.put("refUpdate", true);
        try {
            List result = externalQuery(query, extParameters);
            emailUpdateUsers = convertListToEmailUpdateUser(result);
            LOG.debug("emailUpdateUsers users: {}", emailUpdateUsers.size());
        } catch (Exception ex) {
            LOG.error("exception while getting refUpdateEmail users: ", ex);
        }
        return emailUpdateUsers;
    }

    @Override
    public List<EmailUpdateUser> getPenaltyChargeUpdatePrivilegedUsers() {
        List<EmailUpdateUser> emailUpdateUsers = null;
        String query = "select * from email_update_user where penalty_update =:penaltyUpdate";
        Map extParameters = new HashMap();
        extParameters.put("penaltyUpdate", true);
        try {
            List result = externalQuery(query, extParameters);
            emailUpdateUsers = convertListToEmailUpdateUser(result);
            LOG.debug("emailUpdateUsers users: {}", emailUpdateUsers.size());
        } catch (Exception ex) {
            LOG.error("exception while getting penaltyUpdateEmail users: ", ex);
        }
        return emailUpdateUsers;
    }

    private List<EmailUpdateUser> convertListToEmailUpdateUser(List result) {
        List<EmailUpdateUser> emailUpdateUsers = new ArrayList<EmailUpdateUser>();
        for (Object obj : result) {
            Map data = (Map) obj;
            EmailUpdateUser emailUpdateUser = new EmailUpdateUser();
            emailUpdateUser.setUserName(data.get("user_name").toString());
            emailUpdateUser.setEmail(data.get("email").toString());
            emailUpdateUser.setPassword(data.get("password").toString());
            emailUpdateUsers.add(emailUpdateUser);
        }
        return emailUpdateUsers;
    }
}
