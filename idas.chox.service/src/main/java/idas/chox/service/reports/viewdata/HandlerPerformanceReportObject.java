

package idas.chox.service.reports.viewdata;


import java.util.ArrayList;
import java.util.List;

public class HandlerPerformanceReportObject {
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
