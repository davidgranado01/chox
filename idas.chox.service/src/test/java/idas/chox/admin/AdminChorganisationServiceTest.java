package idas.chox.admin;

import idas.chox.test.BaseTest;
import idas.chox.core.model.Chorganisation;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

public class AdminChorganisationServiceTest extends BaseTest {

    @Test
    @Transactional
    public void testUpdateChorganisationStatus() {
        Chorganisation chorganisation = chorganisationService.getActiveChorganisation().get(0);
        boolean bStatus = chorganisation.isStatus();
        adminChorganisationService.updateChorganisationStatus(chorganisation.getId().toString());
        boolean aStatus = chorganisation.isStatus();
        Assert.assertEquals(bStatus, !aStatus);
    }

    @Test
    @Transactional
    public void testUpdateChorganisation() {

        Chorganisation chorganisation = chorganisationService.getActiveChorganisation().get(0);
        String oAddress2 = "ABC ROAD";
        String oCompanyNum = "1234567890";
        String oVatNum = "987654321";
        String oName = "ABC NAME";

        chorganisation.setAddress2(oAddress2);
        chorganisation.setName(oName);
        chorganisation.setCompanyNo(oCompanyNum);
        chorganisation.setVatNo(oVatNum);
        
        adminChorganisationService.updateChorganisation(chorganisation);

        Assert.assertEquals(oAddress2, chorganisation.getAddress2());
        Assert.assertEquals(oCompanyNum, chorganisation.getCompanyNo());
        Assert.assertEquals(oVatNum, chorganisation.getVatNo());
        Assert.assertEquals(oName, chorganisation.getName());
    }

    @Test
    @Transactional
    public void testIsChorganisationNameExist() {
        Chorganisation chorganisation = chorganisationService.getActiveChorganisation().get(0);
        Assert.assertTrue(adminChorganisationService.isChorganisationNameExist(chorganisation.getName()));
        Assert.assertFalse(adminChorganisationService.isChorganisationNameExist("*&@^$*@&"));
    }

    @Test
    @Transactional
    public void testgetChorganisation() {
        Chorganisation chorg1 = chorganisationService.getActiveChorganisation().get(0);
        Chorganisation chorg2 = adminChorganisationService.getChorganisation(chorg1.getId().toString());
        Assert.assertEquals(chorg1, chorg2);
    }
    
}
