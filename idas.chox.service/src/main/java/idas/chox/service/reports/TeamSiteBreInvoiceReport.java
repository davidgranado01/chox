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
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.ReasonOfRejection;
import idas.chox.core.model.WebUser;
import idas.chox.core.services.ReportDataService;
import idas.chox.core.util.DateHelper;
import idas.chox.core.util.MathHelper;
import idas.chox.core.util.RoleHelper;
import idas.chox.core.util.TextHelper;
import idas.chox.data.services.BaseDataService;
import idas.chox.service.reports.viewdata.TeamSiteBreInvoiceLineItem;
import idas.chox.service.reports.viewdata.TeamSiteBreInvoiceReportObject;

/**
 *
 * @author rajareddydodda
 */
public class TeamSiteBreInvoiceReport implements Report {

    private static final Logger LOG = LoggerFactory.getLogger(TeamSiteBreInvoiceReport.class);
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

    @Override
    public Map<String, Object> getReportParameters() throws Exception {
        Map<String, Object> reportParameters = new HashMap<>();

        try {
            String supplierId;
            Integer selectedCHOId = -1;
            String selectedCHOName = "All";
            Integer insurerId = -1;
            String selectedSite = "";
            String selectedTeam = "";
            String rptInsurerName = "";
            Date startDate = null;
            Date endDate = null;

            user = ((WebUser) externalParameter.get("CurrentUser"));
            List<ReasonOfRejection> reasonsOfRejection = getReasonsOfRejection(user, false);
            // GET INSURER INFORMATION
            if (RoleHelper.isInsurerUser(user)) {
                insurerId = user.getInsurer().getId();
                rptInsurerName = user.getInsurer().getName();
            }
            LOG.debug("rptInsurerName={}", rptInsurerName);

            if ((externalParameter.get("supplierId")) != null) {
                supplierId = ((String[]) externalParameter.get("supplierId"))[0];
                LOG.debug("Team site bre invoice report supplieriD ={}", supplierId);
                if (!supplierId.equalsIgnoreCase("-1") && !supplierId.equalsIgnoreCase("") && !supplierId.equalsIgnoreCase("--- ALL ---")) {
                    selectedCHOId = TextHelper.getId(supplierId);
                    LOG.debug("Team site bre invoice report selectedChoId ={}", selectedCHOId);
                    selectedCHOName = getChorganisation(selectedCHOId).getName();
                }
            }

            if (((String[]) externalParameter.get("site")) != null) {
                selectedSite = ((String[]) externalParameter.get("site"))[0];
                if (selectedSite.equalsIgnoreCase("--- ALL ---") || selectedSite.equalsIgnoreCase("") || selectedSite.equalsIgnoreCase("-1")) {
                    selectedSite = null;
                }
            }

            if (((String[]) externalParameter.get("team")) != null) {
                selectedTeam = ((String[]) externalParameter.get("team"))[0];
                if (selectedTeam.equalsIgnoreCase("--- ALL ---") || selectedTeam.equalsIgnoreCase("") || selectedTeam.equalsIgnoreCase("-1")) {
                    selectedTeam = null;
                }
            }
            LOG.debug("selectedSite={}, selectedTeam={}", selectedSite, selectedTeam);

            if (((String[]) externalParameter.get("startDate")) != null) {
                startDate = DateHelper.parse(((String[]) externalParameter.get("startDate"))[0]);
                LOG.debug("startDate={}", startDate.toString());
            }

            if (((String[]) externalParameter.get("endDate")) != null) {
                endDate = DateHelper.parse(((String[]) externalParameter.get("endDate"))[0]);
                endDate = DateHelper.setEndOfDay(endDate);
                LOG.debug("endDate={}", endDate.toString());
            }

            if (endDate == null || startDate == null) {
                throw new Exception("Start and End dates cannot be empty.");
            }

            if (endDate.before(startDate)) {
                throw new Exception("End date (" + endDate.toString() + ") is before start date (" + startDate.toString() +  ") ");
            }

            List<TeamSiteBreInvoiceReportObject> teamReportObjects = new ArrayList<>();
            HashMap queryParameters = new HashMap();
            queryParameters.put("pInsurerId", insurerId);
            StringBuilder sb = new StringBuilder();
            sb.append("select distinct site from workgroup where insurer_id = :pInsurerId and status = true ");
            if (selectedSite != null) {
                sb.append("and site = :pSite ");
                queryParameters.put("pSite", selectedSite);
            }
            if (selectedTeam != null) {
                queryParameters.put("pTeam", selectedTeam);
                sb.append("and team = :pTeam ");
            }
            sb.append("order by site");
            List result = reportDataService.getReportData(sb.toString(), queryParameters);
            for (Object o : result) {
                Map data = (Map) o;
                TeamSiteBreInvoiceReportObject teamReportObject = new TeamSiteBreInvoiceReportObject();
                teamReportObject.setSite(data.get("site").toString());
                teamReportObjects.add(teamReportObject);
                LOG.debug("Site added: {}", teamReportObject.getSite());
            }

            for (TeamSiteBreInvoiceReportObject obj : teamReportObjects) {
                LOG.debug("Getting teams of site: {}", obj.getSite());
                queryParameters = new HashMap();
                sb = new StringBuilder();
                queryParameters.put("pSite", obj.getSite());
                queryParameters.put("pInsurerId", insurerId);
                sb.append("select distinct site, team from workgroup where site = :pSite and insurer_id = :pInsurerId and status = true ");
                if (selectedTeam != null && selectedTeam.length() > 0) {
                    queryParameters.put("pTeam", selectedTeam);
                    sb.append("and team = :pTeam ");
                }
                sb.append("order by team");
                result = reportDataService.getReportData(sb.toString(), queryParameters);
                boolean first = true;
                if (result.isEmpty()) {
                    teamReportObjects.remove(obj);
                } else {
                    for (Object o : result) {
                        Map data = (Map) o;
                        if (!first) {
                            data.remove("site");
                        } else {
                            first = false;
                        }
                        TeamSiteBreInvoiceLineItem workflowLineItem = TeamSiteBreInvoiceLineItem.getObject(data);
                        LOG.debug("Getting stats for site='{}', team='{}'", workflowLineItem.getSite(), workflowLineItem.getTeam());
                        // Now construct query to get team stats
                        sb = new StringBuilder();
                        sb.append("select ");


                        /*
                         * No of Invoices uploaded
                         */
                        sb.append("(select count(*) from claim c, invoice i, workgroup w ")
                                .append("where c.invoice_id=i.id and c.workgroup_id=w.id and c.claim_type not in ")
                                .append(ClaimType.getInsurerUploadTypeOrdinals())
                                .append(" and w.status=true and w.site = :pSite ")
                                .append("and w.team = :pTeam and w.insurer_id = :pInsurerId ");
                        if (selectedCHOId > 0) {
                            sb.append("and c.chorganisation_id = :pChoId ");
                        }
                        sb.append("and i.created_date between :pStartDate and  :pEndDate ) as no_invoices_uploaded, ");


                        /*
                         * No of invoices Approved by BRE
                         *
                         */
                        sb.append("(select count(*) from claim c, invoice i, workgroup w, audit_trail a ")
                                .append("where c.invoice_id=i.id and c.workgroup_id=w.id ")
                                .append("and a.reverted=false and a.claim_id=c.id and w.status=true ")
                                .append("and w.site = :pSite and w.team = :pTeam ")
                                .append("and w.insurer_id = :pInsurerId ")
                                .append("and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect', 'InvoiceUnassigned') ")
                                .append("and a.new_status='InvoiceApprovedByBRE' ");
                        if (selectedCHOId > 0) {
                            sb.append("and c.chorganisation_id = :pChoId ");
                        }
                        sb.append("and i.created_date between :pStartDate and  :pEndDate ) as no_invoices_approved_bre, ");

                        /*
                         * No of Invoices approved by BRE and Then Disputed
                         *
                         */
                        sb.append("(select count(distinct b.id) from (select c.id from claim c, invoice i, workgroup w, audit_trail a ")
                                .append("where c.invoice_id=i.id and c.workgroup_id=w.id ")
                                .append("and a.reverted=false and a.claim_id=c.id and w.status=true ")
                                .append("and w.site = :pSite and w.team = :pTeam ");
                        if (selectedCHOId > 0) {
                            sb.append("and c.chorganisation_id = :pChoId ");
                        }
                        sb.append("and w.insurer_id = :pInsurerId ")
                                .append("and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect', 'InvoiceUnassigned') ")
                                .append("and a.new_status='InvoiceApprovedByBRE' ")
                                .append("and i.created_date between :pStartDate and  :pEndDate ) b, audit_trail a ")
                                .append("where b.id=a.claim_id and a.reverted=false and a.new_status = 'ContestedInvoiceReferredToCHO' ) as no_invoices_approved_bre_disputed, ");

                        /*
                         *
                         * % of Invoices Approves By Business Rules Engine And  Not Disputed  And Paid Within 15 Days
                         *
                         */
                        sb.append("(select count(*) from (select c.id, i.created_date from claim c, invoice i, workgroup w, audit_trail a ")
                                .append("where c.invoice_id=i.id and c.workgroup_id=w.id ")
                                .append("and a.reverted=false and a.claim_id=c.id and w.status=true ")
                                .append("and w.site = :pSite and w.team = :pTeam ");
                        if (selectedCHOId > 0) {
                            sb.append("and c.chorganisation_id = :pChoId ");
                        }
                        sb.append("and w.insurer_id = :pInsurerId and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect', 'InvoiceUnassigned') ")
                                .append("and a.new_status='InvoiceApprovedByBRE' ")
                                .append("and i.created_date between :pStartDate and  :pEndDate ) b, audit_trail a ")
                                .append("where b.id =a.claim_id and a.reverted=false and a.new_status = 'InvoicePaymentLogged' ")
                                .append("and not exists ( select * from audit_trail a1 where a1.claim_id=b.id and a1.reverted=false and a1.new_status='ContestedInvoiceReferredToCHO') ")
                                .append("and a.update_date between b.created_date  and  b.created_date + interval '15 days' )as no_invoices_approved_bre_not_disputed_paid_15days, ");

                        /**
                         *
                         * % of Invoices Approved By Business Rules Engine And Not Disputed And Paid Within 30 Days
                         *
                         */
                        sb.append("(select count(*) from (select c.id, i.created_date from claim c, invoice i, workgroup w, audit_trail a ")
                                .append("where c.invoice_id=i.id and c.workgroup_id=w.id ")
                                .append("and a.claim_id=c.id and w.status=true ")
                                .append("and w.site = :pSite and w.team = :pTeam ")
                                .append("and w.insurer_id = :pInsurerId ");
                        if (selectedCHOId > 0) {
                            sb.append("and c.chorganisation_id = :pChoId ");
                        }
                        sb.append("and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect', 'InvoiceUnassigned') ")
                                .append("and a.reverted=false and a.new_status='InvoiceApprovedByBRE' ")
                                .append("and i.created_date between :pStartDate and  :pEndDate ) b, audit_trail a ")
                                .append("where b.id=a.claim_id and a.reverted=false and a.new_status = 'InvoicePaymentLogged' ")
                                .append("and not exists ( select * from audit_trail a1 where a1.claim_id=b.id and a1.reverted=false and a1.new_status='ContestedInvoiceReferredToCHO') ")
                                .append("and a.update_date between b.created_date  and  b.created_date + interval '30 days' )as no_invoices_approved_bre_not_disputed_paid_30days, ");


                        /*
                         * % of Invoices Approved  By Business Rules Engine And  Disputed And Paid  within 15 Days
                         *
                         */
                        sb.append("(select count(*) from (select c.id, i.created_date from claim c, invoice i, workgroup w, audit_trail a ")
                                .append("where c.invoice_id=i.id and c.workgroup_id=w.id ")
                                .append("and a.reverted=false and a.claim_id=c.id and w.status=true ")
                                .append("and w.site = :pSite and w.team = :pTeam ")
                                .append("and w.insurer_id = :pInsurerId ");
                        if (selectedCHOId > 0) {
                            sb.append("and c.chorganisation_id = :pChoId ");
                        }
                        sb.append("and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect', 'InvoiceUnassigned') ")
                                .append("and a.new_status='InvoiceApprovedByBRE' ")
                                .append("and i.created_date between :pStartDate and  :pEndDate ) b, audit_trail a ")
                                .append("where b.id=a.claim_id and a.reverted=false and a.new_status = 'InvoicePaymentLogged' ")
                                .append("and exists ( select * from audit_trail a1 where a1.claim_id=b.id and a1.reverted=false and a1.new_status='ContestedInvoiceReferredToCHO') ")
                                .append("and a.update_date between b.created_date  and  b.created_date + interval '15 days' )as no_invoices_approved_bre_disputed_paid_15days, ");


                        /**
                         * % of Invoices Approved By Business Rules Engine and Disputed And Paid Within30 Days
                         *
                         */
                        sb.append("(select count(*) from (select c.id, i.created_date from claim c, invoice i, workgroup w, audit_trail a ")
                                .append("where c.invoice_id=i.id and c.workgroup_id=w.id ")
                                .append("and a.claim_id=c.id and w.status=true ")
                                .append("and w.site = :pSite and w.team = :pTeam ")
                                .append("and w.insurer_id = :pInsurerId ");
                        if (selectedCHOId > 0) {
                            sb.append("and c.chorganisation_id = :pChoId ");
                        }
                        sb.append("and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect', 'InvoiceUnassigned') ")
                                .append("and a.reverted=false and a.new_status='InvoiceApprovedByBRE' ")
                                .append("and i.created_date between :pStartDate and  :pEndDate ) b, audit_trail a ")
                                .append("where b.id =a.claim_id and a.reverted=false and a.new_status = 'InvoicePaymentLogged' ")
                                .append("and exists ( select * from audit_trail a1 where a1.claim_id=b.id and a1.reverted=false and a1.new_status='ContestedInvoiceReferredToCHO') ")
                                .append("and a.update_date between b.created_date  and  b.created_date + interval '30 days' )as no_invoices_approved_bre_disputed_paid_30days, ");

                        for (ReasonOfRejection ror : reasonsOfRejection) {

                            sb.append("(select count(*) from (select c.id, c.invoice_id from claim c, invoice i, workgroup w, audit_trail a ")
                                    .append("where c.invoice_id=i.id and c.workgroup_id=w.id ")
                                    .append("and a.claim_id=c.id and w.status=true ")
                                    .append("and w.site = :pSite and w.team = :pTeam ")
                                    .append("and w.insurer_id = :pInsurerId ");
                            if (selectedCHOId > 0) {
                                sb.append("and c.chorganisation_id = :pChoId ");
                            }
                            sb.append("and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect', 'InvoiceUnassigned') ")
                                    .append("and a.reverted=false and a.new_status='InvoiceApprovedByBRE' ")
                                    .append("and i.created_date between :pStartDate and  :pEndDate ) b, audit_trail a, invoice i ")
                                    .append("where b.id =a.claim_id and a.new_status = 'ContestedInvoiceReferredToCHO' ")
                                    .append("and b.invoice_id = i.id and a.reverted=false and a.invoice_reason_of_rejection = ")
                                    .append(ror.getId())
                                    .append("and not exists (select * from audit_trail a2 where a2.claim_id=a.claim_id and a2.reverted=false and a2.new_status='ContestedInvoiceReferredToCHO'  and a2.update_date < a.update_date))as no_invoices_disputed_due_to_")
                                    .append(ror.getRorName());
                            if (reasonsOfRejection.indexOf(ror) != reasonsOfRejection.size() - 1) {
                                sb.append(", ");
                            }

                        }

                        queryParameters = new HashMap();
                        queryParameters.put("pInsurerId", insurerId);
                        queryParameters.put("pSite", obj.getSite());
                        queryParameters.put("pTeam", workflowLineItem.getTeam());
                        queryParameters.put("pStartDate", startDate);
                        queryParameters.put("pEndDate", endDate);
                        if (selectedCHOId > 0) {
                            queryParameters.put("pChoId", selectedCHOId);
                        }
                        LOG.debug("Query: {}", sb.toString());

                        List detailData = reportDataService.getReportData(sb.toString(), queryParameters);
                        // parse query results and add to workflowLineItem
                        if (detailData.size() > 0) {
                            workflowLineItem.updateObject((Map) detailData.get(0), reasonsOfRejection);
                            obj.getTeams().add(workflowLineItem);
                        }
                    }
                }
            }

            // Now build report parameters
            reportParameters.put("reasonsOfRejectionHeader", getReasonsOfRejection(user, true));
            reportParameters.put("reasonsOfRejection", reasonsOfRejection);
            reportParameters.put("insurerName", rptInsurerName);
            reportParameters.put("choName", selectedCHOName);
            reportParameters.put("createdDate", DateHelper.getCurrentDate());
            reportParameters.put("startDate", startDate);
            reportParameters.put("endDate", endDate);
            reportParameters.put("workflowLineItems", teamReportObjects);
        } catch (Exception ex) {
            LOG.error("Error generating Team Workflow report: {}", ex.getMessage());
            if (ex.getCause() != null) {
                LOG.error("Caused by: {}", ex.getCause().getMessage());
            }
            throw ex;
        }

        return reportParameters;
    }

