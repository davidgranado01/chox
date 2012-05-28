package idas.chox.data.services;

import idas.chox.core.model.AuditEntry;
import idas.chox.core.model.WebUser;
import idas.chox.core.services.FullAuditService;
import java.io.Serializable;

/**
 *
 * @author John
 */
public class FullAuditServiceImpl extends SecureDataService implements FullAuditService {

    @Override
    public void logAuditEntry(String table, Serializable serializableId, String parameter, String oldValue, String newValue, WebUser user) {
        int id = new Integer(serializableId.toString());
        AuditEntry auditEntry = new AuditEntry(table, id, parameter, oldValue, newValue, user);
        save(auditEntry);
    }

}
