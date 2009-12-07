/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.services;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

public class DataService extends HibernateDaoSupport {

    private HibernateTransactionManager transactionManager;

    protected Session getCurrentSession() {

        return getSession();
    }

    public Object get(final Class c, final int id) {

        DetachedCriteria dc = DetachedCriteria.forClass(c).add(Restrictions.eq("id", id));
        List result =  getHibernateTemplate().findByCriteria(dc);
        return (result != null && result.size() == 1) ? result.get(0) : null;
    }
    
    public Object get(final Class c, final Serializable id) {

        return getHibernateTemplate().get(c, id);
    }

    public void Evict(Object object)
    {
        getHibernateTemplate().evict(object);
    }
    

    public List query(final String query) {

        return getHibernateTemplate().find(query);
    }

    public List externalQuery(final String query) {

        SQLQuery q = this.getSession().createSQLQuery(query);
        return q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    public List externalQuery(final String query, final Map parameters) {

        SQLQuery q = this.getSession().createSQLQuery(query);
        
        for (Object p : parameters.keySet()) {
            String parameterName = (String) p;
            q.setParameter(parameterName, parameters.get(parameterName));

        }
        
        return q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    public List externalQuery(final String query, Map parameters, Class entityClass) {

        SQLQuery q = this.getSession().createSQLQuery(query);

        for (Object p : parameters.keySet()) {
            String parameterName = (String) p;
            q.setParameter(parameterName, parameters.get(parameterName));

        }

        return q.setResultTransformer(Transformers.aliasToBean(entityClass)).list();
    }

    public Object getByCriteria(final DetachedCriteria c) {

        List result = getHibernateTemplate().findByCriteria(c);
        if (result != null && !result.isEmpty()) {
            return result.get(0);
        } else {
            return null;
        }
    }
    
    protected Long getCount(String query) {
        Long count = new Long(0);
        List result = getHibernateTemplate().find(query);

        if (result != null && !result.isEmpty()) {
            count = (Long) result.get(0);
        }
        return count;
    }

    public List findByCriteria(final DetachedCriteria c) {
    
        return getHibernateTemplate().findByCriteria(c);
    }

     public List findByCriteria(final DetachedCriteria dc, Boolean cacheable) {

        Criteria c = dc.getExecutableCriteria(getSession());
        c.setCacheable(true);
        return c.list();
     }

    public void save(final Object object) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(getTransactionManager());
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED );
        transactionTemplate.execute(
                new TransactionCallbackWithoutResult() {

                    public void doInTransactionWithoutResult(TransactionStatus status) {
                        getHibernateTemplate().saveOrUpdate(object);
                    }
                });
    }
    
    public void delete(final Object object) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(getTransactionManager());
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED );
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
