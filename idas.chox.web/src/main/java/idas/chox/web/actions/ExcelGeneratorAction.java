package idas.chox.web.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;

import idas.chox.core.model.Claim;
import idas.chox.core.search.ClaimSearchCriteria;
import idas.chox.core.search.SearchResult;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.ReportDataService;
import idas.chox.core.util.DeleteOnCloseFileInputStream;
import idas.chox.data.*;
import idas.chox.data.services.SecureDataService;
import idas.chox.service.reports.ClaimsGridExportReport;

public class ExcelGeneratorAction extends BaseAction {

    private static final Logger LOG = LoggerFactory.getLogger(ExcelGeneratorAction.class);
    private static final int MAX_EXPORT_SIZE = 65536; // Cannot generate an Excel file with more lines than this
    private InputStream excelStream;
    private ClaimService claimService;
    private String errorMessage;
    private int exportedClaimCount;
    private boolean exportFinished;
    private boolean exportCanceled;
    private boolean writingToFile;
    private boolean exceptionThrown;
    private boolean tooManyRows;
    private boolean directDownload;
    private SecureDataService dataService;
    private ReportDataService reportDataService;

    public void setDataService(SecureDataService dataService) {
        this.dataService = dataService;
    }

    public void setReportDataService(ReportDataService reportDataService) {
        this.reportDataService = reportDataService;
    }

    public boolean isDirectDownload() {
        return directDownload;
    }

    public void setDirectDownload(boolean directDownload) {
        this.directDownload = directDownload;
    }

    public ExcelGeneratorAction() {
    }

    public boolean isExceptionOccured() {
        return exceptionThrown;
    }

    public void setExceptionOccured(boolean exceptionOccured) {
        this.exceptionThrown = exceptionOccured;
    }

    public boolean isTooManyRows() {
        return tooManyRows;
    }

    public void setTooManyRows(boolean tooManyRows) {
        this.tooManyRows = tooManyRows;
    }

    public boolean isWritingToFile() {
        return writingToFile;
    }

    public void setWritingToFile(boolean writingToFile) {
        this.writingToFile = writingToFile;
    }

    public boolean isExportCanceled() {
        return exportCanceled;
    }

    public void setExportCanceled(boolean exportCanceled) {
        this.exportCanceled = exportCanceled;
    }

    public boolean isExportFinished() {
        return exportFinished;
    }

    public void setExportFinished(boolean exportFinished) {
        this.exportFinished = exportFinished;
    }

    public int getExportedClaimCount() {
        return exportedClaimCount;
    }

