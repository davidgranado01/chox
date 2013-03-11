package idas.chox.core.services;

import java.io.Serializable;

import idas.chox.core.model.WebUser;

/**
 *
 * @author John
 */
public interface FullAuditService {
    void logAuditEntry(String table, Serializable id, String parameter, String oldValue, String newValue, WebUser user);
    
}
