package idas.chox.data.services;

import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.services.ReportDataService;

public class ReportDataServiceImpl implements ReportDataService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportDataServiceImpl.class);
    private SessionFactory reportSessonFactory;
    private SecureDataService dataService;

    public void setDataService(SecureDataService dataService) {
        this.dataService = dataService;
    }

    public void setReportSessonFactory(SessionFactory reportSessonFactory) {
        this.reportSessonFactory = reportSessonFactory;
    }

    @Override
    public List getReportData(final String query, final Map parameters) {
        return externalQuery(query, parameters, null);
    }

    @Override
    public List getReportData(final String query, final Map parameters, final Class entityClass) {
        return externalQuery(query, parameters, entityClass);
    }

    private List externalQuery(final String query, final Map parameters, Class entityClass) {

        // free up the connection resource created for this request before long running report query execute.
        dataService.getCurrentSession().disconnect(); 
        
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
