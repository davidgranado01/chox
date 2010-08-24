package idas.chox.data.services;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Task;
import idas.chox.core.model.WebUser;
import idas.chox.core.services.TaskService;
import java.util.Date;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author John
 */
public class TaskServiceImpl extends SecureDataService implements TaskService {
    private static final Logger LOG = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Override
    public List<Task> getAllTasks(WebUser user) {
        return getTasks(user, false);
    }

    @Override
    public List<Task> getAllTasksByClaim(Claim claim) {
        return getTasksByClaim(claim.getId(), false);
    }

    @Override
    public List<Task> getIncompleteTasks(WebUser user) {
        return getTasks(user, true);
    }

    @Override
    public List<Task> getIncompleteTasksByClaim(Claim claim) {
        return getTasksByClaim(claim.getId(), true);
    }

    @Override
    public List<Task> getAllTasks(int webUserId) {
        return getTasks(webUserId, false);
    }

    @Override
    public List<Task> getAllTasksByClaim(int claimId) {
        return getTasksByClaim(claimId, false);
    }

    @Override
    public List<Task> getIncompleteTasks(int webUserId) {
        return getTasks(webUserId, true);
    }

    @Override
    public List<Task> getIncompleteTasksByClaim(int claimId) {
        return getTasksByClaim(claimId, true);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void markTaskAsComplete(Task task) {
        task.setComplete(Boolean.TRUE);
        task.setCompletedDate(new Date());
        task.setCompletedBy(getCurrentUser());
        save(task);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void markTaskAsComplete(int taskId) {
        Task task = (Task) get(Task.class, taskId);
        if (task == null) {
            LOG.error("No such task found with id={}", taskId);
            throw new IllegalArgumentException("No such task.");
        }
        else if (task.getComplete()) {
            LOG.error("Task already complete: id={}", taskId);
            throw new IllegalArgumentException("Task has already been completed.");
        }
        markTaskAsComplete(task);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void createNewTask(Task task) {
        this.save(task);
    }

    private List<Task> getTasks(WebUser user, boolean incompleteOnly) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Task.class);
        if (incompleteOnly) {
            criteria.add(Restrictions.eq("complete", false));
        }
        // ToDo: add visibility restrictions
        criteria.addOrder(Order.desc("dueDate"));
        LOG.debug("Querying for tasks...");
        return findByCriteria(criteria);
    }

    private List<Task> getTasks(int webUserId, boolean incompleteOnly) {
        WebUser webUser = (WebUser) get(WebUser.class, webUserId);
        if (webUser == null) {
            LOG.error("No such user found with id={}", webUserId);
            throw new IllegalArgumentException("No such user.");
        }
        return getTasks(webUser, incompleteOnly);
    }


    private List<Task> getTasksByClaim(int claimId, boolean incompleteOnly) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Task.class);
        criteria.createCriteria("claim").add(Restrictions.eq("id", claimId));
        if (incompleteOnly) {
            criteria.add(Restrictions.eq("complete", false));
        }
        criteria.addOrder(Order.asc("dueDate"));
        LOG.debug("Querying for tasks by claim...");
        return findByCriteria(criteria);
    }
}
