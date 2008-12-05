package chox.web.actions;

import chox.data.ClaimSearchCriteria;
import chox.services.HistoryService;
import chox.services.CommentService;
import chox.model.History;
import net.sf.jxls.transformer.XLSTransformer;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.util.*;
import chox.services.ClaimService;
import chox.services.ClaimServiceImpl;
import chox.model.Claim;
import chox.model.Comment;
import java.io.IOException;
import org.apache.struts2.interceptor.SessionAware;
import chox.web.data.ExcelClaim;
import chox.web.data.ExcelInvoice;
import chox.services.WitnessService;
import chox.services.InjuryService;
import chox.services.SolicitorService;
import chox.model.Witness;
import chox.model.Injury;
import chox.model.Solicitor;
import java.io.File;
import chox.Util.FileHelper;

public class ExcelGeneratorAction extends BaseAction implements SessionAware {

    private InputStream excelStream;
    private Map session;
    private HistoryService historyService;
    private CommentService commentService;
    private WitnessService witnessService;
    private InjuryService injuryService;
    private SolicitorService solicitorService;

    public InputStream getExcelStream() {
        return excelStream;
    }

    public void setExcelStream(InputStream excelStream) {
        this.excelStream = excelStream;
    }

    /*    
    public static void main(String[] args)throws IOException {
    
    File thisFile = new File(".");
    System.out.println("A:::::::::::::"+thisFile.getParent());
    System.out.println("B:::::::::::::"+thisFile.getPath());
    System.out.println("C:::::::::::::"+thisFile.getAbsolutePath());
    System.out.println("D:::::::::::::"+thisFile.getCanonicalPath());
    
    String classPath = ExcelGeneratorAction.class.getClassLoader().getResource("claimTemplate.xls").getPath();
    int dotIndex = classPath.lastIndexOf("claimTemplate.xls");
    classPath = classPath.substring(0, classPath.lastIndexOf("claimTemplate.xls"));
    System.out.println("AS:"+classPath);
    
    //System.out.println("E:"+classPath.lastIndexOf("claimTemplate.xls"));
    //System.out.println("E:"+classPath.substring(0, classPath.lastIndexOf("claimTemplate.xls")));
    
    ClaimService cs = new ClaimServiceImpl();
    List<Claim> claims = cs.listAllClaims();
    ExcelGeneratorAction excelhelper = new ExcelGeneratorAction();
    ByteArrayOutputStream buf = excelhelper.generateXML(claims);  
    }
     */
    public ByteArrayOutputStream doExportExcel() throws IOException {

        File thisFile = new File(".");
        System.out.println("A:::::::::::::" + thisFile.getParent());
        System.out.println("B:::::::::::::" + thisFile.getPath());
        System.out.println("C:::::::::::::" + thisFile.getAbsolutePath());
        System.out.println("D:::::::::::::" + thisFile.getCanonicalPath());
        System.out.println("AS:" + FileHelper.getClassPath());

        ClaimSearchCriteria c = null;
        ByteArrayOutputStream buf = null;

        if (session != null) {

            c = (ClaimSearchCriteria) session.get("searchCriteria");

            if (c != null) {
                ClaimService cs = new ClaimServiceImpl();
                List<Claim> claims = cs.searchClaims(c);
                ExcelGeneratorAction excelhelper = new ExcelGeneratorAction();
                if (claims.size() > 0) {
                    buf = excelhelper.generateXML(claims);
                }
            }
        }
        return buf;
    }

    public ByteArrayOutputStream generateXML(List<Claim> claims) throws IOException {

        InputStream templateIS = ExcelGeneratorAction.class.getClassLoader().getResourceAsStream("claimTemplate.xls");

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        List histories = new ArrayList<History>();
        List comments = new ArrayList<Comment>();
        List<ExcelInvoice> invoices = new ArrayList<ExcelInvoice>();

        List<ExcelClaim> excelClaims = new ArrayList<ExcelClaim>();

        // Integer cCount = 0;
        // Integer vCount = 0;


        //for(Integer iCount=0; iCount<claims.size(); iCount++){
        for (Claim claim : claims) {

            ExcelClaim ec = new ExcelClaim();
            ExcelInvoice ev = new ExcelInvoice();
            ec.setClaim(claim);

            // cCount ++;

            // System.out.println("CLAIM CHO : "+claim.getChoReference());

            if (claim.getInvoice() != null) {

                ev = new ExcelInvoice();
                ev.setInvoice(claim.getInvoice());
                ev.setChoReference(claim.getChoReference());
                ev.setClaimStatus(claim.getStatus());
                invoices.add(ev);
            // vCount ++;
            // System.out.println("INVOICE CHO : "+ev.getChoReference());
            }

            if (claim.getIncident() != null) {

                // GET WITNESS
                Witness witness = witnessService.getWitnessByIncident(claim.getIncident());
                if (witness != null) {
                    ec.setWitness(witness);
                }

                // GET INJURY
                Injury injury = injuryService.getInjuryByIncident(claim.getIncident());

                if (injury != null) {

                    Solicitor solicitor = solicitorService.getSolicitorByInjury(injury);

                    if (solicitor != null) {
                        ec.setSolicitor(solicitor);
                    }

                    ec.setInjury(injury);
                }
            }

            excelClaims.add(ec);

            Boolean isShowAll = this.getIsInsurer();
            Boolean isPublic = this.getIsCHO();

            // GET HISTORY BY CLAIM ID;
            histories.addAll(historyService.getHistoryByClaim(claim, isShowAll, isPublic));

            // GET COMMENT BY CLAIM ID;
            comments.addAll(commentService.getCommentByClaimId(claim.getId()));
        }

        // System.out.println("CLAIM COUNT:"+cCount);
        // System.out.println("INVOICE COUNT:"+vCount);

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

        // String templateFileName = "C:\\Tomcat 6.0\\webapps\\CHOX\\WEB-INF\\classes\\claimTemplate.xls";
        // String destFileName = "C:\\Users\\Carlson\\Desktop\\ExcelTest\\excel_report.xls";
        // transformer.transformXLS(templateFileName, excelMap, destFileName);

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

    public void setHistoryService(HistoryService historyService) {
        this.historyService = historyService;
    }

    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }

    public void setWitnessService(WitnessService witnessService) {
        this.witnessService = witnessService;
    }

    public void setInjuryService(InjuryService injuryService) {
        this.injuryService = injuryService;
    }

    public void setSolicitorService(SolicitorService solicitorService) {
        this.solicitorService = solicitorService;
    }
}
