package idas.chox.web.actions;

import idas.chox.core.model.AuditTrail;
import idas.chox.core.model.Claim;
import idas.chox.core.model.Comment;
import idas.chox.core.search.ClaimSearchCriteria;
import idas.chox.core.search.SearchResult;
import idas.chox.core.services.AuditTrailService;
import idas.chox.web.viewdata.AuditTrailViewData;
import idas.chox.core.services.ClaimService;
import idas.chox.core.util.DateHelper;
import idas.chox.core.util.DeleteOnCloseFileInputStream;
import idas.chox.web.ExcelClaim;
import idas.chox.web.ExcelClaimCycle;
import idas.chox.web.ExcelHistory;
import idas.chox.web.ExcelInvoice;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;

public class ExcelGeneratorAction extends BaseAction {

    private static final Logger LOG = LoggerFactory.getLogger(ExcelGeneratorAction.class);
    private InputStream excelStream;
    private ClaimService claimService;
    private AuditTrailService auditTrailService;
    private String claimSizeError;
    private int exportedClaimCount;
    private boolean exportFinished;
    private boolean exportCanceled;
    private boolean writingToFile;
    private boolean exceptionThrown;
    private boolean directDownload;

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

    public void setClaimSizeError(String claimSizeError) {
        this.claimSizeError = claimSizeError;
    }

    public void setAuditTrailService(AuditTrailService auditTrailService) {
        this.auditTrailService = auditTrailService;
    }

    public InputStream getExcelStream() {
        return excelStream;
    }

    public void setExcelStream(InputStream excelStream) {
        this.excelStream = excelStream;
    }

    public String getJsonData() {
        return "{exportedClaimCount:" + exportedClaimCount + ",isExportProcessFinished:" + exportFinished + ",exportCancelled:" + exportCanceled + ",writingToFile:" + writingToFile + ",exceptionThrown:" + exceptionThrown + "}";
    }

    public void setTab(int tab) {
        LOG.debug("setTab is called with the tab value of   '{}'", tab);
        if (tab > 0) {
            getSession().put("tabIndex", tab);
            LOG.debug("tabindex is put in the session with the value of '{}'", tab);
        } else {
            getSession().put("tabIndex", 0);
            LOG.debug("tabindex is put in the session with the value of 0");
        }

    }

    public String getClaimSizeError() {
        LOG.debug("getClaimSizeError is called and returning the value:   '{}'", claimSizeError);
        return claimSizeError;
    }

    public String doExportExcel() throws IOException {

        synchronized (getSession()) {
            getSession().put("isExportFinished", false);
            getSession().put("cancelExportOperation", false);
            getSession().put("writingToFile", false);
            getSession().put("numberOfClaimsProcessed", 0);
            getSession().put("reportFileLocation", null);
            getSession().put("exceptionThrown", false);
        }

        String rtnStr = ERROR;
        claimSizeError = null;
        ClaimSearchCriteria c = null;
//        ByteArrayOutputStream buf = null;

        if (getSession() != null) {

            c = (ClaimSearchCriteria) getSession().get("searchReportCriteria");

            if (c != null && c.getLimit() > 0) {
                SearchResult searchResult = claimService.searchClaims(c);
                List claims = searchResult.getResult();
                if (claims.size() > 0 && claims.size() <= 10000) {
                    LOG.debug("Total No of Claims : '{}'", claims.size());

                    try {
                        if (!generateXML(claims))
                            LOG.info("Report cancelled");
                    } catch (Exception ex) {
                        LOG.error("Exception thrown generating report: {}", ex.getMessage(), ex);
                        setClaimSizeError("Error encountered generating report.");
                        return rtnStr;
                    }
                    rtnStr = SUCCESS;

                } else if (claims.size() > 10000) {
                    setClaimSizeError("The Export To Excel feature is restricted to exporting a maximum of 9,000 claims, please refine your search.");
                    LOG.debug("claimSizeError is setup with the value:   '{}'", getClaimSizeError());
                }
            }
        }

        return rtnStr;
    }

    private String getReportTemplatePath(String reportTemplateName) {
        String reportDefinationFilePath = ServletActionContext.getServletContext().getRealPath("/WEB-INF/classes/reports/" + reportTemplateName);

        return reportDefinationFilePath;
    }

