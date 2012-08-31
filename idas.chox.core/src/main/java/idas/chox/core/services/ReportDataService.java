package idas.chox.core.services;

import java.util.Map;

public interface ReportDataService {

    public Object getReportData(final String query, final Map parameters);
    
    public Object getReportData(final String query, final Map parameters, Class entityClass);
}
