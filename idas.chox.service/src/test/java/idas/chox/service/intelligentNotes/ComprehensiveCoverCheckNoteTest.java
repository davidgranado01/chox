
package idas.chox.service.intelligentNotes;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Customer;
import org.junit.Assert;
import org.junit.Test;


public class ComprehensiveCoverCheckNoteTest {
    
    @Test
    public void testComprehensiveCoverCheckNotePass() {
        Claim c = new Claim();
        Customer customer = new Customer();
        
        customer.setComprehensive(false);
        c.setCustomer(customer);
        c.setManagingRepair(true);
        
        Assert.assertTrue((new ComprehensiveCoverCheckNote()).isShowingFor(c));
    }

    @Test
    public void testComprehensiveCoverCheckNoteFail() {
        Claim c = new Claim();
        Customer customer = new Customer();
        
        customer.setComprehensive(false);
        c.setCustomer(customer);
        c.setManagingRepair(false);
        
        Assert.assertFalse((new ComprehensiveCoverCheckNote()).isShowingFor(c));
    }
    
    @Test
    public void testComprehensiveCoverCheckNoteFail2() {
        Claim c = new Claim();
        Customer customer = new Customer();
        
        customer.setComprehensive(true);
        c.setCustomer(customer);
        c.setManagingRepair(false);
        
        Assert.assertFalse((new ComprehensiveCoverCheckNote()).isShowingFor(c));
    }
}
