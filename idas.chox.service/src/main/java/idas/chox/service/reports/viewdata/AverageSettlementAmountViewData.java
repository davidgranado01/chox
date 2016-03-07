package idas.chox.service.reports.viewdata;

import java.util.List;

public class AverageSettlementAmountViewData {
    private int month;
    private int year;
    private String labelTitle;
    private List<AverageSettlementAmountDtlViewData> reportColumns;

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
    
    public String getLabelTitle(){
        
        String sReturn = "";
        
        switch (this.month) {
            case 1:  sReturn = "January"; break;
            case 2:  sReturn = "February"; break;
            case 3:  sReturn = "March"; break;
            case 4:  sReturn = "April"; break;
            case 5:  sReturn = "May"; break;
            case 6:  sReturn = "June"; break;
            case 7:  sReturn = "July"; break;
            case 8:  sReturn = "August"; break;
            case 9:  sReturn = "September"; break;
            case 10: sReturn = "October"; break;
            case 11: sReturn = "November"; break;
            case 12: sReturn = "December"; break;
            default: sReturn = "Invalid month."; break;
        }
        
        return sReturn + " - " + this.year;
    }

    public List<AverageSettlementAmountDtlViewData> getReportColumns() {
        return reportColumns;
    }

    public void setReportColumns(List<AverageSettlementAmountDtlViewData> reportColumns) {
        this.reportColumns = reportColumns;
    }
    
}
