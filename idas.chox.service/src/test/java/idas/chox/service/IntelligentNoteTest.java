package idas.chox.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import idas.chox.core.model.*;
import idas.chox.test.BaseTest;
import idas.chox.core.util.DateHelper;
import idas.chox.service.intelligentNotes.*;

public class IntelligentNoteTest extends BaseTest {

    @Autowired
    IntelligentNoteDisplayEngine displayEngine;

    
    /**
     * Test of getUserFromCache method, of class UserCacheManager.
     */
    @Test
    public void testClassInjection() {

        //make sure the intelligentNotes get injected from spring
        List<IntelligentNote> intelligentNotes = displayEngine.getAvailableIntelligentNotes();
        Assert.assertNotNull(intelligentNotes);
        Assert.assertFalse(intelligentNotes.isEmpty());
        Assert.assertEquals(15, intelligentNotes.size());
    }

    
    @Test
    public void testCanShowVehicleClassCheckNote() throws Exception {
        //prepere the IntelligentNoteDisplayEngine
        IntelligentNoteDisplayEngine testDisplayEngine = new IntelligentNoteDisplayEngine();

        List<IntelligentNote> intelligentNotes = new ArrayList<IntelligentNote>();
        intelligentNotes.add(new VehicleClassCheckNote());
        testDisplayEngine.setAvailableIntelligentNotes(intelligentNotes);

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        claim.setPolicyHolderContactDate(DateHelper.getCurrentDate());
        Customer cust = new Customer();
        VehicleClass vc = new VehicleClass();
        vc.setName("P1");
        cust.setVehicleClass(vc);
        cust.setInitialECD(DateHelper.addDay(DateHelper.getCurrentDate(), 4));
        claim.setCustomer(cust);


        //test note  showing 
        Assert.assertEquals(1, testDisplayEngine.getIntelligentNotes(claim).size());

        //test if initial ecd > 5 days from policy holder contact date, VehicleClassCheckNote should not be displayed
        claim.getCustomer().setInitialECD(DateHelper.addDay(DateHelper.getCurrentDate(), 6));
        Assert.assertEquals(0, testDisplayEngine.getIntelligentNotes(claim).size());

        //test if new ecd added and the latest ecd date < 5 days from policy holder contact date, VehicleClassCheckNote should not be displayed
        HireMonitoringEcd ecd = new HireMonitoringEcd();
        ecd.setEcdDate(DateHelper.addDay(DateHelper.getCurrentDate(), 1));

        claim.addHireMonitoringEcd(ecd);

        Assert.assertEquals(1, testDisplayEngine.getIntelligentNotes(claim).size());

        // Test if vehicle class is PV then note not displayed
        vc.setName("PV1");
        Assert.assertEquals(0, testDisplayEngine.getIntelligentNotes(claim).size());

        // Test if vehicle class is a sports vehicle then note is displayed
        vc.setName("SP1");
        Assert.assertEquals(1, testDisplayEngine.getIntelligentNotes(claim).size());
    }

    
    @Test
    public void testCanShowUnroadworthyVehicleCheckNote() throws Exception {
        //prepere the IntelligentNoteDisplayEngine
        IntelligentNoteDisplayEngine testDisplayEngine = new IntelligentNoteDisplayEngine();

        List<IntelligentNote> intelligentNotes = new ArrayList<IntelligentNote>();
        intelligentNotes.add(new UnroadworthyVehicleCheckNote());
        testDisplayEngine.setAvailableIntelligentNotes(intelligentNotes);

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        Customer cust = new Customer();

        //test note showing if customers car is not usable
        cust.setIsUsable(false);
        claim.setCustomer(cust);
        Assert.assertEquals(1, testDisplayEngine.getIntelligentNotes(claim).size());

        //test note are not showing customers car is usable
        claim.getCustomer().setIsUsable(true);
        Assert.assertEquals(0, testDisplayEngine.getIntelligentNotes(claim).size());
    }

    
    @Test
    public void testCanShowTotalLossVehicleCheckNote() throws Exception {
        //prepere the IntelligentNoteDisplayEngine
        IntelligentNoteDisplayEngine testDisplayEngine = new IntelligentNoteDisplayEngine();

        List<IntelligentNote> intelligentNotes = new ArrayList<IntelligentNote>();
        intelligentNotes.add(new TotalLossVehicleCheckNote());
        testDisplayEngine.setAvailableIntelligentNotes(intelligentNotes);


        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        Customer cust = new Customer();
        HireMonitoringDetail hmd = new HireMonitoringDetail();
        claim.setCustomer(cust);
        claim.setHireMonitoringDetail(hmd);

        claim.getCustomer().setIsTotalLoss(Boolean.TRUE);
        claim.setManagingRepair(false);
        claim.getHireMonitoringDetail().setIsNFInsurerManagingRepair(false);
        //test note is showing
        Assert.assertEquals(1, testDisplayEngine.getIntelligentNotes(claim).size());

        // Test Note not showing when not total loss
        claim.getCustomer().setIsTotalLoss(Boolean.FALSE);
        Assert.assertEquals(0, testDisplayEngine.getIntelligentNotes(claim).size());
        claim.getCustomer().setIsTotalLoss(Boolean.TRUE);

        // Test Note not showing when managing repair
        claim.setManagingRepair(true);
        Assert.assertEquals(0, testDisplayEngine.getIntelligentNotes(claim).size());
        claim.setManagingRepair(false);

        // Test Note not showing when 'Non-Fault Insurer Managing Repair?' field is true
        claim.getHireMonitoringDetail().setIsNFInsurerManagingRepair(true);
        Assert.assertEquals(0, testDisplayEngine.getIntelligentNotes(claim).size());
        claim.getHireMonitoringDetail().setIsNFInsurerManagingRepair(false);

    }

    
    @Test
    public void testCanShowCHOManagingRepairCheckNote() throws Exception {
        //prepere the IntelligentNoteDisplayEngine
        IntelligentNoteDisplayEngine testDisplayEngine = new IntelligentNoteDisplayEngine();

        List<IntelligentNote> intelligentNotes = new ArrayList<IntelligentNote>();
        intelligentNotes.add(new CHOManagingRepairCheckNote());
        testDisplayEngine.setAvailableIntelligentNotes(intelligentNotes);


        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        //test note not showing if CHO not managing repair
        claim.setManagingRepair(false);
        Assert.assertEquals(0, testDisplayEngine.getIntelligentNotes(claim).size());
        //test note  showing managing repair
        claim.setManagingRepair(true);
        Assert.assertEquals(1, testDisplayEngine.getIntelligentNotes(claim).size());

    }

    
    @Test
    public void testCanShowVehicleClassAboveSCheckNote() throws Exception {
        //prepere the IntelligentNoteDisplayEngine
        IntelligentNoteDisplayEngine testDisplayEngine = new IntelligentNoteDisplayEngine();

        List<IntelligentNote> intelligentNotes = new ArrayList<IntelligentNote>();
        intelligentNotes.add(new VehicleClassAboveSCheckNote());
        testDisplayEngine.setAvailableIntelligentNotes(intelligentNotes);

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        Customer cust = new Customer();
        VehicleClass vc = new VehicleClass();
        cust.setVehicleClass(vc);
        claim.setCustomer(cust);
        //test note not showing if vehicle class is S
        vc.setName("S1");
        Assert.assertEquals(0, testDisplayEngine.getIntelligentNotes(claim).size());
        vc.setName("S2");
        Assert.assertEquals(0, testDisplayEngine.getIntelligentNotes(claim).size());
        vc.setName("S3");
        Assert.assertEquals(0, testDisplayEngine.getIntelligentNotes(claim).size());
        vc.setName("S4");
        Assert.assertEquals(0, testDisplayEngine.getIntelligentNotes(claim).size());
        vc.setName("S5");
        Assert.assertEquals(0, testDisplayEngine.getIntelligentNotes(claim).size());

        //test note only showing if VehicleClass above S
        vc.setName("SP1");
        Assert.assertEquals(1, testDisplayEngine.getIntelligentNotes(claim).size());
        vc.setName("P1");
        Assert.assertEquals(1, testDisplayEngine.getIntelligentNotes(claim).size());
    }

    
    @Test
    public void testCanShowFrontalDamageCheckNote() throws Exception {
        //prepere the IntelligentNoteDisplayEngine
        IntelligentNoteDisplayEngine testDisplayEngine = new IntelligentNoteDisplayEngine();

        List<IntelligentNote> intelligentNotes = new ArrayList<IntelligentNote>();
        intelligentNotes.add(new FrontalDamageCheckNote());
        testDisplayEngine.setAvailableIntelligentNotes(intelligentNotes);


        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        Customer cust = new Customer();
        cust.setDamage("frontal damage");
        claim.setCustomer(cust);
        //test note not showing if customers car damage contains the string 'front'
        Assert.assertEquals(1, testDisplayEngine.getIntelligentNotes(claim).size());

        cust.setDamage("rear damage");
        claim.setCustomer(cust);
        //test note not showing if customers car damage contains the string 'front'
        Assert.assertEquals(0, testDisplayEngine.getIntelligentNotes(claim).size());

    }

    
    @Test
    public void testCanShowNeedForSPandPClassCheckWithoutECDNote() throws Exception {
        //prepere the IntelligentNoteDisplayEngine
        IntelligentNoteDisplayEngine testDisplayEngine = new IntelligentNoteDisplayEngine();

        List<IntelligentNote> intelligentNotes = new ArrayList<IntelligentNote>();
        intelligentNotes.add(new NeedForSPandPClassCheckWithoutECDNote());
        testDisplayEngine.setAvailableIntelligentNotes(intelligentNotes);


        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);

