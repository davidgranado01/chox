package idas.chox.web.actions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.struts2.interceptor.ParameterAware;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.LookupItem;
import idas.chox.core.services.LookupService;
import idas.chox.core.services.ReportDataService;
import idas.chox.core.util.DeleteOnCloseFileInputStream;
import idas.chox.core.util.TextHelper;
import idas.chox.data.services.BaseDataService;
import idas.chox.service.reports.Report;
import idas.chox.service.reports.ReportFactory;
import idas.chox.service.security.ApplicationAccessibility;
import idas.chox.service.security.ReportAccessibility;

public class ReportAction extends BaseAction implements ParameterAware {

    private static final Logger LOG = LoggerFactory.getLogger(ReportAction.class);
    private String actionResult;
    private Map parametersMap;
    private InputStream reportStream;
    private String reportName;
    private BaseDataService baseDataService;
    private ReportDataService reportDataService;
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
            reportAccessibility = new ReportAccessibility(applicationAccessibility, getAuthenticatedUser());
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
        LOG.debug("Generating report file '{}' for user '{}'", reportName, this.getAuthenticatedUser().getDisplayName());
        synchronized (getSessionLock()) {
            getSession().put("isExportFinished", false);
            getSession().put("exceptionThrown", false);
            getSession().put("cancelExportOperation", false);
            getSession().put("reportFileLocation", null);
        }
        LOG.trace("Session variables cleared.");

        if ("ClaimFileReport-Excel".equals(reportName) && !getCanExport()) {
            LOG.error("Illegal attempt to generate Claim File Report by user '{}'", getAuthenticatedUser().getDisplayName());
            throw new AccessDeniedException("Illegal attempt to generate Claim File Report.");
        }

        final Report report = ReportFactory.getReportByName(reportName);
        if (!getReportAccessibility().canAccess(report.getReportCode())) {
            LOG.error("Illegal attempt to access report '{}' (code '{}'", reportName, report.getReportCode());
            throw new AccessDeniedException("Illegal attempt to access report '" + reportName + "'");
        }
        
        report.setExternalParameter(parametersMap);
        report.setBaseDataService(baseDataService);
        report.setReportDataService(reportDataService);

