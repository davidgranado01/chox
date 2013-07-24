package idas.chox.service.reports;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
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
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.ReasonOfRejection;
import idas.chox.core.model.WebUser;
import idas.chox.core.services.ReportDataService;
import idas.chox.core.util.DateHelper;
import idas.chox.core.util.MathHelper;
import idas.chox.core.util.TextHelper;
import idas.chox.data.services.BaseDataService;
import idas.chox.service.reports.viewdata.BreInvoiceApprovalDisputeCumulativeData;
import idas.chox.service.reports.viewdata.BreInvoiceApprovalDisputedData;
import idas.chox.service.reports.viewdata.BreInvoiceApprovalDisputedRoRData;

/**
 *
 * @author rajareddydodda
 */
public class BreInvoiceApprovalDisputeReport implements Report {

    private static final Logger LOG = LoggerFactory.getLogger(BreInvoiceApprovalDisputeReport.class);
    private Map externalParameter;
    private List<String> reportParameterNames;
    private BaseDataService baseDataService;
    private ReportDataService reportDataService;

    @Override
    public void setBaseDataService(BaseDataService baseDataService) {
        this.baseDataService = baseDataService;
    }
    
    public BreInvoiceApprovalDisputeReport() {
        reportParameterNames = new ArrayList<String>();
    }

    @Override
    public void setExternalParameter(Map parameters) {
        this.externalParameter = parameters;
    }

    @Override
    public void setReportDataService(ReportDataService reportDataService) {
        this.reportDataService = reportDataService;
    }
    
