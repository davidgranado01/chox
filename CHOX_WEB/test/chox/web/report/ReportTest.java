/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.report;

import chox.web.report.viewdata.InvoiceSummary;
import chox.web.report.viewdata.InvoiceSummaryReportObject;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
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

    /**
     * Test of build method, of class Report.
     */
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
        is1.setChoName("Driver Assit");
        is1.setNoInvoiceSubmitted(BigInteger.valueOf(10));
        is1.setTotalInvoiceValue(BigDecimal.valueOf(500.50));
        is1.setNoInvoicesPaid(BigInteger.valueOf(500));
        is1.setValueOfPaidInvoices(BigDecimal.valueOf(500.50));
        //result.setAverageInvoiceValue((BigDecimal)data.get("averageInvoiceValue".toLowerCase()));
        is1.setAverageInvoiceValue(BigDecimal.ZERO);
        is1.setNoInvoiceAwaitingPayment(BigInteger.valueOf(500));
        is1.setInvoiceAwaitingPaymentValue(BigDecimal.valueOf(500.50));
        is1.setNoInvoicePending(BigInteger.valueOf(500));
        is1.setInvoicePendingValue(BigDecimal.valueOf(500.50));
        is1.setNoInvoiceWithdrawn(BigInteger.valueOf(500));
        is1.setInvoiceWithdrawnValue(BigDecimal.valueOf(500.50));
        invoiceSummaries.add(is1);

        InvoiceSummary is2 = new InvoiceSummary();
        is2.setChoName("ABC Org");
        is2.setNoInvoiceSubmitted(BigInteger.valueOf(10));
        is2.setTotalInvoiceValue(BigDecimal.valueOf(500.50));
        is2.setNoInvoicesPaid(BigInteger.valueOf(500));
        is2.setValueOfPaidInvoices(BigDecimal.valueOf(500.50));
        //result.setAverageInvoiceValue((BigDecimal)data.get("averageInvoiceValue".toLowerCase()));
        is2.setAverageInvoiceValue(BigDecimal.ZERO);
        is2.setNoInvoiceAwaitingPayment(BigInteger.valueOf(500));
        is2.setInvoiceAwaitingPaymentValue(BigDecimal.valueOf(500.50));
        is2.setNoInvoicePending(BigInteger.valueOf(500));
        is2.setInvoicePendingValue(BigDecimal.valueOf(500.50));
        is2.setNoInvoiceWithdrawn(BigInteger.valueOf(500));
        is2.setInvoiceWithdrawnValue(BigDecimal.valueOf(500.50));
        invoiceSummaries.add(is2);

        InvoiceSummaryReportObject reportObject = new InvoiceSummaryReportObject();

        reportObject.setInvoiceUploadDateFrom("2008-11-01");
        reportObject.setInvoiceUploadDateTo("2009-01-31");
        reportObject.setCreatedDate("2009-01-31");
        reportObject.setName("Invoice Summary Report");

        reportParameters.put("invoiceSummaries", invoiceSummaries);
        reportParameters.put("reportObj", reportObject);
        reportParameters.put("insurerObj", reportObject);
        return reportParameters;
    }
}