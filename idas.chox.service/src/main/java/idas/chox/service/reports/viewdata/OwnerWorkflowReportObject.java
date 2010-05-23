/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package idas.chox.service.reports.viewdata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author John
 */
public class OwnerWorkflowReportObject {
    private String workgroup;
    private Integer id;
    private List<OwnerWorkflowLineItem> owner;

    public OwnerWorkflowReportObject() {
        owner = new ArrayList<OwnerWorkflowLineItem>();
    }

    public List<OwnerWorkflowLineItem> getOwner() {
        return owner;
    }

    public void setOwner(List<OwnerWorkflowLineItem> owner) {
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
