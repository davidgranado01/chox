
package idas.chox.service.intelligentNotes;


import org.junit.Assert;
import org.junit.Test;

import idas.chox.core.model.*;
import idas.chox.core.util.DateHelper;


public class RepairBookInDateisFridayCheckTest {
    
    @Test
    // Repair book in date is Friday.
    public void testRepairBookInDateisFridayCheckPass() {
        Claim c = new Claim();
        HireMonitoringDetail hireMonitoringDetail = new HireMonitoringDetail();
        Customer customer = new Customer();
        
        customer.setIsUsable(true);
        hireMonitoringDetail.setOriginalRepairBookInDate(DateHelper.parse("26/10/2012"));
        c.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        c.setHireMonitoringDetail(hireMonitoringDetail);
        c.setCustomer(customer);
        
        Assert.assertTrue((new RepairBookInDateisFridayCheck()).isShowingFor(c));
    }

    @Test
    // Repair book in date is Saturday.
    public void testRepairBookInDateisFridayCheckFail() {
        Claim c = new Claim();
        HireMonitoringDetail hireMonitoringDetail = new HireMonitoringDetail();
        Customer customer = new Customer();
        
        customer.setIsUsable(true);
        hireMonitoringDetail.setOriginalRepairBookInDate(DateHelper.parse("27/10/2012"));
        c.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        c.setHireMonitoringDetail(hireMonitoringDetail);
        c.setCustomer(customer);
        
        Assert.assertFalse((new RepairBookInDateisFridayCheck()).isShowingFor(c));
    }
}
