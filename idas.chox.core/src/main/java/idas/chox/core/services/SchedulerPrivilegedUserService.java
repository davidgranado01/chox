package idas.chox.core.services;

import java.util.List;

import idas.chox.core.model.EmailUpdateUser;


public interface SchedulerPrivilegedUserService {

    public List<EmailUpdateUser> getECDUpdatePrivilegedUsers();
    
    public List<EmailUpdateUser> getReferenceNumberUpdatePrivilegedUsers();
    
    public List<EmailUpdateUser> getPenaltyChargeUpdatePrivilegedUsers();
    
}
