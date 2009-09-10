/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.model;

import chox.Util.DateHelper;
import chox.data.FakeSecurityInfoProvider;
import chox.model.intelligentNotes.*;
import chox.services.UploadClaimXMLService;
import chox.xmlValidation.model.BordereauResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author emmanuel
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext-IntelligentNote.xml","classpath:applicationContext.xml"})
public class IntelligentNoteTest {

    @Autowired
    IntelligentNoteDisplayEngine displayEngine;

    @Autowired
    UploadClaimXMLService uploadClaimXMLService;

    /**
     * Test of getUserFromCache method, of class UserCacheManager.
     */
    @Test
    public void testClassInjection() {       

        Assert.assertNotNull(displayEngine);
        //make sure the security info provider get injected from spring
        Assert.assertNotNull(displayEngine.getSecurityInfoProvider());
        //make sure the intelligentNotes get injected from spring
        List<IntelligentNote> intelligentNotes = displayEngine.getAvailableIntelligentNotes();
        Assert.assertNotNull(intelligentNotes);
        Assert.assertFalse(intelligentNotes.isEmpty());
    }

    @Test
    @Transactional
    public void testCanShowVehicleClassCheckNote() throws IOException {
        //prepere the IntelligentNoteDisplayEngine
        IntelligentNoteDisplayEngine vehicleClassCheckNoteOnlyDisplayEngine = new IntelligentNoteDisplayEngine();
     
        List<IntelligentNote> intelligentNotes = new ArrayList<IntelligentNote>();
        intelligentNotes.add(new VehicleClassCheckNote());
        vehicleClassCheckNoteOnlyDisplayEngine.setAvailableIntelligentNotes(intelligentNotes);

        String path = "/chox/testFile/intelligentNotes/VehicleClassCheckNote_test.xml";
        Claim claim = getSampleClaim(path);
        //test note not showing if claim status not in status
        //ClaimUnacknowledgedRouted, ClaimPending, ClaimRejectionContested, ClaimUpdatedByEngineer and ClaimReferredToEngineer
        Assert.assertEquals(0, vehicleClassCheckNoteOnlyDisplayEngine.getIntelligentNotes(claim).size());
        //test note not showing if claim status in correct status
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        Assert.assertEquals(1, vehicleClassCheckNoteOnlyDisplayEngine.getIntelligentNotes(claim).size());
        claim.setStatus(ClaimStatus.CLAIM_PENDING);
        Assert.assertEquals(1, vehicleClassCheckNoteOnlyDisplayEngine.getIntelligentNotes(claim).size());
        claim.setStatus(ClaimStatus.CLAIM_REJECTION_CONTESTED);
        Assert.assertEquals(1, vehicleClassCheckNoteOnlyDisplayEngine.getIntelligentNotes(claim).size());
        claim.setStatus(ClaimStatus.CLAIM_REF_TO_ENG);
        Assert.assertEquals(1, vehicleClassCheckNoteOnlyDisplayEngine.getIntelligentNotes(claim).size());
        claim.setStatus(ClaimStatus.CLAIM_UPDATE_BY_ENG);
        Assert.assertEquals(1, vehicleClassCheckNoteOnlyDisplayEngine.getIntelligentNotes(claim).size());

        //test if initial ecd > 5 days from policy holder contact date, VehicleClassCheckNote should not be displayed
        claim.setPolicyHolderContactDate(DateHelper.getCurrentDate());
        claim.setHireMonitoringEcd(DateHelper.addDay(DateHelper.getCurrentDate(), 6));

        Assert.assertEquals(0, vehicleClassCheckNoteOnlyDisplayEngine.getIntelligentNotes(claim).size());

        //test if new ecd added and the latest ecd date < 5 days from policy holder contact date, VehicleClassCheckNote should not be displayed
        HireMonitoringEcd ecd = new HireMonitoringEcd();
        ecd.setEcdDate(DateHelper.addDay(DateHelper.getCurrentDate(), 1));

        claim.addHireMonitoringEcd(ecd);

        Assert.assertEquals(1, vehicleClassCheckNoteOnlyDisplayEngine.getIntelligentNotes(claim).size());
    }
    
    @Test
    @Transactional
    public void testCanShowUnroadworthyVehicleCheckNote() throws IOException {
        //prepere the IntelligentNoteDisplayEngine
        IntelligentNoteDisplayEngine displayEngine = new IntelligentNoteDisplayEngine();

        FakeSecurityInfoProvider securityInfoProvider = new FakeSecurityInfoProvider();
        securityInfoProvider.setIsINS(true);
        displayEngine.setSecurityInfoProvider(securityInfoProvider);
     
        List<IntelligentNote> intelligentNotes = new ArrayList<IntelligentNote>();
        intelligentNotes.add(new UnroadworthyVehicleCheckNote());
        displayEngine.setAvailableIntelligentNotes(intelligentNotes);

        String path = "/chox/testFile/intelligentNotes/UnroadworthyVehicleCheckNote_test.xml";
        Claim claim = getSampleClaim(path);
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

    //make sure all sample claim loaded from xml must able to show intelligentNote
    private Claim getSampleClaim(String path) throws IOException    {
        
        File file = new ClassPathResource(path).getFile();
        BordereauResult bordereauResult = uploadClaimXMLService.processBordereau(file, file.getName());
        return bordereauResult.getClaimResult().get(0).getClaim();
    }
}
