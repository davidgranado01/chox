/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

/**
 *
 * @author seeni
 */
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.ReasonOfRejection;
import idas.chox.core.model.VehicleClass;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import idas.chox.core.services.LookupService;
import idas.chox.core.services.ClaimService;
import idas.chox.service.security.ApplicationAccessibility;
import idas.chox.core.model.Claim;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvoiceRecalculationAction extends BaseAction implements Preparable {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceRecalculationAction.class);
    private InvoiceAction invoiceAction = new InvoiceAction();
    private VehicleHireAction vehicleHireAction = new VehicleHireAction();
    private EngineerReportAction engineerReportAction = new EngineerReportAction();
    private int claimId = 0;
    private ClaimService claimService;
    private LookupService lookupService;
    private String actionResult;
    private ApplicationAccessibility applicationAccessibility;
    private Claim claim;
    private Map session = ActionContext.getContext().getSession();
    public static final String READ_ONLY = "r";
    public static final String EDITABLE = "w";
    public static final String DECLINE = "decline";

    public int getClaimId() {
        return claimId;
    }

    public void setClaimId(int claimId) {
        this.claimId = claimId;
    }

    public String getClaimStatus() {
        return invoiceAction.getClaimStatus();
    }

    public void setLookupService(LookupService lookupService) {

        this.lookupService = lookupService;
        LOG.debug("lookupService is being called");
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
        LOG.debug("claimservice is being called");
    }

    @Override
    public String getActionResult() {

        return this.actionResult;
    }

    @Override
    public void setActionResult(String actionResult) {

        this.actionResult = actionResult;
    }

    public void setApplicationAccessibility(ApplicationAccessibility applicationAccessibility) {
        this.applicationAccessibility = applicationAccessibility;
    }

    // <editor-fold defaultstate="collapsed" desc="Invoice">
    public java.util.Date getDateInvoiced() {
        return invoiceAction.model.getDateInvoiced();
    }

    public void setDateInvoiced(java.util.Date dateInvoiced) {
        invoiceAction.model.setDateInvoiced(dateInvoiced);
    }

    public java.math.BigDecimal getHireNet() {
        return invoiceAction.model.getHireNet();
    }

    public void setHireNet(java.math.BigDecimal hireNet) {
        invoiceAction.model.setHireNet(hireNet);
    }

    public java.math.BigDecimal getHireVat() {
        return invoiceAction.model.getHireVat();
    }

    public void setHireVat(java.math.BigDecimal hireVat) {
        invoiceAction.model.setHireVat(hireVat);
    }

    public java.math.BigDecimal getHireGross() {
        return invoiceAction.model.getHireGross();
    }

    public void setHireGross(java.math.BigDecimal hireGross) {
        invoiceAction.model.setHireGross(hireGross);
    }

    public java.math.BigDecimal getRepairNet() {
        return invoiceAction.model.getRepairNet();
    }

    public void setRepairNet(java.math.BigDecimal repairNet) {
        invoiceAction.model.setRepairNet(repairNet);
    }

    public java.math.BigDecimal getRepairVat() {
        return invoiceAction.model.getRepairVat();
    }

    public void setRepairVat(java.math.BigDecimal repairVat) {
        invoiceAction.model.setRepairVat(repairVat);
    }

    public java.math.BigDecimal getRepairGross() {
        return invoiceAction.model.getRepairGross();
    }

    public void setRepairGross(java.math.BigDecimal repairGross) {
        invoiceAction.model.setRepairGross(repairGross);
    }

    public java.math.BigDecimal getEngineerFeeNet() {
        return invoiceAction.model.getEngineerFeeVat();
    }

    public void setEngineerFeeNet(java.math.BigDecimal engineerFeeNet) {
        invoiceAction.model.setEngineerFeeNet(engineerFeeNet);
    }

    public java.math.BigDecimal getEngineerFeeVat() {
        return invoiceAction.model.getEngineerFeeVat();
    }

    public void setEngineerFeeVat(java.math.BigDecimal engineerFeeVat) {
        invoiceAction.model.setEngineerFeeVat(engineerFeeVat);
    }

    public java.math.BigDecimal getEngineerFeeGross() {
        return invoiceAction.model.getEngineerFeeGross();
    }

    public void setEngineerFeeGross(java.math.BigDecimal engineerFeeGross) {
        invoiceAction.model.setEngineerFeeGross(engineerFeeGross);
    }

    public java.math.BigDecimal getStorageRecoveryNet() {
        return invoiceAction.model.getStorageRecoveryNet();
    }

    public void setStorageRecoveryNet(java.math.BigDecimal storageRecoveryNet) {
        invoiceAction.model.setStorageRecoveryNet(storageRecoveryNet);
    }

    public java.math.BigDecimal getStorageRecoveryVat() {
        return invoiceAction.model.getStorageRecoveryVat();
    }

    public void setStorageRecoveryVat(java.math.BigDecimal storageRecoveryVat) {
        invoiceAction.model.setStorageRecoveryVat(storageRecoveryVat);
    }

    public java.math.BigDecimal getStorageRecoveryGross() {
        return invoiceAction.model.getStorageRecoveryGross();
    }

    public void setStorageRecoveryGross(java.math.BigDecimal storageRecoveryGross) {
        invoiceAction.model.setStorageRecoveryGross(storageRecoveryGross);
    }

    public java.math.BigDecimal getTotalNet() {
        return invoiceAction.model.getTotalNet();
    }

    public void setTotalNet(java.math.BigDecimal totalNet) {
        invoiceAction.model.setTotalNet(totalNet);
    }

    public java.math.BigDecimal getTotalVat() {
        return invoiceAction.model.getTotalVat();
    }

    public void setTotalVat(java.math.BigDecimal totalVat) {
        invoiceAction.model.setTotalVat(totalVat);
    }

    public java.math.BigDecimal getTotalGross() {
        return invoiceAction.model.getTotalGross();
    }

    public void setTotalGross(java.math.BigDecimal totalGross) {
        invoiceAction.model.setTotalGross(totalGross);
    }

    public java.math.BigDecimal getClaimsHandlingInvoiceAmount() {
        return invoiceAction.model.getClaimsHandlingInvoiceAmount();
    }

    public void setClaimsHandlingInvoiceAmount(java.math.BigDecimal claimsHandlingInvoiceAmount) {
        invoiceAction.model.setClaimsHandlingInvoiceAmount(claimsHandlingInvoiceAmount);
    }

    public java.math.BigDecimal getDeductionForClaimsHandlingFee() {
        return invoiceAction.model.getDeductionForClaimsHandlingFee();
    }

    public void setDeductionForClaimsHandlingFee(java.math.BigDecimal deductionForClaimsHandlingFee) {
        invoiceAction.model.setDeductionForClaimsHandlingFee(deductionForClaimsHandlingFee);
    }

    public java.math.BigDecimal getDiscount() {
        return invoiceAction.model.getDiscount();
    }

    public void setDiscount(java.math.BigDecimal discount) {
        invoiceAction.model.setDiscount(discount);
    }

    public java.math.BigDecimal getFullTotalToPay() {
        return invoiceAction.model.getFullTotalToPay();
    }

    public void setFullTotalToPay(java.math.BigDecimal totalToPay) {
        invoiceAction.model.setTotalToPay(totalToPay);
    }

    public java.lang.String getHandlingInvoiceNo() {
        return invoiceAction.model.getHandlingInvoiceNo();
    }

    public void setHandlingInvoiceNo(java.lang.String handlingInvoiceNo) {
        invoiceAction.model.setHandlingInvoiceNo(handlingInvoiceNo);
    }

    public java.lang.String getClaimInvoiceNo() {
        return invoiceAction.model.getClaimInvoiceNo();
    }

    public void setClaimInvoiceNo(java.lang.String claimInvoiceNo) {
        invoiceAction.model.setClaimInvoiceNo(claimInvoiceNo);
    }

    public java.math.BigDecimal getCdwFee() {
        return invoiceAction.model.getCdwFee();
    }

    public void setCdwFee(java.math.BigDecimal cdwFee) {
        invoiceAction.model.setCdwFee(cdwFee);
    }

