package idas.chox.data.services;

import java.util.*;

import org.hibernate.Criteria;
import org.hibernate.criterion.*;
import org.hibernate.transform.Transformers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.*;
import idas.chox.core.search.SearchResult;
import idas.chox.core.services.TaskService;
import idas.chox.core.services.WebUserUserRoleService;
import idas.chox.core.util.DateHelper;
import idas.chox.data.events.ChoxEvent;

/**
 *
 * @author John
 */
public class TaskServiceImpl extends SecureDataService implements TaskService {

    private static final Logger LOG = LoggerFactory.getLogger(TaskServiceImpl.class);
    private WebUserUserRoleService webUserUserRoleService;
    private EventService eventService;

    public void setWebUserUserRoleService(WebUserUserRoleService webUserUserRoleService) {
        this.webUserUserRoleService = webUserUserRoleService;
    }

    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public SearchResult getAllTasks(int start, int limit, String sort, String dir) {
        return getTasks(null, null, false, false, false, start, limit, sort, dir, true);
    }

    @Override
    public SearchResult getAllVisibleTasks(int webUserId, Set<Integer> supplierClaimOwnerIds, boolean hasOwnership, boolean hasWorkgroups, int start, int limit, String sort, String dir, boolean showAssignedTasksOnly) {
        return getTasks(webUserId, supplierClaimOwnerIds, false, hasOwnership, hasWorkgroups, start, limit, sort, dir, showAssignedTasksOnly);
    }

    @Override
    public int getAllVisibleTaskCount(int webUserId, boolean hasOwnership, boolean hasWorkgroups, boolean showAssignedTasksOnly) {
        WebUser webUser = null;
        if (webUserId > 0) {
            webUser = (WebUser) get(WebUser.class, webUserId);
            if (webUser == null) {
                LOG.warn("No such user found with id={}", webUserId);
                throw new IllegalArgumentException("No such user.");
            }
        }
        return getTaskCount(webUser, false, hasOwnership, hasWorkgroups, showAssignedTasksOnly);
    }

    @Override
    public SearchResult getIncompleteTasks(int start, int limit, String sort, String dir) {
        return getTasks(null, null, true, false, false, start, limit, sort, dir, true);
    }

    @Override
    public SearchResult getIncompleteVisibleTasks(int webUserId, Set<Integer> supplierClaimOwnerIds, boolean hasOwnership, boolean hasWorkgroups, int start, int limit, String sort, String dir, boolean showAssignedTasksOnly) {
        return getTasks(webUserId, supplierClaimOwnerIds, true, hasOwnership, hasWorkgroups, start, limit, sort, dir, showAssignedTasksOnly);
    }

    @Override
    public int getIncompleteVisibleTaskCount(int webUserId, boolean hasOwnership, boolean hasWorkgroups, boolean showAssignedTasksOnly) {
        WebUser webUser = null;
        if (webUserId > 0) {
            webUser = (WebUser) get(WebUser.class, webUserId);
            if (webUser == null) {
                LOG.warn("No such user found with id={}", webUserId);
                throw new IllegalArgumentException("No such user.");
            }
        }
        return getTaskCount(webUser, true, hasOwnership, hasWorkgroups, showAssignedTasksOnly);
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
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value = "transactionManager")
    public void markTaskAsComplete(int webUserId, int taskId) {
        Task task = (Task) get(Task.class, taskId);
        if (task == null) {
            LOG.warn("No such task found with id={}", taskId);
            throw new IllegalArgumentException("No such task.");
        } else if (task.getComplete()) {
            LOG.info("Task already complete: id={}", taskId);
            throw new IllegalArgumentException("Task has already been completed.");
        }
        WebUser webUser = null;
        if (webUserId > 0) {
            webUser = (WebUser) get(WebUser.class, webUserId);
            if (webUser == null) {
                LOG.warn("No such user found with id={}", webUserId);
                throw new IllegalArgumentException("No such user.");
            }
        } else {
            throw new IllegalArgumentException("No such user.");
        }

        // Check that if the task is on a claim, then the webuser belongs to the same org as that of the claim
        if (task.getClaim() != null) {
            if ((webUser.isCHO() && webUser.getChorganisation().getId().intValue() != task.getClaim().getChorganisation().getId())
                    || (webUser.isAnInsurer() && webUser.getInsurer().getId().intValue() != task.getClaim().getInsurer().getId())) {
                LOG.error("User trying to complete a task from a different organisation: webUserId={}, taskId={}", webUserId, taskId);
                throw new IllegalArgumentException("You are not authorised to mark this task as complete.");
            }
        }

        // Check that user can mark task as complete:
        //       true if user created task
        //       true if no visibility role defined
        //       true if role defined and user is in role
        //       true if user is a manager
        //       false otherwise
        boolean canComplete = false;

        if (task.getCreatedBy().getId().intValue() == webUser.getId()) {
            canComplete = true;
        } else if ((task.getVisibilityRole() == null || task.getVisibilityRole().length() == 0) && task.getVisibility() != 1) {
            canComplete = true;
        } else if (userInRole(webUser, task.getVisibilityRole())) {
            canComplete = true;
        } else if (task.getVisibilityRole2() != null && userInRole(webUser, task.getVisibilityRole2())) {
            canComplete = true;
        } else if (userInRole(webUser, WebUserRole.ROLE_CHO_MNG) || userInRole(webUser, WebUserRole.ROLE_INS_MNG)) {
            canComplete = true;
        }

        if (!canComplete) {
            LOG.warn("User {} has no access to complete task {}", webUser.getFirstName() + ' ' + webUser.getLastName(), taskId);
            throw new IllegalArgumentException("You are not authorised to mark this task as complete.");
        }
        markTaskAsComplete(task);
        if (task.getClaim() != null) {
            eventService.generate(task.getClaim(), ChoxEvent.TASK_COMPLETED_EVENT, task);
        }
    }

