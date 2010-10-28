package idas.chox.service.reports;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.model.WebUser;
import idas.chox.core.util.DateHelper;
import idas.chox.core.util.MathHelper;
import idas.chox.data.services.BaseDataService;
import idas.chox.service.reports.viewdata.OverviewSummaryLineItem;
import idas.chox.service.reports.viewdata.OverviewSummaryLineItemDetail;
import idas.chox.service.reports.viewdata.OverviewSummaryReportByOrg;
import idas.chox.service.reports.viewdata.OverviewSummaryReportObject;

public class OverviewSummaryReport implements Report {
    private static final Logger LOG = LoggerFactory.getLogger(OverviewSummaryReport.class);
    Map externalParameter;
    List<String> reportParameterNames;
    private BaseDataService baseDataService;
    private Date dataStart;
    private Date dataEnd;
    WebUser currentUser;
    private Integer userOrgId = -1;

    @Override
    public HashMap getReportParameters() {

        HashMap reportParameters = new HashMap();

        String userOrgLabel = "";
        String userOrgName = "";

        // currentUser = ((PermissionedUser) externalParameter.get("CurrentUser"));
        currentUser = ((WebUser) externalParameter.get("CurrentUser"));
        userOrgName = currentUser.getOrganisationName();

        if (currentUser.getInsurer()!=null) {
            userOrgLabel = "Insurer";
            userOrgId = currentUser.getInsurer().getId();
        } else {
            userOrgLabel = "Credit Hire Organisation";
            userOrgId = currentUser.getChorganisation().getId();
        }

        try {


            /*
            dataStart = DateHelper.LocalDateFormat.parse(((String[]) externalParameter.get("DateStart"))[0]);
            dataEnd = DateHelper.LocalDateFormat.parse(((String[]) externalParameter.get("DateEnd"))[0]);
            */

            if(((String[]) externalParameter.get("DateStart"))!=null){
                dataStart = DateHelper.Parse(((String[]) externalParameter.get("DateStart"))[0]);
            }

            if(((String[]) externalParameter.get("DateStart"))!=null){
                dataEnd = DateHelper.Parse(((String[]) externalParameter.get("DateEnd"))[0]);
            }

            StringBuffer sb = new StringBuffer();

            if (currentUser.getInsurer()!=null) {
                sb.append("select chorganisation.id as org_id, chorganisation.name as org_name, ");
            } else {
                sb.append("select insurer.id as org_id, insurer.name as org_name, ");
            }

            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_all_claim_with_invoice where date(claim_created_date) between :pUploadDateFrom and :pUploadDateTo and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id) as total_no_claims_num, ");
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice where date(claim_created_date) between :pUploadDateFrom and :pUploadDateTo and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id) as total_no_invoice_num, ");
            sb.append("(select case when sum(total_to_pay) is null then 0 else sum(total_to_pay) end as no_count from rpt_claim_invoice where date(claim_created_date) between :pUploadDateFrom and :pUploadDateTo and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id) as total_no_invoice_val, ");
            sb.append("(select case when sum(rpt_all_claim_with_invoice.total_to_pay) is null then 0.00 else sum(rpt_all_claim_with_invoice.total_to_pay) end as no_count from rpt_all_claim_with_invoice where date(claim_created_date) between :pUploadDateFrom and :pUploadDateTo and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id) as total_no_claims_val, ");
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_all_claim_with_invoice a, (select distinct claim_id from audit_trail where new_status='AwaitingCarHireInfo') b where a.claim_id=b.claim_id and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id and date(claim_created_date) between :pUploadDateFrom and :pUploadDateTo) as total_no_accepted_claims_num, ");
            sb.append("(select case when sum(a.total_to_pay) is null then 0.00 else sum(a.total_to_pay) end as no_count from rpt_all_claim_with_invoice a, (select distinct claim_id from audit_trail where new_status='AwaitingCarHireInfo') b where a.claim_id=b.claim_id and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id and date(claim_created_date) between :pUploadDateFrom and :pUploadDateTo) as total_no_accepted_claims_val, ");
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_all_claim_with_invoice a, (select distinct claim_id from audit_trail where new_status='ClaimRejectionAccepted') b where a.claim_id=b.claim_id and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id and date(claim_created_date) between :pUploadDateFrom and :pUploadDateTo) as total_no_rejected_claims_num, ");
            sb.append("(select case when sum(a.total_to_pay) is null then 0.00 else sum(a.total_to_pay) end as no_count from rpt_all_claim_with_invoice a, (select distinct claim_id from audit_trail where new_status='ClaimRejectionAccepted') b where a.claim_id=b.claim_id and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id and date(claim_created_date) between :pUploadDateFrom and :pUploadDateTo) as total_no_rejected_claims_val, ");
            
            //Changed to correct discrepency with invoice summary report
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice where status in ('InvoicePaymentLogged','PaymentReceived') and date(claim_created_date) between :pUploadDateFrom and :pUploadDateTo and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id) as total_no_approved_invoice_num, ");
            sb.append("(select case when sum(total_to_pay) is null then 0 else sum(total_to_pay) end as no_count from rpt_claim_invoice where status in ('InvoicePaymentLogged','PaymentReceived') and date(claim_created_date) between :pUploadDateFrom and :pUploadDateTo and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id) as total_no_approved_invoice_val, ");            
            //sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_all_claim_with_invoice a, (select distinct claim_id from audit_trail where new_status in ('PaymentReceived','InvoicePaymentLogged')) b where a.claim_id=b.claim_id and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id and date(claim_created_date) between :pUploadDateFrom and :pUploadDateTo) as total_no_approved_invoice_num, ");
            //sb.append("(select case when sum(a.total_to_pay) is null then 0.00 else sum(a.total_to_pay) end as no_count from rpt_all_claim_with_invoice a, (select distinct claim_id from audit_trail where new_status in ('PaymentReceived','InvoicePaymentLogged')) b where a.claim_id=b.claim_id and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id and date(claim_created_date) between :pUploadDateFrom and :pUploadDateTo) as total_no_approved_invoice_val, ");            
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_all_claim_with_invoice a, (select distinct claim_id from audit_trail where new_status='InvoiceRejectionAccepted') b where a.claim_id=b.claim_id and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id and date(claim_created_date) between :pUploadDateFrom and :pUploadDateTo) as total_no_rejected_invoice_num, ");
            sb.append("(select case when sum(a.total_to_pay) is null then 0.00 else sum(a.total_to_pay) end as no_count from rpt_all_claim_with_invoice a, (select distinct claim_id from audit_trail where new_status='InvoiceRejectionAccepted') b where a.claim_id=b.claim_id and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id and date(claim_created_date) between :pUploadDateFrom and :pUploadDateTo) as total_no_rejected_invoice_val, ");
            sb.append("(select case when count(*) is null or count(*) = 0 then 0 else cast(round(sum(EXTRACT(DAY FROM (audit.update_date - invoice.claim_created_date)))/count(*)) as bigint) end as no_count from rpt_claim_invoice invoice inner join audit_trail audit on audit.claim_id=invoice.claim_id and audit.new_status='PaymentReceived' where date(claim_created_date) between :pUploadDateFrom and :pUploadDateTo and invoice.insurer_id=insurer_chorganisation.insurer_id and invoice.chorganisation_id=insurer_chorganisation.chorganisation_id) as average_claim_cycle_day, ");
            sb.append("(select case when count(*) is null or count(*) = 0 then 0 else cast(round(sum(EXTRACT(DAY FROM (audit.update_date - invoice.created_date)))/count(*)) as bigint) end as no_count from rpt_claim_invoice invoice inner join audit_trail audit on audit.claim_id=invoice.claim_id and audit.new_status='PaymentReceived' where date(claim_created_date) between :pUploadDateFrom and :pUploadDateTo and invoice.insurer_id=insurer_chorganisation.insurer_id and invoice.chorganisation_id=insurer_chorganisation.chorganisation_id) as average_invoice_cycle_day, ");
            sb.append("(select case when count(*) is null or count(*) = 0 then 0 else cast(round(sum(vehicle_hire.days)/count(*)) as bigint) end as no_count from rpt_claim_invoice invoice left outer join vehicle_hire vehicle_hire on vehicle_hire.id = invoice.claim_vehicle_hire_id where date(invoice.claim_created_date) between :pUploadDateFrom and :pUploadDateTo and invoice.chorganisation_id=insurer_chorganisation.chorganisation_id and invoice.insurer_id=insurer_chorganisation.insurer_id) as average_hire_duration_day, ");
            sb.append("(select case when count(*) is null or count(*) = 0 then 0 else cast(sum(invoice.total_to_pay)/count(*) as numeric(20,2)) end as no_count from rpt_claim_invoice invoice where date(invoice.claim_created_date) between :pUploadDateFrom and :pUploadDateTo and invoice.chorganisation_id=insurer_chorganisation.chorganisation_id and invoice.insurer_id=insurer_chorganisation.insurer_id) as average_invoice_val, ");
            sb.append("(select case when count(*) is null or count(*) = 0 then 0 else cast(sum(invoice.total_penalty_charge)/count(*) as numeric(20,2)) end as no_count from rpt_claim_invoice invoice where date(invoice.claim_created_date) between :pUploadDateFrom and :pUploadDateTo and invoice.chorganisation_id=insurer_chorganisation.chorganisation_id and invoice.insurer_id=insurer_chorganisation.insurer_id) as average_penalty_val, ");
            sb.append("(select case when sum(original_full_total_to_pay - total_to_pay) is null then 0 else sum(original_full_total_to_pay - total_to_pay) end as no_count from rpt_claim_invoice where date(claim_created_date) between :pUploadDateFrom and :pUploadDateTo and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id and total_to_pay < original_full_total_to_pay) as amount_saved_val, ");
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice where date(claim_created_date) between :pUploadDateFrom and :pUploadDateTo and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id and repair_gross > 0.0) as total_no_creditrepair_invoice_num, ");
            sb.append("(select case when sum(total_to_pay) is null then 0 else sum(total_to_pay) end as no_count from rpt_claim_invoice where date(claim_created_date) between :pUploadDateFrom and :pUploadDateTo and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id and repair_gross > 0.0) as total_no_creditrepair_invoice_val, ");
