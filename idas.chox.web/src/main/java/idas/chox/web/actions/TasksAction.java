package idas.chox.web.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.tasks.TaskSearchCriteria;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.struts2.ServletActionContext;

import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.access.annotation.Secured;

import idas.chox.core.model.AuditTrail;
import idas.chox.core.model.Claim;
import idas.chox.core.model.Task;
import idas.chox.core.model.TaskType;
import idas.chox.core.search.SearchResult;
import idas.chox.core.services.AuditTrailService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.TaskService;
import idas.chox.core.services.WebUserUserRoleService;
import idas.chox.core.util.DateHelper;
import idas.chox.core.util.DeleteOnCloseFileInputStream;
import idas.chox.data.ExcelTask;
import idas.chox.web.viewdata.TaskViewData;

/**
 *
 * @author John
 */
public class TasksAction extends BaseAction implements ModelDriven<TaskSearchCriteria>, Preparable {

    private static final Logger LOG = LoggerFactory.getLogger(TasksAction.class);
    private static final int MAX_EXPORT_SIZE = 65535;
    private TaskService taskService;
    private ClaimService claimService;
    private List<Task> tasks;
    private String jObject;
    private int jObjectSize;
    private boolean hideCompleted;
    private int selectedTaskId;
    private String taskDescription;
    private String taskType;
    private Date dueDate;
    private String choReference;
    private String visibilityRole;
    private int visibility;
    private int claimId = -1;
    private int totalCount;
    private String colorCode;
    private WebUserUserRoleService webUserUserRoleService;
    private AuditTrailService auditTrailService;
    private InputStream reportStream;
    private boolean directDownload;
    private InputStream excelStream;
    private int exportedTaskCount;
    private boolean exportFinished;
    private boolean exportCanceled;
    private boolean writingToFile;
    private boolean exceptionThrown;
    private boolean tooManyRows;
    private String paymentMethod;
    private Date paymentDate;
    private TaskSearchCriteria taskSearchCriteria;
    private boolean canLoadData = true;
    private boolean loadingTaskPanelFirstTimeAfterLogin;

    public boolean isLoadingTaskPanelFirstTimeAfterLogin() {
        return loadingTaskPanelFirstTimeAfterLogin;
    }

    public void setLoadingTaskPanelFirstTimeAfterLogin(boolean loadingTaskPanelFirstTimeAfterLogin) {
        this.loadingTaskPanelFirstTimeAfterLogin = loadingTaskPanelFirstTimeAfterLogin;
    }

    public void setSelectedTaskId(int selectedTaskId) {
        this.selectedTaskId = selectedTaskId;
    }

    public void setHideCompleted(boolean hideCompleted) {
        this.hideCompleted = hideCompleted;
    }

