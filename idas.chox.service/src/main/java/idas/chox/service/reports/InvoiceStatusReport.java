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
import idas.chox.core.services.ReportDataService;
import idas.chox.core.util.DateHelper;
import idas.chox.core.util.TextHelper;
import idas.chox.data.services.BaseDataService;
import idas.chox.service.reports.viewdata.InvoiceStatusReportCummulativeData;
import idas.chox.service.reports.viewdata.InvoiceStatusReportViewData;

public class InvoiceStatusReport implements Report {
    private static final Logger LOG = LoggerFactory.getLogger(InvoiceStatusReport.class);
    private Map externalParameter;
    private List<String> reportParameterNames;
    private BaseDataService baseDataService;
    private WebUser user = new WebUser();
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

    private Chorganisation getChorganisation(int orgId) {
        Chorganisation chorg = new Chorganisation();

        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Chorganisation.class);
            criteria.add(Restrictions.eq("id", orgId));
            chorg = (Chorganisation) baseDataService.getByCriteria(criteria);
        } catch (Exception e) {
            LOG.error("Error thrown getting Chorganisation from id={}: {}", orgId, e.getMessage());
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
            LOG.error("Error thrown getting Insurer from id={}: {}", orgId, e.getMessage());
        }

        return ins;
    }


    @Override
    public Map<String, Object> getReportParameters() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        Date dataStart = null;
        String supplierId;
        String insrId;
        Integer choId = -1;
        Integer insurerId = -1;
        String selectedOrgLabel = "";
        String reportColumnHeader = "";
        String userOrgLabel = "";
        String selectedOrgName = "All";
        String userOrgName = "";
        Date createDate = new Date();
        List<InvoiceStatusReportViewData> invoiceStatusReport = null;
        InvoiceStatusReportCummulativeData invoiceStatusReportDataCumm = null;

        WebUser currentUser = ((WebUser) externalParameter.get("CurrentUser"));

        try {
        userOrgName = currentUser.getOrganisationName();
        if (((String[]) externalParameter.get("DateStart")) != null) {
            dataStart = DateHelper.parse(((String[]) externalParameter.get("DateStart"))[0]);
            LOG.debug("dataStart :" + dataStart);
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

            LOG.debug("insurerId :" + insurerId);
            LOG.debug("choId :" + choId);
            LOG.debug("selectedOrgName :" + selectedOrgName);
        } else {
            choId = currentUser.getChorganisation().getId();

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

        invoiceStatusReport = new ArrayList<InvoiceStatusReportViewData>();
        // List<InvoiceStatusReportCummulativeData> invoiceStatusReportCummulative = new ArrayList<InvoiceStatusReportCummulativeData>();

        StringBuilder sb1 = new StringBuilder();
        sb1.append("select ");
        sb1.append("(select TEXT(\'Last 12 Months\'))as month_header, ");

        sb1.append("(select count(*) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as no_invoices_uploaded_total, ");
        sb1.append("(select COALESCE(sum(i.total_gross),0.0) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as val_invoices_uploaded_total, ");
        sb1.append("(select COALESCE(sum(i.interim_payment_made),0.0) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as val_interim_payment_invoices_uploaded_total, ");
        sb1.append("(select count(*) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and i.total_penalty_charge > 0.0 ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as no_invoices_penalty_total, ");
        sb1.append("(select COALESCE(sum(i.total_penalty_charge),0.0) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and i.total_penalty_charge > 0.0 ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as val_invoices_penalty_total, ");
        sb1.append("(select count(*) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status = 'InvoicePaymentLogged' ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as no_invoices_payment_logged_total, ");
        sb1.append("(select COALESCE(sum(i.total_gross),0.0) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status = 'InvoicePaymentLogged' ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as val_invoices_payment_logged_total, ");
        sb1.append("(select COALESCE(sum(i.interim_payment_made),0.0) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status = 'InvoicePaymentLogged' ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as val_interim_payment_payment_logged_total, ");
        sb1.append("(select count(*) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status = 'PaymentReceived' ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as no_invoices_payment_reconciled_total, ");
        sb1.append("(select COALESCE(sum(i.total_gross),0.0) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status = 'PaymentReceived' ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as val_invoices_payment_reconciled_total, ");
        sb1.append("(select COALESCE(sum(i.interim_payment_made),0.0) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status = 'PaymentReceived' ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as val_interim_payment_reconciled_total, ");
        sb1.append("(select count(*) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status in ('InvoiceRejectionAccepted', 'ClaimClosed') ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as no_invoices_withdrawn_total, ");
        sb1.append("(select COALESCE(sum(i.total_gross),0.0) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status in ('InvoiceRejectionAccepted', 'ClaimClosed') ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as val_invoices_withdrawn_total, ");
        sb1.append("(select COALESCE(sum(i.interim_payment_made),0.0) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status in ('InvoiceRejectionAccepted', 'ClaimClosed') ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as val_interim_payment_withdrawn_total, ");
        sb1.append("(select count(*) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status in ('AwaitingLiabilityResolution', 'ContestedInvoiceReferredToCHO', 'InvoiceDataCalculationIncorrect', 'InvoiceApprovedByBRE', 'InvoiceEscalatedToHandler', 'InvoiceEscalated', 'InvoiceReferredToEngineer', 'InvoiceReferredToClaimsHandler', 'ContestedInvoiceReferredToInsurer', 'AwaitingInvoicePayment', 'InvoiceUnassigned', 'ManualInvoiceBREApproved', 'ManualInvoiceBRERejected', 'ManualInvoiceContested', 'ManualInvoiceUnassigned', 'AwaitingLitigationOutcome') ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as no_invoices_awaiting_total, ");
        sb1.append("(select COALESCE(sum(i.total_gross),0.0) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status in ('AwaitingLiabilityResolution', 'ContestedInvoiceReferredToCHO', 'InvoiceDataCalculationIncorrect', 'InvoiceApprovedByBRE', 'InvoiceEscalatedToHandler', 'InvoiceEscalated', 'InvoiceReferredToEngineer', 'InvoiceReferredToClaimsHandler', 'ContestedInvoiceReferredToInsurer', 'AwaitingInvoicePayment', 'InvoiceUnassigned', 'ManualInvoiceBREApproved', 'ManualInvoiceBRERejected', 'ManualInvoiceContested', 'ManualInvoiceUnassigned', 'AwaitingLitigationOutcome') ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as val_invoices_awaiting_total, ");
        sb1.append("(select COALESCE(sum(i.interim_payment_made),0.0) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status in ('AwaitingLiabilityResolution', 'ContestedInvoiceReferredToCHO', 'InvoiceDataCalculationIncorrect', 'InvoiceApprovedByBRE', 'InvoiceEscalatedToHandler', 'InvoiceEscalated', 'InvoiceReferredToEngineer', 'InvoiceReferredToClaimsHandler', 'ContestedInvoiceReferredToInsurer', 'AwaitingInvoicePayment', 'InvoiceUnassigned', 'ManualInvoiceBREApproved', 'ManualInvoiceBRERejected', 'ManualInvoiceContested', 'ManualInvoiceUnassigned', 'AwaitingLitigationOutcome') ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as val_interim_payment_awaiting_total, ");
        sb1.append("(select count(*) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status = 'AwaitingLiabilityResolution' ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as no_invoices_awaitingliability_total, ");
        sb1.append("(select COALESCE(sum(i.total_gross),0.0) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status = 'AwaitingLiabilityResolution' ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as val_invoices_awaitingliability_total, ");
        sb1.append("(select COALESCE(sum(i.interim_payment_made),0.0) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status = 'AwaitingLiabilityResolution' ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as val_interim_payment_awaitingliability_total, ");
        sb1.append("(select count(*) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status in ( 'ContestedInvoiceReferredToCHO','InvoiceDataCalculationIncorrect') ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as no_invoices_cho_awaiting_total, ");
        sb1.append("(select COALESCE(sum(i.total_gross),0.0) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status in ( 'ContestedInvoiceReferredToCHO','InvoiceDataCalculationIncorrect') ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as val_invoices_cho_awaiting_total, ");
        sb1.append("(select COALESCE(sum(i.interim_payment_made),0.0) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status in ( 'ContestedInvoiceReferredToCHO','InvoiceDataCalculationIncorrect') ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as val_interim_payment_cho_awaiting_total, ");
        sb1.append("(select count(*) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status in ('InvoiceApprovedByBRE','InvoiceEscalatedToHandler','InvoiceEscalated','InvoiceReferredToEngineer','InvoiceReferredToClaimsHandler','ContestedInvoiceReferredToInsurer','AwaitingInvoicePayment', 'InvoiceUnassigned', 'ManualInvoiceBREApproved', 'ManualInvoiceBRERejected', 'ManualInvoiceContested', 'ManualInvoiceUnassigned') ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as no_invoices_insurer_awaiting_total, ");
        sb1.append("(select COALESCE(sum(i.total_gross),0.0) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status in ('InvoiceApprovedByBRE','InvoiceEscalatedToHandler','InvoiceEscalated','InvoiceReferredToEngineer','InvoiceReferredToClaimsHandler','ContestedInvoiceReferredToInsurer','AwaitingInvoicePayment', 'InvoiceUnassigned', 'ManualInvoiceBREApproved', 'ManualInvoiceBRERejected', 'ManualInvoiceContested', 'ManualInvoiceUnassigned') ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as val_invoices_insurer_awaiting_total, ");
        sb1.append("(select COALESCE(sum(i.interim_payment_made),0.0) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status in ('InvoiceApprovedByBRE','InvoiceEscalatedToHandler','InvoiceEscalated','InvoiceReferredToEngineer','InvoiceReferredToClaimsHandler','ContestedInvoiceReferredToInsurer','AwaitingInvoicePayment', 'InvoiceUnassigned', 'ManualInvoiceBREApproved', 'ManualInvoiceBRERejected', 'ManualInvoiceContested', 'ManualInvoiceUnassigned') ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as val_interim_payment_insurer_awaiting_total, ");
        sb1.append("(select count(*) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status = 'InvoiceApprovedByBRE' ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as no_invoices_approved_by_businessrules_total, ");
        sb1.append("(select COALESCE(sum(i.total_gross),0.0) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status = 'InvoiceApprovedByBRE' ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as val_invoices_approved_by_businessrules_total, ");
        sb1.append("(select COALESCE(sum(i.interim_payment_made),0.0) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status = 'InvoiceApprovedByBRE' ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as val_interim_payment_approved_by_businessrules_total, ");
        sb1.append("(select count(*) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status = 'InvoiceEscalatedToHandler' ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as no_invoices_escalated_to_handler_total, ");
        sb1.append("(select COALESCE(sum(i.total_gross),0.0) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status = 'InvoiceEscalatedToHandler' ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as val_invoices_escalated_to_handler_total, ");
        sb1.append("(select COALESCE(sum(i.interim_payment_made),0.0) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status = 'InvoiceEscalatedToHandler' ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as val_interim_payment_escalated_to_handler_total, ");
        sb1.append("(select count(*) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status = 'InvoiceEscalated' ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as no_invoices_escalated_total, ");
        sb1.append("(select COALESCE(sum(i.total_gross),0.0) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status = 'InvoiceEscalated' ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as val_invoices_escalated_total, ");
        sb1.append("(select COALESCE(sum(i.interim_payment_made),0.0) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status = 'InvoiceEscalated' ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as val_interim_payment_escalated_total, ");
        sb1.append("(select count(*) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status = 'InvoiceReferredToEngineer' ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as no_invoices_referred_to_engineer_total, ");
        sb1.append("(select COALESCE(sum(i.total_gross),0.0) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status = 'InvoiceReferredToEngineer' ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as val_invoices_referred_to_engineer_total, ");
        sb1.append("(select COALESCE(sum(i.interim_payment_made),0.0) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status = 'InvoiceReferredToEngineer' ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as val_interim_payment_referred_to_engineer_total, ");
        sb1.append("(select count(*) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status = 'InvoiceReferredToClaimsHandler' ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as no_invoices_referred_to_handler_total, ");
        sb1.append("(select COALESCE(sum(i.total_gross),0.0) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status = 'InvoiceReferredToClaimsHandler' ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as val_invoices_referred_to_handler_total, ");
        sb1.append("(select COALESCE(sum(i.interim_payment_made),0.0) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status = 'InvoiceReferredToClaimsHandler' ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as val_interim_payment_referred_to_handler_total, ");
        sb1.append("(select count(*) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status = 'ContestedInvoiceReferredToInsurer' ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as no_invoices_cho_dispute_total, ");
        sb1.append("(select COALESCE(sum(i.total_gross),0.0) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status = 'ContestedInvoiceReferredToInsurer' ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as val_invoices_cho_dispute_total, ");
        sb1.append("(select COALESCE(sum(i.interim_payment_made),0.0) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status = 'ContestedInvoiceReferredToInsurer' ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as val_interim_payment_cho_dispute_total, ");
        sb1.append("(select count(*) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status = 'InvoiceUnassigned' ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as no_invoices_unassigned_total, ");
        sb1.append("(select COALESCE(sum(i.total_gross),0.0) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status = 'InvoiceUnassigned' ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as val_invoices_unassigned_total, ");
        sb1.append("(select COALESCE(sum(i.interim_payment_made),0.0) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status = 'InvoiceUnassigned' ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as val_interim_payment_unassigned_total, ");
        sb1.append("(select count(*) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status = 'AwaitingInvoicePayment' ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as no_invoices_awaiting_payment_total, ");
        sb1.append("(select COALESCE(sum(i.total_gross),0.0) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status = 'AwaitingInvoicePayment' ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as val_invoices_awaiting_payment_total, ");
        sb1.append("(select COALESCE(sum(i.interim_payment_made),0.0) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status = 'AwaitingInvoicePayment' ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as val_interim_payment_awaiting_payment_total, ");
        sb1.append("(select count(*) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status = 'ManualInvoicePaid' ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as no_manual_invoices_paid_total, ");
        sb1.append("(select COALESCE(sum(i.total_gross),0.0) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status = 'ManualInvoicePaid' ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as val_manual_invoices_paid_total, ");
        sb1.append("(select count(*) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status = 'ManualInvoiceUnassigned' ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as no_manual_invoices_in_to_be_assigned_total, ");
        sb1.append("(select COALESCE(sum(i.total_gross),0.0) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status = 'ManualInvoiceUnassigned' ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as val_manual_invoices_in_to_be_assigned_total, ");
        sb1.append("(select count(*) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status = 'ManualInvoiceBREApproved' ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as no_manual_invoices_approved_total, ");
        sb1.append("(select COALESCE(sum(i.total_gross),0.0) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status = 'ManualInvoiceBREApproved' ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as val_manual_invoices_approved_total, ");
        sb1.append("(select count(*) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status = 'ManualInvoiceBRERejected' ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as no_manual_invoices_rejected_total, ");
        sb1.append("(select COALESCE(sum(i.total_gross),0.0) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status = 'ManualInvoiceBRERejected' ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as val_manual_invoices_rejected_total, ");
        sb1.append("(select count(*) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status = 'ManualInvoiceContested' ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as no_manual_invoices_contested_total, ");
        sb1.append("(select COALESCE(sum(i.total_gross),0.0) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status = 'ManualInvoiceContested' ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as val_manual_invoices_contested_total, ");
        sb1.append("(select count(*) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status in ('AwaitingLitigationOutcome') ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as no_invoices_in_litigation_status_total, ");
        sb1.append("(select COALESCE(sum(i.total_gross),0.0) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status in ('AwaitingLitigationOutcome') ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as val_invoices_in_litigation_status_total, ");
        sb1.append("(select COALESCE(sum(i.interim_payment_made),0.0) from claim c, invoice i ")
           .append( "where c.invoice_id = i.id ")
           .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
           .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
           .append( "and c.status in ('AwaitingLitigationOutcome') ")
           .append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) - interval '11 months'  ")
           .append( "and to_date(to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'MM\')) || '-01-' || to_char(cast(:pStartDate as Date) + interval '1 month', TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')))  as val_interim_payment_invoices_in_litigation_status_total ");
        
        String query1 = sb1.toString();
        LOG.debug(query1);
        Map paramMap1 = new HashMap();
        paramMap1.put("pStartDate", dataStart);
        paramMap1.put("pChorgId", choId);
        paramMap1.put("pInsurerId", insurerId);

        List result1 = reportDataService.getReportData(query1, paramMap1);

        for (Object o : result1) {
            Map data = (Map) o;
            invoiceStatusReportDataCumm = InvoiceStatusReportCummulativeData.getObject(data);
            // invoiceStatusReportCummulative.add(invoiceStatusReportDataCumm);
        }

        LOG.debug("outside for loop");

        for (int x = 0; x <= 11; x++) {
            StringBuilder sb = new StringBuilder();

            sb.append("select ");
            if (x == 0) {
                sb.append("(select TO_CHAR(cast(:pStartDate as Date), TEXT(\'MON\')) || TEXT(\'-\') || TO_CHAR(cast(:pStartDate as Date), TEXT(\'yyyy\')))as month_header, ");
            }
            else if (x == 1) {
                sb.append("(select TO_CHAR(cast(:pStartDate as Date) - interval '1 month', TEXT(\'MON\')) || TEXT(\'-\') || TO_CHAR(cast(:pStartDate as Date) - interval '1 month', TEXT(\'yyyy\')))as month_header, ");
            }
            else {
                sb.append("(select TO_CHAR(cast(:pStartDate as Date) - interval '")
                  .append(x)
                  .append(" months', TEXT(\'MON\')) || TEXT(\'-\') || TO_CHAR(cast(:pStartDate as Date) - interval '")
                  .append(x)
                  .append(" months', TEXT(\'yyyy\'))) as month_header, ");
            }

            /*
             * Build the query restriction on created_date which we will re-use
             */
            StringBuilder createdDateRestrictionSb = new StringBuilder();
            createdDateRestrictionSb.append( "and i.created_date between to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) ")
                .append( "|| '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) ");

            if (x == 1) {
                createdDateRestrictionSb.append(" - interval ' ").append(x).append("  month' ");
            } else if (x > 1) {
                createdDateRestrictionSb.append(" - interval ' ").append(x).append("  months' ");
            }

            createdDateRestrictionSb.append("and to_date(to_char(cast(:pStartDate as Date), TEXT(\'MM\')) ")
                .append( "|| '-01-' || to_char(cast(:pStartDate as Date), TEXT(\'yyyy\')), TEXT(\'mm-dd-yyyy\')) ");

            if (x == 0) {
                createdDateRestrictionSb.append(" + interval '1 month'");
            }
            else if (x == 2) {
                createdDateRestrictionSb.append(" - interval '1 month'");
            } else if (x > 2) {
                createdDateRestrictionSb.append(" - interval '").append(x-1).append(" months'");
            }

            String createdDateRestriction = createdDateRestrictionSb.toString();

            sb.append("(select count(*) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append(createdDateRestriction)
              .append(") as no_invoices_uploaded_current_month, ");

            sb.append("(select COALESCE(sum(i.total_gross),0.0) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append(createdDateRestriction)
              .append(") as val_invoices_uploaded_current_month, ");
            
            sb.append("(select COALESCE(sum(i.interim_payment_made),0.0) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append(createdDateRestriction)
              .append(") as val_interim_payment_invoices_uploaded_current_month, ");
            
            sb.append("(select count(*) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and i.total_penalty_charge > 0.0 ")
              .append(createdDateRestriction)
              .append(") as no_invoices_penalty_current_month, ");

            sb.append("(select COALESCE(sum(i.total_penalty_charge),0.0) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and i.total_penalty_charge > 0.0 ")
              .append(createdDateRestriction)
              .append(") as val_invoices_penalty_current_month, ");


            sb.append("(select count(*) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status = 'InvoicePaymentLogged' ")
              .append(createdDateRestriction)
              .append(") as no_invoices_payment_logged_current_month, ");

            sb.append("(select COALESCE(sum(i.total_gross),0.0) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status = 'InvoicePaymentLogged' ")
              .append(createdDateRestriction)
              .append(") as val_invoices_payment_logged_current_month, ");

            sb.append("(select COALESCE(sum(i.interim_payment_made),0.0) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status = 'InvoicePaymentLogged' ")
              .append(createdDateRestriction)
              .append(") as val_interim_payment_payment_logged_current_month, ");
            
            sb.append("(select count(*) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status = 'PaymentReceived' ")
              .append(createdDateRestriction)
              .append(") as no_invoices_payment_reconciled_current_month, ");

            sb.append("(select COALESCE(sum(i.total_gross),0.0) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status = 'PaymentReceived' ")
              .append(createdDateRestriction)
              .append(") as val_invoices_payment_reconciled_current_month, ");

            sb.append("(select COALESCE(sum(i.interim_payment_made),0.0) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status = 'PaymentReceived' ")
              .append(createdDateRestriction)
              .append(") as val_interim_payment_reconciled_current_month, ");
            
            sb.append("(select count(*) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status in ('InvoiceRejectionAccepted', 'ClaimClosed') ")
              .append(createdDateRestriction)
              .append(") as no_invoices_withdrawn_current_month, ");

            sb.append("(select COALESCE(sum(i.total_gross),0.0) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status in ('InvoiceRejectionAccepted', 'ClaimClosed') ")
              .append(createdDateRestriction)
              .append(") as val_invoices_withdrawn_current_month, ");

            sb.append("(select COALESCE(sum(i.interim_payment_made),0.0) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status in ('InvoiceRejectionAccepted', 'ClaimClosed') ")
              .append(createdDateRestriction)
              .append(") as val_interim_payment_withdrawn_current_month, ");
            
            sb.append("(select count(*) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status in ('AwaitingLiabilityResolution', 'ContestedInvoiceReferredToCHO', 'InvoiceDataCalculationIncorrect', 'InvoiceApprovedByBRE', 'InvoiceEscalatedToHandler', 'InvoiceEscalated', 'InvoiceReferredToEngineer', 'InvoiceReferredToClaimsHandler', 'ContestedInvoiceReferredToInsurer', 'AwaitingInvoicePayment', 'InvoiceUnassigned', 'ManualInvoiceBREApproved', 'ManualInvoiceBRERejected', 'ManualInvoiceContested', 'AwaitingLitigationOutcome') ")
              .append(createdDateRestriction)
              .append(") as no_invoices_awaiting_current_month, ");

            sb.append("(select COALESCE(sum(i.total_gross),0.0) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status in ('AwaitingLiabilityResolution', 'ContestedInvoiceReferredToCHO', 'InvoiceDataCalculationIncorrect', 'InvoiceApprovedByBRE', 'InvoiceEscalatedToHandler', 'InvoiceEscalated', 'InvoiceReferredToEngineer', 'InvoiceReferredToClaimsHandler', 'ContestedInvoiceReferredToInsurer', 'AwaitingInvoicePayment', 'InvoiceUnassigned', 'ManualInvoiceBREApproved', 'ManualInvoiceBRERejected', 'ManualInvoiceContested', 'AwaitingLitigationOutcome') ")
              .append(createdDateRestriction)
              .append(") as val_invoices_awaiting_current_month, ");

            sb.append("(select COALESCE(sum(i.interim_payment_made),0.0) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status in ('AwaitingLiabilityResolution', 'ContestedInvoiceReferredToCHO', 'InvoiceDataCalculationIncorrect', 'InvoiceApprovedByBRE', 'InvoiceEscalatedToHandler', 'InvoiceEscalated', 'InvoiceReferredToEngineer', 'InvoiceReferredToClaimsHandler', 'ContestedInvoiceReferredToInsurer', 'AwaitingInvoicePayment', 'InvoiceUnassigned', 'ManualInvoiceBREApproved', 'ManualInvoiceBRERejected', 'ManualInvoiceContested', 'AwaitingLitigationOutcome') ")
              .append(createdDateRestriction)
              .append(") as val_interim_payment_awaiting_current_month, ");
            
            sb.append("(select count(*) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status = 'AwaitingLiabilityResolution' ")
              .append(createdDateRestriction)
              .append(") as no_invoices_awaitingliability_current_month, ");

            sb.append("(select COALESCE(sum(i.total_gross),0.0) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status = 'AwaitingLiabilityResolution' ")
              .append(createdDateRestriction)
              .append(") as val_invoices_awaitingliability_current_month, ");

            
            sb.append("(select COALESCE(sum(i.interim_payment_made),0.0) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status = 'AwaitingLiabilityResolution' ")
              .append(createdDateRestriction)
              .append(") as val_interim_payment_awaitingliability_current_month, ");
            
            
            sb.append("(select count(*) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status in ( 'ContestedInvoiceReferredToCHO','InvoiceDataCalculationIncorrect') ")
              .append(createdDateRestriction)
              .append(") as no_invoices_cho_awaiting_current_month, ");

            sb.append("(select COALESCE(sum(i.total_gross),0.0) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status in ( 'ContestedInvoiceReferredToCHO','InvoiceDataCalculationIncorrect') ")
              .append(createdDateRestriction)
              .append(") as val_invoices_cho_awaiting_current_month, ");

            sb.append("(select COALESCE(sum(i.interim_payment_made),0.0) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status in ( 'ContestedInvoiceReferredToCHO','InvoiceDataCalculationIncorrect') ")
              .append(createdDateRestriction)
              .append(") as val_interim_payment_cho_awaiting_current_month, ");
            
            sb.append("(select count(*) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status in ('InvoiceApprovedByBRE','InvoiceEscalatedToHandler','InvoiceEscalated','InvoiceReferredToEngineer','InvoiceReferredToClaimsHandler','ContestedInvoiceReferredToInsurer','AwaitingInvoicePayment', 'ManualInvoiceBREApproved', 'ManualInvoiceBRERejected', 'ManualInvoiceContested') ")
              .append(createdDateRestriction)
              .append(") as no_invoices_insurer_awaiting_current_month, ");

            sb.append("(select COALESCE(sum(i.total_gross),0.0) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status in ('InvoiceApprovedByBRE','InvoiceEscalatedToHandler','InvoiceEscalated','InvoiceReferredToEngineer','InvoiceReferredToClaimsHandler','ContestedInvoiceReferredToInsurer','AwaitingInvoicePayment', 'ManualInvoiceBREApproved', 'ManualInvoiceBRERejected', 'ManualInvoiceContested') ")
              .append(createdDateRestriction)
              .append(") as val_invoices_insurer_awaiting_current_month, ");

            sb.append("(select COALESCE(sum(i.interim_payment_made),0.0) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status in ('InvoiceApprovedByBRE','InvoiceEscalatedToHandler','InvoiceEscalated','InvoiceReferredToEngineer','InvoiceReferredToClaimsHandler','ContestedInvoiceReferredToInsurer','AwaitingInvoicePayment') ")
              .append(createdDateRestriction)
              .append(") as val_interim_payment_insurer_awaiting_current_month, ");
            
            sb.append("(select count(*) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status = 'InvoiceApprovedByBRE' ")
              .append(createdDateRestriction)
              .append(") as no_invoices_approved_by_businessrules_current_month, ");

            sb.append("(select COALESCE(sum(i.total_gross),0.0) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status = 'InvoiceApprovedByBRE' ")
              .append(createdDateRestriction)
              .append(") as val_invoices_approved_by_businessrules_current_month, ");

            sb.append("(select COALESCE(sum(i.interim_payment_made),0.0) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status = 'InvoiceApprovedByBRE' ")
              .append(createdDateRestriction)
              .append(") as val_interim_payment_approved_by_businessrules_current_month, ");
            
            sb.append("(select count(*) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status = 'InvoiceEscalatedToHandler' ")
              .append(createdDateRestriction)
              .append(") as no_invoices_escalated_to_handler_current_month, ");

            sb.append("(select COALESCE(sum(i.total_gross),0.0) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status = 'InvoiceEscalatedToHandler' ")
              .append(createdDateRestriction)
              .append(") as val_invoices_escalated_to_handler_current_month, ");

            sb.append("(select COALESCE(sum(i.interim_payment_made),0.0) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status = 'InvoiceEscalatedToHandler' ")
              .append(createdDateRestriction)
              .append(") as val_interim_payment_escalated_to_handler_current_month, ");
            
            sb.append("(select count(*) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status = 'InvoiceEscalated' ")
              .append(createdDateRestriction)
              .append(") as no_invoices_escalated_current_month, ");

            sb.append("(select COALESCE(sum(i.total_gross),0.0) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status = 'InvoiceEscalated' ")
              .append(createdDateRestriction)
              .append(") as val_invoices_escalated_current_month, ");

            sb.append("(select COALESCE(sum(i.interim_payment_made),0.0) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status = 'InvoiceEscalated' ")
              .append(createdDateRestriction)
              .append(") as val_interim_payment_escalated_current_month, ");
            
            sb.append("(select count(*) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status = 'InvoiceReferredToEngineer' ")
              .append(createdDateRestriction)
              .append(") as no_invoices_referred_to_engineer_current_month, ");

            sb.append("(select COALESCE(sum(i.total_gross),0.0) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status = 'InvoiceReferredToEngineer' ")
              .append(createdDateRestriction)
              .append(") as val_invoices_referred_to_engineer_current_month, ");

            sb.append("(select COALESCE(sum(i.interim_payment_made),0.0) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status = 'InvoiceReferredToEngineer' ")
              .append(createdDateRestriction)
              .append(") as val_interim_payment_referred_to_engineer_current_month, ");
            
            sb.append("(select count(*) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status = 'InvoiceReferredToClaimsHandler' ")
              .append(createdDateRestriction)
              .append(") as no_invoices_referred_to_handler_current_month, ");

            sb.append("(select COALESCE(sum(i.total_gross),0.0) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status = 'InvoiceReferredToClaimsHandler' ")
              .append(createdDateRestriction)
              .append(") as val_invoices_referred_to_handler_current_month, ");

            sb.append("(select COALESCE(sum(i.interim_payment_made),0.0) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status = 'InvoiceReferredToClaimsHandler' ")
              .append(createdDateRestriction)
              .append(") as val_interim_payment_referred_to_handler_current_month, ");
            
            sb.append("(select count(*) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status = 'InvoiceUnassigned' ")
              .append(createdDateRestriction)
              .append(") as no_invoices_unassigned_current_month, ");

            sb.append("(select COALESCE(sum(i.total_gross),0.0) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status = 'InvoiceUnassigned' ")
              .append(createdDateRestriction)
              .append(") as val_invoices_unassigned_current_month, ");

            sb.append("(select COALESCE(sum(i.interim_payment_made),0.0) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status = 'InvoiceUnassigned' ")
              .append(createdDateRestriction)
              .append(") as val_interim_payment_unassigned_current_month, ");
            
            sb.append("(select count(*) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status = 'ContestedInvoiceReferredToInsurer' ")
              .append(createdDateRestriction)
              .append(") as no_invoices_cho_dispute_current_month, ");

            sb.append("(select COALESCE(sum(i.total_gross),0.0) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status = 'ContestedInvoiceReferredToInsurer' ")
              .append(createdDateRestriction)
              .append(") as val_invoices_cho_dispute_current_month, ");


            sb.append("(select COALESCE(sum(i.interim_payment_made),0.0) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status = 'ContestedInvoiceReferredToInsurer' ")
              .append(createdDateRestriction)
              .append(") as val_interim_payment_cho_dispute_current_month, ");
            
            sb.append("(select count(*) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status = 'AwaitingInvoicePayment' ")
              .append(createdDateRestriction)
              .append(") as no_invoices_awaiting_payment_current_month, ");

            sb.append("(select COALESCE(sum(i.total_gross),0.0) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status = 'AwaitingInvoicePayment' ")
              .append(createdDateRestriction)
              .append(") as val_invoices_awaiting_payment_current_month, ");
            
            sb.append("(select COALESCE(sum(i.interim_payment_made),0.0) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status = 'AwaitingInvoicePayment' ")
              .append(createdDateRestriction)
              .append(") as val_interim_payment_awaiting_payment_current_month, ");

            sb.append("(select count(*) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status = 'ManualInvoicePaid' ")
              .append(createdDateRestriction)
              .append(") as no_manual_invoices_paid_current_month, ");

            sb.append("(select COALESCE(sum(i.total_gross),0.0) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status = 'ManualInvoicePaid' ")
              .append(createdDateRestriction)
              .append(") as val_manual_invoices_paid_current_month, ");

            sb.append("(select count(*) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status = 'ManualInvoiceUnassigned' ")
              .append(createdDateRestriction)
              .append( ")  as no_manual_invoices_in_to_be_assigned_current_month, ");
            
            sb.append("(select COALESCE(sum(i.total_gross),0.0) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status = 'ManualInvoiceUnassigned' ")
              .append(createdDateRestriction)
              .append( ")  as val_manual_invoices_in_to_be_assigned_current_month, ");
        
            sb.append("(select count(*) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status = 'ManualInvoiceBREApproved' ")
              .append(createdDateRestriction)
              .append(") as no_manual_invoices_approved_current_month, ");

            sb.append("(select COALESCE(sum(i.total_gross),0.0) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status = 'ManualInvoiceBREApproved' ")
              .append(createdDateRestriction)
              .append(") as val_manual_invoices_approved_current_month, ");

            sb.append("(select count(*) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status = 'ManualInvoiceBRERejected' ")
              .append(createdDateRestriction)
              .append(") as no_manual_invoices_rejected_current_month, ");

            sb.append("(select COALESCE(sum(i.total_gross),0.0) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status = 'ManualInvoiceBRERejected' ")
              .append(createdDateRestriction)
              .append(") as val_manual_invoices_rejected_current_month, ");
            
            sb.append("(select count(*) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status = 'ManualInvoiceContested' ")
              .append(createdDateRestriction)
              .append(") as no_manual_invoices_contested_current_month, ");

            sb.append("(select COALESCE(sum(i.total_gross),0.0) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status = 'ManualInvoiceContested' ")
              .append(createdDateRestriction)
              .append(") as val_manual_invoices_contested_current_month, ");
            
            sb.append("(select count(*) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status in ('AwaitingLitigationOutcome') ")
              .append(createdDateRestriction)
              .append(") as no_invoices_in_litigation_status_current_month, ");

            sb.append("(select COALESCE(sum(i.total_gross),0.0) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status in ('AwaitingLitigationOutcome') ")
              .append(createdDateRestriction)
              .append(") as val_invoices_in_litigation_status_current_month, ");

            sb.append("(select COALESCE(sum(i.interim_payment_made),0.0) from claim c, invoice i ")
              .append( "where c.invoice_id = i.id ")
              .append( "and (c.insurer_id = :pInsurerId or :pInsurerId < 0) ")
              .append( "and (c.chorganisation_id = :pChorgId or :pChorgId < 0) ")
              .append( "and c.status in ('AwaitingLitigationOutcome') ")
              .append(createdDateRestriction)
              .append(") as val_interim_payment_invoices_in_litigation_status_current_month ");

            String query = sb.toString();
            LOG.debug(query);

            Map paramMap = new HashMap();
            paramMap.put("pStartDate", dataStart);
            paramMap.put("pChorgId", choId);
            paramMap.put("pInsurerId", insurerId);

            List result = reportDataService.getReportData(query, paramMap);

            for (Object o : result) {
                Map data = (Map) o;
                InvoiceStatusReportViewData invoiceStatusReportData = InvoiceStatusReportViewData.getObject(data);
                invoiceStatusReport.add(invoiceStatusReportData);
            }
        }
        } catch (Exception ex) {
            LOG.error("Exception thrown generating Invoice Status Report: {} [user={}]", ex.getMessage(), currentUser.getId());
            LOG.error("Report params were: startDate={}", dataStart);
            throw ex;
        }

        map.put("invoiceStatusReportCummulative", invoiceStatusReportDataCumm);
        map.put("invoiceStatusReport", invoiceStatusReport);
        map.put("dataStart", dataStart.toString());
        map.put("createDate", createDate.toString());
        map.put("userOrgLabel", userOrgLabel);
        map.put("userOrgName", userOrgName);
        map.put("selectedOrgName", selectedOrgName);
        map.put("selectedOrgLabel", selectedOrgLabel);
        map.put("reportColumnHeader", reportColumnHeader);

        return map;
    }

    
    @Override
    public String getReportTemplateFileName() {
        return "template_InvoiceStatusReport.xls";
    }

    
    @Override
    public ByteArrayOutputStream build() throws Exception {
        ReportBuilder builder = new ExcelReportBuilder();
        return builder.buildReport(this);
    }

    
    @Override
    public String getReportCode() {
        return "RPT025";
    }
    
    @Override
    public short[] getColumnsToHide() {
        return null;
    }
}
