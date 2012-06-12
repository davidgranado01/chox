package idas.chox.core.services;

import java.util.List;

import idas.chox.core.model.IPWhitelist;

/**
 *
 * @author John
 */
public interface  IPWhitelistService {
    
    public boolean validateUserIPAddress(int orgId, String ipAddress, boolean isChoUser, boolean isInsurerUser);
   
    public List<IPWhitelist> getIPWhitelistsByOrgId(int orgId, boolean cho, boolean ins);
    
    public IPWhitelist getIPWhitelistById(int ipWhitelistId);
    
    public void saveIPWhitelist(IPWhitelist iPWhitelist);
    
    public void deleteIPWhitelist(IPWhitelist iPWhitelist);
    
}
