package idas.chox.data.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.hibernate.Criteria;
import org.hibernate.criterion.*;
import org.hibernate.transform.Transformers;

import idas.chox.core.model.*;
import idas.chox.core.search.SearchResult;
import idas.chox.core.services.TaskService;
import idas.chox.core.services.WebUserUserRoleService;
import idas.chox.core.util.DateHelper;
import idas.chox.data.events.ChoxEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        return getTasks(null, false, false, false, start, limit, sort, dir, true);
    }

    @Override
    public SearchResult getAllVisibleTasks(int webUserId, boolean hasOwnership, boolean hasWorkgroups, int start, int limit, String sort, String dir, boolean  showAssignedTasksOnly) {
        return getTasks(webUserId, false, hasOwnership, hasWorkgroups, start, limit, sort, dir, showAssignedTasksOnly);
    }

    @Override
    public SearchResult getIncompleteTasks(int start, int limit, String sort, String dir) {
        return getTasks(null, true, false, false, start, limit, sort, dir, true);
    }

    @Override
    public SearchResult getIncompleteVisibleTasks(int webUserId, boolean hasOwnership, boolean hasWorkgroups, int start, int limit, String sort, String dir, boolean  showAssignedTasksOnly) {
        return getTasks(webUserId, true, hasOwnership, hasWorkgroups, start, limit, sort, dir, showAssignedTasksOnly);
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
            if ((webUser.isCHO() && webUser.getChorganisation().getId().intValue() != task.getClaim().getChorganisation().getId().intValue())
                    || (webUser.isAnInsurer() && webUser.getInsurer().getId().intValue() != task.getClaim().getInsurer().getId().intValue())) {
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

        if (task.getCreatedBy().getId().intValue() == webUser.getId().intValue()) {
            canComplete = true;
        } else if ((task.getVisibilityRole() == null || task.getVisibilityRole().length() == 0) && task.getVisibility()!= 1) {
            canComplete = true;
        } else if (userInRole(webUser, task.getVisibilityRole())) {
            canComplete = true;
        } else if (userInRole(webUser, WebUserRole.ROLE_CH_MNG) || userInRole(webUser, WebUserRole.ROLE_INS_MNG)) {
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
        if (task.getRelatedTask() != null) {
            LOG.debug("Marking related task as complete: {}", task.getRelatedTask().getId());
            markTaskAsComplete(task.getRelatedTask());
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
                taskCH.setRaisedBy(task.getRaisedBy());
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
            } else if (claimStatus.equals(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED) || claimStatus.equals(ClaimStatus.INVOICE_UNASSIGNED)) {
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
                taskCH.setRaisedBy(task.getRaisedBy());
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
            } else if (claimStatus.equals(ClaimStatus.AWAITING_LITIGATION_OUTCOME)) {
                task.setVisibilityRole(WebUserRole.ROLE_CH);
            } else if (claimStatus.equals(ClaimStatus.MANUAL_INVOICE_APPROVED)) {
                task.setVisibilityRole(WebUserRole.ROLE_CH);
            } else if (claimStatus.equals(ClaimStatus.MANUAL_INVOICE_CONTESTED)) {
                task.setVisibilityRole(WebUserRole.ROLE_CH);
            } else if (claimStatus.equals(ClaimStatus.MANUAL_INVOICE_REJECTED)) {
                task.setVisibilityRole(WebUserRole.ROLE_CH);
            }  else if (claimStatus.equals(ClaimStatus.MANUAL_INVOICE_UNASSIGNED)) {
                task.setVisibilityRole(WebUserRole.ROLE_CH);
            } else {
                LOG.error("Cannot raise a task on a claim in status '{}'", claimStatus);
                throw new IllegalArgumentException("Sorry, cannot raise a task on a claim when the status is '" + claimStatus + "'");
            }
        } else if (task.getInsurer() && task.getVisibility() == 3) { // Insurer external task
            // No visibility role needed for CHOs so make sure its not defined
            task.setVisibilityRole(null);
        }
        this.save(task);
        if (task.getClaim() != null) {
            eventService.generate(task.getClaim(), ChoxEvent.TASK_CREATED_EVENT, task);
        }
    }

    private SearchResult getTasks(WebUser user, boolean incompleteOnly, boolean hasOwnership, boolean hasWorkgroups, int start, int limit, String sort, String dir, boolean  showAssignedTasksOnly) {

        boolean isCHO = false;
        List<Task> results = new ArrayList<Task>();
        String colorCode = null;

        // select insurer task depends upon the visibility role. 
        ArrayList<String> visibilityRole = new ArrayList<String>();
        ArrayList<String> visibilityRole1 = new ArrayList<String>();
        ArrayList<String> visibilityRole2 = new ArrayList<String>();
        ArrayList<String> visibilityRole3 = new ArrayList<String>();
        ArrayList<String> visibilityRole4 = new ArrayList<String>();

        List<HashMap> resultMap;
        Integer totalCount = 0;

        try {

            Criteria criteria = getSession().createCriteria(Task.class);

            if (incompleteOnly) {
                LOG.debug("Restricting to incomplete tasks");
                criteria.add(Restrictions.eq("complete", Boolean.FALSE));
            }
            // Add visibility restrictions
            if (user != null) {
                LOG.debug("Getting tasks for user with id={} ('{}')", user.getId(), user.getFullName());

                // Restrict to private tasks that user owns
                Criterion privateTasks = Restrictions.conjunction()
                        .add(Restrictions.eq("visibility", 1))
                        .add(Restrictions.eq("createdBy", user));

                if (user.getChorganisation() != null) {
                    isCHO = true;
                }
                if (isCHO) {

                    // declare subqueries which will be used in Criterion.
                    DetachedCriteria choUsers = DetachedCriteria.forClass(WebUser.class)
                            .setProjection(Property.forName("id"))
                            .add(Restrictions.eq("chorganisation", user.getChorganisation()));

                    // Add CHO internal tasks with no claim number
                    Criterion internalTasksWithNoClaimNumber = Restrictions.conjunction()
                            .add(Restrictions.eq("insurer", Boolean.FALSE))
                            .add(Restrictions.eq("visibility", 2))
                            .add(Restrictions.isNull("claim"))
                            .add(Subqueries.propertyIn("createdBy", choUsers));
                    
                    // declare default Criterion restriction to avoid null value when below if condition not passed. 
                    Criterion internalTasksOnClaimsUserOwns = Restrictions.eq("id", -1);
                    Criterion externalTasksOnClaimsUserOwns = Restrictions.eq("id", -1);
                    Criterion internalTasksOnClaimsBelongsToUserOrg = Restrictions.eq("id", -1);
                    Criterion externalTasksOnClaimsBelongsToUserOrg = Restrictions.eq("id", -1);
                    Criterion internalTasksOnClaimsNobodyOwns = Restrictions.eq("id", -1);
                    Criterion externalTasksOnClaimsNobodyOwns = Restrictions.eq("id", -1);
                    
                    if (showAssignedTasksOnly) {
                        
                        DetachedCriteria claimsUserOwns = DetachedCriteria.forClass(Claim.class)
                            .setProjection(Property.forName("id"))
                            .add(Restrictions.eq("supplierClaimOwner", user));
                        
                        // Add CHO internal tasks on claims user owns
                        internalTasksOnClaimsUserOwns = Restrictions.conjunction()
                                .add(Restrictions.eq("insurer", Boolean.FALSE))
                                .add(Restrictions.eq("visibility", 2))
                                .add(Subqueries.propertyIn("claim", claimsUserOwns));

                        // Add Insurer external tasks on claims user own
                        externalTasksOnClaimsUserOwns = Restrictions.conjunction()
                                .add(Restrictions.eq("insurer", Boolean.TRUE))
                                .add(Restrictions.eq("visibility", 3))
                                .add(Subqueries.propertyIn("claim", claimsUserOwns));

                        // Add tasks on claims nobody owns to users with an ownership based role only
                        List<WebUserUserRole> webUserUserRoles = webUserUserRoleService.getMappedUserRole(user.getId());
                        for (WebUserUserRole webUserUserRole : webUserUserRoles) {
                            if (webUserUserRole.getWebUserRole().isOwnershipRelated()) {

                                LOG.debug("Getting tasks for users role {}", webUserUserRole.getWebUserRole().getName());

                                DetachedCriteria claimsNotAssignedToUser = DetachedCriteria.forClass(Claim.class)
                                        .setProjection(Property.forName("id"))
                                        .add(Restrictions.isNull("supplierClaimOwner"))
                                        .add(Restrictions.eq("chorganisation", user.getChorganisation()));

                                // Add CHO internal tasks on claims nobody owns (but of this CHO)
                                internalTasksOnClaimsNobodyOwns = Restrictions.conjunction()
                                        .add(Restrictions.eq("insurer", Boolean.FALSE))
                                        .add(Restrictions.eq("visibility", 2))
                                        .add(Subqueries.propertyIn("claim", claimsNotAssignedToUser));

                                // Add Insurer external tasks on claims nobody owns
                                externalTasksOnClaimsNobodyOwns = Restrictions.conjunction()
                                        .add(Restrictions.eq("insurer", Boolean.TRUE))
                                        .add(Restrictions.eq("visibility", 3))
                                        .add(Subqueries.propertyIn("claim", claimsNotAssignedToUser));
                                break;
                            }
                        }
                        
                    } else { // show tasks belongs to user organisation.
                        
                        DetachedCriteria claimsBelongsToUserCho = DetachedCriteria.forClass(Claim.class)
                            .setProjection(Property.forName("id"))
                            .add(Restrictions.eq("chorganisation", user.getChorganisation()));
                        
                        // Add CHO internal tasks on claims belongs to user organisation.
                        internalTasksOnClaimsBelongsToUserOrg = Restrictions.conjunction()
                                .add(Restrictions.eq("insurer", Boolean.FALSE))
                                .add(Restrictions.eq("visibility", 2))
                                .add(Subqueries.propertyIn("claim", claimsBelongsToUserCho));

                        // Add Insurer external tasks on claims belongs to user organisation.
                        externalTasksOnClaimsBelongsToUserOrg = Restrictions.conjunction()
                                .add(Restrictions.eq("insurer", Boolean.TRUE))
                                .add(Restrictions.eq("visibility", 3))
                                .add(Subqueries.propertyIn("claim", claimsBelongsToUserCho));
                    }

                    // add all Criterion together to make final query for CHO tasks.
                    criteria.add(Restrictions.disjunction()
                            .add(privateTasks)
                            .add(internalTasksWithNoClaimNumber)
                            .add(internalTasksOnClaimsUserOwns)
                            .add(externalTasksOnClaimsUserOwns)
                            .add(internalTasksOnClaimsBelongsToUserOrg)
                            .add(externalTasksOnClaimsBelongsToUserOrg)
                            .add(internalTasksOnClaimsNobodyOwns)
                            .add(externalTasksOnClaimsNobodyOwns));

                } else { // user is an Insurer user

                    List<WebUserUserRole> webUserUserRoles = webUserUserRoleService.getMappedUserRole(user.getId());
                    List<Integer> userWorkgroups = new ArrayList<Integer>();
                    boolean ownershipEnabled = false;
                    boolean workgroupEnabled = false;
                    boolean ownershipAndWorkgroupDisabled = false;

                    // declare default Criterion restriction to avoid null value when below if condition not passed. 
                    Criterion internalTasksWithNoClaimsAssigned = Restrictions.eq("id", -1);
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
                    


                    // declare subqueries which will be used in Criterion.
                    DetachedCriteria insurerUsers = DetachedCriteria.forClass(WebUser.class)
                            .setProjection(Property.forName("id"))
                            .add(Restrictions.eq("insurer", user.getInsurer()));

                    DetachedCriteria claimsBelongsToUserInsurer = DetachedCriteria.forClass(Claim.class)
                            .setProjection(Property.forName("id"))
                            .add(Restrictions.eq("insurer", user.getInsurer()));
                    
                    
                    if (showAssignedTasksOnly) {

                        DetachedCriteria claimsUserOwns = DetachedCriteria.forClass(Claim.class)
                                .setProjection(Property.forName("id"))
                                .add(Restrictions.eq("claimOwner", user));

                        DetachedCriteria claimsNotAssignedToUser = DetachedCriteria.forClass(Claim.class)
                                .setProjection(Property.forName("id"))
                                .add(Restrictions.isNull("claimOwner"))
                                .add(Restrictions.eq("insurer", user.getInsurer()));

                        DetachedCriteria claimsNotAssignedToWorkgroup = DetachedCriteria.forClass(Claim.class)
                                .setProjection(Property.forName("id"))
                                .add(Restrictions.isNull("workgroup"))
                                .add(Restrictions.eq("insurer", user.getInsurer()));
                        // get visibilityRoles depends upon the workgroup and ownership.
                        for (WebUserUserRole webUserUserRole : webUserUserRoles) {

                            LOG.debug("Getting tasks for users role {}", webUserUserRole.getWebUserRole().getName());
                            visibilityRole.add(webUserUserRole.getWebUserRole().getName());

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

                            // Add all Insurer internal tasks with no claim assigned to this role that user is in
                            internalTasksWithNoClaimsAssigned = Restrictions.conjunction()
                                    .add(Restrictions.eq("insurer", Boolean.TRUE))
                                    .add(Restrictions.eq("visibility", 2))
                                    .add(Restrictions.in("visibilityRole", visibilityRole))
                                    .add(Restrictions.isNull("claim"))
                                    .add(Subqueries.propertyIn("createdBy", insurerUsers));

                            if (ownershipEnabled) {

                                // Add all Insurer internal tasks assigned to this role on claims user owns
                                internalTasksAssignedToThisRoleOnClaimsUserOwns = Restrictions.conjunction()
                                        .add(Restrictions.eq("insurer", Boolean.TRUE))
                                        .add(Restrictions.eq("visibility", 2))
                                        .add(Restrictions.in("visibilityRole", visibilityRole1))
                                        .add(Subqueries.propertyIn("claim", claimsUserOwns));

                                // Add all Insurer internal tasks assigned to this role on claims no-one owns
                                internalTasksAssignedToThisRoleOnClaimsNobodyOwns = Restrictions.conjunction()
                                        .add(Restrictions.eq("insurer", Boolean.TRUE))
                                        .add(Restrictions.eq("visibility", 2))
                                        .add(Restrictions.in("visibilityRole", visibilityRole1))
                                        .add(Subqueries.propertyIn("claim", claimsNotAssignedToUser));


                                // Add all CHO external tasks assigned to role on claims user owns
                                externalTasksAssignedToThisRoleOnClaimsUserOwns = Restrictions.conjunction()
                                        .add(Restrictions.eq("insurer", Boolean.FALSE))
                                        .add(Restrictions.eq("visibility", 3))
                                        .add(Restrictions.in("visibilityRole", visibilityRole1))
                                        .add(Subqueries.propertyIn("claim", claimsUserOwns));

                            }

                            if (workgroupEnabled) {

                                // Add all Insurer internal tasks assigned to role on claims with no workgroup yet assigned
                                internalTaskesAssignedToThisRoleAndNoWorkgroup = Restrictions.conjunction()
                                        .add(Restrictions.eq("insurer", Boolean.TRUE))
                                        .add(Restrictions.eq("visibility", 2))
                                        .add(Restrictions.in("visibilityRole", visibilityRole2))
                                        .add(Subqueries.propertyIn("claim", claimsNotAssignedToWorkgroup));

                                // declare subquery which will be used in Criterion.
                                DetachedCriteria claimsWithThisWG = DetachedCriteria.forClass(Claim.class)
                                        .setProjection(Property.forName("id"))
                                        .createCriteria("workgroup")
                                        .add(Restrictions.in("id", userWorkgroups));

                                if (userWorkgroups.size() > 0) {

                                    // Add all Insurer internal tasks assigned to role on claims assigned to a workgroup that user is in
                                    internalTaskesAssignedToThisRoleAndWorkgroup = Restrictions.conjunction()
                                            .add(Restrictions.eq("insurer", Boolean.TRUE))
                                            .add(Restrictions.eq("visibility", 2))
                                            .add(Restrictions.in("visibilityRole", visibilityRole3))
                                            .add(Subqueries.propertyIn("claim", claimsWithThisWG));

                                    // Add all CHO external tasks assigned to this role on claims assigned to a workgroup that the user is in
                                    extTskAssignedToThisRoleOnClaimsAssignedToWG = Restrictions.conjunction()
                                            .add(Restrictions.eq("insurer", Boolean.FALSE))
                                            .add(Restrictions.eq("visibility", 3))
                                            .add(Restrictions.in("visibilityRole", visibilityRole3))
                                            .add(Subqueries.propertyIn("claim", claimsWithThisWG));
                                }
                            }

                            if (ownershipAndWorkgroupDisabled) {
                                // Add all Insurer internal tasks assigned to a claim and this role
                                intTskAssignedToClaimAndThisRole = Restrictions.conjunction()
                                        .add(Restrictions.eq("insurer", Boolean.TRUE))
                                        .add(Restrictions.eq("visibility", 2))
                                        .add(Restrictions.in("visibilityRole", visibilityRole4))
                                        .add(Subqueries.propertyIn("claim", claimsBelongsToUserInsurer));

                                // Add all CHO external tasks assigned to role
                                extTskAssingedToRole = Restrictions.conjunction()
                                        .add(Restrictions.eq("insurer", Boolean.FALSE))
                                        .add(Restrictions.eq("visibility", 3))
                                        .add(Restrictions.in("visibilityRole", visibilityRole4))
                                        .add(Subqueries.propertyIn("claim", claimsBelongsToUserInsurer));
                            }

                        }
                    } else {
                        
                        // Add all Insurer internal tasks with no claim assigned to the task
                        internalTasksWithNoClaimsAssigned = Restrictions.conjunction()
                                .add(Restrictions.eq("insurer", Boolean.TRUE))
                                .add(Restrictions.eq("visibility", 2))
                                .add(Restrictions.isNull("claim"))
                                .add(Subqueries.propertyIn("createdBy", insurerUsers));

                        // Add all Insurer internal tasks with claim assigned to the task
                        intTskAssingedToClaimBelongsToUserInsurer = Restrictions.conjunction()
                                .add(Restrictions.eq("insurer", Boolean.TRUE))
                                .add(Restrictions.eq("visibility", 2))
                                .add(Subqueries.propertyIn("claim", claimsBelongsToUserInsurer));

                        // Add all Insurer external tasks
                        extTskAssingedToClaimBelongsToUserInsurer = Restrictions.conjunction()
                                .add(Restrictions.eq("insurer", Boolean.FALSE))
                                .add(Restrictions.eq("visibility", 3))
                                .add(Subqueries.propertyIn("claim", claimsBelongsToUserInsurer));

                    }

                    // add all Criterion together to make final query for insurer task.
                    criteria.add(Restrictions.disjunction()
                            .add(privateTasks)
                            .add(internalTasksWithNoClaimsAssigned)
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

            // combine visibility role to include relative insurer task asigned to the role.
            visibilityRole.addAll(visibilityRole1);
            visibilityRole.addAll(visibilityRole2);
            visibilityRole.addAll(visibilityRole3);
            visibilityRole.addAll(visibilityRole4);

            // remove any related tasks
            criteria.createAlias("relatedTask", "t2", CriteriaSpecification.LEFT_JOIN);
            
            Criterion relatedTaskRestriction;
            
            if (visibilityRole.size() > 0) {
                relatedTaskRestriction = Restrictions.disjunction()
                    .add(Restrictions.gtProperty("id", "t2.id"))
                    .add(Restrictions.isNull("t2.id"))
                    .add(Restrictions.in("visibilityRole", visibilityRole));
            } else {
                relatedTaskRestriction = Restrictions.disjunction()
                    .add(Restrictions.gtProperty("id", "t2.id"))
                    .add(Restrictions.isNull("t2.id"));
            }
            
            criteria.add(relatedTaskRestriction);
            totalCount = totalCount(criteria);
            
            Date minDueDate = getTaskMinDueDate(criteria);
            if (minDueDate != null) {
                int days = DateHelper.getNumberOfDaysBetween(new Date(), minDueDate);
                colorCode = (days > 0) ? "green" : (days < 0) ? "red" : "orange";
            } else {
                colorCode = "green";
            }

            LOG.debug("Found {} tasks", totalCount);
            
            if (sort != null && sort.equalsIgnoreCase("choReference")) {
                // left join on claim used here to sort the task by choReference.
                criteria.createAlias("this.claim", "c", CriteriaSpecification.LEFT_JOIN);
            }
            
            criteria.setFirstResult(start);
            criteria.setMaxResults(limit);
            criteria.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            sortTasks(criteria, sort, dir);
            resultMap = criteria.list();
            
            for (HashMap m : resultMap) {
                results.add((Task) m.get("this"));
            }

            LOG.debug("Returning {} tasks", results.size());

        } catch (Exception ex) {
            LOG.error("Exception caught retrieving visible tasks: ", ex);
        }
        return new SearchResult(results, totalCount, colorCode);
    }
    
    private void sortTasks(Criteria criteria, String sort, String dir) {

        if (sort != null && dir !=null && !sort.isEmpty() && !dir.isEmpty()) {
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
            } else if (sort.equalsIgnoreCase("createdBy")) {
                addSort(criteria, "createdBy", dir);
            }
        } else {
            criteria.addOrder(Order.desc("dueDate"));
        }
    }
    
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

    private SearchResult getTasks(int webUserId, boolean incompleteOnly, boolean hasOwnership, boolean hasWorkgroups, int start, int limit, String sort, String dir, boolean  showAssignedTasksOnly) {
        WebUser webUser = null;
        if (webUserId > 0) {
            webUser = (WebUser) get(WebUser.class, webUserId);
            if (webUser == null) {
                LOG.warn("No such user found with id={}", webUserId);
                throw new IllegalArgumentException("No such user.");
            }
        }
        return getTasks(webUser, incompleteOnly, hasOwnership, hasWorkgroups, start, limit, sort, dir, showAssignedTasksOnly);
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
            LOG.debug("Found {} tasks", results.size());
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