//    public int getCdwQty() {
//        return invoiceAction.model.getCdwQty();
//    }
//    public void setCdwQty(java.lang.Integer cdwQty) {
//        invoiceAction.model.setCdwQty(cdwQty);
//    }
    public java.math.BigDecimal getAutomaticFee() {
        return invoiceAction.model.getAutomaticFee();
    }

//    public void setAutomaticFee(java.math.BigDecimal automaticFee) {
//        invoiceAction.model.setAutomaticFee(automaticFee);
//    }
//    public int getAutomaticQty() {
//        return invoiceAction.model.getAutomaticQty();
//    }
    ////////     from extra action
    public Integer getEstateQty() {
        return invoiceAction.model.getEstateQty();
    }

    public void setEstateQty(Integer estateQty) {
        invoiceAction.model.setEstateQty(estateQty);
    }

    public Integer getCdwQty() {
        return invoiceAction.model.getCdwQty();
    }

    public void setCdwQty(Integer cdwQty) {
        invoiceAction.model.setCdwQty(cdwQty);
    }

    public Integer getAutomaticQty() {
        return invoiceAction.model.getAutomaticQty();
    }

    public void setAutomaticQty(Integer automaticQty) {
        invoiceAction.model.setAutomaticQty(automaticQty);
    }

    public Integer getSatNavQty() {
        return invoiceAction.model.getSatNavQty();
    }

    public void setSatNavQty(Integer satNavQty) {
        invoiceAction.model.setSatNavQty(satNavQty);
    }

    public Integer getBabySeatQty() {
        return invoiceAction.model.getBabySeatQty();
    }

    public void setBabySeatQty(Integer babySeatQty) {
        invoiceAction.model.setBabySeatQty(babySeatQty);
    }

    public Integer getTowBarsQty() {
        return invoiceAction.model.getTowBarsQty();
    }

    public void setTowBarsQty(Integer towBarsQty) {
        invoiceAction.model.setTowBarsQty(towBarsQty);
    }

    public Integer getNonStandardInsurancePremiumQty() {
        return invoiceAction.model.getNonStandardInsurancePremiumQty();
    }

    public void setNonStandardInsurancePremiumQty(Integer nonStandardInsurancePremiumQty) {
        invoiceAction.model.setNonStandardInsurancePremiumQty(nonStandardInsurancePremiumQty);
    }

    public Integer getAdminQty() {
        return invoiceAction.model.getAdminQty();
    }

    public void setAdminQty(Integer adminQty) {
        invoiceAction.model.setAdminQty(adminQty);
    }

    public Integer getRoofRackQty() {
        return invoiceAction.model.getRoofRackQty();
    }

    public void setRoofRackQty(Integer roofRackQty) {
        invoiceAction.model.setRoofRackQty(roofRackQty);
    }

    public Integer getDualControlQty() {
        return invoiceAction.model.getDualControlQty();
    }

    public void setDualControlQty(Integer dualControlQty) {
        invoiceAction.model.setDualControlQty(dualControlQty);
    }

    public Integer getDeliveryCollectionQty() {
        return invoiceAction.model.getDeliveryCollectionQty();
    }

    public void setDeliveryCollectionQty(Integer deliveryCollectionQty) {
        invoiceAction.model.setDeliveryCollectionQty(deliveryCollectionQty);
    }

/////////// untill here from extra action
//    public void setAutomaticQty(java.lang.Integer automaticQty) {
//        invoiceAction.model.setAutomaticQty(automaticQty);
//    }
    public java.math.BigDecimal getSatNavFee() {
        return invoiceAction.model.getSatNavFee();
    }

    public void setSatNavFee(java.math.BigDecimal satNavFee) {
        invoiceAction.model.setSatNavFee(satNavFee);
    }

