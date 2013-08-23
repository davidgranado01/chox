package idas.chox.events;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author John
 */
public class ChoxEventRegister implements EventRegister {

    private static final Logger LOG = LoggerFactory.getLogger(ChoxEventRegister.class);
    public static final String CLAIM_ACKNOWLEDGED_EVENT= "ClaimAcknowledgedEvent";
    public static final String LIABILITY_UPDATED_EVENT= "LiabilityUpdatedEvent";
    public static final String INSURER_OWNER_ASSIGNED_EVENT= "InsurerOwnerAssignedEvent";
    public static final String CHO_OWNER_ASSIGNED_EVENT= "ChoOwnerAssignedEvent";
    public static final String CLAIM_ROUTED_EVENT= "ClaimRoutedEvent";
    public static final String CLAIM_AWAITING_LITIGATION_OUTCOME_EVENT= "ClaimAwaitingLitigationOutcomeEvent";
    public static final String HIRE_CAR_INFO_PROVIDED_EVENT= "HireCarInfoProvidedEvent";
    public static final String CLAIM_NUMBER_ASSIGNED_EVENT= "ClaimNumberAssignedEvent";
    public static final String CLAIM_REJECTED_EVENT= "ClaimRejectedEvent";
    public static final String CLAIM_REJECTION_ACCEPTED_EVENT= "ClaimRejectionAcceptedEvent";
    public static final String CLAIM_REJECTION_CONTESTED_EVENT= "ClaimRejectionContestedEvent";
    public static final String CLAIM_CLOSED_EVENT= "ClaimClosedEvent";
    public static final String ECD_UPDATED_EVENT= "EcdUpdatedEvent";
    public static final String FULL_PAYMENT_RECEIVED_EVENT= "FullPaymentReceivedEvent";
    public static final String FULL_PAYMENT_NOT_RECEIVED_EVENT= "FullPaymentNotReceivedEvent";
    public static final String INVOICE_UPLOADED_EVENT= "InvoiceUploadedEvent";
    public static final String INVOICE_ACCEPTED_EVENT= "InvoiceAcceptedEvent";
    public static final String INVOICE_PAID_EVENT= "InvoicePaidEvent";
    public static final String INVOICE_PAYMENT_RECEIVED_EVENT= "InvoicePaymentReceivedEvent";
    public static final String INVOICE_REJECTED_EVENT= "InvoiceRejectedEvent";
    public static final String INVOICE_REJECTION_ACCEPTED_EVENT= "InvoiceRejectionAcceptedEvent";
    public static final String INVOICE_REJECTION_CONTESTED_EVENT= "InvoiceRejectionContestedEvent";
    public static final String INVOICE_RESUBMITTED_EVENT= "InvoiceResubmittedEvent";
    public static final String BRE_RESULT_EVENT= "BreResultEvent";
    public static final String INTERIM_PAYMENT_UPDATED_EVENT= "InterimPaymentUpdatedEvent";
    public static final String PAYMENT_RECEIVED_EVENT= "PaymentReceivedEvent";
    public static final String NEW_CLAIM_EVENT= "NewClaimEvent";
    public static final String INVOICE_SUBMITTED_EVENT= "InvoiceSubmittedEvent";
    public static final String PAYMENT_NOT_RECEIVED_EVENT= "PaymentNotReceivedEvent";
    public static final String CLAIM_REVERTED_EVENT= "ClaimRevertedEvent";
    public static final String SLA_EXTENSION_GRANTED_EVENT= "SlaExtensionGrantedEvent";
    public static final String CLAIM_SWITCHED_TO_GTA_EVENT= "ClaimSwitchedToGTAEvent";
    public static final String INTERIM_PAYMENT_ACCEPTED_AS_FINAL_EVENT= "InterimPaymentAcceptedAsFinalEvent";
    public static final String INTERIM_PAYMENT_RECEIVED_EVENT= "InterimPaymentReceivedEvent";
    public static final String CLAIM_PENDING_EVENT= "ClaimPendingEvent";
    public static final String CLAIM_REFERRED_TO_ENG_EVENT= "ClaimReferToEngEvent";
    public static final String CLAIM_REFERRED_TO_FNOL_EVENT= "ClaimReferToFnolEvent";
    public static final String CLAIM_REGISTERED_BY_FNOL_EVENT= "ClaimRegisteredByFnolEvent";
    public static final String CLAIM_REVIEW_BY_ENG_EVENT= "ClaimReviewByEngEvent";
    public static final String INVOICE_REFERRED_TO_CH_EVENT= "InvoiceReferToCHEvent";
    public static final String INVOICE_REFERRED_TO_ENG_EVENT= "InvoiceReferToEng";
    public static final String NEW_SUPPLEMENTARY_INVOICE_EVENT= "NewSupplementaryInvoice";
    // Non-activity based events
    public static final String CLAIM_UPDATED_EVENT= "ClaimUpdatedEvent";
    public static final String HIRE_MONITORING_UPDATED_EVENT= "HireMonitoringUpdatedEvent";
    public static final String ATTACHMENT_UPLOADED_EVENT= "AttachmentUploadedEvent";
    public static final String ATTACHMENT_DELETED_EVENT= "AttachmentDeletedEvent";
    public static final String CHO_REFERENCE_NO_UPDATED_EVENT= "ChoReferenceNumberUpdatedEvent";
    public static final String CLAIM_NUMBER_UPDATED_EVENT= "ClaimNumberUpdatedEvent";
    public static final String TASK_CREATED_EVENT= "TaskCreatedEvent";
    public static final String TASK_COMPLETED_EVENT= "TaskCompletedEvent";
    
    private List<Event> events;
    private Event openEvent;
    private ChoxJmsEventSender choxJmsEventSender;

    public void setChoxJmsEventSender(ChoxJmsEventSender choxJmsEventSender) {
        this.choxJmsEventSender = choxJmsEventSender;
    }

    @Override
    public void startEvent(String name, int insurerId, int choId, int claimId, int claimType) throws Exception {
        if (openEvent != null) {
            throw new Exception("Event already started.");
        }

        openEvent = new Event(name, insurerId, choId, claimId, claimType);
    }

    @Override
    public void addParameter(String paramName, Object paramValue) {
        openEvent.addParameter(paramName, paramValue);
    }

    @Override
    public void completeEvent() throws Exception {
        if (openEvent == null) {
            throw new Exception("No event has been started.");
        }
        if (events == null) {
            events = new ArrayList(3);
        }

        events.add(openEvent);
        openEvent = null;
    }

    @Override
    public void sendEvents() throws Exception {
        if (events != null) {
            for (Event ev : events) {
                try {
                    choxJmsEventSender.send(ev);
                } catch (Exception ex) {
                    LOG.error("Error sending CHOX event: {}\n", ex.getMessage(), ex);
                    throw ex;
                }

            }
        }
    }
}
