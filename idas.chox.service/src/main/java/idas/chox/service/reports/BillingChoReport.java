package idas.chox.service.reports;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.BillingCho;
import idas.chox.core.model.BillingChoRate;
import idas.chox.core.services.ReportDataService;
import idas.chox.data.services.BaseDataService;
import idas.chox.service.reports.viewdata.BillingChoReportObject;
import idas.chox.service.reports.viewdata.BillingChoReportViewData;

/**
 *
 * @author abrar
 */
public class BillingChoReport implements Report {

    private static final Logger LOG = LoggerFactory.getLogger(BillingChoReport.class);
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
        Map<String, Object> reportParameters = new HashMap<>();
        try {
            final String billingId = ((String[]) externalParameter.get("billingId"))[0];

            BillingCho bc = getBillingCho(Integer.parseInt(billingId));

            LOG.debug("Data Start: {}", bc.getDateFrom());
            LOG.debug("Data End: {}", bc.getDateTo());
            
            if(bc.getDateTo() == null || bc.getDateFrom() == null){
                throw new Exception("Start and End dates must not be empty.");
            }

            if(bc.getDateTo().before(bc.getDateFrom())){
                throw new Exception("End date (" + bc.getDateTo().toString() + ") is before start date (" + bc.getDateFrom().toString() +  ") ");
            }

            StringBuilder sb = new StringBuilder();
            sb.append("select ")
                .append("cm.cho_reference, ")
                .append("cm.claim_number, ")
                .append("case when cr.vehicle_registration is null then '-' else cr.vehicle_registration end as vehicle_registration, ")
                .append("cr.first_name || ' ' || cr.last_name as name, ")
                .append("at.update_date as received_date, ")
                .append("inv.total_to_pay, ")
                .append("bcd.net_claim_cost, ")
                .append("bcd.vat_net_claim_cost, ")
                .append("bcd.gross_claim_cost ");
            sb.append("from ")
                .append("claim as cm, ")
                .append("billing_cho as bc, ")
                .append("billing_cho_detail as bcd, ")
                .append("customer as cr, ")
                .append("audit_trail as at, ")
                .append("invoice as inv ");
            sb.append("where ")
                .append("cm.id=bcd.claim_reference_id ")
                .append("and cr.id = cm.customer_id ")
                .append("and inv.id = cm.invoice_id ")
                .append("and cm.id = at.claim_id ")
                .append("and at.new_status='PaymentReceived' ")
                .append("and at.created_date < bc.created_date ")
                .append("and bc.id =  :p_billing_cho_id ")
                .append("and bcd.billing_cho_id =  :p_billing_cho_id ")
                .append("and not exists (select * from audit_trail a where a.claim_id=at.claim_id and a.new_status='PaymentReceived' and a.update_date > at.update_date and a.update_date < bc.created_date)");
            String query = sb.toString();
            Map paramMap = new HashMap();
            paramMap.put("p_billing_cho_id", bc.getId());
            List result = reportDataService.getReportData(query,paramMap);
            List<BillingChoReportViewData> reportRows= new ArrayList<>();
            for (Object o : result) {
                LOG.debug("Adding row...");
                Map data = (Map) o;
                BillingChoReportViewData row = BillingChoReportViewData.getObject(data);
                reportRows.add(row);
            }

            BillingChoReportObject reportObject = new BillingChoReportObject();
            reportObject.setScheduleName(bc.getScheduleName());
            reportObject.setDateFrom(bc.getDateFrom());
            reportObject.setDateTo(bc.getDateTo());
            reportObject.setCreatedDate(new Date());
            reportObject.setChoName(bc.getCho().getName());
            reportObject.setReportTitle("");
            reportObject.setNumberOfInvoicesSubmitted(bc.getNumberInvoicesSubmitted());
            reportObject.setNumberOfPaymentsReceived(bc.getNumberPaymentsReceived());
            reportObject.setIsFixedTransactionalFee(bc.isFixedTransaction());
            if (bc.isFixedTransaction()) {
                LOG.debug("Fixed Transaction Fee is {}", bc.getFixedTransactionFee());
                reportObject.setFixedTransactionFee(bc.getFixedTransactionFee());
            }
            else {
                LOG.debug("Charge Rate is {}", bc.getChargeRate());
                reportObject.setChargeRate(bc.getChargeRate().divide(new BigDecimal(100.0), 4, BigDecimal.ROUND_HALF_UP));
            }

            reportParameters.put("reportObj", reportObject);
            reportParameters.put("reportRows", reportRows);
        
        } catch (Exception ex) {
            LOG.error("Exception thrown getting report parameters: {}", ex.getMessage(), ex);
            throw ex;
        } 
        
        return reportParameters;
    }

    @Override
    public String getReportTemplateFileName() {
        return "template_BillingChoReport.xls";
    }

    @Override
    public ByteArrayOutputStream build() throws Exception {
        ReportBuilder builder = getReportBuilder();
        return builder.buildReport(this);
    }

    protected ReportBuilder getReportBuilder() {
        return new ExcelReportBuilder(){

            @Override
            public HSSFWorkbook appendImage(HSSFWorkbook resultWorkbook, boolean brandingLogo) {
                return resultWorkbook;
            }

        };
    }


    public BillingCho getBillingCho(int id) throws Exception{
        BillingCho bc = null;
        try{
            DetachedCriteria criteria = DetachedCriteria.forClass(BillingCho.class);
            criteria.add(Restrictions.eq("id", id));
            bc = (BillingCho) baseDataService.getByCriteria(criteria);
        }catch(Exception e){
           LOG.error("Exception thrown in getBillingCho for id={} :{}",id, e.getMessage());
           throw e;
        }
        return bc;
    }

    public BigDecimal getChargeRate(int cho_organisation_id, int volume) throws Exception {
        BillingChoRate billingChoRate;

        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(BillingChoRate.class);
            criteria.createCriteria("chorganisation").add(Restrictions.eq("id", cho_organisation_id));
            Criterion minVolume = Restrictions.le("minVolume", volume);
            Criterion maxVolume = Restrictions.ge("maxVolume", volume);
            Criterion isNull = Restrictions.isNull("maxVolume");

            LogicalExpression and1 = Restrictions.and(minVolume, maxVolume);
            LogicalExpression and2 = Restrictions.and(minVolume, isNull);

            LogicalExpression or = Restrictions.or(and1, and2);
            criteria.add(or);

            List  myList =  baseDataService.findByCriteria(criteria);
            if ( myList.size() != 1){
                LOG.error("Multiple charge rates found for volume={}, choId={}", volume, cho_organisation_id);
                throw new RuntimeException("Multipe/Or rate matches error");
            }
            billingChoRate = (BillingChoRate)myList.get(0);
        } catch (Exception e) {
           LOG.error("Exception thrown in getChargeRate for volume={} : {}", volume, e.getMessage());
           throw e;
        }

        return billingChoRate.getFee();
    }

    @Override
    public String getReportCode() {
        return "RPT010";
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
