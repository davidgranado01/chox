package idas.chox.service.xml.validations;


import idas.chox.core.services.BusinessRulesEngService;
import idas.chox.core.services.BreBandService;
import idas.chox.core.services.ChorganisationService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.HireMonitoringEcdService;
import idas.chox.core.services.HistoryService;
import idas.chox.core.services.InsurerAliasService;
import idas.chox.core.services.InsurerChorganisationService;
import idas.chox.core.services.InvoiceService;
import idas.chox.core.services.VehicleClassService;
import idas.chox.core.xmlValidation.BordereauResult;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.core.xmlValidation.DataValidationParameter;
import java.io.File;

public class BordereauDataValidation {
            
    public BordereauResult validate(File file, String fileName, 
            BordereauResult bordereauResult, 
            ClaimService claimService,
            ChorganisationService chorganisationService,
            BreBandService choBandService,
            VehicleClassService vehicleClassService,
            InsurerAliasService insurerAlliasService,
            InsurerChorganisationService insurerChorganisationService,
            HireMonitoringEcdService hireMonitoringEcdService,
            InvoiceService invoiceService,
            HistoryService historyService,
            BusinessRulesEngService businessRuleEngService){
        
        try {
            
            DataValidationParameter dataValidationParameter = new DataValidationParameter();

            // COLLECT ALL CHO REFERENCE
            // List<String> choReferences = new ArrayList<String>();

            for(ClaimResult claimResult : bordereauResult.getClaimResult()){

                System.out.println("********************");
                System.out.println("START: is Claim Valid?: " + claimResult.isValid());
                System.out.println("START: is Claim Data valid?: " + claimResult.isDataValid());
                System.out.println("START: Claim Process Status: " + claimResult.getClaimParseStatus());
                System.out.println("START: Claim Process Msg Size: " + claimResult.getMessage().size());

                if(claimResult.isValid()){
                    
                    // CHECK CLAIM HEADER
                    ClaimHeaderValidation claimHeaderVal = new ClaimHeaderValidation(claimResult, dataValidationParameter, claimService, chorganisationService, choBandService);
                    claimResult = claimHeaderVal.execute();

                    // CHECK CUSTOMER / DRIVER DETAIL (NEW CLAIM ONLY)
                    CustomerValidation custVal = new CustomerValidation(claimResult, dataValidationParameter, claimService);
                    claimResult = custVal.execute();
                    
                    // CHECK CLAIM - CUSTOMER (NEW CLAIM ONLY)
                    ClaimCustomerValidation claimCustVal = new ClaimCustomerValidation(claimResult, dataValidationParameter, claimService, vehicleClassService);
                    claimResult = claimCustVal.execute();
                    
                    // CHECK CLAIM - THIRD PARTY (NEW CLAIM ONLY)
                    ClaimThirdPartyValidation claimThirdPartyVal = new ClaimThirdPartyValidation(claimResult, dataValidationParameter, claimService, vehicleClassService, insurerAlliasService, insurerChorganisationService);
                    claimResult = claimThirdPartyVal.execute();

                    // CHECK CLAIM - INCIDENT (NEW CLAIM ONLY)
                    ClaimIncidentValidation claimIncidentVal = new ClaimIncidentValidation(claimResult, dataValidationParameter, claimService);
                    claimResult = claimIncidentVal.execute();
                    
                    // ALL CLAIM EXCEPT PROCESSED INVOICE
                    ClaimEngineeringReportValidation engReportVal = new ClaimEngineeringReportValidation(claimResult, dataValidationParameter);
                    claimResult = engReportVal.execute();
                    
                    // ALL CLAIM EXCEPT PROCESSED INVOICE
                    ClaimHireMonitoringDetailValidation hireMonitoringDtlVal = new ClaimHireMonitoringDetailValidation(claimResult, dataValidationParameter);
                    claimResult = hireMonitoringDtlVal.execute();
                    
                    // ALL CLAIM EXCEPT PROCESSED INVOICE
                    ClaimVehicleHireValidation vehicleHireVal = new ClaimVehicleHireValidation(claimResult, dataValidationParameter, vehicleClassService);
                    claimResult = vehicleHireVal.execute();
                    
                    // ALL NEW INVOICE ONLY
                    InvoiceValidation invVal = new InvoiceValidation(claimResult, dataValidationParameter, claimService, chorganisationService, choBandService);
                    claimResult = invVal.execute();
                    
                    /* MANTIS : 719 */
                    if(claimResult.getClaimParseStatus().equals(ClaimParseStatus.newClaim)){
                        
                        if(claimResult.getClaim().getHireMonitoringDetail()!=null
                                && claimResult.getClaim().getCustomer()!=null
                                && claimResult.getClaim().getCustomer().getIsTotalLoss()!=null){
                            
                            claimResult.getClaim().getHireMonitoringDetail().setIsTotalLostCheck(claimResult.getClaim().getCustomer().getIsTotalLoss());
                        }
                        
                    }

                    // RUN BRE VALIDATION FOR ALL NEW INVOICE ONLY                    
                    claimResult = businessRuleEngService.execute(claimResult);

                }
            }
            
        } catch (Exception ex) {
            bordereauResult.setValid(false);
            bordereauResult.addMessage(ex.getLocalizedMessage());
        }
        
        return bordereauResult;
    }    
}
