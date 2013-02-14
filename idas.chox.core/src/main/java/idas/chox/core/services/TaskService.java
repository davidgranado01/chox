package idas.chox.core.services;

import idas.chox.core.model.Task;
import idas.chox.core.search.SearchResult;
import java.util.List;

/**
 *
 * @author John
 */
public interface TaskService {
    public SearchResult getAllTasks(int start, int limit, String sort, String dir);
    public SearchResult getIncompleteTasks(int start, int limit, String sort, String dir);
    public SearchResult getAllVisibleTasks(int webUserId, boolean hasOwnership, boolean hasWorkgroups, int start, int limit, String sort, String dir, boolean showAssignedTasksOnly);
    public SearchResult getIncompleteVisibleTasks(int webUserId, boolean hasOwnership, boolean hasWorkgroups, int start, int limit, String sort, String dir, boolean showAssignedTasksOnly);
    public List<Task> getAllTasksByClaim(int claimId);
    public List<Task> getIncompleteTasksByClaim(int claimId);
    public List<Task> getIncompleteTasksByClaim(int webUserId, int claimId);
    public List<Task> getAllTasksByClaim(int webUserId, int claimId);
    public void markTaskAsComplete(int webUserId, int taskId);
    public void autoCompleteTasksForClaim(int claimId);
    public void autoUndoCompleteTasksForClaim(int claimId);
    public void createNewTask(Task task);
    public void deleteAllTasksByClaimId(int claimId);
}
