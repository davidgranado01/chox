package idas.chox.service;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.HireMonitoringEcd;
import idas.chox.core.model.IntelligentNote;
import idas.chox.core.model.VehicleClass;
import idas.chox.core.services.UploadClaimXMLService;
import idas.chox.core.util.DateHelper;
import idas.chox.core.util.DocumentHelper;

import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.intelligentNotes.CHOManagingRepairCheckNote;
import idas.chox.service.intelligentNotes.FrontalDamageCheckNote;
import idas.chox.service.intelligentNotes.IntelligentNoteDisplayEngine;
import idas.chox.service.intelligentNotes.NeedForSPandPClassCheckWithoutECDNote;
import idas.chox.service.intelligentNotes.TotalLossVehicleCheckNote;
import idas.chox.service.intelligentNotes.UnroadworthyVehicleCheckNote;
import idas.chox.service.intelligentNotes.VehicleClassAboveSCheckNote;
import idas.chox.service.intelligentNotes.VehicleClassCheckNote;
import idas.chox.service.xml.readers.BordereauReader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;

@RunWith(SpringJUnit4ClassRunner.class)

@ContextConfiguration(locations = {"classpath:applicationContext-IntelligentNote-test.xml", "classpath:applicationContext-Workflow-test.xml", "classpath:applicationContext-test.xml", "classpath:applicationContext-services-test.xml", "classpath:applicationContext-XMLReader-test.xml", "classpath:applicationContext-BRE-test.xml"})
public class IntelligentNoteTest {

    @Autowired
    IntelligentNoteDisplayEngine displayEngine;
    @Autowired
    BordereauReader bordereauReader;
    @Autowired
    UploadClaimXMLService service;

    /**
     * Test of getUserFromCache method, of class UserCacheManager.
     */
    @Test
    public void testClassInjection() {

        Assert.assertNotNull(displayEngine);
        Assert.assertNotNull(bordereauReader);
        //make sure the security info provider get injected from spring
        Assert.assertNotNull(displayEngine.getSecurityInfoProvider());
        //make sure the intelligentNotes get injected from spring
        List<IntelligentNote> intelligentNotes = displayEngine.getAvailableIntelligentNotes();
        Assert.assertNotNull(intelligentNotes);
        Assert.assertFalse(intelligentNotes.isEmpty());
        Assert.assertEquals(13, intelligentNotes.size());
    }

    @Test
    @Transactional
    public void testCanShowVehicleClassCheckNote() throws Exception {
        //prepere the IntelligentNoteDisplayEngine
        IntelligentNoteDisplayEngine vehicleClassCheckNoteOnlyDisplayEngine = new IntelligentNoteDisplayEngine();

        List<IntelligentNote> intelligentNotes = new ArrayList<IntelligentNote>();
        intelligentNotes.add(new VehicleClassCheckNote());
        vehicleClassCheckNoteOnlyDisplayEngine.setAvailableIntelligentNotes(intelligentNotes);

        String path = "VehicleClassCheckNote_test.xml";
        List<ClaimResult> claimResults = loadClaimResults(path);

        for (ClaimResult cr : claimResults) {
            Assert.assertNotNull(cr);
            Claim claim = cr.getClaim();
            Assert.assertNotNull(claim);

            //test note not showing if claim status not in status
            //ClaimUnacknowledgedRouted, ClaimPending, ClaimRejectionContested, ClaimUpdatedByEngineer and ClaimReferredToEngineer
            Assert.assertEquals(0, vehicleClassCheckNoteOnlyDisplayEngine.getIntelligentNotes(claim).size());
            //test note not showing if claim status in correct status
            claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
            Assert.assertEquals(0, vehicleClassCheckNoteOnlyDisplayEngine.getIntelligentNotes(claim).size());
            claim.setStatus(ClaimStatus.CLAIM_PENDING);
            Assert.assertEquals(0, vehicleClassCheckNoteOnlyDisplayEngine.getIntelligentNotes(claim).size());
            claim.setStatus(ClaimStatus.CLAIM_REJECTION_CONTESTED);
            Assert.assertEquals(0, vehicleClassCheckNoteOnlyDisplayEngine.getIntelligentNotes(claim).size());
            claim.setStatus(ClaimStatus.CLAIM_REF_TO_ENG);
            Assert.assertEquals(0, vehicleClassCheckNoteOnlyDisplayEngine.getIntelligentNotes(claim).size());
            claim.setStatus(ClaimStatus.CLAIM_UPDATE_BY_ENG);
            Assert.assertEquals(0, vehicleClassCheckNoteOnlyDisplayEngine.getIntelligentNotes(claim).size());

            //test if initial ecd > 5 days from policy holder contact date, VehicleClassCheckNote should not be displayed
            claim.setPolicyHolderContactDate(DateHelper.getCurrentDate());
            claim.getCustomer().setInitialECD(DateHelper.addDay(DateHelper.getCurrentDate(), 6));

            Assert.assertEquals(0, vehicleClassCheckNoteOnlyDisplayEngine.getIntelligentNotes(claim).size());

            //test if new ecd added and the latest ecd date < 5 days from policy holder contact date, VehicleClassCheckNote should not be displayed
            HireMonitoringEcd ecd = new HireMonitoringEcd();
            ecd.setEcdDate(DateHelper.addDay(DateHelper.getCurrentDate(), 1));

            claim.addHireMonitoringEcd(ecd);

            Assert.assertEquals(0, vehicleClassCheckNoteOnlyDisplayEngine.getIntelligentNotes(claim).size());
        }
    }

