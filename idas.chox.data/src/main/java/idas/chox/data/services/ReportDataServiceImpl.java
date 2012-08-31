package idas.chox.data.services;

import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import idas.chox.core.services.ReportDataService;

public class ReportDataServiceImpl implements ReportDataService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportDataServiceImpl.class);
    private SessionFactory reportSessonFactory;
    private PlatformTransactionManager reportsTransactionManager;

    public void setReportsTransactionManager(PlatformTransactionManager reportsTransactionManager) {
        this.reportsTransactionManager = reportsTransactionManager;
    }

    public void setReportSessonFactory(SessionFactory reportSessonFactory) {
        this.reportSessonFactory = reportSessonFactory;
    }

    @Override
    public Object getReportData(final String query, final Map parameters) {
        return new TransactionTemplate(reportsTransactionManager).execute(new TransactionCallback() {

            // the code in this method executes in a transactional context
            @Override
            public Object doInTransaction(TransactionStatus status) {
                return externalQuery(query, parameters, null);
            }
        });
    }

    @Override
    public Object getReportData(final String query, final Map parameters, final Class entityClass) {
        return new TransactionTemplate(reportsTransactionManager).execute(new TransactionCallback() {

            // the code in this method executes in a transactional context
            @Override
            public Object doInTransaction(TransactionStatus status) {
                return externalQuery(query, parameters, entityClass);
            }
        });
    }

    private List externalQuery(final String query, final Map parameters, Class entityClass) {

        SQLQuery q = reportSessonFactory.getCurrentSession().createSQLQuery(query);

        for (Object p : parameters.keySet()) {
            String parameterName = (String) p;
            LOG.debug("Setting parameter: {}", parameterName);
            q.setParameter(parameterName, parameters.get(parameterName));

        }
        LOG.debug("Running query...");
        if (entityClass != null) {
            return q.setResultTransformer(Transformers.aliasToBean(entityClass)).list();
        } else {
            return q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        }

    }
}
