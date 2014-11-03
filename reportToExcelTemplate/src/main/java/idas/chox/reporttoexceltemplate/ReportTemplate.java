package idas.chox.reporttoexceltemplate;

/**
 *
 * @author John
 */
public class ReportTemplate {
    private final String templateName;
    private final int rowOffset;

    public ReportTemplate(String reportFileName) {
        if (reportFileName.contains("AccidentExchange-Processed_Notifications_Report_DLG")) {
            templateName = "AX_Template.xls";
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
