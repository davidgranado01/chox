package idas.chox.admin;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.WebUser;
import idas.chox.core.services.ChorganisationService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.InsurerService;
import idas.chox.core.services.UserService;
import idas.chox.service.ActionResponse;
import idas.chox.service.admin.AdminUserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml", "classpath:applicationContext-services-test.xml"})
public class AdminUserServiceTest {

    @Autowired
    AdminUserService adminUserService;
    @Autowired
    UserService userService;
    @Autowired
    InsurerService insurerService;
    @Autowired
    ChorganisationService chorganisationService;
    @Autowired
    ClaimService claimService;

    // <editor-fold defaultstate="collapsed" desc="USERS">
    
    @Test
    @Transactional
    public void testUpdateUser() {
        String newUserName = "PeterDavidJohnson";
        WebUser webUser1 = userService.getUsers().get(0);
        webUser1.setUserName(newUserName);
        ActionResponse response1 = adminUserService.updateUser(webUser1);
        Assert.assertTrue(response1.getIsValid());
        WebUser webUser2 = userService.getUsers().get(1);
        webUser2.setUserName(newUserName);
        ActionResponse response2 = adminUserService.updateUser(webUser2);
        Assert.assertFalse(response2.getIsValid());
        Assert.assertEquals("User Name is already exist!", response2.getErrors().get(0));
    }

    @Test
    @Transactional
    public void testAddNewUserWithNewUserName() {
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
    public void testAddNewUserWithOldUserName() {
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
        Assert.assertEquals("User Name is already exist!", response.getErrors().get(0));
    }

    @Test
    @Transactional
    public void testUserPassword() {
        WebUser webUser = userService.getUsers().get(0);
        String newPassword = "abc1234567890";
        String encodedNewPassword = adminUserService.encodePassword(newPassword);
        webUser.setPassword(newPassword);
        ActionResponse response = adminUserService.updateUserPassword(webUser);
        Assert.assertTrue(response.getIsValid());
        Assert.assertEquals(encodedNewPassword, webUser.getPassword());
    }

    @Test
    @Transactional
    public void testTriggerPasswordExpiredStatus() {
        Insurer insurer = insurerService.getInsurerByName("RSA");
        WebUser webUser = userService.getUsers(insurer.getId(), 2, -1).get(0);
        webUser.setStatus(true);
        webUser.setIsExpired(true);
        Assert.assertTrue(adminUserService.updateUser(webUser).getIsValid());
        Assert.assertTrue(adminUserService.triggerPasswordExpiredStatus(webUser).getIsValid());
        Assert.assertFalse(webUser.getIsExpired());
    }

    // TRUE to FALSE: WITH OPEN ITEM
    @Test
    @Transactional
    public void testTriggerUserStatusToFalseWithOpenClaim() {

        Insurer insurer = insurerService.getInsurerByName("RSA");
        WebUser webUser = userService.getUsers(insurer.getId(), 2, -1).get(0);
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
    public void testTriggerUserStatusToFalseWithoutOpenClaim() {

        Insurer insurer = insurerService.getInsurerByName("RSA");
        WebUser webUser = userService.getUsers(insurer.getId(), 2, -1).get(0);
        webUser.setStatus(true);
        ActionResponse response = adminUserService.updateUser(webUser);
        Assert.assertTrue(response.getIsValid());

        WebUser webUser2 = userService.getUsers(insurer.getId(), 2, -1).get(2);
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
    public void testTriggerUserStatusToTrue() {

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
}
