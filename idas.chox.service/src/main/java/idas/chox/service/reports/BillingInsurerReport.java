/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.reports;

import idas.chox.core.model.Insurer;
import idas.chox.core.model.WebUser;
import idas.chox.core.util.DateHelper;
import idas.chox.data.services.BaseDataService;
import idas.chox.service.reports.viewdata.BillingInsurerReportObject;
import idas.chox.service.reports.viewdata.BillingInsurerReportViewData;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author abrar
 */
public class BillingInsurerReport implements Report {

    private static final Logger log = Logger.getLogger(BillingInsurerReport.class);
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
            WebUser currentUser = ((WebUser) externalParameter.get("CurrentUser"));
            //PermissionedUser currentUser = ((PermissionedUser) externalParameter.get("CurrentUser"));
            //user = currentUser.getUser();

            final Date dateStart = DateHelper.sdf.parse(((String[]) externalParameter.get("dateFrom"))[0]);
            final Date dateEnd = DateHelper.sdf.parse(((String[]) externalParameter.get("dateTo"))[0]);
            final String insurerId = ((String[]) externalParameter.get("column2"))[0];

            log.debug("Data Start " + dateStart);
            log.debug("Data End " + dateEnd);
            String insurerName = "";
            String chOrganisationName = "";
            Integer iInsurerId = -1;
            Integer iChOrganisationId = -1;

            // GET INSURER NAME AND ID
            Insurer ins = getInsurer(Integer.parseInt(insurerId));
            iInsurerId = ins.getId();
            insurerName = ins.getName();

            // GET CREDIT HIRE NAME AND ID
            //Chorganisation chorganisation = getChorganisation(chOrganisationId);
            //iChOrganisationId = chorganisation.getId();
//            chOrganisationName = chorganisation.getName();

            List<BillingInsurerReportViewData> reportRows = new ArrayList<BillingInsurerReportViewData>();

            StringBuffer queryBuffer = new StringBuffer();
            queryBuffer.append("select ");
            queryBuffer.append("claim.cho_reference, ");
            queryBuffer.append("customer.claim_reference, ");
            queryBuffer.append("third_party.policy_number, ");
            queryBuffer.append("case when third_party.vehicle_registration is null then '-' else third_party.vehicle_registration end as vehicle_registration, ");
            queryBuffer.append("third_party.first_name || ' ' || third_party.last_name as name,");
            queryBuffer.append("claim.created_date, ");
            queryBuffer.append("12.5 as net_claim_cost, ");
            queryBuffer.append("12.5 * .15 as vat_on_claim_cost, ");
            queryBuffer.append("12.5 + 12.5 * .15 as gross_claim_cost ");
            queryBuffer.append("from ");
            queryBuffer.append("claim,");
            queryBuffer.append("customer,");
            queryBuffer.append("third_party ");
            queryBuffer.append("where ");
            queryBuffer.append("claim.customer_id = customer.id ");
            queryBuffer.append("and claim.third_party_id = third_party.id ");
            queryBuffer.append("and percentage_liability_accepted > 0 ");
            queryBuffer.append("and claim.created_date between :pDateStart and :pDateTo ");


            String query = queryBuffer.toString();


            Map paramMap = new HashMap();

            //paramMap.put("pInsurerId", iInsurerId);
            paramMap.put("pDateStart", dateStart);
            paramMap.put("pDateTo", dateEnd);

            List result = baseDataService.externalQuery(query, paramMap);
            double sumNetClaimCost = 12.5 * result.size();
            double sumVatOnClaimCost = 12.5 * result.size() * .15;
            for (Object o : result) {

                Map data = (Map) o;
                BillingInsurerReportViewData reportRow = BillingInsurerReportViewData.getObject(data);

                reportRows.add(reportRow);
            }

            BillingInsurerReportObject reportObject = new BillingInsurerReportObject();
            reportObject.setCurrentDate(new Date());
            reportObject.setClaimUploadDateFrom(dateStart);
            reportObject.setClaimUploadDateTo(dateEnd);
            reportObject.setCountOfClaims(result.size());
            reportObject.setAgreedBenefitValue(50);
            reportObject.setScsBenefitShare(.25f);
            reportObject.setSumNetClaimCost(sumNetClaimCost);
            reportObject.setSumVatOnClaimCost(sumVatOnClaimCost);

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
        return new ExcelReportBuilder();
    }

    private Insurer getInsurer(int orgId) {

        Insurer insurer = new Insurer();

        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(Insurer.class);
            criteria.add(Restrictions.eq("id", orgId));
            insurer = (Insurer) baseDataService.getByCriteria(criteria);

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return insurer;
    }

    public String getReportCode() {
        return "RPT009";
    }
}
