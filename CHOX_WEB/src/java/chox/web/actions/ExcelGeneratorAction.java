package chox.web.actions;

import chox.data.ClaimSearchCriteria;
import chox.services.HistoryService;
import chox.services.HistoryServiceImpl;
import chox.services.CommentService;
import chox.services.CommentServiceImpl;
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
import chox.services.WitnessServiceImpl;
import chox.services.InjuryService;
import chox.services.InjuryServiceImpl;
import chox.services.SolicitorService;
import chox.services.SolicitorServiceImpl;
import chox.model.Witness;
import chox.model.Injury;
import chox.model.Solicitor;
import chox.model.Invoice;

public class ExcelGeneratorAction extends BaseAction implements SessionAware{
    
    private InputStream excelStream;
    private Map session;
    
    public InputStream getExcelStream() {
        return excelStream;
    }

    public void setExcelStream(InputStream excelStream) {
        this.excelStream = excelStream;
    }
    
    
     public static void main(String[] args)throws IOException {
            ClaimService cs = new ClaimServiceImpl();
            List<Claim> claims = cs.listAllClaims();
            ExcelGeneratorAction excelhelper = new ExcelGeneratorAction();
            ByteArrayOutputStream buf = excelhelper.generateXML(claims);
     }
    
    
    public ByteArrayOutputStream doExportExcel()throws IOException{

        ClaimSearchCriteria c = null;
        
        if(session!=null){
            c = (ClaimSearchCriteria)session.get("searchCriteria");
        }

        ByteArrayOutputStream buf = null;
        
        ClaimService cs = new ClaimServiceImpl();
        List<Claim> claims = cs.searchClaims(c);
        
        ExcelGeneratorAction excelhelper = new ExcelGeneratorAction();
        if(claims.size()>0){
            buf = excelhelper.generateXML(claims);
        }
        
        return buf;
    }

    public ByteArrayOutputStream generateXML(List<Claim> claims) throws IOException{
        //InputStream templateIS = Thread.currentThread().getContextClassLoader().getResourceAsStream("claimTemplate.xls");
        InputStream templateIS = ExcelGeneratorAction.class.getClassLoader().getResourceAsStream("claimTemplate.xls");
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        HistoryService historyService = new HistoryServiceImpl();
        CommentService commentService = new CommentServiceImpl();
        WitnessService witnessService = new WitnessServiceImpl();
        InjuryService injuryService = new InjuryServiceImpl();
        SolicitorService solicitorService = new SolicitorServiceImpl();
        
        List histories = new ArrayList<History>();
        List comments = new ArrayList<Comment>();
        List<ExcelInvoice> invoices = new ArrayList<ExcelInvoice>();
        
        List<ExcelClaim> excelClaims = new ArrayList<ExcelClaim>();
        
        //for(Integer iCount=0; iCount<claims.size(); iCount++){
        for(Claim claim : claims){
            
            ExcelClaim ec = new ExcelClaim();
            ExcelInvoice ev = new ExcelInvoice();
            ec.setClaim(claim);
            
            if(claim.getInvoice()!=null){
                ev.setInvoice(claim.getInvoice());
                ev.setChoReference(claim.getChoReference());
                ev.setClaimStatus(claim.getStatus());
                invoices.add(ev);
            }
            
            if(claim.getIncident()!=null){
                
                // GET WITNESS
                Witness witness = witnessService.getWitnessByIncident(claim.getIncident());
                if(witness!=null){
                    ec.setWitness(witness);
                }
                // GET INJURY
                Injury injury = injuryService.getInjuryByIncident(claim.getIncident());
                
                if(injury!=null){
                    
                    Solicitor solicitor = solicitorService.getSolicitorByInjury(injury);
                    
                    if(solicitor!=null){
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
        
        if(histories.size()<=0){
            histories = new ArrayList<History>();
        }
        
        if(comments.size()<=0){
            comments = new ArrayList<Comment>();
        }
        
        Map excelMap = new HashMap();
        excelMap.put("excelclaims", excelClaims);
        excelMap.put("excelinvoices", invoices);
        excelMap.put("histories", histories);
        excelMap.put("comments", comments);

        //String templateFileName = "C:\\Project Workplace\\Greefinch\\choxida\\trunk\\CHOX_WEB\\web\\excelTemplate\\claimTemplate.xls";
        //String destFileName = "C:\\Users\\Carlson\\Desktop\\ExcelTest\\excel_report.xls";
        
        XLSTransformer transformer = new XLSTransformer();
        //transformer.transformXLS(templateFileName, excelMap, destFileName);
        transformer.transformXLS(templateIS, excelMap).write(out);
        
        /*
        XLSTransformer transformer = new XLSTransformer();
        HSSFWorkbook results  = transformer.transformXLS(Thread.currentThread().getContextClassLoader().getResourceAsStream("daysToProvideInstructions.xls"), beans);
         */

        excelMap.clear();
        return out;
    }
    
    public String execute() throws Exception {
        
        ByteArrayOutputStream buf = doExportExcel();
        String returnStr = "";
        
        if(buf != null){
            String excelString = buf.toString();
            excelStream = new ByteArrayInputStream(excelString.getBytes(), 0, excelString.length());
            returnStr = "success";
        }else{
            returnStr = "failed";
        }
        
        return returnStr;
    }

    public void setSession(Map session) {
        this.session = session;
    }
}
