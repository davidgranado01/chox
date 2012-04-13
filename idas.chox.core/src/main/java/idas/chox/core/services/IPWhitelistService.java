package idas.chox.core.services;

/**
 *
 * @author John
 */
public interface  IPWhitelistService {
    public boolean validateInsurerIP(int orgId, String ipAddress);
    public boolean validateChoIP(int orgId, String ipAddress);
}
