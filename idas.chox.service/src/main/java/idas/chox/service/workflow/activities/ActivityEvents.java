package idas.chox.service.workflow.activities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;

import idas.chox.core.model.Claim;
import idas.chox.events.ChoxEventRegister;
import idas.chox.events.EventRegister;

/**
 *
 * @author John
 */
public class ActivityEvents {

    private static Logger LOG = LoggerFactory.getLogger(ActivityEvents.class);
    private EventRegister choxEventRegister;

    public void setChoxEventRegister(EventRegister choxEventRegister) {
        this.choxEventRegister = choxEventRegister;
    }

    // Utility function
    public void startEvent(Claim claim, String name) throws Exception {
        int claimId = -1;
        if (claim.getId() != null) {
            claimId = claim.getId().intValue();
        }
        choxEventRegister.startEvent(name, claim.getInsurer().getId().intValue(), claim.getChorganisation().getId().intValue(), claimId, claim.getClaimType().ordinal());
        choxEventRegister.addParameter("insurerName", claim.getInsurer().getName());
        choxEventRegister.addParameter("choName", claim.getChorganisation().getName());
        choxEventRegister.addParameter("choReference", claim.getChoReference());
        choxEventRegister.addParameter("claimNumber", claim.getClaimNumber());
    }


    public void completeEvent(Claim claim) throws Exception {
        choxEventRegister.addParameter("claimStatus", claim.getStatus());
        choxEventRegister.addParameter("claimType", claim.getClaimType().toString());
        choxEventRegister.completeEvent();
    }
    

