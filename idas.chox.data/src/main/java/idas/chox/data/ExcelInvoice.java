package idas.chox.data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author John
 */
public class ExcelInvoice {

    private static final Logger LOG = LoggerFactory.getLogger(ExcelInvoice.class);

    private String claimStatus;
    private String choReference;
    private String claimnumber;
    private Date createdDate;
    private Date autoPenaltyStart;
    private BigDecimal collaborationFee;
    private Short collaborationQty;
    private BigDecimal miscellaneousFee;
    private BigDecimal automaticFee;
    private Short automaticQty;
    private BigDecimal additionalDriverFee;
    private Short additionalDriverQty;
    private BigDecimal satNavFee;
    private Short satNavQty;
    private BigDecimal estateFee;
    private Short estateQty;
    private BigDecimal babySeatFee;
    private Short babySeatQty;
    private BigDecimal towBarsFee;
    private Short towBarsQty;
    private BigDecimal nonStandardInsurancePremiumFee;
    private Short nonStandardInsurancePremiumQty;
    private BigDecimal vedFee;
    private Short vedQty;
    private Boolean coverNoteRequired;
    private String coverNoteRequiredDesc;
    private BigDecimal adminFee;
    private Short adminQty;
    private BigDecimal roofRackFee;
    private Short roofRackQty;
    private BigDecimal dualControlFee;
    private Short dualControlQty;
    private BigDecimal deliveryCollectionFee;
    private Short deliveryCollectionQty;
    private BigDecimal excessAmountCollected;
    private BigDecimal vatAmountCollected;
    private String handlingInvoiceNo;
    private BigDecimal claimsHandlingInvoiceAmount;
    private String claimInvoiceNo;
    private BigDecimal hireRateChargedPerDay;
    private BigDecimal hireNet;
    private BigDecimal hireVat;
    private BigDecimal hireGross;
    private BigDecimal repairNet;
    private BigDecimal repairVat;
    private BigDecimal repairGross;
    private BigDecimal engineerFeeNet;
    private BigDecimal engineerFeeVat;
    private BigDecimal engineerFeeGross;
    private BigDecimal totalLossFeeNet;
    private BigDecimal totalLossFeeVat;
    private BigDecimal totalLossFeeGross;
    private BigDecimal storageRecoveryNet;
    private BigDecimal storageRecoveryVat;
    private BigDecimal storageRecoveryGross;
    private BigDecimal deductionForClaimsHandlingFee;
    private BigDecimal hirePenaltyCharge;
    private String hirePenaltyPercentageString;
    private BigDecimal repairPenaltyCharge;
    private String repairPenaltyPercentageString;
    private BigDecimal totalPenaltyCharge;
    private BigDecimal totalNet;
    private BigDecimal totalVat;
    private BigDecimal totalGross;
    private BigDecimal discount;
    private BigDecimal insurerDiscount;
    private BigDecimal gtaDiscount;
    private BigDecimal fullTotalToPay;
    private BigDecimal fullTotalToPayOriginal;
    private BigDecimal totalToPay;
    private BigDecimal totalToPayOriginal;
    private BigDecimal interimPaymentMade;
    private BigDecimal interimPaymentReceived;
    private Date dateInvoiced;
    private BigDecimal hireGrossPaid;
    private BigDecimal repairGrossPaid;
    private BigDecimal engineerFeeGrossPaid;
    private BigDecimal totalLossFeeGrossPaid;
    private BigDecimal storageRecoveryGrossPaid;
    private BigDecimal hirePenaltyChargePaid;
    private BigDecimal repairPenaltyChargePaid;
    private BigDecimal claimHandlerChargePaid;
    private BigDecimal deductionClaimHandlerFeePaid;
    private BigDecimal choDiscountFeePaid;
    private BigDecimal insurerDiscountFeePaid;
    private BigDecimal finalPayment;
    private BigDecimal repairAdminFee;
    private BigDecimal repairAcquisitionFee;
    private BigDecimal repairParts;
    private BigDecimal repairLabour;
    private BigDecimal repairMaterials;
    private BigDecimal repairSpecialist;
    private Boolean paymentsTeam;
    private String choName;

