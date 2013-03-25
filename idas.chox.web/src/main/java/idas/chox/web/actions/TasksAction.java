package idas.chox.web.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONArray;
import net.sf.jxls.transformer.XLSTransformer;

import idas.chox.core.model.AuditTrail;
import idas.chox.core.model.Claim;
import idas.chox.core.model.Task;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.search.SearchResult;
import idas.chox.core.services.AuditTrailService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.TaskService;
import idas.chox.core.services.WebUserUserRoleService;
import idas.chox.core.util.DeleteOnCloseFileInputStream;
import idas.chox.data.ExcelTask;
import idas.chox.web.viewdata.TaskViewData;

/**
 *
 * @author John
 */
public class TasksAction extends BaseAction {

    private static final Logger LOG = LoggerFactory.getLogger(TasksAction.class);
    private static final int MAX_EXPORT_SIZE = 65535;
    private TaskService taskService;
    private ClaimService claimService;
    private List<Task> tasks;
    private JSONArray jObject;
    private boolean hideCompleted;
    private boolean showAssignedTasksOnly;
    private int selectedTaskId;
    private String taskDescription;
    private String taskType;
    private Date dueDate;
    private boolean linkToClaim;
    private String choReference;
    private String visibilityRole;
    private int visibility;
    private int claimId = -1;
    private int start;
    private int limit;
    private String sort;
    private String dir;
    private int totalCount;
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

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setSelectedTaskId(int selectedTaskId) {
        this.selectedTaskId = selectedTaskId;
    }

    public void setHideCompleted(boolean hideCompleted) {
        this.hideCompleted = hideCompleted;
    }

