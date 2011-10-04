package idas.chox.service.workflow.activities;

import idas.chox.core.bre.RulesEngineResponse;
import idas.chox.core.hpi.*;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;
import idas.chox.core.model.History;
import idas.chox.core.model.Invoice;
import idas.chox.core.model.Task;
import idas.chox.core.model.VehicleClass;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.InsurerDiscountService;
import idas.chox.core.services.TaskService;
import idas.chox.core.services.UserService;
import idas.chox.core.services.VehicleClassPriceService;
import idas.chox.core.util.DateHelper;
import idas.chox.service.bre.util.ClaimCalcHelper;
import idas.chox.service.bre.util.VehicleClassHelper;
import idas.chox.service.xml.util.NodeHelper;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import org.springframework.security.AccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewInvoice extends BaseActivity {

    private static final Logger LOG = LoggerFactory.getLogger(NewInvoice.class);
    private VehicleClassPriceService vehicleClassPriceService;
    private InsurerDiscountService insurerDiscountService;
    private TaskService taskService;
    private UserService userService;

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
    protected void validate(Claim claim) throws Exception {
        LOG.debug("Validating Claim in NewInvoice activity: {}", claim.getChoReference());
        if (claim.isTpiClaim()) {
            LOG.debug("Validating a TPI claim");
            if (!claim.isTransient()) {
                LOG.error("Claim isn't transient!!! : {}", claim.getChoReference());
                throw new Exception("A process new claim attempt failed due to claim is already exist.");
            }
            expectingStatuses.clear();
            expectingStatuses.add(null);
            String claimNumber = claim.getClaimNumber();
            if (claimNumber != null && !claimNumber.isEmpty()) {
                claim.setClaimNumber(claimNumber.trim());
            }
        } else {
            LOG.debug("Non TPI claim");
        }
        super.validate(claim);
        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        if (!securityInfoProvider.isInRoleOf(WebUserRole.ROLE_CHO)) {
            throw new AccessDeniedException("Not in correct role to upload an invoice.");
        }
        LOG.debug("Claim validated in NewInvoice activity: {}", claim.getChoReference());
    }

    @Override
    protected void beforeProcess(Claim claim) {
            String claimNumber = claim.getThirdParty().getClaimReference();
            if (claimNumber != null && !claimNumber.equalsIgnoreCase("") && claim.getInsurer().getTpiRegexExpression() != null) {
                NodeHelper nodeHelper = new NodeHelper();
                if (nodeHelper.isRegularExpressionCheckPass(claim.getInsurer().getTpiRegexExpression(), claimNumber.toUpperCase())) {
                    claim.setSpecialRoutedTpiClaim(false);
                } else {
                    claim.setSpecialRoutedTpiClaim(true);
                }
            } else {
                claim.setSpecialRoutedTpiClaim(true);
            }
    }

    @Override
    protected void doProcess(Claim claim) throws Exception {
        LOG.debug("Processing New Invoice activity for claim: {}", claim.getChoReference());
        // Perform HPI check
        if (!claim.isTpiClaim() || (claim.isTpiClaim() && claim.getVehicleHire() != null && claim.getVehicleHire().getVehicleRegistration() != null)) {
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
                LOG.warn("Error getting HPI info for vrn '{}': {}", claim.getCustomer().getVehicleRegistration(), ex.getMessage());
                claim.getVehicleHire().setHpiError(ex.getMessage());
            }


            // If CHO has automatic Daily Rate Charge Adjustment activated then check daily rate
            if (claim.getChorganisation().isAdjustDailyRateCharge() && claim.getBreBand().isHasCalculatedCorrectDailyRate()) {
                adjustDailyRateCharge(claim);
            }
        }
        /*
         *  Add insurer dicount amount (price is configured in chox (or) insurer admin - insurance - discounts tab)
         */
        BigDecimal insurerDiscountPercentage = insurerDiscountService.getDiscountPercentage(claim.getInsurer().getId(), claim.getChorganisation().getId(), Calendar.getInstance().getTime());

        /*
         *  Add public note for insurer discount percentage
         */
        LOG.debug("INSURER DISCOUNT PERCENTAGE in new invoice comparision value is {} ", insurerDiscountPercentage.compareTo(BigDecimal.ZERO));
        if (insurerDiscountPercentage.compareTo(BigDecimal.ZERO) == 1) {
//            LOG.debug("insurerdiscount in new invoice comparision value is {} ", insurerDiscountAmount.compareTo(BigDecimal.ZERO));
            Comment comment = Comment.New(0, "A discount of £" + claim.getInvoice().getInsurerDiscount().multiply(new BigDecimal(-1))+" ("+insurerDiscountPercentage +"%) "+ "has been applied to this invoice based on the discount contract in place.");
            comment.setRaisedBy(userService.findByUserName("system"));
            claim.addComment(comment);
        }

        LOG.debug("Processing invoice for claim '{}'", claim.getChoReference());
        RulesEngineResponse response = getWorkflowContext().getBusinessRulesEngService().processResubmitInvoice(claim);
        LOG.debug("Rules engine response received for claim '{}'", claim.getChoReference());
        for (History history : History.New(response)) {
            LOG.debug("Adding BRE history to claim '{}': {} - " + history.getNarrative(), claim.getChoReference(), history.getRuleId());
            claim.addHistory(history);
        }


        //   new task creation for new invoice if repair gross is not 0.00 ////////////////////////////

        LOG.debug("repair gross double value for claim with cho ref no is {}, {}", claim.getInvoice().getRepairGross(), claim.getChoReference());
        if (claim.getInvoice().getRepairGross() != null && claim.getInvoice().getRepairGross().compareTo(BigDecimal.ZERO) != 0 && !claim.isTpiClaim()) {
            if (!createAutomaticInvoiceUploadInsNotificationTask(claim)) {
                LOG.debug("new task creation failed.");
            }
        }

        
        // Check to see if we have an Insurer vs Insurer claim (that doesn't match the regex)
        if (claim.isInsurerVsInsurerClaim()) {
            if (ClaimStatus.INVOICE_APPROVED_BY_BRE.equals(claim.getStatus()) && claim.isSpecialRoutedTpiClaim()) {
                // re-route claim
                if (claim.getInsurer().isWorkgroupEnable() && claim.getInsurer().getTpiWorkgroup() != null) {
                    claim.setWorkgroupOriginal(claim.getWorkgroup());
                    claim.setWorkgroup(claim.getInsurer().getTpiWorkgroup());
                }

                //re-assign claim
                if (claim.getInsurer().isClaimOwnershipEnable() && claim.getInsurer().getTpiClaimOwner() != null) {
                    claim.setClaimOwnerOriginal(claim.getClaimOwner());
                    claim.setClaimOwner(claim.getInsurer().getTpiClaimOwner());
                }
                getDataService().save(claim);
                logTransaction(claim, claim.getPreviousStatus(), claim.getStatus(), 0);
                // move claim to next status
                currentStatus = claim.getStatus();
                claim.setPreviousStatus(currentStatus);
                claim.setStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT);
            }

        }
    }

    @Override
    protected void afterProcess(Claim claim) throws Exception {
        LOG.debug("Saving Claim '{}' ", claim.getChoReference());
        if (!claim.isTpiClaim()) {
            getDataService().save(claim);
            logTransaction(claim);
        } else {

            if (chainActivity != null) {
                LOG.debug("Processing next chain activity.");
                chainActivity.setWorkflowContext(processContext);
                chainActivity.processInBatch(claim);
            }
        }
    }

    public boolean createAutomaticInvoiceUploadInsNotificationTask(Claim claim) {
        if (claim.getChorganisation().isTaskManagementEnable()) {
            Task task = new Task();
            task.setComplete(Boolean.FALSE);
            task.setDescription("It is advisable to upload the repair invoice for this claim in order to support the associated repair costs.");
            task.setDueDate(DateHelper.getCurrentDateTime());
            task.setType("Repair Documentation");
            task.setVisibility(2);
//                                    task.setVisibilityRole(visibilityRole);
            task.setInsurer(Boolean.FALSE);
            task.setRaisedBy(userService.findByUserName("system"));
            task.setClaim(claim);
            try {
                taskService.createNewTask(task);
                LOG.debug("Task creation successful for claim '{}'", claim.getChoReference());
                return true;
            } catch (Exception ex) {
                LOG.debug("Exception caught in creating task for claim '{}': {}", claim.getChoReference(), ex.getMessage());
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA);
    }

    private void adjustDailyRateCharge(Claim claim) {
        if (claim.getVehicleHire() != null && VehicleClassHelper.isVehicleClassValid(claim.getVehicleHire().getVehicleClass())) {
            VehicleClass vehicleClass = claim.getVehicleHire().getVehicleClass();
            ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);
            BigDecimal allowedDailyRate = new BigDecimal(0.00);
            BigDecimal vehicleClassPrice = new BigDecimal(0.00);
            try {
                vehicleClassPrice = vehicleClassPriceService.getPrice(vehicleClass, claim.getVehicleHire().getHireStart(), claim.getInsurer().getId(), claim.getChorganisation().getId());
            } catch (Exception ex) {
                LOG.info("Vehicle Class Price set to 0.0 as no price found for vehicle class {} (Supplier ref='{}')", vehicleClass.getName(), claim.getChoReference());
            }
            allowedDailyRate = vehicleClassPrice.add(claim.getBreBand().getHireRateChargeTolerance());
            BigDecimal dailyHireRateCharged = cCalc.getDailyHireRateCharged();
            LOG.debug("Comparing dailyHireRateCharged={} to allowedDailyRate={} for claim " + claim.getChoReference(), dailyHireRateCharged, allowedDailyRate);
            if (dailyHireRateCharged.compareTo(allowedDailyRate) > 0) {
                LOG.info("Allowed Daily Rate rule would fail for claim '{}'", claim.getChoReference());
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
                    LOG.debug("Changing Total To Pay from {} to {}", invoice.getTotalToPay(), invoice.getFullTotalToPay().multiply(claim.getPercentageLiabilityAccepted()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
                    invoice.setTotalToPay(invoice.getFullTotalToPay().multiply(claim.getPercentageLiabilityAccepted()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
                    LOG.debug("Hire Rate Charged per Day changed  from {} to {}", invoice.getHireRateChargedPerDay(), allowedDailyRate);
                    invoice.setHireRateChargedPerDay(allowedDailyRate);
                    Comment comment = Comment.New(2, "Hire rate adjusted from " + dailyHireRateCharged.setScale(2, BigDecimal.ROUND_HALF_UP) + " to " + allowedDailyRate);
                    comment.setClaim(claim);
                    claim.getComments().add(comment);
                } else {
                    LOG.debug("Difference outside allowed limit - no adjustments will be made.");
                }
            }
        }
    }
}
