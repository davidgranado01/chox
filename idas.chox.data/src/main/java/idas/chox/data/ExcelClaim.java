package idas.chox.data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.ClaimType;
import idas.chox.core.model.LiabilityStatus;

/**
 * @author John
 */
public class ExcelClaim {

    private static final Logger LOG = LoggerFactory.getLogger(ExcelClaim.class);
    private String claimStatus;
    private String claimType;
    private String claimChoReference;
    private String claimChorganisationName;
    private String claimWorkgroupName;
    private Date claimLastReviewDate;
    private Date claimStatusModifiedDate;
    private BigDecimal claimIdemnity;
    private String claimIndemnityStance;
    private String claimLiabilityStatus;
    private Date claimLiabilityAgreedDate;
    private BigDecimal claimPercentageLiabilityAccepted;
    private BigDecimal claimPercentageLiabilityApplied;
    private BigDecimal claimPercentageLiabilityCho;
    private String claimManagingRepair;
    private Date claimPolicyHolderContactDate;
    private Date claimCreditAgreementDate;
    private Date claimGtaNoticeDate;
    private String claimClaimNumber;
    private String claimClaimOwnerDisplayName;
    private String claimSupplierClaimOwnerDisplayName;
    private String claimCustomerTitle;
    private String claimCustomerFirstName;
    private String claimCustomerLastName;
    private String claimCustomerAddress1;
    private String claimCustomerAddress2;
    private String claimCustomerAddress3;
    private String claimCustomerAddress4;
    private String claimCustomerAddress5;
    private String claimCustomerPostcode;
    private String claimCustomerTelephoneDay;
    private String claimCustomerTelephoneEvening;
    private String claimCustomerEmail;
    private Integer claimCustomerAge;
    private String claimCustomerOccupation;
    private String claimCustomerPolicyUsage;
    private String claimCustomerInsurerName;
    private String claimCustomerPolicyNumber;
    private String claimCustomerClaimReference;
    private String claimCustomerComprehensive;
    private String claimCustomerVehicleManufacturer;
    private String claimCustomerVehicleModel;
    private String claimCustomerVehicleRegistration;
    private String claimCustomerVehicleYear;
    private String claimCustomerVehicleClassName;
    private String claimCustomerLocation;
    private String claimCustomerHpiVehicleManufacturer;
    private String claimCustomerHpiVehicleModel;
    private String claimCustomerHpiVehicleYear;
    private Date claimCustomerHpiFirstRegistration;
    private String claimCustomerHpiVehicleCapacity;
    private String claimCustomerHpiVehicleDoorplan;
    private String claimCustomerHpiVehicleTransmission;
    private String claimCustomerCanAccessOtherVehicleDesc;
    private String claimCustomerOtherVehicleUsedDesc;
    private String claimCustomerOtherVehicle;
    private String claimCustomerCourtesyCarEntitledDesc;
    private String claimCustomerSpecificVehicleRequiredDesc;
    private String claimCustomerSpecificVehicleReason;
    private String claimCustomerTypeVehicleRequired;
    private String claimCustomerSpecialRequirements;
    private String claimCustomerAverageDailyMileage;
    private String claimThirdPartyTitle;
    private String claimThirdPartyFirstName;
    private String claimThirdPartyLastName;
    private String claimThirdPartyAddress1;
    private String claimThirdPartyAddress2;
    private String claimThirdPartyAddress3;
    private String claimThirdPartyAddress4;
    private String claimThirdPartyAddress5;
    private String claimThirdPartyPostcode;
    private String claimThirdPartyTelephoneDay;
    private String claimThirdPartyTelephoneEvening;
    private String claimThirdPartyEmail;
    private String claimThirdPartyInsurerName;
    private String claimThirdPartyPolicyNumber;
    private String claimThirdPartyVehicleManufacturer;
    private String claimThirdPartyVehicleModel;
    private String claimThirdPartyVehicleRegistration;
    private String claimThirdPartyVehicleClassName;
    private String claimCustomerDamage;
    private String claimCustomerIsUsable;
    private String claimCustomerIsTotalLoss;
    private Date claimCustomerInitialECD;
    private Date claimIncidentDate;
    private String claimIncidentLocation;
    private String claimIncidentIsPoliceInvolved;
    private String claimIncidentIncidentDescription;
    private String claimWitnessName;
    private String claimWitnessAddress1;
    private String claimWitnessAddress2;
    private String claimWitnessAddress3;
    private String claimWitnessAddress4;
    private String claimWitnessAddress5;
    private String claimWitnessPostcode;
    private String claimWitnessTelephoneDay;
    private String claimWitnessTelephoneEvening;
    private String claimWitnessEmail;
    private String claimInjuryName;
    private String claimInjuryAddress1;
    private String claimInjuryAddress2;
    private String claimInjuryAddress3;
    private String claimInjuryAddress4;
    private String claimInjuryAddress5;
    private String claimInjuryPostcode;
    private String claimInjuryTelephoneDay;
    private String claimInjuryTelephoneEvening;
    private String claimInjuryEmail;
    private String claimSolicitorName;
    private String claimSolicitorAddress1;
    private String claimSolicitorAddress2;
    private String claimSolicitorAddress3;
    private String claimSolicitorAddress4;
    private String claimSolicitorAddress5;
    private String claimSolicitorPostcode;
    private String claimSolicitorTelephone;
    private String claimSolicitorEmail;
    private BigDecimal claimEngineerReportLabourAmount;
    private BigDecimal claimEngineerReportRepairAmount;
    private Integer claimEngineerReportDays;
    private String claimEngineerReportIsUsable;
    private String claimEngineerReportName;
    private String claimEngineerReportCompany;
    private String claimEngineerReportAddress1;
    private String claimEngineerReportAddress2;
    private String claimEngineerReportAddress3;
    private String claimEngineerReportAddress4;
    private String claimEngineerReportAddress5;
    private String claimEngineerReportPostcode;
    private String claimEngineerReportTelephone;
    private String claimEngineerReportEmail;
    private String claimVehicleHireVehicleManufacturer;
    private String claimVehicleHireVehicleModel;
    private String claimVehicleHireVehicleRegistration;
    private String claimVehicleHireVehicleClassName;
    private Date claimVehicleHireRentalStart;
    private Date claimVehicleHireRentalEnd;
    private Integer claimVehicleHireDays;
    private String claimVehicleHireCollectionReason;
    private String claimVehicleHireHpiVehicleManufacturer;
    private String claimVehicleHireHpiVehicleModel;
    private String claimVehicleHireHpiVehicleYear;
    private Date claimVehicleHireHpiFirstRegistration;
    private String claimVehicleHireHpiVehicleCapacity;
    private String claimVehicleHireHpiVehicleDoorplan;
    private String claimVehicleHireHpiVehicleTransmission;
    private String claimHireMonitoringDetailNameOfRepairer;
    private Date claimHireMonitoringDetailRepairBookInDate;
    private Date claimHireMonitoringDetailRepairAuthorisedDate;
    private Date claimHireMonitoringDetailRepairCommencedDate;
    private Date claimHireMonitoringDetailInspectionBookedDate;
    private Date claimHireMonitoringDetailInspectionDate;
    private String claimHireMonitoringDetailNameOfIme;
    private Date claimHireMonitoringDetailRepairCompletionDate;
    private String claimHireMonitoringDetailIsTotalLostCheck;
    private Date claimHireMonitoringDetailTotalLossOfferMadeDate;
    private Date claimHireMonitoringDetailTotalLossOfferAcceptedDate;
    private Date claimHireMonitoringDetailTotalLossOfferCheckIssuedDate;
    private Date claimHireMonitoringDetailTotalLossOfferCheckReceivedDate;
    private BigDecimal claimHireMonitoringDetailLabourRate;
    private BigDecimal claimHireMonitoringDetailLabourHour;
    private String claimRepairOnlyCheck;
    private String claimNonFaultInsurerRepair;
    private String claimClientVatRegistered;
    private BigDecimal claimHireMonitoringDetailLabourCost;
    private String claimHireMonitoringDetailNonProvisionReason;
    private Date claimHireMonitoringDetailNextReviewDate;
    private String claimFinalReview;
    private String claimInvoiceReview;
    private String claimInvoiceReviewReason;
    private String claimRemainingSlaDays;
    private String claimFraudScore;
    private String claimFraudStatus;
    private boolean isInsurer;
    private Date claimInsHMDRepairBookInDate;
    private Date claimInsHMDRepairAuthorisedDate;
    private Date claimInsHMDRepairCommencedDate;
    private Date claimInsHMDInspectionBookedDate;
    private Date claimInsHMDInspectionDate;
    private Date claimInsHMDRepairCompletionDate;
    private Date claimInsHMDTotalLossOfferMadeDate;
    private Date claimInsHMDTotalLossOfferAcceptedDate;
    private Date claimInsHMDTotalLossOfferPaymentIssuedDate;
    private String claimInsHMDPaymentType;
    private Date claimInsHMDTotalLossOfferPaymentReceivedDate;
    private BigDecimal claimInsHMDLabourRate;
    private BigDecimal claimInsHMDLabourHour;
    private BigDecimal claimInsHMDLabourCost;
    private Boolean claimInsHMDClaimantImpecunious;
    private String claimInsHMDWhoManagedRepair;
    private String claimInsHMDReplacementVehicleClass;
    private Date claimInsHMDRentalStart;
    private Date claimInsHMDTpReportedIncidentToTPI;
    private Date claimInsHMDTotalLossReportSentToUs;
    private Date claimInsHMDPartsReceived;
    private Date claimInsHMDRentalEnd;
    private Boolean claimCopleyOfferMade;
    private Date claimCopleyOfferMadeDate;
    private boolean isCopleyOffer;

