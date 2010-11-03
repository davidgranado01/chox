package idas.chox.web.actions;

import idas.chox.core.model.Invoice;
import idas.chox.service.security.ApplicationAccessibility;
import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvoiceAction extends ClaimModelAction<Invoice> {
    private static final Logger LOG = LoggerFactory.getLogger(InvoiceAction.class);
    private String daysWithCHOForReview = null;

    @Override
    public Invoice loadModel() {

        Invoice invoice = claim.getInvoice();
        if (invoice != null) {
            return invoice;
        }
        return new Invoice();
    }

    @Override
    public String updateModel() {
        claim.setInvoice(model);
        claim.updateLiabilityPayment();
   //     updateLiabilityPayment(claim);
        return super.updateModel();
    }

    @Override
    String getTabName() {
        return ApplicationAccessibility.TAB_INVOICE_DETAIL;
    }

    public String getDaysWithCHOForReview() {
        LOG.debug("Getting number of days claim was with CHO for review");
        if (daysWithCHOForReview == null)
            daysWithCHOForReview = claimService.getDaysWithCHOForReview(claim.getId());
        return daysWithCHOForReview;
    }

    public String getDaysWithInsurerForReview() {
        LOG.debug("Getting number of days claim was with Insurer for review");
        BigDecimal invoicedDays = new BigDecimal(loadModel().getInvoicedDays());
        BigDecimal daysWithCHO = new BigDecimal(getDaysWithCHOForReview());

        LOG.debug("invoicedDays={}, daysWithCHO={}", invoicedDays, daysWithCHO);
        BigDecimal daysWithInsurer = invoicedDays.subtract(daysWithCHO);
        LOG.debug("Returning {}", daysWithInsurer);
        return daysWithInsurer.toString();
    }

/*
    public void updateLiabilityPayment(Claim claim){
        LiabilityStatus l = claim.getLiabilityStatus();
        if ( l != null && l.equals(LiabilityStatus.LIABILITY_SPLIT) ){
            BigDecimal ttp = claim.getInvoice().getFullTotalToPay();
            BigDecimal insper = claim.getPercentageLiabilityAccepted();
            claim.getInvoice().setTotalToPay(ttp.multiply(insper).divide(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP));
        }
    }
 *
 */
}
