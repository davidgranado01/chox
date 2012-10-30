
package idas.chox.service.intelligentNotes;


import org.junit.Assert;
import org.junit.Test;

import idas.chox.core.model.*;


public class VehicleClassAboveSCheckNoteTest {
   
     @Test
    // Vehicle class is p.
    public void testVehicleClassAboveSCheckNotePass() {
        Claim c = new Claim();
        
        Customer customer = new Customer();
        VehicleClass vclass = new VehicleClass();
        
        vclass.setName("sp");
        
        customer.setVehicleClass(vclass);
        c.setCustomer(customer);
        
        Assert.assertTrue((new VehicleClassAboveSCheckNote()).isShowingFor(c));
    }

    @Test
    // Vehicle class is s.
    public void testVehicleClassAboveSCheckNoteFail() {
        Claim c = new Claim();
        
        Customer customer = new Customer();
        VehicleClass vclass = new VehicleClass();
        
        vclass.setName("s");
        
        customer.setVehicleClass(vclass);
        c.setCustomer(customer);
        
        Assert.assertFalse((new VehicleClassAboveSCheckNote()).isShowingFor(c));
    }
}
