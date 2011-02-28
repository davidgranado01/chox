package idas.chox.service.reports;

import idas.chox.core.util.TextHelper;
import idas.chox.data.services.BaseDataService;
import idas.chox.service.reports.viewdata.BreInvoiceApprovalDisputeCumulativeData;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.Chorganisation;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import idas.chox.core.model.WebUser;
import idas.chox.core.util.DateHelper;
import idas.chox.service.reports.viewdata.BreInvoiceApprovalDisputedData;



/**
 *
 * @author rajareddydodda
 */
public class BreInvoiceApprovalDisputeReport implements Report {


    private static final Logger LOG = LoggerFactory.getLogger(BreInvoiceApprovalDisputeReport.class);

    Map externalParameter;
    List<String> reportParameterNames;
    private BaseDataService baseDataService;


    public BreInvoiceApprovalDisputeReport() {
        reportParameterNames = new ArrayList<String>();
    }
    @Override
    public void setExternalParameter(Map parameters) {
        this.externalParameter = parameters;
    }

    @Override
    public void setDataService(BaseDataService baseDataService) {
        this.baseDataService = baseDataService;
    }

    private Chorganisation getChorganisation(int orgId) {

        Chorganisation chorg = new Chorganisation();

        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Chorganisation.class);
            criteria.add(Restrictions.eq("id", orgId));
            chorg = (Chorganisation) baseDataService.getByCriteria(criteria);

        } catch (Throwable e) {
           
            LOG.error("Error generating getChorganisation: {}", e.getMessage());
            
        }

        return chorg;
    }

    private Insurer getInsurer(int orgId) {
        Insurer ins = new Insurer();

        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(Insurer.class);
            criteria.add(Restrictions.eq("id", orgId));
            ins = (Insurer) baseDataService.getByCriteria(criteria);

        } catch (Throwable e) {
            LOG.error("Error generating getInsurer: {}", e.getMessage());
            
        }

        return ins;
    }






    @Override
    public HashMap getReportParameters() {
        HashMap reportParameters = new HashMap();

        reportParameters.put("date", new Date());

        LOG.debug("inside getReportParameters()");

        Date dataStart = null;

        String supplierId = "";
        String insrId = "";

        Integer choId = -1;
        Integer insurerId = -1;
        Integer userOrgId = -1;
        String insurerName = "";
        String choOrgName = "";
        String selectedOrgLabel = "";
        String reportColumnHeader = "";
        String userOrgLabel = "";
        String selectedOrgName = "All";
        String userOrgName = "";
        Date createDate = new Date();

        WebUser currentUser = ((WebUser) externalParameter.get("CurrentUser"));
        Chorganisation chorg = new Chorganisation();

        userOrgName = currentUser.getOrganisationName();
        if (((String[]) externalParameter.get("DateStart")) != null) {
            dataStart = DateHelper.Parse(((String[]) externalParameter.get("DateStart"))[0]);

            LOG.debug("dataStart :" + dataStart);

        }


        if (currentUser.getInsurer() != null) {

            Insurer ins = currentUser.getInsurer();
            insurerId = ins.getId();
            userOrgId = insurerId;

            reportColumnHeader = "Credit Hire Organisation";
            selectedOrgLabel = "Credit Hire Organisation";
            userOrgLabel = "Insurer";

            if ((externalParameter.get("supplierId")) != null) {
                supplierId = ((String[]) externalParameter.get("supplierId"))[0];
                if (!supplierId.equalsIgnoreCase("")) {
                    choId = TextHelper.getId(supplierId);
                    selectedOrgName = getChorganisation(choId).getName();
                }
            }

            LOG.debug("insurerId :" + insurerId);
            LOG.debug("choId :" + choId);
            LOG.debug("selectedOrgName :" + selectedOrgName);



        } else {

            chorg = currentUser.getChorganisation();
            choId = chorg.getId();
            userOrgId = choId;

            reportColumnHeader = "Insurer";
            selectedOrgLabel = "Insurer";
            userOrgLabel = "Credit Hire Organisation";

            if ((externalParameter.get("insurerId")) != null) {
                insrId = ((String[]) externalParameter.get("insurerId"))[0];
                if (!insrId.equalsIgnoreCase("")) {
                    insurerId = TextHelper.getId(insrId);
                    selectedOrgName = getInsurer(insurerId).getName();
                }
            }

            LOG.debug("choId :" + choId);
            LOG.debug("insurerId :" + insurerId);
            LOG.debug("selectedOrgName :" + selectedOrgName);


        }


        List<BreInvoiceApprovalDisputedData> breInvoiceApproval = new ArrayList<BreInvoiceApprovalDisputedData>();

        StringBuffer sb = new StringBuffer();
        sb.append("select ");
        sb.append("(select TEXT(\'Last 12 Months\'))as month_header, ");
        sb.append("(select count(*) from claim c, invoice i "
                + "where c.invoice_id = i.id "
                + "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) "
                + "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) "
                + "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  "
                + "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as invoice_uploaded_total, ");

        sb.append("(select count(*) from claim c, invoice i, audit_trail a "
                + "where c.invoice_id = i.id "
                + "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) "
                + "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) "
                + "and a.claim_id = c.id "
                + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                + "and a.new_status='InvoiceApprovedByBRE' "
                + "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months' "
                + "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\'))) as  invoice_approved_by_bre_total, ");
        sb.append("(select count(distinct b.id) from ( select c.id, c.invoice_id from claim c, invoice i, audit_trail a "
                + "where c.invoice_id = i.id "
                + "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) "
                + "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) "
                + "and a.claim_id = c.id "
                + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                + "and a.new_status='InvoiceApprovedByBRE' "
                + "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months' "
                + "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\'))) b, audit_trail a "
                + "where b.id = a.claim_id and a.new_status = 'ContestedInvoiceReferredToCHO' ) as  invoice_approved_by_bre_disputed_total, ");

        sb.append("(select count(*) from (select c.id, c.invoice_id, i.created_date from claim c, invoice i, audit_trail a "
                + "where c.invoice_id = i.id "
                + "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) "
                + "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) "
                + "and a.claim_id = c.id "
                + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                + "and a.new_status='InvoiceApprovedByBRE' "
                + "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months' "
                + "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\'))) b , audit_trail a1 "
                + "where b.id = a1.claim_id "
                + "and a1.new_status ='PaymentReceived' "
                + "and not exists ( select * from audit_trail a1 where a1.claim_id =b.id and a1.new_status='ContestedInvoiceReferredToCHO') "
                + "and a1.update_date between b.created_date  and  b.created_date + interval '15 days' )as invoice_approved_by_bre_not_disputed_paid_within_15days_total, ");

        sb.append("(select count(*) from (select c.id, c.invoice_id, i.created_date from claim c, invoice i, audit_trail a "
                + "where c.invoice_id = i.id "
                + "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) "
                + "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) "
                + "and a.claim_id = c.id "
                + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                + "and a.new_status='InvoiceApprovedByBRE' "
                + "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months' "
                + "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\'))) b , audit_trail a1 "
                + "where b.id = a1.claim_id "
                + "and a1.new_status ='PaymentReceived' "
                + "and not exists ( select * from audit_trail a1 where a1.claim_id =b.id and a1.new_status='ContestedInvoiceReferredToCHO') "
                + "and a1.update_date between b.created_date  and  b.created_date + interval '30 days' )as invoice_approved_by_bre_not_disputed_paid_within_30days_total, ");

        sb.append("(select count(*) from (select c.id, c.invoice_id, i.created_date from claim c, invoice i, audit_trail a "
                + "where c.invoice_id = i.id "
                + "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) "
                + "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) "
                + "and a.claim_id = c.id "
                + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                + "and a.new_status='InvoiceApprovedByBRE' "
                + "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months' "
                + "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\'))) b , audit_trail a1 "
                + "where b.id = a1.claim_id "
                + "and a1.new_status ='PaymentReceived' "
                + "and exists ( select * from audit_trail a1 where a1.claim_id =b.id and a1.new_status='ContestedInvoiceReferredToCHO') "
                + "and a1.update_date    between b.created_date  and  b.created_date + interval '15 days' )as invoice_approved_by_bre_disputed_paid_within_15days_total, ");
        sb.append("(select count(*) from (select c.id, c.invoice_id, i.created_date from claim c, invoice i, audit_trail a "
                + "where c.invoice_id = i.id "
                + "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) "
                + "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) "
                + "and a.claim_id = c.id "
                + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                + "and a.new_status='InvoiceApprovedByBRE' "
                + "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months' "
                + "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\'))) b , audit_trail a1 "
                + "where b.id = a1.claim_id "
                + "and a1.new_status ='PaymentReceived' "
                + "and exists ( select * from audit_trail a1 where a1.claim_id =b.id and a1.new_status='ContestedInvoiceReferredToCHO') "
                + "and a1.update_date between b.created_date  and  b.created_date + interval '30 days' )as invoice_approved_by_bre_disputed_paid_within_30days_total, ");

        sb.append("(select count(*) from ( select c.id, c.invoice_id from claim c, invoice i, audit_trail a "
                + "where c.invoice_id = i.id "
                + "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) "
                + "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) "
                + "and a.claim_id = c.id "
                + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                + "and a.new_status='InvoiceApprovedByBRE' "
                + "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months' "
                + "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\'))) b, audit_trail a, invoice i, reason_of_rejection r "
                + "where b.id = a.claim_id "
                + "and a.new_status = 'ContestedInvoiceReferredToCHO' "
                + "and b.invoice_id = i.id "
                + "and a.invoice_reason_of_rejection = r.id "
                + "and r.name = 'Hire Charge' "
                + "and not exists (select * from audit_trail a2 where a2.claim_id=a.claim_id and a2.new_status='ContestedInvoiceReferredToCHO'  and a2.update_date < a.update_date)) as invoice_disputed_due_to_hire_charge_total, ");
        sb.append("(select count(*) from ( select c.id, c.invoice_id from claim c, invoice i, audit_trail a "
                + "where c.invoice_id = i.id "
                + "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) "
                + "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) "
                + "and a.claim_id = c.id "
                + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                + "and a.new_status='InvoiceApprovedByBRE' "
                + "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months' "
                + "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\'))) b, audit_trail a, invoice i, reason_of_rejection r "
                + "where b.id = a.claim_id "
                + "and a.new_status = 'ContestedInvoiceReferredToCHO' "
                + "and b.invoice_id = i.id "
                + "and a.invoice_reason_of_rejection = r.id "
                + "and r.name = 'Hire Duration' "
                + "and not exists (select * from audit_trail a2 where a2.claim_id=a.claim_id and a2.new_status='ContestedInvoiceReferredToCHO'  and a2.update_date < a.update_date)) as invoice_disputed_due_to_hire_duration_total, ");

       sb.append("(select count(*) from ( select c.id, c.invoice_id from claim c, invoice i, audit_trail a "
                + "where c.invoice_id = i.id "
                + "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) "
                + "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) "
                + "and a.claim_id = c.id "
                + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                + "and a.new_status='InvoiceApprovedByBRE' "
                + "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months' "
                + "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\'))) b, audit_trail a, invoice i, reason_of_rejection r "
                + "where b.id = a.claim_id "
                + "and a.new_status = 'ContestedInvoiceReferredToCHO' "
                + "and b.invoice_id = i.id "
                + "and a.invoice_reason_of_rejection = r.id "
                + "and r.name = 'Liability Dispute' "
                + "and not exists (select * from audit_trail a2 where a2.claim_id=a.claim_id and a2.new_status='ContestedInvoiceReferredToCHO'  and a2.update_date < a.update_date)) as invoice_disputed_due_to_liability_dispute_total, ");

       sb.append("(select count(*) from ( select c.id, c.invoice_id from claim c, invoice i, audit_trail a "
                + "where c.invoice_id = i.id "
                + "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) "
                + "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) "
                + "and a.claim_id = c.id "
                + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                + "and a.new_status='InvoiceApprovedByBRE' "
                + "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months' "
                + "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\'))) b, audit_trail a, invoice i, reason_of_rejection r "
                + "where b.id = a.claim_id "
                + "and a.new_status = 'ContestedInvoiceReferredToCHO' "
                + "and b.invoice_id = i.id "
                + "and a.invoice_reason_of_rejection = r.id "
                + "and r.name = 'Like for Like' "
                + "and not exists (select * from audit_trail a2 where a2.claim_id=a.claim_id and a2.new_status='ContestedInvoiceReferredToCHO'  and a2.update_date < a.update_date)) as invoice_disputed_due_to_like_for_like_total, ");
 
        sb.append("(select count(*) from ( select c.id, c.invoice_id from claim c, invoice i, audit_trail a "
                + "where c.invoice_id = i.id "
                + "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) "
                + "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) "
                + "and a.claim_id = c.id "
                + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                + "and a.new_status='InvoiceApprovedByBRE' "
                + "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months' "
                + "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\'))) b, audit_trail a, invoice i, reason_of_rejection r "
                + "where b.id = a.claim_id "
                + "and a.new_status = 'ContestedInvoiceReferredToCHO' "
                + "and b.invoice_id = i.id "
                + "and a.invoice_reason_of_rejection = r.id "
                + "and r.name = 'Quantum' "
                + "and not exists (select * from audit_trail a2 where a2.claim_id=a.claim_id and a2.new_status='ContestedInvoiceReferredToCHO'  and a2.update_date < a.update_date)) as invoice_disputed_due_to_quantum_total, ");
        sb.append("(select count(*) from ( select c.id, c.invoice_id from claim c, invoice i, audit_trail a "
                + "where c.invoice_id = i.id "
                + "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) "
                + "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) "
                + "and a.claim_id = c.id "
                + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                + "and a.new_status='InvoiceApprovedByBRE' "
                + "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months' "
                + "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\'))) b, audit_trail a, invoice i, reason_of_rejection r "
                + "where b.id = a.claim_id "
                + "and a.new_status = 'ContestedInvoiceReferredToCHO' "
                + "and b.invoice_id = i.id "
                + "and a.invoice_reason_of_rejection = r.id "
                + "and r.name = 'Repair Cost' "
                + "and not exists (select * from audit_trail a2 where a2.claim_id=a.claim_id and a2.new_status='ContestedInvoiceReferredToCHO'  and a2.update_date < a.update_date)) as invoice_disputed_due_to_repair_cost_total, ");

       sb.append("(select count(*) from ( select c.id, c.invoice_id from claim c, invoice i, audit_trail a "
                + "where c.invoice_id = i.id "
                + "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) "
                + "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) "
                + "and a.claim_id = c.id "
                + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                + "and a.new_status='InvoiceApprovedByBRE' "
                + "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months' "
                + "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\'))) b, audit_trail a, invoice i, reason_of_rejection r "
                + "where b.id = a.claim_id "
                + "and a.new_status = 'ContestedInvoiceReferredToCHO' "
                + "and b.invoice_id = i.id "
                + "and a.invoice_reason_of_rejection = r.id "
                + "and r.name = 'Invoice Already Paid' "
                + "and not exists (select * from audit_trail a2 where a2.claim_id=a.claim_id and a2.new_status='ContestedInvoiceReferredToCHO'  and a2.update_date < a.update_date)) as invoice_disputed_due_to_invoice_already_paid_total, ");

       sb.append("(select count(*) from ( select c.id, c.invoice_id from claim c, invoice i, audit_trail a "
                + "where c.invoice_id = i.id "
                + "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) "
                + "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) "
                + "and a.claim_id = c.id "
                + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                + "and a.new_status='InvoiceApprovedByBRE' "
                + "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months' "
                + "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\'))) b, audit_trail a, invoice i, reason_of_rejection r "
                + "where b.id = a.claim_id "
                + "and a.new_status = 'ContestedInvoiceReferredToCHO' "
                + "and b.invoice_id = i.id "
                + "and a.invoice_reason_of_rejection = r.id "
                + "and r.name = 'Undisclosed' "
                + "and not exists (select * from audit_trail a2 where a2.claim_id=a.claim_id and a2.new_status='ContestedInvoiceReferredToCHO'  and a2.update_date < a.update_date)) as invoice_disputed_due_to_undisclosed_total, ");


       sb.append("(select count(*) from ( select c.id, c.invoice_id from claim c, invoice i, audit_trail a "
                + "where c.invoice_id = i.id "
                + "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) "
                + "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) "
                + "and a.claim_id = c.id "
                + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                + "and a.new_status='InvoiceApprovedByBRE' "
                + "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months' "
                + "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\'))) b, audit_trail a, invoice i, reason_of_rejection r "
                + "where b.id = a.claim_id "
                + "and a.new_status = 'ContestedInvoiceReferredToCHO' "
                + "and b.invoice_id = i.id "
                + "and a.invoice_reason_of_rejection = r.id "
                + "and r.name = 'Other' "
                + "and not exists (select * from audit_trail a2 where a2.claim_id=a.claim_id and a2.new_status='ContestedInvoiceReferredToCHO'  and a2.update_date < a.update_date)) as invoice_disputed_due_to_other_total ");




        String query = sb.toString();
        LOG.debug(query);
        Map paramMap = new HashMap();
        paramMap.put("pStartDate", dataStart);
        paramMap.put("pChorgId", choId);
        paramMap.put("pInsurerId", insurerId);

        BreInvoiceApprovalDisputeCumulativeData breInvoiceApprovalDisputeCumulativeData = null;

        List result = baseDataService.externalQuery(query, paramMap);

        for (int i = 0; i < result.size(); i++) {


            LOG.debug("results :" + result.get(i));
        }



        for (Object o : result) {

            LOG.debug("inside for loop");
            Map data = (Map) o;
            breInvoiceApprovalDisputeCumulativeData = BreInvoiceApprovalDisputeCumulativeData.getObject(data);
            // invoiceStatusReportCummulative.add(invoiceStatusReportDataCumm);

        }

        LOG.debug("outside for loop");

        for (int x = 0; x <= 11; x++) {

            int y = 1 - x;


            StringBuffer sb1 = new StringBuffer();
            sb1.append("select ");
            if (x == 0) {
                sb1.append("(select TO_CHAR(cast(:pStartDate as Date), TEXT(\'MON\')) || TEXT(\'-\') || TO_CHAR(cast(:pStartDate as Date), TEXT(\'yyyy\')))as month_header, ");
            }
            if (x == 1) {
                sb1.append("(select TO_CHAR(cast(:pStartDate as Date) - interval '1 month', TEXT(\'MON\')) || TEXT(\'-\') || TO_CHAR(cast(:pStartDate as Date) - interval '1 month', TEXT(\'yyyy\')))as month_header, ");
            }
            if (x == 2) {
                sb1.append("(select TO_CHAR(cast(:pStartDate as Date) - interval '2 months', TEXT(\'MON\')) || TEXT(\'-\') || TO_CHAR(cast(:pStartDate as Date) - interval '2 months', TEXT(\'yyyy\')))as month_header, ");
            }
            if (x == 3) {
                sb1.append("(select TO_CHAR(cast(:pStartDate as Date) - interval '3 months', TEXT(\'MON\')) || TEXT(\'-\') || TO_CHAR(cast(:pStartDate as Date) - interval '3 months', TEXT(\'yyyy\')))as month_header, ");
            }
            if (x == 4) {
                sb1.append("(select TO_CHAR(cast(:pStartDate as Date) - interval '4 months', TEXT(\'MON\')) || TEXT(\'-\') || TO_CHAR(cast(:pStartDate as Date) - interval '4 months', TEXT(\'yyyy\')))as month_header, ");
            }
            if (x == 5) {
                sb1.append("(select TO_CHAR(cast(:pStartDate as Date) - interval '5 months', TEXT(\'MON\')) || TEXT(\'-\') || TO_CHAR(cast(:pStartDate as Date) - interval '5 months', TEXT(\'yyyy\')))as month_header, ");
            }
            if (x == 6) {
                sb1.append("(select TO_CHAR(cast(:pStartDate as Date) - interval '6 months', TEXT(\'MON\')) || TEXT(\'-\') || TO_CHAR(cast(:pStartDate as Date) - interval '6 months', TEXT(\'yyyy\')))as month_header, ");
            }
            if (x == 7) {
                sb1.append("(select TO_CHAR(cast(:pStartDate as Date) - interval '7 months', TEXT(\'MON\')) || TEXT(\'-\') || TO_CHAR(cast(:pStartDate as Date) - interval '7 months', TEXT(\'yyyy\')))as month_header, ");
            }
            if (x == 8) {
                sb1.append("(select TO_CHAR(cast(:pStartDate as Date) - interval '8 months', TEXT(\'MON\')) || TEXT(\'-\') || TO_CHAR(cast(:pStartDate as Date) - interval '8 months', TEXT(\'yyyy\')))as month_header, ");
            }
            if (x == 9) {
                sb1.append("(select TO_CHAR(cast(:pStartDate as Date) - interval '9 months', TEXT(\'MON\')) || TEXT(\'-\') || TO_CHAR(cast(:pStartDate as Date) - interval '9 months', TEXT(\'yyyy\')))as month_header, ");
            }
            if (x == 10) {
                sb1.append("(select TO_CHAR(cast(:pStartDate as Date) - interval '10 months', TEXT(\'MON\')) || TEXT(\'-\') || TO_CHAR(cast(:pStartDate as Date) - interval '10 months', TEXT(\'yyyy\'))) as month_header, ");
            }
            if (x == 11) {
                sb1.append("(select TO_CHAR(cast(:pStartDate as Date) - interval '11 months', TEXT(\'MON\')) || TEXT(\'-\') || TO_CHAR(cast(:pStartDate as Date) - interval '11 months', TEXT(\'yyyy\')))as month_header, ");
            }




            


            sb1.append("(select count(*) from claim c, invoice i "
                    + "where c.invoice_id = i.id "
                    + "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) "
                    + "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) "
                    + "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) "
                    + "|| '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) ");
            if (x > 0 && x == 1) {

                sb1.append(" - interval ' " + x + "  month' ");
            } else {
                sb1.append(" - interval ' " + x + "  months' ");
            }

            sb1.append("and to_date(to_char(cast(:pStartDate as Date) ");

            if (y > 0) {
                sb1.append(" + interval '" + y + " month'");
            }

            if (y < 0 && y == -1) {
                int z = -y;
                sb1.append(" - interval '" + z + " month'");

            }else {

                int z = -y;
                sb1.append(" - interval '" + z + " months'");

            }

            sb1.append(", TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) ");

            if (y > 0) {
                sb1.append(" + interval '" + y + " month'");
            }
            if (y < 0 && y == -1) {
                int z = -y;
                sb1.append(" - interval '" + z + " month'");
            } else {

                int z = -y;
                sb1.append(" - interval '" + z + " months'");

            }

            sb1.append(", TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\'))) "
                    + "as invoice_uploaded_current_total, ");


            sb1.append("(select count(*) from claim c, invoice i, audit_trail a "
                    + "where c.invoice_id = i.id "
                    + "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) "
                    + "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) "
                    + "and a.claim_id = c.id "
                    + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                    + "and a.new_status='InvoiceApprovedByBRE' "
                     + "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) "
                    + "|| '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) ");
            if (x > 0 && x == 1) {

                sb1.append(" - interval ' " + x + "  month' ");
            } else {
                sb1.append(" - interval ' " + x + "  months' ");
            }

            sb1.append("and to_date(to_char(cast(:pStartDate as Date) ");

            if (y > 0) {
                sb1.append(" + interval '" + y + " month'");
            }

            if (y < 0 && y == -1) {
                int z = -y;
                sb1.append(" - interval '" + z + " month'");

            }else {

                int z = -y;
                sb1.append(" - interval '" + z + " months'");

            }

            sb1.append(", TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) ");

            if (y > 0) {
                sb1.append(" + interval '" + y + " month'");
            }
            if (y < 0 && y == -1) {
                int z = -y;
                sb1.append(" - interval '" + z + " month'");
            } else {

                int z = -y;
                sb1.append(" - interval '" + z + " months'");

            }

            sb1.append(", TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\'))) "
                    + "as invoice_approved_by_bre_current, ");




            sb1.append("(select count(distinct b.id) from ( select c.id, c.invoice_id from claim c, invoice i, audit_trail a "
                    + "where c.invoice_id = i.id "
                    + "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) "
                    + "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) "
                    + "and a.claim_id = c.id "
                    + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                    + "and a.new_status='InvoiceApprovedByBRE' "
                    + "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) "
                    + "|| '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) ");
            if (x > 0 && x == 1) {

                sb1.append(" - interval ' " + x + "  month' ");
            } else {
                sb1.append(" - interval ' " + x + "  months' ");
            }

            sb1.append("and to_date(to_char(cast(:pStartDate as Date) ");

            if (y > 0) {
                sb1.append(" + interval '" + y + " month'");
            }

            if (y < 0 && y == -1) {
                int z = -y;
                sb1.append(" - interval '" + z + " month'");

            }else {

                int z = -y;
                sb1.append(" - interval '" + z + " months'");

            }

            sb1.append(", TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) ");

            if (y > 0) {
                sb1.append(" + interval '" + y + " month'");
            }
            if (y < 0 && y == -1) {
                int z = -y;
                sb1.append(" - interval '" + z + " month'");
            } else {

                int z = -y;
                sb1.append(" - interval '" + z + " months'");

            }

            sb1.append(", TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\'))) "
                    + "b, audit_trail a where b.id = a.claim_id and a.new_status = 'ContestedInvoiceReferredToCHO' ) as invoice_approved_by_bre_disputed_current, ");
       


            sb1.append("(select count(*) from (select c.id, c.invoice_id, i.created_date from claim c, invoice i, audit_trail a "
                    + "where c.invoice_id = i.id "
                    + "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) "
                    + "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) "
                    + "and a.claim_id = c.id "
                    + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                    + "and a.new_status='InvoiceApprovedByBRE' "
                    + "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) "
                    + "|| '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) ");
            if (x > 0 && x == 1) {

                sb1.append(" - interval ' " + x + "  month' ");
            } else {
                sb1.append(" - interval ' " + x + "  months' ");
            }

            sb1.append("and to_date(to_char(cast(:pStartDate as Date) ");

            if (y > 0) {
                sb1.append(" + interval '" + y + " month'");
            }

            if (y < 0 && y == -1) {
                int z = -y;
                sb1.append(" - interval '" + z + " month'");

            }else {

                int z = -y;
                sb1.append(" - interval '" + z + " months'");

            }

            sb1.append(", TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) ");

            if (y > 0) {
                sb1.append(" + interval '" + y + " month'");
            }
            if (y < 0 && y == -1) {
                int z = -y;
                sb1.append(" - interval '" + z + " month'");
            } else {

                int z = -y;
                sb1.append(" - interval '" + z + " months'");

            }

            sb1.append(", TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\'))) "
                    + "b , audit_trail a1 "
                    + "where b.id = a1.claim_id "
                    + "and a1.new_status ='PaymentReceived' "
                    + "and not exists ( select * from audit_trail a1 where a1.claim_id =b.id and a1.new_status='ContestedInvoiceReferredToCHO') "
                    + "and a1.update_date between b.created_date  and  b.created_date + interval '15 days'  )as invoice_approved_by_bre_not_disputed_paid_within_15days_current, ");




            sb1.append("(select count(*) from (select c.id, c.invoice_id, i.created_date from claim c, invoice i, audit_trail a "
                    + "where c.invoice_id = i.id "
                    + "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) "
                    + "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) "
                    + "and a.claim_id = c.id "
                    + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                    + "and a.new_status='InvoiceApprovedByBRE' "
                    + "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) "
                    + "|| '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) ");
            if (x > 0 && x == 1) {

                sb1.append(" - interval ' " + x + "  month' ");
            } else {
                sb1.append(" - interval ' " + x + "  months' ");
            }

            sb1.append("and to_date(to_char(cast(:pStartDate as Date) ");

            if (y > 0) {
                sb1.append(" + interval '" + y + " month'");
            }

            if (y < 0 && y == -1) {
                int z = -y;
                sb1.append(" - interval '" + z + " month'");

            }else {

                int z = -y;
                sb1.append(" - interval '" + z + " months'");

            }

            sb1.append(", TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) ");

            if (y > 0) {
                sb1.append(" + interval '" + y + " month'");
            }
            if (y < 0 && y == -1) {
                int z = -y;
                sb1.append(" - interval '" + z + " month'");
            } else {

                int z = -y;
                sb1.append(" - interval '" + z + " months'");

            }

            sb1.append(", TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\'))) "
                    + "b , audit_trail a1 where b.id = a1.claim_id "
                    + "and a1.new_status ='PaymentReceived' "
                    + "and not exists ( select * from audit_trail a1 where a1.claim_id =b.id and a1.new_status='ContestedInvoiceReferredToCHO') "
                    + "and a1.update_date between b.created_date  and  b.created_date + interval '30 days'  )as invoice_approved_by_bre_not_disputed_paid_within_30days_current, ");



            sb1.append("(select count(*) from (select c.id, c.invoice_id, i.created_date from claim c, invoice i, audit_trail a "
                    + "where c.invoice_id = i.id "
                    + "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) "
                    + "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) "
                    + "and a.claim_id = c.id "
                    + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                    + "and a.new_status='InvoiceApprovedByBRE' "
                    + "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) "
                    + "|| '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) ");
            if (x > 0 && x == 1) {

                sb1.append(" - interval ' " + x + "  month' ");
            } else {
                sb1.append(" - interval ' " + x + "  months' ");
            }

            sb1.append("and to_date(to_char(cast(:pStartDate as Date) ");

            if (y > 0) {
                sb1.append(" + interval '" + y + " month'");
            }

            if (y < 0 && y == -1) {
                int z = -y;
                sb1.append(" - interval '" + z + " month'");

            }else {

                int z = -y;
                sb1.append(" - interval '" + z + " months'");

            }

            sb1.append(", TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) ");

            if (y > 0) {
                sb1.append(" + interval '" + y + " month'");
            }
            if (y < 0 && y == -1) {
                int z = -y;
                sb1.append(" - interval '" + z + " month'");
            } else {

                int z = -y;
                sb1.append(" - interval '" + z + " months'");

            }

            sb1.append(", TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\'))) "
                    + "b , audit_trail a1 "
                    + "where b.id = a1.claim_id "
                    + "and a1.new_status ='PaymentReceived' "
                    + "and exists ( select * from audit_trail a1 where a1.claim_id =b.id and a1.new_status='ContestedInvoiceReferredToCHO') "
                    + "and a1.update_date between b.created_date  and  b.created_date + interval '15 days'  )as invoice_approved_by_bre_disputed_paid_within_15days_current, ");



            sb1.append("(select count(*) from (select c.id, c.invoice_id, i.created_date from claim c, invoice i, audit_trail a "
                    + "where c.invoice_id = i.id "
                    + "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) "
                    + "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) "
                    + "and a.claim_id = c.id "
                    + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                    + "and a.new_status='InvoiceApprovedByBRE' "
                    + "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) "
                    + "|| '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) ");
            if (x > 0 && x == 1) {

                sb1.append(" - interval ' " + x + "  month' ");
            } else {
                sb1.append(" - interval ' " + x + "  months' ");
            }

            sb1.append("and to_date(to_char(cast(:pStartDate as Date) ");

            if (y > 0) {
                sb1.append(" + interval '" + y + " month'");
            }

            if (y < 0 && y == -1) {
                int z = -y;
                sb1.append(" - interval '" + z + " month'");

            }else {

                int z = -y;
                sb1.append(" - interval '" + z + " months'");

            }

            sb1.append(", TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) ");

            if (y > 0) {
                sb1.append(" + interval '" + y + " month'");
            }
            if (y < 0 && y == -1) {
                int z = -y;
                sb1.append(" - interval '" + z + " month'");
            } else {

                int z = -y;
                sb1.append(" - interval '" + z + " months'");

            }

            sb1.append(", TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\'))) "
                    + "b , audit_trail a1 where b.id = a1.claim_id "
                    + "and a1.new_status ='PaymentReceived' "
                    + "and exists ( select * from audit_trail a1 where a1.claim_id =b.id and a1.new_status='ContestedInvoiceReferredToCHO') "
                    + "and a1.update_date between b.created_date  and  b.created_date + interval '30 days'  ) as invoice_approved_by_bre_disputed_paid_within_30days_current, ");

            sb1.append("(select count(*) from ( select c.id, c.invoice_id from claim c, invoice i, audit_trail a "
                    + "where c.invoice_id = i.id "
                    + "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) "
                    + "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) "
                    + "and a.claim_id = c.id "
                    + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                    + "and a.new_status='InvoiceApprovedByBRE' "
                     + "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) "
                    + "|| '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) ");
            if (x > 0 && x == 1) {

                sb1.append(" - interval ' " + x + "  month' ");
            } else {
                sb1.append(" - interval ' " + x + "  months' ");
            }

            sb1.append("and to_date(to_char(cast(:pStartDate as Date) ");

            if (y > 0) {
                sb1.append(" + interval '" + y + " month'");
            }

            if (y < 0 && y == -1) {
                int z = -y;
                sb1.append(" - interval '" + z + " month'");

            }else {

                int z = -y;
                sb1.append(" - interval '" + z + " months'");

            }

            sb1.append(", TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) ");

            if (y > 0) {
                sb1.append(" + interval '" + y + " month'");
            }
            if (y < 0 && y == -1) {
                int z = -y;
                sb1.append(" - interval '" + z + " month'");
            } else {

                int z = -y;
                sb1.append(" - interval '" + z + " months'");

            }

            sb1.append(", TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\'))) "
                    + "b, audit_trail a, invoice i, reason_of_rejection r "
                    + "where b.id = a.claim_id "
                    + "and a.new_status = 'ContestedInvoiceReferredToCHO' "
                    + "and b.invoice_id = i.id "
                    + "and a.invoice_reason_of_rejection = r.id "
                    + "and r.name = 'Hire Charge' "
                    + "and not exists (select * from audit_trail a2 where a2.claim_id=a.claim_id and a2.new_status='ContestedInvoiceReferredToCHO'  and a2.update_date < a.update_date)) as invoice_disputed_due_to_hire_charge_current, ");


            sb1.append("(select count(*) from ( select c.id, c.invoice_id from claim c, invoice i, audit_trail a "
                    + "where c.invoice_id = i.id "
                    + "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) "
                    + "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) "
                    + "and a.claim_id = c.id "
                    + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                    + "and a.new_status='InvoiceApprovedByBRE' "
                     + "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) "
                    + "|| '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) ");
            if (x > 0 && x == 1) {

                sb1.append(" - interval ' " + x + "  month' ");
            } else {
                sb1.append(" - interval ' " + x + "  months' ");
            }

            sb1.append("and to_date(to_char(cast(:pStartDate as Date) ");

            if (y > 0) {
                sb1.append(" + interval '" + y + " month'");
            }

            if (y < 0 && y == -1) {
                int z = -y;
                sb1.append(" - interval '" + z + " month'");

            }else {

                int z = -y;
                sb1.append(" - interval '" + z + " months'");

            }

            sb1.append(", TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) ");

            if (y > 0) {
                sb1.append(" + interval '" + y + " month'");
            }
            if (y < 0 && y == -1) {
                int z = -y;
                sb1.append(" - interval '" + z + " month'");
            } else {

                int z = -y;
                sb1.append(" - interval '" + z + " months'");

            }

            sb1.append(", TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\'))) "
                    + "b, audit_trail a, invoice i, reason_of_rejection r "
                    + "where b.id = a.claim_id "
                    + "and a.new_status = 'ContestedInvoiceReferredToCHO' "
                    + "and b.invoice_id = i.id "
                    + "and a.invoice_reason_of_rejection = r.id "
                    + "and r.name = 'Hire Duration' "
                    + "and not exists (select * from audit_trail a2 where a2.claim_id=a.claim_id and a2.new_status='ContestedInvoiceReferredToCHO'  and a2.update_date < a.update_date)) as invoice_disputed_due_to_hire_duration_current, ");


            sb1.append("(select count(*) from ( select c.id, c.invoice_id from claim c, invoice i, audit_trail a "
                    + "where c.invoice_id = i.id "
                    + "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) "
                    + "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) "
                    + "and a.claim_id = c.id "
                    + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                    + "and a.new_status='InvoiceApprovedByBRE' "
                     + "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) "
                    + "|| '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) ");
            if (x > 0 && x == 1) {

                sb1.append(" - interval ' " + x + "  month' ");
            } else {
                sb1.append(" - interval ' " + x + "  months' ");
            }

            sb1.append("and to_date(to_char(cast(:pStartDate as Date) ");

            if (y > 0) {
                sb1.append(" + interval '" + y + " month'");
            }

            if (y < 0 && y == -1) {
                int z = -y;
                sb1.append(" - interval '" + z + " month'");

            }else {

                int z = -y;
                sb1.append(" - interval '" + z + " months'");

            }

            sb1.append(", TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) ");

            if (y > 0) {
                sb1.append(" + interval '" + y + " month'");
            }
            if (y < 0 && y == -1) {
                int z = -y;
                sb1.append(" - interval '" + z + " month'");
            } else {

                int z = -y;
                sb1.append(" - interval '" + z + " months'");

            }

            sb1.append(", TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\'))) "
                    + "b, audit_trail a, invoice i, reason_of_rejection r "
                    + "where b.id = a.claim_id "
                    + "and a.new_status = 'ContestedInvoiceReferredToCHO' "
                    + "and b.invoice_id = i.id "
                    + "and a.invoice_reason_of_rejection = r.id "
                    + "and r.name = 'Liability Dispute' "
                    + "and not exists (select * from audit_trail a2 where a2.claim_id=a.claim_id and a2.new_status='ContestedInvoiceReferredToCHO'  and a2.update_date < a.update_date)) as invoice_disputed_due_to_liability_dispute_current, ");


            sb1.append("(select count(*) from ( select c.id, c.invoice_id from claim c, invoice i, audit_trail a "
                    + "where c.invoice_id = i.id "
                    + "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) "
                    + "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) "
                    + "and a.claim_id = c.id "
                    + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                    + "and a.new_status='InvoiceApprovedByBRE' "
                     + "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) "
                    + "|| '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) ");
            if (x > 0 && x == 1) {

                sb1.append(" - interval ' " + x + "  month' ");
            } else {
                sb1.append(" - interval ' " + x + "  months' ");
            }

            sb1.append("and to_date(to_char(cast(:pStartDate as Date) ");

            if (y > 0) {
                sb1.append(" + interval '" + y + " month'");
            }

            if (y < 0 && y == -1) {
                int z = -y;
                sb1.append(" - interval '" + z + " month'");

            }else {

                int z = -y;
                sb1.append(" - interval '" + z + " months'");

            }

            sb1.append(", TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) ");

            if (y > 0) {
                sb1.append(" + interval '" + y + " month'");
            }
            if (y < 0 && y == -1) {
                int z = -y;
                sb1.append(" - interval '" + z + " month'");
            } else {

                int z = -y;
                sb1.append(" - interval '" + z + " months'");

            }

            sb1.append(", TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\'))) "
                    + "b, audit_trail a, invoice i, reason_of_rejection r "
                    + "where b.id = a.claim_id "
                    + "and a.new_status = 'ContestedInvoiceReferredToCHO' "
                    + "and b.invoice_id = i.id "
                    + "and a.invoice_reason_of_rejection = r.id "
                    + "and r.name = 'Like for Like' "
                    + "and not exists (select * from audit_trail a2 where a2.claim_id=a.claim_id and a2.new_status='ContestedInvoiceReferredToCHO'  and a2.update_date < a.update_date)) as invoice_disputed_due_to_like_for_like_current, ");


            sb1.append("(select count(*) from ( select c.id, c.invoice_id from claim c, invoice i, audit_trail a "
                    + "where c.invoice_id = i.id "
                    + "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) "
                    + "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) "
                    + "and a.claim_id = c.id "
                    + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                    + "and a.new_status='InvoiceApprovedByBRE' "
                     + "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) "
                    + "|| '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) ");
            if (x > 0 && x == 1) {

                sb1.append(" - interval ' " + x + "  month' ");
            } else {
                sb1.append(" - interval ' " + x + "  months' ");
            }

            sb1.append("and to_date(to_char(cast(:pStartDate as Date) ");

            if (y > 0) {
                sb1.append(" + interval '" + y + " month'");
            }

            if (y < 0 && y == -1) {
                int z = -y;
                sb1.append(" - interval '" + z + " month'");

            }else {

                int z = -y;
                sb1.append(" - interval '" + z + " months'");

            }

            sb1.append(", TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) ");

            if (y > 0) {
                sb1.append(" + interval '" + y + " month'");
            }
            if (y < 0 && y == -1) {
                int z = -y;
                sb1.append(" - interval '" + z + " month'");
            } else {

                int z = -y;
                sb1.append(" - interval '" + z + " months'");

            }

            sb1.append(", TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\'))) "
                    + "b, audit_trail a, invoice i, reason_of_rejection r "
                    + "where b.id = a.claim_id "
                    + "and a.new_status = 'ContestedInvoiceReferredToCHO' "
                    + "and b.invoice_id = i.id "
                    + "and a.invoice_reason_of_rejection = r.id "
                    + "and r.name = 'Quantum' "
                    + "and not exists (select * from audit_trail a2 where a2.claim_id=a.claim_id and a2.new_status='ContestedInvoiceReferredToCHO'  and a2.update_date < a.update_date)) as invoice_disputed_due_to_quantum_current, ");


            sb1.append("(select count(*) from ( select c.id, c.invoice_id from claim c, invoice i, audit_trail a "
                    + "where c.invoice_id = i.id "
                    + "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) "
                    + "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) "
                    + "and a.claim_id = c.id "
                    + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                    + "and a.new_status='InvoiceApprovedByBRE' "
                     + "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) "
                    + "|| '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) ");
            if (x > 0 && x == 1) {

                sb1.append(" - interval ' " + x + "  month' ");
            } else {
                sb1.append(" - interval ' " + x + "  months' ");
            }

            sb1.append("and to_date(to_char(cast(:pStartDate as Date) ");

            if (y > 0) {
                sb1.append(" + interval '" + y + " month'");
            }

            if (y < 0 && y == -1) {
                int z = -y;
                sb1.append(" - interval '" + z + " month'");

            }else {

                int z = -y;
                sb1.append(" - interval '" + z + " months'");

            }

            sb1.append(", TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) ");

            if (y > 0) {
                sb1.append(" + interval '" + y + " month'");
            }
            if (y < 0 && y == -1) {
                int z = -y;
                sb1.append(" - interval '" + z + " month'");
            } else {

                int z = -y;
                sb1.append(" - interval '" + z + " months'");

            }

            sb1.append(", TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\'))) "
                    + "b, audit_trail a, invoice i, reason_of_rejection r "
                    + "where b.id = a.claim_id "
                    + "and a.new_status = 'ContestedInvoiceReferredToCHO' "
                    + "and b.invoice_id = i.id "
                    + "and a.invoice_reason_of_rejection = r.id "
                    + "and r.name = 'Repair Cost' "
                    + "and not exists (select * from audit_trail a2 where a2.claim_id=a.claim_id and a2.new_status='ContestedInvoiceReferredToCHO'  and a2.update_date < a.update_date)) as invoice_disputed_due_to_repair_cost_current, ");


            sb1.append("(select count(*) from ( select c.id, c.invoice_id from claim c, invoice i, audit_trail a "
                    + "where c.invoice_id = i.id "
                    + "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) "
                    + "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) "
                    + "and a.claim_id = c.id "
                    + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                    + "and a.new_status='InvoiceApprovedByBRE' "
                     + "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) "
                    + "|| '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) ");
            if (x > 0 && x == 1) {

                sb1.append(" - interval ' " + x + "  month' ");
            } else {
                sb1.append(" - interval ' " + x + "  months' ");
            }

            sb1.append("and to_date(to_char(cast(:pStartDate as Date) ");

            if (y > 0) {
                sb1.append(" + interval '" + y + " month'");
            }

            if (y < 0 && y == -1) {
                int z = -y;
                sb1.append(" - interval '" + z + " month'");

            }else {

                int z = -y;
                sb1.append(" - interval '" + z + " months'");

            }

            sb1.append(", TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) ");

            if (y > 0) {
                sb1.append(" + interval '" + y + " month'");
            }
            if (y < 0 && y == -1) {
                int z = -y;
                sb1.append(" - interval '" + z + " month'");
            } else {

                int z = -y;
                sb1.append(" - interval '" + z + " months'");

            }

            sb1.append(", TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\'))) "
                    + "b, audit_trail a, invoice i, reason_of_rejection r "
                    + "where b.id = a.claim_id "
                    + "and a.new_status = 'ContestedInvoiceReferredToCHO' "
                    + "and b.invoice_id = i.id "
                    + "and a.invoice_reason_of_rejection = r.id "
                    + "and r.name = 'Invoice Already Paid' "
                    + "and not exists (select * from audit_trail a2 where a2.claim_id=a.claim_id and a2.new_status='ContestedInvoiceReferredToCHO'  and a2.update_date < a.update_date)) as invoice_disputed_due_to_invoice_already_paid_current, ");


            sb1.append("(select count(*) from ( select c.id, c.invoice_id from claim c, invoice i, audit_trail a "
                    + "where c.invoice_id = i.id "
                    + "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) "
                    + "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) "
                    + "and a.claim_id = c.id "
                    + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                    + "and a.new_status='InvoiceApprovedByBRE' "
                     + "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) "
                    + "|| '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) ");
            if (x > 0 && x == 1) {

                sb1.append(" - interval ' " + x + "  month' ");
            } else {
                sb1.append(" - interval ' " + x + "  months' ");
            }

            sb1.append("and to_date(to_char(cast(:pStartDate as Date) ");

            if (y > 0) {
                sb1.append(" + interval '" + y + " month'");
            }

            if (y < 0 && y == -1) {
                int z = -y;
                sb1.append(" - interval '" + z + " month'");

            }else {

                int z = -y;
                sb1.append(" - interval '" + z + " months'");

            }

            sb1.append(", TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) ");

            if (y > 0) {
                sb1.append(" + interval '" + y + " month'");
            }
            if (y < 0 && y == -1) {
                int z = -y;
                sb1.append(" - interval '" + z + " month'");
            } else {

                int z = -y;
                sb1.append(" - interval '" + z + " months'");

            }

            sb1.append(", TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\'))) "
                    + "b, audit_trail a, invoice i, reason_of_rejection r "
                    + "where b.id = a.claim_id "
                    + "and a.new_status = 'ContestedInvoiceReferredToCHO' "
                    + "and b.invoice_id = i.id "
                    + "and a.invoice_reason_of_rejection = r.id "
                    + "and r.name = 'Undisclosed' "
                    + "and not exists (select * from audit_trail a2 where a2.claim_id=a.claim_id and a2.new_status='ContestedInvoiceReferredToCHO'  and a2.update_date < a.update_date)) as invoice_disputed_due_to_undisclosed_current, ");




            sb1.append("(select count(*) from ( select c.id, c.invoice_id from claim c, invoice i, audit_trail a "
                    + "where c.invoice_id = i.id "
                    + "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) "
                    + "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) "
                    + "and a.claim_id = c.id "
                    + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                    + "and a.new_status='InvoiceApprovedByBRE' "
                     + "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) "
                    + "|| '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) ");
            if (x > 0 && x == 1) {

                sb1.append(" - interval ' " + x + "  month' ");
            } else {
                sb1.append(" - interval ' " + x + "  months' ");
            }

            sb1.append("and to_date(to_char(cast(:pStartDate as Date) ");

            if (y > 0) {
                sb1.append(" + interval '" + y + " month'");
            }

            if (y < 0 && y == -1) {
                int z = -y;
                sb1.append(" - interval '" + z + " month'");

            }else {

                int z = -y;
                sb1.append(" - interval '" + z + " months'");

            }

            sb1.append(", TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) ");

            if (y > 0) {
                sb1.append(" + interval '" + y + " month'");
            }
            if (y < 0 && y == -1) {
                int z = -y;
                sb1.append(" - interval '" + z + " month'");
            } else {

                int z = -y;
                sb1.append(" - interval '" + z + " months'");

            }

            sb1.append(", TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\'))) "
                    + "b, audit_trail a, invoice i, reason_of_rejection r "
                    + "where b.id = a.claim_id "
                    + "and a.new_status = 'ContestedInvoiceReferredToCHO' "
                    + "and b.invoice_id = i.id "
                    + "and a.invoice_reason_of_rejection = r.id "
                    + "and r.name = 'Other' "
                    + "and not exists (select * from audit_trail a2 where a2.claim_id=a.claim_id and a2.new_status='ContestedInvoiceReferredToCHO'  and a2.update_date < a.update_date)) as invoice_disputed_due_to_other_current ");



            String query1 = sb1.toString();
            LOG.debug("query1 :"+query1);

            Map paramMap1 = new HashMap();
            paramMap1.put("pStartDate", dataStart);
            paramMap1.put("pChorgId", choId);
            paramMap1.put("pInsurerId", insurerId);





            List result1 = baseDataService.externalQuery(query1, paramMap1);

            for (int i = 0; i < result1.size(); i++) {


                LOG.debug("results :" + result1.get(i));
            }


            for (Object o : result1) {

                LOG.debug("inside for loop individual ");
                Map data1 = (Map) o;
                BreInvoiceApprovalDisputedData breInvoiceApprovalDisputedData = BreInvoiceApprovalDisputedData.getObject(data1);
                breInvoiceApproval.add(breInvoiceApprovalDisputedData);

                LOG.debug("bean is papulated ");

            }

        }


        reportParameters.put("breInvoiceCumulative", breInvoiceApprovalDisputeCumulativeData);
        reportParameters.put("breInvoiceApproval", breInvoiceApproval);

        reportParameters.put("dataStart", dataStart.toString());
        reportParameters.put("createDate", createDate.toString());
        reportParameters.put("userOrgLabel", userOrgLabel);
        reportParameters.put("userOrgName", userOrgName);
        reportParameters.put("selectedOrgName", selectedOrgName);
        reportParameters.put("selectedOrgLabel", selectedOrgLabel);
        reportParameters.put("reportColumnHeader", reportColumnHeader);



        return reportParameters;
    }



    @Override
    public String getReportTemplateFileName() {
        return "template_BreInvoiceApprovalDisputeReport.xls";
    }

    @Override
    public InputStream build() {
        ReportBuilder builder = getReportBuilder();
        return builder.buildReport(this);
    }
     protected ReportBuilder getReportBuilder() {
        return new ExcelReportBuilder();
    }

    @Override
    public String getReportCode() {
        return "RPT030";
    }

}
