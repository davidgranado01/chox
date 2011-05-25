//package idas.chox.service.xml;
//
//import idas.chox.core.model.Bordereau;
//import idas.chox.core.model.Claim;
//import idas.chox.core.model.ClaimStatus;
//import idas.chox.core.model.Customer;
//import idas.chox.core.model.EngineerReport;
//import idas.chox.core.model.VehicleClass;
//import idas.chox.core.model.VehicleHire;
//import idas.chox.core.util.DateHelper;
//import idas.chox.core.xmlValidation.BordereauResult;
//import idas.chox.core.xmlValidation.ClaimResult;
//import java.io.File;
//import java.math.BigDecimal;
//import junit.framework.Assert;
//import org.junit.Test;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.transaction.annotation.Transactional;
//
//public class XMLUploadClaimTest extends  BaseXMLUploadClaimTest{
////
////    @Test
////    @Transactional
//    public void testFile_2_Successful() throws Exception {
//
//        String fileName = "UnitTest-NewClaim_Base.xml";
//
//        // 2. PROCESS THE XML
//        File testFile = new ClassPathResource(fileName).getFile();
//        BordereauResult parseResult = null; //uploadClaimXMLService.processClaimXMLFile(testFile, fileName);
//
//        // 3. CHECK XML RESULT
//        Assert.assertEquals(true, parseResult.isValid());
//        Assert.assertEquals(0, parseResult.getMessage().size());
//        Assert.assertEquals(7, parseResult.getClaimResult().size());
//
//        // 4. CHECK XML CLAIM RESULT
//        ClaimResult claimResult = parseResult.getClaimResult().get(0);
//        Assert.assertEquals(true, claimResult.isValid());
//        Assert.assertEquals(true, claimResult.isDataValid());
//        Assert.assertEquals(0, claimResult.getMessage().size());
//
//        // 5. CHECK BORDEREAU RESULT
//        Bordereau bordereau = null;
//        bordereau = bordereauService.getBordereauByFileName(fileName);
//        Assert.assertEquals(fileName, bordereau.getFileName());
//        // Assert.assertEquals("allRejected", bordereau.getStatus());
//
//        String choReference = claimResult.getClaim().getChoReference();
//        Claim claim = claimService.getClaimByCHOReferenceNumber(choReference);
//
//        // CHECK DATA IN DATABASE
//        checkClaimObject(claim);
//        checkCustomer(claim.getCustomer());
//        checkEngineeringReport(claim.getEngineerReport());
//        checkVehicleHire(claim.getVehicleHire());
//    }
//
//    private void checkClaimObject(Claim claim) {
//
//        Assert.assertEquals(true, claim.getManagingRepair());
//        Assert.assertEquals(DateHelper.ParseDBDateTime("2008-01-01 00:00:00"), claim.getPolicyHolderContactDate());
//        Assert.assertEquals("UT-CLAIM001", claim.getChoReference());
//        Assert.assertEquals(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED, claim.getStatus());
//        Assert.assertEquals(DateHelper.ParseDBDateTime("2008-01-04 12:00:00"), claim.getCreditAgreementDate());
//        //Assert.assertEquals(Date.valueOf(DateHelper.getCurrentTimeStamp(), claim.getGtaNoticeDate());
//        Assert.assertEquals("200912345678", claim.getClaimNumber());
//        Assert.assertEquals(new BigDecimal("0.00"), claim.getIndemnityAmount());
//        Assert.assertEquals(new BigDecimal("0.00"), claim.getPercentageLiabilityAccepted());
//        Assert.assertEquals(false, claim.getIsQuantumDispute());
//        // Assert.assertEquals(false, claim.getIsInvoiceReviewRequired());
//        Assert.assertEquals(false, claim.getIsIsAnomalies());
//        Assert.assertEquals(false, claim.isIsFnolReviewed());
//        Assert.assertEquals(null, claim.getReasonOfRejection());
//        Assert.assertEquals(claim.getThirdParty().getInsurer(), claim.getInsurer());
//
//    }
//
//    private void checkEngineeringReport(EngineerReport engineerReport) {
//        // FROM <engineer-report>
//        Assert.assertEquals(new BigDecimal("2991"), engineerReport.getLabourAmount());
//        Assert.assertEquals(new BigDecimal("234"), engineerReport.getTotalAmount());
//        Assert.assertEquals(Integer.valueOf(2), engineerReport.getDays());
//        Assert.assertEquals(true, (boolean)engineerReport.isIsUsable());
//        Assert.assertEquals("ER-Name", engineerReport.getName());
//        Assert.assertEquals("ER-Company", engineerReport.getCompany());
//        Assert.assertEquals("A0001", engineerReport.getAddress1());
//        Assert.assertEquals("A0002", engineerReport.getAddress2());
//        Assert.assertEquals("A0003", engineerReport.getAddress3());
//        Assert.assertEquals("A0004", engineerReport.getAddress4());
//        Assert.assertEquals("A0005", engineerReport.getAddress5());
//        Assert.assertEquals("01000", engineerReport.getPostcode());
//        Assert.assertEquals("1203984", engineerReport.getTelephone());
//        Assert.assertEquals("dfsdfsf@ss.d", engineerReport.getEmail());
//    }
//
//    private void checkVehicleHire(VehicleHire vehicleHire) {
//        Assert.assertEquals("T456YHU", vehicleHire.getVehicleRegistration());
//        Assert.assertEquals("Ford", vehicleHire.getVehicleManufacturer());
//        Assert.assertEquals("T456YHU", vehicleHire.getVehicleModel());
//        Assert.assertEquals("Repairs Complete", vehicleHire.getCollectionReason());
//        // Assert.assertEquals(Integer.valueOf(9), vehicleHire.getDays());
//        Assert.assertEquals(DateHelper.ParseDBDateTime("2008-01-06 00:00:00"), vehicleHire.getRentalStart());
//        Assert.assertEquals(DateHelper.ParseDBDateTime("2008-01-07 00:00:00"), vehicleHire.getRentalEnd());
//        VehicleClass VehicleClass = vehicleClassService.getVehicleClassByName("F3");
//        Assert.assertEquals(VehicleClass.getId(), vehicleHire.getVehicleClass().getId());
//    }
//
//    private void checkCustomer(Customer customer) {
//        // FROM <driver>
//        Assert.assertEquals("s", customer.getTitle());
//        Assert.assertEquals("James", customer.getFirstName());
//        Assert.assertEquals("Jackson", customer.getLastName());
//        Assert.assertEquals("12 Orsssssschard Rd", customer.getAddress1());
//        Assert.assertEquals("Kembreyshire", customer.getAddress2());
//        Assert.assertEquals("Swindon", customer.getAddress3());
//        Assert.assertEquals("Address4", customer.getAddress4());
//        Assert.assertEquals("Address5", customer.getAddress5());
//        Assert.assertEquals("SN28UH", customer.getPostcode());
//        Assert.assertEquals("01987654736", customer.getTelephoneDay());
//        Assert.assertEquals("01987654736", customer.getTelephoneEvening());
//        Assert.assertEquals("j.jackson@mail.com", customer.getEmail());
//        Assert.assertEquals("23", customer.getAge().toString());
//        Assert.assertEquals("Student", customer.getOccupation());
//        Assert.assertEquals("s", customer.getPolicyUsage());
//        Assert.assertEquals(true, customer.isIsPrimaryDriver());
//
//        // FROM <claim><customer>
//        VehicleClass VehicleClass = vehicleClassService.getVehicleClassByName("F3");
//        Assert.assertEquals(VehicleClass.getId(), customer.getVehicleClass().getId());
//        Assert.assertEquals("MORE THAN", customer.getInsurerName());
//        Assert.assertEquals("0000000001", customer.getPolicyNumber());
//        Assert.assertEquals("CL001", customer.getClaimReference());
//        Assert.assertEquals(true, customer.isComprehensive());
//        Assert.assertEquals("X567XER", customer.getVehicleRegistration());
//        Assert.assertEquals("Manufat", customer.getVehicleManufacturer());
//        Assert.assertEquals("Mondeo", customer.getVehicleModel());
//        Assert.assertEquals("Swindon", customer.getLocation());
//        Assert.assertEquals("Dented passenger front wing and door passenger, driverside front corner dented", customer.getDamage());
//        Assert.assertEquals(true, customer.getIsUsable());
//        Assert.assertEquals(false, customer.isIsActive());
//        Assert.assertEquals(DateHelper.ParseDBDateTime("2008-01-08 00:00:00"), customer.getInitialECD());
//        Assert.assertEquals(new Boolean(false), customer.getIsTotalLoss());
//    }
//}
