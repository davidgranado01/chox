package idas.chox.data.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.jdbc.Work;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import idas.chox.core.model.Entity;
import idas.chox.core.services.DataService;
import java.util.Iterator;

/**
 *
 * @author Emmanuel
 */
public class BaseDataService extends HibernateDaoSupport implements DataService {
    private static final Logger LOG = LoggerFactory.getLogger(BaseDataService.class);

    public List query(final String query) {

        return getHibernateTemplate().find(query);
    }

    public List externalQuery(final String query) {

        SQLQuery q = getSessionFactory().getCurrentSession().createSQLQuery(query);
        return q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    public int externalQueryCount(final String query, final Map parameters) {

        SQLQuery q = this.getSessionFactory().getCurrentSession().createSQLQuery(query);

        for (Object p : parameters.keySet()) {
            String parameterName = (String) p;
            if (parameters.get(parameterName) instanceof Collection) {
                q.setParameterList(parameterName, (Collection) parameters.get(parameterName));
            } else if (parameters.get(parameterName) instanceof String) {
                q.setString(parameterName, (String) parameters.get(parameterName));
            }  else if (parameters.get(parameterName) instanceof Integer) {
                q.setInteger(parameterName, (int) parameters.get(parameterName));
            }else {
                q.setParameter(parameterName, parameters.get(parameterName));
            }
        }

        return q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list().size();
    }

    public List externalQuery(final String query, final Map parameters) {

        SQLQuery q = this.getSessionFactory().getCurrentSession().createSQLQuery(query);

        for (Object p : parameters.keySet()) {
            String parameterName = (String) p;
            if (parameters.get(parameterName) instanceof Collection) {
                q.setParameterList(parameterName, (Collection) parameters.get(parameterName));
            } else if (parameters.get(parameterName) instanceof String) {
                q.setString(parameterName, (String) parameters.get(parameterName));
            }  else if (parameters.get(parameterName) instanceof Integer) {
                q.setInteger(parameterName, (int) parameters.get(parameterName));
            }else {
                q.setParameter(parameterName, parameters.get(parameterName));
            }
        }
        return q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    public List externalQuery(final String query, Map parameters, Class entityClass) {

        SQLQuery q = this.getSessionFactory().getCurrentSession().createSQLQuery(query);

        for (Object p : parameters.keySet()) {
            String parameterName = (String) p;
            if (parameters.get(parameterName) instanceof Collection) {
                q.setParameterList(parameterName, (Collection) parameters.get(parameterName));
            } else if (parameters.get(parameterName) instanceof String) {
                q.setString(parameterName, (String) parameters.get(parameterName));
            }  else if (parameters.get(parameterName) instanceof Integer) {
                q.setInteger(parameterName, (int) parameters.get(parameterName));
            }else {
                q.setParameter(parameterName, parameters.get(parameterName));
            }

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

    public void callApplyAutoPenaltyCharge(final int userId, final int claimId) throws SQLException {
        LOG.debug("Calling stored procedure applyAutoPenaltyCharge({}, {})....", userId, claimId);
        getCurrentSession().flush();

        getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                Statement s = connection.createStatement();
                try {
                    s.execute("select applyAutoPenaltyCharge(" + userId + ", " + claimId + ")");
                } // The stored procedure produces output that will generate an exception - we'll ignore this, but re-throw any others
                catch (SQLException ex) {
                    if (!ex.getMessage().startsWith("A result was returned when none was expected.")) {
                        throw ex;
                    }
                } finally {
                    getCurrentSession().flush();
                    s.close();
                }
            }
        });
    }

    public void callUpdateDashboard(final int userId) throws SQLException {
        LOG.debug("Calling stored procedure updatedashboard({})....", userId);
        getCurrentSession().flush();
        
        getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                Statement s = connection.createStatement();
                
                try {
                    s.execute("select updateDashboard(" + userId + ")");
                } // The stored procedure produces output that will generate an exception - we'll ignore this, but re-throw any others
                catch (SQLException ex) {
                    if (!ex.getMessage().startsWith("A result was returned when none was expected.")) {
                        throw ex;
                    }
                } finally {
                    getCurrentSession().flush();
                    s.close();
                }

            }
        });
    }

    public void callAddMissingEcdTask(final int userId) throws SQLException {
        LOG.debug("Calling stored procedure addMissingEcdTask({})....", userId);
        getCurrentSession().flush();

        getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                Statement s = connection.createStatement();

                try {
                    s.execute("select addMissingEcdTask(" + userId + ")");
                } // The stored procedure produces output that will generate an exception - we'll ignore this, but re-throw any others
                catch (SQLException ex) {
                    if (!ex.getMessage().startsWith("A result was returned when none was expected.")) {
                        throw ex;
                    }
                } finally {
                    getCurrentSession().flush();
                    s.close();
                }
            }
        });
    }

    public void callAddInvoicePenaltyTask(final int userId) throws SQLException {
        LOG.debug("Calling stored procedure addInvoicePenaltyTask({})....", userId);
        getCurrentSession().flush();

        getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                Statement s = connection.createStatement();
                try {
                    s.execute("select addInvoicePenaltyTask(" + userId + ")");
                } // The stored procedure produces output that will generate an exception - we'll ignore this, but re-throw any others
                catch (SQLException ex) {
                    if (!ex.getMessage().startsWith("A result was returned when none was expected.")) {
                        throw ex;
                    }
                } finally {
                    getCurrentSession().flush();
                    s.close();
                }
            }
        });
    }

    public void callUpdateWorkflowTables(int userId) throws SQLException {
        LOG.debug("Calling stored procedure update_user_service({})....", userId);
        getCurrentSession().flush();

        getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                Statement s = connection.createStatement();
                try {
                    s.execute("select update_user_service(id) from insurer where status=true");
                    s.execute("select update_workgroup_service(id) from insurer where status=true");
                } catch (SQLException ex) {
                    if (!ex.getMessage().startsWith("A result was returned when none was expected.")) {
                        throw ex;
                    }
                } finally {
                    getCurrentSession().flush();
                    s.close();
                }
            }
        });
    }
    
    public void callUpdateRemainingSlaDays(int userId) throws SQLException {
        LOG.debug("Calling stored procedure updateRemainingSlaDays()....");
        getCurrentSession().flush();

        getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                Statement s = connection.createStatement();
                try {
                    s.execute("select updateRemainingSlaDays()");
                } catch (SQLException ex) {
                    if (!ex.getMessage().startsWith("A result was returned when none was expected.")) {
                        throw ex;
                    }
                } finally {
                    getCurrentSession().flush();
                    s.close();
                }
            }
        });
    }

    public List findByCriteria(final DetachedCriteria c) {

        return getHibernateTemplate().findByCriteria(c);
    }

    public List findByCriteria(final DetachedCriteria dc, Boolean cacheable) {
        Criteria c = dc.getExecutableCriteria(getSessionFactory().getCurrentSession());
        c.setCacheable(false);
        return c.list();
    }

    public Long getCount(String query) {
        Long count;
        List result = getHibernateTemplate().find(query);

        if (result != null && !result.isEmpty()) {
            count = (Long) result.get(0);
        } else {
            count = new Long("0");
        }
            
        return count;
    }

    public Integer totalCount(Criteria criteria) {
        criteria.setProjection(Projections.rowCount());
        List totalCountResult = criteria.list();
        criteria.setProjection(null);
        return ((Long) totalCountResult.get(0)).intValue();
    }
    
    public Date getTaskMinDueDate(Criteria criteria) {
        criteria.setProjection(Projections.min("dueDate"));
        List minDueDateResult = criteria.list();
        criteria.setProjection(null);
        return ((Date) minDueDateResult.get(0));
    }
    
    public void addSort(Criteria criteria, String sort, String dir) {
        if (dir.equalsIgnoreCase("desc")) {
            criteria.addOrder(Order.desc(sort).ignoreCase());
        } else {
            criteria.addOrder(Order.asc(sort).ignoreCase());
        }
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
    public void flush() {
        getHibernateTemplate().flush();
    }

    @Override
    public void delete(final Object object) {
        getHibernateTemplate().delete(object);
    }

    @Override
    public void evict(Object object) {
        getHibernateTemplate().evict(object);
    }

    public Session getCurrentSession() {
        return getSessionFactory().getCurrentSession();
    }
    
    @Override
    public void saveCollections(List<? extends Object> objects){
        for (Iterator it = objects.iterator(); it.hasNext();) {
            getHibernateTemplate().saveOrUpdate(it.next());
        }
//        getHibernateTemplate().saveOrUpdateAll(objects);
    }
    
    @Override
    public void deleteAll(List<? extends Entity> objects){
        getHibernateTemplate().deleteAll(objects);
    }
}
