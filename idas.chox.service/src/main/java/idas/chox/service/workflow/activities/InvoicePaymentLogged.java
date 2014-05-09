package idas.chox.service.workflow.activities;

import java.math.BigDecimal;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Comment;
import idas.chox.core.model.Invoice;

public class InvoicePaymentLogged extends BaseActivity {

    private BigDecimal hireGrossPaid;
    private BigDecimal repairGrossPaid;
    private BigDecimal engineerFeeGrossPaid;
    private BigDecimal totalLossFeeGrossPaid;
    private BigDecimal storageRecoveryGrossPaid;
    private BigDecimal hirePenaltyChargePaid;
    private BigDecimal repairPenaltyChargePaid;
    private BigDecimal paymentDetailsClaimHandInvAmt;
    private BigDecimal paymentDetailsDeductionClaimHandFee;
    private BigDecimal paymentDetailsCHODiscount;
    private BigDecimal paymentDetailsInsurerDiscount;
    private BigDecimal projectedFinalPayment;
    private BigDecimal finalPayment;
    private boolean penaltyChargesPaid;

    public void setEngineerFeeGrossPaid(BigDecimal engineerFeeGrossPaid) {
        this.engineerFeeGrossPaid = engineerFeeGrossPaid;
    }

    public void setFinalPayment(BigDecimal finalPayment) {
        this.finalPayment = finalPayment;
    }

    public void setHireGrossPaid(BigDecimal hireGrossPaid) {
        this.hireGrossPaid = hireGrossPaid;
    }

    public void setHirePenaltyChargePaid(BigDecimal hirePenaltyChargePaid) {
        this.hirePenaltyChargePaid = hirePenaltyChargePaid;
    }

    public void setProjectedFinalPayment(BigDecimal projectedFinalPayment) {
        this.projectedFinalPayment = projectedFinalPayment;
    }

    public void setRepairGrossPaid(BigDecimal repairGrossPaid) {
        this.repairGrossPaid = repairGrossPaid;
    }

    public void setRepairPenaltyChargePaid(BigDecimal repairPenaltyChargePaid) {
        this.repairPenaltyChargePaid = repairPenaltyChargePaid;
    }

    public void setStorageRecoveryGrossPaid(BigDecimal storageRecoveryGrossPaid) {
        this.storageRecoveryGrossPaid = storageRecoveryGrossPaid;
    }

    public void setTotalLossFeeGrossPaid(BigDecimal totalLossFeeGrossPaid) {
        this.totalLossFeeGrossPaid = totalLossFeeGrossPaid;
    }

    public void setPenaltyChargesPaid(boolean penaltyChargesPaid) {
        this.penaltyChargesPaid = penaltyChargesPaid;
    }

    public void setPaymentDetailsCHODiscount(BigDecimal paymentDetailsCHODiscount) {
        this.paymentDetailsCHODiscount = paymentDetailsCHODiscount;
    }

    public void setPaymentDetailsClaimHandInvAmt(BigDecimal paymentDetailsClaimHandInvAmt) {
        this.paymentDetailsClaimHandInvAmt = paymentDetailsClaimHandInvAmt;
    }

    public void setPaymentDetailsDeductionClaimHandFee(BigDecimal paymentDetailsDeductionClaimHandFee) {
        this.paymentDetailsDeductionClaimHandFee = paymentDetailsDeductionClaimHandFee;
    }

    public void setPaymentDetailsInsurerDiscount(BigDecimal paymentDetailsInsurerDiscount) {
        this.paymentDetailsInsurerDiscount = paymentDetailsInsurerDiscount;
    }

    public BigDecimal getHireGrossPaid() {
        return hireGrossPaid;
    }

    public BigDecimal getRepairGrossPaid() {
        return repairGrossPaid;
    }

    public BigDecimal getEngineerFeeGrossPaid() {
        return engineerFeeGrossPaid;
    }

    public BigDecimal getTotalLossFeeGrossPaid() {
        return totalLossFeeGrossPaid;
    }

    public BigDecimal getStorageRecoveryGrossPaid() {
        return storageRecoveryGrossPaid;
    }

    public BigDecimal getHirePenaltyChargePaid() {
        return hirePenaltyChargePaid;
    }

    public BigDecimal getRepairPenaltyChargePaid() {
        return repairPenaltyChargePaid;
    }

    public BigDecimal getPaymentDetailsClaimHandInvAmt() {
        return paymentDetailsClaimHandInvAmt;
    }

    public BigDecimal getPaymentDetailsDeductionClaimHandFee() {
        return paymentDetailsDeductionClaimHandFee;
    }

    public BigDecimal getPaymentDetailsCHODiscount() {
        return paymentDetailsCHODiscount;
    }

    public BigDecimal getPaymentDetailsInsurerDiscount() {
        return paymentDetailsInsurerDiscount;
    }

    public BigDecimal getProjectedFinalPayment() {
        return projectedFinalPayment;
    }

    public BigDecimal getFinalPayment() {
        return finalPayment;
    }

    public boolean isPenaltyChargesPaid() {
        return penaltyChargesPaid;
    }

    @Override
    public boolean needsClaimLockedCheck() {
        return true;
    }

