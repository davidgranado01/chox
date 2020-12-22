package idas.chox.core.services;

import java.util.List;
import java.util.Set;

import idas.chox.core.model.Task;
import idas.chox.core.search.SearchResult;

/**
 *
 * @author John
 */
public interface TaskService {
    SearchResult getAllTasks(int start, int limit, String sort, String dir);
    SearchResult getIncompleteTasks(int start, int limit, String sort, String dir);
    SearchResult getAllVisibleTasks(int webUserId, Set<Integer> supplierClaimOwnerIds, boolean hasOwnership, boolean hasWorkgroups, int start, int limit, String sort, String dir, boolean showAssignedTasksOnly);
    int getAllVisibleTaskCount(int webUserId, boolean hasOwnership, boolean hasWorkgroups, boolean showAssignedTasksOnly);
    SearchResult getIncompleteVisibleTasks(int webUserId, Set<Integer> supplierClaimOwnerIds, boolean hasOwnership, boolean hasWorkgroups, int start, int limit, String sort, String dir, boolean showAssignedTasksOnly);
    int getIncompleteVisibleTaskCount(int webUserId, boolean hasOwnership, boolean hasWorkgroups, boolean showAssignedTasksOnly);
    List<Task> getAllTasksByClaim(int claimId);
    List<Task> getIncompleteTasksByClaim(int claimId);
    List<Task> getIncompleteTasksByClaim(int webUserId, int claimId);
    List<Task> getAllTasksByClaim(int webUserId, int claimId);
    void markTaskAsComplete(int webUserId, int taskId);
    void autoCompleteTasksForClaim(int claimId);
    void autoUndoCompleteTasksForClaim(int claimId);
    void createNewTask(Task task);
    void deleteAllTasksByClaimId(int claimId);
}