    private boolean generateXML(List<Claim> claims) throws Exception {
        boolean isCho = this.getIsCHO();
        boolean isInsurer = this.getIsInsurer();
        int noClaims = claims.size();
        int processedClaim = 0;
        LOG.info("Exporting to excel with {} claims.", claims.size());

        List<ExcelHistory> histories = new ArrayList<ExcelHistory>(noClaims * 5);
        List<Comment> comments = new ArrayList<Comment>(noClaims * 5);
        List<ExcelClaimCycle> claimCycle = new ArrayList<ExcelClaimCycle>(noClaims * 10);
        List<ExcelInvoice> invoices = new ArrayList<ExcelInvoice>(noClaims);
        List<ExcelClaim> excelClaims = new ArrayList<ExcelClaim>(noClaims);

        ExcelClaim excelClaim;
        ExcelInvoice excelInvoice;
        ExcelHistory excelHistory;
        for (Claim claim : claims) {
            if ((getIsInsurer() && claim.getInsurer().getId().intValue() != getAuthenticatedUser().getInsurer().getId().intValue())
                    || (getIsCHO() && claim.getChorganisation().getId().intValue() != getAuthenticatedUser().getChorganisation().getId().intValue())) {
                throw new AccessDeniedException("Attempt to access a claim that you do not own.");
            }
            excelClaim = new ExcelClaim();
            excelInvoice = new ExcelInvoice();
            excelHistory = new ExcelHistory();
            excelClaim.setClaim(claim);

            if (claim.getInvoice() != null) {
                excelInvoice.setInvoice(claim.getInvoice());
                excelInvoice.setChoReference(claim.getChoReference());
                excelInvoice.setClaimStatus(claim.getStatus());
                if (claim.getThirdParty() != null) {
                    excelInvoice.setThirdPartyClaimReference(claim.getThirdParty().getClaimReference());
                }
                invoices.add(excelInvoice);
            }

            if (claim.getHistories() != null && !(claim.getHistories().isEmpty())) {
                excelHistory.setHistories(claim.getHistories(), isCho);
                histories.add(excelHistory);
            }

            excelClaims.add(excelClaim);

            // GET COMMENT BY CLAIM ID;
            if (claim.getComments() != null && !claim.getComments().isEmpty()) {
                for (Comment c : claim.getComments()) {
                    if (c.isReverted() || ((c.getVisibilityType() == 1 && isCho) || (c.getVisibilityType() == 2 && isInsurer))) {
                        continue;
                    }
                    if (c.getRaisedBy() != null) {
                        c.setCreatedBy(c.getRaisedBy());
                    }
                    comments.add(c);
                }
            }

            // Add AuditTrail / claim cycle
            List<AuditTrail> auditTrail = auditTrailService.getFullAuditTrailByClaim(claim.getId(), true);
            for (AuditTrail a : auditTrail) {
                ExcelClaimCycle cycle = new ExcelClaimCycle();
                cycle.setChoReference(claim.getChoReference());
                cycle.setModifiedBy((new AuditTrailViewData(a)).getModifiedBy());
                cycle.setModifiedDate(DateHelper.getLocalDateTimeFormat().format(a.getUpdateDate()));
                cycle.setStatus(a.getNewStatus());
                cycle.setReverted(a.getReverted() == true ? "Yes" : "");
                claimCycle.add(cycle);
            }
            claimService.evict(claim);
            processedClaim += 1;
            if (isExportClaimOperationCancelled()) {
//                break;
                synchronized (getSession()) {
                    getSession().put("numberOfClaimsProcessed", null);
                }
                return false;
            }

            synchronized (getSession()) {
                getSession().put("numberOfClaimsProcessed", processedClaim);
            }
        }

        final Map excelMap = new HashMap();
        excelMap.put("excelclaims", excelClaims);
        excelMap.put("excelinvoices", invoices);
        excelMap.put("claimHistories", histories);
        excelMap.put("comments", comments);
        excelMap.put("cycle", claimCycle);

        final String templateFilePath = getReportTemplatePath("claimTemplate.xls");
        Calendar cal = Calendar.getInstance();
        final File reportFile = File.createTempFile("excel_", ".xls");
        reportFile.deleteOnExit();
 //       final String reportFileName = System.getProperty("java.io.tmpdir") + File.pathSeparator + "excel_report_" + Thread.currentThread().hashCode() + cal.getTimeInMillis() + ".xls";
        LOG.info("'Export to Excel' report file will be written to the following location: {}", reportFile.getAbsolutePath());


        Runnable r = new Runnable() {

            @Override
            public void run() {
                try {
                    final XLSTransformer transformer = new XLSTransformer();
                    LOG.debug("XLS transform operation called with seperate thread {}", Thread.currentThread().getId());
                    InputStream is = new FileInputStream(templateFilePath);
                    HSSFWorkbook workbook = transformer.transformXLS(is, excelMap);
                    is.close();
                    LOG.debug("Workbook created - writing to file '{}'...", reportFile.getAbsolutePath());
                    OutputStream os = new FileOutputStream(reportFile);
                    workbook.write(os);
                    os.flush();
                    LOG.debug("file writing operation finished {}", Thread.currentThread().getId());
                    os.close();
                } catch (Exception ex) {
                    LOG.error("Exception thrown transforming report: {}", ex.getMessage());
                    LOG.error("Report requested by: {}, org name: {}", getAuthenticatedUser().getDisplayName(), getAuthenticatedUser().getOrganisationName());
                    getSession().put("exceptionThrown", true);
                }
            }
        };

        Thread t = new Thread(r);
        t.setDaemon(true);
        t.start();

        synchronized (getSession()) {
            getSession().put("writingToFile", true);
        }

        try {
            while (!isExportClaimOperationCancelled()) {
                Thread.sleep(200);
                if (!t.isAlive()) {
                    LOG.debug("writing to file operation finished existing from the loop ");
                    break;
                }
            }

            if (isExportClaimOperationCancelled()) {
                LOG.debug("writing to file operation cancelled. in thread {}", Thread.currentThread().getId());
                t.interrupt();
                t.stop();
                t.join();
                if (!t.isAlive()) {
                    LOG.debug("writing to xls thread is dead after cancelling the operation... ");
                } else {
                    LOG.debug("writing to xls thread is still alive even after cancelling the operation... ");
                }
            }
        } catch (Exception ex) {
            LOG.error("Exception thrown while tranforming map to xls file. exception message : {} .", ex.getMessage());
            LOG.error("Report requested by: {}, org name: {}", getAuthenticatedUser().getDisplayName(), getAuthenticatedUser().getOrganisationName());
            getSession().put("exceptionThrown", true);
        }

        synchronized (getSession()) {
            if (getSession().get("exceptionThrown") != null) {
                getSession().put("numberOfClaimsProcessed", null);
                getSession().put("cancelExportOperation", false);
                getSession().put("isExportFinished", true);
                getSession().put("reportFileLocation", reportFile.getAbsolutePath());
                getSession().put("writingToFile", false);
            }
            else
                throw new Exception("Error Generating Report.");
        }

//        excelMap.clear();
        return true;
    }

