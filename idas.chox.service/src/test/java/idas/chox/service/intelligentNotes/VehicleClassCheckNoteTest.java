
package idas.chox.service.intelligentNotes;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Customer;
import idas.chox.core.model.HireMonitoringEcd;
import idas.chox.core.model.VehicleClass;
import idas.chox.core.util.DateHelper;


public class VehicleClassCheckNoteTest {
  
     @Test
    // Vehicle class is p and ECD is less than 5 days from Policy Holder Contact Date
    public void testVehicleClassCheckNotePass() {
        Claim c = new Claim();
        HireMonitoringEcd ecd = new HireMonitoringEcd();
        List<HireMonitoringEcd> ecds = new ArrayList<HireMonitoringEcd>();
        Customer customer = new Customer();
        VehicleClass vclass = new VehicleClass();
        
        ecd.setEcdDate(DateHelper.parse("09/12/2012"));
        ecds.add(ecd);
        vclass.setName("p");
        
        c.setPolicyHolderContactDate(DateHelper.parse("07/12/2012"));
        c.setHireMonitoringEcds(ecds);
        customer.setVehicleClass(vclass);
        c.setCustomer(customer);
        
        Assert.assertTrue((new VehicleClassCheckNote()).isShowingFor(c));
    }

    @Test
    // Vehicle class is p and ECD is more than 5 days from Policy Holder Contact Date
    public void testVehicleClassCheckNoteFail() {
        Claim c = new Claim();
        HireMonitoringEcd ecd = new HireMonitoringEcd();
        List<HireMonitoringEcd> ecds = new ArrayList<HireMonitoringEcd>();
        Customer customer = new Customer();
        VehicleClass vclass = new VehicleClass();
        
        ecd.setEcdDate(DateHelper.parse("09/12/2012"));
        ecds.add(ecd);
        vclass.setName("p");
        
        c.setPolicyHolderContactDate(DateHelper.parse("01/12/2012"));
        c.setHireMonitoringEcds(ecds);
        customer.setVehicleClass(vclass);
        c.setCustomer(customer);
        
        Assert.assertFalse((new VehicleClassCheckNote()).isShowingFor(c));
    }
}
