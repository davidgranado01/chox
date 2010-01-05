//package idas.chox.service.xml.validations;
//
//
//import idas.chox.service.xml.readers.ClaimCustomerReader;
//import idas.chox.service.xml.readers.ClaimEngineeringReportReader;
//import idas.chox.service.xml.readers.InvoiceReader;
//import idas.chox.service.xml.readers.ClaimHireMonitoringDetailReader;
//import idas.chox.service.xml.readers.CustomerReader;
//import idas.chox.service.xml.readers.ClaimVehicleHireReader;
//import idas.chox.service.xml.readers.ClaimIncidentReader;
//import idas.chox.service.xml.readers.ClaimHeaderReader;
//import idas.chox.service.xml.readers.ClaimThirdPartyReader;
//import idas.chox.core.services.BusinessRulesEngService;
//import idas.chox.core.services.BreBandService;
//import idas.chox.core.services.ChorganisationService;
//import idas.chox.core.services.ClaimService;
//import idas.chox.core.services.InsurerAliasService;
//import idas.chox.core.services.InsurerChorganisationService;
//import idas.chox.core.services.VehicleClassService;
//import idas.chox.core.xmlValidation.BordereauResult;
//import idas.chox.core.xmlValidation.ClaimParseStatus;
//import idas.chox.core.xmlValidation.ClaimResult;
//
//public class BordereauDataValidation {
//
//    public static void validate(BordereauResult bordereauResult,
//            ClaimService claimService,
//            ChorganisationService chorganisationService,
//            BreBandService choBandService,
//            VehicleClassService vehicleClassService,
//            InsurerAliasService insurerAlliasService,
//            InsurerChorganisationService insurerChorganisationService,
//            BusinessRulesEngService businessRuleEngService){
//
//        try {
//
//            DataValidationParameter dataValidationParameter = new DataValidationParameter();
//
//            // COLLECT ALL CHO REFERENCE
//            // List<String> choReferences = new ArrayList<String>();
//
//            for(ClaimResult claimResult : bordereauResult.getClaimResult()){
//
//                System.out.println("********************");
//                System.out.println("START: is Claim Valid?: " + claimResult.isValid());
//                System.out.println("START: is Claim Data valid?: " + claimResult.isDataValid());
//                System.out.println("START: Claim Process Status: " + claimResult.getClaimParseStatus());
//                System.out.println("START: Claim Process Msg Size: " + claimResult.getMessage().size());
//
//                if(claimResult.isValid()){
//
//                    // CHECK CLAIM HEADER
//                    ClaimHeaderReader claimHeaderVal = new ClaimHeaderReader(claimResult, dataValidationParameter, claimService, chorganisationService, choBandService);
//                    claimResult = claimHeaderVal.execute();
//
//                    // CHECK CUSTOMER / DRIVER DETAIL (NEW CLAIM ONLY)
//                    CustomerReader custVal = new CustomerReader(claimResult, dataValidationParameter, claimService);
//                    claimResult = custVal.execute();
//
//                    // CHECK CLAIM - CUSTOMER (NEW CLAIM ONLY)
//                    ClaimCustomerReader claimCustVal = new ClaimCustomerReader(claimResult, dataValidationParameter, claimService, vehicleClassService);
//                    claimResult = claimCustVal.execute();
//
//                    // CHECK CLAIM - THIRD PARTY (NEW CLAIM ONLY)
//                    ClaimThirdPartyReader claimThirdPartyVal = new ClaimThirdPartyReader(claimResult, dataValidationParameter, claimService, vehicleClassService, insurerAlliasService, insurerChorganisationService);
//                    claimResult = claimThirdPartyVal.execute();
//
//                    // CHECK CLAIM - INCIDENT (NEW CLAIM ONLY)
//                    ClaimIncidentReader claimIncidentVal = new ClaimIncidentReader(claimResult, dataValidationParameter, claimService);
//                    claimResult = claimIncidentVal.execute();
//
//                    // ALL CLAIM EXCEPT PROCESSED INVOICE
//                    ClaimEngineeringReportReader engReportVal = new ClaimEngineeringReportReader(claimResult, dataValidationParameter);
//                    claimResult = engReportVal.execute();
//
//                    // ALL CLAIM EXCEPT PROCESSED INVOICE
//                    ClaimHireMonitoringDetailReader hireMonitoringDtlVal = new ClaimHireMonitoringDetailReader(claimResult, dataValidationParameter);
//                    claimResult = hireMonitoringDtlVal.execute();
//
//                    // ALL CLAIM EXCEPT PROCESSED INVOICE
//                    ClaimVehicleHireReader vehicleHireVal = new ClaimVehicleHireReader(claimResult, dataValidationParameter, vehicleClassService);
//                    claimResult = vehicleHireVal.execute();
//
//                    // ALL NEW INVOICE ONLY
//                    InvoiceReader invVal = new InvoiceReader(claimResult, dataValidationParameter, claimService, chorganisationService, choBandService);
//                    claimResult = invVal.execute();
//
//                    /* MANTIS : 719 */
//                    if(claimResult.getClaimParseStatus().equals(ClaimParseStatus.newClaim)){
//
//                    }
//
//                    // RUN BRE VALIDATION FOR ALL NEW INVOICE ONLY
//                    claimResult = businessRuleEngService.execute(claimResult);
//
//                }
//            }
//
//        } catch (Exception ex) {
//            bordereauResult.setValid(false);
//            bordereauResult.addMessage(ex.getLocalizedMessage());
//        }
//    }
//}
