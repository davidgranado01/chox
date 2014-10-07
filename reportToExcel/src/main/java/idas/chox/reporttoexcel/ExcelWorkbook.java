package idas.chox.reporttoexcel;

import java.io.File;
import java.io.IOException;
import jxl.CellView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 *
 * @author John
 */
public class ExcelWorkbook {

    private static final Logger LOG = LoggerFactory.getLogger(ExcelWorkbook.class);
    private static final String WORKSHEET_PASSWORD = "C0mpliance";

    private final WritableWorkbook workbook;

    public ExcelWorkbook(ReportToExcel reportToExcel, String filename) throws IOException {
        workbook = Workbook.createWorkbook(new File(filename));

        WritableFont.FontName calibri = WritableFont.createFont("Calibri");
        
        // Create a cell format for the header
//        WritableFont headerFont = new WritableFont(WritableFont.ARIAL, 11, WritableFont.BOLD, true);
        WritableFont headerFont = new WritableFont(calibri, 11, WritableFont.BOLD, true);
        WritableCellFormat headerCellFormat = new WritableCellFormat(headerFont);
        try {
            headerCellFormat.setAlignment(Alignment.CENTRE);
        } catch (WriteException ex) {
            LOG.error("Cannot set cell alignment");
        }

        // Create a cell format for the body
//        WritableFont bodyFont = new WritableFont(WritableFont.ARIAL, 11, WritableFont.NO_BOLD, true);
        WritableFont bodyFont = new WritableFont(calibri, 11, WritableFont.NO_BOLD, true);
        WritableCellFormat bodyCellFormat = new WritableCellFormat(bodyFont);
        try {
            bodyCellFormat.setAlignment(Alignment.CENTRE);
        } catch (WriteException ex) {
            LOG.error("Cannot set cell alignment");
        }

        int sheetNo = 0;
        for (Report report : reportToExcel.getReports()) {
            WritableSheet sheet;
            if (report.getName() == null) {
                sheet = workbook.createSheet("Report", sheetNo);
            } else {
                sheet = workbook.createSheet(report.getName(), sheetNo);
            }

            // Add header
            int columnNo = 0;
            for (String columnHeader : report.getHeaders()) {
                Label label = new Label(columnNo, 0, columnHeader, headerCellFormat);
                try {
                    sheet.addCell(label);
//                    sheet.setColumnView(columnNo++, columnHeader.length() + 4);
                    CellView cv = sheet.getColumnView(columnNo);
                    cv.setAutosize(true);
                    sheet.setColumnView(columnNo++, cv);
                } catch (WriteException ex) {
                    LOG.error("Error adding column header cell '{}' at position {}", columnHeader, columnNo - 1);
                }
            }
            int rowNo = 1;

            for (Object[] row : report.getBody()) {
                columnNo = 0;
                for (Object cell : row) {
                    if (cell instanceof String) {
                        Label label = new Label(columnNo++, rowNo, (String) cell, bodyCellFormat);
                        try {
                            sheet.addCell(label);
                        } catch (WriteException ex) {
                            LOG.error("Error adding report cell '{}' at position ({},{})",
                                    new Object[]{cell, row, columnNo - 1});
                        }
                    }
                }
                rowNo++;
            }
            // Set protection - stops cells being edited only
//            SheetSettings sheetSettings = sheet.getSettings();
//            sheetSettings.setPassword(WORKSHEET_PASSWORD);
//            sheetSettings.setProtected(true);
        }

        // This doesn't seem to do anything
//        workbook.setProtected(true);
    }

    public void write() throws IOException {
        workbook.write();
        try {
            workbook.close();
        } catch (WriteException ex) {
            LOG.error("Exception thrown closing workbook : {}", ex.getMessage(), ex);
        }
    }
}
