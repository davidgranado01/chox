package idas.chox.service.workflow.activities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;

import idas.chox.core.model.Attachment;
import idas.chox.core.model.Claim;
import idas.chox.core.workflow.Activity;
import idas.chox.data.events.EventGenerator;

/**
 *
 * @author John
 */
public class ActivityEventGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(ActivityEventGenerator.class);
    private EventGenerator eventGenerator;

    public void setEventGenerator(EventGenerator eventGenerator) {
        this.eventGenerator = eventGenerator;
    }

    // Utility function
    public void startEvent(Claim claim, String name, int id, boolean insurerOnly, boolean choOnly) throws Exception {
        eventGenerator.startEvent(claim, name, id, insurerOnly, choOnly);
    }
    public void startEvent(Claim claim, String name, int id) throws Exception {
        startEvent(claim, name, id, false, false);
    }
    public void addParameter(String name, Object value) {
        eventGenerator.addParameter(name, value);
    }
    
    public void completeEvent(Claim claim) throws Exception {
        eventGenerator.completeEvent(claim);
    }
    

    public void generate(final Claim claim, ActivityEvent event) {
        try {
            event.build(this, claim);
        } catch (Exception ex) {
            LOG.error("Error generating events for event '{}' : {}\n", new Object[]{event, ex.getMessage(), ex});
            return;
        }

        try {
            eventGenerator.sendEvents();
        } catch (Exception ex) {
            LOG.error("Error sending generated events for activity '{}' : {}", event, ex.getMessage());
        }
    }

