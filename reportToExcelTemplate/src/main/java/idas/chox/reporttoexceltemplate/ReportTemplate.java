package idas.chox.reporttoexceltemplate;

/**
 *
 * @author John
 */
public class ReportTemplate {
    private final String templateName;
    private final int rowOffset;

    public ReportTemplate(String reportFileName) {
        if (reportFileName.contains("AccidentExchange-Processed_Notifications_Report_RSA")) {
            templateName = "AX_Template-RSA.xls";
            rowOffset = 5; // excel rownumber of format row  - 2
        } else if (reportFileName.contains("AccidentExchange-Processed_Notifications_Report_DLG")) {
            templateName = "AX_Template-DLG.xls";
            rowOffset = 5; // excel rownumber of format row  - 2
        } else if (reportFileName.contains("AccidentExchange-Processed_Notifications_Report_LV")) {
            templateName = "AX_Template-LV.xls";
            rowOffset = 5; // excel rownumber of format row  - 2
        } else {
            templateName = ""; rowOffset = 0;
        }
    }

    public String getTemplateName() {
        return templateName;
    }

    public int getRowOffset() {
        return rowOffset;
    }
    
    
}
