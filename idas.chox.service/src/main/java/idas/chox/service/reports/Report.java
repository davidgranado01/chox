package idas.chox.service.reports;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import idas.chox.core.services.ReportDataService;
import idas.chox.data.services.BaseDataService;

public interface Report {

    void setExternalParameter(Map parameters);

    void setReportDataService(ReportDataService reportDataService);
    
    void setBaseDataService(BaseDataService baseDataService);
    
    Map<String, Object> getReportParameters() throws Exception;

    String getReportTemplateFileName();

    ByteArrayOutputStream build() throws Exception;

    String getReportCode();
    
    public short[] getColumnsToHide();
    
    public boolean isBrandingReportFormat();
    
}
