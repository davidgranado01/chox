package chox.services;

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

    public ClaimResult execute(ClaimResult claimResult) {

        if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.newInvoice) && claimResult.isValid() && claimResult.isDataValid()) {
            //doPrintResult(claimResult);//disabled debug print function
            process(claimResult);
        }

        return claimResult;
    }

    public RulesEngineResponse validate(Claim claim) {
        RulesEngine r = RulesEngine.getInstance(claim);
        RulesEngineResponse reponse = r.ResolveStatus();
        return reponse;
    }

    public Claim constructBreValidateObject(Claim claim) {

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

        claim.setHireMonitoringEcd(hireMonitoringEcdService.getLatestHireMonitoringECDDate(claim));
        return claim;
    }

    private void process(ClaimResult claimResult) {

        Boolean isEngReportExist = false;
        if (claimResult.getClaim().getEngineerReport() != null) {
            isEngReportExist = true;
        }

        VehicleClass cust_VehicleClass = claimResult.getClaim().getCustomer().getVehicleClass();
        VehicleClass thirdVehicleClass = claimResult.getClaim().getThirdParty().getVehicleClass();

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

