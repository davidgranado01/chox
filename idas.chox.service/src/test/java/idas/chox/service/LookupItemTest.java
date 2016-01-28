package idas.chox.service;

import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

import idas.chox.test.BaseTest;

/**
 *
 * @author emmanuel
 */
public class LookupItemTest extends BaseTest {


    @Test
    public void testCanGetAllVehicleClass() {


        Assert.assertNotNull(sessionFactory);
        Assert.assertNotNull(vehicleClassService);

        List vehicleClasses = vehicleClassService.getAllVehicleClass();
        System.out.println(vehicleClasses.size());
        Assert.assertTrue(vehicleClasses.size() > 0);
    }

    @Test
    public void testCanGetAllSupplier() {

        Assert.assertNotNull(sessionFactory);
        Assert.assertNotNull(chorganisationService);

        List chorganisations = chorganisationService.getChorganisations("name");
        System.out.println(chorganisations.size());
        Assert.assertTrue(chorganisations.size() > 0);
    }

    @Test
    public void testCanGetAllInsurers() {

        Assert.assertNotNull(sessionFactory);
        Assert.assertNotNull(insurerService);

        List insurers = insurerService.getInsurers();
        System.out.println(insurers.size());
        Assert.assertTrue(insurers.size() > 0);
    }

    @Test
    public void testCanGetAllWorkgroups() {

        Assert.assertNotNull(sessionFactory);
        Assert.assertNotNull(workgroupService);

        List workgroups = workgroupService.getActiveWorkgroupsByInsurer(3);
        System.out.println(workgroups.size());
        Assert.assertTrue(workgroups.size() > 0);
    }

    @Test
    public void testCanGetAllUserRoless() {

        Assert.assertNotNull(sessionFactory);
        Assert.assertNotNull(webUserUserRoleService);

        Set userRoles = webUserUserRoleService.getWebUserRoles(2, true, true, true, true,true, true, true, false);
        System.out.println(userRoles.size());
        Assert.assertTrue(userRoles.size() > 0);
    }

    @Test
    public void testCanGetAllReasonOfDelay() {

        Assert.assertNotNull(sessionFactory);
        Assert.assertNotNull(reasonOfDelayService);

        List reasonOfDelays = reasonOfDelayService.getAllReasonOfDelay();
        System.out.println(reasonOfDelays.size());
        Assert.assertTrue(reasonOfDelays.size() > 0);
    }

    @Test
    public void testCanGetAllReasonOfRejection() {

        Assert.assertNotNull(sessionFactory);
        Assert.assertNotNull(reasonOfRejectionService);

        List reasonOfRejections = reasonOfRejectionService.getInsurerReasons(3, null, null, null,null);
        System.out.println(reasonOfRejections.size());
        Assert.assertTrue(reasonOfRejections.size() > 0);
    }
}