//            sb.append("(select ) as total_no_creditrepair_invoice_per, ");
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice where date(claim_created_date) between :pUploadDateFrom and :pUploadDateTo and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id and repair_gross > 0.0 and exists (select * from audit_trail where claim_id=rpt_claim_invoice.claim_id and new_status='InvoicePaymentLogged')) as total_no_creditrepair_paid_invoice_num, ");
            sb.append("(select case when sum(total_to_pay) is null then 0 else sum(total_to_pay) end as no_count from rpt_claim_invoice where date(claim_created_date) between :pUploadDateFrom and :pUploadDateTo and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id and repair_gross > 0.0 and exists (select * from audit_trail where claim_id=rpt_claim_invoice.claim_id and new_status='InvoicePaymentLogged')) as total_no_creditrepair_paid_invoice_val, ");
//            sb.append("(select ) as total_no_creditrepair_paid_invoice_per, ");
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice r, vehicle_hire vh, vehicle_class vc where r.claim_vehicle_hire_id=vh.id and vh.vehicle_class_id=vc.id and vc.name in ('S1','S2','S3','S4','S5','S6','S7') and date(r.claim_created_date) between :pUploadDateFrom and :pUploadDateTo and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id) as total_s_class_invoice_num, ");
            sb.append("(select case when sum(total_to_pay) is null then 0 else sum(total_to_pay) end as no_count from rpt_claim_invoice r, vehicle_hire vh, vehicle_class vc where r.claim_vehicle_hire_id=vh.id and vh.vehicle_class_id=vc.id and vc.name in ('S1','S2','S3','S4','S5','S6','S7') and date(r.claim_created_date) between :pUploadDateFrom and :pUploadDateTo and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id) as total_s_class_invoice_val, ");
//            sb.append("(select ) as total_s_class_invoice_per, ");
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice r, vehicle_hire vh, vehicle_class vc where r.claim_vehicle_hire_id=vh.id and vh.vehicle_class_id=vc.id and vc.name in ('S1','S2','S3','S4','S5','S6','S7') and date(r.claim_created_date) between :pUploadDateFrom and :pUploadDateTo and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id and exists (select * from audit_trail where claim_id=rpt_claim_invoice.claim_id and new_status='InvoicePaymentLogged')) as total_s_class_paid_invoice_num, ");
            sb.append("(select case when sum(total_to_pay) is null then 0 else sum(total_to_pay) end as no_count from rpt_claim_invoice r, vehicle_hire vh, vehicle_class vc where r.claim_vehicle_hire_id=vh.id and vh.vehicle_class_id=vc.id and vc.name in ('S1','S2','S3','S4','S5','S6','S7') and date(r.claim_created_date) between :pUploadDateFrom and :pUploadDateTo and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id and exists (select * from audit_trail where claim_id=rpt_claim_invoice.claim_id and new_status='InvoicePaymentLogged')) as total_s_class_paid_invoice_val, ");
//            sb.append("(select ) as total_s_class_paid_invoice_per, ");
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice r, vehicle_hire vh, vehicle_class vc where r.claim_vehicle_hire_id=vh.id and vh.vehicle_class_id=vc.id and vc.name in ('P1','P2','P3','P4','P5','P6','P7','P8','P9','P10','P11','P12','P13') and date(r.claim_created_date) between :pUploadDateFrom and :pUploadDateTo and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id) as total_p_class_invoice_num, ");
            sb.append("(select case when sum(total_to_pay) is null then 0 else sum(total_to_pay) end as no_count from rpt_claim_invoice r, vehicle_hire vh, vehicle_class vc where r.claim_vehicle_hire_id=vh.id and vh.vehicle_class_id=vc.id and vc.name in ('P1','P2','P3','P4','P5','P6','P7','P8','P9','P10','P11','P12','P13') and date(r.claim_created_date) between :pUploadDateFrom and :pUploadDateTo and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id) as total_p_class_invoice_val, ");
