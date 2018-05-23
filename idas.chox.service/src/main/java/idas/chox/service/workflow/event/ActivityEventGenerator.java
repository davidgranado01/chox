package idas.chox.service.workflow.event;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.aop.support.AopUtils;

import idas.chox.core.model.Claim;
import idas.chox.core.util.DateHelper;
import idas.chox.core.workflow.Activity;
import idas.chox.events.AwaitingLitigationOutcomeEvent;
import idas.chox.events.BaseActivityEvent;
import idas.chox.events.ClaimAcknowledgedEvent;
import idas.chox.events.ClaimAuditReviewUpdatedEvent;
import idas.chox.events.ClaimClosedEvent;
import idas.chox.events.ClaimMatchingEvent;
import idas.chox.events.ClaimNumberUpdatedEvent;
import idas.chox.events.ClaimPendingEvent;
import idas.chox.events.ClaimReferredToEngEvent;
import idas.chox.events.ClaimReferredToFnolEvent;
import idas.chox.events.ClaimRegisteredByFnolEvent;
import idas.chox.events.ClaimRejectedEvent;
import idas.chox.events.ClaimRejectionAcceptedEvent;
import idas.chox.events.ClaimRejectionContestedEvent;
import idas.chox.events.ClaimResubmittedEvent;
import idas.chox.events.ClaimRevertedEvent;
import idas.chox.events.ClaimReviewedByEngEvent;
import idas.chox.events.ClaimRoutedEvent;
import idas.chox.events.ClaimSwitchedToGtaEvent;
import idas.chox.events.ClaimUpdatedEvent;
import idas.chox.events.EcdUpdatedEvent;
import idas.chox.events.FullPaymentNotReceivedEvent;
import idas.chox.events.FullPaymentReceivedEvent;
import idas.chox.events.HireMonitoringInfoProvidedEvent;
import idas.chox.events.HireVehicleUpdatedEvent;
import idas.chox.events.InsurerEcdUpdatedEvent;
import idas.chox.events.InsurerHireMonitoringInfoProvidedEvent;
import idas.chox.events.InsurerHireVehicleUpdatedEvent;
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
import idas.chox.events.InvoiceUpdatedEvent;
import idas.chox.events.InvoiceUploadedEvent;
import idas.chox.events.LiabilityUpdatedEvent;
import idas.chox.events.NewClaimEvent;
import idas.chox.events.NoteAddedEvent;
import idas.chox.events.SlaExtensionGrantedEvent;
import idas.chox.events.SubscriberClaimRejectedToGtaEvent;
import idas.chox.events.SwitchChoEvent;
import idas.chox.events.SwitchInsEvent;
import idas.chox.events.UpdateCaseWithSolicitorEvent;
import idas.chox.events.UpdateCustomerClaimNumberEvent;
import idas.chox.events.UpdateInsurerClaimNumberEvent;
import idas.chox.events.UpdateSupplierClaimOwnerEvent;
import idas.chox.events.UpdateSupplierReferenceEvent;
import idas.chox.service.workflow.activities.AcknowledgeClaim;
import idas.chox.service.workflow.activities.AddNote;
import idas.chox.service.workflow.activities.AssignWorkgroup;
import idas.chox.service.workflow.activities.ClaimPending;
import idas.chox.service.workflow.activities.ClaimReferToEng;
import idas.chox.service.workflow.activities.ClaimReferToFnol;
import idas.chox.service.workflow.activities.ClaimRegisterByFnol;
import idas.chox.service.workflow.activities.ClaimRejection;
import idas.chox.service.workflow.activities.EcdUpdate;
import idas.chox.service.workflow.activities.FullPaymentNotReceived;
import idas.chox.service.workflow.activities.InsurerEcdUpdate;
import idas.chox.service.workflow.activities.InsurerUpload;
import idas.chox.service.workflow.activities.InvoiceRejection;
import idas.chox.service.workflow.activities.InvoiceRejectionAccept;
import idas.chox.service.workflow.activities.InvoiceRejectionContest;
import idas.chox.service.workflow.activities.InvoiceResubmit;
import idas.chox.service.workflow.activities.MakeInterimPayment;
import idas.chox.service.workflow.activities.NewInvoice;
import idas.chox.service.workflow.activities.NewSupplementaryInvoice;
import idas.chox.service.workflow.activities.NewTpiClaim;
import idas.chox.service.workflow.activities.PaymentNotReceived;
import idas.chox.service.workflow.activities.ResolveLiability;
import idas.chox.service.workflow.activities.SlaExtension;
import idas.chox.service.workflow.activities.SubscriberClaimRejectionAccept;
import idas.chox.service.workflow.activities.SwitchCho;
import idas.chox.service.workflow.activities.SwitchClaim;
import idas.chox.service.workflow.activities.SwitchClaimToMultipleInsurer;
import idas.chox.service.workflow.activities.UpdateCaseWithSolicitor;
import idas.chox.service.workflow.activities.UpdateCustomerClaimNumber;
import idas.chox.service.workflow.activities.UpdateSupplierClaimOwner;
import idas.chox.service.workflow.activities.UpdateInsurerClaimNumber;
import idas.chox.service.workflow.activities.UpdateInterimPaymentFullAndFinal;
import idas.chox.service.workflow.activities.UpdateInterimPaymentReceived;
import idas.chox.service.workflow.activities.UpdateLiability;
import idas.chox.service.workflow.activities.UpdateSupplierReference;
import idas.chox.service.workflow.activities.WorkgroupRouting;

