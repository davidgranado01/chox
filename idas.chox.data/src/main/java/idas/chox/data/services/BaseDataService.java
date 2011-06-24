/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.data.services;

import idas.chox.core.model.UploadedXMLClaimsDetail;
import idas.chox.core.services.DataService;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 *
 * @author Emmanuel
 */
public class BaseDataService extends HibernateDaoSupport implements DataService {

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

    public void callUpdateUserService(int insurerId) throws SQLException {
//        Statement s = this.getCurrentSession().connection().createStatement();
//        ResultSet rs = s.executeQuery("select update_user_service(" + insurerId + ")");
    }

    public void callUpdateWorkgroupService(int insurerId) throws SQLException {
//        Statement s = this.getCurrentSession().connection().createStatement();
//        ResultSet rs = s.executeQuery("select update_workgroup_service(" + insurerId + ")");
    }

    public List findByCriteria(final DetachedCriteria c) {

        return getHibernateTemplate().findByCriteria(c);
    }

    public List findByCriteria(final DetachedCriteria dc, Boolean cacheable) {
        Criteria c = dc.getExecutableCriteria(getSession());
        c.setCacheable(true);
        return c.list();
    }

    public Long getCount(String query) {
        Long count = new Long(0);
        List result = getHibernateTemplate().find(query);

        if (result != null && !result.isEmpty()) {
            count = (Long) result.get(0);
        }
        return count;
    }

    @Override
    public Object get(final Class c, final int id) {

        DetachedCriteria dc = DetachedCriteria.forClass(c).add(Restrictions.eq("id", id));
        List result = getHibernateTemplate().findByCriteria(dc);
        return (result != null && result.size() == 1) ? result.get(0) : null;
    }

    @Override
    public void save(final Object object) {
        getHibernateTemplate().saveOrUpdate(object);
    }

    @Override
    public void delete(final Object object) {
        getHibernateTemplate().delete(object);
    }

    @Override
    public void evict(Object object) {
        getHibernateTemplate().evict(object);
    }

    protected Session getCurrentSession() {
        return getSession();
    }
    
    @Override
    public void saveCollections(List<? extends Object> objects){
        getHibernateTemplate().saveOrUpdateAll(objects);
    }
}
