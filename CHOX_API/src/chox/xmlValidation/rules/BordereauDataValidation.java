package chox.xmlValidation.rules;

import chox.xmlValidation.rules.enginee.ClaimHeaderValidation;
import chox.services.ChoBandService;
import chox.services.ChorganisationService;
import chox.services.ClaimService;
import chox.services.HireMonitoringEcdService;
import chox.services.HistoryService;
import chox.services.InsurerAlliasService;
import chox.services.InsurerChorganisationService;
import chox.services.InvoiceService;
import chox.services.VehicleClassService;
import chox.xmlValidation.model.BordereauResult;
import chox.xmlValidation.model.ClaimResult;
import chox.xmlValidation.rules.enginee.BusinessRuleEngProcess;
import chox.xmlValidation.rules.enginee.ClaimCustomerValidation;
import chox.xmlValidation.rules.enginee.ClaimEngineeringReportValidation;
import chox.xmlValidation.rules.enginee.ClaimHireMonitoringDetailValidation;
import chox.xmlValidation.rules.enginee.ClaimIncidentValidation;
import chox.xmlValidation.rules.enginee.ClaimThirdPartyValidation;
import chox.xmlValidation.rules.enginee.ClaimVehicleHireValidation;
import chox.xmlValidation.rules.enginee.CustomerValidation;
import chox.xmlValidation.rules.enginee.InvoiceValidation;
import java.io.File;

public class BordereauDataValidation {
            
    public BordereauResult validate(File file, String fileName, 
            BordereauResult bordereauResult, 
            ClaimService claimService,
            ChorganisationService chorganisationService,
            ChoBandService choBandService,
            VehicleClassService vehicleClassService,
            InsurerAlliasService insurerAlliasService,
            InsurerChorganisationService insurerChorganisationService,
            HireMonitoringEcdService hireMonitoringEcdService,
            InvoiceService invoiceService,
            HistoryService historyService){
        
        try {
            
            DataValidationParameter dataValidationParameter = new DataValidationParameter();
            
            for(ClaimResult claimResult : bordereauResult.getClaimResult()){
                
                // System.out.println("========================================================================");
                // System.out.println("*** is Claim Valid?: " + claimResult.isValid());
                // System.out.println("*** is Claim Data valid?: " + claimResult.isDataValid());
                // System.out.println("*** Claim Process Status: " + claimResult.getClaimParseStatus());
                // System.out.println("*** Claim Process Msg Size: " + claimResult.getMessage().size());
                
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
                    
                    // RUN BRE VALIDATION FOR ALL NEW INVOICE ONLY
                    BusinessRuleEngProcess brePrc = new BusinessRuleEngProcess(claimResult, claimService, hireMonitoringEcdService, invoiceService, historyService);
                    claimResult = brePrc.execute();

                }
            }
            
            // System.out.println("");
            // System.out.println("");
            // System.out.println("");
            
        } catch (Exception ex) {
            bordereauResult.setValid(false);
            bordereauResult.addMessage(ex.getLocalizedMessage());
        }
        
        return bordereauResult;
    }    
}
