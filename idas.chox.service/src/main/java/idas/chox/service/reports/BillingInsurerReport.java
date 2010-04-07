/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.reports;

import idas.chox.core.model.BillingInsurer;
import idas.chox.data.services.BaseDataService;
import idas.chox.service.bre.util.CalcHelper;
import idas.chox.service.reports.viewdata.BillingInsurerReportObject;
import idas.chox.service.reports.viewdata.BillingInsurerReportViewData;

import java.io.InputStream;
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

/**
 *
 * @author abrar
 */
public class BillingInsurerReport implements Report {

	private static final Logger log = LoggerFactory.getLogger(BillingInsurerReport.class);
    Map externalParameter;
    List<String> reportParameterNames;
    private BaseDataService baseDataService;
    

    @Override
    public void setExternalParameter(Map parameters) {
        this.externalParameter = parameters;
    }

    @Override
    public void setDataService(BaseDataService baseDataService) {
        this.baseDataService = baseDataService;
    }

    @Override
    public HashMap getReportParameters() {
        HashMap reportParameters = new HashMap();
        try {

            final String billingId = ((String[]) externalParameter.get("billingId"))[0];
            BillingInsurer bi = getBillingInsurer(Integer.parseInt(billingId));

            log.debug("Data Start " + bi.getDateFrom());
            log.debug("Data End " + bi.getDateTo());


            List<BillingInsurerReportViewData> reportRows = new ArrayList<BillingInsurerReportViewData>();

            StringBuffer sb = new StringBuffer();


            sb.append("select ");
                sb.append("cm.cho_reference, ");
                sb.append("cm.claim_number, ");
                sb.append("tp.policy_number, ");
                sb.append("case when tp.vehicle_registration is null then '-' else tp.vehicle_registration end as vehicle_registration, ");
                sb.append("tp.first_name || ' ' || tp.last_name as name, ");
                sb.append("cm.created_date ");
            // to fetch total to pay liability after libility change
                //sb.append("inv.total_to_pay ");
            sb.append("from ");
                sb.append("claim as cm, ");
                sb.append("billing_insurer_detail as bid, ");
                sb.append("customer as cr, ");
                sb.append("third_party as tp ");
                
            sb.append("where ");
                sb.append("cm.id=bid.claim_reference_id ");
                sb.append("and cr.id = cm.customer_id ");
                sb.append("and tp.id = cm.third_party_id ");
                
                sb.append("and bid.billing_insurer_id =  :p_billing_insurer_id");

            

            String query = sb.toString();

            Map paramMap = new HashMap();

            paramMap.put("p_billing_insurer_id",bi.getId());
            List result = baseDataService.externalQuery(query, paramMap);

            BigDecimal bdBenefitShareValue = bi.getInsurer().getScsAgreedBenefitShareValue();
            BigDecimal bdVatOnBenefitShareValue = bdBenefitShareValue.multiply(CalcHelper.VAT_RATE).setScale(2,BigDecimal.ROUND_HALF_UP);
            BigDecimal bdGrossShareValue = bdBenefitShareValue.add(bdVatOnBenefitShareValue);

            BigDecimal bdSumNetClaimCost = bdBenefitShareValue.multiply(new BigDecimal(result.size())).setScale(2,BigDecimal.ROUND_HALF_UP);
            BigDecimal bdSumVat = bdSumNetClaimCost.multiply( CalcHelper.VAT_RATE).setScale(2,BigDecimal.ROUND_HALF_UP);
            BigDecimal bdSumGrossClaimCost = bdSumNetClaimCost.add(bdSumVat);
            //double sumNetClaimCost = dbSumNetClaimCost.
            //double sumVatOnClaimCost = CalcHelper.VAT_RATE * result.size() * .15;
            for (Object o : result) {

                Map data = (Map) o;
                BillingInsurerReportViewData reportRow = BillingInsurerReportViewData.getObject(data);
                reportRow.setNetClaimCost(bdBenefitShareValue);
                reportRow.setVatOnClaimCost(bdVatOnBenefitShareValue);
                reportRow.setGrossClaimCost(bdGrossShareValue);
                reportRows.add(reportRow);
            }

            BillingInsurerReportObject reportObject = new BillingInsurerReportObject();
            reportObject.setCurrentDate(new Date());
            reportObject.setClaimUploadDateFrom(bi.getDateFrom());
            reportObject.setClaimUploadDateTo(bi.getDateTo());
            reportObject.setCountOfClaims(result.size());
            reportObject.setAgreedBenefitValue(50);
            reportObject.setScsBenefitShare(.25);
            reportObject.setTotalAgreedBenefit(bi.getInsurer().getScsAgreedBenefitShareValue().doubleValue());
            reportObject.setSumNetClaimCost(bdSumNetClaimCost.doubleValue());
            reportObject.setSumVatOnClaimCost(bdSumVat.doubleValue());
            reportObject.setSumGrossClaimCost(bdSumGrossClaimCost.doubleValue());

            reportParameters.put("reportObject", reportObject);
            reportParameters.put("reportRows", reportRows);


        } catch (RuntimeException ex) {
            log.error(ex.getMessage(),ex);
            throw ex;
        } 

        return reportParameters;
    }

    @Override
    public String getReportTemplateFileName() {
        return "template_BillingInsurerReport.xls";
    }

    @Override
    public InputStream build() {
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
           log.error(e.getMessage(),e);
           throw new RuntimeException(e);
        }
        return bc;
    }



    public String getReportCode() {
        return "RPT009";
    }
}