    public String getExportedClaimsCount() {
        synchronized (getSession()) {
            if (getSession().containsKey("numberOfClaimsProcessed") && getSession().get("numberOfClaimsProcessed") != null) {
                setExportedClaimCount((Integer) getSession().get("numberOfClaimsProcessed"));
                setExportFinished((Boolean) getSession().get("isExportFinished"));
                setWritingToFile((Boolean) getSession().get("writingToFile"));
                setExportCanceled((Boolean) getSession().get("cancelExportOperation"));
                if (getSession().get("exceptionThrown") == null) {
                    setExceptionOccured(Boolean.FALSE);
                } else
                    setExceptionOccured((Boolean) getSession().get("exceptionThrown"));
            } else {
                setExportedClaimCount(0);
                setExportFinished((Boolean) getSession().get("isExportFinished"));
                setWritingToFile((Boolean) getSession().get("writingToFile"));
                setExportCanceled((Boolean) getSession().get("cancelExportOperation"));
                if (getSession().get("exceptionThrown") == null) {
                    setExceptionOccured(Boolean.FALSE);
                } else
                    setExceptionOccured((Boolean) getSession().get("exceptionThrown"));
            }
        }
        return SUCCESS;

    }

    public String cancelExportOperation() {
        synchronized (getSession()) {
            LOG.debug("export operation cancellation called ...");
            getSession().put("cancelExportOperation", true);
            if (getSession().containsKey("reportFileLocation") && getSession().get("reportFileLocation") != null) {
                getSession().put("reportFileLocation", null);
            }
            setExportCanceled(true);
        }
        return SUCCESS;
    }

    private boolean isExportClaimOperationCancelled() {
        synchronized (getSession()) {
            return (Boolean) getSession().get("cancelExportOperation");
        }
    }

    @Override
    public String execute() {

        if (isDirectDownload()) {
            LOG.debug("Request to direct download report file ");
            try {
                doExportExcel();
            } catch (Exception ex) {
                LOG.error("Exception thrown when trying to Export To Excel. exception message : {} .", ex.getMessage(), ex);
                LOG.error("Report requested by: {}, org name: {}", getAuthenticatedUser().getDisplayName(), getAuthenticatedUser().getOrganisationName());
                getSession().put("exceptionThrown", true);
            }
        }

        synchronized (getSession()) {
            if (getSession().containsKey("reportFileLocation") && getSession().get("reportFileLocation") != null) {
                try {
                    excelStream = new DeleteOnCloseFileInputStream((String) getSession().get("reportFileLocation"));
                } catch (Exception ex) {
                    LOG.error("exception in generating report {}", ex.getMessage(), ex);
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
        try {
            File emptyFile = File.createTempFile("emptyExcel_", ".xls");
            emptyFile.deleteOnExit();
            PrintWriter printWriter = new PrintWriter(emptyFile);
            printWriter.print("Unexpected error occured, Please contact Chox support.");
            printWriter.close();
            excelStream = new DeleteOnCloseFileInputStream(emptyFile);
//            deleteReportFile("emptyFile");
        } catch (FileNotFoundException ex) {
            LOG.error("file not found exception thrown {}", ex.getMessage(), ex);
        } catch (Exception ex) {
            LOG.error("Exception thrown {}", ex.getMessage(), ex);
        }
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }
}
