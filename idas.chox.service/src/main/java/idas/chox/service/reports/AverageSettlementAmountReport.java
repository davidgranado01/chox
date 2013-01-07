package idas.chox.service.reports;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.IdLookupItem;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.WebUser;
import idas.chox.core.services.ReportDataService;
import idas.chox.core.util.DateHelper;
import idas.chox.data.services.BaseDataService;
import idas.chox.service.reports.viewdata.AverageSettlementAmountDtlViewData;
import idas.chox.service.reports.viewdata.AverageSettlementAmountReportObject;
import idas.chox.service.reports.viewdata.AverageSettlementAmountRowData;
import idas.chox.service.reports.viewdata.AverageSettlementAmountViewData;

public class AverageSettlementAmountReport implements Report {
    private static final Logger LOG = LoggerFactory.getLogger(AverageSettlementAmountReport.class);

    private Map externalParameter;
    private List<String> reportParameterNames;
    private BaseDataService baseDataService;
    private ReportDataService reportDataService;

    @Override
    public void setBaseDataService(BaseDataService baseDataService) {
        this.baseDataService = baseDataService;
    }
    
    public AverageSettlementAmountReport() {
        reportParameterNames = new ArrayList<String>();
    }

    @Override
    public String getReportTemplateFileName() {
        return "template_AverageSettlementAmountReport.xls";
    }

    @Override
    public void setExternalParameter(Map parameters) {
        this.externalParameter = parameters;
    }

