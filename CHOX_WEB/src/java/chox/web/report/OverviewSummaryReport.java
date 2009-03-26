package chox.web.report;

import chox.Util.DateHelper;
import chox.model.Chorganisation;
import chox.model.Insurer;
import chox.services.DataService;
import chox.web.report.viewdata.OverviewSummaryLineItem;
import chox.web.report.viewdata.OverviewSummaryReportObject;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import chox.web.report.viewdata.OverviewSummaryLineItemDetail;
import chox.web.report.viewdata.OverviewSummaryReportByOrg;
import chox.web.security.PermissionedUser;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OverviewSummaryReport implements Report {

    Map externalParameter;
    List<String> reportParameterNames;
    private DataService dataService;
    
    public HashMap getReportParameters() {
        
        HashMap reportParameters = new HashMap();
        
        boolean bAction = true;
        String sActionMsg = "";
        Integer userOrgId = -1;
        String userOrgLabel = "";
        String userOrgName = "";
        
        PermissionedUser currentUser = ((PermissionedUser) externalParameter.get("CurrentUser"));
        boolean isInsReport = currentUser.getIsINS();
        userOrgName = currentUser.getUser().getOrganisationName();
                
        if(isInsReport){
            userOrgLabel = "Insurer";
            userOrgId = currentUser.getUser().getInsurer().getId();
        }else{
            userOrgLabel = "Credit Hire Organisation";
            userOrgId = currentUser.getUser().getChorganisation().getId();
        }

        try {
            
            Date dataStart = DateHelper.LocalDateFormat.parse(((String[]) externalParameter.get("DateStart"))[0]);
            Date dataEnd = DateHelper.LocalDateFormat.parse(((String[]) externalParameter.get("DateEnd"))[0]);        
            
            StringBuffer sb = new StringBuffer();
            
            if(isInsReport){
                sb.append("select chorganisation.id as org_id, chorganisation.name as org_name, ");
            }else{
                sb.append("select insurer.id as org_id, insurer.name as org_name, ");
            }
            
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_all_claim_with_invoice where date(claim_created_date) between @pUploadDateFrom and @pUploadDateTo and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id) as total_no_claims_num, ");
            sb.append("(select case when sum(rpt_all_claim_with_invoice.total_to_pay) is null then 0.00 else sum(rpt_all_claim_with_invoice.total_to_pay) end as no_count from rpt_all_claim_with_invoice where date(claim_created_date) between @pUploadDateFrom and @pUploadDateTo and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id) as total_no_claims_val, ");
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_all_claim_with_invoice a, (select distinct claim_id from audit_trail where new_status='AwaitingCarHireInfo') b where a.claim_id=b.claim_id and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id and date(claim_created_date) between @pUploadDateFrom and @pUploadDateTo) as total_no_accepted_claims_num, ");
            sb.append("(select case when sum(a.total_to_pay) is null then 0.00 else sum(a.total_to_pay) end as no_count from rpt_all_claim_with_invoice a, (select distinct claim_id from audit_trail where new_status='AwaitingCarHireInfo') b where a.claim_id=b.claim_id and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id and date(claim_created_date) between @pUploadDateFrom and @pUploadDateTo) as total_no_accepted_claims_val, ");
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_all_claim_with_invoice a, (select distinct claim_id from audit_trail where new_status='ClaimRejectionAccepted') b where a.claim_id=b.claim_id and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id and date(claim_created_date) between @pUploadDateFrom and @pUploadDateTo) as total_no_rejected_claims_num, ");
            sb.append("(select case when sum(a.total_to_pay) is null then 0.00 else sum(a.total_to_pay) end as no_count from rpt_all_claim_with_invoice a, (select distinct claim_id from audit_trail where new_status='ClaimRejectionAccepted') b where a.claim_id=b.claim_id and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id and date(claim_created_date) between @pUploadDateFrom and @pUploadDateTo) as total_no_rejected_claims_val, ");
            sb.append("5.00 as total_no_rejected_claims_per, ");
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_all_claim_with_invoice a, (select distinct claim_id from audit_trail where new_status='AwaitingInvoicePayment') b where a.claim_id=b.claim_id and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id and date(claim_created_date) between @pUploadDateFrom and @pUploadDateTo) as total_no_approved_invoice_num, ");
            sb.append("(select case when sum(a.total_to_pay) is null then 0.00 else sum(a.total_to_pay) end as no_count from rpt_all_claim_with_invoice a, (select distinct claim_id from audit_trail where new_status='AwaitingInvoicePayment') b where a.claim_id=b.claim_id and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id and date(claim_created_date) between @pUploadDateFrom and @pUploadDateTo) as total_no_approved_invoice_val, ");
            sb.append("8.00 as total_no_approved_invoice_per, ");
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_all_claim_with_invoice a, (select distinct claim_id from audit_trail where new_status='InvoiceRejectionAccepted') b where a.claim_id=b.claim_id and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id and date(claim_created_date) between @pUploadDateFrom and @pUploadDateTo) as total_no_rejected_invoice_num, ");
            sb.append("(select case when sum(a.total_to_pay) is null then 0.00 else sum(a.total_to_pay) end as no_count from rpt_all_claim_with_invoice a, (select distinct claim_id from audit_trail where new_status='InvoiceRejectionAccepted') b where a.claim_id=b.claim_id and chorganisation_id=insurer_chorganisation.chorganisation_id and insurer_id=insurer_chorganisation.insurer_id and date(claim_created_date) between @pUploadDateFrom and @pUploadDateTo) as total_no_rejected_invoice_val, ");
            sb.append("11.00 as total_no_rejected_invoice_per, ");
            sb.append("12 as average_claim_cycle_day, ");
            sb.append("13 as average_hire_duration_day, ");
            sb.append("14.00 as average_invoice_val, ");
            sb.append("15.00 as average_penalty_val ");
            
            if(isInsReport){
                sb.append("from insurer_chorganisation insurer_chorganisation, chorganisation chorganisation ");
                sb.append("where chorganisation.id=insurer_chorganisation.chorganisation_id  ");
                sb.append("and insurer_chorganisation.insurer_id=@pUserOrgId ");
            }else{
                sb.append("from insurer_chorganisation insurer_chorganisation, insurer insurer ");
                sb.append("where insurer.id=insurer_chorganisation.insurer_id  ");
                sb.append("and insurer_chorganisation.chorganisation_id=@pUserOrgId ");                
            }
            
            String query = sb.toString();
            query = query.replaceAll("@pUploadDateFrom", "'" + DateHelper.DBDateFormat.format(dataStart) + "'");
            query = query.replaceAll("@pUploadDateTo", "'" + DateHelper.DBDateFormat.format(dataEnd) + "'");
            query = query.replaceAll("@pUserOrgId", userOrgId.toString());
            
            List result = dataService.externalQuery(query);
            List<OverviewSummaryReportByOrg> overviewSummaryReportByOrgs = new ArrayList<OverviewSummaryReportByOrg>();
            
            List<String> orgName = new ArrayList<String>();
            
            for (Object o : result) {
                Map data = (Map) o;
                
                OverviewSummaryReportByOrg overviewSummaryReportByOrg = OverviewSummaryReportByOrg.getObject(data, isInsReport);
                overviewSummaryReportByOrgs.add(overviewSummaryReportByOrg);
                
                orgName.add(overviewSummaryReportByOrg.getOrgName());
            }
            
            List<OverviewSummaryLineItem> summaries = doOverviewSummaryLineItem(overviewSummaryReportByOrgs);
            
            reportParameters.put("orgName", orgName);
            reportParameters.put("userOrgLabel",userOrgLabel);
            reportParameters.put("userOrgName",userOrgName);
            reportParameters.put("OverviewSummaryLineItems", summaries);
            
        } catch (Exception ex) {
            ex.printStackTrace();
            bAction = false;
            sActionMsg = ex.getLocalizedMessage();
        } finally {
            dataService.logSystemLog(getReportCode(), sActionMsg, bAction);
        }
        
        return reportParameters;
    }
    
    private List<OverviewSummaryLineItem> doOverviewSummaryLineItem(List<OverviewSummaryReportByOrg> inputList){
    
        List<OverviewSummaryLineItem> reportLines = getReportLineItems();
        
        for(OverviewSummaryLineItem reportLine : reportLines){
            
            System.out.println(" REPORT LINE ITEM : "+reportLine.getName());
            
            List<OverviewSummaryLineItemDetail> lineItemDetails = new ArrayList<OverviewSummaryLineItemDetail>();
            
            for(OverviewSummaryReportByOrg recordPerOrg : inputList){
                
                OverviewSummaryLineItemDetail lineItemDetail = new OverviewSummaryLineItemDetail();
                
                switch (reportLine.getLineId()) {
                    case 1:
                        lineItemDetail.setNoCount(recordPerOrg.getTotal_no_claims_num());
                        lineItemDetail.setTotalValue(recordPerOrg.getTotal_no_claims_val());
                        break;
                    case 2:
                        lineItemDetail.setNoCount(recordPerOrg.getTotal_no_accepted_claims_num());
                        lineItemDetail.setTotalValue(recordPerOrg.getTotal_no_accepted_claims_val());                        
                        lineItemDetail.setTotalPercentage(recordPerOrg.getTotal_no_accepted_claims_per());
                        break;
                    case 3:
                        lineItemDetail.setNoCount(recordPerOrg.getTotal_no_rejected_claims_num());
                        lineItemDetail.setTotalValue(recordPerOrg.getTotal_no_rejected_claims_val());                        
                        lineItemDetail.setTotalPercentage(recordPerOrg.getTotal_no_rejected_claims_per());
                        break;
                    case 4:
                        lineItemDetail.setNoCount(recordPerOrg.getTotal_no_approved_invoice_num());
                        lineItemDetail.setTotalValue(recordPerOrg.getTotal_no_approved_invoice_val());                        
                        lineItemDetail.setTotalPercentage(recordPerOrg.getTotal_no_approved_invoice_per());
                        break;
                    case 5:
                        lineItemDetail.setNoCount(recordPerOrg.getTotal_no_rejected_invoice_num());
                        lineItemDetail.setTotalValue(recordPerOrg.getTotal_no_rejected_invoice_val());                        
                        lineItemDetail.setTotalPercentage(recordPerOrg.getTotal_no_rejected_invoice_per());
                        break;
                    case 6:
                        lineItemDetail.setTotalDay(recordPerOrg.getAverage_claim_cycle_day());
                        break;
                    case 7:
                        lineItemDetail.setTotalDay(recordPerOrg.getAverage_hire_duration_day());
                        break;
                    case 8:
                        lineItemDetail.setTotalValue(recordPerOrg.getAverage_invoice_val());
                        break; 
                    case 9:
                        lineItemDetail.setTotalValue(recordPerOrg.getAverage_penalty_val());
                        break;                         
                    default: break;
                }
                
                lineItemDetails.add(lineItemDetail);
                
            }
            
            reportLine.setLineItem(lineItemDetails);
        }
        
        return reportLines;
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
    
    public void setExternalParameter(Map parameters) {
        this.externalParameter = parameters;
    }

    public String getReportTemplateFileName() {
        return "template_SummaryReport.xls";
    }

    public String getReportCode() {
        return "RPT001";
    }
    
    
    public List<OverviewSummaryLineItem> getReportLineItems(){
        
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
        lineItem.setName("Total No. Approved Invoices");
        summaries.add(lineItem);  

        lineItem = new OverviewSummaryLineItem();
        lineItem.setLineId(5);
        lineItem.setName("Total No. Rejected Invoices");
        summaries.add(lineItem);  
        
        lineItem = new OverviewSummaryLineItem();
        lineItem.setLineId(6);
        lineItem.setName("Average Claim Cycle Time");
        summaries.add(lineItem);  
        
        lineItem = new OverviewSummaryLineItem();
        lineItem.setLineId(7);
        lineItem.setName("Average Hire Duration");
        summaries.add(lineItem);  
        
        lineItem = new OverviewSummaryLineItem();
        lineItem.setLineId(8);
        lineItem.setName("Average Invoice Value");
        summaries.add(lineItem);  
        
        lineItem = new OverviewSummaryLineItem();
        lineItem.setLineId(9);
        lineItem.setName("Average Penalty Charge");
        summaries.add(lineItem);  
        
        return summaries;
    }
    
    
}
