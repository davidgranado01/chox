package idas.chox.reporttoexcel;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author John
 */
public class ReportToExcel {

    private static final Logger LOG = LoggerFactory.getLogger(ReportToExcel.class);
    private List<Report> reports = new ArrayList<>();
    private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private static final DateFormat dateFormatInput1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final DateFormat dateFormatInput2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    
    private Report readReport(BufferedReader br) throws IOException, ReportNotFoundException {
        String[] headers;
        List<Object[]> body = new ArrayList<>();

        // 1st non-empty line MUST contain the headers
        String line = br.readLine();
        while (line != null && line.trim().isEmpty()) {
            line = br.readLine();
        }
        if (line == null) {
            LOG.warn("No report found and reached EOF.");
            throw new ReportNotFoundException("No report found and reached EOF");
        }
        headers = parseHeader(line);

        // 2nd line will be the separator - ignore
        br.readLine();

        // Now read each line until we either reach the rowcount or end-of-file
        line = br.readLine();
        int rowCount = 0;
        Object[] values;
        while (line != null) {
            if (line.startsWith("(") && (line.endsWith("rows)") || line.endsWith("row)"))) {
                break;
            }

            String[] bodyLine = parseBody(line);
            rowCount++;
            if (bodyLine.length != headers.length) {
                LOG.warn("Column count mismatch at row {} between header ({}) and body ({}): '{}'",
                        new Object[]{rowCount, headers.length, bodyLine.length, bodyLine});
            } else {
                values = convertBody(bodyLine);
                body.add(values);
            }
            line = br.readLine();
        }
        Report report = new Report();
        report.setHeaders(headers);
        report.setBody(body);

        return report;
    }

    public ReportToExcel(String inputFile) {
        // Open input file
        Report report = null;
        // Get report name from file name
        String reportName;
        try {
            reportName = inputFile.substring(inputFile.indexOf("-")+1, inputFile.lastIndexOf("-201")-1);
        } catch (Exception ex) {
            LOG.warn("Cannot determine tab name from report file name: {}", inputFile);
            reportName = "Report";
        }
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            while (true) {
                try {
                    report = readReport(br);
                } catch (IOException ex) {
                    LOG.error("IOException thrown reading report line.");
                    usageAndExit();
                } catch (ReportNotFoundException ex) {
                    LOG.info("End of file reached - finishing");
                    break;
                }
                if (report != null && report.getHeaders().length == 1 && report.getBody().size() == 1) {
                    // We have a label for the following report
                    Report label = report;
                    LOG.info("Found report label: '{}'", label.getBody().get(0)[0]);
                    try {
                        report = readReport(br);
                        report.setName((String) label.getBody().get(0)[0]);
                    } catch (IOException ex) {
                        LOG.error("IOException thrown reading report line.");
                        usageAndExit();
                    } catch (ReportNotFoundException ex) {
                        LOG.warn("Found label report but no following report - adding as separate report");
                        reports.add(report);
                        break;
                    }
                } else if (report != null) {
                    report.setName(reportName);
                }

                if (report != null) {
                    reports.add(report);
                    LOG.info("File '{}' has been parsed and contains {} rows (excluding header)", inputFile, report.getBody().size());
                }
            }
        } catch (FileNotFoundException ex) {
            LOG.error("Input file '{}' could not be found.", inputFile);
            usageAndExit();
        } catch (IOException ex) {
            LOG.warn("Input file '{}' could not be closed.", inputFile);
        }
    }

    public void exportReports() {
        for (Report report : reports) {
            if (report.getName() != null) {
                System.out.println("Report for '" + report.getName() + "'");
                System.out.println();
            }
            System.out.println(report.getHeaders()[0]);
            for (int i = 1; i < report.getHeaders().length; i++) {
                System.out.print("\t" + report.getHeaders()[i]);
            }
            System.out.println();

            for (Object[] bodyLine : report.getBody()) {
                System.out.println(bodyLine[0]);
                for (int i = 1; i < bodyLine.length; i++) {
                    System.out.print("\t" + bodyLine[i]);
                }
                System.out.println();
            }
            System.out.println();
            System.out.println();
            System.out.println("==============================================================");
        }
    }

    public final Object[] convertBody(String[] bodyLine) {
        Object[] returnVals = new Object[bodyLine.length];

        int i = 0;
        for (String value : bodyLine) {
            if (Util.isMoney(value)) {
                returnVals[i] = (new Money(value)).setScale(2);
//                returnVals[i] = "£" + value;
//            } else if (Util.isInteger(value)) {
//                returnVals[i] = Integer.parseInt(value);
//            } else if (Util.isNumeric(value)) {
//                returnVals[i] = new BigDecimal(value);
            } else if (Util.isDateTime(value)) {
                String dateValue = null;
                try {
                   returnVals[i] = dateFormat.format(dateFormatInput2.parse(value));
                   LOG.debug("Date {} converted to {} with via {}", new Object[]{value, returnVals[i], dateFormatInput2.parse(value)});
                } catch (ParseException ex) {
                    try {
                        returnVals[i] = dateFormat.format(dateFormatInput1.parse(value));
                        LOG.debug("Date {} converted to {} with dateFormatInput1", value, returnVals[i]);
                    } catch (ParseException ex1) {
                        LOG.error("Cannot parse date '{}'", value);
                        returnVals[i] = value;
                    }
                }
            } else { // treat as a String
                returnVals[i] = value;
            }
            i++;
        }

        return returnVals;
    }

    public final String[] parseBody(String line) {
        String delims = "[|]";
        String[] tokens = line.split(delims);

        String[] bodyValues = new String[tokens.length];

        int i = 0;
        for (String headerString : tokens) {
            bodyValues[i++] = headerString.trim();
        }

        return bodyValues;
    }

    public final String[] parseHeader(String line) {
        String delims = "[|]";
        String[] tokens = line.split(delims);

        String[] parsedHeaders = new String[tokens.length];

        int i = 0;
        for (String headerString : tokens) {
            parsedHeaders[i++] = headerString.trim();
        }

        return parsedHeaders;
    }

    public List<Report> getReports() {
        return reports;
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            usageAndExit();
        }

        ReportToExcel report = new ReportToExcel(args[0]);
        try {
            ExcelWorkbook workbook = new ExcelWorkbook(report, args[1]);
            workbook.write();
//            PasswordProtect.protect(args[1]);
        } catch (IOException ex) {
            LOG.error("Esception thrown generating workbook: {}", ex.getMessage(), ex);
        }
    }

    private static void usageAndExit() {
        System.out.println("Usage: ReportToExcel <reportInputFile> <excelOutputFile>");
        System.exit(0);
    }
}
