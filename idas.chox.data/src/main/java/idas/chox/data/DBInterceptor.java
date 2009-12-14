package idas.chox.data;


import idas.chox.core.model.Auditable;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.util.DateHelper;
import java.io.Serializable;

import java.util.Date;
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
                    state[i] = this.getSecurityInfoProvider().getCurrentUser();
                } else if ("lastModifiedDate".equals(propertyNames[i])) {
                    state[i] = DateHelper.getCurrentTimeStamp();
                } else if ("lastModifiedBy".equals(propertyNames[i])) {
                    state[i] = this.getSecurityInfoProvider().getCurrentUser();
                }
            }
        }
        return true;

    }

    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] state1, Object[] state2, String[] propertyNames, Type[] types) {
        if (entity instanceof Auditable) {
            
            Integer indexOfStatusModifiedDate = null;
            Integer indexForPrevStatus = null;
            Date statusModifiedDate = null;
            String prevStatus = null;

            for (int i = 0; i < propertyNames.length; i++) {
                if ("lastModifiedDate".equals(propertyNames[i])) {
                    state1[i] = DateHelper.getCurrentTimeStamp();
                } else if ("lastModifiedBy".equals(propertyNames[i])) {
                    state1[i] = this.getSecurityInfoProvider().getCurrentUser();
                }
                else if ("statusModifiedDate".equals(propertyNames[i])) {
                    if(statusModifiedDate == null)
                    {
                        indexOfStatusModifiedDate = i;
                    }
                    else
                    {
                        state1[i] = statusModifiedDate;
                    }
                }
                else if ("previousStatus".equals(propertyNames[i])) {
                    if(prevStatus == null)
                    {
                        indexForPrevStatus = i;
                    }
                    else
                    {
                        state1[i] = prevStatus;
                    }
                }
                else if ("status".equals(propertyNames[i])) {
                    
                    String newStatus = state1[i].toString();
                    String oldStatus = state2[i].toString();
                    
                    if(!newStatus.equalsIgnoreCase(oldStatus))
                    {
                        if(indexOfStatusModifiedDate != null)
                        {
                            state1[indexOfStatusModifiedDate] = new Date();
                        }
                        else
                        {
                            statusModifiedDate = new Date();
                        }
                        
                        if(indexForPrevStatus != null)
                        {
                            state1[indexForPrevStatus] = oldStatus;
                        }
                        else
                        {
                            prevStatus = oldStatus;
                        }
                    }

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
