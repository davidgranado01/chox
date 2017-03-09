package idas.chox.events;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimAuditReview;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Customer;
import idas.chox.core.model.EngineerReport;
import idas.chox.core.model.Entity;
import idas.chox.core.model.HireMonitoringDetail;
import idas.chox.core.model.HireMonitoringEcd;
import idas.chox.core.model.Incident;
import idas.chox.core.model.Injury;
import idas.chox.core.model.InsurerHireMonitoringDetail;
import idas.chox.core.model.Invoice;
import idas.chox.core.model.Solicitor;
import idas.chox.core.model.ThirdParty;
import idas.chox.core.model.VehicleHire;
import idas.chox.core.model.Witness;
import idas.chox.core.util.DateHelper;

/**
 *
 * @author john
 */
public class BaseActivityEvent extends Entity implements Serializable {

    Claim claim;
    private String activityName;
    private String eventName;
    private String claimStatus;
    private Integer insurerId;
    private Integer choId;
    private ClaimType claimType;
    private Map<String, String> attributes;

    public BaseActivityEvent() {
    }

    ;
    
    public BaseActivityEvent(final Claim claim, String activityName) {
        this.claim = claim;
        this.claimStatus = claim.getStatus();
        this.eventName = this.getClass().getSimpleName();
        this.activityName = activityName;
        // If insurer or CHO null, set to 0
        // This is a hack for the unit tests as many claims are not set-up correctly.
        // This can be removed once the unit tests have been updated.
        this.insurerId = claim.getInsurer() == null ? 0 : claim.getInsurer().getId();
        this.choId = claim.getChorganisation() == null ? 0 : claim.getChorganisation().getId();
        this.claimType = claim.getClaimType();
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Claim getClaim() {
        return claim;
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
    }

    public String getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(String claimStatus) {
        this.claimStatus = claimStatus;
    }

    public Integer getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(Integer insurerId) {
        this.insurerId = insurerId;
    }

    public Integer getChoId() {
        return choId;
    }

    public void setChoId(Integer choId) {
        this.choId = choId;
    }

    public ClaimType getClaimType() {
        return claimType;
    }

    public void setClaimType(ClaimType claimType) {
        this.claimType = claimType;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    @Override
    public String toString() {
        return activityName;
    }

    public final void addAttribute(String key, String value) {
        if (attributes == null) {
            attributes = new HashMap<>();
        }
        attributes.put(key, value);
    }

    protected void addAllClaimAttributes(Claim claim) {
        addClaimAttributes(claim);
        addClaimCustomerAttributes(claim);
        addClaimCustomerVehicleAttributes(claim);
        addClaimCustomerMitigationAttributes(claim);
        addClaimCustomerIncidentAttributes(claim);
        addClaimCustomerIncidentWitnessAttributes(claim);
        addClaimCustomerIncidentInjuryAttributes(claim);
        addClaimCustomerIncidentInjurySolicitorAttributes(claim);
        addClaimThirdPartyAttributes(claim);
        addClaimThirdPartyVehicleAttributes(claim);
        addClaimEngineerReportAttributes(claim);
        addClaimHireVehicleAttributes(claim);
        addClaimHireMonitoringAttributes(claim);
//        addClaimHireMonitoringEcdAttributes(generator, claim);
    }
    
    
    protected void addClaimAttributes(final Claim claim) {
        addAttribute("managingRepair", String.valueOf(claim.isManagingRepair()));
        addAttribute("choReference", claim.getChoReference());
        addAttribute("status", claim.getStatus());
        addAttribute("insurerName", claim.getInsurer().getName());
        addAttribute("choName", claim.getChorganisation().getName());
        addAttribute("claimNumber", claim.getClaimNumber());
        addAttribute("isQuantumDispute", String.valueOf(claim.getIsQuantumDispute()));
        addAttribute("isInvoiceReviewRequired", String.valueOf(claim.getIsInvoiceReviewRequired()));
        addAttribute("claimType", claim.getClaimType().toString());
        if (claim.getPolicyHolderContactDate() != null) {
            addAttribute("policyHolderContactDate", DateHelper.getLocalDateFormat().format(claim.getPolicyHolderContactDate()));
        } else {
            addAttribute("policyHolderContactDate", null);
        }
        if (claim.getCreditAgreementDate() != null) {
            addAttribute("creditAgreementDate", DateHelper.getLocalDateFormat().format(claim.getCreditAgreementDate()));
        } else {
            addAttribute("creditAgreementDate", null);
        }
        if (claim.getGtaNoticeDate() != null) {
            addAttribute("gtaNoticeDate", DateHelper.getLocalDateFormat().format(claim.getGtaNoticeDate()));
        } else {
            addAttribute("gtaNoticeDate", null);
        }
        if (claim.getFinalReviewByCho() != null) {
            addAttribute("finalReviewCho", claim.getFinalReviewByCho().getDisplayName());
        } else {
            addAttribute("finalReviewCho", null);
        }
        if (claim.getFinalReviewByIns() != null) {
            addAttribute("finalReviewInsurer", claim.getFinalReviewByIns().getDisplayName());
        } else {
            addAttribute("finalReviewInsurer", null);
        }
        if (claim.getFinalReviewDateIns() != null) {
            addAttribute("finalReviewDateCho", DateHelper.getLocalDateFormat().format(claim.getFinalReviewDateCho()));
        } else {
            addAttribute("finalReviewDateCho", null);
        }
        if (claim.getFinalReviewDateIns() != null) {
            addAttribute("finalReviewDateInsurer", DateHelper.getLocalDateFormat().format(claim.getFinalReviewDateIns()));
        } else {
            addAttribute("finalReviewDateInsurer", null);
        }
        if (claim.getSupplierClaimOwner() != null) {
            addAttribute("choOwnerName", claim.getSupplierClaimOwner().getDisplayName());
        } else {
            addAttribute("choOwnerName", null);
        }
        if (claim.getClaimOwner() != null) {
            addAttribute("insurerOwnerName", claim.getClaimOwner().getDisplayName());
        } else {
            addAttribute("insurerOwnerName", null);
        }
        if (claim.getWorkgroup() != null) {
            addAttribute("insurerWorkgroupName", claim.getWorkgroup().getName());
        } else {
            addAttribute("insurerWorkgroupName", null);
        }

    }

    protected void addClaimCustomerAttributes(final Claim claim) {
        Customer customer = claim.getCustomer();
        if (customer != null) {
            addAttribute("customerTitle", customer.getTitle());
            addAttribute("customerFirstName", customer.getFirstName());
            addAttribute("customerLastName", customer.getLastName());
            addAttribute("customerAddress1", customer.getAddress1());
            addAttribute("cuatomerAddress2", customer.getAddress2());
            addAttribute("cuatomerAddress3", customer.getAddress3());
            addAttribute("cuatomerAddress4", customer.getAddress4());
            addAttribute("cuatomerAddress5", customer.getAddress5());
            addAttribute("customerPostcode", customer.getPostcode());
            addAttribute("customerTelephoneDay", customer.getTelephoneDay());
            addAttribute("customerTelephoneEvening", customer.getTelephoneEvening());
            addAttribute("customerEmail", customer.getEmail());
            addAttribute("customerPolicyNumber", customer.getPolicyNumber());
            addAttribute("customerClaimNumber", customer.getClaimReference());
            addAttribute("customerHasComprehensiveCover", customer.getIsComprehensiveDesc());
            addAttribute("customerInsurerName", customer.getInsurerName());
            addAttribute("customerAge", String.valueOf(customer.getAge()));
            addAttribute("customerOccupation", customer.getOccupation());
            addAttribute("customerPolicyUsage", customer.getPolicyUsage());
        } else {
            addAttribute("customerTitle", null);
            addAttribute("customerFirstName", null);
            addAttribute("customerLastName", null);
            addAttribute("customerAddress1", null);
            addAttribute("cuatomerAddress2", null);
            addAttribute("cuatomerAddress3", null);
            addAttribute("cuatomerAddress4", null);
            addAttribute("cuatomerAddress5", null);
            addAttribute("customerPostcode", null);
            addAttribute("customerTelephoneDay", null);
            addAttribute("customerTelephoneEvening", null);
            addAttribute("customerEmail", null);
            addAttribute("customerPolicyNumber", null);
            addAttribute("customerClaimNumber", null);
            addAttribute("customerHasComprehensiveCover", null);
            addAttribute("customerInsurerName", null);
            addAttribute("customerAge", null);
            addAttribute("customerOccupation", null);
            addAttribute("customerPolicyUsage", null);
        }
    }
    
    protected void addClaimCustomerVehicleAttributes(final Claim claim) {
        Customer customer = claim.getCustomer();
        if (customer != null) {
            addAttribute("customerVehicleRegistration", customer.getVehicleRegistration());
            addAttribute("customerVehicleManufacturer", customer.getVehicleManufacturer());
            addAttribute("customerVehicleModel", customer.getVehicleModel());
            if (customer.getVehicleClass() != null) {
                addAttribute("customerVehicleClass", customer.getVehicleClass().getName());
            } else {
                addAttribute("customerVehicleClass", null);
            }
            addAttribute("customerVehicleLocation", customer.getLocation());
            addAttribute("customerVehicleDamage", customer.getDamage());
            if (customer.getInitialECD() != null) {
                addAttribute("customerVehicalInitialEcd", DateHelper.getLocalDateFormat().format(customer.getInitialECD()));
            } else {
                addAttribute("customerVehicalInitialEcd", null);
            }
            addAttribute("customerVehicleIsUsable", String.valueOf(customer.getIsUsable()));
            addAttribute("customerVehicleIsTotalLoss", customer.getIsTotalLossDesc());
            addAttribute("customerVehicleYear", customer.getVehicleYear());
        } else {
            addAttribute("customerVehicleRegistration", null);
            addAttribute("customerVehicleManufacturer", null);
            addAttribute("customerVehicleModel", null);
            addAttribute("customerVehicleClass", null);
            addAttribute("customerVehicleLocation", null);
            addAttribute("customerVehicleDamage", null);
            addAttribute("customerVehicalInitialEcd", null);
            addAttribute("customerVehicleIsUsable", null);
            addAttribute("customerVehicleIsTotalLoss", null);
            addAttribute("customerVehicleYear", null);
        }
    }
    
    protected void addClaimCustomerMitigationAttributes(final Claim claim) {
        Customer customer = claim.getCustomer();
        if (customer != null) {
            addAttribute("customerCanAccessOtherVehicle", customer.getCanAccessOtherVehicleDesc());
            addAttribute("customerOtherVehicleUsed", customer.getOtherVehicleUsedDesc());
            addAttribute("customerOtherVehicle", customer.getOtherVehicle());
            addAttribute("customerEntitledToCourtesyCar", customer.getCourtesyCarEntitledDesc());
            addAttribute("customerSpecificVehicleRequired", customer.getSpecificVehicleRequiredDesc());
            addAttribute("customerSpecificVehicleReason", customer.getSpecificVehicleReason());
            addAttribute("customerSpecificVehicleType", customer.getTypeVehicleRequired());
            addAttribute("customerSpecialRequirements", customer.getSpecialRequirements());
            addAttribute("customerAverageDailyMilage", customer.getAverageDailyMileage());
        } else {
            addAttribute("customerCanAccessOtherVehicle", null);
            addAttribute("customerOtherVehicleUsed", null);
            addAttribute("customerOtherVehicle", null);
            addAttribute("customerEntitledToCourtesyCar", null);
            addAttribute("customerSpecificVehicleRequired", null);
            addAttribute("customerSpecificVehicleReason", null);
            addAttribute("customerSpecificVehicleType", null);
            addAttribute("customerSpecialRequirements", null);
            addAttribute("customerAverageDailyMilage", null);
        }
    }
    
    protected void addClaimCustomerIncidentAttributes(final Claim claim) {
        Incident incident = claim.getIncident();
        if (incident != null) {
            if (incident.getDate() != null) {
                addAttribute("incidentDate", DateHelper.getLocalDateFormat().format(incident.getDate()));
            } else {
                addAttribute("incidentDate", null);
            }
            addAttribute("incidentLocation", incident.getLocation());
            addAttribute("incidentDescription", incident.getIncidentDescription());
            addAttribute("incidentIsPoliceInvolved", incident.getIsPoliceInvolvedDesc());
        } else {
            addAttribute("incidentDate", null);
            addAttribute("incidentLocation", null);
            addAttribute("incidentDescription", null);
            addAttribute("incidentIsPoliceInvolved", null);
        }
    }
    
    protected void addClaimCustomerIncidentWitnessAttributes(final Claim claim) {
        if (claim.getIncident() != null && claim.getIncident().getWitness() != null) {
            Witness witness = claim.getIncident().getWitness();
            addAttribute("witnessName", witness.getName());
            addAttribute("witnessAddress1", witness.getAddress1());
            addAttribute("witnessAddress2", witness.getAddress2());
            addAttribute("witnessAddress3", witness.getAddress3());
            addAttribute("witnessAddress4", witness.getAddress4());
            addAttribute("witnessAddress5", witness.getAddress5());
            addAttribute("witnessPostcode", witness.getPostcode());
            addAttribute("witnessTelephoneDay", witness.getTelephoneDay());
            addAttribute("witnessTelephoneEvening", witness.getTelephoneEvening());
            addAttribute("witnessEmail", witness.getEmail());
        } else {
            addAttribute("witnessName", null);
            addAttribute("witnessAddress1", null);
            addAttribute("witnessAddress2", null);
            addAttribute("witnessAddress3", null);
            addAttribute("witnessAddress4", null);
            addAttribute("witnessAddress5", null);
            addAttribute("witnessPostcode", null);
            addAttribute("witnessTelephoneDay", null);
            addAttribute("witnessTelephoneEvening", null);
            addAttribute("witnessEmail", null);
        }
    }
    
    protected void addClaimCustomerIncidentInjuryAttributes(final Claim claim) {
        if (claim.getIncident() != null && claim.getIncident().getInjury() != null) {
            Injury injury = claim.getIncident().getInjury();
            addAttribute("injuryName", injury.getName());
            addAttribute("injuryAddress1", injury.getAddress1());
            addAttribute("injuryAddress2", injury.getAddress2());
            addAttribute("injuryAddress3", injury.getAddress3());
            addAttribute("injuryAddress4", injury.getAddress4());
            addAttribute("injuryAddress5", injury.getAddress5());
            addAttribute("injuryPostcode", injury.getPostcode());
            addAttribute("injuryTelephoneDay", injury.getTelephoneDay());
            addAttribute("injuryTelephoneEvening", injury.getTelephoneEvening());
            addAttribute("injuryEmail", injury.getEmail());
        } else {
            addAttribute("injuryName", null);
            addAttribute("injuryAddress1", null);
            addAttribute("injuryAddress2", null);
            addAttribute("injuryAddress3", null);
            addAttribute("injuryAddress4", null);
            addAttribute("injuryAddress5", null);
            addAttribute("injuryPostcode", null);
            addAttribute("injuryTelephoneDay", null);
            addAttribute("injuryTelephoneEvening", null);
            addAttribute("injuryEmail", null);
        }
    }
    
    protected void addClaimCustomerIncidentInjurySolicitorAttributes(final Claim claim) {
        if (claim.getIncident() != null && claim.getIncident().getInjury() != null
                && claim.getIncident().getInjury().getSolicitor() != null) {
            Solicitor solicitor = claim.getIncident().getInjury().getSolicitor();
            addAttribute("injurySolicitorName", solicitor.getName());
            addAttribute("injurySolicitorAddress1", solicitor.getAddress1());
            addAttribute("injurySolicitorAddress2", solicitor.getAddress2());
            addAttribute("injurySolicitorAddress3", solicitor.getAddress3());
            addAttribute("injurySolicitorAddress4", solicitor.getAddress4());
            addAttribute("injurySolicitorAddress5", solicitor.getAddress5());
            addAttribute("injurySolicitorPostcode", solicitor.getPostcode());
            addAttribute("injurySolicitorTelephone", solicitor.getTelephone());
            addAttribute("injurySolicitorEmail", solicitor.getEmail());
        } else {
            addAttribute("injurySolicitorName", null);
            addAttribute("injurySolicitorAddress1", null);
            addAttribute("injurySolicitorAddress2", null);
            addAttribute("injurySolicitorAddress3", null);
            addAttribute("injurySolicitorAddress4", null);
            addAttribute("injurySolicitorAddress5", null);
            addAttribute("injurySolicitorPostcode", null);
            addAttribute("injurySolicitorTelephone", null);
            addAttribute("injurySolicitorEmail", null);
        }
    }
    
    protected void addClaimThirdPartyAttributes(final Claim claim) {
        ThirdParty thirdParty = claim.getThirdParty();
        if (thirdParty != null) {
            if (thirdParty.getInsurer() != null) {
                addAttribute("thirdPartyInsurerName", thirdParty.getInsurer().getName());
            } else {
                addAttribute("thirdPartyInsurerName", null);
            }
            addAttribute("thirdPartyPolicyNumber", thirdParty.getPolicyNumber());
            addAttribute("thirdPartyClaimReference", thirdParty.getClaimReference());
            addAttribute("thirdPartyFirstName", thirdParty.getFirstName());
            addAttribute("thirdPartyAddress1", thirdParty.getAddress1());
            addAttribute("thirdPartyAddress2", thirdParty.getAddress2());
            addAttribute("thirdPartyAddress3", thirdParty.getAddress3());
            addAttribute("thirdPartyAddress4", thirdParty.getAddress4());
            addAttribute("thirdPartyAddress5", thirdParty.getAddress5());
            addAttribute("thirdPartyPostcode", thirdParty.getPostcode());
            addAttribute("thirdPartyTelephoneDay", thirdParty.getTelephoneDay());
            addAttribute("thirdPartyTelephoneEvening", thirdParty.getTelephoneEvening());
            addAttribute("thirdPartyEmail", thirdParty.getEmail());
            addAttribute("thirdPartyLastName", thirdParty.getLastName());
            addAttribute("thirdPartyTitle", thirdParty.getTitle());
            addAttribute("thirdPartyInsurerBrand", thirdParty.getInsurerBrand());
        } else {
            addAttribute("thirdPartyInsurerName", null);
            addAttribute("thirdPartyPolicyNumber", null);
            addAttribute("thirdPartyClaimReference", null);
            addAttribute("thirdPartyFirstName", null);
            addAttribute("thirdPartyAddress1", null);
            addAttribute("thirdPartyAddress2", null);
            addAttribute("thirdPartyAddress3", null);
            addAttribute("thirdPartyAddress4", null);
            addAttribute("thirdPartyAddress5", null);
            addAttribute("thirdPartyPostcode", null);
            addAttribute("thirdPartyTelephoneDay", null);
            addAttribute("thirdPartyTelephoneEvening", null);
            addAttribute("thirdPartyEmail", null);
            addAttribute("thirdPartyLastName", null);
            addAttribute("thirdPartyTitle", null);
            addAttribute("thirdPartyInsurerBrand", null);
        }
    }
    
    protected void addClaimThirdPartyVehicleAttributes(final Claim claim) {
        ThirdParty thirdParty = claim.getThirdParty();
        if (thirdParty != null) {
            addAttribute("thirdPartyVehicleRegistration", thirdParty.getVehicleRegistration());
            addAttribute("thirdPartyVehicleManufacturer", thirdParty.getVehicleManufacturer());
            addAttribute("thirdPartyVehicleModel", thirdParty.getVehicleModel());
            if (thirdParty.getVehicleClass() != null) {
                addAttribute("thirdPartyVehicleClass", thirdParty.getVehicleClass().getName());
            } else {
                addAttribute("thirdPartyVehicleClass", null);
            }
        } else {
            addAttribute("thirdPartyVehicleRegistration", null);
            addAttribute("thirdPartyVehicleManufacturer", null);
            addAttribute("thirdPartyVehicleModel", null);
            addAttribute("thirdPartyVehicleClass", null);
        }
    }
    
    protected void addClaimHireVehicleAttributes(final Claim claim) {
        VehicleHire vehicleHire = claim.getVehicleHire();
        if (vehicleHire != null) {
            addAttribute("hireVehicleRegistration", vehicleHire.getVehicleRegistration());
            addAttribute("hireVehicleManufacturer", vehicleHire.getVehicleManufacturer());
            addAttribute("hireVehicleModel", vehicleHire.getVehicleModel());
            if (vehicleHire.getVehicleClass() != null) {
                addAttribute("hireVehicleClass", vehicleHire.getVehicleClass().getName());
            } else {
                addAttribute("hireVehicleClass", null);
            }
            addAttribute("hireVehicleRentalStart", vehicleHire.getRentalStart() == null ? null : DateHelper.getLocalDateFormat().format(vehicleHire.getRentalStart()));
            addAttribute("hireVehicleRentalEnd", vehicleHire.getRentalEnd() == null ? null : DateHelper.getLocalDateFormat().format(vehicleHire.getRentalEnd()));
            addAttribute("hireVehicleCollectionReason", vehicleHire.getCollectionReason());
            addAttribute("hireVehicleDays", String.valueOf(vehicleHire.getDays()));
            addAttribute("hireVehicleHpiVehicleManufacturer", vehicleHire.getHpiVehicleManufacturer());
            addAttribute("hireVehicleHpiVehicleModel", vehicleHire.getHpiVehicleModel());
            addAttribute("hireVehicleHpiVehicleYear", vehicleHire.getHpiVehicleYear());
            addAttribute("hireVehicleHpiVehicleCapacity", vehicleHire.getHpiVehicleCapacity());
            addAttribute("hireVehicleHpiVehicleDoorplan", vehicleHire.getHpiVehicleDoorplan());
            addAttribute("hireVehicleHpiVehicleTransmission", vehicleHire.getHpiVehicleTransmission());
            if (vehicleHire.getHpiFirstRegistration() != null) {
                addAttribute("hireVehicleHpiVehicleFirstRegistration", DateHelper.getLocalDateFormat().format(vehicleHire.getHpiFirstRegistration()));
            } else {
                addAttribute("hireVehicleHpiVehicleFirstRegistration", null);
            }
        } else {
            addAttribute("hireVehicleRegistration", null);
            addAttribute("hireVehicleManufacturer", null);
            addAttribute("hireVehicleModel", null);
            addAttribute("hireVehicleClass", null);
            addAttribute("hireVehicleRentalStart", null);
            addAttribute("hireVehicleRentalEnd", null);
            addAttribute("hireVehicleCollectionReason", null);
            addAttribute("hireVehicleDays", null);
            addAttribute("hireVehicleHpiVehicleManufacturer", null);
            addAttribute("hireVehicleHpiVehicleModel", null);
            addAttribute("hireVehicleHpiVehicleYear", null);
            addAttribute("hireVehicleHpiVehicleCapacity", null);
            addAttribute("hireVehicleHpiVehicleDoorplan", null);
            addAttribute("hireVehicleHpiVehicleTransmission", null);
            addAttribute("hireVehicleHpiVehicleFirstRegistration", null);
        }
    }
    
    protected void addClaimEngineerReportAttributes(final Claim claim) {
        EngineerReport engineerReport = claim.getEngineerReport();
        if (engineerReport != null) {
            addAttribute("engineerReportName", engineerReport.getName());
            addAttribute("engineerReportCompany", engineerReport.getCompany());
            addAttribute("engineerReportAddress1", engineerReport.getAddress1());
            addAttribute("engineerReportAddress2", engineerReport.getAddress2());
            addAttribute("engineerReportAddress3", engineerReport.getAddress3());
            addAttribute("engineerReportAddress4", engineerReport.getAddress4());
            addAttribute("engineerReportAddress5", engineerReport.getAddress5());
            addAttribute("engineerReportPostcode", engineerReport.getPostcode());
            addAttribute("engineerReportTelephone", engineerReport.getTelephone());
            addAttribute("engineerReportEmail", engineerReport.getEmail());
            addAttribute("engineerReportIsUsable", String.valueOf(engineerReport.isIsUsable()));
            addAttribute("engineerReportDays", engineerReport.getDays().toString());
            addAttribute("engineerReportLabourAmount", engineerReport.getLabourAmount().toPlainString());
            addAttribute("engineerReportTotalAmount", engineerReport.getTotalAmount().toPlainString());
        } else {
            addAttribute("engineerReportName", null);
            addAttribute("engineerReportCompany", null);
            addAttribute("engineerReportAddress1", null);
            addAttribute("engineerReportAddress2", null);
            addAttribute("engineerReportAddress3", null);
            addAttribute("engineerReportAddress4", null);
            addAttribute("engineerReportAddress5", null);
            addAttribute("engineerReportPostcode", null);
            addAttribute("engineerReportTelephone", null);
            addAttribute("engineerReportEmail", null);
            addAttribute("engineerReportIsUsable", null);
            addAttribute("engineerReportDays", null);
            addAttribute("engineerReportLabourAmount", null);
            addAttribute("engineerReportTotalAmount", null);
        }
    }

    protected void addClaimHireMonitoringAttributes(Claim claim) {
        HireMonitoringDetail hireMonitoringDetail = claim.getHireMonitoringDetail();
        if (hireMonitoringDetail != null) {
            addAttribute("hireMonitoringRepairerName", hireMonitoringDetail.getNameOfRepairer());
            if (hireMonitoringDetail.getRepairBookInDate() != null) {
                addAttribute("hireMonitoringRepairBookedInDate", DateHelper.getLocalDateFormat().format(hireMonitoringDetail.getRepairBookInDate()));
            } else {
                addAttribute("hireMonitoringRepairBookedInDate", null);
            }
            if (hireMonitoringDetail.getInspectionDate() != null) {
                addAttribute("hireMonitoringInspectionDate", DateHelper.getLocalDateFormat().format(hireMonitoringDetail.getInspectionDate()));
            } else {
                addAttribute("hireMonitoringInspectionDate", null);
            }
            addAttribute("hireMonitoringImeName", hireMonitoringDetail.getNameOfIme());
            if (hireMonitoringDetail.getRepairCompletionDate() != null) {
                addAttribute("hireMonitoringRepairCompletionDate", DateHelper.getLocalDateFormat().format(hireMonitoringDetail.getRepairCompletionDate()));
            } else {
                addAttribute("hireMonitoringRepairCompletionDate", null);
            }
            addAttribute("hireMonitoringLabourRate", hireMonitoringDetail.getLabourRate() == null ? null: hireMonitoringDetail.getLabourRate().toPlainString());
            addAttribute("hireMonitoringLabourHours", hireMonitoringDetail.getLabourHour() == null ? null : hireMonitoringDetail.getLabourHour().toPlainString());
            addAttribute("hireMonitoringLabourCost", hireMonitoringDetail.getLabourCost() == null ? null : hireMonitoringDetail.getLabourCost().toPlainString());
            addAttribute("hireMonitoringNonProvisionReason", hireMonitoringDetail.getNonProvisionReason());
            if (hireMonitoringDetail.getRepairAuthorisedDate() != null) {
                addAttribute("hireMonitoringRepairAuthorisedDate", DateHelper.getLocalDateFormat().format(hireMonitoringDetail.getRepairAuthorisedDate()));
            } else {
                addAttribute("hireMonitoringRepairAuthorisedDate", null);
            }
            if (hireMonitoringDetail.getRepairCommencedDate() != null) {
                addAttribute("hireMonitoringRepairCommencedDate", DateHelper.getLocalDateFormat().format(hireMonitoringDetail.getRepairCommencedDate()));
            } else {
                addAttribute("hireMonitoringRepairCommencedDate", null);
            }
            if (hireMonitoringDetail.getTotalLossOfferMadeDate() != null) {
                addAttribute("hireMonitoringTotalLossOfferMadeDate", DateHelper.getLocalDateFormat().format(hireMonitoringDetail.getTotalLossOfferMadeDate()));
            } else {
                addAttribute("hireMonitoringTotalLossOfferMadeDate", null);
            }
            if (hireMonitoringDetail.getTotalLossOfferAcceptedDate() != null) {
                addAttribute("hireMonitoringTotalLossOfferAcceptedDate", DateHelper.getLocalDateFormat().format(hireMonitoringDetail.getTotalLossOfferAcceptedDate()));
            } else {
                addAttribute("hireMonitoringTotalLossOfferAcceptedDate", null);
            }
            if (hireMonitoringDetail.getTotalLossOfferCheckIssuedDate() != null) {
                addAttribute("hireMonitoringTotalLossCheckIssuedDate", DateHelper.getLocalDateFormat().format(hireMonitoringDetail.getTotalLossOfferCheckIssuedDate()));
            } else {
                addAttribute("hireMonitoringTotalLossCheckIssuedDate", null);
            }
            if (hireMonitoringDetail.getTotalLossOfferCheckReceivedDate() != null) {
                addAttribute("hireMonitoringTotalLossCheckReceivedDate", DateHelper.getLocalDateFormat().format(hireMonitoringDetail.getTotalLossOfferCheckReceivedDate()));
            } else {
                addAttribute("hireMonitoringTotalLossCheckReceivedDate", null);
            }
            addAttribute("hireMonitoringIsTotalLoss", hireMonitoringDetail.getIsTotalLossDesc());
            addAttribute("hireMonitoringIsRepairOnly", String.valueOf(hireMonitoringDetail.isIsRepairOnlyCheck()));
            addAttribute("hireMonitoringIsClientVatRegistered", hireMonitoringDetail.getClientVatRegisteredDesc());
        } else {
            addAttribute("hireMonitoringRepairerName", null);
            addAttribute("hireMonitoringRepairBookedInDate", null);
            addAttribute("hireMonitoringInspectionDate", null);
            addAttribute("hireMonitoringImeName", null);
            addAttribute("hireMonitoringRepairCompletionDate", null);
            addAttribute("hireMonitoringLabourRate", null);
            addAttribute("hireMonitoringLabourHours", null);
            addAttribute("hireMonitoringLabourCost", null);
            addAttribute("hireMonitoringNonProvisionReason", null);
            addAttribute("hireMonitoringRepairAuthorisedDate", null);
            addAttribute("hireMonitoringRepairCommencedDate", null);
            addAttribute("hireMonitoringTotalLossOfferMadeDate", null);
            addAttribute("hireMonitoringTotalLossOfferAcceptedDate", null);
            addAttribute("hireMonitoringTotalLossCheckIssuedDate", null);
            addAttribute("hireMonitoringTotalLossCheckReceivedDate", null);
            addAttribute("hireMonitoringIsTotalLoss", null);
            addAttribute("hireMonitoringIsRepairOnly", null);
            addAttribute("hireMonitoringIsClientVatRegistered", null);
        }
        addClaimHireVehicleAttributes(claim);
    }
    
    protected void addClaimInsurerHireMonitoringAttributes(final Claim claim) {
        InsurerHireMonitoringDetail insurerHireMonitoringDetail = claim.getInsurerHireMonitoringDetail();
        if (insurerHireMonitoringDetail != null) {
            if (insurerHireMonitoringDetail.getRepairBookInDate() != null) {
                addAttribute("hireMonitoringRepairBookedInDate", DateHelper.getLocalDateFormat().format(insurerHireMonitoringDetail.getRepairBookInDate()));
            } else {
                addAttribute("hireMonitoringRepairBookedInDate", null);
            }
            if (insurerHireMonitoringDetail.getInspectionDate() != null) {
                addAttribute("hireMonitoringInspectionDate", DateHelper.getLocalDateFormat().format(insurerHireMonitoringDetail.getInspectionDate()));
            } else {
                addAttribute("hireMonitoringInspectionDate", null);
            }
            if (insurerHireMonitoringDetail.getRepairCompletionDate() != null) {
                addAttribute("hireMonitoringRepairCompletionDate", DateHelper.getLocalDateFormat().format(insurerHireMonitoringDetail.getRepairCompletionDate()));
            } else {
                addAttribute("hireMonitoringRepairCompletionDate", null);
            }
            addAttribute("hireMonitoringLabourRate", insurerHireMonitoringDetail.getLabourRate().toPlainString());
            addAttribute("hireMonitoringLabourHours", insurerHireMonitoringDetail.getLabourHour().toPlainString());
            addAttribute("hireMonitoringLabourCost", insurerHireMonitoringDetail.getLabourCost().toPlainString());
            if (insurerHireMonitoringDetail.getRepairAuthorisedDate() != null) {
                addAttribute("hireMonitoringRepairAuthorisedDate", DateHelper.getLocalDateFormat().format(insurerHireMonitoringDetail.getRepairAuthorisedDate()));
            } else {
                addAttribute("hireMonitoringRepairAuthorisedDate", null);
            }
            if (insurerHireMonitoringDetail.getRepairCommencedDate() != null) {
                addAttribute("hireMonitoringRepairCommencedDate", DateHelper.getLocalDateFormat().format(insurerHireMonitoringDetail.getRepairCommencedDate()));
            } else {
                addAttribute("hireMonitoringRepairCommencedDate", null);
            }
            if (insurerHireMonitoringDetail.getTotalLossOfferMadeDate() != null) {
                addAttribute("hireMonitoringTotalLossOfferMadeDate", DateHelper.getLocalDateFormat().format(insurerHireMonitoringDetail.getTotalLossOfferMadeDate()));
            } else {
                addAttribute("hireMonitoringTotalLossOfferMadeDate", null);
            }
            if (insurerHireMonitoringDetail.getTotalLossOfferAcceptedDate() != null) {
                addAttribute("hireMonitoringTotalLossOfferAcceptedDate", DateHelper.getLocalDateFormat().format(insurerHireMonitoringDetail.getTotalLossOfferAcceptedDate()));
            } else {
                addAttribute("hireMonitoringTotalLossOfferAcceptedDate", null);
            }
            if (insurerHireMonitoringDetail.getTotalLossOfferCheckIssuedDate() != null) {
                addAttribute("hireMonitoringTotalLossCheckIssuedDate", DateHelper.getLocalDateFormat().format(insurerHireMonitoringDetail.getTotalLossOfferCheckIssuedDate()));
            } else {
                addAttribute("hireMonitoringTotalLossCheckIssuedDate", null);
            }
            if (insurerHireMonitoringDetail.getTotalLossOfferCheckReceivedDate() != null) {
                addAttribute("hireMonitoringTotalLossCheckReceivedDate", DateHelper.getLocalDateFormat().format(insurerHireMonitoringDetail.getTotalLossOfferCheckReceivedDate()));
            } else {
                addAttribute("hireMonitoringTotalLossCheckReceivedDate", null);
            }
        } else {
            addAttribute("hireMonitoringRepairBookedInDate", null);
            addAttribute("hireMonitoringInspectionDate", null);
            addAttribute("hireMonitoringRepairCompletionDate", null);
            addAttribute("hireMonitoringLabourRate", null);
            addAttribute("hireMonitoringLabourHours", null);
            addAttribute("hireMonitoringLabourCost", null);
            addAttribute("hireMonitoringRepairAuthorisedDate", null);
            addAttribute("hireMonitoringRepairCommencedDate", null);
            addAttribute("hireMonitoringTotalLossOfferMadeDate", null);
            addAttribute("hireMonitoringTotalLossOfferAcceptedDate", null);
            addAttribute("hireMonitoringTotalLossCheckIssuedDate", null);
            addAttribute("hireMonitoringTotalLossCheckReceivedDate", null);
        }
//        addClaimInsurerHireVehicleParameters(generator, claim);
    }
    
    protected void addClaimAuditReviewAttributes(final Claim claim) {
        ClaimAuditReview claimAuditReview = claim.getClaimAuditReview();
        if (claimAuditReview != null) {
            addAttribute("claimType", claimAuditReview.getClaimType());
            addAttribute("whoManagedRepair", claimAuditReview.getWhoManagedRepair());
            addAttribute("hireDurationNotAcceptableReason", claimAuditReview.getHireDurationNotAcceptableReason());
            addAttribute("penaltyChargeAvoidableNote", claimAuditReview.getPenaltyChargeAvoidableNote());
            addAttribute("customerVehicleClass", claimAuditReview.getCustomerVehicleClass() != null ? claimAuditReview.getCustomerVehicleClass().getName() : null);
            addAttribute("hireVehicleClass", claimAuditReview.getHireVehicleClass() != null ? claimAuditReview.getHireVehicleClass().getName() : null);
            addAttribute("totalLoss", String.valueOf(claimAuditReview.getTotalLoss()));
            addAttribute("hireDurationAcceptable", String.valueOf(claimAuditReview.getHireDurationAcceptable()));
            addAttribute("repairCostExceedsEngRec", String.valueOf(claimAuditReview.getRepairCostExceedsEngRec()));
            addAttribute("withinABPGuidelines", String.valueOf(claimAuditReview.getWithinABPGuidelines()));
            addAttribute("storageClaimed", String.valueOf(claimAuditReview.getStorageClaimed()));
            addAttribute("recoveryClaimed", String.valueOf(claimAuditReview.getRecoveryClaimed()));
            addAttribute("hireLeakage", String.valueOf(claimAuditReview.getHireLeakage()));
            addAttribute("penaltyChargeAvoidable", String.valueOf(claimAuditReview.getPenaltyChargeAvoidable()));
            addAttribute("storageClaimedCorrectly", String.valueOf(claimAuditReview.getStorageClaimedCorrectly()));
            addAttribute("recoveryClaimedCorrectly", String.valueOf(claimAuditReview.getRecoveryClaimedCorrectly()));
            addAttribute("claimAuditReviewCompleted", String.valueOf(claimAuditReview.isClaimAuditReviewCompleted()));
            addAttribute("hireDuration", String.valueOf(claimAuditReview.getHireDuration()));
            addAttribute("totalHireCost", claimAuditReview.getTotalHireCost().toPlainString());
            addAttribute("totalRepairCost", claimAuditReview.getTotalRepairCost().toPlainString());
            addAttribute("penaltyChargesPaid", claimAuditReview.getPenaltyChargesPaid().toPlainString());
            addAttribute("hireLeakageCost", claimAuditReview.getHireLeakageCost().toPlainString());
            addAttribute("exceededRepairCost", claimAuditReview.getExceededRepairCost().toPlainString());
            addAttribute("nonABPGuidelineRepairLabourRate", claimAuditReview.getNonABPGuidelineRepairLabourRate().toPlainString());
            addAttribute("auditCompletedDate", DateHelper.getLocalDateFormat().format(claimAuditReview.getAuditCompletedDate()));
            addAttribute("completedBy", claimAuditReview.getCompletedBy().getDisplayName());
        }
    }

    protected void addClaimHireMonitoringEcdAttributes(final Claim claim) {
        List<HireMonitoringEcd> hireMonitoringEcds = claim.getHireMonitoringEcds();
        if (hireMonitoringEcds != null) {
            // For now, we'll just send the latest
            addAttribute("hireMonitoringEcdDate",  DateHelper.getLocalDateFormat().format(hireMonitoringEcds.get(hireMonitoringEcds.size()-1).getEcdDate()));
            addAttribute("hireMonitoringEcdSupportingNote", hireMonitoringEcds.get(hireMonitoringEcds.size()-1).getSupportingNote());
            addAttribute("hireMonitoringEcdReason", hireMonitoringEcds.get(hireMonitoringEcds.size()-1).getReason());
        } else {
            addAttribute("hireMonitoringEcdDate", null);
            addAttribute("hireMonitoringEcdSupportingNote", null);
            addAttribute("hireMonitoringEcdReason", null);
        }
    }

    protected void addClaimLiabilityAttributes(final Claim claim) {
        addAttribute("liabilityAcceptedInsurer", claim.getPercentageLiabilityAccepted().toPlainString());
        addAttribute("liabilityAcceptedCHO", claim.getPercentageLiabilityCho().toPlainString());
        if (claim.getLiabilityAgreedDate() != null) {
            addAttribute("liabilityAgreedDate", DateHelper.getLocalDateFormat().format(claim.getLiabilityAgreedDate()));
        } else {
            addAttribute("liabilityAgreedDate", null);
        }
        if (claim.getLiabilityStatus() != null) {
            addAttribute("liabilityStatus", claim.getLiabilityStatus().toString());
        } else {
            addAttribute("liabilityStatus", null);
        }
    }

    protected void addInvoiceAttributes(final Claim claim) {
        Invoice invoice = claim.getInvoice();
        if (invoice != null) {
            if (invoice.getDateInvoiced() != null) {
                addAttribute("invoiceDate", DateHelper.getLocalDateFormat().format(invoice.getDateInvoiced()));
            } else {
                addAttribute("invoiceDate", null);
            }
            addAttribute("invoiceInvoiceNo", invoice.getClaimInvoiceNo());
            addAttribute("invoiceHireNet", invoice.getHireNet().toPlainString());
            addAttribute("invoiceHireVat", invoice.getHireVat().toPlainString());
            addAttribute("invoiceHireGross", invoice.getHireGross().toPlainString());
            addAttribute("invoiceRepairNet", invoice.getRepairNet().toPlainString());
            addAttribute("invoiceRepairVat", invoice.getRepairVat().toPlainString());
            addAttribute("invoiceRepairGross", invoice.getRepairGross().toPlainString());
            addAttribute("invoiceEngineerFeeNet", invoice.getEngineerFeeNet().toPlainString());
            addAttribute("invoiceEngineerFeeVat", invoice.getEngineerFeeVat().toPlainString());
            addAttribute("invoiceEngineerFeeGross", invoice.getEngineerFeeGross().toPlainString());
            addAttribute("invoiceStorageRecoveryNet", invoice.getStorageRecoveryNet().toPlainString());
            addAttribute("invoiceStorageRecoveryVat", invoice.getStorageRecoveryVat().toPlainString());
            addAttribute("invoiceStorageRecoveryGross", invoice.getStorageRecoveryGross().toPlainString());
            addAttribute("invoiceTotalNet", invoice.getTotalNet().toPlainString());
            addAttribute("invoiceTotalVat", invoice.getTotalVat().toPlainString());
            addAttribute("invoiceTotalGross", invoice.getTotalGross().toPlainString());
            addAttribute("invoiceFullTotaRequested", invoice.getFullTotalToPay().toPlainString());
            addAttribute("invoiceMiscellaneousFee", invoice.getMiscellaneousFee().toPlainString());
            addAttribute("invoiceAutomaticFee", invoice.getAutomaticFee().toPlainString());
            addAttribute("invoiceSatNavFee", invoice.getSatNavFee().toPlainString());
            addAttribute("invoiceEstateFee", invoice.getEstateFee().toPlainString());
            addAttribute("invoiceBabySeatFee", invoice.getBabySeatFee().toPlainString());
            addAttribute("invoiceTowBarsFee", invoice.getTowBarsFee().toPlainString());
            addAttribute("invoiceNonStandardPremiumFee", invoice.getNonStandardInsurancePremiumFee().toPlainString());
            addAttribute("invoiceAdminFee", invoice.getAdminFee().toPlainString());
            addAttribute("invoiceRoofRackFee", invoice.getRoofRackFee().toPlainString());
            addAttribute("invoiceDualControlFee", invoice.getDualControlFee().toPlainString());
            addAttribute("invoiceDeliveryCollectionFee", invoice.getDeliveryCollectionFee().toPlainString());
            addAttribute("invoiceEngineerReviewNotes", invoice.getEngineerInvoiceReviewNotes());
            addAttribute("invoiceDayHireRate", invoice.getHireRateChargedPerDay().toPlainString());
            addAttribute("invoiceExcessCollected", invoice.getExcessAmountCollected().toPlainString());
            addAttribute("invoiceVatCollected", invoice.getVatAmountCollected().toPlainString());
            addAttribute("invoiceHirePenaltyCharge", invoice.getHirePenaltyCharge().toPlainString());
            if (invoice.getHirePenaltyChargeAppliedDate() != null) {
                addAttribute("invoiceHirePenaltyChargeAppliedDate", DateHelper.getLocalDateFormat().format(invoice.getHirePenaltyChargeAppliedDate()));
            } else {
                addAttribute("invoiceHirePenaltyChargeAppliedDate", null);
            }
            addAttribute("invoiceTotalToPay", invoice.getTotalToPay().toPlainString());
            addAttribute("invoiceAdditionalDriverFee", invoice.getAdditionalDriverFee().toPlainString());
            addAttribute("invoiceIsCoverNoteRequired", invoice.getCoverNoteRequired() == null ? "" : invoice.getCoverNoteRequired().toString());
            addAttribute("invoiceTotalLossNet", invoice.getTotalLossFeeNet().toPlainString());
            addAttribute("invoiceTotalLossVat", invoice.getTotalLossFeeVat().toPlainString());
            addAttribute("invoiceTotalLossGross", invoice.getTotalLossFeeGross().toPlainString());
            addAttribute("invoiceHirePenaltyPercentage", invoice.getHirePenaltyPercentage());
            addAttribute("invoiceInterimPaymentMade", invoice.getInterimPaymentMade() == null ? null : invoice.getInterimPaymentMade().toPlainString());
            addAttribute("invoiceRepairPenaltyPercentage", invoice.getRepairPenaltyPercentage());
            if (invoice.getRepairPenaltyChargeAppliedDate() != null) {
                addAttribute("invoiceRepairPenaltyChargeAppliedDate", DateHelper.getLocalDateFormat().format(invoice.getRepairPenaltyChargeAppliedDate()));
            } else {
                addAttribute("invoiceRepairPenaltyChargeAppliedDate", null);
            }
            addAttribute("invoiceTotalPenaltyCharge", invoice.getTotalPenaltyCharge().toPlainString());
            addAttribute("invoiceInterimPaymentReceived", invoice.getInterimPaymentReceived().toPlainString());
            addAttribute("invoiceRepairAdminFee", invoice.getRepairAdminFee() == null ? null : invoice.getRepairAdminFee().toPlainString());
            addAttribute("invoiceRepairAcquisitionFee", invoice.getRepairAcquisitionFee() == null ? null : invoice.getRepairAcquisitionFee().toPlainString());
            addAttribute("invoiceCollaborationFee", invoice.getCollaborationFee() == null ? null : invoice.getCollaborationFee().toPlainString());
        } else {
            addAttribute("invoiceDate", null);
            addAttribute("invoiceInvoiceNo", null);
            addAttribute("invoiceHireNet", null);
            addAttribute("invoiceHireVat", null);
            addAttribute("invoiceHireGross", null);
            addAttribute("invoiceRepairNet", null);
            addAttribute("invoiceRepairVat", null);
            addAttribute("invoiceRepairGross", null);
            addAttribute("invoiceEngineerFeeNet", null);
            addAttribute("invoiceEngineerFeeVat", null);
            addAttribute("invoiceEngineerFeeGross", null);
            addAttribute("invoiceStorageRecoveryNet", null);
            addAttribute("invoiceStorageRecoveryVat", null);
            addAttribute("invoiceStorageRecoveryGross", null);
            addAttribute("invoiceTotalNet", null);
            addAttribute("invoiceTotalVat", null);
            addAttribute("invoiceTotalGross", null);
            addAttribute("invoiceFullTotaRequested", null);
            addAttribute("invoiceMiscellaneousFee", null);
            addAttribute("invoiceAutomaticFee", null);
            addAttribute("invoiceSatNavFee", null);
            addAttribute("invoiceEstateFee", null);
            addAttribute("invoiceBabySeatFee", null);
            addAttribute("invoiceTowBarsFee", null);
            addAttribute("invoiceNonStandardPremiumFee", null);
            addAttribute("invoiceAdminFee", null);
            addAttribute("invoiceRoofRackFee", null);
            addAttribute("invoiceDualControlFee", null);
            addAttribute("invoiceDeliveryCollectionFee", null);
            addAttribute("invoiceEngineerReviewNotes", null);
            addAttribute("invoiceDayHireRate", null);
            addAttribute("invoiceExcessCollected", null);
            addAttribute("invoiceVatCollected", null);
            addAttribute("invoiceHirePenaltyCharge", null);
            addAttribute("invoiceHirePenaltyChargeAppliedDate", null);
            addAttribute("invoiceTotalToPay", null);
            addAttribute("invoiceAdditionalDriverFee", null);
            addAttribute("invoiceIsCoverNoteRequired", null);
            addAttribute("invoiceTotalLossNet", null);
            addAttribute("invoiceTotalLossVat", null);
            addAttribute("invoiceTotalLossGross", null);
            addAttribute("invoiceHirePenaltyPercentage", null);
            addAttribute("invoiceInterimPaymentMade", null);
            addAttribute("invoiceRepairPenaltyPercentage", null);
            addAttribute("invoiceRepairPenaltyChargeAppliedDate", null);
            addAttribute("invoiceTotalPenaltyCharge", null);
            addAttribute("invoiceInterimPaymentReceived", null);
            addAttribute("invoiceRepairAdminFee", null);
            addAttribute("invoiceRepairAcquisitionFee", null);
            addAttribute("invoiceCollaborationFee", null);
        }
    }
    protected void addInvoicePaidAttributes(Claim claim) {
        Invoice invoice = claim.getInvoice();
        if (invoice != null) {
            addAttribute("invoiceHireGrossPaid", invoice.getHireGrossPaid().toPlainString());
            addAttribute("invoiceRepairGrossPaid", invoice.getRepairGrossPaid().toPlainString());
            addAttribute("invoiceEngineerFeeGrossPaid", invoice.getEngineerFeeGrossPaid().toPlainString());
            addAttribute("invoiceTotalLossFeeGrossPaid", invoice.getTotalLossFeeGrossPaid().toPlainString());
            addAttribute("invoiceStorageRecoveryGrossPaid", invoice.getStorageRecoveryGrossPaid().toPlainString());
            addAttribute("invoiceHirePenaltyChargePaid", invoice.getHirePenaltyChargePaid().toPlainString());
            addAttribute("invoiceRepairPenaltyChargePaid", invoice.getRepairPenaltyChargePaid().toPlainString());
            addAttribute("invoiceFinalPayment", invoice.getFinalPayment().toPlainString());
        } else {
            addAttribute("invoiceHireGrossPaid", null);
            addAttribute("invoiceRepairGrossPaid", null);
            addAttribute("invoiceEngineerFeeGrossPaid", null);
            addAttribute("invoiceTotalLossFeeGrossPaid", null);
            addAttribute("invoiceStorageRecoveryGrossPaid", null);
            addAttribute("invoiceHirePenaltyChargePaid", null);
            addAttribute("invoiceRepairPenaltyChargePaid", null);
            addAttribute("invoiceFinalPayment", null);
        }
    }
}