    public void generate(final Claim claim, final BaseActivity activity) {
//        activity.generateEvents(claim);
        
        String activityName = AopUtils.getTargetClass(activity).getSimpleName();
        LOG.info("Generating events for activity {}", activityName);

        try {
            if (activityName.equalsIgnoreCase("AcknowledgeClaim")) {
                LOG.debug("AcknowledgeClaim activity found");
                generateAcknowledgeClaimEvents(claim, (AcknowledgeClaim) activity);
            } else if (activityName.equalsIgnoreCase("AssignManualInvoiceOwner")) {
                LOG.debug("AssignManualInvoiceOwner activity found");
                generateAssignManualInvoiceOwnerEvents(claim, (AssignManualInvoiceOwner) activity);
            } else if (activityName.equalsIgnoreCase("AssignOwner")) {
                LOG.debug("AssignOwner activity found");
                generateAssignOwnerEvents(claim, (AssignOwner) activity);
            } else if (activityName.equalsIgnoreCase("AssignSupplierOwner")) {
                LOG.debug("AssignSupplierOwner activity found");
                generateAssignSupplierOwnerEvents(claim, (AssignSupplierOwner) activity);
            } else if (activityName.equalsIgnoreCase("AssignWorkgroup")) {
                LOG.debug("AssignWorkgroup activity found");
                generateAssignWorkgroupEvents(claim, (AssignWorkgroup) activity);
            } else if (activityName.equalsIgnoreCase("AwaitingLitigationOutcome")) {
                LOG.debug("AwaitingLitigationOutcome activity found");
                generateAwaitingLitigationOutcomeEvents(claim, (AwaitingLitigationOutcome) activity);
            } else if (activityName.equalsIgnoreCase("ClaimAwaitingCarHireInfo")) {
                LOG.debug("ClaimAwaitingCarHireInfo activity found");
                generateClaimAwaitingCarHireInfoEvents(claim, (ClaimAwaitingCarHireInfo) activity);
            } else if (activityName.equalsIgnoreCase("ClaimPending")) {
                LOG.debug("ClaimPending activity found");
                generateClaimPendingEvents(claim, (ClaimPending) activity);
            } else if (activityName.equalsIgnoreCase("ClaimReferToEng")) {
                LOG.debug("ClaimReferrToEng activity found");
                generateClaimReferToEngEvents(claim, (ClaimReferToEng) activity);
            } else if (activityName.equalsIgnoreCase("ClaimReferToFnol")) {
                LOG.debug("ClaimReferToFnol activity found");
                generateClaimReferToFnolEvents(claim, (ClaimReferToFnol) activity);
            } else if (activityName.equalsIgnoreCase("ClaimRegisterByFnol")) {
                LOG.debug(" activity found");
                generateClaimRegisterByFnolEvents(claim, (ClaimRegisterByFnol) activity);
            } else if (activityName.equalsIgnoreCase("ClaimRejection")) {
                LOG.debug("ClaimRejection activity found");
                generateClaimRejectionEvents(claim, (ClaimRejection) activity);
            } else if (activityName.equalsIgnoreCase("ClaimRejectionAccept")) {
                LOG.debug("ClaimRejectionAccept activity found");
                generateClaimRejectionAcceptEvents(claim, (ClaimRejectionAccept) activity);
            } else if (activityName.equalsIgnoreCase("ClaimRejectionContest")) {
                LOG.debug("ClaimRejectionContest activity found");
                generateClaimRejectionContestEvents(claim, (ClaimRejectionContest) activity);
            } else if (activityName.equalsIgnoreCase("ClaimReviewByEng")) {
                LOG.debug("ClaimReviewByEng activity found");
                generateClaimReviewByEngEvents(claim, (ClaimReviewByEng) activity);
            } else if (activityName.equalsIgnoreCase("CloseClaim")) {
                LOG.debug("CloseClaim activity found");
                generateCloseClaimEvents(claim, (CloseClaim) activity);
            } else if (activityName.equalsIgnoreCase("EcdUpdate")) {
                LOG.debug("EcdUpdate activity found");
                generateEcdUpdateEvents(claim, (EcdUpdate) activity);
            } else if (activityName.equalsIgnoreCase("FullInvoicePaymentReceived")) {
                LOG.debug("FullInvoicePaymentReceived activity found");
                generateFullInvoicePaymentReceivedEvents(claim, (FullInvoicePaymentReceived) activity);
            } else if (activityName.equalsIgnoreCase("FullPaymentNotReceived")) {
                LOG.debug("FullPaymentNotReceived activity found");
                generateFullPaymentNotReceivedEvents(claim, (FullPaymentNotReceived) activity);
            } else if (activityName.equalsIgnoreCase("InsurerUpload")) {
                LOG.debug("InsurerUpload activity found");
                generateInsurerUploadEvents(claim, (InsurerUpload) activity);
            } else if (activityName.equalsIgnoreCase("InvoiceAccepted")) {
                LOG.debug("InvoiceAccepted activity found");
                generateInvoiceAcceptedEvents(claim, (InvoiceAccepted) activity);
            } else if (activityName.equalsIgnoreCase("InvoicePaymentLogged")) {
                LOG.debug("InvoicePaymentLogged activity found");
                generateInvoicePaymentLoggedEvents(claim, (InvoicePaymentLogged) activity);
            } else if (activityName.equalsIgnoreCase("InvoicePaymentReceived")) {
                LOG.debug("InvoicePaymentReceived activity found");
                generateInvoicePaymentReceivedEvents(claim, (InvoicePaymentReceived) activity);
            } else if (activityName.equalsIgnoreCase("InvoiceReferToCH")) {
                LOG.debug("InvoiceReferToCH activity found");
                generateInvoiceReferToCHEvents(claim, (InvoiceReferToCH) activity);
            } else if (activityName.equalsIgnoreCase("InvoiceReferToEng")) {
                LOG.debug("InvoiceReferToEng activity found");
                generateInvoiceReferToEngEvents(claim, (InvoiceReferToEng) activity);
            } else if (activityName.equalsIgnoreCase("InvoiceRejection")) {
                LOG.debug("InvoiceRejection activity found");
                generateInvoiceRejectionEvents(claim, (InvoiceRejection) activity);
            } else if (activityName.equalsIgnoreCase("InvoiceRejectionAccept")) {
                LOG.debug("InvoiceRejectionAccept activity found");
                generateInvoiceRejectionAcceptEvents(claim, (InvoiceRejectionAccept) activity);
            } else if (activityName.equalsIgnoreCase("InvoiceRejectionContest")) {
                LOG.debug("InvoiceRejectionContest activity found");
                generateInvoiceRejectionContestEvents(claim, (InvoiceRejectionContest) activity);
            } else if (activityName.equalsIgnoreCase("InvoiceResubmit")) {
                LOG.debug("InvoiceResubmit activity found");
                generateInvoiceResubmitEvents(claim, (InvoiceResubmit) activity);
            } else if (activityName.equalsIgnoreCase("MakeInterimPayment")) {
                LOG.debug("MakeInterimPayment activity found");
                generateMakeInterimPaymentEvents(claim, (MakeInterimPayment) activity);
            } else if (activityName.equalsIgnoreCase("MoveToInvoicePaymentLogged")) {
                LOG.debug("MoveToInvoicePaymentLogged activity found");
                generateMoveToInvoicePaymentLoggedEvents(claim, (MoveToInvoicePaymentLogged) activity);
            } else if (activityName.equalsIgnoreCase("NewClaim")) {
                LOG.debug("NewClaim activity found");
                generateNewClaimEvents(claim, (NewClaim) activity);
            } else if (activityName.equalsIgnoreCase("NewInvoice")) {
                LOG.debug("NewInvoice activity found");
                generateNewInvoiceEvents(claim, (NewInvoice) activity);
            } else if (activityName.equalsIgnoreCase("NewSupplementaryInvoice")) {
                LOG.debug("NewSupplementaryInvoice activity found");
                generateNewSupplementaryInvoiceEvents(claim, (NewSupplementaryInvoice) activity);
            } else if (activityName.equalsIgnoreCase("NewTpiClaim")) {
                LOG.debug("NewTpiClaim activity found");
                generateNewTpiClaimEvents(claim, (NewTpiClaim) activity);
            } else if (activityName.equalsIgnoreCase("PaymentNotReceived")) {
                LOG.debug("PaymentNotReceived activity found");
                generatePaymentNotReceivedEvents(claim, (PaymentNotReceived) activity);
            } else if (activityName.equalsIgnoreCase("ReopenClaim")) {
                LOG.debug("ReopenClaim activity found");
                generateReopenClaimEvents(claim, (ReopenClaim) activity);
            } else if (activityName.equalsIgnoreCase("ResolveLiability")) {
                LOG.debug("ResolveLiability activity found");
                generateResolveLiabilityEvents(claim, (ResolveLiability) activity);
            } else if (activityName.equalsIgnoreCase("RevertClaim")) {
                LOG.debug("RevertClaim activity found");
                generateRevertClaimEvents(claim, (RevertClaim) activity);
            } else if (activityName.equalsIgnoreCase("SlaExtension")) {
                LOG.debug("SlaExtension activity found");
                generateSlaExtensionEvents(claim, (SlaExtension) activity);
            } else if (activityName.equalsIgnoreCase("SubscriberClaimRejectionAccept")) {
                LOG.debug("SubscriberClaimRejectionAccept activity found");
                generateSubscriberClaimRejectionAcceptEvents(claim, (SubscriberClaimRejectionAccept) activity);
            } else if (activityName.equalsIgnoreCase("SubscriberClaimToGta")) {
                LOG.debug("SubscriberClaimToGta activity found");
                generateSubscriberClaimToGtaEvents(claim, (SubscriberClaimToGta) activity);
            } else if (activityName.equalsIgnoreCase("SwitchClaim")) {
                LOG.debug("SwitchClaim activity found");
                generateSwitchClaimEvents(claim, (SwitchClaim) activity);
            } else if (activityName.equalsIgnoreCase("SwitchClaimToMultipleInsurer")) {
                LOG.debug("SwitchClaimToMultipleInsurer activity found");
                generateSwitchClaimToMultipleInsurerEvents(claim, (SwitchClaimToMultipleInsurer) activity);
            } else if (activityName.equalsIgnoreCase("UpdateInterimPaymentFullAndFinal")) {
                LOG.debug("UpdateInterimPaymentFullAndFinal activity found");
                generateUpdateInterimPaymentFullAndFinalEvents(claim, (UpdateInterimPaymentFullAndFinal) activity);
            } else if (activityName.equalsIgnoreCase("UpdateInterimPaymentReceived")) {
                LOG.debug("UpdateInterimPaymentReceived activity found");
                generateUpdateInterimPaymentReceivedEvents(claim, (UpdateInterimPaymentReceived) activity);
            } else if (activityName.equalsIgnoreCase("UpdateLiability")) {
                LOG.debug("UpdateLiability activity found");
                generateUpdateLiabilityEvents(claim, (UpdateLiability) activity);
            } else if (activityName.equalsIgnoreCase("UpdateManualInvoiceContested")) {
                LOG.debug("UpdateManualInvoicecontested activity found");
                generateUpdateManualInvoiceContestedEvents(claim, (UpdateManualInvoiceContested) activity);
            } else if (activityName.equalsIgnoreCase("UpdateManualInvoicePaid")) {
                LOG.debug("UpdateManualInvoicePaid activity found");
                generateUpdateManualInvoicePaidEvents(claim, (UpdateManualInvoicePaid) activity);
            } else if (activityName.equalsIgnoreCase("WorkgroupRouting")) {
                LOG.debug("WorkgroupRouting activity found");
                generateWorkgroupRoutingEvents(claim, (WorkgroupRouting) activity);
            } else {
                LOG.error("Activity not found");
            }
        } catch (Exception ex) {
            LOG.error("Error generating events for activity '{}' : {}", activityName, ex.getMessage());
        }
        try {
            choxEventRegister.sendEvents();
        } catch (Exception ex) {
            LOG.error("Error sending generated events for activity '{}' : {}", activityName, ex.getMessage());
        }
    }