//    public int getSatNavQty() {
//        return invoiceAction.model.getSatNavQty();
//    }
//
//    public void setSatNavQty(java.lang.Integer satNavQty) {
//        invoiceAction.model.setSatNavQty(satNavQty);
//    }
    public java.math.BigDecimal getEstateFee() {
        return invoiceAction.model.getEstateFee();
    }

    public void setEstateFee(java.math.BigDecimal estateFee) {
        invoiceAction.model.setEstateFee(estateFee);
    }

//    public int getEstateQty() {
//        return invoiceAction.model.getEstateQty();
//    }
//
//    public void setEstateQty(java.lang.Integer estateQty) {
//        invoiceAction.model.setEstateQty(estateQty);
//    }
    public java.math.BigDecimal getBabySeatFee() {
        return invoiceAction.model.getBabySeatFee();
    }

    public void setBabySeatFee(java.math.BigDecimal babySeatFee) {
        invoiceAction.model.setBabySeatFee(babySeatFee);
    }

//    public int getBabySeatQty() {
//        return invoiceAction.model.getBabySeatQty();
//    }
//
//    public void setBabySeatQty(java.lang.Integer babySeatQty) {
//        invoiceAction.model.setBabySeatQty(babySeatQty);
//    }
    public java.math.BigDecimal getTowBarsFee() {
        return invoiceAction.model.getTowBarsFee();
    }

    public void setTowBarsFee(java.math.BigDecimal towBarsFee) {
        invoiceAction.model.setTowBarsFee(towBarsFee);
    }

//    public int getTowBarsQty() {
//        return invoiceAction.model.getTowBarsQty();
//    }
//
//    public void setTowBarsQty(java.lang.Integer towBarsQty) {
//        invoiceAction.model.setTowBarsQty(towBarsQty);
//    }
    public java.math.BigDecimal getNonStandardInsurancePremiumFee() {
        return invoiceAction.model.getNonStandardInsurancePremiumFee();
    }

    public void setNonStandardInsurancePremiumFee(java.math.BigDecimal nonStandardInsurancePremiumFee) {
        invoiceAction.model.setNonStandardInsurancePremiumFee(nonStandardInsurancePremiumFee);
    }

//    public int getNonStandardInsurancePremiumQty() {
//        return invoiceAction.model.getNonStandardInsurancePremiumQty();
//    }
//
//    public void setNonStandardInsurancePremiumQty(java.lang.Integer nonStandardInsurancePremiumQty) {
//        invoiceAction.model.setNonStandardInsurancePremiumQty(nonStandardInsurancePremiumQty);
//    }
    public java.math.BigDecimal getAdminFee() {
        return invoiceAction.model.getAdminFee();
    }

    public void setAdminFee(java.math.BigDecimal adminFee) {
        invoiceAction.model.setAdminFee(adminFee);
    }

//    public int getAdminQty() {
//        return invoiceAction.model.getAdminQty();
//    }
//
//    public void setAdminQty(java.lang.Integer adminQty) {
//        invoiceAction.model.setAdminQty(adminQty);
//    }
    public java.math.BigDecimal getRoofRackFee() {
        return invoiceAction.model.getRoofRackFee();
    }

    public void setRoofRackFee(java.math.BigDecimal roofRackFee) {
        invoiceAction.model.setRoofRackFee(roofRackFee);
    }

//    public int getRoofRackQty() {
//        return invoiceAction.model.getRoofRackQty();
//    }
//
//    public void setRoofRackQty(java.lang.Integer roofRackQty) {
//        invoiceAction.model.setRoofRackQty(roofRackQty);
//    }
    public java.math.BigDecimal getDualControlFee() {
        return invoiceAction.model.getDualControlFee();
    }

    public void setDualControlFee(java.math.BigDecimal dualControlFee) {
        invoiceAction.model.setDualControlFee(dualControlFee);
    }

//    public int getDualControlQty() {
//        return invoiceAction.model.getDualControlQty();
//    }
//
//    public void setDualControlQty(java.lang.Integer dualControlQty) {
//        invoiceAction.model.setDualControlQty(dualControlQty);
//    }
    public java.math.BigDecimal getDeliveryCollectionFee() {
        return invoiceAction.model.getDeliveryCollectionFee();
    }

    public void setDeliveryCollectionFee(java.math.BigDecimal deliveryCollectionFee) {
        invoiceAction.model.setDeliveryCollectionFee(deliveryCollectionFee);
    }

