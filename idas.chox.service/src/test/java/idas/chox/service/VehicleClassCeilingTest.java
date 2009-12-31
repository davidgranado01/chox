package idas.chox.service;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import idas.chox.core.model.Insurer;
import idas.chox.core.model.VehicleClass;
import idas.chox.core.model.VehicleClassCeiling;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.InsurerService;
import idas.chox.core.services.UploadClaimXMLService;
import idas.chox.core.services.VehicleClassService;
import java.io.IOException;
import java.math.BigDecimal;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml", "classpath:applicationContext-services-test.xml"})
public class VehicleClassCeilingTest {

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

        VehicleClassCeiling vehicleClassCeiling = new VehicleClassCeiling(new BigDecimal(1000), new BigDecimal(1000));
        vehicleClassCeiling.setInsurer(RSA);
        vehicleClassCeiling.setVehicleClass(P1);
        RSA.AddVehicleClassCeiling(vehicleClassCeiling);

        insurerService.updateInsurer(RSA);

        Insurer insurerWithVehicleClassCeiling = insurerService.getInsurer(RSA.getId());
        Assert.assertNotNull(insurerWithVehicleClassCeiling);

        Assert.assertEquals(1, insurerWithVehicleClassCeiling.getVehicleClassCeilings().size());

    }
}

