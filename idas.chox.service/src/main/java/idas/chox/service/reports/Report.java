package idas.chox.service.reports;

import idas.chox.data.services.BaseDataService;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public interface Report {

    void setExternalParameter(Map parameters);

    void setReportDataService(BaseDataService baseDataService);

    HashMap getReportParameters();

    String getReportTemplateFileName();

    ByteArrayOutputStream build();

    String getReportCode();
    
    public short[] getColumnsToHide();
    
    boolean canUseReportsSessionFactory();
}