    private boolean userInRole(WebUser user, String roleName) {
        List<WebUserUserRole> webUserUserRoles = webUserUserRoleService.getMappedUserRole(user.getId());
        return webUserUserRoles.stream().anyMatch((webUserUserRole) -> (webUserUserRole.getWebUserRole().getName().equals(roleName)));
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value = "transactionManager")
    public void createNewTask(Task task) {
        task.setComplete(Boolean.FALSE);
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

        if (task.getRaisedBy() == null) {
            task.setRaisedBy(this.getSecurityInfoProvider().getCurrentUser());
        }

        if (!task.getInsurer() && task.getVisibility() == 3) {
            // Need to set visibility role depending upon the claim status for CHO external tasks
            if (task.getClaim() == null) {
                throw new IllegalArgumentException("Claim number must be present for external tasks");
            }
            String claimStatus = task.getClaim().getStatus();
            switch (claimStatus) {
                case ClaimStatus.AWAITING_INVOICE_PAYMENT: {
                    task.setVisibilityRole(WebUserRole.ROLE_INS_PC);
                    // Also need visibility of task by CH
                    task.setVisibilityRole2(WebUserRole.ROLE_INS_CH);
                    break;
                }
                case ClaimStatus.AWAITING_LIABILITY_RESOLUTION:
                    task.setVisibilityRole(WebUserRole.ROLE_INS_CH);
                    break;
                case ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO:
                    task.setVisibilityRole(WebUserRole.ROLE_INS_CH);
                    break;
                case ClaimStatus.CLAIM_AWAITING_INVOICE_DATA:
                    task.setVisibilityRole(WebUserRole.ROLE_INS_CH);
                    break;
                case ClaimStatus.CLAIM_CLOSED:
                    task.setVisibilityRole(WebUserRole.ROLE_INS_CH);
                    break;
                case ClaimStatus.CLAIM_PENDING:
                    task.setVisibilityRole(WebUserRole.ROLE_INS_CH);
                    break;
                case ClaimStatus.CLAIM_REF_TO_ENG:
                    task.setVisibilityRole(WebUserRole.ROLE_INS_SCR);
                    break;
                case ClaimStatus.CLAIM_REJECTED:
                    task.setVisibilityRole(WebUserRole.ROLE_INS_CH);
                    break;
                case ClaimStatus.SUBSCRIBER_CLAIM_REJECTED:
                    task.setVisibilityRole(WebUserRole.ROLE_INS_CH);
                    break;
                case ClaimStatus.CLAIM_REJECTION_ACCEPTED:
                    task.setVisibilityRole(WebUserRole.ROLE_INS_CH);
                    break;
                case ClaimStatus.CLAIM_REJECTION_CONTESTED:
                    task.setVisibilityRole(WebUserRole.ROLE_INS_CH);
                    break;
                case ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED:
                    task.setVisibilityRole(WebUserRole.ROLE_INS_CH);
                    break;
                case ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED:
                    task.setVisibilityRole(WebUserRole.ROLE_INS_CH);
                    break;
                case ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED:
                case ClaimStatus.INVOICE_UNASSIGNED: {
                    task.setVisibilityRole(WebUserRole.ROLE_INS_CR);
                    //Also create a new task visible by CH
                    // Also need visibility of task by CH
                    task.setVisibilityRole2(WebUserRole.ROLE_INS_CH);
                    break;
                }
                case ClaimStatus.CLAIM_UPDATE_BY_ENG:
                    task.setVisibilityRole(WebUserRole.ROLE_INS_CH);
                    break;
                case ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO:
                    task.setVisibilityRole(WebUserRole.ROLE_INS_CH);
                    break;
                case ClaimStatus.CONTESTED_INVOICE_REF_TO_INS:
                    task.setVisibilityRole(WebUserRole.ROLE_INS_CH);
                    break;
                case ClaimStatus.INVOICE_APPROVED_BY_BRE:
                    task.setVisibilityRole(WebUserRole.ROLE_INS_CH);
                    break;
                case ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT:
                    task.setVisibilityRole(WebUserRole.ROLE_INS_CH);
                    break;
                case ClaimStatus.INVOICE_ESCALATED:
                    task.setVisibilityRole(WebUserRole.ROLE_INS_SCR);
                    break;
                case ClaimStatus.INVOICE_ESCALATED_TO_CH:
                    task.setVisibilityRole(WebUserRole.ROLE_INS_CH);
                    break;
                case ClaimStatus.INVOICE_PAYMENT_LOGGED:
                    task.setVisibilityRole(WebUserRole.ROLE_INS_CH);
                    break;
                case ClaimStatus.INVOICE_PAYMENT_RECEIVED:
                    task.setVisibilityRole(WebUserRole.ROLE_INS_CH);
                    break;
                case ClaimStatus.INVOICE_REF_TO_CH:
                    task.setVisibilityRole(WebUserRole.ROLE_INS_CH);
                    break;
                case ClaimStatus.INVOICE_REF_TO_ENG:
                    task.setVisibilityRole(WebUserRole.ROLE_INS_SCR);
                    break;
                case ClaimStatus.INVOICE_REJECTED_ACCEPTED:
                    task.setVisibilityRole(WebUserRole.ROLE_INS_CH);
                    break;
                case ClaimStatus.CLAIM_REFERRED_TO_FNOL:
                    task.setVisibilityRole(WebUserRole.ROLE_INS_FNOL);
                    break;
                case ClaimStatus.AWAITING_LITIGATION_OUTCOME:
                    task.setVisibilityRole(WebUserRole.ROLE_INS_CH);
                    break;
                case ClaimStatus.MANUAL_INVOICE_APPROVED:
                    task.setVisibilityRole(WebUserRole.ROLE_INS_CH);
                    break;
                case ClaimStatus.MANUAL_INVOICE_CONTESTED:
                    task.setVisibilityRole(WebUserRole.ROLE_INS_CH);
                    break;
                case ClaimStatus.MANUAL_INVOICE_REJECTED:
                    task.setVisibilityRole(WebUserRole.ROLE_INS_CH);
                    break;
                case ClaimStatus.MANUAL_INVOICE_UNASSIGNED:
                    task.setVisibilityRole(WebUserRole.ROLE_INS_CH);
                    break;
                default:
                    LOG.error("Cannot raise a task on a claim in status '{}'", claimStatus);
                    throw new IllegalArgumentException("Sorry, cannot raise a task on a claim when the status is '" + claimStatus + "'");
            }
        } else if (task.getInsurer() && task.getVisibility() == 3) { // Insurer external task
            // No visibility role needed for CHOs so make sure its not defined
            task.setVisibilityRole(null);
        }
        try {
            this.save(task);
        } catch (Exception ex) {
            if (task.getClaim() != null) {
                LOG.error("Error saving task on claim with id='{}' in status {}: {}", new Object[]{task.getClaim().getId(), task.getClaim().getStatus(), ex.getMessage()});
            } else {
                LOG.error("Error saving task not on claim: {}", ex.getMessage());
            }
        }
        if (task.getClaim() != null) {
            try {
                eventService.generate(task.getClaim(), ChoxEvent.TASK_CREATED_EVENT, task);
            } catch (Exception ex) {
                LOG.error("Error generating task created event on claim with id='{}' in status {}: {}", new Object[]{task.getClaim().getId(), task.getClaim().getStatus(), ex.getMessage()});
            }
        }
    }

