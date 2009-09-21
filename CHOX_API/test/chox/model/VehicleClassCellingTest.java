/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.model;

import chox.services.ClaimService;
import chox.services.InsurerService;
import chox.services.UploadClaimXMLService;
import chox.services.VehicleClassService;
import java.io.IOException;
import java.math.BigDecimal;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Emmanuel
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml","classpath:applicationContext-services.xml"})
public class VehicleClassCellingTest {

    @Autowired
    UploadClaimXMLService uploadClaimXMLService;
    @Autowired
    ClaimService claimService;
    @Autowired
    InsurerService insurerService;
    @Autowired
    VehicleClassService vehicleClassService;

    @Test
    @Transactional
    public void testCanSave() throws IOException {

        Insurer RSA = insurerService.getInsurerByName("RSA");
        VehicleClass P1 = vehicleClassService.getVehicleClassByName("P1");

        Assert.assertNotNull(RSA);
        Assert.assertNotNull(P1);

        VehicleClassCelling vehicleClassCelling = new VehicleClassCelling(new BigDecimal(1000),new BigDecimal(1000));
        vehicleClassCelling.setInsurer(RSA);
        vehicleClassCelling.setVehicleClass(P1);
        RSA.AddVehicleClassCelling(vehicleClassCelling);

        insurerService.updateObject(RSA);

        Insurer insurerWithVehicleClassCelling = insurerService.getObject(RSA.getId());
        Assert.assertNotNull(insurerWithVehicleClassCelling);

        Assert.assertEquals(1,insurerWithVehicleClassCelling.getVehicleClassCellings().size());

    }
}

