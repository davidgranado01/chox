package scsbre.engine;

import scsbre.engine.rules.IBusinessRule;
import scsbre.engine.util.ClaimCalcHelper;
import scsbre.engine.util.CHOBandCalcHelper;
import scsbre.engine.util.CalcHelper;
import java.math.BigDecimal;

import scsbre.engine.util.InvoiceCalcHelper;
import scsbre.model.ICHOBandInfo;
import scsbre.model.IClaimInfo;
import scsbre.model.ICustomerVehicleDamageInfo;
import scsbre.model.IEngineerReportInfo;
import scsbre.model.IHireInfo;
import scsbre.model.IInsurerInfo;
import scsbre.model.IInvoiceInfo;
import scsbre.model.IVehicleClassInfo;


import scsbre.engine.rules.*;

public class RulesEngine {

    private IClaimInfo claim;
    private ClaimCalcHelper cCalc;
    private InvoiceCalcHelper iCalc;
    private CHOBandCalcHelper bandCalc;    //convenience accessors
    private IInvoiceInfo invoice;
    private IHireInfo hireDetail;
    private ICHOBandInfo choBand;
    private IInsurerInfo insurer;
    private ICustomerVehicleDamageInfo cvdamage;
    private IEngineerReportInfo eReport;
    private IVehicleClassInfo customerVClass;
    private IVehicleClassInfo hireVClass;
    private BigDecimal vatRate;

    public static RulesEngine getInstance(IClaimInfo c, BigDecimal vatRate) {

        RulesEngine engine = new RulesEngine(c);
        
 
        
        return engine;
    }

    
    
    private RuleEvaluationResult execute(IBusinessRule rule){

        RuleEvaluationResult result = new RuleEvaluationResult();   
        boolean success = rule.run(claim);
        result.setMessage (success ? "" : rule.getErrorMessage());
        result.setIsVisibleToCHO(rule.isVisibleToCHO());
        return result;
    }

    private RulesEngine(IClaimInfo c) {

        claim = c;
        this.vatRate = CalcHelper.VAT_RATE;

        invoice = c.getInvoice();
        hireDetail = c.getHireDetail();
        choBand = c.getChoBand();
        insurer = c.getInsurer();
        cvdamage = c.getCustomerVehicleDamage();
        eReport = c.getEngineeringReport();
        customerVClass = c.getVClass();
        hireVClass = hireDetail.getVClass();

        cCalc = ClaimCalcHelper.getInstance(claim);
        iCalc = InvoiceCalcHelper.getInstance(invoice, vatRate);
        bandCalc = CHOBandCalcHelper.getInstance(choBand);
    }

    public RulesEngineResponse ResolveStatus() {
        
        RulesEngineResponse response = new RulesEngineResponse();
        RuleEvaluationResult resRule1 = execute(new HasAllowedVehicleClass());
        response.addRuleEvaulation(resRule1);

        return response;
    }

    /*--------------- business rules on Claim ----------------------------------*/    //rule 1.
    public boolean hasAllowedVehicleClass() {
        return hireVClass.getPrice().compareTo(customerVClass.getPrice()) <= 0;
    }

    //rule 2.
    public boolean hasCalculatedCorrectDailyRate() {
        return cCalc.getDailyHireRateChargedWithToleranceDeduction().compareTo(customerVClass.getPrice()) <= 0;
    }

    //rule 3.
    public boolean hireNetDoesNotExceedBandHireNetCeiling() {
        return invoice.getHireNet().compareTo(choBand.getHireNetCeiling()) <= 0;
    }

    //rule 4.
    public boolean hireDayCountDoesNotExceedBandHireDayCeiling() {
        return hireDetail.getNumberOfHireDays() <= choBand.getHireDayCeiling();
    }

    //rule 5.
    public boolean hasCorrectHireGrossCalculation() {
        return CalcHelper.EqualTo(invoice.getHireGross(), iCalc.getCalculatedHireGross());
    }

    //rule 6.
    public boolean actualHireDaysDoesNotExceedAllowableHireDays() {
        return hireDetail.getNumberOfHireDays() <= cCalc.getAllowedDays();
    }

    //rule 7.
    public boolean actualHireDaysDoesNotExceedTotalLossInspection() {
        return hireDetail.getNumberOfHireDays() <= bandCalc.getTotalLossInspectionDays();

    }

    //rule 8.
    public boolean repairGrossIsLessThanEstimatedTotalRepairAmount() {
        if (eReport.getEstimatedTotalRepairAmount().compareTo(BigDecimal.ZERO) > 0) {
            return invoice.getRepairGross().compareTo(eReport.getEstimatedTotalRepairAmount()) <= 0;
        } else {
            return CalcHelper.EqualTo(invoice.getRepairGross(), BigDecimal.ZERO);
            //return invoice.getRepairGross().compareTo(BigDecimal.ZERO) == 0;
        }
    }

