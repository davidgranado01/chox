package chox.web.report;

import chox.Util.DateHelper;
import chox.model.Chorganisation;
import chox.model.Insurer;
import chox.model.WebUser;
import chox.services.ChorganisationService;
import chox.services.DataService;
import chox.services.InsurerService;
import chox.web.actions.BaseAction;
import chox.web.report.viewdata.InvoiceSavingSummaryReportObject;
import chox.web.report.viewdata.InvoiceSavingSummaryReportViewData;
import chox.web.security.PermissionedUser;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvoiceSavingSummaryReport extends BaseAction implements Report{

    Map externalParameter;
    List<String> reportParameterNames;
    private DataService dataService;
    
    WebUser user = new WebUser();

    public InvoiceSavingSummaryReport() {
        reportParameterNames = new ArrayList<String>();
    }

    public String getReportTemplateFileName() {
        return "template_InvoiceSavingSummaryReport.xls";
    }

    public void setExternalParameter(Map parameters) {
        this.externalParameter = parameters;
    }
    
    public HashMap getReportParameters() {

        HashMap reportParameters = new HashMap();

        try {

            PermissionedUser currentUser = ((PermissionedUser) externalParameter.get("CurrentUser"));
            user = currentUser.getUser();

            final Date dataStart = DateHelper.LocalDateFormat.parse(((String[]) externalParameter.get("DateStart"))[0]);
            final Date dataEnd = DateHelper.LocalDateFormat.parse(((String[]) externalParameter.get("DateEnd"))[0]);
            final String insurerId = ((String[]) externalParameter.get("insurerId"))[0];
            final String chOrganisationId = ((String[]) externalParameter.get("chOrganisationId"))[0];

            String insurerName = "";
            String chOrganisationName = "";
            Integer iInsurerId = -1;
            Integer iChOrganisationId = -1;

            // GET INSURER NAME AND ID
            Insurer ins = getInsurer(insurerId);
            iInsurerId = ins.getId();
            insurerName = ins.getName();

            // GET CREDIT HIRE NAME AND ID
            Chorganisation chorganisation = getChorganisation(chOrganisationId);
            iChOrganisationId = chorganisation.getId();
            chOrganisationName = chorganisation.getName();
            
            List<InvoiceSavingSummaryReportViewData> reportRows = new ArrayList<InvoiceSavingSummaryReportViewData>();

            StringBuffer sb = new StringBuffer();
            sb.append("select claim.cho_reference as supplier_reference_number, claim.claim_number as claim_number, ");
            sb.append("case when third_party.vehicle_registration is null then '-' else third_party.vehicle_registration end as policy_holder_vehicle_registeration_number, ");
            sb.append("case when workgroup.name is null then '-' else workgroup.name end as workgroup_name, ");
            sb.append("invoice.original_total_to_pay as original_invoice_amount, invoice.total_to_pay as agreed_settlement_value ");
            sb.append("from claim claim inner join invoice invoice on claim.invoice_id=invoice.id ");
            sb.append("left outer join workgroup workgroup on workgroup.id=claim.workgroup_id ");
            sb.append("left outer join third_party third_party on third_party.id=claim.third_party_id ");
            sb.append("where claim.id in (select distinct claim_id from audit_trail where new_status='InvoicePaymentLogged') ");
            sb.append("and claim.insurer_id = :pInsurerId and claim.chorganisation_id = :pChorganisationId ");
            sb.append("and date(invoice.created_date) between :pInvUploadDateFrom and :pInvUploadDateTo ");
            sb.append("order by (invoice.original_total_to_pay - invoice.total_to_pay) desc");
            String query = sb.toString();

            Map paramMap = new HashMap();
            paramMap.put("pChorganisationId", iChOrganisationId);
            paramMap.put("pInsurerId", iInsurerId);
            paramMap.put("pInvUploadDateFrom", dataStart);
            paramMap.put("pInvUploadDateTo", dataEnd);
            
            List result = dataService.externalQuery(query, paramMap);

            for (Object o : result) {
                
                Map data = (Map)o;                
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
            ex.printStackTrace();
            // bAction = false;
            // sActionMsg = ex.getLocalizedMessage();
        } finally {
            // dataService.logSystemLog(getReportCode(), sActionMsg, bAction);
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
        return "RPT007";
    }

    private Insurer getInsurer(String sObjectId){

        Insurer insurer = new Insurer();
        ReportHelper reportHelper = new ReportHelper();

        if(!sObjectId.equalsIgnoreCase("")){
            insurer = reportHelper.getInsurer(Integer.valueOf(sObjectId), this.dataService);
        }else{
            insurer = user.getInsurer();
        }

        return insurer;
            
    }

    private Chorganisation getChorganisation(String sObjectId){

        Chorganisation chorganisation = new Chorganisation();
        ReportHelper reportHelper = new ReportHelper();
        
        if(!sObjectId.equalsIgnoreCase("")){

            Integer iChorganisation = Integer.valueOf(sObjectId);
            chorganisation = reportHelper.getChorganisation(iChorganisation, this.dataService);
        }else{
            chorganisation = user.getChorganisation();
        }

        return chorganisation;

    }
    
}