    @Test
    @Transactional
    public void testCanShowUnroadworthyVehicleCheckNote() throws Exception {
        //prepere the IntelligentNoteDisplayEngine
        IntelligentNoteDisplayEngine displayEngine = new IntelligentNoteDisplayEngine();

        FakeSecurityInfoProvider securityInfoProvider = new FakeSecurityInfoProvider();
        securityInfoProvider.setIsINS(true);
        displayEngine.setSecurityInfoProvider(securityInfoProvider);

        List<IntelligentNote> intelligentNotes = new ArrayList<IntelligentNote>();
        intelligentNotes.add(new UnroadworthyVehicleCheckNote());
        displayEngine.setAvailableIntelligentNotes(intelligentNotes);

        String path = "UnroadworthyVehicleCheckNote_test.xml";
        List<ClaimResult> claimResults = loadClaimResults(path);

        for (ClaimResult cr : claimResults) {
            Assert.assertNotNull(cr);
            Claim claim = cr.getClaim();
            Assert.assertNotNull(claim);

            //test note not showing if claim status not in status
            //ClaimUnacknowledgedRouted, ClaimPending, ClaimRejectionContested, ClaimUpdatedByEngineer and ClaimReferredToEngineer
            Assert.assertEquals(0, displayEngine.getIntelligentNotes(claim).size());
            //test note not showing if claim status in correct status
            claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
            Assert.assertEquals(1, displayEngine.getIntelligentNotes(claim).size());
            claim.setStatus(ClaimStatus.CLAIM_PENDING);
            Assert.assertEquals(1, displayEngine.getIntelligentNotes(claim).size());
            claim.setStatus(ClaimStatus.CLAIM_REJECTION_CONTESTED);
            Assert.assertEquals(1, displayEngine.getIntelligentNotes(claim).size());
            claim.setStatus(ClaimStatus.CLAIM_REF_TO_ENG);
            Assert.assertEquals(1, displayEngine.getIntelligentNotes(claim).size());
            claim.setStatus(ClaimStatus.CLAIM_UPDATE_BY_ENG);
            Assert.assertEquals(1, displayEngine.getIntelligentNotes(claim).size());

            //test note are not showing if is usable is true
            claim.getCustomer().setIsUsable(true);
            Assert.assertEquals(0, displayEngine.getIntelligentNotes(claim).size());

            //test note not showing if user is not insurer
            securityInfoProvider.setIsINS(false);
            displayEngine.setSecurityInfoProvider(securityInfoProvider);
            Assert.assertEquals(0, displayEngine.getIntelligentNotes(claim).size());
        }
    }