    public ExcelClaim(Map data, Boolean isIns) {
        try {
            claimStatus = (String) data.get("status");
            claimRemainingSlaDays = (String) data.get("remaining_sla_days_str");
            claimType = (ClaimType.values()[(Integer) data.get("claim_type")]).toString();
            claimChoReference = (String) data.get("cho_reference");
            claimChorganisationName = (String) data.get("chorg_name");
            claimWorkgroupName = (String) data.get("workgroup_name");
            claimStatusModifiedDate = (Date) data.get("status_modified_date");
            claimLastReviewDate = (Date) data.get("last_review_date");
            claimIdemnity = (BigDecimal) data.get("indeminty_amount");
            claimLiabilityStatus = (LiabilityStatus.values()[(Short) data.get("liability_status")]).toString();
            claimLiabilityAgreedDate = (Date) data.get("liability_agreed_date");
            claimIndemnityStance = (String) data.get("indemnity_stance");
            claimPercentageLiabilityAccepted = (BigDecimal) data.get("percentage_liability_accepted");
            claimPercentageLiabilityApplied = (BigDecimal) data.get("applied_liability");
            claimPercentageLiabilityCho = (BigDecimal) data.get("percentage_liability_cho");
            claimManagingRepair = ((Boolean) data.get("managing_repair")) ? "Yes" : "No";
            claimPolicyHolderContactDate = (Date) data.get("policy_holder_contact_date");
            claimCreditAgreementDate = (Date) data.get("credit_agreement_date");
            claimGtaNoticeDate = (Date) data.get("gta_notice_date");
            claimClaimNumber = (String) data.get("claim_number");
            claimClaimOwnerDisplayName = (String) data.get("claim_owner");
            claimSupplierClaimOwnerDisplayName = (String) data.get("claim_supplier_owner");
            claimCustomerTitle = (String) data.get("customer_title");
            claimCustomerFirstName = (String) data.get("customer_first_name");
            claimCustomerLastName = (String) data.get("customer_last_name");
            claimCustomerAddress1 = (String) data.get("customer_address1");
            claimCustomerAddress2 = (String) data.get("customer_address2");
            claimCustomerAddress3 = (String) data.get("customer_address3");
            claimCustomerAddress4 = (String) data.get("customer_address4");
            claimCustomerAddress5 = (String) data.get("customer_address5");
            claimCustomerPostcode = (String) data.get("customer_postcode");
            claimCustomerTelephoneDay = (String) data.get("customer_telephone_day");
            claimCustomerTelephoneEvening = (String) data.get("customer_telephone_evening");
            claimCustomerEmail = (String) data.get("customer_email");
            claimCustomerAge = (Integer) data.get("customer_age");
            claimCustomerOccupation = (String) data.get("customer_occupation");
            claimCustomerPolicyUsage = (String) data.get("customer_policy_usage");
            claimCustomerInsurerName = (String) data.get("customer_insurer_name");
            claimCustomerPolicyNumber = (String) data.get("customer_policy_number");
            claimCustomerClaimReference = (String) data.get("customer_claim_reference");
            claimCustomerComprehensive = ((Boolean) data.get("customer_comprehensive")) ? "Yes" : "No";
            claimCustomerVehicleManufacturer = (String) data.get("customer_vehicle_manufacturer");
            claimCustomerVehicleModel = (String) data.get("customer_vehicle_model");
            claimCustomerVehicleRegistration = ((String) data.get("customer_vehicle_registration")) != null && ((String) data.get("customer_vehicle_registration")).startsWith("~~") ? "GDPR: data removed" : (String) data.get("customer_vehicle_registration");
            claimCustomerVehicleYear = (String) data.get("customer_vehicle_year");
            claimCustomerVehicleClassName = (String) data.get("customer_vehicle_class");
            claimCustomerLocation = (String) data.get("customer_location");
            claimCustomerHpiVehicleManufacturer = (String) data.get("customer_hpi_vehicle_manufacturer");
            claimCustomerHpiVehicleModel = (String) data.get("customer_hpi_vehicle_model");
            claimCustomerHpiVehicleYear = (String) data.get("customer_hpi_vehicle_year");
            claimCustomerHpiFirstRegistration = (Date) data.get("customer_hpi_vehicle_first_registration");
            claimCustomerHpiVehicleCapacity = (String) data.get("customer_hpi_vehicle_capacity");
            claimCustomerHpiVehicleDoorplan = (String) data.get("customer_hpi_vehicle_doorplan");
            claimCustomerHpiVehicleTransmission = (String) data.get("customer_hpi_vehicle_transmission");
            if (isIns == null) {
                claimFinalReview = (((Boolean) data.get("final_review_cho")) ? "Yes" : "No") + " (CHO), "
                        + (((Boolean) data.get("final_review_ins")) ? "Yes" : "No") + " (Ins)";
                claimCopleyOfferMade = null;
                claimCopleyOfferMadeDate = null;
                isCopleyOffer = false;
                claimInvoiceReview = (((Boolean) data.get("is_invoice_review_required")) ? "Yes" : "No");
                claimInvoiceReviewReason = (String) data.get("invoice_review_reason");
            } else if (isIns) {
                claimFinalReview = ((Boolean) data.get("final_review_ins")) ? "Yes" : "No";
                claimInvoiceReview = (((Boolean) data.get("is_invoice_review_required")) ? "Yes" : "No");
                claimInvoiceReviewReason = (String) data.get("invoice_review_reason");
                isCopleyOffer = ((boolean) data.get("copley_question"));
                if (isCopleyOffer) {
                    claimCopleyOfferMade = (Boolean) data.get("copley_offer_made");
                    claimCopleyOfferMadeDate = (Date) data.get("copley_offer_made_date");
                } else {
                    claimCopleyOfferMade = null;
                    claimCopleyOfferMadeDate = null;
                }
            } else { // CHO
                claimCopleyOfferMade = null;
                claimCopleyOfferMadeDate = null;
                isCopleyOffer = false;
                claimFinalReview = ((Boolean) data.get("final_review_cho")) ? "Yes" : "No";
                claimInvoiceReview = null;
                claimInvoiceReviewReason = null;
            }
            Boolean canAccessOtherVehicle = (Boolean) data.get("customer_access_other_vehicle");
            if (canAccessOtherVehicle == null) {
                claimCustomerCanAccessOtherVehicleDesc = "";
            } else {
                claimCustomerCanAccessOtherVehicleDesc = canAccessOtherVehicle ? "Yes" : "No";
            }
            Boolean otherVehicleUsed = (Boolean) data.get("customer_other_vehicle_used");
            if (otherVehicleUsed == null) {
                claimCustomerOtherVehicleUsedDesc = "";
            } else {
                claimCustomerOtherVehicleUsedDesc = otherVehicleUsed ? "Yes" : "No";
            }
            claimCustomerOtherVehicle = (String) data.get("customer_other_vehicle");
            Boolean courtesyCarEntitled = (Boolean) data.get("customer_courtesy_car");
            if (courtesyCarEntitled == null) {
                claimCustomerCourtesyCarEntitledDesc = "";
            } else {
                claimCustomerCourtesyCarEntitledDesc = courtesyCarEntitled ? "Yes" : "No";
            }
            Boolean specificVehicleRequiredDesc = (Boolean) data.get("customer_specific_vehicle");
            if (specificVehicleRequiredDesc == null) {
                claimCustomerSpecificVehicleRequiredDesc = "";
            } else {
                claimCustomerSpecificVehicleRequiredDesc = specificVehicleRequiredDesc ? "Yes" : "No";
            }
            claimCustomerSpecificVehicleReason = (String) data.get("customer_specific_vehicle_reason");
            claimCustomerTypeVehicleRequired = (String) data.get("customer_vehicle_type_required");
            claimCustomerSpecialRequirements = (String) data.get("customer_special_requirements");
            claimCustomerAverageDailyMileage = (String) data.get("customer_average_daily_mileage");
            claimCustomerDamage = (String) data.get("customer_damage");
            Boolean b = (Boolean) data.get("customer_is_usable");
            claimCustomerIsUsable = b == null ? "Unknown" : b ? "Yes" : "No";
            b = (Boolean) data.get("customer_is_total_loss");
            claimCustomerIsTotalLoss = b == null ? "" : b ? "Yes" : "No";
            claimCustomerInitialECD = (Date) data.get("customer_initial_ecd");
            claimThirdPartyTitle = (String) data.get("tp_title");
            claimThirdPartyFirstName = (String) data.get("tp_first_name");
            claimThirdPartyLastName = (String) data.get("tp_last_name");
            claimThirdPartyAddress1 = (String) data.get("tp_address1");
            claimThirdPartyAddress2 = (String) data.get("tp_address2");
            claimThirdPartyAddress3 = (String) data.get("tp_address3");
            claimThirdPartyAddress4 = (String) data.get("tp_address4");
            claimThirdPartyAddress5 = (String) data.get("tp_address5");
            claimThirdPartyPostcode = (String) data.get("tp_postcode");
            claimThirdPartyTelephoneDay = (String) data.get("tp_telephone_day");
            claimThirdPartyTelephoneEvening = (String) data.get("tp_telephone_evening");
            claimThirdPartyEmail = (String) data.get("tp_email");
            claimThirdPartyInsurerName = (String) data.get("tp_insurer_name");
            claimThirdPartyPolicyNumber = (String) data.get("tp_policy_number");
            claimThirdPartyVehicleManufacturer = (String) data.get("tp_vehicle_manufacturer");
            claimThirdPartyVehicleModel = (String) data.get("tp_vehicle_model");
            claimThirdPartyVehicleRegistration = ((String) data.get("tp_vehicle_registration")) != null && ((String) data.get("tp_vehicle_registration")).startsWith("~~") ? "GDPR: data removed" : (String) data.get("tp_vehicle_registration");
            claimThirdPartyVehicleClassName = (String) data.get("tp_vehicle_class");
            claimIncidentDate = (Date) data.get("incident_date");
            claimIncidentLocation = (String) data.get("incident_location");
            Boolean isPoliceInvolved = (Boolean) data.get("incident_is_police_involved");
            if (isPoliceInvolved == null) {
                claimIncidentIsPoliceInvolved = "";
            } else {
                claimIncidentIsPoliceInvolved = isPoliceInvolved ? "Yes" : "No";
            }
            claimIncidentIncidentDescription = (String) data.get("incident_description");
            claimWitnessName = (String) data.get("witness_name");
            claimWitnessAddress1 = (String) data.get("witness_address1");
            claimWitnessAddress2 = (String) data.get("witness_address2");
            claimWitnessAddress3 = (String) data.get("witness_address3");
            claimWitnessAddress4 = (String) data.get("witness_address4");
            claimWitnessAddress5 = (String) data.get("witness_address5");
            claimWitnessPostcode = (String) data.get("witness_postcode");
            claimWitnessTelephoneDay = (String) data.get("witness_telephone_day");
            claimWitnessTelephoneEvening = (String) data.get("witness_telephone_evening");
            claimWitnessEmail = (String) data.get("witness_email");
            claimInjuryName = (String) data.get("injury_name");
            claimInjuryAddress1 = (String) data.get("injury_address1");
            claimInjuryAddress2 = (String) data.get("injury_address2");
            claimInjuryAddress3 = (String) data.get("injury_address3");
            claimInjuryAddress4 = (String) data.get("injury_address4");
            claimInjuryAddress5 = (String) data.get("injury_address5");
            claimInjuryPostcode = (String) data.get("injury_postcode");
            claimInjuryTelephoneDay = (String) data.get("injury_evening_day");
            claimInjuryTelephoneEvening = (String) data.get("injury_telephone_evening");
            claimInjuryEmail = (String) data.get("injury_email");
            claimSolicitorName = (String) data.get("injury_solicitor_name");
            claimSolicitorAddress1 = (String) data.get("injury_solicitor_address1");
            claimSolicitorAddress2 = (String) data.get("injury_solicitor_address2");
            claimSolicitorAddress3 = (String) data.get("injury_solicitor_address3");
            claimSolicitorAddress4 = (String) data.get("injury_solicitor_address4");
            claimSolicitorAddress5 = (String) data.get("injury_solicitor_address5");
            claimSolicitorPostcode = (String) data.get("injury_solicitor_postcode");
            claimSolicitorTelephone = (String) data.get("injury_solicitor_telephone");
            claimSolicitorEmail = (String) data.get("injury_solicitor_email");

            claimEngineerReportLabourAmount = (BigDecimal) data.get("er_labour_amount");
            claimEngineerReportRepairAmount = (BigDecimal) data.get("er_repair_amount");
            BigDecimal days = (BigDecimal) data.get("er_days");
            if (days == null) {
                claimEngineerReportDays = null;
            } else {
                claimEngineerReportDays = days.intValue();
            }
            Boolean isUsable = (Boolean) data.get("er_is_usable");
            if (isUsable == null) {
                claimEngineerReportIsUsable = "Unknown";
            } else {
                claimEngineerReportIsUsable = isUsable ? "Yes" : "No";
            }
            claimEngineerReportName = (String) data.get("er_name");
            claimEngineerReportCompany = (String) data.get("er_company");
            claimEngineerReportAddress1 = (String) data.get("er_address1");
            claimEngineerReportAddress2 = (String) data.get("er_address2");
            claimEngineerReportAddress3 = (String) data.get("er_address3");
            claimEngineerReportAddress4 = (String) data.get("er_address4");
            claimEngineerReportAddress5 = (String) data.get("er_address5");
            claimEngineerReportPostcode = (String) data.get("er_postcode");
            claimEngineerReportTelephone = (String) data.get("er_telephone");
            claimEngineerReportEmail = (String) data.get("er_email");
            claimVehicleHireVehicleManufacturer = (String) data.get("vh_vehicle_manufacturer");
            claimVehicleHireVehicleModel = (String) data.get("vh_vehicle_model");
            claimVehicleHireVehicleRegistration = ((String) data.get("vh_vehicle_registration")) != null && ((String) data.get("vh_vehicle_registration")).startsWith("~~") ? "GDPR: data removed" : (String) data.get("vh_vehicle_registration");
            claimVehicleHireVehicleClassName = (String) data.get("vh_vehicle_class_name");
            claimVehicleHireRentalStart = (Date) data.get("vh_rental_start");
            claimVehicleHireRentalEnd = (Date) data.get("vh_rental_end");
            days = (BigDecimal) data.get("vh_days");
            if (days == null) {
                claimVehicleHireDays = null;
            } else {
                claimVehicleHireDays = days.intValue();
            }
//        claimVehicleHireDays = ((BigDecimal) data.get("vh_days")).intValue();
            claimVehicleHireCollectionReason = (String) data.get("vh_collection_reason");
            claimVehicleHireHpiVehicleManufacturer = (String) data.get("vh_hpi_vehicle_manufacturer");
            claimVehicleHireHpiVehicleModel = (String) data.get("vh_hpi_vehicle_model");
            claimVehicleHireHpiVehicleYear = (String) data.get("vh_hpi_vehicle_year");
            claimVehicleHireHpiFirstRegistration = (Date) data.get("vh_hpi_vehicle_first_registration");
            claimVehicleHireHpiVehicleCapacity = (String) data.get("vh_hpi_vehicle_capacity");
            claimVehicleHireHpiVehicleDoorplan = (String) data.get("vh_hpi_vehicle_doorplan");
            claimVehicleHireHpiVehicleTransmission = (String) data.get("vh_hpi_vehicle_transmission");

            claimHireMonitoringDetailNameOfRepairer = (String) data.get("hmd_name_of_repairer");
            claimHireMonitoringDetailRepairBookInDate = (Date) data.get("hmd_repair_book_in_date");
            claimHireMonitoringDetailRepairAuthorisedDate = (Date) data.get("hmd_repair_authorised_date");
            claimHireMonitoringDetailRepairCommencedDate = (Date) data.get("hmd_repair_commenced_date");
            claimHireMonitoringDetailInspectionBookedDate = (Date) data.get("hmd_inspection_booked_date");
            claimHireMonitoringDetailInspectionDate = (Date) data.get("hmd_inspection_date");
            claimHireMonitoringDetailNameOfIme = (String) data.get("hmd_name_of_ime");
            claimHireMonitoringDetailRepairCompletionDate = (Date) data.get("hmd_repair_completion_date");

            Boolean isTotalLostCheck = (Boolean) data.get("hmd_is_total_lost_check");
            if (isTotalLostCheck == null) {
                claimHireMonitoringDetailIsTotalLostCheck = "";
            } else {
                claimHireMonitoringDetailIsTotalLostCheck = isTotalLostCheck ? "Yes" : "No";
            }
            claimHireMonitoringDetailTotalLossOfferMadeDate = (Date) data.get("hmd_total_loss_offer_made");
            claimHireMonitoringDetailTotalLossOfferAcceptedDate = (Date) data.get("hmd_total_loss_offer_accepted");
            claimHireMonitoringDetailTotalLossOfferCheckIssuedDate = (Date) data.get("hmd_total_loss_check_issued");
            claimHireMonitoringDetailTotalLossOfferCheckReceivedDate = (Date) data.get("hmd_total_loss_check_received");
            claimHireMonitoringDetailLabourRate = (BigDecimal) data.get("hmd_labour_rate");
            claimHireMonitoringDetailLabourHour = (BigDecimal) data.get("hmd_labour_hour");
            Boolean isRepairCheckOnly = (Boolean) data.get("claim_repair_only_check");
            if (isRepairCheckOnly == null) {
                claimRepairOnlyCheck = "";
            } else {
                claimRepairOnlyCheck = isRepairCheckOnly ? "Yes" : "No";
            }
            Boolean isNonFaultInsurerRepair = (Boolean) data.get("claim_non_fault_insurer_repair");
            if (isNonFaultInsurerRepair == null) {
                claimNonFaultInsurerRepair = "";
            } else {
                claimNonFaultInsurerRepair = isNonFaultInsurerRepair ? "Yes" : "No";
            }
            Boolean clientVatRegistered = (Boolean) data.get("claim_client_vat_registered");
            if (clientVatRegistered == null) {
                claimClientVatRegistered = "";
            } else {
                claimClientVatRegistered = clientVatRegistered ? "Yes" : "No";
            }
            claimHireMonitoringDetailLabourCost = (BigDecimal) data.get("hmd_labour_cost");
            claimHireMonitoringDetailNonProvisionReason = (String) data.get("hmd_non_provision_reason");
            claimHireMonitoringDetailNextReviewDate = (Date) data.get("hmd_next_review_date");

            int fraudCheckStatus = (Integer) data.get("fraud_check_status");
            claimFraudScore = isIns != null && isIns && fraudCheckStatus == 3 ? ((Integer) data.get("fraud_score")).toString() : "";
            claimFraudStatus = isIns != null && isIns && fraudCheckStatus == 3 ? (String) data.get("fraud_status") : "";
            isInsurer = isIns == null ? false : isIns;

            claimInsHMDRepairBookInDate = (Date) data.get("ihmd_repair_book_in_date");
            claimInsHMDRepairAuthorisedDate = (Date) data.get("ihmd_repair_authorised_date");
            claimInsHMDRepairCommencedDate = (Date) data.get("ihmd_repair_commenced_date");
            claimInsHMDInspectionBookedDate = (Date) data.get("ihmd_inspection_booked_date");
            claimInsHMDInspectionDate = (Date) data.get("ihmd_inspection_date");
            claimInsHMDRepairCompletionDate = (Date) data.get("ihmd_repair_completion_date");
            claimInsHMDTotalLossOfferMadeDate = (Date) data.get("ihmd_total_loss_offer_made");
            claimInsHMDTotalLossOfferAcceptedDate = (Date) data.get("ihmd_total_loss_offer_accepted");
            claimInsHMDTotalLossOfferPaymentIssuedDate = (Date) data.get("ihmd_total_loss_payment_issued");
            claimInsHMDPaymentType = (String) data.get("ihmd_payment_type");
            claimInsHMDTotalLossOfferPaymentReceivedDate = (Date) data.get("ihmd_total_loss_payment_received");
            claimInsHMDLabourRate = (BigDecimal) data.get("ihmd_labour_rate");
            claimInsHMDLabourHour = (BigDecimal) data.get("ihmd_labour_hour");
            claimInsHMDLabourCost = (BigDecimal) data.get("ihmd_labour_cost");
            claimInsHMDClaimantImpecunious = (Boolean) data.get("ihmd_claimant_impecunious");
            claimInsHMDWhoManagedRepair = (String) data.get("ihmd_who_managed_repair");
            claimInsHMDReplacementVehicleClass = (String) data.get("ihmd_replacement_vehicle_class");
            claimInsHMDRentalStart = (Date) data.get("ihmd_rental_start");
            claimInsHMDTpReportedIncidentToTPI = (Date) data.get("ihmd_tp_reported_incident_to_tpi");
            claimInsHMDTotalLossReportSentToUs = (Date) data.get("ihmd_tl_report_sent_to_us");
            claimInsHMDPartsReceived = (Date) data.get("ihmd_parts_received");
            claimInsHMDRentalEnd = (Date) data.get("ihmd_rental_end");
        } catch (Exception ex) {
            LOG.error("Exception thrown export claims grid: \n", ex);
        }
    }