//            sb.append("(select ) as total_p_class_invoice_per, ");
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice r, vehicle_hire vh, vehicle_class vc where r.claim_vehicle_hire_id=vh.id and vh.vehicle_class_id=vc.id and vc.name in ('P1','P2','P3','P4','P5','P6','P7','P8','P9','P10','P11','P12','P13') and date(r.claim_created_date) between :pUploadDateFrom and :pUploadDateTo and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id and exists (select * from audit_trail where claim_id=rpt_claim_invoice.claim_id and new_status='InvoicePaymentLogged')) as total_p_class_paid_invoice_num, ");
            sb.append("(select case when sum(total_to_pay) is null then 0 else sum(total_to_pay) end as no_count from rpt_claim_invoice r, vehicle_hire vh, vehicle_class vc where r.claim_vehicle_hire_id=vh.id and vh.vehicle_class_id=vc.id and vc.name in ('P1','P2','P3','P4','P5','P6','P7','P8','P9','P10','P11','P12','P13') and date(r.claim_created_date) between :pUploadDateFrom and :pUploadDateTo and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id and exists (select * from audit_trail where claim_id=rpt_claim_invoice.claim_id and new_status='InvoicePaymentLogged')) as total_p_class_paid_invoice_val, ");
//            sb.append("(select ) as total_p_class_paid_invoice_per, ");
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice r, vehicle_hire vh, vehicle_class vc where r.claim_vehicle_hire_id=vh.id and vh.vehicle_class_id=vc.id and vc.name in ('M','M1','M2','M3','M4','M5','M6') and date(r.claim_created_date) between :pUploadDateFrom and :pUploadDateTo and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id) as total_mv_class_invoice_num, ");
            sb.append("(select case when sum(total_to_pay) is null then 0 else sum(total_to_pay) end as no_count from rpt_claim_invoice r, vehicle_hire vh, vehicle_class vc where r.claim_vehicle_hire_id=vh.id and vh.vehicle_class_id=vc.id and vc.name in ('M','M1','M2','M3','M4','M5','M6') and date(r.claim_created_date) between :pUploadDateFrom and :pUploadDateTo and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id) as total_mv_class_invoice_val, ");
//            sb.append("(select ) as total_mv_class_invoice_per, ");
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice r, vehicle_hire vh, vehicle_class vc where r.claim_vehicle_hire_id=vh.id and vh.vehicle_class_id=vc.id and vc.name in ('M','M1','M2','M3','M4','M5','M6') and date(r.claim_created_date) between :pUploadDateFrom and :pUploadDateTo and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id and exists (select * from audit_trail where claim_id=rpt_claim_invoice.claim_id and new_status='InvoicePaymentLogged')) as total_mv_class_paid_invoice_num, ");
            sb.append("(select case when sum(total_to_pay) is null then 0 else sum(total_to_pay) end as no_count from rpt_claim_invoice r, vehicle_hire vh, vehicle_class vc where r.claim_vehicle_hire_id=vh.id and vh.vehicle_class_id=vc.id and vc.name in ('M','M1','M2','M3','M4','M5','M6') and date(r.claim_created_date) between :pUploadDateFrom and :pUploadDateTo and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id and exists (select * from audit_trail where claim_id=rpt_claim_invoice.claim_id and new_status='InvoicePaymentLogged')) as total_mv_class_paid_invoice_val, ");
//            sb.append("(select ) as total_mv_class_paid_invoice_per, ");
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice r, vehicle_hire vh, vehicle_class vc where r.claim_vehicle_hire_id=vh.id and vh.vehicle_class_id=vc.id and vc.name in ('F1','F2','F3','F4','F5','F6','F7','F8','F9') and date(r.claim_created_date) between :pUploadDateFrom and :pUploadDateTo and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id) as total_m_class_invoice_num, ");
            sb.append("(select case when sum(total_to_pay) is null then 0 else sum(total_to_pay) end as no_count from rpt_claim_invoice r, vehicle_hire vh, vehicle_class vc where r.claim_vehicle_hire_id=vh.id and vh.vehicle_class_id=vc.id and vc.name in ('F1','F2','F3','F4','F5','F6','F7','F8','F9') and date(r.claim_created_date) between :pUploadDateFrom and :pUploadDateTo and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id) as total_m_class_invoice_val, ");
//            sb.append("(select ) as total_m_class_invoice_per, ");
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice r, vehicle_hire vh, vehicle_class vc where r.claim_vehicle_hire_id=vh.id and vh.vehicle_class_id=vc.id and vc.name in ('F1','F2','F3','F4','F5','F6','F7','F8','F9') and date(r.claim_created_date) between :pUploadDateFrom and :pUploadDateTo and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id and exists (select * from audit_trail where claim_id=rpt_claim_invoice.claim_id and new_status='InvoicePaymentLogged')) as total_m_class_paid_invoice_num, ");
            sb.append("(select case when sum(total_to_pay) is null then 0 else sum(total_to_pay) end as no_count from rpt_claim_invoice r, vehicle_hire vh, vehicle_class vc where r.claim_vehicle_hire_id=vh.id and vh.vehicle_class_id=vc.id and vc.name in ('F1','F2','F3','F4','F5','F6','F7','F8','F9') and date(r.claim_created_date) between :pUploadDateFrom and :pUploadDateTo and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id and exists (select * from audit_trail where claim_id=rpt_claim_invoice.claim_id and new_status='InvoicePaymentLogged')) as total_m_class_paid_invoice_val, ");
//            sb.append("(select ) as total_m_class_paid_invoice_per, ");
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice r, vehicle_hire vh, vehicle_class vc where r.claim_vehicle_hire_id=vh.id and vh.vehicle_class_id=vc.id and vc.name in ('SP1','SP2','SP3','SP4','SP5','SP6','SP7','SP8','SP9','SP10','SP11','SP12','SP13') and date(r.claim_created_date) between :pUploadDateFrom and :pUploadDateTo and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id) as total_sp_class_invoice_num, ");
            sb.append("(select case when sum(total_to_pay) is null then 0 else sum(total_to_pay) end as no_count from rpt_claim_invoice r, vehicle_hire vh, vehicle_class vc where r.claim_vehicle_hire_id=vh.id and vh.vehicle_class_id=vc.id and vc.name in ('SP1','SP2','SP3','SP4','SP5','SP6','SP7','SP8','SP9','SP10','SP11','SP12','SP13') and date(r.claim_created_date) between :pUploadDateFrom and :pUploadDateTo and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id) as total_sp_class_invoice_val, ");
//            sb.append("(select ) as total_sp_class_invoice_per, ");
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice r, vehicle_hire vh, vehicle_class vc where r.claim_vehicle_hire_id=vh.id and vh.vehicle_class_id=vc.id and vc.name in ('SP1','SP2','SP3','SP4','SP5','SP6','SP7','SP8','SP9','SP10','SP11','SP12','SP13') and date(r.claim_created_date) between :pUploadDateFrom and :pUploadDateTo and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id and exists (select * from audit_trail where claim_id=rpt_claim_invoice.claim_id and new_status='InvoicePaymentLogged')) as total_sp_class_paid_invoice_num, ");
            sb.append("(select case when sum(total_to_pay) is null then 0 else sum(total_to_pay) end as no_count from rpt_claim_invoice r, vehicle_hire vh, vehicle_class vc where r.claim_vehicle_hire_id=vh.id and vh.vehicle_class_id=vc.id and vc.name in ('SP1','SP2','SP3','SP4','SP5','SP6','SP7','SP8','SP9','SP10','SP11','SP12','SP13') and date(r.claim_created_date) between :pUploadDateFrom and :pUploadDateTo and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id and exists (select * from audit_trail where claim_id=rpt_claim_invoice.claim_id and new_status='InvoicePaymentLogged')) as total_sp_class_paid_invoice_val, ");
