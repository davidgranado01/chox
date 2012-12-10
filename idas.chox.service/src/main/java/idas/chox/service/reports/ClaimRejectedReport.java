package idas.chox.service.reports;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.WebUser;
import idas.chox.core.services.ReportDataService;
import idas.chox.core.util.DateHelper;
import idas.chox.core.util.MathHelper;
import idas.chox.data.services.BaseDataService;
import idas.chox.service.reports.viewdata.ClaimRejectedReportObject;
import idas.chox.service.reports.viewdata.ClaimRejection;
import idas.chox.service.reports.viewdata.ClaimRejectionLineItem;
import idas.chox.service.reports.viewdata.ClaimRejectionLineItemDetail;

public class ClaimRejectedReport implements Report {
    private static final Logger LOG = LoggerFactory.getLogger(ClaimRejectedReport.class);

    private Map externalParameter;
    private List<String> reportParameterNames;
    private BaseDataService baseDataService;
    private ReportDataService reportDataService;

    @Override
    public void setBaseDataService(BaseDataService baseDataService) {
        this.baseDataService = baseDataService;
    }

    public ClaimRejectedReport() {
        reportParameterNames = new ArrayList<String>();
    }

    @Override
    public String getReportTemplateFileName() {
        return "template_ClaimRejectedReport.xls";
    }

    @Override
    public void setExternalParameter(Map parameters) {
        this.externalParameter = parameters;
    }