    public void setChoReference(String choReference) {
        this.choReference = StringEscapeUtils.unescapeHtml4(Jsoup.clean(choReference, Whitelist.none()));
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

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public boolean isCanLoadData() {
        return canLoadData;
    }

    public void setCanLoadData(boolean canLoadData) {
        this.canLoadData = canLoadData;
    }

    @Override
    public String execute() throws Exception {
        return SUCCESS;
    }

    public String loadTasks() throws Exception {

        if (getSession().containsKey("loadingTaskPanelFirstTimeAfterLogin")) {
            loadingTaskPanelFirstTimeAfterLogin = false;
        } else {
            synchronized (getSessionLock()) {
                getSession().put("loadingTaskPanelFirstTimeAfterLogin", true);
            }
            loadingTaskPanelFirstTimeAfterLogin = true;
        }

        return SUCCESS;
    }

    public String getJsonArrayData() {
        String jsonString;
        if (jObject != null) {
            jsonString = new StringBuilder().append("{totalCount:").append(totalCount).append(",colorCode:'").append(colorCode).append("',results:").append(jObject).append("}").toString();
        } else { // returning count only
            jsonString = new StringBuilder().append("{totalCount:").append(totalCount).append(",colorCode:'").append(colorCode).append("',results:[]}").toString();
        }
        return jsonString;
    }

    public String getSortedTasks() throws JsonProcessingException {
        boolean showInsurerRole = false;

        if (getIsInsurer() || getIsChoxAdmin()) {
            showInsurerRole = true;
        }

        List<TaskViewData> viewData = new ArrayList<>();
        LOG.debug("Calling taskService to get all tasks");
        if (hideCompleted) {
            SearchResult searchResult = taskService.getIncompleteTasks(taskSearchCriteria.getStart(), taskSearchCriteria.getLimit(), taskSearchCriteria.getSort(), taskSearchCriteria.getDir());
            tasks = searchResult.getResult();
            totalCount = searchResult.getTotalCount();
        } else {
            SearchResult searchResult = taskService.getAllTasks(taskSearchCriteria.getStart(), taskSearchCriteria.getLimit(), taskSearchCriteria.getSort(), taskSearchCriteria.getDir());
            tasks = searchResult.getResult();
            totalCount = searchResult.getTotalCount();
        }

        for (Task c : tasks) {
            viewData.add(new TaskViewData(c, showInsurerRole));
        }

        synchronized (getSessionLock()) {
            ObjectMapper mapper = new ObjectMapper();
            getSession().put("taskSearchCriteria", mapper.writeValueAsString(taskSearchCriteria));
        }

        LOG.debug("total task size is {}", totalCount);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        try {
            jObject = mapper.writeValueAsString(viewData);
            jObjectSize = viewData.size();
        } catch (JsonProcessingException ex) {
            LOG.error("Error converting viewData for sorted tasks to json string.");
            jObject = null;
        }
        return SUCCESS;
    }

    public String getSortedVisibleTasks() throws JsonProcessingException {

        if (canLoadData) {
            boolean showInsurerRole = false;

            if (getIsInsurer() || getIsChoxAdmin()) {
                showInsurerRole = true;
            }

            // if login user is not manager, will not allow user to filter by supplierClaimOwnerIds
            if (!getIsManager()) {
                taskSearchCriteria.setSupplierClaimOwnerIds(null);
                taskSearchCriteria.setClaimOwnerIds(null);
                taskSearchCriteria.setWorkgroupIds(null);
            }

            List<TaskViewData> viewData = new ArrayList<>();
            LOG.debug("Calling taskService to get all visible tasks");
            if (hideCompleted) {
                if (this.getIsCHO()) {
                    SearchResult searchResult = taskService.getIncompleteVisibleTasks(this.getAuthenticatedUser().getId(), taskSearchCriteria.getSupplierClaimOwnerIds(), taskSearchCriteria.getClaimOwnerIds(), taskSearchCriteria.getWorkgroupIds(), this.getChoIsClaimOwnershipEnabled(), false, taskSearchCriteria.getStart(), taskSearchCriteria.getLimit(), taskSearchCriteria.getSort(), taskSearchCriteria.getDir(), taskSearchCriteria.isShowAssignedTasksOnly());
                    tasks = searchResult.getResult();
                    totalCount = searchResult.getTotalCount();
                    colorCode = searchResult.getColorCode();
                } else {
                    SearchResult searchResult = taskService.getIncompleteVisibleTasks(this.getAuthenticatedUser().getId(), taskSearchCriteria.getSupplierClaimOwnerIds(), taskSearchCriteria.getClaimOwnerIds(), taskSearchCriteria.getWorkgroupIds(), this.getInsurerIsClaimOwnershipEnabled(), this.getInsurerIsWorkgroupEnabled(), taskSearchCriteria.getStart(), taskSearchCriteria.getLimit(), taskSearchCriteria.getSort(), taskSearchCriteria.getDir(), taskSearchCriteria.isShowAssignedTasksOnly());
                    tasks = searchResult.getResult();
                    totalCount = searchResult.getTotalCount();
                    colorCode = searchResult.getColorCode();
                }
            } else {
                if (this.getIsCHO()) {
                    SearchResult searchResult = taskService.getAllVisibleTasks(this.getAuthenticatedUser().getId(), taskSearchCriteria.getSupplierClaimOwnerIds(), taskSearchCriteria.getClaimOwnerIds(), taskSearchCriteria.getWorkgroupIds(), this.getChoIsClaimOwnershipEnabled(), false, taskSearchCriteria.getStart(), taskSearchCriteria.getLimit(), taskSearchCriteria.getSort(), taskSearchCriteria.getDir(), taskSearchCriteria.isShowAssignedTasksOnly());
                    tasks = searchResult.getResult();
                    totalCount = searchResult.getTotalCount();
                } else {
                    SearchResult searchResult = taskService.getAllVisibleTasks(this.getAuthenticatedUser().getId(), taskSearchCriteria.getSupplierClaimOwnerIds(), taskSearchCriteria.getClaimOwnerIds(), taskSearchCriteria.getWorkgroupIds(), this.getInsurerIsClaimOwnershipEnabled(), this.getInsurerIsWorkgroupEnabled(),  taskSearchCriteria.getStart(), taskSearchCriteria.getLimit(), taskSearchCriteria.getSort(), taskSearchCriteria.getDir(), taskSearchCriteria.isShowAssignedTasksOnly());
                    tasks = searchResult.getResult();
                    totalCount = searchResult.getTotalCount();
                }
            }

            for (Task c : tasks) {
                if (c.getRaisedBy() != null) {
                    c.setCreatedBy(c.getRaisedBy());
                }
                viewData.add(new TaskViewData(c, showInsurerRole));
            }

            synchronized (getSessionLock()) {
                ObjectMapper mapper = new ObjectMapper();
                getSession().put("taskSearchCriteria", mapper.writeValueAsString(taskSearchCriteria));
            }

            ObjectMapper mapper = new ObjectMapper();
            String jsonString = null;
            try {
                jObject = mapper.writeValueAsString(viewData);
                jObjectSize = viewData.size();
            } catch (JsonProcessingException ex) {
                LOG.error("Error converting viewData for sorted visible tasks to json string.");
            }

        } else {

            synchronized (getSessionLock()) {
                getSession().put("searchCriteria", null);
            }

        }
        return SUCCESS;
    }

    public String getVisibleTaskCount() {
        boolean showInsurerRole = false;

        tasks = null;
        jObject = null;

        if (getIsInsurer() || getIsChoxAdmin()) {
            showInsurerRole = true;
        }

        List<TaskViewData> viewData = new ArrayList<>();
        LOG.debug("Calling taskService to get visible task counts");
        if (hideCompleted) {
            if (this.getIsCHO()) {
                totalCount = taskService.getIncompleteVisibleTaskCount(this.getAuthenticatedUser().getId(), this.getChoIsClaimOwnershipEnabled(), false, true);
            } else {
                totalCount = taskService.getIncompleteVisibleTaskCount(this.getAuthenticatedUser().getId(), this.getInsurerIsClaimOwnershipEnabled(), this.getInsurerIsWorkgroupEnabled(), true);
            }
        } else {
            if (this.getIsCHO()) {
                totalCount = taskService.getAllVisibleTaskCount(this.getAuthenticatedUser().getId(), this.getChoIsClaimOwnershipEnabled(), false, true);
            } else {
                totalCount = taskService.getAllVisibleTaskCount(this.getAuthenticatedUser().getId(), this.getInsurerIsClaimOwnershipEnabled(), this.getInsurerIsWorkgroupEnabled(), true);
            }
        }
        return SUCCESS;
    }

    public String getTasksByClaim() {
        boolean showInsurerRole = false;

        if (getIsInsurer() || getIsChoxAdmin()) {
            showInsurerRole = true;
        }

        List<TaskViewData> viewData = new ArrayList<>();
        if (hideCompleted) {
            LOG.debug("Calling taskService to get incomplete tasks by claim");
            tasks = taskService.getIncompleteTasksByClaim(claimId);
        } else {
            LOG.debug("Calling taskService to get all tasks by claim");
            tasks = taskService.getAllTasksByClaim(claimId);
        }

        for (Task c : tasks) {
            if (c.getRaisedBy() != null) {
                c.setCreatedBy(c.getRaisedBy());
            }
            viewData.add(new TaskViewData(c, showInsurerRole));
        }

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        try {
            jObject = mapper.writeValueAsString(viewData);
            jObjectSize = viewData.size();
        } catch (JsonProcessingException ex) {
            LOG.error("Error converting viewData for tasksByClaim to json string.");
        }
        return SUCCESS;
    }

    public String getVisibleTasksByClaim() {
        boolean showInsurerRole = false;

        if (getIsInsurer() || getIsChoxAdmin()) {
            showInsurerRole = true;
        }

        List<TaskViewData> viewData = new ArrayList<>();
        if (hideCompleted) {
            LOG.debug("Calling taskService to get incomplete tasks by claim");
            tasks = taskService.getIncompleteTasksByClaim(getAuthenticatedUser().getId(), claimId);
        } else {
            LOG.debug("Calling taskService to get all tasks by claim");
            tasks = taskService.getAllTasksByClaim(getAuthenticatedUser().getId(), claimId);
        }

        totalCount = 0;
        for (Task c : tasks) {
            if (c.getRaisedBy() != null) {
                c.setCreatedBy(c.getRaisedBy());
            }
            viewData.add(new TaskViewData(c, showInsurerRole));
            if ((getAuthenticatedUser().isCHO() && !c.getComplete() && c.getInsurer())
                    || (getAuthenticatedUser().isAnInsurer() && !c.getComplete() && !c.getInsurer())) {
                totalCount++;
            }

        }

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        try {
            jObject = mapper.writeValueAsString(viewData);
            jObjectSize = viewData.size();
        } catch (JsonProcessingException ex) {
            LOG.error("Error converting viewData for visible tasksByClaim to json string.");
        }
        return SUCCESS;
    }

    @Secured({"ROLE_INS", "ROLE_CHO"})
    public String createNewTask() {
        if (taskType == null || dueDate == null || taskDescription == null) {
            LOG.warn("Null parameter creating new task: taskType={}, dueDate={}, taskDescription={}",
                    new Object[]{taskType, dueDate, taskDescription});
            setActionError("Cannot create new task as no parameters provide. If this error persists, please contact CHOX support");
            getActionResponse().AssignMessageResult("Cannot create new task as no parameters provide. If this error persists, please contact CHOX support");
            return ERROR;
        }
        Task task = new Task();
        if (taskType.equals(TaskType.TOTAL_LOSS_PAYMENT.getDescription())) {
            if (getIsInsurer()) {
                taskDescription = new StringBuilder().append(taskDescription).append("\nPayment Method - ").append(paymentMethod).append(". Payment Date - ").append(DateHelper.getLocalDateFormat().format(paymentDate)).toString();
            } else {
                taskDescription = new StringBuilder().append(taskDescription).append("\nRequested Payment Method - ").append(paymentMethod).toString();
            }
        }
        task.setDescription(taskDescription);
        task.setDueDate(dueDate);
        task.setType(taskType);
        task.setVisibility(visibility);
        task.setVisibilityRole(visibilityRole);
        task.setInsurer(this.getIsInsurer());

        LOG.debug("Creating new task with description='{}', dueDate='{}'", taskDescription, dueDate);
        LOG.debug("taskType='{}', visibility='{}'", taskType, visibility);
        try {
            if (dueDate == null || dueDate.compareTo(new Date()) <= 0) {
                throw new Exception("The due date for a task must be later than today.");
            }
            Claim taskClaim;
            if (claimId > 0) {// Must be in Claim Detail task panel
                LOG.debug("Getting claim with id: {}", claimId);
                taskClaim = claimService.getClaim(claimId);
            } else {
                LOG.debug("Getting claim with CHO reference: {}", choReference.toUpperCase());
                taskClaim = claimService.getClaimByCHOReferenceNumber(choReference.toUpperCase());
            }
            if (taskClaim == null) {
                throw new Exception(String.format("No such claim with Supplier Reference %s.", choReference));
            }
            if (taskClaim.isRemovedTasks()) {
                throw new Exception("Task cannot be added as tasks have already been removed to comply with GDPR.");
            }
            task.setClaim(taskClaim);
            taskService.createNewTask(task);
            getActionResponse().AssignYesNoResult(Boolean.TRUE);
        } catch (Exception ex) {
            LOG.debug("Error creating new task: {}", ex.getMessage());
            getActionResponse().AssignMessageResult(String.format("Error creating new task: %s", ex.getMessage()));
        }

        return SUCCESS;
    }

    public boolean isClaimHashed() {
        if (claimId > 0) {// Must be in Claim Detail task panel
            LOG.debug("Getting claim with id: {}", claimId);
            Claim claim = claimService.getClaim(claimId);
            return claim.isHashed();
        }
        return false;
    }

    public boolean isRemovedTasks() {
        if (claimId > 0) {// Must be in Claim Detail task panel
            LOG.debug("Getting claim with id: {}", claimId);
            Claim claim = claimService.getClaim(claimId);
            return claim.isRemovedTasks();
        }
        return false;
    }

    public String markTaskAsComplete() {
        try {
            taskService.markTaskAsComplete(getAuthenticatedUser().getId(), selectedTaskId);
            getActionResponse().AssignYesNoResult(Boolean.TRUE);
        } catch (Exception ex) {
            getActionResponse().AssignMessageResult(String.format("Error marking task as completed: %s", ex.getMessage()));
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

    public String exportTask() {
        String result;

        if (isDirectDownload()) {
            LOG.debug("Request to direct download report file ");
            try {
                doTaskExportToExcel();
            } catch (Exception ex) {
                LOG.error("Exception thrown when trying to Export To Excel. exception message : {} .", ex.getMessage(), ex);
                LOG.error("Report requested by: {}, org name: {}", getAuthenticatedUser().getDisplayName(), getAuthenticatedUser().getOrganisationName());
                synchronized (getSessionLock()) {
                    getSession().put("exceptionThrown", true);
                }
            }
        }

        synchronized (getSessionLock()) {
            Map<String, Object> session = getSession();
            if (session.containsKey("reportFileLocation") && session.get("reportFileLocation") != null) {
                try {
                    File reportFile = new File((String) getSession().get("reportFileLocation"));
                    excelStream = new DeleteOnCloseFileInputStream(reportFile);
                    result = SUCCESS;
                } catch (Exception ex) {
                    LOG.error("exception in generating report {}", ex.getMessage(), ex);
                    session.put("exceptionThrown", true);
                    excelStream = null;
                    result = ERROR;
                }
                session.put("reportFileLocation", null);
            } else {
                excelStream = null;
                result = ERROR;
            }
        }

        return result;
    }

    public String doTaskExportToExcel() throws IOException {

        synchronized (getSessionLock()) {
            Map<String, Object> session = getSession();
            session.put("isExportFinished", false);
            session.put("cancelExportOperation", false);
            session.put("writingToFile", false);
            session.put("numberOfTasksProcessed", 0);
            session.put("reportFileLocation", null);
            session.put("exceptionThrown", false);
        }

        String rtnStr = SUCCESS;
        try {
            if (getSession() != null) {
                SearchResult searchResult;
                if (hideCompleted) {
                    if (this.getIsCHO()) {
                        searchResult = taskService.getIncompleteVisibleTasks(this.getAuthenticatedUser().getId(),  taskSearchCriteria.getSupplierClaimOwnerIds(), taskSearchCriteria.getClaimOwnerIds(), taskSearchCriteria.getWorkgroupIds(), this.getChoIsClaimOwnershipEnabled(), false, 0, MAX_EXPORT_SIZE, taskSearchCriteria.getSort(), taskSearchCriteria.getDir(), taskSearchCriteria.isShowAssignedTasksOnly());
                    } else if (this.getIsInsurer()) {
                        searchResult = taskService.getIncompleteVisibleTasks(this.getAuthenticatedUser().getId(),  taskSearchCriteria.getSupplierClaimOwnerIds(), taskSearchCriteria.getClaimOwnerIds(), taskSearchCriteria.getWorkgroupIds(), this.getInsurerIsClaimOwnershipEnabled(), this.getInsurerIsWorkgroupEnabled(), 0, MAX_EXPORT_SIZE, taskSearchCriteria.getSort(), taskSearchCriteria.getDir(), taskSearchCriteria.isShowAssignedTasksOnly());
                    } else {
                        searchResult = taskService.getIncompleteTasks(0, MAX_EXPORT_SIZE, taskSearchCriteria.getSort(), taskSearchCriteria.getDir());
                    }

                } else {
                    if (this.getIsCHO()) {
                        searchResult = taskService.getAllVisibleTasks(this.getAuthenticatedUser().getId(),  taskSearchCriteria.getSupplierClaimOwnerIds(), taskSearchCriteria.getClaimOwnerIds(), taskSearchCriteria.getWorkgroupIds(), this.getChoIsClaimOwnershipEnabled(), false, 0, MAX_EXPORT_SIZE, taskSearchCriteria.getSort(), taskSearchCriteria.getDir(), taskSearchCriteria.isShowAssignedTasksOnly());
                    } else if (this.getIsInsurer()) {
                        searchResult = taskService.getAllVisibleTasks(this.getAuthenticatedUser().getId(),  taskSearchCriteria.getSupplierClaimOwnerIds(), taskSearchCriteria.getClaimOwnerIds(), taskSearchCriteria.getWorkgroupIds(), this.getInsurerIsClaimOwnershipEnabled(), this.getInsurerIsWorkgroupEnabled(), 0, MAX_EXPORT_SIZE, taskSearchCriteria.getSort(), taskSearchCriteria.getDir(), taskSearchCriteria.isShowAssignedTasksOnly());
                    } else {
                        searchResult = taskService.getAllTasks(0, MAX_EXPORT_SIZE, taskSearchCriteria.getSort(), taskSearchCriteria.getDir());
                    }
                }
                if (searchResult != null) {
                    tasks = searchResult.getResult();
                    totalCount = searchResult.getTotalCount();
                }

                if (totalCount > 0) {

                    LOG.debug("Total No of tasks : '{}'", totalCount);
                    if (totalCount <= MAX_EXPORT_SIZE) {

                        try {
                            if (generateExcel(tasks)) {
                                rtnStr = SUCCESS;
                            }
                        } catch (Exception ex) {
                            synchronized (getSessionLock()) {
                                getSession().put("exceptionThrown", true);
                            }
                            LOG.error("Exception thrown generating report: ", ex);
                            return rtnStr;
                        }
                    } else if (totalCount > MAX_EXPORT_SIZE) {
                        synchronized (getSessionLock()) {
                            getSession().put("tooManyRows", true);
                        }
                    }
                }
            }

        } catch (Exception ex) {
            synchronized (getSessionLock()) {
                getSession().put("exceptionThrown", true);
            }
            LOG.error("Exporting thrown while exporting task.", ex);
        }
        return rtnStr;
    }

    private boolean generateExcel(List<Task> tasks) throws Exception {
        boolean cancelled = false;

        LOG.info("Exporting to excel with {} tasks.", tasks.size());

        List<ExcelTask> excelTasks = new ArrayList<>();

        for (Task task : tasks) {

            ExcelTask excelTask = new ExcelTask();

            excelTask.setTaskDueDate(task.getDueDate());
            excelTask.setTaskType(task.getType());
            excelTask.setTaskDescription(task.getDescription());
            excelTask.setTaskCreatedDate(task.getCreatedDate());
            excelTask.setTaskCreatedBy(task.getCreatedBy().getDisplayName());
            excelTask.setTaskComplete(task.getComplete() ? "Yes" : "No");
            if (task.getClaim() != null) {

                excelTask.setSupplierReference(task.getClaim().getChoReference());
                excelTask.setTaskCurrentClaimStatus(task.getClaim().getStatus());

                if (task.getClaim().getClaimOwner() != null) {
                    excelTask.setTaskOwner(task.getClaim().getClaimOwner().getDisplayName());
                }
                if (task.getClaim().getSupplierClaimOwner() != null) {
                    excelTask.setChoTaskOwner(task.getClaim().getSupplierClaimOwner().getDisplayName());
                }

                if (task.getClaim().getWorkgroup() != null) {
                    excelTask.setTaskWrokgroup(task.getClaim().getWorkgroup().getName());
                }

                AuditTrail audit = auditTrailService.getAuditTrailByTaskCreatedDate(task.getClaim().getId(), task.getCreatedDate());
                if (audit != null) {
                    excelTask.setTaskStatusOfClaimWhenTaskCreated(audit.getNewStatus());
                }
            }

            if (task.getCreatedBy().isCHO()) {
                excelTask.setTaskCreatedByOrg(task.getCreatedBy().getChorganisation().getName());
            } else if (task.getCreatedBy().isAnInsurer()) {
                excelTask.setTaskCreatedByOrg(task.getCreatedBy().getInsurer().getName());
            } else {
                excelTask.setTaskCreatedByOrg("System");
            }

            if (task.getVisibilityRole() != null) {
                excelTask.setTaskRoleAssignedTo(webUserUserRoleService.getWebUserRole(task.getVisibilityRole()).getDescription());
            }

            excelTasks.add(excelTask);

            synchronized (getSessionLock()) {
                if (isExportTaskOperationCancelled()) {
                    getSession().put("numberOfTasksProcessed", null);
                    return false;
                } else {
                    getSession().put("numberOfTasksProcessed", excelTasks.size());
                }
            }
        }

        final String templateFilePath = getReportTemplatePath("taskExportTemplate.xls");
        final File reportFile = File.createTempFile("task_export_excel_report", ".xls");
        reportFile.deleteOnExit();
        LOG.info("'Task Export to Excel' report file will be written to the following location: {}", reportFile.getAbsolutePath());
        final boolean isCho = getIsCHO();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    // 'Owner of Claim Task Assigned To' and ‘Role Assigned To’ columns do not apply to the CHO user. Please look at bug#2546.
                    if (isCho) {
//                        transformer.setColumnsToHide(new short[]{(short) 7, (short) 8, (short) 9});
                    } else {
//                        transformer.setColumnsToHide(new short[]{(short) 10});
                    }
                    LOG.debug("XLS transform operation called with seperate thread {}", Thread.currentThread().getId());
                    try (InputStream is = new FileInputStream(templateFilePath)) {
                        try (OutputStream os = new FileOutputStream(reportFile)) {
                            Context context = new Context();
                            context.putVar("excelTasks", excelTasks);
                            JxlsHelper.getInstance().processTemplate(is, os, context);
                            os.flush();
                            LOG.debug("file writing operation finished {}", Thread.currentThread().getId());
                        }
                    }
                    LOG.debug("Workbook created - writing to file '{}'...", reportFile.getAbsolutePath());
                } catch (IOException ex) {
                    LOG.error("Exception thrown transforming report:", ex);
                    LOG.error("Report requested by: {}, org name: {}", getAuthenticatedUser().getDisplayName(), getAuthenticatedUser().getOrganisationName());
                    synchronized (getSessionLock()) {
                        getSession().put("exceptionThrown", true);
                    }
                }
            }
        };

        ExecutorService executor = (ExecutorService) ServletActionContext.getServletContext().getAttribute("CHOX_EXECUTOR");

        synchronized (getSessionLock()) {
            getSession().put("writingToFile", true);
        }
        Future<?> future = executor.submit(r);

        try {
            while (!isExportTaskOperationCancelled() && !future.isDone()) {
                Thread.sleep(100);
            }

            if (isExportTaskOperationCancelled()) {
                cancelled = true;
                LOG.debug("writing to file operation cancelled. in thread {}", Thread.currentThread().getId());
            }
        } catch (Exception ex) {
            LOG.error("Exception thrown while tranforming map to xls file. exception message : {} .", ex.getMessage());
            LOG.error("Report requested by: {}, org name: {}", getAuthenticatedUser().getDisplayName(), getAuthenticatedUser().getOrganisationName());
            synchronized (getSessionLock()) {
                getSession().put("exceptionThrown", true);
            }
        }

        synchronized (getSessionLock()) {
            if (!cancelled && getSession().get("exceptionThrown") != null) {
                Map<String, Object> session = getSession();
                session.put("numberOfTasksProcessed", null);
                session.put("cancelExportOperation", false);
                session.put("isExportFinished", true);
                session.put("reportFileLocation", reportFile.getAbsolutePath());
                session.put("writingToFile", false);
            } else if (!cancelled) {
                throw new Exception("Error Generating Report.");
            }
        }

        return true;
    }

