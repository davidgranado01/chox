package chox.services;

import chox.Util.DateHelper;
import chox.model.Bordereau;
import chox.model.Claim;
import chox.model.Customer;
import chox.model.EngineerReport;
import chox.model.VehicleClass;
import chox.model.VehicleHire;
import chox.xmlValidation.model.BordereauResult;
import chox.xmlValidation.model.ClaimResult;
import java.io.File;
import java.math.BigDecimal;
import junit.framework.Assert;
import org.junit.Test;
import junit.framework.TestCase;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class XMLUpload_newClaim_01 extends TestCase{
    private ClassPathXmlApplicationContext ctx;
    private UploadClaimXMLService service = null;
    private BordereauService bordereauService = null;
    private ClaimService claimService = null;
    private CustomerService customerService = null;
    private VehicleClassService vehicleClassService = null;
    private EngineerReportService engineerReportService = null;
    private VehicleHireService vehicleHireService = null;
    private String testFilePath;
    
    public XMLUpload_newClaim_01() {
        testFilePath = new File("").getAbsolutePath()+"/test/chox/testFile/";
        String[] paths = {"applicationContext.xml"};
        ctx = new ClassPathXmlApplicationContext(paths);        
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        service = (UploadClaimXMLService) ctx.getBean("uploadClaimXMLService");
        bordereauService = (BordereauService) ctx.getBean("bordereauService");
        claimService = (ClaimService) ctx.getBean("claimService");
        customerService = (CustomerService) ctx.getBean("customerService");
        vehicleClassService = (VehicleClassService) ctx.getBean("vehicleClassService");
        engineerReportService = (EngineerReportService) ctx.getBean("engineerReportService");
        vehicleHireService = (VehicleHireService) ctx.getBean("vehicleHireService");
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        service = null;
    }

    @Test
    public void testCanInitUploadClaimXMLServiceFromSpring()
    {
        service.toString();
        Assert.assertNotNull(service);
    }
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
            assertEquals(true, parseResult.isValid());
            assertEquals(0, parseResult.getMessage().size());
            assertEquals(1, parseResult.getClaimResult().size());
            
            // 4. CHECK XML CLAIM RESULT
            ClaimResult claimResult = parseResult.getClaimResult().get(0);
            assertEquals(false, claimResult.isValid());
            assertEquals(false, claimResult.isDataValid());
            assertEquals(4, claimResult.getMessage().size());
            assertEquals("Invalid or incorrect character in 'Managing repair' for 'Claim Header'.".toLowerCase(), claimResult.getMessage().get(0).toLowerCase());
            assertEquals("Invalid or incorrect character in 'Customer's Vehicle Registration' for 'Customer Detail'.".toLowerCase(), claimResult.getMessage().get(1).toLowerCase());
            assertEquals("No 'Third Party's Driver First Name' information supplied for 'Third Party Details'. Please re-submit with this information.".toLowerCase(), claimResult.getMessage().get(2).toLowerCase());
            assertEquals("Invalid or incorrect character in 'Number Days Hire' for 'Vehicle Hire Details'.".toLowerCase(), claimResult.getMessage().get(3).toLowerCase());
            
            // 5. CHECK BORDEREAU RESULT
            Bordereau bordereau = null;
            bordereau = bordereauService.getObject(fileName);
            assertEquals(fileName, bordereau.getFileName());
            assertEquals("allRejected", bordereau.getStatus());
            
        }catch(Exception ex){
        }
    }
    */ 

    @Test
    public void testFile_2_Successful() {
    
        String fileName = "UnitTest-NewClaim_02.xml";
        
        try
        {
            // 1. DELETE OBJECT
            bordereauService.deleteObject(fileName);
            
            // 2. PROCESS THE XML
            File testFile = new File(testFilePath + fileName);
            BordereauResult parseResult = service.processClaimXMLFile(testFile, fileName);
            
            // 3. CHECK XML RESULT
            assertEquals(true, parseResult.isValid());
            assertEquals(0, parseResult.getMessage().size());
            assertEquals(1, parseResult.getClaimResult().size());
            
            // 4. CHECK XML CLAIM RESULT
            ClaimResult claimResult = parseResult.getClaimResult().get(0);
            assertEquals(true, claimResult.isValid());
            assertEquals(true, claimResult.isDataValid());
            assertEquals(0, claimResult.getMessage().size());
            
            // 5. CHECK BORDEREAU RESULT
            Bordereau bordereau = null;
            bordereau = bordereauService.getObject(fileName);
            assertEquals(fileName, bordereau.getFileName());
            // assertEquals("allRejected", bordereau.getStatus());
            
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
            
        }catch(Exception ex){

        }
    }
    
    private void checkClaimObject(Claim claim){
        
        assertEquals(true, claim.getManagingRepair());
        assertEquals("2008-01-01 00:00:00.0", claim.getPolicyHolderContactDate().toString());
        assertEquals("UnitTestNewClaim002", claim.getChoReference());
        assertEquals("ClaimUnacknowledgedUnrouted", claim.getStatus());
        assertEquals("2008-01-04 12:00:00.0", claim.getCreditAgreementDate().toString());
        //assertEquals(Date.valueOf(DateHelper.getCurrentTimeStamp(), claim.getGtaNoticeDate());
        assertEquals("200912345678", claim.getClaimNumber());
        assertEquals(new BigDecimal("0.00"), claim.getIndemnityAmount());
        assertEquals(new BigDecimal("0.00"), claim.getPercentageLiabilityAccepted());
        assertEquals(false, claim.getIsQuantumDispute());
        assertEquals(null, claim.getEngineerClaimReviewNotes());
        assertEquals(false, claim.getIsInvoiceReviewRequired());
        assertEquals(false, claim.isIsAnomalies());
        assertEquals(false, claim.isIsFnolReviewed());
        assertEquals(null, claim.getReasonOfRejectionId());
        
    }
    
    private void checkEngineeringReport(int objId){
        
        EngineerReport engineerReport = engineerReportService.getObject(objId);
        
        // FROM <engineer-report>
        assertEquals(new BigDecimal("2991.00"), engineerReport.getLabourAmount());
        assertEquals(new BigDecimal("234.00"), engineerReport.getTotalAmount());
        assertEquals(Integer.valueOf(2), engineerReport.getDays());
        assertEquals(true, engineerReport.isIsUsable());
        assertEquals("ER-Name", engineerReport.getName());
        assertEquals("ER-Company", engineerReport.getCompany());
        assertEquals("A0001", engineerReport.getAddress1());
        assertEquals("A0002", engineerReport.getAddress2());
        assertEquals("A0003", engineerReport.getAddress3());
        assertEquals("A0004", engineerReport.getAddress4());
        assertEquals("A0005", engineerReport.getAddress5());
        assertEquals("01000", engineerReport.getPostcode());
        assertEquals("1203984", engineerReport.getTelephone());
        assertEquals("dfsdfsf@ss.d", engineerReport.getEmail());     
    }
    
    private void checkVehicleHire(int objId){
        VehicleHire vehicleHire = vehicleHireService.getObject(objId);
        assertEquals("T456YHU", vehicleHire.getVehicleRegistration());
        assertEquals("Ford", vehicleHire.getVehicleManufacturer());
        assertEquals("T456YHU", vehicleHire.getVehicleModel());
        assertEquals("Repairs Complete", vehicleHire.getCollectionReason());
        assertEquals(Integer.valueOf(9), vehicleHire.getDays());
        assertEquals("2008-01-06 00:00:00.0", vehicleHire.getRentalStart().toString());
        assertEquals("2008-01-07 00:00:00.0", vehicleHire.getRentalEnd().toString());
        VehicleClass VehicleClass = vehicleClassService.getVehicleClassByName("F3");
        assertEquals(VehicleClass.getId(), vehicleHire.getVehicleClass().getId());
    }
    
    private void checkCustomer(int objId){
        Customer customer = customerService.getObject(objId);
        
        // FROM <driver>
        assertEquals("s", customer.getTitle());
        assertEquals("James", customer.getFirstName());
        assertEquals("Jackson", customer.getLastName());
        assertEquals("12 Orsssssschard Rd", customer.getAddress1());
        assertEquals("Kembreyshire", customer.getAddress2());
        assertEquals("Swindon", customer.getAddress3());
        assertEquals("Address4", customer.getAddress4());
        assertEquals("Address5", customer.getAddress5());
        assertEquals("SN28UH", customer.getPostcode());
        assertEquals("01987654736", customer.getTelephoneDay());
        assertEquals("01987654736", customer.getTelephoneEvening());
        assertEquals("j.jackson@mail.com", customer.getEmail());
        assertEquals("23", customer.getAge().toString());
        assertEquals("Student", customer.getOccupation());        
        assertEquals("s", customer.getPolicyUsage());
        assertEquals(true, customer.isIsPrimaryDriver());
        
        // FROM <claim><customer>
        VehicleClass VehicleClass = vehicleClassService.getVehicleClassByName("F3");
        assertEquals(VehicleClass.getId(), customer.getVehicleClass().getId());
        assertEquals("MORE THAN", customer.getInsurerName());
        assertEquals("000000001", customer.getPolicyNumber());
        assertEquals("CL001", customer.getClaimReference());
        assertEquals(true, customer.isComprehensive());
        assertEquals("X567XER", customer.getVehicleRegistration());
        assertEquals("Manufat", customer.getVehicleManufacturer());
        assertEquals("Mondeo", customer.getVehicleModel());
        assertEquals("Swindon", customer.getLocation());
        assertEquals("Dented passenger front wing and door passenger, driverside front corner dented", customer.getDamage());
        assertEquals(true, customer.getIsUsable());
        assertEquals(false, customer.isIsActive());
        assertEquals("2008-01-08 00:00:00.0", customer.getInitialECD().toString());
        assertEquals(new Boolean(false), customer.getIsTotalLoss());
    }
}
