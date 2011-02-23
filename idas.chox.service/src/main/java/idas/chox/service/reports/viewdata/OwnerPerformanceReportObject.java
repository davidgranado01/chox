

package idas.chox.service.reports.viewdata;


import java.util.ArrayList;
import java.util.List;

public class OwnerPerformanceReportObject {
    private String workgroup;
    private Integer id;
    private List<OwnerPerformanceLineItem> owner;

    public OwnerPerformanceReportObject() {
        owner = new ArrayList<OwnerPerformanceLineItem>();
    }

    public List<OwnerPerformanceLineItem> getOwner() {
        return owner;
    }

    public void setOwner(List<OwnerPerformanceLineItem> owner) {
        this.owner = owner;
    }

    public String getWorkgroup() {
        return workgroup;
    }

    public void setWorkgroup(String workgroup) {
        this.workgroup = workgroup;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
