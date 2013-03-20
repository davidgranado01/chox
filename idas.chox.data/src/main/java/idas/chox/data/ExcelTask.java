package idas.chox.data;

import java.util.Date;

public class ExcelTask {
    
    private String supplierReference;
    private Date taskDueDate;
    private String taskType;
    private String taskDescription;
    private Date taskCreatedDate;
    private String taskCreatedBy;
    private String taskCreatedByOrg;
    private String taskOwner = "N/A";
    private String taskRoleAssignedTo = "N/A";
    private String taskWrokgroup;
    private String taskCurrentClaimStatus;
    private String taskStatusOfClaimWhenTaskCreated;
    private boolean taskComplete;

    public boolean isTaskComplete() {
        return taskComplete;
    }

    public void setTaskComplete(boolean taskComplete) {
        this.taskComplete = taskComplete;
    }

    public String getSupplierReference() {
        return supplierReference;
    }

    public void setSupplierReference(String supplierReference) {
        this.supplierReference = supplierReference;
    }

    public Date getTaskDueDate() {
        return taskDueDate;
    }

    public void setTaskDueDate(Date taskDueDate) {
        this.taskDueDate = taskDueDate;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public Date getTaskCreatedDate() {
        return taskCreatedDate;
    }

    public void setTaskCreatedDate(Date taskCreatedDate) {
        this.taskCreatedDate = taskCreatedDate;
    }

    public String getTaskCreatedBy() {
        return taskCreatedBy;
    }

    public void setTaskCreatedBy(String taskCreatedBy) {
        this.taskCreatedBy = taskCreatedBy;
    }

    public String getTaskCreatedByOrg() {
        return taskCreatedByOrg;
    }

    public void setTaskCreatedByOrg(String taskCreatedByOrg) {
        this.taskCreatedByOrg = taskCreatedByOrg;
    }

    public String getTaskOwner() {
        return taskOwner;
    }

    public void setTaskOwner(String taskOwner) {
        this.taskOwner = taskOwner;
    }

    public String getTaskRoleAssignedTo() {
        return taskRoleAssignedTo;
    }

    public void setTaskRoleAssignedTo(String taskRoleAssignedTo) {
        this.taskRoleAssignedTo = taskRoleAssignedTo;
    }

    public String getTaskWrokgroup() {
        return taskWrokgroup;
    }

    public void setTaskWrokgroup(String taskWrokgroup) {
        this.taskWrokgroup = taskWrokgroup;
    }

    public String getTaskCurrentClaimStatus() {
        return taskCurrentClaimStatus;
    }

    public void setTaskCurrentClaimStatus(String taskCurrentClaimStatus) {
        this.taskCurrentClaimStatus = taskCurrentClaimStatus;
    }

    public String getTaskStatusOfClaimWhenTaskCreated() {
        return taskStatusOfClaimWhenTaskCreated;
    }

    public void setTaskStatusOfClaimWhenTaskCreated(String taskStatusOfClaimWhenTaskCreated) {
        this.taskStatusOfClaimWhenTaskCreated = taskStatusOfClaimWhenTaskCreated;
    }
    
}
