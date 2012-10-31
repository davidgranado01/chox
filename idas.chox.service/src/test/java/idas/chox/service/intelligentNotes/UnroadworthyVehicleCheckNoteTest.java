
package idas.chox.service.intelligentNotes;


import org.junit.Assert;
import org.junit.Test;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Customer;


public class UnroadworthyVehicleCheckNoteTest {
    
    @Test
    public void testUnroadworthyVehicleCheckNotePass() {
        Claim c = new Claim();
        Customer customer = new Customer();
        
        customer.setIsUsable(false);
        c.setCustomer(customer);
        
        Assert.assertTrue(new UnroadworthyVehicleCheckNote().isShowingFor(c));
    }

    @Test
    public void testUnroadworthyVehicleCheckNoteFail() {
        Claim c = new Claim();
        Customer customer = new Customer();
        
        customer.setIsUsable(true);
        c.setCustomer(customer);
        
        Assert.assertFalse(new UnroadworthyVehicleCheckNote().isShowingFor(c));
    }
}