    private void generateAssignWorkgroupEvents(final Claim claim, final AssignWorkgroup activity) throws Exception {
        startEvent(claim, ChoxEventRegister.CLAIM_ROUTED_EVENT);
        choxEventRegister.addParameter("insurerWorkgroupName", activity.getWorkgroup().getName());
        choxEventRegister.addParameter("insurerWorkgroupId", activity.getWorkgroup().getId().intValue());
        completeEvent(claim);
    }

    private void generateAcknowledgeClaimEvents(final Claim claim, final AcknowledgeClaim activity) throws Exception {
        startEvent(claim, ChoxEventRegister.CLAIM_ACKNOWLEDGED_EVENT);
//        choxEventRegister.addParameter("", activity.);
        completeEvent(claim);
    }

    private void generateAssignManualInvoiceOwnerEvents(final Claim claim, final AssignManualInvoiceOwner activity) throws Exception {
        startEvent(claim, ChoxEventRegister.INSURER_OWNER_ASSIGNED_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

    private void generateAssignOwnerEvents(final Claim claim, final AssignOwner activity) throws Exception {
        startEvent(claim, ChoxEventRegister.INSURER_OWNER_ASSIGNED_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

    private void generateAssignSupplierOwnerEvents(final Claim claim, final AssignSupplierOwner activity) throws Exception {
        startEvent(claim, ChoxEventRegister.CHO_OWNER_ASSIGNED_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

    private void generateAwaitingLitigationOutcomeEvents(final Claim claim, final AwaitingLitigationOutcome activity) throws Exception {
        startEvent(claim, ChoxEventRegister.CLAIM_AWAITING_LITIGATION_OUTCOME_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

    private void generateClaimAwaitingCarHireInfoEvents(final Claim claim, final ClaimAwaitingCarHireInfo activity) throws Exception {
        startEvent(claim, ChoxEventRegister.HIRE_CAR_INFO_PROVIDED_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

    private void generateClaimPendingEvents(final Claim claim, final ClaimPending activity) throws Exception {
        startEvent(claim, ChoxEventRegister.CLAIM_PENDING_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

    private void generateClaimReferToEngEvents(final Claim claim, final ClaimReferToEng activity) throws Exception {
        startEvent(claim, ChoxEventRegister.CLAIM_REFERRED_TO_ENG_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

    private void generateClaimReferToFnolEvents(final Claim claim, final ClaimReferToFnol activity) throws Exception {
        startEvent(claim, ChoxEventRegister.CLAIM_REFERRED_TO_FNOL_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

    private void generateClaimRegisterByFnolEvents(final Claim claim, final ClaimRegisterByFnol activity) throws Exception {
        startEvent(claim, ChoxEventRegister.CLAIM_REGISTERED_BY_FNOL_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

    private void generateClaimRejectionEvents(final Claim claim, final ClaimRejection activity) throws Exception {
        startEvent(claim, ChoxEventRegister.CLAIM_REJECTED_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

    private void generateClaimRejectionAcceptEvents(final Claim claim, final ClaimRejectionAccept activity) throws Exception {
        startEvent(claim, ChoxEventRegister.CLAIM_REJECTION_ACCEPTED_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

    private void generateClaimRejectionContestEvents(final Claim claim, final ClaimRejectionContest activity) throws Exception {
        startEvent(claim, ChoxEventRegister.CLAIM_REJECTION_CONTESTED_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

    private void generateClaimReviewByEngEvents(final Claim claim, final ClaimReviewByEng activity) throws Exception {
        startEvent(claim, ChoxEventRegister.CLAIM_REVIEW_BY_ENG_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

    private void generateCloseClaimEvents(final Claim claim, final CloseClaim activity) throws Exception {
        startEvent(claim, ChoxEventRegister.CLAIM_CLOSED_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

    private void generateEcdUpdateEvents(final Claim claim, final EcdUpdate activity) throws Exception {
        startEvent(claim, ChoxEventRegister.ECD_UPDATED_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

    private void generateFullInvoicePaymentReceivedEvents(final Claim claim, final FullInvoicePaymentReceived activity) throws Exception {
        startEvent(claim, ChoxEventRegister.FULL_PAYMENT_RECEIVED_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

    private void generateFullPaymentNotReceivedEvents(final Claim claim, final FullPaymentNotReceived activity) throws Exception {
        startEvent(claim, ChoxEventRegister.FULL_PAYMENT_NOT_RECEIVED_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

    private void generateInsurerUploadEvents(final Claim claim, final InsurerUpload activity) throws Exception {
        startEvent(claim, ChoxEventRegister.NEW_CLAIM_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

    private void generateInvoiceAcceptedEvents(final Claim claim, final InvoiceAccepted activity) throws Exception {
        startEvent(claim, ChoxEventRegister.INVOICE_ACCEPTED_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

    private void generateInvoicePaymentLoggedEvents(final Claim claim, final InvoicePaymentLogged activity) throws Exception {
        startEvent(claim, ChoxEventRegister.INVOICE_PAID_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

    private void generateInvoicePaymentReceivedEvents(final Claim claim, final InvoicePaymentReceived activity) throws Exception {
        startEvent(claim, ChoxEventRegister.INVOICE_PAYMENT_RECEIVED_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

    private void generateInvoiceReferToCHEvents(final Claim claim, final InvoiceReferToCH activity) throws Exception {
        startEvent(claim, ChoxEventRegister.INVOICE_REFERRED_TO_CH_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

    private void generateInvoiceReferToEngEvents(final Claim claim, final InvoiceReferToEng activity) throws Exception {
        startEvent(claim, ChoxEventRegister.INVOICE_REFERRED_TO_ENG_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

    private void generateInvoiceRejectionEvents(final Claim claim, final InvoiceRejection activity) throws Exception {
        startEvent(claim, ChoxEventRegister.INVOICE_REJECTED_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

    private void generateInvoiceRejectionAcceptEvents(final Claim claim, final InvoiceRejectionAccept activity) throws Exception {
        startEvent(claim, ChoxEventRegister.INVOICE_REJECTION_ACCEPTED_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

    private void generateInvoiceRejectionContestEvents(final Claim claim, final InvoiceRejectionContest activity) throws Exception {
        startEvent(claim, ChoxEventRegister.INVOICE_REJECTION_CONTESTED_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

    private void generateInvoiceResubmitEvents(final Claim claim, final InvoiceResubmit activity) throws Exception {
        startEvent(claim, ChoxEventRegister.INVOICE_RESUBMITTED_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

    private void generateMakeInterimPaymentEvents(final Claim claim, final MakeInterimPayment activity) throws Exception {
        startEvent(claim, ChoxEventRegister.INTERIM_PAYMENT_UPDATED_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

    private void generateMoveToInvoicePaymentLoggedEvents(final Claim claim, final MoveToInvoicePaymentLogged activity) throws Exception {
        startEvent(claim, ChoxEventRegister.INVOICE_PAID_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

    private void generateNewClaimEvents(final Claim claim, final NewClaim activity) throws Exception {
        startEvent(claim, ChoxEventRegister.NEW_CLAIM_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

    private void generateNewInvoiceEvents(final Claim claim, final NewInvoice activity) throws Exception {
        startEvent(claim, ChoxEventRegister.INVOICE_SUBMITTED_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

    private void generateNewSupplementaryInvoiceEvents(final Claim claim, final NewSupplementaryInvoice activity) throws Exception {
        startEvent(claim, ChoxEventRegister.NEW_CLAIM_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

    private void generateNewTpiClaimEvents(final Claim claim, final NewTpiClaim activity) throws Exception {
        startEvent(claim, ChoxEventRegister.NEW_CLAIM_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

    private void generatePaymentNotReceivedEvents(final Claim claim, final PaymentNotReceived activity) throws Exception {
        startEvent(claim, ChoxEventRegister.FULL_PAYMENT_NOT_RECEIVED_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

    private void generateReopenClaimEvents(final Claim claim, final ReopenClaim activity) throws Exception {
        startEvent(claim, ChoxEventRegister.CLAIM_REVERTED_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

    private void generateResolveLiabilityEvents(final Claim claim, final ResolveLiability activity) throws Exception {
        startEvent(claim, ChoxEventRegister.LIABILITY_UPDATED_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

    private void generateRevertClaimEvents(final Claim claim, final RevertClaim activity) throws Exception {
        startEvent(claim, ChoxEventRegister.CLAIM_REVERTED_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

    private void generateSlaExtensionEvents(final Claim claim, final SlaExtension activity) throws Exception {
        startEvent(claim, ChoxEventRegister.SLA_EXTENSION_GRANTED_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

    private void generateSubscriberClaimRejectionAcceptEvents(final Claim claim, final SubscriberClaimRejectionAccept activity) throws Exception {
        startEvent(claim, ChoxEventRegister.CLAIM_REJECTION_ACCEPTED_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

    private void generateSubscriberClaimToGtaEvents(final Claim claim, final SubscriberClaimToGta activity) throws Exception {
        startEvent(claim, ChoxEventRegister.CLAIM_SWITCHED_TO_GTA_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

    private void generateSwitchClaimEvents(final Claim claim, final SwitchClaim activity) throws Exception {
        startEvent(claim, ChoxEventRegister.CLAIM_CLOSED_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

    private void generateUpdateInterimPaymentFullAndFinalEvents(final Claim claim, final UpdateInterimPaymentFullAndFinal activity) throws Exception {
        startEvent(claim, ChoxEventRegister.INTERIM_PAYMENT_ACCEPTED_AS_FINAL_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

    private void generateSwitchClaimToMultipleInsurerEvents(final Claim claim, final SwitchClaimToMultipleInsurer activity) throws Exception {
        startEvent(claim, ChoxEventRegister.CLAIM_CLOSED_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

    private void generateUpdateInterimPaymentReceivedEvents(final Claim claim, final UpdateInterimPaymentReceived activity) throws Exception {
        startEvent(claim, ChoxEventRegister.INTERIM_PAYMENT_RECEIVED_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

    private void generateUpdateLiabilityEvents(final Claim claim, final UpdateLiability activity) throws Exception {
        startEvent(claim, ChoxEventRegister.LIABILITY_UPDATED_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

    private void generateUpdateManualInvoiceContestedEvents(final Claim claim, final UpdateManualInvoiceContested activity) throws Exception {
        startEvent(claim, ChoxEventRegister.INVOICE_REJECTED_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

    private void generateUpdateManualInvoicePaidEvents(final Claim claim, final UpdateManualInvoicePaid activity) throws Exception {
        startEvent(claim, ChoxEventRegister.INVOICE_PAID_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }
    private void generateWorkgroupRoutingEvents(final Claim claim, final WorkgroupRouting activity) throws Exception {
        startEvent(claim, ChoxEventRegister.CLAIM_ROUTED_EVENT);
//        choxEventRegister.addParameter("", activity.get);
        completeEvent(claim);
    }

}
