package idas.chox.web.actions;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Comment;
import idas.chox.core.model.History;
import idas.chox.core.model.Injury;
import idas.chox.core.model.Solicitor;
import idas.chox.core.model.Witness;
import idas.chox.core.search.ClaimSearchCriteria;
import idas.chox.core.search.SearchResult;
import idas.chox.core.services.ClaimService;
import idas.chox.web.ExcelClaim;
import idas.chox.web.ExcelInvoice;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
import org.apache.struts2.interceptor.SessionAware;

public class ExcelGeneratorAction extends BaseAction implements SessionAware {

    private InputStream excelStream;
    private Map session;
    private ClaimService claimService;

    public InputStream getExcelStream() {
        return excelStream;
    }

    public void setExcelStream(InputStream excelStream) {
        this.excelStream = excelStream;
    }

    public ByteArrayOutputStream doExportExcel() throws IOException {

        File thisFile = new File(".");

        ClaimSearchCriteria c = null;
        ByteArrayOutputStream buf = null;

        if (session != null) {

            c = (ClaimSearchCriteria) session.get("searchCriteria");

            if (c != null) {
                SearchResult searchResult = claimService.searchClaims(c);
                List claims = searchResult.getResult();
                if (claims.size() > 0) {
                    buf = generateXML(claims);
                }
            }
        }
        return buf;
    }

    protected String getReportTemplatePath(String reportTemplateName) {
        String reportDefinationFilePath = ServletActionContext.getServletContext().getRealPath("/excelTemplate/" + reportTemplateName);
        return reportDefinationFilePath;
    }

    public ByteArrayOutputStream generateXML(List claims) throws IOException {

        InputStream templateIS = new FileInputStream(getReportTemplatePath("claimTemplate.xls"));
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        List histories = new ArrayList<History>();
        List comments = new ArrayList<Comment>();
        List<ExcelInvoice> invoices = new ArrayList<ExcelInvoice>();

        List<ExcelClaim> excelClaims = new ArrayList<ExcelClaim>();

        for (Object obj : claims) {
            Claim claim = (Claim) obj;
            ExcelClaim ec = new ExcelClaim();
            ExcelInvoice ev = new ExcelInvoice();
            ec.setClaim(claim);

            if (claim.getInvoice() != null) {

                ev = new ExcelInvoice();
                ev.setInvoice(claim.getInvoice());
                ev.setChoReference(claim.getChoReference());
                ev.setClaimStatus(claim.getStatus());
                invoices.add(ev);

            }

            if (claim.getIncident() != null) {

                // GET INJURY
                Injury injury = claim.getIncident().getInjury();

                if (injury != null) {

                    Solicitor solicitor = injury.getSolicitor();

                    if (solicitor != null) {
                        ec.setSolicitor(solicitor);
                    }

                    ec.setInjury(injury);
                }

                // GET WITNESS
                Witness witness = claim.getIncident().getWitness();
                if (witness != null) {
                    ec.setWitness(witness);
                }

            }

            excelClaims.add(ec);

            Boolean isShowAll = this.getIsInsurer();
            Boolean isPublic = this.getIsCHO();

            // GET HISTORY BY CLAIM ID;
            histories.addAll(claim.getHistories());
            // GET COMMENT BY CLAIM ID;
            comments.addAll(claim.getComments());
        }

        if (histories.size() <= 0) {
            histories = new ArrayList<History>();
        }

        if (comments.size() <= 0) {
            comments = new ArrayList<Comment>();
        }

        Map excelMap = new HashMap();
        excelMap.put("excelclaims", excelClaims);
        excelMap.put("excelinvoices", invoices);
        excelMap.put("histories", histories);
        excelMap.put("comments", comments);

        /*
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(templateIS, excelMap).write(out);
         */

        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(templateIS, excelMap).write(out);

        excelMap.clear();
        return out;
    }

    public String execute() throws Exception {

        ByteArrayOutputStream buf = doExportExcel();
        String returnStr = "";

        if (buf != null) {
            excelStream = new ByteArrayInputStream(buf.toByteArray());
            returnStr = "success";
        } else {
            returnStr = "failed";
        }

        return returnStr;
    }

    public void setSession(Map session) {
        this.session = session;
    }  

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }
}