    //rule 9.
    public boolean hasCorrectHireVatCalculation() {

        return CalcHelper.EqualTo(invoice.getHireVat(), iCalc.getCalculatedHireVat());

    }

    //rule 10.
    public boolean hasCorrectRepairVatCalculation() {
        return CalcHelper.EqualTo(invoice.getRepairVat(), iCalc.getCalculatedRepairVat());
    }

    //rule 11.
    public boolean hasCorrectRepairGrossCalculation() {
        return CalcHelper.EqualTo(invoice.getRepairGross(), iCalc.getCalculatedRepairGross());
    }

    //rule 12.
    public boolean hasCorrectTotalNet() {
        return CalcHelper.EqualTo(invoice.getTotalNet(), iCalc.getCalculatedTotalNet());
    }

    //rule 13.
    public boolean hasCorrectTotalVat() {
        return CalcHelper.EqualTo(invoice.getTotalVat(), iCalc.getCalculatedTotalVat());
    }

    //rule 14
    public boolean hasCalculatedTotalGrossEqualSuppliedTotalGross() {
        return CalcHelper.EqualTo(invoice.getTotalGross(), iCalc.getCalculatedTotalGross());
    }

    //rule 15.
    public boolean hasCorrectDiscountForNonDA() throws InvalidTestException {
        if (claim.getCHOrg().getIsDelegatedAuthority()) {
            throw new InvalidTestException("Not a valid test for DA CHO's");
        }
        return CalcHelper.EqualTo(invoice.getDiscount(),
                (insurer.getAdminHandlingCharge()).multiply(vatRate).negate());
    }

    //rule 16.
    public boolean handlingAmountAndDeductionBothEqualZeroForDA() throws InvalidTestException {
        if (claim.getCHOrg().getIsDelegatedAuthority()) {
            throw new InvalidTestException("Not a valid test for DA CHO's");
        }
        
        boolean result = CalcHelper.EqualTo(invoice.getClaimsHandlingInvoiceAmount(), BigDecimal.ZERO);
        return result && CalcHelper.EqualTo(invoice.getDeductionForClaimsHandlingFee(), BigDecimal.ZERO);
      
    }

    //rule 17.
    public boolean claimHasZeroDiscountForNonDA() throws InvalidTestException {
        if (!claim.getCHOrg().getIsDelegatedAuthority()) {

            throw new InvalidTestException("Invalid Test. CHO is NOT a DA.");
        }
        return CalcHelper.EqualTo(invoice.getDiscount(), BigDecimal.ZERO);
        //return this.invoice.getDiscount().compareTo(BigDecimal.ZERO) == 0;
    }
    //rule 18.
    public boolean handlingInvoiceAmountAddedToDeductionForHandlingFeeEqualsZero() {

        BigDecimal sum = invoice.getClaimsHandlingInvoiceAmount().add(invoice.getDeductionForClaimsHandlingFee());
        return CalcHelper.EqualTo(sum, BigDecimal.ZERO);
        //return sum.compareTo(BigDecimal.ZERO) == 0;
    }

    //rule 19.
    public boolean hasSuppliedCorrectTotalToPay() {
        return CalcHelper.EqualTo(iCalc.getCalculatedTotalToPay(), invoice.getTotalToPay());
    }

    //rule 20.
    public boolean estimatedRepairDaysPlusBandDaysDoNotExceedHireDays() throws InvalidTestException {

        if (claim.getHireDetail().getIsTotalLoss()) {
            throw new InvalidTestException("Not a valid test for a Total Loss Claim");
        }

        int hireDays = claim.getHireDetail().getNumberOfHireDays();
        int takeVehicleToGarageDays = cvdamage.getIsUsable()
                ? choBand.getTakeVehicleToGarageDaysMobile()
                : choBand.getTakeVehicleToGarageDaysNonMobile();

        if (eReport.getEstimatedDaysUnderRepair() < 1) {
            throw new InvalidTestException("No value has been supplied for ClaimEngineeringReport.EstimatedDaysUnderRepair");
        }

        int maxDays = eReport.getEstimatedDaysUnderRepair();

        maxDays += takeVehicleToGarageDays;
        maxDays += choBand.getWeekendBufferDays();
        maxDays += choBand.getTakeVehicleOutDays();
        maxDays += choBand.getEngineerInspectionDelayDays();

        return hireDays <= maxDays;

    }
}
