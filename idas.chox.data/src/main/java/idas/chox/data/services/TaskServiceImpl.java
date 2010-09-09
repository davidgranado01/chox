package idas.chox.data.services;

import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Task;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.model.WebUserUserRole;
import idas.chox.core.model.WebUserWorkgroup;
import idas.chox.core.services.TaskService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
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
    public List<Task> getAllTasks() {
        return getTasks(null, false, false, false, false, false);
    }

    @Override
    public List<Task> getAllVisibleTasks(int webUserId, boolean isCHO, boolean hasOwnership, boolean hasWorkgroups, boolean isCH) {
        return getTasks(webUserId, false, isCHO, hasOwnership, hasWorkgroups, isCH);
    }

    @Override
    public List<Task> getIncompleteTasks() {
        return getTasks(null, true, false, false, false, false);
    }

    @Override
    public List<Task> getIncompleteVisibleTasks(int webUserId, boolean isCHO, boolean hasOwnership, boolean hasWorkgroups, boolean isCH) {
        return getTasks(webUserId, true, isCHO, hasOwnership, hasWorkgroups, isCH);
    }


    @Override
    public List<Task> getAllTasksByClaim(int webUserId, int claimId, boolean isCHO) {
        return getTasksByClaim(webUserId, claimId, false, isCHO);
    }

    @Override
    public List<Task> getIncompleteTasksByClaim(int webUserId, int claimId, boolean isCHO) {
        return getTasksByClaim(webUserId, claimId, true, isCHO);
    }

    @Override
    public List<Task> getAllTasksByClaim(int claimId) {
        return getTasksByClaim(null, claimId, false, false);
    }

    @Override
    public List<Task> getIncompleteTasksByClaim(int claimId) {
        return getTasksByClaim(null, claimId, true, false);
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

        if (task.getRelatedTask() != null) {
            LOG.debug("Marking related task as complete: {}", task.getRelatedTask().getId());
//            task = (Task)get(Task.class, task.getRelatedTask().getId());
//            if (task != null && !task.getComplete()) {
                markTaskAsComplete(task.getRelatedTask());
//            }
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void createNewTask(Task task) {
        if (!task.getInsurer() && task.getVisibility() == 3) {
            // Need to set visibility role depending upon the claim status for CHO external tasks
            if (task.getClaim() == null)
                throw new IllegalArgumentException("Claim number must be present for external tasks");
            String claimStatus = task.getClaim().getStatus();
            if (claimStatus.equals(ClaimStatus.AWAITING_INVOICE_PAYMENT)) {
                task.setVisibilityRole(WebUserRole.ROLE_PC);
                //Also create a new task visible by CH
                Task taskCH = new Task();
                taskCH.setComplete(Boolean.FALSE);
                taskCH.setDescription(task.getDescription());
                taskCH.setDueDate(task.getDueDate());
                taskCH.setType(task.getType());
                taskCH.setVisibility(task.getVisibility());
                taskCH.setInsurer(task.getInsurer());
                taskCH.setVisibilityRole(WebUserRole.ROLE_CH);
                taskCH.setRelatedTask(task);
                task.setRelatedTask(taskCH);
                this.save(taskCH);
            }
            else if(claimStatus.equals(ClaimStatus.AWAITING_LIABILITY_RESOLUTION)) {
                task.setVisibilityRole(WebUserRole.ROLE_CH);
            }
            else if(claimStatus.equals(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO)) {
                task.setVisibilityRole(WebUserRole.ROLE_CH);
            }
            else if(claimStatus.equals(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA)) {
                task.setVisibilityRole(WebUserRole.ROLE_CH);
            }
            else if(claimStatus.equals(ClaimStatus.CLAIM_CLOSED)) {
                task.setVisibilityRole(WebUserRole.ROLE_CH);
            }
            else if(claimStatus.equals(ClaimStatus.CLAIM_PENDING)) {
                task.setVisibilityRole(WebUserRole.ROLE_CH);
            }
            else if(claimStatus.equals(ClaimStatus.CLAIM_REF_TO_ENG)) {
                task.setVisibilityRole(WebUserRole.ROLE_INS_SCR);
            }
            else if(claimStatus.equals(ClaimStatus.CLAIM_REJECTED)) {
                task.setVisibilityRole(WebUserRole.ROLE_CH);
            }
            else if(claimStatus.equals(ClaimStatus.CLAIM_REJECTION_ACCEPTED)) {
                task.setVisibilityRole(WebUserRole.ROLE_CH);
            }
            else if(claimStatus.equals(ClaimStatus.CLAIM_REJECTION_CONTESTED)) {
                task.setVisibilityRole(WebUserRole.ROLE_CH);
            }
            else if(claimStatus.equals(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED)) {
                task.setVisibilityRole(WebUserRole.ROLE_CH);
            }
            else if(claimStatus.equals(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED)) {
                task.setVisibilityRole(WebUserRole.ROLE_COM);
            }
            else if(claimStatus.equals(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED)) {
                task.setVisibilityRole(WebUserRole.ROLE_CR);
            }
            else if(claimStatus.equals(ClaimStatus.CLAIM_UPDATE_BY_ENG)) {
                task.setVisibilityRole(WebUserRole.ROLE_CH);
            }
            else if(claimStatus.equals(ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO)) {
                task.setVisibilityRole(WebUserRole.ROLE_CH);
            }
            else if(claimStatus.equals(ClaimStatus.CONTESTED_INVOICE_REF_TO_INS)) {
                task.setVisibilityRole(WebUserRole.ROLE_CH);
            }
            else if(claimStatus.equals(ClaimStatus.INVOICE_APPROVED_BY_BRE)) {
                task.setVisibilityRole(WebUserRole.ROLE_CH);
            }
            else if(claimStatus.equals(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT)) {
                task.setVisibilityRole(WebUserRole.ROLE_CH);
            }
            else if(claimStatus.equals(ClaimStatus.INVOICE_ESCALATED)) {
                task.setVisibilityRole(WebUserRole.ROLE_INS_SCR);
            }
            else if(claimStatus.equals(ClaimStatus.INVOICE_ESCALATED_TO_CH)) {
                task.setVisibilityRole(WebUserRole.ROLE_CH);
            }
            else if(claimStatus.equals(ClaimStatus.INVOICE_PAYMENT_LOGGED)) {
                task.setVisibilityRole(WebUserRole.ROLE_CH);
            }
            else if(claimStatus.equals(ClaimStatus.INVOICE_PAYMENT_RECEIVED)) {
                task.setVisibilityRole(WebUserRole.ROLE_CH);
            }
            else if(claimStatus.equals(ClaimStatus.INVOICE_REF_TO_CH)) {
                task.setVisibilityRole(WebUserRole.ROLE_CH);
            }
            else if(claimStatus.equals(ClaimStatus.INVOICE_REF_TO_ENG)) {
                task.setVisibilityRole(WebUserRole.ROLE_INS_SCR);
            }
            else if(claimStatus.equals(ClaimStatus.INVOICE_REJECTED_ACCEPTED)) {
                task.setVisibilityRole(WebUserRole.ROLE_CH);
            }
            else if(claimStatus.equals(ClaimStatus.CLAIM_REFERRED_TO_FNOL)) {
                task.setVisibilityRole(WebUserRole.ROLE_FNOL);
            }
            else  {
                throw new IllegalArgumentException("Sorry, cannot raise a task on a claim when the status is '" + claimStatus + "'");
            }
        }
        this.save(task);
    }

    private List<Task> getTasks(WebUser user, boolean incompleteOnly, boolean isCHO, boolean hasOwnership, boolean hasWorkgroups, boolean isCH) {
        List<Task> results = null;

        LOG.debug("isCHO={}, isCH={}", isCHO, isCH);
        DetachedCriteria criteria = DetachedCriteria.forClass(Task.class);
        if (incompleteOnly) {
            LOG.debug("Restricting to incomplete tasks");
            criteria.add(Restrictions.eq("complete", Boolean.FALSE));
        }
        // Add visibility restrictions
        if (user != null) {
            LOG.debug("Getting tasks for user with id={} ('{}')", user.getId(), user.getFullName());
            // Restrict to private tasks that user owns
            criteria.add(Restrictions.eq("visibility", 1));
//            criteria.createCriteria("createdBy").add(Restrictions.eq("id", user.getId()));
            criteria.add(Restrictions.eq("createdBy", user));
//            criteria.add(Restrictions.eq("createdBy", user.getId()));
            results = findByCriteria(criteria);
            LOG.debug("Found {} private tasks", results.size());
            if (isCHO) { // user is a CHO user
                LOG.debug("User is a CHO");
                // Add CHO internal tasks with no claim number
                DetachedCriteria criteria2 = DetachedCriteria.forClass(Task.class);
                if (incompleteOnly) {
                    criteria2.add(Restrictions.eq("complete", Boolean.FALSE));
                }
                criteria2.add(Restrictions.eq("insurer", Boolean.FALSE));
                criteria2.add(Restrictions.eq("visibility", 2));
                criteria2.add(Restrictions.isNull("claim"));
                criteria2.createCriteria("createdBy").add(Restrictions.eq("chorganisation", user.getChorganisation()));
                List<Task> results2 = findByCriteria(criteria2);
                LOG.debug("Found {} CHO internal tasks with no claim number", results2.size());
                results.addAll(results2);

                // Add CHO internal tasks on claims user owns
                DetachedCriteria criteria3 = DetachedCriteria.forClass(Task.class);
                if (incompleteOnly) {
                    criteria3.add(Restrictions.eq("complete", Boolean.FALSE));
                }
                criteria3.add(Restrictions.eq("insurer", Boolean.FALSE));
                criteria3.add(Restrictions.eq("visibility", 2));
//                criteria3.createCriteria("claim").add(Restrictions.eq("claimOwner", user));
//                criteria3.createCriteria("claim").createCriteria("supplierClaimOwner").add(Restrictions.eq("id", user.getId()));
                criteria3.createCriteria("claim").add(Restrictions.eq("supplierClaimOwner", user));
                List<Task> results3 = findByCriteria(criteria3);
                LOG.debug("Found {} CHO internal tasks on claims user owns", results3.size());
                results.addAll(results3);

                // Add CHO internal tasks on claims nobody owns (but of this CHO)
                DetachedCriteria criteria4 = DetachedCriteria.forClass(Task.class);
                if (incompleteOnly) {
                    criteria4.add(Restrictions.eq("complete", Boolean.FALSE));
                }
                criteria4.add(Restrictions.eq("insurer", Boolean.FALSE));
                criteria4.add(Restrictions.eq("visibility", 2));
                criteria4.createCriteria("claim").add(Restrictions.eq("chorganisation", user.getChorganisation())).add(Restrictions.isNull("supplierClaimOwner"));
      //          criteria4.createCriteria("claim").add(Restrictions.isNull("supplierClaimOwner"));
                List<Task> results4 = findByCriteria(criteria4);
                LOG.debug("Found {} CHO internal tasks on claims nobody owns", results4.size());
                results.addAll(results4);

                // Add Insurer external tasks on claims user own
                DetachedCriteria criteria5 = DetachedCriteria.forClass(Task.class);
                if (incompleteOnly) {
                    criteria5.add(Restrictions.eq("complete", Boolean.FALSE));
                }
                criteria5.add(Restrictions.eq("insurer", Boolean.TRUE));
                criteria5.add(Restrictions.eq("visibility", 3));
//                criteria5.createCriteria("claim").add(Restrictions.eq("claimOwner", user));
                criteria5.createCriteria("claim").add(Restrictions.eq("supplierClaimOwner", user));
                List<Task> results5 = findByCriteria(criteria5);
                LOG.debug("Found {} Insurer external tasks on claims user owns", results5.size());
                results.addAll(results5);

                // Add Insurer external tasks on claims nobody owns
                DetachedCriteria criteria6 = DetachedCriteria.forClass(Task.class);
                if (incompleteOnly) {
                    criteria6.add(Restrictions.eq("complete", Boolean.FALSE));
                }
                criteria6.add(Restrictions.eq("insurer", Boolean.TRUE));
                criteria6.add(Restrictions.eq("visibility", 3));
                criteria6.createCriteria("claim").add(Restrictions.eq("chorganisation", user.getChorganisation())).add(Restrictions.isNull("supplierClaimOwner"));
      //          criteria6.createCriteria("claim").add(Restrictions.isNull("supplierClaimOwner"));
                List<Task> results6 = findByCriteria(criteria6);
                LOG.debug("Found {} Insurer external tasks on claims nobody owns", results6.size());
                results.addAll(results6);
            }
            else { // user is an Insurer user
                LOG.debug("User is an Insurer");
                // First, get list of users roles
                List<WebUserRole> webUserRole = getUserRoles(user);

                // Add all Insurer internal tasks with no claim assigned to a role that user is in
                DetachedCriteria criteria2 = DetachedCriteria.forClass(Task.class);
                if (incompleteOnly) {
                    criteria2.add(Restrictions.eq("complete", Boolean.FALSE));
                }
                criteria2.add(Restrictions.eq("insurer", Boolean.TRUE));
                criteria2.add(Restrictions.eq("visibility", 2));
                criteria2.add(Restrictions.isNull("claim"));
                criteria2.createCriteria("createdBy").add(Restrictions.eq("insurer", user.getInsurer()));
                List<Task> results2 = findByCriteria(criteria2);
                LOG.debug("Found {} Insurer internal tasks with no claim number - will restrict to a role of user", results2.size());
                results.addAll(restrictTasksToRoles(results2, webUserRole));

                if (isCH) { // user is a claims handler
                    if (hasOwnership) { // Claim Ownership is enabled for the Insurer
                        // Add all Insurer internal tasks assigned to CH on claims user owns
                        DetachedCriteria criteria3 = DetachedCriteria.forClass(Task.class);
                        if (incompleteOnly) {
                            criteria3.add(Restrictions.eq("complete", Boolean.FALSE));
                        }
                        criteria3.add(Restrictions.eq("insurer", Boolean.TRUE));
                        criteria3.add(Restrictions.eq("visibility", 2));
                        criteria3.add(Restrictions.eq("visibilityRole", "ROLE_INS_CH"));
//                            criteria3.createCriteria("claim").add(Restrictions.eq("claimOwner", user));
                        criteria3.createCriteria("claim").add(Restrictions.eq("claimOwner", user));
                        List<Task> results3 = findByCriteria(criteria3);
                        LOG.debug("Found {} Insurer (with ownership) internal tasks assigned to CH on claims user owns", results3.size());
                        results.addAll(results3);

                        // Add all Insurer internal tasks assigned to CH on claims no-one owns
                        DetachedCriteria criteria5 = DetachedCriteria.forClass(Task.class);
                        if (incompleteOnly) {
                            criteria5.add(Restrictions.eq("complete", Boolean.FALSE));
                        }
                        criteria5.add(Restrictions.eq("insurer", Boolean.TRUE));
                        criteria5.add(Restrictions.eq("visibility", 2));
                        criteria5.add(Restrictions.eq("visibilityRole", "ROLE_INS_CH"));
//                            criteria3.createCriteria("claim").add(Restrictions.eq("claimOwner", user));
                        criteria5.createCriteria("claim").add(Restrictions.isNull("claimOwner")).add(Restrictions.eq("insurer", user.getInsurer()));
      //                  criteria5.createCriteria("claim").add(Restrictions.eq("insurer", user.getInsurer()));
                        List<Task> results5 = findByCriteria(criteria5);
                        LOG.debug("Found {} Insurer (with ownership) internal tasks assigned to claims with no ownership", results5.size());
                        results.addAll(results5);

                        // Add all CHO external tasks assigned to CH on claims user owns
                        DetachedCriteria criteria4 = DetachedCriteria.forClass(Task.class);
                        if (incompleteOnly) {
                            criteria4.add(Restrictions.eq("complete", Boolean.FALSE));
                        }
                        criteria4.add(Restrictions.eq("insurer", Boolean.FALSE));
                        criteria4.add(Restrictions.eq("visibility", 3));
                        criteria4.add(Restrictions.eq("visibilityRole", "ROLE_INS_CH"));
                        criteria4.createCriteria("claim").add(Restrictions.eq("claimOwner", user));
                        List<Task> results4 = findByCriteria(criteria4);
                        LOG.debug("Found {} CHO external tasks assigned to CH on claims user owns", results4.size());
                        results.addAll(results4);
                    }
                    else if (hasWorkgroups) { // Workgroups are enabled for the Insurer
                        // Add all Insurer internal tasks assigned to CH on claims assigned to a workgroup that user is in
                        // Lets first get the users workgroups
                        List<Integer> userWorkgroups = getUserWorkgroupIds(user);

                        DetachedCriteria criteria3 = DetachedCriteria.forClass(Task.class);
                        if (incompleteOnly) {
                            criteria3.add(Restrictions.eq("complete", Boolean.FALSE));
                        }
                        criteria3.add(Restrictions.eq("insurer", Boolean.TRUE));
                        criteria3.add(Restrictions.eq("visibility", 2));
                        criteria3.add(Restrictions.eq("visibilityRole", "ROLE_INS_CH"));
//                        criteria3.createCriteria("claim").add(Restrictions.in("workgroup", userWorkgroups));
                        criteria3.createCriteria("claim").createCriteria("workgroup").add(Restrictions.in("id", userWorkgroups));

                        List<Task> results3 = findByCriteria(criteria3);
                        LOG.debug("Found {} Insurer (with workgroups) internal tasks assigned to CH on claims assigned to workgroup of user", results3.size());
                        results.addAll(results3);

                        // Add all Insurer internal tasks assigned to CH on claims with no workgroup yet assigned
                        DetachedCriteria criteria5 = DetachedCriteria.forClass(Task.class);
                        if (incompleteOnly) {
                            criteria5.add(Restrictions.eq("complete", Boolean.FALSE));
                        }
                        criteria5.add(Restrictions.eq("insurer", Boolean.TRUE));
                        criteria5.add(Restrictions.eq("visibility", 2));
                        criteria5.add(Restrictions.eq("visibilityRole", "ROLE_INS_CH"));
//                        criteria5.createCriteria("claim").add(Restrictions.in("workgroup", userWorkgroups));
                        criteria5.createCriteria("claim").add(Restrictions.isNull("workgroup")).add(Restrictions.eq("insurer", user.getInsurer()));
      //                  criteria5.createCriteria("claim").add(Restrictions.eq("insurer", user.getInsurer()));

                        List<Task> results5 = findByCriteria(criteria5);
                        LOG.debug("Found {} Insurer (with workgroups) internal tasks assigned to CH on claims with no workgroup yet assigned", results5.size());
                        results.addAll(results5);

                        // Add all CHO external tasks assigned to CH on claims assigned to a workgroup that user is in
                        DetachedCriteria criteria4 = DetachedCriteria.forClass(Task.class);
                        if (incompleteOnly) {
                            criteria4.add(Restrictions.eq("complete", Boolean.FALSE));
                        }
                        criteria4.add(Restrictions.eq("insurer", Boolean.FALSE));
                        criteria4.add(Restrictions.eq("visibility", 3));
                        criteria4.add(Restrictions.eq("visibilityRole", "ROLE_INS_CH"));
                        criteria4.createCriteria("claim").add(Restrictions.in("workgroup", userWorkgroups));
//                        criteria4.createCriteria("claim").createCriteria("workgroup").add(Restrictions.in("id", userWorkgroups));

                        List<Task> results4 = findByCriteria(criteria4);
                        LOG.debug("Found {} CHO external (to Insurer with with workgroups)  tasks assigned to CH on claims assigned to workgroup of user", results4.size());
                        results.addAll(results4);

                    }
                    else { // CH, no ownership and no workgroups
                        // Add all Insurer internal tasks assigned to a claim and CH role
                        DetachedCriteria criteria3 = DetachedCriteria.forClass(Task.class);
                        if (incompleteOnly) {
                            criteria3.add(Restrictions.eq("complete", Boolean.FALSE));
                        }
                        criteria3.add(Restrictions.eq("insurer", Boolean.TRUE));
                        criteria3.add(Restrictions.eq("visibility", 2));
                        criteria3.add(Restrictions.eq("visibilityRole", "ROLE_INS_CH"));
//                            criteria3.createCriteria("claim").add(Restrictions.eq("claimOwner", user));
      //                  criteria3.add(Restrictions.isNotNull("claim"));
                        criteria3.createCriteria("claim").add(Restrictions.eq("insurer", user.getInsurer()));
                        List<Task> results3 = findByCriteria(criteria3);
                        LOG.debug("Found {} Insurer (no ownership, no workgroups) internal tasks assigned to CH and a claim", results3.size());
                        results.addAll(results3);

                        // Add all CHO external tasks assigned to CH
                        DetachedCriteria criteria4 = DetachedCriteria.forClass(Task.class);
                        if (incompleteOnly) {
                            criteria4.add(Restrictions.eq("complete", Boolean.FALSE));
                        }
                        criteria4.add(Restrictions.eq("insurer", Boolean.FALSE));
                        criteria4.add(Restrictions.eq("visibility", 3));
                        criteria4.add(Restrictions.eq("visibilityRole", "ROLE_INS_CH"));
//                            criteria4.createCriteria("claim").add(Restrictions.eq("claimOwner", user));
                        criteria4.createCriteria("claim").add(Restrictions.eq("insurer", user.getInsurer()));
                        List<Task> results4 = findByCriteria(criteria4);
                        LOG.debug("Found {} CHO external tasks assigned to CH (with Insurer having no claim ownership & no workgroups", results4.size());
                        results.addAll(results4);
                    }
                } // Not CH
                else {
                    // Add all Insurer internal tasks with a claim assigned to a role that user is in
                    DetachedCriteria criteria3 = DetachedCriteria.forClass(Task.class);
                    if (incompleteOnly) {
                        criteria3.add(Restrictions.eq("complete", Boolean.FALSE));
                    }
                    criteria3.add(Restrictions.eq("insurer", Boolean.TRUE));
                    criteria3.add(Restrictions.eq("visibility", 2));
                    criteria3.add(Restrictions.isNotNull("claim"));
                    criteria3.createCriteria("createdBy").add(Restrictions.eq("insurer", user.getInsurer()));
                    List<Task> results3 = findByCriteria(criteria3);
                    LOG.debug("Found {} Insurer internal tasks with claim number - will restrict to a role of user", results3.size());
                    results.addAll(restrictTasksToRoles(results3, webUserRole));

                    // Add all CHO external tasks assigned to a role user is in
                    DetachedCriteria criteria4 = DetachedCriteria.forClass(Task.class);
                    if (incompleteOnly) {
                        criteria4.add(Restrictions.eq("complete", Boolean.FALSE));
                    }
                    criteria4.add(Restrictions.eq("insurer", Boolean.FALSE));
                    criteria4.add(Restrictions.eq("visibility", 3));
                    criteria4.createCriteria("claim").add(Restrictions.eq("insurer", user.getInsurer()));

                    List<Task> results4 = findByCriteria(criteria4);
                    LOG.debug("Found {} CHO external tasks - will restrict to a role of user", results4.size());
                    results.addAll(restrictTasksToRoles(results4, webUserRole));
                }
            }

        }
        else { // No user specified - get all tasks
            LOG.debug("Getting all tasks (no user specified)");
            results = findByCriteria(criteria);
            LOG.debug("Found {} tasks", results.size());
        }

        return removeDuplicateTasks(results);
    }

    private List<Task> restrictTasksToRoles(List<Task> tasks, List<WebUserRole> roles) {
        List<Task> restrictedList = new ArrayList<Task>();
        for (Task task : tasks) {
            for (WebUserRole role : roles) {
                if (role.getName().equals(task.getVisibilityRole())) {
                    restrictedList.add(task);
                    LOG.debug("Task '{}' is visible to user with role '{}'", task.getId(), role.getName());
                }
                else
                    LOG.debug("Task '{}' is not visible to user in role '{}'", task.getId(), role.getName());
            }
        }
        return restrictedList;
    }
    private List<WebUserRole> getUserRoles(WebUser user) {
        // return a list of roles allocated to the user
        List<WebUserRole> userRoles = new ArrayList<WebUserRole>();
        DetachedCriteria roleCriteria = DetachedCriteria.forClass(WebUserUserRole.class);
        roleCriteria.createCriteria("webUser").add(Restrictions.eq("id", user.getId()));
        List<WebUserUserRole> webUserUserRoles = findByCriteria(roleCriteria);

        for (WebUserUserRole webUserUserRole : webUserUserRoles) {
            userRoles.add(webUserUserRole.getWebUserRole());
            LOG.debug("Usr in role '{}'", webUserUserRole.getWebUserRole().getName());
        }

        LOG.debug("User has {} roles", userRoles.size());
        return userRoles;
    }

    private List<Integer> getUserWorkgroupIds(WebUser user) {
        // return a list of roles allocated to the user
        List<Integer> userWorkgroupIds = new ArrayList<Integer>();
        DetachedCriteria workgroupCriteria = DetachedCriteria.forClass(WebUserWorkgroup.class);
        workgroupCriteria.createCriteria("userser").add(Restrictions.eq("id", user.getId()));
        List<WebUserWorkgroup> webUserWorkgroups = findByCriteria(workgroupCriteria);

        for (WebUserWorkgroup webUserWorkgroup : webUserWorkgroups) {
            userWorkgroupIds.add(webUserWorkgroup.getWorkgroup().getId());
            LOG.debug("Usr in workgroup with id={}", webUserWorkgroup.getWorkgroup().getId());
        }

        LOG.debug("User belongs to {} workgroups", userWorkgroupIds.size());
        return userWorkgroupIds;
    }

    private List<Task> getTasks(int webUserId, boolean incompleteOnly, boolean isCHO, boolean hasOwnership, boolean hasWorkgroups, boolean isCH) {
        WebUser webUser = null;
        if (webUserId > 0) {
            webUser = (WebUser) get(WebUser.class, webUserId);
            if (webUser == null) {
                LOG.error("No such user found with id={}", webUserId);
                throw new IllegalArgumentException("No such user.");
            }
        }
        return getTasks(webUser, incompleteOnly, isCHO, hasOwnership, hasWorkgroups, isCH);
    }

    private List<Task> getTasksByClaim(int webUserId, int claimId, boolean incompleteOnly, boolean isCHO) {
        WebUser webUser = null;
        if (webUserId > 0) {
            webUser = (WebUser) get(WebUser.class, webUserId);
            if (webUser == null) {
                LOG.error("No such user found with id={}", webUserId);
                throw new IllegalArgumentException("No such user.");
            }
        }
        return getTasksByClaim(webUser, claimId, incompleteOnly, isCHO);
    }


    private List<Task> getTasksByClaim(WebUser user, int claimId, boolean incompleteOnly, boolean isCHO) {
        List<Task> results = null;

        DetachedCriteria criteria = DetachedCriteria.forClass(Task.class);
        criteria.createCriteria("claim").add(Restrictions.eq("id", claimId));
        if (incompleteOnly) {
            criteria.add(Restrictions.eq("complete", false));
        }
        // Add visibility restrictions
        if (user != null) {
            // Restrict to tasks that user created
//            criteria.add(Restrictions.eq("visibility", 1));
            criteria.add(Restrictions.eq("createdBy", user));
            results = findByCriteria(criteria);
            LOG.debug("Found {} private tasks", results.size());
            if (isCHO) { // user is a CHO user
                LOG.debug("User is a CHO");
                // Add CHO  tasks on claim
                DetachedCriteria criteria2 = DetachedCriteria.forClass(Task.class);
                if (incompleteOnly) {
                    criteria2.add(Restrictions.eq("complete", Boolean.FALSE));
                }
                criteria2.add(Restrictions.eq("insurer", Boolean.FALSE));
                criteria2.add(Restrictions.eq("visibility", 2));
                criteria2.createCriteria("claim").add(Restrictions.eq("id", claimId));
                List<Task> results2 = findByCriteria(criteria2);
                LOG.debug("Found {} CHO internal tasks ", results2.size());
                results.addAll(results2);

                // Add Insurer external tasks on claim
                DetachedCriteria criteria5 = DetachedCriteria.forClass(Task.class);
                if (incompleteOnly) {
                    criteria5.add(Restrictions.eq("complete", Boolean.FALSE));
                }
                criteria5.add(Restrictions.eq("insurer", Boolean.TRUE));
                criteria5.add(Restrictions.eq("visibility", 3));
//                criteria5.createCriteria("claim").add(Restrictions.eq("claimOwner", user));
                criteria5.createCriteria("claim").add(Restrictions.eq("id", claimId));
                List<Task> results5 = findByCriteria(criteria5);
                LOG.debug("Found {} Insurer external tasks on claims", results5.size());
                results.addAll(results5);
            }
            else { // user is an Insurer user
                LOG.debug("User is an Insurer");

                // Add all Insurer tasks on claim
                DetachedCriteria criteria2 = DetachedCriteria.forClass(Task.class);
                if (incompleteOnly) {
                    criteria2.add(Restrictions.eq("complete", Boolean.FALSE));
                }
                criteria2.add(Restrictions.eq("insurer", Boolean.TRUE));
//                criteria2.add(Restrictions.eq("visibility", 2));
                criteria2.createCriteria("claim").add(Restrictions.eq("id", claimId));

                List<Task> results2 = findByCriteria(criteria2);
                LOG.debug("Found {} Insurer internal tasks on claim", results2.size());
                results.addAll(results2);

                // Add CHO external tasks on claim
                DetachedCriteria criteria3 = DetachedCriteria.forClass(Task.class);
                if (incompleteOnly) {
                    criteria3.add(Restrictions.eq("complete", Boolean.FALSE));
                }
                criteria3.add(Restrictions.eq("insurer", Boolean.FALSE));
                criteria3.add(Restrictions.eq("visibility", 3));
                criteria3.createCriteria("claim").add(Restrictions.eq("id", claimId));
                List<Task> results3 = findByCriteria(criteria3);
                LOG.debug("Found {} CHO external tasks ", results3.size());
                results.addAll(results3);
            }

        }
        else { // No user specified - get all tasks
            results = findByCriteria(criteria);
            LOG.debug("Found {} tasks", results.size());
        }

        // Remove duplicate/linked tasks and return

        return removeDuplicateTasks(results);
    }

    private void markTaskAsComplete(Task task) {
        task.setComplete(Boolean.TRUE);
        task.setCompletedDate(new Date());
        task.setCompletedBy(getCurrentUser());
        save(task);
    }

    private List<Task> removeDuplicateTasks(List<Task> tasks) {
        List<Task> results = new ArrayList<Task>();

        for (Task task : tasks) {
            if (!results.contains(task) && (task.getRelatedTask() == null || !isTaskInList(task.getRelatedTask().getId(), results)))
                results.add(task);
        }

        return results;
    }

    private boolean isTaskInList(int taskId, List<Task> tasks) {
        for (Task task : tasks)
            if (task.getId() == taskId)
                return true;

        return false;
    }

 }
