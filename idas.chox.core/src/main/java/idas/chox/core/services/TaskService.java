package idas.chox.core.services;

import idas.chox.core.model.Task;
import java.util.List;

/**
 *
 * @author John
 */
public interface TaskService {
    public List<Task> getAllTasks();
    public List<Task> getIncompleteTasks();
    public List<Task> getAllVisibleTasks(int webUserId, boolean hasOwnership, boolean hasWorkgroups);
    public List<Task> getIncompleteVisibleTasks(int webUserId, boolean hasOwnership, boolean hasWorkgroups);
    public List<Task> getAllTasksByClaim(int claimId);
    public List<Task> getIncompleteTasksByClaim(int claimId);
    public List<Task> getIncompleteTasksByClaim(int webUserId, int claimId);
    public List<Task> getAllTasksByClaim(int webUserId, int claimId);
    public void markTaskAsComplete(int webUserId, int taskId);
    public void autoCompleteTasksForClaim(int claimId);
    public void autoUndoCompleteTasksForClaim(int claimId);
    public void createNewTask(Task task);
}