    public String cancelTaskExportOperation() {
        synchronized (getSessionLock()) {
            Map<String, Object> session = getSession();
            LOG.debug("export operation cancellation called ...");
            session.put("cancelExportOperation", true);
            if (session.containsKey("reportFileLocation") && session.get("reportFileLocation") != null) {
                session.put("reportFileLocation", null);
            }
            setExportCanceled(true);
        }
        return SUCCESS;
    }

    public String getExportedTasksCount() {
        synchronized (getSessionLock()) {
            Map<String, Object> session = getSession();
            try {
                if (session.containsKey("numberOfTasksProcessed") && session.get("numberOfTasksProcessed") != null) {
                    setExportedTaskCount((Integer) session.get("numberOfTasksProcessed"));
                    setExportFinished((Boolean) session.get("isExportFinished"));
                    setWritingToFile((Boolean) session.get("writingToFile"));
                    setExportCanceled((Boolean) session.get("cancelExportOperation"));
                    if (session.get("exceptionThrown") == null) {
                        setExceptionOccured(Boolean.FALSE);
                    } else {
                        setExceptionOccured((Boolean) session.get("exceptionThrown"));
                    }
                } else {
                    setExportedTaskCount(0);
                    setExportFinished((Boolean) session.get("isExportFinished"));
                    setWritingToFile((Boolean) session.get("writingToFile"));
                    setExportCanceled((Boolean) session.get("cancelExportOperation"));
                    if (session.get("exceptionThrown") == null) {
                        setExceptionOccured(Boolean.FALSE);
                    } else {
                        setExceptionOccured((Boolean) session.get("exceptionThrown"));
                    }
                    if (session.get("tooManyRows") == null) {
                        setTooManyRows(Boolean.FALSE);
                    } else {
                        setTooManyRows((Boolean) session.get("tooManyRows"));
                    }
                }
            } catch (Exception ex) {
                LOG.warn("Exception getting exported tasks count: {}", ex.getMessage(), ex);
            }
        }
        return SUCCESS;
    }