    public String getClaimType() {
        return claimType;
    }

    public String getClaimChoReference() {
        return claimChoReference;
    }

    public String getClaimChorganisationName() {
        return claimChorganisationName;
    }

    public String getClaimClaimNumber() {
        return claimClaimNumber;
    }

    public String getClaimClaimOwnerDisplayName() {
        return claimClaimOwnerDisplayName;
    }

    public String getClaimSupplierClaimOwnerDisplayName() {
        return claimSupplierClaimOwnerDisplayName;
    }

    public Date getClaimCreditAgreementDate() {
        return claimCreditAgreementDate;
    }

    public String getClaimCustomerAddress1() {
        return claimCustomerAddress1;
    }

    public String getClaimCustomerAddress2() {
        return claimCustomerAddress2;
    }

    public String getClaimCustomerAddress3() {
        return claimCustomerAddress3;
    }

    public String getClaimCustomerAddress4() {
        return claimCustomerAddress4;
    }

    public String getClaimCustomerAddress5() {
        return claimCustomerAddress5;
    }

    public String getClaimCustomerAverageDailyMileage() {
        return claimCustomerAverageDailyMileage;
    }

    public String getClaimCustomerCanAccessOtherVehicleDesc() {
        return claimCustomerCanAccessOtherVehicleDesc;
    }

