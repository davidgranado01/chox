package idas.chox.core.services;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Task;
import idas.chox.core.model.WebUser;
import java.util.List;

/**
 *
 * @author John
 */
public interface TaskService {
    public List<Task> getAllTasks(WebUser user);
    public List<Task> getAllTasks(int webUserId);
    public List<Task> getAllTasksByClaim(Claim claim);
    public List<Task> getAllTasksByClaim(int claimId);
    public List<Task> getIncompleteTasks(WebUser user);
    public List<Task> getIncompleteTasks(int webUserId);
    public List<Task> getIncompleteTasksByClaim(Claim claim);
    public List<Task> getIncompleteTasksByClaim(int claimId);
    public void markTaskAsComplete(Task task);
    public void markTaskAsComplete(int taskId);
    public void createNewTask(Task task);
}