    public String getJsonData() {
        return String.format("{exportedTaskCount:%s,isExportProcessFinished:%s,exportCancelled:%s,writingToFile:%s,exceptionThrown:%s,tooManyRows:%s}", exportedTaskCount, exportFinished, exportCanceled, writingToFile, exceptionThrown, tooManyRows);
    }

    public void setExportedTaskCount(int exportedTaskCount) {
        this.exportedTaskCount = exportedTaskCount;
    }

    public boolean isExportFinished() {
        return exportFinished;
    }

    public void setExportFinished(boolean exportFinished) {
        this.exportFinished = exportFinished;
    }

    public boolean isExportCanceled() {
        return exportCanceled;
    }

    public void setExportCanceled(boolean exportCanceled) {
        this.exportCanceled = exportCanceled;
    }

    public boolean isExceptionThrown() {
        return exceptionThrown;
    }

    public void setExceptionThrown(boolean exceptionThrown) {
        this.exceptionThrown = exceptionThrown;
    }

    public boolean isTooManyRows() {
        return tooManyRows;
    }

    public void setTooManyRows(boolean tooManyRows) {
        this.tooManyRows = tooManyRows;
    }

    public boolean isDirectDownload() {
        return directDownload;
    }

    public void setDirectDownload(boolean directDownload) {
        this.directDownload = directDownload;
    }

