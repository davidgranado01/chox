/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.report;

import chox.Util.DateHelper;
import chox.Util.MathHelper;
import chox.model.Insurer;
import chox.services.DataService;
import chox.web.report.viewdata.ClaimRejected;
import chox.web.report.viewdata.ClaimRejectedReportDtlObject;
import chox.web.report.viewdata.ClaimRejectedReportObject;
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
        
        List<ClaimRejected> reportRows = getReasonOfRejection();
        PermissionedUser currentUser = ((PermissionedUser) externalParameter.get("CurrentUser"));
        
        try {
            
            Date dataStart = DateHelper.LocalDateFormat.parse(((String[]) externalParameter.get("DateStart"))[0]);
            Date dataEnd = DateHelper.LocalDateFormat.parse(((String[]) externalParameter.get("DateEnd"))[0]);
            boolean isInsReport = currentUser.getIsINS();
            
            String sOrganisationLabel = "";
            String sOrganisationName = "";
            
            if(isInsReport){
                
                Insurer ins = currentUser.getUser().getInsurer();
                Integer insId = ins.getId();                
                sOrganisationLabel = "Insurer:";
                sOrganisationName = ins.getName();
                 
                reportRows = getInsurerResult(insId, reportRows, dataStart, dataEnd);
                reportParameters.put("ClaimRejectedReportDtlObject", setReportOrgHeader(isInsReport, insId, dataStart, dataEnd));
                
            }else{

            }
            
            ClaimRejectedReportObject reportObject = new ClaimRejectedReportObject();
            reportObject.setDateFrom(dataStart);
            reportObject.setDateTo(dataEnd);
            reportObject.setCreatedDate(new Date());
            
            reportParameters.put("reportRows", reportRows);
            reportParameters.put("reportObj", reportObject);
            reportParameters.put("organisationLabel", sOrganisationLabel);
            reportParameters.put("organisationName", sOrganisationName);
            
        } catch (Exception ex) {
            
        }

        return reportParameters;
    }
    
    private ClaimRejectedReportDtlObject setReportOrgHeader(boolean isIns, Integer id, Date dataStart, Date dataEnd){
        
        ClaimRejectedReportDtlObject object = new ClaimRejectedReportDtlObject();
        String query = "";
        
        if(isIns){
            
            query = "select chorganisation.id, chorganisation.name, "
            + "(select count(*) from claim claim where (date_trunc('day', claim.created_date) between @pCreatedDateFrom and @pCreatedDateTo) and claim.insurer_id=insurer_chorganisation.insurer_id and claim.chorganisation_id=insurer_chorganisation.chorganisation_id) as iTotal, "
            + "(select count(*) from claim claim where (date_trunc('day', claim.created_date) between @pCreatedDateFrom and @pCreatedDateTo) and claim.insurer_id=insurer_chorganisation.insurer_id and claim.chorganisation_id=insurer_chorganisation.chorganisation_id and claim.status='ClaimRejectionAccepted') as iTotalRejected "
            + "from insurer_chorganisation insurer_chorganisation "
            + "inner join chorganisation chorganisation on chorganisation.id=insurer_chorganisation.chorganisation_id "
            + "where insurer_id=@pInsId order by chorganisation.name asc ";            

            query = query.replaceAll("@pInsId", id.toString());

        }else{
            
            query = "select insurer.id, insurer.name, "
            + "(select count(*) from claim claim where (date_trunc('day', claim.created_date) between @pCreatedDateFrom and @pCreatedDateTo) and claim.insurer_id=insurer_chorganisation.insurer_id and claim.chorganisation_id=insurer_chorganisation.chorganisation_id) as iTotal, "
            + "(select count(*) from claim claim where (date_trunc('day', claim.created_date) between @pCreatedDateFrom and @pCreatedDateTo) and claim.insurer_id=insurer_chorganisation.insurer_id and claim.chorganisation_id=insurer_chorganisation.chorganisation_id and claim.status='ClaimRejectionAccepted') as iTotalRejected "
            + "from insurer_chorganisation insurer_chorganisation "
            + "inner join insurer insurer on insurer.id=insurer_chorganisation.insurer_id "
            + "where chorganisation_id=@pChorganisationId order by insurer.name asc ";            

            query = query.replaceAll("@pChorganisationId", id.toString());
            
        }
        
        query = query.replaceAll("@pCreatedDateFrom", "'" + DateHelper.DBDateFormat.format(dataStart) + "'");
        query = query.replaceAll("@pCreatedDateTo", "'" + DateHelper.DBDateFormat.format(dataEnd) + "'");        
        List result = dataService.externalQuery(query);   
        
        int iCount = 0;
        Integer iTotalAll = 0;
        Integer iTotalRejectedAll = 0;
        
        for (Object o : result) {
            
            Map data = (Map) o;
            String  orgName = data.get("name").toString();
            Integer iTotal = MathHelper.getIntegerValue(data.get("iTotal".toLowerCase()));
            Integer iTotalRejected = MathHelper.getIntegerValue(data.get("iTotalRejected".toLowerCase()));
            
            iTotalAll = iTotalAll + iTotal;
            iTotalRejectedAll = iTotalRejectedAll + iTotalRejected;
            
            switch (iCount) {
                case 0:
                    object.setOrgName0(orgName);
                    object.setOrgClaimTotal0(iTotal);
                    object.setOrgClaimRejectedTotal0(iTotalRejected);
                    object.setOrgClaimRejectedPerc0(MathHelper.getPercentage(iTotalRejected.floatValue(), iTotal.floatValue()));
                    break;
                case 1: 
                    object.setOrgName1(orgName); 
                    object.setOrgClaimTotal1(iTotal);
                    object.setOrgClaimRejectedTotal1(iTotalRejected);
                    object.setOrgClaimRejectedPerc1(MathHelper.getPercentage(iTotalRejected.floatValue(), iTotal.floatValue()));
                    break;
                case 2: 
                    object.setOrgName2(orgName);
                    object.setOrgClaimTotal2(iTotal);
                    object.setOrgClaimRejectedTotal2(iTotalRejected);
                    object.setOrgClaimRejectedPerc2(MathHelper.getPercentage(iTotalRejected.floatValue(), iTotal.floatValue()));
                    break;
                case 3: 
                    object.setOrgName3(orgName);
                    object.setOrgClaimTotal3(iTotal);
                    object.setOrgClaimRejectedTotal3(iTotalRejected);
                    object.setOrgClaimRejectedPerc3(MathHelper.getPercentage(iTotalRejected.floatValue(), iTotal.floatValue()));
                    break;
                case 4:
                    object.setOrgName4(orgName);  
                    object.setOrgClaimTotal4(iTotal);
                    object.setOrgClaimRejectedTotal4(iTotalRejected);
                    object.setOrgClaimRejectedPerc4(MathHelper.getPercentage(iTotalRejected.floatValue(), iTotal.floatValue()));
                    break;
                case 5:
                    object.setOrgName5(orgName);
                    object.setOrgClaimTotal5(iTotal);
                    object.setOrgClaimRejectedTotal5(iTotalRejected);
                    object.setOrgClaimRejectedPerc5(MathHelper.getPercentage(iTotalRejected.floatValue(), iTotal.floatValue()));
                    break;
                case 6:
                    object.setOrgName6(orgName); 
                    object.setOrgClaimTotal6(iTotal);
                    object.setOrgClaimRejectedTotal6(iTotalRejected);
                    object.setOrgClaimRejectedPerc6(MathHelper.getPercentage(iTotalRejected.floatValue(), iTotal.floatValue()));
                    break;
                case 7:
                    object.setOrgName7(orgName);
                    object.setOrgClaimTotal7(iTotal);
                    object.setOrgClaimRejectedTotal7(iTotalRejected);
                    object.setOrgClaimRejectedPerc7(MathHelper.getPercentage(iTotalRejected.floatValue(), iTotal.floatValue()));
                    break;
                case 8:
                    object.setOrgName8(orgName);
                    object.setOrgClaimTotal8(iTotal);
                    object.setOrgClaimRejectedTotal8(iTotalRejected);
                    object.setOrgClaimRejectedPerc8(MathHelper.getPercentage(iTotalRejected.floatValue(), iTotal.floatValue()));
                    break;
                case 9:
                    object.setOrgName9(orgName); 
                    object.setOrgClaimTotal9(iTotal);
                    object.setOrgClaimRejectedTotal9(iTotalRejected);
                    object.setOrgClaimRejectedPerc9(MathHelper.getPercentage(iTotalRejected.floatValue(), iTotal.floatValue()));
                    break;
            }

            iCount++;
        }
        
        object.setOrgNameAll("All");
        object.setOrgClaimTotalAll(iTotalAll);
        object.setOrgClaimRejectedTotalAll(iTotalRejectedAll);
        object.setOrgClaimRejectedPercAll(MathHelper.getPercentage(iTotalRejectedAll.floatValue(), iTotalAll.floatValue()));
        return object;
    }
    
    private List<ClaimRejected> getInsurerResult(Integer insId, List<ClaimRejected> reportRows, Date dataStart, Date dataEnd){
        
        for(ClaimRejected cRejected : reportRows){
            
            Integer rejectedReasonId = cRejected.getId();
            String query = "select chorganisation.id, chorganisation.name, "
            + "(select count(*) from claim claim where (date_trunc('day', claim.created_date) between @pCreatedDateFrom and @pCreatedDateTo) and claim.insurer_id=insurer_chorganisation.insurer_id and claim.chorganisation_id=insurer_chorganisation.chorganisation_id) as iTotal, "
            + "(select count(*) from claim claim where (date_trunc('day', claim.created_date) between @pCreatedDateFrom and @pCreatedDateTo) and claim.insurer_id=insurer_chorganisation.insurer_id and claim.chorganisation_id=insurer_chorganisation.chorganisation_id and claim.status='ClaimRejectionAccepted') as iTotalRejected, "                    
            + "(select count(*) from claim claim, (select distinct claim_id, new_status from audit_trail where new_status='ClaimRejectionAccepted' and claim_reason_of_rejection=@pRejectedReasonId) as audit_trail where (date_trunc('day', claim.created_date) between @pCreatedDateFrom and @pCreatedDateTo) and claim.id=audit_trail.claim_id and claim.insurer_id=insurer_chorganisation.insurer_id and claim.chorganisation_id=insurer_chorganisation.chorganisation_id) as claimCount "
            + "from insurer_chorganisation insurer_chorganisation  "
            + "inner join chorganisation chorganisation on chorganisation.id=insurer_chorganisation.chorganisation_id "
            + "where insurer_id=@pInsId order by chorganisation.name asc ";            
            
            query = query.replaceAll("@pInsId", insId.toString());
            query = query.replaceAll("@pRejectedReasonId", rejectedReasonId.toString());
            query = query.replaceAll("@pCreatedDateFrom", "'" + DateHelper.DBDateFormat.format(dataStart) + "'");
            query = query.replaceAll("@pCreatedDateTo", "'" + DateHelper.DBDateFormat.format(dataEnd) + "'");
            
            List result = dataService.externalQuery(query);
            
            int iCount = 0;
            Integer iTotalRejectedAll = 0;
            Integer iRejectedClaimCountAll = 0;
            
            for (Object o : result) {
                
                Map data = (Map) o;
                Integer iTotalRejected = MathHelper.getIntegerValue(data.get("iTotalRejected".toLowerCase()));
                Integer rejectedClaimCount = MathHelper.getIntegerValue(data.get("claimCount".toLowerCase()));
                BigDecimal rejectedClaimPercentage = MathHelper.getPercentage(rejectedClaimCount.floatValue(), iTotalRejected);
                
                iTotalRejectedAll = iTotalRejectedAll + iTotalRejected;
                iRejectedClaimCountAll = iRejectedClaimCountAll + rejectedClaimCount;
            
                switch (iCount) {
                    case 0:
                        cRejected.setOrgCount0(rejectedClaimCount);
                        cRejected.setOrgPercentage0(rejectedClaimPercentage);  
                        break;
                    case 1: 
                        cRejected.setOrgCount1(rejectedClaimCount);
                        cRejected.setOrgPercentage1(rejectedClaimPercentage);  
                        break;
                    case 2: 
                        cRejected.setOrgCount2(rejectedClaimCount);
                        cRejected.setOrgPercentage2(rejectedClaimPercentage);  
                        break;
                    case 3: 
                        cRejected.setOrgCount3(rejectedClaimCount);
                        cRejected.setOrgPercentage3(rejectedClaimPercentage);
                        break;
                    case 4:
                        cRejected.setOrgCount4(rejectedClaimCount);
                        cRejected.setOrgPercentage4(rejectedClaimPercentage);                          
                        break;
                    case 5:
                        cRejected.setOrgCount5(rejectedClaimCount);
                        cRejected.setOrgPercentage5(rejectedClaimPercentage);
                        break;
                    case 6:
                        cRejected.setOrgCount6(rejectedClaimCount);
                        cRejected.setOrgPercentage6(rejectedClaimPercentage); 
                        break;
                    case 7:
                        cRejected.setOrgCount7(rejectedClaimCount);
                        cRejected.setOrgPercentage7(rejectedClaimPercentage); 
                        break;
                    case 8:
                        cRejected.setOrgCount8(rejectedClaimCount);
                        cRejected.setOrgPercentage8(rejectedClaimPercentage); 
                        break;
                    case 9:
                        cRejected.setOrgCount9(rejectedClaimCount);
                        cRejected.setOrgPercentage9(rejectedClaimPercentage);  
                        break;
                }
                iCount++;
            }
            
            cRejected.setOrgCountAll(iRejectedClaimCountAll);
            cRejected.setOrgPercentageAll(MathHelper.getPercentage(iRejectedClaimCountAll.floatValue(), iTotalRejectedAll.floatValue()));
        }
        
        return reportRows;
    }
    
    public List<ClaimRejected> getReasonOfRejection() {
        
        List<ClaimRejected> reportRows = new ArrayList<ClaimRejected>();
        String query = "select id, name from reason_of_rejection where type='Claim' and status=true order by name asc";
        List result = dataService.externalQuery(query);

        for (Object o : result) {
            Map data = (Map) o;
            ClaimRejected reportRow = new ClaimRejected();
            reportRow.setId(MathHelper.getIntegerValue(data.get("id".toLowerCase())));
            reportRow.setName(data.get("name").toString());
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
}