package idas.chox.service.bre;

import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.bre.RulesEngine;
import idas.chox.core.bre.RulesEngineResponse;
import idas.chox.core.model.BreBand;
import idas.chox.core.model.Claim;
import idas.chox.core.model.EngineerReport;
import idas.chox.core.model.History;
import idas.chox.core.model.VehicleClass;
import idas.chox.core.model.VehicleClassCeiling;
import idas.chox.core.services.BusinessRulesEngService;
import idas.chox.core.services.BreBandService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.InsurerService;
import idas.chox.core.xmlValidation.ClaimResult;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BusinessRulesEngServiceImpl implements BusinessRulesEngService {

    private static String sectionName = "BRE Enginee";
    private ClaimService claimService;
    private BreBandService choBandService;
    private InsurerService insurerService;
    private RulesEngine rulesEngine;

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    public void setBreBandService(BreBandService choBandService) {
        this.choBandService = choBandService;
    }

    public void setInsurerService(InsurerService insurerService) {
        this.insurerService = insurerService;
    }

    /**** GENERAL **********************************************************************************************************/
    private Claim constructBreValidateObject(Claim claim) {

        Boolean isIsTotalLostCheck = false;
        if (claim.getHireMonitoringDetail() != null) {
            isIsTotalLostCheck = claim.getHireMonitoringDetail().isIsTotalLostCheck();
        }

        claim.getVehicleHire().setIsTotalLoss(isIsTotalLostCheck);

        if (claim.getEngineerReport() == null) {
            EngineerReport engineerreport = new EngineerReport();
            engineerreport.setDays(0);
            engineerreport.setLabourAmount(new BigDecimal("0.00"));
            engineerreport.setTotalAmount(new BigDecimal("0.00"));
            claim.setEngineerReport(engineerreport);
        }

        if (claimService.getCountOfClaimByVRN(claim.getCustomer().getVehicleRegistration(), claim.getId()) > 0) {
            claim.getCustomer().setIsVehicleRegistrationExist(true);
        }

        // SET VEHICLE CLASS TO NULL WHEN
        if (claim.getThirdParty().getVehicleClass() != null) {
            if (claim.getThirdParty().getVehicleClass().getName().equalsIgnoreCase("Unattached") || claim.getThirdParty().getVehicleClass().getName().equalsIgnoreCase("UNATTACHED")) {
                claim.getThirdParty().setVehicleClass(null);
            }
        }

        // SET VEHICLE CLASS TO NULL WHEN
        if (claim.getCustomer().getVehicleClass() != null) {
            if (claim.getCustomer().getVehicleClass().getName().equalsIgnoreCase("Unattached") || claim.getCustomer().getVehicleClass().getName().equalsIgnoreCase("UNATTACHED")) {
                claim.getCustomer().setVehicleClass(null);
            }
        }

        // Mantis id: 630
        // Change to read vehicleHire's Vehicle Class
        // SET VEHICLE CLASS TO NULL WHEN
        if (claim.getVehicleHire().getVehicleClass() != null) {
            if (claim.getVehicleHire().getVehicleClass().getName().equalsIgnoreCase("Unattached") || claim.getVehicleHire().getVehicleClass().getName().equalsIgnoreCase("UNATTACHED")) {
                claim.getVehicleHire().setVehicleClass(null);
            }
        }
        return claim;
    }

    private RulesEngineResponse validate(Claim claim) {
        RulesEngineResponse reponse = rulesEngine.Validate(claim);
        return reponse;
    }

    @Override
    public void process(ClaimResult claimResult) {

        BreBand choBand = choBandService.getBreBand(claimResult.getClaim().getChorganisation().getId(), claimResult.getClaim().getInsurer().getId());

        if (choBand.getId() != null) {

            Claim claim = claimResult.getClaim();

            VehicleClassCeiling vehicleClassCeiling = insurerService.getVechileClassCeilingForClaim(claim);
            choBand.setVehicleClassCeiling(vehicleClassCeiling);
            claim.setBreBand(choBand);

            Boolean isEngReportExist = false;
            if (claim.getEngineerReport() != null) {
                isEngReportExist = true;
            }

            VehicleClass cust_VehicleClass = claim.getCustomer().getVehicleClass();
            VehicleClass thirdVehicleClass = claim.getThirdParty().getVehicleClass();
            VehicleClass vehicle_HireClass = claim.getVehicleHire().getVehicleClass();

            Claim breClaim = constructBreValidateObject(claim);
            RulesEngineResponse validationResult = validate(breClaim);

            String oldStatus = claim.getStatus();
            String newClaimStatus = validationResult.getStatus().toString();

            claim.setPreviousStatus(oldStatus);
            claim.setStatus(newClaimStatus);

            if (validationResult.getResults().size() > 0) {
                List<History> histories =  processBreErrorMessage(validationResult.getResults(), claimResult);
                for(History history : histories)
                {
                    claim.addHistory(history);
                }
            }

            /** END BRE VALIDATION **/
            if (!isEngReportExist) {
                claim.setEngineerReport(null);
            }

            claim.getCustomer().setVehicleClass(cust_VehicleClass);
            claim.getThirdParty().setVehicleClass(thirdVehicleClass);
            claim.getVehicleHire().setVehicleClass(vehicle_HireClass);

        } else {

            claimResult.setValid(false);
            claimResult.getMessage().add("BRE Band is Not Defined, Please contact CHOX Admin");

        }
    }

    @Override
    public RulesEngineResponse processResubmitInvoice(Claim breClaim) {

        BreBand choBand = choBandService.getBreBand(breClaim.getChorganisation().getId(), breClaim.getInsurer().getId());
        VehicleClassCeiling vehicleClassCeiling = insurerService.getVechileClassCeilingForClaim(breClaim);
        choBand.setVehicleClassCeiling(vehicleClassCeiling);
        breClaim.setBreBand(choBand);

        Boolean isEngReportExist = false;
        if (breClaim.getEngineerReport() != null) {
            isEngReportExist = true;
        }

        // GET CURRENT RECORDS
        VehicleClass cust_VehicleClass = breClaim.getCustomer().getVehicleClass();
        VehicleClass thirdVehicleClass = breClaim.getThirdParty().getVehicleClass();
        VehicleClass vehicle_HireClass = breClaim.getVehicleHire().getVehicleClass();
        String oldStatus = breClaim.getStatus();

        breClaim = constructBreValidateObject(breClaim);
        RulesEngineResponse validationResult = validate(breClaim);
        String newClaimStatus = validationResult.getStatus().toString();

        breClaim.setPreviousStatus(oldStatus);
        breClaim.setStatus(newClaimStatus);

        /*
        if (validationResult.getResults().size() > 0) {
        claimResult.setHistory(processBreErrorMessage(validationResult.getResults(), claimResult));
        }
         */

        /** END BRE VALIDATION */
        if (!isEngReportExist) {
            breClaim.setEngineerReport(null);
        }

        breClaim.getCustomer().setVehicleClass(cust_VehicleClass);
        breClaim.getThirdParty().setVehicleClass(thirdVehicleClass);
        breClaim.getVehicleHire().setVehicleClass(vehicle_HireClass);

        return validationResult;
    }

    private List<History> processBreErrorMessage(List<RuleEvaluation> results, ClaimResult claimResult) {

        List<History> histories = new ArrayList<History>();

        for (int iCount = 0; iCount < results.size(); iCount++) {

            RuleEvaluation rv = results.get(iCount);
            if (rv.getIsVisibleToCHO() && rv.getResult() == RuleEvaluationResult.RuleFailed) {
                claimResult.getMessage().add(rv.toString());
            }

            histories.add(History.New(rv));
        }

        return histories;

    }
   
    public void setRulesEngine(RulesEngine rulesEngine) {
        this.rulesEngine = rulesEngine;
    }
}

