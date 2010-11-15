/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.reports;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.WebUser;
import idas.chox.core.util.DateHelper;
import idas.chox.core.util.MathHelper;
import idas.chox.data.services.BaseDataService;
import idas.chox.service.reports.viewdata.ClaimRejectedReportObject;
import idas.chox.service.reports.viewdata.ClaimRejection;
import idas.chox.service.reports.viewdata.ClaimRejectionLineItem;
import idas.chox.service.reports.viewdata.ClaimRejectionLineItemDetail;

public class ClaimRejectedReport implements Report {
    private static final Logger LOG = LoggerFactory.getLogger(ClaimRejectedReport.class);

    Map externalParameter;
    List<String> reportParameterNames;
    private BaseDataService baseDataService;

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
        // PermissionedUser currentUser = ((PermissionedUser) externalParameter.get("CurrentUser"));

        try {

            List<ClaimRejectionLineItem> reportRows = getReasonOfRejection();

            Date dataStart = null;
            Date dataEnd = null;

            if (((String[]) externalParameter.get("DateStart")) != null) {
                dataStart = DateHelper.Parse(((String[]) externalParameter.get("DateStart"))[0]);
            }

            if (((String[]) externalParameter.get("DateStart")) != null) {
                dataEnd = DateHelper.Parse(((String[]) externalParameter.get("DateEnd"))[0]);
            }

            boolean isInsReport = (currentUser.getInsurer() != null);

            String sOrganisationLabel = "";
            String sOrganisationName = "";

            ClaimRejection claimRejection = new ClaimRejection();
            claimRejection.setClaimRejectionLineItem(reportRows);

            Integer iOrgId = null;

            if (isInsReport) {

                Insurer ins = currentUser.getInsurer();
                iOrgId = ins.getId();
                sOrganisationLabel = "Insurer";
                sOrganisationName = ins.getName();

            } else {
                isInsReport = false;

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
            ex.printStackTrace();
        }

        return reportParameters;
    }