    @Override
    public HashMap getReportParameters() throws Exception {

        HashMap reportParameters = new HashMap();

        try {

            WebUser currentUser = ((WebUser) externalParameter.get("CurrentUser"));
            // PermissionedUser currentUser = ((PermissionedUser) externalParameter.get("CurrentUser"));
            final Date dataStart = DateHelper.getLocalDateFormat().parse(((String[]) externalParameter.get("DateStart"))[0]);
            Date dataEnd = DateHelper.getLocalDateFormat().parse(((String[]) externalParameter.get("DateEnd"))[0]);
            if(dataStart != null && dataEnd != null && dataEnd.before(dataStart)){
                throw new Exception("End date (" + dataEnd.toString() + ") is before start date (" + dataStart.toString() +  ") ");
            }
            dataEnd = DateHelper.setEndOfDay(dataEnd);
            final String insurerId = ((String[]) externalParameter.get("insurerId"))[0];

            String insurerName;
            Integer iInsurerId;

            Insurer ins;
            ReportHelper reportHelper = new ReportHelper();

            if (!insurerId.equalsIgnoreCase("")) {

                iInsurerId = Integer.valueOf(insurerId);
                ins = reportHelper.getInsurer(iInsurerId, this.baseDataService);

            } else {
                
                ins = currentUser.getInsurer();
                iInsurerId = ins.getId();
            }

            insurerName = ins.getName();

            // DEFINE START DATE TO FIRST DAY OF START MONTH
            Date tDateFrom = DateHelper.getLocalDateFormat().parse(((String[]) externalParameter.get("DateStart"))[0]);
            tDateFrom.setDate(1);

            // DEFINE END DATE TO FIRST DAT OF NEXT MONTH
            Date tDateTo = DateHelper.getLocalDateFormat().parse(((String[]) externalParameter.get("DateEnd"))[0]);
            tDateTo = DateHelper.addMonth(tDateTo, 1);
            tDateTo.setDate(1);

            List<AverageSettlementAmountViewData> reportRows = new ArrayList<AverageSettlementAmountViewData>();
            List<Chorganisation> chorg = getCreditHire(iInsurerId);

            boolean isReadName = true;
            List<String> orgNames = new ArrayList<String>();
            orgNames.add("All");

            do {
                List<AverageSettlementAmountDtlViewData> dtls = new ArrayList<AverageSettlementAmountDtlViewData>();

                int selectedMonth = DateHelper.getMonth(tDateFrom);
                int selectedYear = DateHelper.getYear(tDateFrom);

                List<AverageSettlementAmountRowData> rows = getReportRecordRows(dataStart, dataEnd, iInsurerId, selectedMonth, selectedYear);

                if (chorg.size() > 0) {

                    AverageSettlementAmountDtlViewData data = new AverageSettlementAmountDtlViewData();
                    data.setChorganisationId(-1);
                    dtls.add(data);

                    BigDecimal ttlToPay = new BigDecimal("0.00");
                    int recordCount = 0;

                    for (Chorganisation c : chorg) {

                        data = processRecord(rows, c.getId());

                        ttlToPay = ttlToPay.add(data.getTotalToPay());
                        recordCount = recordCount + data.getRecortCount();

                        dtls.add(data);

                        if (isReadName) {
                            orgNames.add(c.getName());
                        }
                    }

                    if (ttlToPay.doubleValue() > 0 && recordCount > 0) {
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

            } while (tDateFrom.before(tDateTo));

            AverageSettlementAmountReportObject reportObject = new AverageSettlementAmountReportObject();
            reportObject.setCreatedDate(new Date());
            reportObject.setSettlementDateFrom(dataStart);
            reportObject.setSettlementDateTo(dataEnd);

            reportParameters.put("reportHeaderName", orgNames);
            reportParameters.put("reportRows", reportRows);
            reportParameters.put("reportObj", reportObject);
            reportParameters.put("organisationName", insurerName);

        } catch (Exception ex) {
            LOG.error("Exception generation report: {}", ex.getMessage());
            if (ex.getCause() != null) {
                LOG.error("    Caused by: {}", ex.getCause().getMessage());
            }
            throw ex;
        }

        return reportParameters;
    }

    private AverageSettlementAmountDtlViewData processRecord(
            List<AverageSettlementAmountRowData> rows,
            int chorgId) {

        AverageSettlementAmountDtlViewData data = new AverageSettlementAmountDtlViewData();

        int iTotalRecord = 0;
        BigDecimal bTotalAvg = new BigDecimal("0.00");
        BigDecimal bTotalToPay = new BigDecimal("0.00");

        for (AverageSettlementAmountRowData r : rows) {

            if (r.getChorganisationId() == chorgId) {

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

    private List<Chorganisation> getCreditHire(int insurerId) {

        List<Chorganisation> results = new ArrayList<Chorganisation>();

        try {

            StringBuilder sb = new StringBuilder();
            sb.append("select a.id as id, a.name as name from chorganisation a ");
            sb.append("inner join insurer_chorganisation b on a.id = b.chorganisation_id and b.status=true ");
            sb.append("where a.status=true and b.insurer_id=:pInsurerId");

            Map extParameters = new HashMap();
            extParameters.put("pInsurerId", insurerId);
            List result = reportDataService.getReportData(sb.toString(), extParameters, IdLookupItem.class);

            for (Object o : result) {
                IdLookupItem data = (IdLookupItem) o;
                Chorganisation item = new Chorganisation();
                item.setId(data.getId());
                item.setName(data.getName());
                results.add(item);
            }

        } catch (Exception ex) {
            LOG.error("Exception getting CHOs for insurer id={}: {}", insurerId, ex.getMessage());
            if (ex.getCause() != null) {
                LOG.error("    Caused by: {}", ex.getCause().getMessage());
            }
        }

        return results;
    }

    private List<AverageSettlementAmountRowData> getReportRecordRows(Date startDate, Date endDate, int insurerId, int iMonth, int iYear) {

        List<AverageSettlementAmountRowData> rows = new ArrayList<AverageSettlementAmountRowData>();

        StringBuilder sb = new StringBuilder();

        sb.append("select * from (select date_part('month', audit.update_date) as date_month, date_part('year', audit.update_date) as date_year, ");
        sb.append("claim.chorganisation_id, count(*) as total_record, sum(invoice.total_to_pay) as total_to_pay, ");
        sb.append("case when sum(invoice.total_to_pay) is not null or sum(invoice.total_to_pay) > 0 then round(sum(invoice.total_to_pay)/count(*), 2) else 0 end as total_avg ");
        sb.append("from audit_trail audit, claim claim, invoice invoice where ");
        sb.append("invoice.id=claim.invoice_id and claim.id=audit.claim_id ");
        sb.append("and audit.reverted = false and audit.new_status='PaymentReceived' and audit.original_status='InvoicePaymentLogged' ");
        sb.append("and claim.insurer_id=:pInsurerId and claim.status!='ClaimClosed' ");
        sb.append("and audit.update_date between date(:pDateFrom) and date(:pDateTo) ");
        sb.append("group by date_month, date_year, chorganisation_id) a ");
        sb.append("where date_month=:pMonth and date_year=:pYear ");
        String query = sb.toString();

        Map paramMap = new HashMap();
        paramMap.put("pInsurerId", insurerId);
        paramMap.put("pDateFrom", "'" + DateHelper.getDBDateFormat().format(startDate) + "'");
        paramMap.put("pDateTo", "'" + DateHelper.getDBDateFormat().format(endDate) + "'");
        paramMap.put("pMonth", iMonth);
        paramMap.put("pYear", iYear);

        List result = reportDataService.getReportData(query, paramMap);

        for (Object o : result) {
            Map data = (Map) o;
            AverageSettlementAmountRowData row = AverageSettlementAmountRowData.getObject(data);
            rows.add(row);
        }

        return rows;
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
    public void setReportDataService(ReportDataService reportDataService) {
        this.reportDataService = reportDataService;
    }

    @Override
    public String getReportCode() {
        return "RPT006";
    }
    
    @Override
    public short[] getColumnsToHide() {
        return null;
    }
}
