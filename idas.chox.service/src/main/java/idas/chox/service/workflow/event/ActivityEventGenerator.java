package idas.chox.service.workflow.event;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.aop.support.AopUtils;

import idas.chox.core.model.Claim;
import idas.chox.core.util.DateHelper;
import idas.chox.core.workflow.Activity;
import idas.chox.data.events.EventGenerator;
import idas.chox.events.AwaitingLitigationOutcomeEvent;
import idas.chox.events.BaseActivityEvent;
import idas.chox.events.ChoOwnerAssignedEvent;
import idas.chox.events.ClaimAcknowledgedEvent;
import idas.chox.events.ClaimAuditReviewUpdatedEvent;
import idas.chox.events.ClaimClosedEvent;
import idas.chox.events.ClaimNumberUpdatedEvent;
import idas.chox.events.ClaimPendingEvent;
import idas.chox.events.ClaimReferredToEngEvent;
import idas.chox.events.ClaimReferredToFnolEvent;
import idas.chox.events.ClaimRegisteredByFnolEvent;
import idas.chox.events.ClaimRejectedEvent;
import idas.chox.events.ClaimRejectionAcceptedEvent;
import idas.chox.events.ClaimRejectionContestedEvent;
import idas.chox.events.ClaimRevertedEvent;
import idas.chox.events.ClaimReviewedByEngEvent;
import idas.chox.events.ClaimRoutedEvent;
import idas.chox.events.ClaimSwitchedToGtaEvent;
import idas.chox.events.EcdUpdatedEvent;
import idas.chox.events.FullPaymentNotReceivedEvent;
import idas.chox.events.FullPaymentReceivedEvent;
import idas.chox.events.HireMonitoringInfoProvidedEvent;
import idas.chox.events.HireVehicleUpdatedEvent;
import idas.chox.events.InsurerOwnerAssignedEvent;
import idas.chox.events.InterimPaymentAcceptedAsFinalEvent;
import idas.chox.events.InterimPaymentReceivedEvent;
import idas.chox.events.InterimPaymentUpdatedEvent;
import idas.chox.events.InvoiceAcceptedEvent;
import idas.chox.events.InvoiceCreatedEvent;
import idas.chox.events.InvoicePaidEvent;
import idas.chox.events.InvoicePaymentReceivedEvent;
import idas.chox.events.InvoiceReferredToCHEvent;
import idas.chox.events.InvoiceReferredToEngEvent;
import idas.chox.events.InvoiceRejectedEvent;
import idas.chox.events.InvoiceRejectionAcceptedEvent;
import idas.chox.events.InvoiceRejectionContestedEvent;
import idas.chox.events.InvoiceSubmittedEvent;
import idas.chox.events.InvoiceSwitchedFromPaymentsTeamEvent;
import idas.chox.events.InvoiceUploadedEvent;
import idas.chox.events.LiabilityUpdatedEvent;
import idas.chox.events.NewClaimEvent;
import idas.chox.events.NoteAddedEvent;
import idas.chox.events.SlaExtensionGrantedEvent;
import idas.chox.events.SubscriberClaimRejectedToGtaEvent;
import idas.chox.events.SwitchChoEvent;
import idas.chox.events.UpdateCaseWithSolicitorEvent;
import idas.chox.service.workflow.activities.AcknowledgeClaim;
import idas.chox.service.workflow.activities.AddNote;
import idas.chox.service.workflow.activities.AssignManualInvoiceOwner;
import idas.chox.service.workflow.activities.AssignOwner;
import idas.chox.service.workflow.activities.AssignSupplierOwner;
import idas.chox.service.workflow.activities.AssignWorkgroup;
import idas.chox.service.workflow.activities.AwaitingLitigationOutcome;
import idas.chox.service.workflow.activities.ClaimAwaitingCarHireInfo;
import idas.chox.service.workflow.activities.ClaimPending;
import idas.chox.service.workflow.activities.ClaimReferToEng;
import idas.chox.service.workflow.activities.ClaimReferToFnol;
import idas.chox.service.workflow.activities.ClaimRegisterByFnol;
import idas.chox.service.workflow.activities.ClaimRejection;
import idas.chox.service.workflow.activities.ClaimRejectionAccept;
import idas.chox.service.workflow.activities.ClaimRejectionContest;
import idas.chox.service.workflow.activities.ClaimReviewByEng;
import idas.chox.service.workflow.activities.CloseClaim;
import idas.chox.service.workflow.activities.EcdUpdate;
import idas.chox.service.workflow.activities.FullInvoicePaymentReceived;
import idas.chox.service.workflow.activities.FullPaymentNotReceived;
import idas.chox.service.workflow.activities.HireUpdate;
import idas.chox.service.workflow.activities.InsurerUpload;
import idas.chox.service.workflow.activities.InvoiceAccepted;
import idas.chox.service.workflow.activities.InvoicePaymentLogged;
import idas.chox.service.workflow.activities.InvoicePaymentReceived;
import idas.chox.service.workflow.activities.InvoiceReferToCH;
import idas.chox.service.workflow.activities.InvoiceReferToEng;
import idas.chox.service.workflow.activities.InvoiceRejection;
import idas.chox.service.workflow.activities.InvoiceRejectionAccept;
import idas.chox.service.workflow.activities.InvoiceRejectionContest;
import idas.chox.service.workflow.activities.InvoiceResubmit;
import idas.chox.service.workflow.activities.MakeInterimPayment;
import idas.chox.service.workflow.activities.MoveToInvoicePaymentLogged;
import idas.chox.service.workflow.activities.NewClaim;
import idas.chox.service.workflow.activities.NewInvoice;
import idas.chox.service.workflow.activities.NewSupplementaryInvoice;
import idas.chox.service.workflow.activities.NewTpiClaim;
import idas.chox.service.workflow.activities.PaymentNotReceived;
import idas.chox.service.workflow.activities.ReopenClaim;
import idas.chox.service.workflow.activities.ResolveLiability;
import idas.chox.service.workflow.activities.RevertClaim;
import idas.chox.service.workflow.activities.SlaExtension;
import idas.chox.service.workflow.activities.SubscriberClaimRejectionAccept;
import idas.chox.service.workflow.activities.SubscriberClaimToGta;
import idas.chox.service.workflow.activities.SwitchCho;
import idas.chox.service.workflow.activities.SwitchClaim;
import idas.chox.service.workflow.activities.SwitchClaimToMultipleInsurer;
import idas.chox.service.workflow.activities.UpdateCaseWithSolicitor;
import idas.chox.service.workflow.activities.UpdateInterimPaymentFullAndFinal;
import idas.chox.service.workflow.activities.UpdateInterimPaymentReceived;
import idas.chox.service.workflow.activities.UpdateLiability;
import idas.chox.service.workflow.activities.UpdateManualInvoiceAgreeQuantum;
import idas.chox.service.workflow.activities.UpdateManualInvoiceContested;
import idas.chox.service.workflow.activities.UpdateManualInvoicePaid;
import idas.chox.service.workflow.activities.WorkgroupRouting;

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

    public List<BaseActivityEvent> getEvents(final Claim claim, Activity activity) {
        ArrayList events = new ArrayList();
        
        String activityName = AopUtils.getTargetClass(activity).getSimpleName();
        
        switch(activityName) {
            case "AcknowledgeClaim":
                if (((AcknowledgeClaim) activity).isLiabilityUpdated()) {
                    events.add(new LiabilityUpdatedEvent(claim, activityName, ((AcknowledgeClaim)activity).getSupportingLiabilityNotes()));
                }
                if (((AcknowledgeClaim) activity).isClaimNumberUpdated()) {
                    events.add(new ClaimNumberUpdatedEvent(claim, activityName, ((AcknowledgeClaim)activity).getClaimNumber()));
                }
                events.add(new ClaimAcknowledgedEvent(claim, activityName));
                break;

            case "AssignWorkgroup":
                events.add(new ClaimRoutedEvent(claim, activityName, ((AssignWorkgroup)activity).getWorkgroup().getId(), ((AssignWorkgroup)activity).getWorkgroup().getName()));
                break;

            case "NewInvoice":
                if (((NewInvoice) activity).isClaimRouted()) {
                    events.add(new ClaimRoutedEvent(claim, activityName, claim.getWorkgroup().getId(), claim.getWorkgroup().getName()));
                }
                if (((NewInvoice) activity).isClaimOwnerAssigned()) {
                    events.add(new InsurerOwnerAssignedEvent(claim, activityName));
                }
                events.add(new InvoiceCreatedEvent(claim, activityName));
                events.add(new InvoiceSubmittedEvent(claim, activityName));
                if (((NewInvoice) activity).isInvoiceAccepted()) {
                    events.add(new InvoiceAcceptedEvent(claim, activityName));
                }
                break;

            case "AddNote":
                events.add(new NoteAddedEvent(claim, activityName, ((AddNote)activity).getComment(), ((AddNote)activity).isReviewRequired(), ((AddNote)activity).getVisibilityType()));
                break;

            case "AssignManualInvoiceOwner":
            case "AssignOwner":
                    events.add(new InsurerOwnerAssignedEvent(claim, activityName));
                break;
                
            case "AssignSupplierOwner":
                    events.add(new ChoOwnerAssignedEvent(claim, activityName));
                break;
                
            case "AwaitingLitigationOutcome":
                    events.add(new AwaitingLitigationOutcomeEvent(claim, activityName));
                break;
                
            case "ClaimAwaitingCarHireInfo":
                    events.add(new HireMonitoringInfoProvidedEvent(claim, activityName));
                break;
                
            case "ClaimPending":
                if (((ClaimPending) activity).isLiabilityUpdated()) {
                    events.add(new LiabilityUpdatedEvent(claim, activityName, ((ClaimPending)activity).getSupportingLiabilityNotes()));
                }
                if (((ClaimPending) activity).isClaimNumberUpdated()) {
                    events.add(new ClaimNumberUpdatedEvent(claim, activityName, ((ClaimPending)activity).getClaimNumber()));
                }
                events.add(new ClaimPendingEvent(claim, activityName));
                break;

            case "ClaimReferToEng":
                if (((ClaimReferToEng) activity).isLiabilityUpdated()) {
                    events.add(new LiabilityUpdatedEvent(claim, activityName, ((ClaimReferToEng)activity).getSupportingLiabilityNotes()));
                }
                if (((ClaimReferToEng) activity).isClaimNumberUpdated()) {
                    events.add(new ClaimNumberUpdatedEvent(claim, activityName, ((ClaimReferToEng)activity).getClaimNumber()));
                }
                events.add(new ClaimReferredToEngEvent(claim, activityName));
                break;

            case "ClaimReferToFnol":
                if (((ClaimReferToFnol) activity).isClaimRouted()) {
                    events.add(new ClaimRoutedEvent(claim, activityName, claim.getWorkgroup().getId(), claim.getWorkgroup().getName()));
                }
                if (((ClaimReferToFnol) activity).isOwnerAssigned()) {
                    events.add(new InsurerOwnerAssignedEvent(claim, activityName));
                }
                if (((ClaimReferToFnol) activity).isLiabilityUpdated()) {
                    events.add(new LiabilityUpdatedEvent(claim, activityName, ((ClaimReferToFnol)activity).getSupportingLiabilityNotes()));
                }
                if (((ClaimReferToFnol) activity).isClaimNumberUpdated()) {
                    events.add(new ClaimNumberUpdatedEvent(claim, activityName, ((ClaimReferToFnol)activity).getClaimNumber()));
                }
                events.add(new ClaimReferredToFnolEvent(claim, activityName));
                break;

            case "ClaimRegisterByFnol":
                if (((ClaimRegisterByFnol) activity).isClaimNumberUpdated()) {
                    events.add(new ClaimNumberUpdatedEvent(claim, activityName, ((ClaimRegisterByFnol)activity).getClaimNumber()));
                }
                events.add(new ClaimRegisteredByFnolEvent(claim, activityName));
                break;

            case "ClaimRejection":
                if (((ClaimRejection) activity).isLiabilityUpdated()) {
                    events.add(new LiabilityUpdatedEvent(claim, activityName, ((ClaimRejection)activity).getSupportingLiabilityNotes()));
                }
                if (((ClaimRejection) activity).isClaimNumberUpdated()) {
                    events.add(new ClaimNumberUpdatedEvent(claim, activityName, ((ClaimRejection)activity).getClaimNumber()));
                }
                events.add(new ClaimRejectedEvent(claim, activityName, ((ClaimRejection)activity).getRejectionDescription(), ((ClaimRejection)activity).getSupportingLiabilityNotes()));
                break;

            case "ClaimRejectionAccept":
                events.add(new ClaimRejectionAcceptedEvent(claim, activityName));
                break;

            case "ClaimRejectionContest":
                events.add(new ClaimRejectionContestedEvent(claim, activityName));
                break;

            case "ClaimReviewByEng":
                events.add(new ClaimReviewedByEngEvent(claim, activityName));
                break;

            case "CloseClaim":
                events.add(new ClaimClosedEvent(claim, activityName));
                break;

            case "EcdUpdate":
                events.add(new EcdUpdatedEvent(claim, activityName, DateHelper.getLocalDateFormat().format(((EcdUpdate)activity).getEcdDate()),
                        ((EcdUpdate)activity).getReason(), ((EcdUpdate)activity).getSupportingNote()));
                break;

            case "HireUpdate":
                events.add(new HireVehicleUpdatedEvent(claim, activityName));
                break;

            case "FullInvoicePaymentReceived":
                events.add(new FullPaymentReceivedEvent(claim, activityName));
                break;

            case "FullPaymentNotReceived":
                events.add(new FullPaymentNotReceivedEvent(claim, activityName, ((FullPaymentNotReceived)activity).getInterimPaymentReceived().toPlainString()));
                break;

            case "InsurerUpload":
                if (((InsurerUpload) activity).isClaimRouted()) {
                    events.add(new ClaimRoutedEvent(claim, activityName, claim.getWorkgroup().getId(), claim.getWorkgroup().getName()));
                }
                if (((InsurerUpload) activity).isClaimOwnerAssigned()) {
                    events.add(new InsurerOwnerAssignedEvent(claim, activityName));
                }
                events.add(new InvoiceUploadedEvent(claim, activityName));
                events.add(new InvoiceSubmittedEvent(claim, activityName));
                events.add(new InvoiceCreatedEvent(claim, activityName));
                break;

            case "InvoiceAccepted":
            case "UpdateManualInvoiceAgreeQuantum":
                events.add(new InvoiceAcceptedEvent(claim, activityName));
                break;

            case "InvoicePaymentLogged":
                events.add(new InvoicePaidEvent(claim, activityName));
                break;

            case "InvoicePaymentReceived":
                events.add(new InvoicePaymentReceivedEvent(claim, activityName));
                break;

            case "InvoiceReferToCH":
                events.add(new InvoiceReferredToCHEvent(claim, activityName));
                break;

            case "InvoiceReferToEng":
                events.add(new InvoiceReferredToEngEvent(claim, activityName));
                break;

            case "InvoiceRejection":
                events.add(new InvoiceRejectedEvent(claim, activityName, ((InvoiceRejection)activity).getReasonOfRejection().getRorName(),
                            ((InvoiceRejection)activity).getRejectionDescription()));
                break;

            case "InvoiceRejectionAccept":
                events.add(new InvoiceRejectionAcceptedEvent(claim, activityName, ((InvoiceRejectionAccept)activity).getSupportingLiabilityNotes()));
                break;

            case "InvoiceRejectionContest":
                events.add(new InvoiceRejectionContestedEvent(claim, activityName, ((InvoiceRejectionContest)activity).getSupportingLiabilityNotes()));
                events.add(new InvoiceSubmittedEvent(claim, activityName));
                break;

            case "InvoiceResubmit":
                if (((InvoiceResubmit) activity).isClaimRouted()) {
                    events.add(new ClaimRoutedEvent(claim, activityName, claim.getWorkgroup().getId(), claim.getWorkgroup().getName()));
                }
                if (((InvoiceResubmit) activity).isClaimOwnerAssigned()) {
                    events.add(new InsurerOwnerAssignedEvent(claim, activityName));
                }
                events.add(new InvoiceSubmittedEvent(claim, activityName));
                if (((InvoiceResubmit) activity).isInvoiceAccepted()) {
                    events.add(new InvoiceAcceptedEvent(claim, activityName));
                }
                break;

            case "MakeInterimPayment":
                events.add(new InterimPaymentUpdatedEvent(claim, activityName, ((MakeInterimPayment)activity).getAdditionalInterimPayment().toPlainString(),
                        ((MakeInterimPayment)activity).getNewTotalInterimPayment().toPlainString()));
                break;

            case "UpdateInterimPaymentReceived":
                events.add(new InterimPaymentReceivedEvent(claim, activityName, ((UpdateInterimPaymentReceived)activity).getPartialInterimPayment().toPlainString()));
                break;

            case "UpdateInterimPaymentFullAndFinal":
                if (((UpdateInterimPaymentFullAndFinal) activity).isClaimReverted()) {
                    events.add(new ClaimRevertedEvent(claim, activityName));
                }
                if (((UpdateInterimPaymentFullAndFinal) activity).isInvoiceAccepted()) {
                    events.add(new InvoiceAcceptedEvent(claim, activityName));
                }
                if (((UpdateInterimPaymentFullAndFinal) activity).isPaymentLogged()) {
                    events.add(new InvoicePaidEvent(claim, activityName));
                }
                events.add(new InterimPaymentAcceptedAsFinalEvent(claim, activityName, claim.getInvoice().getInterimPaymentReceived().toPlainString()));
                break;

            case "MoveToInvoicePaymentLogged":
                events.add(new InvoicePaidEvent(claim, activityName));
                break;

            case "NewClaim":
                events.add(new NewClaimEvent(claim, activityName));
                break;

            case "NewTpiClaim":
                events.add(new LiabilityUpdatedEvent(claim, activityName, "Initial Liability set on TPI claim"));
                if (((NewTpiClaim) activity).isNewClaim()) {
                    events.add(new NewClaimEvent(claim, activityName));
                    events.add(new HireMonitoringInfoProvidedEvent(claim, activityName));
                }
                if (((NewTpiClaim) activity).isClaimRouted()) {
                    events.add(new ClaimRoutedEvent(claim, activityName, claim.getWorkgroup().getId(), claim.getWorkgroup().getName()));
                }
                if (((NewTpiClaim) activity).isClaimOwnerAssigned()) {
                    events.add(new InsurerOwnerAssignedEvent(claim, activityName));
                }
                events.add(new InvoiceSubmittedEvent(claim, activityName));
                if (((NewTpiClaim) activity).isInvoiceAccepted()) {
                    events.add(new InvoiceAcceptedEvent(claim, activityName));
                }
                break;

            case "PaymentNotReceived":
                events.add(new FullPaymentNotReceivedEvent(claim, activityName, ((PaymentNotReceived)activity).getAmountReceived().toPlainString()));
                break;

            case "ReopenClaim":
                events.add(new ClaimRevertedEvent(claim, activityName));
                break;

            case "ResolveLiability":
                events.add(new LiabilityUpdatedEvent(claim, activityName, ((ResolveLiability)activity).getEngineerClaimReviewNotes()));
                break;

            case "UpdateLiability":
                events.add(new LiabilityUpdatedEvent(claim, activityName, ((UpdateLiability)activity).getClaimReviewNotes()));
                break;

            case "RevertClaim":
                events.add(new ClaimRevertedEvent(claim, activityName));
                break;

            case "SlaExtension":
                events.add(new SlaExtensionGrantedEvent(claim, activityName, String.valueOf(((SlaExtension)activity).getSlaExtDays())));
                break;

            case "SubscriberClaimRejectionAccept":
                if (((SubscriberClaimRejectionAccept) activity).isHireCarInfoProvided()) {
                    events.add(new HireMonitoringInfoProvidedEvent(claim, activityName));
                    events.add(new SubscriberClaimRejectedToGtaEvent(claim, activityName));
                } else {
                    events.add(new ClaimRejectionAcceptedEvent(claim, activityName));
                }
                break;

            case "SubscriberClaimToGta":
                    events.add(new ClaimSwitchedToGtaEvent(claim, activityName));
                break;

            case "SwitchClaim":
                events.add(new ClaimClosedEvent(claim, activityName, ((SwitchClaim)activity).getOldInsurer().getId()));
                events.add(new NewClaimEvent(claim, activityName));
                break;

            case "SwitchCho":
                events.add(new SwitchChoEvent(claim, activityName, ((SwitchCho)activity).getOldCho().getId(), ((SwitchCho)activity).getOldCho().getName(), claim.getChorganisation().getName()));
                events.add(new NewClaimEvent(claim, activityName));
                break;

            case "SwitchClaimToMultipleInsurer":
                events.add(new ClaimClosedEvent(claim, activityName, ((SwitchClaimToMultipleInsurer)activity).getOldInsurer().getId()));
                events.add(new NewClaimEvent(claim, activityName));
                break;

            case "UpdateManualInvoiceContested":
                events.add(new InvoiceRejectedEvent(claim, activityName));
                break;

            case "UpdateManualInvoicePaid":
                events.add(new InvoicePaidEvent(claim, activityName));
                break;

            case "WorkgroupRouting":
                    events.add(new ClaimRoutedEvent(claim, activityName, claim.getWorkgroup().getId(), claim.getWorkgroup().getName()));
                break;

            case "NewSupplementaryInvoice":
                if (((NewSupplementaryInvoice) activity).isIsNewClaim()) {
                    events.add(new NewClaimEvent(claim, activityName));
                    events.add(new HireMonitoringInfoProvidedEvent(claim, activityName));
                }
                break;

            case "SwitchFromPaymentsTeam":
                events.add(new InvoiceSwitchedFromPaymentsTeamEvent(claim, activityName));
                break;

            case "UpdateCaseWithSolicitor":
                events.add(new UpdateCaseWithSolicitorEvent(claim, activityName, String.valueOf(((UpdateCaseWithSolicitor)activity).isCaseWithSolicitor())));
                break;

            case "SaveOrSubmitClaimAuditReview":
                events.add(new ClaimAuditReviewUpdatedEvent(claim, activityName));
                break;

            default:
                events.add(new BaseActivityEvent(claim, activityName));
                break;
        }
        
        // Uncomment below to generate "original" events to activeMQ broker
//        generate(claim, activity);

        return events;
    }
    
    public void generate(final Claim claim, Activity activity) {
//        activity.generateEvents(claim);
        
        String activityName = AopUtils.getTargetClass(activity).getSimpleName();
        LOG.debug("Generating events for activity {}", activityName);

        try {
            if (activityName.equalsIgnoreCase("AcknowledgeClaim")) {
                LOG.debug("AcknowledgeClaim activity found");
                if (((AcknowledgeClaim) activity).isLiabilityUpdated()) {
                    ActivityEvent.LIABILITY_UPDATED_EVENT.build(this, (AcknowledgeClaim) activity, claim);
                }
                if (((AcknowledgeClaim) activity).isClaimNumberUpdated()) {
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
                if (((ClaimPending) activity).isLiabilityUpdated()) {
                    ActivityEvent.LIABILITY_UPDATED_EVENT.build(this, (ClaimPending) activity, claim);
                }
                if (((ClaimPending) activity).isClaimNumberUpdated()) {
                    ActivityEvent.CLAIM_NUMBER_ASSIGNED_EVENT.build(this, claim);
                }
                ActivityEvent.CLAIM_PENDING_EVENT.build(this, (ClaimPending) activity, claim);
            } else if (activityName.equalsIgnoreCase("ClaimReferToEng")) {
                LOG.debug("ClaimReferToEng activity found");
                if (((ClaimReferToEng) activity).isLiabilityUpdated()) {
                    ActivityEvent.LIABILITY_UPDATED_EVENT.build(this, (ClaimReferToEng) activity, claim);
                }
                if (((ClaimReferToEng) activity).isClaimNumberUpdated()) {
                    ActivityEvent.CLAIM_NUMBER_ASSIGNED_EVENT.build(this, claim);
                }
                ActivityEvent.CLAIM_REFERRED_TO_ENG_EVENT.build(this, (ClaimReferToEng) activity, claim);
            } else if (activityName.equalsIgnoreCase("ClaimReferToFnol")) {
                LOG.debug("ClaimReferToFnol activity found");
                if (((ClaimReferToFnol) activity).isClaimRouted()) {
                    ActivityEvent.CLAIM_ROUTED_EVENT.build(this, (ClaimReferToFnol) activity, claim);
                }
                if (((ClaimReferToFnol) activity).isOwnerAssigned()) {
                    ActivityEvent.INSURER_OWNER_ASSIGNED_EVENT.build(this, (ClaimReferToFnol) activity, claim);
                }
                if (((ClaimReferToFnol) activity).isLiabilityUpdated()) {
                    ActivityEvent.LIABILITY_UPDATED_EVENT.build(this, (ClaimReferToFnol) activity, claim);
                }
                if (((ClaimReferToFnol) activity).isClaimNumberUpdated()) {
                    ActivityEvent.CLAIM_NUMBER_ASSIGNED_EVENT.build(this, claim);
                }
                ActivityEvent.CLAIM_REFERRED_TO_FNOL_EVENT.build(this, (ClaimReferToFnol) activity, claim);
            } else if (activityName.equalsIgnoreCase("ClaimRegisterByFnol")) {
                LOG.debug("ClaimRegisterByFnol activity found");
                if (((ClaimRegisterByFnol) activity).isClaimNumberUpdated()) {
                    ActivityEvent.CLAIM_NUMBER_ASSIGNED_EVENT.build(this, (ClaimRegisterByFnol) activity, claim);
                }
                ActivityEvent.CLAIM_REGISTERED_BY_FNOL_EVENT.build(this, (ClaimRegisterByFnol) activity, claim);
            } else if (activityName.equalsIgnoreCase("ClaimRejection")) {
                LOG.debug("ClaimRejection activity found");
                if (((ClaimRejection) activity).isLiabilityUpdated()) {
                    ActivityEvent.LIABILITY_UPDATED_EVENT.build(this, (ClaimRejection) activity, claim);
                }
                if (((ClaimRejection) activity).isClaimNumberUpdated()) {
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
                if (((InsurerUpload) activity).isClaimRouted()) {
                    ActivityEvent.CLAIM_ROUTED_EVENT.build(this, (InsurerUpload) activity, claim);
                }
                if (((InsurerUpload) activity).isClaimOwnerAssigned()) {
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
                if (((InvoiceResubmit) activity).isClaimRouted()) {
                    ActivityEvent.CLAIM_ROUTED_EVENT.build(this, (InvoiceResubmit) activity, claim);
                }
                if (((InvoiceResubmit) activity).isClaimOwnerAssigned()) {
                    ActivityEvent.INSURER_OWNER_ASSIGNED_EVENT.build(this, (InvoiceResubmit) activity, claim);
                }
                ActivityEvent.INVOICE_SUBMITTED_EVENT.build(this, (InvoiceResubmit) activity, claim);
                if (((InvoiceResubmit) activity).isInvoiceAccepted()) {
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
                if (((UpdateInterimPaymentFullAndFinal) activity).isClaimReverted()) {
                    ActivityEvent.CLAIM_REVERTED_EVENT.build(this, (UpdateInterimPaymentFullAndFinal) activity, claim);
                }
                if (((UpdateInterimPaymentFullAndFinal) activity).isInvoiceAccepted()) {
                    ActivityEvent.INVOICE_ACCEPTED_EVENT.build(this, (UpdateInterimPaymentFullAndFinal) activity, claim);
                }
                if (((UpdateInterimPaymentFullAndFinal) activity).isPaymentLogged()) {
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
                if (((NewInvoice) activity).isClaimRouted()) {
                    ActivityEvent.CLAIM_ROUTED_EVENT.build(this, (NewInvoice) activity, claim);
                }
                if (((NewInvoice) activity).isClaimOwnerAssigned()) {
                    ActivityEvent.INSURER_OWNER_ASSIGNED_EVENT.build(this, (NewInvoice) activity, claim);
                }
                ActivityEvent.INVOICE_SUBMITTED_EVENT.build(this, (NewInvoice) activity, claim);
                if (((NewInvoice) activity).isInvoiceAccepted()) {
                    ActivityEvent.INVOICE_ACCEPTED_EVENT.build(this, (NewInvoice) activity, claim);
                }
            } else if (activityName.equalsIgnoreCase("NewTpiClaim")) {
                LOG.debug("NewTpiClaim activity found");
                ActivityEvent.LIABILITY_UPDATED_EVENT.build(this, (NewTpiClaim) activity, claim);
                if (((NewTpiClaim) activity).isNewClaim()) {
                    ActivityEvent.NEW_CLAIM_EVENT.build(this, (NewTpiClaim) activity, claim);
                    ActivityEvent.HIRE_CAR_INFO_PROVIDED_EVENT.build(this, (NewTpiClaim) activity, claim);            
                }
                if (((NewTpiClaim) activity).isClaimRouted()) {
                    ActivityEvent.CLAIM_ROUTED_EVENT.build(this, (NewTpiClaim) activity, claim);
                }
                if (((NewTpiClaim) activity).isClaimOwnerAssigned()) {
                    ActivityEvent.INSURER_OWNER_ASSIGNED_EVENT.build(this, (NewTpiClaim) activity, claim);
                }
//                ActivityEvent.HIRE_CAR_INFO_PROVIDED_EVENT.build(this, (NewTpiClaim) activity, claim);
//                ActivityEvent.INVOICE_SUBMITTED_EVENT.build(this, (NewTpiClaim) activity, claim);
//                ActivityEvent.BRE_RESULT_EVENT.build(this, (NewTpiClaim) activity, claim);
                if (((NewTpiClaim) activity).isInvoiceAccepted()) {
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
                if (((NewSupplementaryInvoice) activity).isIsNewClaim()) {
                    ActivityEvent.NEW_CLAIM_EVENT.build(this, (NewSupplementaryInvoice) activity, claim);
                    ActivityEvent.HIRE_CAR_INFO_PROVIDED_EVENT.build(this, (NewSupplementaryInvoice) activity, claim);            
                }
            } else if (activityName.equalsIgnoreCase("SwitchFromPaymentsTeam")) {
                LOG.debug("SwitchFromPaymentsTeam activity found");
                ActivityEvent.INVOICE_SWITCHED_FROM_PAYMENTS_TEAM_EVENT.build(this, claim);
            } else if (activityName.equalsIgnoreCase("UpdateCaseWithSolicitor")) {
                LOG.debug("UpdateCaseWithSolicitor activity found");
                ActivityEvent.UPDATE_CASE_WITH_SOLICITOR_EVENT.build(this, (UpdateCaseWithSolicitor) activity, claim);
            } else if (activityName.equalsIgnoreCase("SaveOrSubmitClaimAuditReview")) {
                LOG.debug("SaveOrSubmitClaimAuditReview activity found");
                ActivityEvent.CLAIM_AUDIT_REVIEW_UPDATED_EVENT.build(this, claim);
            } else {
                LOG.warn("No events to generate for activity '{}'", activityName);
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
