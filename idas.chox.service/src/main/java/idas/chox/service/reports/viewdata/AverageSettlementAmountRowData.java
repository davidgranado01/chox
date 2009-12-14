package idas.chox.service.reports.viewdata;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

public class AverageSettlementAmountRowData {
    private int dateMonth;
    private int dateYear;
    private int chorganisationId;
    private int totalRecord = 0;
    private BigDecimal totalToPay = new BigDecimal("0.00");
    private BigDecimal totalAvg = new BigDecimal("0.00");

    public static AverageSettlementAmountRowData getObject(Map data) {
        AverageSettlementAmountRowData result = new AverageSettlementAmountRowData();
        result.setDateMonth(getIntegerValue(data.get("date_month".toLowerCase())));
        result.setDateYear(getIntegerValue(data.get("date_year".toLowerCase())));
        result.setChorganisationId(getIntegerValue(data.get("chorganisation_id".toLowerCase())));
        result.setTotalRecord(getIntegerValue(data.get("total_record".toLowerCase())));
        result.setTotalToPay((BigDecimal)data.get("total_to_pay".toLowerCase()));
        result.setTotalAvg((BigDecimal)data.get("total_avg".toLowerCase()));
        return result;
    }
    
    private static Integer getIntegerValue(Object v) {
        if (v.getClass().equals(Integer.class)) {
            return (Integer) v;
        } else if (v.getClass().equals(BigInteger.class)) {
            return ((BigInteger) v).intValue();
        } else {
            return 0;
        }
    }
    
    public int getChorganisationId() {
        return chorganisationId;
    }

    public void setChorganisationId(int chorganisationId) {
        this.chorganisationId = chorganisationId;
    }

    public int getDateMonth() {
        return dateMonth;
    }

    public void setDateMonth(int dateMonth) {
        this.dateMonth = dateMonth;
    }

    public int getDateYear() {
        return dateYear;
    }

    public void setDateYear(int dateYear) {
        this.dateYear = dateYear;
    }

    public BigDecimal getTotalAvg() {
        return totalAvg;
    }

    public void setTotalAvg(BigDecimal totalAvg) {
        this.totalAvg = totalAvg;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public BigDecimal getTotalToPay() {
        return totalToPay;
    }

    public void setTotalToPay(BigDecimal totalToPay) {
        this.totalToPay = totalToPay;
    }
    
    
}