    public String getClaimCustomerClaimReference() {
        return claimCustomerClaimReference;
    }

    public String getClaimCustomerComprehensive() {
        return claimCustomerComprehensive;
    }

    public String getClaimCustomerCourtesyCarEntitledDesc() {
        return claimCustomerCourtesyCarEntitledDesc;
    }

    public String getClaimCustomerDamage() {
        return claimCustomerDamage;
    }

    public String getClaimCustomerEmail() {
        return claimCustomerEmail;
    }

    public String getClaimCustomerFirstName() {
        return claimCustomerFirstName;
    }

    public Date getClaimCustomerHpiFirstRegistration() {
        return claimCustomerHpiFirstRegistration;
    }

    public String getClaimCustomerHpiVehicleCapacity() {
        return claimCustomerHpiVehicleCapacity;
    }

    public String getClaimCustomerHpiVehicleDoorplan() {
        return claimCustomerHpiVehicleDoorplan;
    }

    public String getClaimCustomerHpiVehicleManufacturer() {
        return claimCustomerHpiVehicleManufacturer;
    }

    public String getClaimCustomerHpiVehicleModel() {
        return claimCustomerHpiVehicleModel;
    }

    public String getClaimCustomerHpiVehicleTransmission() {
        return claimCustomerHpiVehicleTransmission;
    }

