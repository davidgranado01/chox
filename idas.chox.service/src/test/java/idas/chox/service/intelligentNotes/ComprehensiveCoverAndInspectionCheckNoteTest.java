
package idas.chox.service.intelligentNotes;


import org.junit.Assert;
import org.junit.Test;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Customer;


public class ComprehensiveCoverAndInspectionCheckNoteTest {
    
    @Test
    public void testComprehensiveCoverAndInspectionCheckNotePass() {
        Claim c = new Claim();
        Customer customer = new Customer();
        
        customer.setComprehensive(false);
        c.setCustomer(customer);
        c.setManagingRepair(false);
        
        Assert.assertTrue((new ComprehensiveCoverAndInspectionCheckNote()).isShowingFor(c));
    }

    @Test
    public void testComprehensiveCoverAndInspectionCheckNoteFail() {
        Claim c = new Claim();
        Customer customer = new Customer();
        
        customer.setComprehensive(false);
        c.setCustomer(customer);
        c.setManagingRepair(true);
        
        Assert.assertFalse((new ComprehensiveCoverAndInspectionCheckNote()).isShowingFor(c));
    }
    
    @Test
    public void testComprehensiveCoverAndInspectionCheckNoteFail2() {
        Claim c = new Claim();
        Customer customer = new Customer();
        
        customer.setComprehensive(true);
        c.setCustomer(customer);
        c.setManagingRepair(false);
        
        Assert.assertFalse((new ComprehensiveCoverAndInspectionCheckNote()).isShowingFor(c));
    }
}
