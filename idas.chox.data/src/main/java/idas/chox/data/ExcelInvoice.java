package idas.chox.data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Map;

public class ExcelInvoice {

    String claimStatus;
    String choReference;
    String thirdPartyClaimReference;
    Date createdDate;
    Date autoPenaltyStart;
    BigDecimal miscellaneousFee;
    BigDecimal automaticFee;
    Short automaticQty;
    BigDecimal additionalDriverFee;
    Short additionalDriverQty;
    BigDecimal satNavFee;
    Short satNavQty;
    BigDecimal estateFee;
    Short estateQty;
    BigDecimal babySeatFee;
    Short babySeatQty;
    BigDecimal towBarsFee;
    Short towBarsQty;
    BigDecimal nonStandardInsurancePremiumFee;
    Short nonStandardInsurancePremiumQty;
    Boolean coverNoteRequired;
    String coverNoteRequiredDesc;
    BigDecimal adminFee;
    Short adminQty;
    BigDecimal roofRackFee;
    Short roofRackQty;
    BigDecimal dualControlFee;
    Short dualControlQty;
    BigDecimal deliveryCollectionFee;
    Short deliveryCollectionQty;
    BigDecimal excessAmountCollected;
    BigDecimal vatAmountCollected;
    String handlingInvoiceNo;
    BigDecimal claimsHandlingInvoiceAmount;
    String claimInvoiceNo;
    BigDecimal hireRateChargedPerDay;
    BigDecimal hireNet;
    BigDecimal hireVat;
    BigDecimal hireGross;
    BigDecimal repairNet;
    BigDecimal repairVat;
    BigDecimal repairGross;
    BigDecimal engineerFeeNet;
    BigDecimal engineerFeeVat;
    BigDecimal engineerFeeGross;
    BigDecimal totalLossFeeNet;
    BigDecimal totalLossFeeVat;
    BigDecimal totalLossFeeGross;
    BigDecimal storageRecoveryNet;
    BigDecimal storageRecoveryVat;
    BigDecimal storageRecoveryGross;
    BigDecimal deductionForClaimsHandlingFee;
    BigDecimal hirePenaltyCharge;
    String hirePenaltyPercentageString;
    BigDecimal repairPenaltyCharge;
    String repairPenaltyPercentageString;
    BigDecimal totalPenaltyCharge;
    BigDecimal totalNet;
    BigDecimal totalVat;
    BigDecimal totalGross;
    BigDecimal discount;
    BigDecimal insurerDiscount;
    BigDecimal fullTotalToPay;
    BigDecimal fullTotalToPayOriginal;
    BigDecimal totalToPay;
    BigDecimal totalToPayOriginal;
    BigDecimal interimPaymentMade;
    BigDecimal interimPaymentReceived;
    Date dateInvoiced;
    BigDecimal hireGrossPaid;
    BigDecimal repairGrossPaid;
    BigDecimal engineerFeeGrossPaid;
    BigDecimal totalLossFeeGrossPaid;
    BigDecimal storageRecoveryGrossPaid;
    BigDecimal hirePenaltyChargePaid;
    BigDecimal repairPenaltyChargePaid;
    BigDecimal claimHandlerChargePaid;
    BigDecimal deductionClaimHandlerFeePaid;
    BigDecimal choDiscountFeePaid;
    BigDecimal insurerDiscountFeePaid;
    BigDecimal finalPayment;

