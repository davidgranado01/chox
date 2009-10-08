package chox.web.report;

import chox.Util.DateHelper;
import chox.model.Chorganisation;
import chox.model.IdLookupItem;
import chox.model.Insurer;
import chox.services.ChorganisationService;
import chox.services.DataService;
import chox.services.InsurerService;
import chox.web.actions.BaseAction;
import chox.web.report.viewdata.AverageSettlementAmountDtlViewData;
import chox.web.report.viewdata.AverageSettlementAmountReportObject;
import chox.web.report.viewdata.AverageSettlementAmountRowData;
import chox.web.report.viewdata.AverageSettlementAmountViewData;
import chox.web.security.PermissionedUser;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class AverageSettlementAmountReport extends BaseAction implements Report{

    Map externalParameter;
    List<String> reportParameterNames;
    private DataService dataService;

    public AverageSettlementAmountReport() {
        reportParameterNames = new ArrayList<String>();
    }

    public String getReportTemplateFileName() {
        return "template_AverageSettlementAmountReport.xls";
    }

    public void setExternalParameter(Map parameters) {
        this.externalParameter = parameters;
    }
    
    public HashMap getReportParameters() {
        
        HashMap reportParameters = new HashMap();

        boolean bAction = true;
        String sActionMsg = "";
        
        try {
            
            PermissionedUser currentUser = ((PermissionedUser) externalParameter.get("CurrentUser"));
            final Date dataStart = DateHelper.LocalDateFormat.parse(((String[]) externalParameter.get("DateStart"))[0]);
            final Date dataEnd = DateHelper.LocalDateFormat.parse(((String[]) externalParameter.get("DateEnd"))[0]);
            final String insurerId = ((String[]) externalParameter.get("insurerId"))[0];
            
            String insurerName = "";
            Integer iInsurerId = -1;       
            
            Insurer ins = new Insurer();
            ReportHelper reportHelper = new ReportHelper();
            
            if(!insurerId.equalsIgnoreCase("")){
                
                iInsurerId = Integer.valueOf(insurerId);
                ins = reportHelper.getInsurer(iInsurerId, this.dataService);
                
            }else{
                ins = currentUser.getUser().getInsurer();
                iInsurerId = ins.getId();
            }
            
            insurerName = ins.getName();
            
            // DEFINE START DATE TO FIRST DAY OF START MONTH
            Date tDateFrom = DateHelper.LocalDateFormat.parse(((String[]) externalParameter.get("DateStart"))[0]);
            tDateFrom.setDate(1);

            // DEFINE END DATE TO FIRST DAT OF NEXT MONTH
            Date tDateTo = DateHelper.LocalDateFormat.parse(((String[]) externalParameter.get("DateEnd"))[0]);
            tDateTo = DateHelper.addMonth(tDateTo, 1);
            tDateTo.setDate(1);

            List<AverageSettlementAmountViewData> reportRows = new ArrayList<AverageSettlementAmountViewData>();
            List<Chorganisation> chorg = getCreditHire(iInsurerId);
            
            boolean isReadName = true;
            List<String> orgNames = new ArrayList<String>();
            orgNames.add("All");
            
            do{
                List<AverageSettlementAmountDtlViewData> dtls = new ArrayList<AverageSettlementAmountDtlViewData>();
                
                int selectedMonth = DateHelper.getMonth(tDateFrom);
                int selectedYear = DateHelper.getYear(tDateFrom);
                
                List<AverageSettlementAmountRowData> rows = getReportRecordRows(dataStart, dataEnd, iInsurerId, selectedMonth, selectedYear);
                
                if(chorg.size()>0){
                    
                    AverageSettlementAmountDtlViewData data = new AverageSettlementAmountDtlViewData();
                    data.setChorganisationId(-1);
                    dtls.add(data);

                    BigDecimal ttlToPay = new BigDecimal("0.00");
                    int recordCount = 0;

                    for(Chorganisation c : chorg){
                        
                        data = processRecord(rows, c.getId());
                        
                        ttlToPay = ttlToPay.add(data.getTotalToPay());
                        recordCount = recordCount + data.getRecortCount();
                        
                        dtls.add(data);
                        
                        if(isReadName){
                            orgNames.add(c.getName());
                        }
                    }
                    
                    if(ttlToPay.doubleValue()>0 && recordCount>0){
                        double totalAvg = ttlToPay.doubleValue() / recordCount;
                        dtls.get(0).setValue(new BigDecimal(totalAvg));
                    }
                    
                    
                    
                }
                
                // SET VALUE
                AverageSettlementAmountViewData reportRow = new AverageSettlementAmountViewData();
                reportRow.setMonth(selectedMonth);
                reportRow.setYear(selectedYear);
                reportRow.setReportColumns(dtls);
                reportRows.add(reportRow);
                
                // SET CONDITION
                tDateFrom = DateHelper.addMonth(tDateFrom, 1);
                isReadName = false;
                
            }while(tDateFrom.before(tDateTo));
            
            AverageSettlementAmountReportObject reportObject = new AverageSettlementAmountReportObject();
            reportObject.setCreatedDate(new Date());
            reportObject.setSettlementDateFrom(dataStart);
            reportObject.setSettlementDateTo(dataEnd);
                      
            reportParameters.put("reportHeaderName", orgNames);
            reportParameters.put("reportRows", reportRows);
            reportParameters.put("reportObj", reportObject);
            reportParameters.put("organisationName", insurerName);
            
        } catch (Exception ex) {
            ex.printStackTrace();
            bAction = false;
            sActionMsg = ex.getLocalizedMessage();
        } finally {
            // dataService.logSystemLog(getReportCode(), sActionMsg, bAction);
        }
        
        return reportParameters;
    }
    
    private AverageSettlementAmountDtlViewData processRecord(
            List<AverageSettlementAmountRowData> rows, 
            int chorgId){
        
        AverageSettlementAmountDtlViewData data = new AverageSettlementAmountDtlViewData();

        int iTotalRecord = 0;
        BigDecimal bTotalAvg = new BigDecimal("0.00");
        BigDecimal bTotalToPay = new BigDecimal("0.00");
        
        for(AverageSettlementAmountRowData r : rows){

            if(r.getChorganisationId()==chorgId){
                
                iTotalRecord = r.getTotalRecord();
                bTotalAvg = r.getTotalAvg();
                bTotalToPay = r.getTotalToPay();
                
                break;
            }

        }

        data.setChorganisationId(chorgId);
        data.setRecortCount(iTotalRecord);
        data.setValue(bTotalAvg);
        data.setTotalToPay(bTotalToPay);
        
        return data;
    }
    
    private List<Chorganisation> getCreditHire(int insurerId){
        
        List<Chorganisation> results = new ArrayList<Chorganisation>();

        try {
            
            StringBuffer sb = new StringBuffer();
            sb.append("select a.id as id, a.name as name from chorganisation ");
            sb.append("a inner join insurer_chorganisation b on a.id = b.chorganisation_id and b.status=true ");
            sb.append("where a.status=true and b.insurer_id=:pInsurerId");
            
            Map extParameters = new HashMap();
            extParameters.put("pInsurerId", insurerId);
            List result = dataService.externalQuery(sb.toString(), extParameters, IdLookupItem.class);
            
            for(Object o : result){
                IdLookupItem data = (IdLookupItem) o;
                Chorganisation item = new Chorganisation();
                item.setId(data.getId());
                item.setName(data.getName());
                results.add(item);
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return results;
    }
    
    private List<AverageSettlementAmountRowData> getReportRecordRows(Date startDate, Date endDate, int insurerId, int iMonth, int iYear){
        
        List<AverageSettlementAmountRowData> rows = new ArrayList<AverageSettlementAmountRowData>();
        
        StringBuffer sb = new StringBuffer();  
        
        sb.append("select * from (select date_part('month', audit.update_date) as date_month, date_part('year', audit.update_date) as date_year, "); 
        sb.append("claim.chorganisation_id, count(*) as total_record, sum(invoice.total_to_pay) as total_to_pay, "); 
        sb.append("case when sum(invoice.total_to_pay) is not null or sum(invoice.total_to_pay) > 0 then round(sum(invoice.total_to_pay)/count(*), 2) else 0 end as total_avg "); 
        sb.append("from audit_trail audit, claim claim, invoice invoice where "); 
        sb.append("invoice.id=claim.invoice_id and claim.id=audit.claim_id "); 
        sb.append("and audit.new_status='PaymentReceived' and audit.original_status='InvoicePaymentLogged' "); 
        sb.append("and claim.insurer_id=:pInsurerId and claim.status!='ClaimClosed' "); 
        sb.append("and audit.update_date between date(:pDateFrom) and date(:pDateTo) "); 
        sb.append("group by date_month, date_year, chorganisation_id) a "); 
        sb.append("where date_month=:pMonth and date_year=:pYear "); 
        String query = sb.toString();            

        Map paramMap = new HashMap();
        paramMap.put("pInsurerId", insurerId);
        paramMap.put("pDateFrom", "'" + DateHelper.DBDateFormat.format(startDate) + "'");
        paramMap.put("pDateTo", "'" + DateHelper.DBDateFormat.format(endDate) + "'");
        paramMap.put("pMonth", iMonth);
        paramMap.put("pYear", iYear);
        
        List result = dataService.externalQuery(query, paramMap);
        
        for (Object o : result) {
            Map data = (Map) o;
            AverageSettlementAmountRowData row = AverageSettlementAmountRowData.getObject(data);
            rows.add(row);
        }

        return rows;
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
        return "RPT006";
    }
}