    private Chorganisation getChorganisation(int orgId) {

        Chorganisation chorg = new Chorganisation();

        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Chorganisation.class);
            criteria.add(Restrictions.eq("id", orgId));
            chorg = (Chorganisation) baseDataService.getByCriteria(criteria);

        } catch (Exception e) {
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

        } catch (Exception e) {
            LOG.error("Error generating getInsurer: {}", e.getMessage());

        }

        return ins;
    }

    
    @Override
    public Map<String, Object> getReportParameters() throws Exception {
        Map<String, Object> reportParameters = new HashMap<String, Object>();

        reportParameters.put("date", new Date());

        LOG.debug("inside getReportParameters()");

        Date dataStart = null;

        String supplierId;
        String insrId;

        Integer choId = -1;
        Integer insurerId = -1;
        String selectedOrgLabel;
        String reportColumnHeader;
        String userOrgLabel;
        String selectedOrgName = "All";
        String userOrgName;
        Date createDate = new Date();

        WebUser currentUser = ((WebUser) externalParameter.get("CurrentUser"));
        Chorganisation chorg ;

        userOrgName = currentUser.getOrganisationName();
        if (((String[]) externalParameter.get("DateStart")) != null) {
            dataStart = DateHelper.parse(((String[]) externalParameter.get("DateStart"))[0]);
            LOG.debug("dataStart : {}", dataStart);
        } else {
            throw new Exception("Start date cannot be empty.");
        }


        if (currentUser.getInsurer() != null) {

            Insurer ins = currentUser.getInsurer();
            insurerId = ins.getId();

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

            LOG.debug("insurerId : {}", insurerId);
            LOG.debug("choId : {}", choId);
            LOG.debug("selectedOrgName : {}", selectedOrgName);

        } else {

            chorg = currentUser.getChorganisation();
            choId = chorg.getId();

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

            LOG.debug("choId : {}", choId);
            LOG.debug("insurerId : {}" , insurerId);
            LOG.debug("selectedOrgName : {}", selectedOrgName);

        }
        
        List<ReasonOfRejection> reasonsOfRejection = getReasonsOfRejection(currentUser, false);
        List<BreInvoiceApprovalDisputedRoRData> breInvRorData = new ArrayList<BreInvoiceApprovalDisputedRoRData>();
        List<BreInvoiceApprovalDisputedData> breInvoiceApproval = new ArrayList<BreInvoiceApprovalDisputedData>();

        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append("(select TEXT(\'Last 12 Months\'))as month_header, ");
        sb.append("(select count(*) from claim c, invoice i where c.invoice_id = i.id and c.claim_type not in ")
                .append(ClaimType.getInsurerUploadTypeOrdinals())
                .append(" and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
                .append("and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
                .append("and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
                .append("and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as invoice_uploaded_total, ");

        sb.append("(select count(*) from claim c, invoice i, audit_trail a ")
                .append("where c.invoice_id = i.id ")
                .append("and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
                .append("and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
                .append("and a.reverted=false and a.claim_id = c.id ")
                .append("and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect', 'InvoiceUnassigned') ")
                .append("and a.new_status='InvoiceApprovedByBRE' ")
                .append("and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months' ")
                .append("and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\'))) as  invoice_approved_by_bre_total, ");
        sb.append("(select count(distinct b.id) from ( select c.id, c.invoice_id from claim c, invoice i, audit_trail a ")
                .append("where c.invoice_id = i.id ")
                .append("and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
                .append("and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
                .append("and a.reverted=false and a.claim_id = c.id ")
                .append("and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect', 'InvoiceUnassigned') ")
                .append("and a.new_status='InvoiceApprovedByBRE' ")
                .append("and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months' ")
                .append("and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\'))) b, audit_trail a ")
                .append("where b.id = a.claim_id and a.reverted=false and a.new_status = 'ContestedInvoiceReferredToCHO' ) as  invoice_approved_by_bre_disputed_total, ");

        sb.append("(select count(*) from (select c.id, c.invoice_id, i.created_date from claim c, invoice i, audit_trail a ")
                .append("where c.invoice_id = i.id ")
                .append("and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
                .append("and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
                .append("and a.reverted=false and a.claim_id = c.id ")
                .append("and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect', 'InvoiceUnassigned') ")
                .append("and a.new_status='InvoiceApprovedByBRE' ")
                .append("and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months' ")
                .append("and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\'))) b , audit_trail a1 ")
                .append("where b.id = a1.claim_id ")
                .append("and a1.reverted=false and a1.new_status ='InvoicePaymentLogged' ")
                .append("and not exists ( select * from audit_trail a2 where a2.reverted=false and a2.claim_id =b.id and a2.new_status='ContestedInvoiceReferredToCHO') ")
                .append("and a1.update_date between b.created_date  and  b.created_date + interval '15 days' )as invoice_approved_by_bre_not_disputed_paid_within_15days_total, ");

        sb.append("(select count(*) from (select c.id, c.invoice_id, i.created_date from claim c, invoice i, audit_trail a ")
                .append("where c.invoice_id = i.id ")
                .append("and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
                .append("and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
                .append("and a.reverted=false and a.claim_id = c.id ")
                .append("and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect', 'InvoiceUnassigned') ")
                .append("and a.new_status='InvoiceApprovedByBRE' ")
                .append("and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months' ")
                .append("and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\'))) b , audit_trail a1 ")
                .append("where b.id = a1.claim_id ")
                .append("and a1.reverted=false and a1.new_status ='InvoicePaymentLogged' ")
                .append("and not exists ( select * from audit_trail a2 where a2.reverted=false and a2.claim_id = b.id and a2.new_status='ContestedInvoiceReferredToCHO') ")
                .append("and a1.update_date between b.created_date  and  b.created_date + interval '30 days' )as invoice_approved_by_bre_not_disputed_paid_within_30days_total, ");

        sb.append("(select count(*) from (select c.id, c.invoice_id, i.created_date from claim c, invoice i, audit_trail a ")
                .append("where c.invoice_id = i.id ")
                .append("and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
                .append("and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
                .append("and a.reverted=false and a.claim_id = c.id ")
                .append("and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect', 'InvoiceUnassigned') ")
                .append("and a.new_status='InvoiceApprovedByBRE' ")
                .append("and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months' ")
                .append("and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\'))) b , audit_trail a1 ")
                .append("where b.id = a1.claim_id ")
                .append("and a1.reverted=false and a1.new_status ='InvoicePaymentLogged' ")
                .append("and exists ( select * from audit_trail a2 where a2.reverted=false and a2.claim_id = b.id and a2.new_status='ContestedInvoiceReferredToCHO') ")
                .append("and a1.update_date    between b.created_date  and  b.created_date + interval '15 days' )as invoice_approved_by_bre_disputed_paid_within_15days_total, ");
        
        sb.append("(select count(*) from (select c.id, c.invoice_id, i.created_date from claim c, invoice i, audit_trail a ")
                .append("where c.invoice_id = i.id ")
                .append("and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
                .append("and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
                .append("and a.reverted=false and a.claim_id = c.id ")
                .append("and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect', 'InvoiceUnassigned') ")
                .append("and a.new_status='InvoiceApprovedByBRE' ")
                .append("and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months' ")
                .append("and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\'))) b , audit_trail a1 ")
                .append("where b.id = a1.claim_id ")
                .append("and a1.reverted=false and a1.new_status ='InvoicePaymentLogged' ")
                .append("and exists ( select * from audit_trail a2 where a2.reverted=false and a2.claim_id = b.id and a2.new_status='ContestedInvoiceReferredToCHO') ")
                .append("and a1.update_date between b.created_date  and  b.created_date + interval '30 days' )as invoice_approved_by_bre_disputed_paid_within_30days_total, ");

        for(ReasonOfRejection ror : reasonsOfRejection){
            sb.append("(select count(*) from ( select c.id, c.invoice_id from claim c, invoice i, audit_trail a ")
                    .append("where c.invoice_id = i.id ")
                    .append("and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
                    .append("and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
                    .append("and a.reverted=false and a.claim_id = c.id ")
                    .append("and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect', 'InvoiceUnassigned') ")
                    .append("and a.new_status='InvoiceApprovedByBRE' ")
                    .append("and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months' ")
                    .append("and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\'))) b, audit_trail a, invoice i ")
                    .append("where b.id = a.claim_id ")
                    .append("and a.reverted=false and a.new_status = 'ContestedInvoiceReferredToCHO' ")
                    .append("and b.invoice_id = i.id ")
                    .append("and a.invoice_reason_of_rejection =")
                    .append(ror.getId())
                    .append(" and not exists (select * from audit_trail a2 where a2.reverted=false and a2.claim_id=a.claim_id and a2.new_status='ContestedInvoiceReferredToCHO'  and a2.update_date < a.update_date)) as invoice_disputed_due_to_")
                    .append(ror.getRorName());
            if(reasonsOfRejection.indexOf(ror) != reasonsOfRejection.size() -1) {
                sb.append(", ");
            }
        }

        String query = sb.toString();
        LOG.debug(query);
        Map paramMap = new HashMap();
        paramMap.put("pStartDate", dataStart);
        paramMap.put("pChorgId", choId);
        paramMap.put("pInsurerId", insurerId);
        
       

        BreInvoiceApprovalDisputeCumulativeData breInvoiceApprovalDisputeCumulativeData = null;

        List result = reportDataService.getReportData(query, paramMap);

        for (Object o : result) {
            LOG.debug("inside for loop");
            Map data = (Map) o;
            breInvoiceApprovalDisputeCumulativeData = BreInvoiceApprovalDisputeCumulativeData.getObject(data, reasonsOfRejection);
        }
        
        for (int x = 0; x <= 11; x++) {

            int y = 1 - x;

            StringBuilder sb1 = new StringBuilder();
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

            sb1.append("(select count(*) from claim c, invoice i where c.invoice_id = i.id and c.claim_type not in ")
                .append(ClaimType.getInsurerUploadTypeOrdinals())
                .append("and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
                .append("and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
                .append("and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) ")
                .append("|| '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) ");

            if (x == 1) {
                sb1.append(" - interval ' ").append(x).append("  month' ");
            } else {
                sb1.append(" - interval ' ").append(x).append("  months' ");
            }

            sb1.append("and to_date(to_char(cast(:pStartDate as Date) ");

            if (y > 0) {
                sb1.append(" + interval '").append(y).append(" month'");
            }
            else if (y == -1) {
                sb1.append(" - interval '").append(-y).append(" month'");
            } else {
                sb1.append(" - interval '").append(-y).append(" months'");

            }

            sb1.append(", TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) ");

            if (y > 0) {
                sb1.append(" + interval '").append(y).append(" month'");
            }
            else if (y == -1) {
                sb1.append(" - interval '").append(-y).append(" month'");
            } else {
                sb1.append(" - interval '").append(-y).append(" months'");
            }

            sb1.append(", TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\'))) "
                    + "as invoice_uploaded_current_total, ");


            sb1.append("(select count(*) from claim c, invoice i, audit_trail a "
                    + "where c.invoice_id = i.id "
                    + "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) "
                    + "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) "
                    + "and a.reverted=false and a.claim_id = c.id "
                    + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect', 'InvoiceUnassigned') "
                    + "and a.new_status='InvoiceApprovedByBRE' "
                    + "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) "
                    + "|| '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) ");

            if (x == 1) {
                sb1.append(" - interval ' ").append(x).append("  month' ");
            } else {
                sb1.append(" - interval ' ").append(x).append("  months' ");
            }

            sb1.append("and to_date(to_char(cast(:pStartDate as Date) ");

            if (y > 0) {
                sb1.append(" + interval '").append(y).append(" month'");
            }
            else if (y == -1) {
                sb1.append(" - interval '").append(-y).append(" month'");
            } else {
                sb1.append(" - interval '").append(-y).append(" months'");
            }

            sb1.append(", TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) ");

            if (y > 0) {
                sb1.append(" + interval '").append(y).append(" month'");
            }
            else if (y == -1) {
                sb1.append(" - interval '").append(-y).append(" month'");
            } else {
                sb1.append(" - interval '").append(-y).append(" months'");
            }

            sb1.append(", TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\'))) "
                    + "as invoice_approved_by_bre_current, ");

            sb1.append("(select count(distinct b.id) from ( select c.id, c.invoice_id from claim c, invoice i, audit_trail a "
                    + "where c.invoice_id = i.id "
                    + "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) "
                    + "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) "
                    + "and a.reverted=false and a.claim_id = c.id "
                    + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect', 'InvoiceUnassigned') "
                    + "and a.new_status='InvoiceApprovedByBRE' "
                    + "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) "
                    + "|| '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) ");

            if (x == 1) {
                sb1.append(" - interval ' ").append(x).append("  month' ");
            } else {
                sb1.append(" - interval ' ").append(x).append("  months' ");
            }

            sb1.append("and to_date(to_char(cast(:pStartDate as Date) ");

            if (y > 0) {
                sb1.append(" + interval '").append(y).append(" month'");
            }
            else if (y == -1) {
                sb1.append(" - interval '").append(-y).append(" month'");
            } else {
                sb1.append(" - interval '").append(-y).append(" months'");
            }

            sb1.append(", TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) ");

            if (y > 0) {
                sb1.append(" + interval '").append(y).append(" month'");
            }
            else if (y == -1) {
                sb1.append(" - interval '").append(-y).append(" month'");
            } else {
                sb1.append(" - interval '").append(-y).append(" months'");
            }

            sb1.append(", TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\'))) "
                    + "b, audit_trail a where b.id = a.claim_id and a.reverted=false and a.new_status = 'ContestedInvoiceReferredToCHO' ) as invoice_approved_by_bre_disputed_current, ");

            sb1.append("(select count(*) from (select c.id, c.invoice_id, i.created_date from claim c, invoice i, audit_trail a "
                    + "where c.invoice_id = i.id "
                    + "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) "
                    + "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) "
                    + "and a.reverted=false and a.claim_id = c.id "
                    + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect', 'InvoiceUnassigned') "
                    + "and a.new_status='InvoiceApprovedByBRE' "
                    + "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) "
                    + "|| '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) ");

            if (x == 1) {
                sb1.append(" - interval ' ").append(x).append("  month' ");
            } else {
                sb1.append(" - interval ' ").append(x).append("  months' ");
            }

            sb1.append("and to_date(to_char(cast(:pStartDate as Date) ");

            if (y > 0) {
                sb1.append(" + interval '").append(y).append(" month'");
            }
            else if (y == -1) {
                sb1.append(" - interval '").append(-y).append(" month'");
            } else {
                sb1.append(" - interval '").append(-y).append(" months'");
            }

            sb1.append(", TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) ");

            if (y > 0) {
                sb1.append(" + interval '").append(y).append(" month'");
            }
            else if (y == -1) {
                sb1.append(" - interval '").append(-y).append(" month'");
            } else {
                sb1.append(" - interval '").append(-y).append(" months'");
            }

            sb1.append(", TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\'))) "
                    + "b , audit_trail a1 "
                    + "where b.id = a1.claim_id "
                    + "and a1.reverted=false and a1.new_status ='InvoicePaymentLogged' "
                    + "and not exists ( select * from audit_trail a2 where a2.reverted=false and a2.claim_id = b.id and a2.new_status='ContestedInvoiceReferredToCHO') "
                    + "and a1.update_date between b.created_date  and  b.created_date + interval '15 days'  )as invoice_approved_by_bre_not_disputed_paid_within_15days_current, ");

            sb1.append("(select count(*) from (select c.id, c.invoice_id, i.created_date from claim c, invoice i, audit_trail a "
                    + "where c.invoice_id = i.id "
                    + "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) "
                    + "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) "
                    + "and a.reverted=false and a.claim_id = c.id "
                    + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect', 'InvoiceUnassigned') "
                    + "and a.new_status='InvoiceApprovedByBRE' "
                    + "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) "
                    + "|| '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) ");

            if (x == 1) {
                sb1.append(" - interval ' ").append(x).append("  month' ");
            } else {
                sb1.append(" - interval ' ").append(x).append("  months' ");
            }

            sb1.append("and to_date(to_char(cast(:pStartDate as Date) ");

            if (y > 0) {
                sb1.append(" + interval '").append(y).append(" month'");
            }
            else if (y == -1) {
                sb1.append(" - interval '").append(-y).append(" month'");

            } else {
                sb1.append(" - interval '").append(-y).append(" months'");
            }

            sb1.append(", TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) ");

            if (y > 0) {
                sb1.append(" + interval '").append(y).append(" month'");
            }
            else if (y == -1) {
                sb1.append(" - interval '").append(-y).append(" month'");
            } else {
                sb1.append(" - interval '").append(-y).append(" months'");
            }

            sb1.append(", TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\'))) "
                    + "b , audit_trail a1 where b.id = a1.claim_id "
                    + "and a1.reverted=false and a1.new_status ='InvoicePaymentLogged' "
                    + "and not exists ( select * from audit_trail a2 where a2.reverted=false and a2.claim_id =b.id and a2.new_status='ContestedInvoiceReferredToCHO') "
                    + "and a1.update_date between b.created_date  and  b.created_date + interval '30 days'  )as invoice_approved_by_bre_not_disputed_paid_within_30days_current, ");

            sb1.append("(select count(*) from (select c.id, c.invoice_id, i.created_date from claim c, invoice i, audit_trail a "
                    + "where c.invoice_id = i.id "
                    + "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) "
                    + "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) "
                    + "and a.reverted=false and a.claim_id = c.id "
                    + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect', 'InvoiceUnassigned') "
                    + "and a.new_status='InvoiceApprovedByBRE' "
                    + "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) "
                    + "|| '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) ");

            if (x == 1) {
                sb1.append(" - interval ' ").append(x).append("  month' ");
            } else {
                sb1.append(" - interval ' ").append(x).append("  months' ");
            }

            sb1.append("and to_date(to_char(cast(:pStartDate as Date) ");

            if (y > 0) {
                sb1.append(" + interval '").append(y).append(" month'");
            }
            else if (y == -1) {
                sb1.append(" - interval '").append(-y).append(" month'");
            } else {
                sb1.append(" - interval '").append(-y).append(" months'");
            }

            sb1.append(", TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) ");

            if (y > 0) {
                sb1.append(" + interval '").append(y).append(" month'");
            }
            else if (y == -1) {
                sb1.append(" - interval '").append(-y).append(" month'");
            } else {
                sb1.append(" - interval '").append(-y).append(" months'");
            }

            sb1.append(", TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\'))) "
                    + "b , audit_trail a1 "
                    + "where b.id = a1.claim_id "
                    + "and a1.reverted=false and a1.new_status ='InvoicePaymentLogged' "
                    + "and exists ( select * from audit_trail a2 where a2.reverted=false and a2.claim_id = b.id and a2.new_status='ContestedInvoiceReferredToCHO') "
                    + "and a1.update_date between b.created_date  and  b.created_date + interval '15 days'  )as invoice_approved_by_bre_disputed_paid_within_15days_current, ");

            sb1.append("(select count(*) from (select c.id, c.invoice_id, i.created_date from claim c, invoice i, audit_trail a "
                    + "where c.invoice_id = i.id "
                    + "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) "
                    + "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) "
                    + "and a.reverted=false and a.claim_id = c.id "
                    + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect', 'InvoiceUnassigned') "
                    + "and a.new_status='InvoiceApprovedByBRE' "
                    + "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) "
                    + "|| '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) ");
            if (x == 1) {
                sb1.append(" - interval ' ").append(x).append("  month' ");
            } else {
                sb1.append(" - interval ' ").append(x).append("  months' ");
            }

            sb1.append("and to_date(to_char(cast(:pStartDate as Date) ");

            if (y > 0) {
                sb1.append(" + interval '").append(y).append(" month'");
            }
            else if (y == -1) {
                sb1.append(" - interval '").append(-y).append(" month'");
            } else {
                sb1.append(" - interval '").append(-y).append(" months'");
            }

            sb1.append(", TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) ");

            if (y > 0) {
                sb1.append(" + interval '").append(y).append(" month'");
            }
            else if (y == -1) {
                sb1.append(" - interval '").append(-y).append(" month'");
            } else {
                sb1.append(" - interval '").append(-y).append(" months'");
            }

            sb1.append(", TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\'))) "
                    + "b , audit_trail a1 where b.id = a1.claim_id "
                    + "and a1.reverted=false and a1.new_status ='InvoicePaymentLogged' "
                    + "and exists ( select * from audit_trail a2 where a2.reverted=false and a2.claim_id=b.id and a2.new_status='ContestedInvoiceReferredToCHO') "
                    + "and a1.update_date between b.created_date  and  b.created_date + interval '30 days'  ) as invoice_approved_by_bre_disputed_paid_within_30days_current, ");
            
            for(ReasonOfRejection ror : reasonsOfRejection){
                sb1.append("(select count(*) from ( select c.id, c.invoice_id from claim c, invoice i, audit_trail a "
                        + "where c.invoice_id = i.id "
                        + "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) "
                        + "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) "
                        + "and a.reverted=false and a.claim_id = c.id "
                        + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect', 'InvoiceUnassigned') "
                        + "and a.new_status='InvoiceApprovedByBRE' "
                        + "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) "
                        + "|| '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) ");

                if (x == 1) {
                    sb1.append(" - interval ' ").append(x).append("  month' ");
                } else {
                    sb1.append(" - interval ' ").append(x).append("  months' ");
                }

                sb1.append("and to_date(to_char(cast(:pStartDate as Date) ");

                if (y > 0) {
                    sb1.append(" + interval '").append(y).append(" month'");
                }
                else if (y == -1) {
                    sb1.append(" - interval '").append(-y).append(" month'");

                } else {
                    sb1.append(" - interval '").append(-y).append(" months'");
                }

                sb1.append(", TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) ");

                if (y > 0) {
                    sb1.append(" + interval '").append(y).append(" month'");
                }
                else if (y == -1) {
                    sb1.append(" - interval '").append(-y).append(" month'");
                } else {
                    sb1.append(" - interval '").append(-y).append(" months'");
                }

                sb1.append(", TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\'))) "
                        + "b, audit_trail a, invoice i "
                        + "where b.id = a.claim_id "
                        + "and a.reverted=false and a.new_status = 'ContestedInvoiceReferredToCHO' "
                        + "and b.invoice_id = i.id "
                        + "and a.invoice_reason_of_rejection = ")
                        .append(ror.getId())
                        .append(" and not exists (select * from audit_trail a2 where a2.reverted=false and a2.claim_id=a.claim_id and a2.new_status='ContestedInvoiceReferredToCHO'  and a2.update_date < a.update_date)) as invoice_disputed_due_to_")
                        .append(ror.getRorName());
                        if(reasonsOfRejection.indexOf(ror) != reasonsOfRejection.size() - 1) {
                    sb1.append(", ");
                }
            }
            
            String query1 = sb1.toString();
            LOG.debug("query1 :" + query1);

            Map paramMap1 = new HashMap();
            paramMap1.put("pStartDate", dataStart);
            paramMap1.put("pChorgId", choId);
            paramMap1.put("pInsurerId", insurerId);

            List result1 = reportDataService.getReportData(query1, paramMap1);

            for (int i = 0; i < result1.size(); i++) {
                LOG.debug("results :" + result1.get(i));
            }

            List<BreInvoiceApprovalDisputedData> lineData = new ArrayList<BreInvoiceApprovalDisputedData>();
            for (Object o : result1) {

                LOG.debug("inside for loop individual ");
                Map data1 = (Map) o;
                BreInvoiceApprovalDisputedData breInvoiceApprovalDisputedData = BreInvoiceApprovalDisputedData.getObject(data1, reasonsOfRejection);
                breInvoiceApproval.add(breInvoiceApprovalDisputedData);
                LOG.debug("bean is populated ");
            }
        }
        
        
        
        //sets the data for uoter loop which loops trough reasons of rejection
        for(ReasonOfRejection ror : getReasonsOfRejection(currentUser, true)){
            BreInvoiceApprovalDisputedRoRData rorData = new BreInvoiceApprovalDisputedRoRData();
            
            Map<Integer, BigDecimal> listOfCommLineData = breInvoiceApprovalDisputeCumulativeData.getDisputedApprovalReasonsMap();
            if (listOfCommLineData != null) {
                rorData.setCommlineData(listOfCommLineData.get(ror.getId()));
            } else {
                rorData.setCommlineData(BigDecimal.ZERO);
            }
            List<BigDecimal> lineData = new ArrayList<BigDecimal>();
            for(BreInvoiceApprovalDisputedData disData : breInvoiceApproval){
                if(disData.getDisputedApprovalReasonsMap() != null) {
                    lineData.add(disData.getDisputedApprovalReasonsMap().get(ror.getId()));
                }
            }
            rorData.setLineData(lineData);
            rorData.setName(ror.getRorName());
            rorData.setId(ror.getId());
            
            breInvRorData.add(rorData);
        }
        
        //populates the 'reason of rejection loop' 
//        reportParameters.put("breInvoiceRorHeader",getReasonsOfRejection(currentUser, true));
        reportParameters.put("breInvoiceRor", breInvRorData);
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
    
    private List<ReasonOfRejection> getReasonsOfRejection(WebUser currentUser, boolean displayInHeader) {
        List<ReasonOfRejection> reportRows = new ArrayList<ReasonOfRejection>();
        List result;
        if(currentUser.getInsurer() != null){
            String query = "select ror.id, ror.name from reason_of_rejection ror join invoice iv on ror.id = iv.reason_of_rejection_id " +
                    "where ror.type='Invoice' " +
                    "and ror.insurer_id = :insurerId group by ror.id " +
                    "union select id, name from reason_of_rejection where (gta_active = true or insurer_vs_insurer_active=true or subscriber_active=true or fixed_fee_active=true or insurer_upload_active = true or tpi_active= true) and type='Invoice' and insurer_id = :insurerId order by name asc ";
            Map paramMap = new HashMap();
            paramMap.put("insurerId", currentUser.getInsurer().getId());
            result = reportDataService.getReportData(query, paramMap);
        } else {
            String query = "select ror.id, ror.name from reason_of_rejection ror join invoice iv on ror.id = iv.reason_of_rejection_id " +
                    "where ror.type='Invoice' " +
                    "and ror.insurer_id in (select insurer_id from insurer_chorganisation where chorganisation_id = :choId)" +
                    " group by ror.id  " +
                    "union select id, name from reason_of_rejection where (gta_active = true or insurer_vs_insurer_active=true or subscriber_active=true or fixed_fee_active=true or insurer_upload_active = true or tpi_active= true) and type='Invoice' and insurer_id " +
                    "in (select insurer_id from insurer_chorganisation where chorganisation_id = :choId) order by name asc ";
            Map paramMap = new HashMap();
            paramMap.put("choId", currentUser.getChorganisation().getId());
            result = reportDataService.getReportData(query, paramMap);
        }
        
        for (Object o : result) {
            Map data = (Map) o;
            ReasonOfRejection reportRow = new ReasonOfRejection();
            reportRow.setId(MathHelper.getIntegerValue(data.get("id".toLowerCase())));
            if(!displayInHeader){
                reportRow.setRorName(data.get("name").toString().replaceAll("\\s+", "_").replaceAll("[^A-Za-z0-9_]", ""));
            } else {
                reportRow.setRorName(data.get("name").toString());
            }
            reportRows.add(reportRow);
        }

        return reportRows;
    }

    @Override
    public String getReportTemplateFileName() {
        return "template_BreInvoiceApprovalDisputeReport.xls";
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
    public String getReportCode() {
        return "RPT030";
    }
    
    @Override
    public short[] getColumnsToHide() {
        return null;
    }
}
