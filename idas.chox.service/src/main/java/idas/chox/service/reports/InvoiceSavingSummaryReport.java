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
import idas.chox.core.model.Insurer;
import idas.chox.core.model.WebUser;
import idas.chox.core.services.ReportDataService;
import idas.chox.core.util.DateHelper;
import idas.chox.data.services.BaseDataService;
import idas.chox.service.reports.viewdata.InvoiceSavingSummaryReportObject;
import idas.chox.service.reports.viewdata.InvoiceSavingSummaryReportViewData;

public class InvoiceSavingSummaryReport implements Report {
    private static final Logger LOG = LoggerFactory.getLogger(InvoiceSavingSummaryReport.class);

    private Map externalParameter;
    private List<String> reportParameterNames;
    private BaseDataService baseDataService;
    private WebUser user = new WebUser();
    private ReportDataService reportDataService;

    @Override
    public void setBaseDataService(BaseDataService baseDataService) {
        this.baseDataService = baseDataService;
    }
    
    public InvoiceSavingSummaryReport() {
        reportParameterNames = new ArrayList<String>();
    }

    @Override
    public String getReportTemplateFileName() {
        return "template_InvoiceSavingSummaryReport.xls";
    }

    @Override
    public void setExternalParameter(Map parameters) {
        this.externalParameter = parameters;
    }

    @Override
    public Map<String, Object> getReportParameters() throws Exception {

        Map<String, Object> reportParameters = new HashMap<String, Object>();

        try {

            user = ((WebUser) externalParameter.get("CurrentUser"));
            // PermissionedUser currentUser = ((PermissionedUser) externalParameter.get("CurrentUser"));
            // user = currentUser.getUser();

            final Date dataStart = DateHelper.getLocalDateFormat().parse(((String[]) externalParameter.get("DateStart"))[0]);
            Date dataEnd = DateHelper.getLocalDateFormat().parse(((String[]) externalParameter.get("DateEnd"))[0]);
            dataEnd = DateHelper.setEndOfDay(dataEnd);
            
            if (dataEnd == null || dataStart == null) {
                throw new Exception("Start and End dates cannot be empty.");
            }

            if (dataEnd.before(dataStart)) {
                throw new Exception("End date (" + dataEnd.toString() + ") is before start date (" + dataStart.toString() +  ") ");
            }


            final String insurerId = ((String[]) externalParameter.get("insurerId"))[0];
            final String chOrganisationId = ((String[]) externalParameter.get("chOrganisationId"))[0];

            String insurerName;
            String chOrganisationName;
            Integer iInsurerId;
            Integer iChOrganisationId;

            // GET INSURER NAME AND ID
            Insurer ins = getInsurer(insurerId);
            iInsurerId = ins.getId();
            insurerName = ins.getName();

            // GET CREDIT HIRE NAME AND ID
            Chorganisation chorganisation = getChorganisation(chOrganisationId);
            iChOrganisationId = chorganisation.getId();
            chOrganisationName = chorganisation.getName();

            List<InvoiceSavingSummaryReportViewData> reportRows = new ArrayList<InvoiceSavingSummaryReportViewData>();

            StringBuilder sb = new StringBuilder();
            sb.append("select claim.cho_reference as supplier_reference_number, claim.claim_number as claim_number, ");
            sb.append("case when third_party.vehicle_registration is null then '-' else third_party.vehicle_registration end as policy_holder_vehicle_registeration_number, ");
            sb.append("case when workgroup.name is null then '-' else workgroup.name end as workgroup_name, ");
            sb.append("invoiceOriginal.total_to_pay as original_invoice_amount, invoice.total_to_pay as agreed_settlement_value ");
            sb.append("from invoice_original invoiceOriginal, claim claim inner join invoice invoice on claim.invoice_id=invoice.id ");
            sb.append("left outer join workgroup workgroup on workgroup.id=claim.workgroup_id ");
            sb.append("left outer join third_party third_party on third_party.id=claim.third_party_id ");
            sb.append("where invoiceOriginal.id = invoice.invoice_original_id ");
            sb.append("and claim.id in (select distinct claim_id from audit_trail where new_status='InvoicePaymentLogged' and reverted=false) ");
            sb.append("and claim.insurer_id = :pInsurerId and claim.chorganisation_id = :pChorganisationId ");
            sb.append("and date(invoice.created_date) between :pInvUploadDateFrom and :pInvUploadDateTo ");
            sb.append("order by (invoiceOriginal.total_to_pay - invoice.total_to_pay) desc");
            String query = sb.toString();

            Map paramMap = new HashMap();
            paramMap.put("pChorganisationId", iChOrganisationId);
            paramMap.put("pInsurerId", iInsurerId);
            paramMap.put("pInvUploadDateFrom", dataStart);
            paramMap.put("pInvUploadDateTo", dataEnd);

            List result = reportDataService.getReportData(query, paramMap);

            for (Object o : result) {

                Map data = (Map) o;
                InvoiceSavingSummaryReportViewData reportRow = InvoiceSavingSummaryReportViewData.getObject(data);
                reportRows.add(reportRow);
            }

            InvoiceSavingSummaryReportObject reportObject = new InvoiceSavingSummaryReportObject();
            reportObject.setCreatedDate(new Date());
            reportObject.setInvoiceUploadDateFrom(dataStart);
            reportObject.setInvoiceUploadDateTo(dataEnd);
            reportObject.setCreditHireName(chOrganisationName);
            reportObject.setInsurerName(insurerName);

            reportParameters.put("reportObject", reportObject);
            reportParameters.put("reportRows", reportRows);

        } catch (Exception ex) {
            LOG.error("Error generating Invoice Savings Summary Report: {}", ex.getMessage());
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
        return "RPT007";
    }

    private Insurer getInsurer(String sObjectId) {

        Insurer insurer;
        ReportHelper reportHelper = new ReportHelper();

        if (!sObjectId.equalsIgnoreCase("")) {
            insurer = reportHelper.getInsurer(Integer.valueOf(sObjectId), this.baseDataService);
        } else {
            insurer = user.getInsurer();
        }

        return insurer;

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
        return (Boolean) externalParameter.get("isBrandingReport");
    }

}
