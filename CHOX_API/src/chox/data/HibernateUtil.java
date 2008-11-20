package chox.data;

import chox.exception.InvalidaUserDataException;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import java.util.List;
import java.util.Date;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * This file is generated from a template that you can modify if you want different behaviour.
 *
 */
public class HibernateUtil {

    private static final SessionFactory sessionFactory;
    private static final ThreadLocal session = new ThreadLocal();
    private static FilterProvider filterProvider;
    

    static {

        sessionFactory = new Configuration().configure().buildSessionFactory();

    }

    public static void setFilterProvider(FilterProvider filterProvider) {
        HibernateUtil.filterProvider = filterProvider;
    }
    
    public static boolean getIsFilterProviderSet()
    {
        return filterProvider != null;
    }

    public static Session currentSession() throws HibernateException {
        Session s = (Session) session.get();
        if (s == null) {
            s = sessionFactory.openSession();

            if (filterProvider != null) {
         
                    filterProvider.setFilter(s);
              
            }
            session.set(s);
        }
        return s;
    }

    public static void closeSession() throws HibernateException {
        Session s = (Session) session.get();
        session.set(null);
        if (s != null) {
            s.close();
        }
    }

    public static List executeSQLQuery(String tableName, Class theClass, String where, Object arg[]) {
        Session session = currentSession();
        SQLQuery q = session.createSQLQuery("select {" + tableName + ".*} from " + tableName + " where " + where);
        q.addEntity(tableName, theClass);
        for (int i = 0; i < arg.length; i++) {
            if (arg[i] instanceof Integer) {
                q.setInteger(i, ((Integer) arg[i]).intValue());
            } else if (arg[i] instanceof Short) {
                q.setShort(i, ((Short) arg[i]).shortValue());
            } else if (arg[i] instanceof Long) {
                q.setLong(i, ((Long) arg[i]).longValue());
            } else if (arg[i] instanceof Double) {
                q.setDouble(i, ((Double) arg[i]).doubleValue());
            } else if (arg[i] instanceof Float) {
                q.setFloat(i, ((Float) arg[i]).floatValue());
            } else if (arg[i] instanceof BigDecimal) {
                q.setBigDecimal(i, (BigDecimal) arg[i]);
            } else if (arg[i] instanceof BigInteger) {
                q.setBigInteger(i, (BigInteger) arg[i]);
            } else if (arg[i] instanceof String) {
                q.setString(i, (String) arg[i]);
            } else if (arg[i] instanceof Boolean) {
                q.setBoolean(i, ((Boolean) arg[i]).booleanValue());
            } else if (arg[i] instanceof Date) {
                q.setDate(i, (Date) arg[i]);
            } else {
                throw new IllegalStateException("bad type");
            }
        }

        List list = q.list();
        closeSession();

        return list;
    }
}
