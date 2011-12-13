package idas.chox.data.services;

import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Entity;
import idas.chox.core.model.Task;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.model.WebUserUserRole;
import idas.chox.core.model.WebUserWorkgroup;
import idas.chox.core.services.TaskService;
import idas.chox.core.services.WebUserUserRoleService;
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
    private WebUserUserRoleService webUserUserRoleService;

    public void setWebUserUserRoleService(WebUserUserRoleService webUserUserRoleService) {
        this.webUserUserRoleService = webUserUserRoleService;
    }

    @Override
    public List<Task> getAllTasks() {
        return getTasks(null, false, false, false);
    }

    @Override
    public List<Task> getAllVisibleTasks(int webUserId, boolean hasOwnership, boolean hasWorkgroups) {
        return getTasks(webUserId, false, hasOwnership, hasWorkgroups);
    }

    @Override
    public List<Task> getIncompleteTasks() {
        return getTasks(null, true, false, false);
    }

    @Override
    public List<Task> getIncompleteVisibleTasks(int webUserId, boolean hasOwnership, boolean hasWorkgroups) {
        return getTasks(webUserId, true, hasOwnership, hasWorkgroups);
    }

    @Override
    public List<Task> getAllTasksByClaim(int webUserId, int claimId) {
        return getTasksByClaim(webUserId, claimId, false);
    }

    @Override
    public List<Task> getIncompleteTasksByClaim(int webUserId, int claimId) {
        return getTasksByClaim(webUserId, claimId, true);
    }

    @Override
    public List<Task> getAllTasksByClaim(int claimId) {
        return getTasksByClaim(null, claimId, false);
    }

    @Override
    public List<Task> getIncompleteTasksByClaim(int claimId) {
        return getTasksByClaim(null, claimId, true);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void markTaskAsComplete(int webUserId, int taskId) {
        Task task = (Task) get(Task.class, taskId);
        if (task == null) {
            LOG.error("No such task found with id={}", taskId);
            throw new IllegalArgumentException("No such task.");
        } else if (task.getComplete()) {
            LOG.info("Task already complete: id={}", taskId);
            throw new IllegalArgumentException("Task has already been completed.");
        }
        WebUser webUser = null;
        if (webUserId > 0) {
            webUser = (WebUser) get(WebUser.class, webUserId);
            if (webUser == null) {
                LOG.error("No such user found with id={}", webUserId);
                throw new IllegalArgumentException("No such user.");
            }
        }
        // Check that user can mark task as complete:
        //       true if user created task
        //       true if no visibility role defined
        //       true if role defined and user is in role
        //       false otherwise
        boolean canComplete = false;

        if (task.getCreatedBy().getId() == webUser.getId()) {
            canComplete = true;
        } else if ((task.getVisibilityRole() == null || task.getVisibilityRole().length() == 0) && task.getVisibility()!= 1) {
            canComplete = true;
        } else if (userInRole(webUser, task.getVisibilityRole())) {
            canComplete = true;
        }

        if (!canComplete) {
            LOG.warn("User {} has no access to complete task {}", webUser.getFirstName() + ' ' + webUser.getLastName(), taskId);
            throw new IllegalArgumentException("You are not authorised to mark this task as complete.");
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

    private boolean userInRole(WebUser user, String roleName) {
        List<WebUserUserRole> webUserUserRoles = webUserUserRoleService.getMappedUserRole(user.getId());

        for (WebUserUserRole webUserUserRole : webUserUserRoles) {
            if (webUserUserRole.getWebUserRole().getName().equals(roleName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void createNewTask(Task task) {
        if (task.getClaim() != null) {
            // Check task management enabled for Insurer/CHO
            if ((task.getInsurer() && task.getVisibility() != 3) || (!task.getInsurer() && task.getVisibility() == 3)) {
                if (!task.getClaim().getInsurer().isTaskManagementEnable()) {
                    throw new IllegalArgumentException("Task management is not enabled for the Insurer of this claim");
                }
            } else if ((!task.getInsurer() && task.getVisibility() != 3) || (task.getInsurer() && task.getVisibility() == 3)) {
                if (!task.getClaim().getChorganisation().isTaskManagementEnable()) {
                    throw new IllegalArgumentException("Task management is not enabled for the CHO of this claim");
                }
            }
        }
        if (!task.getInsurer() && task.getVisibility() == 3) {
            // Need to set visibility role depending upon the claim status for CHO external tasks
            if (task.getClaim() == null) {
                throw new IllegalArgumentException("Claim number must be present for external tasks");
            }
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
                taskCH.setClaim(task.getClaim());
                task.setRelatedTask(taskCH);
                this.save(taskCH);
            } else if (claimStatus.equals(ClaimStatus.AWAITING_LIABILITY_RESOLUTION)) {
                task.setVisibilityRole(WebUserRole.ROLE_CH);
            } else if (claimStatus.equals(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO)) {
                task.setVisibilityRole(WebUserRole.ROLE_CH);
            } else if (claimStatus.equals(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA)) {
                task.setVisibilityRole(WebUserRole.ROLE_CH);
            } else if (claimStatus.equals(ClaimStatus.CLAIM_CLOSED)) {
                task.setVisibilityRole(WebUserRole.ROLE_CH);
            } else if (claimStatus.equals(ClaimStatus.CLAIM_PENDING)) {
                task.setVisibilityRole(WebUserRole.ROLE_CH);
            } else if (claimStatus.equals(ClaimStatus.CLAIM_REF_TO_ENG)) {
                task.setVisibilityRole(WebUserRole.ROLE_INS_SCR);
            } else if (claimStatus.equals(ClaimStatus.CLAIM_REJECTED)) {
                task.setVisibilityRole(WebUserRole.ROLE_CH);
            } else if (claimStatus.equals(ClaimStatus.SUBSCRIBER_CLAIM_REJECTED)) {
                task.setVisibilityRole(WebUserRole.ROLE_CH);
            } else if (claimStatus.equals(ClaimStatus.CLAIM_REJECTION_ACCEPTED)) {
                task.setVisibilityRole(WebUserRole.ROLE_CH);
            } else if (claimStatus.equals(ClaimStatus.CLAIM_REJECTION_CONTESTED)) {
                task.setVisibilityRole(WebUserRole.ROLE_CH);
            } else if (claimStatus.equals(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED)) {
                task.setVisibilityRole(WebUserRole.ROLE_CH);
            } else if (claimStatus.equals(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED)) {
                task.setVisibilityRole(WebUserRole.ROLE_CH);
            } else if (claimStatus.equals(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED)) {
                task.setVisibilityRole(WebUserRole.ROLE_CR);
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
                taskCH.setClaim(task.getClaim());
                task.setRelatedTask(taskCH);
                this.save(taskCH);
            } else if (claimStatus.equals(ClaimStatus.CLAIM_UPDATE_BY_ENG)) {
                task.setVisibilityRole(WebUserRole.ROLE_CH);
            } else if (claimStatus.equals(ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO)) {
                task.setVisibilityRole(WebUserRole.ROLE_CH);
            } else if (claimStatus.equals(ClaimStatus.CONTESTED_INVOICE_REF_TO_INS)) {
                task.setVisibilityRole(WebUserRole.ROLE_CH);
            } else if (claimStatus.equals(ClaimStatus.INVOICE_APPROVED_BY_BRE)) {
                task.setVisibilityRole(WebUserRole.ROLE_CH);
            } else if (claimStatus.equals(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT)) {
                task.setVisibilityRole(WebUserRole.ROLE_CH);
            } else if (claimStatus.equals(ClaimStatus.INVOICE_ESCALATED)) {
                task.setVisibilityRole(WebUserRole.ROLE_INS_SCR);
            } else if (claimStatus.equals(ClaimStatus.INVOICE_ESCALATED_TO_CH)) {
                task.setVisibilityRole(WebUserRole.ROLE_CH);
            } else if (claimStatus.equals(ClaimStatus.INVOICE_PAYMENT_LOGGED)) {
                task.setVisibilityRole(WebUserRole.ROLE_CH);
            } else if (claimStatus.equals(ClaimStatus.INVOICE_PAYMENT_RECEIVED)) {
                task.setVisibilityRole(WebUserRole.ROLE_CH);
            } else if (claimStatus.equals(ClaimStatus.INVOICE_REF_TO_CH)) {
                task.setVisibilityRole(WebUserRole.ROLE_CH);
            } else if (claimStatus.equals(ClaimStatus.INVOICE_REF_TO_ENG)) {
                task.setVisibilityRole(WebUserRole.ROLE_INS_SCR);
            } else if (claimStatus.equals(ClaimStatus.INVOICE_REJECTED_ACCEPTED)) {
                task.setVisibilityRole(WebUserRole.ROLE_CH);
            } else if (claimStatus.equals(ClaimStatus.CLAIM_REFERRED_TO_FNOL)) {
                task.setVisibilityRole(WebUserRole.ROLE_FNOL);
            } else {
                LOG.error("Cannot raise a task on a claim in status '{}'", claimStatus);
                throw new IllegalArgumentException("Sorry, cannot raise a task on a claim when the status is '" + claimStatus + "'");
            }
        } else if (task.getInsurer() && task.getVisibility() == 3) { // Insurer external task
            // No visibility role needed for CHOs so make sure its not defined
            task.setVisibilityRole(null);
        }
        this.save(task);
    }

    private List<Task> getTasks(WebUser user, boolean incompleteOnly, boolean hasOwnership, boolean hasWorkgroups) {
        boolean isCHO = false;
        List<Task> results = null;

        LOG.debug("hasOwnership={}, hasWorkgroups={}", hasOwnership, hasWorkgroups);
        DetachedCriteria criteria = DetachedCriteria.forClass(Task.class);
        if (incompleteOnly) {
            LOG.debug("Restricting to incomplete tasks");
            criteria.add(Restrictions.eq("complete", Boolean.FALSE));
        }
        try {
            // Add visibility restrictions
            if (user != null) {
                LOG.debug("Getting tasks for user with id={} ('{}')", user.getId(), user.getFullName());
                if (user.getChorganisation() != null) {
                    isCHO = true;
                }
                LOG.debug("isCHO={}", isCHO);
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

                    // Add tasks on claims nobody owns to users with an ownership based role only
                    List<WebUserUserRole> webUserUserRoles = webUserUserRoleService.getMappedUserRole(user.getId());
                    for (WebUserUserRole webUserUserRole : webUserUserRoles) {
                        if (webUserUserRole.getWebUserRole().isOwnershipRelated()) {
                            LOG.debug("Getting tasks for users role {}", webUserUserRole.getWebUserRole().getName());
                            // Add CHO internal tasks on claims nobody owns (but of this CHO)
                            DetachedCriteria criteria4 = DetachedCriteria.forClass(Task.class);
                            if (incompleteOnly) {
                                criteria4.add(Restrictions.eq("complete", Boolean.FALSE));
                            }
                            criteria4.add(Restrictions.eq("insurer", Boolean.FALSE));
                            criteria4.add(Restrictions.eq("visibility", 2));
                            criteria4.createCriteria("claim").add(Restrictions.eq("chorganisation", user.getChorganisation())).add(Restrictions.isNull("supplierClaimOwner"));
//                        criteria4.createCriteria("claim").add(Restrictions.isNull("supplierClaimOwner"));
                            List<Task> results4 = findByCriteria(criteria4);
                            LOG.debug("Found {} CHO internal tasks on claims nobody owns", results4.size());
                            results.addAll(results4);

                            // Add Insurer external tasks on claims nobody owns
                            DetachedCriteria criteria6 = DetachedCriteria.forClass(Task.class);
                            if (incompleteOnly) {
                                criteria6.add(Restrictions.eq("complete", Boolean.FALSE));
                            }
                            criteria6.add(Restrictions.eq("insurer", Boolean.TRUE));
                            criteria6.add(Restrictions.eq("visibility", 3));
                            criteria6.createCriteria("claim").add(Restrictions.eq("chorganisation", user.getChorganisation())).add(Restrictions.isNull("supplierClaimOwner"));
//                        criteria6.createCriteria("claim").add(Restrictions.isNull("supplierClaimOwner"));
                            List<Task> results6 = findByCriteria(criteria6);
                            LOG.debug("Found {} Insurer external tasks on claims nobody owns", results6.size());
                            results.addAll(results6);
                        }
                    }

                } else { // user is an Insurer user
                    LOG.debug("User is an Insurer");
                    // First, get list of users roles
//                List<WebUserRole> webUserRole = getUserRoles(user);
                    List<WebUserUserRole> webUserUserRoles = webUserUserRoleService.getMappedUserRole(user.getId());

                    for (WebUserUserRole webUserUserRole : webUserUserRoles) {
                        LOG.debug("Getting tasks for users role {}", webUserUserRole.getWebUserRole().getName());

                        // Add all Insurer internal tasks with no claim assigned to this role that user is in
                        DetachedCriteria criteria2 = DetachedCriteria.forClass(Task.class);
                        if (incompleteOnly) {
                            criteria2.add(Restrictions.eq("complete", Boolean.FALSE));
                        }
                        criteria2.add(Restrictions.eq("insurer", Boolean.TRUE));
                        criteria2.add(Restrictions.eq("visibility", 2));
                        criteria2.add(Restrictions.eq("visibilityRole", webUserUserRole.getWebUserRole().getName()));
                        criteria2.add(Restrictions.isNull("claim"));
                        criteria2.createCriteria("createdBy").add(Restrictions.eq("insurer", user.getInsurer()));
                        List<Task> results2 = findByCriteria(criteria2);
                        LOG.debug("Found {} Insurer internal tasks with no claim number", results2.size());
                        results.addAll(results2);

                        if (webUserUserRole.getWebUserRole().isOwnershipRelated() && hasOwnership) {
                            // Claim Ownership is enabled for the Insurer and this is an ownership-related role
                            LOG.debug("Ownership related role with ownership enabled");
                            // Add all Insurer internal tasks assigned to this role on claims user owns
                            DetachedCriteria criteria3 = DetachedCriteria.forClass(Task.class);
                            if (incompleteOnly) {
                                criteria3.add(Restrictions.eq("complete", Boolean.FALSE));
                            }
                            criteria3.add(Restrictions.eq("insurer", Boolean.TRUE));
                            criteria3.add(Restrictions.eq("visibility", 2));
                            criteria3.add(Restrictions.eq("visibilityRole", webUserUserRole.getWebUserRole().getName()));
                            criteria3.createCriteria("claim").add(Restrictions.eq("claimOwner", user));
                            List<Task> results3 = findByCriteria(criteria3);
                            LOG.debug("Found {} Insurer (with ownership) internal tasks assigned to {} on claims user owns", results3.size(), webUserUserRole.getWebUserRole().getName());
                            results.addAll(results3);

                            // Add all Insurer internal tasks assigned to this role on claims no-one owns
                            DetachedCriteria criteria5 = DetachedCriteria.forClass(Task.class);
                            if (incompleteOnly) {
                                criteria5.add(Restrictions.eq("complete", Boolean.FALSE));
                            }
                            criteria5.add(Restrictions.eq("insurer", Boolean.TRUE));
                            criteria5.add(Restrictions.eq("visibility", 2));
                            criteria5.add(Restrictions.eq("visibilityRole", webUserUserRole.getWebUserRole().getName()));
//                            criteria3.createCriteria("claim").add(Restrictions.eq("claimOwner", user));
                            criteria5.createCriteria("claim").add(Restrictions.isNull("claimOwner")).add(Restrictions.eq("insurer", user.getInsurer()));
//                        criteria5.createCriteria("claim").add(Restrictions.eq("insurer", user.getInsurer()));
                            List<Task> results5 = findByCriteria(criteria5);
                            LOG.debug("Found {} Insurer (with ownership) internal tasks assigned to claims with no ownership for role {}", results5.size(), webUserUserRole.getWebUserRole().getName());
                            results.addAll(results5);

                            // Add all CHO external tasks assigned to role on claims user owns
                            DetachedCriteria criteria4 = DetachedCriteria.forClass(Task.class);
                            if (incompleteOnly) {
                                criteria4.add(Restrictions.eq("complete", Boolean.FALSE));
                            }
                            criteria4.add(Restrictions.eq("insurer", Boolean.FALSE));
                            criteria4.add(Restrictions.eq("visibility", 3));
                            criteria4.add(Restrictions.eq("visibilityRole", webUserUserRole.getWebUserRole().getName()));
                            criteria4.createCriteria("claim").add(Restrictions.eq("claimOwner", user));
                            List<Task> results4 = findByCriteria(criteria4);
                            LOG.debug("Found {} CHO external tasks assigned to {} on claims user owns", results4.size(), webUserUserRole.getWebUserRole().getName());
                            results.addAll(results4);
                        } else if (webUserUserRole.getWebUserRole().isWorkgroupRelated() && hasWorkgroups) {
                            // Workgroups are enabled for the Insurer and the role is workgroup related
                            LOG.debug("Workgroup related role with workgroups enabled");
                            // Add all Insurer internal tasks assigned to role on claims assigned to a workgroup that user is in
                            // Lets first get the users workgroups
                            List<Integer> userWorkgroups = getUserWorkgroupIds(user);
                            LOG.debug("User in role {} belongs to {} workgroups", webUserUserRole.getWebUserRole().getName(), userWorkgroups.size());
                            if (userWorkgroups.size() > 0) {
                                DetachedCriteria criteria3 = DetachedCriteria.forClass(Task.class);
                                if (incompleteOnly) {
                                    criteria3.add(Restrictions.eq("complete", Boolean.FALSE));
                                }
                                criteria3.add(Restrictions.eq("insurer", Boolean.TRUE));
                                criteria3.add(Restrictions.eq("visibility", 2));
                                criteria3.add(Restrictions.eq("visibilityRole", webUserUserRole.getWebUserRole().getName()));
//                            criteria3.createCriteria("claim").add(Restrictions.in("workgroup", userWorkgroups));
                                criteria3.createCriteria("claim").createCriteria("workgroup").add(Restrictions.in("id", userWorkgroups));

                                List<Task> results3 = findByCriteria(criteria3);
                                LOG.debug("Found {} Insurer (with workgroups) internal tasks assigned to {} on claims assigned to workgroup of user", results3.size(), webUserUserRole.getWebUserRole().getName());
                                results.addAll(results3);
                            } else {
                                LOG.debug("User not assigned to any workgroups - skipping internal tasks on claims assigned to workgroup");
                            }

                            // Add all Insurer internal tasks assigned to role on claims with no workgroup yet assigned
                            DetachedCriteria criteria5 = DetachedCriteria.forClass(Task.class);
                            if (incompleteOnly) {
                                criteria5.add(Restrictions.eq("complete", Boolean.FALSE));
                            }
                            criteria5.add(Restrictions.eq("insurer", Boolean.TRUE));
                            criteria5.add(Restrictions.eq("visibility", 2));
                            criteria5.add(Restrictions.eq("visibilityRole", webUserUserRole.getWebUserRole().getName()));
//                        criteria5.createCriteria("claim").add(Restrictions.in("workgroup", userWorkgroups));
                            criteria5.createCriteria("claim").add(Restrictions.isNull("workgroup")).add(Restrictions.eq("insurer", user.getInsurer()));
//                        criteria5.createCriteria("claim").add(Restrictions.eq("insurer", user.getInsurer()));

                            List<Task> results5 = findByCriteria(criteria5);
                            LOG.debug("Found {} Insurer (with workgroups) internal tasks assigned to {} on claims with no workgroup yet assigned", results5.size(), webUserUserRole.getWebUserRole().getName());
                            results.addAll(results5);

                            // Add all CHO external tasks assigned to this role on claims assigned to a workgroup that the user is in
                            if (userWorkgroups.size() > 0) {
                                DetachedCriteria criteria4 = DetachedCriteria.forClass(Task.class);
                                if (incompleteOnly) {
                                    criteria4.add(Restrictions.eq("complete", Boolean.FALSE));
                                }
                                criteria4.add(Restrictions.eq("insurer", Boolean.FALSE));
                                criteria4.add(Restrictions.eq("visibility", 3));
                                criteria4.add(Restrictions.eq("visibilityRole", webUserUserRole.getWebUserRole().getName()));
//                            criteria4.createCriteria("claim").add(Restrictions.in("workgroup", userWorkgroups));
                                criteria4.createCriteria("claim").createCriteria("workgroup").add(Restrictions.in("id", userWorkgroups));

                                List<Task> results4 = findByCriteria(criteria4);
                                LOG.debug("Found {} CHO external (to Insurer with workgroups)  tasks assigned to {} on claims assigned to workgroup of user", results4.size(), webUserUserRole.getWebUserRole().getName());
                                results.addAll(results4);
                            }
                        } else { // no ownership and no workgroups
                            // Add all Insurer internal tasks assigned to a claim and this role
                            LOG.debug("No ownership or workgroups for role");
                            DetachedCriteria criteria3 = DetachedCriteria.forClass(Task.class);
                            if (incompleteOnly) {
                                criteria3.add(Restrictions.eq("complete", Boolean.FALSE));
                            }
                            criteria3.add(Restrictions.eq("insurer", Boolean.TRUE));
                            criteria3.add(Restrictions.eq("visibility", 2));
                            criteria3.add(Restrictions.eq("visibilityRole", webUserUserRole.getWebUserRole().getName()));
//                        criteria3.createCriteria("claim").add(Restrictions.eq("claimOwner", user));
//                        criteria3.add(Restrictions.isNotNull("claim"));
                            criteria3.createCriteria("claim").add(Restrictions.eq("insurer", user.getInsurer()));
                            List<Task> results3 = findByCriteria(criteria3);
                            LOG.debug("Found {} Insurer (no ownership, no workgroups) internal tasks assigned to {} and a claim", results3.size(), webUserUserRole.getWebUserRole().getName());
                            results.addAll(results3);

                            // Add all CHO external tasks assigned to role
                            DetachedCriteria criteria4 = DetachedCriteria.forClass(Task.class);
                            if (incompleteOnly) {
                                criteria4.add(Restrictions.eq("complete", Boolean.FALSE));
                            }
                            criteria4.add(Restrictions.eq("insurer", Boolean.FALSE));
                            criteria4.add(Restrictions.eq("visibility", 3));
                            criteria4.add(Restrictions.eq("visibilityRole", webUserUserRole.getWebUserRole().getName()));
//                            criteria4.createCriteria("claim").add(Restrictions.eq("claimOwner", user));
                            criteria4.createCriteria("claim").add(Restrictions.eq("insurer", user.getInsurer()));
                            List<Task> results4 = findByCriteria(criteria4);
                            LOG.debug("Found {} CHO external tasks assigned to {} (with Insurer having no claim ownership & no workgroups", results4.size(), webUserUserRole.getWebUserRole().getName());
                            results.addAll(results4);
                        }
                    }
                }

            } else { // No user specified - get all tasks
                LOG.debug("Getting all tasks (no user specified)");
                results = findByCriteria(criteria);
                LOG.debug("Found {} tasks", results.size());
            }
        } catch (Exception ex) {
            LOG.error("Exception caught retrieving visible tasks: {}", ex.getMessage());
        }
        LOG.debug("Found {} tasks (including duplicates) - removing any duplicates", results.size());
//        LOG.debug("Found {} tasks.", results.size());
        return removeDuplicateTasks(results);
    }

    /****************
    private List<Task> restrictTasksToRoles(List<Task> tasks, List<WebUserUserRole> roles) {
    List<Task> restrictedList = new ArrayList<Task>();
    for (Task task : tasks) {
    LOG.debug("Checking task role: {}", task.getVisibilityRole());
    for (WebUserUserRole userRoles : roles) {
    WebUserRole role = userRoles.getWebUserRole();
    LOG.debug("User has role: {}", role.getName());
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
     ******************/
    private List<Integer> getUserWorkgroupIds(WebUser user) {
        // return a list of roles allocated to the user
        List<Integer> userWorkgroupIds = new ArrayList<Integer>();
        DetachedCriteria workgroupCriteria = DetachedCriteria.forClass(WebUserWorkgroup.class);
        workgroupCriteria.createCriteria("user").add(Restrictions.eq("id", user.getId()));
        List<WebUserWorkgroup> webUserWorkgroups = findByCriteria(workgroupCriteria);
        LOG.debug("Found {} user workgroups", webUserWorkgroups.size());
        for (WebUserWorkgroup webUserWorkgroup : webUserWorkgroups) {
            userWorkgroupIds.add(webUserWorkgroup.getWorkgroup().getId());
            LOG.debug("User in workgroup with id={}", webUserWorkgroup.getWorkgroup().getId());
        }

        LOG.debug("User belongs to {} workgroups", userWorkgroupIds.size());
        return userWorkgroupIds;
    }

    private List<Task> getTasks(int webUserId, boolean incompleteOnly, boolean hasOwnership, boolean hasWorkgroups) {
        WebUser webUser = null;
        if (webUserId > 0) {
            webUser = (WebUser) get(WebUser.class, webUserId);
            if (webUser == null) {
                LOG.error("No such user found with id={}", webUserId);
                throw new IllegalArgumentException("No such user.");
            }
        }
        return getTasks(webUser, incompleteOnly, hasOwnership, hasWorkgroups);
    }

    private List<Task> getTasksByClaim(int webUserId, int claimId, boolean incompleteOnly) {
        WebUser webUser = null;
        if (webUserId > 0) {
            webUser = (WebUser) get(WebUser.class, webUserId);
            if (webUser == null) {
                LOG.error("No such user found with id={}", webUserId);
                throw new IllegalArgumentException("No such user.");
            }
        }
        return getTasksByClaim(webUser, claimId, incompleteOnly);
    }

    private List<Task> getTasksByClaim(WebUser user, int claimId, boolean incompleteOnly) {
        boolean isCHO = false;
        List<Task> results = null;

        if (user != null && user.getChorganisation() != null) {
            isCHO = true;
        }

        // Add visibility restrictions
        if (user != null) {
            DetachedCriteria criteria = DetachedCriteria.forClass(Task.class);
            criteria.createCriteria("claim").add(Restrictions.eq("id", claimId));
            if (incompleteOnly) {
                criteria.add(Restrictions.eq("complete", false));
            }
            // Add private tasks
            criteria.add(Restrictions.eq("visibility", 1));
            criteria.add(Restrictions.eq("createdBy", user));
            results = findByCriteria(criteria);
            LOG.debug("Found {} private tasks", results.size());

            if (isCHO) { // user is a CHO user
                LOG.debug("User is a CHO");
                // Add CHO created tasks on claim
                DetachedCriteria criteria2 = DetachedCriteria.forClass(Task.class);
                if (incompleteOnly) {
                    criteria2.add(Restrictions.eq("complete", Boolean.FALSE));
                }
                criteria2.add(Restrictions.eq("insurer", Boolean.FALSE));
                criteria2.add(Restrictions.ne("visibility", 1));
                criteria2.createCriteria("claim").add(Restrictions.eq("id", claimId));
                List<Task> results2 = findByCriteria(criteria2);
                LOG.debug("Found {} CHO created tasks ", results2.size());
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
            } else { // user is an Insurer user
                LOG.debug("User is an Insurer");

                // Add all Insurer created tasks on claim
                DetachedCriteria criteria2 = DetachedCriteria.forClass(Task.class);
                if (incompleteOnly) {
                    criteria2.add(Restrictions.eq("complete", Boolean.FALSE));
                }
                criteria2.add(Restrictions.eq("insurer", Boolean.TRUE));
                criteria2.add(Restrictions.ne("visibility", 1));
//                criteria2.add(Restrictions.eq("visibility", 2));
                criteria2.createCriteria("claim").add(Restrictions.eq("id", claimId));

                List<Task> results2 = findByCriteria(criteria2);
                LOG.debug("Found {} Insurer created tasks on claim", results2.size());
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
        } else { // No user specified - get all tasks
            DetachedCriteria criteria = DetachedCriteria.forClass(Task.class);
            criteria.createCriteria("claim").add(Restrictions.eq("id", claimId));
            if (incompleteOnly) {
                criteria.add(Restrictions.eq("complete", false));
            }
            results = findByCriteria(criteria);
            LOG.debug("Found {} tasks", results.size());
        }

        // Remove duplicate/linked tasks and return
//        return removeDuplicateTasks(results);
        return results;
    }

    private void markTaskAsComplete(Task task) {
        task.setComplete(Boolean.TRUE);
        task.setCompletedDate(new Date());
        task.setCompletedBy(getCurrentUser());
        save(task);
    }

    private void markTaskAsAutoComplete(Task task) {
        task.setComplete(Boolean.TRUE);
        task.setCompletedDate(new Date());
        task.setCompletedBy(getCurrentUser());
        task.setAutoCompleted(Boolean.TRUE);
        save(task);
    }

    private void markTaskAsOpen(Task task) {
        task.setComplete(Boolean.FALSE);
        task.setCompletedDate(null);
        task.setCompletedBy(null);
        task.setAutoCompleted(Boolean.FALSE);
        save(task);
    }

    private List<Task> removeDuplicateTasks(List<Task> tasks) {
        List<Task> results = new ArrayList<Task>();

        for (Task task : tasks) {
            if (!results.contains(task) && (task.getRelatedTask() == null || !isTaskInList(task.getRelatedTask().getId(), results))) {
                results.add(task);
            }
        }

        return results;
    }

    private boolean isTaskInList(int taskId, List<Task> tasks) {
        for (Task task : tasks) {
            if (task.getId() == taskId) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void autoCompleteTasksForClaim(int claimId) {
        List<Task> tasks = getIncompleteTasksByClaim(claimId);
        for (Task task : tasks) {
            markTaskAsAutoComplete(task);
        }
    }

    @Override
    public void autoUndoCompleteTasksForClaim(int claimId) {
        List<Task> tasks = getAutoCompletedTasksByClaim(claimId);
        for (Task task : tasks) {
            markTaskAsOpen(task);
        }
    }

    private List<Task> getAutoCompletedTasksByClaim(int claimId) {
        List<Task> results = new ArrayList<Task>();
        DetachedCriteria criteria = DetachedCriteria.forClass(Task.class);
        criteria.createCriteria("claim").add(Restrictions.eq("id", claimId));
        criteria.add(Restrictions.eq("autoCompleted", true));
        results = findByCriteria(criteria);
        LOG.debug("Found {} auto-completed tasks", results.size());

        return results;
    }
    
    @Override
    public void deleteAllTasksByClaimId(int claimId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Task.class);
        criteria.createCriteria("claim").add(Restrictions.eq("id", claimId));
        List<Entity> entries = findByCriteria(criteria);
        if (entries.size() > 0) {
            this.deleteAll(entries);
        }
    }
}
