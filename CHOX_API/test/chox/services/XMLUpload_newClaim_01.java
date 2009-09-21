package chox.services;

import chox.model.*;
import chox.xmlValidation.model.BordereauResult;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml","classpath:applicationContext-services.xml"})
public class XMLUpload_newClaim_01 {

    @Autowired
    private UploadClaimXMLService service;
    @Autowired
    private BordereauService bordereauService;
    @Autowired
    private ClaimService claimService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private VehicleClassService vehicleClassService;
    @Autowired
    private EngineerReportService engineerReportService;
    @Autowired
    private VehicleHireService vehicleHireService;
    private String testFilePath = "/chox/testFile/";

    /*
    @Test
    public void testFile_1_Error() {
    
    String fileName = "UnitTest-NewClaim_01.xml";
    try
    {

    // 1. DELETE OBJECT
    bordereauService.deleteObject(fileName);

    // 2. PROCESS THE XML
    File testFile = new File(testFilePath + fileName);
    BordereauResult parseResult = service.processClaimXMLFile(testFile, fileName);

    // 3. CHECK XML RESULT
    Assert.assertEquals(true, parseResult.isValid());
    Assert.assertEquals(0, parseResult.getMessage().size());
    Assert.assertEquals(1, parseResult.getClaimResult().size());

    // 4. CHECK XML CLAIM RESULT
    ClaimResult claimResult = parseResult.getClaimResult().get(0);
    Assert.assertEquals(false, claimResult.isValid());
    Assert.assertEquals(false, claimResult.isDataValid());
    Assert.assertEquals(4, claimResult.getMessage().size());
    Assert.assertEquals("Invalid or incorrect character in 'Managing repair' for 'Claim Header'.".toLowerCase(), claimResult.getMessage().get(0).toLowerCase());
    Assert.assertEquals("Invalid or incorrect character in 'Customer's Vehicle Registration' for 'Customer Detail'.".toLowerCase(), claimResult.getMessage().get(1).toLowerCase());
    Assert.assertEquals("No 'Third Party's Driver First Name' information supplied for 'Third Party Details'. Please re-submit with this information.".toLowerCase(), claimResult.getMessage().get(2).toLowerCase());
    Assert.assertEquals("Invalid or incorrect character in 'Number Days Hire' for 'Vehicle Hire Details'.".toLowerCase(), claimResult.getMessage().get(3).toLowerCase());

    // 5. CHECK BORDEREAU RESULT
    Bordereau bordereau = null;
    bordereau = bordereauService.getObject(fileName);
    Assert.assertEquals(fileName, bordereau.getFileName());
    Assert.assertEquals("allRejected", bordereau.getStatus());

    }catch(Exception ex){
    }
    }
     */
    @Test
    @Transactional
    public void testFile_2_Successful() throws IOException {

        String fileName = "UnitTest-NewClaim_02.xml";
       
        // 2. PROCESS THE XML
        File testFile = new ClassPathResource(testFilePath + fileName).getFile();
        BordereauResult parseResult = service.processClaimXMLFile(testFile, fileName);

        // 3. CHECK XML RESULT
        Assert.assertEquals(true, parseResult.isValid());
        Assert.assertEquals(0, parseResult.getMessage().size());
        Assert.assertEquals(1, parseResult.getClaimResult().size());

        // 4. CHECK XML CLAIM RESULT
        ClaimResult claimResult = parseResult.getClaimResult().get(0);
        Assert.assertEquals(true, claimResult.isValid());
        Assert.assertEquals(true, claimResult.isDataValid());
        Assert.assertEquals(0, claimResult.getMessage().size());

        // 5. CHECK BORDEREAU RESULT
        Bordereau bordereau = null;
        bordereau = bordereauService.getObject(fileName);
        Assert.assertEquals(fileName, bordereau.getFileName());
        // Assert.assertEquals("allRejected", bordereau.getStatus());

        String choReference = claimResult.getClaim().getChoReference();
        Claim claim = claimService.getClaimByCHOReferenceNumber(choReference);

        // CHECK DATA IN DATABASE
        checkClaimObject(claim);
        checkCustomer(claim.getCustomer().getId());
        checkEngineeringReport(claim.getEngineerReport().getId());
        checkVehicleHire(claim.getVehicleHire().getId());


        /*
        protected Insurer insurer;
        protected Chorganisation chorganisation;
        protected Incident incident;
        protected Invoice invoice;
        protected ThirdParty thirdParty;
        protected VehicleHire vehicleHire;
        protected EngineerReport engineerReport;
        protected HireMonitoringDetail hireMonitoringDetail;
         */

    }

