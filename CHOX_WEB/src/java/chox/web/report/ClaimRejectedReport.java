/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.report;

import chox.Util.DateHelper;
import chox.Util.MathHelper;
import chox.model.Chorganisation;
import chox.model.Insurer;
import chox.services.DataService;
import chox.web.report.viewdata.ClaimRejectedReportObject;
import chox.web.report.viewdata.ClaimRejection;
import chox.web.report.viewdata.ClaimRejectionLineItem;
import chox.web.report.viewdata.ClaimRejectionLineItemDetail;
import chox.web.security.PermissionedUser;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Emmanuel
 */

public class ClaimRejectedReport implements Report {

    Map externalParameter;
    List<String> reportParameterNames;
    private DataService dataService;

    public ClaimRejectedReport() {
        reportParameterNames = new ArrayList<String>();
    }

    public String getReportTemplateFileName() {
        return "template_ClaimRejectedReport.xls";
    }

    public void setExternalParameter(Map parameters) {
        this.externalParameter = parameters;
    }

    public HashMap getReportParameters() {
        
        HashMap reportParameters = new HashMap();
        boolean bAction = true;
        String sActionMsg = "";        
        
        PermissionedUser currentUser = ((PermissionedUser) externalParameter.get("CurrentUser"));
        
        try {
            
            List<ClaimRejectionLineItem> reportRows = getReasonOfRejection();
            
            Date dataStart = DateHelper.LocalDateFormat.parse(((String[]) externalParameter.get("DateStart"))[0]);
            Date dataEnd = DateHelper.LocalDateFormat.parse(((String[]) externalParameter.get("DateEnd"))[0]);
            boolean isInsReport = currentUser.getIsINS();
            
            String sOrganisationLabel = "";
            String sOrganisationName = "";
            
            ClaimRejection claimRejection = new ClaimRejection();
            claimRejection.setClaimRejectionLineItem(reportRows);
            
            Integer iOrgId = null;
            
            if(isInsReport){
                
                Insurer ins = currentUser.getUser().getInsurer();
                iOrgId = ins.getId();                
                sOrganisationLabel = "Insurer";
                sOrganisationName = ins.getName();

            }else{
                isInsReport = false;
                
                Chorganisation cho = currentUser.getUser().getChorganisation();
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
            bAction = false;
            sActionMsg = ex.getLocalizedMessage();
        } finally {
            dataService.logSystemLog(getReportCode(), sActionMsg, bAction);
        }

        return reportParameters;
    }

    private ClaimRejection getReportLineResult(boolean isIns, Integer iOrgId, ClaimRejection claimRejection, Date dataStart, Date dataEnd){
        
        claimRejection = getReportHeader(isIns, iOrgId, dataStart, dataEnd, claimRejection);
        
        StringBuffer sb = new StringBuffer();
        sb.append("select ");
        sb.append("(select count(*) from claim claim where (date_trunc('day', claim.created_date) between @pCreatedDateFrom and @pCreatedDateTo) and claim.insurer_id=insurer_chorganisation.insurer_id and claim.chorganisation_id=insurer_chorganisation.chorganisation_id) as iTotal, ");
        sb.append("(select count(*) from claim claim where (date_trunc('day', claim.created_date) between @pCreatedDateFrom and @pCreatedDateTo) and claim.insurer_id=insurer_chorganisation.insurer_id and claim.chorganisation_id=insurer_chorganisation.chorganisation_id and claim.status='ClaimRejectionAccepted') as iTotalRejected, ");
        
        for(ClaimRejectionLineItem cRejected : claimRejection.getClaimRejectionLineItem()){
            if(cRejected.getId()!=null){
                
                sb.append("(select count(*) from claim claim, (select distinct claim_id, new_status from audit_trail where new_status='ClaimRejectionAccepted' and claim_reason_of_rejection="+cRejected.getId()+") as audit_trail where (date_trunc('day', claim.created_date) between @pCreatedDateFrom and @pCreatedDateTo) and claim.id=audit_trail.claim_id and claim.insurer_id=insurer_chorganisation.insurer_id and claim.chorganisation_id=insurer_chorganisation.chorganisation_id) as REJ_"+cRejected.getId()+", ");
                
                if(isIns){
                    sb.append("(select count(*) from claim claim, (select distinct claim_id, new_status from audit_trail where new_status='ClaimRejectionAccepted' and claim_reason_of_rejection="+cRejected.getId()+") as audit_trail where (date_trunc('day', claim.created_date) between @pCreatedDateFrom and @pCreatedDateTo) and claim.id=audit_trail.claim_id and claim.insurer_id=insurer_chorganisation.insurer_id) as REJ_PERC_"+cRejected.getId()+", ");
                }else{
                    sb.append("(select count(*) from claim claim, (select distinct claim_id, new_status from audit_trail where new_status='ClaimRejectionAccepted' and claim_reason_of_rejection="+cRejected.getId()+") as audit_trail where (date_trunc('day', claim.created_date) between @pCreatedDateFrom and @pCreatedDateTo) and claim.id=audit_trail.claim_id and claim.chorganisation_id=insurer_chorganisation.chorganisation_id) as REJ_PERC_"+cRejected.getId()+", ");
                }
            }
        }
        
        if(isIns){
            sb.append("chorganisation.id, chorganisation.name ");
            sb.append("from insurer_chorganisation insurer_chorganisation ");
            sb.append("inner join chorganisation chorganisation on chorganisation.id=insurer_chorganisation.chorganisation_id ");
            sb.append("where insurer_chorganisation.insurer_id=@pOrgId ");
            sb.append("order by chorganisation.name asc "); 
        }else{
            sb.append("insurer.id, insurer.name ");
            sb.append("from insurer_chorganisation insurer_chorganisation ");
            sb.append("inner join insurer insurer on insurer.id=insurer_chorganisation.insurer_id ");
            sb.append("where insurer_chorganisation.chorganisation_id=@pOrgId ");
            sb.append("order by insurer.name asc "); 
        }
        
        String query = sb.toString();
        query = query.replaceAll("@pOrgId", iOrgId.toString());
        query = query.replaceAll("@pCreatedDateFrom", "'" + DateHelper.DBDateFormat.format(dataStart) + "'");
        query = query.replaceAll("@pCreatedDateTo", "'" + DateHelper.DBDateFormat.format(dataEnd) + "'");

        List result = dataService.externalQuery(query);
        
        for (Object o : result) {
            
            Map data = (Map) o;
            
            Integer iTotalClaimRejected = MathHelper.getIntegerValue(data.get("iTotalRejected".toLowerCase()));
            
            for(ClaimRejectionLineItem cRejected : claimRejection.getClaimRejectionLineItem()){
                
                if(cRejected.getId()!=null){
                    
                    String keyName = ("REJ_"+cRejected.getId()).toLowerCase();
                    // String ketPercName = ("REJ_PERC_"+cRejected.getId()).toLowerCase();
                    
                    ClaimRejectionLineItemDetail ReportColumn = new ClaimRejectionLineItemDetail();
                    ReportColumn.setNumberOfClaim(MathHelper.getIntegerValue(data.get(keyName)));
                    ReportColumn.setNumberOfClaimPercentage(MathHelper.getPercentage(ReportColumn.getNumberOfClaim(), iTotalClaimRejected));
                    // ReportColumn.setNumberOfClaimPercentage(MathHelper.getPercentage(ReportColumn.getNumberOfClaim(), MathHelper.getIntegerValue(data.get(ketPercName))));
                    cRejected.getReportColumns().add(ReportColumn);
                }
            }
        }
        
        claimRejection = getAllOrgCount(claimRejection);
        
        return claimRejection;
    }
    
    private ClaimRejection getAllOrgCount(ClaimRejection claimRejection){
        
        Integer AllRejectedClaims = claimRejection.getClaimRejectionLineItem().get(1).getAllOrgClaimCount();
        
        for(ClaimRejectionLineItem cRejected : claimRejection.getClaimRejectionLineItem()){
            
            if(cRejected.getId()!=null){
                Integer iAllOrgClaimCount = 0;

                for(ClaimRejectionLineItemDetail cRejectedDtl : cRejected.getReportColumns()){
                    iAllOrgClaimCount = iAllOrgClaimCount + cRejectedDtl.getNumberOfClaim();
                }

                cRejected.setAllOrgClaimCount(iAllOrgClaimCount);
                cRejected.setAllOrgClaimCountPerc(MathHelper.getPercentage(iAllOrgClaimCount.floatValue(), AllRejectedClaims.floatValue()));
            }
        }
        
        return claimRejection;
    }
    
    public ClaimRejection getReportHeader(boolean isInsReport, Integer iOrgId, Date dataStart, Date dataEnd, ClaimRejection claimRejection){
        
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
        sb.append("(select count(*) from claim claim where (date_trunc('day', claim.created_date) between @pCreatedDateFrom and @pCreatedDateTo) and claim.insurer_id=insurer_chorganisation.insurer_id and claim.chorganisation_id=insurer_chorganisation.chorganisation_id) as iTotal, ");
        sb.append("(select count(*) from claim claim where (date_trunc('day', claim.created_date) between @pCreatedDateFrom and @pCreatedDateTo) and claim.insurer_id=insurer_chorganisation.insurer_id and claim.chorganisation_id=insurer_chorganisation.chorganisation_id and claim.status='ClaimRejectionAccepted') as iTotalRejected, ");            

        if(isInsReport){
            
            sb.append("chorganisation.id, chorganisation.name ");
            sb.append("from insurer_chorganisation insurer_chorganisation ");
            sb.append("inner join chorganisation chorganisation on chorganisation.id=insurer_chorganisation.chorganisation_id ");
            sb.append("where insurer_chorganisation.insurer_id=@pOrgId ");
            sb.append("order by chorganisation.name asc "); 
            
        }else{
            
            sb.append("insurer.id, insurer.name ");
            sb.append("from insurer_chorganisation insurer_chorganisation ");
            sb.append("inner join insurer insurer on insurer.id=insurer_chorganisation.insurer_id ");
            sb.append("where insurer_chorganisation.chorganisation_id=@pOrgId ");
            sb.append("order by insurer.name asc "); 
            
        }
        
        String query = sb.toString();
        query = query.replaceAll("@pOrgId", iOrgId.toString());
        query = query.replaceAll("@pCreatedDateFrom", "'" + DateHelper.DBDateFormat.format(dataStart) + "'");
        query = query.replaceAll("@pCreatedDateTo", "'" + DateHelper.DBDateFormat.format(dataEnd) + "'");        
        
        List result = dataService.externalQuery(query);
        
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
        String query = "select id, name from reason_of_rejection where type='Claim' and status = true order by id asc";
        List result = dataService.externalQuery(query);

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
    
    public InputStream build() {
        ReportBuilder builder = getReportBuilder();
        return builder.buildReport(this);
    }

    protected ReportBuilder getReportBuilder() {
        return new ExcelReportBuilder();
    }

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }

    public String getReportCode() {
        return "RPT003";
    }
}