    public ExcelInvoice(Map data, boolean isCHO) {
        claimStatus = (String) data.get("claimstatus");
        choReference = (String) data.get("choreference");
        thirdPartyClaimReference = (String) data.get("thirdpartyclaimceference");
        createdDate = (Date) data.get("createddate");
        autoPenaltyStart = (Date) data.get("autopenaltystart");
        miscellaneousFee = (BigDecimal) data.get("miscellaneousfee");
        automaticFee = (BigDecimal) data.get("automaticfee");
        automaticQty = (Short) data.get("automaticqty");
        additionalDriverFee = (BigDecimal) data.get("additionaldriverfee");
        additionalDriverQty = (Short) data.get("additionaldriverqty");
        satNavFee = (BigDecimal) data.get("satnavfee");
        satNavQty = (Short) data.get("satnavqty");
        estateFee = (BigDecimal) data.get("estatefee");
        estateQty = (Short) data.get("estateqty");
        babySeatFee = (BigDecimal) data.get("babyseatfee");
        babySeatQty = (Short) data.get("babyseatqty");
        towBarsFee = (BigDecimal) data.get("towbarsfee");
        towBarsQty = (Short) data.get("towbarsqty");
        nonStandardInsurancePremiumFee = (BigDecimal) data.get("nonstandardinsurancepremiumfee");
        nonStandardInsurancePremiumQty = (Short) data.get("nonstandardinsurancepremiumqty");
        coverNoteRequired = (Boolean) data.get("covernoterequired");
        if (coverNoteRequired == null)
            coverNoteRequiredDesc = "";
        else
            coverNoteRequiredDesc = coverNoteRequired ? "Yes" : "No";
        adminFee = (BigDecimal) data.get("adminfee");
        adminQty = (Short) data.get("adminqty");
        roofRackFee = (BigDecimal) data.get("roofrackfee");
        roofRackQty = (Short) data.get("roofrackqty");
        dualControlFee = (BigDecimal) data.get("dualcontrolfee");
        dualControlQty = (Short) data.get("dualcontrolqty");
        deliveryCollectionFee = (BigDecimal) data.get("deliverycollectionfee");
        deliveryCollectionQty = (Short) data.get("deliverycollectionqty");
        excessAmountCollected = (BigDecimal) data.get("excessamountcollected");
        vatAmountCollected = (BigDecimal) data.get("vatamountcollected");
        handlingInvoiceNo = (String) data.get("handlinginvoiceno");
        claimsHandlingInvoiceAmount = (BigDecimal) data.get("claimshandlinginvoiceamount");
        claimInvoiceNo = (String) data.get("claiminvoiceno");
        hireRateChargedPerDay = (BigDecimal) data.get("hireratechargedperday");
        hireNet = (BigDecimal) data.get("hirenet");
        hireVat = (BigDecimal) data.get("hirevat");
        hireGross = (BigDecimal) data.get("hiregross");
        repairNet = (BigDecimal) data.get("repairnet");
        repairVat = (BigDecimal) data.get("repairvat");
        repairGross = (BigDecimal) data.get("repairgross");
        engineerFeeNet = (BigDecimal) data.get("engineerfeenet");
        engineerFeeVat = (BigDecimal) data.get("engineerfeevat");
        engineerFeeGross = (BigDecimal) data.get("engineerfeegross");
        totalLossFeeNet = (BigDecimal) data.get("totallossfeenet");
        totalLossFeeVat = (BigDecimal) data.get("totallossfeevat");
        totalLossFeeGross = (BigDecimal) data.get("totallossfeegross");
        storageRecoveryNet = (BigDecimal) data.get("storagerecoverynet");
        storageRecoveryVat = (BigDecimal) data.get("storagerecoveryvat");
        storageRecoveryGross = (BigDecimal) data.get("storagerecoverygross");
        deductionForClaimsHandlingFee = (BigDecimal) data.get("deductionforclaimshandlingfee");
        hirePenaltyCharge = (BigDecimal) data.get("hirepenaltycharge");
        repairPenaltyCharge = (BigDecimal) data.get("repairpenaltycharge");
        hirePenaltyPercentageString = (String) data.get("hirepenaltypercentage");
        repairPenaltyPercentageString = (String) data.get("repairpenaltypercentage");
        if (!isCHO && hirePenaltyPercentageString != null && hirePenaltyCharge != null && hireGross != null && hireGross.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal actualPercentage = hirePenaltyCharge.multiply(BigDecimal.valueOf(100)).divide((hireGross), 2, RoundingMode.HALF_UP);
            hirePenaltyPercentageString = hirePenaltyPercentageString.concat(" [actual:" + actualPercentage.toString() + "%]");
        }
        if (!isCHO && repairPenaltyPercentageString != null && repairPenaltyCharge != null && repairGross != null && repairGross.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal actualPercentage = repairPenaltyCharge.multiply(BigDecimal.valueOf(100)).divide((repairGross), 2, RoundingMode.HALF_UP);
            repairPenaltyPercentageString = repairPenaltyPercentageString.concat(" [actual:" + actualPercentage.toString() + "%]");
        }

        totalPenaltyCharge = (BigDecimal) data.get("totalpenaltycharge");
        totalNet = (BigDecimal) data.get("totalnet");
        totalVat = (BigDecimal) data.get("totalvat");
        totalGross = (BigDecimal) data.get("totalgross");
        discount = (BigDecimal) data.get("discount");
        insurerDiscount = (BigDecimal) data.get("insurerdiscount");
        fullTotalToPay = (BigDecimal) data.get("fulltotaltopay");
        fullTotalToPayOriginal = (BigDecimal) data.get("original_fulltotaltopay");
        totalToPay = (BigDecimal) data.get("totaltopay");
        totalToPayOriginal = (BigDecimal) data.get("original_totaltopay");
        interimPaymentMade = (BigDecimal) data.get("interimpaymentmade");
        interimPaymentReceived = (BigDecimal) data.get("interimpaymentreceived");
        dateInvoiced = (Date) data.get("dateinvoiced");
        hireGrossPaid = (BigDecimal) data.get("hiregrosspaid");
        repairGrossPaid = (BigDecimal) data.get("repairgrosspaid");
        engineerFeeGrossPaid = (BigDecimal) data.get("engineerfeegrosspaid");
        totalLossFeeGrossPaid = (BigDecimal) data.get("totallossfeegrosspaid");
        storageRecoveryGrossPaid = (BigDecimal) data.get("storagerecoverygrosspaid");
        hirePenaltyChargePaid = (BigDecimal) data.get("hirepenaltychargepaid");
        repairPenaltyChargePaid = (BigDecimal) data.get("repairpenaltychargepaid");
        claimHandlerChargePaid = (BigDecimal) data.get("claimhandlerchargepaid");
        deductionClaimHandlerFeePaid = (BigDecimal) data.get("deductionclaimhandlerfeepaid");
        choDiscountFeePaid = (BigDecimal) data.get("chodiscountfeepaid");
        insurerDiscountFeePaid = (BigDecimal) data.get("insurerdiscountfeepaid");
        finalPayment = (BigDecimal) data.get("finalpayment");
    }

