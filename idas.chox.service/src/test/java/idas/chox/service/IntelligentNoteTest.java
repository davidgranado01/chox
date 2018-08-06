package idas.chox.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import idas.chox.core.model.*;
import idas.chox.test.BaseTest;
import idas.chox.core.util.DateHelper;
import idas.chox.service.intelligentNotes.*;

public class IntelligentNoteTest extends BaseTest {

    /**
     * Test of getUserFromCache method, of class UserCacheManager.
     */
    @Test
    public void testClassInjection() {

        //make sure the intelligentNotes get injected from spring
        List<IntelligentNote> intelligentNotes = intelligentNoteDisplayEngine.getAvailableIntelligentNotes();
        Assert.assertNotNull(intelligentNotes);
        Assert.assertFalse(intelligentNotes.isEmpty());
        Assert.assertEquals(15, intelligentNotes.size());
    }

    
    @Test
    public void testCanShowVehicleClassCheckNote() throws Exception {
        List<IntelligentNote> intelligentNotes = new ArrayList<>();
        List<IntelligentNote> intelligentNotesOriginal = intelligentNoteDisplayEngine.getAvailableIntelligentNotes();
        intelligentNotes.add(new VehicleClassCheckNote());
        intelligentNoteDisplayEngine.setAvailableIntelligentNotes(intelligentNotes);

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        claim.setPolicyHolderContactDate(DateHelper.getCurrentDate());
        Customer cust = new Customer();
        VehicleClass vc = new VehicleClass();
        vc.setName("P1");
        cust.setVehicleClass(vc);
        cust.setInitialECD(DateHelper.addDay(DateHelper.getCurrentDate(), 4));
        claim.setCustomer(cust);
        claim.setInsurer(insurerService.getInsurer(3));

        //test note  showing 
        Assert.assertEquals(1, intelligentNoteDisplayEngine.getIntelligentNotes(claim).size());

        //test if initial ecd > 5 days from policy holder contact date, VehicleClassCheckNote should not be displayed
        claim.getCustomer().setInitialECD(DateHelper.addDay(DateHelper.getCurrentDate(), 6));
        Assert.assertEquals(0, intelligentNoteDisplayEngine.getIntelligentNotes(claim).size());

        //test if new ecd added and the latest ecd date < 5 days from policy holder contact date, VehicleClassCheckNote should not be displayed
        HireMonitoringEcd ecd = new HireMonitoringEcd();
        ecd.setEcdDate(DateHelper.addDay(DateHelper.getCurrentDate(), 1));

        claim.addHireMonitoringEcd(ecd);

        Assert.assertEquals(1, intelligentNoteDisplayEngine.getIntelligentNotes(claim).size());

        // Test if vehicle class is PV then note not displayed
        vc.setName("PV1");
        Assert.assertEquals(0, intelligentNoteDisplayEngine.getIntelligentNotes(claim).size());

        // Test if vehicle class is a sports vehicle then note is displayed
        vc.setName("SP1");
        Assert.assertEquals(1, intelligentNoteDisplayEngine.getIntelligentNotes(claim).size());
        intelligentNoteDisplayEngine.setAvailableIntelligentNotes(intelligentNotesOriginal);
    }

    
    @Test
    public void testCanShowUnroadworthyVehicleCheckNote() throws Exception {
        List<IntelligentNote> intelligentNotes = new ArrayList<>();
        List<IntelligentNote> intelligentNotesOriginal = intelligentNoteDisplayEngine.getAvailableIntelligentNotes();
        intelligentNotes.add(new UnroadworthyVehicleCheckNote());
        intelligentNoteDisplayEngine.setAvailableIntelligentNotes(intelligentNotes);

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        Customer cust = new Customer();

        //test note showing if customers car is not usable
        cust.setIsUsable(false);
        claim.setCustomer(cust);
        claim.setInsurer(insurerService.getInsurer(3));
        Assert.assertEquals(1, intelligentNoteDisplayEngine.getIntelligentNotes(claim).size());

        //test note are not showing customers car is usable
        claim.getCustomer().setIsUsable(true);
        Assert.assertEquals(0, intelligentNoteDisplayEngine.getIntelligentNotes(claim).size());
        intelligentNoteDisplayEngine.setAvailableIntelligentNotes(intelligentNotesOriginal);
    }

    
    @Test
    public void testCanShowTotalLossVehicleCheckNote() throws Exception {
        List<IntelligentNote> intelligentNotes = new ArrayList<>();
        List<IntelligentNote> intelligentNotesOriginal = intelligentNoteDisplayEngine.getAvailableIntelligentNotes();
        intelligentNotes.add(new TotalLossVehicleCheckNote());
        intelligentNoteDisplayEngine.setAvailableIntelligentNotes(intelligentNotes);


        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        Customer cust = new Customer();
        HireMonitoringDetail hmd = new HireMonitoringDetail();
        claim.setCustomer(cust);
        claim.setHireMonitoringDetail(hmd);

        claim.getCustomer().setIsTotalLoss(Boolean.TRUE);
        claim.setManagingRepair(false);
        claim.getHireMonitoringDetail().setIsNFInsurerManagingRepair(false);
        claim.setInsurer(insurerService.getInsurer(3));
        //test note is showing
        Assert.assertEquals(1, intelligentNoteDisplayEngine.getIntelligentNotes(claim).size());

        // Test Note not showing when not total loss
        claim.getCustomer().setIsTotalLoss(Boolean.FALSE);
        Assert.assertEquals(0, intelligentNoteDisplayEngine.getIntelligentNotes(claim).size());
        claim.getCustomer().setIsTotalLoss(Boolean.TRUE);

        // Test Note not showing when managing repair
        claim.setManagingRepair(true);
        Assert.assertEquals(0, intelligentNoteDisplayEngine.getIntelligentNotes(claim).size());
        claim.setManagingRepair(false);

        // Test Note not showing when 'Non-Fault Insurer Managing Repair?' field is true
        claim.getHireMonitoringDetail().setIsNFInsurerManagingRepair(true);
        Assert.assertEquals(0, intelligentNoteDisplayEngine.getIntelligentNotes(claim).size());
        claim.getHireMonitoringDetail().setIsNFInsurerManagingRepair(false);
        intelligentNoteDisplayEngine.setAvailableIntelligentNotes(intelligentNotesOriginal);

    }

    
    @Test
    public void testCanShowCHOManagingRepairCheckNote() throws Exception {
        List<IntelligentNote> intelligentNotes = new ArrayList<>();
        List<IntelligentNote> intelligentNotesOriginal = intelligentNoteDisplayEngine.getAvailableIntelligentNotes();
        intelligentNotes.add(new CHOManagingRepairCheckNote());
        intelligentNoteDisplayEngine.setAvailableIntelligentNotes(intelligentNotes);


        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        //test note not showing if CHO not managing repair
        claim.setManagingRepair(false);
        claim.setInsurer(insurerService.getInsurer(3));
        Assert.assertEquals(0, intelligentNoteDisplayEngine.getIntelligentNotes(claim).size());
        //test note  showing managing repair
        claim.setManagingRepair(true);
        Assert.assertEquals(1, intelligentNoteDisplayEngine.getIntelligentNotes(claim).size());
        intelligentNoteDisplayEngine.setAvailableIntelligentNotes(intelligentNotesOriginal);

    }

    
    @Test
    public void testCanShowVehicleClassAboveSCheckNote() throws Exception {
        List<IntelligentNote> intelligentNotes = new ArrayList<>();
        List<IntelligentNote> intelligentNotesOriginal = intelligentNoteDisplayEngine.getAvailableIntelligentNotes();
        intelligentNotes.add(new VehicleClassAboveSCheckNote());
        intelligentNoteDisplayEngine.setAvailableIntelligentNotes(intelligentNotes);

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        Customer cust = new Customer();
        VehicleClass vc = new VehicleClass();
        cust.setVehicleClass(vc);
        claim.setCustomer(cust);
        claim.setInsurer(insurerService.getInsurer(3));
        //test note not showing if vehicle class is S
        vc.setName("S1");
        Assert.assertEquals(0, intelligentNoteDisplayEngine.getIntelligentNotes(claim).size());
        vc.setName("S2");
        Assert.assertEquals(0, intelligentNoteDisplayEngine.getIntelligentNotes(claim).size());
        vc.setName("S3");
        Assert.assertEquals(0, intelligentNoteDisplayEngine.getIntelligentNotes(claim).size());
        vc.setName("S4");
        Assert.assertEquals(0, intelligentNoteDisplayEngine.getIntelligentNotes(claim).size());
        vc.setName("S5");
        Assert.assertEquals(0, intelligentNoteDisplayEngine.getIntelligentNotes(claim).size());

        //test note only showing if VehicleClass above S
        vc.setName("SP1");
        Assert.assertEquals(1, intelligentNoteDisplayEngine.getIntelligentNotes(claim).size());
        vc.setName("P1");
        Assert.assertEquals(1, intelligentNoteDisplayEngine.getIntelligentNotes(claim).size());
        intelligentNoteDisplayEngine.setAvailableIntelligentNotes(intelligentNotesOriginal);
    }

    
    @Test
    public void testCanShowFrontalDamageCheckNote() throws Exception {
        List<IntelligentNote> intelligentNotes = new ArrayList<>();
        List<IntelligentNote> intelligentNotesOriginal = intelligentNoteDisplayEngine.getAvailableIntelligentNotes();
        intelligentNotes.add(new FrontalDamageCheckNote());
        intelligentNoteDisplayEngine.setAvailableIntelligentNotes(intelligentNotes);


        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        Customer cust = new Customer();
        cust.setDamage("frontal damage");
        claim.setCustomer(cust);
        claim.setInsurer(insurerService.getInsurer(3));
        //test note not showing if customers car damage contains the string 'front'
        Assert.assertEquals(1, intelligentNoteDisplayEngine.getIntelligentNotes(claim).size());

        cust.setDamage("rear damage");
        claim.setCustomer(cust);
        claim.setInsurer(insurerService.getInsurer(3));
        //test note not showing if customers car damage contains the string 'front'
        Assert.assertEquals(0, intelligentNoteDisplayEngine.getIntelligentNotes(claim).size());
        intelligentNoteDisplayEngine.setAvailableIntelligentNotes(intelligentNotesOriginal);

    }

    
    @Test
    public void testCanShowNeedForSPandPClassCheckWithoutECDNote() throws Exception {
        List<IntelligentNote> intelligentNotes = new ArrayList<>();
        List<IntelligentNote> intelligentNotesOriginal = intelligentNoteDisplayEngine.getAvailableIntelligentNotes();
        intelligentNotes.add(new NeedForSPandPClassCheckWithoutECDNote());
        intelligentNoteDisplayEngine.setAvailableIntelligentNotes(intelligentNotes);


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
        claim.setInsurer(insurerService.getInsurer(3));
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

        Assert.assertEquals(1, intelligentNoteDisplayEngine.getIntelligentNotes(claim).size());

        vc.setName("SP1");
        Assert.assertEquals(1, intelligentNoteDisplayEngine.getIntelligentNotes(claim).size());

        vc.setName("PV1");
        Assert.assertEquals(0, intelligentNoteDisplayEngine.getIntelligentNotes(claim).size());

        vc.setName("SP1");
        claim.getCustomer().setIsUsable(false);
        Assert.assertEquals(0, intelligentNoteDisplayEngine.getIntelligentNotes(claim).size());
        claim.getCustomer().setIsUsable(true);

        claim.setManagingRepair(true);
        Assert.assertEquals(0, intelligentNoteDisplayEngine.getIntelligentNotes(claim).size());
        claim.setManagingRepair(false);

        claim.getHireMonitoringDetail().setIsNFInsurerManagingRepair(true);
        Assert.assertEquals(0, intelligentNoteDisplayEngine.getIntelligentNotes(claim).size());
        claim.getHireMonitoringDetail().setIsNFInsurerManagingRepair(false);

        claim.getCustomer().setInitialECD(new Date());
        Assert.assertEquals(0, intelligentNoteDisplayEngine.getIntelligentNotes(claim).size());
        claim.getCustomer().setInitialECD(null);

        ArrayList<HireMonitoringEcd> hm = new ArrayList<>();
        HireMonitoringEcd hmEcd = new HireMonitoringEcd();
        hmEcd.setClaim(claim);
        hmEcd.setEcdDate(new Date());
        hm.add(hmEcd);
        claim.setHireMonitoringEcds(hm);
        Assert.assertEquals(0, intelligentNoteDisplayEngine.getIntelligentNotes(claim).size());
        claim.setHireMonitoringEcds(new ArrayList<HireMonitoringEcd>());
        intelligentNoteDisplayEngine.setAvailableIntelligentNotes(intelligentNotesOriginal);
    }

    
    @Test
    public void testCanShowHireCommenced48hSinceNotificationCheckNote() throws Exception {
        List<IntelligentNote> intelligentNotes = new ArrayList<>();
        List<IntelligentNote> intelligentNotesOriginal = intelligentNoteDisplayEngine.getAvailableIntelligentNotes();
        intelligentNotes.add(new HireCommenced48hSinceNotificationCheck());
        intelligentNoteDisplayEngine.setAvailableIntelligentNotes(intelligentNotes);


        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        claim.setInsurer(insurerService.getInsurer(3));
        VehicleHire vh = new VehicleHire();
        vh.setHireStart(new Date());
        claim.setVehicleHire(vh);
        claim.setCreatedDate(DateHelper.addDay(DateHelper.getCurrentDate(), 3));
        //test note  showing if claim created date is more than 48h after hire start
        Assert.assertEquals(1, intelligentNoteDisplayEngine.getIntelligentNotes(claim).size());

        //test note not showing if claim created date is more than 48h after hire start
        claim.setCreatedDate(DateHelper.addDay(DateHelper.getCurrentDate(), 1));
        Assert.assertEquals(0, intelligentNoteDisplayEngine.getIntelligentNotes(claim).size());
        intelligentNoteDisplayEngine.setAvailableIntelligentNotes(intelligentNotesOriginal);

    }


}