    public InputStream getExcelStream() {
        return excelStream;
    }

    public void setExcelStream(InputStream excelStream) {
        this.excelStream = excelStream;
    }

    public boolean isExceptionOccured() {
        return exceptionThrown;
    }

    public void setExceptionOccured(boolean exceptionOccured) {
        this.exceptionThrown = exceptionOccured;
    }

    public boolean isWritingToFile() {
        return writingToFile;
    }

    public void setWritingToFile(boolean writingToFile) {
        this.writingToFile = writingToFile;
    }

    public void setWebUserUserRoleService(WebUserUserRoleService webUserUserRoleService) {
        this.webUserUserRoleService = webUserUserRoleService;
    }

    public void setAuditTrailService(AuditTrailService auditTrailService) {
        this.auditTrailService = auditTrailService;
    }

    public InputStream getReportStream() {
        return reportStream;
    }

    public void setReportStream(InputStream reportStream) {
        this.reportStream = reportStream;
    }

    private String getReportTemplatePath(String reportTemplateName) {
        String reportDefinationFilePath = ServletActionContext.getServletContext().getRealPath(new StringBuilder().append("/WEB-INF/classes/reports/").append(reportTemplateName).toString());

        return reportDefinationFilePath;
    }

    private boolean isExportTaskOperationCancelled() {
        synchronized (getSessionLock()) {
            return (Boolean) getSession().get("cancelExportOperation");
        }
    }

    @Override
    public TaskSearchCriteria getModel() {
        return taskSearchCriteria;
    }

    @Override
    public void prepare() throws Exception {
        if (taskSearchCriteria == null) {
            if (getSession() != null && getSession().containsKey("taskSearchCriteria")) {
                String taskSearchCriteriaStr = getSession().get("taskSearchCriteria").toString();
                ObjectMapper mapper = new ObjectMapper();
                taskSearchCriteria = mapper.readValue(taskSearchCriteriaStr, TaskSearchCriteria.class);
            } else {
                taskSearchCriteria = new TaskSearchCriteria();
            }
        }
    }
}
