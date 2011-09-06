package idas.chox.web.actions;

import idas.chox.core.model.AuditTrail;
import idas.chox.core.model.Claim;
import idas.chox.core.model.Comment;
import idas.chox.core.search.ClaimSearchCriteria;
import idas.chox.core.search.SearchResult;
import idas.chox.core.services.AuditTrailService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.util.DateHelper;
import idas.chox.web.ExcelClaim;
import idas.chox.web.ExcelClaimCycle;
import idas.chox.web.ExcelHistory;
import idas.chox.web.ExcelInvoice;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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

    public ExcelGeneratorAction() {
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
        return "{exportedClaimCount:" + exportedClaimCount + ",isExportProcessFinished:" + exportFinished + ",exportCancelled:" + exportCanceled + ",writingToFile:" + writingToFile + "}";
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

                    generateXML(claims);
                    rtnStr = SUCCESS;

                } else if (claims.size() > 10000) {
                    setClaimSizeError("The Export To Excel feature is restricted to exporting a maximum of 9,000 claims, please refine your search.");
                    LOG.debug("claimSizeError is setup with the value:   '{}'", getClaimSizeError());
                }
            }
        }

        return rtnStr;
    }

    protected String getReportTemplatePath(String reportTemplateName) {
        String reportDefinationFilePath = ServletActionContext.getServletContext().getRealPath("/WEB-INF/classes/excelTemplate/" + reportTemplateName);

        return reportDefinationFilePath;
    }

    
    public boolean generateXML(List<Claim> claims) throws IOException {
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
                    if ((c.getVisibilityType() == 1 && isCho) || (c.getVisibilityType() == 2 && isInsurer)) {
                        continue;
                    }
                    comments.add(c);
                }
            }

            // Add AuditTrail / claim cycle
            List<AuditTrail> auditTrail = auditTrailService.getFullAuditTrailByClaim(claim.getId());
            for (AuditTrail a : auditTrail) {
                ExcelClaimCycle cycle = new ExcelClaimCycle();
                cycle.setChoReference(claim.getChoReference());
                cycle.setModifiedBy(a.getUser().getDisplayName());
                cycle.setModifiedDate(DateHelper.LocalDateTimeFormat.format(a.getUpdateDate()));
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
        final String reportFileName = "excel_report_" + Thread.currentThread().hashCode() + ".xls";


        Runnable r = new Runnable() {

            @Override
            public void run() {
                try {
                    final XLSTransformer transformer = new XLSTransformer();
                    LOG.debug("file writing operation called with seperate thread {}", Thread.currentThread().getId());
                    transformer.transformXLS(templateFilePath, excelMap, reportFileName);
                    LOG.debug("file writing operation finished {}", Thread.currentThread().getId());
                } catch(Exception ex){
                    LOG.error("Exception thrown transforming report: {}", ex.getMessage());
                }
            }
        };

        Thread t = new Thread(r);

        t.start();

        synchronized (getSession()) {
            getSession().put("writingToFile", true);
        }

        try {
            while (!isExportClaimOperationCancelled()) {
                Thread.sleep(500);
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
                }else{
                    LOG.debug("writing to xls thread is still alive even after cancelling the operation... ");
                }
                if (deleteReportFile(reportFileName))
                    LOG.debug("Report file '{}' deleted.", reportFileName);
                else
                    LOG.debug("Failed to delete report file '{}'.", reportFileName);
            }
        } catch (InterruptedException ex) {
            LOG.debug("Exception thrown while tranforming map to xls file. exception message : {} .", ex.getMessage());
        }

        synchronized (getSession()) {
            getSession().put("numberOfClaimsProcessed", null);
            getSession().put("cancelExportOperation", false);
            getSession().put("isExportFinished", true);
            getSession().put("reportFileLocation", reportFileName);
            getSession().put("writingToFile", false);
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
            } else {
                setExportedClaimCount(0);
                setExportFinished((Boolean) getSession().get("isExportFinished"));
                setWritingToFile((Boolean) getSession().get("writingToFile"));
            }
        }
        return SUCCESS;

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

    public boolean isExportClaimOperationCancelled() {
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
        }
        else
            LOG.debug("No such report file exists: '{}'", reportFile.getName());
        return false;
    }

    @Override
    public String execute() throws Exception {

        synchronized (getSession()) {
            if (getSession().containsKey("reportFileLocation") && getSession().get("reportFileLocation") != null) {
                try {
                    excelStream = new FileInputStream((String) getSession().get("reportFileLocation"));
                    deleteReportFile((String) getSession().get("reportFileLocation"));
                } catch (Throwable th) {
                    String msg = th.getMessage();
                }

                getSession().put("reportFileLocation", null);
            }
            return SUCCESS;
        }
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }
}