//            sb.append("(select ) as total_sp_class_paid_invoice_per, ");
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice r, vehicle_hire vh, vehicle_class vc where r.claim_vehicle_hire_id=vh.id and vh.vehicle_class_id=vc.id and vc.name not in ('S1','S2','S3','S4','S5','S6','S7','P1','P2','P3','P4','P5','P6','P7','P8','P9','P10','P11','P12','P13','F1','F2','F3','F4','F5','F6','F7','F8','F9','M','M1','M2','M3','M4','M5','M6','SP1','SP2','SP3','SP4','SP5','SP6','SP7','SP8','SP9','SP10','SP11','SP12','SP13') and date(r.claim_created_date) between :pUploadDateFrom and :pUploadDateTo and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id) as total_other_class_invoice_num, ");
            sb.append("(select case when sum(total_to_pay) is null then 0 else sum(total_to_pay) end as no_count from rpt_claim_invoice r, vehicle_hire vh, vehicle_class vc where r.claim_vehicle_hire_id=vh.id and vh.vehicle_class_id=vc.id and vc.name not in ('S1','S2','S3','S4','S5','S6','S7','P1','P2','P3','P4','P5','P6','P7','P8','P9','P10','P11','P12','P13','F1','F2','F3','F4','F5','F6','F7','F8','F9','M','M1','M2','M3','M4','M5','M6','SP1','SP2','SP3','SP4','SP5','SP6','SP7','SP8','SP9','SP10','SP11','SP12','SP13') and date(r.claim_created_date) between :pUploadDateFrom and :pUploadDateTo and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id) as total_other_class_invoice_val, ");
//            sb.append("(select ) as total_other_class_invoice_per, ");
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice r, vehicle_hire vh, vehicle_class vc where r.claim_vehicle_hire_id=vh.id and vh.vehicle_class_id=vc.id and vc.name not in ('S1','S2','S3','S4','S5','S6','S7','P1','P2','P3','P4','P5','P6','P7','P8','P9','P10','P11','P12','P13','F1','F2','F3','F4','F5','F6','F7','F8','F9','M','M1','M2','M3','M4','M5','M6','SP1','SP2','SP3','SP4','SP5','SP6','SP7','SP8','SP9','SP10','SP11','SP12','SP13') and date(r.claim_created_date) between :pUploadDateFrom and :pUploadDateTo and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id and exists (select * from audit_trail where claim_id=rpt_claim_invoice.claim_id and new_status='InvoicePaymentLogged')) as total_other_class_paid_invoice_num, ");
            sb.append("(select case when sum(total_to_pay) is null then 0 else sum(total_to_pay) end as no_count from rpt_claim_invoice r, vehicle_hire vh, vehicle_class vc where r.claim_vehicle_hire_id=vh.id and vh.vehicle_class_id=vc.id and vc.name not in ('S1','S2','S3','S4','S5','S6','S7','P1','P2','P3','P4','P5','P6','P7','P8','P9','P10','P11','P12','P13','F1','F2','F3','F4','F5','F6','F7','F8','F9','M','M1','M2','M3','M4','M5','M6','SP1','SP2','SP3','SP4','SP5','SP6','SP7','SP8','SP9','SP10','SP11','SP12','SP13') and date(r.claim_created_date) between :pUploadDateFrom and :pUploadDateTo and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id and exists (select * from audit_trail where claim_id=rpt_claim_invoice.claim_id and new_status='InvoicePaymentLogged')) as total_other_class_paid_invoice_val, ");
