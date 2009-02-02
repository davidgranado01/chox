/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.report;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.struts2.ServletActionContext;

/**
 *
 * @author Emmanuel
 */
public class ExcelReportBuilder implements ReportBuilder {

    public InputStream buildReport(Report report) {

        String templeteName = report.getReportTemplateFileName();
        String templatePath = getReportTemplatePath(templeteName);
        Map reportParameters = report.getReportParameters();
        ByteArrayOutputStream buf = doCreateReport(reportParameters, templatePath);
        InputStream reportStream = new ByteArrayInputStream(buf.toByteArray());

        return reportStream;
    }
    
     public ByteArrayOutputStream buildReport(Map reportParameters, String templatePath) {

        ByteArrayOutputStream buf = doCreateReport(reportParameters, templatePath);       
        return buf;
    }

    protected ByteArrayOutputStream doCreateReport(Map reportParameters, String templatePath) {
        ByteArrayOutputStream out = null;
        try {
            InputStream templateIS = new FileInputStream(templatePath);
            out = new ByteArrayOutputStream();
            XLSTransformer transformer = new XLSTransformer();
            transformer.transformXLS(templateIS, reportParameters).write(out);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return out;
    }

    protected String getReportTemplatePath(String reportTemplateName) {
        String reportDefinationFilePath = ServletActionContext.getServletContext().getRealPath("/reports/" + reportTemplateName);
        //String reportDefinationFilePath = "C:\\Greenfinch\\Projects\\CHOX\\reports\\" + reportTemplateName;
        return reportDefinationFilePath;
    }
}