    /*
     * private String claimStatus; private String choReference; private String
     * thirdPartyClaimReference; private Invoice invoice; private String
     * hirePenaltyPercentageString; private String
     * repairPenaltyPercentageString;
     *
     * public String getChoReference() { return choReference; }
     *
     * public void setChoReference(String choReference) { this.choReference =
     * choReference; }
     *
     * public String getClaimStatus() { return claimStatus; }
     *
     * public void setClaimStatus(String claimStatus) { this.claimStatus =
     * claimStatus; }
     *
     * public Invoice getInvoice() { return invoice; }
     *
     * public void setInvoice(Invoice invoice) { this.invoice = invoice; }
     *
     * public String getThirdPartyClaimReference() { return
     * thirdPartyClaimReference; }
     *
     * public void setThirdPartyClaimReference(String thirdPartyClaimReference)
     * { this.thirdPartyClaimReference = thirdPartyClaimReference; }
     *
     * public String getHirePenaltyPercentageString() { return
     * hirePenaltyPercentageString; }
     *
     * public void setHirePenaltyPercentageString(String
     * hirePenaltyPercentageString) { this.hirePenaltyPercentageString =
     * hirePenaltyPercentageString; }
     *
     * public String getRepairPenaltyPercentageString() { return
     * repairPenaltyPercentageString; }
     *
     * public void setRepairPenaltyPercentageString(String
     * repairPenaltyPercentageString) { this.repairPenaltyPercentageString =
     * repairPenaltyPercentageString; }
     */
    public BigDecimal getAdditionalDriverFee() {
        return additionalDriverFee;
    }

    public Short getAdditionalDriverQty() {
        return additionalDriverQty;
    }

    public BigDecimal getAdminFee() {
        return adminFee;
    }

    public Short getAdminQty() {
        return adminQty;
    }

    public Date getAutoPenaltyStart() {
        return autoPenaltyStart;
    }

    public BigDecimal getAutomaticFee() {
        return automaticFee;
    }

    public Short getAutomaticQty() {
        return automaticQty;
    }

    public BigDecimal getBabySeatFee() {
        return babySeatFee;
    }

    public Short getBabySeatQty() {
        return babySeatQty;
    }

    public BigDecimal getChoDiscountFeePaid() {
        return choDiscountFeePaid;
    }

    public String getChoReference() {
        return choReference;
    }

    public BigDecimal getClaimHandlerChargePaid() {
        return claimHandlerChargePaid;
    }

    public String getClaimInvoiceNo() {
        return claimInvoiceNo;
    }

    public String getClaimStatus() {
        return claimStatus;
    }

    public BigDecimal getClaimsHandlingInvoiceAmount() {
        return claimsHandlingInvoiceAmount;
    }