        File reportFile = null;
        FileOutputStream fos = null;
        try {
            reportFile = File.createTempFile("report_", ".xls");
            reportFile.deleteOnExit();
            LOG.info("Generating report '{}' to file '{}'...", reportName, reportFile.getAbsolutePath());
            fos = new FileOutputStream(reportFile);
            report.build().writeTo(fos);
            fos.flush();
            fos.close();
            LOG.trace("Report generation complete");
        } catch (IOException ex) {
            LOG.error("io exception in generation report {}, error message {}", reportName, ex.getMessage());
            LOG.error("Report requested by: {}, org name: {}", getAuthenticatedUser().getDisplayName(), getAuthenticatedUser().getOrganisationName());
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ex2) {
                    LOG.error("Exception closing report output stream: {}", ex.getMessage(), ex);
                }
            }
            synchronized (getSessionLock()) {
                getSession().put("exceptionThrown", true);
            }
        } catch (Exception ex) {
            LOG.error("Exception in generation of report {}, error message='{}'\n", new Object[]{reportName, ex.getMessage(), ex});
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ex2) {
                    LOG.error("Exception closing report output stream: {}", ex.getMessage(), ex);
                }
            }
            synchronized (getSessionLock()) {
                getSession().put("exceptionThrown", true);
            }
        }

        LOG.trace("Updating session with report generation result");
        synchronized (getSessionLock()) {
            if (!(Boolean) getSession().get("exceptionThrown")) {
                if (reportFile != null) {
                    getSession().put("reportFileLocation", reportFile.getAbsolutePath());
                    getSession().put("cancelExportOperation", false);
                    getSession().put("isExportFinished", true);
                    LOG.debug("Export finished,details added to session - report file written to: {}", reportFile.getAbsolutePath());
                } else {
                    LOG.error("Cannot add null reportFileLocation to session");
                }
            } else {
                    LOG.debug("Exception thrown (in session) generating report");
            }
        }
        return SUCCESS;
    }

    public String getReportGenerationStatus() {
        synchronized (getSessionLock()) {
            if (getSession() != null) { // Add extra null checks as session sometimes empty!
                if (getSession().get("isExportFinished") != null) {
                    setExportFinished((Boolean) getSession().get("isExportFinished"));
                } else {
                    setExportFinished(Boolean.TRUE);
                }
                if (getSession().get("cancelExportOperation") != null) {
                    setExportCanceled((Boolean) getSession().get("cancelExportOperation"));
                } else {
                    setExportCanceled(Boolean.FALSE);
                }
                if (getSession().get("exceptionThrown") != null) {
                    setExceptionOccured((Boolean) getSession().get("exceptionThrown"));
                } else {
                    setExceptionOccured(Boolean.FALSE);
                }
            } else {
                LOG.warn("Session is null.");
            }
        }
        return SUCCESS;
    }

    public String downloadReport() {

        if (isDirectDownload()) {
            LOG.debug("Request to direct download report file ");
            exportReport();
        }
        synchronized (getSessionLock()) {
            if (getSession().containsKey("reportFileLocation") && getSession().get("reportFileLocation") != null) {
                LOG.debug("Request to download report file '{}'", getSession().get("reportFileLocation"));
                try {
                    File reportFile = new File((String) getSession().get("reportFileLocation"));
                    reportStream = new DeleteOnCloseFileInputStream(reportFile);
                } catch (FileNotFoundException ex) {
                    LOG.error("FileNotFoundException in generating report: {}\n", ex.getMessage(), ex);
                    createEmptyReport();
                }
                getSession().remove("reportFileLocation");
                getSession().remove("isExportFinished");
                getSession().remove("exceptionThrown");
                getSession().remove("cancelExportOperation");
            } else {
                LOG.error("reportFileLocation not in session or is null: {}", getSession().containsKey("reportFileLocation"));
                createEmptyReport();
            }

            return SUCCESS;
        }
    }

    private void createEmptyReport() {
        LOG.error("Request to download report file does not exist. Creating empty file to avoid error shown in UI. Report requested by: {}, org name: {}", getAuthenticatedUser().getDisplayName(), getAuthenticatedUser().getOrganisationName());
        try {
            File emptyFile = File.createTempFile("emptyReport_", ".xls");
            emptyFile.deleteOnExit();
            try (PrintWriter printWriter = new PrintWriter(emptyFile)) {
                printWriter.print("Unexpected error occurred generating this report. Please contact CHOX support if this problem persists.");
            }
            reportStream = new DeleteOnCloseFileInputStream(emptyFile);
        } catch (FileNotFoundException ex) {
            LOG.error("file not found exception thrown {}", ex.getMessage(), ex);
        } catch (IOException ex) {
            LOG.error("Exception thrown {}", ex.getMessage(), ex);
        }
    }

    private boolean isExportClaimOperationCancelled() {
        synchronized (getSessionLock()) {
            return (Boolean) getSession().get("cancelExportOperation");
        }
    }

    public String cancelExportOperation() {
        LOG.info("Report being written to '{}' has been cancelled ...", getSession().get("reportFileLocation"));
        synchronized (getSessionLock()) {
            getSession().put("cancelExportOperation", Boolean.TRUE);
            if (getSession().containsKey("reportFileLocation") && getSession().get("reportFileLocation") != null) {
                getSession().put("reportFileLocation", null);
            }
            setExportCanceled(true);
        }
        return SUCCESS;
    }

    public String getSuppliersJsonString() {
        List<LookupItem> luItems = new ArrayList<>(getSuppliers().size());
        for (Chorganisation supplier : suppliers) {
            luItems.add(new LookupItem(supplier.getId().toString(), supplier.getName()));
        }
        return StringEscapeUtils.escapeEcmaScript("{totalCount:" + luItems.size() + ", results:" + JSONArray.fromObject(luItems).toString() + "}");
    }

    public String getInsurersJsonString() {
        List<LookupItem> luItems = new ArrayList<>(getInsurers().size());
        for (Insurer insurer : insurers) {
            luItems.add(new LookupItem(insurer.getId().toString(), insurer.getName()));
        }
        return StringEscapeUtils.escapeEcmaScript("{totalCount:" + luItems.size() + ", results:" + JSONArray.fromObject(luItems).toString() + "}");
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
            if (reportName.equals(ReportFactory.BRE_INVOICE_APPROVAL_DISPUTE_RPT)
                    || reportName.equals(ReportFactory.TEAM_SITE_BRE_INVOICE_RPT)
                    || reportName.equals(ReportFactory.WORKGROUP_OWNER_BRE_RPT)) {
                suppliers = this.lookupService.getSuppliers(true); // exclude manual CHO.
            } else {
                suppliers = this.lookupService.getSuppliers(false); // include manual CHO.
            }
        }
        return suppliers;
    }

    public void setDataService(BaseDataService baseDataService) {
        this.baseDataService = baseDataService;
    }

    public void setReportDataService(ReportDataService reportDataService) {
        this.reportDataService = reportDataService;
    }
   
    public void setLookupService(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    @Override
    public void validate() {

        if (reportName != null && !reportName.isEmpty()) {
            if (!getAuthenticatedUser().isCHOXAdmin()) {
                if (getAuthenticatedUser().isAnInsurer()) {

                    if (parametersMap.containsKey("insurerId")) {
                        LOG.debug("insurer logged in and insurer id is '{}' ", parametersMap.get("insurerId"));

                        if (getAuthenticatedUser().getInsurer().getId() != TextHelper.getId(((String[]) parametersMap.get("insurerId"))[0])) {
                            // log out insurer user who tries to generate report for another insurer
                            LOG.error("Illegal attempt to access report for another insurer report name '{}' insurer name '{}'", reportName, getAuthenticatedUser().getInsurer().getName());
                            throw new AccessDeniedException("Illegal attempt to access report '" + reportName + "'");
                        }
                    }

                } else if (getAuthenticatedUser().getChorganisation() != null) {
                    if (parametersMap.containsKey("supplierId")) {
                        if (getAuthenticatedUser().getChorganisation().getId() != TextHelper.getId(((String[]) parametersMap.get("supplierId"))[0])) {
                            // log out cho user who tries to generate report for another cho
                            LOG.error("Illegal attempt to access report for another CHO report name '{}' CHO name '{}'", reportName, getAuthenticatedUser().getChorganisation().getName());
                            throw new AccessDeniedException("Illegal attempt to access report '" + reportName + "'");
                        }
                    }

                } else {
                    // log out user who blongs to no organisation
                    LOG.error("Illegal attempt to access report '{}' ", reportName);
                    throw new AccessDeniedException("Illegal attempt to access report '" + reportName + "'");
                }

            }
        }

    }
}
