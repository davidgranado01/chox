
package idas.chox.service.intelligentNotes;


import org.junit.Assert;
import org.junit.Test;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Customer;
import idas.chox.core.model.HireMonitoringDetail;
import idas.chox.core.util.DateHelper;

public class RepairBookInDateisSundayCheckTest {
    
    @Test
    // Repair book in date is Sunday.
    public void testRepairBookInDateisSundayCheckPass() {
        Claim c = new Claim();
        HireMonitoringDetail hireMonitoringDetail = new HireMonitoringDetail();
        Customer customer = new Customer();
        
        customer.setIsUsable(true);
        hireMonitoringDetail.setOriginalRepairBookInDate(DateHelper.parse("28/10/2012"));
        c.setStatus(ClaimStatus.CLAIM_PENDING);
        c.setHireMonitoringDetail(hireMonitoringDetail);
        c.setCustomer(customer);
        
        Assert.assertTrue((new RepairBookInDateisSundayCheck()).isShowingFor(c));
    }

    @Test
    // Repair book in date is Friday.
    public void testRepairBookInDateisSundayCheckFail() {
        Claim c = new Claim();
        HireMonitoringDetail hireMonitoringDetail = new HireMonitoringDetail();
        Customer customer = new Customer();
        
        customer.setIsUsable(true);
        hireMonitoringDetail.setOriginalRepairBookInDate(DateHelper.parse("26/10/2012"));
        c.setStatus(ClaimStatus.CLAIM_PENDING);
        c.setHireMonitoringDetail(hireMonitoringDetail);
        c.setCustomer(customer);
        
        Assert.assertFalse((new RepairBookInDateisSundayCheck()).isShowingFor(c));
    }
}
