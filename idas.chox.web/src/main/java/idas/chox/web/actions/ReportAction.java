package idas.chox.web.actions;

import idas.chox.core.model.LookupItem;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import org.apache.struts2.interceptor.ParameterAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.services.LookupService;
import idas.chox.data.services.BaseDataService;
import idas.chox.service.reports.Report;
import idas.chox.service.reports.ReportFactory;
import idas.chox.service.security.ReportAccessibility;
import idas.chox.service.security.ApplicationAccessibility;
import java.util.ArrayList;
import org.springframework.security.AccessDeniedException;
import idas.chox.core.model.Chorganisation;
import net.sf.json.JSONArray;
import idas.chox.core.model.Insurer;

public class ReportAction extends BaseAction implements ParameterAware {
    private static final Logger LOG = LoggerFactory.getLogger(ReportAction.class);

    private String actionResult;
    private Map parametersMap;
    private InputStream reportStream;
    private String reportName;
    private BaseDataService baseDataService;
    private LookupService lookupService;
    private List insurers;
    private List suppliers;
    private List<Chorganisation> suppliersJson;
    private List<Insurer> insurersJson;
    private ReportAccessibility reportAccessibility;
    private ApplicationAccessibility applicationAccessibility;

    public void setApplicationAccessibility(ApplicationAccessibility applicationAccessibility) {
        this.applicationAccessibility = applicationAccessibility;
    }

    public ReportAccessibility getReportAccessibility() {

        if (reportAccessibility == null) {
            reportAccessibility = applicationAccessibility.getReportAccessibility(super.getAuthenticatedUser());
        }
        return reportAccessibility;
    }

    public String buildReport() {
        return SUCCESS;
    }

    public String loadParameterPanel() {
        return this.reportName;
    }

    public String exportReport() {
        Report report = ReportFactory.getReportByName(reportName);
        if (!getReportAccessibility().canAccess(report.getReportCode())) {
            LOG.error("Illegal attempt to access report '{}' (code '{}'", reportName, report.getReportCode());
//            return ERROR;
            throw new AccessDeniedException("Illegal attempt to access report '" + reportName + "'");
        }

        report.setExternalParameter(parametersMap);
        report.setDataService(baseDataService);
        reportStream = report.build();
        String returnStr = "";

        if (reportStream != null) {
            returnStr = SUCCESS;
        } else {
            returnStr = ERROR;
        }

        LOG.debug("returnStr  :"+returnStr);

        return returnStr;
    }

    public String getSuppliersJsonString() {
            List<LookupItem> luItems = new ArrayList<LookupItem>(getSuppliersJson().size());
            for (Chorganisation supplier : suppliersJson) {
                luItems.add(new LookupItem(supplier.getId().toString(), supplier.getName()));
            }
//           System.out.println("Insurers json is :" + JSONArray.fromObject(luItems).toString());
           return "{totalCount:" + luItems.size() + ", results:" + JSONArray.fromObject(luItems).toString() + "}";
    }

    public String getInsurersJsonString() {
            List<LookupItem> luItems = new ArrayList<LookupItem>(getInsurersJson().size());
            for (Insurer insurer : insurersJson) {
                luItems.add(new LookupItem(insurer.getId().toString(), insurer.getName()));
            }
//           System.out.println("Insurers json is :" + JSONArray.fromObject(luItems).toString());
           return "{totalCount:" + luItems.size() + ", results:" + JSONArray.fromObject(luItems).toString() + "}";
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

    @Override
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

    public void setDataService(BaseDataService baseDataService) {
        this.baseDataService = baseDataService;
    }

    public void setLookupService(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    /**
     * @return the insurersJson
     */
    public List<Insurer> getInsurersJson() {


        if (insurersJson == null) {
            insurersJson = this.lookupService.getInsurers();
        }
        //return insurers;
        return insurersJson;
    }

    /**
     * @return the suppliersJson
     */
    public List<Chorganisation> getSuppliersJson() {

        if (suppliersJson == null) {
            suppliersJson = this.lookupService.getAllSuppliers();
        }
       // return suppliers;
        return suppliersJson;
    }

//    public boolean getIsCH() {
//        return getAuthenticatedUser().isClaimHandler();
//    }
}