    @Test
    @Transactional
    public void testCanShowTotalLossVehicleCheckNote() throws Exception {
        //prepere the IntelligentNoteDisplayEngine
        IntelligentNoteDisplayEngine displayEngine = new IntelligentNoteDisplayEngine();

        FakeSecurityInfoProvider securityInfoProvider = new FakeSecurityInfoProvider();
        securityInfoProvider.setIsINS(true);
        displayEngine.setSecurityInfoProvider(securityInfoProvider);

        List<IntelligentNote> intelligentNotes = new ArrayList<IntelligentNote>();
        intelligentNotes.add(new TotalLossVehicleCheckNote());
        displayEngine.setAvailableIntelligentNotes(intelligentNotes);

        String path = "TotalLossVehicleCheckNote_test.xml";
        List<ClaimResult> claimResults = loadClaimResults(path);

        for (ClaimResult cr : claimResults) {
            Assert.assertNotNull(cr);
            Claim claim = cr.getClaim();
            Assert.assertNotNull(claim);
            //test note not showing if claim status not in status
            //ClaimUnacknowledgedRouted, ClaimPending, ClaimRejectionContested, ClaimUpdatedByEngineer and ClaimReferredToEngineer
            Assert.assertEquals(0, displayEngine.getIntelligentNotes(claim).size());
            //test note not showing if claim status in correct status
//            claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
//            Assert.assertEquals(1, displayEngine.getIntelligentNotes(claim).size());
//            claim.setStatus(ClaimStatus.CLAIM_PENDING);
//            Assert.assertEquals(1, displayEngine.getIntelligentNotes(claim).size());
//            claim.setStatus(ClaimStatus.CLAIM_REJECTION_CONTESTED);
//            Assert.assertEquals(1, displayEngine.getIntelligentNotes(claim).size());
//            claim.setStatus(ClaimStatus.CLAIM_REF_TO_ENG);
//            Assert.assertEquals(1, displayEngine.getIntelligentNotes(claim).size());
//            claim.setStatus(ClaimStatus.CLAIM_UPDATE_BY_ENG);
//            Assert.assertEquals(1, displayEngine.getIntelligentNotes(claim).size());
//
//            //test note are not showing if is total loss false
//            claim.getCustomer().setIsTotalLoss(false);
//            Assert.assertEquals(0, displayEngine.getIntelligentNotes(claim).size());
//
//            //test note not showing if user is not insurer
//            securityInfoProvider.setIsINS(false);
//            displayEngine.setSecurityInfoProvider(securityInfoProvider);
//            Assert.assertEquals(0, displayEngine.getIntelligentNotes(claim).size());
        }
    }

    @Test
    @Transactional
    public void testCanShowCHOManagingRepairCheckNote() throws Exception {
        //prepere the IntelligentNoteDisplayEngine
        IntelligentNoteDisplayEngine displayEngine = new IntelligentNoteDisplayEngine();

        FakeSecurityInfoProvider securityInfoProvider = new FakeSecurityInfoProvider();
        securityInfoProvider.setIsINS(true);
        displayEngine.setSecurityInfoProvider(securityInfoProvider);

        List<IntelligentNote> intelligentNotes = new ArrayList<IntelligentNote>();
        intelligentNotes.add(new CHOManagingRepairCheckNote());
        displayEngine.setAvailableIntelligentNotes(intelligentNotes);

        String path = "CHOManagingRepairCheckNote_test.xml";
        List<ClaimResult> claimResults = loadClaimResults(path);

        for (ClaimResult cr : claimResults) {
            Assert.assertNotNull(cr);
            Claim claim = cr.getClaim();
            Assert.assertNotNull(claim);
            //test note not showing if claim status not in status
            //ClaimUnacknowledgedRouted, ClaimPending, ClaimRejectionContested, ClaimUpdatedByEngineer and ClaimReferredToEngineer
            Assert.assertEquals(0, displayEngine.getIntelligentNotes(claim).size());
            //test note not showing if claim status in correct status
            claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
            Assert.assertEquals(1, displayEngine.getIntelligentNotes(claim).size());
            claim.setStatus(ClaimStatus.CLAIM_PENDING);
            Assert.assertEquals(1, displayEngine.getIntelligentNotes(claim).size());
            claim.setStatus(ClaimStatus.CLAIM_REJECTION_CONTESTED);
            Assert.assertEquals(1, displayEngine.getIntelligentNotes(claim).size());
            claim.setStatus(ClaimStatus.CLAIM_REF_TO_ENG);
            Assert.assertEquals(1, displayEngine.getIntelligentNotes(claim).size());
            claim.setStatus(ClaimStatus.CLAIM_UPDATE_BY_ENG);
            Assert.assertEquals(1, displayEngine.getIntelligentNotes(claim).size());

            //test note are not showing if is managing repair false
            claim.setManagingRepair(false);
            Assert.assertEquals(0, displayEngine.getIntelligentNotes(claim).size());

            //test note not showing if user is not insurer
            securityInfoProvider.setIsINS(false);
            displayEngine.setSecurityInfoProvider(securityInfoProvider);
            Assert.assertEquals(0, displayEngine.getIntelligentNotes(claim).size());
        }
    }

