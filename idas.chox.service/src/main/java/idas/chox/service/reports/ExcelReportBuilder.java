package idas.chox.service.reports;

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
    private static final String reportTemplatePath = "/reports/";

    @Override
    public ByteArrayOutputStream buildReport(Report report) throws Exception {
        LOG.info("Building report '{}'", report.getReportCode());

        boolean addLogo = true;

//        if (report.getReportCode().equals("RPT100")) {
//            addLogo = false;
//        }
        String templeteName = report.getReportTemplateFileName();
        Map reportParameters = report.getReportParameters();
        short[] columnsToHide = report.getColumnsToHide();
        LOG.debug("Report data generated - constructing report from template file '{}'", templeteName);
        return doCreateReport(reportParameters, templeteName, addLogo, columnsToHide, report.isBrandingReportFormat());
    }

    
    public HSSFWorkbook appendImage(HSSFWorkbook resultWorkbook, boolean brandingLogo) {

        int col = 1, row = 0;

        try {
            InputStream fis;
            if (brandingLogo) {
                fis = new ClassPathResource(reportTemplatePath + "erac.jpg").getInputStream();
            } else {
                fis = new ClassPathResource(reportTemplatePath + "choxLogo.jpg").getInputStream();
            }
            
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
            LOG.error("Exception adding image to report: " + ioe.getMessage(), ioe);
        }

        return resultWorkbook;
    }

    
    protected ByteArrayOutputStream doCreateReport(Map reportParameters, String templateFileName, boolean addLogo, short[] columnsToHide, boolean brandingLogo) {
        ByteArrayOutputStream out = null;
        try {
            InputStream templateIS = new ClassPathResource(reportTemplatePath + templateFileName).getInputStream();
            out = new ByteArrayOutputStream();
            XLSTransformer transformer = new XLSTransformer();
            if (columnsToHide != null) {
                transformer.setColumnsToHide(columnsToHide);
            }
            HSSFWorkbook resultWorkbook = transformer.transformXLS(templateIS, reportParameters);
            
            if (addLogo) {
                resultWorkbook = appendImage(resultWorkbook, brandingLogo);
            }
            resultWorkbook.write(out);
        } catch (Exception e) {
            LOG.error("Exception creating report: " + e.getMessage(), e);
        }
        LOG.info("Report written to stream");
        return out;
    }
}
