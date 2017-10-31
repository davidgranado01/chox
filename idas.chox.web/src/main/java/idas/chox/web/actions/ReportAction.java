package idas.chox.web.actions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.struts2.dispatcher.HttpParameters;
import org.apache.struts2.interceptor.HttpParametersAware;

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

public class ReportAction extends BaseAction implements HttpParametersAware {

    private static final Logger LOG = LoggerFactory.getLogger(ReportAction.class);
    private String actionResult;
    private Map<String, Object> parametersMap;
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
            Map<String, Object> session = getSession();
            session.put("isExportFinished", false);
            session.put("exceptionThrown", false);
            session.put("cancelExportOperation", false);
            session.put("reportFileLocation", null);
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
        try {
            reportFile = File.createTempFile("report_", ".xls");
            reportFile.deleteOnExit();
            LOG.info("Generating report '{}' to file '{}'...", reportName, reportFile.getAbsolutePath());
            try (FileOutputStream fos = new FileOutputStream(reportFile)) {
                synchronized (getSessionLock()) {
                    Map<String, Object> session = getSession();
                    session.put("reportFileLocation", reportFile.getAbsolutePath());
                }
                try (ByteArrayOutputStream baos = report.build()) {
                    baos.writeTo(fos);
//                fos.flush();
                }
            }
            LOG.trace("Report generation complete");
        } catch (IOException ex) {
            LOG.error("io exception in generation report {}, error message {}", reportName, ex.getMessage());
            LOG.error("Report requested by: {}, org name: {}", getAuthenticatedUser().getDisplayName(), getAuthenticatedUser().getOrganisationName());
            if (reportFile != null) {
                reportFile.delete();
            }
            synchronized (getSessionLock()) {
                Map<String, Object> session = getSession();
                if (session == null || session.get("reportFileLocation") == null || reportFile == null || !session.get("reportFileLocation").equals(reportFile.getAbsolutePath())) {
                    LOG.info("Report generation '{}' threw exception but cancelled - not updating session", reportFile);
                } else {
                    session.put("exceptionThrown", true);
                }
            }
            return ERROR;
        } catch (Exception ex) {
            LOG.error("Exception in generation of report {}, error message='{}'\n", new Object[]{reportName, ex.getMessage(), ex});
            synchronized (getSessionLock()) {
                Map<String, Object> session = getSession();
                if (session == null || session.get("reportFileLocation") == null || reportFile == null || !session.get("reportFileLocation").equals(reportFile.getAbsolutePath())) {
                    LOG.info("Report generation '{}' threw exception but cancelled - not updating session", reportFile);
                } else {
                    session.put("exceptionThrown", true);
                }
            }
            return ERROR;
        }

        // If reportFile in session is different tha the one we are using, the report has been cancelled and we can ignore
        synchronized (getSessionLock()) {
            Map<String, Object> session = getSession();
            if (session == null || session.get("reportFileLocation") == null || !session.get("reportFileLocation").equals(reportFile.getAbsolutePath())) {
                LOG.info("Report generation '{}' finished but cancelled - not updating session", reportFile);
                reportFile.delete();
                return SUCCESS;
            }
            LOG.trace("Updating session with report generation finished result");
            session.put("cancelExportOperation", false);
            session.put("isExportFinished", true);
        }

        return SUCCESS;
    }

    public String getReportGenerationStatus() {
        LOG.trace("Getting report generation status...");
        synchronized (getSessionLock()) {
            Map<String, Object> session = getSession();
            if (session != null) { // Add extra null checks as session sometimes empty!
                if (session.get("isExportFinished") != null) {
                    setExportFinished((Boolean) session.get("isExportFinished"));
                } else {
                    LOG.debug("isExportFinished is null: setting to false in response");
                    setExportFinished(Boolean.FALSE);
                }
                if (session.get("cancelExportOperation") != null) {
                    setExportCanceled((Boolean) session.get("cancelExportOperation"));
                } else {
                    setExportCanceled(Boolean.TRUE);
                }
                if (session.get("exceptionThrown") != null) {
                    setExceptionOccured((Boolean) session.get("exceptionThrown"));
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
            LOG.debug("Request to direct download report file...generating...");
            exportReport();
            LOG.debug("Direct download report file generated.");
        }
        synchronized (getSessionLock()) {
            LOG.trace("Getting report file details from session...");
            Map<String, Object> session = getSession();
            String reportFileLocation = (String) session.get("reportFileLocation");
            if (reportFileLocation != null) {
                LOG.debug("Creating stream for report file '{}'", reportFileLocation);
                try {
                    File reportFile = new File(reportFileLocation);
                    reportStream = new FileInputStream(reportFile);
                } catch (FileNotFoundException ex) {
                    LOG.error("FileNotFoundException in generating report: {}\n", ex.getMessage(), ex);
                    createEmptyReport();
                }
                session.remove("exceptionThrown");
                session.remove("cancelExportOperation");
            } else {
                LOG.error("reportFileLocation not in session ({}) or is null", !session.containsKey("reportFileLocation"));
                createEmptyReport();
            }

            return SUCCESS;
        }
    }

    private void createEmptyReport() {
        LOG.debug("Request to download report file does not exist. Creating empty file to avoid error shown in UI. Report requested by: {}, org name: {}", getAuthenticatedUser().getDisplayName(), getAuthenticatedUser().getOrganisationName());
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
        synchronized (getSessionLock()) {
            LOG.info("Report being written to '{}' has been cancelled ...", getSession().get("reportFileLocation"));
            Map<String, Object> session = getSession();
            session.put("cancelExportOperation", Boolean.TRUE);
            if (session.containsKey("reportFileLocation") && session.get("reportFileLocation") != null) {
                session.put("reportFileLocation", null);
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
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(luItems);
        } catch (JsonProcessingException ex) {
            LOG.error("Error converting Suppliers luItems to json string.");
        }
        return StringEscapeUtils.escapeEcmaScript("{totalCount:" + luItems.size() + ", results:" + jsonString + "}");
    }

    public String getInsurersJsonString() {
        List<LookupItem> luItems = new ArrayList<>(getInsurers().size());
        for (Insurer insurer : insurers) {
            luItems.add(new LookupItem(insurer.getId().toString(), insurer.getName()));
        }
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(luItems);
        } catch (JsonProcessingException ex) {
            LOG.error("Error converting Insurers luItems to json string.");
        }
        return StringEscapeUtils.escapeEcmaScript("{totalCount:" + luItems.size() + ", results:" + jsonString + "}");
    }

    public void setReportName(String report) {
        reportName = report;
    }

    public String getReportName() {
        return this.reportName;
    }

    @Override
    public void setParameters(HttpParameters httParameters) {
        parametersMap = new HashMap<>(httParameters.size() + 1);
        for (String key : httParameters.keySet()) {
            parametersMap.put(key, httParameters.get(key).getObject());
        }
        parametersMap.put("CurrentUser", this.getAuthenticatedUser());
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
                    || reportName.equals(ReportFactory.INSURER_PAYMENT_RPT)
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