    private int getTaskCount(WebUser user, boolean incompleteOnly, boolean hasOwnership, boolean hasWorkgroups, boolean showAssignedTasksOnly) {
        boolean isCHO = false;
        List<Task> results = new ArrayList<>();
        String colorCode = null;

        // select insurer task depends upon the visibility role. 
        ArrayList<String> visibilityRole = new ArrayList<>();
        ArrayList<String> visibilityRole1 = new ArrayList<>();
        ArrayList<String> visibilityRole2 = new ArrayList<>();
        ArrayList<String> visibilityRole3 = new ArrayList<>();
        ArrayList<String> visibilityRole4 = new ArrayList<>();

        List<HashMap> resultMap;
        Integer totalCount = 0;

        try {

            Criteria criteria = getSearchCriteria(user, null, incompleteOnly, hasOwnership, hasWorkgroups, showAssignedTasksOnly);

            totalCount = totalCount(criteria);
        } catch (Exception ex) {
            LOG.error("Exception caught retrieving  task count: ", ex);
        }

        LOG.debug("Found {} tasks in count.", totalCount);
        return totalCount;
    }

    private Criteria getSearchCriteria(WebUser user, Set<Integer> supplierClaimOwnerIds, boolean incompleteOnly, boolean hasOwnership, boolean hasWorkgroups, boolean showAssignedTasksOnly) {
        boolean isCHO = false;

        // select insurer task depends upon the visibility role. 
        ArrayList<String> visibilityRole1 = new ArrayList<>();
        ArrayList<String> visibilityRole2 = new ArrayList<>();
        ArrayList<String> visibilityRole3 = new ArrayList<>();
        ArrayList<String> visibilityRole4 = new ArrayList<>();

        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Task.class)
                .createAlias("this.claim", "c", org.hibernate.sql.JoinType.INNER_JOIN);

