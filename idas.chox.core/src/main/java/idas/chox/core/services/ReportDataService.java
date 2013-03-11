package idas.chox.core.services;

import java.util.List;
import java.util.Map;

public interface ReportDataService {

    List getReportData(final String query);
    
    List getReportData(final String query, final Map parameters);
    
    List getReportData(final String query, final Map parameters, Class entityClass);
}
