package idas.chox.admin;

import idas.chox.test.BaseTest;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.IdLookupItem;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.WebUserUserRole;
import idas.chox.core.model.WebUserWorkgroup;
import idas.chox.core.search.SearchResult;
import idas.chox.service.ActionResponse;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

public class AdminUserServiceTest extends BaseTest {


    // <editor-fold defaultstate="collapsed" desc="USERS">
    @Test
    @Transactional
    public void testUser_UpdateUser() {
//        String newUserName = "PeterDavidJohnson";
//        WebUser webUser1 = userService.getUsers().get(0);
//        webUser1.setUserName(newUserName);
//        ActionResponse response1 = adminUserService.updateUser(webUser1);
//        Assert.assertTrue(response1.getIsValid());
//
//        WebUser webUser2 = userService.getUsers().get(1);
//        webUser2.setUserName(newUserName);
//        ActionResponse response2 = adminUserService.updateUser(webUser2);
//        Assert.assertFalse(response2.getIsValid());
//        Assert.assertEquals("User Name already exists in CHOX", response2.getErrors().get(0));
    }

    @Test
    @Transactional
    public void testUser_AddNewUserWithNewUserName() {
        WebUser newUser = new WebUser();
        newUser.setUserName("jenny.jackson");
        newUser.setEmail("jenny@abc.com");
        newUser.setFirstName("Jenny");
        newUser.setLastName("Jackson");
        newUser.setStatus(true);
        int insurerId = insurerService.getInsurers().get(0).getId();
        int supplierId = -1;
        ActionResponse response = adminUserService.doAddNewUser(newUser, insurerId, supplierId, 2);
        Assert.assertTrue(response.getIsValid());
    }

    @Test
    @Transactional
    public void testUser_AddNewUserWithOldUserName() {
        WebUser existingUser = userService.getUsers().get(0);
        WebUser newUser = new WebUser();
        newUser.setUserName(existingUser.getUserName());
        newUser.setEmail("jenny@abc.com");
        newUser.setFirstName("Jenny");
        newUser.setLastName("Jackson");
        newUser.setIsExpired(Boolean.FALSE);
        newUser.setStatus(true);
        int insurerId = insurerService.getInsurers().get(0).getId();
        int supplierId = -1;
        ActionResponse response = adminUserService.doAddNewUser(newUser, insurerId, supplierId, 2);
        Assert.assertFalse(response.getIsValid());
        Assert.assertEquals("User Name already exists in CHOX", response.getErrors().get(0));
    }

    @Test
    @Transactional
    public void testUser_UserPassword() {
        WebUser webUser = userService.getUsers().get(1);
        String newPassword = "Abc1234567890";
        String encodedNewPassword = adminUserService.encodePassword(newPassword);
        webUser.setPassword(newPassword);
        ActionResponse response = adminUserService.updateUserPassword(webUser);
        Assert.assertTrue(response.getIsValid());
        Assert.assertEquals(encodedNewPassword, webUser.getPassword());
    }

    @Test
    @Transactional
    public void testUser_TriggerPasswordExpiredStatus() {
        Insurer insurer = insurerService.getInsurerByName("RSA");
        SearchResult searchResult = userService.getUsers(insurer.getId(), 2, -1, 0, 20, "", "");
        List<WebUser> userData = searchResult.getResult();
        WebUser webUser = userData.get(0);
        webUser.setStatus(true);
        webUser.setIsExpired(true);
        Assert.assertTrue(adminUserService.updateUser(webUser).getIsValid());
        Assert.assertTrue(adminUserService.triggerPasswordExpiredStatus(webUser).getIsValid());
        Assert.assertFalse(webUser.getIsExpired());
    }

    // TRUE to FALSE: WITH OPEN ITEM
    @Test
    @Transactional
    public void testUser_TriggerUserStatusToFalseWithOpenClaim() {

        Insurer insurer = insurerService.getInsurerByName("RSA");
        SearchResult searchResult = userService.getUsers(insurer.getId(), 2, -1, 0, 20, "", "");
        List<WebUser> userData = searchResult.getResult();
        WebUser webUser = userData.get(0);
        webUser.setStatus(true);
        ActionResponse response = adminUserService.updateUser(webUser);
        Assert.assertTrue(response.getIsValid());

        Claim claim = new Claim();
        claim.setClaimNumber("ABC123455");
        claim.setManagingRepair(true);
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED);
        claim.setInsurer(insurer);
        claim.setChorganisation(chorganisationService.getActiveChorganisation().get(0));
        claim.setChoReference("HJU12345678");
        claim.setClaimOwner(webUser);
        claimService.updateClaim(claim);