    private void checkClaimObject(Claim claim) {

        Assert.assertEquals(true, claim.getManagingRepair());
        Assert.assertEquals("2008-01-01 00:00:00.0", claim.getPolicyHolderContactDate().toString());
        Assert.assertEquals("UnitTestNewClaim002", claim.getChoReference());
        Assert.assertEquals("ClaimUnacknowledgedUnrouted", claim.getStatus());
        Assert.assertEquals("2008-01-04 12:00:00.0", claim.getCreditAgreementDate().toString());
        //Assert.assertEquals(Date.valueOf(DateHelper.getCurrentTimeStamp(), claim.getGtaNoticeDate());
        Assert.assertEquals("200912345678", claim.getClaimNumber());
        Assert.assertEquals(new BigDecimal("0.00"), claim.getIndemnityAmount());
        Assert.assertEquals(new BigDecimal("0.00"), claim.getPercentageLiabilityAccepted());
        Assert.assertEquals(false, claim.getIsQuantumDispute());
        Assert.assertEquals(null, claim.getEngineerClaimReviewNotes());
        Assert.assertEquals(false, claim.getIsInvoiceReviewRequired());
        Assert.assertEquals(false, claim.getIsIsAnomalies());
        Assert.assertEquals(false, claim.isIsFnolReviewed());
        Assert.assertEquals(null, claim.getReasonOfRejectionId());

    }

    private void checkEngineeringReport(int objId) {

        EngineerReport engineerReport = engineerReportService.getObject(objId);

        // FROM <engineer-report>
        Assert.assertEquals(new BigDecimal("2991"), engineerReport.getLabourAmount());
        Assert.assertEquals(new BigDecimal("234"), engineerReport.getTotalAmount());
        Assert.assertEquals(Integer.valueOf(2), engineerReport.getDays());
        Assert.assertEquals(true, engineerReport.isIsUsable());
        Assert.assertEquals("ER-Name", engineerReport.getName());
        Assert.assertEquals("ER-Company", engineerReport.getCompany());
        Assert.assertEquals("A0001", engineerReport.getAddress1());
        Assert.assertEquals("A0002", engineerReport.getAddress2());
        Assert.assertEquals("A0003", engineerReport.getAddress3());
        Assert.assertEquals("A0004", engineerReport.getAddress4());
        Assert.assertEquals("A0005", engineerReport.getAddress5());
        Assert.assertEquals("01000", engineerReport.getPostcode());
        Assert.assertEquals("1203984", engineerReport.getTelephone());
        Assert.assertEquals("dfsdfsf@ss.d", engineerReport.getEmail());
    }

    private void checkVehicleHire(int objId) {
        VehicleHire vehicleHire = vehicleHireService.getObject(objId);
        Assert.assertEquals("T456YHU", vehicleHire.getVehicleRegistration());
        Assert.assertEquals("Ford", vehicleHire.getVehicleManufacturer());
        Assert.assertEquals("T456YHU", vehicleHire.getVehicleModel());
        Assert.assertEquals("Repairs Complete", vehicleHire.getCollectionReason());
        Assert.assertEquals(Integer.valueOf(9), vehicleHire.getDays());
        Assert.assertEquals("2008-01-06 00:00:00.0", vehicleHire.getRentalStart().toString());
        Assert.assertEquals("2008-01-07 00:00:00.0", vehicleHire.getRentalEnd().toString());
        VehicleClass VehicleClass = vehicleClassService.getVehicleClassByName("F3");
        Assert.assertEquals(VehicleClass.getId(), vehicleHire.getVehicleClass().getId());
    }

    private void checkCustomer(int objId) {
        Customer customer = customerService.getObject(objId);

        // FROM <driver>
        Assert.assertEquals("s", customer.getTitle());
        Assert.assertEquals("James", customer.getFirstName());
        Assert.assertEquals("Jackson", customer.getLastName());
        Assert.assertEquals("12 Orsssssschard Rd", customer.getAddress1());
        Assert.assertEquals("Kembreyshire", customer.getAddress2());
        Assert.assertEquals("Swindon", customer.getAddress3());
        Assert.assertEquals("Address4", customer.getAddress4());
        Assert.assertEquals("Address5", customer.getAddress5());
        Assert.assertEquals("SN28UH", customer.getPostcode());
        Assert.assertEquals("01987654736", customer.getTelephoneDay());
        Assert.assertEquals("01987654736", customer.getTelephoneEvening());
        Assert.assertEquals("j.jackson@mail.com", customer.getEmail());
        Assert.assertEquals("23", customer.getAge().toString());
        Assert.assertEquals("Student", customer.getOccupation());
        Assert.assertEquals("s", customer.getPolicyUsage());
        Assert.assertEquals(true, customer.isIsPrimaryDriver());

        // FROM <claim><customer>
        VehicleClass VehicleClass = vehicleClassService.getVehicleClassByName("F3");
        Assert.assertEquals(VehicleClass.getId(), customer.getVehicleClass().getId());
        Assert.assertEquals("MORE THAN", customer.getInsurerName());
        Assert.assertEquals("000000001", customer.getPolicyNumber());
        Assert.assertEquals("CL001", customer.getClaimReference());
        Assert.assertEquals(true, customer.isComprehensive());
        Assert.assertEquals("X567XER", customer.getVehicleRegistration());
        Assert.assertEquals("Manufat", customer.getVehicleManufacturer());
        Assert.assertEquals("Mondeo", customer.getVehicleModel());
        Assert.assertEquals("Swindon", customer.getLocation());
        Assert.assertEquals("Dented passenger front wing and door passenger, driverside front corner dented", customer.getDamage());
        Assert.assertEquals(true, customer.getIsUsable());
        Assert.assertEquals(false, customer.isIsActive());
        Assert.assertEquals("2008-01-08 00:00:00.0", customer.getInitialECD().toString());
        Assert.assertEquals(new Boolean(false), customer.getIsTotalLoss());
    }
}
