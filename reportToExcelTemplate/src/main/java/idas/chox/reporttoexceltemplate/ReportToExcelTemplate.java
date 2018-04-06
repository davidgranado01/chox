package idas.chox.reporttoexceltemplate;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author John
 */
public class ReportToExcelTemplate {

    private static final Logger LOG = LoggerFactory.getLogger(ReportToExcelTemplate.class);

    public ReportToExcelTemplate(String inputFile, String outputFile) {
        String line;
        int rowCount = 0;

        // Get template
        ReportTemplate reportTemplate = new ReportTemplate(inputFile);
        InputStream template = ReportToExcelTemplate.class.getClassLoader().getResourceAsStream(reportTemplate.getTemplateName());

        try {
            HSSFWorkbook workbook = new HSSFWorkbook(template);
            HSSFSheet worksheet = workbook.getSheetAt(0);

            try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
                // ignore first two lines (header and separator)
                br.readLine();
                br.readLine();
                line = br.readLine();
                while (line != null) {
                    rowCount++;
                    // Parse line
                    String[] row = parseBody(line);
                    LOG.debug("Row contains {} entries: {}", row.length, row);
                    if (row.length > 1) {
                        // Create an excel row
                        worksheet.shiftRows(rowCount + reportTemplate.getRowOffset(), worksheet.getLastRowNum(), 1);
                        HSSFRow newRow = worksheet.createRow(rowCount + reportTemplate.getRowOffset());
                        HSSFRow formatRow = worksheet.getRow(rowCount + reportTemplate.getRowOffset() + 1);
                        for (int i = 0; i < row.length; i++) {
                            // Grab a copy of the old/new cell
                            HSSFCell newCell = newRow.createCell(i);
                            HSSFCellStyle cellStyle = workbook.createCellStyle();
                            HSSFCell existingCell = formatRow.getCell(i);
//                            if (existingCell.getCellType() ==1)
//                                LOG.info("Copying style from cell with value [type={}] '{}'", existingCell.getCellType(), existingCell.getStringCellValue());
                            cellStyle.cloneStyleFrom(existingCell.getCellStyle());
//                            cellStyle.setAlignment(existingCell.getCellStyle().getAlignment());
//                            newCell.setCellType(existingCell.getCellType());
                            //           newCell.setCellType(Cell.CELL_TYPE_STRING);
                            cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
                            cellStyle.setBorderTop(CellStyle.BORDER_THIN);
                            cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                            cellStyle.setBorderRight(CellStyle.BORDER_THIN);
                            newCell.setCellStyle(cellStyle);

                            newCell.setCellValue(row[i]);
                        }
                        line = br.readLine();
                    } else {
                        line = null;
                    }
                }
                HSSFRow formatRow = worksheet.getRow(rowCount + reportTemplate.getRowOffset());
                worksheet.removeRow(formatRow);
                worksheet.shiftRows(rowCount + reportTemplate.getRowOffset()+1, worksheet.getLastRowNum(), -1);
                LOG.info("Processed {} rows", rowCount);
            } catch (FileNotFoundException ex) {
                LOG.error("Input file '{}' could not be found.", inputFile);
                usageAndExit();
            } catch (IOException ex) {
                LOG.warn("Input file '{}' could not be closed.", inputFile);
            }

            try (
                    FileOutputStream out = new FileOutputStream(outputFile)) {
                workbook.write(out);
            }
        } catch (IOException ex) {
            LOG.error("IOException thrown : {}", ex.getMessage(), ex);
        }
    }

    private String[] parseBody(String line) {
        String delims = "[|]";
        String[] tokens = line.split(delims);

        String[] bodyValues = new String[tokens.length];

        int i = 0;
        for (String headerString : tokens) {
            if (headerString.trim().startsWith("~~")) {
                bodyValues[i++] = "GDPR: data removed";
            } else {
                bodyValues[i++] = headerString.trim();
            }
        }

        return bodyValues;
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            usageAndExit();
        }

        ReportToExcelTemplate report = new ReportToExcelTemplate(args[0], args[1]);

    }

    private static void usageAndExit() {
        System.out.println("Usage: AxNotificationReportToExcel <reportInputFile> <excelOutputFile>");
        System.exit(0);
    }

}