/**
 *
 * @author John
 */
public class ActivityEventGenerator {
    
    private static final Logger LOG = LoggerFactory.getLogger(ActivityEventGenerator.class);
    
    public List<BaseActivityEvent> getEvents(final Claim claim, String modelName) {
        ArrayList events = new ArrayList();
        // Remove everything from the first underscore character _ to handle proxies
        if (modelName.indexOf('_') > 0) {
            modelName = modelName.substring(0, modelName.indexOf('_'));
        }
        LOG.debug("Generating events for model '{}'", modelName);
        switch(modelName) {
            case "Claim":
            case "Customer":
            case "Incident":
            case "ThirdParty":
            case "Injury":
            case "Witness":
                events.add(new ClaimUpdatedEvent(claim, modelName));
                break;
            case "VehicleHire":
                events.add(new HireVehicleUpdatedEvent(claim, modelName));
                break;
            case "HireMonitoringDetail":
                events.add(new HireMonitoringInfoProvidedEvent(claim, modelName));
                break;
            case "InsurerHireMonitoringDetail":
                events.add(new InsurerHireMonitoringInfoProvidedEvent(claim, modelName));
                break;
            case "InsurerVehicleHire":
                events.add(new InsurerHireVehicleUpdatedEvent(claim, modelName));
                break;
            case "Invoice":
                events.add(new InvoiceUpdatedEvent(claim, modelName));
                break;
            case "UploadClaimXMLService":
                events.add(new ClaimResubmittedEvent(claim, modelName));
                break;
            default:
                LOG.error("No events for model '{}'", modelName);
                break;
        }
        LOG.debug("{} events generated for model '{}'", events.size(), modelName);
        
        return events;
    }

