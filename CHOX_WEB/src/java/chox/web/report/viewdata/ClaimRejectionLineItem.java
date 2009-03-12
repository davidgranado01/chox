package chox.web.report.viewdata;

import java.util.ArrayList;
import java.util.List;

public class ClaimRejectionLineItem {

    private Integer id;
    private String name;
    private String displayName;
    private List<ClaimRejectionLineItemDetail> ReportColumns = new ArrayList<ClaimRejectionLineItemDetail>();

    
    public List<ClaimRejectionLineItemDetail> getReportColumns() {
        return ReportColumns;
    }

    public void setReportColumns(List<ClaimRejectionLineItemDetail> ReportColumns) {
        this.ReportColumns = ReportColumns;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public static String getDisplayNameMap(String name){
        return name;
    }
    
}
