/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.services;

import chox.data.FakeSecurityInfoProvider;
import chox.data.SecurityInfoProvider;
import chox.model.WebUser;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Subqueries;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

/**
 *
 * @author Emmanuel
 */
public class DataService extends HibernateDaoSupport {

    private HibernateTransactionManager transactionManager;
    private SecurityInfoProvider securityInforProvider;

    public void setSecurityInfoProvider(SecurityInfoProvider provider) {
        this.securityInforProvider = provider;
        
        if (this.securityInforProvider != null) {

            if (!this.getSecurityInfoProvider().getIsCHOXAdmin()) {
                if (this.getSecurityInfoProvider().getIsCHO()) {
                    getCurrentSession().enableFilter("Claim_CHOFilter").setParameter("chorganisationId", this.getCurrentUser().getChorganisation().getId());
                } else if (this.getSecurityInfoProvider().getIsINS()) {
                    getCurrentSession().enableFilter("Claim_InsurerFilter").setParameter("insurerId", this.getCurrentUser().getInsurer().getId());
                }
            }
        }
    }

    public WebUser getCurrentUser() {
        return getSecurityInfoProvider().getCurrentUSer();
    }

    public SecurityInfoProvider getSecurityInfoProvider() {
        if (this.securityInforProvider == null) {
            //for testing purpose, will inject by spring in web application
            setSecurityInfoProvider(new FakeSecurityInfoProvider());
        }
        return this.securityInforProvider;
    }

    private Session getCurrentSession() {        

        return getSession();
    }

    public Object get(final Class c, final int id) {

        return getHibernateTemplate().get(c, id);
    }

    public List query(final String query) {

        return getHibernateTemplate().find(query);
    }

    public List externalQuery(final String query) {

        SQLQuery q = this.getSession().createSQLQuery(query);
        return q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }
    
    public List externalQuery(final String query,Map parameters) {

        SQLQuery q = this.getSession().createSQLQuery(query);
        
        for(Object p : parameters.keySet())
        {
            String parameterName = (String)p;            
            q.setParameter(parameterName, parameters.get(parameterName));
            
        }
        
        return q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    public Object getByCriteria(final DetachedCriteria c) {

        List result = getHibernateTemplate().findByCriteria(c);
        if (result != null && !result.isEmpty()) {
            return result.get(0);
        } else {
            return null;
        }
    }

    public List findByCriteria(final DetachedCriteria c) {

        return getHibernateTemplate().findByCriteria(c);
    }

    public List findByCriteria(final DetachedCriteria dc, final Class c, final int start, final int limit) {

        dc.setProjection(Projections.id());

        Criteria outer = getCurrentSession().createCriteria(c);
        outer.add(Subqueries.propertyIn("id", dc));
        outer.setFirstResult(start);
        outer.setMaxResults(limit);
        return outer.list();
    }

    public void save(final Object object) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(getTransactionManager());
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        transactionTemplate.execute(
                new TransactionCallbackWithoutResult() {

                    public void doInTransactionWithoutResult(TransactionStatus status) {
                        getHibernateTemplate().saveOrUpdate(object);
                    }
                });
    }

    public void delete(final Object object) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(getTransactionManager());
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        transactionTemplate.execute(
                new TransactionCallbackWithoutResult() {

                    public void doInTransactionWithoutResult(TransactionStatus status) {
                        getHibernateTemplate().delete(object);
                    }
                });
    }

    public HibernateTransactionManager getTransactionManager() {
        return transactionManager;
    }

    public void setTransactionManager(HibernateTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
}
