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
import idas.chox.core.xmlValidation.ClaimParseStatus;
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

    /**** XML UPLOAD **********************************************************************************************************/
    public ClaimResult execute(ClaimResult claimResult) {

        if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.newInvoice) && claimResult.isValid() && claimResult.isDataValid()) {
            process(claimResult);
        }

        return claimResult;
    }

    private void process(ClaimResult claimResult) {

        BreBand choBand = choBandService.getBreBand(claimResult.getClaim().getChorganisation().getId(), claimResult.getClaim().getInsurer().getId());

        // System.out.println("getInsurer:"+claimResult.getClaim().getInsurer().getId());
        // System.out.println("getChorganisation:"+claimResult.getClaim().getChorganisation().getId());
        // System.out.println("choBand:"+choBand.getId());

        if (choBand.getId() != null) {

            VehicleClassCeiling vehicleClassCeiling = insurerService.getVechileClassCeilingForClaim(claimResult.getClaim());
            choBand.setVehicleClassCeiling(vehicleClassCeiling);
            claimResult.getClaim().setBreBand(choBand);

            Boolean isEngReportExist = false;
            if (claimResult.getClaim().getEngineerReport() != null) {
                isEngReportExist = true;
            }

            VehicleClass cust_VehicleClass = claimResult.getClaim().getCustomer().getVehicleClass();
            VehicleClass thirdVehicleClass = claimResult.getClaim().getThirdParty().getVehicleClass();
            VehicleClass vehicle_HireClass = claimResult.getClaim().getVehicleHire().getVehicleClass();

            Claim breClaim = constructBreValidateObject(claimResult.getClaim());
            RulesEngineResponse validationResult = validate(breClaim);

            String oldStatus = claimResult.getClaim().getStatus();
            String newClaimStatus = validationResult.getStatus().toString();

            claimResult.getClaim().setPreviousStatus(oldStatus);
            claimResult.getClaim().setStatus(newClaimStatus);

            if (validationResult.getResults().size() > 0) {
                claimResult.setHistory(processBreErrorMessage(validationResult.getResults(), claimResult));
            }

            /** END BRE VALIDATION **/
            if (!isEngReportExist) {
                claimResult.getClaim().setEngineerReport(null);
            }

            claimResult.getClaim().getCustomer().setVehicleClass(cust_VehicleClass);
            claimResult.getClaim().getThirdParty().setVehicleClass(thirdVehicleClass);
            claimResult.getClaim().getVehicleHire().setVehicleClass(vehicle_HireClass);

        } else {

            claimResult.setValid(false);
            claimResult.getMessage().add("BRE Band is Not Defined, Please contact CHOX Admin");

        }
    }

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

    private void doPrintResult(ClaimResult claimResult) {

        System.out.println("-------");
        System.out.println(sectionName + "| getChorganisation :" + claimResult.getClaim().getClaimNumber());
        System.out.println(sectionName + "| getChorganisation :" + claimResult.getClaim().getChorganisation());
        System.out.println(sectionName + "| getBreBand :" + claimResult.getClaim().getBreBand());
        System.out.println(sectionName + "| getCustomer :" + claimResult.getClaim().getCustomer());
        // System.out.println(sectionName + "| getEngineerReport :"+claimResult.getClaim().getEngineerReport().getAddress1());
        // System.out.println(sectionName + "| getHireMonitoringDetail :"+claimResult.getClaim().getHireMonitoringDetail().getNameOfIme());
        // System.out.println(sectionName + "| getIncident :"+claimResult.getClaim().getIncident().getLocation());
        System.out.println(sectionName + "| getInsurer :" + claimResult.getClaim().getInsurer());
        System.out.println(sectionName + "| getInvoice :" + claimResult.getClaim().getInvoice().getClaimInvoiceNo());
        System.out.println(sectionName + "| getThirdParty :" + claimResult.getClaim().getThirdParty().getFirstName());
        System.out.println(sectionName + "| getVehicleHire :" + claimResult.getClaim().getVehicleHire().getIsTotalLoss());


    }

    /**
     * @param rulesEngine the rulesEngine to set
     */
    public void setRulesEngine(RulesEngine rulesEngine) {
        this.rulesEngine = rulesEngine;
    }
}

