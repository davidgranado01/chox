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
import javax.servlet.http.HttpServlet;

public class ExcelGeneratorAction extends HttpServlet implements SessionAware{
    
    private InputStream excelStream;
    private Map session;
    
    public InputStream getExcelStream() {
        return excelStream;
    }

    public void setExcelStream(InputStream excelStream) {
        this.excelStream = excelStream;
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

        InputStream templateIS = ExcelGeneratorAction.class.getClassLoader().getResourceAsStream("claimTemplate.xls");
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        HistoryService historyService = new HistoryServiceImpl();
        CommentService commentService = new CommentServiceImpl();
        
        List histories = new ArrayList<History>();
        List comments = new ArrayList<Comment>();
        
        for(Integer iCount=0; iCount<claims.size(); iCount++){
            
            // GET HISTORY BY CLAIM ID;
            histories.addAll(historyService.getHistoryByClaim(claims.get(iCount)));
            
            // GET COMMENT BY CLAIM ID;
            comments.addAll(commentService.getCommentByClaim(claims.get(iCount)));
        }
        
        Map excelMap = new HashMap();
        excelMap.put("claims", claims);
        excelMap.put("histories", histories);
        excelMap.put("comments", comments);

        XLSTransformer transformer = new XLSTransformer();
        // transformer.transformXLS(templateFileName, excelMap, destFileName);
        
        transformer.transformXLS(templateIS, excelMap).write(out);
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
