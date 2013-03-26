
package idas.chox.service.intelligentNotes;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import idas.chox.core.model.*;
import idas.chox.core.util.DateHelper;


public class NeedForSPandPClassCheckWithoutECDNoteTest {
    
    @Test
    // ecd and hiremonitoring detail does not exis and vehicle class is p. should pass.
    public void testNeedForSPandPClassCheckWithoutECDNotePass() {
        Claim c = new Claim();
        
        Customer customer = new Customer();
        HireMonitoringDetail hireMonitoringDetail = new HireMonitoringDetail();
        VehicleClass vclass = new VehicleClass();
        
        vclass.setName("p");
        
        customer.setIsUsable(true);
        customer.setVehicleClass(vclass);
        
        hireMonitoringDetail.setIsNFInsurerManagingRepair(false);
       
        c.setCustomer(customer);
        c.setManagingRepair(false);
        c.setHireMonitoringDetail(hireMonitoringDetail);
        
        Assert.assertTrue((new NeedForSPandPClassCheckWithoutECDNote()).isShowingFor(c));
    }

    @Test
    // ecd and hiremonitoring detail exists. so should fail.
    public void testNeedForSPandPClassCheckWithoutECDNoteFail() {
        Claim c = new Claim();
        
        Customer customer = new Customer();
        HireMonitoringDetail hireMonitoringDetail = new HireMonitoringDetail();
        List<HireMonitoringEcd> ecds = new ArrayList<HireMonitoringEcd>();
        VehicleClass vclass = new VehicleClass();
        HireMonitoringEcd ecd = new HireMonitoringEcd();
        
        vclass.setName("p");
        
        customer.setInitialECD(DateHelper.parse("07/12/2012 05:00",DateHelper.getLocalDateTimeFormat()));
        customer.setIsUsable(true);
        customer.setVehicleClass(vclass);
        
        hireMonitoringDetail.setIsNFInsurerManagingRepair(false);
       
        ecds.add(ecd);
        
        c.setCustomer(customer);
        c.setHireMonitoringEcds(ecds);
        c.setManagingRepair(false);
        c.setHireMonitoringDetail(hireMonitoringDetail);
        
        Assert.assertFalse((new NeedForSPandPClassCheckWithoutECDNote()).isShowingFor(c));
    }
}