        if (incompleteOnly) {
            LOG.debug("Restricting to incomplete tasks");
            criteria.add(Restrictions.eq("complete", Boolean.FALSE));
        }

        // Add visibility restrictions
        if (user != null) {
            LOG.debug("Getting tasks for user with id={} ('{}')", user.getId(), user.getFullName());

            // Restrict to private tasks that user owns
            Criterion privateTasks = Restrictions.conjunction()
                    .add(Restrictions.eq("this.visibility", 1))
                    .add(Restrictions.eq("this.createdBy", user));

            if (user.getChorganisation() != null) {
                isCHO = true;
            }
            if (isCHO) {
                // declare default Criterion restriction to avoid null value when below if condition not passed.
                Criterion internalTasksOnClaimsUserOwns = Restrictions.eq("id", -1);
                Criterion externalTasksOnClaimsUserOwns = Restrictions.eq("id", -1);
                Criterion internalTasksOnClaimsBelongsToUserOrg = Restrictions.eq("id", -1);
                Criterion externalTasksOnClaimsBelongsToUserOrg = Restrictions.eq("id", -1);
                Criterion internalTasksOnClaimsNobodyOwns = Restrictions.eq("id", -1);
                Criterion externalTasksOnClaimsNobodyOwns = Restrictions.eq("id", -1);

                if (showAssignedTasksOnly) {

                    // Add CHO internal tasks on claims user owns
                    internalTasksOnClaimsUserOwns = Restrictions.conjunction()
                            .add(Restrictions.eq("this.insurer", Boolean.FALSE))
                            .add(Restrictions.eq("this.visibility", 2))
                            .add(Restrictions.eq("c.supplierClaimOwner", user));

                    // Add Insurer external tasks on claims user own
                    externalTasksOnClaimsUserOwns = Restrictions.conjunction()
                            .add(Restrictions.eq("this.insurer", Boolean.TRUE))
                            .add(Restrictions.eq("this.visibility", 3))
                            .add(Restrictions.eq("c.supplierClaimOwner", user));

                    // Add tasks on claims nobody owns to users with an ownership based role only
                    List<WebUserUserRole> webUserUserRoles = webUserUserRoleService.getMappedUserRole(user.getId());
                    for (WebUserUserRole webUserUserRole : webUserUserRoles) {
                        if (webUserUserRole.getWebUserRole().getName().equals(WebUserRole.ROLE_CHO)) {
                            continue;
                        }
                        if (webUserUserRole.getWebUserRole().isOwnershipRelated()) {

                            LOG.debug("Getting tasks for users role {}", webUserUserRole.getWebUserRole().getName());

                            // Add CHO internal tasks on claims nobody owns (but of this CHO)
                            internalTasksOnClaimsNobodyOwns = Restrictions.conjunction()
                                    .add(Restrictions.eq("insurer", Boolean.FALSE))
                                    .add(Restrictions.eq("visibility", 2))
                                    .add(Restrictions.isNull("c.supplierClaimOwner"))
                                    .add(Restrictions.eq("c.chorganisation", user.getChorganisation()));

                            // Add Insurer external tasks on claims nobody owns
                            externalTasksOnClaimsNobodyOwns = Restrictions.conjunction()
                                    .add(Restrictions.eq("this.insurer", Boolean.TRUE))
                                    .add(Restrictions.eq("this.visibility", 3))
                                    .add(Restrictions.isNull("c.supplierClaimOwner"))
                                    .add(Restrictions.eq("c.chorganisation", user.getChorganisation()));
                            break;
                        }
                    }

                } else {
                    if (supplierClaimOwnerIds != null && supplierClaimOwnerIds.size() > 0) {
                        // Add CHO internal tasks on claims belongs to user organisation.
                        internalTasksOnClaimsBelongsToUserOrg = Restrictions.conjunction()
                                .add(Restrictions.eq("this.insurer", Boolean.FALSE))
                                .add(Restrictions.eq("this.visibility", 2))
                                .add(Restrictions.eq("c.chorganisation", user.getChorganisation()))
                                .add(Restrictions.in("c.supplierClaimOwner.id", supplierClaimOwnerIds.toArray()));

                        // Add Insurer external tasks on claims belongs to user organisation.
                        externalTasksOnClaimsBelongsToUserOrg = Restrictions.conjunction()
                                .add(Restrictions.eq("this.insurer", Boolean.TRUE))
                                .add(Restrictions.eq("this.visibility", 3))
                                .add(Restrictions.eq("c.chorganisation", user.getChorganisation()))
                                .add(Restrictions.in("c.supplierClaimOwner.id", supplierClaimOwnerIds.toArray()));
                    } else { // show tasks belongs to user organisation.
                        // Add CHO internal tasks on claims belongs to user organisation.
                        internalTasksOnClaimsBelongsToUserOrg = Restrictions.conjunction()
                                .add(Restrictions.eq("this.insurer", Boolean.FALSE))
                                .add(Restrictions.eq("this.visibility", 2))
                                .add(Restrictions.eq("c.chorganisation", user.getChorganisation()));

                        // Add Insurer external tasks on claims belongs to user organisation.
                        externalTasksOnClaimsBelongsToUserOrg = Restrictions.conjunction()
                                .add(Restrictions.eq("this.insurer", Boolean.TRUE))
                                .add(Restrictions.eq("this.visibility", 3))
                                .add(Restrictions.eq("c.chorganisation", user.getChorganisation()));
                    }
                }

                // add all Criterion together to make final query for CHO tasks.
                criteria.add(Restrictions.disjunction()
                        .add(privateTasks)
                        .add(internalTasksOnClaimsUserOwns)
                        .add(externalTasksOnClaimsUserOwns)
                        .add(internalTasksOnClaimsBelongsToUserOrg)
                        .add(externalTasksOnClaimsBelongsToUserOrg)
                        .add(internalTasksOnClaimsNobodyOwns)
                        .add(externalTasksOnClaimsNobodyOwns));

            } else { // user is an Insurer user

                List<WebUserUserRole> webUserUserRoles = webUserUserRoleService.getMappedUserRole(user.getId());
                List<Integer> userWorkgroups = new ArrayList<>();
                boolean ownershipEnabled = false;
                boolean workgroupEnabled = false;
                boolean ownershipAndWorkgroupDisabled = false;

                // declare default Criterion restriction to avoid null value when below if condition not passed. 
                Criterion internalTasksAssignedToThisRoleOnClaimsUserOwns = Restrictions.eq("id", -1);
                Criterion internalTasksAssignedToThisRoleOnClaimsNobodyOwns = Restrictions.eq("id", -1);
                Criterion externalTasksAssignedToThisRoleOnClaimsUserOwns = Restrictions.eq("id", -1);
                Criterion internalTaskesAssignedToThisRoleAndWorkgroup = Restrictions.eq("id", -1);
                Criterion internalTaskesAssignedToThisRoleAndNoWorkgroup = Restrictions.eq("id", -1);
                Criterion extTskAssignedToThisRoleOnClaimsAssignedToWG = Restrictions.eq("id", -1);
                Criterion intTskAssignedToClaimAndThisRole = Restrictions.eq("id", -1);
                Criterion extTskAssingedToRole = Restrictions.eq("id", -1);
                Criterion intTskAssingedToClaimBelongsToUserInsurer = Restrictions.eq("id", -1);
                Criterion extTskAssingedToClaimBelongsToUserInsurer = Restrictions.eq("id", -1);

                if (showAssignedTasksOnly) {
                    // get visibilityRoles depends upon the workgroup and ownership.
                    for (WebUserUserRole webUserUserRole : webUserUserRoles) {
                        if (webUserUserRole.getWebUserRole().getName().equals(WebUserRole.ROLE_INS)) {
                            continue;
                        }

                        if (webUserUserRole.getWebUserRole().isOwnershipRelated() && hasOwnership) {
                            // Claim Ownership is enabled for the Insurer and this is an ownership-related role
                            LOG.debug("Ownership related role with ownership enabled");
                            ownershipEnabled = true;
                            visibilityRole1.add(webUserUserRole.getWebUserRole().getName());

                        } else if (webUserUserRole.getWebUserRole().isWorkgroupRelated() && hasWorkgroups) {
                            // Workgroups are enabled for the Insurer and the role is workgroup related
                            LOG.debug("Workgroup related role with workgroups enabled");
                            workgroupEnabled = true;
                            visibilityRole2.add(webUserUserRole.getWebUserRole().getName());

                            if (userWorkgroups.size() <= 0) {
                                userWorkgroups = getUserWorkgroupIds(user);
                            }
                            if (userWorkgroups.size() > 0) {
                                visibilityRole3.add(webUserUserRole.getWebUserRole().getName());
                            }

                        } else { // no ownership and no workgroups
                            LOG.debug("No ownership or workgroups for role");
                            ownershipAndWorkgroupDisabled = true;
                            visibilityRole4.add(webUserUserRole.getWebUserRole().getName());
                        }
                    }

                    // declare Criterian depends upon the visibilityRole
                    if (webUserUserRoles.size() > 0) {

                        if (ownershipEnabled) {

                            // Add all Insurer internal tasks assigned to this role on claims user owns
                            internalTasksAssignedToThisRoleOnClaimsUserOwns = Restrictions.conjunction()
                                    .add(Restrictions.eq("this.insurer", Boolean.TRUE))
                                    .add(Restrictions.eq("this.visibility", 2))
                                    .add(Restrictions.in("this.visibilityRole", visibilityRole1))
                                    .add(Restrictions.eq("c.claimOwner", user));

                            // Add all Insurer internal tasks assigned to this role on claims no-one owns
                            internalTasksAssignedToThisRoleOnClaimsNobodyOwns = Restrictions.conjunction()
                                    .add(Restrictions.eq("this.insurer", Boolean.TRUE))
                                    .add(Restrictions.eq("this.visibility", 2))
                                    .add(Restrictions.in("this.visibilityRole", visibilityRole1))
                                    .add(Restrictions.isNull("c.claimOwner"))
                                    .add(Restrictions.eq("c.insurer", user.getInsurer()));

                            // Add all CHO external tasks assigned to role on claims user owns
                            externalTasksAssignedToThisRoleOnClaimsUserOwns = Restrictions.conjunction()
                                    .add(Restrictions.eq("this.insurer", Boolean.FALSE))
                                    .add(Restrictions.eq("this.visibility", 3))
                                    .add(Restrictions.or(Restrictions.in("this.visibilityRole", visibilityRole1), Restrictions.in("this.visibilityRole2", visibilityRole1)))
                                    .add(Restrictions.eq("c.claimOwner", user));

                        }

                        if (workgroupEnabled) {

                            // Add all Insurer internal tasks assigned to role on claims with no workgroup yet assigned
                            internalTaskesAssignedToThisRoleAndNoWorkgroup = Restrictions.conjunction()
                                    .add(Restrictions.eq("this.insurer", Boolean.TRUE))
                                    .add(Restrictions.eq("this.visibility", 2))
                                    .add(Restrictions.in("this.visibilityRole", visibilityRole2))
                                    .add(Restrictions.isNull("c.workgroup"))
                                    .add(Restrictions.eq("c.insurer", user.getInsurer()));

                            if (userWorkgroups.size() > 0) {

                                // Add all Insurer internal tasks assigned to role on claims assigned to a workgroup that user is in
                                internalTaskesAssignedToThisRoleAndWorkgroup = Restrictions.conjunction()
                                        .add(Restrictions.eq("this.insurer", Boolean.TRUE))
                                        .add(Restrictions.eq("this.visibility", 2))
                                        .add(Restrictions.in("this.visibilityRole", visibilityRole3))
                                        .add(Restrictions.in("c.workgroup.id", userWorkgroups));

                                // Add all CHO external tasks assigned to this role on claims assigned to a workgroup that the user is in
                                extTskAssignedToThisRoleOnClaimsAssignedToWG = Restrictions.conjunction()
                                        .add(Restrictions.eq("this.insurer", Boolean.FALSE))
                                        .add(Restrictions.eq("this.visibility", 3))
                                        .add(Restrictions.or(Restrictions.in("this.visibilityRole", visibilityRole3), Restrictions.in("this.visibilityRole2", visibilityRole3)))
                                        .add(Restrictions.in("c.workgroup.id", userWorkgroups));
                            }
                        }

                        if (ownershipAndWorkgroupDisabled) {
                            // Add all Insurer internal tasks assigned to a claim and this role
                            intTskAssignedToClaimAndThisRole = Restrictions.conjunction()
                                    .add(Restrictions.eq("this.insurer", Boolean.TRUE))
                                    .add(Restrictions.eq("this.visibility", 2))
                                    .add(Restrictions.in("this.visibilityRole", visibilityRole4))
                                    .add(Restrictions.eq("c.insurer", user.getInsurer()));

                            // Add all CHO external tasks assigned to role
                            extTskAssingedToRole = Restrictions.conjunction()
                                    .add(Restrictions.eq("this.insurer", Boolean.FALSE))
                                    .add(Restrictions.eq("this.visibility", 3))
                                    .add(Restrictions.or(Restrictions.in("this.visibilityRole", visibilityRole4), Restrictions.in("this.visibilityRole2", visibilityRole4)))
                                    .add(Restrictions.eq("c.insurer", user.getInsurer()));
                        }

                    }
                } else {

                    // Add all Insurer internal tasks with claim assigned to the task
                    intTskAssingedToClaimBelongsToUserInsurer = Restrictions.conjunction()
                            .add(Restrictions.eq("this.insurer", Boolean.TRUE))
                            .add(Restrictions.eq("this.visibility", 2))
                            .add(Restrictions.eq("c.insurer", user.getInsurer()));

                    // Add all Insurer external tasks
                    extTskAssingedToClaimBelongsToUserInsurer = Restrictions.conjunction()
                            .add(Restrictions.eq("this.insurer", Boolean.FALSE))
                            .add(Restrictions.eq("this.visibility", 3))
                            .add(Restrictions.eq("c.insurer", user.getInsurer()));

                }

                // add all Criterion together to make final query for insurer task.
                criteria.add(Restrictions.disjunction()
                        .add(privateTasks)
                        .add(internalTasksAssignedToThisRoleOnClaimsUserOwns)
                        .add(internalTasksAssignedToThisRoleOnClaimsNobodyOwns)
                        .add(externalTasksAssignedToThisRoleOnClaimsUserOwns)
                        .add(internalTaskesAssignedToThisRoleAndWorkgroup)
                        .add(internalTaskesAssignedToThisRoleAndNoWorkgroup)
                        .add(extTskAssignedToThisRoleOnClaimsAssignedToWG)
                        .add(intTskAssignedToClaimAndThisRole)
                        .add(intTskAssingedToClaimBelongsToUserInsurer)
                        .add(extTskAssingedToClaimBelongsToUserInsurer)
                        .add(extTskAssingedToRole));
            }
        }