    @Override
    public HashMap getReportParameters() {

        HashMap reportParameters = new HashMap();

        WebUser currentUser = ((WebUser) externalParameter.get("CurrentUser"));

        try {

            List<ClaimRejectionLineItem> reportRows = getReasonOfRejection(currentUser);

            Date dataStart = null;
            Date dataEnd = null;

            if (((String[]) externalParameter.get("DateStart")) != null) {
                dataStart = DateHelper.Parse(((String[]) externalParameter.get("DateStart"))[0]);
            }

            if (((String[]) externalParameter.get("DateStart")) != null) {
                dataEnd = DateHelper.Parse(((String[]) externalParameter.get("DateEnd"))[0]);
                dataEnd = DateHelper.setEndOfDay(dataEnd);
            }
            
            if(dataEnd != null && dataStart != null && dataEnd.before(dataStart)){
                throw new Exception("End date (" + dataEnd.toString() + ") is before start date (" + dataStart.toString() +  ") ");
            }

            boolean isInsReport = (currentUser.getInsurer() != null);

            String sOrganisationLabel;
            String sOrganisationName;

            ClaimRejection claimRejection = new ClaimRejection();
            claimRejection.setClaimRejectionLineItem(reportRows);

            Integer iOrgId;

            if (isInsReport) {
                Insurer ins = currentUser.getInsurer();
                iOrgId = ins.getId();
                sOrganisationLabel = "Insurer";
                sOrganisationName = ins.getName();

            } else {
                Chorganisation cho = currentUser.getChorganisation();
                iOrgId = cho.getId();
                sOrganisationLabel = "Credit Hire";
                sOrganisationName = cho.getName();
            }

            claimRejection = getReportLineResult(isInsReport, iOrgId, claimRejection, dataStart, dataEnd);

            ClaimRejectedReportObject reportObject = new ClaimRejectedReportObject();
            reportObject.setDateFrom(dataStart);
            reportObject.setDateTo(dataEnd);
            reportObject.setCreatedDate(new Date());

            reportParameters.put("reportHeaderName", claimRejection.getOrgName());
            reportParameters.put("reportRows", reportRows);
            reportParameters.put("reportObj", reportObject);
            reportParameters.put("organisationLabel", sOrganisationLabel);
            reportParameters.put("organisationName", sOrganisationName);

        } catch (Exception ex) {
            LOG.error("Exception thrown: {}", ex.getMessage());
        }

        return reportParameters;
    }

    
    private ClaimRejection getReportLineResult(boolean isIns, Integer iOrgId, ClaimRejection claimRejection, Date dataStart, Date dataEnd) {

        claimRejection = getReportHeader(isIns, iOrgId, dataStart, dataEnd, claimRejection);
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append("(select count(*) from claim where (date_trunc('day', claim.created_date) between :pCreatedDateFrom and :pCreatedDateTo) and claim.insurer_id=insurer_chorganisation.insurer_id and claim.chorganisation_id=insurer_chorganisation.chorganisation_id and (claim.claim_type not in ").append(ClaimType.getSupplementaryInvoiceTypeOrdinals()).append(")) as iTotal, ");
        sb.append("(select count(*) from claim, audit_trail a where claim.id=a.claim_id and a.reverted=false and (a.new_status='ClaimRejectionAccepted' or (a.new_status = 'AwaitingCarHireInfo' and a.original_status='SubscriberClaimRejected')) and (date_trunc('day', claim.created_date) between :pCreatedDateFrom and :pCreatedDateTo) and claim.insurer_id=insurer_chorganisation.insurer_id and claim.chorganisation_id=insurer_chorganisation.chorganisation_id) as iTotalRejected, ");

            for (ClaimRejectionLineItem cRejected : claimRejection.getClaimRejectionLineItem()) {
                if (cRejected.getId() != null) {
                    if(isIns){
                        sb.append("(select count(*) from claim, audit_trail a where claim.id=a.claim_id and a.reverted=false and (a.new_status='ClaimRejectionAccepted' or (a.new_status = 'AwaitingCarHireInfo' and a.original_status='SubscriberClaimRejected')) and a.claim_reason_of_rejection=")
                        .append(cRejected.getId()).append(" and (date_trunc('day', claim.created_date) between :pCreatedDateFrom and :pCreatedDateTo) and claim.insurer_id=insurer_chorganisation.insurer_id and claim.chorganisation_id=insurer_chorganisation.chorganisation_id) as REJ_")
                        .append(cRejected.getId()).append(", ");
                        sb.append("(select count(*) from claim, audit_trail a where claim.id=a.claim_id and a.reverted=false and (a.new_status='ClaimRejectionAccepted' or (a.new_status = 'AwaitingCarHireInfo' and a.original_status='SubscriberClaimRejected')) and a.claim_reason_of_rejection=")
                        .append(cRejected.getId()).append(" and (date_trunc('day', claim.created_date) between :pCreatedDateFrom and :pCreatedDateTo) and claim.insurer_id=insurer_chorganisation.insurer_id) as REJ_PERC_")
                        .append(cRejected.getId()).append(", ");
                    } else {
                        sb.append("(select count(*) from claim, audit_trail a where claim.id=a.claim_id and a.reverted=false and (a.new_status='ClaimRejectionAccepted' or (a.new_status = 'AwaitingCarHireInfo' and a.original_status='SubscriberClaimRejected')) and a.claim_reason_of_rejection in ")
                        .append("(select id from reason_of_rejection where name = '").append(cRejected.getName()).append("' and insurer_id=insurer.id) and (date_trunc('day', claim.created_date) between :pCreatedDateFrom and :pCreatedDateTo) and claim.insurer_id=insurer_chorganisation.insurer_id and claim.chorganisation_id=insurer_chorganisation.chorganisation_id) as REJ_")
                        .append(cRejected.getId()).append(", ");
                        sb.append("(select count(*) from claim, audit_trail a where claim.id=a.claim_id and a.reverted=false and (a.new_status='ClaimRejectionAccepted' or (a.new_status = 'AwaitingCarHireInfo' and a.original_status='SubscriberClaimRejected')) and a.claim_reason_of_rejection in")
                        .append("(select id from reason_of_rejection where name = '").append(cRejected.getName()).append("' and insurer_id=insurer.id) and (date_trunc('day', claim.created_date) between :pCreatedDateFrom and :pCreatedDateTo) and claim.chorganisation_id=insurer_chorganisation.chorganisation_id) as REJ_PERC_")
                        .append(cRejected.getId()).append(", ");
                    }    
                }
            }

        if (isIns) {
            sb.append("chorganisation.id, chorganisation.name ");
            sb.append("from insurer_chorganisation insurer_chorganisation ");
            sb.append("inner join chorganisation chorganisation on chorganisation.id=insurer_chorganisation.chorganisation_id ");
            sb.append("where insurer_chorganisation.insurer_id=:pOrgId and chorganisation.insurer_upload_only=false ");
            sb.append("order by chorganisation.name asc ");
        } else {
            sb.append("insurer.id, insurer.name ");
            sb.append("from insurer_chorganisation insurer_chorganisation ");
            sb.append("inner join insurer insurer on insurer.id=insurer_chorganisation.insurer_id ");
            sb.append("where insurer_chorganisation.chorganisation_id=:pOrgId ");
            sb.append("order by insurer.name asc ");
        }

        String query = sb.toString();
        LOG.debug("Report query for claim rejection line items is:\n {}", query);
        
        Map paramMap = new HashMap();
        paramMap.put("pOrgId", iOrgId);
        paramMap.put("pCreatedDateFrom", dataStart);
        paramMap.put("pCreatedDateTo", dataEnd);

        List result = reportDataService.getReportData(query, paramMap);

        for (Object o : result) {
            Map data = (Map) o;
            Integer iTotalClaimRejected = MathHelper.getIntegerValue(data.get("iTotalRejected".toLowerCase()));
            for (ClaimRejectionLineItem cRejected : claimRejection.getClaimRejectionLineItem()) {
                if (cRejected.getId() != null) {
                    String keyName = ("REJ_" + cRejected.getId()).toLowerCase();
                    ClaimRejectionLineItemDetail reportColumn = new ClaimRejectionLineItemDetail();
                    reportColumn.setNumberOfClaim(MathHelper.getIntegerValue(data.get(keyName)));
                    reportColumn.setNumberOfClaimPercentage(MathHelper.getPercentage(reportColumn.getNumberOfClaim(), iTotalClaimRejected));
                    cRejected.getReportColumns().add(reportColumn);
            LOG.debug("Added column for '{}': Number of claims={}, %={}", new Object[]{keyName, reportColumn.getNumberOfClaim(), reportColumn.getNumberOfClaimPercentage()});
                }
            }
        }
        claimRejection = getAllOrgCount(claimRejection);
        return claimRejection;
    }

