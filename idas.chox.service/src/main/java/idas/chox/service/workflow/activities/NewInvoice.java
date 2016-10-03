package idas.chox.service.workflow.activities;

import java.math.BigDecimal;
import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.bre.RulesEngineResponse;
import idas.chox.core.hpi.*;
import idas.chox.core.model.*;
import idas.chox.core.services.*;
import idas.chox.core.util.DateHelper;
import idas.chox.keoghs.Keoghs;
import idas.chox.service.bre.util.ClaimCalcHelper;
import idas.chox.service.bre.util.VehicleClassHelper;
import idas.chox.service.xml.util.NodeHelper;

public class NewInvoice extends BaseActivity {

    private static final Logger LOG = LoggerFactory.getLogger(NewInvoice.class);
    private VehicleClassPriceService vehicleClassPriceService;
    private InsurerDiscountService insurerDiscountService;
    private TaskService taskService;
    private UserService userService;
    private BrePenaltyBandService brePenaltyBandService;
    private boolean autoRoutedInvoice = false;
    protected boolean claimRouted = false;
    protected boolean claimOwnerAssigned = false;
    protected boolean invoiceAccepted = false;
    protected RulesEngineResponse breResponse = null;
    private Keoghs keoghs;

    public void setKeoghs(Keoghs keoghs) {
        this.keoghs = keoghs;
    }

    public RulesEngineResponse getBreResponse() {
        return breResponse;
    }
    
    public boolean isAutoRoutedInvoice() {
        return autoRoutedInvoice;
    }

    public BrePenaltyBandService getBrePenaltyBandService() {
        return brePenaltyBandService;
    }

    public void setBrePenaltyBandService(BrePenaltyBandService brePenaltyBandService) {
        this.brePenaltyBandService = brePenaltyBandService;
    }

    public void setInsurerDiscountService(InsurerDiscountService insurerDiscountService) {
        this.insurerDiscountService = insurerDiscountService;
    }

