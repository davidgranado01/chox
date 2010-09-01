package idas.chox.web.actions;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Task;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.TaskService;
import idas.chox.web.viewdata.TaskViewData;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author John
 */
public class TasksAction extends BaseAction {
    private static final Logger LOG = LoggerFactory.getLogger(TasksAction.class);
    private TaskService taskService;
    private ClaimService claimService;
    private List<Task> tasks;
    private JSONArray jObject;
    private boolean hideCompleted;
    private int selectedTaskId;
    private String taskDescription;
    private String taskType;
    private Date dueDate;
    private boolean linkToClaim;
    private String choReference;
    private String visibilityRole;
    private int visibility;
    private int claimId = -1;

    public void setSelectedTaskId(int selectedTaskId) {
        this.selectedTaskId = selectedTaskId;
    }

    public void setHideCompleted(boolean hideCompleted) {
        this.hideCompleted = hideCompleted;
    }

    public void setChoReference(String choReference) {
        this.choReference = choReference;
    }

    public void setLinkToClaim(boolean linkToClaim) {
        this.linkToClaim = linkToClaim;
    }

    public void setDueDate(Date dueDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dueDate);
        cal.add(Calendar.HOUR, 23);
        cal.add(Calendar.MINUTE, 59);
        cal.add(Calendar.SECOND, 59);
        this.dueDate = cal.getTime();
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public void setVisibilityRole(String visibilityRole) {
        this.visibilityRole = visibilityRole;
    }

    @Override
    public String execute() throws Exception {
        return SUCCESS;
    }

    public String getJsonArrayData() {
        if (jObject != null) {
            String jsonString = "{totalCount:" + this.jObject.size() + ",results:" + jObject.toString() + "}";
//            LOG.debug("Returning json string: '{}'", jsonString);
            return jsonString;
        }
        return "";
    }

    public String getTasks() {

        List<TaskViewData> viewData = new ArrayList<TaskViewData>();
        LOG.debug("Calling taskService to get all tasks");
        if (hideCompleted)
            tasks = taskService.getIncompleteTasks();
        else
            tasks = taskService.getAllTasks();

        for (Task c : tasks) {
            viewData.add(new TaskViewData(c));
        }

        this.jObject = JSONArray.fromObject(viewData);
//        LOG.debug("Returning tasks: '{}'", jObject.toString());
        return SUCCESS;
    }

    public String getTasksByClaim() {

        List<TaskViewData> viewData = new ArrayList<TaskViewData>();
        if (hideCompleted) {
            LOG.debug("Calling taskService to get incomplete tasks by claim");
            tasks = taskService.getIncompleteTasksByClaim(claimId);
        }
        else {
            LOG.debug("Calling taskService to get all tasks by claim");
            tasks = taskService.getAllTasksByClaim(claimId);
        }

        for (Task c : tasks) {
            viewData.add(new TaskViewData(c));
        }

        this.jObject = JSONArray.fromObject(viewData);
//        LOG.debug("Returning tasks: '{}'", jObject.toString());
        return SUCCESS;
    }

    public String getVisibleTasksByClaim() {

        List<TaskViewData> viewData = new ArrayList<TaskViewData>();
        if (hideCompleted) {
            LOG.debug("Calling taskService to get incomplete tasks by claim");
            tasks = taskService.getIncompleteTasksByClaim(this.getAuthenticatedUser().getId(), claimId, this.getIsCHO());
        }
        else {
            LOG.debug("Calling taskService to get all tasks by claim");
            tasks = taskService.getAllTasksByClaim(this.getAuthenticatedUser().getId(), claimId, this.getIsCHO());
        }

        for (Task c : tasks) {
            viewData.add(new TaskViewData(c));
        }

        this.jObject = JSONArray.fromObject(viewData);
//        LOG.debug("Returning tasks: '{}'", jObject.toString());
        return SUCCESS;
    }

    public String getVisibleTasks() {

        List<TaskViewData> viewData = new ArrayList<TaskViewData>();
        LOG.debug("Calling taskService to get all visible tasks");
        if (hideCompleted) {
            if (this.getIsCHO())
                tasks = taskService.getIncompleteVisibleTasks(this.getAuthenticatedUser().getId(), true, this.getChoIsClaimOwnershipEnabled(), false, false);
            else
                tasks = taskService.getIncompleteVisibleTasks(this.getAuthenticatedUser().getId(), false, this.getInsurerIsClaimOwnershipEnabled(), this.getInsurerIsWorkgroupEnabled(), this.getIsCH());
        } 
        else {
            if (this.getIsCHO())
                tasks = taskService.getAllVisibleTasks(this.getAuthenticatedUser().getId(), true, this.getChoIsClaimOwnershipEnabled(), false, false);
            else
                tasks = taskService.getAllVisibleTasks(this.getAuthenticatedUser().getId(), false, this.getInsurerIsClaimOwnershipEnabled(), this.getInsurerIsWorkgroupEnabled(), this.getIsCH());
        }

        for (Task c : tasks) {
            viewData.add(new TaskViewData(c));
        }

        this.jObject = JSONArray.fromObject(viewData);
//        LOG.debug("Returning tasks: '{}'", jObject.toString());
        return SUCCESS;
    }

    public String createNewTask() {
        Task task = new Task();
        task.setComplete(Boolean.FALSE);
        task.setDescription(taskDescription);
        task.setDueDate(dueDate);
        task.setType(taskType);
        task.setVisibility(visibility);
        task.setVisibilityRole(visibilityRole);
        task.setInsurer(this.getIsInsurer());

        LOG.debug("Creating new task with description='{}', dueDate='{}'", taskDescription, dueDate);
        LOG.debug("taskType='{}', visibility='{}'", taskType, visibility);
        LOG.debug("linkedToClaim='{}', choReference='{}'", linkToClaim, choReference);
        try {
            if (dueDate == null || dueDate.compareTo(new Date()) <= 0) {
                throw new Exception("The due date for a task must be later than today.");
            }
            if(linkToClaim) {
                Claim taskClaim = null;
                if (claimId > 0) {// Must be in Claim Detail task panel
                    LOG.debug("Getting claim with id: {}", claimId);
                    taskClaim = claimService.getClaim(claimId);
                }
                else {
                    LOG.debug("Getting claim with CHO reference: {}", choReference);
                    taskClaim = claimService.getClaimByCHOReferenceNumber(choReference);
                }
                if (taskClaim == null)
                    throw new Exception("No such claim with Supplier Reference '" + choReference + "'.");
                task.setClaim(taskClaim);
            }
            taskService.createNewTask(task);
            getActionResponse().AssignYesNoResult(Boolean.TRUE);
        }
        catch(Exception ex) {
            LOG.debug("Error creating new task: {}", ex.getMessage());
            getActionResponse().AssignMessageResult("Error creating new task: " + ex.getMessage());
        }

        return SUCCESS;
    }

    public String markTaskAsComplete() {
        try {
            taskService.markTaskAsComplete(selectedTaskId);
            getActionResponse().AssignYesNoResult(Boolean.TRUE);
        }
        catch(Exception ex) {
            getActionResponse().AssignMessageResult("Error marking task as completed: " + ex.getMessage());
        }

        return SUCCESS;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    public void setClaimId(int claimId) {
        this.claimId = claimId;
    }

    public int getClaimId() {
        return claimId;
    }


}
