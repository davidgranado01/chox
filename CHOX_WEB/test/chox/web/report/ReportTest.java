/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.report;

import chox.Util.DateHelper;
import chox.web.report.viewdata.AverageSettlementAmountViewData;
import chox.web.report.viewdata.InvoiceSummary;
import chox.web.report.viewdata.InvoiceSummaryReportObject;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Emmanuel
 */
public class ReportTest {

    public ReportTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

        @Test
    public void testBuildInvoiceSummaryReport() throws ParseException {
        
        final Date startDate = DateHelper.LocalDateFormat.parse("2/01/2009");
        final Date endDate = DateHelper.LocalDateFormat.parse("01/07/2009");
        
        // DEFINE START DATE TO FIRST DAY OF START MONTH
        Date tDateFrom = DateHelper.LocalDateFormat.parse("2/01/2009");
        tDateFrom.setDate(1);

        // DEFINE END DATE TO FIRST DAT OF NEXT MONTH
        Date tDateTo = DateHelper.LocalDateFormat.parse("01/07/2009");
        tDateTo = DateHelper.addMonth(tDateTo, 1);
        tDateTo.setDate(1);
        
        do{
            
            System.out.println(">>>>>"+tDateFrom);
            
            int selectedMonth = DateHelper.getMonth(tDateFrom);
            int selectedYear = DateHelper.getYear(tDateFrom);
            
            // DO SOMETHING
            // SET VALUE
            
            AverageSettlementAmountViewData reportRow = new AverageSettlementAmountViewData();
            reportRow.setMonth(selectedMonth);
            reportRow.setYear(selectedYear);

            System.out.println("mm-YYYY : "+reportRow.getLabelTitle());
            
            tDateFrom = DateHelper.addMonth(tDateFrom, 1);
        }while(tDateFrom.before(tDateTo));

    }

    /**
    @Test
    public void testBuildInvoiceSummaryReport() {

        String templatePath = getReportTemplatePath("template_InvoiceSummaryReport2.xls");

        HashMap reportParameters = getReportParameters();

        ExcelReportBuilder builder = new ExcelReportBuilder();
        ByteArrayOutputStream data = builder.buildReport(reportParameters, templatePath);

        String filename = "C:\\Greenfinch\\InvoiceSummaryReport.xls";

        try {
            FileOutputStream fout = new FileOutputStream(filename);
            fout.write(data.toByteArray());
            fout.flush();
            fout.close();
        } catch (Exception ex) {
        }
    }

    protected String getReportTemplatePath(String reportTemplateName) {
        //String reportDefinationFilePath = ServletActionContext.getServletContext().getRealPath("/jsp/reports/" + reportTemplateName);
        String reportDefinationFilePath = "C:\\Greenfinch\\Projects\\CHOX\\reports\\" + reportTemplateName;
        return reportDefinationFilePath;
    }

    public HashMap getReportParameters() {
        HashMap reportParameters = new HashMap();

        List<InvoiceSummary> invoiceSummaries = new ArrayList<InvoiceSummary>();

        InvoiceSummary is1 = new InvoiceSummary();
        is1.setOrgName("Driver Assit");
        is1.setNoInvoiceSubmitted(Integer.valueOf(10));
        is1.setTotalInvoiceValue(BigDecimal.valueOf(500.50));
        is1.setNoInvoicesPaid(Integer.valueOf(500));
        is1.setValueOfPaidInvoices(BigDecimal.valueOf(500.50));
        //result.setAverageInvoiceValue((BigDecimal)data.get("averageInvoiceValue".toLowerCase()));
        is1.setAverageInvoiceValue(BigDecimal.ZERO);
        is1.setNoInvoiceAwaitingPayment(Integer.valueOf(500));
        is1.setInvoiceAwaitingPaymentValue(BigDecimal.valueOf(500.50));
        is1.setNoInvoicePending(Integer.valueOf(500));
        is1.setInvoicePendingValue(BigDecimal.valueOf(500.50));
        is1.setNoInvoiceWithdrawn(Integer.valueOf(500));
        is1.setInvoiceWithdrawnValue(BigDecimal.valueOf(500.50));
        invoiceSummaries.add(is1);

        InvoiceSummary is2 = new InvoiceSummary();
        is2.setOrgName("ABC Org");
        is2.setNoInvoiceSubmitted(Integer.valueOf(10));
        is2.setTotalInvoiceValue(BigDecimal.valueOf(500.50));
        is2.setNoInvoicesPaid(Integer.valueOf(500));
        is2.setValueOfPaidInvoices(BigDecimal.valueOf(500.50));
        //result.setAverageInvoiceValue((BigDecimal)data.get("averageInvoiceValue".toLowerCase()));
        is2.setAverageInvoiceValue(BigDecimal.ZERO);
        is2.setNoInvoiceAwaitingPayment(Integer.valueOf(500));
        is2.setInvoiceAwaitingPaymentValue(BigDecimal.valueOf(500.50));
        is2.setNoInvoicePending(Integer.valueOf(500));
        is2.setInvoicePendingValue(BigDecimal.valueOf(500.50));
        is2.setNoInvoiceWithdrawn(Integer.valueOf(500));
        is2.setInvoiceWithdrawnValue(BigDecimal.valueOf(500.50));
        invoiceSummaries.add(is2);

        InvoiceSummaryReportObject reportObject = new InvoiceSummaryReportObject();
        reportParameters.put("invoiceSummaries", invoiceSummaries);
        reportParameters.put("reportObj", reportObject);
        reportParameters.put("insurerObj", reportObject);
        return reportParameters;
    }
    */
}