package idas.chox.service.reports;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.services.ReportDataService;
import idas.chox.core.util.RoleHelper;
import idas.chox.core.util.TextHelper;
import idas.chox.data.services.BaseDataService;
import idas.chox.service.reports.viewdata.PaymentReport;
import idas.chox.service.reports.viewdata.PaymentReportObject;

public class InsurerPaymentReport implements Report {
    private static final Logger LOG = LoggerFactory.getLogger(InsurerPaymentReport.class);

    private Map externalParameter;
    private List<String> reportParameterNames;
    private BaseDataService baseDataService;
    private ReportDataService reportDataService;

    @Override
    public void setBaseDataService(BaseDataService baseDataService) {
        this.baseDataService = baseDataService;
    }

    public InsurerPaymentReport() {
        reportParameterNames = new ArrayList<String>();
    }

    @Override
    public String getReportTemplateFileName() {
        return "template_PaymentReport.xls";
    }

    @Override
    public void setExternalParameter(Map parameters) {
        this.externalParameter = parameters;
    }

    private Chorganisation getChorganisation(int orgId) {

        Chorganisation chorg = new Chorganisation();

        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(Chorganisation.class);
            criteria.add(Restrictions.eq("id", orgId));
            chorg = (Chorganisation) baseDataService.getByCriteria(criteria);

        } catch (Exception e) {
            LOG.error("Error getting CH Organisation: {}", e.getMessage());
        }

        return chorg;
    }

    @Override
    public Map<String, Object> getReportParameters() {

        Map<String, Object> reportParameters = new HashMap<String, Object>();

        try {

            WebUser currentUser = ((WebUser) externalParameter.get("CurrentUser"));
            // PermissionedUser currentUser = ((PermissionedUser) externalParameter.get("CurrentUser"));

            String insurerName = "";
            Integer iSupplierId = -1;
            Integer iInsurerId = -1;
            Integer iWorkgroupId = -1;

            Chorganisation chorg = new Chorganisation();

            if (currentUser.getInsurer()!=null) {

                Insurer ins = currentUser.getInsurer();
                iInsurerId = ins.getId();
                insurerName = ins.getName();

                if((externalParameter.get("supplierId"))!=null){
                    String supplierId = ((String[]) externalParameter.get("supplierId"))[0];
                    if(!supplierId.equalsIgnoreCase("")){
                        iSupplierId = TextHelper.getId(supplierId);
                        chorg = getChorganisation(iSupplierId);
                    }
                }

            }
            
            if((externalParameter.get("workgroupId"))!=null){
                String workgroupId = ((String[]) externalParameter.get("workgroupId"))[0];
                if(!workgroupId.equalsIgnoreCase("")){
                    iWorkgroupId = TextHelper.getId(workgroupId);
                }
            }

            StringBuilder sb = new StringBuilder();
            sb.append("select invoice.* from rpt_claim_invoice invoice ");
            sb.append("where invoice.status = 'AwaitingInvoicePayment' ");
            sb.append("and insurer_id = :pInsurerId and chorganisation_id = :pChorganisationId ");

            // FILTER BY WORKGROUP AND OWNERSHIO ONLY
            if (RoleHelper.isCheckSelectedRoleExist(currentUser.getRoles(), WebUserRole.ROLE_INS_CH)) {

                if (RoleHelper.isWorkgroupValidationEnabledUser(currentUser)) {
                    sb.append("and invoice.workgroup_id in (select workgroup_id from web_user_workgroup where user_id=").append(currentUser.getId()).append(") ");
                }

                if (RoleHelper.isOwnershipValidationEnabledUser(currentUser)) {
                    sb.append("and invoice.owner = ").append(currentUser.getId()).append(" ");
                }

            }

            if (iWorkgroupId > 0) {
                sb.append("and invoice.workgroup_id = ").append(iWorkgroupId).append(" ");
            }

            sb.append("order by cho_reference asc");
            String query = sb.toString();

            Map paramMap = new HashMap();
            paramMap.put("pChorganisationId", iSupplierId);
            paramMap.put("pInsurerId", iInsurerId);

            List result = reportDataService.getReportData(query, paramMap);

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
            LOG.error("Error getting Insurer Payment Report parameters:: {}", ex.getMessage());
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
        return "RPT002";
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