    public List<BaseActivityEvent> getEvents(final Claim claim, Activity activity) {
        ArrayList events = new ArrayList();
        
        String activityName = AopUtils.getTargetClass(activity).getSimpleName();
        
        try {
            switch (activityName) {
                case "AcknowledgeClaim":
                    if (((AcknowledgeClaim) activity).isLiabilityUpdated()) {
                        events.add(new LiabilityUpdatedEvent(claim, activityName, ((AcknowledgeClaim) activity).getSupportingLiabilityNotes()));
                    }
                    if (((AcknowledgeClaim) activity).isClaimNumberUpdated()) {
                        events.add(new ClaimNumberUpdatedEvent(claim, activityName, ((AcknowledgeClaim) activity).getClaimNumber()));
                    }
                    events.add(new ClaimAcknowledgedEvent(claim, activityName));
                    break;
                
                case "AssignWorkgroup":
                    events.add(new ClaimRoutedEvent(claim, activityName, ((AssignWorkgroup) activity).getWorkgroup().getId(), ((AssignWorkgroup) activity).getWorkgroup().getName()));
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
                    events.add(new NoteAddedEvent(claim, activityName, ((AddNote) activity).getComment(), ((AddNote) activity).isReviewRequired(), ((AddNote) activity).getVisibilityType()));
                    break;
                
                case "AssignOwner":
                    events.add(new InsurerOwnerAssignedEvent(claim, activityName));
                    break;
                
                case "AwaitingLitigationOutcome":
                    events.add(new AwaitingLitigationOutcomeEvent(claim, activityName));
                    break;
                
                case "ClaimAwaitingCarHireInfo":
                    events.add(new HireMonitoringInfoProvidedEvent(claim, activityName));
                    break;
                
                case "ClaimMatching":
                    events.add(new ClaimMatchingEvent(claim, activityName));
                    break;
                
                case "ClaimPending":
                    if (((ClaimPending) activity).isLiabilityUpdated()) {
                        events.add(new LiabilityUpdatedEvent(claim, activityName, ((ClaimPending) activity).getSupportingLiabilityNotes()));
                    }
                    if (((ClaimPending) activity).isClaimNumberUpdated()) {
                        events.add(new ClaimNumberUpdatedEvent(claim, activityName, ((ClaimPending) activity).getClaimNumber()));
                    }
                    events.add(new ClaimPendingEvent(claim, activityName));
                    break;
                
                case "ClaimReferToEng":
                    if (((ClaimReferToEng) activity).isLiabilityUpdated()) {
                        events.add(new LiabilityUpdatedEvent(claim, activityName, ((ClaimReferToEng) activity).getSupportingLiabilityNotes()));
                    }
                    if (((ClaimReferToEng) activity).isClaimNumberUpdated()) {
                        events.add(new ClaimNumberUpdatedEvent(claim, activityName, ((ClaimReferToEng) activity).getClaimNumber()));
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
                        events.add(new LiabilityUpdatedEvent(claim, activityName, ((ClaimReferToFnol) activity).getSupportingLiabilityNotes()));
                    }
                    if (((ClaimReferToFnol) activity).isClaimNumberUpdated()) {
                        events.add(new ClaimNumberUpdatedEvent(claim, activityName, ((ClaimReferToFnol) activity).getClaimNumber()));
                    }
                    events.add(new ClaimReferredToFnolEvent(claim, activityName));
                    break;
                
                case "ClaimRegisterByFnol":
                    if (((ClaimRegisterByFnol) activity).isClaimNumberUpdated()) {
                        events.add(new ClaimNumberUpdatedEvent(claim, activityName, ((ClaimRegisterByFnol) activity).getClaimNumber()));
                    }
                    events.add(new ClaimRegisteredByFnolEvent(claim, activityName));
                    break;
                
                case "ClaimRejection":
                    if (((ClaimRejection) activity).isLiabilityUpdated()) {
                        events.add(new LiabilityUpdatedEvent(claim, activityName, ((ClaimRejection) activity).getSupportingLiabilityNotes()));
                    }
                    if (((ClaimRejection) activity).isClaimNumberUpdated()) {
                        events.add(new ClaimNumberUpdatedEvent(claim, activityName, ((ClaimRejection) activity).getClaimNumber()));
                    }
                    events.add(new ClaimRejectedEvent(claim, activityName, ((ClaimRejection) activity).getRejectionDescription(), ((ClaimRejection) activity).getSupportingLiabilityNotes()));
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
                    events.add(new EcdUpdatedEvent(claim, activityName, DateHelper.getLocalDateFormat().format(((EcdUpdate) activity).getEcdDate()),
                            ((EcdUpdate) activity).getReason(), ((EcdUpdate) activity).getSupportingNote()));
                    break;
                
                case "InsurerEcdUpdate":
                    events.add(new InsurerEcdUpdatedEvent(claim, activityName, DateHelper.getLocalDateFormat().format(((InsurerEcdUpdate) activity).getEcdDate()),
                            ((InsurerEcdUpdate) activity).getReason(), ((InsurerEcdUpdate) activity).getSupportingNote()));
                    break;
                
                case "HireUpdate":
                    events.add(new HireVehicleUpdatedEvent(claim, activityName));
                    break;
                
                case "LouUpdate":
                    events.add(new HireMonitoringInfoProvidedEvent(claim, activityName));
                    break;
                
                case "FullInvoicePaymentReceived":
                    events.add(new FullPaymentReceivedEvent(claim, activityName));
                    break;
                
                case "FullPaymentNotReceived":
                    events.add(new FullPaymentNotReceivedEvent(claim, activityName, ((FullPaymentNotReceived) activity).getInterimPaymentReceived().toPlainString()));
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
                    events.add(new InvoiceRejectedEvent(claim, activityName, ((InvoiceRejection) activity).getReasonOfRejection().getRorName(),
                            ((InvoiceRejection) activity).getRejectionDescription()));
                    break;
                
                case "InvoiceRejectionAccept":
                    events.add(new InvoiceRejectionAcceptedEvent(claim, activityName, ((InvoiceRejectionAccept) activity).getSupportingLiabilityNotes()));
                    break;
                
                case "InvoiceRejectionContest":
                    events.add(new InvoiceRejectionContestedEvent(claim, activityName, ((InvoiceRejectionContest) activity).getSupportingLiabilityNotes()));
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
                    events.add(new InterimPaymentUpdatedEvent(claim, activityName, ((MakeInterimPayment) activity).getAdditionalInterimPayment() == null ? "" : ((MakeInterimPayment) activity).getAdditionalInterimPayment().toPlainString(),
                            ((MakeInterimPayment) activity).getNewTotalInterimPayment() == null ? "" : ((MakeInterimPayment) activity).getNewTotalInterimPayment().toPlainString()));
                    break;
                
                case "UpdateInterimPaymentReceived":
                    events.add(new InterimPaymentReceivedEvent(claim, activityName, ((UpdateInterimPaymentReceived) activity).getPartialInterimPayment().toPlainString()));
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
                    events.add(new FullPaymentNotReceivedEvent(claim, activityName, ((PaymentNotReceived) activity).getAmountReceived().toPlainString()));
                    break;
                
                case "ReopenClaim":
                    events.add(new ClaimRevertedEvent(claim, activityName));
                    break;
                
                case "ResolveLiability":
                    events.add(new LiabilityUpdatedEvent(claim, activityName, ((ResolveLiability) activity).getEngineerClaimReviewNotes()));
                    break;
                
                case "UpdateLiability":
                    events.add(new LiabilityUpdatedEvent(claim, activityName, ((UpdateLiability) activity).getClaimReviewNotes()));
                    break;
                
                case "RevertClaim":
                    events.add(new ClaimRevertedEvent(claim, activityName));
                    break;
                
                case "SlaExtension":
                    events.add(new SlaExtensionGrantedEvent(claim, activityName, String.valueOf(((SlaExtension) activity).getSlaExtDays())));
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
                    events.add(new SwitchInsEvent(claim, activityName, ((SwitchClaim) activity).getOldInsurer().getId(), claim.getInsurer().getName()));
                    break;
                
                case "SwitchCho":
                    events.add(new SwitchChoEvent(claim, activityName, ((SwitchCho) activity).getOldCho().getId(), ((SwitchCho) activity).getOldCho().getName(), claim.getChorganisation().getName()));
                    break;
                
                case "SwitchClaimToMultipleInsurer":
                    events.add(new SwitchInsEvent(claim, activityName, ((SwitchClaimToMultipleInsurer) activity).getOldInsurer().getId(), claim.getInsurer().getName()));
                    break;
                
                case "UpdateManualInvoiceContested":
                    events.add(new InvoiceRejectedEvent(claim, activityName));
                    break;
                
                case "UpdateManualInvoicePaid":
                    events.add(new InvoicePaidEvent(claim, activityName));
                    break;
                
                case "WorkgroupRouting":
                    if (((WorkgroupRouting) activity).isRouted()) {
                        events.add(new ClaimRoutedEvent(claim, activityName, claim.getWorkgroup().getId(), claim.getWorkgroup().getName()));
                    }
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
                    events.add(new UpdateCaseWithSolicitorEvent(claim, activityName, String.valueOf(((UpdateCaseWithSolicitor) activity).isCaseWithSolicitor())));
                    break;
                
                case "UpdateInsurerClaimNumber":
                    events.add(new UpdateInsurerClaimNumberEvent(claim, activityName, String.valueOf(((UpdateInsurerClaimNumber) activity).getClaimNumber())));
                    break;
                
                case "UpdateSupplierReference":
                    events.add(new UpdateSupplierReferenceEvent(claim, activityName, String.valueOf(((UpdateSupplierReference) activity).getSupplierReference())));
                    break;
                
                case "UpdateCustomerClaimNumber":
                    events.add(new UpdateCustomerClaimNumberEvent(claim, activityName, String.valueOf(((UpdateCustomerClaimNumber) activity).getCustomerClaimNumber())));
                    break;
                
                case "UpdateSupplierClaimOwner":
                    events.add(new UpdateSupplierClaimOwnerEvent(claim, activityName, ((UpdateSupplierClaimOwner) activity).getOldClaimOwner()));
                    break;
                
                case "SaveOrSubmitClaimAuditReview":
                    events.add(new ClaimAuditReviewUpdatedEvent(claim, activityName));
                    break;
                    
                case "InvoiceSaving":
                case "LastReviewDate":
                case "ClaimMatchedReview":
                case "TlTaskCreation":
                default:
                    events.add(new BaseActivityEvent(claim, activityName));
                    break;
            }
        } catch (Exception ex) {
            LOG.error("Exception thrown generating events for activity '{}': {}", activityName, ex.getMessage(), ex);
        }

        return events;
    }
       
}
