package chox.web.report;

import chox.Util.DateHelper;
import chox.Util.RoleHelper;
import chox.model.Chorganisation;
import chox.model.Insurer;
import chox.model.WebUserRole;
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
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class InsurerPaymentReport extends BaseAction implements Report{

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
    
    private Chorganisation getChorganisation(int orgId){
        
        Chorganisation chorg = new Chorganisation();

        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Chorganisation.class);
            criteria.add(Restrictions.eq("id", orgId));
            chorg = (Chorganisation)dataService.getByCriteria(criteria);
            
        } catch (Throwable e) {
           e.printStackTrace();
        } 
        
        return chorg;
    }
    
    public HashMap getReportParameters() {
        
        HashMap reportParameters = new HashMap();
        
        try {
            
            PermissionedUser currentUser = ((PermissionedUser) externalParameter.get("CurrentUser"));
            
            String insurerName = "";
            Integer iSupplierId = -1;
            Integer iInsurerId = -1;
            Integer iWorkgroupId = -1;
            
            boolean isWorkgroupEnabled = false;
            boolean isOrwnerEnabled = false;

            Chorganisation chorg = new Chorganisation();

            if(currentUser.getIsINS()){
                
                Insurer ins = currentUser.getUser().getInsurer();
                iInsurerId = ins.getId();
                insurerName = ins.getName();
                
                isWorkgroupEnabled = ins.isWorkgroupEnable();
                isOrwnerEnabled = ins.isClaimOwnershipEnable();
                
                String supplierId = ((String[]) externalParameter.get("supplierId"))[0];
                if(!supplierId.equalsIgnoreCase("")){
                    iSupplierId = Integer.parseInt(supplierId);
                    chorg = getChorganisation(iSupplierId);
                }
            }
            
            String workgroupId = ((String[]) externalParameter.get("workgroupId"))[0];
            if(!workgroupId.equalsIgnoreCase("")){
                iWorkgroupId = Integer.parseInt(workgroupId);
            }

            StringBuffer sb = new StringBuffer();
            sb.append("Select invoice.* from rpt_claim_invoice invoice ");
            sb.append("where invoice.status = 'AwaitingInvoicePayment' ");
            sb.append("and insurer_id = :pInsurerId and chorganisation_id = :pChorganisationId ");

            // FILTER BY WORKGROUP AND OWNERSHIO ONLY
            if(RoleHelper.isCheckSelectedRoleExist(currentUser.getUser().getRoles(), WebUserRole.ROLE_CH)){
                
                if(RoleHelper.isUserCheckByWorkgroup(currentUser.getUser())){
                    sb.append("and invoice.workgroup_id in (select workgroup_id from user_workgroup where user_id="+currentUser.getUser().getId()+") ");
                }

                if(RoleHelper.isUserCheckByOwnership(currentUser.getUser())){
                    sb.append("and invoice.owner = "+currentUser.getUser().getId()+" ");
                }
                
            }
            
            if(iWorkgroupId>0){
                sb.append("and invoice.workgroup_id = "+iWorkgroupId+" ");
            }

            sb.append("order by cho_reference asc");
            String query = sb.toString();

            // Emmanuel
            // 27-07-2009
            // prevent SQL Injection
            Map paramMap = new HashMap();
            paramMap.put("pChorganisationId", iSupplierId);
            paramMap.put("pInsurerId", iInsurerId);

            List result = dataService.externalQuery(query,paramMap);                           
            
            List<PaymentReport> payments = new ArrayList<PaymentReport>();
            
            for (Object o : result) {
                Map data = (Map) o;
                PaymentReport payment = PaymentReport.getObject(data);
                payments.add(payment);
            }            
            
            PaymentReportObject reportObject = new PaymentReportObject();
            reportObject.setCreatedDate(new Date());
            
            reportParameters.put("Chorganisation", chorg);
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

    public String getReportCode() {
        return "RPT002";
    }
}