    public void setShowAssignedTasksOnly(boolean showAssignedTasksOnly) {
        this.showAssignedTasksOnly = showAssignedTasksOnly;
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
            String jsonString = "{totalCount:" + totalCount + ",results:" + jObject.toString() + "}";
            return jsonString;
        }
        return "";
    }

    public String getSortedTasks() {
        boolean showInsurerRole = false;

        if (getIsInsurer() || getIsChoxAdmin()) {
            showInsurerRole = true;
        }

        List<TaskViewData> viewData = new ArrayList<TaskViewData>();
        LOG.debug("Calling taskService to get all tasks");
        if (hideCompleted) {
            SearchResult searchResult = taskService.getIncompleteTasks(start, limit, sort, dir);
            tasks = searchResult.getResult();
            totalCount = searchResult.getTotalCount();
        } else {
            SearchResult searchResult = taskService.getAllTasks(start, limit, sort, dir);
            tasks = searchResult.getResult();
            totalCount = searchResult.getTotalCount();
        }

        for (Task c : tasks) {
            if (c.getRaisedBy() != null) {
                c.setCreatedBy(c.getRaisedBy());
            }
            viewData.add(new TaskViewData(c, showInsurerRole));
        }

        LOG.debug("total task size is {}", totalCount);
        this.jObject = JSONArray.fromObject(viewData);
        return SUCCESS;
    }

    public String getSortedVisibleTasks() {
        boolean showInsurerRole = false;

        if (getIsInsurer() || getIsChoxAdmin()) {
            showInsurerRole = true;
        }

        List<TaskViewData> viewData = new ArrayList<TaskViewData>();
        LOG.debug("Calling taskService to get all visible tasks");
        if (hideCompleted) {
            if (this.getIsCHO()) {
                SearchResult searchResult = taskService.getIncompleteVisibleTasks(this.getAuthenticatedUser().getId(), this.getChoIsClaimOwnershipEnabled(), false, start, limit, sort, dir, showAssignedTasksOnly);
                tasks = searchResult.getResult();
                totalCount = searchResult.getTotalCount();
            } else {
                SearchResult searchResult = taskService.getIncompleteVisibleTasks(this.getAuthenticatedUser().getId(), this.getInsurerIsClaimOwnershipEnabled(), this.getInsurerIsWorkgroupEnabled(), start, limit, sort, dir, showAssignedTasksOnly);
                tasks = searchResult.getResult();
                totalCount = searchResult.getTotalCount();
            }
        } else {
            if (this.getIsCHO()) {
                SearchResult searchResult = taskService.getAllVisibleTasks(this.getAuthenticatedUser().getId(), this.getChoIsClaimOwnershipEnabled(), false, start, limit, sort, dir, showAssignedTasksOnly);
                tasks = searchResult.getResult();
                totalCount = searchResult.getTotalCount();
            } else {
                SearchResult searchResult = taskService.getAllVisibleTasks(this.getAuthenticatedUser().getId(), this.getInsurerIsClaimOwnershipEnabled(), this.getInsurerIsWorkgroupEnabled(), start, limit, sort, dir, showAssignedTasksOnly);
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

        this.jObject = JSONArray.fromObject(viewData);
        return SUCCESS;
    }

    public String getTasksByClaim() {
        boolean showInsurerRole = false;

        if (getIsInsurer() || getIsChoxAdmin()) {
            showInsurerRole = true;
        }


        List<TaskViewData> viewData = new ArrayList<TaskViewData>();
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

        this.jObject = JSONArray.fromObject(viewData);
        return SUCCESS;
    }

    public String getVisibleTasksByClaim() {
        boolean showInsurerRole = false;

        if (getIsInsurer() || getIsChoxAdmin()) {
            showInsurerRole = true;
        }

        List<TaskViewData> viewData = new ArrayList<TaskViewData>();
        if (hideCompleted) {
            LOG.debug("Calling taskService to get incomplete tasks by claim");
            tasks = taskService.getIncompleteTasksByClaim(this.getAuthenticatedUser().getId(), claimId);
        } else {
            LOG.debug("Calling taskService to get all tasks by claim");
            tasks = taskService.getAllTasksByClaim(this.getAuthenticatedUser().getId(), claimId);
        }

        for (Task c : tasks) {
            if (c.getRaisedBy() != null) {
                c.setCreatedBy(c.getRaisedBy());
            }
            viewData.add(new TaskViewData(c, showInsurerRole));
        }

        this.jObject = JSONArray.fromObject(viewData);
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
            if (linkToClaim) {
                Claim taskClaim = null;
                if (claimId > 0) {// Must be in Claim Detail task panel
                    LOG.debug("Getting claim with id: {}", claimId);
                    taskClaim = claimService.getClaim(claimId);
                } else {
                    LOG.debug("Getting claim with CHO reference: {}", choReference.toUpperCase());
                    taskClaim = claimService.getClaimByCHOReferenceNumber(choReference.toUpperCase());
                }
                if (taskClaim == null) {
                    throw new Exception("No such claim with Supplier Reference '" + choReference + "'.");
                }
                task.setClaim(taskClaim);
            }
            taskService.createNewTask(task);
            getActionResponse().AssignYesNoResult(Boolean.TRUE);
        } catch (Exception ex) {
            LOG.debug("Error creating new task: {}", ex.getMessage());
            getActionResponse().AssignMessageResult("Error creating new task: " + ex.getMessage());
        }

        return SUCCESS;
    }

    public String markTaskAsComplete() {
        try {
            taskService.markTaskAsComplete(getAuthenticatedUser().getId(), selectedTaskId);
            getActionResponse().AssignYesNoResult(Boolean.TRUE);
        } catch (Exception ex) {
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

    public String exportTask() {
        String result;

        if (isDirectDownload()) {
            LOG.debug("Request to direct download report file ");
            try {
                doTaskExportToExcel();
            } catch (Exception ex) {
                LOG.error("Exception thrown when trying to Export To Excel. exception message : {} .", ex.getMessage(), ex);
                LOG.error("Report requested by: {}, org name: {}", getAuthenticatedUser().getDisplayName(), getAuthenticatedUser().getOrganisationName());
                getSession().put("exceptionThrown", true);
            }
        }

        synchronized (getSession()) {
            if (getSession().containsKey("reportFileLocation") && getSession().get("reportFileLocation") != null) {
                try {
                    File reportFile = new File((String) getSession().get("reportFileLocation"));
                    excelStream = new DeleteOnCloseFileInputStream(reportFile);
                    result = SUCCESS;
                } catch (Exception ex) {
                    LOG.error("exception in generating report {}", ex.getMessage(), ex);
                    getSession().put("exceptionThrown", true);
                    excelStream = null;
                    result = ERROR;
                }
                getSession().put("reportFileLocation", null);
            } else {
                excelStream = null;
                result = ERROR;
            }
        }

        return result;
    }

    public String doTaskExportToExcel() throws IOException {

        synchronized (getSession()) {
            getSession().put("isExportFinished", false);
            getSession().put("cancelExportOperation", false);
            getSession().put("writingToFile", false);
            getSession().put("numberOfTasksProcessed", 0);
            getSession().put("reportFileLocation", null);
            getSession().put("exceptionThrown", false);
        }

        String rtnStr = ERROR;
        try {
            if (getSession() != null) {
                SearchResult searchResult;
                if (hideCompleted) {
                    if (this.getIsCHO()) {
                        searchResult = taskService.getIncompleteVisibleTasks(this.getAuthenticatedUser().getId(), this.getChoIsClaimOwnershipEnabled(), false, 0, MAX_EXPORT_SIZE, sort, dir, showAssignedTasksOnly);
                    } else if (this.getIsInsurer()) {
                        searchResult = taskService.getIncompleteVisibleTasks(this.getAuthenticatedUser().getId(), this.getInsurerIsClaimOwnershipEnabled(), this.getInsurerIsWorkgroupEnabled(), 0, MAX_EXPORT_SIZE, sort, dir, showAssignedTasksOnly);
                    } else {
                        searchResult = taskService.getIncompleteTasks(0, MAX_EXPORT_SIZE, sort, dir);
                    }
                    
                } else {
                    if (this.getIsCHO()) {
                        searchResult = taskService.getAllVisibleTasks(this.getAuthenticatedUser().getId(), this.getChoIsClaimOwnershipEnabled(), false, 0, MAX_EXPORT_SIZE, sort, dir, showAssignedTasksOnly);
                    } else if (this.getIsInsurer()) {
                        searchResult = taskService.getAllVisibleTasks(this.getAuthenticatedUser().getId(), this.getInsurerIsClaimOwnershipEnabled(), this.getInsurerIsWorkgroupEnabled(), 0, MAX_EXPORT_SIZE, sort, dir, showAssignedTasksOnly);
                    } else {
                        searchResult = taskService.getAllTasks(0, MAX_EXPORT_SIZE, sort, dir);
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
                            getSession().put("exceptionThrown", true);
                            LOG.error("Exception thrown generating report: ",  ex);
                            return rtnStr;
                        }
                    } else if (totalCount > MAX_EXPORT_SIZE) {
                        getSession().put("tooManyRows", true);
                    }
                }
            }
            
        } catch (Exception ex) {
            getSession().put("exceptionThrown", true);
            LOG.error("Exporting thrown while exporting task.", ex);
        }
        return rtnStr;
    }

    private boolean generateExcel(List<Task> tasks) throws Exception {

        LOG.info("Exporting to excel with {} tasks.", tasks.size());

        List<ExcelTask> excelTasks = new ArrayList<ExcelTask>();
        
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

                if (task.getVisibilityRole() != null && task.getVisibilityRole().equals(WebUserRole.ROLE_CH)) {
                    if (task.getClaim().getClaimOwner() != null) {
                        excelTask.setTaskOwner(task.getClaim().getClaimOwner().getDisplayName());
                    }
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

            synchronized (getSession()) {
                if (isExportTaskOperationCancelled()) {
                    getSession().put("numberOfTasksProcessed", null);
                    return false;
                } else {
                    getSession().put("numberOfTasksProcessed", excelTasks.size());
                }
            }
        }

        final Map excelMap = new HashMap();
        excelMap.put("excelTasks", excelTasks);
        excelMap.put("isCho", getIsCHO());

        final String templateFilePath = getReportTemplatePath("taskExportTemplate.xls");
        final File reportFile = File.createTempFile("task_export_excel_report", ".xls");
        reportFile.deleteOnExit();
        LOG.info("'Task Export to Excel' report file will be written to the following location: {}", reportFile.getAbsolutePath());

        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    final XLSTransformer transformer = new XLSTransformer();
                    LOG.debug("XLS transform operation called with seperate thread {}", Thread.currentThread().getId());
                    InputStream is = new FileInputStream(templateFilePath);
                    HSSFWorkbook workbook = transformer.transformXLS(is, excelMap);
                    is.close();
                    LOG.debug("Workbook created - writing to file '{}'...", reportFile.getAbsolutePath());
                    OutputStream os = new FileOutputStream(reportFile);
                    workbook.write(os);
                    os.flush();
                    LOG.debug("file writing operation finished {}", Thread.currentThread().getId());
                    os.close();
                } catch (Exception ex) {
                    LOG.error("Exception thrown transforming report: {}", ex.getMessage());
                    LOG.error("Report requested by: {}, org name: {}", getAuthenticatedUser().getDisplayName(), getAuthenticatedUser().getOrganisationName());
                    getSession().put("exceptionThrown", true);
                }
            }
        };

        Thread t = new Thread(r);
        t.setDaemon(true);
        t.start();

        synchronized (getSession()) {
            getSession().put("writingToFile", true);
        }

        try {
            while (!isExportTaskOperationCancelled()) {
                Thread.sleep(200);
                if (!t.isAlive()) {
                    LOG.debug("writing to file operation finished existing from the loop ");
                    break;
                }
            }

            if (isExportTaskOperationCancelled()) {
                LOG.debug("writing to file operation cancelled. in thread {}", Thread.currentThread().getId());
                t.interrupt();
                t.stop();
                t.join();
                if (!t.isAlive()) {
                    LOG.debug("writing to xls thread is dead after cancelling the operation... ");
                } else {
                    LOG.debug("writing to xls thread is still alive even after cancelling the operation... ");
                }
            }
        } catch (Exception ex) {
            LOG.error("Exception thrown while tranforming map to xls file. exception message : {} .", ex.getMessage());
            LOG.error("Report requested by: {}, org name: {}", getAuthenticatedUser().getDisplayName(), getAuthenticatedUser().getOrganisationName());
            getSession().put("exceptionThrown", true);
        }

        synchronized (getSession()) {
            if (getSession().get("exceptionThrown") != null) {
                getSession().put("numberOfTasksProcessed", null);
                getSession().put("cancelExportOperation", false);
                getSession().put("isExportFinished", true);
                getSession().put("reportFileLocation", reportFile.getAbsolutePath());
                getSession().put("writingToFile", false);
            } else {
                throw new Exception("Error Generating Report.");
            }
        }

        return true;
    }

    public String cancelTaskExportOperation() {
        synchronized (getSession()) {
            LOG.debug("export operation cancellation called ...");
            getSession().put("cancelExportOperation", true);
            if (getSession().containsKey("reportFileLocation") && getSession().get("reportFileLocation") != null) {
                getSession().put("reportFileLocation", null);
            }
            setExportCanceled(true);
        }
        return SUCCESS;
    }

    public String getExportedTasksCount() {
        synchronized (getSession()) {
            if (getSession().containsKey("numberOfTasksProcessed") && getSession().get("numberOfTasksProcessed") != null) {
                setExportedTaskCount((Integer) getSession().get("numberOfTasksProcessed"));
                setExportFinished((Boolean) getSession().get("isExportFinished"));
                setWritingToFile((Boolean) getSession().get("writingToFile"));
                setExportCanceled((Boolean) getSession().get("cancelExportOperation"));
                if (getSession().get("exceptionThrown") == null) {
                    setExceptionOccured(Boolean.FALSE);
                } else {
                    setExceptionOccured((Boolean) getSession().get("exceptionThrown"));
                }
            } else {
                setExportedTaskCount(0);
                setExportFinished((Boolean) getSession().get("isExportFinished"));
                setWritingToFile((Boolean) getSession().get("writingToFile"));
                setExportCanceled((Boolean) getSession().get("cancelExportOperation"));
                if (getSession().get("exceptionThrown") == null) {
                    setExceptionOccured(Boolean.FALSE);
                } else {
                    setExceptionOccured((Boolean) getSession().get("exceptionThrown"));
                }
                if (getSession().get("tooManyRows") == null) {
                    setTooManyRows(Boolean.FALSE);
                } else {
                    setTooManyRows((Boolean) getSession().get("tooManyRows"));
                }
            }
        }
        return SUCCESS;
    }

    public String getJsonData() {
        return "{exportedTaskCount:" + exportedTaskCount + ",isExportProcessFinished:" + exportFinished + ",exportCancelled:" + exportCanceled + ",writingToFile:" + writingToFile + ",exceptionThrown:" + exceptionThrown + ",tooManyRows:" + tooManyRows + "}";
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
        String reportDefinationFilePath = ServletActionContext.getServletContext().getRealPath("/WEB-INF/classes/reports/" + reportTemplateName);

        return reportDefinationFilePath;
    }

    private boolean isExportTaskOperationCancelled() {
        synchronized (getSession()) {
            return (Boolean) getSession().get("cancelExportOperation");
        }
    }
}