    public void setVehicleClassPriceService(VehicleClassPriceService vehicleClassPriceService) {
        this.vehicleClassPriceService = vehicleClassPriceService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void beforeProcess(Claim claim) {

        String claimNumber = claim.getThirdParty().getClaimReference();
        //TPI claim type is handled in NewTpiClaim activity
        //Insurer Upload claim type is handled in InsurerUpload activity
        if (ClaimType.isGTA(claim.getClaimType())
                && claim.getInsurer().isGtaAutoRoutingEnable()
                && (claimNumber == null || claim.getInsurer().getGtaRegexExpression() == null
                    || claim.getInsurer().getGtaRegexExpression().isEmpty()
                    || !NodeHelper.isRegularExpressionCheckPass(claim.getInsurer().getGtaRegexExpression(), claimNumber.toUpperCase()))) {
            autoRoutedInvoice = true;
        } else if (ClaimType.isSubscriber(claim.getClaimType())
                && claim.getInsurer().isSubscriberAutoRoutingEnable()
                && (claimNumber == null || claim.getInsurer().getSubscriberRegexExpression() == null
                    || claim.getInsurer().getSubscriberRegexExpression().isEmpty()
                    || !NodeHelper.isRegularExpressionCheckPass(claim.getInsurer().getSubscriberRegexExpression(), claimNumber.toUpperCase()))) {
            autoRoutedInvoice = true;
        } else if (ClaimType.isInsurerVsInsurer(claim.getClaimType())
                && claim.getInsurer().isInsurerVsInsurerAutoRoutingEnable()
                && (claimNumber == null || claim.getInsurer().getInsurerVsInsurerRegexExpression() == null
                    || claim.getInsurer().getInsurerVsInsurerRegexExpression().isEmpty()
                    || !NodeHelper.isRegularExpressionCheckPass(claim.getInsurer().getInsurerVsInsurerRegexExpression(), claimNumber.toUpperCase()))) {
            autoRoutedInvoice = true;
        } else if (ClaimType.isFixedFee(claim.getClaimType())
                && claim.getInsurer().isFixedFeeAutoRoutingEnable()
                && (claimNumber == null || claim.getInsurer().getFixedFeeRegexExpression() == null
                    || claim.getInsurer().getFixedFeeRegexExpression().isEmpty()
                    || !NodeHelper.isRegularExpressionCheckPass(claim.getInsurer().getFixedFeeRegexExpression(), claimNumber.toUpperCase()))) {
            autoRoutedInvoice = true;
        } else if (ClaimType.isCollaborationProtocol(claim.getClaimType())
                && claim.getInsurer().isColaborationProtocolAutoRoutingEnable()
                && (claimNumber == null || claim.getInsurer().getCollaborationProtocolRegexExpression() == null
                    || claim.getInsurer().getCollaborationProtocolRegexExpression().isEmpty()
                    || !NodeHelper.isRegularExpressionCheckPass(claim.getInsurer().getCollaborationProtocolRegexExpression(), claimNumber.toUpperCase()))) {
            autoRoutedInvoice = true;
        }
    }

    @Override
    protected void doProcess(Claim claim) throws Exception {
        LOG.debug("Processing New Invoice activity for claim: {}", claim.getChoReference());
        if (claim.getBreBand() == null) {
            BreBand choBand = getWorkflowContext().getBreBandService().getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
            claim.setBreBand(choBand);
        }
        insurerDiscountService.applyGtaDiscount(claim);
        insurerDiscountService.applyInsurerDiscounts(claim, userService.findByUserName("system"), true);
        claimService.updateLiabilityPayment(claim);

        
        // Set initial penalty band
        claimService.setInitialPenaltyBand(claim);

        // Perform HPI check
        if (!ClaimType.isTPI(claim.getClaimType()) || (ClaimType.isTPI(claim.getClaimType()) && claim.getVehicleHire() != null && claim.getVehicleHire().getVehicleRegistration() != null)) {
            try {
                HpiResponse hpiResponse = Hpi.getHpiInfo(claim.getVehicleHire().getVehicleRegistration());
                LOG.debug("HPI response received: {}", hpiResponse.getModel());
                claim.getVehicleHire().setHpiVehicleManufacturer(hpiResponse.getManufacturer());
                claim.getVehicleHire().setHpiVehicleModel(hpiResponse.getModel());
                claim.getVehicleHire().setHpiVehicleYear(hpiResponse.getYear());
                claim.getVehicleHire().setHpiVehicleCapacity(hpiResponse.getCapacity());
                claim.getVehicleHire().setHpiVehicleDoorplan(hpiResponse.getDoorPlan());
                claim.getVehicleHire().setHpiVehicleTransmission(hpiResponse.getTransmission());
                claim.getVehicleHire().setHpiFirstRegistration(hpiResponse.getFirstRegistration());
            } catch (HpiException ex) {
                LOG.warn("Error getting HPI info for vrn '{}': {}", claim.getVehicleHire().getVehicleRegistration(), ex.getMessage());
                claim.getVehicleHire().setHpiError(ex.getMessage());
            }

            // If CHO has automatic Daily Rate Charge Adjustment activated then check daily rate
            if (claim.getChorganisation().isAdjustDailyRateCharge() && claim.getBreBand().isHasCalculatedCorrectDailyRate()) {
                adjustDailyRateCharge(claim);
            }
        }

        LOG.debug("Processing invoice for claim '{}'", claim.getChoReference());
        breResponse = getWorkflowContext().getBusinessRulesEngService().processResubmitInvoice(claim);
        LOG.debug("Rules engine response received for claim '{}'", claim.getChoReference());
        for (History history : History.New(breResponse)) {
            LOG.debug("Adding BRE history to claim '{}': {} - {}", new Object[]{claim.getChoReference(), history.getRuleId(), history.getNarrative()});
            claim.addHistory(history);
        }

        /*
         * newComment task creation for new invoice if repair gross is not 0.00 
         * and automated repair tasks (for managing/not managing repaur) is
         * activated in the BRE Band
         */
        LOG.debug("repair gross double value for claim with cho ref no is {}, {}", claim.getInvoice().getRepairGross(), claim.getChoReference());
        if (claim.getBreBand().isAllowManagingRepairAutomatedTasks() && claim.getManagingRepair()
                && claim.getInvoice().getRepairGross() != null && claim.getInvoice().getRepairGross().compareTo(BigDecimal.ZERO) != 0 && !ClaimType.isTPI(claim.getClaimType())) {
            if (!createAutomaticInvoiceUploadInsNotificationTask(claim)) {
                LOG.warn("New task creation failed - automatic 'Upload Repair Documentation' task.");
            }
        }
        else if (claim.getBreBand().isAllowNotManagingRepairAutomatedTasks() && !claim.getManagingRepair()
                && claim.getInvoice().getRepairGross() != null && claim.getInvoice().getRepairGross().compareTo(BigDecimal.ZERO) != 0 && !ClaimType.isTPI(claim.getClaimType())) {
            if (!createAutomaticInvoiceUploadInsNotificationTask(claim)) {
                LOG.warn("New task creation failed - automatic 'Upload Repair Documentation' task.");
            }
        }

        if (autoRoutedInvoice && ClaimStatus.INVOICE_APPROVED_BY_BRE.equals(claim.getStatus())) {
            invoiceAccepted = true;
            // re-route claim
            if (claim.getInsurer().isWorkgroupEnable() && claim.getInsurer().getInvoiceWorkgroup() != null) {
                claim.setWorkgroupOriginal(claim.getWorkgroup());
                claim.setWorkgroup(claim.getInsurer().getInvoiceWorkgroup());
                claimRouted = true;
            }

            //re-assign claim
            if (claim.getInsurer().isClaimOwnershipEnable() && claim.getInsurer().getInvoiceOwner() != null) {
                claim.setClaimOwnerOriginal(claim.getClaimOwner());
                claim.setClaimOwner(claim.getInsurer().getInvoiceOwner());
                claimOwnerAssigned = true;
            }
        }
            
        if (claim.getBreBand().isPaymentTeamActive()
                && ((ClaimType.isGTA(claim.getClaimType()) && claim.getInsurer().isGtaPaymentsTeamEnable())
                    || (ClaimType.isSubscriber(claim.getClaimType()) && claim.getInsurer().isSubscriberPaymentsTeamEnable())
                    || (ClaimType.isInsurerUpload(claim.getClaimType()) && claim.getInsurer().isInsurerManualPaymentsTeamEnable())
                    || (ClaimType.isInsurerVsInsurer(claim.getClaimType()) && claim.getInsurer().isInsurerVsInsurerPaymentsTeamEnable())
                    || (ClaimType.isFixedFee(claim.getClaimType()) && claim.getInsurer().isFixedFeePaymentsTeamEnable())
                    || (ClaimType.isCollaborationProtocol(claim.getClaimType()) && claim.getInsurer().isCollaborationPaymentsTeamEnable()))
                && (!claim.getInsurer().isWorkgroupEnable() || claim.getWorkgroup() == null || !claim.getWorkgroup().isStpExcluded())) {
            claim.getInvoice().setPaymentTeam(true);
            if (ClaimStatus.INVOICE_APPROVED_BY_BRE.equals(claim.getStatus())) {
                invoiceAccepted = true;
            }
        }
        
        if (invoiceAccepted) {
            getDataService().save(claim);
            logTransaction(claim, claim.getPreviousStatus(), claim.getStatus(), 0);
            // move claim to next status
            setCurrentStatus(claim.getStatus());
            claim.setPreviousStatus(getCurrentStatus());
            if (!ClaimType.isInsurerVsInsurer(claim.getClaimType())
                && !ClaimType.isSubscriber(claim.getClaimType()) && !ClaimType.isFixedFee(claim.getClaimType()) &&
                   (claim.getLiabilityStatus() == LiabilityStatus.LIABILITY_NULL
                    || claim.getLiabilityStatus() == LiabilityStatus.LIABILITY_UNKNOWN
                    || claim.getLiabilityStatus() == LiabilityStatus.LIABILITY_DISPUTED
                    || claim.getLiabilityStatus() == LiabilityStatus.LIABILITY_REPUDIATED)) {
                claim.setStatus(ClaimStatus.AWAITING_LIABILITY_RESOLUTION);
            } else {
                    claim.setStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT);
            }
        }
        // If not a supplementary claim, Queue to send to Keoghs for ADA fraud check
        if (claim.getBreBand().isFraudCheckEnable() && !ClaimType.isSupplementaryInvoice(claim.getClaimType())) {
            try {
                KeoghsRequest request  = keoghs.queue(claim, "Invoice Upload");
                LOG.debug("New Invoice '{}' queued to Keoghs.", claim.getChoReference());
            } catch (Exception ex) {
                LOG.error("Error sending new invoice with choref '{}' to keoghs: {}", claim.getChoReference(), ex.getMessage(), ex);
            }
        }
    }