    public String getClaimCustomerHpiVehicleYear() {
        return claimCustomerHpiVehicleYear;
    }

    public Date getClaimCustomerInitialECD() {
        return claimCustomerInitialECD;
    }

    public String getClaimCustomerInsurerName() {
        return claimCustomerInsurerName;
    }

    public String getClaimCustomerIsTotalLoss() {
        return claimCustomerIsTotalLoss;
    }

    public String getClaimCustomerIsUsable() {
        return claimCustomerIsUsable;
    }

    public String getClaimCustomerLastName() {
        return claimCustomerLastName;
    }

    public String getClaimCustomerLocation() {
        return claimCustomerLocation;
    }

    public String getClaimCustomerOtherVehicle() {
        return claimCustomerOtherVehicle;
    }

    public String getClaimCustomerOtherVehicleUsedDesc() {
        return claimCustomerOtherVehicleUsedDesc;
    }

    public String getClaimCustomerPolicyNumber() {
        return claimCustomerPolicyNumber;
    }

    public String getClaimCustomerPostcode() {
        return claimCustomerPostcode;
    }

    public String getClaimCustomerSpecialRequirements() {
        return claimCustomerSpecialRequirements;
    }

    public String getClaimCustomerSpecificVehicleReason() {
        return claimCustomerSpecificVehicleReason;
    }

