package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;
import idas.chox.core.model.Invoice;
import idas.chox.core.security.SecurityInfoProvider;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.security.access.AccessDeniedException;

public class InvoicePaymentLogged extends BaseActivity {

    private BigDecimal hireGrossPaid;
    private BigDecimal repairGrossPaid;
    private BigDecimal engineerFeeGrossPaid;
    private BigDecimal totalLossFeeGrossPaid;
    private BigDecimal storageRecoveryGrossPaid;
    private BigDecimal hirePenaltyChargePaid;
    private BigDecimal repairPenaltyChargePaid;
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

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        if (!securityInfoProvider.isInRoleOf("ROLE_INS_CH") && !securityInfoProvider.isInRoleOf("ROLE_INS_PC")
                && !securityInfoProvider.isInRoleOf("ROLE_INS_MNG") && !securityInfoProvider.getIsCHOXAdmin()) {
            throw new AccessDeniedException("Not in correct role to log invoice payment.");
        }
    }

    @Override
    protected void doProcess(Claim claim) {
        Invoice invoice = claim.getInvoice();
        if (finalPayment == null) { // Ok hit - no fields changed. Take values from invoice
            invoice.setHireGrossPaid(invoice.getHireGross());
            invoice.setRepairGrossPaid(invoice.getRepairGross());
            invoice.setEngineerFeeGrossPaid(invoice.getEngineerFeeGross());
            invoice.setTotalLossFeeGrossPaid(invoice.getTotalLossFeeGross());
            invoice.setStorageRecoveryGrossPaid(invoice.getStorageRecoveryGross());
            invoice.setHirePenaltyChargePaid(invoice.getHirePenaltyCharge());
            invoice.setRepairPenaltyChargePaid(invoice.getRepairPenaltyCharge());
            if (invoice.getInterimPaymentMade() != null) {
                invoice.setFinalPayment(invoice.getTotalToPay().subtract(invoice.getInterimPaymentMade()));
            } else {
                invoice.setFinalPayment(invoice.getTotalToPay());
            }
            if (invoice.getTotalPenaltyCharge() != null && invoice.getTotalPenaltyCharge().compareTo(BigDecimal.ZERO) > 0) {
                claim.addComment(Comment.New(0, "A full payment amount of £" + invoice.getFinalPayment() + " has been made."));
                invoice.setPenaltyChargesPaid(Boolean.TRUE);
            } else {
                claim.addComment(Comment.New(0, "A full payment amount of £" + invoice.getFinalPayment() + " has been made."));
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
            invoice.setFinalPayment(finalPayment);
            invoice.setPenaltyChargesPaid(penaltyChargesPaid);
            if (!penaltyChargesPaid && invoice.getTotalPenaltyCharge() != null && invoice.getTotalPenaltyCharge().compareTo(BigDecimal.ZERO) > 0) {
                claim.addComment(Comment.New(0, "A payment amount of £" + finalPayment + " has been made on a total of £" + invoice.getTotalToPay() + " (penalty charges have not been paid)."));
            } else if (total.compareTo(finalPayment) != 0) {
                claim.addComment(Comment.New(0, "A payment amount of £" + finalPayment + " has been made on a total of £" + invoice.getTotalToPay()));
            } else {
                claim.addComment(Comment.New(0, "A full payment amount of £" + finalPayment + " has been made."));
            }
        }
        claim.setStatus(ClaimStatus.INVOICE_PAYMENT_LOGGED);
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.AWAITING_INVOICE_PAYMENT);
    }
}