    public ExcelInvoice(Map data, boolean isCHO) {
        boolean hireCommercial = false;
        boolean repairCommercial = false;
        claimStatus = (String) data.get("claimstatus");
        choReference = (String) data.get("choreference");
        claimnumber = (String) data.get("claimnumber");
        createdDate = (Date) data.get("createddate");
        autoPenaltyStart = (Date) data.get("autopenaltystart");
        collaborationFee = (BigDecimal) data.get("collaborationfee");
        collaborationQty = (Short) data.get("collaborationqty");
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
        vedFee = (BigDecimal) data.get("vedfee");
        vedQty = (Short) data.get("vedqty");
        coverNoteRequired = (Boolean) data.get("covernoterequired");
        if (coverNoteRequired == null) {
            coverNoteRequiredDesc = "";
        } else {
            coverNoteRequiredDesc = coverNoteRequired ? "Yes" : "No";
        }
        repairAdminFee = (BigDecimal) data.get("repairadminfee");
        repairAcquisitionFee = (BigDecimal) data.get("repairacquisitionfee");
        repairParts = (BigDecimal) data.get("repairparts");
        repairLabour = (BigDecimal) data.get("repairlabour");
        repairMaterials = (BigDecimal) data.get("repairmaterials");
        repairSpecialist = (BigDecimal) data.get("repairspecialist");
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
        if (hirePenaltyPercentageString != null && hirePenaltyPercentageString.startsWith("Commercial")) {
            hirePenaltyPercentageString = hirePenaltyPercentageString.replaceAll("%", "");
            hireCommercial = true;
        }
        repairPenaltyPercentageString = (String) data.get("repairpenaltypercentage");
        if (repairPenaltyPercentageString != null && repairPenaltyPercentageString.startsWith("Commercial")) {
            repairPenaltyPercentageString = hirePenaltyPercentageString.replaceAll("%", "");
            repairCommercial = true;
        }
        if (!isCHO && hirePenaltyCharge != null && hireGross != null && hirePenaltyCharge.compareTo(BigDecimal.ZERO) > 0 && hireGross.compareTo(BigDecimal.ZERO) > 0) {
            if (hirePenaltyPercentageString != null) {
                try {
                    BigDecimal givenPercentage = null;
                    if (!hireCommercial) {
                        givenPercentage = new BigDecimal(hirePenaltyPercentageString.replaceAll("%", ""));
                    }
                    BigDecimal actualPercentage = hirePenaltyCharge.multiply(BigDecimal.valueOf(100)).divide((hireGross), 2, RoundingMode.HALF_UP);
                    if (hireCommercial || actualPercentage.compareTo(givenPercentage) != 0) {
                        hirePenaltyPercentageString = hirePenaltyPercentageString.concat(" [actual:" + actualPercentage.toString() + "%]");
                    }
                } catch (Exception ex) {
                    LOG.error("Error determining actual hire penalty % for string {}: ", hirePenaltyPercentageString, ex);
                }
            } else {
                LOG.warn("hirePenaltyPercentageString is null but hirePenaltyCharge='{}'", hirePenaltyCharge);
            }
        }

        if (!isCHO && repairPenaltyCharge != null && repairGross != null && repairPenaltyCharge.compareTo(BigDecimal.ZERO) > 0 && repairGross.compareTo(BigDecimal.ZERO) > 0) {
            if (repairPenaltyPercentageString != null) {
                try {
                    BigDecimal givenPercentage = null;
                    if (!repairCommercial) {
                        givenPercentage = new BigDecimal(repairPenaltyPercentageString.replaceAll("%", ""));
                    }
                    BigDecimal actualPercentage = repairPenaltyCharge.multiply(BigDecimal.valueOf(100)).divide((repairGross), 2, RoundingMode.HALF_UP);
                    if (repairCommercial || actualPercentage.compareTo(givenPercentage) != 0) {
                        LOG.debug("Repair penalty string is '{}', actual is '{}'", repairPenaltyPercentageString, actualPercentage.toString());
                        repairPenaltyPercentageString = repairPenaltyPercentageString.concat(" [actual:" + actualPercentage.toString() + "%]");
                    }
                } catch (Exception ex) {
                    LOG.error("Error determining actual repair penalty % for string {}: ", repairPenaltyPercentageString, ex);
                }
            } else {
                LOG.warn("repairPenaltyPercentageString is null but hirePenaltyCharge='{}'", repairPenaltyCharge);
            }
        }

        totalPenaltyCharge = (BigDecimal) data.get("totalpenaltycharge");
        totalNet = (BigDecimal) data.get("totalnet");
        totalVat = (BigDecimal) data.get("totalvat");
        totalGross = (BigDecimal) data.get("totalgross");
        discount = (BigDecimal) data.get("discount");
        insurerDiscount = (BigDecimal) data.get("insurerdiscount");
        gtaDiscount = (BigDecimal) data.get("gtadiscount");
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
        paymentsTeam = (Boolean) data.get("paymentsteam");
        choName = (String) data.get("choname");
    }

    public String getChoName() {
        return choName;
    }

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

    public BigDecimal getGtaDiscount() {
        return gtaDiscount;
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

    public BigDecimal getCollaborationFee() {
        return collaborationFee;
    }

    public Short getCollaborationQty() {
        return collaborationQty;
    }

    public Boolean getCoverNoteRequired() {
        return coverNoteRequired;
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

    public String getClaimnumber() {
        return claimnumber;
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

    public BigDecimal getVedFee() {
        return vedFee;
    }

    public Short getVedQty() {
        return vedQty;
    }

    public BigDecimal getVatAmountCollected() {
        return vatAmountCollected;
    }

    public BigDecimal getRepairAdminFee() {
        return repairAdminFee;
    }

    public BigDecimal getRepairAcquisitionFee() {
        return repairAcquisitionFee;
    }

    public BigDecimal getRepairParts() {
        return repairParts;
    }

    public BigDecimal getRepairLabour() {
        return repairLabour;
    }

    public BigDecimal getRepairMaterials() {
        return repairMaterials;
    }

    public BigDecimal getRepairSpecialist() {
        return repairSpecialist;
    }

    public Boolean getPaymentsTeam() {
        return paymentsTeam;
    }

    public String getPaymentsTeamDesc() {
        if (paymentsTeam == null) {
            return "";
        }

        return paymentsTeam ? "Yes" : "No";
    }
}
