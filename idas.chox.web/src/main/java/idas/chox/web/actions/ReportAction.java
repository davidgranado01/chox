package idas.chox.web.actions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import org.apache.struts2.interceptor.ParameterAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.services.LookupService;
import idas.chox.core.model.LookupItem;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Calendar;

public class ReportAction extends BaseAction implements ParameterAware {

    private static final Logger LOG = LoggerFactory.getLogger(ReportAction.class);
    private String actionResult;
    private Map parametersMap;
    private InputStream reportStream;
    private String reportName;
    private BaseDataService baseDataService;
    private LookupService lookupService;
    private List<Insurer> insurers;
    private List<Chorganisation> suppliers;
    private ReportAccessibility reportAccessibility;
    private ApplicationAccessibility applicationAccessibility;
    private boolean exportFinished;
    private boolean exportCanceled;
    private boolean exceptionThrown;
    private boolean directDownload;

    public boolean isDirectDownload() {
        return directDownload;
    }

    public void setDirectDownload(boolean directDownload) {
        this.directDownload = directDownload;
    }

    public boolean isExceptionOccured() {
        return exceptionThrown;
    }

    public void setExceptionOccured(boolean exceptionOccured) {
        this.exceptionThrown = exceptionOccured;
    }

    public boolean isExportFinished() {
        return exportFinished;
    }

    public void setExportFinished(boolean exportFinished) {
        this.exportFinished = exportFinished;
    }

    public boolean isExportCanceled() {
        return exportCanceled;
    }

    public void setExportCanceled(boolean exportCanceled) {
        this.exportCanceled = exportCanceled;
    }

    public void setApplicationAccessibility(ApplicationAccessibility applicationAccessibility) {
        this.applicationAccessibility = applicationAccessibility;
    }

    public ReportAccessibility getReportAccessibility() {

        if (reportAccessibility == null) {
            reportAccessibility = applicationAccessibility.getReportAccessibility(super.getAuthenticatedUser());
        }
        return reportAccessibility;
    }

    public String getJsonData() {
        return "{isExportProcessFinished:" + exportFinished + ",exportCancelled:" + exportCanceled + ",exceptionThrown:" + exceptionThrown + "}";
    }

    public String buildReport() {
        return SUCCESS;
    }

    public String loadParameterPanel() {
        return this.reportName;
    }

    public String exportReport() {

        synchronized (getSession()) {
            getSession().put("isExportFinished", false);
            getSession().put("exceptionThrown", false);
            getSession().put("cancelExportOperation", false);
            getSession().put("reportFileLocation", null);
        }

        final Report report = ReportFactory.getReportByName(reportName);
        LOG.debug("report generated from the reportfactory");
        if (!getReportAccessibility().canAccess(report.getReportCode())) {
            LOG.debug("Illegal attempt to access report '{}' (code '{}'", reportName, report.getReportCode());
            throw new AccessDeniedException("Illegal attempt to access report '" + reportName + "'");
        }
        LOG.info("Generating report '{}'", reportName);
        report.setExternalParameter(parametersMap);
        report.setDataService(baseDataService);
        
        Calendar cal = Calendar.getInstance();

        final String reportFileName = System.getProperty("java.io.tmpdir")+"/"+"excel_report_" + Thread.currentThread().hashCode() +cal.getTimeInMillis()+ ".xls";
        LOG.info("file will be written to the following location with name {}", reportFileName);
        
        /*
         *  Below three reports access collection from object which is lazy loaded (e.g accessing comments from claim), when run report generation in separate thread this throw session closed or not opend exception.
         *  to avoid this exception , these three reports will run in the same thread which is called this method.
         */
        if (reportName.equalsIgnoreCase("ClaimFileReport-Excel") || reportName.equalsIgnoreCase("BillingChoReport-Excel") || reportName.equalsIgnoreCase("BillingInsurerReport-Excel")) {
            try {
                report.build().writeTo(new FileOutputStream(reportFileName));
                if (isExportClaimOperationCancelled()) {
                    if (deleteReportFile(reportFileName)) {
                        LOG.debug("Report file '{}' deleted.", reportFileName);
                    } else {
                        LOG.debug("Failed to delete report file '{}'.", reportFileName);
                    }
                }
            } catch (IOException ex) {
                LOG.error("io exception in generation report {}, error message {}", reportName, ex.getMessage());
                LOG.error("Report requested by: {}, org name: {}", getAuthenticatedUser().getDisplayName(), getAuthenticatedUser().getOrganisationName());
                getSession().put("exceptionThrown", true);
            }
        } else {
            Runnable r = new Runnable() {

                @Override
                public void run() {
                    try {
                        LOG.debug("file writing operation for report {} called with seperate thread id ={}", reportName, Thread.currentThread().getId());
                        report.build().writeTo(new FileOutputStream(reportFileName));
                        LOG.debug("file writing operation for report {} finished , thread id = {}", reportName, Thread.currentThread().getId());
                    } catch (Exception ex) {
                        LOG.error("Exception thrown while generating report: {}, error message : {}", reportName, ex.getMessage());
                        LOG.error("Report requested by: {}, org name: {}", getAuthenticatedUser().getDisplayName(), getAuthenticatedUser().getOrganisationName());
                        getSession().put("exceptionThrown", true);
                    }
                }
            };

            Thread t = new Thread(r);

            t.start();

            try {
                while (!isExportClaimOperationCancelled()) {
                    Thread.sleep(500);
                    if (!t.isAlive()) {
                        LOG.debug("writing to file operation finished for report {} , existing from the loop ", reportName);
                        break;
                    }
                }

                if (isExportClaimOperationCancelled()) {
                    LOG.debug("writing to file operation cancelled for report {} , in thread {}", reportName, Thread.currentThread().getId());
                    t.interrupt();
                    t.stop();
                    t.join();
                    if (!t.isAlive()) {
                        LOG.debug("writing to xls thread is dead after cancelling the operation for report {}... ", reportName);
                    } else {
                        LOG.debug("writing to xls thread is still alive even after cancelling the operation for report {}... ", reportName);
                    }
                    if (deleteReportFile(reportFileName)) {
                        LOG.debug("Report file '{}' deleted.", reportFileName);
                    } else {
                        LOG.debug("Failed to delete report file '{}'.", reportFileName);
                    }
                }
            } catch (InterruptedException ex) {
                LOG.error("Exception thrown while generating report {}. exception message : {} .", reportName, ex.getMessage());
                LOG.error("Report requested by: {}, org name: {}", getAuthenticatedUser().getDisplayName(), getAuthenticatedUser().getOrganisationName());
                getSession().put("exceptionThrown", true);
            }
        }

        synchronized (getSession()) {
            if (!(Boolean) getSession().get("exceptionThrown")) {
                getSession().put("reportFileLocation", reportFileName);
                getSession().put("cancelExportOperation", false);
                getSession().put("isExportFinished", true);
            }
        }
        return SUCCESS;
    }

