package idas.chox.core.services;

import java.util.List;

import idas.chox.core.model.IPWhitelist;

/**
 *
 * @author John
 */
public interface  IPWhitelistService {
    
    boolean validateUserIPAddress(int orgId, String ipAddress, boolean isChoUser, boolean isInsurerUser);
   
    List<IPWhitelist> getIPWhitelistsByOrgId(int orgId, boolean cho, boolean ins);
    
    IPWhitelist getIPWhitelistById(int ipWhitelistId);
   
    void saveIPWhitelist(IPWhitelist iPWhitelist);
    
    void deleteIPWhitelist(IPWhitelist iPWhitelist);
    
}
