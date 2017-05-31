package idas.chox.service.reports;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;

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
        Map<String, Object> reportParameters = new HashMap<>();
        try {
            
            final String billingId = ((String[]) externalParameter.get("billingId"))[0];
            BillingInsurer bi = getBillingInsurer(Integer.parseInt(billingId));

            LOG.debug("Data Start: {}", bi.getDateFrom());
            LOG.debug("Data End: {} ", bi.getDateTo());

            if(bi.getDateTo() == null || bi.getDateFrom() == null){
                throw new Exception("Start and End dates must not be empty.");
            }

            if(bi.getDateTo().before(bi.getDateFrom())){
                throw new Exception("End date (" + bi.getDateTo().toString() + ") is before start date (" + bi.getDateFrom().toString() +  ") ");
            }

            List<BillingInsurerReportViewData> reportRows = new ArrayList<>();

            StringBuilder sb = new StringBuilder();
            
            sb.append("select ")
                .append("cm.cho_reference, ")
                .append("cm.claim_number, ")
                .append("cho.name as cho_name, ")
                .append("tp.policy_number, ")
                .append("case when tp.vehicle_registration is null then '-' else tp.vehicle_registration end as vehicle_registration, ")
                .append("case when tp.first_name is null and tp.last_name is null then '-' when tp.first_name is null then tp.last_name when tp.last_name is null then tp.first_name else tp.first_name || ' ' || tp.last_name end as name, ")
                .append("bid.trigger_date as trigger_date, ")
                .append("bid.trigger_point as trigger_point, ")
                .append("bid.net_claim_cost as net_claim_cost, ")
                .append("bid.vat_claim_cost as vat_claim_cost, ")
                .append("bid.gross_claim_cost as gross_claim_cost ")
                .append("from ")
                .append("claim as cm, ")
                .append("billing_insurer as bi, ")
                .append("billing_insurer_detail as bid, ")
                .append("customer as cr, ")
                .append("chorganisation as cho, ")
                .append("third_party as tp ")
                .append("where ")
                .append("cm.id=bid.claim_reference_id ")
                .append("and cm.customer_id = cr.id ")
                .append("and cm.third_party_id = tp.id ")
                .append("and cm.chorganisation_id = cho.id ")
                .append("and bid.billing_insurer_id =  bi.id ")
                .append("and bi.id = :p_billing_insurer_id");

            String query = sb.toString();

            Map paramMap = new HashMap();

            paramMap.put("p_billing_insurer_id",bi.getId());
            List result = reportDataService.getReportData(query, paramMap);

            LOG.debug("Found {} matching claims to bill", result.size());

            for (Object o : result) {

                Map data = (Map) o;
                BillingInsurerReportViewData reportRow = BillingInsurerReportViewData.getObject(data);
                reportRows.add(reportRow);
            }

            BillingInsurerReportObject reportObject = new BillingInsurerReportObject();
            reportObject.setScheduleName(bi.getScheduleName());
            reportObject.setInsurerName(bi.getInsurer().getName());
            reportObject.setCurrentDate(new Date());
            reportObject.setClaimUploadDateFrom(bi.getDateFrom());
            reportObject.setClaimUploadDateTo(bi.getDateTo());
            reportObject.setCountOfClaims(result.size());

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
            public Workbook appendImage(Workbook resultWorkbook, boolean brandingLogo) {
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

    @Override
    public boolean isBrandingReportFormat() {
        return externalParameter.get("isBrandingReport")==null ? false : (Boolean)externalParameter.get("isBrandingReport");
    }
}