//    public int getDeliveryCollectionQty() {
//        return invoiceAction.model.getDeliveryCollectionQty();
//    }
//
//    public void setDeliveryCollectionQty(java.lang.Integer deliveryCollectionQty) {
//        invoiceAction.model.setDeliveryCollectionQty(deliveryCollectionQty);
//    }
    public String getEngineerInvoiceReviewNotes() {
        return invoiceAction.model.getEngineerInvoiceReviewNotes();
    }

    public void setEngineerInvoiceReviewNotes(String engineerInvoiceReviewNotes) {
        invoiceAction.model.setEngineerInvoiceReviewNotes(engineerInvoiceReviewNotes);
    }

    public boolean isIsEngineerDecisionApproved() {
        return invoiceAction.model.isIsEngineerDecisionApproved();
    }

    public void setIsEngineerDecisionApproved(boolean isEngineerDecisionApproved) {
        invoiceAction.model.setIsEngineerDecisionApproved(isEngineerDecisionApproved);
    }

    public boolean isIsPaymentMode() {
        return invoiceAction.model.isIsPaymentMode();
    }

    public void setIsPaymentMode(boolean isPaymentMode) {
        invoiceAction.model.setIsPaymentMode(isPaymentMode);
    }

    public String getRejectionReason() {
        return invoiceAction.model.getRejectionReason();
    }

    public void setRejectionReason(String rejectionReason) {
        invoiceAction.model.setRejectionReason(rejectionReason);
    }

    public BigDecimal getHireRateChargedPerDay() {
        return invoiceAction.model.getHireRateChargedPerDay();
    }

    public void setHireRateChargedPerDay(BigDecimal hireRateChargedPerDay) {
        invoiceAction.model.setHireRateChargedPerDay(hireRateChargedPerDay);
    }

    public BigDecimal getExcessAmountCollected() {
        return invoiceAction.model.getExcessAmountCollected();
    }

    public void setExcessAmountCollected(BigDecimal excessAmountCollected) {
        invoiceAction.model.setExcessAmountCollected(excessAmountCollected);
    }

    public BigDecimal getVatAmountCollected() {
        return invoiceAction.model.getVatAmountCollected();
    }

    public void setVatAmountCollected(BigDecimal vatAmountCollected) {
        invoiceAction.model.setVatAmountCollected(vatAmountCollected);
    }

    public BigDecimal getHirePenaltyCharge() {
        return invoiceAction.model.getHirePenaltyCharge();
    }

    public void setHirePenaltyCharge(BigDecimal hirePenaltyCharge) {
        invoiceAction.model.setHirePenaltyCharge(hirePenaltyCharge);

    }

    public BigDecimal getRepairPenaltyCharge() {
        return invoiceAction.model.getRepairPenaltyCharge();
    }

    public void setRepairPenaltyCharge(BigDecimal repairPenaltyCharge) {
        invoiceAction.model.setRepairPenaltyCharge(repairPenaltyCharge);
    }

    public Integer getPenaltyAlertQty() {
        return invoiceAction.model.getPenaltyAlertQty();
    }

    public void setPenaltyAlertQty(Integer penaltyAlertQty) {
        invoiceAction.model.setPenaltyAlertQty(penaltyAlertQty);
    }

    public long getInvoicedDays() {
        // long dateDiff = DateHelper.daysBetween(getDateInvoiced(), new Date()) + 1;
        return invoiceAction.model.getInvoicedDays();

    }

    public ReasonOfRejection getReasonOfRejection() {
        return invoiceAction.model.getReasonOfRejection();
    }

    public void setReasonOfRejection(ReasonOfRejection reasonOfRejection) {
        invoiceAction.model.setReasonOfRejection(reasonOfRejection);
    }

    public Date getHirePenaltyChargeAppliedDate() {
        return invoiceAction.model.getHirePenaltyChargeAppliedDate();
    }

    public void setHirePenaltyChargeAppliedDate(Date hirePenaltyChargeAppliedDate) {
        invoiceAction.model.setHirePenaltyChargeAppliedDate(hirePenaltyChargeAppliedDate);
    }

    public Date getRepairPenaltyChargeAppliedDate() {
        return invoiceAction.model.getRepairPenaltyChargeAppliedDate();
    }

    public void setRepairPenaltyChargeAppliedDate(Date repairPenaltyChargeAppliedDate) {
        invoiceAction.model.setRepairPenaltyChargeAppliedDate(repairPenaltyChargeAppliedDate);
    }

    public BigDecimal getOriginalFullTotalToPay() {
        return invoiceAction.model.getOriginalFullTotalToPay();
    }

    public void setOriginalFullTotalToPay(BigDecimal originalTotalToPay) {
        invoiceAction.model.setOriginalTotalToPay(originalTotalToPay);
    }

    public BigDecimal getTotalToPay() {
        return invoiceAction.model.getTotalToPay();
    }

    public void setTotalToPay(BigDecimal totalToPaySplitLiability) {
        invoiceAction.model.setTotalToPay(totalToPaySplitLiability);
    }

    public BigDecimal getOriginalTotalToPay() {
        return invoiceAction.model.getOriginalTotalToPay();
    }

    public void setOriginalTotalToPay(BigDecimal originalTotalToPay) {
        invoiceAction.model.setOriginalTotalToPay(originalTotalToPay);
    }

    public BigDecimal getAdditionalDriverFee() {
//        if (additionalDriverFee == null)
//            return BigDecimal.ZERO;
//        else
        return invoiceAction.model.getAdditionalDriverFee();
    }

    public void setAdditionalDriverFee(BigDecimal additionalDriverFee) {
        invoiceAction.model.setAdditionalDriverFee(additionalDriverFee);
    }

    public Integer getAdditionalDriverQty() {
//        if (additionalDriverQty == null)
//            return 0;
//        else
        return invoiceAction.model.getAdditionalDriverQty();
    }

    public void setAdditionalDriverQty(Integer additionalDriverQty) {
        invoiceAction.model.setAdditionalDriverQty(additionalDriverQty);
    }

    public Boolean getCoverNoteRequired() {
        return invoiceAction.model.getCoverNoteRequired();
    }

    public void setCoverNoteRequired(Boolean coverNoteRequired) {
        invoiceAction.model.setCoverNoteRequired(coverNoteRequired);
    }

    public String getCoverNoteRequiredDesc() {

        return invoiceAction.model.getCoverNoteRequiredDesc();

    }

    public BigDecimal getTotalLossFeeGross() {
        return invoiceAction.model.getTotalLossFeeGross();
    }

    public void setTotalLossFeeGross(BigDecimal totalLossFeeGross) {
        invoiceAction.model.setTotalLossFeeGross(totalLossFeeGross);
    }

    public BigDecimal getTotalLossFeeNet() {
        return invoiceAction.model.getTotalLossFeeNet();
    }

    public void setTotalLossFeeNet(BigDecimal totalLossFeeNet) {
        invoiceAction.model.setTotalLossFeeNet(totalLossFeeNet);
    }

    public BigDecimal getTotalLossFeeVat() {
        return invoiceAction.model.getTotalLossFeeVat();
    }

    public void setTotalLossFeeVat(BigDecimal totalLossFeeVat) {
        invoiceAction.model.setTotalLossFeeVat(totalLossFeeVat);
    }

    public String getHirePenaltyPercentage() {
        return invoiceAction.model.getHirePenaltyPercentage();
    }

    public void setHirePenaltyPercentage(String hirePenaltyPercentage) {
        invoiceAction.model.setHirePenaltyPercentage(hirePenaltyPercentage);
    }

    public String getRepairPenaltyPercentage() {
        return invoiceAction.model.getRepairPenaltyPercentage();
    }

    public void setRepairPenaltyPercentage(String repairPenaltyPercentage) {
        invoiceAction.model.setRepairPenaltyPercentage(repairPenaltyPercentage);
    }

    public BigDecimal getInterimPayment() {
        return invoiceAction.model.getInterimPayment();
    }

    public void setInterimPayment(BigDecimal interimPayment) {
        invoiceAction.model.setInterimPayment(interimPayment);
    }

    public Boolean getInterimPaymentReceived() {
        return invoiceAction.model.getInterimPaymentReceived();
    }

    public void setInterimPaymentReceived(Boolean interimPaymentReceived) {
        invoiceAction.model.setInterimPaymentReceived(interimPaymentReceived);
    }

    public String getInterimPaymentReceivedDesc() {
        return invoiceAction.model.getInterimPaymentReceivedDesc();
    }

    public void setInterimPaymentReceivedDesc(String interimPaymentReceivedDesc) {
        invoiceAction.model.setInterimPaymentReceivedDesc(interimPaymentReceivedDesc);
    }

    public BigDecimal getTotalPenaltyCharge() {
        return invoiceAction.model.getTotalPenaltyCharge();
    }

    public void setTotalPenaltyCharge(BigDecimal totalPenaltyCharge) {
        invoiceAction.model.setTotalPenaltyCharge(totalPenaltyCharge);
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="InvoiceAction">
    String getTabName() {
        return invoiceAction.getTabName();
    }

    public String getDaysWithCHOForReview() {
        return invoiceAction.getDaysWithCHOForReview();
    }

    public String getDaysWithInsurerForReview() {
        return invoiceAction.getDaysWithInsurerForReview();
    }

    public String getDaysAwaitingLiabilityResolution() {
        return invoiceAction.getDaysAwaitingLiabilityResolution();
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="ExtraAction">
    // Everything defined in InvoiceAction
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="VehicleHireAction">
    public void setVehicleClassId(int vehicleClassId) {
        vehicleHireAction.setVehicleClassId(vehicleClassId);
    }

    public int getVehicleClassId() {
        return vehicleHireAction.getVehicleClassId();
    }

    public List<VehicleClass> getVehicleClasses() {
        return vehicleHireAction.getVehicleClasses();
    }

    public String getRentalStartTime() {
        return vehicleHireAction.getRentalStartTime();
    }

    public void setRentalStartTime(String time) {
        vehicleHireAction.setRentalStartTime(time);
    }

    public String getRentalEndTime() {
        return vehicleHireAction.getRentalEndTime();
    }

    public void setRentalEndTime(String time) {
        vehicleHireAction.setRentalEndTime(time);

    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="VehicleHire">
    public void setIsTotalLoss(boolean IsTotalLoss) {
        vehicleHireAction.model.setIsTotalLoss(IsTotalLoss);
    }

    public java.lang.String getVehicleRegistration() {
        return vehicleHireAction.model.getVehicleRegistration();
    }

    public void setVehicleRegistration(java.lang.String vehicleRegistration) {
        vehicleHireAction.model.setVehicleRegistration(vehicleRegistration);
    }

    public java.lang.String getVehicleManufacturer() {
        return vehicleHireAction.model.getVehicleManufacturer();
    }

    public void setVehicleManufacturer(java.lang.String vehicleManufacturer) {
        vehicleHireAction.model.setVehicleManufacturer(vehicleManufacturer);
    }

    public java.lang.String getVehicleModel() {
        return vehicleHireAction.model.getVehicleModel();
    }

    public void setVehicleModel(java.lang.String vehicleModel) {
        vehicleHireAction.model.setVehicleModel(vehicleModel);
    }

    public java.util.Date getRentalStart() {
        return vehicleHireAction.model.getRentalStart();
    }

    public void setRentalStart(java.util.Date rentalStart) {
        vehicleHireAction.model.setRentalStart(rentalStart);
    }

    public java.util.Date getRentalEnd() {
        return vehicleHireAction.model.getRentalEnd();
    }

    public void setRentalEnd(java.util.Date rentalEnd) {
        vehicleHireAction.model.setRentalEnd(rentalEnd);
    }

    public java.lang.String getCollectionReason() {
        return vehicleHireAction.model.getCollectionReason();
    }

    public void setCollectionReason(java.lang.String collectionReason) {
        vehicleHireAction.model.setCollectionReason(collectionReason);
    }

    public int getDays() {
        return vehicleHireAction.model.getDays();
    }

    public void setDays(int days) {
        vehicleHireAction.model.setDays(days);
    }

    public boolean isVHCdwFee() {
        return vehicleHireAction.model.isCdwFee();
    }

    public void setVHCdwFee(boolean cdwFee) {
        vehicleHireAction.model.setCdwFee(cdwFee);
    }

    public boolean isVHAutomaticFee() {
        return vehicleHireAction.model.isAutomaticFee();
    }

    public void setVHAutomaticFee(boolean automaticFee) {
        vehicleHireAction.model.setAutomaticFee(automaticFee);
    }

    public boolean isVHSatNavFee() {
        return vehicleHireAction.model.isSatNavFee();
    }

    public void setVHSatNavFee(boolean satNavFee) {
        vehicleHireAction.model.setSatNavFee(satNavFee);
    }

    public boolean isVHEstateFee() {
        return vehicleHireAction.model.isEstateFee();
    }

    public void setVHEstateFee(boolean estateFee) {
        vehicleHireAction.model.setEstateFee(estateFee);
    }

    public boolean isVHBabySeatFee() {
        return vehicleHireAction.model.isBabySeatFee();
    }

    public void setVHBabySeatFee(boolean babySeatFee) {
        vehicleHireAction.model.setBabySeatFee(babySeatFee);
    }

    public boolean isVHTowBarsFee() {
        return vehicleHireAction.model.isTowBarsFee();
    }

    public void setVHTowBarsFee(boolean towBarsFee) {
        vehicleHireAction.model.setTowBarsFee(towBarsFee);
    }

    public boolean isVHNonStandardInsurancePremiumFee() {
        return vehicleHireAction.model.isNonStandardInsurancePremiumFee();
    }

    public void setVHNonStandardInsurancePremiumFee(boolean nonStandardInsurancePremiumFee) {
        vehicleHireAction.model.setNonStandardInsurancePremiumFee(nonStandardInsurancePremiumFee);
    }

    public boolean isVHAdminFee() {
        return vehicleHireAction.model.isAdminFee();
    }

    public void setVHAdminFee(boolean adminFee) {
        vehicleHireAction.model.setAdminFee(adminFee);
    }

    public boolean isVHRoofRackFee() {
        return vehicleHireAction.model.isRoofRackFee();
    }

    public void setVHRoofRackFee(boolean roofRackFee) {
        vehicleHireAction.model.setRoofRackFee(roofRackFee);
    }

    public boolean isVHDualControlFee() {
        return vehicleHireAction.model.isDualControlFee();
    }

    public void setVHDualControlFee(boolean dualControlFee) {
        vehicleHireAction.model.setDualControlFee(dualControlFee);
    }

    public boolean isVHDeliveryCollectionFee() {
        return vehicleHireAction.model.isDeliveryCollectionFee();
    }

    public void setVHDeliveryCollectionFee(boolean deliveryCollectionFee) {
        vehicleHireAction.model.setDeliveryCollectionFee(deliveryCollectionFee);
    }

    public VehicleClass getVehicleClass() {
        return vehicleHireAction.model.getVehicleClass();
    }

    public void setVehicleClass(VehicleClass vehicleClass) {
        vehicleHireAction.model.setVehicleClass(vehicleClass);
    }

    public java.util.Date getHireStart() {
        return vehicleHireAction.model.getHireStart();
    }

    public void setHireStart(Date hireStart) {
        vehicleHireAction.model.setHireStart(hireStart);
    }

    public java.util.Date getHireEnd() {
        return vehicleHireAction.model.getHireEnd();
    }

    public void setHireEnd(Date hireEnd) {
        vehicleHireAction.model.setHireEnd(hireEnd);
    }

    // ##### NOT FROM HERE #############
    public boolean getIsTotalLoss() {
        return vehicleHireAction.model.getIsTotalLoss();
    }

    public String getHpiError() {
        return vehicleHireAction.model.getHpiError();
    }

    public void setHpiError(String hpiError) {
        vehicleHireAction.model.setHpiError(hpiError);
    }

    public String getHpiVehicleCapacity() {
        return vehicleHireAction.model.getHpiVehicleCapacity();
    }

    public void setHpiVehicleCapacity(String hpiVehicleCapacity) {
        vehicleHireAction.model.setHpiVehicleCapacity(hpiVehicleCapacity);
    }

    public String getHpiVehicleDoorplan() {
        return vehicleHireAction.model.getHpiVehicleDoorplan();
    }

    public void setHpiVehicleDoorplan(String hpiVehicleDoorplan) {
        vehicleHireAction.model.setHpiVehicleDoorplan(hpiVehicleDoorplan);
    }

    public String getHpiVehicleManufacturer() {
        return vehicleHireAction.model.getHpiVehicleManufacturer();
    }

    public void setHpiVehicleManufacturer(String hpiVehicleManufacturer) {
        vehicleHireAction.model.setHpiVehicleManufacturer(hpiVehicleManufacturer);
    }

    public String getHpiVehicleModel() {
        return vehicleHireAction.model.getHpiVehicleModel();
    }

    public void setHpiVehicleModel(String hpiVehicleModel) {
        vehicleHireAction.model.setHpiVehicleModel(hpiVehicleModel);
    }

    public String getHpiVehicleTransmission() {
        return vehicleHireAction.model.getHpiVehicleTransmission();
    }

    public void setHpiVehicleTransmission(String hpiVehicleTransmission) {
        vehicleHireAction.model.setHpiVehicleTransmission(hpiVehicleTransmission);
    }

    public String getHpiVehicleYear() {
        return vehicleHireAction.model.getHpiVehicleYear();
    }

    public void setHpiVehicleYear(String hpiVehicleYear) {
        vehicleHireAction.model.setHpiVehicleYear(hpiVehicleYear);
    }

    public void setHpiFirstRegistration(Date firstRegistration) {
        vehicleHireAction.model.setHpiFirstRegistration(firstRegistration);
    }

    public Date getHpiFirstRegistration() {
        return vehicleHireAction.model.getHpiFirstRegistration();
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="EngineerReportAction">
    // Nothing to be added ( no getter and setter for this perticular action class)
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="EngineerReport">
    public java.lang.Integer getEstimatedDays() {
        return engineerReportAction.model.getDays();
    }

    public void setEstimatedDays(java.lang.Integer days) {
        engineerReportAction.model.setDays(days);
    }

    public java.lang.String getName() {
        return engineerReportAction.model.getName();
    }

    public void setName(java.lang.String name) {
        engineerReportAction.model.setName(name);
    }

    public java.lang.String getCompany() {
        return engineerReportAction.model.getCompany();
    }

    public void setCompany(java.lang.String company) {
        engineerReportAction.model.setCompany(company);
    }

    public java.lang.String getAddress1() {
        return engineerReportAction.model.getAddress1();
    }

    public void setAddress1(java.lang.String address1) {
        engineerReportAction.model.setAddress1(address1);
    }

    public java.lang.String getAddress2() {
        return engineerReportAction.model.getAddress2();
    }

    public void setAddress2(java.lang.String address2) {
        engineerReportAction.model.setAddress2(address2);
    }

    public java.lang.String getAddress3() {
        return engineerReportAction.model.getAddress3();
    }

    public void setAddress3(java.lang.String address3) {
        engineerReportAction.model.setAddress3(address3);
    }

    public java.lang.String getAddress4() {
        return engineerReportAction.model.getAddress4();
    }

    public void setAddress4(java.lang.String address4) {
        engineerReportAction.model.setAddress4(address4);
    }

    public java.lang.String getAddress5() {
        return engineerReportAction.model.getAddress5();
    }

    public void setAddress5(java.lang.String address5) {
        engineerReportAction.model.setAddress5(address5);
    }

    public java.lang.String getPostcode() {
        return engineerReportAction.model.getPostcode();
    }

    public void setPostcode(java.lang.String postcode) {
        engineerReportAction.model.setPostcode(postcode);
    }

    public java.lang.String getTelephone() {
        return engineerReportAction.model.getTelephone();
    }

    public void setTelephone(java.lang.String telephone) {
        engineerReportAction.model.setTelephone(telephone);
    }

    public java.lang.String getEmail() {
        return engineerReportAction.model.getEmail();
    }

    public void setEmail(java.lang.String email) {
        engineerReportAction.model.setEmail(email);
    }

    public Boolean isIsUsable() {
        return engineerReportAction.model.isIsUsable();
    }

    public void setIsUsable(Boolean isUsable) {
        engineerReportAction.model.setIsUsable(isUsable);
    }

    public java.math.BigDecimal getLabourAmount() {
        return engineerReportAction.model.getLabourAmount();
    }

    public void setLabourAmount(java.math.BigDecimal labourAmount) {
        engineerReportAction.model.setLabourAmount(labourAmount);
    }

    public java.math.BigDecimal getTotalAmount() {
        return engineerReportAction.model.getTotalAmount();
    }

    public void setTotalAmount(java.math.BigDecimal totalAmount) {
        engineerReportAction.model.setTotalAmount(totalAmount);
    }

    public BigDecimal getEstimatedLabourAmount() {
        return engineerReportAction.model.getEstimatedLabourAmount();
    }

    public BigDecimal getEstimatedTotalRepairAmount() {
        return engineerReportAction.model.getEstimatedTotalRepairAmount();
    }

    public int getEstimatedDaysUnderRepair() {
        return engineerReportAction.model.getEstimatedDaysUnderRepair();
    }

    public String getIsUsableDesc() {
        return engineerReportAction.model.getIsUsableDesc();

    }

    // </editor-fold>
    public String updateModel() {

        if ((invoiceAction.updateModel()).equals(SUCCESS)) {
            LOG.debug("INVOICEACTION update is done ");

            if (vehicleHireAction.updateModel().equals(SUCCESS)) {

                LOG.debug("VEHICLEHIREACTION update is done ");

                if (engineerReportAction.updateModel().equals(SUCCESS)) {

                    LOG.debug("ENGINEERREPORTACTION update is done ");


                    try {
                        invoiceAction.checkVersion(invoiceAction.getModel());
                        vehicleHireAction.checkVersion(vehicleHireAction.getModel());
                        engineerReportAction.checkVersion(engineerReportAction.getModel());

                       invoiceAction.updateModel();

                        if (!(invoiceAction.getModel().getVersion().equals((Integer) session.get(invoiceAction.getModel().getClass().getName())))) {
                            LOG.debug("Setting model version in session: {}={}", invoiceAction.getModel().getClass().getName(), invoiceAction.getModel().getVersion());
                            session.put(invoiceAction.getModel().getClass().getName(), invoiceAction.getModel().getVersion());
                            LOG.debug("Setting is done for model version in session: {}={}", invoiceAction.getModel().getClass().getName(), invoiceAction.getModel().getVersion());
                        }


                        if (!(vehicleHireAction.getModel().getVersion().equals((Integer) session.get(vehicleHireAction.getModel().getClass().getName())))) {
                            LOG.debug("Setting model version in session: {}={}", vehicleHireAction.getModel().getClass().getName(), vehicleHireAction.getModel().getVersion());
                            session.put(vehicleHireAction.getModel().getClass().getName(), vehicleHireAction.getModel().getVersion());
                            LOG.debug("Setting is done for model version in session: {}={}", vehicleHireAction.getModel().getClass().getName(), vehicleHireAction.getModel().getVersion());
                        }


                        if (!(engineerReportAction.getModel().getVersion().equals((Integer) session.get(engineerReportAction.getModel().getClass().getName())))) {
                            LOG.debug("Setting model version in session: {}={}", engineerReportAction.getModel().getClass().getName(), engineerReportAction.getModel().getVersion());
                            session.put(engineerReportAction.getModel().getClass().getName(), engineerReportAction.getModel().getVersion());
                            LOG.debug("Setting is done for model version in session: {}={}", engineerReportAction.getModel().getClass().getName(), engineerReportAction.getModel().getVersion());
                        }


                        this.setActionResult("Your Changes Have Been Saved");

                        return SUCCESS;

                    } catch (Exception ex) {
                        handleException(ex);
                        return ERROR;
                    }

                }
                setActionError(engineerReportAction.getActionError());
                LOG.debug("ERROR ON ENGINEERREPORTACTOIN update");
            }
            setActionError(vehicleHireAction.getActionError());
            LOG.debug("ERROR ON VEHICLEHIREACTION update");
        }
        setActionError(invoiceAction.getActionError());
        LOG.debug("ERROR ON INVOICEACTION update");
        LOG.debug("update is not saved and returning ERROR");
        return ERROR;






    }

    @Override
    public String execute() {

        String tabName = getTabName();
        short accessRight = applicationAccessibility.checkTabAccessibility(tabName, super.getAuthenticatedUser(), claim);

        String result = accessRight > 1 ? EDITABLE : READ_ONLY;
        LOG.debug("Returning accessibility={} for tab.status={}", result, tabName + '.' + claim.getStatus());


        if (invoiceAction.getModel() != null) {
            LOG.debug("invoiceAction getModel is not null and value of object is: {} ", invoiceAction.getModel());
//            if (!(invoiceAction.getModel().getVersion().equals((Integer) session.get(invoiceAction.getModel().getClass().getName())))) {
            LOG.debug("Setting model version in session: {}={}", invoiceAction.getModel().getClass().getName(), invoiceAction.getModel().getVersion());
            session.put(invoiceAction.getModel().getClass().getName(), invoiceAction.getModel().getVersion());
//                LOG.debug("Setting is done for model version in session: {}={}", invoiceAction.getModel().getClass().getName(), invoiceAction.getModel().getVersion());
        }
//
        if (vehicleHireAction.getModel() != null) {
//                 LOG.debug("vehicleHireAction getModel is not null and value of object is: {}",vehicleHireAction.getModel());
//                if (!(vehicleHireAction.getModel().getVersion().equals((Integer) session.get(vehicleHireAction.getModel().getClass().getName())))) {
            LOG.debug("Setting model version in session: {}={}", vehicleHireAction.getModel().getClass().getName(), vehicleHireAction.getModel().getVersion());
            session.put(vehicleHireAction.getModel().getClass().getName(), vehicleHireAction.getModel().getVersion());
//                    LOG.debug("Setting is done for model version in session: {}={}", vehicleHireAction.getModel().getClass().getName(), vehicleHireAction.getModel().getVersion());
        }
//
        if (engineerReportAction.getModel() != null) {
//                      LOG.debug("engineerReportAction getModel is not null and value of object is: {}",engineerReportAction.getModel());
//                    if (!(engineerReportAction.getModel().getVersion().equals((Integer) session.get(engineerReportAction.getModel().getClass().getName())))) {
            LOG.debug("Setting model version in session: {}={}", engineerReportAction.getModel().getClass().getName(), engineerReportAction.getModel().getVersion());
            session.put(engineerReportAction.getModel().getClass().getName(), engineerReportAction.getModel().getVersion());
//                        LOG.debug("Setting is done for model version in session: {}={}", engineerReportAction.getModel().getClass().getName(), engineerReportAction.getModel().getVersion());
        }


//        if (invoiceAction.getModel() != null) {
//            try{
//                int i=(Integer)session.get(invoiceAction.getModel().getClass().getName());
//                LOG.debug("invoice model version is : {}",i);
//            }catch(Exception ex){
//                LOG.debug("caught exception is {}",ex.getMessage());
//            }
//            LOG.debug("invoiceAction getModel is not null and value of object is: {} and session value is {}",invoiceAction.getModel(),(Integer) session.get(invoiceAction.getModel().getClass().getName()));
//            if (!(invoiceAction.getModel().getVersion().equals((Integer) session.get(invoiceAction.getModel().getClass().getName())))) {
//                LOG.debug("Setting model version in session: {}={}", invoiceAction.getModel().getClass().getName(), invoiceAction.getModel().getVersion());
//                session.put(invoiceAction.getModel().getClass().getName(), invoiceAction.getModel().getVersion());
//                LOG.debug("Setting is done for model version in session: {}={}", invoiceAction.getModel().getClass().getName(), invoiceAction.getModel().getVersion());
//            }
//
//            if (vehicleHireAction.getModel() != null) {
//                 LOG.debug("vehicleHireAction getModel is not null and value of object is: {}",vehicleHireAction.getModel());
//                if (!(vehicleHireAction.getModel().getVersion().equals((Integer) session.get(vehicleHireAction.getModel().getClass().getName())))) {
//                    LOG.debug("Setting model version in session: {}={}", vehicleHireAction.getModel().getClass().getName(), vehicleHireAction.getModel().getVersion());
//                    session.put(vehicleHireAction.getModel().getClass().getName(), vehicleHireAction.getModel().getVersion());
//                    LOG.debug("Setting is done for model version in session: {}={}", vehicleHireAction.getModel().getClass().getName(), vehicleHireAction.getModel().getVersion());
//                }
//
//                if (engineerReportAction.getModel() != null) {
//                      LOG.debug("engineerReportAction getModel is not null and value of object is: {}",engineerReportAction.getModel());
//                    if (!(engineerReportAction.getModel().getVersion().equals((Integer) session.get(engineerReportAction.getModel().getClass().getName())))) {
//                        LOG.debug("Setting model version in session: {}={}", engineerReportAction.getModel().getClass().getName(), engineerReportAction.getModel().getVersion());
//                        session.put(engineerReportAction.getModel().getClass().getName(), engineerReportAction.getModel().getVersion());
//                        LOG.debug("Setting is done for model version in session: {}={}", engineerReportAction.getModel().getClass().getName(), engineerReportAction.getModel().getVersion());
//                    }
//                }
//            }
//        }





        return result;

    }

    @Override
    public void prepare() throws Exception {
        LOG.debug("preparing... ");
        claim = this.claimService.getClaim(claimId);



        if (claim == null) {
            throw new Exception("An attempt to retrieve claim by id failed due to invalid id provided.");


        }
        invoiceAction.setClaimService(claimService);
        invoiceAction.setClaimId(claimId);
        invoiceAction.prepare();
        vehicleHireAction.setLookupService(lookupService);
        vehicleHireAction.setClaimService(claimService);
        vehicleHireAction.setClaimId(claimId);
        vehicleHireAction.prepare();
        engineerReportAction.setClaimService(claimService);
        engineerReportAction.setClaimId(claimId);
        engineerReportAction.prepare();

        LOG.debug("ALL PREPARATION DONE");


    }
}
