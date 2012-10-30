
package idas.chox.service.intelligentNotes;


import org.junit.Assert;
import org.junit.Test;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Customer;
import idas.chox.core.model.HireMonitoringDetail;


public class TotalLossVehicleCheckNoteTest {
    
    @Test
    public void testTotalLossVehicleCheckNotePass() {
        Claim c = new Claim();
        HireMonitoringDetail hireMonitoringDetail = new HireMonitoringDetail();
        hireMonitoringDetail.setIsNFInsurerManagingRepair(false);
        Customer customer = new Customer();
        customer.setIsTotalLoss(true);
        c.setHireMonitoringDetail(hireMonitoringDetail);
        c.setCustomer(customer);
        c.setManagingRepair(false);
        Assert.assertTrue((new TotalLossVehicleCheckNote()).isShowingFor(c));
    }

    @Test
    public void testTotalLossVehicleCheckNoteFail() {
        Claim c = new Claim();
        HireMonitoringDetail hireMonitoringDetail = new HireMonitoringDetail();
        Customer customer = new Customer();
        
        hireMonitoringDetail.setIsNFInsurerManagingRepair(false);
        customer.setIsTotalLoss(true);
        c.setHireMonitoringDetail(hireMonitoringDetail);
        c.setCustomer(customer);
        c.setManagingRepair(true);
        
        Assert.assertFalse((new TotalLossVehicleCheckNote()).isShowingFor(c));
    }
    
    @Test
    public void testTotalLossVehicleCheckNoteFail2() {
        Claim c = new Claim();
        HireMonitoringDetail hireMonitoringDetail = new HireMonitoringDetail();
        Customer customer = new Customer();
        
        hireMonitoringDetail.setIsNFInsurerManagingRepair(true);
        customer.setIsTotalLoss(true);
        c.setHireMonitoringDetail(hireMonitoringDetail);
        c.setCustomer(customer);
        c.setManagingRepair(false);
        
        Assert.assertFalse((new TotalLossVehicleCheckNote()).isShowingFor(c));
    }
    
    @Test
    public void testTotalLossVehicleCheckNoteFail3() {
        Claim c = new Claim();
        HireMonitoringDetail hireMonitoringDetail = new HireMonitoringDetail();
        Customer customer = new Customer();
        
        hireMonitoringDetail.setIsNFInsurerManagingRepair(false);
        customer.setIsTotalLoss(false);
        c.setHireMonitoringDetail(hireMonitoringDetail);
        c.setCustomer(customer);
        c.setManagingRepair(false);
        
        Assert.assertFalse((new TotalLossVehicleCheckNote()).isShowingFor(c));
    }
}