    public String getClaimCustomerSpecificVehicleRequiredDesc() {
        return claimCustomerSpecificVehicleRequiredDesc;
    }

    public String getClaimCustomerTelephoneDay() {
        return claimCustomerTelephoneDay;
    }

    public String getClaimCustomerTelephoneEvening() {
        return claimCustomerTelephoneEvening;
    }

    public String getClaimCustomerTitle() {
        return claimCustomerTitle;
    }

    public String getClaimCustomerTypeVehicleRequired() {
        return claimCustomerTypeVehicleRequired;
    }

    public String getClaimCustomerVehicleClassName() {
        return claimCustomerVehicleClassName;
    }

    public String getClaimCustomerVehicleManufacturer() {
        return claimCustomerVehicleManufacturer;
    }

    public String getClaimCustomerVehicleModel() {
        return claimCustomerVehicleModel;
    }

    public String getClaimCustomerVehicleRegistration() {
        return claimCustomerVehicleRegistration;
    }

    public String getClaimCustomerVehicleYear() {
        return claimCustomerVehicleYear;
    }

    public String getClaimEngineerReportAddress1() {
        return claimEngineerReportAddress1;
    }

    public String getClaimEngineerReportAddress2() {
        return claimEngineerReportAddress2;
    }

    public String getClaimEngineerReportAddress3() {
        return claimEngineerReportAddress3;
    }

    public String getClaimEngineerReportAddress4() {
        return claimEngineerReportAddress4;
    }

    public String getClaimEngineerReportAddress5() {
        return claimEngineerReportAddress5;
    }

    public String getClaimEngineerReportCompany() {
        return claimEngineerReportCompany;
    }

    public Integer getClaimEngineerReportDays() {
        return claimEngineerReportDays;
    }

    public String getClaimEngineerReportEmail() {
        return claimEngineerReportEmail;
    }

    public String getClaimEngineerReportIsUsable() {
        return claimEngineerReportIsUsable;
    }

    public BigDecimal getClaimEngineerReportLabourAmount() {
        return claimEngineerReportLabourAmount;
    }

    public String getClaimEngineerReportName() {
        return claimEngineerReportName;
    }

    public String getClaimEngineerReportPostcode() {
        return claimEngineerReportPostcode;
    }

    public BigDecimal getClaimEngineerReportRepairAmount() {
        return claimEngineerReportRepairAmount;
    }

    public String getClaimEngineerReportTelephone() {
        return claimEngineerReportTelephone;
    }

    public Date getClaimGtaNoticeDate() {
        return claimGtaNoticeDate;
    }

    public Date getClaimHireMonitoringDetailInspectionBookedDate() {
        return claimHireMonitoringDetailInspectionBookedDate;
    }

    public Date getClaimHireMonitoringDetailInspectionDate() {
        return claimHireMonitoringDetailInspectionDate;
    }

    public String getClaimHireMonitoringDetailIsTotalLostCheck() {
        return claimHireMonitoringDetailIsTotalLostCheck;
    }

    public BigDecimal getClaimHireMonitoringDetailLabourCost() {
        return claimHireMonitoringDetailLabourCost;
    }

    public BigDecimal getClaimHireMonitoringDetailLabourHour() {
        return claimHireMonitoringDetailLabourHour;
    }

    public BigDecimal getClaimHireMonitoringDetailLabourRate() {
        return claimHireMonitoringDetailLabourRate;
    }

    public String getClaimHireMonitoringDetailNameOfIme() {
        return claimHireMonitoringDetailNameOfIme;
    }

    public String getClaimHireMonitoringDetailNameOfRepairer() {
        return claimHireMonitoringDetailNameOfRepairer;
    }

    public Date getClaimHireMonitoringDetailNextReviewDate() {
        return claimHireMonitoringDetailNextReviewDate;
    }

    public String getClaimHireMonitoringDetailNonProvisionReason() {
        return claimHireMonitoringDetailNonProvisionReason;
    }

    public Date getClaimHireMonitoringDetailRepairAuthorisedDate() {
        return claimHireMonitoringDetailRepairAuthorisedDate;
    }

    public Date getClaimHireMonitoringDetailRepairBookInDate() {
        return claimHireMonitoringDetailRepairBookInDate;
    }

