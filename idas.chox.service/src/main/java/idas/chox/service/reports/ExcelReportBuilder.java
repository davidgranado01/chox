package idas.chox.service.reports;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.core.io.ClassPathResource;

public class ExcelReportBuilder implements ReportBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(ExcelReportBuilder.class);

    @Override
    public InputStream buildReport(Report report) {
//        if (!report.canAcess()) {
//            LOG.error("AccessDeniedException thrown accessing report '{}'", report.getReportCode());
//            throw new AccessDeniedException("Trying to access report '" + report.getReportCode() + "' (ILLEGAL ACCESS ATTEMPT)");
//        }
        boolean addLogo = true;

        if (report.getReportCode().equals("RPT100"))
            addLogo=false;
        String templeteName = report.getReportTemplateFileName();
        Map reportParameters = report.getReportParameters();
        ByteArrayOutputStream buf = doCreateReport(reportParameters, templeteName, addLogo);
        InputStream reportStream = new ByteArrayInputStream(buf.toByteArray());
        return reportStream;
    }

    public ByteArrayOutputStream buildReport(Map reportParameters, String templatePath) {

        ByteArrayOutputStream buf = doCreateReport(reportParameters, templatePath, true);
        return buf;
    }

    public HSSFWorkbook appendImage(HSSFWorkbook resultWorkbook) {

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
            LOG.error("Exception adding image to report: " + ioe.getMessage());
        }

        return resultWorkbook;
    }

    protected ByteArrayOutputStream doCreateReport(Map reportParameters, String templatePath, boolean addLogo) {
        ByteArrayOutputStream out = null;
        try {
            InputStream templateIS = new ClassPathResource(templatePath).getInputStream();
            out = new ByteArrayOutputStream();
            XLSTransformer transformer = new XLSTransformer();
            HSSFWorkbook resultWorkbook = transformer.transformXLS(templateIS, reportParameters);
            if (addLogo)
                resultWorkbook = appendImage(resultWorkbook);
            resultWorkbook.write(out);

        } catch (Exception e) {
            LOG.error("Exception creating report: " + e.getMessage());
        }

        return out;
    }
}
