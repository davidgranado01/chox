package idas.chox.service.reports;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.BillingInsurer;
import idas.chox.core.services.ReportDataService;
import idas.chox.data.services.BaseDataService;
import idas.chox.service.reports.viewdata.BillingInsurerReportObject;
import idas.chox.service.reports.viewdata.BillingInsurerReportViewData;

/**
 *
 * @author abrar
 */
public class BillingInsurerReport implements Report {

    private static final Logger LOG = LoggerFactory.getLogger(BillingInsurerReport.class);
    private Map externalParameter;
    private List<String> reportParameterNames;
    private BaseDataService baseDataService;
    private ReportDataService reportDataService;

    @Override
    public void setBaseDataService(BaseDataService baseDataService) {
        this.baseDataService = baseDataService;
    }

    @Override
    public void setExternalParameter(Map parameters) {
        this.externalParameter = parameters;
    }

    @Override
    public void setReportDataService(ReportDataService reportDataService) {
        this.reportDataService = reportDataService;
    }

    @Override
    public Map<String, Object> getReportParameters() throws Exception {
        Map<String, Object> reportParameters = new HashMap<String, Object>();
        try {

            final String billingId = ((String[]) externalParameter.get("billingId"))[0];
            BillingInsurer bi = getBillingInsurer(Integer.parseInt(billingId));

            LOG.debug("Data Start " + bi.getDateFrom());
            LOG.debug("Data End " + bi.getDateTo());

            if(bi.getDateTo() == null || bi.getDateFrom() == null){
                throw new Exception("Start and End dates must not be empty.");
            }

            if(bi.getDateTo().before(bi.getDateFrom())){
                throw new Exception("End date (" + bi.getDateTo().toString() + ") is before start date (" + bi.getDateFrom().toString() +  ") ");
            }

            List<BillingInsurerReportViewData> reportRows = new ArrayList<BillingInsurerReportViewData>();

            StringBuilder sb = new StringBuilder();


            sb.append("select ");
                sb.append("cm.cho_reference, ");
                sb.append("cm.claim_number, ");
                sb.append("cho.name as cho_name, ");
                sb.append("tp.policy_number, ");
                sb.append("case when tp.vehicle_registration is null then '-' else tp.vehicle_registration end as vehicle_registration, ");
                sb.append("case when tp.first_name is null and tp.last_name is null then '-' when tp.first_name is null then tp.last_name when tp.last_name is null then tp.first_name else tp.first_name || ' ' || tp.last_name end as name, ");
                sb.append("at.update_date as received_date, ");
                sb.append("bid.net_claim_cost as net_claim_cost, ");
                sb.append("bid.vat_claim_cost as vat_claim_cost, ");
                sb.append("bid.gross_claim_cost as gross_claim_cost ");
            // to fetch total to pay liability after libility change
                //sb.append("inv.total_to_pay ");
            sb.append("from ");
                sb.append("claim as cm, ");
                sb.append("billing_insurer_detail as bid, ");
                sb.append("audit_trail as at, ");
                sb.append("customer as cr, ");
                sb.append("chorganisation as cho, ");
                sb.append("third_party as tp ");
                
            sb.append("where ");
                sb.append("cm.id=bid.claim_reference_id ");
                sb.append("and cr.id = cm.customer_id ");
                sb.append("and tp.id = cm.third_party_id ");
                sb.append("and cm.id = at.claim_id ");
                sb.append("and at.reverted=false and (at.new_status='PaymentReceived' or at.new_status='ManualInvoicePaid')");
                sb.append("and cm.chorganisation_id = cho.id ");
                sb.append("and bid.billing_insurer_id =  :p_billing_insurer_id ");
                sb.append("and not exists (select * from audit_trail a where a.reverted=false and a.claim_id=at.claim_id and (a.new_status='PaymentReceived' or a.new_status='ManualInvoicePaid') and a.update_date < at.update_date)");

            

            String query = sb.toString();

            Map paramMap = new HashMap();

            paramMap.put("p_billing_insurer_id",bi.getId());
            List result = reportDataService.getReportData(query, paramMap);


            for (Object o : result) {

                Map data = (Map) o;
                BillingInsurerReportViewData reportRow = BillingInsurerReportViewData.getObject(data);
                reportRows.add(reportRow);
            }

            BillingInsurerReportObject reportObject = new BillingInsurerReportObject();
            reportObject.setScheduleName(bi.getScheduleName());
            reportObject.setCurrentDate(new Date());
            reportObject.setClaimUploadDateFrom(bi.getDateFrom());
            reportObject.setClaimUploadDateTo(bi.getDateTo());
            reportObject.setCountOfClaims(result.size());
            if (bi.isFixedTransaction()) {
                reportObject.setIsFixedTransactionFee(true);
                reportObject.setFixedTransactionFee(bi.getFixedTransactionFee().doubleValue());
            }
            else {
                reportObject.setIsFixedTransactionFee(false);
                reportObject.setAgreedBenefitValue(bi.getBenefitValue().doubleValue());
                reportObject.setScsBenefitShare(bi.getBenefitShare().doubleValue()/100.0);
                reportObject.setTotalAgreedBenefit(bi.getBenefitValue().multiply(bi.getBenefitShare()).divide(new BigDecimal(100.0), 2,BigDecimal.ROUND_HALF_UP).doubleValue());
            }

            reportParameters.put("reportObject", reportObject);
            reportParameters.put("reportRows", reportRows);


        } catch (RuntimeException ex) {
            LOG.error(ex.getMessage(),ex);
            throw ex;
        } catch (Exception e) {
            LOG.error(e.getMessage(),e);
            throw e;
        } 

        return reportParameters;
    }

    @Override
    public String getReportTemplateFileName() {
        return "template_BillingInsurerReport.xls";
    }

    @Override
    public ByteArrayOutputStream build() throws Exception {
        ReportBuilder builder = getReportBuilder();
        return builder.buildReport(this);
    }

    protected ReportBuilder getReportBuilder() {
        return new ExcelReportBuilder(){

            @Override
            public HSSFWorkbook appendImage(HSSFWorkbook resultWorkbook) {
                return resultWorkbook;
            }

        };
    }

    public BillingInsurer getBillingInsurer(int id) {
        BillingInsurer bc = null;
        try{
            DetachedCriteria criteria = DetachedCriteria.forClass(BillingInsurer.class);
            criteria.add(Restrictions.eq("id", id));
            bc = (BillingInsurer) baseDataService.getByCriteria(criteria);
        }catch(Exception e){
           LOG.error(e.getMessage(),e);
           throw new RuntimeException(e);
        }
        return bc;
    }

    @Override
    public String getReportCode() {
        return "RPT009";
    }
    
    @Override
    public short[] getColumnsToHide() {
        return null;
    }
}