        /*
         * test note is showing only if
         *      - the ‘Is Usable’ field is true
         *      - vehicle class is a P or SP
         *      - Managing Repair?' field is 'N' (No)
         *      -'Non-Fault Insurer Managing Repair?' field is 'N' (No)
         *      - no ECD has been provided
         */

        Customer cust = new Customer();
        cust.setIsUsable(true);
        claim.setCustomer(cust);
        VehicleClass vc = new VehicleClass();
        vc.setName("P1");
        cust.setVehicleClass(vc);
        claim.setManagingRepair(false);
        HireMonitoringDetail hmd = new HireMonitoringDetail();
        hmd.setIsNFInsurerManagingRepair(false);
        claim.setHireMonitoringDetail(hmd);
        claim.getCustomer().setInitialECD(null);
        claim.setHireMonitoringEcds(null);

        Assert.assertEquals(1, testDisplayEngine.getIntelligentNotes(claim).size());

        vc.setName("SP1");
        Assert.assertEquals(1, testDisplayEngine.getIntelligentNotes(claim).size());

        vc.setName("PV1");
        Assert.assertEquals(0, testDisplayEngine.getIntelligentNotes(claim).size());

        vc.setName("SP1");
        claim.getCustomer().setIsUsable(false);
        Assert.assertEquals(0, testDisplayEngine.getIntelligentNotes(claim).size());
        claim.getCustomer().setIsUsable(true);

