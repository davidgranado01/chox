package idas.chox.service.reports;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.WebUser;
import idas.chox.core.util.DateHelper;
import idas.chox.core.util.RoleHelper;
import idas.chox.core.util.TextHelper;
import idas.chox.data.services.DataService;
import idas.chox.service.reports.viewdata.PaymentReport;
import idas.chox.service.security.PermissionedUser;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvoiceReport implements Report {

    Map externalParameter;
    List<String> reportParameterNames;
    private DataService dataService;
    private WebUser user = new WebUser();

    public InvoiceReport() {
        reportParameterNames = new ArrayList<String>();
    }

    public String getReportTemplateFileName() {
        return "template_InvoiceReport.xls";
    }

    public void setExternalParameter(Map parameters) {
        this.externalParameter = parameters;
    }

    //TODO: CHECK REPORT
    public HashMap getReportParameters() {

        HashMap reportParameters = new HashMap();
        Map paramMap = new HashMap();

        try {

            PermissionedUser currentUser = ((PermissionedUser) externalParameter.get("CurrentUser"));
            user = currentUser.getUser();

            final Date dataStart = DateHelper.Parse(((String[]) externalParameter.get("DateStart"))[0]);
            final Date dataEnd = DateHelper.Parse(((String[]) externalParameter.get("DateEnd"))[0]);

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
            if (!supplierId.equalsIgnoreCase("") && supplierId != null) {
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

            StringBuffer sb = new StringBuffer();
            sb.append("Select invoice.* from rpt_claim_invoice invoice ");
            sb.append("where insurer_id = :pInsurerId and chorganisation_id = :pChorganisationId ");

            if (dataStart != null) {
                sb.append("and date_trunc('day', invoice.created_date) >= :pInvUploadDateFrom ");
                paramMap.put("pInvUploadDateFrom", dataStart);
            }

            if (dataEnd != null) {
                sb.append("and date_trunc('day', invoice.created_date) <= :pInvUploadDateTo ");
                paramMap.put("pInvUploadDateTo", dataEnd);
            }

            if (SupplierRefs.length() > 0) {
                sb.append("and invoice.cho_reference in (" + SupplierRefs + ") ");
            }

            if (iWorkgroupId > 0) {
                sb.append("and invoice.workgroup_id = " + iWorkgroupId + " ");
            }

            sb.append("order by cho_reference asc");
            String query = sb.toString();
            paramMap.put("pChorganisationId", creditHireId);
            paramMap.put("pInsurerId", insurerId);

            List result = dataService.externalQuery(query, paramMap);

            List<PaymentReport> payments = new ArrayList<PaymentReport>();

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
            ex.printStackTrace();
        }

        return reportParameters;
    }

    public InputStream build() {
        ReportBuilder builder = getReportBuilder();
        return builder.buildReport(this);
    }

    protected ReportBuilder getReportBuilder() {
        return new ExcelReportBuilder();
    }

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }

    public String getReportCode() {
        return "RPT008";
    }

    private Chorganisation getChorganisation(String sObjectId) {

        Chorganisation chorganisation = new Chorganisation();
        ReportHelper reportHelper = new ReportHelper();

        if (!sObjectId.equalsIgnoreCase("")) {

            Integer iChorganisation = Integer.valueOf(sObjectId);
            chorganisation = reportHelper.getChorganisation(iChorganisation, this.dataService);
        } else {
            chorganisation = user.getChorganisation();
        }

        return chorganisation;

    }
}
