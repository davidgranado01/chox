/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.services;

import chox.data.FakeSecurityInfoProvider;
import chox.data.SecurityInfoProvider;
import chox.model.WebUser;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Subqueries;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

/**
 *
 * @author Emmanuel
 */
public class DataService extends HibernateDaoSupport {

    private HibernateTemplate hibernateTemplate;
    private HibernateTransactionManager transactionManager;
    private SecurityInfoProvider securityInforProvider;

    public void setSecurityInfoProvider(SecurityInfoProvider provider) {
        this.securityInforProvider = provider;
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

    private Session getCurrentSession() {
        return getSession();
    }

    protected Object get(final Class c, final int id) {

        return getHibernateTemplate().get(c, id);
    }

    protected List query(final String query) {

        return getHibernateTemplate().find(query);
    }

    protected Object getByCriteria(final DetachedCriteria c) {

        List result = getHibernateTemplate().findByCriteria(c);
        if (result != null && !result.isEmpty()) {
            return result.get(0);
        } else {
            return null;
        }
    }

    protected List findByCriteria(final DetachedCriteria c) {

        return getHibernateTemplate().findByCriteria(c);
    }

    protected List findByCriteria(final DetachedCriteria dc, final Class c, final int start, final int limit) {

        dc.setProjection(Projections.id());

        Criteria outer = getCurrentSession().createCriteria(c);
        outer.add(Subqueries.propertyIn("id", dc));
        outer.setFirstResult(start);
        outer.setMaxResults(limit);
        return outer.list();
    }

    protected void save(final Object object) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(getTransactionManager());
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        transactionTemplate.execute(
                new TransactionCallbackWithoutResult() {

                    public void doInTransactionWithoutResult(TransactionStatus status) {
                        getHibernateTemplate().saveOrUpdate(object);
                    }
                });
    }

    protected void delete(final Object object) {
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
