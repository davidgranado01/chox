package idas.chox.web.actions;

/**
 *
 * @author seeni
 */
import idas.chox.service.bre.util.CalcHelper;
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
import idas.chox.core.services.VehicleClassPriceService;
import idas.chox.core.services.VehicleClassService;
import idas.chox.core.util.DateHelper;
import idas.chox.web.VehicleClassPriceMapperComparator;
import idas.chox.web.VehicleClassPriceMapper;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvoiceRecalculationAction extends BaseAction implements Preparable {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceRecalculationAction.class);
    private InvoiceAction invoiceAction = new InvoiceAction();
    private InvoiceOriginalAction invoiceOriginalAction = new InvoiceOriginalAction();
    private VehicleHireAction vehicleHireAction = new VehicleHireAction();
    private EngineerReportAction engineerReportAction = new EngineerReportAction();
    private int claimId = 0;
    private ClaimService claimService;
    private LookupService lookupService;
    private String actionResult;
    private ApplicationAccessibility applicationAccessibility;
    private Claim claim = new Claim();;
    private Map session;
    private int actionSelected;
    private int submit = 10;
    private int recalculate = 20;
    private int reset = 30;
    public static final String READ_ONLY = "r";
    public static final String EDITABLE = "w";
    public static final String DECLINE = "decline";
    private VehicleClassService vehicleClassService;
    private VehicleClassPriceService vehicleClassPriceService;
    private VehicleClass vehicleClass;
    private BigDecimal Vat_Rate = CalcHelper.VAT_RATE;
    
    private BigDecimal hire_vat_used;
    private BigDecimal repair_vat_used;
    private BigDecimal engineerFee_vat_used;
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

    private boolean tpiClaimChk;

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


    // <editor-fold defaultstate="collapsed" desc="Getter and Setter">

    public BigDecimal getEngineerFee_vat_used() {
        return engineerFee_vat_used.multiply(new BigDecimal(100));
    }

    public void setEngineerFee_vat_used(BigDecimal engineerFee_vat_used) {
        this.engineerFee_vat_used = engineerFee_vat_used;
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


    private int formChanged=-1;
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
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="InvoiceOriginal">

    public java.util.Date getDateInvoiced_original() {

        return invoiceOriginalAction.model.getDateInvoiced_original();
    }

    public void setDateInvoiced_original(java.util.Date dateInvoiced) {
        if ((getDateInvoiced_original() == null) && (dateInvoiced != getDateInvoiced_original())) {
            invoiceOriginalAction.model.setDateInvoiced_original(dateInvoiced);
        }
    }

    public java.math.BigDecimal getHireNet_original() {


        LOG.debug("getHireNet_original is being called");

        return invoiceOriginalAction.model.getHireNet_original();

    }

    public void setHireNet_original(java.math.BigDecimal hireNet) {

        LOG.debug("setHireNet_original is being called");

        if ((getHireNet_original() == null) && (hireNet != getHireNet_original())) {
            invoiceOriginalAction.model.setHireNet_original(hireNet);
        }
    }

    public java.math.BigDecimal getHireVat_original() {

        return invoiceOriginalAction.model.getHireVat_original();
    }

    public void setHireVat_original(java.math.BigDecimal hireVat) {

        if (hireVat != getHireVat_original() && (getHireVat_original() == null)) {
            invoiceOriginalAction.model.setHireVat_original(hireVat);
        }
    }

    public java.math.BigDecimal getHireGross_original() {

        return invoiceOriginalAction.model.getHireGross_original();
    }

    public void setHireGross_original(java.math.BigDecimal hireGross) {

        if (hireGross != getHireGross_original() && (getHireGross_original() == null)) {
            invoiceOriginalAction.model.setHireGross_original(hireGross);
        }
    }

    public java.math.BigDecimal getRepairNet_original() {

        return invoiceOriginalAction.model.getRepairNet_original();
    }

    public void setRepairNet_original(java.math.BigDecimal repairNet) {

        if (repairNet != getRepairNet_original() && (getRepairNet_original() == null)) {
            invoiceOriginalAction.model.setRepairNet_original(repairNet);
        }
    }

    public java.math.BigDecimal getRepairVat_original() {
        return invoiceOriginalAction.model.getRepairVat_original();
    }

    public void setRepairVat_original(java.math.BigDecimal repairVat) {
        if (repairVat != getRepairVat_original() && (getRepairVat_original() == null)) {
            invoiceOriginalAction.model.setRepairVat_original(repairVat);
        }
    }

    public java.math.BigDecimal getRepairGross_original() {
        return invoiceOriginalAction.model.getRepairGross_original();
    }

    public void setRepairGross_original(java.math.BigDecimal repairGross) {
        if (repairGross != getRepairGross_original() && (getRepairGross_original() == null)) {
            invoiceOriginalAction.model.setRepairGross_original(repairGross);
        }
    }

    public java.math.BigDecimal getEngineerFeeNet_original() {
        return invoiceOriginalAction.model.getEngineerFeeNet_original();
    }

    public void setEngineerFeeNet_original(java.math.BigDecimal engineerFeeNet) {
        if (engineerFeeNet != getEngineerFeeNet_original() && (getEngineerFeeNet_original() == null)) {
            invoiceOriginalAction.model.setEngineerFeeNet_original(engineerFeeNet);
        }
    }

    public java.math.BigDecimal getEngineerFeeVat_original() {
        return invoiceOriginalAction.model.getEngineerFeeVat_original();
    }

    public void setEngineerFeeVat_original(java.math.BigDecimal engineerFeeVat) {
        if (engineerFeeVat != getEngineerFeeVat_original() && (getEngineerFeeVat_original() == null)) {
            invoiceOriginalAction.model.setEngineerFeeVat_original(engineerFeeVat);
        }
    }

    public java.math.BigDecimal getEngineerFeeGross_original() {
        return invoiceOriginalAction.model.getEngineerFeeGross_original();
    }

    public void setEngineerFeeGross_original(java.math.BigDecimal engineerFeeGross) {
        if (engineerFeeGross != getEngineerFeeGross_original() && (getEngineerFeeGross_original() == null)) {
            invoiceOriginalAction.model.setEngineerFeeGross_original(engineerFeeGross);
        }
    }

    public java.math.BigDecimal getStorageRecoveryNet_original() {
        return invoiceOriginalAction.model.getStorageRecoveryNet_original();
    }

    public void setStorageRecoveryNet_original(java.math.BigDecimal storageRecoveryNet) {
        if (storageRecoveryNet != getStorageRecoveryNet_original() && (getStorageRecoveryNet_original() == null)) {
            invoiceOriginalAction.model.setStorageRecoveryNet_original(storageRecoveryNet);
        }
    }

    public java.math.BigDecimal getStorageRecoveryVat_original() {
        return invoiceOriginalAction.model.getStorageRecoveryVat_original();
    }

    public void setStorageRecoveryVat_original(java.math.BigDecimal storageRecoveryVat) {
        if (storageRecoveryVat != getStorageRecoveryVat_original() && (getStorageRecoveryVat_original() == null)) {
            invoiceOriginalAction.model.setStorageRecoveryVat_original(storageRecoveryVat);
        }
    }

    public java.math.BigDecimal getStorageRecoveryGross_original() {
        return invoiceOriginalAction.model.getStorageRecoveryGross_original();
    }

    public void setStorageRecoveryGross_original(java.math.BigDecimal storageRecoveryGross) {
        if (storageRecoveryGross != getStorageRecoveryGross_original() && (getStorageRecoveryGross_original() == null)) {
            invoiceOriginalAction.model.setStorageRecoveryGross_original(storageRecoveryGross);
        }
    }

    public java.math.BigDecimal getTotalNet_original() {
        return invoiceOriginalAction.model.getTotalNet_original();
    }

    public void setTotalNet_original(java.math.BigDecimal totalNet) {
        if (totalNet != getTotalNet_original() && (getTotalNet_original() == null)) {
            invoiceOriginalAction.model.setTotalNet_original(totalNet);
        }
    }

    public java.math.BigDecimal getTotalVat_original() {
        return invoiceOriginalAction.model.getTotalVat_original();
    }

    public void setTotalVat_original(java.math.BigDecimal totalVat) {
        if (totalVat != getTotalVat_original() && (getTotalVat_original() == null)) {
            invoiceOriginalAction.model.setTotalVat_original(totalVat);
        }
    }

    public java.math.BigDecimal getTotalGross_original() {
        return invoiceOriginalAction.model.getTotalGross_original();
    }

    public void setTotalGross_original(java.math.BigDecimal totalGross) {
        if (totalGross != getTotalGross_original() && (getTotalGross_original() == null)) {
            invoiceOriginalAction.model.setTotalGross_original(totalGross);
        }
    }

    public java.math.BigDecimal getClaimsHandlingInvoiceAmount_original() {
        return invoiceOriginalAction.model.getClaimsHandlingInvoiceAmount_original();
    }

    public void setClaimsHandlingInvoiceAmount_original(java.math.BigDecimal claimsHandlingInvoiceAmount) {
        if (claimsHandlingInvoiceAmount != getClaimsHandlingInvoiceAmount_original() && (getClaimsHandlingInvoiceAmount_original() == null)) {
            invoiceOriginalAction.model.setClaimsHandlingInvoiceAmount_original(claimsHandlingInvoiceAmount);
        }
    }

    public java.math.BigDecimal getDeductionForClaimsHandlingFee_original() {
        return invoiceOriginalAction.model.getDeductionForClaimsHandlingFee_original();
    }

    public void setDeductionForClaimsHandlingFee_original(java.math.BigDecimal deductionForClaimsHandlingFee) {
        if (deductionForClaimsHandlingFee != getDeductionForClaimsHandlingFee_original() && (getDeductionForClaimsHandlingFee_original() == null)) {
            invoiceOriginalAction.model.setDeductionForClaimsHandlingFee_original(deductionForClaimsHandlingFee);
        }
    }

    public java.math.BigDecimal getDiscount_original() {
        return invoiceOriginalAction.model.getDiscount_original();
    }

    public void setDiscount_original(java.math.BigDecimal discount) {
        if (discount != getDiscount_original() && (getDiscount_original() == null)) {
            invoiceOriginalAction.model.setDiscount_original(discount);
        }
    }

    public java.math.BigDecimal getFullTotalToPay_original() {
        return invoiceOriginalAction.model.getFullTotalToPay_original();
    }

    public void setFullTotalToPay_original(java.math.BigDecimal totalToPay) {
        if (totalToPay != getFullTotalToPay_original() && (getFullTotalToPay_original() == null)) {
            invoiceOriginalAction.model.setFullTotalToPay_original(totalToPay);

        }
        if (invoiceAction.model.getTotalToPay() != getTotalToPay_original() && (getTotalToPay_original() == null)) {
            LOG.debug("setTotalToPay_original is set with the value of {}", invoiceAction.model.getTotalToPay());
            invoiceOriginalAction.model.setTotalToPay_original(invoiceAction.model.getTotalToPay());
        }

    }

    public java.math.BigDecimal getCdwFee_original() {
        return invoiceOriginalAction.model.getCdwFee_original();
    }

    public void setCdwFee_original(java.math.BigDecimal cdwFee) {
        if (cdwFee != getCdwFee_original() && (getCdwFee_original() == null)) {
            invoiceOriginalAction.model.setCdwFee_original(cdwFee);
        }
    }

    public java.math.BigDecimal getAutomaticFee_original() {
        return invoiceOriginalAction.model.getAutomaticFee_original();
    }

    public void setAutomaticFee_original(java.math.BigDecimal automaticFee) {
        if (automaticFee != getAutomaticFee_original() && (getAutomaticFee_original() == null)) {
            invoiceOriginalAction.model.setAutomaticFee_original(automaticFee);
        }

    }

    public Integer getEstateQty_original() {
        return invoiceOriginalAction.model.getEstateQty_original();
    }

    public void setEstateQty_original(Integer estateQty) {
        if (estateQty != getEstateQty_original() && (getEstateQty_original() == null)) {
            invoiceOriginalAction.model.setEstateQty_original(estateQty);
        }
    }

    public Integer getCdwQty_original() {
        return invoiceOriginalAction.model.getCdwQty_original();
    }

    public void setCdwQty_original(Integer cdwQty) {
        if (cdwQty != getCdwQty_original() && (getCdwQty_original() == null)) {
            invoiceOriginalAction.model.setCdwQty_original(cdwQty);
        }
    }

    public Integer getAutomaticQty_original() {
        return invoiceOriginalAction.model.getAutomaticQty_original();
    }

    public void setAutomaticQty_original(Integer automaticQty) {
        if (automaticQty != getAutomaticQty_original() && (getAutomaticQty_original() == null)) {
            invoiceOriginalAction.model.setAutomaticQty_original(automaticQty);
        }
    }

    public Integer getSatNavQty_original() {
        return invoiceOriginalAction.model.getSatNavQty_original();
    }

    public void setSatNavQty_original(Integer satNavQty) {
        if (satNavQty != getSatNavQty_original() && (getSatNavQty_original() == null)) {
            invoiceOriginalAction.model.setSatNavQty_original(satNavQty);
        }
    }

    public Integer getBabySeatQty_original() {
        return invoiceOriginalAction.model.getBabySeatQty_original();
    }

    public void setBabySeatQty_original(Integer babySeatQty) {
        if (babySeatQty != getBabySeatQty_original() && (getBabySeatQty_original() == null)) {
            invoiceOriginalAction.model.setBabySeatQty_original(babySeatQty);
        }
    }

    public Integer getTowBarsQty_original() {
        return invoiceOriginalAction.model.getTowBarsQty_original();
    }

    public void setTowBarsQty_original(Integer towBarsQty) {
        if (towBarsQty != getTowBarsQty_original() && (getTowBarsQty_original() == null)) {
            invoiceOriginalAction.model.setTowBarsQty_original(towBarsQty);
        }
    }

    public Integer getNonStandardInsurancePremiumQty_original() {
        return invoiceOriginalAction.model.getNonStandardInsurancePremiumQty_original();
    }

    public void setNonStandardInsurancePremiumQty_original(Integer nonStandardInsurancePremiumQty) {
        if (nonStandardInsurancePremiumQty != getNonStandardInsurancePremiumQty_original() && (getNonStandardInsurancePremiumQty_original() == null)) {
            invoiceOriginalAction.model.setNonStandardInsurancePremiumQty_original(nonStandardInsurancePremiumQty);
        }
    }

    public Integer getAdminQty_original() {
        return invoiceOriginalAction.model.getAdminQty_original();
    }

    public void setAdminQty_original(Integer adminQty) {
        if (adminQty != getAdminQty_original() && (getAdminQty_original() == null)) {
            invoiceOriginalAction.model.setAdminQty_original(adminQty);
        }
    }

    public Integer getRoofRackQty_original() {
        return invoiceOriginalAction.model.getRoofRackQty_original();
    }

    public void setRoofRackQty_original(Integer roofRackQty) {
        if (roofRackQty != getRoofRackQty_original() && (getRoofRackQty_original() == null)) {
            invoiceOriginalAction.model.setRoofRackQty_original(roofRackQty);
        }
    }

    public Integer getDualControlQty_original() {
        return invoiceOriginalAction.model.getDualControlQty_original();
    }

    public void setDualControlQty_original(Integer dualControlQty) {
        if (dualControlQty != getDualControlQty_original() && (getDualControlQty_original() == null)) {
            invoiceOriginalAction.model.setDualControlQty_original(dualControlQty);
        }
    }

    public Integer getDeliveryCollectionQty_original() {
        return invoiceOriginalAction.model.getDeliveryCollectionQty_original();
    }

    public void setDeliveryCollectionQty_original(Integer deliveryCollectionQty) {
        if (deliveryCollectionQty != getDeliveryCollectionQty_original() && (getDeliveryCollectionQty_original() == null)) {
            invoiceOriginalAction.model.setDeliveryCollectionQty_original(deliveryCollectionQty);
        }
    }

    public java.math.BigDecimal getSatNavFee_original() {
        return invoiceOriginalAction.model.getSatNavFee_original();
    }

    public void setSatNavFee_original(java.math.BigDecimal satNavFee) {
        if (satNavFee != getSatNavFee_original() && (getSatNavFee_original() == null)) {
            invoiceOriginalAction.model.setSatNavFee_original(satNavFee);
        }
    }

    public java.math.BigDecimal getEstateFee_original() {
        return invoiceOriginalAction.model.getEstateFee_original();
    }

    public void setEstateFee_original(java.math.BigDecimal estateFee) {
        if (estateFee != getEstateFee_original() && (getEstateFee_original() == null)) {
            invoiceOriginalAction.model.setEstateFee_original(estateFee);
        }
    }

    public java.math.BigDecimal getBabySeatFee_original() {
        return invoiceOriginalAction.model.getBabySeatFee_original();
    }

    public void setBabySeatFee_original(java.math.BigDecimal babySeatFee) {
        if (babySeatFee != getBabySeatFee_original() && (getBabySeatFee_original() == null)) {
            invoiceOriginalAction.model.setBabySeatFee_original(babySeatFee);
        }
    }

    public java.math.BigDecimal getTowBarsFee_original() {
        return invoiceOriginalAction.model.getTowBarsFee_original();
    }

    public void setTowBarsFee_original(java.math.BigDecimal towBarsFee) {
        if (towBarsFee != getTowBarsFee_original() && (getTowBarsFee_original() == null)) {
            invoiceOriginalAction.model.setTowBarsFee_original(towBarsFee);
        }
    }

    public java.math.BigDecimal getNonStandardInsurancePremiumFee_original() {
        return invoiceOriginalAction.model.getNonStandardInsurancePremiumFee_original();
    }

    public void setNonStandardInsurancePremiumFee_original(java.math.BigDecimal nonStandardInsurancePremiumFee) {
        if (nonStandardInsurancePremiumFee != getNonStandardInsurancePremiumFee_original() && (getNonStandardInsurancePremiumFee_original() == null)) {
            invoiceOriginalAction.model.setNonStandardInsurancePremiumFee_original(nonStandardInsurancePremiumFee);
        }
    }

    public java.math.BigDecimal getAdminFee_original() {
        return invoiceOriginalAction.model.getAdminFee_original();
    }

    public void setAdminFee_original(java.math.BigDecimal adminFee) {
        if (adminFee != getAdminFee_original() && (getAdminFee_original() == null)) {
            invoiceOriginalAction.model.setAdminFee_original(adminFee);
        }
    }

    public java.math.BigDecimal getRoofRackFee_original() {
        return invoiceOriginalAction.model.getRoofRackFee_original();
    }

    public void setRoofRackFee_original(java.math.BigDecimal roofRackFee) {
        if (roofRackFee != getRoofRackFee_original() && (getRoofRackFee_original() == null)) {
            invoiceOriginalAction.model.setRoofRackFee_original(roofRackFee);
        }
    }

    public java.math.BigDecimal getDualControlFee_original() {
        return invoiceOriginalAction.model.getDualControlFee_original();
    }

    public void setDualControlFee_original(java.math.BigDecimal dualControlFee) {
        if (dualControlFee != getDualControlFee_original() && (getDualControlFee_original() == null)) {
            invoiceOriginalAction.model.setDualControlFee_original(dualControlFee);
        }
    }

    public java.math.BigDecimal getDeliveryCollectionFee_original() {
        return invoiceOriginalAction.model.getDeliveryCollectionFee_original();
    }

    public void setDeliveryCollectionFee_original(java.math.BigDecimal deliveryCollectionFee) {
        if (deliveryCollectionFee != getDeliveryCollectionFee_original() && (getDeliveryCollectionFee_original() == null)) {
            invoiceOriginalAction.model.setDeliveryCollectionFee_original(deliveryCollectionFee);
        }
    }

    public BigDecimal getHireRateChargedPerDay_original() {
        return invoiceOriginalAction.model.getHireRateChargedPerDay_original();
    }

    public void setHireRateChargedPerDay_original(BigDecimal hireRateChargedPerDay) {
        if (hireRateChargedPerDay != getHireRateChargedPerDay_original() && (getHireRateChargedPerDay_original() == null)) {
            invoiceOriginalAction.model.setHireRateChargedPerDay_original(hireRateChargedPerDay);
        }
    }

    public BigDecimal getExcessAmountCollected_original() {
        return invoiceOriginalAction.model.getExcessAmountCollected_original();
    }

    public void setExcessAmountCollected_original(BigDecimal excessAmountCollected) {
        if (excessAmountCollected != getExcessAmountCollected_original() && (getExcessAmountCollected_original() == null)) {
            invoiceOriginalAction.model.setExcessAmountCollected_original(excessAmountCollected);
        }
    }

    public BigDecimal getVatAmountCollected_original() {
        return invoiceOriginalAction.model.getVatAmountCollected_original();
    }

    public void setVatAmountCollected_original(BigDecimal vatAmountCollected) {
        if (vatAmountCollected != getVatAmountCollected_original() && (getVatAmountCollected_original() == null)) {
            invoiceOriginalAction.model.setVatAmountCollected_original(vatAmountCollected);
        }
    }

    public BigDecimal getHirePenaltyCharge_original() {
        return invoiceOriginalAction.model.getHirePenaltyCharge_original();
    }

    public void setHirePenaltyCharge_original(BigDecimal hirePenaltyCharge) {
        LOG.debug("setHirePenaltyCharge_original() is called with the value of {}", hirePenaltyCharge);
        if (hirePenaltyCharge != getHirePenaltyCharge_original() && (getHirePenaltyCharge_original() == null)) {
            LOG.debug("setHirePenaltyCharge_original is set with the value of {}", hirePenaltyCharge);
            invoiceOriginalAction.model.setHirePenaltyCharge_original(hirePenaltyCharge);
        }
        LOG.debug("setHirePenaltyCharge_original is called but condition failed value did not setup");

    }

    public BigDecimal getRepairPenaltyCharge_original() {
        return invoiceOriginalAction.model.getRepairPenaltyCharge_original();
    }

    public void setRepairPenaltyCharge_original(BigDecimal repairPenaltyCharge) {
        if (repairPenaltyCharge != getRepairPenaltyCharge_original() && (getRepairPenaltyCharge_original() == null)) {
            invoiceOriginalAction.model.setRepairPenaltyCharge_original(repairPenaltyCharge);
        }
    }

    public Integer getPenaltyAlertQty_original() {
        return invoiceOriginalAction.model.getPenaltyAlertQty_original();
    }

    public void setPenaltyAlertQty_original(Integer penaltyAlertQty) {
        if (penaltyAlertQty != getPenaltyAlertQty_original() && (getPenaltyAlertQty_original() == null)) {
            invoiceOriginalAction.model.setPenaltyAlertQty_original(invoiceAction.model.getPenaltyAlertQty());
        }
    }

    public BigDecimal getOriginalFullTotalToPay_original() {
        return invoiceOriginalAction.model.getOriginalFullTotalToPay_original();
    }

    public void setOriginalFullTotalToPay_original(BigDecimal originalTotalToPay) {
        if (originalTotalToPay != getOriginalFullTotalToPay_original() && (getOriginalFullTotalToPay_original() == null)) {
            invoiceOriginalAction.model.setOriginalFullTotalToPay_original(originalTotalToPay);
        }
    }

    public BigDecimal getTotalToPay_original() {
        return invoiceOriginalAction.model.getTotalToPay_original();
    }

    public void setTotalToPay_original(BigDecimal totalToPaySplitLiability) {
        LOG.debug("setTotalToPay_original() is called with the value of {}", totalToPaySplitLiability);
        if (totalToPaySplitLiability != getTotalToPay_original() && (getTotalToPay_original() == null)) {
            LOG.debug("setTotalToPay_original is set with the value of {}", totalToPaySplitLiability);
            invoiceOriginalAction.model.setTotalToPay_original(totalToPaySplitLiability);
        }
        LOG.debug("setTotalToPay_original() is called but value is not set as condition failed");
    }

    public BigDecimal getOriginalTotalToPay_original() {
        return invoiceOriginalAction.model.getOriginalTotalToPay_original();
    }

    public void setOriginalTotalToPay_original(BigDecimal originalTotalToPay) {
        if (originalTotalToPay != getOriginalTotalToPay_original() && (getOriginalTotalToPay_original() == null)) {
            invoiceOriginalAction.model.setOriginalTotalToPay_original(originalTotalToPay);
        }
    }

    public BigDecimal getAdditionalDriverFee_original() {

        return invoiceOriginalAction.model.getAdditionalDriverFee_original();
    }

    public void setAdditionalDriverFee_original(BigDecimal additionalDriverFee) {
        if (additionalDriverFee != getAdditionalDriverFee_original() && (getAdditionalDriverFee_original() == null)) {
            invoiceOriginalAction.model.setAdditionalDriverFee_original(additionalDriverFee);
        }
    }

    public Integer getAdditionalDriverQty_original() {

        return invoiceOriginalAction.model.getAdditionalDriverQty_original();
    }

    public void setAdditionalDriverQty_original(Integer additionalDriverQty) {
        if (additionalDriverQty != getAdditionalDriverQty_original() && (getAdditionalDriverQty_original() == null)) {
            invoiceOriginalAction.model.setAdditionalDriverQty_original(additionalDriverQty);
        }
    }

    public BigDecimal getTotalLossFeeGross_original() {
        return invoiceOriginalAction.model.getTotalLossFeeGross_original();
    }

    public void setTotalLossFeeGross_original(BigDecimal totalLossFeeGross) {
        if (totalLossFeeGross != getTotalLossFeeGross_original() && (getTotalLossFeeGross_original() == null)) {
            invoiceOriginalAction.model.setTotalLossFeeGross_original(totalLossFeeGross);
        }
    }

    public BigDecimal getTotalLossFeeNet_original() {
        return invoiceOriginalAction.model.getTotalLossFeeNet_original();
    }

    public void setTotalLossFeeNet_original(BigDecimal totalLossFeeNet) {
        if (totalLossFeeNet != getTotalLossFeeNet_original() && (getTotalLossFeeNet_original() == null)) {
            invoiceOriginalAction.model.setTotalLossFeeNet_original(totalLossFeeNet);
        }
    }

    public BigDecimal getTotalLossFeeVat_original() {
        return invoiceOriginalAction.model.getTotalLossFeeVat_original();
    }

    public void setTotalLossFeeVat_original(BigDecimal totalLossFeeVat) {
        if (totalLossFeeVat != getTotalLossFeeVat_original() && (getTotalLossFeeVat_original() == null)) {
            invoiceOriginalAction.model.setTotalLossFeeVat_original(totalLossFeeVat);
        }
    }

    public String getHirePenaltyPercentage_original() {
        return invoiceOriginalAction.model.getHirePenaltyPercentage_original();
    }

    public void setHirePenaltyPercentage_original(String hirePenaltyPercentage) {
        if (!hirePenaltyPercentage.equals(getHirePenaltyPercentage_original()) && (getHirePenaltyPercentage_original() == null)) {
            invoiceOriginalAction.model.setHirePenaltyPercentage_original(hirePenaltyPercentage);
        }
    }

    public String getRepairPenaltyPercentage_original() {
        return invoiceOriginalAction.model.getRepairPenaltyPercentage_original();
    }

    public void setRepairPenaltyPercentage_original(String repairPenaltyPercentage) {
        if (!repairPenaltyPercentage.equals(getRepairPenaltyPercentage_original()) && (getRepairPenaltyPercentage_original() == null)) {
            invoiceOriginalAction.model.setRepairPenaltyPercentage_original(repairPenaltyPercentage);
        }
    }

    public BigDecimal getInterimPayment_original() {

        return invoiceOriginalAction.model.getInterimPayment_original();
    }

    public void setInterimPayment_original(BigDecimal interimPayment) {
        if (interimPayment != getInterimPayment_original() && (getInterimPayment_original() == null)) {
            invoiceOriginalAction.model.setInterimPayment_original(interimPayment);
        }
    }

    public BigDecimal getTotalPenaltyCharge_original() {
        return invoiceOriginalAction.model.getTotalPenaltyCharge_original();
    }

    public void setTotalPenaltyCharge_original(BigDecimal totalPenaltyCharge) {
        if (totalPenaltyCharge != getTotalPenaltyCharge_original() && (getTotalPenaltyCharge_original() == null)) {
            invoiceOriginalAction.model.setTotalPenaltyCharge_original(totalPenaltyCharge);
        }
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Invoice">

    public java.util.Date getInvoiceCreatedDate(){
        return invoiceAction.model.getCreatedDate();
    }
    public java.util.Date getDateInvoiced() {

        return invoiceAction.model.getDateInvoiced();
    }

    public void setDateInvoiced(java.util.Date dateInvoiced) {
        if (actionSelected != reset) {
            setDateInvoiced_original(getDateInvoiced());
            invoiceAction.model.setDateInvoiced(dateInvoiced);
        }
    }

    public java.math.BigDecimal getHireNet() {

        return invoiceAction.model.getHireNet();
    }

    public void setHireNet(java.math.BigDecimal hireNet) {

        if (actionSelected != reset) {

            setPreviousHireNet(invoiceAction.model.getHireNet());

            setHireNet_original(invoiceAction.model.getHireNet());
            invoiceAction.model.setHireNet(hireNet);

          
        }
    }

    public java.math.BigDecimal getHireVat() {

        return invoiceAction.model.getHireVat();
    }

    public void setHireVat(java.math.BigDecimal hireVat) {
        if (actionSelected != reset) {
            setPreviousHireVat(invoiceAction.model.getHireVat());
            setHireVat_original(invoiceAction.model.getHireVat());
            invoiceAction.model.setHireVat(hireVat);
        }
    }

    public java.math.BigDecimal getHireGross() {
        return invoiceAction.model.getHireGross();
    }

    public void setHireGross(java.math.BigDecimal hireGross) {
        if (actionSelected != reset) {
            setHireGross_original(invoiceAction.model.getHireGross());
            invoiceAction.model.setHireGross(hireGross);
        }
    }

    public java.math.BigDecimal getRepairNet() {
        return invoiceAction.model.getRepairNet();
    }

    public void setRepairNet(java.math.BigDecimal repairNet) {
        if (actionSelected != reset) {
            setPreviousRepairNet(invoiceAction.model.getRepairNet());
            setRepairNet_original(invoiceAction.model.getRepairNet());
            invoiceAction.model.setRepairNet(repairNet);
        }
    }

    public java.math.BigDecimal getRepairVat() {
        return invoiceAction.model.getRepairVat();
    }

    public void setRepairVat(java.math.BigDecimal repairVat) {
        if (actionSelected != reset) {
            setPreviousRepairVat(invoiceAction.model.getRepairVat());
            setRepairVat_original(invoiceAction.model.getRepairVat());
            invoiceAction.model.setRepairVat(repairVat);
        }
    }

    public java.math.BigDecimal getRepairGross() {
        return invoiceAction.model.getRepairGross();
    }

    public void setRepairGross(java.math.BigDecimal repairGross) {
        if (actionSelected != reset) {
            setRepairGross_original(invoiceAction.model.getRepairGross());
            invoiceAction.model.setRepairGross(repairGross);
        }
    }

    public java.math.BigDecimal getEngineerFeeNet() {
        return invoiceAction.model.getEngineerFeeNet();
    }

    public void setEngineerFeeNet(java.math.BigDecimal engineerFeeNet) {
        if (actionSelected != reset) {
            setPreviousEngineerFeeNet(invoiceAction.model.getEngineerFeeNet());
            setEngineerFeeNet_original(invoiceAction.model.getEngineerFeeNet());
            invoiceAction.model.setEngineerFeeNet(engineerFeeNet);
        }
    }

    public java.math.BigDecimal getEngineerFeeVat() {
        return invoiceAction.model.getEngineerFeeVat();
    }

    public void setEngineerFeeVat(java.math.BigDecimal engineerFeeVat) {
        if (actionSelected != reset) {
            setPreviousEngineerFeeVat(invoiceAction.model.getEngineerFeeVat());
            setEngineerFeeVat_original(invoiceAction.model.getEngineerFeeVat());
            invoiceAction.model.setEngineerFeeVat(engineerFeeVat);
        }
    }

    public java.math.BigDecimal getEngineerFeeGross() {
        return invoiceAction.model.getEngineerFeeGross();
    }

    public void setEngineerFeeGross(java.math.BigDecimal engineerFeeGross) {
        if (actionSelected != reset) {
            setEngineerFeeGross_original(invoiceAction.model.getEngineerFeeGross());
            invoiceAction.model.setEngineerFeeGross(engineerFeeGross);
        }
    }

    public java.math.BigDecimal getStorageRecoveryNet() {
        return invoiceAction.model.getStorageRecoveryNet();
    }

    public void setStorageRecoveryNet(java.math.BigDecimal storageRecoveryNet) {
        if (actionSelected != reset) {
            setPreviousStorageNet(invoiceAction.model.getStorageRecoveryNet());
            setStorageRecoveryNet_original(invoiceAction.model.getStorageRecoveryNet());
            invoiceAction.model.setStorageRecoveryNet(storageRecoveryNet);
        }
    }

    public java.math.BigDecimal getStorageRecoveryVat() {
        return invoiceAction.model.getStorageRecoveryVat();
    }

    public void setStorageRecoveryVat(java.math.BigDecimal storageRecoveryVat) {
        if (actionSelected != reset) {
            setPreviousStorageVat(invoiceAction.model.getStorageRecoveryVat());
            setStorageRecoveryVat_original(invoiceAction.model.getStorageRecoveryVat());
            invoiceAction.model.setStorageRecoveryVat(storageRecoveryVat);
        }
    }

    public java.math.BigDecimal getStorageRecoveryGross() {
        return invoiceAction.model.getStorageRecoveryGross();
    }

    public void setStorageRecoveryGross(java.math.BigDecimal storageRecoveryGross) {
        if (actionSelected != reset) {
            setStorageRecoveryGross_original(invoiceAction.model.getStorageRecoveryGross());
            invoiceAction.model.setStorageRecoveryGross(storageRecoveryGross);
        }
    }

    public java.math.BigDecimal getTotalNet() {
        return invoiceAction.model.getTotalNet();
    }

    public void setTotalNet(java.math.BigDecimal totalNet) {
        if (actionSelected != reset) {
            setTotalNet_original(invoiceAction.model.getTotalNet());
            invoiceAction.model.setTotalNet(totalNet);
        }
    }

    public java.math.BigDecimal getTotalVat() {
        return invoiceAction.model.getTotalVat();
    }

    public void setTotalVat(java.math.BigDecimal totalVat) {
        if (actionSelected != reset) {
            setTotalVat_original(invoiceAction.model.getTotalVat());
            invoiceAction.model.setTotalVat(totalVat);
        }
    }

    public java.math.BigDecimal getTotalGross() {
        return invoiceAction.model.getTotalGross();
    }

    public void setTotalGross(java.math.BigDecimal totalGross) {
        if (actionSelected != reset) {
            setTotalGross_original(invoiceAction.model.getTotalGross());
            invoiceAction.model.setTotalGross(totalGross);
        }
    }

    public java.math.BigDecimal getClaimsHandlingInvoiceAmount() {
        return invoiceAction.model.getClaimsHandlingInvoiceAmount();
    }

    public void setClaimsHandlingInvoiceAmount(java.math.BigDecimal claimsHandlingInvoiceAmount) {
        if (actionSelected != reset) {
            setClaimsHandlingInvoiceAmount_original(invoiceAction.model.getClaimsHandlingInvoiceAmount());
            invoiceAction.model.setClaimsHandlingInvoiceAmount(claimsHandlingInvoiceAmount);
        }
    }

    public java.math.BigDecimal getDeductionForClaimsHandlingFee() {
        return invoiceAction.model.getDeductionForClaimsHandlingFee();
    }

    public void setDeductionForClaimsHandlingFee(java.math.BigDecimal deductionForClaimsHandlingFee) {
        if (actionSelected != reset) {
            setDeductionForClaimsHandlingFee_original(invoiceAction.model.getDeductionForClaimsHandlingFee());
            invoiceAction.model.setDeductionForClaimsHandlingFee(deductionForClaimsHandlingFee);
        }
    }

    public java.math.BigDecimal getDiscount() {
        return invoiceAction.model.getDiscount();
    }

    public void setDiscount(java.math.BigDecimal discount) {
        if (actionSelected != reset) {
            setDiscount_original(invoiceAction.model.getDiscount());
            invoiceAction.model.setDiscount(discount);
        }
    }

    public java.math.BigDecimal getFullTotalToPay() {
        return invoiceAction.model.getFullTotalToPay();
    }

    public void setFullTotalToPay(java.math.BigDecimal totalToPay) {
        if (actionSelected != reset) {
            setFullTotalToPay_original(invoiceAction.model.getFullTotalToPay());
            invoiceAction.model.setFullTotalToPay(totalToPay);
        }
    }

    public java.lang.String getHandlingInvoiceNo() {
        return invoiceAction.model.getHandlingInvoiceNo();
    }

    public void setHandlingInvoiceNo(java.lang.String handlingInvoiceNo) {
        if (actionSelected != reset) {

            invoiceAction.model.setHandlingInvoiceNo(handlingInvoiceNo);
        }
    }

    public java.lang.String getClaimInvoiceNo() {
        return invoiceAction.model.getClaimInvoiceNo();
    }

    public void setClaimInvoiceNo(java.lang.String claimInvoiceNo) {
        if (actionSelected != reset) {

            invoiceAction.model.setClaimInvoiceNo(claimInvoiceNo);
        }
    }

    public java.math.BigDecimal getCdwFee() {
        return invoiceAction.model.getCdwFee();
    }

    public void setCdwFee(java.math.BigDecimal cdwFee) {
        if (actionSelected != reset) {
            setCdwFee_original(invoiceAction.model.getCdwFee());
            invoiceAction.model.setCdwFee(cdwFee);
        }
    }

    public java.math.BigDecimal getAutomaticFee() {
        return invoiceAction.model.getAutomaticFee();
    }

    public void setAutomaticFee(java.math.BigDecimal automaticFee) {
        if (actionSelected != reset) {
            setAutomaticFee_original(invoiceAction.model.getAutomaticFee());
            invoiceAction.model.setAutomaticFee(automaticFee);
        }
    }

    public Integer getEstateQty() {
        return invoiceAction.model.getEstateQty();
    }

    public void setEstateQty(Integer estateQty) {
        if (actionSelected != reset) {
            setEstateQty_original(invoiceAction.model.getEstateQty());
            invoiceAction.model.setEstateQty(estateQty);
        }
    }

    public Integer getCdwQty() {
        return invoiceAction.model.getCdwQty();
    }

    public void setCdwQty(Integer cdwQty) {
        if (actionSelected != reset) {
            setCdwQty_original(invoiceAction.model.getCdwQty());
            invoiceAction.model.setCdwQty(cdwQty);
        }
    }

    public Integer getAutomaticQty() {
        return invoiceAction.model.getAutomaticQty();
    }

    public void setAutomaticQty(Integer automaticQty) {
        if (actionSelected != reset) {
            setAutomaticQty_original(invoiceAction.model.getAutomaticQty());
            invoiceAction.model.setAutomaticQty(automaticQty);
        }
    }

    public Integer getSatNavQty() {
        return invoiceAction.model.getSatNavQty();
    }

    public void setSatNavQty(Integer satNavQty) {
        if (actionSelected != reset) {
            setSatNavQty_original(invoiceAction.model.getSatNavQty());
            invoiceAction.model.setSatNavQty(satNavQty);
        }
    }

    public Integer getBabySeatQty() {
        return invoiceAction.model.getBabySeatQty();
    }

    public void setBabySeatQty(Integer babySeatQty) {
        if (actionSelected != reset) {
            setBabySeatQty_original(invoiceAction.model.getBabySeatQty());
            invoiceAction.model.setBabySeatQty(babySeatQty);
        }
    }

    public Integer getTowBarsQty() {
        return invoiceAction.model.getTowBarsQty();
    }

    public void setTowBarsQty(Integer towBarsQty) {
        if (actionSelected != reset) {
            setTowBarsQty_original(invoiceAction.model.getTowBarsQty());
            invoiceAction.model.setTowBarsQty(towBarsQty);
        }
    }

    public Integer getNonStandardInsurancePremiumQty() {
        return invoiceAction.model.getNonStandardInsurancePremiumQty();
    }

    public void setNonStandardInsurancePremiumQty(Integer nonStandardInsurancePremiumQty) {
        if (actionSelected != reset) {
            setNonStandardInsurancePremiumQty_original(invoiceAction.model.getNonStandardInsurancePremiumQty());
            invoiceAction.model.setNonStandardInsurancePremiumQty(nonStandardInsurancePremiumQty);
        }
    }

    public Integer getAdminQty() {
        return invoiceAction.model.getAdminQty();
    }

    public void setAdminQty(Integer adminQty) {
        if (actionSelected != reset) {
            setAdminQty_original(invoiceAction.model.getAdminQty());
            invoiceAction.model.setAdminQty(adminQty);
        }
    }

    public Integer getRoofRackQty() {
        return invoiceAction.model.getRoofRackQty();
    }

    public void setRoofRackQty(Integer roofRackQty) {
        if (actionSelected != reset) {
            setRoofRackQty_original(invoiceAction.model.getRoofRackQty());
            invoiceAction.model.setRoofRackQty(roofRackQty);
        }
    }

    public Integer getDualControlQty() {
        return invoiceAction.model.getDualControlQty();
    }

    public void setDualControlQty(Integer dualControlQty) {
        if (actionSelected != reset) {
            setDualControlQty_original(invoiceAction.model.getDualControlQty());
            invoiceAction.model.setDualControlQty(dualControlQty);
        }
    }

    public Integer getDeliveryCollectionQty() {
        return invoiceAction.model.getDeliveryCollectionQty();
    }

    public void setDeliveryCollectionQty(Integer deliveryCollectionQty) {
        if (actionSelected != reset) {
            setDeliveryCollectionQty_original(invoiceAction.model.getDeliveryCollectionQty());
            invoiceAction.model.setDeliveryCollectionQty(deliveryCollectionQty);
        }
    }

    public java.math.BigDecimal getSatNavFee() {
        return invoiceAction.model.getSatNavFee();
    }

    public void setSatNavFee(java.math.BigDecimal satNavFee) {
        if (actionSelected != reset) {
            setSatNavFee_original(invoiceAction.model.getSatNavFee());
            invoiceAction.model.setSatNavFee(satNavFee);
        }
    }

    public java.math.BigDecimal getEstateFee() {
        return invoiceAction.model.getEstateFee();
    }

    public void setEstateFee(java.math.BigDecimal estateFee) {
        if (actionSelected != reset) {
            setEstateFee_original(invoiceAction.model.getEstateFee());
            invoiceAction.model.setEstateFee(estateFee);
        }
    }

    public java.math.BigDecimal getBabySeatFee() {
        return invoiceAction.model.getBabySeatFee();
    }

    public void setBabySeatFee(java.math.BigDecimal babySeatFee) {
        if (actionSelected != reset) {
            setBabySeatFee_original(invoiceAction.model.getBabySeatFee());
            invoiceAction.model.setBabySeatFee(babySeatFee);
        }
    }

    public java.math.BigDecimal getTowBarsFee() {
        return invoiceAction.model.getTowBarsFee();
    }

    public void setTowBarsFee(java.math.BigDecimal towBarsFee) {
        if (actionSelected != reset) {
            setTowBarsFee_original(invoiceAction.model.getTowBarsFee());
            invoiceAction.model.setTowBarsFee(towBarsFee);
        }
    }

    public java.math.BigDecimal getNonStandardInsurancePremiumFee() {
        return invoiceAction.model.getNonStandardInsurancePremiumFee();
    }

    public void setNonStandardInsurancePremiumFee(java.math.BigDecimal nonStandardInsurancePremiumFee) {
        if (actionSelected != reset) {
            setNonStandardInsurancePremiumFee_original(invoiceAction.model.getNonStandardInsurancePremiumFee());
            invoiceAction.model.setNonStandardInsurancePremiumFee(nonStandardInsurancePremiumFee);
        }
    }

    public java.math.BigDecimal getAdminFee() {
        return invoiceAction.model.getAdminFee();
    }

    public void setAdminFee(java.math.BigDecimal adminFee) {
        if (actionSelected != reset) {
            setAdminFee_original(invoiceAction.model.getAdminFee());
            invoiceAction.model.setAdminFee(adminFee);
        }
    }

    public java.math.BigDecimal getRoofRackFee() {
        return invoiceAction.model.getRoofRackFee();
    }

    public void setRoofRackFee(java.math.BigDecimal roofRackFee) {
        if (actionSelected != reset) {
            setRoofRackFee_original(invoiceAction.model.getRoofRackFee());
            invoiceAction.model.setRoofRackFee(roofRackFee);
        }
    }

    public java.math.BigDecimal getDualControlFee() {
        return invoiceAction.model.getDualControlFee();
    }

    public void setDualControlFee(java.math.BigDecimal dualControlFee) {
        if (actionSelected != reset) {
            setDualControlFee_original(invoiceAction.model.getDualControlFee());
            invoiceAction.model.setDualControlFee(dualControlFee);
        }
    }

    public java.math.BigDecimal getDeliveryCollectionFee() {
        return invoiceAction.model.getDeliveryCollectionFee();
    }

    public void setDeliveryCollectionFee(java.math.BigDecimal deliveryCollectionFee) {
        if (actionSelected != reset) {
            setDeliveryCollectionFee_original(invoiceAction.model.getDeliveryCollectionFee());
            invoiceAction.model.setDeliveryCollectionFee(deliveryCollectionFee);
        }
    }

    public String getEngineerInvoiceReviewNotes() {
        return invoiceAction.model.getEngineerInvoiceReviewNotes();
    }

    public void setEngineerInvoiceReviewNotes(String engineerInvoiceReviewNotes) {
        if (actionSelected != reset) {
            invoiceAction.model.setEngineerInvoiceReviewNotes(engineerInvoiceReviewNotes);
        }
    }

    public boolean isIsEngineerDecisionApproved() {
        return invoiceAction.model.isIsEngineerDecisionApproved();
    }

    public void setIsEngineerDecisionApproved(boolean isEngineerDecisionApproved) {
        if (actionSelected != reset) {
            invoiceAction.model.setIsEngineerDecisionApproved(isEngineerDecisionApproved);
        }
    }

    public boolean isIsPaymentMode() {
        return invoiceAction.model.isIsPaymentMode();
    }

    public void setIsPaymentMode(boolean isPaymentMode) {
        if (actionSelected != reset) {
            invoiceAction.model.setIsPaymentMode(isPaymentMode);
        }
    }

    public String getRejectionReason() {
        return invoiceAction.model.getRejectionReason();
    }

    public void setRejectionReason(String rejectionReason) {
        if (actionSelected != reset) {
            invoiceAction.model.setRejectionReason(rejectionReason);
        }
    }

    public BigDecimal getHireRateChargedPerDay() {
        return invoiceAction.model.getHireRateChargedPerDay();
    }

    public void setHireRateChargedPerDay(BigDecimal hireRateChargedPerDay) {
        if (actionSelected != reset) {
            setHireRateChargedPerDay_original(invoiceAction.model.getHireRateChargedPerDay());
            invoiceAction.model.setHireRateChargedPerDay(hireRateChargedPerDay);
        }
    }

    public BigDecimal getExcessAmountCollected() {
        return invoiceAction.model.getExcessAmountCollected();
    }

    public void setExcessAmountCollected(BigDecimal excessAmountCollected) {
        if (actionSelected != reset) {
            setExcessAmountCollected_original(invoiceAction.model.getExcessAmountCollected());
            invoiceAction.model.setExcessAmountCollected(excessAmountCollected);
        }
    }

    public BigDecimal getVatAmountCollected() {
        return invoiceAction.model.getVatAmountCollected();
    }

    public void setVatAmountCollected(BigDecimal vatAmountCollected) {
        if (actionSelected != reset) {
            setVatAmountCollected_original(invoiceAction.model.getVatAmountCollected());
            invoiceAction.model.setVatAmountCollected(vatAmountCollected);
        }
    }

    public BigDecimal getHirePenaltyCharge() {
        return invoiceAction.model.getHirePenaltyCharge();
    }

    public void setHirePenaltyCharge(BigDecimal hirePenaltyCharge) {
        if (actionSelected != reset) {
            setHirePenaltyCharge_original(invoiceAction.model.getHirePenaltyCharge());
            invoiceAction.model.setHirePenaltyCharge(hirePenaltyCharge);
        }

    }

    public BigDecimal getRepairPenaltyCharge() {
        return invoiceAction.model.getRepairPenaltyCharge();
    }

    public void setRepairPenaltyCharge(BigDecimal repairPenaltyCharge) {
        if (actionSelected != reset) {
            setRepairPenaltyCharge_original(invoiceAction.model.getRepairPenaltyCharge());
            invoiceAction.model.setRepairPenaltyCharge(repairPenaltyCharge);
        }
    }

    public Integer getPenaltyAlertQty() {
        return invoiceAction.model.getPenaltyAlertQty();
    }

    public void setPenaltyAlertQty(Integer penaltyAlertQty) {
        if (actionSelected != reset) {
            setPenaltyAlertQty_original(invoiceAction.model.getPenaltyAlertQty());
            invoiceAction.model.setPenaltyAlertQty(penaltyAlertQty);
        }
    }

    public long getInvoicedDays() {
        // long dateDiff = DateHelper.daysBetween(getDateInvoiced(), new Date()) + 1;
        return invoiceAction.model.getInvoicedDays();

    }

    public ReasonOfRejection getReasonOfRejection() {
        return invoiceAction.model.getReasonOfRejection();
    }

    public void setReasonOfRejection(ReasonOfRejection reasonOfRejection) {
        if (actionSelected != reset) {
            invoiceAction.model.setReasonOfRejection(reasonOfRejection);
        }
    }

    public Date getHirePenaltyChargeAppliedDate() {
        return invoiceAction.model.getHirePenaltyChargeAppliedDate();
    }

    public void setHirePenaltyChargeAppliedDate(Date hirePenaltyChargeAppliedDate) {
        if (actionSelected != reset) {
            invoiceAction.model.setHirePenaltyChargeAppliedDate(hirePenaltyChargeAppliedDate);
        }
    }

    public Date getRepairPenaltyChargeAppliedDate() {
        return invoiceAction.model.getRepairPenaltyChargeAppliedDate();
    }

    public void setRepairPenaltyChargeAppliedDate(Date repairPenaltyChargeAppliedDate) {
        if (actionSelected != reset) {
            invoiceAction.model.setRepairPenaltyChargeAppliedDate(repairPenaltyChargeAppliedDate);
        }
    }

    public BigDecimal getOriginalFullTotalToPay() {
        return invoiceAction.model.getOriginalFullTotalToPay();
    }

    public void setOriginalFullTotalToPay(BigDecimal originalTotalToPay) {
        if (actionSelected != reset) {
            setOriginalFullTotalToPay_original(invoiceAction.model.getOriginalFullTotalToPay());
            invoiceAction.model.setOriginalTotalToPay(originalTotalToPay);
        }
    }

    public BigDecimal getTotalToPay() {
        return invoiceAction.model.getTotalToPay();
    }

    public void setTotalToPay(BigDecimal totalToPaySplitLiability) {
        LOG.debug("setTotalToPay() is called with the value of {}", totalToPaySplitLiability);
        if (actionSelected != reset) {
            LOG.debug("setTotalToPay() is passed through the reset condition with the value of {}", totalToPaySplitLiability);
            setTotalToPay_original(invoiceAction.model.getTotalToPay());
            LOG.debug("setTotalToPay_original() from settotaltopay() is called with the value of {}", getTotalToPay());
            invoiceAction.model.setTotalToPay(totalToPaySplitLiability);
            LOG.debug("setTotalToPay set up done");
        }
    }

    public BigDecimal getOriginalTotalToPay() {
        return invoiceAction.model.getOriginalTotalToPay();
    }

    public void setOriginalTotalToPay(BigDecimal originalTotalToPay) {
        if (actionSelected != reset) {
            setOriginalTotalToPay_original(invoiceAction.model.getOriginalTotalToPay());
            invoiceAction.model.setOriginalTotalToPay(originalTotalToPay);
        }
    }

    public BigDecimal getAdditionalDriverFee() {

        return invoiceAction.model.getAdditionalDriverFee();
    }

    public void setAdditionalDriverFee(BigDecimal additionalDriverFee) {
        if (actionSelected != reset) {
            setAdditionalDriverFee_original(invoiceAction.model.getAdditionalDriverFee());
            invoiceAction.model.setAdditionalDriverFee(additionalDriverFee);
        }
    }

    public Integer getAdditionalDriverQty() {

        return invoiceAction.model.getAdditionalDriverQty();
    }

    public void setAdditionalDriverQty(Integer additionalDriverQty) {
        if (actionSelected != reset) {
            setAdditionalDriverQty_original(invoiceAction.model.getAdditionalDriverQty());
            invoiceAction.model.setAdditionalDriverQty(additionalDriverQty);
        }
    }

    public Boolean getCoverNoteRequired() {
        return invoiceAction.model.getCoverNoteRequired();
    }

    public void setCoverNoteRequired(Boolean coverNoteRequired) {

        if (actionSelected != reset) {
            invoiceAction.model.setCoverNoteRequired(coverNoteRequired);
        }
    }

    public String getCoverNoteRequiredDesc() {

        return invoiceAction.model.getCoverNoteRequiredDesc();

    }

    public BigDecimal getTotalLossFeeGross() {
        return invoiceAction.model.getTotalLossFeeGross();
    }

    public void setTotalLossFeeGross(BigDecimal totalLossFeeGross) {
        if (actionSelected != reset) {
            setTotalLossFeeGross_original(invoiceAction.model.getTotalLossFeeGross());
            invoiceAction.model.setTotalLossFeeGross(totalLossFeeGross);
        }
    }

    public BigDecimal getTotalLossFeeNet() {
        return invoiceAction.model.getTotalLossFeeNet();
    }

    public void setTotalLossFeeNet(BigDecimal totalLossFeeNet) {
        if (actionSelected != reset) {
            setPreviousTotalLossNet(invoiceAction.model.getTotalLossFeeNet());
            setTotalLossFeeNet_original(invoiceAction.model.getTotalLossFeeNet());
            invoiceAction.model.setTotalLossFeeNet(totalLossFeeNet);
        }
    }

    public BigDecimal getTotalLossFeeVat() {
        return invoiceAction.model.getTotalLossFeeVat();
    }

    public void setTotalLossFeeVat(BigDecimal totalLossFeeVat) {
        if (actionSelected != reset) {
            setPreviousTotalLossVat(invoiceAction.model.getTotalLossFeeVat());
            setTotalLossFeeVat_original(invoiceAction.model.getTotalLossFeeVat());
            invoiceAction.model.setTotalLossFeeVat(totalLossFeeVat);
        }
    }

    public String getHirePenaltyPercentage() {
        return invoiceAction.model.getHirePenaltyPercentage();
    }

    public void setHirePenaltyPercentage(String hirePenaltyPercentage) {
        if (actionSelected != reset) {
            setHirePenaltyPercentage_original(invoiceAction.model.getHirePenaltyPercentage());
            invoiceAction.model.setHirePenaltyPercentage(hirePenaltyPercentage);
        }
    }

    public String getRepairPenaltyPercentage() {
        return invoiceAction.model.getRepairPenaltyPercentage();
    }

    public void setRepairPenaltyPercentage(String repairPenaltyPercentage) {
        if (actionSelected != reset) {
            setRepairPenaltyPercentage_original(invoiceAction.model.getRepairPenaltyPercentage());
            invoiceAction.model.setRepairPenaltyPercentage(repairPenaltyPercentage);
        }
    }

    public BigDecimal getInterimPayment() {
         LOG.debug("getInterimPayment is being called inside InvoiceRecalculationAction and returning value is {}",invoiceAction.model.getInterimPayment());
        return invoiceAction.model.getInterimPayment();
    }

//    public void setInterimPayment(BigDecimal interimPayment) {
//        if (actionSelected != reset) {
//            LOG.debug("setInterimPayment is being called inside InvoiceRecalculation with the value of {}",interimPayment);
//            setInterimPayment_original(invoiceAction.model.getInterimPayment());
//            invoiceAction.model.setInterimPayment(interimPayment);
//        }
//    }

    public Boolean getInterimPaymentReceived() {
        LOG.debug("getInterimPaymentReceived is being called inside InvoiceRecalculationAction and returning value is {}",invoiceAction.model.getInterimPaymentReceived());
        return invoiceAction.model.getInterimPaymentReceived();
    }

//    public void setInterimPaymentReceived(Boolean interimPaymentReceived) {
//
//        if (actionSelected != reset) {
//            LOG.debug("setInterimPaymentReceived is being called inside InvoiceRecalculation with the value of {}",interimPaymentReceived);
//            invoiceAction.model.setInterimPaymentReceived(interimPaymentReceived);
//        }
//    }

    public String getInterimPaymentReceivedDesc() {
        return invoiceAction.model.getInterimPaymentReceivedDesc();
    }

    public void setInterimPaymentReceivedDesc(String interimPaymentReceivedDesc) {

        if (actionSelected != reset) {
            invoiceAction.model.setInterimPaymentReceivedDesc(interimPaymentReceivedDesc);
        }
    }

    public BigDecimal getTotalPenaltyCharge() {
        return invoiceAction.model.getTotalPenaltyCharge();
    }

    public void setTotalPenaltyCharge(BigDecimal totalPenaltyCharge) {
        if (actionSelected != reset) {
            setTotalPenaltyCharge_original(invoiceAction.model.getTotalPenaltyCharge());
            invoiceAction.model.setTotalPenaltyCharge(totalPenaltyCharge);
        }
    }
    public Boolean getInterimPaymentReceivedFullAndFinal() {
        return invoiceAction.model.getInterimPaymentReceivedFullAndFinal();
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
        if (actionSelected != reset) {
            vehicleHireAction.setVehicleClassId(vehicleClassId);
        }
    }

    public int getVehicleClassId() {

        return vehicleHireAction.getVehicleClassId();

    }

    public String getVehicleClassName_original() {


        return vehicleHireAction.getVehicleClassName_original();
    }

    public String getVehicleClassName() {

        return vehicleHireAction.getVehicleClassName();
    }

    public List<VehicleClass> getVehicleClasses() {
        return vehicleHireAction.getVehicleClasses();
    }

    public String getRentalStartTime() {
        return vehicleHireAction.getRentalStartTime();
    }

    public String getRentalStartTime_original() {
        return vehicleHireAction.getRentalStartTime_original();
    }

    public void setRentalStartTime(String time) {
        if (actionSelected != reset) {
            setRentalStartTime_original(getRentalStartTime());
            vehicleHireAction.setRentalStartTime(time);
        }
    }

    public void setRentalStartTime_original(String time) {
        if (!time.equals(getRentalStartTime_original()) && (getRentalStartTime_original() == null)) {

            vehicleHireAction.setRentalStartTime_original(time);

        }

    }

    public String getRentalEndTime() {
        return vehicleHireAction.getRentalEndTime();
    }

    public void setRentalEndTime(String time) {
        if (actionSelected != reset) {

            setRentalEndTime_original(getRentalEndTime());
            vehicleHireAction.setRentalEndTime(time);
        }

    }

    public String getRentalEndTime_original() {
        return vehicleHireAction.getRentalEndTime_original();
    }

    public void setRentalEndTime_original(String time) {
        if (!time.equals(getRentalEndTime_original()) && (getRentalEndTime_original() == null)) {
            vehicleHireAction.setRentalEndTime_original(time);
        }

    }

    public String getRentalStartTimeDisplayFormat() {
        String time = vehicleHireAction.getRentalStartTime_original();
        if (time.equals("24:00")) {
            return "00:00";
        } else {
            return time;
        }
    }

    public String getRentalEndTimeDisplayFormat() {
        String time = vehicleHireAction.getRentalEndTime_original();
        if (time.equals("24:00")) {
            return "00:00";
        } else {
            return time;
        }
    }

    public boolean getCanShowOriginalStartDate() {

        String d1 = DateHelper.LocalDateFormat.format(getRentalStart());
        String d2 = DateHelper.LocalDateFormat.format(getRentalStart_original());

        if (d1.equals(d2)) {
            return false;
        } else {
            return true;
        }
    }

    public boolean getCanShowOriginalEndDate() {

        String d1 = DateHelper.LocalDateFormat.format(getRentalEnd());
        String d2 = DateHelper.LocalDateFormat.format(getRentalEnd_original());

        if (d1.equals(d2)) {
            return false;
        } else {
            return true;
        }
    }

    public boolean getCanShowOriginalInvoicedDate() {

        String d1 = DateHelper.LocalDateFormat.format(getDateInvoiced());
        String d2 = DateHelper.LocalDateFormat.format(getDateInvoiced_original());

        if (d1.equals(d2)) {
            return false;
        } else {
            return true;
        }
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="VehicleHire">
    public void setIsTotalLoss(boolean IsTotalLoss) {
        if (actionSelected != reset) {
            vehicleHireAction.model.setIsTotalLoss(IsTotalLoss);
        }
    }

    public java.lang.String getVehicleRegistration() {
        return vehicleHireAction.model.getVehicleRegistration();
    }

    public void setVehicleRegistration(java.lang.String vehicleRegistration) {
        if (actionSelected != reset) {
            vehicleHireAction.model.setVehicleRegistration(vehicleRegistration);
        }
    }

    public java.lang.String getVehicleManufacturer() {
        return vehicleHireAction.model.getVehicleManufacturer();
    }

    public void setVehicleManufacturer(java.lang.String vehicleManufacturer) {
        if (actionSelected != reset) {
            vehicleHireAction.model.setVehicleManufacturer(vehicleManufacturer);
        }
    }

    public java.lang.String getVehicleModel() {
        return vehicleHireAction.model.getVehicleModel();
    }

    public void setVehicleModel(java.lang.String vehicleModel) {
        if (actionSelected != reset) {
            vehicleHireAction.model.setVehicleModel(vehicleModel);

        }
    }

    public java.util.Date getRentalStart() {
        return vehicleHireAction.model.getRentalStart();
    }

    public void setRentalStart(java.util.Date rentalStart) {
        if (actionSelected != reset) {
            setRentalStart_original(getRentalStart());
            vehicleHireAction.model.setRentalStart(rentalStart);
        }
    }

    public java.util.Date getRentalStart_original() {
        return vehicleHireAction.model.getRentalStart_original();
    }

    public void setRentalStart_original(java.util.Date rentalStart) {
        if (rentalStart != getRentalStart_original() && getRentalStart_original() == null) {
            vehicleHireAction.model.setRentalStart_original(rentalStart);
        }
    }

    public java.util.Date getRentalEnd() {
        return vehicleHireAction.model.getRentalEnd();
    }

    public void setRentalEnd(java.util.Date rentalEnd) {
        if (actionSelected != reset) {
            setRentalEnd_original(getRentalEnd());
            vehicleHireAction.model.setRentalEnd(rentalEnd);
        }
    }

    public java.util.Date getRentalEnd_original() {
        return vehicleHireAction.model.getRentalEnd_original();
    }

    public void setRentalEnd_original(java.util.Date rentalEnd) {
        if (rentalEnd != getRentalEnd_original() && getRentalEnd_original() == null) {
            vehicleHireAction.model.setRentalEnd_original(rentalEnd);
        }
    }

    public java.lang.String getCollectionReason() {
        return vehicleHireAction.model.getCollectionReason();
    }

    public void setCollectionReason(java.lang.String collectionReason) {
        if (actionSelected != reset) {
            vehicleHireAction.model.setCollectionReason(collectionReason);
        }
    }

    public Integer getDays_original() {
        return vehicleHireAction.model.getDays_original();
    }

    public void setDays_original(Integer days) {
        if (days != getDays_original() && (getDays_original() == null)) {
            vehicleHireAction.model.setDays_original(days);
        }
    }

    public Integer getDays() {
        return vehicleHireAction.model.getDays();
    }

    public void setDays(Integer days) {
        if (actionSelected != reset) {

            setDays_original(getDays());

            vehicleHireAction.model.setDays(days);

        }
    }

    public boolean isVHCdwFee() {
        return vehicleHireAction.model.isCdwFee();
    }

    public void setVHCdwFee(boolean cdwFee) {
        if (actionSelected != reset) {
            vehicleHireAction.model.setCdwFee(cdwFee);
        }
    }

    public boolean isVHAutomaticFee() {
        return vehicleHireAction.model.isAutomaticFee();
    }

    public void setVHAutomaticFee(boolean automaticFee) {
        if (actionSelected != reset) {
            vehicleHireAction.model.setAutomaticFee(automaticFee);
        }
    }

    public boolean isVHSatNavFee() {
        return vehicleHireAction.model.isSatNavFee();
    }

    public void setVHSatNavFee(boolean satNavFee) {
        if (actionSelected != reset) {
            vehicleHireAction.model.setSatNavFee(satNavFee);
        }
    }

    public boolean isVHEstateFee() {
        return vehicleHireAction.model.isEstateFee();
    }

    public void setVHEstateFee(boolean estateFee) {
        if (actionSelected != reset) {
            vehicleHireAction.model.setEstateFee(estateFee);
        }
    }

    public boolean isVHBabySeatFee() {
        return vehicleHireAction.model.isBabySeatFee();
    }

    public void setVHBabySeatFee(boolean babySeatFee) {
        if (actionSelected != reset) {
            vehicleHireAction.model.setBabySeatFee(babySeatFee);
        }
    }

    public boolean isVHTowBarsFee() {
        return vehicleHireAction.model.isTowBarsFee();
    }

    public void setVHTowBarsFee(boolean towBarsFee) {
        if (actionSelected != reset) {
            vehicleHireAction.model.setTowBarsFee(towBarsFee);
        }
    }

    public boolean isVHNonStandardInsurancePremiumFee() {
        return vehicleHireAction.model.isNonStandardInsurancePremiumFee();
    }

    public void setVHNonStandardInsurancePremiumFee(boolean nonStandardInsurancePremiumFee) {
        if (actionSelected != reset) {
            vehicleHireAction.model.setNonStandardInsurancePremiumFee(nonStandardInsurancePremiumFee);
        }
    }

    public boolean isVHAdminFee() {
        return vehicleHireAction.model.isAdminFee();
    }

    public void setVHAdminFee(boolean adminFee) {
        if (actionSelected != reset) {
            vehicleHireAction.model.setAdminFee(adminFee);
        }
    }

    public boolean isVHRoofRackFee() {
        return vehicleHireAction.model.isRoofRackFee();
    }

    public void setVHRoofRackFee(boolean roofRackFee) {
        if (actionSelected != reset) {
            vehicleHireAction.model.setRoofRackFee(roofRackFee);
        }
    }

    public boolean isVHDualControlFee() {
        return vehicleHireAction.model.isDualControlFee();
    }

    public void setVHDualControlFee(boolean dualControlFee) {
        if (actionSelected != reset) {
            vehicleHireAction.model.setDualControlFee(dualControlFee);
        }
    }

    public boolean isVHDeliveryCollectionFee() {
        return vehicleHireAction.model.isDeliveryCollectionFee();
    }

    public void setVHDeliveryCollectionFee(boolean deliveryCollectionFee) {
        if (actionSelected != reset) {
            vehicleHireAction.model.setDeliveryCollectionFee(deliveryCollectionFee);
        }
    }

    public VehicleClass getVehicleClass() {
        return vehicleHireAction.model.getVehicleClass();
    }

    public void setVehicleClass(VehicleClass vehicleClass) {
        if (actionSelected != reset) {
            vehicleHireAction.model.setVehicleClass(vehicleClass);
        }
    }

    public java.util.Date getHireStart() {
        return vehicleHireAction.model.getHireStart();
    }

    public void setHireStart(Date hireStart) {
        if (actionSelected != reset) {
            vehicleHireAction.model.setHireStart(hireStart);
        }
    }

    public java.util.Date getHireEnd() {
        return vehicleHireAction.model.getHireEnd();
    }

    public void setHireEnd(Date hireEnd) {
        if (actionSelected != reset) {
            vehicleHireAction.model.setHireEnd(hireEnd);
        }
    }

    // ##### NOT FROM HERE #############
    public boolean getIsTotalLoss() {
        return vehicleHireAction.model.getIsTotalLoss();
    }

    public String getHpiError() {
        return vehicleHireAction.model.getHpiError();
    }

    public void setHpiError(String hpiError) {
        if (actionSelected != reset) {
            vehicleHireAction.model.setHpiError(hpiError);
        }
    }

    public String getHpiVehicleCapacity() {
        return vehicleHireAction.model.getHpiVehicleCapacity();
    }

    public void setHpiVehicleCapacity(String hpiVehicleCapacity) {
        if (actionSelected != reset) {
            vehicleHireAction.model.setHpiVehicleCapacity(hpiVehicleCapacity);
        }
    }

    public String getHpiVehicleDoorplan() {
        return vehicleHireAction.model.getHpiVehicleDoorplan();
    }

    public void setHpiVehicleDoorplan(String hpiVehicleDoorplan) {
        if (actionSelected != reset) {
            vehicleHireAction.model.setHpiVehicleDoorplan(hpiVehicleDoorplan);
        }
    }

    public String getHpiVehicleManufacturer() {
        return vehicleHireAction.model.getHpiVehicleManufacturer();
    }

    public void setHpiVehicleManufacturer(String hpiVehicleManufacturer) {
        if (actionSelected != reset) {
            vehicleHireAction.model.setHpiVehicleManufacturer(hpiVehicleManufacturer);
        }
    }

    public String getHpiVehicleModel() {
        return vehicleHireAction.model.getHpiVehicleModel();
    }

    public void setHpiVehicleModel(String hpiVehicleModel) {
        if (actionSelected != reset) {
            vehicleHireAction.model.setHpiVehicleModel(hpiVehicleModel);
        }
    }

    public String getHpiVehicleTransmission() {
        return vehicleHireAction.model.getHpiVehicleTransmission();
    }

    public void setHpiVehicleTransmission(String hpiVehicleTransmission) {
        if (actionSelected != reset) {
            vehicleHireAction.model.setHpiVehicleTransmission(hpiVehicleTransmission);
        }
    }

    public String getHpiVehicleYear() {
        return vehicleHireAction.model.getHpiVehicleYear();
    }

    public void setHpiVehicleYear(String hpiVehicleYear) {
        if (actionSelected != reset) {
            vehicleHireAction.model.setHpiVehicleYear(hpiVehicleYear);
        }
    }

    public void setHpiFirstRegistration(Date firstRegistration) {
        if (actionSelected != reset) {
            vehicleHireAction.model.setHpiFirstRegistration(firstRegistration);
        }
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
        if (actionSelected != reset) {
            engineerReportAction.model.setDays(days);
        }
    }

    public java.lang.String getName() {
        return engineerReportAction.model.getName();
    }

    public void setName(java.lang.String name) {
        if (actionSelected != reset) {
            engineerReportAction.model.setName(name);
        }
    }

    public java.lang.String getCompany() {
        return engineerReportAction.model.getCompany();
    }

    public void setCompany(java.lang.String company) {
        if (actionSelected != reset) {
            engineerReportAction.model.setCompany(company);
        }
    }

    public java.lang.String getAddress1() {
        return engineerReportAction.model.getAddress1();
    }

    public void setAddress1(java.lang.String address1) {
        if (actionSelected != reset) {
            engineerReportAction.model.setAddress1(address1);
        }
    }

    public java.lang.String getAddress2() {
        return engineerReportAction.model.getAddress2();
    }

    public void setAddress2(java.lang.String address2) {
        if (actionSelected != reset) {
            engineerReportAction.model.setAddress2(address2);
        }
    }

    public java.lang.String getAddress3() {
        return engineerReportAction.model.getAddress3();
    }

    public void setAddress3(java.lang.String address3) {
        if (actionSelected != reset) {
            engineerReportAction.model.setAddress3(address3);
        }
    }

    public java.lang.String getAddress4() {
        return engineerReportAction.model.getAddress4();
    }

    public void setAddress4(java.lang.String address4) {
        if (actionSelected != reset) {
            engineerReportAction.model.setAddress4(address4);
        }
    }

    public java.lang.String getAddress5() {
        return engineerReportAction.model.getAddress5();
    }

    public void setAddress5(java.lang.String address5) {
        if (actionSelected != reset) {
            engineerReportAction.model.setAddress5(address5);
        }
    }

    public java.lang.String getPostcode() {
        return engineerReportAction.model.getPostcode();
    }

    public void setPostcode(java.lang.String postcode) {
        if (actionSelected != reset) {
            engineerReportAction.model.setPostcode(postcode);
        }
    }

    public java.lang.String getTelephone() {
        return engineerReportAction.model.getTelephone();
    }

    public void setTelephone(java.lang.String telephone) {
        if (actionSelected != reset) {
            engineerReportAction.model.setTelephone(telephone);
        }
    }

    public java.lang.String getEmail() {
        return engineerReportAction.model.getEmail();
    }

    public void setEmail(java.lang.String email) {
        if (actionSelected != reset) {
            engineerReportAction.model.setEmail(email);
        }
    }

    public Boolean isIsUsable() {
        return engineerReportAction.model.isIsUsable();
    }

    public void setIsUsable(Boolean isUsable) {
        if (actionSelected != reset) {
            engineerReportAction.model.setIsUsable(isUsable);
        }
    }

    public java.math.BigDecimal getLabourAmount() {
        return engineerReportAction.model.getLabourAmount();
    }

    public void setLabourAmount(java.math.BigDecimal labourAmount) {
        if (actionSelected != reset) {
            engineerReportAction.model.setLabourAmount(labourAmount);
        }
    }

    public java.math.BigDecimal getTotalAmount() {
        return engineerReportAction.model.getTotalAmount();
    }

    public void setTotalAmount(java.math.BigDecimal totalAmount) {
        if (actionSelected != reset) {
            engineerReportAction.model.setTotalAmount(totalAmount);
        }
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
    // <editor-fold defaultstate="collapsed" desc="updateModel">
    public String updateModel() {

        if (actionSelected == reset) {

            this.setActionResult("Invoice Reset");
            return SUCCESS;
        } else if (actionSelected == recalculate) {
            try {
                recalculate();
            } catch (Exception ex) {
                handleException(ex);
                LOG.debug("Exception is thrown and Error will be displayed in the page {} ", ex.getMessage());
                return ERROR;
            }
            return SUCCESS;
        } else if (actionSelected == submit) {

            if (updateInvoiceModel().equals(SUCCESS)) {
                LOG.debug("INVOICEACTION update is done ");
                if (updateVehicleHireModel().equals(SUCCESS)) {
                    LOG.debug("VEHICLEHIREACTION update is done ");
                    if (updateEngineerReportModel().equals(SUCCESS)) {
                        LOG.debug("ENGINEERREPORTACTION update is done ");
                        if (updateInvoiceOriginalModel().equals(SUCCESS)) {
                            LOG.debug(" INVOICEORIGINALMODEL is done ");

                            try {
                                invoiceAction.checkVersion(invoiceAction.getModel());
                                vehicleHireAction.checkVersion(vehicleHireAction.getModel());
                                engineerReportAction.checkVersion(engineerReportAction.getModel());
                                updateAllModel();
                                invoiceAction.prepare();
                                invoiceAction.updateSessionModel();
                                engineerReportAction.prepare();
                                engineerReportAction.updateSessionModel();
                                vehicleHireAction.prepare();
                                vehicleHireAction.updateSessionModel();
                                this.setActionResult("Your Changes Have Been Saved");
                                return SUCCESS;
                            } catch (Exception ex) {
                                LOG.debug("Exception is thrown and passing to baseAction {} ", ex.getMessage());
                                handleException(ex);
                                return ERROR;
                            }
                        }

                    }

                }

            }

            return ERROR;
        } else {
            return ERROR;
        }

    }

    public String updateInvoiceModel() {
        return invoiceAction.updateModel(claim);
    }

    public String updateEngineerReportModel() {
        return engineerReportAction.updateModel(claim);
    }

    public String updateVehicleHireModel() {
        return vehicleHireAction.updateModel(claim);
    }

    public String updateInvoiceOriginalModel() {
        return invoiceOriginalAction.updateModel(claim);
    }

    public void updateAllModel() throws Exception {
        LOG.debug("Updating all model claim");
        claimService.updateClaim(claim);
        LOG.debug("claim is saved");
        claim = claimService.getClaim(claimId);
    }
    // </editor-fold>

    @Override
    public String execute() {

        String tabName = getTabName();
        accessRight = applicationAccessibility.checkTabAccessibility(tabName, super.getAuthenticatedUser(), claim);
        String result = accessRight > 1 ? EDITABLE : READ_ONLY;
        LOG.debug("Returning accessibility={} for tab.status={}", result, tabName + '.' + claim.getStatus());
        if (invoiceAction.getModel() != null) {
            LOG.debug("invoiceAction getModel is not null and value of object is: {} ", invoiceAction.getModel());
            LOG.debug("Setting model version in session: {}={}", invoiceAction.getModel().getClass().getName(), invoiceAction.getModel().getVersion());
            session = ActionContext.getContext().getSession();
            session.put(invoiceAction.getModel().getClass().getName(), invoiceAction.getModel().getVersion());
        }
        if (vehicleHireAction.getModel() != null) {
            LOG.debug("Setting model version in session: {}={}", vehicleHireAction.getModel().getClass().getName(), vehicleHireAction.getModel().getVersion());
            session = ActionContext.getContext().getSession();
            session.put(vehicleHireAction.getModel().getClass().getName(), vehicleHireAction.getModel().getVersion());
        }
        if (engineerReportAction.getModel() != null) {
            LOG.debug("Setting model version in session: {}={}", engineerReportAction.getModel().getClass().getName(), engineerReportAction.getModel().getVersion());
            session = ActionContext.getContext().getSession();
            session.put(engineerReportAction.getModel().getClass().getName(), engineerReportAction.getModel().getVersion());
        }
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
        invoiceOriginalAction.setClaimService(claimService);
        invoiceOriginalAction.setClaimId(claimId);
        invoiceOriginalAction.prepare();
        vehicleHireAction.setLookupService(lookupService);
        vehicleHireAction.setClaimService(claimService);
        vehicleHireAction.setClaimId(claimId);
        vehicleHireAction.prepare();
        engineerReportAction.setClaimService(claimService);
        engineerReportAction.setClaimId(claimId);
        engineerReportAction.prepare();
        LOG.debug("ALL PREPARATION DONE");
    }

    // <editor-fold defaultstate="collapsed" desc="SERVICES">
    public List<VehicleClassPriceMapper> getAllVehicleClassPriceMapper() {
        List<VehicleClassPriceMapper> vehicleClassPriceMapper = new ArrayList<VehicleClassPriceMapper>();
        Iterator itr = vehicleClassService.getAllVehicleClass().iterator();
        LOG.debug("total number of iterator {}:", vehicleClassService.getAllVehicleClass().size());
        Date firstRegistration = claim.getCustomer().getHpiFirstRegistration();
        Date hireStart = null;
        if (claim.getVehicleHire() == null) {
            LOG.warn("No vehicle hire for claim: {}", claim.getChoReference());
            hireStart = new Date();
        }
        else
            hireStart = claim.getVehicleHire().getHireStart();
        BigDecimal age = BigDecimal.ZERO;

        if (hireStart != null & firstRegistration != null)
            age = new BigDecimal(DateHelper.DifferenceInYears(hireStart, firstRegistration));

        while (itr.hasNext()) {
            vehicleClass = (VehicleClass) itr.next();
            BigDecimal price = new BigDecimal(0.0);
            try {
                price = vehicleClassPriceService.getPrice(vehicleClass, getHireStart(), age, claim.getInsurer().getId(), claim.getChorganisation().getId());
            } catch (Exception e) {
                LOG.info("Price set to 0.0 as no price found for vehicle class {}", vehicleClass.getName());
            }
            vehicleClassPriceMapper.add(new VehicleClassPriceMapper(vehicleClass.getName(), price));
        }
        LOG.debug("total size in vehicleclasspricemaper list is {}:", vehicleClassPriceMapper.size());
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
    // <editor-fold defaultstate="collapsed" desc="Re-Calculation">

    public void recalculate() throws Exception {


        BigDecimal totalExtras = new BigDecimal(0);
        BigDecimal hireNet = new BigDecimal(0);
        BigDecimal hireVat = new BigDecimal(0);
        BigDecimal hireGross = new BigDecimal(0);
        BigDecimal totalNet = new BigDecimal(0);
        BigDecimal totalVat = new BigDecimal(0);
        BigDecimal totalGross = new BigDecimal(0);
        BigDecimal repairVat = new BigDecimal(0);
        BigDecimal repairGross = new BigDecimal(0);
        BigDecimal engineerVat = new BigDecimal(0);
        BigDecimal engineerGross = new BigDecimal(0);
        BigDecimal storageRecoveryVat = new BigDecimal(0);
        BigDecimal storageRecoveryGross = new BigDecimal(0);
        BigDecimal totalLossVat = new BigDecimal(0);
        BigDecimal totalLossGross = new BigDecimal(0);
        BigDecimal fullTotalRequested = new BigDecimal(0);
        BigDecimal fullTotalToPay = new BigDecimal(0);
        BigDecimal liablitityPercentage = new BigDecimal(0);

        LOG.debug("initial value setup done in recalculate() function");


        totalExtras = totalExtras.add(getCdwFee());

        totalExtras = totalExtras.add(getAutomaticFee());
        totalExtras = totalExtras.add(getAdditionalDriverFee());
        totalExtras = totalExtras.add(getSatNavFee());
        totalExtras = totalExtras.add(getEstateFee());
        totalExtras = totalExtras.add(getBabySeatFee());
        totalExtras = totalExtras.add(getTowBarsFee());
        totalExtras = totalExtras.add(getNonStandardInsurancePremiumFee());
        totalExtras = totalExtras.add(getRoofRackFee());
        totalExtras = totalExtras.add(getAdminFee());
        totalExtras = totalExtras.add(getDualControlFee());
        totalExtras = totalExtras.add(getDeliveryCollectionFee());
        LOG.debug("total extras {}", totalExtras);

        



        


        if (getPreviousHireNet() != null && getPreviousHireVat() != null && !(getPreviousHireNet().doubleValue()==0) && !(getPreviousHireVat().doubleValue()==0)) {
            setHire_vat_used((getPreviousHireVat().divide(getPreviousHireNet(), 4, BigDecimal.ROUND_HALF_UP)));
            LOG.debug(" Hire_vat_used value{} ", getHire_vat_used());
            

            

//            if((hire_vat_used.doubleValue()*100>((Vat_Rate.doubleValue()*100)+1))||(hire_vat_used.doubleValue()*100<((Vat_Rate.doubleValue()*100)-5))){
//                throw new CannotProceed();
//            }

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
        

        hireGross = hireGross.add(hireVat);
        hireGross = hireGross.add(hireNet);

        setHireGross(hireGross.setScale(2, RoundingMode.HALF_UP));
        



        if (getPreviousRepairNet() != null && getPreviousRepairVat() != null && !(getPreviousRepairNet().doubleValue()==0) && !(getPreviousRepairVat().doubleValue()==0)) {
            setRepair_vat_used(getPreviousRepairVat().divide(getPreviousRepairNet(), 4, BigDecimal.ROUND_HALF_UP));//.setScale(3);
            LOG.debug(" Repair_vat_used value{} ", getRepair_vat_used());
             
//            if((repair_vat_used.doubleValue()*100>((Vat_Rate.doubleValue()*100)+1))||(repair_vat_used.doubleValue()*100<((Vat_Rate.doubleValue()*100)-5))){
//                throw new CannotProceed();
//            }

        } else {
            
            repair_vat_used = Vat_Rate;
        }






        

        repairVat = repairVat.add(getRepairNet());
        repairVat = repairVat.multiply(repair_vat_used);

        setRepairVat(repairVat.setScale(2, RoundingMode.HALF_UP));
        

        repairGross = repairGross.add(getRepairVat());
        repairGross = repairGross.add(getRepairNet());

        setRepairGross(repairGross.setScale(2, RoundingMode.HALF_UP));
       


        if (getPreviousEngineerFeeNet() != null && getPreviousEngineerFeeVat() != null && !(getPreviousEngineerFeeNet().doubleValue()==0) && !(getPreviousEngineerFeeVat().doubleValue()==0)) {
            setEngineerFee_vat_used(getPreviousEngineerFeeVat().divide(getPreviousEngineerFeeNet(), 4, BigDecimal.ROUND_HALF_UP));//.setScale(3);
            LOG.debug(" EngineerFee_vat_used value{} ", getEngineerFee_vat_used());
            


//            if((engineerFee_vat_used.doubleValue()*100>((Vat_Rate.doubleValue()*100)+1))||(engineerFee_vat_used.doubleValue()*100<((Vat_Rate.doubleValue()*100)-5))){
//                throw new CannotProceed();
//            }

        } else {
            //LOG.debug(" Used Hire Vat value is Null and default VAT_RATE is used for vat calculation {} ", Vat_Rate);
            engineerFee_vat_used = Vat_Rate;
        }





        engineerVat = engineerVat.add(getEngineerFeeNet());
        engineerVat = engineerVat.multiply(engineerFee_vat_used);

        setEngineerFeeVat(engineerVat.setScale(2, RoundingMode.HALF_UP));

        engineerGross = engineerGross.add(getEngineerFeeVat());
        engineerGross = engineerGross.add(getEngineerFeeNet());

        setEngineerFeeGross(engineerGross.setScale(2, RoundingMode.HALF_UP));



        if (getPreviousTotalLossNet() != null && getPreviousTotalLossVat() != null && !(getPreviousTotalLossNet().doubleValue()==0) && !(getPreviousTotalLossVat().doubleValue()==0)) {
            setTotalLossFee_vat_used(getPreviousTotalLossVat().divide(getPreviousTotalLossNet(), 4, BigDecimal.ROUND_HALF_UP));//.setScale(3);
            LOG.debug(" TotalLossFee_vat_used value{} ", getTotalLossFee_vat_used());
             


//            if((totalLossFee_vat_used.doubleValue()*100>((Vat_Rate.doubleValue()*100)+1))||(totalLossFee_vat_used.doubleValue()*100<((Vat_Rate.doubleValue()*100)-5))){
//                throw new CannotProceed();
//            }

        } else {
            totalLossFee_vat_used = Vat_Rate;
        }





        totalLossVat = totalLossVat.add(totalLossFee_vat_used);
        totalLossVat = totalLossVat.multiply(getTotalLossFeeNet());

        setTotalLossFeeVat(totalLossVat.setScale(2, RoundingMode.HALF_UP));

        totalLossGross = totalLossGross.add(getTotalLossFeeNet());
        totalLossGross = totalLossGross.add(getTotalLossFeeVat());

        setTotalLossFeeGross(totalLossGross.setScale(2, RoundingMode.HALF_UP));


        if (getPreviousStorageNet() != null && getPreviousStorageVat() != null && !(getPreviousStorageNet().doubleValue()==0) && !(getPreviousStorageVat().doubleValue()==0)) {
            setStorageRecovery_vat_used(getPreviousStorageVat().divide(getPreviousStorageNet(), 4, BigDecimal.ROUND_HALF_UP));//.setScale(3);
            LOG.debug(" StorageRecovery_vat_used value{} ", getStorageRecovery_vat_used());


//            if((storageRecovery_vat_used.doubleValue()*100>((Vat_Rate.doubleValue()*100)+1))||(storageRecovery_vat_used.doubleValue()*100<((Vat_Rate.doubleValue()*100)-5))){
//                throw new CannotProceed();
//            }

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
//        LOG.debug(" getRepairNet() value{} ", getRepairNet());
//        LOG.debug(" totalVat value{} ", totalVat.setScale(2, RoundingMode.HALF_UP));

        totalGross = totalGross.add(hireGross);
        totalGross = totalGross.add(repairGross);
        totalGross = totalGross.add(engineerGross);
        totalGross = totalGross.add(totalLossGross);
        totalGross = totalGross.add(storageRecoveryGross);

        setTotalGross(totalGross.setScale(2, RoundingMode.HALF_UP));
        //LOG.debug(" totalGross value{} ", totalGross.setScale(2, RoundingMode.HALF_UP));

        fullTotalRequested = fullTotalRequested.add(totalGross);
        fullTotalRequested = fullTotalRequested.add(getClaimsHandlingInvoiceAmount());
        fullTotalRequested = fullTotalRequested.add(getDeductionForClaimsHandlingFee());
        fullTotalRequested = fullTotalRequested.add(getDiscount());
        fullTotalRequested = fullTotalRequested.add(getTotalPenaltyCharge());

        setFullTotalToPay(fullTotalRequested.setScale(2, RoundingMode.HALF_UP));
        LOG.debug(" fullTotalRequested value{} ", fullTotalRequested);

        liablitityPercentage = liablitityPercentage.add(getPercentageLiabilityAccepted());
        LOG.debug(" liablitityPercentage() value{} ", liablitityPercentage);
        liablitityPercentage = liablitityPercentage.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);

        fullTotalToPay = fullTotalToPay.add(fullTotalRequested);
        fullTotalToPay = fullTotalToPay.multiply(liablitityPercentage);

        setTotalToPay(fullTotalToPay.setScale(2, RoundingMode.HALF_UP));
//        invoiceOriginalAction.model.setTotalToPay_original(fullTotalToPay.setScale(2, RoundingMode.HALF_UP));
        LOG.debug(" fullTotalToPay value{} ", fullTotalToPay);


    }

    /**
     * @return the tpiClaimChk
     */
    public boolean isTpiClaimChk() {

        return vehicleHireAction.getClaim().isTpiClaim();
        //return tpiClaimChk;
    }

    /**
     * @param tpiClaimChk the tpiClaimChk to set
     */
    public void setTpiClaimChk(boolean tpiClaimChk) {
        this.tpiClaimChk = tpiClaimChk;
    }
    // </editor-fold>
}
