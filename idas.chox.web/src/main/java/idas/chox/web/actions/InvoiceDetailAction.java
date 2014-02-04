package idas.chox.web.actions;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import org.springframework.security.access.AccessDeniedException;

import org.apache.commons.lang3.SerializationUtils;


import org.hibernate.proxy.HibernateProxy;

import com.opensymphony.xwork2.Preparable;

import idas.chox.core.hpi.Hpi;
import idas.chox.core.hpi.HpiException;
import idas.chox.core.hpi.HpiResponse;
import idas.chox.core.model.*;
import idas.chox.core.services.*;
import idas.chox.core.util.CalcHelper;
import idas.chox.core.util.CompareUtil;
import idas.chox.core.util.DateHelper;
import idas.chox.service.security.ApplicationAccessibility;
import idas.chox.service.security.TabAccessibility;
import idas.chox.data.events.ActivityEvent;
import idas.chox.service.workflow.activities.ActivityEventGenerator;
import idas.chox.web.VehicleClassComparator;
import idas.chox.web.VehicleClassPriceMapper;
import idas.chox.web.VehicleClassPriceMapperComparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvoiceDetailAction extends BaseAction implements Preparable {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceDetailAction.class);
    private int claimId = 0;
    private ClaimService claimService;
    private LookupService lookupService;
    private InsurerDiscountService insurerDiscountService;
    private String actionResult;
    private ApplicationAccessibility applicationAccessibility;
    private Claim claim = null;
    private int actionSelected;
    private int submit = 10;
    private int recalculate = 20;
    private int reset = 30;
    private static final String READ_ONLY = "r";
    private static final String EDITABLE = "w";
    private static final String DECLINE = "decline";
    private VehicleClassService vehicleClassService;
    private VehicleClassPriceService vehicleClassPriceService;
    private VehicleClass vehicleClass;
    private BigDecimal Vat_Rate = CalcHelper.VAT_RATE;
    private BigDecimal hire_vat_used;
    private BigDecimal repair_vat_used;
    private BigDecimal engineerFee_vat_used;
    private BigDecimal tpiInsurancePremiumVatUsed;
    private BigDecimal totalLossFee_vat_used;
    private BigDecimal storageRecovery_vat_used;
    private BigDecimal previousHireNet;
    private BigDecimal previousRepairNet;
    private BigDecimal previousEngineerFeeNet;
    private BigDecimal previousTotalLossNet;
    private BigDecimal previousStorageNet;
    private BigDecimal previousHireVat;
    private BigDecimal previousRepairVat;
    private BigDecimal previousEngineerFeeVat;
    private BigDecimal previousTotalLossVat;
    private BigDecimal previousStorageVat;
    private BigDecimal previousNonStandardInsurancePremiumFee;
    private Boolean canAddTotalGrossInsurerDiscountComment = false;
    private Boolean canAddRepairGrossInsurerDiscountComment = false;
    private Boolean canAddHireGrossInsurerDiscountComment = false;
    private UserService userService;
    private BigDecimal insurerDiscountPercentageApplied;
    private boolean modelSaved = false;
    private String oldVRN;
    private InvoiceOriginal invoiceOriginal;
    private EngineerReport engineerReport;
    private VehicleHire vehicleHire;
    private Invoice invoice;
    private String daysWithCHOForReview;
    private String daysWithInsurerForReview;
    private String daysAwaitingLiabilityResolution;
    private Invoice originalInvoice;
    private EngineerReport originalEngineerReport;
    private VehicleHire originalVehicleHire;
    private ActivityEventGenerator eventGenerator;
    
    // <editor-fold defaultstate="collapsed" desc="Getter and Setter">

    public BigDecimal getHireInsurerDiscountCalculated() {
        return invoice.getHireInsurerDiscountCalculated();
    }

    public BigDecimal getRepairInsurerDiscountCalculated() {
        return invoice.getRepairInsurerDiscountCalculated();
    }

    public BigDecimal getTotalInsurerDiscountCalculated() {
        return invoice.getTotalInsurerDiscountCalculated();
    }
    
    public BigDecimal getPaymentDetailsCHODiscount() {
        return invoice.getChoDiscountFeePaid();
    }

    public BigDecimal getPaymentDetailsClaimHandInvAmt() {
        return invoice.getClaimHandlerChargePaid();
    }

    public BigDecimal getPaymentDetailsDeductionClaimHandFee() {
        return invoice.getDeductionClaimHandlerFeePaid();
    }

    public BigDecimal getPaymentDetailsInsurerDiscount() {
        return invoice.getInsurerDiscountFeePaid();
    }

    public Boolean getCanAddHireGrossInsurerDiscountComment() {
        return canAddHireGrossInsurerDiscountComment;
    }

    public void setCanAddHireGrossInsurerDiscountComment(Boolean canAddHireGrossInsurerDiscountComment) {
        this.canAddHireGrossInsurerDiscountComment = canAddHireGrossInsurerDiscountComment;
    }

    public Boolean getCanAddTotalGrossInsurerDiscountComment() {
        return canAddTotalGrossInsurerDiscountComment;
    }

    public void setCanAddTotalGrossInsurerDiscountComment(Boolean canAddTotalGrossInsurerDiscountComment) {
        this.canAddTotalGrossInsurerDiscountComment = canAddTotalGrossInsurerDiscountComment;
    }

    public Boolean getCanAddRepairGrossInsurerDiscountComment() {
        return canAddRepairGrossInsurerDiscountComment;
    }

    public void setCanAddRepairGrossInsurerDiscountComment(Boolean canAddRepairGrossInsurerDiscountComment) {
        this.canAddRepairGrossInsurerDiscountComment = canAddRepairGrossInsurerDiscountComment;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public BigDecimal getInsurerDiscountPercentageApplied() {
        return insurerDiscountPercentageApplied;
    }

    public void setInsurerDiscountPercentageApplied(BigDecimal insurerDiscountApplied) {
        this.insurerDiscountPercentageApplied = insurerDiscountApplied;
    }

    public BigDecimal getPreviousNonStandardInsurancePremiumFee() {
        return previousNonStandardInsurancePremiumFee;
    }

    public void setPreviousNonStandardInsurancePremiumFee(BigDecimal previousNonStandardInsurancePremiumFee) {
        this.previousNonStandardInsurancePremiumFee = previousNonStandardInsurancePremiumFee;
    }

    public BigDecimal getPreviousHireNet() {
        return previousHireNet;
    }

    public void setPreviousHireNet(BigDecimal previousHireNet) {
        this.previousHireNet = previousHireNet;
    }

    public BigDecimal getPreviousHireVat() {
        return previousHireVat;
    }

    public void setPreviousHireVat(BigDecimal previousHireVat) {
        this.previousHireVat = previousHireVat;
    }

    public BigDecimal getPreviousEngineerFeeVat() {
        return previousEngineerFeeVat;
    }

    public void setPreviousEngineerFeeVat(BigDecimal previousEngineerFeeVat) {
        this.previousEngineerFeeVat = previousEngineerFeeVat;
    }

    public BigDecimal getPreviousRepairVat() {
        return previousRepairVat;
    }

    public void setPreviousRepairVat(BigDecimal previousRepairVat) {
        this.previousRepairVat = previousRepairVat;
    }

    public BigDecimal getPreviousStorageVat() {
        return previousStorageVat;
    }

    public void setPreviousStorageVat(BigDecimal previousStorageVat) {
        this.previousStorageVat = previousStorageVat;
    }

    public BigDecimal getPreviousTotalLossVat() {
        return previousTotalLossVat;
    }

    public void setPreviousTotalLossVat(BigDecimal previousTotalLossVat) {
        this.previousTotalLossVat = previousTotalLossVat;
    }

    public BigDecimal getPreviousEngineerFeeNet() {
        return previousEngineerFeeNet;
    }

    public void setPreviousEngineerFeeNet(BigDecimal previousEngineerFeeNet) {
        this.previousEngineerFeeNet = previousEngineerFeeNet;
    }

    public BigDecimal getPreviousRepairNet() {
        return previousRepairNet;
    }

    public void setPreviousRepairNet(BigDecimal previousRepairNet) {
        this.previousRepairNet = previousRepairNet;
    }

    public BigDecimal getPreviousStorageNet() {
        return previousStorageNet;
    }

    public void setPreviousStorageNet(BigDecimal previousStorageNet) {
        this.previousStorageNet = previousStorageNet;
    }

    public BigDecimal getPreviousTotalLossNet() {
        return previousTotalLossNet;
    }

    public void setPreviousTotalLossNet(BigDecimal previousTotalLossNet) {
        this.previousTotalLossNet = previousTotalLossNet;
    }

    private BigDecimal getTotalGrossInsurerDiscountPercentage(Claim claim) {
        if (claim.getInsurer().isInsurerDiscountEnable()) {
            return insurerDiscountService.getDiscountPercentage(claim.getInsurer().getId(), claim.getChorganisation().getId(), claim.getInvoice().getCreatedDate(), InsurerDiscountType.TOTAL.getInsurerDiscountTypeValue());
        } else {
            return BigDecimal.ZERO;
        }
    }

    private BigDecimal getRepairGrossInsurerDiscountPercentage(Claim claim) {
        if (claim.getInsurer().isInsurerDiscountEnable()) {
            return insurerDiscountService.getDiscountPercentage(claim.getInsurer().getId(), claim.getChorganisation().getId(), claim.getInvoice().getCreatedDate(), InsurerDiscountType.REPAIR.getInsurerDiscountTypeValue());
        } else {
            return BigDecimal.ZERO;
        }
    }
    
    private BigDecimal getHireGrossInsurerDiscountPercentage(Claim claim) {
        if (claim.getInsurer().isInsurerDiscountEnable()) {
            return insurerDiscountService.getDiscountPercentage(claim.getInsurer().getId(), claim.getChorganisation().getId(), claim.getInvoice().getCreatedDate(), InsurerDiscountType.HIRE.getInsurerDiscountTypeValue());
        } else {
            return BigDecimal.ZERO;
        }
    }
    
    public BigDecimal getEngineerFee_vat_used() {
        return engineerFee_vat_used.multiply(new BigDecimal(100));
    }

    public void setEngineerFee_vat_used(BigDecimal engineerFee_vat_used) {
        this.engineerFee_vat_used = engineerFee_vat_used;
    }

    public BigDecimal getTpiInsurancePremiumVatUsed() {
        return tpiInsurancePremiumVatUsed.multiply(new BigDecimal(100));
    }

    public void setTpiInsurancePremiumVatUsed(BigDecimal tpiInsurancePremiumVatUsed) {
        this.tpiInsurancePremiumVatUsed = tpiInsurancePremiumVatUsed;
    }

    public BigDecimal getHire_vat_used() {
        return hire_vat_used.multiply(new BigDecimal(100));
    }

    public void setHire_vat_used(BigDecimal hire_vat_used) {
        this.hire_vat_used = hire_vat_used;
    }

    public BigDecimal getRepair_vat_used() {
        return repair_vat_used.multiply(new BigDecimal(100));
    }

    public void setRepair_vat_used(BigDecimal repair_vat_used) {
        this.repair_vat_used = repair_vat_used;
    }

    public BigDecimal getStorageRecovery_vat_used() {
        return storageRecovery_vat_used.multiply(new BigDecimal(100));
    }

    public void setStorageRecovery_vat_used(BigDecimal storageRecovery_vat_used) {
        this.storageRecovery_vat_used = storageRecovery_vat_used;
    }

    public BigDecimal getTotalLossFee_vat_used() {
        return totalLossFee_vat_used.multiply(new BigDecimal(100));
    }

    public void setTotalLossFee_vat_used(BigDecimal totalLossFee_vat_used) {
        this.totalLossFee_vat_used = totalLossFee_vat_used;
    }
    private int formChanged = -1;
    private short accessRight;

    public short getAccessRight() {
        return accessRight;
    }

    public int getFormChanged() {
        return formChanged;
    }

    public void setFormChanged(int formChanged) {
        this.formChanged = formChanged;
    }

    public BigDecimal getPercentageLiabilityAccepted() {
        return claim.getPercentageLiabilityAccepted();
    }

    public BigDecimal getVat_Rate() {
        return Vat_Rate;
    }

    public int getActionSelected() {
        return actionSelected;
    }

    public void setActionSelected(int actionSelected) {
        LOG.debug("setbuttonclicked called with the value of {}", actionSelected);
        this.actionSelected = actionSelected;
    }

    public int getClaimId() {
        return claimId;
    }

    public void setClaimId(int claimId) {
        this.claimId = claimId;
    }

    public String getClaimStatus() {
        return claim.getStatus();
    }

    public void setInsurerDiscountService(InsurerDiscountService insurerDiscountService) {
        this.insurerDiscountService = insurerDiscountService;
    }

    public void setLookupService(LookupService lookupService) {

        this.lookupService = lookupService;
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    public boolean getCanShowPaymentDetails() {
        LOG.debug("Claim is {}, claimId={}", claim, claimId);
        if ((claim.getStatus().equalsIgnoreCase(ClaimStatus.INVOICE_PAYMENT_LOGGED)
                || claim.getStatus().equalsIgnoreCase(ClaimStatus.INVOICE_PAYMENT_RECEIVED)
                || claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_CLOSED))
                && claim.getInvoice().getFinalPayment() != null) {
            return true;
        }
        return false;
    }
    
    public BigDecimal getFinalPayment() {
        return invoice.getFinalPayment();
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

    public void setEventGenerator(ActivityEventGenerator eventGenerator) {
        this.eventGenerator = eventGenerator;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="InvoiceOriginal">

    public java.util.Date getDateInvoicedOriginal() {

        return invoiceOriginal.getDateInvoicedOriginal();
    }

    public java.math.BigDecimal getHireNetOriginal() {


        LOG.debug("getHireNetOriginal is being called");

        return invoiceOriginal.getHireNetOriginal();

    }

    public java.math.BigDecimal getHireVatOriginal() {

        return invoiceOriginal.getHireVatOriginal();
    }

    public java.math.BigDecimal getHireGrossOriginal() {

        return invoiceOriginal.getHireGrossOriginal();
    }

    public java.math.BigDecimal getRepairNetOriginal() {

        return invoiceOriginal.getRepairNetOriginal();
    }

    public java.math.BigDecimal getRepairVatOriginal() {
        return invoiceOriginal.getRepairVatOriginal();
    }

    public java.math.BigDecimal getRepairGrossOriginal() {
        return invoiceOriginal.getRepairGrossOriginal();
    }

    public java.math.BigDecimal getEngineerFeeNetOriginal() {
        return invoiceOriginal.getEngineerFeeNetOriginal();
    }

    public java.math.BigDecimal getEngineerFeeVatOriginal() {
        return invoiceOriginal.getEngineerFeeVatOriginal();
    }

    public java.math.BigDecimal getEngineerFeeGrossOriginal() {
        return invoiceOriginal.getEngineerFeeGrossOriginal();
    }

    public java.math.BigDecimal getStorageRecoveryNetOriginal() {
        return invoiceOriginal.getStorageRecoveryNetOriginal();
    }

    public java.math.BigDecimal getStorageRecoveryVatOriginal() {
        return invoiceOriginal.getStorageRecoveryVatOriginal();
    }

    public java.math.BigDecimal getStorageRecoveryGrossOriginal() {
        return invoiceOriginal.getStorageRecoveryGrossOriginal();
    }

    public java.math.BigDecimal getTotalNetOriginal() {
        return invoiceOriginal.getTotalNetOriginal();
    }

    public java.math.BigDecimal getTotalVatOriginal() {
        return invoiceOriginal.getTotalVatOriginal();
    }

    public java.math.BigDecimal getTotalGrossOriginal() {
        return invoiceOriginal.getTotalGrossOriginal();
    }

    public java.math.BigDecimal getClaimsHandlingInvoiceAmountOriginal() {
        return invoiceOriginal.getClaimsHandlingInvoiceAmountOriginal();
    }

    public java.math.BigDecimal getDeductionForClaimsHandlingFeeOriginal() {
        return invoiceOriginal.getDeductionForClaimsHandlingFeeOriginal();
    }

    public java.math.BigDecimal getDiscountOriginal() {
        return invoiceOriginal.getDiscountOriginal();
    }

    public java.math.BigDecimal getInsurerDiscountOriginal() {
        return invoiceOriginal.getInsurerDiscountOriginal();
    }

    public java.math.BigDecimal getFullTotalToPayOriginal() {
        return invoiceOriginal.getFullTotalToPayOriginal();
    }

    public java.math.BigDecimal getMiscellaneousFeeOriginal() {
        return invoiceOriginal.getMiscellaneousFeeOriginal();
    }

    public java.math.BigDecimal getCollaborationFeeOriginal() {
        return invoiceOriginal.getCollaborationFeeOriginal();
    }

    public Integer getCollaborationQtyOriginal() {
        return invoiceOriginal.getCollaborationQtyOriginal();
    }

    public java.math.BigDecimal getAutomaticFeeOriginal() {
        return invoiceOriginal.getAutomaticFeeOriginal();
    }

    public Integer getEstateQtyOriginal() {
        return invoiceOriginal.getEstateQtyOriginal();
    }

    public Integer getAutomaticQtyOriginal() {
        return invoiceOriginal.getAutomaticQtyOriginal();
    }

    public Integer getSatNavQtyOriginal() {
        return invoiceOriginal.getSatNavQtyOriginal();
    }

    public Integer getBabySeatQtyOriginal() {
        return invoiceOriginal.getBabySeatQtyOriginal();
    }

    public Integer getTowBarsQtyOriginal() {
        return invoiceOriginal.getTowBarsQtyOriginal();
    }

    public Integer getNonStandardInsurancePremiumQtyOriginal() {
        return invoiceOriginal.getNonStandardInsurancePremiumQtyOriginal();
    }

    public Integer getAdminQtyOriginal() {
        return invoiceOriginal.getAdminQtyOriginal();
    }

    public Integer getRoofRackQtyOriginal() {
        return invoiceOriginal.getRoofRackQtyOriginal();
    }

    public Integer getDualControlQtyOriginal() {
        return invoiceOriginal.getDualControlQtyOriginal();
    }

    public Integer getDeliveryCollectionQtyOriginal() {
        return invoiceOriginal.getDeliveryCollectionQtyOriginal();
    }

    public java.math.BigDecimal getSatNavFeeOriginal() {
        return invoiceOriginal.getSatNavFeeOriginal();
    }

    public java.math.BigDecimal getEstateFeeOriginal() {
        return invoiceOriginal.getEstateFeeOriginal();
    }

    public java.math.BigDecimal getBabySeatFeeOriginal() {
        return invoiceOriginal.getBabySeatFeeOriginal();
    }

    public java.math.BigDecimal getTowBarsFeeOriginal() {
        return invoiceOriginal.getTowBarsFeeOriginal();
    }

    public java.math.BigDecimal getNonStandardInsurancePremiumFeeOriginal() {
        return invoiceOriginal.getNonStandardInsurancePremiumFeeOriginal();
    }

    public java.math.BigDecimal getAdminFeeOriginal() {
        return invoiceOriginal.getAdminFeeOriginal();
    }
    
    public java.math.BigDecimal getRepairAdminFeeOriginal() {
        return invoiceOriginal.getRepairAdminFeeOriginal();
    }
    
    public java.math.BigDecimal getRepairAcquisitionFeeOriginal() {
        return invoiceOriginal.getRepairAcquisitionFeeOriginal();
    }

    public java.math.BigDecimal getRoofRackFeeOriginal() {
        return invoiceOriginal.getRoofRackFeeOriginal();
    }

    public java.math.BigDecimal getDualControlFeeOriginal() {
        return invoiceOriginal.getDualControlFeeOriginal();
    }

    public java.math.BigDecimal getDeliveryCollectionFeeOriginal() {
        return invoiceOriginal.getDeliveryCollectionFeeOriginal();
    }

    public BigDecimal getHireRateChargedPerDayOriginal() {
        return invoiceOriginal.getHireRateChargedPerDayOriginal();
    }

    public BigDecimal getExcessAmountCollectedOriginal() {
        return invoiceOriginal.getExcessAmountCollectedOriginal();
    }

    public BigDecimal getVatAmountCollectedOriginal() {
        return invoiceOriginal.getVatAmountCollectedOriginal();
    }

    public BigDecimal getHirePenaltyChargeOriginal() {
        return invoiceOriginal.getHirePenaltyChargeOriginal();
    }

    public BigDecimal getRepairPenaltyChargeOriginal() {
        return invoiceOriginal.getRepairPenaltyChargeOriginal();
    }

    public BigDecimal getTotalToPayOriginal() {
        return invoiceOriginal.getTotalToPayOriginal();
    }

    public BigDecimal getAdditionalDriverFeeOriginal() {

        return invoiceOriginal.getAdditionalDriverFeeOriginal();
    }

    public Integer getAdditionalDriverQtyOriginal() {

        return invoiceOriginal.getAdditionalDriverQtyOriginal();
    }

    public BigDecimal getTotalLossFeeGrossOriginal() {
        return invoiceOriginal.getTotalLossFeeGrossOriginal();
    }

    public BigDecimal getTotalLossFeeNetOriginal() {
        return invoiceOriginal.getTotalLossFeeNetOriginal();
    }

    public BigDecimal getTotalLossFeeVatOriginal() {
        return invoiceOriginal.getTotalLossFeeVatOriginal();
    }

    public String getHirePenaltyPercentageOriginal() {
        return invoiceOriginal.getHirePenaltyPercentageOriginal();
    }

    public String getRepairPenaltyPercentageOriginal() {
        return invoiceOriginal.getRepairPenaltyPercentageOriginal();
    }

    public BigDecimal getInterimPaymentOriginal() {

        return invoiceOriginal.getInterimPaymentOriginal();
    }

    public BigDecimal getTotalPenaltyChargeOriginal() {
        return invoiceOriginal.getTotalPenaltyChargeOriginal();
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Invoice">
    public java.util.Date getInvoiceCreatedDate() {
        return invoice.getCreatedDate();
    }

    public java.util.Date getPenaltyChargeDate() {
        return invoice.getAutoPenaltyStart();
    }

    public java.util.Date getDateInvoiced() {

        return invoice.getDateInvoiced();
    }

    public void setDateInvoiced(java.util.Date dateInvoiced) {
        if (actionSelected != reset && invoice != null) {
            invoice.setDateInvoiced(dateInvoiced);
        }
    }

    public java.math.BigDecimal getHireNet() {

        return invoice.getHireNet();
    }

    public void setHireNet(java.math.BigDecimal hireNet) {

        if (actionSelected != reset && invoice != null) {

            setPreviousHireNet(invoice.getHireNet());

            invoice.setHireNet(hireNet);


        }
    }

    public java.math.BigDecimal getHireVat() {

        return invoice.getHireVat();
    }

    public void setHireVat(java.math.BigDecimal hireVat) {
        if (actionSelected != reset && invoice != null) {
            setPreviousHireVat(invoice.getHireVat());
            invoice.setHireVat(hireVat);
        }
    }

    public java.math.BigDecimal getHireGross() {
        return invoice.getHireGross();
    }

    public void setHireGross(java.math.BigDecimal hireGross) {
        if (actionSelected != reset && invoice != null) {
            invoice.setHireGross(hireGross);
        }
    }

    public java.math.BigDecimal getRepairNet() {
        return invoice.getRepairNet();
    }

    public void setRepairNet(java.math.BigDecimal repairNet) {
        if (actionSelected != reset && invoice != null) {
            setPreviousRepairNet(invoice.getRepairNet());
            invoice.setRepairNet(repairNet);
        }
    }

    public java.math.BigDecimal getRepairVat() {
        return invoice.getRepairVat();
    }

    public void setRepairVat(java.math.BigDecimal repairVat) {
        if (actionSelected != reset && invoice != null) {
            setPreviousRepairVat(invoice.getRepairVat());
            invoice.setRepairVat(repairVat);
        }
    }

    public java.math.BigDecimal getRepairGross() {
        return invoice.getRepairGross();
    }

    public void setRepairGross(java.math.BigDecimal repairGross) {
        if (actionSelected != reset && invoice != null) {
            invoice.setRepairGross(repairGross);
        }
    }

    public java.math.BigDecimal getEngineerFeeNet() {
        return invoice.getEngineerFeeNet();
    }

    public void setEngineerFeeNet(java.math.BigDecimal engineerFeeNet) {
        if (actionSelected != reset && invoice != null) {
            setPreviousEngineerFeeNet(invoice.getEngineerFeeNet());
            invoice.setEngineerFeeNet(engineerFeeNet);
        }
    }

    public java.math.BigDecimal getEngineerFeeVat() {
        return invoice.getEngineerFeeVat();
    }

    public void setEngineerFeeVat(java.math.BigDecimal engineerFeeVat) {
        if (actionSelected != reset && invoice != null) {
            setPreviousEngineerFeeVat(invoice.getEngineerFeeVat());
            invoice.setEngineerFeeVat(engineerFeeVat);
        }
    }

    public java.math.BigDecimal getEngineerFeeGross() {
        return invoice.getEngineerFeeGross();
    }

    public void setEngineerFeeGross(java.math.BigDecimal engineerFeeGross) {
        if (actionSelected != reset && invoice != null) {
            invoice.setEngineerFeeGross(engineerFeeGross);
        }
    }

    public java.math.BigDecimal getStorageRecoveryNet() {
        return invoice.getStorageRecoveryNet();
    }

    public void setStorageRecoveryNet(java.math.BigDecimal storageRecoveryNet) {
        if (actionSelected != reset && invoice != null) {
            setPreviousStorageNet(invoice.getStorageRecoveryNet());
            invoice.setStorageRecoveryNet(storageRecoveryNet);
        }
    }

    public java.math.BigDecimal getStorageRecoveryVat() {
        return invoice.getStorageRecoveryVat();
    }

    public void setStorageRecoveryVat(java.math.BigDecimal storageRecoveryVat) {
        if (actionSelected != reset && invoice != null) {
            setPreviousStorageVat(invoice.getStorageRecoveryVat());
            invoice.setStorageRecoveryVat(storageRecoveryVat);
        }
    }

    public java.math.BigDecimal getStorageRecoveryGross() {
        return invoice.getStorageRecoveryGross();
    }

    public void setStorageRecoveryGross(java.math.BigDecimal storageRecoveryGross) {
        if (actionSelected != reset && invoice != null) {
            invoice.setStorageRecoveryGross(storageRecoveryGross);
        }
    }

    public java.math.BigDecimal getTotalNet() {
        return invoice.getTotalNet();
    }

    public void setTotalNet(java.math.BigDecimal totalNet) {
        if (actionSelected != reset && invoice != null) {
            invoice.setTotalNet(totalNet);
        }
    }

    public java.math.BigDecimal getTotalVat() {
        return invoice.getTotalVat();
    }

    public void setTotalVat(java.math.BigDecimal totalVat) {
        if (actionSelected != reset && invoice != null) {
            invoice.setTotalVat(totalVat);
        }
    }

    public java.math.BigDecimal getTotalGross() {
        return invoice.getTotalGross();
    }

    public void setTotalGross(java.math.BigDecimal totalGross) {
        if (actionSelected != reset && invoice != null) {
            invoice.setTotalGross(totalGross);
        }
    }

    public java.math.BigDecimal getClaimsHandlingInvoiceAmount() {
        return invoice.getClaimsHandlingInvoiceAmount();
    }

    public void setClaimsHandlingInvoiceAmount(java.math.BigDecimal claimsHandlingInvoiceAmount) {
        if (actionSelected != reset && invoice != null) {
            invoice.setClaimsHandlingInvoiceAmount(claimsHandlingInvoiceAmount);
        }
    }

    public java.math.BigDecimal getDeductionForClaimsHandlingFee() {
        return invoice.getDeductionForClaimsHandlingFee();
    }

    public void setDeductionForClaimsHandlingFee(java.math.BigDecimal deductionForClaimsHandlingFee) {
        if (actionSelected != reset && invoice != null) {
            invoice.setDeductionForClaimsHandlingFee(deductionForClaimsHandlingFee);
        }
    }

    public java.math.BigDecimal getDiscount() {
        return invoice.getDiscount();
    }

    public void setDiscount(java.math.BigDecimal discount) {
        if (actionSelected != reset && invoice != null) {
            invoice.setDiscount(discount);
        }
    }

    public java.math.BigDecimal getInsurerDiscount() {
        return invoice.getInsurerDiscount();
    }

    public void setInsurerDiscount(java.math.BigDecimal insurerDiscount) {
        if (actionSelected != reset && invoice != null) {
            if (insurerDiscount.compareTo(invoice.getInsurerDiscount()) != 0) {
                LOG.debug("insurerDiscount from form is {} and existing insurerdiscount is {} ", insurerDiscount, invoice.getInsurerDiscount());
            }
            invoice.setInsurerDiscount(insurerDiscount);
        }
    }

    public BigDecimal getTotalGrossInsurerDiscount() {
        return invoice.getTotalGrossInsurerDiscount();
    }
    
    public void setTotalGrossInsurerDiscount(java.math.BigDecimal totalGrossInsurerDiscount) {
        if (actionSelected != reset && invoice != null) {
            if (totalGrossInsurerDiscount.compareTo(invoice.getTotalGrossInsurerDiscount()) != 0 && getTotalGrossInsurerDiscountPercentage(claim).compareTo(BigDecimal.ZERO) == 1) {
                LOG.debug("TotalGrossinsurerDiscount from form is {} and existing TotalGrossinsurerdiscount is {} ", totalGrossInsurerDiscount, invoice.getTotalGrossInsurerDiscount());
                setCanAddTotalGrossInsurerDiscountComment(true);
            }
            invoice.setTotalGrossInsurerDiscount(totalGrossInsurerDiscount);
        }
    }

    
    public BigDecimal getRepairGrossInsurerDiscount() {
        return invoice.getRepairGrossInsurerDiscount();
    }
    
    public void setRepairGrossInsurerDiscount(java.math.BigDecimal repairGrossInsurerDiscount) {
        if (actionSelected != reset && invoice != null) {
            if (repairGrossInsurerDiscount.compareTo(invoice.getRepairGrossInsurerDiscount()) != 0 && getRepairGrossInsurerDiscountPercentage(claim).compareTo(BigDecimal.ZERO) == 1) {
                LOG.debug("RepairGrossinsurerDiscount from form is {} and existing RepairGrossinsurerdiscount is {} ", repairGrossInsurerDiscount, invoice.getRepairGrossInsurerDiscount());
                setCanAddRepairGrossInsurerDiscountComment(true);
            }
            invoice.setRepairGrossInsurerDiscount(repairGrossInsurerDiscount);
        }
    }
    
    public BigDecimal getHireGrossInsurerDiscount() {
        return invoice.getHireGrossInsurerDiscount();
    }
    
    public void setHireGrossInsurerDiscount(java.math.BigDecimal hireGrossInsurerDiscount) {
        if (actionSelected != reset && invoice != null) {
            if (hireGrossInsurerDiscount.compareTo(invoice.getHireGrossInsurerDiscount()) != 0 && getHireGrossInsurerDiscountPercentage(claim).compareTo(BigDecimal.ZERO) == 1) {
                LOG.debug("HireGrossinsurerDiscount from form is {} and existing HireGrossinsurerdiscount is {} ", hireGrossInsurerDiscount, invoice.getHireGrossInsurerDiscount());
                setCanAddHireGrossInsurerDiscountComment(true);
            }
            invoice.setHireGrossInsurerDiscount(hireGrossInsurerDiscount);
        }
    }
    
    public java.math.BigDecimal getFullTotalToPay() {
        return invoice.getFullTotalToPay();
    }

    public void setFullTotalToPay(java.math.BigDecimal totalToPay) {
        if (actionSelected != reset && invoice != null) {
            invoice.setFullTotalToPay(totalToPay);
        }
    }

    public java.lang.String getHandlingInvoiceNo() {
        return invoice.getHandlingInvoiceNo();
    }

    public void setHandlingInvoiceNo(java.lang.String handlingInvoiceNo) {
        if (actionSelected != reset && invoice != null) {

            invoice.setHandlingInvoiceNo(handlingInvoiceNo);
        }
    }

    public java.lang.String getClaimInvoiceNo() {
        return invoice.getClaimInvoiceNo();
    }

    public void setClaimInvoiceNo(java.lang.String claimInvoiceNo) {
        if (actionSelected != reset && invoice != null) {

            invoice.setClaimInvoiceNo(claimInvoiceNo);
        }
    }

    public java.math.BigDecimal getMiscellaneousFee() {
        return invoice.getMiscellaneousFee();
    }

    public void setMiscellaneousFee(java.math.BigDecimal miscellaneousFee) {
        if (actionSelected != reset && invoice != null) {
            invoice.setMiscellaneousFee(miscellaneousFee);
        }
    }

    public java.math.BigDecimal getCollaborationFee() {
        return invoice.getCollaborationFee();
    }

    public void setCollaborationFee(java.math.BigDecimal collaborationFee) {
        if (actionSelected != reset && invoice != null) {
            invoice.setCollaborationFee(collaborationFee);
        }
    }
    
    public Integer getCollaborationQty() {
        return invoice.getCollaborationQty();
    }

    public void setCollaborationQty(Integer collaborationQty) {
        if (actionSelected != reset && invoice != null) {
            invoice.setCollaborationQty(collaborationQty);
        }
    }


    public java.math.BigDecimal getAutomaticFee() {
        return invoice.getAutomaticFee();
    }

    public void setAutomaticFee(java.math.BigDecimal automaticFee) {
        if (actionSelected != reset && invoice != null) {
            invoice.setAutomaticFee(automaticFee);
        }
    }

    public Integer getEstateQty() {
        return invoice.getEstateQty();
    }

    public void setEstateQty(Integer estateQty) {
        if (actionSelected != reset && invoice != null) {
            invoice.setEstateQty(estateQty);
        }
    }

    /**
     * public Integer getMiscellaneousQty() { return
     * invoice.getMiscellaneousQty(); }
     *
     * public void setMiscellaneousQty(Integer miscellaneousQty) { if
     * (actionSelected != reset) {
     * setMiscellaneousQtyOriginal(invoice.getMiscellaneousQty());
     * invoice.setMiscellaneousQty(miscellaneousQty); } }
     *
     */
    public Integer getAutomaticQty() {
        return invoice.getAutomaticQty();
    }

    public void setAutomaticQty(Integer automaticQty) {
        if (actionSelected != reset && invoice != null) {
            invoice.setAutomaticQty(automaticQty);
        }
    }

    public Integer getSatNavQty() {
        return invoice.getSatNavQty();
    }

    public void setSatNavQty(Integer satNavQty) {
        if (actionSelected != reset && invoice != null) {
            invoice.setSatNavQty(satNavQty);
        }
    }

    public Integer getBabySeatQty() {
        return invoice.getBabySeatQty();
    }

    public void setBabySeatQty(Integer babySeatQty) {
        if (actionSelected != reset && invoice != null) {
            invoice.setBabySeatQty(babySeatQty);
        }
    }

    public Integer getTowBarsQty() {
        return invoice.getTowBarsQty();
    }

    public void setTowBarsQty(Integer towBarsQty) {
        if (actionSelected != reset && invoice != null) {
            invoice.setTowBarsQty(towBarsQty);
        }
    }

    public Integer getNonStandardInsurancePremiumQty() {
        return invoice.getNonStandardInsurancePremiumQty();
    }

    public void setNonStandardInsurancePremiumQty(Integer nonStandardInsurancePremiumQty) {
        if (actionSelected != reset && invoice != null) {
            invoice.setNonStandardInsurancePremiumQty(nonStandardInsurancePremiumQty);
        }
    }

    public Integer getAdminQty() {
        return invoice.getAdminQty();
    }

    public void setAdminQty(Integer adminQty) {
        if (actionSelected != reset && invoice != null) {
            invoice.setAdminQty(adminQty);
        }
    }

    public Integer getRoofRackQty() {
        return invoice.getRoofRackQty();
    }

    public void setRoofRackQty(Integer roofRackQty) {
        if (actionSelected != reset && invoice != null) {
            invoice.setRoofRackQty(roofRackQty);
        }
    }

    public Integer getDualControlQty() {
        return invoice.getDualControlQty();
    }

    public void setDualControlQty(Integer dualControlQty) {
        if (actionSelected != reset && invoice != null) {
            invoice.setDualControlQty(dualControlQty);
        }
    }

    public Integer getDeliveryCollectionQty() {
        return invoice.getDeliveryCollectionQty();
    }

    public void setDeliveryCollectionQty(Integer deliveryCollectionQty) {
        if (actionSelected != reset && invoice != null) {
            invoice.setDeliveryCollectionQty(deliveryCollectionQty);
        }
    }

    public java.math.BigDecimal getSatNavFee() {
        return invoice.getSatNavFee();
    }

    public void setSatNavFee(java.math.BigDecimal satNavFee) {
        if (actionSelected != reset && invoice != null) {
            invoice.setSatNavFee(satNavFee);
        }
    }

    public java.math.BigDecimal getEstateFee() {
        return invoice.getEstateFee();
    }

    public void setEstateFee(java.math.BigDecimal estateFee) {
        if (actionSelected != reset && invoice != null) {
            invoice.setEstateFee(estateFee);
        }
    }

    public java.math.BigDecimal getBabySeatFee() {
        return invoice.getBabySeatFee();
    }

    public void setBabySeatFee(java.math.BigDecimal babySeatFee) {
        if (actionSelected != reset && invoice != null) {
            invoice.setBabySeatFee(babySeatFee);
        }
    }

    public java.math.BigDecimal getTowBarsFee() {
        return invoice.getTowBarsFee();
    }

    public void setTowBarsFee(java.math.BigDecimal towBarsFee) {
        if (actionSelected != reset && invoice != null) {
            invoice.setTowBarsFee(towBarsFee);
        }
    }

    public java.math.BigDecimal getNonStandardInsurancePremiumFee() {
        return invoice.getNonStandardInsurancePremiumFee();
    }

    public void setNonStandardInsurancePremiumFee(java.math.BigDecimal nonStandardInsurancePremiumFee) {
        if (actionSelected != reset && invoice != null) {
            setPreviousNonStandardInsurancePremiumFee(invoice.getNonStandardInsurancePremiumFee());
            invoice.setNonStandardInsurancePremiumFee(nonStandardInsurancePremiumFee);
        }
    }

    public java.math.BigDecimal getAdminFee() {
        return invoice.getAdminFee();
    }

    public java.math.BigDecimal getRepairAdminFee() {
        return invoice.getRepairAdminFee();
    }
    
    public java.math.BigDecimal getRepairAcquisitionFee() {
        return invoice.getRepairAcquisitionFee();
    }
    
    public void setAdminFee(java.math.BigDecimal adminFee) {
        if (actionSelected != reset && invoice != null) {
            invoice.setAdminFee(adminFee);
        }
    }
    
    public void setRepairAdminFee(java.math.BigDecimal adminFee) {
        if (actionSelected != reset && invoice != null) {
            invoice.setRepairAdminFee(adminFee);
        }
    }

    public void setRepairAcquisitionFee(java.math.BigDecimal adminFee) {
        if (actionSelected != reset && invoice != null) {
            invoice.setRepairAcquisitionFee(adminFee);
        }
    }

    public java.math.BigDecimal getRoofRackFee() {
        return invoice.getRoofRackFee();
    }

    public void setRoofRackFee(java.math.BigDecimal roofRackFee) {
        if (actionSelected != reset && invoice != null) {
            invoice.setRoofRackFee(roofRackFee);
        }
    }

    public java.math.BigDecimal getDualControlFee() {
        return invoice.getDualControlFee();
    }

    public void setDualControlFee(java.math.BigDecimal dualControlFee) {
        if (actionSelected != reset && invoice != null) {
            invoice.setDualControlFee(dualControlFee);
        }
    }

    public java.math.BigDecimal getDeliveryCollectionFee() {
        return invoice.getDeliveryCollectionFee();
    }

    public void setDeliveryCollectionFee(java.math.BigDecimal deliveryCollectionFee) {
        if (actionSelected != reset && invoice != null) {
            invoice.setDeliveryCollectionFee(deliveryCollectionFee);
        }
    }

    public String getEngineerInvoiceReviewNotes() {
        return invoice.getEngineerInvoiceReviewNotes();
    }

    public void setEngineerInvoiceReviewNotes(String engineerInvoiceReviewNotes) {
        if (actionSelected != reset && invoice != null) {
            invoice.setEngineerInvoiceReviewNotes(engineerInvoiceReviewNotes);
        }
    }

    public boolean isIsEngineerDecisionApproved() {
        return invoice.isIsEngineerDecisionApproved();
    }

    public void setIsEngineerDecisionApproved(boolean isEngineerDecisionApproved) {
        if (actionSelected != reset && invoice != null) {
            invoice.setIsEngineerDecisionApproved(isEngineerDecisionApproved);
        }
    }

    public boolean isIsPaymentMode() {
        return invoice.isIsPaymentMode();
    }

    public void setIsPaymentMode(boolean isPaymentMode) {
        if (actionSelected != reset && invoice != null) {
            invoice.setIsPaymentMode(isPaymentMode);
        }
    }

    public BigDecimal getHireRateChargedPerDay() {
        return invoice.getHireRateChargedPerDay();
    }

    public void setHireRateChargedPerDay(BigDecimal hireRateChargedPerDay) {
        if (actionSelected != reset && invoice != null) {
            invoice.setHireRateChargedPerDay(hireRateChargedPerDay);
        }
    }

    public BigDecimal getExcessAmountCollected() {
        return invoice.getExcessAmountCollected();
    }

    public void setExcessAmountCollected(BigDecimal excessAmountCollected) {
        if (actionSelected != reset && invoice != null) {
            invoice.setExcessAmountCollected(excessAmountCollected);
        }
    }

    public BigDecimal getVatAmountCollected() {
        return invoice.getVatAmountCollected();
    }

    public void setVatAmountCollected(BigDecimal vatAmountCollected) {
        if (actionSelected != reset && invoice != null) {
            invoice.setVatAmountCollected(vatAmountCollected);
        }
    }

    public BigDecimal getHirePenaltyCharge() {
        return invoice.getHirePenaltyCharge();
    }

    public void setHirePenaltyCharge(BigDecimal hirePenaltyCharge) {
        if (actionSelected != reset && invoice != null) {
            invoice.setHirePenaltyCharge(hirePenaltyCharge);
        }

    }

    public BigDecimal getRepairPenaltyCharge() {
        return invoice.getRepairPenaltyCharge();
    }

    public void setRepairPenaltyCharge(BigDecimal repairPenaltyCharge) {
        if (actionSelected != reset && invoice != null) {
            invoice.setRepairPenaltyCharge(repairPenaltyCharge);
        }
    }

    public Integer getPenaltyBand() {
        return invoice.getPenaltyBand();
    }

    public void setPenaltyBand(Integer penaltyBand) {
        if (actionSelected != reset && invoice != null) {
            invoice.setPenaltyBand(penaltyBand);
        }
    }

    public int getInvoicedDays() {
        // long dateDiff = DateHelper.getNumberOf24HourPeriodsBetween(getDateInvoiced(), new Date()) + 1;
        return invoice.getInvoicedDays();

    }

    public ReasonOfRejection getReasonOfRejection() {
        return invoice.getReasonOfRejection();
    }

    public void setReasonOfRejection(ReasonOfRejection reasonOfRejection) {
        if (actionSelected != reset && invoice != null) {
            invoice.setReasonOfRejection(reasonOfRejection);
        }
    }

    public Date getHirePenaltyChargeAppliedDate() {
        return invoice.getHirePenaltyChargeAppliedDate();
    }

    public void setHirePenaltyChargeAppliedDate(Date hirePenaltyChargeAppliedDate) {
        if (actionSelected != reset && invoice != null) {
            invoice.setHirePenaltyChargeAppliedDate(hirePenaltyChargeAppliedDate);
        }
    }

    public Date getRepairPenaltyChargeAppliedDate() {
        return invoice.getRepairPenaltyChargeAppliedDate();
    }

    public void setRepairPenaltyChargeAppliedDate(Date repairPenaltyChargeAppliedDate) {
        if (actionSelected != reset && invoice != null) {
            invoice.setRepairPenaltyChargeAppliedDate(repairPenaltyChargeAppliedDate);
        }
    }

    public BigDecimal getTotalToPay() {
        return invoice.getTotalToPay();
    }

    public void setTotalToPay(BigDecimal totalToPaySplitLiability) {
        LOG.debug("setTotalToPay() is called with the value of {}", totalToPaySplitLiability);
        if (actionSelected != reset && invoice != null) {
            LOG.debug("setTotalToPay() is passed through the reset condition with the value of {}", totalToPaySplitLiability);
            LOG.debug("setTotalToPayOriginal() from settotaltopay() is called with the value of {}", getTotalToPay());
            invoice.setTotalToPay(totalToPaySplitLiability);
            LOG.debug("setTotalToPay set up done");
        }
    }

    public BigDecimal getAdditionalDriverFee() {

        return invoice.getAdditionalDriverFee();
    }

    public void setAdditionalDriverFee(BigDecimal additionalDriverFee) {
        if (actionSelected != reset && invoice != null) {
            invoice.setAdditionalDriverFee(additionalDriverFee);
        }
    }

    public Integer getAdditionalDriverQty() {

        return invoice.getAdditionalDriverQty();
    }

    public void setAdditionalDriverQty(Integer additionalDriverQty) {
        if (actionSelected != reset && invoice != null) {
            invoice.setAdditionalDriverQty(additionalDriverQty);
        }
    }

    public Boolean getCoverNoteRequired() {
        return invoice.getCoverNoteRequired();
    }

    public void setCoverNoteRequired(Boolean coverNoteRequired) {

        if (actionSelected != reset && invoice != null) {
            invoice.setCoverNoteRequired(coverNoteRequired);
        }
    }

    public String getCoverNoteRequiredDesc() {

        return invoice.getCoverNoteRequiredDesc();

    }

    public BigDecimal getTotalLossFeeGross() {
        return invoice.getTotalLossFeeGross();
    }

    public void setTotalLossFeeGross(BigDecimal totalLossFeeGross) {
        if (actionSelected != reset && invoice != null) {
            invoice.setTotalLossFeeGross(totalLossFeeGross);
        }
    }

    public BigDecimal getTotalLossFeeNet() {
        return invoice.getTotalLossFeeNet();
    }

    public void setTotalLossFeeNet(BigDecimal totalLossFeeNet) {
        if (actionSelected != reset && invoice != null) {
            setPreviousTotalLossNet(invoice.getTotalLossFeeNet());
            invoice.setTotalLossFeeNet(totalLossFeeNet);
        }
    }

    public BigDecimal getTotalLossFeeVat() {
        return invoice.getTotalLossFeeVat();
    }

    public void setTotalLossFeeVat(BigDecimal totalLossFeeVat) {
        if (actionSelected != reset && invoice != null) {
            setPreviousTotalLossVat(invoice.getTotalLossFeeVat());
            invoice.setTotalLossFeeVat(totalLossFeeVat);
        }
    }

    public String getHirePenaltyPercentage() {
        return invoice.getHirePenaltyPercentage();
    }

    public void setHirePenaltyPercentage(String hirePenaltyPercentage) {
        if (actionSelected != reset && invoice != null) {
            invoice.setHirePenaltyPercentage(hirePenaltyPercentage);
        }
    }

    public String getRepairPenaltyPercentage() {
        return invoice.getRepairPenaltyPercentage();
    }

    public void setRepairPenaltyPercentage(String repairPenaltyPercentage) {
        if (actionSelected != reset && invoice != null) {
            invoice.setRepairPenaltyPercentage(repairPenaltyPercentage);
        }
    }

    public boolean isAppliedHirePenaltyPercentageDifferent() {
        return invoice.isAppliedHirePenaltyPercentageDifferent();
    }

    public boolean isAppliedRepairPenaltyPercentageDifferent() {
        return invoice.isAppliedRepairPenaltyPercentageDifferent();
    }
    
    public String getRepairPenaltyPercentageApplied() {
        return invoice.getRepairPenaltyPercentageApplied();
    }

    public String getHirePenaltyPercentageApplied() {
        return invoice.getHirePenaltyPercentageApplied();
    }
    
    public BigDecimal getInterimPaymentReceived() {
        LOG.debug("getInterimPaymentReceived is being called inside InvoiceDetailAction and returning value is {}", invoice.getInterimPaymentReceived());
        if (invoice.getInterimPaymentReceived() == null) {
            return BigDecimal.ZERO;
        }
        return invoice.getInterimPaymentReceived();
    }

    public BigDecimal getOutstandingInterimPayment() {
        BigDecimal interimPayment = BigDecimal.ZERO.setScale(2);
        if (invoice.getInterimPaymentMade() != null) {
            interimPayment = invoice.getInterimPaymentMade();
        }
        return interimPayment.subtract(getInterimPaymentReceived());
    }

    public BigDecimal getInterimPaymentMade() {
        LOG.debug("getInterimPaymentMade() is being called inside InvoiceDetailAction and returning value is {}", invoice.getInterimPaymentMade());
        if (invoice.getInterimPaymentMade() == null) {
            return BigDecimal.ZERO;
        }
        return invoice.getInterimPaymentMade();
    }

    public Boolean isInterimPaymentReceivedFullAndFinal() {
        return invoice.isInterimPaymentReceivedFullAndFinal();
    }

    public BigDecimal getTotalPenaltyCharge() {
        return invoice.getTotalPenaltyCharge();
    }

    public void setTotalPenaltyCharge(BigDecimal totalPenaltyCharge) {
        if (actionSelected != reset && invoice != null) {
            invoice.setTotalPenaltyCharge(totalPenaltyCharge);
        }
    }

    public BigDecimal getEngineerFeeGrossPaid() {
        return invoice.getEngineerFeeGrossPaid();
    }

    public BigDecimal getHireGrossPaid() {
        return invoice.getHireGrossPaid();
    }

    public BigDecimal getHirePenaltyChargePaid() {
        return invoice.getHirePenaltyChargePaid();
    }

    public BigDecimal getRepairGrossPaid() {
        return invoice.getRepairGrossPaid();
    }

    public BigDecimal getRepairPenaltyChargePaid() {
        return invoice.getRepairPenaltyChargePaid();
    }

    public BigDecimal getStorageRecoveryGrossPaid() {
        return invoice.getStorageRecoveryGrossPaid();
    }

    public BigDecimal getTotalLossFeeGrossPaid() {
        return invoice.getTotalLossFeeGrossPaid();
    }

    public boolean isPenaltyChargesPaid() {
        return invoice.isPenaltyChargesPaid();
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="InvoiceAction">
    String getTabName() {
        return TabAccessibility.TAB_INVOICE_DETAIL;
    }

    public String getDaysWithCHOForReview() {
        LOG.debug("Getting number of days claim was with CHO for review");
        if (daysWithCHOForReview == null) {
            daysWithCHOForReview = claimService.getDaysWithCHOForReview(claim.getId());
        }
        return daysWithCHOForReview;
    }

    public String getDaysWithInsurerForReview() {
        LOG.debug("Getting number of days claim was with Insurer for review");
        if (daysWithInsurerForReview == null) {
            daysWithInsurerForReview = claimService.getDaysWithInsurerForReview(claim.getId());
        }
        return daysWithInsurerForReview;
    }

    public String getDaysAwaitingLiabilityResolution() {
        LOG.debug("Getting number of days claim was with Insurer for review");
        if (daysAwaitingLiabilityResolution == null) {
            daysAwaitingLiabilityResolution = claimService.getDaysAwaitingLiabilityResolution(claim.getId());
        }
        return daysAwaitingLiabilityResolution;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="VehicleHireAction">
    private void updateHpi() {
        if (!oldVRN.equalsIgnoreCase(vehicleHire.getVehicleRegistration())) {
            try {
                LOG.debug("VRN has changed - performing HPI check/retrieval");
                HpiResponse response = Hpi.getHpiInfo(vehicleHire.getVehicleRegistration());
                vehicleHire.setHpiVehicleManufacturer(response.getManufacturer());
                vehicleHire.setHpiVehicleModel(response.getModel());
                vehicleHire.setHpiVehicleYear(response.getYear());
                vehicleHire.setHpiVehicleCapacity(response.getCapacity());
                vehicleHire.setHpiVehicleDoorplan(response.getDoorPlan());
                vehicleHire.setHpiVehicleTransmission(response.getTransmission());
                vehicleHire.setHpiFirstRegistration(response.getFirstRegistration());
                vehicleHire.setHpiError(null);
            } catch (HpiException ex) {
                LOG.warn("Error getting HPI info for vrn '{}': {}", claim.getCustomer().getVehicleRegistration(), ex.getMessage());
                vehicleHire.setHpiError(ex.getMessage());
                vehicleHire.setHpiVehicleManufacturer(null);
                vehicleHire.setHpiVehicleModel(null);
                vehicleHire.setHpiVehicleYear(null);
                vehicleHire.setHpiVehicleCapacity(null);
                vehicleHire.setHpiVehicleDoorplan(null);
                vehicleHire.setHpiVehicleTransmission(null);
                vehicleHire.setHpiFirstRegistration(null);
            }
        }
    }

    public boolean isModelSaved() {
        return modelSaved;
    }

    public String getOldVRN() {
        return oldVRN;
    }

    public String getVehicleClassNameOriginal() {
        if (getVehicleClassIdOriginal() != 0) {
            return lookupService.getVehicleClassName(getVehicleClassIdOriginal());
        } else {
            return null;
        }

    }

    public String getVehicleClassName() {
        if (getVehicleClassId() != 0) {
            return lookupService.getVehicleClassName(getVehicleClassId());
        } else {
            return null;
        }

    }

    public String getRentalStartTime() {
        if (vehicleHire == null) {
            return null;
        }
        return DateHelper.getTimeFormat().format(vehicleHire.getHireStart());
    }
    
    public void setRentalStartTime(String time) {
        if (actionSelected != reset && vehicleHire != null) {
            setRentalStartTimeOriginal(getRentalStartTime());
            try {
                Date a = vehicleHire.getHireStart();
                Date b = DateHelper.getTimeFormat().parse(time);
                vehicleHire.setHireStart(DateHelper.mergeTimeToDate(a, b));
            } catch (Exception ex) {
                LOG.error("Error setting Rental Start-time to '{}': {}", time, ex.getMessage());
            }
        }
    }

    public String getRentalEndTime() {
        return DateHelper.getTimeFormat().format(vehicleHire.getHireEnd());
    }

    public void setRentalEndTime(String time) {
        if (actionSelected != reset && vehicleHire != null) {
            setRentalEndTimeOriginal(getRentalEndTime());
            if (vehicleHire != null) {
                try {
                    Date a = vehicleHire.getHireEnd();
                    Date b = DateHelper.getTimeFormat().parse(time);
                    vehicleHire.setHireEnd(DateHelper.mergeTimeToDate(a, b));
                } catch (Exception ex) {
                    LOG.error("Error setting Rental End-time to '{}': {}", time, ex.getMessage());
                }
            }
        }


    }

    public String getRentalStartTimeOriginal() {
        return DateHelper.getTimeFormat().format(vehicleHire.getHireStartOriginal());
    }

    public void setRentalStartTimeOriginal(String time) {
        if (time != null && !time.equals(getRentalStartTimeOriginal()) && (getRentalStartTimeOriginal() == null)) {
            if (vehicleHire != null) {
                try {
                    Date a = vehicleHire.getHireStartOriginal();
                    Date b = DateHelper.getTimeFormat().parse(time);
                    vehicleHire.setHireStartOriginal(DateHelper.mergeTimeToDate(a, b));
                } catch (Exception ex) {
                    LOG.error("Error setting Rental Start-time-original to '{}': {}", time, ex.getMessage());
                }
            }

        }

    }

    public String getRentalEndTimeOriginal() {
        return DateHelper.getTimeFormat().format(vehicleHire.getHireEndOriginal());

    }

    public void setRentalEndTimeOriginal(String time) {

        if (!time.equals(getRentalEndTimeOriginal()) && (getRentalEndTimeOriginal() == null)) {
            if (vehicleHire != null) {
                try {
                    Date a = vehicleHire.getHireEndOriginal();
                    Date b = DateHelper.getTimeFormat().parse(time);
                    vehicleHire.setHireEndOriginal(DateHelper.mergeTimeToDate(a, b));
                } catch (Exception ex) {
                    LOG.error("Error setting Rental End-time-original to '{}': {}", time, ex.getMessage());
                }
            }
        }


    }

    public int getVehicleClassId() {
        if (this.vehicleHire != null && this.vehicleHire.getVehicleClass() != null) {
            return this.vehicleHire.getVehicleClass().getId();
        }
        return 0;
    }

    public List<VehicleClass> getVehicleClasses() {
        List<VehicleClass> vehicleClasses = this.lookupService.getVehicleClasses();
        Collections.sort(vehicleClasses, new VehicleClassComparator());

        return vehicleClasses;
    }

    public int getVehicleClassIdOriginal() {
        if (this.vehicleHire != null && this.vehicleHire.getVehicleClassOriginal() != null) {
            return this.vehicleHire.getVehicleClassOriginal().getId();
        }
        return 0;
    }

    public void setVehicleClassIdOriginal(int vehicleClassId) {

        if (vehicleClassId != getVehicleClassIdOriginal() && getVehicleClassIdOriginal() == 0) {

            List<VehicleClass> vehicleClasses = this.lookupService.getVehicleClasses();
            for (VehicleClass vClass : vehicleClasses) {
                if (vClass.getId() == vehicleClassId) {
                    vehicleHire.setVehicleClassOriginal(vClass);
                    break;
                }
            }
        }

    }

    public void setVehicleClassId(int vehicleClassId) {
        if (actionSelected != reset) {
            if (vehicleHire != null) {
                setVehicleClassIdOriginal(getVehicleClassId());
                if (vehicleHire.getVehicleClass().getId() != vehicleClassId) {
                    List<VehicleClass> vehicleClasses = this.lookupService.getVehicleClasses();
                    for (VehicleClass vClass : vehicleClasses) {
                        if (vClass.getId() == vehicleClassId) {
                            vehicleHire.setVehicleClass(vClass);
                            break;
                        }
                    }
                }
            }
        }
    }

    public String getRentalStartTimeDisplayFormat() {
        String time = getRentalStartTimeOriginal();
        if (time.equals("24:00")) {
            return "00:00";
        } else {
            return time;
        }
    }

    public String getRentalEndTimeDisplayFormat() {
        String time = getRentalEndTimeOriginal();
        if (time.equals("24:00")) {
            return "00:00";
        } else {
            return time;
        }
    }

    public boolean getIsFixedFeeOrSubscriberClaim() {
        return ClaimType.isSubscriber(claim.getClaimType()) || ClaimType.isFixedFee(claim.getClaimType());
    }

    public boolean getIsSubscriberClaim() {
        return ClaimType.isSubscriber(claim.getClaimType());
    }

    public boolean getIsCollaborationProtocolClaim() {
        return ClaimType.isCollaborationProtocol(claim.getClaimType());
    }

    public boolean getIsFixedFeeClaim() {
        return ClaimType.isFixedFee(claim.getClaimType());
    }

    public boolean getCanShowOriginalStartDate() {

        String d1 = DateHelper.getLocalDateFormat().format(getRentalStart());
        String d2 = DateHelper.getLocalDateFormat().format(getRentalStartOriginal());
        
        return !d1.equals(d2);
    }

    public boolean getCanShowOriginalEndDate() {

        String d1 = DateHelper.getLocalDateFormat().format(getRentalEnd());
        String d2 = DateHelper.getLocalDateFormat().format(getRentalEndOriginal());

        return !d1.equals(d2);
    }

    public boolean getCanShowOriginalInvoicedDate() {

        String d1 = DateHelper.getLocalDateFormat().format(getDateInvoiced());
        String d2 = DateHelper.getLocalDateFormat().format(getDateInvoicedOriginal());

        return !d1.equals(d2);
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="VehicleHire">
    public boolean isTpiClaim() {
        return ClaimType.isTPI(claim.getClaimType());
    }

    public String getCourtesyCarProvidedDesc() {
        return vehicleHire.getCourtesyCarProvidedDesc();
    }

    public boolean isCourtesyCarProvided() {
        return vehicleHire.isCourtesyCarProvided();
    }

    public void setCourtesyCarProvided(boolean courtesyCarProvided) {
        if (actionSelected != reset && vehicleHire != null) {
            vehicleHire.setCourtesyCarProvided(courtesyCarProvided);
        }
    }

    public void setIsTotalLoss(boolean IsTotalLoss) {
        if (actionSelected != reset && vehicleHire != null) {
            vehicleHire.setIsTotalLoss(IsTotalLoss);
        }
    }

    public java.lang.String getVehicleRegistration() {
        return vehicleHire.getVehicleRegistration();
    }

    public void setVehicleRegistration(java.lang.String vehicleRegistration) {
        if (actionSelected != reset && vehicleHire != null) {
            vehicleHire.setVehicleRegistration(vehicleRegistration);
        }
    }

    public java.lang.String getVehicleManufacturer() {
        return vehicleHire.getVehicleManufacturer();
    }

    public void setVehicleManufacturer(java.lang.String vehicleManufacturer) {
        if (actionSelected != reset && vehicleHire != null) {
            vehicleHire.setVehicleManufacturer(vehicleManufacturer);
        }
    }

    public java.lang.String getVehicleModel() {
        return vehicleHire.getVehicleModel();
    }

    public void setVehicleModel(java.lang.String vehicleModel) {
        if (actionSelected != reset && vehicleHire != null) {
            vehicleHire.setVehicleModel(vehicleModel);

        }
    }

    public java.util.Date getRentalStart() {
        return vehicleHire.getRentalStart();
    }

    public void setRentalStart(java.util.Date rentalStart) {
        if (actionSelected != reset && vehicleHire != null) {
            setRentalStartOriginal(getRentalStart());
            vehicleHire.setRentalStart(rentalStart);
        }
    }

    public java.util.Date getRentalStartOriginal() {
        return vehicleHire.getRentalStartOriginal();
    }

    public void setRentalStartOriginal(java.util.Date rentalStart) {
        if (rentalStart != getRentalStartOriginal() && getRentalStartOriginal() == null && vehicleHire != null) {
            vehicleHire.setRentalStartOriginal(rentalStart);
        }
    }

    public java.util.Date getRentalEnd() {
        return vehicleHire.getRentalEnd();
    }

    public void setRentalEnd(java.util.Date rentalEnd) {
        if (actionSelected != reset && vehicleHire != null) {
            setRentalEndOriginal(getRentalEnd());
            vehicleHire.setRentalEnd(rentalEnd);
        }
    }

    public java.util.Date getRentalEndOriginal() {
        return vehicleHire.getRentalEndOriginal();
    }

    public void setRentalEndOriginal(java.util.Date rentalEnd) {
        if (rentalEnd != getRentalEndOriginal() && getRentalEndOriginal() == null && vehicleHire != null) {
            vehicleHire.setRentalEndOriginal(rentalEnd);
        }
    }

    public java.lang.String getCollectionReason() {
        return vehicleHire.getCollectionReason();
    }

    public void setCollectionReason(java.lang.String collectionReason) {
        if (actionSelected != reset && vehicleHire != null) {
            vehicleHire.setCollectionReason(collectionReason);
        }
    }

    public Integer getDaysOriginal() {
        return vehicleHire.getDaysOriginal();
    }

    public void setDaysOriginal(Integer days) {
        if (getDaysOriginal() == null && vehicleHire != null) {
            vehicleHire.setDaysOriginal(days);
        }
    }

    public Integer getDays() {

        return vehicleHire.getDays();
    }

    public void setDays(Integer days) {


        days = (days == null) ? 0 : days;

        if (actionSelected != reset && vehicleHire != null) {

            setDaysOriginal(getDays());

            vehicleHire.setDays(days);

        }
    }

    public VehicleClass getVehicleClass() {
        return vehicleHire.getVehicleClass();
    }

    public void setVehicleClass(VehicleClass vehicleClass) {
        if (actionSelected != reset && vehicleHire != null) {
            vehicleHire.setVehicleClass(vehicleClass);
        }
    }

    public java.util.Date getHireStart() {
        return vehicleHire.getHireStart();
    }

    public void setHireStart(Date hireStart) {
        if (actionSelected != reset && vehicleHire != null) {
            vehicleHire.setHireStart(hireStart);
        }
    }

    public java.util.Date getHireEnd() {
        return vehicleHire.getHireEnd();
    }

    public void setHireEnd(Date hireEnd) {
        if (actionSelected != reset && vehicleHire != null) {
            vehicleHire.setHireEnd(hireEnd);
        }
    }

    // ##### NOT FROM HERE #############
    public boolean getIsTotalLoss() {
        return vehicleHire.getIsTotalLoss();
    }

    public String getHpiError() {
        return vehicleHire.getHpiError();
    }

    public void setHpiError(String hpiError) {
        if (actionSelected != reset && vehicleHire != null) {
            vehicleHire.setHpiError(hpiError);
        }
    }

    public String getHpiVehicleCapacity() {
        return vehicleHire.getHpiVehicleCapacity();
    }

    public void setHpiVehicleCapacity(String hpiVehicleCapacity) {
        if (actionSelected != reset && vehicleHire != null) {
            vehicleHire.setHpiVehicleCapacity(hpiVehicleCapacity);
        }
    }

    public String getHpiVehicleDoorplan() {
        return vehicleHire.getHpiVehicleDoorplan();
    }

    public void setHpiVehicleDoorplan(String hpiVehicleDoorplan) {
        if (actionSelected != reset && vehicleHire != null) {
            vehicleHire.setHpiVehicleDoorplan(hpiVehicleDoorplan);
        }
    }

    public String getHpiVehicleManufacturer() {
        return vehicleHire.getHpiVehicleManufacturer();
    }

    public void setHpiVehicleManufacturer(String hpiVehicleManufacturer) {
        if (actionSelected != reset && vehicleHire != null) {
            vehicleHire.setHpiVehicleManufacturer(hpiVehicleManufacturer);
        }
    }

    public String getHpiVehicleModel() {
        return vehicleHire.getHpiVehicleModel();
    }

    public void setHpiVehicleModel(String hpiVehicleModel) {
        if (actionSelected != reset && vehicleHire != null) {
            vehicleHire.setHpiVehicleModel(hpiVehicleModel);
        }
    }

    public String getHpiVehicleTransmission() {
        return vehicleHire.getHpiVehicleTransmission();
    }

    public void setHpiVehicleTransmission(String hpiVehicleTransmission) {
        if (actionSelected != reset && vehicleHire != null) {
            vehicleHire.setHpiVehicleTransmission(hpiVehicleTransmission);
        }
    }

    public String getHpiVehicleYear() {
        return vehicleHire.getHpiVehicleYear();
    }

    public void setHpiVehicleYear(String hpiVehicleYear) {
        if (actionSelected != reset && vehicleHire != null) {
            vehicleHire.setHpiVehicleYear(hpiVehicleYear);
        }
    }

    public void setHpiFirstRegistration(Date firstRegistration) {
        if (actionSelected != reset && vehicleHire != null) {
            vehicleHire.setHpiFirstRegistration(firstRegistration);
        }
    }

    public Date getHpiFirstRegistration() {
        return vehicleHire.getHpiFirstRegistration();
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="EngineerReportAction">
    // Nothing to be added ( no getter and setter for this perticular action class)
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="EngineerReport">
    public java.lang.Integer getEstimatedDays() {
        return engineerReport.getDays();
    }

    public void setEstimatedDays(java.lang.Integer days) {
        if (actionSelected != reset && vehicleHire != null) {
            engineerReport.setDays(days);
        }
    }

    public java.lang.String getName() {
        return engineerReport.getName();
    }

    public void setName(java.lang.String name) {
        if (actionSelected != reset && vehicleHire != null) {
            engineerReport.setName(name);
        }
    }

    public java.lang.String getCompany() {
        return engineerReport.getCompany();
    }

    public void setCompany(java.lang.String company) {
        if (actionSelected != reset && vehicleHire != null) {
            engineerReport.setCompany(company);
        }
    }

    public java.lang.String getAddress1() {
        return engineerReport.getAddress1();
    }

    public void setAddress1(java.lang.String address1) {
        if (actionSelected != reset && vehicleHire != null) {
            engineerReport.setAddress1(address1);
        }
    }

    public java.lang.String getAddress2() {
        return engineerReport.getAddress2();
    }

    public void setAddress2(java.lang.String address2) {
        if (actionSelected != reset && vehicleHire != null) {
            engineerReport.setAddress2(address2);
        }
    }

    public java.lang.String getAddress3() {
        return engineerReport.getAddress3();
    }

    public void setAddress3(java.lang.String address3) {
        if (actionSelected != reset && vehicleHire != null) {
            engineerReport.setAddress3(address3);
        }
    }

    public java.lang.String getAddress4() {
        return engineerReport.getAddress4();
    }

    public void setAddress4(java.lang.String address4) {
        if (actionSelected != reset && vehicleHire != null) {
            engineerReport.setAddress4(address4);
        }
    }

    public java.lang.String getAddress5() {
        return engineerReport.getAddress5();
    }

    public void setAddress5(java.lang.String address5) {
        if (actionSelected != reset && vehicleHire != null) {
            engineerReport.setAddress5(address5);
        }
    }

    public java.lang.String getPostcode() {
        return engineerReport.getPostcode();
    }

    public void setPostcode(java.lang.String postcode) {
        if (actionSelected != reset && vehicleHire != null) {
            engineerReport.setPostcode(postcode);
        }
    }

    public java.lang.String getTelephone() {
        return engineerReport.getTelephone();
    }

    public void setTelephone(java.lang.String telephone) {
        if (actionSelected != reset && vehicleHire != null) {
            engineerReport.setTelephone(telephone);
        }
    }

    public java.lang.String getEmail() {
        return engineerReport.getEmail();
    }

    public void setEmail(java.lang.String email) {
        if (actionSelected != reset && vehicleHire != null) {
            engineerReport.setEmail(email);
        }
    }

    public Boolean getIsUsable() {
        return engineerReport.isIsUsable();
    }

    public void setIsUsable(Boolean isUsable) {
        if (actionSelected != reset && vehicleHire != null) {
            engineerReport.setIsUsable(isUsable);
        }
    }

    public java.math.BigDecimal getLabourAmount() {
        return engineerReport.getLabourAmount();
    }

    public void setLabourAmount(java.math.BigDecimal labourAmount) {
        if (actionSelected != reset && vehicleHire != null) {
            engineerReport.setLabourAmount(labourAmount);
        }
    }

    public java.math.BigDecimal getTotalAmount() {
        return engineerReport.getTotalAmount();
    }

    public void setTotalAmount(java.math.BigDecimal totalAmount) {
        if (actionSelected != reset && vehicleHire != null) {
            engineerReport.setTotalAmount(totalAmount);
        }
    }

    public BigDecimal getEstimatedLabourAmount() {
        return engineerReport.getEstimatedLabourAmount();
    }

    public BigDecimal getEstimatedTotalRepairAmount() {
        return engineerReport.getEstimatedTotalRepairAmount();
    }

    public int getEstimatedDaysUnderRepair() {
        return engineerReport.getEstimatedDaysUnderRepair();
    }

    public String getIsUsableDesc() {
        return engineerReport.getIsUsableDesc();

    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="updateModel">
//    @Secured({"ROLE_CHOX_ADMIN", "ROLE_CHO"})
    public String updateModel() {

        if (actionSelected == reset) {
            this.setActionResult("Invoice Reset");
            return SUCCESS;
        } else if (actionSelected == recalculate) {
            try {
                recalculate(claim);
            } catch (Exception ex) {
                handleException(ex);
                LOG.debug("Exception is thrown and Error will be displayed in the page {} ", ex.getMessage());
                return ERROR;
            }
            return SUCCESS;
        } else if (actionSelected == submit) {
            try {
                checkVersion(Arrays.asList(engineerReport, vehicleHire, invoice, claim));
                claim.setEngineerReport(engineerReport);
//                claim.setInvoiceOriginal(invoiceOriginal);
                claim.setVehicleHire(vehicleHire);
                claim.setInvoice(invoice);
                claimService.updateLiabilityPayment(claim);
                addModifiedFieldsComment();
                updateHpi();
                for (InsurerDiscountType insurerDiscountType : InsurerDiscountType.values()) {
                    if (getCanAddTotalGrossInsurerDiscountComment() && insurerDiscountType.getInsurerDiscountTypeValue() == InsurerDiscountType.TOTAL.getInsurerDiscountTypeValue()) {
                        BigDecimal totalGrossInsurerDiscountPercentage = getTotalGrossInsurerDiscountPercentage(claim);
                        insurerDiscountService.addInsurerDiscountComment(claim, getTotalGrossInsurerDiscount().multiply(BigDecimal.valueOf(-1)), totalGrossInsurerDiscountPercentage, insurerDiscountType.toString(), userService.findByUserName("system"));
                    }
                    if (getCanAddRepairGrossInsurerDiscountComment() && insurerDiscountType.getInsurerDiscountTypeValue() == InsurerDiscountType.REPAIR.getInsurerDiscountTypeValue()) {
                        BigDecimal repairGrossInsurerDiscountPercentage = getRepairGrossInsurerDiscountPercentage(claim);
                        insurerDiscountService.addInsurerDiscountComment(claim, getRepairGrossInsurerDiscount().multiply(BigDecimal.valueOf(-1)), repairGrossInsurerDiscountPercentage, insurerDiscountType.toString(), userService.findByUserName("system"));
                    }
                    if (getCanAddHireGrossInsurerDiscountComment() && insurerDiscountType.getInsurerDiscountTypeValue() == InsurerDiscountType.HIRE.getInsurerDiscountTypeValue()) {
                        BigDecimal hireGrossInsurerDiscountPercentage = getHireGrossInsurerDiscountPercentage(claim);
                        insurerDiscountService.addInsurerDiscountComment(claim, getHireGrossInsurerDiscount().multiply(BigDecimal.valueOf(-1)), hireGrossInsurerDiscountPercentage, insurerDiscountType.toString(), userService.findByUserName("system"));
                    }
                }
                
                claimService.updateClaim(claim);
                updateModelInSession(Arrays.asList(engineerReport, vehicleHire, invoice, claim));
                modelSaved = true;
                this.setActionResult("Your Changes Have Been Saved");
                eventGenerator.generate(claim, ActivityEvent.INVOICE_UPDATED_EVENT);
                return SUCCESS;
            } catch (Exception ex) {
                LOG.warn("Exception is thrown and passing to baseAction ", ex);
                handleException(ex);
                return ERROR;
            }
        } else {
            return ERROR;
        }
    }
    
    public boolean isPenaltyChargesAppled() {
        if (modelSaved && claim.getInvoice().getTotalPenaltyCharge() != null
                && claim.getInvoice().getTotalPenaltyCharge().compareTo(BigDecimal.ZERO) != 0) {
            return true;
        }

        return false;
    }

    private void addModifiedFieldsComment() {
        try {
            Map<String, String> fieldNames = new LinkedHashMap<String, String>();
            StringBuilder sb = new StringBuilder();
            boolean isSubscriberClaim = false;
            boolean isCollaborationProtocolClaim = false;
            int stringLength = 0;
            if (ClaimType.isSubscriber(claim.getClaimType())) {
                isSubscriberClaim = true;
            } else if (ClaimType.isCollaborationProtocol(claim.getClaimType())) {
                isCollaborationProtocolClaim = true;
            }
            // get the list of fields name and corresponding dispaly name of Invoice.
            for (Invoice.DisplayName displayName : Invoice.DisplayName.values()) {
                if (isSubscriberClaim) {
                    if (displayName.getParameterName().equals("miscellaneousFee")) {
                        fieldNames.put(displayName.getParameterName(), "Acquisition Fee");
                        continue;
                    }
                }
                fieldNames.put(displayName.getParameterName(), displayName.toString());
            }
            // remove acquisitionFee otherwise for subscriber claims 'Acquisition Fee' will be duplicated.
            if (!isCollaborationProtocolClaim) { 
                fieldNames.remove("acquisitionFee");
            }
            // get the changes made to invoice detail section.
            stringLength = sb.length();
            getModifiedFieldAsText(Invoice.class, originalInvoice, invoice, fieldNames, sb);
            if (sb.length() > stringLength) {
                sb.insert(stringLength, "Invoice Details:");// if this text needs changing, also change in p_claim_detail_comment.jsp page.
            }
            
            // get the list of fields name and corresponding dispaly name of VehicleHire.
            for (VehicleHire.DisplayName displayName : VehicleHire.DisplayName.values()) {
                fieldNames.put(displayName.getParameterName(), displayName.toString());
            }
            // get the changes made to vehicleHire detail section.
            stringLength = sb.length();
            getModifiedFieldAsText(VehicleHire.class, originalVehicleHire, vehicleHire, fieldNames, sb);
            if (sb.length() > stringLength) {
                sb.insert(stringLength, "Hire Vehicle Details:");// if this text needs changing, also change in p_claim_detail_comment.jsp page.
            }

            // get the list of fields name and corresponding dispaly name of EngineerReport.
            for (EngineerReport.DisplayName displayName : EngineerReport.DisplayName.values()) {
                fieldNames.put(displayName.getParameterName(), displayName.toString());
            }
            // get the changes made to engineer report detail section.
            stringLength = sb.length();
            getModifiedFieldAsText(EngineerReport.class, originalEngineerReport, engineerReport, fieldNames, sb);
            if (sb.length() > stringLength) {
                sb.insert(stringLength, "Engineer Report:");// if this text needs changing, also change in p_claim_detail_comment.jsp page.
            }
            
            if (sb.length() > 0) {
                sb.insert(0, "An invoice amendment has been made to the following fields: "); // if this text needs changing, also change in p_claim_detail_comment.jsp page.
                claim.addComment(Comment.newComment(0, sb.toString()));
            }
        } catch (Exception ex) {
            LOG.error("Exception while adding modifiedFieldsComment : ", ex);
        }
    }
    
    private void getModifiedFieldAsText(Class model, Object originalObject, Object modifiedObject, Map<String, String> fieldNames, StringBuilder sb) {
        Map<String, Object[]> modifiedFields = CompareUtil.compare(model, originalObject, modifiedObject, fieldNames);
        // iterate through enum fieldNames instead modifiedFields so that the order the text added is same as in the UI.
        for (String key : fieldNames.keySet()) {
            if (modifiedFields.containsKey(fieldNames.get(key))) {
                if ((modifiedFields.get(fieldNames.get(key))[0]) instanceof BigDecimal && (modifiedFields.get(fieldNames.get(key))[1]) instanceof BigDecimal) {
                        sb.append(fieldNames.get(key)).append(": £")
                                .append(modifiedFields.get(fieldNames.get(key))[1]).append(" ")
                                .append("(£").append(modifiedFields.get(fieldNames.get(key))[0]).append("). ");
                        continue;
                }
                sb.append(fieldNames.get(key)).append(": ")
                        .append(modifiedFields.get(fieldNames.get(key))[1]).append(" ")
                        .append("(").append(modifiedFields.get(fieldNames.get(key))[0]).append("). ");
                
            }
        }
        fieldNames.clear(); // clear the values in the map to avoid duplicate key being entered by another entity.
    }
    
    public boolean isPenaltyChargeDateModified() {
        if (DateHelper.removeTime(claim.getInvoice().getCreatedDate()).compareTo(DateHelper.removeTime(claim.getInvoice().getAutoPenaltyStart())) != 0) {
            return true;
        }

        return false;
    }

    // </editor-fold>
    @Override
    public String execute() {

        String tabName = getTabName();
        accessRight = applicationAccessibility.checkTabAccessibilityEditable(tabName,
                super.getAuthenticatedUser(), claim);
        String result = accessRight > 1 ? EDITABLE : READ_ONLY;
        LOG.debug("Returning accessibility={} for tab.status={}", result, tabName + '.' + claim.getStatus());
        updateModelInSession(Arrays.asList(engineerReport, vehicleHire, invoice, claim));
        return result;
    }

    @Override
    public void prepare() throws Exception {
        try {
            LOG.debug("Preparing... ");
            claim = this.claimService.getClaim(claimId);
            if (claim == null) {
                throw new Exception("An attempt to retrieve claim by id failed due to invalid id provided.");
            }
            engineerReport = (claim.getEngineerReport() != null) ? claim.getEngineerReport() : new EngineerReport();
            invoice = (claim.getInvoice() != null) ? claim.getInvoice() : new Invoice();
            invoiceOriginal = (invoice.getInvoiceOriginal() != null) ? invoice.getInvoiceOriginal() : new InvoiceOriginal();
            vehicleHire = (claim.getVehicleHire() != null) ? claim.getVehicleHire() : new VehicleHire();
            /* 
             * If the Invoice is submitted then force load all the lazy loaded collection entities into the memory 
             * and get a deep copy of the entity before struts apply the changes to the entity.
             * This is neede for adding note about the changes made to the form. 
             */
            if (actionSelected == submit) {
                forceLoadClaimsProxyObject();
                originalEngineerReport = (EngineerReport) SerializationUtils.clone(engineerReport);
                originalInvoice = (Invoice) SerializationUtils.clone(invoice);
                originalVehicleHire = (VehicleHire) SerializationUtils.clone(vehicleHire);
            }
            
            oldVRN = (vehicleHire.getVehicleRegistration() != null) ? vehicleHire.getVehicleRegistration() : "";

            addModelToSession(Arrays.asList(claim, engineerReport, vehicleHire, invoice));

        } catch (Exception ex) {
            LOG.error("Exception in preparing for InvoiceDetailAction : ", ex);
        }
    }

    /*  
     * This method used to force load the lazyLoaded entities from the claim. 
     * hibernate proxy objects need to be force loaded into memory when these objects are accessed by java reflection. 
     */
    private void forceLoadClaimsProxyObject() {
        invoice = (Invoice) forceLoadProxyObject(invoice);
        vehicleHire = (VehicleHire) forceLoadProxyObject(vehicleHire);
        engineerReport = (EngineerReport) forceLoadProxyObject(engineerReport);
        forceLoadProxyObject(invoice.getInvoiceOriginal());
        forceLoadProxyObject(vehicleHire.getVehicleClass());
    }

    private Object forceLoadProxyObject(Object model) {
        if (model instanceof HibernateProxy) {
            return ((HibernateProxy) model).getHibernateLazyInitializer().getImplementation();
        }
        return model;
    }
    
    // <editor-fold defaultstate="collapsed" desc="SERVICES">
    public List<VehicleClassPriceMapper> getAllVehicleClassPriceMapper() {
        List<VehicleClassPriceMapper> vehicleClassPriceMapper = new ArrayList<VehicleClassPriceMapper>();
        Iterator itr = vehicleClassService.getAllVehicleClass().iterator();
        LOG.debug("total number of iterator {}:", vehicleClassService.getAllVehicleClass().size());
        Date firstRegistration = claim.getCustomer().getHpiFirstRegistration();
        Date hireStart;
        if (claim.getVehicleHire() == null) {
            LOG.warn("No vehicle hire for claim: {}", claim.getChoReference());
            hireStart = new Date();
        } else {
            hireStart = claim.getVehicleHire().getHireStart();
        }
        BigDecimal age = BigDecimal.ZERO;

        if (hireStart != null & firstRegistration != null) {
            age = new BigDecimal(DateHelper.differenceInYears(hireStart, firstRegistration));
        }

        while (itr.hasNext()) {
            vehicleClass = (VehicleClass) itr.next();
            BigDecimal price;
            try {
                price = vehicleClassPriceService.getPrice(claim.getClaimType(), vehicleClass, getHireStart(), age, claim.getInsurer().getId(), claim.getChorganisation().getId());
                vehicleClassPriceMapper.add(new VehicleClassPriceMapper(vehicleClass.getName(), price));
            } catch (Exception e) {
                LOG.debug("VehicleClassPriceMapper: No price found for vehicle class {} with age {} at hire-start '{}'", new Object[]{vehicleClass.getName(), age, getHireStart()});
            }
        }
        LOG.debug("Total size in vehicleclasspricemaper list is {}:", vehicleClassPriceMapper.size());
        Collections.sort(vehicleClassPriceMapper, new VehicleClassPriceMapperComparator());
        return vehicleClassPriceMapper;
    }

    public VehicleClassPriceService getVehicleClassPriceService() {
        return vehicleClassPriceService;
    }

    public void setVehicleClassPriceService(VehicleClassPriceService vehicleClassPriceService) {
        this.vehicleClassPriceService = vehicleClassPriceService;
    }

    public VehicleClassService getVehicleClassService() {
        return vehicleClassService;
    }

    public void setVehicleClassService(VehicleClassService vehicleClassService) {
        this.vehicleClassService = vehicleClassService;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="validation">

    @Override
    public void validate() {
        if (claim != null) {
            LOG.debug("inside attachment action validate method, claim is present and validation started");
            if ((getIsInsurer() && claim.getInsurer().getId().intValue() != getAuthenticatedUser().getInsurer().getId().intValue())
                    || (getIsCHO() && claim.getChorganisation().getId().intValue() != getAuthenticatedUser().getChorganisation().getId().intValue())) {
                LOG.error("InvoiceDetailAction validation failed, Attempt to access a claim that you do not own.");
                throw new AccessDeniedException("Attempt to access a claim that you do not own.");
            }
        } else {
            LOG.debug("inside attachment action validate method, claim is null no validation done");
        }

    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Re-Calculation">
//    @Secured({"ROLE_CHOX_ADMIN", "ROLE_CHO",""})

    public void recalculate(Claim claim) throws Exception {

        BigDecimal tpiInsurancePremiumFee = BigDecimal.ZERO;
        BigDecimal tpiInsurancePremiumVat = BigDecimal.ZERO;
        BigDecimal totalExtras = BigDecimal.ZERO;
        BigDecimal hireNet = BigDecimal.ZERO;
        BigDecimal hireVat = BigDecimal.ZERO;
        BigDecimal hireGross = BigDecimal.ZERO;
        BigDecimal totalNet = BigDecimal.ZERO;
        BigDecimal totalVat = BigDecimal.ZERO;
        BigDecimal totalGross = BigDecimal.ZERO;
        BigDecimal repairVat = BigDecimal.ZERO;
        BigDecimal repairGross = BigDecimal.ZERO;
        BigDecimal engineerVat = BigDecimal.ZERO;
        BigDecimal engineerGross = BigDecimal.ZERO;
        BigDecimal storageRecoveryVat = BigDecimal.ZERO;
        BigDecimal storageRecoveryGross = BigDecimal.ZERO;
        BigDecimal totalLossVat = BigDecimal.ZERO;
        BigDecimal totalLossGross = BigDecimal.ZERO;
        BigDecimal fullTotalRequested = BigDecimal.ZERO;

        LOG.debug("initial value setup done in recalculate() function");

        totalExtras = totalExtras.add(getMiscellaneousFee());

        totalExtras = totalExtras.add(getAutomaticFee());
        totalExtras = totalExtras.add(getAdditionalDriverFee());
        totalExtras = totalExtras.add(getSatNavFee());
        totalExtras = totalExtras.add(getEstateFee());
        totalExtras = totalExtras.add(getBabySeatFee());
        totalExtras = totalExtras.add(getTowBarsFee());
        if (ClaimType.isCollaborationProtocol(claim.getClaimType())) {
            totalExtras = totalExtras.add(getCollaborationFee());
        }
        if (!ClaimType.isTPI(claim.getClaimType())) {
            totalExtras = totalExtras.add(getNonStandardInsurancePremiumFee());
        } else {
            tpiInsurancePremiumFee = tpiInsurancePremiumFee.add(getNonStandardInsurancePremiumFee());
            tpiInsurancePremiumVat = tpiInsurancePremiumVat.add(tpiInsurancePremiumFee);
            tpiInsurancePremiumVatUsed = CalcHelper.getInsurancePremiumVatRate(claim.getInvoice().getDateInvoiced());
            tpiInsurancePremiumVat = tpiInsurancePremiumVat.multiply(tpiInsurancePremiumVatUsed);
        }
        totalExtras = totalExtras.add(getRoofRackFee());
        totalExtras = totalExtras.add(getAdminFee());
        totalExtras = totalExtras.add(getDualControlFee());
        totalExtras = totalExtras.add(getDeliveryCollectionFee());
        LOG.debug("total extras {}", totalExtras);

        if (getPreviousHireNet() != null && getPreviousHireVat() != null && !(getPreviousHireNet().doubleValue() == 0)) {
            if (ClaimType.isTPI(claim.getClaimType()) && getPreviousNonStandardInsurancePremiumFee().compareTo(BigDecimal.ZERO) >= 1) {
                setHire_vat_used(((getPreviousHireVat().subtract(getPreviousNonStandardInsurancePremiumFee().multiply(tpiInsurancePremiumVatUsed))).divide((getPreviousHireNet().subtract(getPreviousNonStandardInsurancePremiumFee())), 4, BigDecimal.ROUND_HALF_UP)));
            } else {
                setHire_vat_used((getPreviousHireVat().divide(getPreviousHireNet(), 4, BigDecimal.ROUND_HALF_UP)));
            }
            LOG.debug(" Hire_vat_used value{} ", getHire_vat_used());

        } else {
            LOG.debug(" Used Hire Vat value is Null and default VAT_RATE is used for vat calculation {} ", Vat_Rate);
            hire_vat_used = Vat_Rate;
        }

        hireNet = hireNet.add(new BigDecimal(getDays()));

        hireNet = hireNet.multiply(getHireRateChargedPerDay());
        hireNet = hireNet.add(totalExtras);

        setHireNet(hireNet.setScale(2, RoundingMode.HALF_UP));

        hireVat = hireVat.add(hireNet);

        hireVat = hireVat.multiply(hire_vat_used);

        setHireVat(hireVat.setScale(2, RoundingMode.HALF_UP));

        if (ClaimType.isTPI(claim.getClaimType())) {

            hireNet = hireNet.add(tpiInsurancePremiumFee);
            setHireNet(hireNet.setScale(2, RoundingMode.HALF_UP));
            hireVat = hireVat.add(tpiInsurancePremiumVat);
            setHireVat(hireVat.setScale(2, RoundingMode.HALF_UP));
        }
        hireGross = hireGross.add(hireVat);
        hireGross = hireGross.add(hireNet);
        setHireGross(hireGross.setScale(2, RoundingMode.HALF_UP));

        if (getPreviousRepairNet() != null && getPreviousRepairVat() != null && !(getPreviousRepairNet().doubleValue() == 0)) {
            setRepair_vat_used(getPreviousRepairVat().divide(getPreviousRepairNet(), 4, BigDecimal.ROUND_HALF_UP));//.setScale(3);
            LOG.debug(" Repair_vat_used value{} ", getRepair_vat_used());

        } else {

            repair_vat_used = Vat_Rate;
        }

        repairVat = repairVat.add(getRepairNet());
        repairVat = repairVat.multiply(repair_vat_used);

        setRepairVat(repairVat.setScale(2, RoundingMode.HALF_UP));

        repairGross = repairGross.add(getRepairVat());
        repairGross = repairGross.add(getRepairNet());

        setRepairGross(repairGross.setScale(2, RoundingMode.HALF_UP));

        if (getPreviousEngineerFeeNet() != null && getPreviousEngineerFeeVat() != null && !(getPreviousEngineerFeeNet().doubleValue() == 0)) {
            setEngineerFee_vat_used(getPreviousEngineerFeeVat().divide(getPreviousEngineerFeeNet(), 4, BigDecimal.ROUND_HALF_UP));//.setScale(3);
            LOG.debug(" EngineerFee_vat_used value{} ", getEngineerFee_vat_used());

        } else {
            engineerFee_vat_used = Vat_Rate;
        }

        engineerVat = engineerVat.add(getEngineerFeeNet());
        engineerVat = engineerVat.multiply(engineerFee_vat_used);

        setEngineerFeeVat(engineerVat.setScale(2, RoundingMode.HALF_UP));

        engineerGross = engineerGross.add(getEngineerFeeVat());
        engineerGross = engineerGross.add(getEngineerFeeNet());

        setEngineerFeeGross(engineerGross.setScale(2, RoundingMode.HALF_UP));

        if (getPreviousTotalLossNet() != null && getPreviousTotalLossVat() != null && !(getPreviousTotalLossNet().doubleValue() == 0)) {
            setTotalLossFee_vat_used(getPreviousTotalLossVat().divide(getPreviousTotalLossNet(), 4, BigDecimal.ROUND_HALF_UP));//.setScale(3);
            LOG.debug(" TotalLossFee_vat_used value{} ", getTotalLossFee_vat_used());

        } else {
            totalLossFee_vat_used = Vat_Rate;
        }

        totalLossVat = totalLossVat.add(totalLossFee_vat_used);
        totalLossVat = totalLossVat.multiply(getTotalLossFeeNet());

        setTotalLossFeeVat(totalLossVat.setScale(2, RoundingMode.HALF_UP));

        totalLossGross = totalLossGross.add(getTotalLossFeeNet());
        totalLossGross = totalLossGross.add(getTotalLossFeeVat());

        setTotalLossFeeGross(totalLossGross.setScale(2, RoundingMode.HALF_UP));

        if (getPreviousStorageNet() != null && getPreviousStorageVat() != null && !(getPreviousStorageNet().doubleValue() == 0)) {
            setStorageRecovery_vat_used(getPreviousStorageVat().divide(getPreviousStorageNet(), 4, BigDecimal.ROUND_HALF_UP));//.setScale(3);
            LOG.debug(" StorageRecovery_vat_used value{} ", getStorageRecovery_vat_used());

        } else {
            storageRecovery_vat_used = Vat_Rate;
        }

        storageRecoveryVat = storageRecoveryVat.add(storageRecovery_vat_used);
        storageRecoveryVat = storageRecoveryVat.multiply(getStorageRecoveryNet());

        setStorageRecoveryVat(storageRecoveryVat.setScale(2, RoundingMode.HALF_UP));

        storageRecoveryGross = storageRecoveryGross.add(getStorageRecoveryVat());
        storageRecoveryGross = storageRecoveryGross.add(getStorageRecoveryNet());

        setStorageRecoveryGross(storageRecoveryGross.setScale(2, RoundingMode.HALF_UP));

        totalNet = totalNet.add(hireNet);
        totalNet = totalNet.add(getRepairNet());
        totalNet = totalNet.add(getEngineerFeeNet());
        totalNet = totalNet.add(getTotalLossFeeNet());
        totalNet = totalNet.add(getStorageRecoveryNet());

        setTotalNet(totalNet.setScale(2, RoundingMode.HALF_UP));
        LOG.debug(" totalNet value{} ", totalNet.setScale(2, RoundingMode.HALF_UP));

        totalVat = totalVat.add(hireVat);
        totalVat = totalVat.add(repairVat);
        totalVat = totalVat.add(engineerVat);
        totalVat = totalVat.add(totalLossVat);
        totalVat = totalVat.add(storageRecoveryVat);

        setTotalVat(totalVat.setScale(2, RoundingMode.HALF_UP));

        totalGross = totalGross.add(hireGross);
        totalGross = totalGross.add(repairGross);
        totalGross = totalGross.add(engineerGross);
        totalGross = totalGross.add(totalLossGross);
        totalGross = totalGross.add(storageRecoveryGross);

        setTotalGross(totalGross.setScale(2, RoundingMode.HALF_UP));

        insurerDiscountService.applyInsurerDiscounts(claim, null, false);
        fullTotalRequested = fullTotalRequested.add(totalGross);
        fullTotalRequested = fullTotalRequested.add(getClaimsHandlingInvoiceAmount());
        fullTotalRequested = fullTotalRequested.add(getDeductionForClaimsHandlingFee());
        fullTotalRequested = fullTotalRequested.add(getDiscount());
        fullTotalRequested = fullTotalRequested.add(getTotalPenaltyCharge());
        fullTotalRequested = fullTotalRequested.add(getInsurerDiscount());

        setFullTotalToPay(fullTotalRequested.setScale(2, RoundingMode.HALF_UP));
        LOG.debug(" fullTotalRequested value{} ", fullTotalRequested);

        claimService.updateLiabilityPayment(claim);

    }
}