        // TRIGGER USER STATUS FROM FALSE TO TRUE
        ActionResponse response2 = adminUserService.triggerUserStatus(webUser);

        // CHECK PROCESSED RESULT
        Assert.assertTrue(response2.getIsValid());
        Assert.assertEquals(response2.getResult(), "This user currently has assigned claims. Please reassign these claims before de-activating this user account");
    }

    // TRUE to FALSE: WITHOUT OPEN ITEM
    @Test
    @Transactional
    public void testUser_TriggerUserStatusToFalseWithoutOpenClaim() {

        Insurer insurer = insurerService.getInsurerByName("RSA");
        SearchResult searchResult = userService.getUsers(insurer.getId(), 2, -1, 0, 20, "", "");
        List<WebUser> userData = searchResult.getResult();
        WebUser webUser = userData.get(0);
        webUser.setStatus(true);
        ActionResponse response = adminUserService.updateUser(webUser);
        Assert.assertTrue(response.getIsValid());

        SearchResult searchResult2 = userService.getUsers(insurer.getId(), 2, -1, 0, 20, "", "");
        List<WebUser> userData2= searchResult.getResult();
        WebUser webUser2 = userData.get(2);
        Claim claim = new Claim();
        claim.setClaimNumber("ABC123455");
        claim.setManagingRepair(true);
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED);
        claim.setInsurer(insurer);
        claim.setChorganisation(chorganisationService.getActiveChorganisation().get(0));
        claim.setChoReference("HJU12345678");
        claim.setClaimOwner(webUser2);
        claimService.updateClaim(claim);

        // TRIGGER USER STATUS FROM FALSE TO TRUE
        ActionResponse response2 = adminUserService.triggerUserStatus(webUser);
        Assert.assertTrue(response2.getIsValid());

    }

    // FALSE TO TRUE: WITHOUT OPEN ITEM
    @Test
    @Transactional
    public void testUser_TriggerUserStatusToTrue() {

        // SETUP TEST USER
        WebUser webUser = userService.getUsers().get(0);
        webUser.setStatus(false);
        ActionResponse response = adminUserService.updateUser(webUser);
        Assert.assertTrue(response.getIsValid());

        // TRIGGER USER STATUS FROM FALSE TO TRUE
        ActionResponse response2 = adminUserService.triggerUserStatus(webUser);
        Assert.assertTrue(response2.getIsValid());
        Assert.assertTrue(webUser.getStatus());
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="USER ROLES">
    @Test
    @Transactional
    public void testUserRole_CreditHireUserAddNewRole() {

        WebUser webUser = userService.findByUserName("op@cho.com");

        // CHECK SELECTED USER'S WEB USER USER ROLES
        List<WebUserUserRole> selectedUserRoles = adminUserService.getMappedUserRole(webUser.getId());
        List availableUserRole = adminUserService.getAvailableUserRoles(3, webUser.getId());
        Assert.assertEquals(2, selectedUserRoles.size());
        Assert.assertEquals(2, availableUserRole.size());
//        IdLookupItem item = (IdLookupItem) (availableUserRole.get(0));
//        int webUserUserRoleId = item.getId();
        // ADD NEW WEB-USER-USER ROLE
//        ActionResponse response = adminUserService.addNewWebUserRoleMapping(webUser.getId(), webUserUserRoleId);
//        System.out.println(response.getIsValid());
//        Assert.assertTrue(response.getIsValid());
//        Assert.assertEquals(3, (adminUserService.getMappedUserRole(webUser.getId())).size());
//        Assert.assertEquals(0, (adminUserService.getAvailableUserRoles(3, webUser.getId())).size());

    }

    @Test
    @Transactional
    public void testUserRole_CreditHireUserDeleteRole() {

        WebUser webUser = userService.findByUserName("op@cho.com");

        // CHECK SELECTED USER'S WEB-USER-USER-ROLES
        List<WebUserUserRole> selectedUserRoles = adminUserService.getMappedUserRole(webUser.getId());
        List availableUserRole = adminUserService.getAvailableUserRoles(3, webUser.getId());
        Assert.assertEquals(2, selectedUserRoles.size());
        Assert.assertEquals(2, availableUserRole.size());

        int webUserUserRoleId = selectedUserRoles.get(0).getId();

        // DELETE WEB-USER-USER-ROLE
        ActionResponse response1 = adminUserService.deleteWebUserRoleMapping(webUserUserRoleId);
        Assert.assertTrue(response1.getIsValid());
        Assert.assertEquals(1, (adminUserService.getMappedUserRole(webUser.getId())).size());
        Assert.assertEquals(3, (adminUserService.getAvailableUserRoles(3, webUser.getId())).size());

    }

    @Test
    @Transactional
    public void testUserRole_AddNewWorkgroup() {

        // CLAIM HANDLER
        WebUser webUser = userService.findByUserName("ch@ins.com");

        Assert.assertEquals(0, adminUserService.getUserWorkgroupsByUserId(webUser.getId()).size());

        // MAKE SURE INSURER IS WORKGROUP AND CLAIM OWNERSHIP ENABLE
        Insurer insurer = insurerService.getInsurer(webUser.getInsurer().getId());
        insurer.setWorkgroupEnable(true);
        insurer.setClaimOwnershipEnable(true);
        insurerService.saveInsurer(insurer);
        insurerService.getInsurer(webUser.getInsurer().getId());

        // ADD NEW WORKGROUPS
        int workgroupId = ((IdLookupItem) adminUserService.getWorkgroups(webUser.getId()).get(0)).getId();
        ActionResponse response = adminUserService.addNewWebUserWorkgroupMapping(workgroupId, webUser.getId());
        Assert.assertTrue(response.getIsValid());
        Assert.assertEquals(1, adminUserService.getUserWorkgroupsByUserId(webUser.getId()).size());
    }

    @Test
    @Transactional
    public void testUserRole_DeleteWorkgroup() {

        // CLAIM HANDLER
        WebUser webUser = userService.findByUserName("ch@ins.com");

        Assert.assertEquals(0, adminUserService.getUserWorkgroupsByUserId(webUser.getId()).size());

        // MAKE SURE INSURER IS WORKGROUP AND CLAIM OWNERSHIP ENABLE
        Insurer insurer = insurerService.getInsurer(webUser.getInsurer().getId());
        insurer.setWorkgroupEnable(true);
        insurer.setClaimOwnershipEnable(true);
        insurerService.saveInsurer(insurer);
        insurerService.getInsurer(webUser.getInsurer().getId());

        // SAVE NEW WORKGROUP FIRST
        int workgroupId = ((IdLookupItem) adminUserService.getWorkgroups(webUser.getId()).get(0)).getId();
        ActionResponse response = adminUserService.addNewWebUserWorkgroupMapping(workgroupId, webUser.getId());
        Assert.assertTrue(response.getIsValid());
        List<WebUserWorkgroup> webUserWorkgroups = adminUserService.getUserWorkgroupsByUserId(webUser.getId());
        Assert.assertEquals(1, webUserWorkgroups.size());

        // DELETE NEW ADDED WORKGROUP
        WebUserWorkgroup webUserWorkgroup = webUserWorkgroups.get(0);
        ActionResponse response2 = adminUserService.removeWebUserWorkgroupMapping(webUserWorkgroup.getWorkgroup().getId(), webUserWorkgroup.getUser().getId());
        Assert.assertTrue(response2.getIsValid());

    }

    @Test
    @Transactional
    public void testUserRole_RoleRemoveValidation_CH_without_Workgroup() {
        // CLAIM HANDLER
//        WebUser webUser = userService.findByUserName("ch@ins.com");
        // MAKE SURE INSURER IS WORKGROUP AND CLAIM OWNERSHIP ENABLE
//        Insurer insurer = insurerService.getInsurer(webUser.getInsurer().getId());
//        insurer.setWorkgroupEnable(true);
//        insurer.setClaimOwnershipEnable(true);
//        insurerService.saveInsurer(insurer);
        //webUser.getWorkgroups().add(workgroupService.getActiveWorkgroupsByInsurer(webUser.getInsurer().getId()).get(0));
        //webUser.getWorkgroups().add(workgroupService.getActiveWorkgroupsByInsurer(webUser.getInsurer().getId()).get(1));
        // userService.saveUser(webUser);
        //ActionResponse response = adminUserService.ValidateRoleToBeDeleted(webUser.getId(), WebUserRole.ROLE_CH);
        //Assert.assertFalse(response.getIsValid());
        //Assert.assertEquals(response.getErrors().get(0), "It is not possible to remove this role against a user who has workgroup(s). Please remove the workgroup(s) from this user.");
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="USER WORKGROUPS">
    @Test
    @Transactional
    public void testUserWorkgroup_CheckUserWorkgroupMappingIsAllowToDelete_NoOpenClaim() {

        // CLAIM HANDLER
        WebUser webUser = userService.findByUserName("ch@ins.com");

        // MAKE SURE INSURER IS WORKGROUP AND CLAIM OWNERSHIP ENABLE
        Insurer insurer = insurerService.getInsurer(webUser.getInsurer().getId());
        insurer.setWorkgroupEnable(true);
        insurer.setClaimOwnershipEnable(true);
        insurerService.saveInsurer(insurer);

        int workgroupId = ((IdLookupItem) adminUserService.getWorkgroups(webUser.getId()).get(0)).getId();
        ActionResponse response = adminUserService.addNewWebUserWorkgroupMapping(workgroupId, webUser.getId());
        Assert.assertTrue(response.getIsValid());
        List<WebUserWorkgroup> webUserWorkgroups = adminUserService.getUserWorkgroupsByUserId(webUser.getId());
        Assert.assertEquals(1, webUserWorkgroups.size());

        WebUserWorkgroup webUserWorkgroup = webUserWorkgroups.get(0);

        ActionResponse response2 = adminUserService.checkUserWorkgroupAllowToDelete(webUserWorkgroup.getId());
        Assert.assertTrue(response2.getIsValid());

    }

    @Test
    @Transactional
    public void testUserWorkgroup_CheckUserWorkgroupMappingIsAllowToDelete_OpenClaim() {

        // CLAIM HANDLER
        WebUser webUser = userService.findByUserName("ch@ins.com");

        // MAKE SURE INSURER IS WORKGROUP AND CLAIM OWNERSHIP ENABLE
        Insurer insurer = insurerService.getInsurer(webUser.getInsurer().getId());
        insurer.setWorkgroupEnable(true);
        insurer.setClaimOwnershipEnable(true);
        insurerService.saveInsurer(insurer);

        // GET WORKGROUP
        int workgroupId = ((IdLookupItem) adminUserService.getWorkgroups(webUser.getId()).get(0)).getId();
        ActionResponse response = adminUserService.addNewWebUserWorkgroupMapping(workgroupId, webUser.getId());
        Assert.assertTrue(response.getIsValid());
        List<WebUserWorkgroup> webUserWorkgroups = adminUserService.getUserWorkgroupsByUserId(webUser.getId());
        Assert.assertEquals(1, webUserWorkgroups.size());
        WebUserWorkgroup webUserWorkgroup = webUserWorkgroups.get(0);

        // CREATE NEW CLAIM
        Claim claim = new Claim();
        claim.setClaimNumber("ABC123455");
        claim.setManagingRepair(true);
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED);
        claim.setInsurer(insurer);
        claim.setChorganisation(chorganisationService.getActiveChorganisation().get(0));
        claim.setChoReference("HJU12345678");
        // claim.setClaimOwner(webUser);
        claim.setWorkgroup(webUserWorkgroup.getWorkgroup());
        claimService.updateClaim(claim);

        ActionResponse response2 = adminUserService.checkUserWorkgroupAllowToDelete(webUserWorkgroup.getId());
        Assert.assertTrue(response2.getIsValid());
        Assert.assertEquals(response2.getResult(), "User '" + webUserWorkgroup.getUser().getDisplayName() + "' is the last user that has Insurer Claim Handler Role, and is assigned to Workgroup '" + webUserWorkgroup.getWorkgroup().getName() + "'. Are you sure you want to remove this Workgroup?");

    }
    // </editor-fold>
}
