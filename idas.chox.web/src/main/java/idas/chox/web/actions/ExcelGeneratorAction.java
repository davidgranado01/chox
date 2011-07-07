package idas.chox.web.actions;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Comment;
import idas.chox.core.model.Injury;
import idas.chox.core.model.Solicitor;
import idas.chox.core.model.Witness;
import idas.chox.core.search.ClaimSearchCriteria;
import idas.chox.core.search.SearchResult;
import idas.chox.core.services.ClaimService;
import idas.chox.web.ExcelClaim;
import idas.chox.web.ExcelHistory;
import idas.chox.web.ExcelInvoice;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.struts2.ServletActionContext;
import org.springframework.core.io.ClassPathResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelGeneratorAction extends BaseAction {

    private static final Logger LOG = LoggerFactory.getLogger(ExcelGeneratorAction.class);
    private InputStream excelStream;
    private ClaimService claimService;
    private String claimSizeError;

    public void setClaimSizeError(String claimSizeError) {
        this.claimSizeError = claimSizeError;
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

    
    public InputStream getExcelStream() {
        return excelStream;
    }
    
    
    public void setExcelStream(InputStream excelStream) {
        this.excelStream = excelStream;
    }

    
    public ByteArrayOutputStream doExportExcel() throws IOException {
        claimSizeError = null;
        ClaimSearchCriteria c = null;
        ByteArrayOutputStream buf = null;

        if (getSession() != null) {

            c = (ClaimSearchCriteria) getSession().get("searchReportCriteria");

            if (c != null && c.getLimit() > 0) {
                SearchResult searchResult = claimService.searchClaims(c);
                List claims = searchResult.getResult();
                if (claims.size() > 0 && claims.size() <= 5000) {
                    LOG.debug("Total No of Claims : '{}'", claims.size());

                    buf = generateXML(claims);

                } else if (claims.size() > 5000) {

                    setClaimSizeError("The Export To Excel feature is restricted to exporting a maximum of 5,000 claims, please refine your search.");
                    LOG.debug("claimSizeError is setup with the value:   '{}'", getClaimSizeError());
                    return buf;
                }
            }
        }

        return buf;
    }


    protected String getReportTemplatePath(String reportTemplateName) {
        String reportDefinationFilePath = ServletActionContext.getServletContext().getRealPath("/excelTemplate/" + reportTemplateName);
        return reportDefinationFilePath;
    }

    
    public ByteArrayOutputStream generateXML(List<Claim> claims) throws IOException {
        boolean isCho = this.getIsCHO();
        boolean isInsurer = this.getIsInsurer();
        int noClaims = claims.size();
        LOG.info("Exporting to excel with {} claims.", claims.size());
        InputStream templateIS = new ClassPathResource("claimTemplate.xls").getInputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        List<ExcelHistory> histories = new ArrayList<ExcelHistory>(noClaims*5);
        List<Comment> comments = new ArrayList<Comment>(noClaims*5);
        List<ExcelInvoice> invoices = new ArrayList<ExcelInvoice>(noClaims);
        List<ExcelClaim> excelClaims = new ArrayList<ExcelClaim>(noClaims);

        ExcelClaim ec;
        ExcelInvoice ev;
        ExcelHistory eh;
        for (Claim claim : claims) {
            ec = new ExcelClaim();
            ev = new ExcelInvoice();
            eh = new ExcelHistory();
            ec.setClaim(claim);

            if (claim.getInvoice() != null) {
                ev.setInvoice(claim.getInvoice());
                ev.setChoReference(claim.getChoReference());
                ev.setClaimStatus(claim.getStatus());
                if (claim.getThirdParty() != null) {
                    ev.setThirdPartyClaimReference(claim.getThirdParty().getClaimReference());
                }
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

            if (claim.getHistories() != null && !(claim.getHistories().isEmpty())) {
                eh.setHistories(claim.getHistories(), isCho);
                histories.add(eh);
            }

            excelClaims.add(ec);

            // GET COMMENT BY CLAIM ID;
            if (claim.getComments() != null && !claim.getComments().isEmpty()) {
                for (Comment c : claim.getComments()) {
                    if ((c.getVisibilityType() == 1 && isCho) || (c.getVisibilityType() == 2 && isInsurer)) {
                        continue;
                    }
                    comments.add(c);
                }
            }
            claimService.evict(claim);
        }


        Map excelMap = new HashMap();
        excelMap.put("excelclaims", excelClaims);
        excelMap.put("excelinvoices", invoices);
        excelMap.put("claimHistories", histories);
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

    
    @Override
    public String execute() throws Exception {
        ByteArrayOutputStream buf1 = doExportExcel();
        if (claimSizeError != null) {
            return ERROR;
        }

        String returnStr = "";

        if (buf1 != null) {
            excelStream = new ByteArrayInputStream(buf1.toByteArray());
            returnStr = "success";
        } else {
            returnStr = "failed";
        }

        return returnStr;
    }
    
    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }
}
