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
    public List<Task> getAllVisibleTasks(int webUserId, boolean isCHO, boolean hasOwnership, boolean hasWorkgroups, boolean isCH);
    public List<Task> getIncompleteVisibleTasks(int webUserId, boolean isCHO, boolean hasOwnership, boolean hasWorkgroups, boolean isCH);
    public List<Task> getAllTasksByClaim(int claimId);
    public List<Task> getIncompleteTasksByClaim(int claimId);
    public List<Task> getIncompleteTasksByClaim(int webUserId, int claimId, boolean isCHO);
    public List<Task> getAllTasksByClaim(int webUserId, int claimId, boolean isCHO);
    public void markTaskAsComplete(int taskId);
    public void createNewTask(Task task);
}