    @Test
    @Transactional
    public void testCanShowVehicleClassAboveSCheckNote() throws Exception {
        //prepere the IntelligentNoteDisplayEngine
        IntelligentNoteDisplayEngine displayEngine = new IntelligentNoteDisplayEngine();

        FakeSecurityInfoProvider securityInfoProvider = new FakeSecurityInfoProvider();
        securityInfoProvider.setIsINS(true);
        displayEngine.setSecurityInfoProvider(securityInfoProvider);

        List<IntelligentNote> intelligentNotes = new ArrayList<IntelligentNote>();
        intelligentNotes.add(new VehicleClassAboveSCheckNote());
        displayEngine.setAvailableIntelligentNotes(intelligentNotes);

        String path = "VehicleClassAboveSCheckNote_test.xml";
        List<ClaimResult> claimResults = loadClaimResults(path);

        for (ClaimResult cr : claimResults) {
            Assert.assertNotNull(cr);
            Claim claim = cr.getClaim();
            Assert.assertNotNull(claim);
            //test note not showing if claim status not in status
            //ClaimUnacknowledgedRouted, ClaimPending, ClaimRejectionContested, ClaimUpdatedByEngineer and ClaimReferredToEngineer
            Assert.assertEquals(0, displayEngine.getIntelligentNotes(claim).size());
            //test note not showing if claim status in correct status
//            claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
//            Assert.assertEquals(1, displayEngine.getIntelligentNotes(claim).size());
//            claim.setStatus(ClaimStatus.CLAIM_PENDING);
//            Assert.assertEquals(1, displayEngine.getIntelligentNotes(claim).size());
//            claim.setStatus(ClaimStatus.CLAIM_REJECTION_CONTESTED);
//            Assert.assertEquals(1, displayEngine.getIntelligentNotes(claim).size());
//            claim.setStatus(ClaimStatus.CLAIM_REF_TO_ENG);
//            Assert.assertEquals(1, displayEngine.getIntelligentNotes(claim).size());
//            claim.setStatus(ClaimStatus.CLAIM_UPDATE_BY_ENG);
//            Assert.assertEquals(1, displayEngine.getIntelligentNotes(claim).size());
//
//            //test note only showing if VehicleClass ablove S
//            VehicleClass vClass = new VehicleClass();
//            vClass.setName("S1");
//            claim.getCustomer().setVehicleClass(vClass);
//            Assert.assertEquals(0, displayEngine.getIntelligentNotes(claim).size());
//
//            //test note not showing if user is not insurer
//            securityInfoProvider.setIsINS(false);
//            displayEngine.setSecurityInfoProvider(securityInfoProvider);
//            Assert.assertEquals(0, displayEngine.getIntelligentNotes(claim).size());
        }
    }

    @Test
    @Transactional
    public void testCanShowFrontalDamageCheckNote() throws Exception {
        //prepere the IntelligentNoteDisplayEngine
        IntelligentNoteDisplayEngine displayEngine = new IntelligentNoteDisplayEngine();

        FakeSecurityInfoProvider securityInfoProvider = new FakeSecurityInfoProvider();
        securityInfoProvider.setIsINS(true);
        displayEngine.setSecurityInfoProvider(securityInfoProvider);

        List<IntelligentNote> intelligentNotes = new ArrayList<IntelligentNote>();
        intelligentNotes.add(new FrontalDamageCheckNote());
        displayEngine.setAvailableIntelligentNotes(intelligentNotes);

        String path = "CHOManagingRepairCheckNote_test.xml";
        List<ClaimResult> claimResults = loadClaimResults(path);

        for (ClaimResult cr : claimResults) {
            Assert.assertNotNull(cr);
            Claim claim = cr.getClaim();
            Assert.assertNotNull(claim);
            //test note not showing if claim status not in status
            //ClaimUnacknowledgedRouted, ClaimPending, ClaimRejectionContested, ClaimUpdatedByEngineer and ClaimReferredToEngineer
            Assert.assertEquals(0, displayEngine.getIntelligentNotes(claim).size());
            //test note not showing if claim status in correct status
//            claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
//            Assert.assertEquals(1, displayEngine.getIntelligentNotes(claim).size());
//            claim.setStatus(ClaimStatus.CLAIM_PENDING);
//            Assert.assertEquals(1, displayEngine.getIntelligentNotes(claim).size());
//            claim.setStatus(ClaimStatus.CLAIM_REJECTION_CONTESTED);
//            Assert.assertEquals(1, displayEngine.getIntelligentNotes(claim).size());
//            claim.setStatus(ClaimStatus.CLAIM_REF_TO_ENG);
//            Assert.assertEquals(1, displayEngine.getIntelligentNotes(claim).size());
//            claim.setStatus(ClaimStatus.CLAIM_UPDATE_BY_ENG);
//            Assert.assertEquals(1, displayEngine.getIntelligentNotes(claim).size());
//
//            //test note are showing only If the ‘Vehicle Damage’ field has the text string ‘front’
//            claim.getCustomer().setDamage("ABCDEFG");
//            Assert.assertEquals(0, displayEngine.getIntelligentNotes(claim).size());
//
//            //test note not showing if user is not insurer
//            securityInfoProvider.setIsINS(false);
//            displayEngine.setSecurityInfoProvider(securityInfoProvider);
//            Assert.assertEquals(0, displayEngine.getIntelligentNotes(claim).size());
        }
    }

