/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

import idas.chox.core.services.LookupService;
import idas.chox.data.services.DataService;
import idas.chox.service.reports.Report;
import idas.chox.service.reports.ReportFactory;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import org.apache.struts2.interceptor.ParameterAware;

/**
 *
 * @author Emmanuel
 */
public class ReportAction extends BaseAction implements ParameterAware {

    private String actionResult;
    private Map parametersMap;
    private InputStream reportStream;
    private String reportName;
    private DataService dataService;
    private LookupService lookupService;
    private List insurers;
    private List suppliers;

    public String buildReport() {
        return SUCCESS;
    }

    public String loadParameterPanel() {
        return this.reportName;
    }

    public String exportReport() {

        Report report = ReportFactory.getReportByName(reportName);
        report.setExternalParameter(parametersMap);
        report.setDataService(dataService);
        reportStream = report.build();
        String returnStr = "";

        if (reportStream != null) {
            returnStr = SUCCESS;
        } else {
            returnStr = ERROR;
        }

        return returnStr;
    }

    public void setReportName(String report) {
        reportName = report;
    }

    public String getReportName() {
        return this.reportName;
    }

    public void setParameters(Map parametersMap) {
        this.parametersMap = parametersMap;
        this.parametersMap.put("CurrentUser", this.getAuthenticatedUser());
    }

    public InputStream getReportStream() {
        return reportStream;
    }

    public void setReportStream(InputStream reportStream) {
        this.reportStream = reportStream;
    }

    public String getActionResult() {
        return this.actionResult;
    }

    public List getInsurers() {
        if (insurers == null) {
            insurers = this.lookupService.getInsurers();
        }
        return insurers;
    }

    public List getSuppliers() {
        if (suppliers == null) {
            suppliers = this.lookupService.getAllSuppliers();
        }
        return suppliers;
    }

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }

    public void setLookupService(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    public boolean getIsCH() {
        return getAuthenticatedUser().getUser().isClaimHandler();
    }
}
