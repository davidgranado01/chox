package idas.chox.service.reports;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.WebUser;
import idas.chox.core.services.ReportDataService;
import idas.chox.core.util.DateHelper;
import idas.chox.core.util.RoleHelper;
import idas.chox.core.util.TextHelper;
import idas.chox.data.services.BaseDataService;
import idas.chox.service.reports.viewdata.PaymentReport;

public class InvoiceReport implements Report {
    private static final Logger LOG = LoggerFactory.getLogger(InvoiceReport.class);

    private Map externalParameter;
    private List<String> reportParameterNames;
    private BaseDataService baseDataService;
    private WebUser user = new WebUser();
    private ReportDataService reportDataService;

    @Override
    public void setBaseDataService(BaseDataService baseDataService) {
        this.baseDataService = baseDataService;
    }
    
    public InvoiceReport() {
        reportParameterNames = new ArrayList<>();
    }

    @Override
    public String getReportTemplateFileName() {
        return "template_InvoiceReport.xls";
    }

    @Override
    public void setExternalParameter(Map parameters) {
        this.externalParameter = parameters;
    }

    @Override
    public Map<String, Object> getReportParameters() throws Exception {

        Map<String, Object> reportParameters = new HashMap<>();
        Map<String, Object> paramMap = new HashMap<>();

        try {

            user = ((WebUser) externalParameter.get("CurrentUser"));

            final Date dataStart = DateHelper.parse(((String[]) externalParameter.get("DateStart"))[0]);
            Date dataEnd = DateHelper.parse(((String[]) externalParameter.get("DateEnd"))[0]);
            dataEnd = DateHelper.setEndOfDay(dataEnd);
            
            if (dataEnd == null || dataStart == null) {
                throw new Exception("Start and End dates cannot be empty.");
            }

            if (dataEnd.before(dataStart)) {
                throw new Exception("End date (" + dataEnd.toString() + ") is before start date (" + dataStart.toString() +  ") ");
            }

            // SUPPLIER ID
            String sSupplierId = "";
            if(((String[]) externalParameter.get("supplierId"))!=null){
                sSupplierId = ((String[]) externalParameter.get("supplierId"))[0];
            }
            final String supplierId = sSupplierId;

            // SUPPLIER REFERENCE
            String sListSupplierRef = "";
            if(((String[]) externalParameter.get("supplierReferences"))!=null){
                sListSupplierRef = ((String[]) externalParameter.get("supplierReferences"))[0];
            }
            final String listSupplierRef = sListSupplierRef;


            // SUPPLIER REF IN COMMA DELIMETERS
            String SupplierRefs = "";
            if (listSupplierRef.length() > 0) {
                SupplierRefs = TextHelper.getComma(listSupplierRef);
            }

            Integer insurerId = -1;
            String rptInsurerName = "";
            String rptInsurerAddress = "";

            // GET INSURER INFORMATION
            if (RoleHelper.isInsurerUser(user)) {
                insurerId = user.getInsurer().getId();
                rptInsurerName = user.getInsurer().getName();
                rptInsurerAddress = user.getInsurer().getDisplayAddress();
            }

            Integer creditHireId = -1;
            String rptCreditHireName = "";
            String rptCreditHireAddress = "";
            String rptCreditHireCompanyNumber = "";
            String rptCreditHireVat = "";

            // GET CREDIT HIRE INFORMAITON
            if (supplierId != null && !supplierId.equalsIgnoreCase("")) {
                creditHireId = Integer.parseInt(supplierId);
                if (creditHireId > 0) {
                    Chorganisation chorganisation = getChorganisation(supplierId);
                    rptCreditHireName = chorganisation.getName();
                    rptCreditHireAddress = chorganisation.getDisplayAddress();
                    rptCreditHireCompanyNumber = chorganisation.getCompanyNo();
                    rptCreditHireVat = chorganisation.getVatNo();
                }
            }

            Integer iWorkgroupId = -1;
            if(((String[]) externalParameter.get("workgroupId"))!=null){
                iWorkgroupId = TextHelper.getId(((String[]) externalParameter.get("workgroupId"))[0]);
            }

            StringBuilder sb = new StringBuilder();
            sb.append("select * from rpt_claim_invoice ");
            sb.append("where insurer_id = :pInsurerId and chorganisation_id = :pChorganisationId ");

            sb.append("and date_trunc('day', created_date) >= :pInvUploadDateFrom ");
            paramMap.put("pInvUploadDateFrom", dataStart);

            sb.append("and date_trunc('day', created_date) <= :pInvUploadDateTo ");
            paramMap.put("pInvUploadDateTo", dataEnd);

            if (SupplierRefs.length() > 0) {
                sb.append("and cho_reference in (").append(SupplierRefs).append(") ");
            }

            if (iWorkgroupId > 0) {
                sb.append("and workgroup_id = ").append(iWorkgroupId).append(" ");
            }

            sb.append("order by cho_reference asc");
            String query = sb.toString();
            paramMap.put("pChorganisationId", creditHireId);
            paramMap.put("pInsurerId", insurerId);

            List result = reportDataService.getReportData(query, paramMap);

            List<PaymentReport> payments = new ArrayList<>();

            for (Object o : result) {
                Map data = (Map) o;
                PaymentReport payment = PaymentReport.getObject(data);
                payments.add(payment);
            }
            
            reportParameters.put("payments", payments);
            reportParameters.put("rptCreatedDate", new Date());
            reportParameters.put("rptInsurerName", rptInsurerName);
            reportParameters.put("rptInsurerAddress", rptInsurerAddress);
            reportParameters.put("rptCreditHireName", rptCreditHireName);
            reportParameters.put("rptCreditHireAddress", rptCreditHireAddress);
            reportParameters.put("rptCreditHireCompanyNumber", rptCreditHireCompanyNumber);
            reportParameters.put("rptCreditHireVat", rptCreditHireVat);
            

        } catch (Exception ex) {
            LOG.error("Error generating Invoice Report: {}", ex.getMessage());
            if (ex.getCause() != null) {
                LOG.error("Caused by: {}", ex.getCause().getMessage());
            }
            throw ex;
        }

        return reportParameters;
    }

    @Override
    public ByteArrayOutputStream build() throws Exception {
        ReportBuilder builder = getReportBuilder();
        return builder.buildReport(this);
    }

    protected ReportBuilder getReportBuilder() {
        return new ExcelReportBuilder();
    }

    @Override
    public void setReportDataService(ReportDataService reportDataService) {
        this.reportDataService = reportDataService;
    }

    @Override
    public String getReportCode() {
        return "RPT008";
    }

    private Chorganisation getChorganisation(String sObjectId) {

        Chorganisation chorganisation;
        ReportHelper reportHelper = new ReportHelper();

        if (!sObjectId.equalsIgnoreCase("")) {

            Integer iChorganisation = Integer.valueOf(sObjectId);
            chorganisation = reportHelper.getChorganisation(iChorganisation, this.baseDataService);
        } else {
            chorganisation = user.getChorganisation();
        }

        return chorganisation;
    }
    
    @Override
    public short[] getColumnsToHide() {
        return null;
    }

    @Override
    public boolean isBrandingReportFormat() {
        return externalParameter.get("isBrandingReport")==null ? false : (Boolean)externalParameter.get("isBrandingReport");
    }

}
