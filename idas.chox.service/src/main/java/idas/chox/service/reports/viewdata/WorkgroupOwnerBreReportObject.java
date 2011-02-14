/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package idas.chox.service.reports.viewdata;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rajareddydodda
 */
public class WorkgroupOwnerBreReportObject {

    private String workgroup;
    private Integer id;
    private List<WorkgroupOwnerBreLineItem> owner;

    public WorkgroupOwnerBreReportObject() {
        owner = new ArrayList<WorkgroupOwnerBreLineItem>();
    }

    /**
     * @return the workgroup
     */
    public String getWorkgroup() {
        return workgroup;
    }

    /**
     * @param workgroup the workgroup to set
     */
    public void setWorkgroup(String workgroup) {
        this.workgroup = workgroup;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the owner
     */
    public List<WorkgroupOwnerBreLineItem> getOwner() {
        return owner;
    }

   

}
