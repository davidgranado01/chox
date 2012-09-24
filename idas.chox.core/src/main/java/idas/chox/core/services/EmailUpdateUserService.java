package idas.chox.core.services;

import java.util.List;

import idas.chox.core.model.EmailUpdateUser;


public interface EmailUpdateUserService {

    public List<EmailUpdateUser> getECDUpdatePrivilegedUsers();
    
    public List<EmailUpdateUser> getReferenceNumberUpdatePrivilegedUsers();
    
    public List<EmailUpdateUser> getPenaltyChargeUpdatePrivilegedUsers();
    
    public List<EmailUpdateUser> getRefBccReceiver();
    
    public List<EmailUpdateUser> getPenaltyBccReceiver();
    
    public List<EmailUpdateUser> getECDBccReceiver();
    
    public List<EmailUpdateUser> getErrorMessageReceiver();
    
}