    public Date getClaimHireMonitoringDetailRepairCommencedDate() {
        return claimHireMonitoringDetailRepairCommencedDate;
    }

    public Date getClaimHireMonitoringDetailRepairCompletionDate() {
        return claimHireMonitoringDetailRepairCompletionDate;
    }

    public Date getClaimHireMonitoringDetailTotalLossOfferAcceptedDate() {
        return claimHireMonitoringDetailTotalLossOfferAcceptedDate;
    }

    public Date getClaimHireMonitoringDetailTotalLossOfferCheckIssuedDate() {
        return claimHireMonitoringDetailTotalLossOfferCheckIssuedDate;
    }

    public Date getClaimHireMonitoringDetailTotalLossOfferCheckReceivedDate() {
        return claimHireMonitoringDetailTotalLossOfferCheckReceivedDate;
    }

    public Date getClaimHireMonitoringDetailTotalLossOfferMadeDate() {
        return claimHireMonitoringDetailTotalLossOfferMadeDate;
    }

    public BigDecimal getClaimIdemnity() {
        return claimIdemnity;
    }

    public Date getClaimIncidentDate() {
        return claimIncidentDate;
    }

    public String getClaimIncidentIncidentDescription() {
        return claimIncidentIncidentDescription;
    }

    public String getClaimIncidentIsPoliceInvolved() {
        return claimIncidentIsPoliceInvolved;
    }

    public String getClaimIncidentLocation() {
        return claimIncidentLocation;
    }

    public String getClaimInjuryAddress1() {
        return claimInjuryAddress1;
    }

    public String getClaimInjuryAddress2() {
        return claimInjuryAddress2;
    }

    public String getClaimInjuryAddress3() {
        return claimInjuryAddress3;
    }

    public String getClaimInjuryAddress4() {
        return claimInjuryAddress4;
    }

    public String getClaimInjuryAddress5() {
        return claimInjuryAddress5;
    }

    public String getClaimInjuryEmail() {
        return claimInjuryEmail;
    }

    public String getClaimInjuryName() {
        return claimInjuryName;
    }

    public String getClaimInjuryPostcode() {
        return claimInjuryPostcode;
    }

    public String getClaimInjuryTelephoneDay() {
        return claimInjuryTelephoneDay;
    }

    public String getClaimInjuryTelephoneEvening() {
        return claimInjuryTelephoneEvening;
    }

    public String getClaimLiabilityStatus() {
        return claimLiabilityStatus;
    }

    public Date getClaimLiabilityAgreedDate() {
        return claimLiabilityAgreedDate;
    }

    public String getClaimIndemnityStance() {
        return claimIndemnityStance;
    }

    public String getClaimManagingRepair() {
        return claimManagingRepair;
    }

    public BigDecimal getClaimPercentageLiabilityAccepted() {
        return claimPercentageLiabilityAccepted;
    }

    public BigDecimal getClaimPercentageLiabilityApplied() {
        return claimPercentageLiabilityApplied;
    }

    public BigDecimal getClaimPercentageLiabilityCho() {
        return claimPercentageLiabilityCho;
    }

    public Date getClaimPolicyHolderContactDate() {
        return claimPolicyHolderContactDate;
    }

    public String getClaimSolicitorAddress1() {
        return claimSolicitorAddress1;
    }

    public String getClaimSolicitorAddress2() {
        return claimSolicitorAddress2;
    }

    public String getClaimSolicitorAddress3() {
        return claimSolicitorAddress3;
    }

    public String getClaimSolicitorAddress4() {
        return claimSolicitorAddress4;
    }

    public String getClaimSolicitorAddress5() {
        return claimSolicitorAddress5;
    }

    public String getClaimSolicitorEmail() {
        return claimSolicitorEmail;
    }

    public String getClaimSolicitorName() {
        return claimSolicitorName;
    }

    public String getClaimSolicitorPostcode() {
        return claimSolicitorPostcode;
    }

    public String getClaimSolicitorTelephone() {
        return claimSolicitorTelephone;
    }

    public String getClaimStatus() {
        return claimStatus;
    }

    public Date getClaimStatusModifiedDate() {
        return claimStatusModifiedDate;
    }

    public Date getClaimLastReviewDate() {
        return claimLastReviewDate;
    }

    public String getClaimThirdPartyAddress1() {
        return claimThirdPartyAddress1;
    }

    public String getClaimThirdPartyAddress2() {
        return claimThirdPartyAddress2;
    }

    public String getClaimThirdPartyAddress3() {
        return claimThirdPartyAddress3;
    }

    public String getClaimThirdPartyAddress4() {
        return claimThirdPartyAddress4;
    }

    public String getClaimThirdPartyAddress5() {
        return claimThirdPartyAddress5;
    }

    public String getClaimThirdPartyEmail() {
        return claimThirdPartyEmail;
    }

    public String getClaimThirdPartyFirstName() {
        return claimThirdPartyFirstName;
    }

    public String getClaimThirdPartyInsurerName() {
        return claimThirdPartyInsurerName;
    }

    public String getClaimThirdPartyLastName() {
        return claimThirdPartyLastName;
    }

    public String getClaimThirdPartyPolicyNumber() {
        return claimThirdPartyPolicyNumber;
    }

    public String getClaimThirdPartyPostcode() {
        return claimThirdPartyPostcode;
    }

    public String getClaimThirdPartyTelephoneDay() {
        return claimThirdPartyTelephoneDay;
    }

    public String getClaimThirdPartyTelephoneEvening() {
        return claimThirdPartyTelephoneEvening;
    }

    public String getClaimThirdPartyTitle() {
        return claimThirdPartyTitle;
    }

    public String getClaimThirdPartyVehicleClassName() {
        return claimThirdPartyVehicleClassName;
    }

    public String getClaimThirdPartyVehicleManufacturer() {
        return claimThirdPartyVehicleManufacturer;
    }

    public String getClaimThirdPartyVehicleModel() {
        return claimThirdPartyVehicleModel;
    }

    public String getClaimVehicleHireCollectionReason() {
        return claimVehicleHireCollectionReason;
    }

    public Integer getClaimVehicleHireDays() {
        return claimVehicleHireDays;
    }

    public Date getClaimVehicleHireHpiFirstRegistration() {
        return claimVehicleHireHpiFirstRegistration;
    }

    public String getClaimVehicleHireHpiVehicleCapacity() {
        return claimVehicleHireHpiVehicleCapacity;
    }

    public String getClaimVehicleHireHpiVehicleDoorplan() {
        return claimVehicleHireHpiVehicleDoorplan;
    }

