/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.services;

import chox.data.DBInterceptor;
import chox.data.FakeSecurityInfoProvider;
import chox.data.HibernateUtil;
import chox.data.SecurityInfoProvider;
import chox.model.WebUser;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

/**
 *
 * @author Emmanuel
 */
public class DataService {

    private Session currentSession;
    private SecurityInfoProvider securityInforProvider;

    public void setSecurityInfoProvider(SecurityInfoProvider provider) {
        this.securityInforProvider = provider;

        if (!this.getSecurityInfoProvider().getIsCHOXAdmin()) {
            if (this.getSecurityInfoProvider().getIsCHO()) {
                getCurrentSession().enableFilter("Claim_CHOFilter").setParameter("chorganisationId", this.getCurrentUser().getChorganisation().getId());
            } else if (this.getSecurityInfoProvider().getIsINS()) {
                getCurrentSession().enableFilter("Claim_InsurerFilter").setParameter("insurerId", this.getCurrentUser().getInsurer().getId());
            }
        }
    }

    protected WebUser getCurrentUser() {
        return getSecurityInfoProvider().getCurrentUSer();
    }

    protected SecurityInfoProvider getSecurityInfoProvider() {
        if (this.securityInforProvider == null) {
            //for testing purpose, will inject by spring in web application
            setSecurityInfoProvider(new FakeSecurityInfoProvider());
        }
        return this.securityInforProvider;
    }
    
    public Session getCurrentSession()
    {
        if(currentSession == null)
        {
            currentSession = SessionFactoryUtils.getSession(HibernateUtil.getSessionFactory(), new DBInterceptor(securityInforProvider),null);
        }
        return currentSession;
    }

}
