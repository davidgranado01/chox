/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.reports;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.core.io.ClassPathResource;

/**
 *
 * @author Emmanuel
 */
public class ExcelReportBuilder implements ReportBuilder {

    public InputStream buildReport(Report report) {

        String templeteName = report.getReportTemplateFileName();
        Map reportParameters = report.getReportParameters();
        ByteArrayOutputStream buf = doCreateReport(reportParameters, templeteName);
        InputStream reportStream = new ByteArrayInputStream(buf.toByteArray());
        return reportStream;
    }

    public ByteArrayOutputStream buildReport(Map reportParameters, String templatePath) {

        ByteArrayOutputStream buf = doCreateReport(reportParameters, templatePath);
        return buf;
    }

    private HSSFWorkbook appendImage(HSSFWorkbook resultWorkbook) {

        int col = 1, row = 0;

        try {

            InputStream fis = new ClassPathResource("choxLogo.jpg").getInputStream();
            ByteArrayOutputStream img_bytes = new ByteArrayOutputStream();
            int b;
            while ((b = fis.read()) != -1) {
                img_bytes.write(b);
            }
            fis.close();

            HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) col, row, (short) ++col, ++row);
            int index = resultWorkbook.addPicture(img_bytes.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG);
            HSSFSheet sheet = resultWorkbook.getSheetAt(0);
            HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
            patriarch.createPicture(anchor, index);
            anchor.setAnchorType(2);

        } catch (IOException ioe) {
            System.out.println("Error exception. " + ioe.getMessage());
        }

        return resultWorkbook;
    }

    protected ByteArrayOutputStream doCreateReport(Map reportParameters, String templatePath) {
        ByteArrayOutputStream out = null;
        try {
            InputStream templateIS = new ClassPathResource(templatePath).getInputStream();
            out = new ByteArrayOutputStream();
            XLSTransformer transformer = new XLSTransformer();
            HSSFWorkbook resultWorkbook = transformer.transformXLS(templateIS, reportParameters);
            resultWorkbook = appendImage(resultWorkbook);
            resultWorkbook.write(out);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return out;
    }
}