//            sb.append("(select ) as total_other_class_paid_invoice_per ");

            if (currentUser.getInsurer()!=null) {

                sb.append("from insurer_chorganisation insurer_chorganisation, chorganisation chorganisation ");
                sb.append("where chorganisation.id=insurer_chorganisation.chorganisation_id  ");
                sb.append("and insurer_chorganisation.insurer_id=:pUserOrgId ");

            } else {

                sb.append("from insurer_chorganisation insurer_chorganisation, insurer insurer ");
                sb.append("where insurer.id=insurer_chorganisation.insurer_id ");
                sb.append("and insurer_chorganisation.chorganisation_id=:pUserOrgId ");

            }

            String query = sb.toString();
            LOG.debug(query);
            Map paramMap = new HashMap();
            paramMap.put("pUploadDateFrom", dataStart);
            paramMap.put("pUploadDateTo", dataEnd);
            paramMap.put("pUserOrgId", userOrgId);

            List result = baseDataService.externalQuery(query, paramMap);

            List<OverviewSummaryReportByOrg> overviewSummaryReportByOrgs = new ArrayList<OverviewSummaryReportByOrg>();

            List<String> orgName = new ArrayList<String>();
            orgName.add("All");

            for (Object o : result) {
                Map data = (Map) o;

                OverviewSummaryReportByOrg overviewSummaryReportByOrg = OverviewSummaryReportByOrg.getObject(data, (currentUser.getInsurer()!=null));
                overviewSummaryReportByOrgs.add(overviewSummaryReportByOrg);

                orgName.add(overviewSummaryReportByOrg.getOrgName());
            }

            List<OverviewSummaryLineItem> summaries = doOverviewSummaryLineItem(overviewSummaryReportByOrgs);

            OverviewSummaryReportObject reportObj = new OverviewSummaryReportObject();
            reportObj.setUploadDateFrom(dataStart);
            reportObj.setUploadDateTo(dataEnd);
            reportObj.setCreatedDate(new Date());

            reportParameters.put("reportObj", reportObj);
            reportParameters.put("orgName", orgName);
            reportParameters.put("userOrgLabel", userOrgLabel);
            reportParameters.put("userOrgName", userOrgName);
            reportParameters.put("OverviewSummaryLineItems", summaries);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return reportParameters;
    }

    private List<OverviewSummaryLineItem> doOverviewSummaryLineItem(List<OverviewSummaryReportByOrg> inputList) {

        List<OverviewSummaryLineItem> reportLines = getReportLineItems();

        for (OverviewSummaryLineItem reportLine : reportLines) {

            List<OverviewSummaryLineItemDetail> lineItemDetails = new ArrayList<OverviewSummaryLineItemDetail>();

            // CREATE
            OverviewSummaryLineItemDetail lineItemDetailAll = new OverviewSummaryLineItemDetail();
            lineItemDetails.add(lineItemDetailAll);

            Integer noCountClaimAll = 0;
            Integer noCountInvoiceAll = 0;
            Integer noCountAll = 0;
            Integer totalDayAll = 0;
            BigDecimal totalPercentageAll = new BigDecimal(0.00);
            BigDecimal totalValueAll = new BigDecimal(0.00);

            for (OverviewSummaryReportByOrg recordPerOrg : inputList) {

                OverviewSummaryLineItemDetail lineItemDetail = new OverviewSummaryLineItemDetail();

                Integer bTotalNoClaims = recordPerOrg.getTotal_no_claims_num();
                Integer bTotalNoInvoices = recordPerOrg.getTotal_no_invoice_num();

                noCountClaimAll = noCountClaimAll + bTotalNoClaims;
                noCountInvoiceAll = noCountInvoiceAll + bTotalNoInvoices;

                switch (reportLine.getLineId()) {
                    case 1:
                        lineItemDetail.setNoCount(recordPerOrg.getTotal_no_claims_num());
                        lineItemDetail.setTotalValue(recordPerOrg.getTotal_no_claims_val());
                        noCountAll = noCountAll + (Integer) lineItemDetail.getNoCount();
                        totalValueAll = totalValueAll.add((BigDecimal) lineItemDetail.getTotalValue());
                        break;
                    case 2:
                        lineItemDetail.setNoCount(recordPerOrg.getTotal_no_accepted_claims_num());
                        lineItemDetail.setTotalValue(recordPerOrg.getTotal_no_accepted_claims_val());
                        lineItemDetail.setTotalPercentage(MathHelper.getPercentageBigDecimal(recordPerOrg.getTotal_no_accepted_claims_num().floatValue(), bTotalNoClaims.floatValue(), 2));
                        noCountAll = noCountAll + (Integer) lineItemDetail.getNoCount();
                        totalValueAll = totalValueAll.add((BigDecimal) lineItemDetail.getTotalValue());
                        break;
                    case 3:
                        lineItemDetail.setNoCount(recordPerOrg.getTotal_no_rejected_claims_num());
                        lineItemDetail.setTotalPercentage(MathHelper.getPercentageBigDecimal(recordPerOrg.getTotal_no_rejected_claims_num().floatValue(), bTotalNoClaims.floatValue(), 2));
                        noCountAll = noCountAll + (Integer) lineItemDetail.getNoCount();
                        break;
                    case 4:
                        lineItemDetail.setNoCount(recordPerOrg.getTotal_no_invoice_num());
                        lineItemDetail.setTotalValue(recordPerOrg.getTotal_no_invoice_val());
                        noCountAll = noCountAll + (Integer) lineItemDetail.getNoCount();
                        totalValueAll = totalValueAll.add((BigDecimal) lineItemDetail.getTotalValue());
                        break;
                    case 5:
                        lineItemDetail.setNoCount(recordPerOrg.getTotal_no_approved_invoice_num());
                        lineItemDetail.setTotalValue(recordPerOrg.getTotal_no_approved_invoice_val());
                        lineItemDetail.setTotalPercentage(MathHelper.getPercentageBigDecimal(recordPerOrg.getTotal_no_approved_invoice_num().floatValue(), bTotalNoInvoices.floatValue(), 2));
                        noCountAll = noCountAll + (Integer) lineItemDetail.getNoCount();
                        totalValueAll = totalValueAll.add((BigDecimal) lineItemDetail.getTotalValue());
                        break;
                    case 6:
                        lineItemDetail.setNoCount(recordPerOrg.getTotal_no_rejected_invoice_num());
                        lineItemDetail.setTotalValue(recordPerOrg.getTotal_no_rejected_invoice_val());
                        lineItemDetail.setTotalPercentage(MathHelper.getPercentageBigDecimal(recordPerOrg.getTotal_no_rejected_invoice_num().floatValue(), bTotalNoInvoices.floatValue(), 2));
                        noCountAll = noCountAll + (Integer) lineItemDetail.getNoCount();
                        totalValueAll = totalValueAll.add((BigDecimal) lineItemDetail.getTotalValue());
                        break;
                    case 7:
                        lineItemDetail.setTotalDay(recordPerOrg.getAverage_claim_cycle_day());
                        totalDayAll = totalDayAll + (Integer) lineItemDetail.getTotalDay();
                        break;
                    case 8:
                        lineItemDetail.setTotalDay(recordPerOrg.getAverage_invoice_cycle_day());
                        totalDayAll = totalDayAll + (Integer) lineItemDetail.getTotalDay();
                        break;
                    case 9:
                        lineItemDetail.setTotalDay(recordPerOrg.getAverage_hire_duration_day());
                        totalDayAll = totalDayAll + (Integer) lineItemDetail.getTotalDay();
                        break;
                    case 10:
                        lineItemDetail.setTotalValue(recordPerOrg.getAverage_invoice_val());
                        totalValueAll = totalValueAll.add((BigDecimal) lineItemDetail.getTotalValue());
                        break;
                    case 11:
                        lineItemDetail.setTotalValue(recordPerOrg.getAverage_penalty_val());
                        totalValueAll = totalValueAll.add((BigDecimal) lineItemDetail.getTotalValue());
                        break;
                    case 12: // Amount Saved
                        lineItemDetail.setTotalValue(recordPerOrg.getAmount_saved_val());
                        break;
                    case 13: // Credit Repair
                        lineItemDetail.setNoCount(recordPerOrg.getCredit_repair_no_invoice_num());
                        lineItemDetail.setTotalValue(recordPerOrg.getCredit_repair_no_invoice_val());
                        lineItemDetail.setTotalPercentage(MathHelper.getPercentageBigDecimal(recordPerOrg.getCredit_repair_no_invoice_num().floatValue(), bTotalNoInvoices.floatValue(), 2));
                        break;
                    case 14: // Credit Repair Paid
                        lineItemDetail.setNoCount(recordPerOrg.getCredit_repair_no_paid_invoice_num());
                        lineItemDetail.setTotalValue(recordPerOrg.getCredit_repair_no_paid_invoice_val());
                        lineItemDetail.setTotalPercentage(MathHelper.getPercentageBigDecimal(recordPerOrg.getCredit_repair_no_paid_invoice_num().floatValue(), bTotalNoInvoices.floatValue(), 2));
                        break;
                    case 15: // S-Class
                        lineItemDetail.setNoCount(recordPerOrg.getS_class_no_invoice_num());
                        lineItemDetail.setTotalValue(recordPerOrg.getS_class_no_invoice_val());
                        lineItemDetail.setTotalPercentage(MathHelper.getPercentageBigDecimal(recordPerOrg.getS_class_no_invoice_num().floatValue(), bTotalNoInvoices.floatValue(), 2));
                        break;
                    case 16: // S-Class Paid
                        lineItemDetail.setNoCount(recordPerOrg.getS_class_no_paid_invoice_num());
                        lineItemDetail.setTotalValue(recordPerOrg.getS_class_no_paid_invoice_val());
                        lineItemDetail.setTotalPercentage(MathHelper.getPercentageBigDecimal(recordPerOrg.getS_class_no_paid_invoice_num().floatValue(), bTotalNoInvoices.floatValue(), 2));
                        break;
                    case 17: // P-Class
                        lineItemDetail.setNoCount(recordPerOrg.getP_class_no_invoice_num());
                        lineItemDetail.setTotalValue(recordPerOrg.getP_class_no_invoice_val());
                        lineItemDetail.setTotalPercentage(MathHelper.getPercentageBigDecimal(recordPerOrg.getP_class_no_invoice_num().floatValue(), bTotalNoInvoices.floatValue(), 2));
                        break;
                    case 18: // P-Class Paid
                        lineItemDetail.setNoCount(recordPerOrg.getP_class_no_paid_invoice_num());
                        lineItemDetail.setTotalValue(recordPerOrg.getP_class_no_paid_invoice_val());
                        lineItemDetail.setTotalPercentage(MathHelper.getPercentageBigDecimal(recordPerOrg.getP_class_no_paid_invoice_num().floatValue(), bTotalNoInvoices.floatValue(), 2));
                        break;
                    case 19: // MV-Class
                        lineItemDetail.setNoCount(recordPerOrg.getMv_class_no_invoice_num());
                        lineItemDetail.setTotalValue(recordPerOrg.getMv_class_no_invoice_val());
                        lineItemDetail.setTotalPercentage(MathHelper.getPercentageBigDecimal(recordPerOrg.getMv_class_no_invoice_num().floatValue(), bTotalNoInvoices.floatValue(), 2));
                        break;
                    case 20: // MV-Class Paid
                        lineItemDetail.setNoCount(recordPerOrg.getMv_class_no_paid_invoice_num());
                        lineItemDetail.setTotalValue(recordPerOrg.getMv_class_no_paid_invoice_val());
                        lineItemDetail.setTotalPercentage(MathHelper.getPercentageBigDecimal(recordPerOrg.getMv_class_no_paid_invoice_num().floatValue(), bTotalNoInvoices.floatValue(), 2));
                        break;
                    case 21: // M-Class
                        lineItemDetail.setNoCount(recordPerOrg.getM_class_no_invoice_num());
                        lineItemDetail.setTotalValue(recordPerOrg.getM_class_no_invoice_val());
                        lineItemDetail.setTotalPercentage(MathHelper.getPercentageBigDecimal(recordPerOrg.getM_class_no_invoice_num().floatValue(), bTotalNoInvoices.floatValue(), 2));
                        break;
                    case 22: // M-Class Paid
                        lineItemDetail.setNoCount(recordPerOrg.getM_class_no_paid_invoice_num());
                        lineItemDetail.setTotalValue(recordPerOrg.getM_class_no_paid_invoice_val());
                        lineItemDetail.setTotalPercentage(MathHelper.getPercentageBigDecimal(recordPerOrg.getM_class_no_paid_invoice_num().floatValue(), bTotalNoInvoices.floatValue(), 2));
                        break;
                    case 23: // SP-Class
                        lineItemDetail.setNoCount(recordPerOrg.getSp_class_no_invoice_num());
                        lineItemDetail.setTotalValue(recordPerOrg.getSp_class_no_invoice_val());
                        lineItemDetail.setTotalPercentage(MathHelper.getPercentageBigDecimal(recordPerOrg.getSp_class_no_invoice_num().floatValue(), bTotalNoInvoices.floatValue(), 2));
                        break;
                    case 24: // SP-Class Paid
                        lineItemDetail.setNoCount(recordPerOrg.getSp_class_no_paid_invoice_num());
                        lineItemDetail.setTotalValue(recordPerOrg.getSp_class_no_paid_invoice_val());
                        lineItemDetail.setTotalPercentage(MathHelper.getPercentageBigDecimal(recordPerOrg.getSp_class_no_paid_invoice_num().floatValue(), bTotalNoInvoices.floatValue(), 2));
                        break;
                    case 25: // Other-Class
                        lineItemDetail.setNoCount(recordPerOrg.getOther_class_no_invoice_num());
                        lineItemDetail.setTotalValue(recordPerOrg.getOther_class_no_invoice_val());
                        lineItemDetail.setTotalPercentage(MathHelper.getPercentageBigDecimal(recordPerOrg.getOther_class_no_invoice_num().floatValue(), bTotalNoInvoices.floatValue(), 2));
                        break;
                    case 26: // Other-Class Paid
                        lineItemDetail.setNoCount(recordPerOrg.getOther_class_no_paid_invoice_num());
                        lineItemDetail.setTotalValue(recordPerOrg.getOther_class_no_paid_invoice_val());
                        lineItemDetail.setTotalPercentage(MathHelper.getPercentageBigDecimal(recordPerOrg.getOther_class_no_paid_invoice_num().floatValue(), bTotalNoInvoices.floatValue(), 2));
                        break;
                    default:
                        break;
                }

                lineItemDetails.add(lineItemDetail);

            }

            lineItemDetails = processAllOrganisationDetailPerLines(
                    lineItemDetails,
                    reportLine.getLineId(),
                    noCountAll,
                    totalDayAll,
                    totalPercentageAll,
                    totalValueAll,
                    noCountClaimAll,
                    noCountInvoiceAll);

            reportLine.setLineItem(lineItemDetails);
        }

        return processTotalAverageSection(reportLines);

    }

    private List<OverviewSummaryLineItem> processTotalAverageSection(List<OverviewSummaryLineItem> reportLines) {

        String sqlStatement1 = "";
        StringBuffer sb = new StringBuffer();

        sb.append("select ");
        sb.append("(select case when count(*) is null or count(*) = 0 then 0 else cast(round(sum(EXTRACT(DAY FROM (audit.update_date - invoice.claim_created_date)))/count(*)) as bigint) end as no_count from rpt_claim_invoice invoice inner join audit_trail audit on audit.claim_id=invoice.claim_id and audit.new_status='PaymentReceived' where date(claim_created_date) between :pUploadDateFrom and :pUploadDateTo and @sqlStatement1) as averageClaimCycleForAllOrg, ");
        sb.append("(select case when count(*) is null or count(*) = 0 then 0 else cast(round(sum(EXTRACT(DAY FROM (audit.update_date - invoice.created_date)))/count(*)) as bigint) end as no_count from rpt_claim_invoice invoice inner join audit_trail audit on audit.claim_id=invoice.claim_id and audit.new_status='PaymentReceived' where date(claim_created_date) between :pUploadDateFrom and :pUploadDateTo and @sqlStatement1) as averageInvoiceCycleForAllOrg, ");
        sb.append("(select case when count(*) is null or count(*) = 0 then 0 else cast(round(sum(vehicle_hire.days)/count(*)) as bigint) end as no_count from rpt_claim_invoice invoice left outer join vehicle_hire vehicle_hire on vehicle_hire.id = invoice.claim_vehicle_hire_id where date(invoice.claim_created_date) between :pUploadDateFrom and :pUploadDateTo and @sqlStatement1) as averageHireDurationForAllOrg, ");
        sb.append("(select case when count(*) is null or count(*) = 0 then 0 else cast(sum(invoice.total_to_pay)/count(*) as numeric(20,2)) end as no_count from rpt_claim_invoice invoice where date(claim_created_date) between :pUploadDateFrom and :pUploadDateTo and @sqlStatement1) as averageInvoiceValueForAllOrg, ");
        sb.append("(select case when count(*) is null or count(*) = 0 then 0 else cast(sum(invoice.total_penalty_charge)/count(*) as numeric(20,2)) end as no_count from rpt_claim_invoice invoice where date(claim_created_date) between :pUploadDateFrom and :pUploadDateTo and @sqlStatement1) as averagePenaltyValueForAllOrg ");

        if (currentUser.getInsurer()!=null) {
            sb.append("from insurer insurer where insurer.id=:pUserOrgId ");
            sqlStatement1 = "invoice.insurer_id=insurer.id";
        } else {
            sb.append("from chorganisation chorganisation where chorganisation.id=:pUserOrgId ");
            sqlStatement1 = "invoice.chorganisation_id=chorganisation.id";
        }

        String query = sb.toString();
        query = query.replaceAll("@sqlStatement1", sqlStatement1);

        Map paramMap = new HashMap();
        paramMap.put("pUploadDateFrom", dataStart);
        paramMap.put("pUploadDateTo", dataEnd);
        paramMap.put("pUserOrgId", userOrgId);

        List result = baseDataService.externalQuery(query, paramMap);

        for (Object o : result) {
            Map data = (Map) o;

            reportLines.get(getLineItemIndex(7, reportLines)).getLineItem().get(0).setTotalDay(ReportHelper.getIntegerValue(data.get("averageClaimCycleForAllOrg".toLowerCase())));
            reportLines.get(getLineItemIndex(8, reportLines)).getLineItem().get(0).setTotalDay(ReportHelper.getIntegerValue(data.get("averageInvoiceCycleForAllOrg".toLowerCase())));
            reportLines.get(getLineItemIndex(9, reportLines)).getLineItem().get(0).setTotalDay(ReportHelper.getIntegerValue(data.get("averageHireDurationForAllOrg".toLowerCase())));
            reportLines.get(getLineItemIndex(10, reportLines)).getLineItem().get(0).setTotalValue(ReportHelper.getBigDecimalValue(data.get("averageInvoiceValueForAllOrg".toLowerCase())));
            reportLines.get(getLineItemIndex(11, reportLines)).getLineItem().get(0).setTotalValue(ReportHelper.getBigDecimalValue(data.get("averagePenaltyValueForAllOrg".toLowerCase())));
        }

        return reportLines;
    }

    private Integer getLineItemIndex(Integer lineItemId, List<OverviewSummaryLineItem> reportLines) {

        Integer iIndex = 0;

        int iCount = 0;
        for (OverviewSummaryLineItem item : reportLines) {
            if (item.getLineId() == lineItemId) {
                iIndex = iCount;
                break;
            }
            iCount++;
        }

        return iIndex;

    }

    private List<OverviewSummaryLineItemDetail> processAllOrganisationDetailPerLines(
            List<OverviewSummaryLineItemDetail> lineItemDetails,
            Integer reportLineId,
            Integer noCount,
            Integer totalDay,
            BigDecimal totalPercentage,
            BigDecimal totalValue,
            Integer noCountClaimAll,
            Integer noCountInvoiceAll) {

        switch (reportLineId) {
            case 1:
                lineItemDetails.get(0).setNoCount(noCount);
                lineItemDetails.get(0).setTotalValue(totalValue);
                break;
            case 2:
                lineItemDetails.get(0).setNoCount(noCount);
                lineItemDetails.get(0).setTotalValue(totalValue);
                lineItemDetails.get(0).setTotalPercentage(MathHelper.getPercentageBigDecimal(noCount, noCountClaimAll, 2));
                break;
            case 3:
                lineItemDetails.get(0).setNoCount(noCount);
                lineItemDetails.get(0).setTotalPercentage(MathHelper.getPercentageBigDecimal(noCount, noCountClaimAll, 2));
                break;
            case 4:
                lineItemDetails.get(0).setNoCount(noCount);
                lineItemDetails.get(0).setTotalValue(totalValue);
                break;
            case 5:
                lineItemDetails.get(0).setNoCount(noCount);
                lineItemDetails.get(0).setTotalValue(totalValue);
                lineItemDetails.get(0).setTotalPercentage(MathHelper.getPercentageBigDecimal(noCount, noCountInvoiceAll, 2));
                break;
            case 6:
                lineItemDetails.get(0).setNoCount(noCount);
                lineItemDetails.get(0).setTotalValue(totalValue);
                lineItemDetails.get(0).setTotalPercentage(MathHelper.getPercentageBigDecimal(noCount, noCountInvoiceAll, 2));
                break;
            case 12:
                lineItemDetails.get(0).setNoCount(noCount);
                lineItemDetails.get(0).setTotalValue(totalValue);
                lineItemDetails.get(0).setTotalPercentage(MathHelper.getPercentageBigDecimal(noCount, noCountInvoiceAll, 2));
                break;
            case 13:
                lineItemDetails.get(0).setNoCount(noCount);
                lineItemDetails.get(0).setTotalValue(totalValue);
                lineItemDetails.get(0).setTotalPercentage(MathHelper.getPercentageBigDecimal(noCount, noCountInvoiceAll, 2));
                break;
            case 14:
                lineItemDetails.get(0).setNoCount(noCount);
                lineItemDetails.get(0).setTotalValue(totalValue);
                lineItemDetails.get(0).setTotalPercentage(MathHelper.getPercentageBigDecimal(noCount, noCountInvoiceAll, 2));
                break;
            case 15:
                lineItemDetails.get(0).setNoCount(noCount);
                lineItemDetails.get(0).setTotalValue(totalValue);
                lineItemDetails.get(0).setTotalPercentage(MathHelper.getPercentageBigDecimal(noCount, noCountInvoiceAll, 2));
                break;
            case 16:
                lineItemDetails.get(0).setNoCount(noCount);
                lineItemDetails.get(0).setTotalValue(totalValue);
                lineItemDetails.get(0).setTotalPercentage(MathHelper.getPercentageBigDecimal(noCount, noCountInvoiceAll, 2));
                break;
            case 17:
                lineItemDetails.get(0).setNoCount(noCount);
                lineItemDetails.get(0).setTotalValue(totalValue);
                lineItemDetails.get(0).setTotalPercentage(MathHelper.getPercentageBigDecimal(noCount, noCountInvoiceAll, 2));
                break;
            case 18:
                lineItemDetails.get(0).setNoCount(noCount);
                lineItemDetails.get(0).setTotalValue(totalValue);
                lineItemDetails.get(0).setTotalPercentage(MathHelper.getPercentageBigDecimal(noCount, noCountInvoiceAll, 2));
                break;
            case 19:
                lineItemDetails.get(0).setNoCount(noCount);
                lineItemDetails.get(0).setTotalValue(totalValue);
                lineItemDetails.get(0).setTotalPercentage(MathHelper.getPercentageBigDecimal(noCount, noCountInvoiceAll, 2));
                break;
            case 20:
                lineItemDetails.get(0).setNoCount(noCount);
                lineItemDetails.get(0).setTotalValue(totalValue);
                lineItemDetails.get(0).setTotalPercentage(MathHelper.getPercentageBigDecimal(noCount, noCountInvoiceAll, 2));
                break;
            case 21:
                lineItemDetails.get(0).setNoCount(noCount);
                lineItemDetails.get(0).setTotalValue(totalValue);
                lineItemDetails.get(0).setTotalPercentage(MathHelper.getPercentageBigDecimal(noCount, noCountInvoiceAll, 2));
                break;
            case 22:
                lineItemDetails.get(0).setNoCount(noCount);
                lineItemDetails.get(0).setTotalValue(totalValue);
                lineItemDetails.get(0).setTotalPercentage(MathHelper.getPercentageBigDecimal(noCount, noCountInvoiceAll, 2));
                break;
            case 23:
                lineItemDetails.get(0).setNoCount(noCount);
                lineItemDetails.get(0).setTotalValue(totalValue);
                lineItemDetails.get(0).setTotalPercentage(MathHelper.getPercentageBigDecimal(noCount, noCountInvoiceAll, 2));
                break;
            case 24:
                lineItemDetails.get(0).setNoCount(noCount);
                lineItemDetails.get(0).setTotalValue(totalValue);
                lineItemDetails.get(0).setTotalPercentage(MathHelper.getPercentageBigDecimal(noCount, noCountInvoiceAll, 2));
                break;
            case 25:
                lineItemDetails.get(0).setNoCount(noCount);
                lineItemDetails.get(0).setTotalValue(totalValue);
                lineItemDetails.get(0).setTotalPercentage(MathHelper.getPercentageBigDecimal(noCount, noCountInvoiceAll, 2));
                break;
            case 26:
                lineItemDetails.get(0).setNoCount(noCount);
                lineItemDetails.get(0).setTotalValue(totalValue);
                lineItemDetails.get(0).setTotalPercentage(MathHelper.getPercentageBigDecimal(noCount, noCountInvoiceAll, 2));
                break;
            default:
                break;
        }

        return lineItemDetails;
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
    public void setDataService(BaseDataService baseDataService) {
        this.baseDataService = baseDataService;
    }

    @Override
    public void setExternalParameter(Map parameters) {
        this.externalParameter = parameters;
    }

    @Override
    public String getReportTemplateFileName() {
        return "template_SummaryReport.xls";
    }

    public String getReportCode() {
        return "RPT001";
    }

    public List<OverviewSummaryLineItem> getReportLineItems() {

        List<OverviewSummaryLineItem> summaries = new ArrayList<OverviewSummaryLineItem>();

        OverviewSummaryLineItem lineItem = new OverviewSummaryLineItem();
        lineItem.setLineId(1);
        lineItem.setName("Total No. Claims");
        summaries.add(lineItem);

        lineItem = new OverviewSummaryLineItem();
        lineItem.setLineId(2);
        lineItem.setName("Total No. Accepted Claims");
        summaries.add(lineItem);

        lineItem = new OverviewSummaryLineItem();
        lineItem.setLineId(3);
        lineItem.setName("Total No. Rejected Claims");
        summaries.add(lineItem);

        lineItem = new OverviewSummaryLineItem();
        lineItem.setLineId(4);
        lineItem.setName("Total No. Invoices");
        summaries.add(lineItem);

        lineItem = new OverviewSummaryLineItem();
        lineItem.setLineId(5);
        lineItem.setName("Total No. Paid Invoices");
        summaries.add(lineItem);

        lineItem = new OverviewSummaryLineItem();
        lineItem.setLineId(6);
        lineItem.setName("Total No. Rejected Invoices");
        summaries.add(lineItem);

        lineItem = new OverviewSummaryLineItem();
        lineItem.setLineId(7);
        lineItem.setName("Average Claim Cycle Time");
        summaries.add(lineItem);

        lineItem = new OverviewSummaryLineItem();
        lineItem.setLineId(8);
        lineItem.setName("Average Invoice Cycle Time");
        summaries.add(lineItem);

        lineItem = new OverviewSummaryLineItem();
        lineItem.setLineId(9);
        lineItem.setName("Average Hire Duration");
        summaries.add(lineItem);

        lineItem = new OverviewSummaryLineItem();
        lineItem.setLineId(10);
        lineItem.setName("Average Invoice Value");
        summaries.add(lineItem);

        lineItem = new OverviewSummaryLineItem();
        lineItem.setLineId(11);
        lineItem.setName("Average Penalty Charge");
        summaries.add(lineItem);

        lineItem = new OverviewSummaryLineItem();
        lineItem.setLineId(12);
        lineItem.setName("Amount Saved (Paid Invoices)");
        summaries.add(lineItem);

        lineItem = new OverviewSummaryLineItem();
        lineItem.setLineId(13);
        lineItem.setName("Credit Repair (£ = Repair Costs) - No. Invoices");
        summaries.add(lineItem);

        lineItem = new OverviewSummaryLineItem();
        lineItem.setLineId(14);
        lineItem.setName("Credit Repair (£ = Repair Costs) - No. Paid Invoices");
        summaries.add(lineItem);

        lineItem = new OverviewSummaryLineItem();
        lineItem.setLineId(15);
        lineItem.setName("S Class Claims - No. Invoices");
        summaries.add(lineItem);

        lineItem = new OverviewSummaryLineItem();
        lineItem.setLineId(16);
        lineItem.setName("S Class Claims - No. Paid Invoices");
        summaries.add(lineItem);

        lineItem = new OverviewSummaryLineItem();
        lineItem.setLineId(17);
        lineItem.setName("Prestige Vehicle Claims - No. Invoices");
        summaries.add(lineItem);

        lineItem = new OverviewSummaryLineItem();
        lineItem.setLineId(18);
        lineItem.setName("Prestige Vehicle Claims - No. Paid Invoices");
        summaries.add(lineItem);

        lineItem = new OverviewSummaryLineItem();
        lineItem.setLineId(19);
        lineItem.setName("MPV Claims - No. Invoices");
        summaries.add(lineItem);

        lineItem = new OverviewSummaryLineItem();
        lineItem.setLineId(20);
        lineItem.setName("MPV Claims - No. Paid Invoices");
        summaries.add(lineItem);

        lineItem = new OverviewSummaryLineItem();
        lineItem.setLineId(21);
        lineItem.setName("4 by 4 Claims - No. Invoices");
        summaries.add(lineItem);

        lineItem = new OverviewSummaryLineItem();
        lineItem.setLineId(22);
        lineItem.setName("4 by 4 Claims - No. Paid Invoices");
        summaries.add(lineItem);

        lineItem = new OverviewSummaryLineItem();
        lineItem.setLineId(23);
        lineItem.setName("Sports Car Claims - No. Invoices");
        summaries.add(lineItem);

        lineItem = new OverviewSummaryLineItem();
        lineItem.setLineId(24);
        lineItem.setName("Sports Car Claims - No. Paid Invoices");
        summaries.add(lineItem);

        lineItem = new OverviewSummaryLineItem();
        lineItem.setLineId(25);
        lineItem.setName("All Other Claims - No. Invoices");
        summaries.add(lineItem);

        lineItem = new OverviewSummaryLineItem();
        lineItem.setLineId(26);
        lineItem.setName("All Other Claims - No. Paid Invoices");
        summaries.add(lineItem);

        return summaries;
    }

}