        return criteria;
    }

    private SearchResult getTasks(WebUser user, Set<Integer> supplierClaimOwnerIds, boolean incompleteOnly, boolean hasOwnership, boolean hasWorkgroups, int start, int limit, String sort, String dir, boolean showAssignedTasksOnly) {
        Integer totalCount = 0;
        String colorCode = null;
        List<HashMap> resultMap;
        List<Task> results = new ArrayList<>();

        try {
            Criteria criteria = getSearchCriteria(user, supplierClaimOwnerIds, incompleteOnly, hasOwnership, hasWorkgroups, showAssignedTasksOnly);

            totalCount = totalCount(criteria);

            LOG.debug("Found {} tasks in count, now retrieving....", totalCount);

            Date minDueDate = getTaskMinDueDate(criteria);
            if (minDueDate != null) {
                int days = DateHelper.getNumberOfDaysBetween(new Date(), minDueDate);
                colorCode = (days > 0) ? "green" : (days < 0) ? "red" : "orange";
            } else {
                colorCode = "green";
            }

            if (sort != null && sort.equalsIgnoreCase("choReference")) {
                // left join on claim used here to sort the task by choReference.
//                criteria.createAlias("this.claim", "c", org.hibernate.sql.JoinType.LEFT_OUTER_JOIN);
            }

            if (sort != null && sort.equalsIgnoreCase("raisedBy")) {
                // left join on claim used here to sort the task by choReference.
                criteria.createAlias("this.raisedBy", "w", org.hibernate.sql.JoinType.LEFT_OUTER_JOIN);
            }
            if (sort != null && sort.equalsIgnoreCase("insurerOwner")) {
                // left join on claim used here to sort the task by choReference.
//                criteria.createAlias("this.claim", "c", org.hibernate.sql.JoinType.LEFT_OUTER_JOIN);
                criteria.createAlias("c.claimOwner", "w", org.hibernate.sql.JoinType.LEFT_OUTER_JOIN);
            }
            if (sort != null && sort.equalsIgnoreCase("choOwner")) {
                // left join on claim used here to sort the task by choReference.
//                criteria.createAlias("this.claim", "c", org.hibernate.sql.JoinType.LEFT_OUTER_JOIN);
                criteria.createAlias("c.supplierClaimOwner", "w", org.hibernate.sql.JoinType.LEFT_OUTER_JOIN);
            }

            criteria.setFirstResult(start);
            criteria.setMaxResults(limit);
            criteria.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            sortTasks(criteria, sort, dir);
            resultMap = criteria.list();

            resultMap.forEach((m) -> {
                results.add((Task) m.get("this"));
            });

            LOG.debug("Returning {} tasks", results.size());

        } catch (Exception ex) {
            LOG.error("Exception caught retrieving visible tasks: ", ex);
        }
        return new SearchResult(results, totalCount, colorCode);
    }

    private void sortTasks(Criteria criteria, String sort, String dir) {

        if (sort != null && dir != null && !sort.isEmpty() && !dir.isEmpty()) {
            if (sort.equalsIgnoreCase("choReference")) {
                addSort(criteria, "c.choReference", dir);
            } else if (sort.equalsIgnoreCase("dueDate")) {
                addSort(criteria, "dueDate", dir);
            } else if (sort.equalsIgnoreCase("type")) {
                addSort(criteria, "type", dir);
            } else if (sort.equalsIgnoreCase("description")) {
                addSort(criteria, "description", dir);
            } else if (sort.equalsIgnoreCase("createdDate")) {
                addSort(criteria, "createdDate", dir);
            } else if (sort.equalsIgnoreCase("raisedBy")) {
                addSort(criteria, "w.lastName", dir);
                addSort(criteria, "w.firstName", dir);
            } else if (sort.equalsIgnoreCase("insurerOwner") || sort.equalsIgnoreCase("choOwner")) {
                addSort(criteria, "w.lastName", dir);
                addSort(criteria, "w.firstName", dir);
            }
        } else {
            criteria.addOrder(Order.desc("dueDate"));
        }
    }

    private List<Integer> getUserWorkgroupIds(WebUser user) {
        // return a list of roles allocated to the user
        List<Integer> userWorkgroupIds = new ArrayList<>();
        DetachedCriteria workgroupCriteria = DetachedCriteria.forClass(WebUserWorkgroup.class);
        workgroupCriteria.createCriteria("user").add(Restrictions.eq("id", user.getId()));
        List<WebUserWorkgroup> webUserWorkgroups = findByCriteria(workgroupCriteria);
        LOG.debug("Found {} user workgroups", webUserWorkgroups.size());
        webUserWorkgroups.forEach((webUserWorkgroup) -> {
            userWorkgroupIds.add(webUserWorkgroup.getWorkgroup().getId());
        });

        LOG.debug("User belongs to {} workgroups", userWorkgroupIds.size());
        return userWorkgroupIds;
    }

    private SearchResult getTasks(int webUserId, Set<Integer> supplierClaimOwnerIds, boolean incompleteOnly, boolean hasOwnership, boolean hasWorkgroups, int start, int limit, String sort, String dir, boolean showAssignedTasksOnly) {
        WebUser webUser = null;
        if (webUserId > 0) {
            webUser = (WebUser) get(WebUser.class, webUserId);
            if (webUser == null) {
                LOG.warn("No such user found with id={}", webUserId);
                throw new IllegalArgumentException("No such user.");
            }
        }
        return getTasks(webUser, supplierClaimOwnerIds, incompleteOnly, hasOwnership, hasWorkgroups, start, limit, sort, dir, showAssignedTasksOnly);
    }

    private List<Task> getTasksByClaim(int webUserId, int claimId, boolean incompleteOnly) {
        WebUser webUser = null;
        if (webUserId > 0) {
            webUser = (WebUser) get(WebUser.class, webUserId);
            if (webUser == null) {
                LOG.warn("No such user found with id={}", webUserId);
                throw new IllegalArgumentException("No such user.");
            }
        }
        return getTasksByClaim(webUser, claimId, incompleteOnly);
    }

    private List<Task> getTasksByClaim(WebUser user, int claimId, boolean incompleteOnly) {
        boolean isCHO = false;
        List<Task> results;

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
            LOG.debug("No user, Found {} tasks", results.size());
        }

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

    private boolean isTaskInList(int taskId, List<Task> tasks) {
        return tasks.stream().anyMatch((task) -> (task.getId() == taskId));
    }

    @Override
    public void autoCompleteTasksForClaim(int claimId) {
        List<Task> tasks = getIncompleteTasksByClaim(claimId);
        tasks.forEach((task) -> {
            markTaskAsAutoComplete(task);
        });
    }

    @Override
    public void autoUndoCompleteTasksForClaim(int claimId) {
        List<Task> tasks = getAutoCompletedTasksByClaim(claimId);
        tasks.forEach((task) -> {
            markTaskAsOpen(task);
        });
    }

    private List<Task> getAutoCompletedTasksByClaim(int claimId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Task.class);
        criteria.createCriteria("claim").add(Restrictions.eq("id", claimId));
        criteria.add(Restrictions.eq("autoCompleted", true));
        List<Task> results = findByCriteria(criteria);
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
