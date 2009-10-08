package chox.services;

import chox.model.ChoBand;
import chox.model.Claim;
import chox.model.EngineerReport;
import chox.model.History;
import chox.model.VehicleClass;
import chox.xmlValidation.model.status.ClaimParseStatus;
import chox.xmlValidation.rules.Util.HistoryHelper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.engine.RulesEngine;
import scsbre.engine.RulesEngineResponse;

public class BusinessRulesEngServiceImpl implements BusinessRulesEngService {

    private static String sectionName = "BRE Enginee";
    private ClaimService claimService;
    private HireMonitoringEcdService hireMonitoringEcdService;
    private InvoiceService invoiceService;
    private HistoryService historyService;
    private ChoBandService choBandService;
    
    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }
    public void setHireMonitoringEcdService(HireMonitoringEcdService hireMonitoringEcdService) {
        this.hireMonitoringEcdService = hireMonitoringEcdService;
    }
    public void setInvoiceService(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }
    public void setHistoryService(HistoryService historyService) {
        this.historyService = historyService;
    }
    public void setChoBandService(ChoBandService choBandService) {
        this.choBandService = choBandService;
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
        if(claim.getVehicleHire().getVehicleClass()!=null){
            if(claim.getVehicleHire().getVehicleClass().getName().equalsIgnoreCase("Unattached") || claim.getVehicleHire().getVehicleClass().getName().equalsIgnoreCase("UNATTACHED")){
                claim.getVehicleHire().setVehicleClass(null);
            }
        }

        claim.setHireMonitoringEcd(hireMonitoringEcdService.getLatestHireMonitoringECDDate(claim));
        return claim;
    }

    private RulesEngineResponse validate(Claim claim) {
        RulesEngine r = RulesEngine.getInstance(claim);
        RulesEngineResponse reponse = r.ResolveStatus();
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
    }

    public RulesEngineResponse processResubmitInvoice(Claim breClaim){

        // SET CHO BAND
        ChoBand choBand = choBandService.getChoBandByChorganisationIdAndInsurerId(breClaim.getChorganisation().getId(), breClaim.getInsurer().getId());
        breClaim.setChoband(choBand);
        
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
            //claimResult.setHistory(processBreErrorMessage(validationResult.getResults(), claimResult));
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

            histories.add(HistoryHelper.createHistory(claimResult.getClaim(), rv));
        }

        return histories;

    }

    private void doPrintResult(ClaimResult claimResult) {

        System.out.println("-------");
        System.out.println(sectionName + "| getChorganisation :" + claimResult.getClaim().getClaimNumber());
        System.out.println(sectionName + "| getChorganisation :" + claimResult.getClaim().getChorganisation());
        System.out.println(sectionName + "| getChoBand :" + claimResult.getClaim().getChoBand());
        System.out.println(sectionName + "| getCustomer :" + claimResult.getClaim().getCustomer());
        // System.out.println(sectionName + "| getEngineerReport :"+claimResult.getClaim().getEngineerReport().getAddress1());
        // System.out.println(sectionName + "| getHireMonitoringDetail :"+claimResult.getClaim().getHireMonitoringDetail().getNameOfIme());
        // System.out.println(sectionName + "| getIncident :"+claimResult.getClaim().getIncident().getLocation());
        System.out.println(sectionName + "| getInsurer :" + claimResult.getClaim().getInsurer());
        System.out.println(sectionName + "| getInvoice :" + claimResult.getClaim().getInvoice().getClaimInvoiceNo());
        System.out.println(sectionName + "| getThirdParty :" + claimResult.getClaim().getThirdParty().getFirstName());
        System.out.println(sectionName + "| getVehicleHire :" + claimResult.getClaim().getVehicleHire().getIsTotalLoss());


    }
}