    public String getReportGenerationStatus() {
        synchronized (getSession()) {
            setExportFinished((Boolean) getSession().get("isExportFinished"));
            setExportCanceled((Boolean) getSession().get("cancelExportOperation"));
            setExceptionOccured((Boolean) getSession().get("exceptionThrown"));
        }
        return SUCCESS;
    }

    public String downloadReport() {
        
        if(isDirectDownload()){
            LOG.debug("Request to direct download report file ");
            exportReport();
        }
        synchronized (getSession()) {
            if (getSession().containsKey("reportFileLocation") && getSession().get("reportFileLocation") != null) {
                LOG.debug("Request to download  report file '{}'", getSession().get("reportFileLocation"));
                try {
                    reportStream = new FileInputStream((String) getSession().get("reportFileLocation"));
                    deleteReportFile((String) getSession().get("reportFileLocation"));
                } catch (Exception ex) {
                    LOG.error("exception in generating report {}", ex.getMessage());
                    createEmptyReport();
                }
                getSession().put("reportFileLocation", null);
            } else {
                createEmptyReport();
            }

            return SUCCESS;
        }
    }

    private void createEmptyReport() {
        LOG.error("Request to download report file does not exist. Creating empty file to avoid error shown in UI. Report requested by: {}, org name: {}", getAuthenticatedUser().getDisplayName(), getAuthenticatedUser().getOrganisationName());
        File emptyFile = new File("emptyFile");
        try {
            PrintWriter printWriter = new PrintWriter(emptyFile);
            printWriter.print("Unexpected error occured, Please contact Chox support.");
            printWriter.close();
            reportStream = new FileInputStream(emptyFile);
            deleteReportFile("emptyFile");
        } catch (FileNotFoundException ex) {
            LOG.error("file not found exception thrown {}", ex.getMessage());
        }
    }

    private boolean isExportClaimOperationCancelled() {
        synchronized (getSession()) {
            return (Boolean) getSession().get("cancelExportOperation");
        }
    }

    private boolean deleteReportFile(String filename) {
        LOG.debug("Request to delete  report file '{}'", filename);
        File reportFile = new File(filename);
        if (reportFile.exists()) {
            LOG.debug("Report file '{}' exists - deleting... ", reportFile.getName());
            return reportFile.delete();
        } else {
            LOG.debug("No such report file exists: '{}'", reportFile.getName());
        }
        return false;
    }

    public String cancelExportOperation() {
        synchronized (getSession()) {
            LOG.debug("export operation cancellation called ...");
            getSession().put("cancelExportOperation", true);
            if (getSession().containsKey("reportFileLocation") && getSession().get("reportFileLocation") != null) {
                deleteReportFile((String) getSession().get("reportFileLocation"));
                getSession().put("reportFileLocation", null);
            }
            setExportCanceled(true);
        }
        return SUCCESS;
    }

    public String getSuppliersJsonString() {
        List<LookupItem> luItems = new ArrayList<LookupItem>(getSuppliers().size());
        for (Chorganisation supplier : suppliers) {
            luItems.add(new LookupItem(supplier.getId().toString(), supplier.getName()));
        }
//           System.out.println("Insurers json is :" + JSONArray.fromObject(luItems).toString());
        return "{totalCount:" + luItems.size() + ", results:" + JSONArray.fromObject(luItems).toString() + "}";
    }

    public String getInsurersJsonString() {
        List<LookupItem> luItems = new ArrayList<LookupItem>(getInsurers().size());
        for (Insurer insurer : insurers) {
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

    @Override
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

    public List<Insurer> getInsurers() {
        if (insurers == null) {
            insurers = this.lookupService.getInsurers();
        }
        return insurers;
    }

    public List<Chorganisation> getSuppliers() {
        if (suppliers == null) {
            suppliers = this.lookupService.getSuppliers();
        }
        return suppliers;
    }

    public void setDataService(BaseDataService baseDataService) {
        this.baseDataService = baseDataService;
    }

    public void setLookupService(LookupService lookupService) {
        this.lookupService = lookupService;
    }
}