    @Override
    protected void afterProcess(Claim claim) throws Exception {
        activityEventGenerator.generate(claim, this);
        // If this is a TPI claim, we now need to process the chained NewTpiClaim activity
        if (getChainActivity() != null && ClaimType.isTPI(claim.getClaimType())) {
            LOG.debug("Processing next chain activity.");
            getChainActivity().setWorkflowContext(getProcessContext());
            getChainActivity().processInBatch(claim);
        } else {
            LOG.debug("Saving Claim '{}' ", claim.getChoReference());
            getDataService().save(claim);
            logTransaction(claim);
        }
    }


    private boolean createAutomaticInvoiceUploadInsNotificationTask(Claim claim) {
        if (claim.getChorganisation().isTaskManagementEnable()) {
            Task task = new Task();
            task.setComplete(Boolean.FALSE);
            task.setDescription("It is advisable to upload the repair invoice for this claim in order to support the associated repair costs.");
            task.setDueDate(DateHelper.getCurrentDateTime());
            task.setType("Repair Documentation");
            task.setVisibility(2);
            task.setInsurer(Boolean.FALSE);
            task.setRaisedBy(userService.findByUserName("system"));
            task.setClaim(claim);
            try {
                taskService.createNewTask(task);
                LOG.debug("Task creation successful for claim '{}'", claim.getChoReference());
                return true;
            } catch (Exception ex) {
                LOG.error("Exception creating invoice upload notification task for insurer on claim '{}': {}", claim.getChoReference(), ex);
                return false;
            }
        } else {
            return true;
        }
    }