    @Override
    protected void doProcess(Claim claim) {
        Invoice invoice = claim.getInvoice();
        if (finalPayment == null) { // Ok hit - no fields changed. Take values from invoice
            if (ClaimType.isInsurerVsInsurer(claim.getClaimType()) || ClaimType.isSubscriber(claim.getClaimType()) 
                  || ClaimType.isFixedFee(claim.getClaimType()) || ClaimType.isCollaborationProtocol(claim.getClaimType()) ) {
                invoice.setHireGrossPaid(invoice.getHireGross());
                invoice.setRepairGrossPaid(invoice.getRepairGross());
                invoice.setEngineerFeeGrossPaid(invoice.getEngineerFeeGross());
                invoice.setTotalLossFeeGrossPaid(invoice.getTotalLossFeeGross());
                invoice.setStorageRecoveryGrossPaid(invoice.getStorageRecoveryGross());
                invoice.setHirePenaltyChargePaid(invoice.getHirePenaltyCharge());
                invoice.setRepairPenaltyChargePaid(invoice.getRepairPenaltyCharge());
                invoice.setChoDiscountFeePaid(invoice.getDiscount());
                invoice.setClaimHandlerChargePaid(invoice.getClaimsHandlingInvoiceAmount());
                invoice.setDeductionClaimHandlerFeePaid(invoice.getDeductionForClaimsHandlingFee());
                invoice.setInsurerDiscountFeePaid(invoice.getInsurerDiscount());
            } else {
                invoice.setHireGrossPaid(invoice.getHireGross().multiply(claim.getPercentageLiabilityAccepted()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
                invoice.setRepairGrossPaid(invoice.getRepairGross().multiply(claim.getPercentageLiabilityAccepted()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
                invoice.setEngineerFeeGrossPaid(invoice.getEngineerFeeGross().multiply(claim.getPercentageLiabilityAccepted()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
                invoice.setTotalLossFeeGrossPaid(invoice.getTotalLossFeeGross().multiply(claim.getPercentageLiabilityAccepted()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
                invoice.setStorageRecoveryGrossPaid(invoice.getStorageRecoveryGross().multiply(claim.getPercentageLiabilityAccepted()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
                invoice.setHirePenaltyChargePaid(invoice.getHirePenaltyCharge().multiply(claim.getPercentageLiabilityAccepted()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
                invoice.setRepairPenaltyChargePaid(invoice.getRepairPenaltyCharge().multiply(claim.getPercentageLiabilityAccepted()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
                invoice.setChoDiscountFeePaid(invoice.getDiscount().multiply(claim.getPercentageLiabilityAccepted()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
                invoice.setClaimHandlerChargePaid(invoice.getClaimsHandlingInvoiceAmount().multiply(claim.getPercentageLiabilityAccepted()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
                invoice.setDeductionClaimHandlerFeePaid(invoice.getDeductionForClaimsHandlingFee().multiply(claim.getPercentageLiabilityAccepted()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
                invoice.setInsurerDiscountFeePaid(invoice.getInsurerDiscount().multiply(claim.getPercentageLiabilityAccepted()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
            }
            if (invoice.getInterimPaymentMade() != null) {
                invoice.setFinalPayment(invoice.getTotalToPay().subtract(invoice.getInterimPaymentMade()));
            } else {
                invoice.setFinalPayment(invoice.getTotalToPay());
            }
            if (invoice.getTotalPenaltyCharge() != null && invoice.getTotalPenaltyCharge().compareTo(BigDecimal.ZERO) > 0) {
                claim.addComment(Comment.newComment(0, "A full payment amount of £" + invoice.getFinalPayment() + " has been made."));
                invoice.setPenaltyChargesPaid(Boolean.TRUE);
            } else {
                claim.addComment(Comment.newComment(0, "A full payment amount of £" + invoice.getFinalPayment() + " has been made."));
                invoice.setPenaltyChargesPaid(Boolean.FALSE);
            }
        } else {
            BigDecimal total = invoice.getTotalToPay();
            if (invoice.getInterimPaymentMade() != null) {
                total = total.subtract(invoice.getInterimPaymentMade());
            }
            invoice.setHireGrossPaid(hireGrossPaid);
            invoice.setRepairGrossPaid(repairGrossPaid);
            invoice.setEngineerFeeGrossPaid(engineerFeeGrossPaid);
            invoice.setTotalLossFeeGrossPaid(totalLossFeeGrossPaid);
            invoice.setStorageRecoveryGrossPaid(storageRecoveryGrossPaid);
            invoice.setHirePenaltyChargePaid(hirePenaltyChargePaid);
            invoice.setRepairPenaltyChargePaid(repairPenaltyChargePaid);
            invoice.setChoDiscountFeePaid(paymentDetailsCHODiscount);
            invoice.setClaimHandlerChargePaid(paymentDetailsClaimHandInvAmt);
            invoice.setDeductionClaimHandlerFeePaid(paymentDetailsDeductionClaimHandFee);
            invoice.setInsurerDiscountFeePaid(paymentDetailsInsurerDiscount);
            invoice.setFinalPayment(finalPayment);
            invoice.setPenaltyChargesPaid(penaltyChargesPaid);
            if (!penaltyChargesPaid && invoice.getTotalPenaltyCharge() != null && invoice.getTotalPenaltyCharge().compareTo(BigDecimal.ZERO) > 0) {
                claim.addComment(Comment.newComment(0, "A payment amount of £" + finalPayment + " has been made on a total of £" + invoice.getTotalToPay() + " (penalty charges have not been paid)."));
            } else if (total.compareTo(finalPayment) != 0) {
                claim.addComment(Comment.newComment(0, "A payment amount of £" + finalPayment + " has been made on a total of £" + invoice.getTotalToPay()));
            } else {
                claim.addComment(Comment.newComment(0, "A full payment amount of £" + finalPayment + " has been made."));
            }
        }
        claim.setStatus(ClaimStatus.INVOICE_PAYMENT_LOGGED);
    }

}