//    public void generate(final Claim claim, final Comment comment, ActivityEvent event) {
//        try {
//            event.build(this, claim, comment);
//        } catch (Exception ex) {
//            LOG.error("Error generating events for event '{}' : {}\n", new Object[]{event, ex.getMessage(), ex});
//            return;
//        }
//
//        try {
//            eventGenerator.sendEvents();
//        } catch (Exception ex) {
//            LOG.error("Error sending generated events for activity '{}' : {}", event, ex.getMessage());
//        }
//    }

    public void generate(final Claim claim, final Attachment attachment, ActivityEvent event) {
        try {
            event.build(this, claim, attachment);
        } catch (Exception ex) {
            LOG.error("Error generating events for event '{}' : {}\n", new Object[]{event, ex.getMessage(), ex});
            return;
        }

        try {
            eventGenerator.sendEvents();
        } catch (Exception ex) {
            LOG.error("Error sending generated events for activity '{}' : {}", event, ex.getMessage());
        }
    }

    public void generate(final Claim claim, Activity activity) {
//        activity.generateEvents(claim);
        
        String activityName = AopUtils.getTargetClass(activity).getSimpleName();
        LOG.debug("Generating events for activity {}", activityName);

        try {
            if (activityName.equalsIgnoreCase("AcknowledgeClaim")) {
                LOG.debug("AcknowledgeClaim activity found");
                if (((AcknowledgeClaim) activity).liabilityUpdated) {
                    ActivityEvent.LIABILITY_UPDATED_EVENT.build(this, (AcknowledgeClaim) activity, claim);
                }
                if (((AcknowledgeClaim) activity).claimNumberUpdated) {
                    ActivityEvent.CLAIM_NUMBER_ASSIGNED_EVENT.build(this, claim);
                }
                ActivityEvent.CLAIM_ACKNOWLEDGED_EVENT.build(this, (AcknowledgeClaim) activity, claim);
            }  else if (activityName.equalsIgnoreCase("AddNote")) {
                LOG.debug("AddNote activity found");
                ActivityEvent.NOTE_ADDED_EVENT.build(this, (AddNote) activity, claim);
            } else if (activityName.equalsIgnoreCase("AssignManualInvoiceOwner")) {
                LOG.debug("AssignManualInvoiceOwner activity found");
                ActivityEvent.INSURER_OWNER_ASSIGNED_EVENT.build(this, (AssignManualInvoiceOwner) activity, claim);
            } else if (activityName.equalsIgnoreCase("AssignOwner")) {
                LOG.debug("AssignOwner activity found");
                ActivityEvent.INSURER_OWNER_ASSIGNED_EVENT.build(this, (AssignOwner) activity, claim);
            } else if (activityName.equalsIgnoreCase("AssignSupplierOwner")) {
                LOG.debug("AssignSupplierOwner activity found");
                ActivityEvent.CHO_OWNER_ASSIGNED_EVENT.build(this, (AssignSupplierOwner) activity, claim);
            } else if (activityName.equalsIgnoreCase("AssignWorkgroup")) {
                LOG.debug("AssignWorkgroup activity found");
                ActivityEvent.CLAIM_ROUTED_EVENT.build(this, (AssignWorkgroup) activity, claim);
            } else if (activityName.equalsIgnoreCase("AwaitingLitigationOutcome")) {
                LOG.debug("AwaitingLitigationOutcome activity found");
                ActivityEvent.CLAIM_AWAITING_LITIGATION_OUTCOME_EVENT.build(this, (AwaitingLitigationOutcome) activity, claim);
            } else if (activityName.equalsIgnoreCase("ClaimAwaitingCarHireInfo")) {
                LOG.debug("ClaimAwaitingCarHireInfo activity found");
                ActivityEvent.HIRE_CAR_INFO_PROVIDED_EVENT.build(this, (ClaimAwaitingCarHireInfo) activity, claim);
            } else if (activityName.equalsIgnoreCase("ClaimPending")) {
                LOG.debug("ClaimPending activity found");
                if (((ClaimPending) activity).liabilityUpdated) {
                    ActivityEvent.LIABILITY_UPDATED_EVENT.build(this, (ClaimPending) activity, claim);
                }
                if (((ClaimPending) activity).claimNumberUpdated) {
                    ActivityEvent.CLAIM_NUMBER_ASSIGNED_EVENT.build(this, claim);
                }
                ActivityEvent.CLAIM_PENDING_EVENT.build(this, (ClaimPending) activity, claim);
            } else if (activityName.equalsIgnoreCase("ClaimReferToEng")) {
                LOG.debug("ClaimReferToEng activity found");
                if (((ClaimReferToEng) activity).liabilityUpdated) {
                    ActivityEvent.LIABILITY_UPDATED_EVENT.build(this, (ClaimReferToEng) activity, claim);
                }
                if (((ClaimReferToEng) activity).claimNumberUpdated) {
                    ActivityEvent.CLAIM_NUMBER_ASSIGNED_EVENT.build(this, claim);
                }
                ActivityEvent.CLAIM_REFERRED_TO_ENG_EVENT.build(this, (ClaimReferToEng) activity, claim);
            } else if (activityName.equalsIgnoreCase("ClaimReferToFnol")) {
                LOG.debug("ClaimReferToFnol activity found");
                if (((ClaimReferToFnol) activity).claimRouted) {
                    ActivityEvent.CLAIM_ROUTED_EVENT.build(this, (ClaimReferToFnol) activity, claim);
                }
                if (((ClaimReferToFnol) activity).ownerAssigned) {
                    ActivityEvent.INSURER_OWNER_ASSIGNED_EVENT.build(this, (ClaimReferToFnol) activity, claim);
                }
                if (((ClaimReferToFnol) activity).liabilityUpdated) {
                    ActivityEvent.LIABILITY_UPDATED_EVENT.build(this, (ClaimReferToFnol) activity, claim);
                }
                if (((ClaimReferToFnol) activity).claimNumberUpdated) {
                    ActivityEvent.CLAIM_NUMBER_ASSIGNED_EVENT.build(this, claim);
                }
                ActivityEvent.CLAIM_REFERRED_TO_FNOL_EVENT.build(this, (ClaimReferToFnol) activity, claim);
            } else if (activityName.equalsIgnoreCase("ClaimRegisterByFnol")) {
                LOG.debug("ClaimRegisterByFnol activity found");
                if (((ClaimRegisterByFnol) activity).claimNumberUpdated) {
                    ActivityEvent.CLAIM_NUMBER_ASSIGNED_EVENT.build(this, (ClaimRegisterByFnol) activity, claim);
                }
                ActivityEvent.CLAIM_REGISTERED_BY_FNOL_EVENT.build(this, (ClaimRegisterByFnol) activity, claim);
            } else if (activityName.equalsIgnoreCase("ClaimRejection")) {
                LOG.debug("ClaimRejection activity found");
                if (((ClaimRejection) activity).liabilityUpdated) {
                    ActivityEvent.LIABILITY_UPDATED_EVENT.build(this, (ClaimRejection) activity, claim);
                }
                if (((ClaimRejection) activity).claimNumberUpdated) {
                    ActivityEvent.CLAIM_NUMBER_ASSIGNED_EVENT.build(this, claim);
                }
                ActivityEvent.CLAIM_REJECTED_EVENT.build(this, (ClaimRejection) activity, claim);
            } else if (activityName.equalsIgnoreCase("ClaimRejectionAccept")) {
                LOG.debug("ClaimRejectionAccept activity found");
                ActivityEvent.CLAIM_REJECTION_ACCEPTED_EVENT.build(this, (ClaimRejectionAccept) activity, claim);
            } else if (activityName.equalsIgnoreCase("ClaimRejectionContest")) {
                LOG.debug("ClaimRejectionContest activity found");
                ActivityEvent.CLAIM_REJECTION_CONTESTED_EVENT.build(this, (ClaimRejectionContest) activity, claim);
            } else if (activityName.equalsIgnoreCase("ClaimReviewByEng")) {
                LOG.debug("ClaimReviewByEng activity found");
                ActivityEvent.CLAIM_REVIEW_BY_ENG_EVENT.build(this, (ClaimReviewByEng) activity, claim);
            } else if (activityName.equalsIgnoreCase("CloseClaim")) {
                LOG.debug("CloseClaim activity found");
                ActivityEvent.CLAIM_CLOSED_EVENT.build(this, (CloseClaim) activity, claim);
            } else if (activityName.equalsIgnoreCase("EcdUpdate")) {
                LOG.debug("EcdUpdate activity found");
                ActivityEvent.ECD_UPDATED_EVENT.build(this, (EcdUpdate) activity, claim);
            }  else if (activityName.equalsIgnoreCase("HireUpdate")) {
                LOG.debug("HireUpdate activity found");
                ActivityEvent.HIRE_VEHICLE_UPDATED_EVENT.build(this, (HireUpdate) activity, claim);
            } else if (activityName.equalsIgnoreCase("FullInvoicePaymentReceived")) {
                LOG.debug("FullInvoicePaymentReceived activity found");
                ActivityEvent.FULL_PAYMENT_RECEIVED_EVENT.build(this, (FullInvoicePaymentReceived) activity, claim);
            } else if (activityName.equalsIgnoreCase("FullPaymentNotReceived")) {
                LOG.debug("FullPaymentNotReceived activity found");
                ActivityEvent.FULL_PAYMENT_NOT_RECEIVED_EVENT.build(this, (FullPaymentNotReceived) activity, claim);
            } else if (activityName.equalsIgnoreCase("InsurerUpload")) {
                LOG.debug("InsurerUpload activity found");
//                ActivityEvent.NEW_CLAIM_EVENT.build(this, (InsurerUpload) activity, claim);
                if (((InsurerUpload) activity).claimRouted) {
                    ActivityEvent.CLAIM_ROUTED_EVENT.build(this, (InsurerUpload) activity, claim);
                }
                if (((InsurerUpload) activity).claimOwnerAssigned) {
                    ActivityEvent.INSURER_OWNER_ASSIGNED_EVENT.build(this, (InsurerUpload) activity, claim);
                }
                ActivityEvent.INVOICE_UPLOADED_EVENT.build(this, (InsurerUpload) activity, claim);
                ActivityEvent.INVOICE_SUBMITTED_EVENT.build(this, (InsurerUpload) activity, claim);
            } else if (activityName.equalsIgnoreCase("InvoiceAccepted")) {
                LOG.debug("InvoiceAccepted activity found");
                ActivityEvent.INVOICE_ACCEPTED_EVENT.build(this, (InvoiceAccepted) activity, claim);
            }  else if (activityName.equalsIgnoreCase("UpdateManualInvoiceAgreeQuantum")) {
                LOG.debug("UpdateManualInvoiceAgreeQuantum activity found");
                ActivityEvent.INVOICE_ACCEPTED_EVENT.build(this, (UpdateManualInvoiceAgreeQuantum) activity, claim);
            } else if (activityName.equalsIgnoreCase("InvoicePaymentLogged")) {
                LOG.debug("InvoicePaymentLogged activity found");
                ActivityEvent.INVOICE_PAID_EVENT.build(this, (InvoicePaymentLogged) activity, claim);
            } else if (activityName.equalsIgnoreCase("InvoicePaymentReceived")) {
                LOG.debug("InvoicePaymentReceived activity found");
                ActivityEvent.INVOICE_PAYMENT_RECEIVED_EVENT.build(this, (InvoicePaymentReceived) activity, claim);
            } else if (activityName.equalsIgnoreCase("InvoiceReferToCH")) {
                LOG.debug("InvoiceReferToCH activity found");
                ActivityEvent.INVOICE_REFERRED_TO_CH_EVENT.build(this, (InvoiceReferToCH) activity, claim);
            } else if (activityName.equalsIgnoreCase("InvoiceReferToEng")) {
                LOG.debug("InvoiceReferToEng activity found");
                ActivityEvent.INVOICE_REFERRED_TO_ENG_EVENT.build(this, (InvoiceReferToEng) activity, claim);
            } else if (activityName.equalsIgnoreCase("InvoiceRejection")) {
                LOG.debug("InvoiceRejection activity found");
                ActivityEvent.INVOICE_REJECTED_EVENT.build(this, (InvoiceRejection) activity, claim);
            } else if (activityName.equalsIgnoreCase("InvoiceRejectionAccept")) {
                LOG.debug("InvoiceRejectionAccept activity found");
                ActivityEvent.INVOICE_REJECTION_ACCEPTED_EVENT.build(this, (InvoiceRejectionAccept) activity, claim);
            } else if (activityName.equalsIgnoreCase("InvoiceRejectionContest")) {
                LOG.debug("InvoiceRejectionContest activity found");
                ActivityEvent.INVOICE_REJECTION_CONTESTED_EVENT.build(this, (InvoiceRejectionContest) activity, claim);
                ActivityEvent.INVOICE_SUBMITTED_EVENT.build(this, (InvoiceRejectionContest) activity, claim);
            } else if (activityName.equalsIgnoreCase("InvoiceResubmit")) {
                LOG.debug("InvoiceResubmit activity found");
                if (((InvoiceResubmit) activity).claimOwnerAssigned) {
                    ActivityEvent.CLAIM_ROUTED_EVENT.build(this, (InvoiceResubmit) activity, claim);
                }
                if (((InvoiceResubmit) activity).claimRouted) {
                    ActivityEvent.INSURER_OWNER_ASSIGNED_EVENT.build(this, (InvoiceResubmit) activity, claim);
                }
                ActivityEvent.INVOICE_SUBMITTED_EVENT.build(this, (InvoiceResubmit) activity, claim);
                if (((InvoiceResubmit) activity).invoiceAccepted) {
                    ActivityEvent.INVOICE_ACCEPTED_EVENT.build(this, (InvoiceResubmit) activity, claim);
                }
            } else if (activityName.equalsIgnoreCase("MakeInterimPayment")) {
                LOG.debug("MakeInterimPayment activity found");
                ActivityEvent.INTERIM_PAYMENT_UPDATED_EVENT.build(this, (MakeInterimPayment) activity, claim);
            } else if (activityName.equalsIgnoreCase("UpdateInterimPaymentReceived")) {
                LOG.debug("UpdateInterimPaymentReceived activity found");
                ActivityEvent.INTERIM_PAYMENT_RECEIVED_EVENT.build(this, (UpdateInterimPaymentReceived) activity, claim);
            } else if (activityName.equalsIgnoreCase("UpdateInterimPaymentFullAndFinal")) {
                LOG.debug("UpdateInterimPaymentFullAndFinal activity found");
                if (((UpdateInterimPaymentFullAndFinal) activity).claimReverted) {
                    ActivityEvent.CLAIM_REVERTED_EVENT.build(this, (UpdateInterimPaymentFullAndFinal) activity, claim);
                }
                if (((UpdateInterimPaymentFullAndFinal) activity).invoiceAccepted) {
                    ActivityEvent.INVOICE_ACCEPTED_EVENT.build(this, (UpdateInterimPaymentFullAndFinal) activity, claim);
                }
                if (((UpdateInterimPaymentFullAndFinal) activity).paymentLogged) {
                    ActivityEvent.INVOICE_PAID_EVENT.build(this, (UpdateInterimPaymentFullAndFinal) activity, claim);
                }
                ActivityEvent.INTERIM_PAYMENT_ACCEPTED_AS_FINAL_EVENT.build(this, (UpdateInterimPaymentFullAndFinal) activity, claim);
            } else if (activityName.equalsIgnoreCase("MoveToInvoicePaymentLogged")) {
                LOG.debug("MoveToInvoicePaymentLogged activity found");
                ActivityEvent.INVOICE_PAID_EVENT.build(this, (MoveToInvoicePaymentLogged) activity, claim);
            } else if (activityName.equalsIgnoreCase("NewClaim")) {
                LOG.debug("NewClaim activity found");
                ActivityEvent.NEW_CLAIM_EVENT.build(this, (NewClaim) activity, claim);
            } else if (activityName.equalsIgnoreCase("NewInvoice")) {
                LOG.debug("NewInvoice activity found");
                ActivityEvent.INVOICE_UPLOADED_EVENT.build(this, (NewInvoice) activity, claim);
                if (((NewInvoice) activity).claimRouted) {
                    ActivityEvent.CLAIM_ROUTED_EVENT.build(this, (NewInvoice) activity, claim);
                }
                if (((NewInvoice) activity).claimOwnerAssigned) {
                    ActivityEvent.INSURER_OWNER_ASSIGNED_EVENT.build(this, (NewInvoice) activity, claim);
                }
                ActivityEvent.INVOICE_SUBMITTED_EVENT.build(this, (NewInvoice) activity, claim);
                if (((NewInvoice) activity).invoiceAccepted) {
                    ActivityEvent.INVOICE_ACCEPTED_EVENT.build(this, (NewInvoice) activity, claim);
                }
            } else if (activityName.equalsIgnoreCase("NewTpiClaim")) {
                LOG.debug("NewTpiClaim activity found");
                ActivityEvent.LIABILITY_UPDATED_EVENT.build(this, (NewTpiClaim) activity, claim);
                if (((NewTpiClaim) activity).newClaim) {
                    ActivityEvent.NEW_CLAIM_EVENT.build(this, (NewTpiClaim) activity, claim);
                    ActivityEvent.HIRE_CAR_INFO_PROVIDED_EVENT.build(this, (NewTpiClaim) activity, claim);            
                }
                if (((NewTpiClaim) activity).claimRouted) {
                    ActivityEvent.CLAIM_ROUTED_EVENT.build(this, (NewTpiClaim) activity, claim);
                }
                if (((NewTpiClaim) activity).claimOwnerAssigned) {
                    ActivityEvent.INSURER_OWNER_ASSIGNED_EVENT.build(this, (NewTpiClaim) activity, claim);
                }
//                ActivityEvent.HIRE_CAR_INFO_PROVIDED_EVENT.build(this, (NewTpiClaim) activity, claim);
//                ActivityEvent.INVOICE_SUBMITTED_EVENT.build(this, (NewTpiClaim) activity, claim);
//                ActivityEvent.BRE_RESULT_EVENT.build(this, (NewTpiClaim) activity, claim);
                if (((NewTpiClaim) activity).invoiceAccepted) {
                    ActivityEvent.INVOICE_ACCEPTED_EVENT.build(this, (NewTpiClaim) activity, claim);
                }
            } else if (activityName.equalsIgnoreCase("PaymentNotReceived")) {
                LOG.debug("PaymentNotReceived activity found");
                ActivityEvent.FULL_PAYMENT_NOT_RECEIVED_EVENT.build(this, (PaymentNotReceived) activity, claim);
            } else if (activityName.equalsIgnoreCase("ReopenClaim")) {
                LOG.debug("ReopenClaim activity found");
                ActivityEvent.CLAIM_REVERTED_EVENT.build(this, (ReopenClaim) activity, claim);
            } else if (activityName.equalsIgnoreCase("ResolveLiability")) {
                LOG.debug("ResolveLiability activity found");
                ActivityEvent.LIABILITY_UPDATED_EVENT.build(this, (ResolveLiability) activity, claim);
            } else if (activityName.equalsIgnoreCase("UpdateLiability")) {
                LOG.debug("UpdateLiability activity found");
                ActivityEvent.LIABILITY_UPDATED_EVENT.build(this, (UpdateLiability) activity, claim);
            } else if (activityName.equalsIgnoreCase("RevertClaim")) {
                LOG.debug("RevertClaim activity found");
                ActivityEvent.CLAIM_REVERTED_EVENT.build(this, (RevertClaim) activity, claim);
            } else if (activityName.equalsIgnoreCase("SlaExtension")) {
                LOG.debug("SlaExtension activity found");
                ActivityEvent.SLA_EXTENSION_GRANTED_EVENT.build(this, (SlaExtension) activity, claim);
            } else if (activityName.equalsIgnoreCase("SubscriberClaimRejectionAccept")) {
                LOG.debug("SubscriberClaimRejectionAccept activity found");
                // Do we still raise the following if claim moves to AwaitingInvoicedata?
                //     Maybe change to SubscroberClaimRejected event?
// Not sure what events to raise when subscriber claim is rejected and moves to AwaitingInvoiceData
//                ActivityEvent.HIRE_CAR_INFO_PROVIDED_EVENT.build(this, (SubscriberClaimRejectionAccept) activity, claim);
                if (((SubscriberClaimRejectionAccept) activity).isHireCarInfoProvided()) {
                    ActivityEvent.HIRE_CAR_INFO_PROVIDED_EVENT.build(this, (SubscriberClaimRejectionAccept) activity, claim);
                    ActivityEvent.SUBSCRIBER_CLAIM_REJECTED_GTA.build(this, (SubscriberClaimRejectionAccept) activity, claim);
                } else {
                    ActivityEvent.CLAIM_REJECTION_ACCEPTED_EVENT.build(this, (SubscriberClaimRejectionAccept) activity, claim);
                }
            } else if (activityName.equalsIgnoreCase("SubscriberClaimToGta")) {
                LOG.debug("SubscriberClaimToGta activity found");
                ActivityEvent.CLAIM_SWITCHED_TO_GTA_EVENT.build(this, (SubscriberClaimToGta) activity, claim);
            } else if (activityName.equalsIgnoreCase("SwitchClaim")) {
                LOG.debug("SwitchClaim activity found");
                ActivityEvent.CLAIM_CLOSED_EVENT.build(this, (SwitchClaim) activity, claim);
                ActivityEvent.NEW_CLAIM_EVENT.build(this, (SwitchClaim) activity, claim);
            }  else if (activityName.equalsIgnoreCase("SwitchCho")) {
                LOG.debug("SwitchCho activity found");
                ActivityEvent.SWITCH_CHO_EVENT.build(this, (SwitchCho) activity, claim);
            }else if (activityName.equalsIgnoreCase("SwitchClaimToMultipleInsurer")) {
                LOG.debug("SwitchClaimToMultipleInsurer activity found");
                ActivityEvent.CLAIM_CLOSED_EVENT.build(this, (SwitchClaimToMultipleInsurer) activity, claim);
                ActivityEvent.NEW_CLAIM_EVENT.build(this, (SwitchClaimToMultipleInsurer) activity, claim);
            } else if (activityName.equalsIgnoreCase("UpdateManualInvoiceContested")) {
                LOG.debug("UpdateManualInvoiceContested activity found");
                ActivityEvent.INVOICE_REJECTED_EVENT.build(this, (UpdateManualInvoiceContested) activity, claim);
            } else if (activityName.equalsIgnoreCase("UpdateManualInvoicePaid")) {
                LOG.debug("UpdateManualInvoicePaid activity found");
                ActivityEvent.INVOICE_PAID_EVENT.build(this, (UpdateManualInvoicePaid) activity, claim);
            } else if (activityName.equalsIgnoreCase("WorkgroupRouting")) {
                LOG.debug("WorkgroupRouting activity found");
                ActivityEvent.CLAIM_ROUTED_EVENT.build(this, (WorkgroupRouting) activity, claim);
            } else if (activityName.equalsIgnoreCase("NewSupplementaryInvoice")) {
                if (((NewSupplementaryInvoice) activity).isNewClaim) {
                    ActivityEvent.NEW_CLAIM_EVENT.build(this, (NewSupplementaryInvoice) activity, claim);
                    ActivityEvent.HIRE_CAR_INFO_PROVIDED_EVENT.build(this, (NewSupplementaryInvoice) activity, claim);            
                }
            } else if (activityName.equalsIgnoreCase("SwitchFromPaymentsTeam")) {
                LOG.debug("SwitchFromPaymentsTeam activity found");
                ActivityEvent.INVOICE_SWITCHED_FROM_PAYMENTS_TEAM_EVENT.build(this, claim);
            } else {
                LOG.error("No events to generate for activity '{}'", activityName);
            }
        } catch (Exception ex) {
            LOG.error("Error generating events for activity '{}' : {}\n", new Object[]{activityName, ex.getMessage(), ex});
            return;
        }

        try {
            eventGenerator.sendEvents();
        } catch (Exception ex) {
            LOG.error("Error sending generated events for activity '{}' : {}\n", new Object[]{activityName, ex.getMessage(), ex});
        }
    }

}