    public void setExportedClaimCount(int exportedClaimCount) {
        this.exportedClaimCount = exportedClaimCount;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public InputStream getExcelStream() {
        return excelStream;
    }

    public void setExcelStream(InputStream excelStream) {
        this.excelStream = excelStream;
    }

    public String getJsonData() {
        return "{exportedClaimCount:" + exportedClaimCount + ",isExportProcessFinished:" + exportFinished + ",exportCancelled:" + exportCanceled + ",writingToFile:" + writingToFile + ",exceptionThrown:" + exceptionThrown + ",tooManyRows:" + tooManyRows + "}";
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Secured({"ROLE_CHOX_ADMIN", "ROLE_INS_GRIDEXPORT", "ROLE_CHO_GRIDEXPORT"})
    public String doExportExcel() throws IOException {

        synchronized (getSessionLock()) {
            Map<String, Object> session = getSession();
            session.put("isExportFinished", false);
            session.put("cancelExportOperation", false);
            session.put("writingToFile", false);
            session.put("numberOfClaimsProcessed", 0);
            session.put("reportFileLocation", null);
            session.put("exceptionThrown", false);
        }

        String rtnStr = ERROR;
        errorMessage = null;
        ClaimSearchCriteria c;

        if (getSession() != null) {

            c = (ClaimSearchCriteria) getSession().get("searchCriteria");

            if (c != null && c.getLimit() > 0) {

                c.setStart(0);
                c.setLimit(MAX_EXPORT_SIZE);

                SearchResult searchResult = claimService.searchClaims(c);
                List<Claim> claims = searchResult.getResult();
                LOG.debug("Total No of Claims : '{}'", claims.size());
                if (claims.size() > 0 && claims.size() <= MAX_EXPORT_SIZE) {
                    List<Integer> claimIds = new ArrayList<>(claims.size());
                    claims.forEach((claim) -> {
                        claimIds.add(claim.getId());
                    });
                    try {
                        if (!generateXML(claimIds)) {
                            if (getSession().get("tooManyRows") != null) {
                                LOG.info("Report cannot be generated as row-count exceeded {}", MAX_EXPORT_SIZE);
                                setErrorMessage("This data export will exceed the maximum number of allowable rows in Excel (65,536).");
                            } else {
                                LOG.debug("Report cancelled");
                            }
                        } else {
                            rtnStr = SUCCESS;
                        }
                    } catch (Exception ex) {
                        LOG.error("Exception thrown generating report: {}", ex.getMessage(), ex);
                        setErrorMessage("Error encountered generating report.");
                        synchronized (getSessionLock()) {
                            getSession().put("exceptionThrown", true);
                        }
                        return rtnStr;
                    }
                } else if (claims.size() > MAX_EXPORT_SIZE) {
                    setErrorMessage("The Export To Excel feature is restricted to exporting a maximum of" + MAX_EXPORT_SIZE + "claims, please refine your search.");
                }
            }
        }

        return rtnStr;
    }

    private String getReportTemplatePath(String reportTemplateName) {
        String reportDefinationFilePath = ServletActionContext.getServletContext().getRealPath("/WEB-INF/classes/reports/" + reportTemplateName);

        return reportDefinationFilePath;
    }

    private boolean generateXML(List<Integer> claimIds) throws Exception {
        boolean cancelled = false;
        ClaimsGridExportReport gridExportReport = new ClaimsGridExportReport();
        gridExportReport.setDataService(dataService);
        gridExportReport.setReportDataService(reportDataService);

        int noClaims = claimIds.size();
        int processedClaim = 0;
        LOG.info("Exporting to excel with {} claims.", noClaims);

        Boolean isIns = null;
        if (this.getIsInsurer()) {
            isIns = Boolean.TRUE;
        } else if (this.getIsCHO()) {
            isIns = Boolean.FALSE;
        }
        List<ExcelClaim> excelClaims = gridExportReport.getExcelClaims(claimIds, isIns);

        processedClaim += claimIds.size() / 5;
        if (isExportClaimOperationCancelled()) {
            synchronized (getSessionLock()) {
                getSession().put("numberOfClaimsProcessed", null);
            }
            return false;
        } else if (excelClaims.size() > MAX_EXPORT_SIZE) {
            synchronized (getSessionLock()) {
                getSession().put("numberOfClaimsProcessed", null);
                getSession().put("tooManyRows", true);
            }
            return false;
        }

        synchronized (getSessionLock()) {
            getSession().put("numberOfClaimsProcessed", processedClaim);
        }

        List<ExcelHistory> histories = gridExportReport.getExcelHistory(claimIds);
        processedClaim += claimIds.size() / 5;
        if (isExportClaimOperationCancelled()) {
            synchronized (getSessionLock()) {
                getSession().put("numberOfClaimsProcessed", null);
            }
            return false;
        } else if (histories.size() > MAX_EXPORT_SIZE) {
            synchronized (getSessionLock()) {
                getSession().put("numberOfClaimsProcessed", null);
                getSession().put("tooManyRows", true);
            }
            return false;
        }

        synchronized (getSessionLock()) {
            getSession().put("numberOfClaimsProcessed", processedClaim);
        }

        List<ExcelComment> comments = gridExportReport.getExcelComments(claimIds);
        processedClaim += claimIds.size() / 5;
        if (isExportClaimOperationCancelled()) {
            synchronized (getSessionLock()) {
                getSession().put("numberOfClaimsProcessed", null);
            }
            return false;
        } else if (comments.size() > MAX_EXPORT_SIZE) {
            synchronized (getSessionLock()) {
                getSession().put("numberOfClaimsProcessed", null);
                getSession().put("tooManyRows", true);
            }
            return false;
        }

        synchronized (getSessionLock()) {
            getSession().put("numberOfClaimsProcessed", processedClaim);
        }
        List<ExcelClaimCycle> claimCycle = gridExportReport.getExcelClaimCycle(claimIds);
        processedClaim += claimIds.size() / 5;
        if (isExportClaimOperationCancelled()) {
            synchronized (getSessionLock()) {
                getSession().put("numberOfClaimsProcessed", null);
            }
            return false;
        } else if (claimCycle.size() > MAX_EXPORT_SIZE) {
            synchronized (getSessionLock()) {
                getSession().put("numberOfClaimsProcessed", null);
                getSession().put("tooManyRows", true);
            }
            return false;
        }

        synchronized (getSessionLock()) {
            getSession().put("numberOfClaimsProcessed", processedClaim);
        }

        List<ExcelInvoice> invoices = gridExportReport.getExcelInvoices(claimIds);
        processedClaim += claimIds.size() / 5;
        if (isExportClaimOperationCancelled()) {
            synchronized (getSessionLock()) {
                getSession().put("numberOfClaimsProcessed", null);
            }
            return false;
        } else if (invoices.size() > MAX_EXPORT_SIZE) {
            synchronized (getSessionLock()) {
                getSession().put("numberOfClaimsProcessed", null);
                getSession().put("tooManyRows", true);
            }
            return false;
        }

        synchronized (getSessionLock()) {
            getSession().put("numberOfClaimsProcessed", processedClaim);
        }

        final Map<String, Object> excelMap = new HashMap();
        excelMap.put("excelclaims", excelClaims);
        excelMap.put("excelinvoices", invoices);
        excelMap.put("claimHistories", histories);
        excelMap.put("comments", comments);
        excelMap.put("cycle", claimCycle);

        final String templateFilePath = getIsInsurer() ? (isInsurerLouDatesEnabled() ? getReportTemplatePath("claimTemplateInsurerHireMon.xls") : getReportTemplatePath("claimTemplateInsurer.xls")) : getReportTemplatePath("claimTemplate.xls");
        final File reportFile = File.createTempFile("excel_report", ".xls");
        reportFile.deleteOnExit();
        LOG.info("'Export to Excel' report file will be written to the following location: {}", reportFile.getAbsolutePath());

        Runnable r = () -> {
        try {
            // Hide Copley columns where applicable
            if (getIsInsurer()) {
                if (!getAuthenticatedUser().getInsurer().isCopleyQuestion()) {
//                        transformer.setColumnsToHide(new short[]{ (short)117, (short)118} );
                }
            }
            LOG.debug("XLS transform operation called with seperate thread {}", Thread.currentThread().getId());
            try (InputStream is = new FileInputStream(templateFilePath)) {
                try (OutputStream os = new FileOutputStream(reportFile)) {
                    Context context = new Context();
                    for (Map.Entry<String, Object> entry : excelMap.entrySet()) {
                        context.putVar(entry.getKey(), entry.getValue());
                    }
                    LOG.info("Transformming....");
                    JxlsHelper.getInstance().processTemplate(is, os, context);
                    LOG.debug("Workbook created - writing to file '{}'...", reportFile.getAbsolutePath());
                    os.flush();
                    LOG.info("Transformming done");
                } catch (Exception ex) {
                    LOG.error("Exception transforminmg: {}", ex.getMessage(), ex);
                    throw ex;
                }
            }
        } catch (Exception ex) {
            LOG.error("Exception thrown transforming report: {}", ex.getMessage());
            LOG.error("Report requested by: {}, org name: {}", getAuthenticatedUser().getDisplayName(), getAuthenticatedUser().getOrganisationName());
            synchronized (getSessionLock()) {
                getSession().put("exceptionThrown", true);
            }
        }
        };
        ExecutorService executor = (ExecutorService )ServletActionContext.getServletContext().getAttribute("CHOX_EXECUTOR");

        synchronized (getSessionLock()) {
            getSession().put("writingToFile", true);
        }
        LOG.debug("Submitting executor....");
        Future<?> future = executor.submit(r);
        LOG.debug("Submitted");

        try {
            while (!isExportClaimOperationCancelled() && !future.isDone()) {
                Thread.sleep(100);
            }
            if (isExportClaimOperationCancelled()) {
                cancelled = true;
                LOG.debug("writing to file operation cancelled. in thread {}", Thread.currentThread().getId());
            }
        } catch (Exception ex) {
            LOG.error("Exception thrown while tranforming map to xls file. exception message : {} .", ex.getMessage());
            LOG.error("Report requested by: {}, org name: {}", getAuthenticatedUser().getDisplayName(), getAuthenticatedUser().getOrganisationName());
            synchronized (getSessionLock()) {
                getSession().put("exceptionThrown", true);
            }
        }

        synchronized (getSessionLock()) {
            Map<String, Object> session = getSession();
            if (!cancelled && session.get("exceptionThrown") != null) {
                session.put("numberOfClaimsProcessed", null);
                session.put("cancelExportOperation", false);
                session.put("isExportFinished", true);
                session.put("reportFileLocation", reportFile.getAbsolutePath());
                session.put("writingToFile", false);
            } else if (!cancelled) {
                throw new Exception("Error Generating Report.");
            }
        }

        return true;
    }

    public String getExportedClaimsCount() {
        synchronized (getSessionLock()) {
            Map<String, Object> session = getSession();
            if (session.containsKey("numberOfClaimsProcessed") && session.get("numberOfClaimsProcessed") != null) {
                setExportedClaimCount((Integer) session.get("numberOfClaimsProcessed"));
                setExportFinished((Boolean) session.get("isExportFinished"));
                setWritingToFile((Boolean) session.get("writingToFile"));
                setExportCanceled((Boolean) session.get("cancelExportOperation"));
                setExceptionOccured(session.get("exceptionThrown") != null ? (Boolean) session.get("exceptionThrown") : Boolean.FALSE);
            } else {
                setExportedClaimCount(0);
                setExportFinished(session.get("isExportFinished") != null ? (Boolean) session.get("isExportFinished") : Boolean.FALSE);
                setWritingToFile(session.get("writingToFile") != null ? (Boolean) session.get("writingToFile") : Boolean.FALSE);
                setExportCanceled(session.get("cancelExportOperation") != null ? (Boolean) session.get("cancelExportOperation") : Boolean.FALSE);
                setExceptionOccured(session.get("exceptionThrown") != null ? (Boolean) session.get("exceptionThrown") : Boolean.FALSE);
                setTooManyRows(session.get("tooManyRows") != null ? (Boolean) session.get("tooManyRows") : Boolean.FALSE);
            }
        }
        return SUCCESS;

    }

    public String cancelExportOperation() {
        synchronized (getSessionLock()) {
            Map<String, Object> session = getSession();
            LOG.debug("export operation cancellation called ...");
            session.put("cancelExportOperation", true);
            if (session.containsKey("reportFileLocation") && session.get("reportFileLocation") != null) {
                session.put("reportFileLocation", null);
            }
            setExportCanceled(true);
        }
        return SUCCESS;
    }

    private boolean isExportClaimOperationCancelled() {
        synchronized (getSessionLock()) {
            return (Boolean) getSession().get("cancelExportOperation");
        }
    }

    @Override
    public String execute() {
        String result;

        // Not needed as checked on doExportExcel?
        if (!getCanExportGrid()) {
            LOG.error("Illegal attempt to generate 'Export To Excel' Report by user '{}'", getAuthenticatedUser().getDisplayName());
            throw new AccessDeniedException("Illegal attempt to generate Export file.");
        }

        if (isDirectDownload()) {
            LOG.debug("Request to direct download report file ");
            try {
                doExportExcel();
            } catch (Exception ex) {
                LOG.error("Exception thrown when trying to Export To Excel. exception message : {} .", ex.getMessage(), ex);
                LOG.error("Report requested by: {}, org name: {}", getAuthenticatedUser().getDisplayName(), getAuthenticatedUser().getOrganisationName());
                synchronized (getSessionLock()) {
                    getSession().put("exceptionThrown", true);
                }
            }
        }

        synchronized (getSessionLock()) {
            Map<String, Object> session = getSession();
            if (session.containsKey("reportFileLocation") && session.get("reportFileLocation") != null) {
                try {
                    File reportFile = new File((String) session.get("reportFileLocation"));
                    excelStream = new DeleteOnCloseFileInputStream(reportFile);
                    HttpServletResponse response = ServletActionContext.getResponse();
                    response.setContentLength((int) reportFile.length());
                    result = SUCCESS;
                } catch (Exception ex) {
                    LOG.error("exception in generating report {}", ex.getMessage(), ex);
                    excelStream = null;
                    result = ERROR;
                }
                session.put("reportFileLocation", null);
            } else {
                excelStream = null;
                result = ERROR;
            }
        }

        return result;
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }
}
