package chox.data;

import chox.Util.DateHelper;
import chox.model.Auditable;
import java.io.Serializable;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

public class DBInterceptor extends EmptyInterceptor {

    public DBInterceptor(SecurityInfoProvider securityInforProvider) {
        this.securityInforProvider = securityInforProvider;
    }
    private SecurityInfoProvider securityInforProvider;

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
                    state[i] = this.securityInforProvider.getCurrentUSer();
                } else if ("lastModifiedDate".equals(propertyNames[i])) {
                    state[i] = DateHelper.getCurrentTimeStamp();
                } else if ("lastModifiedBy".equals(propertyNames[i])) {
                    state[i] = this.securityInforProvider.getCurrentUSer();
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
                    state1[i] = this.securityInforProvider.getCurrentUSer();
                }
            }
        }
        return true;
    }
}
