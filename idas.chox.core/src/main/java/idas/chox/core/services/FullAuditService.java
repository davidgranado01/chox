package idas.chox.core.services;

import idas.chox.core.model.WebUser;
import java.io.Serializable;


/**
 *
 * @author John
 */
public interface FullAuditService {
    public void logAuditEntry(String table, Serializable id, String parameter, String oldValue, String newValue, WebUser user);
    
}
