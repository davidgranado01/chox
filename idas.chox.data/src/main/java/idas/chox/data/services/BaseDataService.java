package idas.chox.data.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.Entity;
import idas.chox.core.services.DataService;
import java.math.BigInteger;

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

    public int externalQueryCount(final String query, final Map<String, Object> parameters) {

        SQLQuery q = this.getSessionFactory().getCurrentSession().createSQLQuery(query);

        parameters.entrySet().forEach((entry) -> {
            if (entry.getValue() instanceof Collection) {
                q.setParameterList(entry.getKey(), (Collection) entry.getValue());
            } else if (entry.getValue() instanceof String) {
                q.setString(entry.getKey(), (String) entry.getValue());
            } else if (entry.getValue() instanceof Integer) {
                q.setInteger(entry.getKey(), (int) entry.getValue());
            } else {
                q.setParameter(entry.getKey(), entry.getValue());
            }
        });

        return ((BigInteger)q.uniqueResult()).intValue();
    }

    public List externalQuery(final String query, final Map<String, Object> parameters) {

        SQLQuery q = this.getSessionFactory().getCurrentSession().createSQLQuery(query);

        parameters.entrySet().forEach((entry) -> {
            if (entry.getValue() instanceof Collection) {
                q.setParameterList(entry.getKey(), (Collection) entry.getValue());
            } else if (entry.getValue() instanceof String) {
                q.setString(entry.getKey(), (String) entry.getValue());
            } else if (entry.getValue() instanceof Integer) {
                q.setInteger(entry.getKey(), (int) entry.getValue());
            } else {
                q.setParameter(entry.getKey(), entry.getValue());
            }
        });

        return q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    public List externalQuery(final String query, Map<String, Object> parameters, Class entityClass) {

        SQLQuery q = this.getSessionFactory().getCurrentSession().createSQLQuery(query);

        parameters.entrySet().forEach((entry) -> {
            if (entry.getValue() instanceof Collection) {
                q.setParameterList(entry.getKey(), (Collection) entry.getValue());
            } else if (entry.getValue() instanceof String) {
                q.setString(entry.getKey(), (String) entry.getValue());
            } else if (entry.getValue() instanceof Integer) {
                q.setInteger(entry.getKey(), (int) entry.getValue());
            } else {
                q.setParameter(entry.getKey(), entry.getValue());
            }
        });

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

        getCurrentSession().doWork((Connection connection) -> {
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
        });

    }

    public void callUpdateDashboard(final int userId) throws SQLException {
        LOG.debug("Calling stored procedure updatedashboard({})....", userId);
        getCurrentSession().flush();

        getCurrentSession().doWork((Connection connection) -> {
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
        });
    }

    public void callAddMissingEcdTask(final int userId) throws SQLException {
        LOG.debug("Calling stored procedure addMissingEcdTask({})....", userId);
        getCurrentSession().flush();

        getCurrentSession().doWork((Connection connection) -> {
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
        });
    }

    public void callAddInvoicePenaltyTask(final int userId) throws SQLException {
        LOG.debug("Calling stored procedure addInvoicePenaltyTask({})....", userId);
        getCurrentSession().flush();

        getCurrentSession().doWork((Connection connection) -> {
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
        });
    }

    public void callUpdateWorkflowTables(int userId) throws SQLException {
        LOG.debug("Calling stored procedure update_user_service({})....", userId);
        getCurrentSession().flush();

        getCurrentSession().doWork((Connection connection) -> {
            Statement s = connection.createStatement();
            try {
                s.execute("select update_user_service(" + userId + ") from insurer where status=true");
                s.execute("select update_workgroup_service(" + userId + ") from insurer where status=true");
            } catch (SQLException ex) {
                if (!ex.getMessage().startsWith("A result was returned when none was expected.")) {
                    throw ex;
                }
            } finally {
                getCurrentSession().flush();
                s.close();
            }
        });
    }

    public void callHashClaims(int age) throws SQLException {
        LOG.debug("Calling stored procedure hashClaims({})....", age);
        getCurrentSession().flush();

        getCurrentSession().doWork((Connection connection) -> {
            Statement s = connection.createStatement();
            try {
                s.execute("select hashClaims(" + age + ")");
            } catch (SQLException ex) {
                if (!ex.getMessage().startsWith("A result was returned when none was expected.")) {
                    throw ex;
                }
            } finally {
                getCurrentSession().flush();
                s.close();
            }
        });
    }

    public void callCloseOldClaims(int age) throws SQLException {
        LOG.debug("Calling stored procedure closeOldClaims({})....", age);
        getCurrentSession().flush();

        getCurrentSession().doWork((Connection connection) -> {
            Statement s = connection.createStatement();
            try {
                s.execute("select closeOldClaims(" + age + ")");
            } catch (SQLException ex) {
                if (!ex.getMessage().startsWith("A result was returned when none was expected.")) {
                    throw ex;
                }
            } finally {
                getCurrentSession().flush();
                s.close();
            }
        });
    }

    public void callRemoveNotes(int age) throws SQLException {
        LOG.debug("Calling stored procedure removeNotes({})....", age);
        getCurrentSession().flush();

        getCurrentSession().doWork((Connection connection) -> {
            Statement s = connection.createStatement();
            try {
                s.execute("select removeNotes(" + age + ")");
            } catch (SQLException ex) {
                if (!ex.getMessage().startsWith("A result was returned when none was expected.")) {
                    throw ex;
                }
            } finally {
                getCurrentSession().flush();
                s.close();
            }
        });
    }

    public void callRemoveTasks(int age) throws SQLException {
        LOG.debug("Calling stored procedure removeTasks({})....", age);
        getCurrentSession().flush();

        getCurrentSession().doWork((Connection connection) -> {
            Statement s = connection.createStatement();
            try {
                s.execute("select removeTasks(" + age + ")");
            } catch (SQLException ex) {
                if (!ex.getMessage().startsWith("A result was returned when none was expected.")) {
                    throw ex;
                }
            } finally {
                getCurrentSession().flush();
                s.close();
            }
        });
    }

    public void callRemoveAttachments(int age) throws SQLException {
        LOG.debug("Calling stored procedure removeAttachments({})....", age);
        getCurrentSession().flush();

        getCurrentSession().doWork((Connection connection) -> {
            Statement s = connection.createStatement();
            try {
                s.execute("select removeAttachments(" + age + ")");
            } catch (SQLException ex) {
                if (!ex.getMessage().startsWith("A result was returned when none was expected.")) {
                    throw ex;
                }
            } finally {
                getCurrentSession().flush();
                s.close();
            }
        });
    }

    public void callDeactivateUsers(int age) throws SQLException {
        LOG.debug("Calling stored procedure deactivateUsers({})....", age);
        getCurrentSession().flush();

        getCurrentSession().doWork((Connection connection) -> {
            Statement s = connection.createStatement();
            try {
                s.execute("select deactivateUsers(" + age + ")");
            } catch (SQLException ex) {
                if (!ex.getMessage().startsWith("A result was returned when none was expected.")) {
                    throw ex;
                }
            } finally {
                getCurrentSession().flush();
                s.close();
            }
        });
    }

    public void callHashUsers(int age1, int age2) throws SQLException {
        LOG.debug("Calling stored procedure hashUsers({},{})....", age1, age2);
        getCurrentSession().flush();

        getCurrentSession().doWork((Connection connection) -> {
            Statement s = connection.createStatement();
            try {
                s.execute("select hashUsers(" + age1 + "," + age2 + ")");
            } catch (SQLException ex) {
                if (!ex.getMessage().startsWith("A result was returned when none was expected.")) {
                    throw ex;
                }
            } finally {
                getCurrentSession().flush();
                s.close();
            }
        });
    }

    public void callHashVrns(int age1, int age2) throws SQLException {
        LOG.debug("Calling stored procedure hashVrns({},{})....", age1, age2);
        getCurrentSession().flush();

        getCurrentSession().doWork((Connection connection) -> {
            Statement s = connection.createStatement();
            try {
                s.execute("select hashVrns(" + age1 + "," + age2 + ")");
            } catch (SQLException ex) {
                if (!ex.getMessage().startsWith("A result was returned when none was expected.")) {
                    throw ex;
                }
            } finally {
                getCurrentSession().flush();
                s.close();
            }
        });
    }

    public void callUpdateRemainingSlaDays(int userId) throws SQLException {
        LOG.debug("Calling stored procedure updateRemainingSlaDays()....");
        getCurrentSession().flush();

        getCurrentSession().doWork((Connection connection) -> {
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
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value = "transactionManager")
    public void save(final Object object) {
        getHibernateTemplate().saveOrUpdate(object);
    }

    @Override
    public void flush() {
        getHibernateTemplate().flush();
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value = "transactionManager")
    public void delete(final Object object) {
        getHibernateTemplate().delete(object);
    }

    @Override
    public void evict(Object object) {
        getHibernateTemplate().evict(object);
    }

    public Session getCurrentSession() {
        Session session;
        try {
            session = getSessionFactory().getCurrentSession();
        } catch (HibernateException ex) {
            session = getSessionFactory().openSession();
        }
        return session;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value = "transactionManager")
    public void saveCollections(List<? extends Object> objects) {
        for (Iterator it = objects.iterator(); it.hasNext();) {
            getHibernateTemplate().saveOrUpdate(it.next());
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value = "transactionManager")
    public void deleteAll(List<? extends Entity> objects) {
        getHibernateTemplate().deleteAll(objects);
    }
}
