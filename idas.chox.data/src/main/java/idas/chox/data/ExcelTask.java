package idas.chox.data;

import java.util.Date;

public class ExcelTask {
    
    String supplierReference;
    Date taskDueDate;
    String taskType;
    String taskDescription;
    Date taskCreatedDate;
    String taskCreatedBy;
    String taskCreatedByOrg;
    String taskOwner = "N/A";
    String taskRoleAssignedTo = "N/A";
    String taskWrokgroup;
    String taskCurrentClaimStatus;
    String taskStatusOfClaimWhenTaskCreated;

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