        claim.setManagingRepair(true);
        Assert.assertEquals(0, testDisplayEngine.getIntelligentNotes(claim).size());
        claim.setManagingRepair(false);

        claim.getHireMonitoringDetail().setIsNFInsurerManagingRepair(true);
        Assert.assertEquals(0, testDisplayEngine.getIntelligentNotes(claim).size());
        claim.getHireMonitoringDetail().setIsNFInsurerManagingRepair(false);

        claim.getCustomer().setInitialECD(new Date());
        Assert.assertEquals(0, testDisplayEngine.getIntelligentNotes(claim).size());
        claim.getCustomer().setInitialECD(null);

        ArrayList<HireMonitoringEcd> hm = new ArrayList<HireMonitoringEcd>();
        HireMonitoringEcd hmEcd = new HireMonitoringEcd();
        hmEcd.setClaim(claim);
        hmEcd.setEcdDate(new Date());
        hm.add(hmEcd);
        claim.setHireMonitoringEcds(hm);
        Assert.assertEquals(0, testDisplayEngine.getIntelligentNotes(claim).size());
        claim.setHireMonitoringEcds(new ArrayList<HireMonitoringEcd>());
    }

    
    @Test
    public void testCanShowHireCommenced48hSinceNotificationCheckNote() throws Exception {
        //prepere the IntelligentNoteDisplayEngine
        IntelligentNoteDisplayEngine testDisplayEngine = new IntelligentNoteDisplayEngine();

        List<IntelligentNote> intelligentNotes = new ArrayList<IntelligentNote>();
        intelligentNotes.add(new HireCommenced48hSinceNotificationCheck());
        testDisplayEngine.setAvailableIntelligentNotes(intelligentNotes);


        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        VehicleHire vh = new VehicleHire();
        vh.setHireStart(new Date());
        claim.setVehicleHire(vh);
        claim.setCreatedDate(DateHelper.addDay(DateHelper.getCurrentDate(), 3));
        //test note  showing if claim created date is more than 48h after hire start
        Assert.assertEquals(1, testDisplayEngine.getIntelligentNotes(claim).size());

        //test note not showing if claim created date is more than 48h after hire start
        claim.setCreatedDate(DateHelper.addDay(DateHelper.getCurrentDate(), 1));
        Assert.assertEquals(0, testDisplayEngine.getIntelligentNotes(claim).size());

    }


}