    private ClaimRejection getReportLineResult(boolean isIns, Integer iOrgId, ClaimRejection claimRejection, Date dataStart, Date dataEnd) {

        claimRejection = getReportHeader(isIns, iOrgId, dataStart, dataEnd, claimRejection);

        StringBuffer sb = new StringBuffer();
        sb.append("select ");
        sb.append("(select count(*) from claim claim where (date_trunc('day', claim.created_date) between :pCreatedDateFrom and :pCreatedDateTo) and claim.insurer_id=insurer_chorganisation.insurer_id and claim.chorganisation_id=insurer_chorganisation.chorganisation_id) as iTotal, ");
        sb.append("(select count(*) from claim claim left outer join (select * from audit_trail where new_status='ClaimRejectionAccepted') audit on claim.id=audit.claim_id where claim.status='ClaimRejectionAccepted' and (date_trunc('day', claim.created_date) between :pCreatedDateFrom and :pCreatedDateTo) and claim.insurer_id=insurer_chorganisation.insurer_id and claim.chorganisation_id=insurer_chorganisation.chorganisation_id) as iTotalRejected, ");

        for (ClaimRejectionLineItem cRejected : claimRejection.getClaimRejectionLineItem()) {

            if (cRejected.getId() != null) {
                sb.append("(select count(*) from claim claim left outer join (select * from audit_trail where new_status='ClaimRejectionAccepted') audit on claim.id=audit.claim_id where claim.status='ClaimRejectionAccepted' and audit.claim_reason_of_rejection=" + cRejected.getId() + " and (date_trunc('day', claim.created_date) between :pCreatedDateFrom and :pCreatedDateTo) and claim.insurer_id=insurer_chorganisation.insurer_id and claim.chorganisation_id=insurer_chorganisation.chorganisation_id) as REJ_" + cRejected.getId() + ", ");

                if (isIns) {
                    sb.append("(select count(*) from claim claim left outer join (select * from audit_trail where new_status='ClaimRejectionAccepted') audit on claim.id=audit.claim_id where claim.status='ClaimRejectionAccepted' and audit.claim_reason_of_rejection=" + cRejected.getId() + " and (date_trunc('day', claim.created_date) between :pCreatedDateFrom and :pCreatedDateTo) and claim.insurer_id=insurer_chorganisation.insurer_id) as REJ_PERC_" + cRejected.getId() + ", ");
                } else {
                    sb.append("(select count(*) from claim claim left outer join (select * from audit_trail where new_status='ClaimRejectionAccepted') audit on claim.id=audit.claim_id where claim.status='ClaimRejectionAccepted' and audit.claim_reason_of_rejection=" + cRejected.getId() + " and (date_trunc('day', claim.created_date) between :pCreatedDateFrom and :pCreatedDateTo) and claim.chorganisation_id=insurer_chorganisation.chorganisation_id) as REJ_PERC_" + cRejected.getId() + ", ");
                }
            }
        }

        if (isIns) {
            sb.append("chorganisation.id, chorganisation.name ");
            sb.append("from insurer_chorganisation insurer_chorganisation ");
            sb.append("inner join chorganisation chorganisation on chorganisation.id=insurer_chorganisation.chorganisation_id ");
            sb.append("where insurer_chorganisation.insurer_id=:pOrgId ");
            sb.append("order by chorganisation.name asc ");
        } else {
            sb.append("insurer.id, insurer.name ");
            sb.append("from insurer_chorganisation insurer_chorganisation ");
            sb.append("inner join insurer insurer on insurer.id=insurer_chorganisation.insurer_id ");
            sb.append("where insurer_chorganisation.chorganisation_id=:pOrgId ");
            sb.append("order by insurer.name asc ");
        }

        String query = sb.toString();

        Map paramMap = new HashMap();
        paramMap.put("pOrgId", iOrgId);
        paramMap.put("pCreatedDateFrom", dataStart);
        paramMap.put("pCreatedDateTo", dataEnd);

        List result = baseDataService.externalQuery(query, paramMap);

        for (Object o : result) {

            Map data = (Map) o;

            Integer iTotalClaimRejected = MathHelper.getIntegerValue(data.get("iTotalRejected".toLowerCase()));

            for (ClaimRejectionLineItem cRejected : claimRejection.getClaimRejectionLineItem()) {

                if (cRejected.getId() != null) {

                    String keyName = ("REJ_" + cRejected.getId()).toLowerCase();

                    ClaimRejectionLineItemDetail ReportColumn = new ClaimRejectionLineItemDetail();
                    ReportColumn.setNumberOfClaim(MathHelper.getIntegerValue(data.get(keyName)));
                    ReportColumn.setNumberOfClaimPercentage(MathHelper.getPercentage(ReportColumn.getNumberOfClaim(), iTotalClaimRejected));
                    cRejected.getReportColumns().add(ReportColumn);
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

        StringBuffer sb = new StringBuffer();
        sb.append("select ");
        sb.append("(select count(*) from claim claim where (date_trunc('day', claim.created_date) between :pCreatedDateFrom and :pCreatedDateTo) and claim.insurer_id=insurer_chorganisation.insurer_id and claim.chorganisation_id=insurer_chorganisation.chorganisation_id) as iTotal, ");
        sb.append("(select count(*) from claim claim where (date_trunc('day', claim.created_date) between :pCreatedDateFrom and :pCreatedDateTo) and claim.insurer_id=insurer_chorganisation.insurer_id and claim.chorganisation_id=insurer_chorganisation.chorganisation_id and claim.status='ClaimRejectionAccepted') as iTotalRejected, ");

        if (isInsReport) {

            sb.append("chorganisation.id, chorganisation.name ");
            sb.append("from insurer_chorganisation insurer_chorganisation ");
            sb.append("inner join chorganisation chorganisation on chorganisation.id=insurer_chorganisation.chorganisation_id ");
            sb.append("where insurer_chorganisation.insurer_id=:pOrgId ");
            sb.append("order by chorganisation.name asc ");

        } else {

            sb.append("insurer.id, insurer.name ");
            sb.append("from insurer_chorganisation insurer_chorganisation ");
            sb.append("inner join insurer insurer on insurer.id=insurer_chorganisation.insurer_id ");
            sb.append("where insurer_chorganisation.chorganisation_id=:pOrgId ");
            sb.append("order by insurer.name asc ");

        }

        String query = sb.toString();
        /* query = query.replaceAll(":pOrgId", iOrgId.toString());
        query = query.replaceAll(":pCreatedDateFrom", "'" + DateHelper.DBDateFormat.format(dataStart) + "'");
        query = query.replaceAll(":pCreatedDateTo", "'" + DateHelper.DBDateFormat.format(dataEnd) + "'");*/
        //List result = baseDataService.externalQuery(query);

        //Emmanuel
        //27-07-2009
        //prevent SQL Injection
        Map paramMap = new HashMap();
        paramMap.put("pOrgId", iOrgId);
        paramMap.put("pCreatedDateFrom", dataStart);
        paramMap.put("pCreatedDateTo", dataEnd);

        List result = baseDataService.externalQuery(query, paramMap);

        Integer iClaimTotalCount = 0;
        Integer iClaimRejectedTotalCount = 0;

        for (Object o : result) {

            Map data = (Map) o;

            iClaimTotalCount = iClaimTotalCount + MathHelper.getIntegerValue(data.get("iTotal".toLowerCase()));
            iClaimRejectedTotalCount = iClaimRejectedTotalCount + MathHelper.getIntegerValue(data.get("iTotalRejected".toLowerCase()));
            orgNames.add(data.get("name").toString());

            ClaimRejectionLineItemDetail ReportColumnClaim = new ClaimRejectionLineItemDetail();
            ReportColumnClaim.setNumberOfClaim(MathHelper.getIntegerValue(data.get("iTotal".toLowerCase())));
            reportRowAll.getReportColumns().add(ReportColumnClaim);

            ClaimRejectionLineItemDetail ReportColumnRejClaim = new ClaimRejectionLineItemDetail();
            ReportColumnRejClaim.setNumberOfClaim(MathHelper.getIntegerValue(data.get("iTotalRejected".toLowerCase())));
            ReportColumnRejClaim.setNumberOfClaimPercentage(MathHelper.getPercentage(ReportColumnRejClaim.getNumberOfClaim(), ReportColumnClaim.getNumberOfClaim()));
            reportRowRejected.getReportColumns().add(ReportColumnRejClaim);

        }

        reportRowAll.setAllOrgClaimCount(iClaimTotalCount);

        reportRowRejected.setAllOrgClaimCount(iClaimRejectedTotalCount);
        reportRowRejected.setAllOrgClaimCountPerc(MathHelper.getPercentage(iClaimRejectedTotalCount.floatValue(), iClaimTotalCount.floatValue()));

        claimRejection.setOrgName(orgNames);
        claimRejection.getClaimRejectionLineItem().add(0, reportRowAll);
        claimRejection.getClaimRejectionLineItem().add(1, reportRowRejected);
        return claimRejection;
    }

    public List<ClaimRejectionLineItem> getReasonOfRejection() {

        List<ClaimRejectionLineItem> reportRows = new ArrayList<ClaimRejectionLineItem>();
        String query = "select id, name from reason_of_rejection where type='Claim' order by id asc";
        List result = baseDataService.externalQuery(query);

        for (Object o : result) {
            Map data = (Map) o;
            ClaimRejectionLineItem reportRow = new ClaimRejectionLineItem();
            reportRow.setId(MathHelper.getIntegerValue(data.get("id".toLowerCase())));
            reportRow.setName(data.get("name").toString());
            reportRow.setDisplayName(ClaimRejectionLineItem.getDisplayNameMap(data.get("name").toString()));
            reportRows.add(reportRow);
        }

        return reportRows;

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
    public String getReportCode() {
        return "RPT003";
    }
}
