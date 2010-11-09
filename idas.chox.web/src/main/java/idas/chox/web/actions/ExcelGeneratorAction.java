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
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.core.io.ClassPathResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelGeneratorAction extends BaseAction implements SessionAware {

    private static final Logger LOG = LoggerFactory.getLogger(ExcelGeneratorAction.class);

    private InputStream excelStream;
    private Map session;
    private ClaimService claimService;
    ByteArrayOutputStream buf1;
    byte[] b;

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

            c = (ClaimSearchCriteria) session.get("searchReportCriteria");

            if (c != null && c.getLimit()>0) {
                SearchResult searchResult = claimService.searchClaims(c);
                List claims = searchResult.getResult();
                if (claims.size() > 0) {
                    LOG.debug("Total No of Claims : '{}'", claims.size());
                    try{
                       buf=generateXML(claims);
                    }catch(Exception e){

                         LOG.debug("EXCEPTION THROWN IN generateXML(claims) METHOD : '{}'", e.getStackTrace());

                    }
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

        InputStream templateIS = new ClassPathResource("claimTemplate.xls").getInputStream();
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
                if (claim.getThirdParty() != null)
                    ev.setThirdPartyClaimReference(claim.getThirdParty().getClaimReference());
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
        
        List<ExcelClaim> excelClaims1 = (List<ExcelClaim>)excelMap.get("excelclaims");
          List<ExcelInvoice> invoice1 = (List<ExcelInvoice>)excelMap.get("excelinvoices");
            List<History> histories1 = (List<History>)excelMap.get("histories");
              List<Comment> comments1 = (List<Comment>)excelMap.get("comments");




        LOG.debug(" Total Size of the passing excelclaims are : '{}'",  excelClaims1.size());
        LOG.debug(" Total Size of the passing excelinvoice are : '{}'",  invoice1.size());
        LOG.debug(" Total Size of the passing histories are : '{}'",  histories1.size());
        LOG.debug(" Total Size of the passing comments are : '{}'",  comments1.size());



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

         
        try{
            buf1 = doExportExcel();
        }catch(Exception e){

            LOG.debug("EXCEPTION THROWN IN ASSIGNING doExportExcel() TO buf1  : '{}'", e.getStackTrace());

        }
        
        String returnStr = "";

        if (buf1 != null) {
             
        try{
            b=buf1.toByteArray();
        }catch(Exception e){

            LOG.debug("EXCEPTION THROWN IN buf1.toByteArray() METHOD  : '{}'", e.getStackTrace());

        }

        try{
            excelStream = new ByteArrayInputStream(b);
        }catch(Exception e){

            LOG.debug("EXCEPTION THROWN IN ASSIGNING excelStream = new ByteArrayInputStream(b) METHOD : '{}'", e.getStackTrace());

        }

            
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