    public String getCoverNoteRequiredDesc() {
        return coverNoteRequiredDesc;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public Date getDateInvoiced() {
        return dateInvoiced;
    }

    public BigDecimal getDeductionClaimHandlerFeePaid() {
        return deductionClaimHandlerFeePaid;
    }

    public BigDecimal getDeductionForClaimsHandlingFee() {
        return deductionForClaimsHandlingFee;
    }

    public BigDecimal getDeliveryCollectionFee() {
        return deliveryCollectionFee;
    }

    public Short getDeliveryCollectionQty() {
        return deliveryCollectionQty;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public BigDecimal getDualControlFee() {
        return dualControlFee;
    }

    public Short getDualControlQty() {
        return dualControlQty;
    }

    public BigDecimal getEngineerFeeGross() {
        return engineerFeeGross;
    }

    public BigDecimal getEngineerFeeGrossPaid() {
        return engineerFeeGrossPaid;
    }

    public BigDecimal getEngineerFeeNet() {
        return engineerFeeNet;
    }

    public BigDecimal getEngineerFeeVat() {
        return engineerFeeVat;
    }

    public BigDecimal getEstateFee() {
        return estateFee;
    }

    public Short getEstateQty() {
        return estateQty;
    }

    public BigDecimal getExcessAmountCollected() {
        return excessAmountCollected;
    }

    public BigDecimal getFinalPayment() {
        return finalPayment;
    }

    public BigDecimal getFullTotalToPay() {
        return fullTotalToPay;
    }

    public BigDecimal getFullTotalToPayOriginal() {
        return fullTotalToPayOriginal;
    }

    public String getHandlingInvoiceNo() {
        return handlingInvoiceNo;
    }

    public BigDecimal getHireGross() {
        return hireGross;
    }

    public BigDecimal getHireGrossPaid() {
        return hireGrossPaid;
    }

    public BigDecimal getHireNet() {
        return hireNet;
    }

    public BigDecimal getHirePenaltyCharge() {
        return hirePenaltyCharge;
    }

    public BigDecimal getHirePenaltyChargePaid() {
        return hirePenaltyChargePaid;
    }

    public String getHirePenaltyPercentageString() {
        return hirePenaltyPercentageString;
    }

    public BigDecimal getHireRateChargedPerDay() {
        return hireRateChargedPerDay;
    }

    public BigDecimal getHireVat() {
        return hireVat;
    }

    public BigDecimal getInsurerDiscount() {
        return insurerDiscount;
    }

    public BigDecimal getInsurerDiscountFeePaid() {
        return insurerDiscountFeePaid;
    }

    public BigDecimal getInterimPaymentMade() {
        return interimPaymentMade;
    }

    public BigDecimal getInterimPaymentReceived() {
        return interimPaymentReceived;
    }

    public BigDecimal getMiscellaneousFee() {
        return miscellaneousFee;
    }

    public BigDecimal getNonStandardInsurancePremiumFee() {
        return nonStandardInsurancePremiumFee;
    }

    public Short getNonStandardInsurancePremiumQty() {
        return nonStandardInsurancePremiumQty;
    }

    public BigDecimal getRepairGross() {
        return repairGross;
    }

    public BigDecimal getRepairGrossPaid() {
        return repairGrossPaid;
    }

    public BigDecimal getRepairNet() {
        return repairNet;
    }

    public BigDecimal getRepairPenaltyCharge() {
        return repairPenaltyCharge;
    }

    public BigDecimal getRepairPenaltyChargePaid() {
        return repairPenaltyChargePaid;
    }

    public String getRepairPenaltyPercentageString() {
        return repairPenaltyPercentageString;
    }

    public BigDecimal getRepairVat() {
        return repairVat;
    }

    public BigDecimal getRoofRackFee() {
        return roofRackFee;
    }

    public Short getRoofRackQty() {
        return roofRackQty;
    }

    public BigDecimal getSatNavFee() {
        return satNavFee;
    }

    public Short getSatNavQty() {
        return satNavQty;
    }

    public BigDecimal getStorageRecoveryGross() {
        return storageRecoveryGross;
    }

    public BigDecimal getStorageRecoveryGrossPaid() {
        return storageRecoveryGrossPaid;
    }

    public BigDecimal getStorageRecoveryNet() {
        return storageRecoveryNet;
    }

    public BigDecimal getStorageRecoveryVat() {
        return storageRecoveryVat;
    }

    public String getThirdPartyClaimReference() {
        return thirdPartyClaimReference;
    }

    public BigDecimal getTotalGross() {
        return totalGross;
    }

    public BigDecimal getTotalLossFeeGross() {
        return totalLossFeeGross;
    }

    public BigDecimal getTotalLossFeeGrossPaid() {
        return totalLossFeeGrossPaid;
    }

    public BigDecimal getTotalLossFeeNet() {
        return totalLossFeeNet;
    }

    public BigDecimal getTotalLossFeeVat() {
        return totalLossFeeVat;
    }

    public BigDecimal getTotalNet() {
        return totalNet;
    }

    public BigDecimal getTotalPenaltyCharge() {
        return totalPenaltyCharge;
    }

    public BigDecimal getTotalToPay() {
        return totalToPay;
    }

    public BigDecimal getTotalToPayOriginal() {
        return totalToPayOriginal;
    }

    public BigDecimal getTotalVat() {
        return totalVat;
    }

    public BigDecimal getTowBarsFee() {
        return towBarsFee;
    }

    public Short getTowBarsQty() {
        return towBarsQty;
    }

    public BigDecimal getVatAmountCollected() {
        return vatAmountCollected;
    }
}
