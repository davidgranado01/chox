package idas.chox.service.reports.viewdata;

import java.util.ArrayList;
import java.util.List;

public class HandlerActionsReportObject {

    private String workgroup;
    private Integer id;
    private List<HandlerActionsStatusLineItem> owner;

    public HandlerActionsReportObject() {
        owner = new ArrayList<HandlerActionsStatusLineItem>();
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

    public List<HandlerActionsStatusLineItem> getOwner() {
        return owner;
    }

   

}
