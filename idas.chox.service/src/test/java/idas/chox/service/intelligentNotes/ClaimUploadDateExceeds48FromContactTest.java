
package idas.chox.service.intelligentNotes;


import org.junit.Assert;
import org.junit.Test;

import idas.chox.core.model.Claim;
import idas.chox.core.util.DateHelper;

public class ClaimUploadDateExceeds48FromContactTest {

    @Test
    public void testClaimUploadDateExceeds48FromContactPass() {
        Claim c = new Claim();
        
        c.setCreatedDate(DateHelper.parse("07/12/2012 05:01",DateHelper.getLocalDateTimeFormat()));
        c.setPolicyHolderContactDate(DateHelper.parse("05/12/2012 05:00",DateHelper.getLocalDateTimeFormat()));
        
        Assert.assertTrue((new ClaimUploadDateExceeds48FromContact()).isShowingFor(c));
    }

    @Test
    public void testClaimUploadDateExceeds48FromContactFail() {
        Claim c = new Claim();
        
        c.setCreatedDate(DateHelper.parse("06/12/2012 05:01",DateHelper.getLocalDateTimeFormat()));
        c.setPolicyHolderContactDate(DateHelper.parse("05/12/2012 05:00",DateHelper.getLocalDateTimeFormat()));
        
        Assert.assertFalse((new ClaimUploadDateExceeds48FromContact()).isShowingFor(c));
    }
}
