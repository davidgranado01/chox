package idas.chox.core.tasks;

import java.io.Serializable;
import java.util.Set;

public class TaskSearchCriteria implements Serializable {

    private Set<Integer> supplierClaimOwnerIds;
    private Set<Integer> claimOwnerIds;
    private Set<Integer> workgroupIds;
    private int start;
    private int limit;
    private String sort;
    private String dir;
    private boolean showAssignedTasksOnly = true;
    private boolean loadFilterPanelSelectionFromSession;

    public Set<Integer> getSupplierClaimOwnerIds() {
        return supplierClaimOwnerIds;
    }

    public void setSupplierClaimOwnerIds(Set<Integer> supplierClaimOwnerIds) {
        if (supplierClaimOwnerIds.contains(null) || supplierClaimOwnerIds.contains(0)) {
            this.supplierClaimOwnerIds = null;
        } else {
            this.supplierClaimOwnerIds = supplierClaimOwnerIds;
        }
    }

    public Set<Integer> getClaimOwnerIds() { return claimOwnerIds; }

    public void setClaimOwnerIds(Set<Integer> claimOwnerIds) {
        if (claimOwnerIds.contains(null) || claimOwnerIds.contains(0)){
            this.claimOwnerIds = null;
        } else {
            this.claimOwnerIds = claimOwnerIds;
        }
    }

    public Set<Integer> getWorkgroupIds() { return workgroupIds; }

    public void setWorkgroupIds(Set<Integer> workgroupIds) {
        if (workgroupIds.contains(null) || workgroupIds.contains(0)){
            this.workgroupIds = null;
        } else {
            this.workgroupIds = workgroupIds;
        }
    }

    /*
     * Please note this method will return only Workgroup Ids from the
     * loaded(model) claimSearchCriteria and not from available Workgroup Id.
     */
    public String getWorkgroupIdsAsString() {

        if (getWorkgroupIds() != null && !getWorkgroupIds().isEmpty()) {
            StringBuilder returnString = new StringBuilder();
            getWorkgroupIds().forEach((i) -> {
                returnString.append(i.toString()).append(",");
            });
            return returnString.toString().substring(0, returnString.length() - 1);
        }
        return "";
    }

    /*
     * Please note this method will return only Supplier Claim owner Ids from the
     * loaded(model) claimSearchCriteria and not from available Supplier Claim owner Id.
     */
    public String getSupplierClaimOwnerIdsAsString() {

        if (getSupplierClaimOwnerIds() != null && !getSupplierClaimOwnerIds().isEmpty()) {
            StringBuilder returnString = new StringBuilder();
            getSupplierClaimOwnerIds().forEach((i) -> {
                returnString.append(i.toString()).append(",");
            });
            return returnString.toString().substring(0, returnString.length() - 1);
        }
        return "";
    }

    /*
     * Please note this method will return only Supplier Claim owner Ids from the
     * loaded(model) claimSearchCriteria and not from available Supplier Claim owner Id.
     */
    public String getClaimOwnerIdsAsString() {

        if (getClaimOwnerIds() != null && !getClaimOwnerIds().isEmpty()) {
            StringBuilder returnString = new StringBuilder();
            getClaimOwnerIds().forEach((i) -> {
                returnString.append(i.toString()).append(",");
            });
            return returnString.toString().substring(0, returnString.length() - 1);
        }
        return "";
    }

    public boolean getLoadFilterPanelSelectionFromSession() {
        return loadFilterPanelSelectionFromSession;
    }

    public void setLoadFilterPanelSelectionFromSession(boolean loadFilterPanelSelectionFromSession) {
        this.loadFilterPanelSelectionFromSession = loadFilterPanelSelectionFromSession;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        if (sort != null && sort.equals("createdBy")) {
            this.sort = "raisedBy";
        } else {
            this.sort = sort;
        }
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public boolean isShowAssignedTasksOnly() {
        return showAssignedTasksOnly;
    }

    public void setShowAssignedTasksOnly(boolean showAssignedTasksOnly) {
        this.showAssignedTasksOnly = showAssignedTasksOnly;
    }
}
