package idas.chox.service.reports;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import idas.chox.service.reports.viewdata.ClaimRejectionSumLineItem;
import idas.chox.service.reports.viewdata.ClaimRejectionOrgLineItem;

public class ClaimRejectedReport implements Report {
    private static final Logger LOG = LoggerFactory.getLogger(ClaimRejectedReport.class);

    private Map externalParameter;
    private List<String> reportParameterNames;
    private BaseDataService baseDataService;
    private ReportDataService reportDataService;
    private WebUser currentUser;
    private List result1;
    private List<ClaimRejectionOrgLineItem> orgRorList;
    private List<ClaimRejectionSumLineItem> sumRorList;
    private List<Integer> orgClaimCounts;
    private List<String> orgClaimPercs;
    
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

    @Override
    public HashMap getReportParameters() {
        HashMap reportParameters = new HashMap();
        currentUser = ((WebUser) externalParameter.get("CurrentUser"));
        try {

            sumRorList = getReasonOfRejection(currentUser);

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

            setReportResults(isInsReport, iOrgId, dataStart, dataEnd);
            setSumReportResults(isInsReport, iOrgId, dataStart, dataEnd);
            setAllOrgCount();
            
            ClaimRejectedReportObject reportObject = new ClaimRejectedReportObject();
            reportObject.setDateFrom(dataStart);
            reportObject.setDateTo(dataEnd);
            reportObject.setCreatedDate(new Date());
            
            reportParameters.put("orgRorList", orgRorList);
            reportParameters.put("sumRorList", sumRorList);
            reportParameters.put("reportObj", reportObject);
            reportParameters.put("organisationLabel", sOrganisationLabel);
            reportParameters.put("organisationName", sOrganisationName);
            
        } catch (Exception ex) {
            LOG.error("Exception thrown: {}", ex.getMessage());
        }
        return reportParameters;
    }
    
