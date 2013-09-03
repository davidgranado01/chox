package idas.chox.service.workflow.activities;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Customer;
import idas.chox.core.model.EngineerReport;
import idas.chox.core.model.HireMonitoringDetail;
import idas.chox.core.model.HireMonitoringEcd;
import idas.chox.core.model.Incident;
import idas.chox.core.model.Injury;
import idas.chox.core.model.Invoice;
import idas.chox.core.model.Solicitor;
import idas.chox.core.model.ThirdParty;
import idas.chox.core.model.VehicleHire;
import idas.chox.core.model.Witness;
import idas.chox.core.util.DateHelper;

/**
 *
 * @author John
 */
public enum ActivityEvent {

    CLAIM_ACKNOWLEDGED_EVENT            (0, "ClaimAcknowledgedEvent") {
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
    CLAIM_SWITCHED_TO_GTA_EVENT        (1, "ClaimSwitchedToGTAEvent"),
    CLAIM_REGISTERED_BY_FNOL_EVENT        (2, "ClaimRegisteredByFnolEvent"),
    INVOICE_UPLOADED_EVENT        (3, "InvoiceUploadedEvent") {
        public void build(ActivityEventGenerator generator, InsurerUpload activity, Claim claim)  throws Exception {
            LOG.debug("Building from InvoiceUploadedEvent");
            generator.startEvent(claim, this.getName(), this.getEventId());
            addInvoiceParameters(generator, claim);
            generator.completeEvent(claim);
        }
    },
    INVOICE_REJECTED_EVENT        (4, "InvoiceRejectedEvent"),
    INSURER_OWNER_ASSIGNED_EVENT        (5, "InsurerOwnerAssignedEvent") {
        public void build(ActivityEventGenerator generator, AssignOwner activity, Claim claim)  throws Exception {
            LOG.debug("Building from InsurerOwnerAssignedEvent");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.addParameter("insurerOwnerName", activity.getClaimOwner().getFullName());
            generator.completeEvent(claim);
        }
        public void build(ActivityEventGenerator generator, AssignManualInvoiceOwner activity, Claim claim)  throws Exception {
            LOG.debug("Building from InsurerOwnerAssignedEvent");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.addParameter("insurerOwnerName", activity.getClaimOwner().getFullName());
            generator.completeEvent(claim);
        }
        public void build(ActivityEventGenerator generator, ClaimReferToFnol activity, Claim claim)  throws Exception {
            LOG.debug("Building from InsurerOwnerAssignedEvent");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.addParameter("insurerOwnerName", activity.getClaimOwner().getFullName());
            generator.completeEvent(claim);
        }
        public void build(ActivityEventGenerator generator, InsurerUpload activity, Claim claim)  throws Exception {
            LOG.debug("Building from InsurerOwnerAssignedEvent");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.addParameter("insurerOwnerName", claim.getClaimOwner().getFullName());
            generator.completeEvent(claim);
        }
        public void build(ActivityEventGenerator generator, InvoiceResubmit activity, Claim claim)  throws Exception {
            LOG.debug("Building from InsurerOwnerAssignedEvent");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.addParameter("insurerOwnerName", claim.getClaimOwner().getFullName());
            generator.completeEvent(claim);
        }
        public void build(ActivityEventGenerator generator, NewInvoice activity, Claim claim)  throws Exception {
            LOG.debug("Building from InsurerOwnerAssignedEvent");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.addParameter("insurerOwnerName", claim.getClaimOwner().getFullName());
            generator.completeEvent(claim);
        }
        public void build(ActivityEventGenerator generator, NewTpiClaim activity, Claim claim)  throws Exception {
            LOG.debug("Building from InsurerOwnerAssignedEvent");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.addParameter("insurerOwnerName", claim.getClaimOwner().getFullName());
            generator.completeEvent(claim);
        }
    },
    INVOICE_REFERRED_TO_ENG_EVENT        (6, "InvoiceReferToEng"),
    FULL_PAYMENT_RECEIVED_EVENT        (7, "FullPaymentReceivedEvent"),
    INVOICE_SUBMITTED_EVENT        (8, "InvoiceSubmittedEvent"),
    CLAIM_CLOSED_EVENT        (9, "ClaimClosedEvent"),
    INTERIM_PAYMENT_RECEIVED_EVENT        (10, "InterimPaymentReceivedEvent"),
    CLAIM_ROUTED_EVENT        (11, "ClaimRoutedEvent") {
        public void build(ActivityEventGenerator generator, AssignWorkgroup activity, Claim claim)  throws Exception {
            LOG.debug("Building from ClaimRoutedEvent");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.addParameter("insurerWorkgroupName", activity.getWorkgroup().getName());
            generator.addParameter("insurerWorkgroupId", activity.getWorkgroup().getId().intValue());
            generator.completeEvent(claim);
        }
    },
    PAYMENT_NOT_RECEIVED_EVENT        (12, "PaymentNotReceivedEvent"),
    CLAIM_AWAITING_LITIGATION_OUTCOME_EVENT        (13, "ClaimAwaitingLitigationOutcomeEvent"),
    INTERIM_PAYMENT_UPDATED_EVENT        (14, "InterimPaymentUpdatedEvent"),
    BRE_RESULT_EVENT        (15, "BreResultEvent"),
    ECD_UPDATED_EVENT        (16, "EcdUpdatedEvent"),
    CLAIM_REVIEW_BY_ENG_EVENT        (17, "ClaimReviewByEngEvent"),
    INVOICE_RESUBMITTED_EVENT        (18, "InvoiceResubmittedEvent"),
    CLAIM_REVERTED_EVENT        (19, "ClaimRevertedEvent"),
    INVOICE_REFERRED_TO_CH_EVENT        (20, "InvoiceReferToCHEvent"),
    LIABILITY_UPDATED_EVENT        (21, "LiabilityUpdatedEvent") {
        public void build(ActivityEventGenerator generator, AcknowledgeClaim activity, Claim claim)  throws Exception {
            LOG.debug("Building from LiabilityUpdatedEvent");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.addParameter("supportingNote", activity.getSupportingLiabilityNotes());
            this.addClaimLiabilityParameters(generator, claim);
            generator.completeEvent(claim);
        }
        public void build(ActivityEventGenerator generator, ClaimPending activity, Claim claim)  throws Exception {
            LOG.debug("Building from LiabilityUpdatedEvent");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.addParameter("supportingNote", activity.getSupportingLiabilityNotes());
            this.addClaimLiabilityParameters(generator, claim);
            generator.completeEvent(claim);
        }
        public void build(ActivityEventGenerator generator, ClaimReferToEng activity, Claim claim)  throws Exception {
            LOG.debug("Building from LiabilityUpdatedEvent");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.addParameter("supportingNote", activity.getSupportingLiabilityNotes());
            this.addClaimLiabilityParameters(generator, claim);
            generator.completeEvent(claim);
        }
        public void build(ActivityEventGenerator generator, ClaimReferToFnol activity, Claim claim)  throws Exception {
            LOG.debug("Building from LiabilityUpdatedEvent");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.addParameter("supportingNote", activity.getSupportingLiabilityNotes());
            this.addClaimLiabilityParameters(generator, claim);
            generator.completeEvent(claim);
        }
        public void build(ActivityEventGenerator generator, ClaimRejection activity, Claim claim)  throws Exception {
            LOG.debug("Building from LiabilityUpdatedEvent");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.addParameter("supportingNote", activity.getSupportingLiabilityNotes());
            this.addClaimLiabilityParameters(generator, claim);
            generator.completeEvent(claim);
        }
        public void build(ActivityEventGenerator generator, ResolveLiability activity, Claim claim)  throws Exception {
            LOG.debug("Building from LiabilityUpdatedEvent");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.addParameter("supportingNote", activity.getEngineerClaimReviewNotes());
            this.addClaimLiabilityParameters(generator, claim);
            generator.completeEvent(claim);
        }
        public void build(ActivityEventGenerator generator, UpdateLiability activity, Claim claim)  throws Exception {
            LOG.debug("Building from LiabilityUpdatedEvent");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.addParameter("supportingNote", activity.getClaimReviewNotes());
            this.addClaimLiabilityParameters(generator, claim);
            generator.completeEvent(claim);
        }
    },
    CHO_REFERENCE_NO_UPDATED_EVENT        (22, "ChoReferenceNumberUpdatedEvent"),
    INVOICE_REJECTION_CONTESTED_EVENT        (23, "InvoiceRejectionContestedEvent"),
    SLA_EXTENSION_GRANTED_EVENT        (24, "SlaExtensionGrantedEvent"),
    TASK_CREATED_EVENT        (25, "TaskCreatedEvent"),
    INVOICE_REJECTION_ACCEPTED_EVENT        (26, "InvoiceRejectionAcceptedEvent"),
    CLAIM_REJECTION_ACCEPTED_EVENT        (27, "ClaimRejectionAcceptedEvent"),
    ATTACHMENT_DELETED_EVENT        (28, "AttachmentDeletedEvent"),
    CLAIM_PENDING_EVENT        (29, "ClaimPendingEvent"),
    // Non-activity based events
    CLAIM_UPDATED_EVENT        (30, "ClaimUpdatedEvent"),
    CLAIM_NUMBER_ASSIGNED_EVENT        (31, "ClaimNumberAssignedEvent"),
    INTERIM_PAYMENT_ACCEPTED_AS_FINAL_EVENT        (32, "InterimPaymentAcceptedAsFinalEvent"),
    INVOICE_PAYMENT_RECEIVED_EVENT        (33, "InvoicePaymentReceivedEvent"),
    NEW_CLAIM_EVENT        (34, "NewClaimEvent"),
    HIRE_CAR_INFO_PROVIDED_EVENT        (35, "HireCarInfoProvidedEvent"),
    ATTACHMENT_UPLOADED_EVENT        (36, "AttachmentUploadedEvent"),
    NEW_SUPPLEMENTARY_INVOICE_EVENT        (37, "NewSupplementaryInvoice"),
    TASK_COMPLETED_EVENT        (38, "TaskCompletedEvent"),
    CLAIM_REJECTED_EVENT        (39, "ClaimRejectedEvent"),
    PAYMENT_RECEIVED_EVENT        (40, "PaymentReceivedEvent"),
    CLAIM_REJECTION_CONTESTED_EVENT        (41, "ClaimRejectionContestedEvent"),
    HIRE_MONITORING_UPDATED_EVENT        (42, "HireMonitoringUpdatedEvent"),
    CLAIM_REFERRED_TO_FNOL_EVENT        (43, "ClaimReferToFnolEvent"),
    CLAIM_NUMBER_UPDATED_EVENT        (44, "ClaimNumberUpdatedEvent"),
    INVOICE_PAID_EVENT        (45, "InvoicePaidEvent"),
    CHO_OWNER_ASSIGNED_EVENT        (46, "ChoOwnerAssignedEvent") {
        public void build(ActivityEventGenerator generator, AssignSupplierOwner activity, Claim claim)  throws Exception {
            LOG.debug("Building from AssignSupplierOwner");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.addParameter("insurerOwnerName", activity.getSupplierClaimOwner().getFullName());
            generator.completeEvent(claim);
        }
    },
    FULL_PAYMENT_NOT_RECEIVED_EVENT        (47, "FullPaymentNotReceivedEvent"),
    CLAIM_REFERRED_TO_ENG_EVENT        (48, "ClaimReferToEngEvent"),
    INVOICE_ACCEPTED_EVENT        (49, "InvoiceAcceptedEvent");
    
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

    public void build(ActivityEventGenerator generator, BaseActivity activity, Claim claim) throws Exception {
        LOG.error("No Events to build for activity {}", AopUtils.getTargetClass(activity).getSimpleName());
        generator.startEvent(claim, new StringBuilder().append(this.getName()).append(" [Not Mapped]").toString(), this.getEventId());
        generator.completeEvent(claim);
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
        }
        if (claim.getCreditAgreementDate() != null) {
            generator.addParameter("creditAgreementDate", DateHelper.getLocalDateFormat().format(claim.getCreditAgreementDate()));
        }
        if (claim.getGtaNoticeDate() != null) {
            generator.addParameter("gtaNoticeDate", DateHelper.getLocalDateFormat().format(claim.getGtaNoticeDate()));
        }
        if (claim.getFinalReviewByCho() != null) {
            generator.addParameter("finalReviewCho", claim.getFinalReviewByCho().getDisplayName());
        }
        if (claim.getFinalReviewByIns() != null) {
            generator.addParameter("finalReviewInsurer", claim.getFinalReviewByIns().getDisplayName());
        }
        if (claim.getFinalReviewDateIns() != null) {
            generator.addParameter("finalReviewDateCho", DateHelper.getLocalDateFormat().format(claim.getFinalReviewDateCho()));
        }
        if (claim.getFinalReviewDateIns() != null) {
            generator.addParameter("finalReviewDateInsurer", DateHelper.getLocalDateFormat().format(claim.getFinalReviewDateIns()));
        }
        if (claim.getSupplierClaimOwner() != null) {
            generator.addParameter("choOwnerName", claim.getSupplierClaimOwner().getDisplayName());
        }
        if (claim.getClaimOwner() != null) {
            generator.addParameter("insurerOwnerName", claim.getClaimOwner().getDisplayName());
        }
        if (claim.getWorkgroup() != null) {
            generator.addParameter("insurerWorkgroupName", claim.getWorkgroup().getName());
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
            }
            generator.addParameter("customerVehicleLocation", customer.getLocation());
            generator.addParameter("customerVehicleDamage", customer.getDamage());
            if (customer.getInitialECD() != null) {
                generator.addParameter("customerVehicalInitialEcd", DateHelper.getLocalDateFormat().format(customer.getInitialECD()));
            }
            generator.addParameter("customerVehicleIsUsable", customer.getIsUsable());
            generator.addParameter("customerVehicleIsTotalLoss", customer.getIsTotalLossDesc());
            generator.addParameter("customerVehicleYear", customer.getVehicleYear());
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
        }
    }
    
    public void addClaimCustomerIncidentParameters(ActivityEventGenerator generator, Claim claim) {
        Incident incident = claim.getIncident();
        if (incident != null) {
            if (incident.getDate() != null) {
                generator.addParameter("incidentDate", DateHelper.getLocalDateFormat().format(incident.getDate()));
            }
            generator.addParameter("incidentLocation", incident.getLocation());
            generator.addParameter("incidentDescription", incident.getIncidentDescription());
            generator.addParameter("incidentIsPoliceInvolved", incident.getIsPoliceInvolvedDesc());
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
            generator.addParameter("injuryPostcode", injury);
            generator.addParameter("injuryTelephoneDay", injury.getTelephoneDay());
            generator.addParameter("injuryTelephoneEvening", injury.getTelephoneEvening());
            generator.addParameter("injuryEmail", injury.getEmail());
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
        }
    }
    
    public void addClaimThirdPartyParameters(ActivityEventGenerator generator, Claim claim) {
        ThirdParty thirdParty = claim.getThirdParty();
        if (thirdParty != null) {
            if (thirdParty.getInsurer() != null) {
                generator.addParameter("thirdPartyInsurerName", thirdParty.getInsurer().getName());
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
            }
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
            }
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
        }
    }

    public void addClaimHireMonitoringParameters(ActivityEventGenerator generator, Claim claim) {
        HireMonitoringDetail hireMonitoringDetail = claim.getHireMonitoringDetail();
        if (hireMonitoringDetail != null) {
            generator.addParameter("hireMonitoringRepairerName", hireMonitoringDetail.getNameOfRepairer());
            if (hireMonitoringDetail.getRepairBookInDate() != null) {
                generator.addParameter("hireMonitoringRepairBookedInDate", DateHelper.getLocalDateFormat().format(hireMonitoringDetail.getRepairBookInDate()));
            }
            if (hireMonitoringDetail.getInspectionDate() != null) {
                generator.addParameter("hireMonitoringInspectionDate", DateHelper.getLocalDateFormat().format(hireMonitoringDetail.getInspectionDate()));
            }
            generator.addParameter("hireMonitoringImeName", hireMonitoringDetail.getNameOfIme());
            if (hireMonitoringDetail.getRepairCompletionDate() != null) {
                generator.addParameter("hireMonitoringRepairCompletionDate", DateHelper.getLocalDateFormat().format(hireMonitoringDetail.getRepairCompletionDate()));
            }
            generator.addParameter("hireMonitoringLabourRate", hireMonitoringDetail.getLabourRate());
            generator.addParameter("hireMonitoringLabourHours", hireMonitoringDetail.getLabourHour());
            generator.addParameter("hireMonitoringLabourCost", hireMonitoringDetail.getLabourCost());
            generator.addParameter("hireMonitoringNonProvisionReason", hireMonitoringDetail.getNonProvisionReason());
            if (hireMonitoringDetail.getRepairAuthorisedDate() != null) {
                generator.addParameter("hireMonitoringRepairAuthorisedDate", DateHelper.getLocalDateFormat().format(hireMonitoringDetail.getRepairAuthorisedDate()));
            }
            if (hireMonitoringDetail.getRepairCommencedDate() != null) {
                generator.addParameter("hireMonitoringRepairCommencedDate", DateHelper.getLocalDateFormat().format(hireMonitoringDetail.getRepairCommencedDate()));
            }
            if (hireMonitoringDetail.getTotalLossOfferMadeDate() != null) {
                generator.addParameter("hireMonitoringTotalLossOfferMadeDate", DateHelper.getLocalDateFormat().format(hireMonitoringDetail.getTotalLossOfferMadeDate()));
            }
            if (hireMonitoringDetail.getTotalLossOfferAcceptedDate() != null) {
                generator.addParameter("hireMonitoringTotalLossOfferAcceptedDate", DateHelper.getLocalDateFormat().format(hireMonitoringDetail.getTotalLossOfferAcceptedDate()));
            }
            if (hireMonitoringDetail.getTotalLossOfferCheckIssuedDate() != null) {
                generator.addParameter("hireMonitoringTotalLossCheckIssuedDate", DateHelper.getLocalDateFormat().format(hireMonitoringDetail.getTotalLossOfferCheckIssuedDate()));
            }
            if (hireMonitoringDetail.getTotalLossOfferCheckReceivedDate() != null) {
                generator.addParameter("hireMonitoringTotalLossCheckReceivedDate", DateHelper.getLocalDateFormat().format(hireMonitoringDetail.getTotalLossOfferCheckReceivedDate()));
            }
            generator.addParameter("hireMonitoringIsTotalLoss", hireMonitoringDetail.getIsTotalLossDesc());
            generator.addParameter("hireMonitoringIsRepairOnly", hireMonitoringDetail.isIsRepairOnlyCheck());
            generator.addParameter("hireMonitoringIsClientVatRegistered", hireMonitoringDetail.getClientVatRegisteredDesc());
        }
    }

    public void addClaimHireMonitoringEcdParameters(ActivityEventGenerator generator, Claim claim) {
        List<HireMonitoringEcd> hireMonitoringEcds = claim.getHireMonitoringEcds();
        if (hireMonitoringEcds != null) {
            // For now, we'll just send the latest
            generator.addParameter("hireMonitoringEcdDate", hireMonitoringEcds.get(hireMonitoringEcds.size()-1));
            generator.addParameter("hireMonitoringEcdSupportingNote", hireMonitoringEcds.get(hireMonitoringEcds.size()-1));
            generator.addParameter("hireMonitoringEcdReason", hireMonitoringEcds.get(hireMonitoringEcds.size()-1));
        }
    }

    public void addClaimLiabilityParameters(ActivityEventGenerator generator, Claim claim) {
        generator.addParameter("liabilityAcceptedInsurer", claim.getPercentageLiabilityAccepted());
        generator.addParameter("liabilityAcceptedCHO", claim.getPercentageLiabilityCho());
        if (claim.getLiabilityAgreedDate() != null) {
            generator.addParameter("liabilityAgreedDate", DateHelper.getLocalDateFormat().format(claim.getLiabilityAgreedDate()));
        }
        generator.addParameter("liabilityStatus", claim.getLiabilityStatus().toString());
    }



    public void addInvoiceParameters(ActivityEventGenerator generator, Claim claim) {
        Invoice invoice = claim.getInvoice();
        if (invoice != null) {
            if (invoice.getDateInvoiced() != null) {
                generator.addParameter("invoiceDate", DateHelper.getLocalDateFormat().format(invoice.getDateInvoiced()));
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
            }
            generator.addParameter("invoiceTotalPenaltyCharge", invoice.getTotalPenaltyCharge());
            generator.addParameter("invoiceInterimPaymentReceived", invoice.getInterimPaymentReceived());
            generator.addParameter("invoiceRepairAdminFee", invoice.getRepairAdminFee());
            generator.addParameter("invoiceRepairAcquisitionFee", invoice.getRepairAcquisitionFee());
            generator.addParameter("invoiceAcquisitionFee", invoice.getAcquisitionFee());
            generator.addParameter("invoiceOverheadFee", invoice.getOverheadFee());
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
        }
    }
}
