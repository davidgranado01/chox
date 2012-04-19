

package idas.chox.service.reports.viewdata;


import java.util.ArrayList;
import java.util.List;

public class HandlerPerformanceReportObject {
    private String workgroup;
    private Integer id;
    private List<HandlerPerformanceLineItem> owner;

    public HandlerPerformanceReportObject() {
        owner = new ArrayList<HandlerPerformanceLineItem>();
    }

    public List<HandlerPerformanceLineItem> getOwner() {
        return owner;
    }

    public void setOwner(List<HandlerPerformanceLineItem> owner) {
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
