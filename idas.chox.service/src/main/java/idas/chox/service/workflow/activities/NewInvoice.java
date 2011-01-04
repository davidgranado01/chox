package idas.chox.service.workflow.activities;

import idas.chox.core.bre.RulesEngineResponse;
import idas.chox.core.hpi.*;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;
import idas.chox.core.model.History;
import idas.chox.core.model.Invoice;
import idas.chox.core.model.VehicleClass;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.VehicleClassPriceService;
import idas.chox.service.bre.util.CalcHelper;
import idas.chox.service.bre.util.ClaimCalcHelper;
import idas.chox.service.bre.util.VehicleClassHelper;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.security.AccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewInvoice extends BaseActivity {
    private static final Logger LOG = LoggerFactory.getLogger(NewInvoice.class);
    private VehicleClassPriceService vehicleClassPriceService;

    public void setVehicleClassPriceService(VehicleClassPriceService vehicleClassPriceService) {
        this.vehicleClassPriceService = vehicleClassPriceService;
    }

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        if (!securityInfoProvider.isInRoleOf(WebUserRole.ROLE_CHO)) {
            throw new AccessDeniedException("Not in correct role to upload an invoice.");
        }
    }


    @Override
    protected void doProcess(Claim claim) throws Exception {
        // Perform HPI check
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
            LOG.warn("Error getting HPI info for vrn '{}': {}",  claim.getCustomer().getVehicleRegistration(), ex.getMessage());
            claim.getVehicleHire().setHpiError(ex.getMessage());
        }

        // If CHO has automatic Daily Rate Charge Adjustment activated then check daily rate
        if (claim.getChorganisation().isAdjustDailyRateCharge() && claim.getBreBand().isHasCalculatedCorrectDailyRate())
            adjustDailyRateCharge(claim);

        LOG.debug("Processing invoice for claim '{}'", claim.getChoReference());
        RulesEngineResponse response = getWorkflowContext().getBusinessRulesEngService().processResubmitInvoice(claim);
        LOG.debug("Rules engine response received for claim '{}'", claim.getChoReference());
        for (History history : History.New(response)) {
            LOG.debug("Adding BRE history to claim '{}': {}", claim.getChoReference(), history.getNarrative());
            claim.addHistory(history);
        }
        LOG.debug("Setting status for claim '{}'", claim.getChoReference());
        claim.setStatus(response.getStatus(claim.getInsurer().isEngineersEnable()));
        LOG.debug("Status set for claim '{}': ", claim.getChoReference(), claim.getStatus());
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA);
    }

    private void adjustDailyRateCharge(Claim claim) {
            VehicleClass vehicleClass = claim.getVehicleHire().getVehicleClass();
            if (VehicleClassHelper.isVehicleClassValid(vehicleClass)) {
                ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);
                BigDecimal allowedDailyRate = new BigDecimal(0.00);
                BigDecimal vehicleClassPrice = new BigDecimal(0.00);
                try {
                    vehicleClassPrice = vehicleClassPriceService.getPrice(vehicleClass, claim.getVehicleHire().getHireStart());
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
                        if (oldHireNet.compareTo(BigDecimal.ZERO) != 0)
                            vatRateUsed = oldHireVat.divide(oldHireNet, 4, BigDecimal.ROUND_HALF_UP);
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
                    }
                    else
                        LOG.debug("Difference outside allowed limit - no adjustments will be made.");
                }
        }
    }

}
