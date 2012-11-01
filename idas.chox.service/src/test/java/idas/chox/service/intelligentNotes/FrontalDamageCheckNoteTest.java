
package idas.chox.service.intelligentNotes;


import org.junit.Assert;
import org.junit.Test;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Customer;


public class FrontalDamageCheckNoteTest {
   
    @Test
    public void testFrontalDamageCheckNotePass() {
        Claim c = new Claim();
        Customer customer = new Customer();
        
        customer.setDamage("Frontal damage to the vehicle.");
        c.setCustomer(customer);
        
        Assert.assertTrue((new FrontalDamageCheckNote()).isShowingFor(c));
    }

    @Test
    public void testFrontalDamageCheckNoteFail() {
        Claim c = new Claim();
        Customer customer = new Customer();
        
        customer.setDamage("From back bumper to tail light.");
        c.setCustomer(customer);
        
        Assert.assertFalse((new FrontalDamageCheckNote()).isShowingFor(c));
    }
    
    @Test
    public void testFrontalDamageCheckNoteFail2() {
        Claim c = new Claim();
        Customer customer = new Customer();
        
        customer.setDamage("Vehicle damaged severely.");
        c.setCustomer(customer);
        
        Assert.assertFalse((new FrontalDamageCheckNote()).isShowingFor(c));
    }
}
