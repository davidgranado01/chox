package chox.xmlValidation.rules.enginee;

import chox.model.Claim;
import chox.model.EngineerReport;
import chox.model.VehicleClass;
import chox.services.ClaimService;
import chox.services.HireMonitoringEcdService;
import chox.services.HistoryService;
import chox.services.InvoiceService;
import chox.xmlValidation.model.ClaimResult;
import chox.xmlValidation.model.status.ClaimParseStatus;
import java.math.BigDecimal;
import java.util.List;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.engine.RulesEngineResponse;

public class BusinessRuleEngProcess {

    private static String sectionName = "BRE Enginee";
    private ClaimResult claimResult;
    private ClaimService claimService;
    private HireMonitoringEcdService hireMonitoringEcdService;
    private InvoiceService invoiceService;
    private HistoryService historyService;
    
    public void setClaimResult(ClaimResult claimResult) { this.claimResult = claimResult; }
    public void setClaimService(ClaimService claimService) { this.claimService = claimService; }
    public void setHireMonitoringEcdService(HireMonitoringEcdService hireMonitoringEcdService) { this.hireMonitoringEcdService = hireMonitoringEcdService; }
    public void setInvoiceService(InvoiceService invoiceService) { this.invoiceService = invoiceService; }
    public void setHistoryService(HistoryService historyService) { this.historyService = historyService; }
    
    public BusinessRuleEngProcess(
            ClaimResult claimResult,
            ClaimService claimService,
            HireMonitoringEcdService hireMonitoringEcdService,
            InvoiceService invoiceService,
            HistoryService historyService){
        
            setClaimResult(claimResult);
            setClaimService(claimService);
            setHireMonitoringEcdService(hireMonitoringEcdService);
            setInvoiceService(invoiceService);
            setHistoryService(historyService);
    }    
    
    
    public ClaimResult execute(){
        
        if(validate()){
            doPrintResult(true);
            process();
            
        }
        
        return claimResult;
    }
    
    public boolean validate(){
        
        boolean bFlag = false;
        
        if(claimResult.getClaimParseStatus().equals(ClaimParseStatus.newInvoice)){
            bFlag = true;            
        }
        
        return bFlag;
        
    }
    
    private void process(){
        
        Boolean isEngReportExist = false;
        if(this.claimResult.getClaim().getEngineerReport()!=null){
            isEngReportExist = true;
        }
        
        VehicleClass cust_VehicleClass = this.claimResult.getClaim().getCustomer().getVehicleClass();
        VehicleClass thirdVehicleClass = this.claimResult.getClaim().getThirdParty().getVehicleClass();
        
        /** START BRE VALIDATION **/
        
        Claim breClaim = doConstructBreValidateObject(this.claimResult.getClaim());
        RulesEngineResponse validationResult = invoiceService.XMLUploaderInvoiceValidation(breClaim);
        
        // historyService.logInvoiceValidationErrorMsg(validationResult, breClaim);
        
        String newClaimStatus = validationResult.getStatus().toString();     
        this.claimResult.getClaim().setStatus(newClaimStatus);
        
        if(validationResult.getResults().size()>0){
            processBreErrorMessage(validationResult.getResults());
        }
        
        /** END BRE VALIDATION **/
        
        if(!isEngReportExist){
            this.claimResult.getClaim().setEngineerReport(null);
        }
        
        this.claimResult.getClaim().getCustomer().setVehicleClass(cust_VehicleClass);
        this.claimResult.getClaim().getThirdParty().setVehicleClass(thirdVehicleClass);
        
    }
    
    private void processBreErrorMessage(List<RuleEvaluation> results){

        for(int iCount=0; iCount<results.size(); iCount++){
            RuleEvaluation rv = results.get(iCount);
            if(rv.getIsVisibleToCHO() && rv.getResult()==RuleEvaluationResult.RuleFailed){
                this.claimResult.getMessage().add(rv.toString());
            }
        }

    }
    
    private Claim doConstructBreValidateObject(Claim claim){
        
        Boolean isIsTotalLostCheck = false;
        if(claim.getHireMonitoringDetail()!=null){
            isIsTotalLostCheck = claim.getHireMonitoringDetail().isIsTotalLostCheck();
        }
        
        claim.getVehicleHire().setIsTotalLoss(isIsTotalLostCheck);
        
        if(claim.getEngineerReport()==null){
            EngineerReport engineerreport = new EngineerReport();
            engineerreport.setDays(0);
            engineerreport.setLabourAmount(new BigDecimal("0.00"));
            engineerreport.setTotalAmount(new BigDecimal("0.00"));
            claim.setEngineerReport(engineerreport);
        }
        
        if(claimService.getCountOfClaimByVRN(claim.getCustomer().getVehicleRegistration(), claim.getId())>0){
           claim.getCustomer().setIsVehicleRegistrationExist(true);
        }
        
        // SET VEHICLE CLASS TO NULL WHEN 
        if(claim.getThirdParty().getVehicleClass()!=null){
            if(claim.getThirdParty().getVehicleClass().getName().equalsIgnoreCase("Unattached") 
                    || claim.getThirdParty().getVehicleClass().getName().equalsIgnoreCase("UNATTACHED")){
                claim.getThirdParty().setVehicleClass(null);
            }
        }
        
        // SET VEHICLE CLASS TO NULL WHEN 
        if(claim.getCustomer().getVehicleClass()!=null){
            if(claim.getCustomer().getVehicleClass().getName().equalsIgnoreCase("Unattached")
                    || claim.getCustomer().getVehicleClass().getName().equalsIgnoreCase("UNATTACHED")){
                claim.getCustomer().setVehicleClass(null);
            }
        }
        
        claim.setHireMonitoringEcd(hireMonitoringEcdService.getLatestHireMonitoringECDDate(claim));        
        
        return claim;
    }
    
    private void doPrintResult(boolean isAllowed){
        
        if(isAllowed){
            
            System.out.println("-------");
            System.out.println(sectionName + "| getChorganisation :"+this.claimResult.getClaim().getClaimNumber());
            System.out.println(sectionName + "| getChorganisation :"+this.claimResult.getClaim().getChorganisation());
            System.out.println(sectionName + "| getChoBand :"+this.claimResult.getClaim().getChoBand());
            System.out.println(sectionName + "| getCustomer :"+this.claimResult.getClaim().getCustomer());
            System.out.println(sectionName + "| getEngineerReport :"+this.claimResult.getClaim().getEngineerReport().getAddress1());
            System.out.println(sectionName + "| getHireMonitoringDetail :"+this.claimResult.getClaim().getHireMonitoringDetail().getNameOfIme());
            System.out.println(sectionName + "| getIncident :"+this.claimResult.getClaim().getIncident().getLocation());
            System.out.println(sectionName + "| getInsurer :"+this.claimResult.getClaim().getInsurer());
            System.out.println(sectionName + "| getInvoice :"+this.claimResult.getClaim().getInvoice().getClaimInvoiceNo());
            System.out.println(sectionName + "| getLineOfBusiness :"+this.claimResult.getClaim().getLineOfBusiness());
            System.out.println(sectionName + "| getThirdParty :"+this.claimResult.getClaim().getThirdParty().getFirstName());
            System.out.println(sectionName + "| getVehicleHire :"+this.claimResult.getClaim().getVehicleHire().getIsTotalLoss());

        }
          
    }
}

