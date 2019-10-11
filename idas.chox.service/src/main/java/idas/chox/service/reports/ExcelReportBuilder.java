package idas.chox.service.reports;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import net.sf.jxls.exception.ParsePropertyException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jxls.transformer.XLSTransformer;

import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.ClientAnchor.AnchorType;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.ClassPathResource;

public class ExcelReportBuilder implements ReportBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(ExcelReportBuilder.class);
    private static final String REPORT_TEMPLATE_PATH = "/reports/";

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

    public Workbook appendImage(Workbook resultWorkbook, boolean brandingLogo) {

        int col = 1, row = 0;

        InputStream fis = null;
        ByteArrayOutputStream img_bytes = null;
        try {
            if (brandingLogo) {
                fis = new ClassPathResource(REPORT_TEMPLATE_PATH + "erac.jpg").getInputStream();
            } else {
                fis = new ClassPathResource(REPORT_TEMPLATE_PATH + "choxLogo.jpg").getInputStream();
            }

            img_bytes = new ByteArrayOutputStream();
            int b;
            while ((b = fis.read()) != -1) {
                img_bytes.write(b);
            }

            HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) col, row, (short) ++col, ++row);
            int index = resultWorkbook.addPicture(img_bytes.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG);
            Sheet sheet = resultWorkbook.getSheetAt(0);
            Drawing patriarch = sheet.createDrawingPatriarch();
            patriarch.createPicture(anchor, index);
            anchor.setAnchorType(AnchorType.DONT_MOVE_DO_RESIZE);

        } catch (IOException ioe) {
            LOG.error("Exception adding image to report: " + ioe.getMessage(), ioe);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (img_bytes != null) {
                    img_bytes.close();
                }
            } catch (IOException ex) {
                LOG.error("Exception closing streams: " + ex.getMessage());
            }
        }

        return resultWorkbook;
    }

    protected ByteArrayOutputStream doCreateReport(Map reportParameters, String templateFileName, boolean addLogo, short[] columnsToHide, boolean brandingLogo) {
        ByteArrayOutputStream out = null;
        try {
            InputStream templateIS = new ClassPathResource(REPORT_TEMPLATE_PATH + templateFileName).getInputStream();
            out = new ByteArrayOutputStream();
            XLSTransformer transformer = new XLSTransformer();
            if (columnsToHide != null) {
                transformer.setColumnsToHide(columnsToHide);
            }
            Workbook resultWorkbook = transformer.transformXLS(templateIS, reportParameters);

            if (addLogo) {
                resultWorkbook = appendImage(resultWorkbook, brandingLogo);
            }
            resultWorkbook.write(out);
        } catch (IOException | ParsePropertyException | InvalidFormatException e) {
            LOG.error("Exception creating report: " + e.getMessage(), e);
        }
        LOG.info("Report written to stream");
        return out;
    }
}