    private void adjustDailyRateCharge(Claim claim) {
        if (claim.getVehicleHire() != null && VehicleClassHelper.isVehicleClassValid(claim.getVehicleHire().getVehicleClass())) {
            VehicleClass vehicleClass = claim.getVehicleHire().getVehicleClass();
            ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);
            BigDecimal allowedDailyRate;
            BigDecimal vehicleClassPrice = BigDecimal.ZERO;
            try {
                vehicleClassPrice = vehicleClassPriceService.getPrice(claim.getClaimType(), vehicleClass, claim.getVehicleHire().getHireStart(), claim.getInsurer().getId(), claim.getChorganisation().getId());
            } catch (Exception ex) {
                LOG.debug("Vehicle Class Price set to 0.0 as no price found for vehicle class {} (Supplier ref='{}')", vehicleClass.getName(), claim.getChoReference());
            }
            allowedDailyRate = vehicleClassPrice.add(claim.getBreBand().getHireRateChargeTolerance());
            BigDecimal dailyHireRateCharged = cCalc.getDailyHireRateCharged();
            LOG.debug("Comparing dailyHireRateCharged={} to allowedDailyRate={} for claim {}", new Object[]{dailyHireRateCharged, allowedDailyRate, claim.getChoReference()});
            if (dailyHireRateCharged.compareTo(allowedDailyRate) > 0) {
                LOG.debug("Allowed Daily Rate rule would fail for claim '{}'", claim.getChoReference());
                LOG.debug("Comparing daily hire rate difference of {} to daily rate charge limit of {}", dailyHireRateCharged.subtract(allowedDailyRate), new BigDecimal(claim.getChorganisation().getDailyRateChargeLimit()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
                if (dailyHireRateCharged.subtract(allowedDailyRate).compareTo(new BigDecimal(claim.getChorganisation().getDailyRateChargeLimit()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP)) <= 0) {
                    Invoice invoice = claim.getInvoice();
                    BigDecimal oldHireNetLessExtras = invoice.getHireNet().subtract(cCalc.getExCalcHelper().getTotalExtras());
                    BigDecimal newHireNetLessExtras = allowedDailyRate.multiply(new BigDecimal(claim.getVehicleHire().getDays()));
                    BigDecimal hireNetDifference = oldHireNetLessExtras.subtract(newHireNetLessExtras);
                    LOG.debug("Adjusting Hire Net (less extras) from {} to {}", oldHireNetLessExtras, newHireNetLessExtras);
                    BigDecimal oldHireNet = invoice.getHireNet();
                    invoice.setHireNet(oldHireNet.subtract(hireNetDifference));
                    LOG.debug("Hire Net changed  from {} to {}", oldHireNet, invoice.getHireNet());
                    BigDecimal oldHireVat = invoice.getHireVat();
                    // Calculate VAT rate used
                    BigDecimal vatRateUsed = BigDecimal.ZERO;
                    if (oldHireNet.compareTo(BigDecimal.ZERO) != 0) {
                        vatRateUsed = oldHireVat.divide(oldHireNet, 4, BigDecimal.ROUND_HALF_UP);
                    }
                    LOG.debug("Using VAT rate of {}", vatRateUsed);
                    invoice.setHireVat(invoice.getHireNet().multiply(vatRateUsed).setScale(2, BigDecimal.ROUND_HALF_UP));
                    BigDecimal hireVatDifference = oldHireVat.subtract(invoice.getHireVat());
                    LOG.debug("Hire Vat changed  from {} to {}", oldHireVat, invoice.getHireVat());
                    BigDecimal oldHireGross = invoice.getHireGross();
                    invoice.setHireGross(invoice.getHireNet().add(invoice.getHireVat()));
                    LOG.debug("Hire Gross changed  from {} to {}", oldHireGross, invoice.getHireGross());
                    LOG.debug("Changing Total Net from {} to {}", invoice.getTotalNet(), invoice.getTotalNet().subtract(hireNetDifference));
                    invoice.setTotalNet(invoice.getTotalNet().subtract(hireNetDifference));
                    LOG.debug("Changing Total VAT from {} to {}", invoice.getTotalVat(), invoice.getTotalVat().subtract(hireVatDifference));
                    invoice.setTotalVat(invoice.getTotalVat().subtract(hireVatDifference));
                    LOG.debug("Changing Total Gross from {} to {}", invoice.getTotalGross(), invoice.getTotalGross().subtract(hireNetDifference).subtract(hireVatDifference));
                    invoice.setTotalGross(invoice.getTotalGross().subtract(hireNetDifference).subtract(hireVatDifference));
                    LOG.debug("Changing Full Total To Pay from {} to {}", invoice.getFullTotalToPay(), invoice.getFullTotalToPay().subtract(hireNetDifference).subtract(hireVatDifference));
                    invoice.setFullTotalToPay(invoice.getFullTotalToPay().subtract(hireNetDifference).subtract(hireVatDifference));
                    if (ClaimType.isInsurerVsInsurer(claim.getClaimType()) || ClaimType.isSubscriber(claim.getClaimType()) 
                            || ClaimType.isFixedFee(claim.getClaimType()) || ClaimType.isCollaborationProtocol(claim.getClaimType()) ) {
                        LOG.debug("Changing Total To Pay from {} to {}", invoice.getTotalToPay(), invoice.getFullTotalToPay());
                        invoice.setTotalToPay(invoice.getFullTotalToPay());
                    } else {
                        LOG.debug("Changing Total To Pay from {} to {}", invoice.getTotalToPay(), invoice.getFullTotalToPay().multiply(claim.getPercentageLiabilityAccepted()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
                        invoice.setTotalToPay(invoice.getFullTotalToPay().multiply(claim.getPercentageLiabilityAccepted()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
                    }
                        LOG.debug("Hire Rate Charged per Day changed  from {} to {}", invoice.getHireRateChargedPerDay(), allowedDailyRate);
                    invoice.setHireRateChargedPerDay(allowedDailyRate);
                    Comment comment = Comment.newComment(2, MessageFormat.format("Hire rate adjusted from {0} to {1}", dailyHireRateCharged.setScale(2, BigDecimal.ROUND_HALF_UP).toString(), allowedDailyRate.toString()));
                    comment.setClaim(claim);
                    claim.addComment(comment);
                } else {
                    LOG.debug("Difference outside allowed limit - no adjustments will be made.");
                }
            }
        }
    }
}
