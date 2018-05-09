package idas.chox.web.scheduler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.PaidInvoiceEntry;
import idas.chox.core.model.SchedulerJob;
import idas.chox.core.services.PaidInvoiceService;
import idas.chox.core.workflow.Activity;
import idas.chox.service.workflow.ActivityFactory;

public class PaidInvoicesSchedulerJob extends DbSchedulerJob {

    private static final Logger LOG = LoggerFactory.getLogger(PaidInvoicesSchedulerJob.class);
    private ActivityFactory activityFactory;
    public static final String JOB_NAME = "PAID_INVOICES";
    private String insurerName;
    @Autowired
    private PaidInvoiceService paidInvoiceService;

    @Override
    public final boolean doJob() {
        try {
            LOG.debug("Checking for paid invoice entries for insurer '{}'...", insurerName);
            List<PaidInvoiceEntry> paidInvoiceEntries = paidInvoiceService.getPaidInvoiceEntries(insurerName);
            LOG.debug("Found {} paid invoice entries", paidInvoiceEntries == null ? "no" : paidInvoiceEntries.size());
            if (paidInvoiceEntries != null && paidInvoiceEntries.size() > 0) {
                // Start new transaction
                handleHibernateTransactionIntricacies();
                paidInvoiceEntries.stream().map((paidInvoiceEntry) -> {
                    // Find corresponding claim
                    try {
                        List<Claim> claims = claimService.getClaimByCHOReferenceAndClaimNumber(paidInvoiceEntry.getChoReference(), paidInvoiceEntry.getClaimNumber());
                        if (claims != null && claims.size()==1) {
                            Claim claim = claims.get(0);
                            LOG.debug("Match found for import entry with cho reference='{}' and claim number='{}' (id={}): ", new Object[]{
                                paidInvoiceEntry.getChoReference(), paidInvoiceEntry.getClaimNumber(), paidInvoiceEntry.getId()});
                            Activity activity;
                            if (ClaimType.isInsurerUpload(claim.getClaimType())) {
                                activity = activityFactory.getActivity("updateManualInvoicePaid");
                            } else {
                                activity = activityFactory.getActivity("invoicePaymentLogged");
                            }
                            activity.process(claim);
                            LOG.debug("Claim with cho reference = '{}' (id={}) processed to a paid state with activity {}", claim.getChoReference(), claim.getId(), activity.getClass().toString());
                        } else if (claims != null && claims.size() > 1) {
                            LOG.warn("Multiple claims found for paid invoice entry with cho reference='{}' and claim number ='{}' (id={}) - ignoring", new Object[]{
                                paidInvoiceEntry.getChoReference(), paidInvoiceEntry.getClaimNumber(), paidInvoiceEntry.getId()});
                        } else if (LOG.isDebugEnabled()) {
                            LOG.debug("No claim found for paid invoice entry with cho reference='{}' and claim number ='{}' (id={})", new Object[]{
                                paidInvoiceEntry.getChoReference(), paidInvoiceEntry.getClaimNumber(), paidInvoiceEntry.getId()});
                        }
                    } catch (Exception ex) {
                        LOG.error("Exception thrown trying to process paid invoice claim with cho reference='{}' and claim number='{}' (id={}): ", new Object[]{
                            paidInvoiceEntry.getChoReference(), paidInvoiceEntry.getClaimNumber(), paidInvoiceEntry.getId(), ex});
                    }
                    return paidInvoiceEntry;
                }).map((paidInvoiceEntry) -> {
                    // Delete paid invoice entry
                    claimService.delete(paidInvoiceEntry);
                    return paidInvoiceEntry;
                }).forEachOrdered((paidInvoiceEntry) -> {
                    LOG.debug("Paid invoice entry with cho reference='{}' and claim number ='{}' (id={}) removed.", new Object[]{
                        paidInvoiceEntry.getChoReference(), paidInvoiceEntry.getClaimNumber(), paidInvoiceEntry.getId()});
                });
                releaseHibernateSessionConditionally();
            }
        } catch (Exception ex) {
            LOG.error("Exception thrown checking for paid invoices: {}", ex.getMessage(), ex);
        }

        return true;
    }

    @Override
    protected List<SchedulerJob> getSchedulerJobs() {
        return getSchedulerJobService().getSchedulerJobs(JOB_NAME);
    }

    public void setInsurerName(String insurerName) {
        this.insurerName = insurerName;
    }

    public void setPaidInvoiceService(PaidInvoiceService paidInvoiceService) {
        this.paidInvoiceService = paidInvoiceService;
    }

    public void setActivityFactory(ActivityFactory activityFactory) {
        this.activityFactory = activityFactory;
    }
}
