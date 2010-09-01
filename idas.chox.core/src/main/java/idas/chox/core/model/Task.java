package idas.chox.core.model;

import java.io.Serializable;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author John
 */
public class Task extends Entity implements Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(Task.class);

    private Claim claim;
    private Date dueDate;
    private Date completedDate;
    private WebUser completedBy;
    private String type;
    private String description;
    private Boolean complete;
    private Boolean insurer;
    private Task relatedTask;
    private int visibility;
    private String visibilityRole;

    public Claim getClaim() {
        return claim;
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
    }

    public Boolean getComplete() {
        return complete;
    }

    public void setComplete(Boolean complete) {
        this.complete = complete;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public String getVisibilityRole() {
        return visibilityRole;
    }

    public void setVisibilityRole(String visibilityRole) {
        this.visibilityRole = visibilityRole;
    }


    public String getType() {
        if (type == null) {
            type = this.getClass().getSimpleName();
        }
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public TaskType getTaskType(){
    	return TaskType.valueOf(getType());
    }

    public WebUser getCompletedBy() {
        return completedBy;
    }

    public void setCompletedBy(WebUser completedBy) {
        this.completedBy = completedBy;
    }

    public Boolean getInsurer() {
        return insurer;
    }

    public void setInsurer(Boolean insurer) {
        this.insurer = insurer;
    }

    public Task getRelatedTask() {
        return relatedTask;
    }

    public void setRelatedTask(Task relatedTask) {
        this.relatedTask = relatedTask;
    }

}