    @Override
    public String getReportTemplateFileName() {
        return "template_SiteTeamBreInvoiceReport.xls";
    }

    @Override
    public ByteArrayOutputStream build() throws Exception {
        ReportBuilder builder = new ExcelReportBuilder();
        return builder.buildReport(this);
    }

    @Override
    public String getReportCode() {
        return "RPT031";
    }

    private Chorganisation getChorganisation(int orgId) {

        Chorganisation chorg = new Chorganisation();

        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Chorganisation.class);
            criteria.add(Restrictions.eq("id", orgId));
            chorg = (Chorganisation) baseDataService.getByCriteria(criteria);

        } catch (Exception ex) {
            LOG.error("Error generating Team/Site BRE Invoice Report: {}", ex.getMessage());
            if (ex.getCause() != null) {
                LOG.error("Caused by: {}", ex.getCause().getMessage());
            }
        }

        return chorg;
    }

    @Override
    public short[] getColumnsToHide() {
        return null;
    }

    private List<ReasonOfRejection> getReasonsOfRejection(WebUser currentUser, boolean displayInHeader) {
        List<ReasonOfRejection> reportRows = new ArrayList<>();
        List result;
        if (currentUser.getInsurer() != null) {
            String query = "select ror.id, ror.name from reason_of_rejection ror join invoice iv on ror.id = iv.reason_of_rejection_id "
                    + "where ror.type='Invoice Rejection' "
                    + "and ror.insurer_id = :insurerId group by ror.id "
                    + "union select id, name from reason_of_rejection where (gta_active = true or insurer_vs_insurer_active=true or subscriber_active=true or fixed_fee_active=true or insurer_upload_active = true or tpi_active= true) and type='Invoice Rejection' and insurer_id = :insurerId order by name asc ";
            Map paramMap = new HashMap();
            paramMap.put("insurerId", currentUser.getInsurer().getId());
            result = reportDataService.getReportData(query, paramMap);
        } else {
            String query = "select ror.id, ror.name from reason_of_rejection ror join invoice iv on ror.id = iv.reason_of_rejection_id "
                    + "where ror.type='Invoice Rejection' "
                    + "and ror.insurer_id in (select insurer_id from insurer_chorganisation where chorganisation_id = :choId)"
                    + " group by ror.id  "
                    + "union select id, name from reason_of_rejection where (gta_active = true or insurer_vs_insurer_active=true or subscriber_active=true or fixed_fee_active=true or insurer_upload_active = true or tpi_active= true) and type='Invoice Rejection' and insurer_id in "
                    + "(select insurer_id from insurer_chorganisation where chorganisation_id = :choId) order by name asc ";

            Map paramMap = new HashMap();
            paramMap.put("choId", currentUser.getChorganisation().getId());
            result = reportDataService.getReportData(query, paramMap);
        }

        for (Object o : result) {
            Map data = (Map) o;
            ReasonOfRejection reportRow = new ReasonOfRejection();
            reportRow.setId(MathHelper.getIntegerValue(data.get("id".toLowerCase())));
            if (!displayInHeader) {
                reportRow.setRorName(data.get("name").toString().replaceAll("\\s+", "_").replaceAll("[^A-Za-z0-9_]", ""));
            } else {
                reportRow.setRorName(data.get("name").toString());
            }
            reportRows.add(reportRow);
        }

        return reportRows;
    }

    @Override
    public boolean isBrandingReportFormat() {
        return externalParameter.get("isBrandingReport")==null ? false : (Boolean)externalParameter.get("isBrandingReport");
    }
}
