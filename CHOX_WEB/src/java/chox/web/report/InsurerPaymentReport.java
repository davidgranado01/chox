package chox.web.report;

import chox.Util.DateHelper;
import chox.model.Insurer;
import chox.services.DataService;
import chox.web.actions.BaseAction;
import chox.web.report.viewdata.PaymentReport;
import chox.web.report.viewdata.PaymentReportObject;
import chox.web.security.PermissionedUser;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InsurerPaymentReport extends BaseAction implements Report {

    Map externalParameter;
    List<String> reportParameterNames;
    private DataService dataService;

    public InsurerPaymentReport() {
        reportParameterNames = new ArrayList<String>();
    }

    public String getReportTemplateFileName() {
        return "template_PaymentReport.xls";
    }

    public void setExternalParameter(Map parameters) {
        this.externalParameter = parameters;
    }
    
    public HashMap getReportParameters() {
        
        HashMap reportParameters = new HashMap();
        
        try {
            
            PermissionedUser currentUser = ((PermissionedUser) externalParameter.get("CurrentUser"));
            Date dataStart = DateHelper.LocalDateFormat.parse(((String[]) externalParameter.get("DateStart"))[0]);
            Date dataEnd = DateHelper.LocalDateFormat.parse(((String[]) externalParameter.get("DateEnd"))[0]);
            String insurerName = "";
            Integer iSupplierId = -1;
            Integer iInsurerId = -1;            
            
            if(currentUser.getIsINS()){
                Insurer ins = currentUser.getUser().getInsurer();
                iInsurerId = ins.getId();
                insurerName = ins.getName();
                
                String supplierId = ((String[]) externalParameter.get("supplierId"))[0];
                if(!supplierId.equalsIgnoreCase("")){
                    iSupplierId = Integer.parseInt(supplierId);
                }
            }
            
            StringBuffer sb = new StringBuffer();
            sb.append("Select invoice.* from rpt_claim_invoice invoice ");
            sb.append("where invoice.status = 'AwaitingInvoicePayment' ");
            sb.append("and insurer_id = @pInsurerId and chorganisation_id = @pChorganisationId ");
            sb.append("and date_trunc('day', created_date) between @pInvUploadDateFrom and @pInvUploadDateTo ");
            sb.append("order by created_date desc");
            String query = sb.toString();
            
            query = query.replaceAll("@pInvUploadDateFrom", "'" + DateHelper.DBDateFormat.format(dataStart) + "'");
            query = query.replaceAll("@pInvUploadDateTo", "'" + DateHelper.DBDateFormat.format(dataEnd) + "'");
            query = query.replaceAll("@pChorganisationId", iSupplierId.toString());
            query = query.replaceAll("@pInsurerId", iInsurerId.toString());
            
            List result = dataService.externalQuery(query);
            List<PaymentReport> payments = new ArrayList<PaymentReport>();
            
            for (Object o : result) {
                Map data = (Map) o;
                PaymentReport payment = PaymentReport.getObject(data);
                payments.add(payment);
            }            
            
            PaymentReportObject reportObject = new PaymentReportObject();
            reportObject.setInvoiceUploadDateFrom(dataStart);
            reportObject.setInvoiceUploadDateTo(dataEnd);
            reportObject.setCreatedDate(new Date());
            
            reportParameters.put("payments", payments);
            reportParameters.put("reportObj", reportObject); 
            reportParameters.put("insurerName", insurerName); 
            
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

}
