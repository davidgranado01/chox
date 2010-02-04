/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package idas.chox.service.reports;

import idas.chox.core.model.BillingCho;
import idas.chox.core.model.BillingChoRate;
import idas.chox.data.services.BaseDataService;
import idas.chox.service.reports.viewdata.BillingChoReportObject;
import idas.chox.service.reports.viewdata.BillingChoReportViewData;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author abrar
 */
public class BillingChoReport implements Report{

    private static final Logger log = Logger.getLogger(BillingChoReport.class);
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
            //WebUser currentUser = ((WebUser) externalParameter.get("CurrentUser"));
            //PermissionedUser currentUser = ((PermissionedUser) externalParameter.get("CurrentUser"));
            //user = currentUser.getUser();

            //final Date dateStart = DateHelper.sdf.parse(((String[]) externalParameter.get("dateFrom"))[0]);
            //final Date dateEnd = DateHelper.sdf.parse(((String[]) externalParameter.get("dateTo"))[0]);
            //final String choId = ((String[]) externalParameter.get("column2"))[0];
            final String billingId = ((String[]) externalParameter.get("billingId"))[0];

            BillingCho bc = getBillingCho(Integer.parseInt(billingId));

            log.debug("Data Start " + bc.getDateFrom());
            log.debug("Data End " + bc.getDateTo());

            
            //Chorganisation cho = bc.getCho();

            StringBuffer sb = new StringBuffer();
            sb.append("select ");
                sb.append("cm.cho_reference, ");
                sb.append("cr.claim_reference, ");
                sb.append("tp.policy_number, ");
                sb.append("case when tp.vehicle_registration is null then '-' else tp.vehicle_registration end as vehicle_registration, ");
                sb.append("tp.first_name || ' ' || tp.last_name as name, ");
                sb.append("inv.created_date, ");
                sb.append("inv.total_to_pay ");
            sb.append("from ");
                sb.append("claim as cm, ");
                sb.append("billing_cho_detail as bcd, ");
                sb.append("customer as cr, ");
                sb.append("third_party as tp, ");
                sb.append("invoice as inv ");
            sb.append("where ");
                sb.append("cm.id=bcd.claim_reference_id ");
                sb.append("and cr.id = cm.customer_id ");
                sb.append("and tp.id = cm.third_party_id ");
                sb.append("and inv.id = cm.invoice_id ");
                sb.append("and bcd.billing_cho_id =  :p_billing_cho_id");
            String query = sb.toString();
            Map paramMap = new HashMap();
            paramMap.put("p_billing_cho_id", bc.getId());
            List result = baseDataService.externalQuery(query,paramMap);
            BigDecimal chargeRate = getChargeRate(bc.getCho().getId(), result.size());
            log.debug("################################Charge Rate "+chargeRate);
            log.debug("################################## Result size " + result.size());
            List<BillingChoReportViewData> reportRows= new ArrayList<BillingChoReportViewData>();
            BigDecimal totalInvoiceAmount = BigDecimal.ZERO;
            for (Object o : result) {
                Map data = (Map) o;
                BillingChoReportViewData row = BillingChoReportViewData.getObject(data, chargeRate);
                totalInvoiceAmount = totalInvoiceAmount.add(row.getTotalchargePercentageGrossAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
                reportRows.add(row);
            }

            BillingChoReportObject reportObject = new BillingChoReportObject();
            reportObject.setDateFrom(bc.getDateFrom());
            reportObject.setDateTo(bc.getDateTo());
            reportObject.setCreatedDate(new Date());
            reportObject.setChoName(bc.getCho().getName());
            reportObject.setReportTitle("Cho Billing Report");
            reportObject.setChargeRate(chargeRate.divide(new BigDecimal(100)));

            reportObject.setNumberOfInvoicesUploaded(result.size());

            reportParameters.put("reportObj", reportObject);
            reportParameters.put("reportRows", reportRows);

            //String reportTitle = ((String[]) externalParameter.get("ReportTitle"))[0];
            //log.debug("Report title is: " + reportTitle);







             

        
        } catch (Exception ex) {
            log.debug(ex);
            throw new RuntimeException(ex);
        } finally {
           
        }
        
        return reportParameters;
    }

    @Override
    public String getReportTemplateFileName() {
        return "template_BillingChoReport.xls";
    }

    @Override
    public InputStream build() {
        ReportBuilder builder = getReportBuilder();
        return builder.buildReport(this);
    }

    protected ReportBuilder getReportBuilder() {
        return new ExcelReportBuilder();
    }


    public BillingCho getBillingCho(int id) throws Exception{
        BillingCho bc = null;
        try{
            DetachedCriteria criteria = DetachedCriteria.forClass(BillingCho.class);
            criteria.add(Restrictions.eq("id", id));
            bc = (BillingCho) baseDataService.getByCriteria(criteria);
        }catch(Exception e){
           log.error(e.getMessage(),e);
           throw e;
        }
        return bc;
    }

    public BigDecimal getChargeRate(int cho_organisation_id, int volume) throws Exception {
        BillingChoRate billingChoRate;
        BigDecimal fee = BigDecimal.ZERO;

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
                throw new RuntimeException("Multipe rate matches error");
            }
            billingChoRate = (BillingChoRate)myList.get(0);
        } catch (Exception e) {
           log.error(e.getMessage(),e);
           throw e;
        }

        return billingChoRate.getFee();
    }

    public String getReportCode() {
        return "RPT010";
    }

}
