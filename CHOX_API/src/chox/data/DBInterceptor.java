package chox.data;

import chox.Util.DateHelper;
import chox.model.Auditable;
import java.io.Serializable;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

public class DBInterceptor extends EmptyInterceptor {

    private SecurityInfoProvider securityInfoProvider;

    @Override
    public boolean onSave(Object entity,
            Serializable id,
            Object[] state,
            String[] propertyNames,
            Type[] types) {

        if (entity instanceof Auditable) {

            for (int i = 0; i < propertyNames.length; i++) {
                if ("createdDate".equals(propertyNames[i])) {
                    state[i] = DateHelper.getCurrentTimeStamp();
                } else if ("createdBy".equals(propertyNames[i])) {
                    state[i] = this.getSecurityInfoProvider().getCurrentUSer();
                } else if ("lastModifiedDate".equals(propertyNames[i])) {
                    state[i] = DateHelper.getCurrentTimeStamp();
                } else if ("lastModifiedBy".equals(propertyNames[i])) {
                    state[i] = this.getSecurityInfoProvider().getCurrentUSer();
                }
            }
        }
        return true;

    }

    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] state1, Object[] state2, String[] propertyNames, Type[] types) {
        if (entity instanceof Auditable) {

            for (int i = 0; i < propertyNames.length; i++) {
                if ("lastModifiedDate".equals(propertyNames[i])) {
                    state1[i] = DateHelper.getCurrentTimeStamp();
                } else if ("lastModifiedBy".equals(propertyNames[i])) {
                    state1[i] = this.getSecurityInfoProvider().getCurrentUSer();
                }
            }
        }
        return true;
    }

    public SecurityInfoProvider getSecurityInfoProvider() {
        return securityInfoProvider;
    }

    public void setSecurityInfoProvider(SecurityInfoProvider securityInforProvider) {
        this.securityInfoProvider = securityInforProvider;
    }
}
