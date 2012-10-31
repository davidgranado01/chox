package idas.chox.service.intelligentNotes;


import org.junit.Assert;
import org.junit.Test;

import idas.chox.core.model.Claim;
import idas.chox.test.BaseTest;

public class CHOManagingRepairCheckNoteTest extends BaseTest {

    @Test
    public void testCHOManagingRepairPass() {
        Claim c = new Claim();
        
        c.setManagingRepair(true);
        
        Assert.assertTrue(new CHOManagingRepairCheckNote().isShowingFor(c));
    }

    @Test
    public void testCHOManagingRepairFail() {
        Claim c = new Claim();
        
        c.setManagingRepair(false);
        
        Assert.assertFalse(new CHOManagingRepairCheckNote().isShowingFor(c));
    }
}