    @Test
    @Transactional
    public void testCanShowNeedForSPandPClassCheckWithoutECDNote() throws Exception {
        //prepere the IntelligentNoteDisplayEngine
        IntelligentNoteDisplayEngine displayEngine = new IntelligentNoteDisplayEngine();

        FakeSecurityInfoProvider securityInfoProvider = new FakeSecurityInfoProvider();
        securityInfoProvider.setIsINS(true);
        displayEngine.setSecurityInfoProvider(securityInfoProvider);

        List<IntelligentNote> intelligentNotes = new ArrayList<IntelligentNote>();
        intelligentNotes.add(new NeedForSPandPClassCheckWithoutECDNote());
        displayEngine.setAvailableIntelligentNotes(intelligentNotes);

        String path = "NeedForSPandPClassCheckWithoutECDNote_test.xml";
        List<ClaimResult> claimResults = loadClaimResults(path);

        for (ClaimResult cr : claimResults) {
            Assert.assertNotNull(cr);
            Claim claim = cr.getClaim();
            Assert.assertNotNull(claim);
            //test note not showing if claim status not in status
            //ClaimUnacknowledgedRouted, ClaimPending, ClaimRejectionContested, ClaimUpdatedByEngineer and ClaimReferredToEngineer
            Assert.assertEquals(0, displayEngine.getIntelligentNotes(claim).size());
            //test note not showing if claim status in correct status
//            claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
//            Assert.assertEquals(1, displayEngine.getIntelligentNotes(claim).size());
//            claim.setStatus(ClaimStatus.CLAIM_PENDING);
//            Assert.assertEquals(1, displayEngine.getIntelligentNotes(claim).size());
//            claim.setStatus(ClaimStatus.CLAIM_REJECTION_CONTESTED);
//            Assert.assertEquals(1, displayEngine.getIntelligentNotes(claim).size());
//            claim.setStatus(ClaimStatus.CLAIM_REF_TO_ENG);
//            Assert.assertEquals(1, displayEngine.getIntelligentNotes(claim).size());
//            claim.setStatus(ClaimStatus.CLAIM_UPDATE_BY_ENG);
//            Assert.assertEquals(1, displayEngine.getIntelligentNotes(claim).size());
//
//            //test note is showing only If the ‘Is Usable’ field is true
//            claim.getCustomer().setIsUsable(false);
//            Assert.assertEquals(0, displayEngine.getIntelligentNotes(claim).size());
//
//            claim.getCustomer().setIsUsable(true);//restore ‘Is Usable’ field
//            //test note is showing only If no ECD has been provided
//            List<HireMonitoringEcd> hireMonitoringEcds = new ArrayList<HireMonitoringEcd>();
//            hireMonitoringEcds.add(new HireMonitoringEcd());
//            claim.setHireMonitoringEcds(hireMonitoringEcds);
//            Assert.assertEquals(0, displayEngine.getIntelligentNotes(claim).size());
//            claim.getHireMonitoringEcds().clear();//restore ‘HireMonitoringEcds’ field
//
//            //test note is showing to use in any roles
//            securityInfoProvider.setIsINS(false);
//            displayEngine.setSecurityInfoProvider(securityInfoProvider);
//            Assert.assertEquals(1, displayEngine.getIntelligentNotes(claim).size());
        }
    }

    private List<ClaimResult> loadClaimResults(String path) throws Exception {


        File file = new ClassPathResource(path).getFile();
        int totalProcessed = 0;
        List<ClaimResult> claimResults = null;
        List<String> choReferences = new ArrayList<String>();
        Document document = DocumentHelper.getDocumentFromFile(file);
        claimResults = this.service.formClaimResults(document);
        for (ClaimResult claimResult : claimResults) {
            if (this.service.doProcessBordereauResult(claimResult, choReferences)) {

                totalProcessed++;

            }
        }
        return claimResults;

    }

}