    public String getClaimVehicleHireHpiVehicleManufacturer() {
        return claimVehicleHireHpiVehicleManufacturer;
    }

    public String getClaimVehicleHireHpiVehicleModel() {
        return claimVehicleHireHpiVehicleModel;
    }

    public String getClaimVehicleHireHpiVehicleTransmission() {
        return claimVehicleHireHpiVehicleTransmission;
    }

    public String getClaimVehicleHireHpiVehicleYear() {
        return claimVehicleHireHpiVehicleYear;
    }

    public Date getClaimVehicleHireRentalEnd() {
        return claimVehicleHireRentalEnd;
    }

    public Date getClaimVehicleHireRentalStart() {
        return claimVehicleHireRentalStart;
    }

    public String getClaimVehicleHireVehicleClassName() {
        return claimVehicleHireVehicleClassName;
    }

    public String getClaimVehicleHireVehicleManufacturer() {
        return claimVehicleHireVehicleManufacturer;
    }

    public String getClaimVehicleHireVehicleModel() {
        return claimVehicleHireVehicleModel;
    }

    public String getClaimVehicleHireVehicleRegistration() {
        return claimVehicleHireVehicleRegistration;
    }

    public String getClaimWitnessAddress1() {
        return claimWitnessAddress1;
    }

    public String getClaimWitnessAddress2() {
        return claimWitnessAddress2;
    }

    public String getClaimWitnessAddress3() {
        return claimWitnessAddress3;
    }

    public String getClaimWitnessAddress4() {
        return claimWitnessAddress4;
    }

    public String getClaimWitnessAddress5() {
        return claimWitnessAddress5;
    }

    public String getClaimWitnessEmail() {
        return claimWitnessEmail;
    }

    public String getClaimWitnessName() {
        return claimWitnessName;
    }

    public String getClaimWitnessPostcode() {
        return claimWitnessPostcode;
    }

    public String getClaimWitnessTelephoneDay() {
        return claimWitnessTelephoneDay;
    }

    public String getClaimWitnessTelephoneEvening() {
        return claimWitnessTelephoneEvening;
    }

    public String getClaimWorkgroupName() {
        return claimWorkgroupName;
    }

    public String getClaimThirdPartyVehicleRegistration() {
        return claimThirdPartyVehicleRegistration;
    }

    public Integer getClaimCustomerAge() {
        return claimCustomerAge;
    }

    public String getClaimCustomerOccupation() {
        return claimCustomerOccupation;
    }

    public String getClaimCustomerPolicyUsage() {
        return claimCustomerPolicyUsage;
    }

    public String getClaimRepairOnlyCheck() {
        return claimRepairOnlyCheck;
    }

    public String getClaimNonFaultInsurerRepair() {
        return claimNonFaultInsurerRepair;
    }

    public String getClaimClientVatRegistered() {
        return claimClientVatRegistered;
    }

    public String getClaimFinalReview() {
        return claimFinalReview;
    }

    public String getClaimRemainingSlaDays() {
        return claimRemainingSlaDays;
    }

    public String getClaimFraudScore() {
        return claimFraudScore;
    }

    public String getClaimFraudStatus() {
        return claimFraudStatus;
    }

    public boolean isIsInsurer() {
        return isInsurer;
    }

    public Date getClaimInsHMDRepairBookInDate() {
        return claimInsHMDRepairBookInDate;
    }

    public Date getClaimInsHMDRepairAuthorisedDate() {
        return claimInsHMDRepairAuthorisedDate;
    }

    public Date getClaimInsHMDRepairCommencedDate() {
        return claimInsHMDRepairCommencedDate;
    }

    public Date getClaimInsHMDInspectionBookedDate() {
        return claimInsHMDInspectionBookedDate;
    }

    public Date getClaimInsHMDInspectionDate() {
        return claimInsHMDInspectionDate;
    }

    public Date getClaimInsHMDRepairCompletionDate() {
        return claimInsHMDRepairCompletionDate;
    }

    public Date getClaimInsHMDTotalLossOfferMadeDate() {
        return claimInsHMDTotalLossOfferMadeDate;
    }

    public Date getClaimInsHMDTotalLossOfferAcceptedDate() {
        return claimInsHMDTotalLossOfferAcceptedDate;
    }

    public Date getClaimInsHMDTotalLossOfferPaymentIssuedDate() {
        return claimInsHMDTotalLossOfferPaymentIssuedDate;
    }

    public String getClaimInsHMDPaymentType() {
        return claimInsHMDPaymentType;
    }

    public Date getClaimInsHMDTotalLossOfferPaymentReceivedDate() {
        return claimInsHMDTotalLossOfferPaymentReceivedDate;
    }

    public BigDecimal getClaimInsHMDLabourRate() {
        return claimInsHMDLabourRate;
    }

    public BigDecimal getClaimInsHMDLabourHour() {
        return claimInsHMDLabourHour;
    }

    public BigDecimal getClaimInsHMDLabourCost() {
        return claimInsHMDLabourCost;
    }

    public String getClaimInsHMDClaimantImpecunious() {
        return claimInsHMDClaimantImpecunious == null ? "" : claimInsHMDClaimantImpecunious ? "Yes" : "No";
    }

    public String getClaimInsHMDWhoManagedRepair() {
        return claimInsHMDWhoManagedRepair;
    }

    public String getClaimInsHMDReplacementVehicleClass() {
        return claimInsHMDReplacementVehicleClass;
    }

    public Date getClaimInsHMDRentalStart() {
        return claimInsHMDRentalStart;
    }

    public String getClaimCopleyOfferMade() {
        return claimCopleyOfferMade == null ? "" : claimCopleyOfferMade ? "Yes" : "No";
    }

    public Date getClaimCopleyOfferMadeDate() {
        return claimCopleyOfferMadeDate;
    }

    public boolean isIsCopleyOffer() {
        return isCopleyOffer;
    }

    public String getClaimInvoiceReview() {
        return claimInvoiceReview;
    }

    public String getClaimInvoiceReviewReason() {
        return claimInvoiceReviewReason;
    }

    public Date getClaimInsHMDTpReportedIncidentToTPI() {
        return claimInsHMDTpReportedIncidentToTPI;
    }

    public Date getClaimInsHMDTotalLossReportSentToUs() {
        return claimInsHMDTotalLossReportSentToUs;
    }

    public Date getClaimInsHMDPartsReceived() {
        return claimInsHMDPartsReceived;
    }

    public Date getClaimInsHMDRentalEnd() {
        return claimInsHMDRentalEnd;
    }

}
