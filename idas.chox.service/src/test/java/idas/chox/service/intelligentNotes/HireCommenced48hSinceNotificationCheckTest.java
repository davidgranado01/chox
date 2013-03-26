
package idas.chox.service.intelligentNotes;


import org.junit.Assert;
import org.junit.Test;

import idas.chox.core.model.Claim;
import idas.chox.core.model.VehicleHire;
import idas.chox.core.util.DateHelper;


public class HireCommenced48hSinceNotificationCheckTest {
    
    @Test
    public void testHireCommenced48hSinceNotificationCheckPass() {
        Claim c = new Claim();
        VehicleHire vehicleHire = new VehicleHire();
        
        vehicleHire.setHireStart(DateHelper.parse("07/12/2012 05:00",DateHelper.getLocalDateTimeFormat()));
        c.setVehicleHire(vehicleHire);
        c.setCreatedDate(DateHelper.parse("09/12/2012 05:01",DateHelper.getLocalDateTimeFormat()));
        
        Assert.assertTrue((new HireCommenced48hSinceNotificationCheck()).isShowingFor(c));
    }

    @Test
    public void testHireCommenced48hSinceNotificationCheckFail() {
        Claim c = new Claim();
        VehicleHire vehicleHire = new VehicleHire();
        
        vehicleHire.setHireStart(DateHelper.parse("07/12/2012 05:00",DateHelper.getLocalDateTimeFormat()));
        c.setVehicleHire(vehicleHire);
        c.setCreatedDate(DateHelper.parse("08/12/2012 05:01",DateHelper.getLocalDateTimeFormat()));
        
        Assert.assertFalse((new HireCommenced48hSinceNotificationCheck()).isShowingFor(c));
    }
}