    private void setReportResults(boolean isIns, Integer iOrgId, Date dataStart, Date dataEnd) {
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append("(select count(*) from claim where (date_trunc('day', claim.created_date) between :pCreatedDateFrom and :pCreatedDateTo) and claim.insurer_id=insurer_chorganisation.insurer_id and claim.chorganisation_id=insurer_chorganisation.chorganisation_id and (claim.claim_type not in ").append(ClaimType.getSupplementaryInvoiceTypeOrdinals()).append(")) as iTotal, ");
        sb.append("(select count(*) from claim, audit_trail a where claim.id=a.claim_id and a.reverted=false and (a.new_status='ClaimRejectionAccepted' or (a.new_status = 'AwaitingCarHireInfo' and a.original_status='SubscriberClaimRejected')) and (date_trunc('day', claim.created_date) between :pCreatedDateFrom and :pCreatedDateTo) and claim.insurer_id=insurer_chorganisation.insurer_id and claim.chorganisation_id=insurer_chorganisation.chorganisation_id) as iTotalRejected, ");
        
            for (ClaimRejectionSumLineItem cRejected : sumRorList) {
                if (cRejected.getId() != null) {
                    if(isIns){
                        sb.append("(select count(*) from claim, audit_trail a where claim.id=a.claim_id and a.reverted=false and (a.new_status='ClaimRejectionAccepted' or (a.new_status = 'AwaitingCarHireInfo' and a.original_status='SubscriberClaimRejected')) and a.claim_reason_of_rejection=")
                        .append(cRejected.getId()).append(" and (date_trunc('day', claim.created_date) between :pCreatedDateFrom and :pCreatedDateTo) and claim.insurer_id=insurer_chorganisation.insurer_id and claim.chorganisation_id=insurer_chorganisation.chorganisation_id) as REJ_")
                        .append(cRejected.getId()).append(", ");
                    } else {
                        sb.append("(select count(*) from claim, audit_trail a where claim.id=a.claim_id and a.reverted=false and (a.new_status='ClaimRejectionAccepted' or (a.new_status = 'AwaitingCarHireInfo' and a.original_status='SubscriberClaimRejected')) and a.claim_reason_of_rejection in ")
                        .append("(select id from reason_of_rejection where name = '").append(cRejected.getName()).append("' and insurer_id=insurer.id) and (date_trunc('day', claim.created_date) between :pCreatedDateFrom and :pCreatedDateTo) and claim.insurer_id=insurer_chorganisation.insurer_id and claim.chorganisation_id=insurer_chorganisation.chorganisation_id) as REJ_")
                        .append(cRejected.getId()).append(", ");
                    }    
                }
            }

        if (isIns) {
            sb.append("chorganisation.id, chorganisation.name ");
            sb.append("from insurer_chorganisation insurer_chorganisation ");
            sb.append("inner join chorganisation chorganisation on chorganisation.id=insurer_chorganisation.chorganisation_id ");
            sb.append("where insurer_chorganisation.insurer_id=:pOrgId and insurer_chorganisation.status=true ");
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
        result1 = result;
    }
    
    public void setSumReportResults(boolean isInsReport, Integer iOrgId, Date dataStart, Date dataEnd) {
        // INDEX 0
        ClaimRejectionSumLineItem reportRowAll = new ClaimRejectionSumLineItem();
        reportRowAll.setId(null);
        reportRowAll.setName("Total No. Claims.");
        reportRowAll.setDisplayName("Total No. Claims.");

        // INDEX 1
        ClaimRejectionSumLineItem reportRowRejected = new ClaimRejectionSumLineItem();
        reportRowRejected.setId(null);
        reportRowRejected.setName("Total No. Rejected Claims");
        reportRowRejected.setDisplayName("Total No. Rejected Claims");

        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append("(select count(*) from claim where (date_trunc('day', claim.created_date) between :pCreatedDateFrom and :pCreatedDateTo) and claim.insurer_id=insurer_chorganisation.insurer_id and claim.chorganisation_id=insurer_chorganisation.chorganisation_id and (claim_type not in ").append(ClaimType.getSupplementaryInvoiceTypeOrdinals()).append(")) as iTotal, ");
        sb.append("(select count(*) from claim, audit_trail a where claim.id=a.claim_id and a.reverted=false and (a.new_status='ClaimRejectionAccepted' or (a.new_status = 'AwaitingCarHireInfo' and a.original_status='SubscriberClaimRejected')) and (date_trunc('day', claim.created_date) between :pCreatedDateFrom and :pCreatedDateTo) and claim.insurer_id=insurer_chorganisation.insurer_id and claim.chorganisation_id=insurer_chorganisation.chorganisation_id) as iTotalRejected, ");

        if (isInsReport) {
            sb.append("chorganisation.id, chorganisation.name ");
            sb.append("from insurer_chorganisation insurer_chorganisation ");
            sb.append("inner join chorganisation chorganisation on chorganisation.id=insurer_chorganisation.chorganisation_id ");
            sb.append("where insurer_chorganisation.insurer_id=:pOrgId and insurer_chorganisation.status=true ");
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

        List result = reportDataService.getReportData(query, paramMap);

        Integer iClaimTotalCount = 0;
        Integer iClaimRejectedTotalCount = 0;
        orgRorList = new ArrayList<ClaimRejectionOrgLineItem>();

        for (Object o : result) {

            ClaimRejectionOrgLineItem claimRejectionOrg = new ClaimRejectionOrgLineItem();
            
            Map data = (Map) o;
            
            iClaimTotalCount += MathHelper.getIntegerValue(data.get("iTotal".toLowerCase()));
            iClaimRejectedTotalCount += MathHelper.getIntegerValue(data.get("iTotalRejected".toLowerCase()));
            //loops trough reasons of rejection and sets count for each reason of rejection per specific group
            if(result1 != null){
                getOrgRorClaimCountsAndPercentages(isInsReport, iOrgId, dataStart, dataEnd, data.get("id").toString());
                claimRejectionOrg.setOrgClaimCounts(orgClaimCounts);
                //loops trough reasons of rejection and sets percentages for each reason of rejection per specific group
                claimRejectionOrg.setOrgClaimCountPercs(orgClaimPercs);
                claimRejectionOrg.setOrgName(data.get("name").toString());
                orgRorList.add(claimRejectionOrg);
            }
        }

        reportRowAll.setAllOrgClaimCount(iClaimTotalCount);

        reportRowRejected.setAllOrgClaimCount(iClaimRejectedTotalCount);
        reportRowRejected.setAllOrgClaimCountPerc(MathHelper.getPercentage(iClaimRejectedTotalCount.floatValue(), iClaimTotalCount.floatValue()));

        sumRorList.add(0, reportRowAll);
        sumRorList.add(1, reportRowRejected);
        return;
    }
    
    private void getOrgRorClaimCountsAndPercentages (boolean isIns, Integer iOrgId, Date dataStart, Date dataEnd, String id) {
        
        orgClaimCounts = new ArrayList<Integer>();
        orgClaimPercs = new ArrayList<String>();

        for (Object o : result1) {
            Map data = (Map) o;
            Integer iTotalClaimRejected = MathHelper.getIntegerValue(data.get("iTotalRejected".toLowerCase()));
            Integer iTotal = MathHelper.getIntegerValue(data.get("iTotal".toLowerCase()));
            
            if(data.get("id").toString().equals(id)){
                orgClaimCounts.add(0, MathHelper.getIntegerValue(data.get("iTotal".toLowerCase())));
                orgClaimCounts.add(1, iTotalClaimRejected);
                
                orgClaimPercs.add(0, MathHelper.getExcelDisplayPerc(null));
                orgClaimPercs.add(1, MathHelper.getExcelDisplayPerc(MathHelper.getPercentage(iTotalClaimRejected, iTotal)));
            }
            
            for (ClaimRejectionSumLineItem cRejected : sumRorList) {
                if (cRejected.getId() != null && data.get("id").toString().equals(id)) {
                    String keyName = ("REJ_" + cRejected.getId()).toLowerCase();
                    orgClaimCounts.add(MathHelper.getIntegerValue(data.get(keyName)));
                    orgClaimPercs.add(MathHelper.getExcelDisplayPerc(MathHelper.getPercentage(MathHelper.getIntegerValue(data.get(keyName)), iTotalClaimRejected)));
                }
            }
        }
        return;
    }
    
    private void setAllOrgCount() {
        
        Integer allRejectedClaims = sumRorList.get(1).getAllOrgClaimCount();
        
        for (ClaimRejectionSumLineItem cRejected : sumRorList) {
            if(cRejected.getId() != null){
                Integer rejecetedTotalCount = 0;
                for (Object o : result1) {
                    Map data = (Map) o;
                        Set dataSet = data.keySet();
                        for(Object so : dataSet){
                            String ss = (String) so;
                            if(ss.equalsIgnoreCase("rej_" + cRejected.getId()) ){
                                rejecetedTotalCount += ((BigInteger)data.get(ss)).intValue();
                            }
                        }
                }
                sumRorList.get(sumRorList.indexOf(cRejected)).setAllOrgClaimCount(rejecetedTotalCount);
                sumRorList.get(sumRorList.indexOf(cRejected)).setAllOrgClaimCountPerc(MathHelper.getPercentage(rejecetedTotalCount.floatValue(),allRejectedClaims.floatValue()));
            } 
        }
    }
    
    private List<ClaimRejectionSumLineItem> getReasonOfRejection(WebUser currentUser) {

        List<ClaimRejectionSumLineItem> reportRows = new ArrayList<ClaimRejectionSumLineItem>();
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
            ClaimRejectionSumLineItem reportRow = new ClaimRejectionSumLineItem();
            if(currentUser.getInsurer() != null) { 
                reportRow.setId(MathHelper.getIntegerValue(data.get("id".toLowerCase())));
            } else {
                reportRow.setId(i++); // in case of cho we need only unique reason of rejection name
            }
            reportRow.setName(data.get("name").toString());
            reportRow.setDisplayName(ClaimRejectionSumLineItem.getDisplayNameMap(data.get("name").toString()));
            reportRows.add(reportRow);
        }
        return reportRows;
    }

}