    private ClaimRejection getAllOrgCount(ClaimRejection claimRejection) {

        Integer AllRejectedClaims = claimRejection.getClaimRejectionLineItem().get(1).getAllOrgClaimCount();

        for (ClaimRejectionLineItem cRejected : claimRejection.getClaimRejectionLineItem()) {

            if (cRejected.getId() != null) {
                Integer iAllOrgClaimCount = 0;

                for (ClaimRejectionLineItemDetail cRejectedDtl : cRejected.getReportColumns()) {
                    iAllOrgClaimCount = iAllOrgClaimCount + cRejectedDtl.getNumberOfClaim();
                }

                cRejected.setAllOrgClaimCount(iAllOrgClaimCount);
                cRejected.setAllOrgClaimCountPerc(MathHelper.getPercentage(iAllOrgClaimCount.floatValue(), AllRejectedClaims.floatValue()));
            }
        }

        return claimRejection;
    }

    
    public ClaimRejection getReportHeader(boolean isInsReport, Integer iOrgId, Date dataStart, Date dataEnd, ClaimRejection claimRejection) {
        // INDEX 0
        ClaimRejectionLineItem reportRowAll = new ClaimRejectionLineItem();
        reportRowAll.setId(null);
        reportRowAll.setName("Total No. Claims.");
        reportRowAll.setDisplayName("Total No. Claims.");

        // INDEX 1
        ClaimRejectionLineItem reportRowRejected = new ClaimRejectionLineItem();
        reportRowRejected.setId(null);
        reportRowRejected.setName("Total No. Rejected Claims");
        reportRowRejected.setDisplayName("Total No. Rejected Claims");

        List<String> orgNames = new ArrayList<String>();

        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append("(select count(*) from claim where (date_trunc('day', claim.created_date) between :pCreatedDateFrom and :pCreatedDateTo) and claim.insurer_id=insurer_chorganisation.insurer_id and claim.chorganisation_id=insurer_chorganisation.chorganisation_id and (claim_type not in ").append(ClaimType.getSupplementaryInvoiceTypeOrdinals()).append(")) as iTotal, ");
        sb.append("(select count(*) from claim, audit_trail a where claim.id=a.claim_id and a.reverted=false and (a.new_status='ClaimRejectionAccepted' or (a.new_status = 'AwaitingCarHireInfo' and a.original_status='SubscriberClaimRejected')) and (date_trunc('day', claim.created_date) between :pCreatedDateFrom and :pCreatedDateTo) and claim.insurer_id=insurer_chorganisation.insurer_id and claim.chorganisation_id=insurer_chorganisation.chorganisation_id) as iTotalRejected, ");

        if (isInsReport) {

            sb.append("chorganisation.id, chorganisation.name ");
            sb.append("from insurer_chorganisation insurer_chorganisation ");
            sb.append("inner join chorganisation chorganisation on chorganisation.id=insurer_chorganisation.chorganisation_id ");
            sb.append("where insurer_chorganisation.insurer_id=:pOrgId and chorganisation.insurer_upload_only=false ");
            sb.append("order by chorganisation.name asc ");

        } else {

            sb.append("insurer.id, insurer.name ");
            sb.append("from insurer_chorganisation insurer_chorganisation ");
            sb.append("inner join insurer insurer on insurer.id=insurer_chorganisation.insurer_id ");
            sb.append("where insurer_chorganisation.chorganisation_id=:pOrgId ");
            sb.append("order by insurer.name asc ");

        }

        String query = sb.toString();

        //Emmanuel
        //27-07-2009
        //prevent SQL Injection
        Map paramMap = new HashMap();
        paramMap.put("pOrgId", iOrgId);
        paramMap.put("pCreatedDateFrom", dataStart);
        paramMap.put("pCreatedDateTo", dataEnd);

        List result = reportDataService.getReportData(query, paramMap);

        Integer iClaimTotalCount = 0;
        Integer iClaimRejectedTotalCount = 0;

        for (Object o : result) {

            Map data = (Map) o;

            iClaimTotalCount += MathHelper.getIntegerValue(data.get("iTotal".toLowerCase()));
            iClaimRejectedTotalCount += MathHelper.getIntegerValue(data.get("iTotalRejected".toLowerCase()));
            orgNames.add(data.get("name").toString());

            ClaimRejectionLineItemDetail ReportColumnClaim = new ClaimRejectionLineItemDetail();
            ReportColumnClaim.setNumberOfClaim(MathHelper.getIntegerValue(data.get("iTotal".toLowerCase())));
            reportRowAll.getReportColumns().add(ReportColumnClaim);

            ClaimRejectionLineItemDetail ReportColumnRejClaim = new ClaimRejectionLineItemDetail();
            ReportColumnRejClaim.setNumberOfClaim(MathHelper.getIntegerValue(data.get("iTotalRejected".toLowerCase())));
            ReportColumnRejClaim.setNumberOfClaimPercentage(MathHelper.getPercentage(ReportColumnRejClaim.getNumberOfClaim(), ReportColumnClaim.getNumberOfClaim()));
            reportRowRejected.getReportColumns().add(ReportColumnRejClaim);
            LOG.debug("Added column for '{}': Number of claims={}, Number of rejected claims={}", new Object[] {data.get("name").toString(), ReportColumnClaim.getNumberOfClaim(), ReportColumnRejClaim.getNumberOfClaim()});
        }

        reportRowAll.setAllOrgClaimCount(iClaimTotalCount);

        reportRowRejected.setAllOrgClaimCount(iClaimRejectedTotalCount);
        reportRowRejected.setAllOrgClaimCountPerc(MathHelper.getPercentage(iClaimRejectedTotalCount.floatValue(), iClaimTotalCount.floatValue()));

        claimRejection.setOrgName(orgNames);
        claimRejection.getClaimRejectionLineItem().add(0, reportRowAll);
        claimRejection.getClaimRejectionLineItem().add(1, reportRowRejected);
        return claimRejection;
    }

    
    private List<ClaimRejectionLineItem> getReasonOfRejection(WebUser currentUser) {

        List<ClaimRejectionLineItem> reportRows = new ArrayList<ClaimRejectionLineItem>();
        List result;
        if(currentUser.getInsurer() != null){
            
            String query = "select ror.id, ror.name from reason_of_rejection ror " +
                    "join claim cl on ror.id = cl.reason_of_rejection_id " +
                    "where ror.type='Claim' and ror.insurer_id = :insurerId  " +
                    "or ((ror.gta_active = true or ror.insurer_vs_insurer_active=true or ror.subscriber_active=true or ror.fixed_fee_active=true or ror.insurer_upload_active = true or ror.tpi_active= true) and ror.type='Claim' and ror.insurer_id = :insurerId) " +
                    "group by ror.id " +
                    "union select id, name from reason_of_rejection where (gta_active = true or insurer_vs_insurer_active=true or subscriber_active=true or fixed_fee_active=true or insurer_upload_active = true or tpi_active= true) and type='Claim' and insurer_id = :insurerId order by name asc ";
            
            Map paramMap = new HashMap();
            paramMap.put("insurerId", currentUser.getInsurer().getId());
            result = reportDataService.getReportData(query, paramMap);
        } else {
            String query = "select ror.name from reason_of_rejection ror " +
                    "join claim cl on ror.id = cl.reason_of_rejection_id " +
                    "where ror.type='Claim' and ror.insurer_id in " +
                    "(select insurer_id from insurer_chorganisation where chorganisation_id = :choId) " +
                    "or ((ror.gta_active = true or ror.insurer_vs_insurer_active=true or ror.subscriber_active=true or ror.fixed_fee_active=true or ror.insurer_upload_active = true or ror.tpi_active= true) and ror.type='Claim' and ror.insurer_id in " +
                    "(select insurer_id from insurer_chorganisation where chorganisation_id = :choId)) " +
                    "group by ror.id " +
                    "union select name from reason_of_rejection where (gta_active = true or insurer_vs_insurer_active=true or subscriber_active=true or fixed_fee_active=true or insurer_upload_active = true or tpi_active= true) and type='Claim' and insurer_id in " +
                    "(select insurer_id from insurer_chorganisation where chorganisation_id = :choId) order by name asc ";
            
            Map paramMap = new HashMap();
            paramMap.put("choId", currentUser.getChorganisation().getId());
            result = reportDataService.getReportData(query, paramMap);
        }

        int i = 0;
        for (Object o : result) {
            Map data = (Map) o;
            ClaimRejectionLineItem reportRow = new ClaimRejectionLineItem();
            if(currentUser.getInsurer() != null) { 
                reportRow.setId(MathHelper.getIntegerValue(data.get("id".toLowerCase())));
            } else {
                reportRow.setId(i++); // in case of cho we need only unique reason of rejection name
            }
            reportRow.setName(data.get("name").toString());
            reportRow.setDisplayName(ClaimRejectionLineItem.getDisplayNameMap(data.get("name").toString()));
            reportRows.add(reportRow);
        }

        return reportRows;

    }

    
    @Override
    public ByteArrayOutputStream build() {
        ReportBuilder builder = getReportBuilder();
        return builder.buildReport(this);
    }

    
    protected ReportBuilder getReportBuilder() {
        return new ExcelReportBuilder();
    }

    @Override
    public void setReportDataService(ReportDataService reportDataService) {
        this.reportDataService = reportDataService;
    }
    
    @Override
    public String getReportCode() {
        return "RPT003";
    }
    
    @Override
    public short[] getColumnsToHide() {
        return null;
    }
}
