package idas.chox.service.workflow.activities;

import idas.chox.core.model.Attachment;
import java.util.List;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Comment;
import idas.chox.core.model.Customer;
import idas.chox.core.model.EngineerReport;
import idas.chox.core.model.HireMonitoringDetail;
import idas.chox.core.model.HireMonitoringEcd;
import idas.chox.core.model.History;
import idas.chox.core.model.Incident;
import idas.chox.core.model.Injury;
import idas.chox.core.model.Invoice;
import idas.chox.core.model.Solicitor;
import idas.chox.core.model.ThirdParty;
import idas.chox.core.model.VehicleHire;
import idas.chox.core.model.Witness;
import idas.chox.core.util.DateHelper;
import idas.chox.core.workflow.Activity;

/**
 *
 * @author John
 */
public enum ActivityEvent {

    CLAIM_ACKNOWLEDGED_EVENT                (1, "ClaimAcknowledgedEvent") {
        @Override
        public void build(ActivityEventGenerator generator, AcknowledgeClaim activity, Claim claim) throws Exception {
            LOG.debug("Building from ClaimAcknowledgedEvent");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.addParameter("claimNumber", activity.getClaimNumber());
            generator.addParameter("indemnityAmount", activity.getIndemnityAmount());
            generator.addParameter("isInvoiceReviewRequired", activity.isIsInvoiceReviewRequired());
            generator.addParameter("supportingNote", activity.getSupportingLiabilityNotes());
            generator.completeEvent(claim);
        }
    },
    CLAIM_SWITCHED_TO_GTA_EVENT             (2, "ClaimSwitchedToGTAEvent") {
        @Override
        public void build(ActivityEventGenerator generator, SubscriberClaimToGta activity, Claim claim)  throws Exception {
            LOG.debug("Building ClaimSwitchedToGTAEvent from SubscriberClaimToGta activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.completeEvent(claim);
        }
    },
    INVOICE_UPLOADED_EVENT                  (3, "InvoiceUploadedEvent") {
        @Override
        public void build(ActivityEventGenerator generator, InsurerUpload activity, Claim claim)  throws Exception {
            LOG.debug("Building InvoiceUploadedEvent from InsurerUpload activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            addInvoiceParameters(generator, claim);
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, NewInvoice activity, Claim claim)  throws Exception {
            LOG.debug("Building InvoiceUploadedEvent from NewInvoice activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            addInvoiceParameters(generator, claim);
            generator.completeEvent(claim);
        }
    },
    INVOICE_REJECTED_EVENT                  (4, "InvoiceRejectedEvent") {
        @Override
        public void build(ActivityEventGenerator generator, InvoiceRejection activity, Claim claim)  throws Exception {
            LOG.debug("Building InvoiceRejectedEvent from InvoiceRejection activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.addParameter("rejectionReason", activity.getReasonOfRejection().getRorName());
            generator.addParameter("supportingNote", activity.getRejectionDescription());
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, UpdateManualInvoiceContested activity, Claim claim)  throws Exception {
            LOG.debug("Building InvoiceRejectedEvent from UpdateManualInvoiceContested activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
// No additional parameters available here!
//            generator.addParameter("rejectionReason", activity.);
//            generator.addParameter("supportingNote", activity.);
            generator.completeEvent(claim);
        }
    },
    INSURER_OWNER_ASSIGNED_EVENT            (5, "InsurerOwnerAssignedEvent") {
        @Override
        public void build(ActivityEventGenerator generator, AssignOwner activity, Claim claim)  throws Exception {
            LOG.debug("Building InsurerOwnerAssignedEvent from AssignOwner activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            if (claim.getClaimOwner() != null) {
                generator.addParameter("insurerOwnerName", claim.getClaimOwner().getFullName());
            } else {
                generator.addParameter("insurerOwnerName", null);
            }
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, AssignManualInvoiceOwner activity, Claim claim)  throws Exception {
            LOG.debug("Building InsurerOwnerAssignedEvent from AssignManualInvoiceOwner activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            if (claim.getClaimOwner() != null) {
                generator.addParameter("insurerOwnerName", claim.getClaimOwner().getFullName());
            } else {
                generator.addParameter("insurerOwnerName", null);
            }
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, ClaimReferToFnol activity, Claim claim)  throws Exception {
            LOG.debug("Building InsurerOwnerAssignedEvent from ClaimReferToFnol activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            if (claim.getClaimOwner() != null) {
                generator.addParameter("insurerOwnerName", claim.getClaimOwner().getFullName());
            } else {
                generator.addParameter("insurerOwnerName", null);
            }
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, InsurerUpload activity, Claim claim)  throws Exception {
            LOG.debug("Building InsurerOwnerAssignedEvent from InsurerUpload activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            if (claim.getClaimOwner() != null) {
                generator.addParameter("insurerOwnerName", claim.getClaimOwner().getFullName());
            } else {
                generator.addParameter("insurerOwnerName", null);
            }
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, InvoiceResubmit activity, Claim claim)  throws Exception {
            LOG.debug("Building InsurerOwnerAssignedEvent from InvoiceResubmit activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            if (claim.getClaimOwner() != null) {
                generator.addParameter("insurerOwnerName", claim.getClaimOwner().getFullName());
            } else {
                generator.addParameter("insurerOwnerName", null);
            }
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, NewInvoice activity, Claim claim)  throws Exception {
            LOG.debug("Building InsurerOwnerAssignedEvent from NewInvoice activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            if (claim.getClaimOwner() != null) {
                generator.addParameter("insurerOwnerName", claim.getClaimOwner().getFullName());
            } else {
                generator.addParameter("insurerOwnerName", null);
            }
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, NewTpiClaim activity, Claim claim)  throws Exception {
            LOG.debug("Building InsurerOwnerAssignedEvent from NewTpiClaim activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            if (claim.getClaimOwner() != null) {
                generator.addParameter("insurerOwnerName", claim.getClaimOwner().getFullName());
            } else {
                generator.addParameter("insurerOwnerName", null);
            }
            generator.completeEvent(claim);
        }
    },
    FULL_PAYMENT_RECEIVED_EVENT             (6, "FullPaymentReceivedEvent") {
        @Override
        public void build(ActivityEventGenerator generator, FullInvoicePaymentReceived activity, Claim claim)  throws Exception {
            LOG.debug("Building FullPaymentReceivedEvent from FullInvoicePaymentReceived activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.completeEvent(claim);
        }
    },
    INVOICE_SUBMITTED_EVENT                 (7, "InvoiceSubmittedEvent") {
        @Override
        public void build(ActivityEventGenerator generator, NewInvoice activity, Claim claim)  throws Exception {
            LOG.debug("Building InvoiceSubmittedEvent from NewInvoice activity");
            // First generate CHO event for public BRE messages
            generator.startEvent(claim, this.getName(), this.getEventId(), false, true);
            addInvoiceParameters(generator, claim);
            List breResult = new ArrayList<String>();
            for (History history : History.New(activity.breResponse)) {
                if (history.getType().equals("ERROR") && !history.getIsOld() && history.getIsPublic()) {
                    String result = history.getRuleId() + " : " + history.getType() + " - " + history.getNarrative();
                    breResult.add(result);
                }
            }
            generator.addParameter("breResult", breResult);
            generator.completeEvent(claim);
            // Now generate an Insurer event for all BRE messages
            generator.startEvent(claim, this.getName(), this.getEventId(), true, false);
            addInvoiceParameters(generator, claim);
            breResult = new ArrayList<String>();
            for (History history : History.New(activity.breResponse)) {
                if (history.getType().equals("ERROR") && !history.getIsOld()) {
                    String result = history.getRuleId() + " : " + history.getType() + " - " + history.getNarrative();
                    breResult.add(result);
                }
            }
            generator.addParameter("breResult", breResult);
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, InvoiceResubmit activity, Claim claim)  throws Exception {
            LOG.debug("Building from InvoiceSubmittedEvent from InvoiceResubmit activity");
            // First generate CHO event for public BRE messages
            generator.startEvent(claim, this.getName(), this.getEventId(), false, true);
            addInvoiceParameters(generator, claim);
            List breResult = new ArrayList<String>();
            for (History history : History.New(activity.breResponse)) {
                if (history.getType().equals("ERROR") && !history.getIsOld() && history.getIsPublic()) {
                    String result = history.getRuleId() + " : " + history.getType() + " - " + history.getNarrative();
                    breResult.add(result);
                }
            }
            generator.addParameter("breResult", breResult);
            generator.completeEvent(claim);
            // Now generate an Insurer event for all BRE messages
            generator.startEvent(claim, this.getName(), this.getEventId(), true, false);
            addInvoiceParameters(generator, claim);
            breResult = new ArrayList<String>();
            for (History history : History.New(activity.breResponse)) {
                if (history.getType().equals("ERROR") && !history.getIsOld()) {
                    String result = history.getRuleId() + " : " + history.getType() + " - " + history.getNarrative();
                    breResult.add(result);
                }
            }
            generator.addParameter("breResult", breResult);
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, InvoiceRejectionContest activity, Claim claim)  throws Exception {
            LOG.debug("Building from InvoiceSubmittedEvent from InvoiceRejectionContest activity");
            // First generate CHO event for public BRE messages
            generator.startEvent(claim, this.getName(), this.getEventId(), false, true);
            addInvoiceParameters(generator, claim);
            List breResult = new ArrayList<String>();
            for (History history : History.New(activity.breResponse)) {
                if (history.getType().equals("ERROR") && !history.getIsOld() && history.getIsPublic()) {
                    String result = history.getRuleId() + " : " + history.getType() + " - " + history.getNarrative();
                    breResult.add(result);
                }
            }
            generator.addParameter("breResult", breResult);
            generator.completeEvent(claim);
            // Now generate an Insurer event for all BRE messages
            generator.startEvent(claim, this.getName(), this.getEventId(), true, false);
            addInvoiceParameters(generator, claim);
            breResult = new ArrayList<String>();
            for (History history : History.New(activity.breResponse)) {
                if (history.getType().equals("ERROR") && !history.getIsOld()) {
                    String result = history.getRuleId() + " : " + history.getType() + " - " + history.getNarrative();
                    breResult.add(result);
                }
            }
            generator.addParameter("breResult", breResult);
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, InsurerUpload activity, Claim claim) throws Exception {
            LOG.debug("Building BreResultEvent from InsurerUpload activity");
            // First generate CHO event for public BRE messages
            generator.startEvent(claim, this.getName(), this.getEventId(), false, true);
            addInvoiceParameters(generator, claim);
            List breResult = new ArrayList<String>();
            for (History history : History.New(activity.breResponse)) {
                if (history.getType().equals("ERROR") && !history.getIsOld() && history.getIsPublic()) {
                    String result = history.getRuleId() + " : " + history.getType() + " - " + history.getNarrative();
                    breResult.add(result);
                }
            }
            generator.addParameter("breResult", breResult);
            generator.completeEvent(claim);
            // Now generate an Insurer event for all BRE messages
            generator.startEvent(claim, this.getName(), this.getEventId(), true, false);
            addInvoiceParameters(generator, claim);
            breResult = new ArrayList<String>();
            for (History history : History.New(activity.breResponse)) {
                if (history.getType().equals("ERROR") && !history.getIsOld()) {
                    String result = history.getRuleId() + " : " + history.getType() + " - " + history.getNarrative();
                    breResult.add(result);
                }
            }
            generator.addParameter("breResult", breResult);
            generator.completeEvent(claim);
        }       
    },
    CLAIM_CLOSED_EVENT                      (8, "ClaimClosedEvent") {
        @Override
        public void build(ActivityEventGenerator generator, CloseClaim activity, Claim claim)  throws Exception {
            LOG.debug("Building ClaimClosedEvent from CloseClaim activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, SwitchClaim activity, Claim claim)  throws Exception {
            LOG.debug("Building ClaimClosedEvent from SwitchClaim activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, SwitchClaimToMultipleInsurer activity, Claim claim)  throws Exception {
            LOG.debug("Building ClaimClosedEvent from SwitchClaimToMultipleInsurer activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.completeEvent(claim);
        }
    },
    INTERIM_PAYMENT_RECEIVED_EVENT          (9, "InterimPaymentReceivedEvent") {
        @Override
        public void build(ActivityEventGenerator generator, UpdateInterimPaymentReceived activity, Claim claim)  throws Exception {
            LOG.debug("Building InterimPaymentReceivedEvent from UpdateInterimPaymentReceived activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.addParameter("amountReceived", activity.getPartialInterimPayment());
            generator.completeEvent(claim);
        }
    },
    CLAIM_ROUTED_EVENT                      (10, "ClaimRoutedEvent") {
        @Override
        public void build(ActivityEventGenerator generator, AssignWorkgroup activity, Claim claim)  throws Exception {
            LOG.debug("Building ClaimRoutedEvent from AssignWorkgroup activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            if (claim.getWorkgroup() != null) {
                generator.addParameter("insurerWorkgroupName", claim.getWorkgroup().getName());
            } else {
                generator.addParameter("insurerWorkgroupName", null);
            }
//            generator.addParameter("insurerWorkgroupId", claim.getWorkgroup().getId().intValue());
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, ClaimReferToFnol activity, Claim claim)  throws Exception {
            LOG.debug("Building ClaimRoutedEvent from ClaimReferToFnol activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            if (claim.getWorkgroup() != null) {
                generator.addParameter("insurerWorkgroupName", claim.getWorkgroup().getName());
            } else {
                generator.addParameter("insurerWorkgroupName", null);
            }
//            generator.addParameter("insurerWorkgroupId", claim.getWorkgroup().getId().intValue());
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, InsurerUpload activity, Claim claim)  throws Exception {
            LOG.debug("Building ClaimRoutedEvent from InsurerUpload activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            if (claim.getWorkgroup() != null) {
                generator.addParameter("insurerWorkgroupName", claim.getWorkgroup().getName());
            } else {
                generator.addParameter("insurerWorkgroupName", null);
            }
//            generator.addParameter("insurerWorkgroupId", claim.getWorkgroup().getId().intValue());
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, InvoiceResubmit activity, Claim claim)  throws Exception {
            LOG.debug("Building ClaimRoutedEvent from InvoiceResubmit activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            if (claim.getWorkgroup() != null) {
                generator.addParameter("insurerWorkgroupName", claim.getWorkgroup().getName());
            } else {
                generator.addParameter("insurerWorkgroupName", null);
            }
//            generator.addParameter("insurerWorkgroupId", claim.getWorkgroup().getId().intValue());
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, NewInvoice activity, Claim claim)  throws Exception {
            LOG.debug("Building ClaimRoutedEvent from NewInvoice activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            if (claim.getWorkgroup() != null) {
                generator.addParameter("insurerWorkgroupName", claim.getWorkgroup().getName());
            } else {
                generator.addParameter("insurerWorkgroupName", null);
            }
//            generator.addParameter("insurerWorkgroupId", claim.getWorkgroup().getId().intValue());
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, NewTpiClaim activity, Claim claim)  throws Exception {
            LOG.debug("Building ClaimRoutedEvent from NewTpiClaim activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            if (claim.getWorkgroup() != null) {
                generator.addParameter("insurerWorkgroupName", claim.getWorkgroup().getName());
            } else {
                generator.addParameter("insurerWorkgroupName", null);
            }
//            generator.addParameter("insurerWorkgroupId", claim.getWorkgroup().getId().intValue());
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, WorkgroupRouting activity, Claim claim)  throws Exception {
            LOG.debug("Building ClaimRoutedEvent from WorkgroupRouting activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            if (claim.getWorkgroup() != null) {
                generator.addParameter("insurerWorkgroupName", claim.getWorkgroup().getName());
            } else {
                generator.addParameter("insurerWorkgroupName", null);
            }
//            generator.addParameter("insurerWorkgroupId", claim.getWorkgroup().getId().intValue());
            generator.completeEvent(claim);
        }
    },
    INTERIM_PAYMENT_UPDATED_EVENT           (11, "InterimPaymentUpdatedEvent") {
        @Override
        public void build(ActivityEventGenerator generator, MakeInterimPayment activity, Claim claim) throws Exception {
            LOG.debug("Building InterimPaymentUpdatedEvent from MakeInterimPayment activity");
            generator.startEvent(claim, new StringBuilder().append(this.getName()).toString(), this.getEventId());
            generator.addParameter("additionalInterimPaymentMade", activity.getAdditionalInterimPayment());
            generator.addParameter("totalInterimPaymentMade", activity.getNewTotalInterimPayment());
            generator.completeEvent(claim);
        }       
    },
    ECD_UPDATED_EVENT                       (12, "EcdUpdatedEvent") {
        @Override
        public void build(ActivityEventGenerator generator, EcdUpdate activity, Claim claim) throws Exception {
            LOG.debug("Building EcdUpdatedEvent from EcdUpdate activity");
            generator.startEvent(claim, new StringBuilder().append(this.getName()).toString(), this.getEventId());
            generator.addParameter("hireMonitoringEcdDate", DateHelper.getLocalDateFormat().format(activity.getEcdDate()));
            generator.addParameter("hireMonitoringEcdSupportingNote", activity.getSupportingNote());
            generator.addParameter("hireMonitoringEcdReason", activity.getReason());
//            addClaimHireMonitoringEcdParameters(generator, claim);
            generator.completeEvent(claim);
        }       
    },
    CLAIM_REVERTED_EVENT                    (13, "ClaimRevertedEvent") {
        @Override
        public void build(ActivityEventGenerator generator, RevertClaim activity, Claim claim)  throws Exception {
            LOG.debug("Building from ClaimRevertedEvent from RevertClaim activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, ReopenClaim activity, Claim claim)  throws Exception {
            LOG.debug("Building from ClaimRevertedEvent from ReopenClaim activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, UpdateInterimPaymentFullAndFinal activity, Claim claim)  throws Exception {
            LOG.debug("Building from ClaimRevertedEvent from UpdateInterimPaymentFullAndFinal activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.completeEvent(claim);
        }
    },
    LIABILITY_UPDATED_EVENT                 (14, "LiabilityUpdatedEvent") {
        @Override
        public void build(ActivityEventGenerator generator, AcknowledgeClaim activity, Claim claim)  throws Exception {
            LOG.debug("Building from LiabilityUpdatedEvent from AcknowledgeClaim activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.addParameter("supportingNote", activity.getSupportingLiabilityNotes());
            this.addClaimLiabilityParameters(generator, claim);
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, ClaimPending activity, Claim claim)  throws Exception {
            LOG.debug("Building from LiabilityUpdatedEvent from ClaimPending activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.addParameter("supportingNote", activity.getSupportingLiabilityNotes());
            this.addClaimLiabilityParameters(generator, claim);
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, ClaimReferToEng activity, Claim claim)  throws Exception {
            LOG.debug("Building from LiabilityUpdatedEvent from ClaimReferToEng activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.addParameter("supportingNote", activity.getSupportingLiabilityNotes());
            this.addClaimLiabilityParameters(generator, claim);
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, ClaimReferToFnol activity, Claim claim)  throws Exception {
            LOG.debug("Building from LiabilityUpdatedEvent from ClaimReferToFnol activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.addParameter("supportingNote", activity.getSupportingLiabilityNotes());
            this.addClaimLiabilityParameters(generator, claim);
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, ClaimRejection activity, Claim claim)  throws Exception {
            LOG.debug("Building from LiabilityUpdatedEvent from ClaimRejection activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.addParameter("supportingNote", activity.getSupportingLiabilityNotes());
            this.addClaimLiabilityParameters(generator, claim);
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, ResolveLiability activity, Claim claim)  throws Exception {
            LOG.debug("Building from LiabilityUpdatedEvent from ResolveLiability activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.addParameter("supportingNote", activity.getEngineerClaimReviewNotes());
            this.addClaimLiabilityParameters(generator, claim);
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, UpdateLiability activity, Claim claim)  throws Exception {
            LOG.debug("Building from LiabilityUpdatedEvent from UpdateLiability activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.addParameter("supportingNote", activity.getClaimReviewNotes());
            this.addClaimLiabilityParameters(generator, claim);
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, NewTpiClaim activity, Claim claim)  throws Exception {
            LOG.debug("Building from LiabilityUpdatedEvent from NewTpiClaim activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
//            generator.addParameter("supportingNote", "");
            this.addClaimLiabilityParameters(generator, claim);
            generator.completeEvent(claim);
        }
    },
    INVOICE_REJECTION_CONTESTED_EVENT       (15, "InvoiceRejectionContestedEvent") {
        @Override
        public void build(ActivityEventGenerator generator, InvoiceRejectionContest activity, Claim claim)  throws Exception {
            LOG.debug("Building InvoiceRejectionContestEvent from InvoiceRejectionContest activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.addParameter("supportingNote", activity.getSupportingLiabilityNotes());
            generator.completeEvent(claim);
        }
    },
    SLA_EXTENSION_GRANTED_EVENT             (16, "SlaExtensionGrantedEvent") {
        @Override
        public void build(ActivityEventGenerator generator, SlaExtension activity, Claim claim)  throws Exception {
            LOG.debug("Building SlaExtensionGrantedEvent from SlaExtension activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.addParameter("extensionDays", activity.getSlaExtDays());
            generator.completeEvent(claim);
        }
    },
    INVOICE_REJECTION_ACCEPTED_EVENT        (17, "InvoiceRejectionAcceptedEvent") {
        @Override
        public void build(ActivityEventGenerator generator, InvoiceRejectionAccept activity, Claim claim)  throws Exception {
            LOG.debug("Building InvoiceRejectionAcceptedEvent from InvoiceRejectionAccept activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.addParameter("supportingNote", activity.getSupportingLiabilityNotes());
            generator.completeEvent(claim);
        }
    },
    CLAIM_NUMBER_ASSIGNED_EVENT             (18, "ClaimNumberAssignedEvent") {
        @Override
        public void build(ActivityEventGenerator generator, ClaimRegisterByFnol activity, Claim claim)  throws Exception {
            LOG.debug("Building ClaimNumberAssignedEvent from ClaimRegisterByFnol activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.addParameter("claimNumber", claim.getClaimNumber());
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, Claim claim) throws Exception {
            LOG.warn("Building ClaimNumberAssignedEvent event (not from activity!)...");
            generator.startEvent(claim, new StringBuilder().append(this.getName()).append("[*]").toString(), this.getEventId());
            generator.addParameter("claimNumber", claim.getClaimNumber());
            generator.completeEvent(claim);
        }
    },
    INTERIM_PAYMENT_ACCEPTED_AS_FINAL_EVENT (19, "InterimPaymentAcceptedAsFinalEvent") {
        @Override
        public void build(ActivityEventGenerator generator, UpdateInterimPaymentFullAndFinal activity, Claim claim)  throws Exception {
            LOG.debug("Building InterimPaymentAcceptedAsFinalEvent from UpdateInterimPaymentFullAndFinal activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.addParameter("amountReceived", claim.getInvoice().getInterimPaymentReceived());
            generator.completeEvent(claim);
        }
    },
    NEW_CLAIM_EVENT                         (20, "NewClaimEvent") {
        @Override
        public void build(ActivityEventGenerator generator, NewClaim activity, Claim claim)  throws Exception {
            LOG.debug("Building NewClaimEvent from NewClaim activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            this.addAllClaimParameters(generator, claim);
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, InsurerUpload activity, Claim claim)  throws Exception {
            LOG.debug("Building NewClaimEvent from InsurerUpload activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            this.addAllClaimParameters(generator, claim);
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, NewTpiClaim activity, Claim claim)  throws Exception {
            LOG.debug("Building NewClaimEvent from NewTpiClaim activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            this.addAllClaimParameters(generator, claim);
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, SwitchClaim activity, Claim claim)  throws Exception {
            LOG.debug("Building NewClaimEvent from SwitchClaim activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            this.addAllClaimParameters(generator, claim);
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, SwitchClaimToMultipleInsurer activity, Claim claim)  throws Exception {
            LOG.debug("Building NewClaimEvent from SwitchClaimToMultipleInsurer activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            this.addAllClaimParameters(generator, claim);
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, NewSupplementaryInvoice activity, Claim claim)  throws Exception {
            LOG.debug("Building NewClaimEvent from NewSupplementaryInvoice activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            this.addAllClaimParameters(generator, claim);
            generator.completeEvent(claim);
        }
    },
    HIRE_CAR_INFO_PROVIDED_EVENT            (21, "HireCarInfoProvidedEvent") {
        @Override
        public void build(ActivityEventGenerator generator, ClaimAwaitingCarHireInfo activity, Claim claim)  throws Exception {
            LOG.debug("Building HireCarInfoProvidedEvent from ClaimAwaitingCarHireInfo activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            this.addClaimHireMonitoringParameters(generator, claim);
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, SubscriberClaimRejectionAccept activity, Claim claim)  throws Exception {
            LOG.debug("Building HireCarInfoProvidedEvent from SubscriberClaimRejectionAccept activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            this.addClaimHireMonitoringParameters(generator, claim);
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, NewSupplementaryInvoice activity, Claim claim)  throws Exception {
            LOG.debug("Building HireCarInfoProvidedEvent from NewSupplementaryInvoice activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            this.addClaimHireMonitoringParameters(generator, claim);
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, NewTpiClaim activity, Claim claim)  throws Exception {
            LOG.debug("Building HireCarInfoProvidedEvent from NewTpiClaim activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            this.addClaimHireMonitoringParameters(generator, claim);
            generator.completeEvent(claim);
        }
    },
    CLAIM_REJECTED_EVENT                    (22, "ClaimRejectedEvent") {
        @Override
        public void build(ActivityEventGenerator generator, ClaimRejection activity, Claim claim)  throws Exception {
            LOG.debug("Building ClaimRejectedEvent from ClaimRejection activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.addParameter("rejectionReason", claim.getReasonOfRejection().getRorName());
            generator.addParameter("supportingNote", activity.getRejectionDescription());
            generator.completeEvent(claim);
        }
    },
    INVOICE_PAID_EVENT                      (23, "InvoicePaidEvent") {
        @Override
        public void build(ActivityEventGenerator generator, InvoicePaymentLogged activity, Claim claim)  throws Exception {
            LOG.debug("Building InvoicePaidEvent from InvoicePaymentLogged activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            this.addInvoicePaidParameters(generator, claim);
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, UpdateManualInvoicePaid activity, Claim claim)  throws Exception {
            LOG.debug("Building InvoicePaidEvent from InvoicePaymentLogged activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            this.addInvoicePaidParameters(generator, claim);
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, MoveToInvoicePaymentLogged activity, Claim claim)  throws Exception {
            LOG.debug("Building InvoicePaidEvent from MoveToInvoicePaymentLogged activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            this.addInvoicePaidParameters(generator, claim);
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, UpdateInterimPaymentFullAndFinal activity, Claim claim)  throws Exception {
            LOG.debug("Building InvoicePaidEvent from UpdateInterimPaymentFullAndFinal activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            this.addInvoicePaidParameters(generator, claim);
            generator.completeEvent(claim);
        }
    },
    CHO_OWNER_ASSIGNED_EVENT                (24, "ChoOwnerAssignedEvent") {
        @Override
        public void build(ActivityEventGenerator generator, AssignSupplierOwner activity, Claim claim)  throws Exception {
            LOG.debug("Building ChoOwnerAssignedEvent from AssignSupplierOwner activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            if (claim.getSupplierClaimOwner() != null) {
                generator.addParameter("choOwnerName", claim.getSupplierClaimOwner().getFullName());
            } else {
                generator.addParameter("choOwnerName", null);
            }
            generator.completeEvent(claim);
        }
    },
    FULL_PAYMENT_NOT_RECEIVED_EVENT         (25, "FullPaymentNotReceivedEvent") {
        @Override
        public void build(ActivityEventGenerator generator, FullPaymentNotReceived activity, Claim claim)  throws Exception {
            LOG.debug("Building FullPaymentNotReceivedEvent from FullPaymentNotReceived activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.addParameter("amountReceived", activity.getInterimPaymentReceived());
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, PaymentNotReceived activity, Claim claim)  throws Exception {
            LOG.debug("Building FullPaymentNotReceivedEvent from PaymentNotReceived activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.addParameter("amountReceived", activity.getAmountReceived());
            generator.completeEvent(claim);
        }
    },
    CLAIM_REJECTION_ACCEPTED_EVENT          (26, "ClaimRejectionAcceptedEvent") {
        @Override
        public void build(ActivityEventGenerator generator, ClaimRejectionAccept activity, Claim claim)  throws Exception {
            LOG.debug("Building ClaimRejectionAcceptedEvent from ClaimRejectionAccept activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, SubscriberClaimRejectionAccept activity, Claim claim)  throws Exception {
            LOG.debug("Building ClaimRejectionAcceptedEvent from SubscriberClaimRejectionAccept activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.completeEvent(claim);
        }
    },
    CLAIM_PENDING_EVENT                     (27, "ClaimPendingEvent") {
        @Override
        public void build(ActivityEventGenerator generator, ClaimPending activity, Claim claim)  throws Exception {
            LOG.debug("Building ClaimPendingEvent from ClaimPending activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.completeEvent(claim);
        }
    },
    INVOICE_PAYMENT_RECEIVED_EVENT          (28, "InvoicePaymentReceivedEvent") {
        @Override
        public void build(ActivityEventGenerator generator, InvoicePaymentReceived activity, Claim claim)  throws Exception {
            LOG.debug("Building InvoicePaymentReceivedEvent from InvoicePaymentReceived activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.completeEvent(claim);
        }
    },
    CLAIM_REJECTION_CONTESTED_EVENT         (29, "ClaimRejectionContestedEvent") {
        @Override
        public void build(ActivityEventGenerator generator, ClaimRejectionContest activity, Claim claim)  throws Exception {
            LOG.debug("Building ClaimRejectionContestedEvent from ClaimRejectionContest activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.completeEvent(claim);
        }
    },
    CLAIM_REFERRED_TO_FNOL_EVENT            (30, "ClaimReferToFnolEvent") {
        @Override
        public void build(ActivityEventGenerator generator, ClaimReferToFnol activity, Claim claim)  throws Exception {
            LOG.debug("Building ClaimReferToFnolEvent from ClaimReferToFnol activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.completeEvent(claim);
        }
    },
    CLAIM_REFERRED_TO_ENG_EVENT             (31, "ClaimReferToEngEvent") {
        @Override
        public void build(ActivityEventGenerator generator, ClaimReferToEng activity, Claim claim)  throws Exception {
            LOG.debug("Building ClaimReferToEngEvent from InvoiceAccepted activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.completeEvent(claim);
        }
    },
    INVOICE_ACCEPTED_EVENT                  (32, "InvoiceAcceptedEvent") {
        @Override
        public void build(ActivityEventGenerator generator, InvoiceAccepted activity, Claim claim)  throws Exception {
            LOG.debug("Building InvoiceAcceptedEvent from InvoiceAccepted activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, UpdateManualInvoiceAgreeQuantum activity, Claim claim)  throws Exception {
            LOG.debug("Building InvoiceAcceptedEvent from UpdateManualInvoiceAgreeQuantum activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, InvoiceResubmit activity, Claim claim)  throws Exception {
            LOG.debug("Building InvoiceAcceptedEvent from InvoiceResubmit activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, NewInvoice activity, Claim claim)  throws Exception {
            LOG.debug("Building InvoiceAcceptedEvent from NewInvoice activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, NewTpiClaim activity, Claim claim)  throws Exception {
            LOG.debug("Building InvoiceAcceptedEvent from NewTpiClaim activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.completeEvent(claim);
        }
        @Override
        public void build(ActivityEventGenerator generator, UpdateInterimPaymentFullAndFinal activity, Claim claim)  throws Exception {
            LOG.debug("Building InvoiceAcceptedEvent from UpdateInterimPaymentFullAndFinal activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.completeEvent(claim);
        }
    },
    INVOICE_REFERRED_TO_ENG_EVENT           (33, "InvoiceReferToEngEbent") {
        @Override
        public void build(ActivityEventGenerator generator, InvoiceReferToEng activity, Claim claim)  throws Exception {
            LOG.debug("Building InvoiceReferToEng from InvoiceReferToEng activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.completeEvent(claim);
        }
    },
    CLAIM_REGISTERED_BY_FNOL_EVENT          (34, "ClaimRegisterByFnolEvent") {
        @Override
        public void build(ActivityEventGenerator generator, ClaimRegisterByFnol activity, Claim claim)  throws Exception {
            LOG.debug("Building ClaimRegisterByFnolEvent from ClaimRegisterByFnol activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.completeEvent(claim);
        }
    },
    CLAIM_AWAITING_LITIGATION_OUTCOME_EVENT (35, "ClaimAwaitingLitigationOutcomeEvent") {
        @Override
        public void build(ActivityEventGenerator generator, AwaitingLitigationOutcome activity, Claim claim) throws Exception {
            LOG.debug("Building ClaimAwaitingLitigationOutcomeEvent from AwaitingLitigationOutcome activity");
            generator.startEvent(claim, new StringBuilder().append(this.getName()).toString(), this.getEventId());
            generator.completeEvent(claim);
        }       
    },
    CLAIM_REVIEW_BY_ENG_EVENT               (36, "ClaimReviewByEngEvent") {
        @Override
        public void build(ActivityEventGenerator generator, ClaimReviewByEng activity, Claim claim)  throws Exception {
            LOG.debug("Building from ClaimReviewByEngEvent from ClaimReviewByEng activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.completeEvent(claim);
        }
    },
    INVOICE_REFERRED_TO_CH_EVENT            (37, "InvoiceReferToCHEvent") {
        @Override
        public void build(ActivityEventGenerator generator, InvoiceReferToCH activity, Claim claim)  throws Exception {
            LOG.debug("Building from InvoiceReferToCHEvent from InvoiceReferToCH activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.completeEvent(claim);
        }
    },
    SUBSCRIBER_CLAIM_REJECTED_GTA             (38, "SubscriberClaimRejectedToGTA") {
        @Override
        public void build(ActivityEventGenerator generator, SubscriberClaimToGta activity, Claim claim)  throws Exception {
            LOG.debug("Building SubscriberClaimRejectedToGTA from SubscriberClaimToGta activity");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.completeEvent(claim);
        }
    },
//    NEW_SUPPLEMENTARY_INVOICE_EVENT         (39, "NewSupplementaryInvoice"), //TODO - not sure if needed (should generate new claim, carhireinfo provided, new invoice?)
    // Non-activity based events - should be moved to ChoxEvents in data package TODO
//    ATTACHMENT_UPLOADED_EVENT               (50, "AttachmentUploadedEvent")  - moved
    NOTE_ADDED_EVENT                        (51, "NoteAddedEvent") {
        @Override
        public void build(ActivityEventGenerator generator, Claim claim, Comment comment)  throws Exception {
            LOG.debug("Building NoteAddedEvent");
            // 0 - ALL, 1 - INSURER ONLY, 2 - CREDIT HIRE ONLY
            if (comment.getVisibilityType() == 0) { // All
                generator.startEvent(claim, this.getName(), this.getEventId());
            } else if (comment.getVisibilityType() == 1) { // Insurer Only
                generator.startEvent(claim, this.getName(), this.getEventId(), true, false);
            } else if (comment.getVisibilityType() == 2) { // CHO Only
                generator.startEvent(claim, this.getName(), this.getEventId(), false, true);
            }
            generator.addParameter("note", comment.getComment());
            generator.addParameter("createdBy", comment.getCreatedBy().getFullName());
            generator.completeEvent(claim);
        }
    },
    CLAIM_UPDATED_EVENT                     (52, "ClaimUpdatedEvent") {
        @Override
        public void build(ActivityEventGenerator generator, Claim claim)  throws Exception {
            LOG.debug("Building ClaimUpdatedEvent");
            generator.startEvent(claim, this.getName(), this.getEventId());
            this.addAllClaimParameters(generator, claim);
            generator.completeEvent(claim);
        }
    },
    INVOICE_UPDATED_EVENT                   (53, "InvoiceUpdatedEvent") {
        @Override
        public void build(ActivityEventGenerator generator, Claim claim)  throws Exception {
            LOG.debug("Building InvoiceUpdatedEvent");
            generator.startEvent(claim, this.getName(), this.getEventId());
            this.addInvoiceParameters(generator, claim);
            generator.completeEvent(claim);
        }
    },
    HIRE_MONITORING_UPDATED_EVENT           (54, "HireMonitoringUpdatedEvent") {
        @Override
        public void build(ActivityEventGenerator generator, Claim claim)  throws Exception {
            LOG.debug("Building HireMonitoringUpdatedEvent");
            generator.startEvent(claim, this.getName(), this.getEventId());
            this.addClaimHireMonitoringParameters(generator, claim);
            generator.completeEvent(claim);
        }
    };

    private static final Logger LOG = LoggerFactory.getLogger(ActivityEvent.class);
    private final int eventId;
    private final String name;

    public int getEventId() {
        return eventId;
    }

    public String getName() {
        return name;
    }

    ActivityEvent(int eventId, String name) {
        this.eventId = eventId;
        this.name = name;
    }
    
    @Override
    public String toString() {
        return name;
    }

    public void build(ActivityEventGenerator generator, Claim claim) throws Exception {
        LOG.warn("Building generic non-activity based event...");
        generator.startEvent(claim, new StringBuilder().append(this.getName()).append("[*]").toString(), this.getEventId());
        generator.completeEvent(claim);
    }

    public void build(ActivityEventGenerator generator, Claim claim, Comment comment) throws Exception {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void build(ActivityEventGenerator generator, Claim claim, Attachment attachment) throws Exception {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void build(ActivityEventGenerator generator, Activity activity, Claim claim) throws Exception {
        LOG.warn("No activity-specific Events to build for activity {}", AopUtils.getTargetClass(activity).getSimpleName());
//        generator.startEvent(claim, new StringBuilder().append(this.getName()).append("[*]").toString(), this.getEventId());
        generator.startEvent(claim, new StringBuilder().append(this.getName()).toString(), this.getEventId());
        generator.completeEvent(claim);
    }

    public void build(ActivityEventGenerator generator, AcknowledgeClaim activity, Claim claim)  throws Exception {
        LOG.warn("Build with AcknowledgeClaim activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }
    public void build(ActivityEventGenerator generator, AssignManualInvoiceOwner activity, Claim claim)  throws Exception {
        LOG.warn("Build with AssignManualInvoiceOwner activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, AssignOwner activity, Claim claim)  throws Exception {
        LOG.warn("Build with AssignOwner activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, AssignSupplierOwner activity, Claim claim)  throws Exception {
        LOG.warn("Build with AssignSupplierOwner activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, AssignWorkgroup activity, Claim claim)  throws Exception {
        LOG.warn("Build with AssignWorkgroup activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, AwaitingLitigationOutcome activity, Claim claim)  throws Exception {
        LOG.warn("Build with AwaitingLitigationOutcome activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, ClaimAwaitingCarHireInfo activity, Claim claim)  throws Exception {
        LOG.warn("Build with ClaimAwaitingCarHireInfo activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, ClaimPending activity, Claim claim)  throws Exception {
        LOG.warn("Build with ClaimPending activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, ClaimReferToEng activity, Claim claim)  throws Exception {
        LOG.warn("Build with ClaimReferToEng activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, ClaimReferToFnol activity, Claim claim)  throws Exception {
        LOG.warn("Build with ClaimReferToFnol activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, ClaimRegisterByFnol activity, Claim claim)  throws Exception {
        LOG.warn("Build with ClaimRegisterByFnol activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, ClaimRejection activity, Claim claim)  throws Exception {
        LOG.warn("Build with ClaimRejection activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, ClaimRejectionAccept activity, Claim claim)  throws Exception {
        LOG.warn("Build with ClaimRejectionAccept activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, ClaimRejectionContest activity, Claim claim)  throws Exception {
        LOG.warn("Build with ClaimRejectionContest activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, ClaimReviewByEng activity, Claim claim)  throws Exception {
        LOG.warn("Build with ClaimReviewByEng activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, CloseClaim activity, Claim claim)  throws Exception {
        LOG.warn("Build with CloseClaim activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, EcdUpdate activity, Claim claim)  throws Exception {
        LOG.warn("Build with EcdUpdate activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, FullInvoicePaymentReceived activity, Claim claim)  throws Exception {
        LOG.warn("Build with FullInvoicePaymentReceived activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, FullPaymentNotReceived activity, Claim claim)  throws Exception {
        LOG.warn("Build with FullPaymentNotReceived activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, InsurerUpload activity, Claim claim)  throws Exception {
        LOG.warn("Build with InsurerUpload activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, InvoiceAccepted activity, Claim claim)  throws Exception {
        LOG.warn("Build with InvoiceAccepted activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, InvoicePaymentLogged activity, Claim claim)  throws Exception {
        LOG.warn("Build with InvoicePaymentLogged activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, InvoicePaymentReceived activity, Claim claim)  throws Exception {
        LOG.warn("Build with InvoicePaymentReceived activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, InvoiceReferToCH activity, Claim claim)  throws Exception {
        LOG.warn("Build with InvoiceReferToCH activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, InvoiceReferToEng activity, Claim claim)  throws Exception {
        LOG.warn("Build with InvoiceReferToEng activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, InvoiceRejection activity, Claim claim)  throws Exception {
        LOG.warn("Build with InvoiceRejection activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, InvoiceRejectionAccept activity, Claim claim)  throws Exception {
        LOG.warn("Build with InvoiceRejectionAccept activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, InvoiceRejectionContest activity, Claim claim)  throws Exception {
        LOG.warn("Build with InvoiceRejectionContest activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, InvoiceResubmit activity, Claim claim)  throws Exception {
        LOG.warn("Build with InvoiceResubmit activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, MakeInterimPayment activity, Claim claim)  throws Exception {
        LOG.warn("Build with MakeInterimPayment activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, MoveToInvoicePaymentLogged activity, Claim claim)  throws Exception {
        LOG.warn("Build with MoveToInvoicePaymentLogged activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, NewClaim activity, Claim claim)  throws Exception {
        LOG.warn("Build with NewClaim activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, NewInvoice activity, Claim claim)  throws Exception {
        LOG.warn("Build with NewInvoice activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, NewSupplementaryInvoice activity, Claim claim)  throws Exception {
        LOG.warn("Build with NewSupplementaryInvoice activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, NewTpiClaim activity, Claim claim)  throws Exception {
        LOG.warn("Build with NewTpiClaim activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, PaymentNotReceived activity, Claim claim)  throws Exception {
        LOG.warn("Build with PaymentNotReceived activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, ReopenClaim activity, Claim claim)  throws Exception {
        LOG.warn("Build with ReopenClaim activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, ResolveLiability activity, Claim claim)  throws Exception {
        LOG.warn("Build with ResolveLiability activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, RevertClaim activity, Claim claim)  throws Exception {
        LOG.warn("Build with RevertClaim activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, SlaExtension activity, Claim claim)  throws Exception {
        LOG.warn("Build with SlaExtension activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, SubscriberClaimRejectionAccept activity, Claim claim)  throws Exception {
        LOG.warn("Build with SubscriberClaimRejectionAccept activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, SubscriberClaimToGta activity, Claim claim)  throws Exception {
        LOG.warn("Build with SubscriberClaimToGta activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, SwitchClaim activity, Claim claim)  throws Exception {
        LOG.warn("Build with SwitchClaim activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, SwitchClaimToMultipleInsurer activity, Claim claim)  throws Exception {
        LOG.warn("Build with SwitchClaimToMultipleInsurer activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, UpdateInterimPaymentFullAndFinal activity, Claim claim)  throws Exception {
        LOG.warn("Build with UpdateInterimPaymentFullAndFinal activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, UpdateInterimPaymentReceived activity, Claim claim)  throws Exception {
        LOG.warn("Build with UpdateInterimPaymentReceived activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, UpdateLiability activity, Claim claim)  throws Exception {
        LOG.warn("Build with UpdateLiability activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, UpdateManualInvoiceAgreeQuantum activity, Claim claim)  throws Exception {
        LOG.warn("Build with UpdateManualInvoiceAgreeQuantum activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, UpdateManualInvoiceContested activity, Claim claim)  throws Exception {
        LOG.warn("Build with UpdateManualInvoiceContested activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, UpdateManualInvoicePaid activity, Claim claim)  throws Exception {
        LOG.warn("Build with UpdateManualInvoicePaid activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void build(ActivityEventGenerator generator, WorkgroupRouting activity, Claim claim)  throws Exception {
        LOG.warn("Build with WorkgroupRouting activity called and no overiding method - will call generic event builder");
        build(generator, (Activity)activity, claim);
    }

    public void addAllClaimParameters(ActivityEventGenerator generator, Claim claim) {
        addClaimParameters(generator, claim);
        addClaimCustomerParameters(generator, claim);
        addClaimCustomerVehicleParameters(generator, claim);
        addClaimCustomerMitigationParameters(generator, claim);
        addClaimCustomerIncidentParameters(generator, claim);
        addClaimCustomerIncidentWitnessParameters(generator, claim);
        addClaimCustomerIncidentInjuryParameters(generator, claim);
        addClaimCustomerIncidentInjurySolicitorParameters(generator, claim);
        addClaimThirdPartyParameters(generator, claim);
        addClaimThirdPartyVehicleParameters(generator, claim);
        addClaimEngineerReportParameters(generator, claim);
        addClaimHireVehicleParameters(generator, claim);
        addClaimHireMonitoringParameters(generator, claim);
//        addClaimHireMonitoringEcdParameters(generator, claim);
    }
    
    
    public void addClaimParameters(ActivityEventGenerator generator, Claim claim) {
        generator.addParameter("managingRepair", claim.isManagingRepair());
        generator.addParameter("choReference", claim.getChoReference());
        generator.addParameter("status", claim.getStatus());
        generator.addParameter("insurerName", claim.getInsurer().getName());
        generator.addParameter("choName", claim.getChorganisation().getName());
        generator.addParameter("claimNumber", claim.getClaimNumber());
        generator.addParameter("isQuantumDispute", claim.getIsQuantumDispute());
        generator.addParameter("isInvoiceReviewRequired", claim.getIsInvoiceReviewRequired());
        generator.addParameter("claimType", claim.getClaimType().toString());
        if (claim.getPolicyHolderContactDate() != null) {
            generator.addParameter("policyHolderContactDate", DateHelper.getLocalDateFormat().format(claim.getPolicyHolderContactDate()));
        } else {
            generator.addParameter("policyHolderContactDate", null);
        }
        if (claim.getCreditAgreementDate() != null) {
            generator.addParameter("creditAgreementDate", DateHelper.getLocalDateFormat().format(claim.getCreditAgreementDate()));
        } else {
            generator.addParameter("creditAgreementDate", null);
        }
        if (claim.getGtaNoticeDate() != null) {
            generator.addParameter("gtaNoticeDate", DateHelper.getLocalDateFormat().format(claim.getGtaNoticeDate()));
        } else {
            generator.addParameter("gtaNoticeDate", null);
        }
        if (claim.getFinalReviewByCho() != null) {
            generator.addParameter("finalReviewCho", claim.getFinalReviewByCho().getDisplayName());
        } else {
            generator.addParameter("finalReviewCho", null);
        }
        if (claim.getFinalReviewByIns() != null) {
            generator.addParameter("finalReviewInsurer", claim.getFinalReviewByIns().getDisplayName());
        } else {
            generator.addParameter("finalReviewInsurer", null);
        }
        if (claim.getFinalReviewDateIns() != null) {
            generator.addParameter("finalReviewDateCho", DateHelper.getLocalDateFormat().format(claim.getFinalReviewDateCho()));
        } else {
            generator.addParameter("finalReviewDateCho", null);
        }
        if (claim.getFinalReviewDateIns() != null) {
            generator.addParameter("finalReviewDateInsurer", DateHelper.getLocalDateFormat().format(claim.getFinalReviewDateIns()));
        } else {
            generator.addParameter("finalReviewDateInsurer", null);
        }
        if (claim.getSupplierClaimOwner() != null) {
            generator.addParameter("choOwnerName", claim.getSupplierClaimOwner().getDisplayName());
        } else {
            generator.addParameter("choOwnerName", null);
        }
        if (claim.getClaimOwner() != null) {
            generator.addParameter("insurerOwnerName", claim.getClaimOwner().getDisplayName());
        } else {
            generator.addParameter("insurerOwnerName", null);
        }
        if (claim.getWorkgroup() != null) {
            generator.addParameter("insurerWorkgroupName", claim.getWorkgroup().getName());
        } else {
            generator.addParameter("insurerWorkgroupName", null);
        }

    }

    public void addClaimCustomerParameters(ActivityEventGenerator generator, Claim claim) {
        Customer customer = claim.getCustomer();
        if (customer != null) {
            generator.addParameter("customerTitle", customer.getTitle());
            generator.addParameter("customerFirstName", customer.getFirstName());
            generator.addParameter("customerLastName", customer.getLastName());
            generator.addParameter("customerAddress1", customer.getAddress1());
            generator.addParameter("cuatomerAddress2", customer.getAddress2());
            generator.addParameter("cuatomerAddress3", customer.getAddress3());
            generator.addParameter("cuatomerAddress4", customer.getAddress4());
            generator.addParameter("cuatomerAddress5", customer.getAddress5());
            generator.addParameter("customerPostcode", customer.getPostcode());
            generator.addParameter("customerTelephoneDay", customer.getTelephoneDay());
            generator.addParameter("customerTelephoneEvening", customer.getTelephoneEvening());
            generator.addParameter("customerEmail", customer.getEmail());
            generator.addParameter("customerPolicyNumber", customer.getPolicyNumber());
            generator.addParameter("customerClaimNumber", customer.getClaimReference());
            generator.addParameter("customerHasComprehensiveCover", customer.getIsComprehensiveDesc());
            generator.addParameter("customerInsurerName", customer.getInsurerName());
            generator.addParameter("customerAge", customer.getAge());
            generator.addParameter("customerOccupation", customer.getOccupation());
            generator.addParameter("customerPolicyUsage", customer.getPolicyUsage());
        } else {
            generator.addParameter("customerTitle", null);
            generator.addParameter("customerFirstName", null);
            generator.addParameter("customerLastName", null);
            generator.addParameter("customerAddress1", null);
            generator.addParameter("cuatomerAddress2", null);
            generator.addParameter("cuatomerAddress3", null);
            generator.addParameter("cuatomerAddress4", null);
            generator.addParameter("cuatomerAddress5", null);
            generator.addParameter("customerPostcode", null);
            generator.addParameter("customerTelephoneDay", null);
            generator.addParameter("customerTelephoneEvening", null);
            generator.addParameter("customerEmail", null);
            generator.addParameter("customerPolicyNumber", null);
            generator.addParameter("customerClaimNumber", null);
            generator.addParameter("customerHasComprehensiveCover", null);
            generator.addParameter("customerInsurerName", null);
            generator.addParameter("customerAge", null);
            generator.addParameter("customerOccupation", null);
            generator.addParameter("customerPolicyUsage", null);
        }
    }
    
    public void addClaimCustomerVehicleParameters(ActivityEventGenerator generator, Claim claim) {
        Customer customer = claim.getCustomer();
        if (customer != null) {
            generator.addParameter("customerVehicleRegistration", customer.getVehicleRegistration());
            generator.addParameter("customerVehicleManufacturer", customer.getVehicleManufacturer());
            generator.addParameter("customerVehicleModel", customer.getVehicleModel());
            if (customer.getVehicleClass() != null) {
                generator.addParameter("customerVehicleClass", customer.getVehicleClass().getName());
            } else {
                generator.addParameter("customerVehicleClass", null);
            }
            generator.addParameter("customerVehicleLocation", customer.getLocation());
            generator.addParameter("customerVehicleDamage", customer.getDamage());
            if (customer.getInitialECD() != null) {
                generator.addParameter("customerVehicalInitialEcd", DateHelper.getLocalDateFormat().format(customer.getInitialECD()));
            } else {
                generator.addParameter("customerVehicalInitialEcd", null);
            }
            generator.addParameter("customerVehicleIsUsable", customer.getIsUsable());
            generator.addParameter("customerVehicleIsTotalLoss", customer.getIsTotalLossDesc());
            generator.addParameter("customerVehicleYear", customer.getVehicleYear());
        } else {
            generator.addParameter("customerVehicleRegistration", null);
            generator.addParameter("customerVehicleManufacturer", null);
            generator.addParameter("customerVehicleModel", null);
            generator.addParameter("customerVehicleClass", null);
            generator.addParameter("customerVehicleLocation", null);
            generator.addParameter("customerVehicleDamage", null);
            generator.addParameter("customerVehicalInitialEcd", null);
            generator.addParameter("customerVehicleIsUsable", null);
            generator.addParameter("customerVehicleIsTotalLoss", null);
            generator.addParameter("customerVehicleYear", null);
        }
    }
    
    public void addClaimCustomerMitigationParameters(ActivityEventGenerator generator, Claim claim) {
        Customer customer = claim.getCustomer();
        if (customer != null) {
            generator.addParameter("customerCanAccessOtherVehicle", customer.getCanAccessOtherVehicleDesc());
            generator.addParameter("customerOtherVehicleUsed", customer.getOtherVehicleUsedDesc());
            generator.addParameter("customerOtherVehicle", customer.getOtherVehicle());
            generator.addParameter("customerEntitledToCourtesyCar", customer.getCourtesyCarEntitledDesc());
            generator.addParameter("customerSpecificVehicleRequired", customer.getSpecificVehicleRequiredDesc());
            generator.addParameter("customerSpecificVehicleReason", customer.getSpecificVehicleReason());
            generator.addParameter("customerSpecificVehicleType", customer.getTypeVehicleRequired());
            generator.addParameter("customerSpecialRequirements", customer.getSpecialRequirements());
            generator.addParameter("customerAverageDailyMilage", customer.getAverageDailyMileage());
        } else {
            generator.addParameter("customerCanAccessOtherVehicle", null);
            generator.addParameter("customerOtherVehicleUsed", null);
            generator.addParameter("customerOtherVehicle", null);
            generator.addParameter("customerEntitledToCourtesyCar", null);
            generator.addParameter("customerSpecificVehicleRequired", null);
            generator.addParameter("customerSpecificVehicleReason", null);
            generator.addParameter("customerSpecificVehicleType", null);
            generator.addParameter("customerSpecialRequirements", null);
            generator.addParameter("customerAverageDailyMilage", null);
        }
    }
    
    public void addClaimCustomerIncidentParameters(ActivityEventGenerator generator, Claim claim) {
        Incident incident = claim.getIncident();
        if (incident != null) {
            if (incident.getDate() != null) {
                generator.addParameter("incidentDate", DateHelper.getLocalDateFormat().format(incident.getDate()));
            } else {
                generator.addParameter("incidentDate", null);
            }
            generator.addParameter("incidentLocation", incident.getLocation());
            generator.addParameter("incidentDescription", incident.getIncidentDescription());
            generator.addParameter("incidentIsPoliceInvolved", incident.getIsPoliceInvolvedDesc());
        } else {
            generator.addParameter("incidentDate", null);
            generator.addParameter("incidentLocation", null);
            generator.addParameter("incidentDescription", null);
            generator.addParameter("incidentIsPoliceInvolved", null);
        }
    }
    
    public void addClaimCustomerIncidentWitnessParameters(ActivityEventGenerator generator, Claim claim) {
        if (claim.getIncident() != null && claim.getIncident().getWitness() != null) {
            Witness witness = claim.getIncident().getWitness();
            generator.addParameter("witnessName", witness.getName());
            generator.addParameter("witnessAddress1", witness.getAddress1());
            generator.addParameter("witnessAddress2", witness.getAddress2());
            generator.addParameter("witnessAddress3", witness.getAddress3());
            generator.addParameter("witnessAddress4", witness.getAddress4());
            generator.addParameter("witnessAddress5", witness.getAddress5());
            generator.addParameter("witnessPostcode", witness.getPostcode());
            generator.addParameter("witnessTelephoneDay", witness.getTelephoneDay());
            generator.addParameter("witnessTelephoneEvening", witness.getTelephoneEvening());
            generator.addParameter("witnessEmail", witness.getEmail());
        } else {
            generator.addParameter("witnessName", null);
            generator.addParameter("witnessAddress1", null);
            generator.addParameter("witnessAddress2", null);
            generator.addParameter("witnessAddress3", null);
            generator.addParameter("witnessAddress4", null);
            generator.addParameter("witnessAddress5", null);
            generator.addParameter("witnessPostcode", null);
            generator.addParameter("witnessTelephoneDay", null);
            generator.addParameter("witnessTelephoneEvening", null);
            generator.addParameter("witnessEmail", null);
        }
    }
    
    public void addClaimCustomerIncidentInjuryParameters(ActivityEventGenerator generator, Claim claim) {
        if (claim.getIncident() != null && claim.getIncident().getInjury() != null) {
            Injury injury = claim.getIncident().getInjury();
            generator.addParameter("injuryName", injury.getName());
            generator.addParameter("injuryAddress1", injury.getAddress1());
            generator.addParameter("injuryAddress2", injury.getAddress2());
            generator.addParameter("injuryAddress3", injury.getAddress3());
            generator.addParameter("injuryAddress4", injury.getAddress4());
            generator.addParameter("injuryAddress5", injury.getAddress5());
            generator.addParameter("injuryPostcode", injury.getPostcode());
            generator.addParameter("injuryTelephoneDay", injury.getTelephoneDay());
            generator.addParameter("injuryTelephoneEvening", injury.getTelephoneEvening());
            generator.addParameter("injuryEmail", injury.getEmail());
        } else {
            generator.addParameter("injuryName", null);
            generator.addParameter("injuryAddress1", null);
            generator.addParameter("injuryAddress2", null);
            generator.addParameter("injuryAddress3", null);
            generator.addParameter("injuryAddress4", null);
            generator.addParameter("injuryAddress5", null);
            generator.addParameter("injuryPostcode", null);
            generator.addParameter("injuryTelephoneDay", null);
            generator.addParameter("injuryTelephoneEvening", null);
            generator.addParameter("injuryEmail", null);
        }
    }
    
    public void addClaimCustomerIncidentInjurySolicitorParameters(ActivityEventGenerator generator, Claim claim) {
        if (claim.getIncident() != null && claim.getIncident().getInjury() != null
                && claim.getIncident().getInjury().getSolicitor() != null) {
            Solicitor solicitor = claim.getIncident().getInjury().getSolicitor();
            generator.addParameter("injurySolicitorName", solicitor.getName());
            generator.addParameter("injurySolicitorAddress1", solicitor.getAddress1());
            generator.addParameter("injurySolicitorAddress2", solicitor.getAddress2());
            generator.addParameter("injurySolicitorAddress3", solicitor.getAddress3());
            generator.addParameter("injurySolicitorAddress4", solicitor.getAddress4());
            generator.addParameter("injurySolicitorAddress5", solicitor.getAddress5());
            generator.addParameter("injurySolicitorPostcode", solicitor.getPostcode());
            generator.addParameter("injurySolicitorTelephone", solicitor.getTelephone());
            generator.addParameter("injurySolicitorEmail", solicitor.getEmail());
        } else {
            generator.addParameter("injurySolicitorName", null);
            generator.addParameter("injurySolicitorAddress1", null);
            generator.addParameter("injurySolicitorAddress2", null);
            generator.addParameter("injurySolicitorAddress3", null);
            generator.addParameter("injurySolicitorAddress4", null);
            generator.addParameter("injurySolicitorAddress5", null);
            generator.addParameter("injurySolicitorPostcode", null);
            generator.addParameter("injurySolicitorTelephone", null);
            generator.addParameter("injurySolicitorEmail", null);
        }
    }
    
    public void addClaimThirdPartyParameters(ActivityEventGenerator generator, Claim claim) {
        ThirdParty thirdParty = claim.getThirdParty();
        if (thirdParty != null) {
            if (thirdParty.getInsurer() != null) {
                generator.addParameter("thirdPartyInsurerName", thirdParty.getInsurer().getName());
            } else {
                generator.addParameter("thirdPartyInsurerName", null);
            }
            generator.addParameter("thirdPartyPolicyNumber", thirdParty.getPolicyNumber());
            generator.addParameter("thirdPartyClaimReference", thirdParty.getClaimReference());
            generator.addParameter("thirdPartyFirstName", thirdParty.getFirstName());
            generator.addParameter("thirdPartyAddress1", thirdParty.getAddress1());
            generator.addParameter("thirdPartyAddress2", thirdParty.getAddress2());
            generator.addParameter("thirdPartyAddress3", thirdParty.getAddress3());
            generator.addParameter("thirdPartyAddress4", thirdParty.getAddress4());
            generator.addParameter("thirdPartyAddress5", thirdParty.getAddress5());
            generator.addParameter("thirdPartyPostcode", thirdParty.getPostcode());
            generator.addParameter("thirdPartyTelephoneDay", thirdParty.getTelephoneDay());
            generator.addParameter("thirdPartyTelephoneEvening", thirdParty.getTelephoneEvening());
            generator.addParameter("thirdPartyEmail", thirdParty.getEmail());
            generator.addParameter("thirdPartyLastName", thirdParty.getLastName());
            generator.addParameter("thirdPartyTitle", thirdParty.getTitle());
            generator.addParameter("thirdPartyInsurerBrand", thirdParty.getInsurerBrand());
        } else {
            generator.addParameter("thirdPartyInsurerName", null);
            generator.addParameter("thirdPartyPolicyNumber", null);
            generator.addParameter("thirdPartyClaimReference", null);
            generator.addParameter("thirdPartyFirstName", null);
            generator.addParameter("thirdPartyAddress1", null);
            generator.addParameter("thirdPartyAddress2", null);
            generator.addParameter("thirdPartyAddress3", null);
            generator.addParameter("thirdPartyAddress4", null);
            generator.addParameter("thirdPartyAddress5", null);
            generator.addParameter("thirdPartyPostcode", null);
            generator.addParameter("thirdPartyTelephoneDay", null);
            generator.addParameter("thirdPartyTelephoneEvening", null);
            generator.addParameter("thirdPartyEmail", null);
            generator.addParameter("thirdPartyLastName", null);
            generator.addParameter("thirdPartyTitle", null);
            generator.addParameter("thirdPartyInsurerBrand", null);
        }
    }
    
    public void addClaimThirdPartyVehicleParameters(ActivityEventGenerator generator, Claim claim) {
        ThirdParty thirdParty = claim.getThirdParty();
        if (thirdParty != null) {
            generator.addParameter("thirdPartyVehicleRegistration", thirdParty.getVehicleRegistration());
            generator.addParameter("thirdPartyVehicleManufacturer", thirdParty.getVehicleManufacturer());
            generator.addParameter("thirdPartyVehicleModel", thirdParty.getVehicleModel());
            if (thirdParty.getVehicleClass() != null) {
                generator.addParameter("thirdPartyVehicleClass", thirdParty.getVehicleClass().getName());
            } else {
                generator.addParameter("thirdPartyVehicleClass", null);
            }
        } else {
            generator.addParameter("thirdPartyVehicleRegistration", null);
            generator.addParameter("thirdPartyVehicleManufacturer", null);
            generator.addParameter("thirdPartyVehicleModel", null);
            generator.addParameter("thirdPartyVehicleClass", null);
        }
    }
    
    public void addClaimHireVehicleParameters(ActivityEventGenerator generator, Claim claim) {
        VehicleHire vehicleHire = claim.getVehicleHire();
        if (vehicleHire != null) {
            generator.addParameter("hireVehicleRegistration", vehicleHire.getVehicleRegistration());
            generator.addParameter("hireVehicleManufacturer", vehicleHire.getVehicleManufacturer());
            generator.addParameter("hireVehicleModel", vehicleHire.getVehicleModel());
            if (vehicleHire.getVehicleClass() != null) {
                generator.addParameter("hireVehicleClass", vehicleHire.getVehicleClass().getName());
            } else {
                generator.addParameter("hireVehicleClass", null);
            }
            generator.addParameter("hireVehicleRentalStart", vehicleHire.getRentalStart());
            generator.addParameter("hireVehicleRentalEnd", vehicleHire.getRentalEnd());
            generator.addParameter("hireVehicleCollectionReason", vehicleHire.getCollectionReason());
            generator.addParameter("hireVehicleDays", vehicleHire.getDays());
            generator.addParameter("hireVehicleHpiVehicleManufacturer", vehicleHire.getHpiVehicleManufacturer());
            generator.addParameter("hireVehicleHpiVehicleModel", vehicleHire.getHpiVehicleModel());
            generator.addParameter("hireVehicleHpiVehicleYear", vehicleHire.getHpiVehicleYear());
            generator.addParameter("hireVehicleHpiVehicleCapacity", vehicleHire.getHpiVehicleCapacity());
            generator.addParameter("hireVehicleHpiVehicleDoorplan", vehicleHire.getHpiVehicleDoorplan());
            generator.addParameter("hireVehicleHpiVehicleTransmission", vehicleHire.getHpiVehicleTransmission());
            if (vehicleHire.getHpiFirstRegistration() != null) {
                generator.addParameter("hireVehicleHpiVehicleFirstRegistration", DateHelper.getLocalDateFormat().format(vehicleHire.getHpiFirstRegistration()));
            } else {
                generator.addParameter("hireVehicleHpiVehicleFirstRegistration", null);
            }
        } else {
            generator.addParameter("hireVehicleRegistration", null);
            generator.addParameter("hireVehicleManufacturer", null);
            generator.addParameter("hireVehicleModel", null);
            generator.addParameter("hireVehicleClass", null);
            generator.addParameter("hireVehicleRentalStart", null);
            generator.addParameter("hireVehicleRentalEnd", null);
            generator.addParameter("hireVehicleCollectionReason", null);
            generator.addParameter("hireVehicleDays", null);
            generator.addParameter("hireVehicleHpiVehicleManufacturer", null);
            generator.addParameter("hireVehicleHpiVehicleModel", null);
            generator.addParameter("hireVehicleHpiVehicleYear", null);
            generator.addParameter("hireVehicleHpiVehicleCapacity", null);
            generator.addParameter("hireVehicleHpiVehicleDoorplan", null);
            generator.addParameter("hireVehicleHpiVehicleTransmission", null);
            generator.addParameter("hireVehicleHpiVehicleFirstRegistration", null);
        }
    }
    
    public void addClaimEngineerReportParameters(ActivityEventGenerator generator, Claim claim) {
        EngineerReport engineerReport = claim.getEngineerReport();
        if (engineerReport != null) {
            generator.addParameter("engineerReportName", engineerReport.getName());
            generator.addParameter("engineerReportCompany", engineerReport.getCompany());
            generator.addParameter("engineerReportAddress1", engineerReport.getAddress1());
            generator.addParameter("engineerReportAddress2", engineerReport.getAddress2());
            generator.addParameter("engineerReportAddress3", engineerReport.getAddress3());
            generator.addParameter("engineerReportAddress4", engineerReport.getAddress4());
            generator.addParameter("engineerReportAddress5", engineerReport.getAddress5());
            generator.addParameter("engineerReportPostcode", engineerReport.getPostcode());
            generator.addParameter("engineerReportTelephone", engineerReport.getTelephone());
            generator.addParameter("engineerReportEmail", engineerReport.getEmail());
            generator.addParameter("engineerReportIsUsable", engineerReport.isIsUsable());
            generator.addParameter("engineerReportDays", engineerReport.getDays());
            generator.addParameter("engineerReportLabourAmount", engineerReport.getLabourAmount());
            generator.addParameter("engineerReportTotalAmount", engineerReport.getTotalAmount());
        } else {
            generator.addParameter("engineerReportName", null);
            generator.addParameter("engineerReportCompany", null);
            generator.addParameter("engineerReportAddress1", null);
            generator.addParameter("engineerReportAddress2", null);
            generator.addParameter("engineerReportAddress3", null);
            generator.addParameter("engineerReportAddress4", null);
            generator.addParameter("engineerReportAddress5", null);
            generator.addParameter("engineerReportPostcode", null);
            generator.addParameter("engineerReportTelephone", null);
            generator.addParameter("engineerReportEmail", null);
            generator.addParameter("engineerReportIsUsable", null);
            generator.addParameter("engineerReportDays", null);
            generator.addParameter("engineerReportLabourAmount", null);
            generator.addParameter("engineerReportTotalAmount", null);
        }
    }

    public void addClaimHireMonitoringParameters(ActivityEventGenerator generator, Claim claim) {
        HireMonitoringDetail hireMonitoringDetail = claim.getHireMonitoringDetail();
        if (hireMonitoringDetail != null) {
            generator.addParameter("hireMonitoringRepairerName", hireMonitoringDetail.getNameOfRepairer());
            if (hireMonitoringDetail.getRepairBookInDate() != null) {
                generator.addParameter("hireMonitoringRepairBookedInDate", DateHelper.getLocalDateFormat().format(hireMonitoringDetail.getRepairBookInDate()));
            } else {
                generator.addParameter("hireMonitoringRepairBookedInDate", null);
            }
            if (hireMonitoringDetail.getInspectionDate() != null) {
                generator.addParameter("hireMonitoringInspectionDate", DateHelper.getLocalDateFormat().format(hireMonitoringDetail.getInspectionDate()));
            } else {
                generator.addParameter("hireMonitoringInspectionDate", null);
            }
            generator.addParameter("hireMonitoringImeName", hireMonitoringDetail.getNameOfIme());
            if (hireMonitoringDetail.getRepairCompletionDate() != null) {
                generator.addParameter("hireMonitoringRepairCompletionDate", DateHelper.getLocalDateFormat().format(hireMonitoringDetail.getRepairCompletionDate()));
            } else {
                generator.addParameter("hireMonitoringRepairCompletionDate", null);
            }
            generator.addParameter("hireMonitoringLabourRate", hireMonitoringDetail.getLabourRate());
            generator.addParameter("hireMonitoringLabourHours", hireMonitoringDetail.getLabourHour());
            generator.addParameter("hireMonitoringLabourCost", hireMonitoringDetail.getLabourCost());
            generator.addParameter("hireMonitoringNonProvisionReason", hireMonitoringDetail.getNonProvisionReason());
            if (hireMonitoringDetail.getRepairAuthorisedDate() != null) {
                generator.addParameter("hireMonitoringRepairAuthorisedDate", DateHelper.getLocalDateFormat().format(hireMonitoringDetail.getRepairAuthorisedDate()));
            } else {
                generator.addParameter("hireMonitoringRepairAuthorisedDate", null);
            }
            if (hireMonitoringDetail.getRepairCommencedDate() != null) {
                generator.addParameter("hireMonitoringRepairCommencedDate", DateHelper.getLocalDateFormat().format(hireMonitoringDetail.getRepairCommencedDate()));
            } else {
                generator.addParameter("hireMonitoringRepairCommencedDate", null);
            }
            if (hireMonitoringDetail.getTotalLossOfferMadeDate() != null) {
                generator.addParameter("hireMonitoringTotalLossOfferMadeDate", DateHelper.getLocalDateFormat().format(hireMonitoringDetail.getTotalLossOfferMadeDate()));
            } else {
                generator.addParameter("hireMonitoringTotalLossOfferMadeDate", null);
            }
            if (hireMonitoringDetail.getTotalLossOfferAcceptedDate() != null) {
                generator.addParameter("hireMonitoringTotalLossOfferAcceptedDate", DateHelper.getLocalDateFormat().format(hireMonitoringDetail.getTotalLossOfferAcceptedDate()));
            } else {
                generator.addParameter("hireMonitoringTotalLossOfferAcceptedDate", null);
            }
            if (hireMonitoringDetail.getTotalLossOfferCheckIssuedDate() != null) {
                generator.addParameter("hireMonitoringTotalLossCheckIssuedDate", DateHelper.getLocalDateFormat().format(hireMonitoringDetail.getTotalLossOfferCheckIssuedDate()));
            } else {
                generator.addParameter("hireMonitoringTotalLossCheckIssuedDate", null);
            }
            if (hireMonitoringDetail.getTotalLossOfferCheckReceivedDate() != null) {
                generator.addParameter("hireMonitoringTotalLossCheckReceivedDate", DateHelper.getLocalDateFormat().format(hireMonitoringDetail.getTotalLossOfferCheckReceivedDate()));
            } else {
                generator.addParameter("hireMonitoringTotalLossCheckReceivedDate", null);
            }
            generator.addParameter("hireMonitoringIsTotalLoss", hireMonitoringDetail.getIsTotalLossDesc());
            generator.addParameter("hireMonitoringIsRepairOnly", hireMonitoringDetail.isIsRepairOnlyCheck());
            generator.addParameter("hireMonitoringIsClientVatRegistered", hireMonitoringDetail.getClientVatRegisteredDesc());
        } else {
            generator.addParameter("hireMonitoringRepairerName", null);
            generator.addParameter("hireMonitoringRepairBookedInDate", null);
            generator.addParameter("hireMonitoringInspectionDate", null);
            generator.addParameter("hireMonitoringImeName", null);
            generator.addParameter("hireMonitoringRepairCompletionDate", null);
            generator.addParameter("hireMonitoringLabourRate", null);
            generator.addParameter("hireMonitoringLabourHours", null);
            generator.addParameter("hireMonitoringLabourCost", null);
            generator.addParameter("hireMonitoringNonProvisionReason", null);
            generator.addParameter("hireMonitoringRepairAuthorisedDate", null);
            generator.addParameter("hireMonitoringRepairCommencedDate", null);
            generator.addParameter("hireMonitoringTotalLossOfferMadeDate", null);
            generator.addParameter("hireMonitoringTotalLossOfferAcceptedDate", null);
            generator.addParameter("hireMonitoringTotalLossCheckIssuedDate", null);
            generator.addParameter("hireMonitoringTotalLossCheckReceivedDate", null);
            generator.addParameter("hireMonitoringIsTotalLoss", null);
            generator.addParameter("hireMonitoringIsRepairOnly", null);
            generator.addParameter("hireMonitoringIsClientVatRegistered", null);
        }
        addClaimHireVehicleParameters(generator, claim);
    }

    public void addClaimHireMonitoringEcdParameters(ActivityEventGenerator generator, Claim claim) {
        List<HireMonitoringEcd> hireMonitoringEcds = claim.getHireMonitoringEcds();
        if (hireMonitoringEcds != null) {
            // For now, we'll just send the latest
            generator.addParameter("hireMonitoringEcdDate", hireMonitoringEcds.get(hireMonitoringEcds.size()-1));
            generator.addParameter("hireMonitoringEcdSupportingNote", hireMonitoringEcds.get(hireMonitoringEcds.size()-1));
            generator.addParameter("hireMonitoringEcdReason", hireMonitoringEcds.get(hireMonitoringEcds.size()-1));
        } else {
            generator.addParameter("hireMonitoringEcdDate", null);
            generator.addParameter("hireMonitoringEcdSupportingNote", null);
            generator.addParameter("hireMonitoringEcdReason", null);
        }
    }

    public void addClaimLiabilityParameters(ActivityEventGenerator generator, Claim claim) {
        generator.addParameter("liabilityAcceptedInsurer", claim.getPercentageLiabilityAccepted());
        generator.addParameter("liabilityAcceptedCHO", claim.getPercentageLiabilityCho());
        if (claim.getLiabilityAgreedDate() != null) {
            generator.addParameter("liabilityAgreedDate", DateHelper.getLocalDateFormat().format(claim.getLiabilityAgreedDate()));
        } else {
            generator.addParameter("liabilityAgreedDate", null);
        }
        if (claim.getLiabilityStatus() != null) {
            generator.addParameter("liabilityStatus", claim.getLiabilityStatus().toString());
        } else {
            generator.addParameter("liabilityStatus", null);
        }
    }



    public void addInvoiceParameters(ActivityEventGenerator generator, Claim claim) {
        Invoice invoice = claim.getInvoice();
        if (invoice != null) {
            if (invoice.getDateInvoiced() != null) {
                generator.addParameter("invoiceDate", DateHelper.getLocalDateFormat().format(invoice.getDateInvoiced()));
            } else {
                generator.addParameter("invoiceDate", null);
            }
            generator.addParameter("invoiceInvoiceNo", invoice.getClaimInvoiceNo());
            generator.addParameter("invoiceHireNet", invoice.getHireNet());
            generator.addParameter("invoiceHireVat", invoice.getHireVat());
            generator.addParameter("invoiceHireGross", invoice.getHireGross());
            generator.addParameter("invoiceRepairNet", invoice.getRepairNet());
            generator.addParameter("invoiceRepairVat", invoice.getRepairVat());
            generator.addParameter("invoiceRepairGross", invoice.getRepairGross());
            generator.addParameter("invoiceEngineerFeeNet", invoice.getEngineerFeeNet());
            generator.addParameter("invoiceEngineerFeeVat", invoice.getEngineerFeeVat());
            generator.addParameter("invoiceEngineerFeeGross", invoice.getEngineerFeeGross());
            generator.addParameter("invoiceStorageRecoveryNet", invoice.getStorageRecoveryNet());
            generator.addParameter("invoiceStorageRecoveryVat", invoice.getStorageRecoveryVat());
            generator.addParameter("invoiceStorageRecoveryGross", invoice.getStorageRecoveryGross());
            generator.addParameter("invoiceTotalNet", invoice.getTotalNet());
            generator.addParameter("invoiceTotalVat", invoice.getTotalVat());
            generator.addParameter("invoiceTotalGross", invoice.getTotalGross());
            generator.addParameter("invoiceFullTotaRequested", invoice.getFullTotalToPay());
            generator.addParameter("invoiceMiscellaneousFee", invoice.getMiscellaneousFee());
            generator.addParameter("invoiceAutomaticFee", invoice.getAutomaticFee());
            generator.addParameter("invoiceSatNavFee", invoice.getSatNavFee());
            generator.addParameter("invoiceEstateFee", invoice.getEstateFee());
            generator.addParameter("invoiceBabySeatFee", invoice.getBabySeatFee());
            generator.addParameter("invoiceTowBarsFee", invoice.getTowBarsFee());
            generator.addParameter("invoiceNonStandardPremiumFee", invoice.getNonStandardInsurancePremiumFee());
            generator.addParameter("invoiceAdminFee", invoice.getAdminFee());
            generator.addParameter("invoiceRoofRackFee", invoice.getRoofRackFee());
            generator.addParameter("invoiceDualControlFee", invoice.getDualControlFee());
            generator.addParameter("invoiceDeliveryCollectionFee", invoice.getDeliveryCollectionFee());
            generator.addParameter("invoiceEngineerReviewNotes", invoice.getEngineerInvoiceReviewNotes());
            generator.addParameter("invoiceDayHireRate", invoice.getHireRateChargedPerDay());
            generator.addParameter("invoiceExcessCollected", invoice.getExcessAmountCollected());
            generator.addParameter("invoiceVatCollected", invoice.getVatAmountCollected());
            generator.addParameter("invoiceHirePenaltyCharge", invoice.getHirePenaltyCharge());
            if (invoice.getHirePenaltyChargeAppliedDate() != null) {
                generator.addParameter("invoiceHirePenaltyChargeAppliedDate", DateHelper.getLocalDateFormat().format(invoice.getHirePenaltyChargeAppliedDate()));
            } else {
                generator.addParameter("invoiceHirePenaltyChargeAppliedDate", null);
            }
            generator.addParameter("invoiceTotalToPay", invoice.getTotalToPay());
            generator.addParameter("invoiceAdditionalDriverFee", invoice.getAdditionalDriverFee());
            generator.addParameter("invoiceIsCoverNoteRequired", invoice.getCoverNoteRequired());
            generator.addParameter("invoiceTotalLossNet", invoice.getTotalLossFeeNet());
            generator.addParameter("invoiceTotalLossVat", invoice.getTotalLossFeeVat());
            generator.addParameter("invoiceTotalLossGross", invoice.getTotalLossFeeGross());
            generator.addParameter("invoiceHirePenaltyPercentage", invoice.getHirePenaltyPercentage());
            generator.addParameter("invoiceInterimPaymentMade", invoice.getInterimPaymentMade());
            generator.addParameter("invoiceRepairPenaltyPercentage", invoice.getRepairPenaltyPercentage());
            if (invoice.getRepairPenaltyChargeAppliedDate() != null) {
                generator.addParameter("invoiceRepairPenaltyChargeAppliedDate", DateHelper.getLocalDateFormat().format(invoice.getRepairPenaltyChargeAppliedDate()));
            } else {
                generator.addParameter("invoiceRepairPenaltyChargeAppliedDate", null);
            }
            generator.addParameter("invoiceTotalPenaltyCharge", invoice.getTotalPenaltyCharge());
            generator.addParameter("invoiceInterimPaymentReceived", invoice.getInterimPaymentReceived());
            generator.addParameter("invoiceRepairAdminFee", invoice.getRepairAdminFee());
            generator.addParameter("invoiceRepairAcquisitionFee", invoice.getRepairAcquisitionFee());
            generator.addParameter("invoiceCollaborationFee", invoice.getCollaborationFee());
        } else {
            generator.addParameter("invoiceDate", null);
            generator.addParameter("invoiceInvoiceNo", null);
            generator.addParameter("invoiceHireNet", null);
            generator.addParameter("invoiceHireVat", null);
            generator.addParameter("invoiceHireGross", null);
            generator.addParameter("invoiceRepairNet", null);
            generator.addParameter("invoiceRepairVat", null);
            generator.addParameter("invoiceRepairGross", null);
            generator.addParameter("invoiceEngineerFeeNet", null);
            generator.addParameter("invoiceEngineerFeeVat", null);
            generator.addParameter("invoiceEngineerFeeGross", null);
            generator.addParameter("invoiceStorageRecoveryNet", null);
            generator.addParameter("invoiceStorageRecoveryVat", null);
            generator.addParameter("invoiceStorageRecoveryGross", null);
            generator.addParameter("invoiceTotalNet", null);
            generator.addParameter("invoiceTotalVat", null);
            generator.addParameter("invoiceTotalGross", null);
            generator.addParameter("invoiceFullTotaRequested", null);
            generator.addParameter("invoiceMiscellaneousFee", null);
            generator.addParameter("invoiceAutomaticFee", null);
            generator.addParameter("invoiceSatNavFee", null);
            generator.addParameter("invoiceEstateFee", null);
            generator.addParameter("invoiceBabySeatFee", null);
            generator.addParameter("invoiceTowBarsFee", null);
            generator.addParameter("invoiceNonStandardPremiumFee", null);
            generator.addParameter("invoiceAdminFee", null);
            generator.addParameter("invoiceRoofRackFee", null);
            generator.addParameter("invoiceDualControlFee", null);
            generator.addParameter("invoiceDeliveryCollectionFee", null);
            generator.addParameter("invoiceEngineerReviewNotes", null);
            generator.addParameter("invoiceDayHireRate", null);
            generator.addParameter("invoiceExcessCollected", null);
            generator.addParameter("invoiceVatCollected", null);
            generator.addParameter("invoiceHirePenaltyCharge", null);
            generator.addParameter("invoiceHirePenaltyChargeAppliedDate", null);
            generator.addParameter("invoiceTotalToPay", null);
            generator.addParameter("invoiceAdditionalDriverFee", null);
            generator.addParameter("invoiceIsCoverNoteRequired", null);
            generator.addParameter("invoiceTotalLossNet", null);
            generator.addParameter("invoiceTotalLossVat", null);
            generator.addParameter("invoiceTotalLossGross", null);
            generator.addParameter("invoiceHirePenaltyPercentage", null);
            generator.addParameter("invoiceInterimPaymentMade", null);
            generator.addParameter("invoiceRepairPenaltyPercentage", null);
            generator.addParameter("invoiceRepairPenaltyChargeAppliedDate", null);
            generator.addParameter("invoiceTotalPenaltyCharge", null);
            generator.addParameter("invoiceInterimPaymentReceived", null);
            generator.addParameter("invoiceRepairAdminFee", null);
            generator.addParameter("invoiceRepairAcquisitionFee", null);
            generator.addParameter("invoiceCollaborationFee", null);
        }
    }
    public void addInvoicePaidParameters(ActivityEventGenerator generator, Claim claim) {
        Invoice invoice = claim.getInvoice();
        if (invoice != null) {
            generator.addParameter("invoiceHireGrossPaid", invoice.getHireGrossPaid());
            generator.addParameter("invoiceRepairGrossPaid", invoice.getRepairGrossPaid());
            generator.addParameter("invoiceEngineerFeeGrossPaid", invoice.getEngineerFeeGrossPaid());
            generator.addParameter("invoiceTotalLossFeeGrossPaid", invoice.getTotalLossFeeGrossPaid());
            generator.addParameter("invoiceStorageRecoveryGrossPaid", invoice.getStorageRecoveryGrossPaid());
            generator.addParameter("invoiceHirePenaltyChargePaid", invoice.getHirePenaltyChargePaid());
            generator.addParameter("invoiceRepairPenaltyChargePaid", invoice.getRepairPenaltyChargePaid());
            generator.addParameter("invoiceFinalPayment", invoice.getFinalPayment());
        } else {
            generator.addParameter("invoiceHireGrossPaid", null);
            generator.addParameter("invoiceRepairGrossPaid", null);
            generator.addParameter("invoiceEngineerFeeGrossPaid", null);
            generator.addParameter("invoiceTotalLossFeeGrossPaid", null);
            generator.addParameter("invoiceStorageRecoveryGrossPaid", null);
            generator.addParameter("invoiceHirePenaltyChargePaid", null);
            generator.addParameter("invoiceRepairPenaltyChargePaid", null);
            generator.addParameter("invoiceFinalPayment", null);
        }
    }
}
