package idas.chox.service.reports.viewdata;

import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Customer;
import idas.chox.core.model.EngineerReport;
import idas.chox.core.model.HireMonitoringDetail;
import idas.chox.core.model.Incident;
import idas.chox.core.model.Injury;
import idas.chox.core.model.Invoice;
import idas.chox.core.model.LiabilityStatus;
import idas.chox.core.model.Solicitor;
import idas.chox.core.model.ThirdParty;
import idas.chox.core.model.VehicleHire;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.Witness;
import idas.chox.core.services.PenaltyChargeService;
import idas.chox.core.util.DateHelper;


/**
 *
 * @author John
 */
public class ClaimFileReportData {
    private static final Logger LOG = LoggerFactory.getLogger(ClaimFileReportData.class);
    private String choName;
    private String createdBy;
    private String createdOn;
    private String supplierReference;
    private String insurerClaimNumber;
    private String customer;
    private String status;
    private String liabilityStatus;
    private String contactDate;
    private String claimOwner;
    private String supplierClaimOwner;
    private String workgroup;
    private BigDecimal indemnityValue;
    private BigDecimal insurerLiabilityAgreed;
    private BigDecimal choLiabilityAgreed;
    private String dateLiabilityAgreed;
    private String customerTitle;
    private String customerFirstName;
    private String customerSurname;
    private String customerAddress1;
    private String customerAddress2;
    private String customerAddress3;
    private String customerAddress4;
    private String customerAddress5;
    private String customerPostcode;
    private String customerTelephoneDay;
    private String customerTelephoneEvening;
    private String customerEmail;
    private Integer customerAge;
    private String customerOccupation;
    private String customerPolicyUsage;
    private String customerInsurer;
    private String customerPolicyNumber;
    private String customerClaimNumber;
    private String customerComprehensive;
    private String customerVehicleManufacturer;
    private String customerVehicleModel;
    private String customerVehicleYear;
    private String customerVehicleClass;
    private String customerVRN;
    private String customerVehicleLocation;
    private String customerHpiVehicleManufacturer;
    private String customerHpiVehicleModel;
    private String customerHpiVehicleYear;
    private String customerHpiVehicleRegistrationDate;
    private String customerHpiVehicleCapacity;
    private String customerHpiVehicleDoorplan;
    private String customerHpiVehicleTransmission;
    private String thirdPartyTitle;
    private String thirdPartyFirstName;
    private String thirdPartySurname;
    private String thirdPartyAddress1;
    private String thirdPartyAddress2;
    private String thirdPartyAddress3;
    private String thirdPartyAddress4;
    private String thirdPartyAddress5;
    private String thirdPartyPostcode;
    private String thirdPartyTelephoneDay;
    private String thirdPartyTelephoneEvening;
    private String thirdPartyEmail;
    private String thirdPartyInsurer;
    private String thirdPartyInsurerBrand;
    private String thirdPartyPolicyNumber;
    private String thirdPartyVehicleManufacturer;
    private String thirdPartyVehicleModel;
    private String thirdPartyVRN;
    private String thirdPartyVehicleClass;
    private String managingRepair;
    private String noticeDate;
    private String creditAgreementSignedDate;
    private String invoiceReviewRequired;
    private String incidentDate;
    private String incidentLocation;
    private String incidentPoliceInvolved;
    private String incidentDescription;
    private String totalLoss;
    private String usable;
    private String description;
    private String witnessName;
    private String witnessAddress1;
    private String witnessAddress2;
    private String witnessAddress3;
    private String witnessAddress4;
    private String witnessAddress5;
    private String witnessPostcode;
    private String witnessTelephoneDay;
    private String witnessTelephoneEvening;
    private String witnessEmail;
    private String injuryName;
    private String injuryAddress1;
    private String injuryAddress2;
    private String injuryAddress3;
    private String injuryAddress4;
    private String injuryAddress5;
    private String injuryPostcode;
    private String injuryTelephoneDay;
    private String injuryTelephoneEvening;
    private String injuryEmail;
    private String solicitorName;
    private String solicitorAddress1;
    private String solicitorAddress2;
    private String solicitorAddress3;
    private String solicitorAddress4;
    private String solicitorAddress5;
    private String solicitorPostcode;
    private String solicitorTelephoneDay;
    private String solicitorEmail;
    private String hireMonNextReviewDate;
    private String hireMonOriginalECD;
    private String hireMonRepairerName;
    private String hireMonBookedInDate;
    private String hireMonAuthorisedDate;
    private String hireMonCommencedDate;
    private String hireMonInspectionBookedDate;
    private String hireMonInspectionDate;
    private String hireMonTotalLoss;
    private String hireMonTotalLossOfferMadeDate;
    private String hireMonTotalLossAcceptedDate;
    private String hireMonTotalLossChequeIssuedDate;
    private String hireMonTotalLossChequeReceivedDate;
    private String hireMonRepairCompletionDate;
    private String hireMonIME;
    private BigDecimal hireMonLabourRate;
    private BigDecimal hireMonLabourHours;
    private BigDecimal hireMonTotalLabourCost;
    private String hireMonNonProvisionReason;
    private String isRepairOnlyCheck;
    private String isNFInsurerManagingRepair;
    private String clientVatRegistered;
    private String hireVehicleManufacturer;
    private String hireVehicleModel;
    private String hireVehicleRegistration;
    private String hireVehicleClass;
    private String hireVehicleHireStart;
    private String hireVehicleHireEnd;
    private String hireVehicleReasonForCollection;
    private Integer hireVehicleNoHireDays;
    private String hireVehicleHpiVehicleManufacturer;
    private String hireVehicleHpiVehicleModel;
    private String hireVehicleHpiVehicleYear;
    private String hireVehicleHpiVehicleRegistrationDate;
    private String hireVehicleHpiVehicleCapacity;
    private String hireVehicleHpiVehicleDoorplan;
    private String hireVehicleHpiVehicleTransmission;
    private String invoiceSupplierClaimsHandlingNo;
    private String invoiceSupplierClaimInvoiceNo;
    private BigDecimal invoiceHireRate;
    private BigDecimal invoiceHireNet;
    private BigDecimal invoiceHireVat;
    private BigDecimal invoiceHireGross;
    private BigDecimal invoiceRepairNet;
    private BigDecimal invoiceRepairVat;
    private BigDecimal invoiceRepairGross;
    private BigDecimal invoiceEngineerFeeNet;
    private BigDecimal invoiceEngineerFeeVat;
    private BigDecimal invoiceEngineerFeeGross;
    private BigDecimal invoiceTotalLossFeeNet;
    private BigDecimal invoiceTotalLossFeeVat;
    private BigDecimal invoiceTotalLossFeeGross;
    private BigDecimal invoiceStorageRecoveryNet;
    private BigDecimal invoiceStorageRecoveryVat;
    private BigDecimal invoiceStorageRecoveryGross;
    private BigDecimal invoiceTotalNet;
    private BigDecimal invoiceTotalVat;
    private BigDecimal invoiceTotalGross;
    private BigDecimal invoiceClaimsHandlingAmount;
    private BigDecimal invoiceDeductionHandlingFee;
    private BigDecimal invoiceDiscount;
    private BigDecimal invoiceInsurerDiscount;
    private String invoiceInterimPayment;
    private BigDecimal invoiceInterimPaymentAmount;
    private BigDecimal invoiceTotalPenaltyCharge;
    private BigDecimal invoiceHirePenaltyChargeAmount;
    private BigDecimal invoiceRepairPenaltyChargeAmount;
    private String invoiceHirePenaltyChargePercentage;
    private String invoiceHirePenaltyChargePercentageApplied;
    private String invoiceRepairPenaltyChargePercentage;
    private String invoiceRepairPenaltyChargePercentageApplied;
    private BigDecimal invoiceFullTotalToPay;
    private BigDecimal originalInvoiceFullTotalToPay;
    private BigDecimal invoiceTotalToPay;
    private BigDecimal originalInvoiceTotalToPay;
    private BigDecimal invoiceExcessAmountCollected;
    private BigDecimal invoiceVATAmountCollected;
    private String invoiceDate;
    private String invoiceUploadedDate;
    private String penaltyStartDate;
    private BigDecimal extrasMiscellaneousFee;
    private String extrasMiscellaneousTitle;
    private Integer extrasMiscellaneousQuantity;
    private BigDecimal extrasAutomaticFee;
    private Integer extrasAutomaticQuantity;
    private BigDecimal extrasAdditionalDriverFee;
    private Integer extrasAdditionalDriverQuantity;
    private BigDecimal extrasSatNavFee;
    private Integer extrasSatNavQuantity;
    private BigDecimal extrasEstateFee;
    private Integer extrasEstateQuantity;
    private BigDecimal extrasBabySeatFee;
    private Integer extrasBabySeatQuantity;
    private BigDecimal extrasTowBarFee;
    private Integer extrasTowBarQuantity;
    private BigDecimal extrasNSRInsPremiumFee;
    private Integer extrasNSRInsPremiumQuantity;
    private BigDecimal extrasAdminFee;
    private Integer extrasAdminQuantity;
    private BigDecimal extrasRoofRackFee;
    private Integer extrasRoofRackQuantity;
    private BigDecimal extrasDualControlFee;
    private Integer extrasDualControlQuantity;
    private BigDecimal extrasDeliveryCollectionFee;
    private Integer extrasDeliveryCollectionQuantity;
    private String extrasCoverNoteRequired;
    private BigDecimal engReportEstimatedLabourAmount;
    private BigDecimal engReportEstimatedTotalRepairAmount;
    private Integer engReportEstimatedDaysUnderRepair;
    private String engReportUsable;
    private String engReportName;
    private String engReportCompany;
    private String engReportAddress1;
    private String engReportAddress2;
    private String engReportAddress3;
    private String engReportAddress4;
    private String engReportAddress5;
    private String engReportPostcode;
    private String engReportTelephone;
    private String engReportEmail;
    private String otherVehicleAccess;
    private String otherVehicleUsed;
    private String otherVehicleType;
    private String courtesyCarEntitlement;
    private String specificVehicleRequired;
    private String specificVehicleReason;
    private String vehicleTypeRequired;
    private String specialRequirements;
    private String averageDailyMileage;
    private BigDecimal paymentDetailsHirePaid;
    private BigDecimal paymentDetailsRepairPaid;
    private BigDecimal paymentDetailsEngineerFeePaid;
    private BigDecimal paymentDetailsTotalLossPaid;
    private BigDecimal paymentDetailsStorageRecoveryPaid;
    private BigDecimal paymentDetailsHirePenaltyPaid;
    private BigDecimal paymentDetailsRepairPenaltyPaid;
    private BigDecimal paymentDetailsClaimHandlerChargePaid;
    private BigDecimal paymentDetailsDeductionClaimHandlerFeePaid;
    private BigDecimal paymentDetailsChoDiscountFeePaid;
    private BigDecimal paymentDetailsInsurerDiscountFeePaid;
    private BigDecimal paymentDetailsFinalPayment;
    private String claimType;
    private String finalReview;

    public ClaimFileReportData(Claim claim, WebUser currentUser) {
      try {
        claimType = claim.getClaimType().toString();
        if (claim.getChorganisation() != null) {
            choName = claim.getChorganisation().getName();
        }
        createdBy = claim.getCreatedBy().getFullName();
        createdOn = DateHelper.getLocalDateTimeFormat().format(claim.getCreatedDate());
        supplierReference = claim.getChoReference();
        insurerClaimNumber = claim.getClaimNumber();
        status = claim.getStatus();
        if (claim.getLiabilityStatus() == LiabilityStatus.LIABILITY_NULL) {
            liabilityStatus = "";
        }
        else {
            liabilityStatus = claim.getLiabilityStatus().toString();
        }
        if (currentUser.isAnInsurer()) {
            finalReview = claim.isFinalReviewIns() ? "Yes" : "No";
        } else if (currentUser.isCHO()) {
            finalReview = claim.isFinalReviewCho() ? "Yes" : "No";
        } else if (currentUser.isCHOXAdmin()) {
            finalReview = (claim.isFinalReviewCho() ? "Yes (CHO), " : "No (CHO), ") 
                    + (claim.isFinalReviewIns() ? "Yes (Ins)" : "No (Ins)");
        }
        if (claim.getPolicyHolderContactDate() != null) {
            contactDate = DateHelper.getLocalDateTimeFormat().format(claim.getPolicyHolderContactDate());
        }
        if (claim.getClaimOwner() != null) {
            claimOwner = claim.getClaimOwner().getFullName();
        }
        if (claim.getSupplierClaimOwner() != null) {
            supplierClaimOwner = claim.getSupplierClaimOwner().getFullName();
        }
        if (claim.getWorkgroup() != null) {
            workgroup = claim.getWorkgroup().getName();
        }
        indemnityValue = claim.getIndemnityAmount();
        if (claim.getPercentageLiabilityAccepted() != null) {
            insurerLiabilityAgreed = claim.getPercentageLiabilityAccepted().divide(new BigDecimal("100.00"));
        }
        if (claim.getPercentageLiabilityCho() != null) {
            choLiabilityAgreed = claim.getPercentageLiabilityCho().divide(new BigDecimal("100.00"));
        }
        if (claim.getLiabilityAgreedDate() != null) {
            dateLiabilityAgreed  = DateHelper.getLocalDateFormat().format(claim.getLiabilityAgreedDate());
        }
        Customer cust = claim.getCustomer();
        if (cust != null) {
            LOG.debug("Adding customer info.");
            customer = cust.getFormattedName();
            customerTitle = cust.getTitle();
            customerFirstName = cust.getFirstName();
            customerSurname = cust.getLastName();
            customerAddress1 = cust.getAddress1();
            customerAddress2 = cust.getAddress2();
            customerAddress3 = cust.getAddress3();
            customerAddress4 = cust.getAddress4();
            customerAddress5 = cust.getAddress5();
            customerPostcode = cust.getPostcode();
            customerTelephoneDay = cust.getTelephoneDay();
            customerTelephoneEvening = cust.getTelephoneEvening();
            customerEmail = cust.getEmail();
            customerAge = cust.getAge();
            customerOccupation = cust.getOccupation();
            customerPolicyUsage = cust.getPolicyUsage();
            customerClaimNumber = cust.getClaimReference();
            customerInsurer = cust.getInsurerName();
            customerPolicyNumber = cust.getPolicyNumber();
            customerComprehensive = cust.getIsComprehensiveDesc();
            customerVehicleManufacturer = cust.getVehicleManufacturer();
            customerVehicleModel = cust.getVehicleModel();
            if (cust.getVehicleClass() != null) {
                customerVehicleClass = cust.getVehicleClass().getName();
            }
            customerVRN = cust.getVehicleRegistration();
            customerVehicleLocation = cust.getLocation();
            customerHpiVehicleManufacturer = cust.getHpiVehicleManufacturer();
            customerHpiVehicleModel = cust.getHpiVehicleModel();
            customerHpiVehicleYear = cust.getHpiVehicleYear();
            if (cust.getHpiFirstRegistration() != null) {
                customerHpiVehicleRegistrationDate = DateHelper.getLocalDateFormat().format(cust.getHpiFirstRegistration());
            }
            customerHpiVehicleCapacity = cust.getHpiVehicleCapacity();
            customerHpiVehicleDoorplan = cust.getHpiVehicleDoorplan();
            customerHpiVehicleTransmission = cust.getHpiVehicleTransmission();
            totalLoss = cust.getIsTotalLossDesc();
            totalLoss = cust.getIsTotalLossDesc();
            usable = cust.getIsUsableDesc();
            description = cust.getDamage();
            hireMonOriginalECD = cust.getInitialECDDesc();
            otherVehicleAccess = cust.getCanAccessOtherVehicleDesc();
            otherVehicleUsed = cust.getOtherVehicleUsedDesc();
            otherVehicleType = cust.getOtherVehicle();
            courtesyCarEntitlement = cust.getCourtesyCarEntitledDesc();
            specificVehicleRequired = cust.getSpecificVehicleRequiredDesc();
            specificVehicleReason = cust.getSpecificVehicleReason();
            vehicleTypeRequired = cust.getTypeVehicleRequired();
            specialRequirements = cust.getSpecialRequirements();
            if (cust.getAverageDailyMileage() != null) {
                averageDailyMileage = cust.getAverageDailyMileage().toString();
            }
            else {
                averageDailyMileage = "";
            }
            if (cust.getVehicleYear() != null) {
                customerVehicleYear = cust.getVehicleYear().toString();
            }
            else {
                customerVehicleYear = "";
            }
        }
        ThirdParty thirdParty = claim.getThirdParty();
        if (thirdParty != null) {
            LOG.debug("Adding thirdparty info.");
            if (thirdParty.getInsurer() != null) {
                thirdPartyInsurer = thirdParty.getInsurer().getName();
            }
            thirdPartyTitle = thirdParty.getTitle();
            thirdPartyFirstName = thirdParty.getFirstName();
            thirdPartySurname = thirdParty.getLastName();
            thirdPartyAddress1 = thirdParty.getAddress1();
            thirdPartyAddress2 = thirdParty.getAddress2();
            thirdPartyAddress3 = thirdParty.getAddress3();
            thirdPartyAddress4 = thirdParty.getAddress4();
            thirdPartyAddress5 = thirdParty.getAddress5();
            thirdPartyPostcode = thirdParty.getPostcode();
            thirdPartyTelephoneDay = thirdParty.getTelephoneDay();
            thirdPartyTelephoneEvening = thirdParty.getTelephoneEvening();
            thirdPartyEmail = thirdParty.getEmail();
            thirdPartyInsurer = thirdParty.getInsurer().getName();
            thirdPartyInsurerBrand = thirdParty.getInsurerBrand();
            thirdPartyPolicyNumber = thirdParty.getPolicyNumber();
            thirdPartyVehicleManufacturer = thirdParty.getVehicleManufacturer();
            thirdPartyVehicleModel = thirdParty.getVehicleModel();
            thirdPartyVRN = thirdParty.getVehicleRegistration();
            if (thirdParty.getVehicleClass() != null) {
                thirdPartyVehicleClass = thirdParty.getVehicleClass().getName();
            }
        }
        managingRepair = claim.getIsManagingRepairDesc();
        if (claim.getGtaNoticeDate() != null) {
            noticeDate = DateHelper.getLocalDateTimeFormat().format(claim.getGtaNoticeDate());
        }
        if (claim.getCreditAgreementDate() != null) {
            creditAgreementSignedDate = DateHelper.getLocalDateTimeFormat().format(claim.getCreditAgreementDate());
        }
        invoiceReviewRequired = claim.getIsInvoiceReviewRequiredDesc();
        Incident incident = claim.getIncident();
        if (incident != null) {
            LOG.debug("Adding incident info.");
            if (incident.getDate() != null) {
                incidentDate = DateHelper.getLocalDateTimeFormat().format(incident.getDate());
            }
            incidentLocation = incident.getLocation();
            incidentPoliceInvolved = incident.getIsPoliceInvolvedDesc();
            incidentDescription = incident.getIncidentDescription();
            Witness witness = incident.getWitness();
            if (witness != null) {
                LOG.debug("Adding witness info.");
                witnessName = witness.getName();
                witnessAddress1 = witness.getAddress1();
                witnessAddress2 = witness.getAddress2();
                witnessAddress3 = witness.getAddress3();
                witnessAddress4 = witness.getAddress4();
                witnessAddress5 = witness.getAddress5();
                witnessPostcode = witness.getPostcode();
                witnessTelephoneDay = witness.getTelephoneDay();
                witnessTelephoneEvening = witness.getTelephoneEvening();
                witnessEmail = witness.getEmail();
            }
            Injury injury = incident.getInjury();
            if (injury != null) {
                LOG.debug("Adding injury info.");
                injuryName = injury.getName();
                injuryAddress1 = injury.getAddress1();
                injuryAddress2 = injury.getAddress2();
                injuryAddress3 = injury.getAddress3();
                injuryAddress4 = injury.getAddress4();
                injuryAddress5 = injury.getAddress5();
                injuryPostcode = injury.getPostcode();
                injuryTelephoneDay = injury.getTelephoneDay();
                injuryTelephoneEvening = injury.getTelephoneEvening();
                injuryEmail = injury.getEmail();   
                Solicitor solicitor = injury.getSolicitor();
                if (solicitor != null) {
                    LOG.debug("Adding solicitor info.");
                    solicitorName = solicitor.getName();
                    solicitorAddress1 = solicitor.getAddress1();
                    solicitorAddress2 = solicitor.getAddress2();
                    solicitorAddress3 = solicitor.getAddress3();
                    solicitorAddress4 = solicitor.getAddress4();
                    solicitorAddress5 = solicitor.getAddress5();
                    solicitorPostcode = solicitor.getPostcode();
                    solicitorTelephoneDay = solicitor.getTelephone();
                    solicitorEmail = solicitor.getEmail();
                }
            }
        }

        HireMonitoringDetail hireMonitoringDetail = claim.getHireMonitoringDetail();
        if (hireMonitoringDetail != null) {
            LOG.debug("Adding HireMonitoringDetail info.");
            if (hireMonitoringDetail.getNextReviewDate() != null) {
                hireMonNextReviewDate = DateHelper.getLocalDateFormat().format(hireMonitoringDetail.getNextReviewDate());
            }
            hireMonRepairerName = hireMonitoringDetail.getNameOfRepairer();
            if (hireMonitoringDetail.getRepairBookInDate() != null) {
                hireMonBookedInDate = DateHelper.getLocalDateTimeFormat().format(hireMonitoringDetail.getRepairBookInDate());
            }
            if (hireMonitoringDetail.getInspectionBookedDate() != null) {
                hireMonInspectionBookedDate = DateHelper.getLocalDateTimeFormat().format(hireMonitoringDetail.getInspectionBookedDate());
            }
            if (hireMonitoringDetail.getInspectionDate() != null) {
                hireMonInspectionDate = DateHelper.getLocalDateTimeFormat().format(hireMonitoringDetail.getInspectionDate());
            }
            hireMonTotalLoss = hireMonitoringDetail.getIsTotalLossDesc();
            if (hireMonitoringDetail.getRepairCompletionDate() != null) {
                hireMonRepairCompletionDate = DateHelper.getLocalDateTimeFormat().format(hireMonitoringDetail.getRepairCompletionDate());
            }
            hireMonIME = hireMonitoringDetail.getNameOfIme();
            hireMonLabourRate = hireMonitoringDetail.getLabourRate();
            hireMonLabourHours = hireMonitoringDetail.getLabourHour();
            hireMonTotalLabourCost = hireMonitoringDetail.getLabourCost();
            hireMonNonProvisionReason = hireMonitoringDetail.getNonProvisionReason();
            isRepairOnlyCheck = hireMonitoringDetail.isIsRepairOnlyCheck() ? "Yes" : "No";
            isNFInsurerManagingRepair = hireMonitoringDetail.isIsNFInsurerManagingRepair() ? "Yes" : "No";
            clientVatRegistered = hireMonitoringDetail.getClientVatRegisteredDesc();
            if (hireMonitoringDetail.getRepairAuthorisedDate() == null) {
                hireMonAuthorisedDate = "";
            } else {
                hireMonAuthorisedDate = DateHelper.getLocalDateFormat().format(hireMonitoringDetail.getRepairAuthorisedDate());
            }
            if (hireMonitoringDetail.getRepairCommencedDate() == null) {
                hireMonCommencedDate = "";
            } else {
                hireMonCommencedDate = DateHelper.getLocalDateFormat().format(hireMonitoringDetail.getRepairCommencedDate());
            }
            if (hireMonitoringDetail.getTotalLossOfferMadeDate() == null) {
                hireMonTotalLossOfferMadeDate = "";
            } else {
                hireMonTotalLossOfferMadeDate = DateHelper.getLocalDateFormat().format(hireMonitoringDetail.getTotalLossOfferMadeDate());
            }
            if (hireMonitoringDetail.getTotalLossOfferAcceptedDate() == null) {
                hireMonTotalLossAcceptedDate = "";
            } else {
                hireMonTotalLossAcceptedDate = DateHelper.getLocalDateFormat().format(hireMonitoringDetail.getTotalLossOfferAcceptedDate());
            }
            if (hireMonitoringDetail.getTotalLossOfferCheckIssuedDate() == null) {
                hireMonTotalLossChequeIssuedDate = "";
            } else {
                hireMonTotalLossChequeIssuedDate = DateHelper.getLocalDateFormat().format(hireMonitoringDetail.getTotalLossOfferCheckIssuedDate());
            }
            if (hireMonitoringDetail.getTotalLossOfferCheckReceivedDate() == null) {
                hireMonTotalLossChequeReceivedDate = "";
            } else {
                hireMonTotalLossChequeReceivedDate = DateHelper.getLocalDateFormat().format(hireMonitoringDetail.getTotalLossOfferCheckReceivedDate());
            }
        }

        VehicleHire vehicleHire = claim.getVehicleHire();
        if (vehicleHire != null) {
            LOG.debug("Adding vehicleHire info.");
            hireVehicleManufacturer = vehicleHire.getVehicleManufacturer();
            hireVehicleModel = vehicleHire.getVehicleModel();
            hireVehicleRegistration = vehicleHire.getVehicleRegistration();
            if (vehicleHire.getVehicleClass() != null) {
                hireVehicleClass = vehicleHire.getVehicleClass().getName();
            }
            if (vehicleHire.getHireStart() != null) {
                hireVehicleHireStart = DateHelper.getLocalDateTimeFormat().format(vehicleHire.getHireStart());
            }
            if (vehicleHire.getHireEnd() != null) {
                hireVehicleHireEnd = DateHelper.getLocalDateTimeFormat().format(vehicleHire.getHireEnd());
            }
            hireVehicleReasonForCollection = vehicleHire.getCollectionReason();
            hireVehicleNoHireDays = vehicleHire.getDays();
            hireVehicleHpiVehicleManufacturer = vehicleHire.getHpiVehicleManufacturer();
            hireVehicleHpiVehicleModel = vehicleHire.getHpiVehicleModel();
            hireVehicleHpiVehicleYear = vehicleHire.getHpiVehicleYear();
            if (vehicleHire.getHpiFirstRegistration() != null) {
                hireVehicleHpiVehicleRegistrationDate = DateHelper.getLocalDateFormat().format(vehicleHire.getHpiFirstRegistration());
            }
            hireVehicleHpiVehicleCapacity = vehicleHire.getHpiVehicleCapacity();
            hireVehicleHpiVehicleDoorplan = vehicleHire.getHpiVehicleDoorplan();
            hireVehicleHpiVehicleTransmission = vehicleHire.getHpiVehicleTransmission();
        }
        
        if (ClaimType.isSubscriber(claim.getClaimType())) {
            extrasMiscellaneousTitle = "Acquisition Fee";
        }
        else {
            extrasMiscellaneousTitle = "Miscellaneous Fee";
        }

        
        Invoice invoice = claim.getInvoice();
        if (invoice != null) {
            LOG.debug("Adding invoice info.");
            invoiceSupplierClaimsHandlingNo = invoice.getHandlingInvoiceNo();
            invoiceSupplierClaimInvoiceNo = invoice.getClaimInvoiceNo();
            invoiceHireRate = invoice.getHireRateChargedPerDay();
            invoiceHireNet = invoice.getHireNet();
            invoiceHireVat = invoice.getHireVat();
            invoiceHireGross = invoice.getHireGross();
            invoiceRepairNet = invoice.getRepairNet();
            invoiceRepairVat = invoice.getRepairVat();
            invoiceRepairGross = invoice.getRepairGross();
            invoiceEngineerFeeNet = invoice.getEngineerFeeNet();
            invoiceEngineerFeeVat = invoice.getEngineerFeeVat();
            invoiceEngineerFeeGross = invoice.getEngineerFeeGross();
            invoiceTotalLossFeeNet = invoice.getTotalLossFeeNet();
            invoiceTotalLossFeeVat = invoice.getTotalLossFeeVat();
            invoiceTotalLossFeeGross = invoice.getTotalLossFeeGross();
            invoiceStorageRecoveryNet = invoice.getStorageRecoveryNet();
            invoiceStorageRecoveryVat = invoice.getStorageRecoveryVat();
            invoiceStorageRecoveryGross = invoice.getStorageRecoveryGross();
            invoiceTotalNet = invoice.getTotalNet();
            invoiceTotalVat = invoice.getTotalVat();
            invoiceTotalGross = invoice.getTotalGross();
            invoiceClaimsHandlingAmount = invoice.getClaimsHandlingInvoiceAmount();
            invoiceDeductionHandlingFee = invoice.getDeductionForClaimsHandlingFee();
            invoiceDiscount = invoice.getDiscount();
            invoiceInsurerDiscount = invoice.getInsurerDiscount();
            invoiceHirePenaltyChargeAmount = invoice.getHirePenaltyCharge();
            invoiceHirePenaltyChargePercentage = invoice.getHirePenaltyPercentage();
            if (invoice.isAppliedHirePenaltyPercentageDifferent() && !currentUser.isCHO()) {
                invoiceHirePenaltyChargePercentageApplied = invoice.getHirePenaltyPercentageApplied();
            }
            invoiceRepairPenaltyChargeAmount = invoice.getRepairPenaltyCharge();
            invoiceRepairPenaltyChargePercentage = invoice.getRepairPenaltyPercentage();
            if (invoice.isAppliedRepairPenaltyPercentageDifferent() && !currentUser.isCHO()) {
                invoiceRepairPenaltyChargePercentageApplied = invoice.getRepairPenaltyPercentageApplied();
            }
            invoiceTotalPenaltyCharge = invoice.getTotalPenaltyCharge();
            invoiceFullTotalToPay = invoice.getFullTotalToPay();
            invoiceTotalToPay = invoice.getTotalToPay();
            if (invoice.getInvoiceOriginal().getFullTotalToPayOriginal() != null) {
                originalInvoiceFullTotalToPay = invoice.getInvoiceOriginal().getFullTotalToPayOriginal();
            } else {
                originalInvoiceFullTotalToPay = BigDecimal.ZERO;
            }
            if (invoice.getInvoiceOriginal().getTotalToPayOriginal() != null) {
                originalInvoiceTotalToPay = invoice.getInvoiceOriginal().getTotalToPayOriginal();
            } else {
                originalInvoiceTotalToPay = BigDecimal.ZERO;
            }
            invoiceExcessAmountCollected = invoice.getExcessAmountCollected();
            invoiceVATAmountCollected = invoice.getVatAmountCollected();
            invoiceInterimPaymentAmount = invoice.getInterimPaymentMade();
            if (invoiceInterimPaymentAmount == null || invoiceInterimPaymentAmount.compareTo(BigDecimal.ZERO)==0) {
                invoiceInterimPayment = "";
            }
            else if (invoice.getInterimPaymentReceived() !=null && invoice.getInterimPaymentReceived().compareTo(invoiceInterimPaymentAmount) >= 0) {
                invoiceInterimPayment = "£" + invoiceInterimPaymentAmount.toString() + " (Received)";
            }
            else if (invoice.getInterimPaymentReceived() !=null && invoice.getInterimPaymentReceived().compareTo(invoiceInterimPaymentAmount) < 0) {
                invoiceInterimPayment = "£" + invoiceInterimPaymentAmount.toString() + " (Only £" + invoice.getInterimPaymentReceived() + " Received)";
            }
            else {
                invoiceInterimPayment = "£" + invoiceInterimPaymentAmount.toString() + " (Not Yet Received)";
            }
            if (invoice.getDateInvoiced() != null) {
                invoiceDate = DateHelper.getLocalDateTimeFormat().format(invoice.getDateInvoiced());
            }
            if (invoice.getCreatedDate() != null) {
                invoiceUploadedDate = DateHelper.getLocalDateTimeFormat().format(invoice.getCreatedDate());
            }
            if (invoice.getAutoPenaltyStart() != null) {
                penaltyStartDate = DateHelper.getLocalDateTimeFormat().format(invoice.getAutoPenaltyStart());
            }
            extrasMiscellaneousFee = invoice.getMiscellaneousFee();
            extrasMiscellaneousQuantity = invoice.getMiscellaneousQty();
            extrasAutomaticFee = invoice.getAutomaticFee();
            extrasAutomaticQuantity = invoice.getAutomaticQty();
            extrasAdditionalDriverFee = invoice.getAdditionalDriverFee();
            extrasAdditionalDriverQuantity = invoice.getAdditionalDriverQty();
            extrasSatNavFee = invoice.getSatNavFee();
            extrasSatNavQuantity = invoice.getSatNavQty();
            extrasEstateFee = invoice.getEstateFee();
            extrasEstateQuantity = invoice.getEstateQty();
            extrasBabySeatFee = invoice.getBabySeatFee();
            extrasBabySeatQuantity = invoice.getBabySeatQty();
            extrasTowBarFee = invoice.getTowBarsFee();
            extrasTowBarQuantity = invoice.getTowBarsQty();
            extrasNSRInsPremiumFee = invoice.getNonStandardInsurancePremiumFee();
            extrasNSRInsPremiumQuantity = invoice.getNonStandardInsurancePremiumQty();
            extrasAdminFee = invoice.getAdminFee();
            extrasAdminQuantity = invoice.getAdminQty();
            extrasRoofRackFee = invoice.getRoofRackFee();
            extrasRoofRackQuantity = invoice.getRoofRackQty();
            extrasDualControlFee = invoice.getDualControlFee();
            extrasDualControlQuantity = invoice.getDualControlQty();
            extrasDeliveryCollectionFee = invoice.getDeliveryCollectionFee();
            extrasDeliveryCollectionQuantity = invoice.getDeliveryCollectionQty();
            extrasCoverNoteRequired = invoice.getCoverNoteRequiredDesc();

            paymentDetailsHirePaid = invoice.getHireGrossPaid();
            paymentDetailsRepairPaid = invoice.getRepairGrossPaid();
            paymentDetailsEngineerFeePaid = invoice.getEngineerFeeGrossPaid();
            paymentDetailsTotalLossPaid = invoice.getTotalLossFeeGrossPaid();
            paymentDetailsStorageRecoveryPaid = invoice.getStorageRecoveryGrossPaid();
            paymentDetailsHirePenaltyPaid = invoice.getHirePenaltyChargePaid();
            paymentDetailsRepairPenaltyPaid = invoice.getRepairPenaltyChargePaid();
            paymentDetailsClaimHandlerChargePaid = invoice.getClaimHandlerChargePaid();
            paymentDetailsDeductionClaimHandlerFeePaid = invoice.getDeductionClaimHandlerFeePaid();
            paymentDetailsChoDiscountFeePaid = invoice.getChoDiscountFeePaid();
            paymentDetailsInsurerDiscountFeePaid = invoice.getInsurerDiscountFeePaid();
            paymentDetailsFinalPayment = invoice.getFinalPayment();
        }

        EngineerReport engineerReport = claim.getEngineerReport();
        if (engineerReport != null) {
            LOG.debug("Adding engineerReport info.");
            engReportEstimatedLabourAmount = engineerReport.getEstimatedLabourAmount();
            engReportEstimatedTotalRepairAmount = engineerReport.getEstimatedTotalRepairAmount();
            engReportEstimatedDaysUnderRepair = engineerReport.getEstimatedDaysUnderRepair();
            engReportUsable = engineerReport.getIsUsableDesc();
            engReportName = engineerReport.getName();
            engReportCompany = engineerReport.getCompany();
            engReportAddress1 = engineerReport.getAddress1();
            engReportAddress2 = engineerReport.getAddress2();
            engReportAddress3 = engineerReport.getAddress3();
            engReportAddress4 = engineerReport.getAddress4();
            engReportAddress5 = engineerReport.getAddress5();
            engReportPostcode = engineerReport.getPostcode();
            engReportTelephone = engineerReport.getTelephone();
            engReportEmail = engineerReport.getEmail();
        }
      }
      catch (Exception ex) {
          LOG.error("Error creating claim file report for claim '{}':\n", claim.getChoReference(), ex);
          if (ex.getCause() != null) {
              LOG.error("    Caused by: {}", ex.getCause().getMessage(), ex.getCause());
          }
      }
    }

    public String getClaimType() {
        return claimType;
    }

    public void setClaimType(String claimType) {
        this.claimType = claimType;
    }

    public String getLiabilityStatus() {
        return liabilityStatus;
    }

    public void setLiabilityStatus(String liabilityStatus) {
        this.liabilityStatus = liabilityStatus;
    }

    public String getSolicitorEmail() {
        return solicitorEmail;
    }

    public void setSolicitorEmail(String solicitorEmail) {
        this.solicitorEmail = solicitorEmail;
    }

    public String getWitnessEmail() {
        return witnessEmail;
    }

    public void setWitnessEmail(String witnessEmail) {
        this.witnessEmail = witnessEmail;
    }

    public BigDecimal getChoLiabilityAgreed() {
        return choLiabilityAgreed;
    }

    public void setChoLiabilityAgreed(BigDecimal choLiabilityAgreed) {
        this.choLiabilityAgreed = choLiabilityAgreed;
    }

    public String getChoName() {
        return choName;
    }

    public void setChoName(String choName) {
        this.choName = choName;
    }

    public String getClaimOwner() {
        return claimOwner;
    }

    public void setClaimOwner(String claimOwner) {
        this.claimOwner = claimOwner;
    }

    public String getContactDate() {
        return contactDate;
    }

    public void setContactDate(String contactDate) {
        this.contactDate = contactDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreditAgreementSignedDate() {
        return creditAgreementSignedDate;
    }

    public void setCreditAgreementSignedDate(String creditAgreementSignedDate) {
        this.creditAgreementSignedDate = creditAgreementSignedDate;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getCustomerAddress1() {
        return customerAddress1;
    }

    public void setCustomerAddress1(String customerAddress1) {
        this.customerAddress1 = customerAddress1;
    }

    public String getCustomerAddress2() {
        return customerAddress2;
    }

    public void setCustomerAddress2(String customerAddress2) {
        this.customerAddress2 = customerAddress2;
    }

    public String getCustomerAddress3() {
        return customerAddress3;
    }

    public void setCustomerAddress3(String customerAddress3) {
        this.customerAddress3 = customerAddress3;
    }

    public String getCustomerAddress4() {
        return customerAddress4;
    }

    public void setCustomerAddress4(String customerAddress4) {
        this.customerAddress4 = customerAddress4;
    }

    public String getCustomerAddress5() {
        return customerAddress5;
    }

    public void setCustomerAddress5(String customerAddress5) {
        this.customerAddress5 = customerAddress5;
    }

    public Integer getCustomerAge() {
        return customerAge;
    }

    public void setCustomerAge(Integer customerAge) {
        this.customerAge = customerAge;
    }

    public String getCustomerClaimNumber() {
        return customerClaimNumber;
    }

    public void setCustomerClaimNumber(String customerClaimNumber) {
        this.customerClaimNumber = customerClaimNumber;
    }

    public String getCustomerComprehensive() {
        return customerComprehensive;
    }

    public void setCustomerComprehensive(String customerComprehensive) {
        this.customerComprehensive = customerComprehensive;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public void setCustomerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
    }

    public String getCustomerInsurer() {
        return customerInsurer;
    }

    public void setCustomerInsurer(String customerInsurer) {
        this.customerInsurer = customerInsurer;
    }

    public String getCustomerOccupation() {
        return customerOccupation;
    }

    public void setCustomerOccupation(String customerOccupation) {
        this.customerOccupation = customerOccupation;
    }

    public String getCustomerPolicyNumber() {
        return customerPolicyNumber;
    }

    public void setCustomerPolicyNumber(String customerPolicyNumber) {
        this.customerPolicyNumber = customerPolicyNumber;
    }

    public String getCustomerPolicyUsage() {
        return customerPolicyUsage;
    }

    public void setCustomerPolicyUsage(String customerPolicyUsage) {
        this.customerPolicyUsage = customerPolicyUsage;
    }

    public String getCustomerPostcode() {
        return customerPostcode;
    }

    public void setCustomerPostcode(String customerPostcode) {
        this.customerPostcode = customerPostcode;
    }

    public String getCustomerSurname() {
        return customerSurname;
    }

    public void setCustomerSurname(String customerSurname) {
        this.customerSurname = customerSurname;
    }

    public String getCustomerTelephoneDay() {
        return customerTelephoneDay;
    }

    public void setCustomerTelephoneDay(String customerTelephoneDay) {
        this.customerTelephoneDay = customerTelephoneDay;
    }

    public String getCustomerTelephoneEvening() {
        return customerTelephoneEvening;
    }

    public void setCustomerTelephoneEvening(String customerTelephoneEvening) {
        this.customerTelephoneEvening = customerTelephoneEvening;
    }

    public String getCustomerTitle() {
        return customerTitle;
    }

    public void setCustomerTitle(String customerTitle) {
        this.customerTitle = customerTitle;
    }

    public String getCustomerVRN() {
        return customerVRN;
    }

    public void setCustomerVRN(String customerVRN) {
        this.customerVRN = customerVRN;
    }

    public String getCustomerVehicleClass() {
        return customerVehicleClass;
    }

    public void setCustomerVehicleClass(String customerVehicleClass) {
        this.customerVehicleClass = customerVehicleClass;
    }

    public String getCustomerVehicleLocation() {
        return customerVehicleLocation;
    }

    public void setCustomerVehicleLocation(String customerVehicleLocation) {
        this.customerVehicleLocation = customerVehicleLocation;
    }

    public String getCustomerVehicleManufacturer() {
        return customerVehicleManufacturer;
    }

    public void setCustomerVehicleManufacturer(String customerVehicleManufacturer) {
        this.customerVehicleManufacturer = customerVehicleManufacturer;
    }

    public String getCustomerVehicleModel() {
        return customerVehicleModel;
    }

    public void setCustomerVehicleModel(String customerVehicleModel) {
        this.customerVehicleModel = customerVehicleModel;
    }

    public String getDateLiabilityAgreed() {
        return dateLiabilityAgreed;
    }

    public void setDateLiabilityAgreed(String dateLiabilityAgreed) {
        this.dateLiabilityAgreed = dateLiabilityAgreed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEngReportAddress1() {
        return engReportAddress1;
    }

    public void setEngReportAddress1(String engReportAddress1) {
        this.engReportAddress1 = engReportAddress1;
    }

    public String getEngReportAddress2() {
        return engReportAddress2;
    }

    public void setEngReportAddress2(String engReportAddress2) {
        this.engReportAddress2 = engReportAddress2;
    }

    public String getEngReportAddress3() {
        return engReportAddress3;
    }

    public void setEngReportAddress3(String engReportAddress3) {
        this.engReportAddress3 = engReportAddress3;
    }

    public String getEngReportAddress4() {
        return engReportAddress4;
    }

    public void setEngReportAddress4(String engReportAddress4) {
        this.engReportAddress4 = engReportAddress4;
    }

    public String getEngReportAddress5() {
        return engReportAddress5;
    }

    public void setEngReportAddress5(String engReportAddress5) {
        this.engReportAddress5 = engReportAddress5;
    }

    public String getEngReportCompany() {
        return engReportCompany;
    }

    public void setEngReportCompany(String engReportCompany) {
        this.engReportCompany = engReportCompany;
    }

    public String getEngReportEmail() {
        return engReportEmail;
    }

    public void setEngReportEmail(String engReportEmail) {
        this.engReportEmail = engReportEmail;
    }

    public Integer getEngReportEstimatedDaysUnderRepair() {
        return engReportEstimatedDaysUnderRepair;
    }

    public void setEngReportEstimatedDaysUnderRepair(Integer engReportEstimatedDaysUnderRepair) {
        this.engReportEstimatedDaysUnderRepair = engReportEstimatedDaysUnderRepair;
    }

    public BigDecimal getEngReportEstimatedLabourAmount() {
        return engReportEstimatedLabourAmount;
    }

    public void setEngReportEstimatedLabourAmount(BigDecimal engReportEstimatedLabourAmount) {
        this.engReportEstimatedLabourAmount = engReportEstimatedLabourAmount;
    }

    public BigDecimal getEngReportEstimatedTotalRepairAmount() {
        return engReportEstimatedTotalRepairAmount;
    }

    public void setEngReportEstimatedTotalRepairAmount(BigDecimal engReportEstimatedTotalRepairAmount) {
        this.engReportEstimatedTotalRepairAmount = engReportEstimatedTotalRepairAmount;
    }

    public String getEngReportName() {
        return engReportName;
    }

    public void setEngReportName(String engReportName) {
        this.engReportName = engReportName;
    }

    public String getEngReportPostcode() {
        return engReportPostcode;
    }

    public void setEngReportPostcode(String engReportPostcode) {
        this.engReportPostcode = engReportPostcode;
    }

    public String getEngReportTelephone() {
        return engReportTelephone;
    }

    public void setEngReportTelephone(String engReportTelephone) {
        this.engReportTelephone = engReportTelephone;
    }

    public String getEngReportUsable() {
        return engReportUsable;
    }

    public void setEngReportUsable(String engReportUsable) {
        this.engReportUsable = engReportUsable;
    }

    public BigDecimal getExtrasAdminFee() {
        return extrasAdminFee;
    }

    public void setExtrasAdminFee(BigDecimal extrasAdminFee) {
        this.extrasAdminFee = extrasAdminFee;
    }

    public Integer getExtrasAdminQuantity() {
        return extrasAdminQuantity;
    }

    public void setExtrasAdminQuantity(Integer extrasAdminQuantity) {
        this.extrasAdminQuantity = extrasAdminQuantity;
    }

    public BigDecimal getExtrasAutomaticFee() {
        return extrasAutomaticFee;
    }

    public void setExtrasAutomaticFee(BigDecimal extrasAutomaticFee) {
        this.extrasAutomaticFee = extrasAutomaticFee;
    }

    public Integer getExtrasAutomaticQuantity() {
        return extrasAutomaticQuantity;
    }

    public void setExtrasAutomaticQuantity(Integer extrasAutomaticQuantity) {
        this.extrasAutomaticQuantity = extrasAutomaticQuantity;
    }

    public BigDecimal getExtrasBabySeatFee() {
        return extrasBabySeatFee;
    }

    public void setExtrasBabySeatFee(BigDecimal extrasBabySeatFee) {
        this.extrasBabySeatFee = extrasBabySeatFee;
    }

    public Integer getExtrasBabySeatQuantity() {
        return extrasBabySeatQuantity;
    }

    public void setExtrasBabySeatQuantity(Integer extrasBabySeatQuantity) {
        this.extrasBabySeatQuantity = extrasBabySeatQuantity;
    }

    public BigDecimal getExtrasMiscellaneousFee() {
        return extrasMiscellaneousFee;
    }

    public void setExtrasMiscellaneousFee(BigDecimal extrasMiscellaneousFee) {
        this.extrasMiscellaneousFee = extrasMiscellaneousFee;
    }

    public String getExtrasMiscellaneousTitle() {
        return extrasMiscellaneousTitle;
    }

    public void setExtrasMiscellaneousTitle(String extrasMiscellaneousTitle) {
        this.extrasMiscellaneousTitle = extrasMiscellaneousTitle;
    }

    public Integer getExtrasMiscellaneousQuantity() {
        return extrasMiscellaneousQuantity;
    }

    public void setExtrasMiscellaneousQuantity(Integer extrasMiscellaneousQuantity) {
        this.extrasMiscellaneousQuantity = extrasMiscellaneousQuantity;
    }

    public BigDecimal getExtrasDeliveryCollectionFee() {
        return extrasDeliveryCollectionFee;
    }

    public void setExtrasDeliveryCollectionFee(BigDecimal extrasDeliveryCollectionFee) {
        this.extrasDeliveryCollectionFee = extrasDeliveryCollectionFee;
    }

    public Integer getExtrasDeliveryCollectionQuantity() {
        return extrasDeliveryCollectionQuantity;
    }

    public void setExtrasDeliveryCollectionQuantity(Integer extrasDeliveryCollectionQuantity) {
        this.extrasDeliveryCollectionQuantity = extrasDeliveryCollectionQuantity;
    }

    public BigDecimal getExtrasDualControlFee() {
        return extrasDualControlFee;
    }

    public void setExtrasDualControlFee(BigDecimal extrasDualControlFee) {
        this.extrasDualControlFee = extrasDualControlFee;
    }

    public Integer getExtrasDualControlQuantity() {
        return extrasDualControlQuantity;
    }

    public void setExtrasDualControlQuantity(Integer extrasDualControlQuantity) {
        this.extrasDualControlQuantity = extrasDualControlQuantity;
    }

    public BigDecimal getExtrasEstateFee() {
        return extrasEstateFee;
    }

    public void setExtrasEstateFee(BigDecimal extrasEstateFee) {
        this.extrasEstateFee = extrasEstateFee;
    }

    public Integer getExtrasEstateQuantity() {
        return extrasEstateQuantity;
    }

    public void setExtrasEstateQuantity(Integer extrasEstateQuantity) {
        this.extrasEstateQuantity = extrasEstateQuantity;
    }

    public BigDecimal getExtrasNSRInsPremiumFee() {
        return extrasNSRInsPremiumFee;
    }

    public void setExtrasNSRInsPremiumFee(BigDecimal extrasNSRInsPremiumFee) {
        this.extrasNSRInsPremiumFee = extrasNSRInsPremiumFee;
    }

    public Integer getExtrasNSRInsPremiumQuantity() {
        return extrasNSRInsPremiumQuantity;
    }

    public void setExtrasNSRInsPremiumQuantity(Integer extrasNSRInsPremiumQuantity) {
        this.extrasNSRInsPremiumQuantity = extrasNSRInsPremiumQuantity;
    }

    public BigDecimal getExtrasRoofRackFee() {
        return extrasRoofRackFee;
    }

    public void setExtrasRoofRackFee(BigDecimal extrasRoofRackFee) {
        this.extrasRoofRackFee = extrasRoofRackFee;
    }

    public Integer getExtrasRoofRackQuantity() {
        return extrasRoofRackQuantity;
    }

    public void setExtrasRoofRackQuantity(Integer extrasRoofRackQuantity) {
        this.extrasRoofRackQuantity = extrasRoofRackQuantity;
    }

    public BigDecimal getExtrasSatNavFee() {
        return extrasSatNavFee;
    }

    public void setExtrasSatNavFee(BigDecimal extrasSatNavFee) {
        this.extrasSatNavFee = extrasSatNavFee;
    }

    public Integer getExtrasSatNavQuantity() {
        return extrasSatNavQuantity;
    }

    public void setExtrasSatNavQuantity(Integer extrasSatNavQuantity) {
        this.extrasSatNavQuantity = extrasSatNavQuantity;
    }

    public BigDecimal getExtrasTowBarFee() {
        return extrasTowBarFee;
    }

    public void setExtrasTowBarFee(BigDecimal extrasTowBarFee) {
        this.extrasTowBarFee = extrasTowBarFee;
    }

    public Integer getExtrasTowBarQuantity() {
        return extrasTowBarQuantity;
    }

    public void setExtrasTowBarQuantity(Integer extrasTowBarQuantity) {
        this.extrasTowBarQuantity = extrasTowBarQuantity;
    }

    public String getHireMonBookedInDate() {
        return hireMonBookedInDate;
    }

    public void setHireMonBookedInDate(String hireMonBookedInDate) {
        this.hireMonBookedInDate = hireMonBookedInDate;
    }

    public String getHireMonIME() {
        return hireMonIME;
    }

    public void setHireMonIME(String hireMonIME) {
        this.hireMonIME = hireMonIME;
    }

    public String getHireMonInspectionBookedDate() {
        return hireMonInspectionBookedDate;
    }

    public void setHireMonInspectionBookedDate(String hireMonInspectionBookedDate) {
        this.hireMonInspectionBookedDate = hireMonInspectionBookedDate;
    }

    public String getHireMonInspectionDate() {
        return hireMonInspectionDate;
    }

    public void setHireMonInspectionDate(String hireMonInspectionDate) {
        this.hireMonInspectionDate = hireMonInspectionDate;
    }

    public BigDecimal getHireMonLabourHours() {
        return hireMonLabourHours;
    }

    public void setHireMonLabourHours(BigDecimal hireMonLabourHours) {
        this.hireMonLabourHours = hireMonLabourHours;
    }

    public BigDecimal getHireMonLabourRate() {
        return hireMonLabourRate;
    }

    public void setHireMonLabourRate(BigDecimal hireMonLabourRate) {
        this.hireMonLabourRate = hireMonLabourRate;
    }

    public String getHireMonNextReviewDate() {
        return hireMonNextReviewDate;
    }

    public void setHireMonNextReviewDate(String hireMonNextReviewDate) {
        this.hireMonNextReviewDate = hireMonNextReviewDate;
    }

    public String getHireMonNonProvisionReason() {
        return hireMonNonProvisionReason;
    }

    public void setHireMonNonProvisionReason(String hireMonNonProvisionReason) {
        this.hireMonNonProvisionReason = hireMonNonProvisionReason;
    }

    public String getHireMonOriginalECD() {
        return hireMonOriginalECD;
    }

    public void setHireMonOriginalECD(String hireMonOriginalECD) {
        this.hireMonOriginalECD = hireMonOriginalECD;
    }

    public String getHireMonRepairCompletionDate() {
        return hireMonRepairCompletionDate;
    }

    public void setHireMonRepairCompletionDate(String hireMonRepairCompletionDate) {
        this.hireMonRepairCompletionDate = hireMonRepairCompletionDate;
    }

    public String getHireMonRepairerName() {
        return hireMonRepairerName;
    }

    public void setHireMonRepairerName(String hireMonRepairerName) {
        this.hireMonRepairerName = hireMonRepairerName;
    }

    public BigDecimal getHireMonTotalLabourCost() {
        return hireMonTotalLabourCost;
    }

    public void setHireMonTotalLabourCost(BigDecimal hireMonTotalLabourCost) {
        this.hireMonTotalLabourCost = hireMonTotalLabourCost;
    }

    public String getHireMonTotalLoss() {
        return hireMonTotalLoss;
    }

    public void setHireMonTotalLoss(String hireMonTotalLoss) {
        this.hireMonTotalLoss = hireMonTotalLoss;
    }

    public String getHireVehicleClass() {
        return hireVehicleClass;
    }

    public void setHireVehicleClass(String hireVehicleClass) {
        this.hireVehicleClass = hireVehicleClass;
    }

    public String getHireVehicleHireEnd() {
        return hireVehicleHireEnd;
    }

    public void setHireVehicleHireEnd(String hireVehicleHireEnd) {
        this.hireVehicleHireEnd = hireVehicleHireEnd;
    }

    public String getHireVehicleHireStart() {
        return hireVehicleHireStart;
    }

    public void setHireVehicleHireStart(String hireVehicleHireStart) {
        this.hireVehicleHireStart = hireVehicleHireStart;
    }

    public String getHireVehicleManufacturer() {
        return hireVehicleManufacturer;
    }

    public void setHireVehicleManufacturer(String hireVehicleManufacturer) {
        this.hireVehicleManufacturer = hireVehicleManufacturer;
    }

    public String getHireVehicleModel() {
        return hireVehicleModel;
    }

    public void setHireVehicleModel(String hireVehicleModel) {
        this.hireVehicleModel = hireVehicleModel;
    }

    public Integer getHireVehicleNoHireDays() {
        return hireVehicleNoHireDays;
    }

    public void setHireVehicleNoHireDays(Integer hireVehicleNoHireDays) {
        this.hireVehicleNoHireDays = hireVehicleNoHireDays;
    }

    public String getHireVehicleReasonForCollection() {
        return hireVehicleReasonForCollection;
    }

    public void setHireVehicleReasonForCollection(String hireVehicleReasonForCollection) {
        this.hireVehicleReasonForCollection = hireVehicleReasonForCollection;
    }

    public String getHireVehicleRegistration() {
        return hireVehicleRegistration;
    }

    public void setHireVehicleRegistration(String hireVehicleRegistration) {
        this.hireVehicleRegistration = hireVehicleRegistration;
    }

    public BigDecimal getIndemnityValue() {
        return indemnityValue;
    }

    public void setIndemnityValue(BigDecimal indemnityValue) {
        this.indemnityValue = indemnityValue;
    }

    public String getIncidentDate() {
        return incidentDate;
    }

    public void setIncidentDate(String incidentDate) {
        this.incidentDate = incidentDate;
    }

    public String getIncidentDescription() {
        return incidentDescription;
    }

    public void setIncidentDescription(String incidentDescription) {
        this.incidentDescription = incidentDescription;
    }

    public String getIncidentLocation() {
        return incidentLocation;
    }

    public void setIncidentLocation(String incidentLocation) {
        this.incidentLocation = incidentLocation;
    }

    public String getIncidentPoliceInvolved() {
        return incidentPoliceInvolved;
    }

    public void setIncidentPoliceInvolved(String incidentPoliceInvolved) {
        this.incidentPoliceInvolved = incidentPoliceInvolved;
    }

    public String getInjuryAddress1() {
        return injuryAddress1;
    }

    public void setInjuryAddress1(String injuryAddress1) {
        this.injuryAddress1 = injuryAddress1;
    }

    public String getInjuryAddress2() {
        return injuryAddress2;
    }

    public void setInjuryAddress2(String injuryAddress2) {
        this.injuryAddress2 = injuryAddress2;
    }

    public String getInjuryAddress3() {
        return injuryAddress3;
    }

    public void setInjuryAddress3(String injuryAddress3) {
        this.injuryAddress3 = injuryAddress3;
    }

    public String getInjuryAddress4() {
        return injuryAddress4;
    }

    public void setInjuryAddress4(String injuryAddress4) {
        this.injuryAddress4 = injuryAddress4;
    }

    public String getInjuryAddress5() {
        return injuryAddress5;
    }

    public void setInjuryAddress5(String injuryAddress5) {
        this.injuryAddress5 = injuryAddress5;
    }

    public String getInjuryEmail() {
        return injuryEmail;
    }

    public void setInjuryEmail(String injuryEmail) {
        this.injuryEmail = injuryEmail;
    }

    public String getInjuryName() {
        return injuryName;
    }

    public void setInjuryName(String injuryName) {
        this.injuryName = injuryName;
    }

    public String getInjuryPostcode() {
        return injuryPostcode;
    }

    public void setInjuryPostcode(String injuryPostcode) {
        this.injuryPostcode = injuryPostcode;
    }

    public String getInjuryTelephoneDay() {
        return injuryTelephoneDay;
    }

    public void setInjuryTelephoneDay(String injuryTelephoneDay) {
        this.injuryTelephoneDay = injuryTelephoneDay;
    }

    public String getInjuryTelephoneEvening() {
        return injuryTelephoneEvening;
    }

    public void setInjuryTelephoneEvening(String injuryTelephoneEvening) {
        this.injuryTelephoneEvening = injuryTelephoneEvening;
    }

    public String getInsurerClaimNumber() {
        return insurerClaimNumber;
    }

    public void setInsurerClaimNumber(String insurerClaimNumber) {
        this.insurerClaimNumber = insurerClaimNumber;
    }

    public BigDecimal getInsurerLiabilityAgreed() {
        return insurerLiabilityAgreed;
    }

    public void setInsurerLiabilityAgreed(BigDecimal insurerLiabilityAgreed) {
        this.insurerLiabilityAgreed = insurerLiabilityAgreed;
    }

    public BigDecimal getInvoiceClaimsHandlingAmount() {
        return invoiceClaimsHandlingAmount;
    }

    public void setInvoiceClaimsHandlingAmount(BigDecimal invoiceClaimsHandlingAmount) {
        this.invoiceClaimsHandlingAmount = invoiceClaimsHandlingAmount;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public BigDecimal getInvoiceDeductionHandlingFee() {
        return invoiceDeductionHandlingFee;
    }

    public void setInvoiceDeductionHandlingFee(BigDecimal invoiceDeductionHandlingFee) {
        this.invoiceDeductionHandlingFee = invoiceDeductionHandlingFee;
    }

    public BigDecimal getInvoiceInsurerDiscount() {
        return invoiceInsurerDiscount;
    }

    public void setInvoiceInsurerDiscount(BigDecimal invoiceInsurerDiscount) {
        this.invoiceInsurerDiscount = invoiceInsurerDiscount;
    }

    public BigDecimal getInvoiceDiscount() {
        return invoiceDiscount;
    }

    public void setInvoiceDiscount(BigDecimal invoiceDiscount) {
        this.invoiceDiscount = invoiceDiscount;
    }

    public BigDecimal getInvoiceEngineerFeeGross() {
        return invoiceEngineerFeeGross;
    }

    public void setInvoiceEngineerFeeGross(BigDecimal invoiceEngineerFeeGross) {
        this.invoiceEngineerFeeGross = invoiceEngineerFeeGross;
    }

    public BigDecimal getInvoiceEngineerFeeNet() {
        return invoiceEngineerFeeNet;
    }

    public void setInvoiceEngineerFeeNet(BigDecimal invoiceEngineerFeeNet) {
        this.invoiceEngineerFeeNet = invoiceEngineerFeeNet;
    }

    public BigDecimal getInvoiceEngineerFeeVat() {
        return invoiceEngineerFeeVat;
    }

    public void setInvoiceEngineerFeeVat(BigDecimal invoiceEngineerFeeVat) {
        this.invoiceEngineerFeeVat = invoiceEngineerFeeVat;
    }

    public BigDecimal getInvoiceExcessAmountCollected() {
        return invoiceExcessAmountCollected;
    }

    public void setInvoiceExcessAmountCollected(BigDecimal invoiceExcessAmountCollected) {
        this.invoiceExcessAmountCollected = invoiceExcessAmountCollected;
    }

    public BigDecimal getInvoiceFullTotalToPay() {
        return invoiceFullTotalToPay;
    }

    public void setInvoiceFullTotalToPay(BigDecimal invoiceFullTotalToPay) {
        this.invoiceFullTotalToPay = invoiceFullTotalToPay;
    }

    public BigDecimal getInvoiceHireGross() {
        return invoiceHireGross;
    }

    public void setInvoiceHireGross(BigDecimal invoiceHireGross) {
        this.invoiceHireGross = invoiceHireGross;
    }

    public BigDecimal getInvoiceHireNet() {
        return invoiceHireNet;
    }

    public void setInvoiceHireNet(BigDecimal invoiceHireNet) {
        this.invoiceHireNet = invoiceHireNet;
    }

    public BigDecimal getInvoiceHireRate() {
        return invoiceHireRate;
    }

    public void setInvoiceHireRate(BigDecimal invoiceHireRate) {
        this.invoiceHireRate = invoiceHireRate;
    }

    public BigDecimal getInvoiceHireVat() {
        return invoiceHireVat;
    }

    public void setInvoiceHireVat(BigDecimal invoiceHireVat) {
        this.invoiceHireVat = invoiceHireVat;
    }

    public BigDecimal getInvoiceHirePenaltyChargeAmount() {
        return invoiceHirePenaltyChargeAmount;
    }

    public void setInvoiceHirePenaltyChargeAmount(BigDecimal invoiceHirePenaltyChargeAmount) {
        this.invoiceHirePenaltyChargeAmount = invoiceHirePenaltyChargeAmount;
    }

    public String getInvoiceHirePenaltyChargePercentage() {
        return invoiceHirePenaltyChargePercentage;
    }

    public void setInvoiceHirePenaltyChargePercentage(String invoiceHirePenaltyChargePercentage) {
        this.invoiceHirePenaltyChargePercentage = invoiceHirePenaltyChargePercentage;
    }

    public BigDecimal getInvoiceRepairPenaltyChargeAmount() {
        return invoiceRepairPenaltyChargeAmount;
    }

    public void setInvoiceRepairPenaltyChargeAmount(BigDecimal invoiceRepairPenaltyChargeAmount) {
        this.invoiceRepairPenaltyChargeAmount = invoiceRepairPenaltyChargeAmount;
    }

    public String getInvoiceRepairPenaltyChargePercentage() {
        return invoiceRepairPenaltyChargePercentage;
    }

    public void setInvoiceRepairPenaltyChargePercentage(String invoiceRepairPenaltyChargePercentage) {
        this.invoiceRepairPenaltyChargePercentage = invoiceRepairPenaltyChargePercentage;
    }

    public String getInvoiceHirePenaltyChargePercentageApplied() {
        return invoiceHirePenaltyChargePercentageApplied;
    }

    public void setInvoiceHirePenaltyChargePercentageApplied(String invoiceHirePenaltyChargePercentageApplied) {
        this.invoiceHirePenaltyChargePercentageApplied = invoiceHirePenaltyChargePercentageApplied;
    }

    public String getInvoiceRepairPenaltyChargePercentageApplied() {
        return invoiceRepairPenaltyChargePercentageApplied;
    }

    public void setInvoiceRepairPenaltyChargePercentageApplied(String invoiceRepairPenaltyChargePercentageApplied) {
        this.invoiceRepairPenaltyChargePercentageApplied = invoiceRepairPenaltyChargePercentageApplied;
    }

    public BigDecimal getInvoiceTotalPenaltyCharge() {
        return invoiceTotalPenaltyCharge;
    }

    public void setInvoiceTotalPenaltyCharge(BigDecimal invoiceTotalPenaltyCharge) {
        this.invoiceTotalPenaltyCharge = invoiceTotalPenaltyCharge;
    }

    public BigDecimal getInvoiceRepairGross() {
        return invoiceRepairGross;
    }

    public void setInvoiceRepairGross(BigDecimal invoiceRepairGross) {
        this.invoiceRepairGross = invoiceRepairGross;
    }

    public BigDecimal getInvoiceRepairNet() {
        return invoiceRepairNet;
    }

    public void setInvoiceRepairNet(BigDecimal invoiceRepairNet) {
        this.invoiceRepairNet = invoiceRepairNet;
    }

    public BigDecimal getInvoiceRepairVat() {
        return invoiceRepairVat;
    }

    public void setInvoiceRepairVat(BigDecimal invoiceRepairVat) {
        this.invoiceRepairVat = invoiceRepairVat;
    }

    public String getInvoiceReviewRequired() {
        return invoiceReviewRequired;
    }

    public void setInvoiceReviewRequired(String invoiceReviewRequired) {
        this.invoiceReviewRequired = invoiceReviewRequired;
    }

    public BigDecimal getInvoiceStorageRecoveryGross() {
        return invoiceStorageRecoveryGross;
    }

    public void setInvoiceStorageRecoveryGross(BigDecimal invoiceStorageRecoveryGross) {
        this.invoiceStorageRecoveryGross = invoiceStorageRecoveryGross;
    }

    public BigDecimal getInvoiceStorageRecoveryNet() {
        return invoiceStorageRecoveryNet;
    }

    public void setInvoiceStorageRecoveryNet(BigDecimal invoiceStorageRecoveryNet) {
        this.invoiceStorageRecoveryNet = invoiceStorageRecoveryNet;
    }

    public BigDecimal getInvoiceStorageRecoveryVat() {
        return invoiceStorageRecoveryVat;
    }

    public void setInvoiceStorageRecoveryVat(BigDecimal invoiceStorageRecoveryVat) {
        this.invoiceStorageRecoveryVat = invoiceStorageRecoveryVat;
    }

    public String getInvoiceSupplierClaimInvoiceNo() {
        return invoiceSupplierClaimInvoiceNo;
    }

    public void setInvoiceSupplierClaimInvoiceNo(String invoiceSupplierClaimInvoiceNo) {
        this.invoiceSupplierClaimInvoiceNo = invoiceSupplierClaimInvoiceNo;
    }

    public String getInvoiceSupplierClaimsHandlingNo() {
        return invoiceSupplierClaimsHandlingNo;
    }

    public void setInvoiceSupplierClaimsHandlingNo(String invoiceSupplierClaimsHandlingNo) {
        this.invoiceSupplierClaimsHandlingNo = invoiceSupplierClaimsHandlingNo;
    }

    public BigDecimal getInvoiceTotalGross() {
        return invoiceTotalGross;
    }

    public void setInvoiceTotalGross(BigDecimal invoiceTotalGross) {
        this.invoiceTotalGross = invoiceTotalGross;
    }

    public BigDecimal getInvoiceTotalNet() {
        return invoiceTotalNet;
    }

    public void setInvoiceTotalNet(BigDecimal invoiceTotalNet) {
        this.invoiceTotalNet = invoiceTotalNet;
    }

    public BigDecimal getInvoiceTotalToPay() {
        return invoiceTotalToPay;
    }

    public void setInvoiceTotalToPay(BigDecimal invoiceTotalToPay) {
        this.invoiceTotalToPay = invoiceTotalToPay;
    }

    public BigDecimal getInvoiceTotalVat() {
        return invoiceTotalVat;
    }

    public void setInvoiceTotalVat(BigDecimal invoiceTotalVat) {
        this.invoiceTotalVat = invoiceTotalVat;
    }

    public String getInvoiceUploadedDate() {
        return invoiceUploadedDate;
    }

    public void setInvoiceUploadedDate(String invoiceUploadedDate) {
        this.invoiceUploadedDate = invoiceUploadedDate;
    }

    public String getPenaltyStartDate() {
        return penaltyStartDate;
    }

    public void setPenaltyStartDate(String penaltyStartDate) {
        this.penaltyStartDate = penaltyStartDate;
    }

    public BigDecimal getInvoiceVATAmountCollected() {
        return invoiceVATAmountCollected;
    }

    public void setInvoiceVATAmountCollected(BigDecimal invoiceVATAmountCollected) {
        this.invoiceVATAmountCollected = invoiceVATAmountCollected;
    }

    public String getManagingRepair() {
        return managingRepair;
    }

    public void setManagingRepair(String managingRepair) {
        this.managingRepair = managingRepair;
    }

    public String getNoticeDate() {
        return noticeDate;
    }

    public void setNoticeDate(String noticeDate) {
        this.noticeDate = noticeDate;
    }

    public String getSolicitorAddress1() {
        return solicitorAddress1;
    }

    public void setSolicitorAddress1(String solicitorAddress1) {
        this.solicitorAddress1 = solicitorAddress1;
    }

    public String getSolicitorAddress2() {
        return solicitorAddress2;
    }

    public void setSolicitorAddress2(String solicitorAddress2) {
        this.solicitorAddress2 = solicitorAddress2;
    }

    public String getSolicitorAddress3() {
        return solicitorAddress3;
    }

    public void setSolicitorAddress3(String solicitorAddress3) {
        this.solicitorAddress3 = solicitorAddress3;
    }

    public String getSolicitorAddress4() {
        return solicitorAddress4;
    }

    public void setSolicitorAddress4(String solicitorAddress4) {
        this.solicitorAddress4 = solicitorAddress4;
    }

    public String getSolicitorAddress5() {
        return solicitorAddress5;
    }

    public void setSolicitorAddress5(String solicitorAddress5) {
        this.solicitorAddress5 = solicitorAddress5;
    }

    public String getSolicitorName() {
        return solicitorName;
    }

    public void setSolicitorName(String solicitorName) {
        this.solicitorName = solicitorName;
    }

    public String getSolicitorPostcode() {
        return solicitorPostcode;
    }

    public void setSolicitorPostcode(String solicitorPostcode) {
        this.solicitorPostcode = solicitorPostcode;
    }

    public String getSolicitorTelephoneDay() {
        return solicitorTelephoneDay;
    }

    public void setSolicitorTelephoneDay(String solicitorTelephoneDay) {
        this.solicitorTelephoneDay = solicitorTelephoneDay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSupplierReference() {
        return supplierReference;
    }

    public void setSupplierReference(String supplierReference) {
        this.supplierReference = supplierReference;
    }

    public String getThirdPartyAddress1() {
        return thirdPartyAddress1;
    }

    public void setThirdPartyAddress1(String thirdPartyAddress1) {
        this.thirdPartyAddress1 = thirdPartyAddress1;
    }

    public String getThirdPartyAddress2() {
        return thirdPartyAddress2;
    }

    public void setThirdPartyAddress2(String thirdPartyAddress2) {
        this.thirdPartyAddress2 = thirdPartyAddress2;
    }

    public String getThirdPartyAddress3() {
        return thirdPartyAddress3;
    }

    public void setThirdPartyAddress3(String thirdPartyAddress3) {
        this.thirdPartyAddress3 = thirdPartyAddress3;
    }

    public String getThirdPartyAddress4() {
        return thirdPartyAddress4;
    }

    public void setThirdPartyAddress4(String thirdPartyAddress4) {
        this.thirdPartyAddress4 = thirdPartyAddress4;
    }

    public String getThirdPartyAddress5() {
        return thirdPartyAddress5;
    }

    public void setThirdPartyAddress5(String thirdPartyAddress5) {
        this.thirdPartyAddress5 = thirdPartyAddress5;
    }

    public String getThirdPartyEmail() {
        return thirdPartyEmail;
    }

    public void setThirdPartyEmail(String thirdPartyEmail) {
        this.thirdPartyEmail = thirdPartyEmail;
    }

    public String getThirdPartyFirstName() {
        return thirdPartyFirstName;
    }

    public void setThirdPartyFirstName(String thirdPartyFirstName) {
        this.thirdPartyFirstName = thirdPartyFirstName;
    }

    public String getThirdPartyInsurer() {
        return thirdPartyInsurer;
    }

    public void setThirdPartyInsurer(String thirdPartyInsurer) {
        this.thirdPartyInsurer = thirdPartyInsurer;
    }

    public String getThirdPartyInsurerBrand() {
        return thirdPartyInsurerBrand;
    }

    public void setThirdPartyInsurerBrand(String thirdPartyInsurerBrand) {
        this.thirdPartyInsurerBrand = thirdPartyInsurerBrand;
    }

    public String getThirdPartyPolicyNumber() {
        return thirdPartyPolicyNumber;
    }

    public void setThirdPartyPolicyNumber(String thirdPartyPolicyNumber) {
        this.thirdPartyPolicyNumber = thirdPartyPolicyNumber;
    }

    public String getThirdPartyPostcode() {
        return thirdPartyPostcode;
    }

    public void setThirdPartyPostcode(String thirdPartyPostcode) {
        this.thirdPartyPostcode = thirdPartyPostcode;
    }

    public String getThirdPartySurname() {
        return thirdPartySurname;
    }

    public void setThirdPartySurname(String thirdPartySurname) {
        this.thirdPartySurname = thirdPartySurname;
    }

    public String getThirdPartyTelephoneDay() {
        return thirdPartyTelephoneDay;
    }

    public void setThirdPartyTelephoneDay(String thirdPartyTelephoneDay) {
        this.thirdPartyTelephoneDay = thirdPartyTelephoneDay;
    }

    public String getThirdPartyTelephoneEvening() {
        return thirdPartyTelephoneEvening;
    }

    public void setThirdPartyTelephoneEvening(String thirdPartyTelephoneEvening) {
        this.thirdPartyTelephoneEvening = thirdPartyTelephoneEvening;
    }

    public String getThirdPartyTitle() {
        return thirdPartyTitle;
    }

    public void setThirdPartyTitle(String thirdPartyTitle) {
        this.thirdPartyTitle = thirdPartyTitle;
    }

    public String getThirdPartyVRN() {
        return thirdPartyVRN;
    }

    public void setThirdPartyVRN(String thirdPartyVRN) {
        this.thirdPartyVRN = thirdPartyVRN;
    }

    public String getThirdPartyVehicleClass() {
        return thirdPartyVehicleClass;
    }

    public void setThirdPartyVehicleClass(String thirdPartyVehicleClass) {
        this.thirdPartyVehicleClass = thirdPartyVehicleClass;
    }

    public String getThirdPartyVehicleManufacturer() {
        return thirdPartyVehicleManufacturer;
    }

    public void setThirdPartyVehicleManufacturer(String thirdPartyVehicleManufacturer) {
        this.thirdPartyVehicleManufacturer = thirdPartyVehicleManufacturer;
    }

    public String getThirdPartyVehicleModel() {
        return thirdPartyVehicleModel;
    }

    public void setThirdPartyVehicleModel(String thirdPartyVehicleModel) {
        this.thirdPartyVehicleModel = thirdPartyVehicleModel;
    }

    public String getTotalLoss() {
        return totalLoss;
    }

    public void setTotalLoss(String totalLoss) {
        this.totalLoss = totalLoss;
    }

    public String getUsable() {
        return usable;
    }

    public void setUsable(String usable) {
        this.usable = usable;
    }

    public String getWitnessAddress1() {
        return witnessAddress1;
    }

    public void setWitnessAddress1(String witnessAddress1) {
        this.witnessAddress1 = witnessAddress1;
    }

    public String getWitnessAddress2() {
        return witnessAddress2;
    }

    public void setWitnessAddress2(String witnessAddress2) {
        this.witnessAddress2 = witnessAddress2;
    }

    public String getWitnessAddress3() {
        return witnessAddress3;
    }

    public void setWitnessAddress3(String witnessAddress3) {
        this.witnessAddress3 = witnessAddress3;
    }

    public String getWitnessAddress4() {
        return witnessAddress4;
    }

    public void setWitnessAddress4(String witnessAddress4) {
        this.witnessAddress4 = witnessAddress4;
    }

    public String getWitnessAddress5() {
        return witnessAddress5;
    }

    public void setWitnessAddress5(String witnessAddress5) {
        this.witnessAddress5 = witnessAddress5;
    }

    public String getWitnessName() {
        return witnessName;
    }

    public void setWitnessName(String witnessName) {
        this.witnessName = witnessName;
    }

    public String getWitnessPostcode() {
        return witnessPostcode;
    }

    public void setWitnessPostcode(String witnessPostcode) {
        this.witnessPostcode = witnessPostcode;
    }

    public String getWitnessTelephoneDay() {
        return witnessTelephoneDay;
    }

    public void setWitnessTelephoneDay(String witnessTelephoneDay) {
        this.witnessTelephoneDay = witnessTelephoneDay;
    }

    public String getWitnessTelephoneEvening() {
        return witnessTelephoneEvening;
    }

    public void setWitnessTelephoneEvening(String witnessTelephoneEvening) {
        this.witnessTelephoneEvening = witnessTelephoneEvening;
    }

    public String getWorkgroup() {
        return workgroup;
    }

    public void setWorkgroup(String workgroup) {
        this.workgroup = workgroup;
    }

    public String getAverageDailyMileage() {
        return averageDailyMileage;
    }

    public void setAverageDailyMileage(String averageDailyMileage) {
        this.averageDailyMileage = averageDailyMileage;
    }

    public String getCourtesyCarEntitlement() {
        return courtesyCarEntitlement;
    }

    public void setCourtesyCarEntitlement(String courtesyCarEntitlement) {
        this.courtesyCarEntitlement = courtesyCarEntitlement;
    }

    public String getCustomerVehicleYear() {
        return customerVehicleYear;
    }

    public void setCustomerVehicleYear(String customerVehicleYear) {
        this.customerVehicleYear = customerVehicleYear;
    }

    public BigDecimal getExtrasAdditionalDriverFee() {
        return extrasAdditionalDriverFee;
    }

    public void setExtrasAdditionalDriverFee(BigDecimal extrasAdditionalDriverFee) {
        this.extrasAdditionalDriverFee = extrasAdditionalDriverFee;
    }

    public Integer getExtrasAdditionalDriverQuantity() {
        return extrasAdditionalDriverQuantity;
    }

    public void setExtrasAdditionalDriverQuantity(Integer extrasAdditionalDriverQuantity) {
        this.extrasAdditionalDriverQuantity = extrasAdditionalDriverQuantity;
    }

    public String getExtrasCoverNoteRequired() {
        return extrasCoverNoteRequired;
    }

    public void setExtrasCoverNoteRequired(String extrasCoverNoteRequired) {
        this.extrasCoverNoteRequired = extrasCoverNoteRequired;
    }

    public String getHireMonAuthorisedDate() {
        return hireMonAuthorisedDate;
    }

    public void setHireMonAuthorisedDate(String hireMonAuthorisedDate) {
        this.hireMonAuthorisedDate = hireMonAuthorisedDate;
    }

    public String getHireMonCommencedDate() {
        return hireMonCommencedDate;
    }

    public void setHireMonCommencedDate(String hireMonCommencedDate) {
        this.hireMonCommencedDate = hireMonCommencedDate;
    }

    public String getHireMonTotalLossAcceptedDate() {
        return hireMonTotalLossAcceptedDate;
    }

    public void setHireMonTotalLossAcceptedDate(String hireMonTotalLossAcceptedDate) {
        this.hireMonTotalLossAcceptedDate = hireMonTotalLossAcceptedDate;
    }

    public String getHireMonTotalLossChequeIssuedDate() {
        return hireMonTotalLossChequeIssuedDate;
    }

    public void setHireMonTotalLossChequeIssuedDate(String hireMonTotalLossChequeIssuedDate) {
        this.hireMonTotalLossChequeIssuedDate = hireMonTotalLossChequeIssuedDate;
    }

    public String getHireMonTotalLossChequeReceivedDate() {
        return hireMonTotalLossChequeReceivedDate;
    }

    public void setHireMonTotalLossChequeReceivedDate(String hireMonTotalLossChequeReceivedDate) {
        this.hireMonTotalLossChequeReceivedDate = hireMonTotalLossChequeReceivedDate;
    }

    public String getHireMonTotalLossOfferMadeDate() {
        return hireMonTotalLossOfferMadeDate;
    }

    public void setHireMonTotalLossOfferMadeDate(String hireMonTotalLossOfferMadeDate) {
        this.hireMonTotalLossOfferMadeDate = hireMonTotalLossOfferMadeDate;
    }

    public BigDecimal getInvoiceTotalLossFeeGross() {
        return invoiceTotalLossFeeGross;
    }

    public void setInvoiceTotalLossFeeGross(BigDecimal invoiceTotalLossFeeGross) {
        this.invoiceTotalLossFeeGross = invoiceTotalLossFeeGross;
    }

    public BigDecimal getInvoiceTotalLossFeeNet() {
        return invoiceTotalLossFeeNet;
    }

    public void setInvoiceTotalLossFeeNet(BigDecimal invoiceTotalLossFeeNet) {
        this.invoiceTotalLossFeeNet = invoiceTotalLossFeeNet;
    }

    public BigDecimal getInvoiceTotalLossFeeVat() {
        return invoiceTotalLossFeeVat;
    }

    public void setInvoiceTotalLossFeeVat(BigDecimal invoiceTotalLossFeeVat) {
        this.invoiceTotalLossFeeVat = invoiceTotalLossFeeVat;
    }

    public String getOtherVehicleAccess() {
        return otherVehicleAccess;
    }

    public void setOtherVehicleAccess(String otherVehicleAccess) {
        this.otherVehicleAccess = otherVehicleAccess;
    }

    public String getOtherVehicleType() {
        return otherVehicleType;
    }

    public void setOtherVehicleType(String otherVehicleType) {
        this.otherVehicleType = otherVehicleType;
    }

    public String getOtherVehicleUsed() {
        return otherVehicleUsed;
    }

    public void setOtherVehicleUsed(String otherVehicleUsed) {
        this.otherVehicleUsed = otherVehicleUsed;
    }

    public String getSpecialRequirements() {
        return specialRequirements;
    }

    public void setSpecialRequirements(String specialRequirements) {
        this.specialRequirements = specialRequirements;
    }

    public String getSpecificVehicleReason() {
        return specificVehicleReason;
    }

    public void setSpecificVehicleReason(String specificVehicleReason) {
        this.specificVehicleReason = specificVehicleReason;
    }

    public String getSpecificVehicleRequired() {
        return specificVehicleRequired;
    }

    public void setSpecificVehicleRequired(String specificVehicleRequired) {
        this.specificVehicleRequired = specificVehicleRequired;
    }

    public String getVehicleTypeRequired() {
        return vehicleTypeRequired;
    }

    public void setVehicleTypeRequired(String vehicleTypeRequired) {
        this.vehicleTypeRequired = vehicleTypeRequired;
    }

    public String getInvoiceInterimPayment() {
        return invoiceInterimPayment;
    }

    public void setInvoiceInterimPayment(String interimPayment) {
        this.invoiceInterimPayment = interimPayment;
    }

    public BigDecimal getInvoiceInterimPaymentAmount() {
        return invoiceInterimPaymentAmount;
    }

    public void setInvoiceInterimPaymentAmount(BigDecimal invoiceInterimPaymentAmount) {
        this.invoiceInterimPaymentAmount = invoiceInterimPaymentAmount;
    }

    public String getCustomerHpiVehicleCapacity() {
        return customerHpiVehicleCapacity;
    }

    public void setCustomerHpiVehicleCapacity(String customerHpiVehicleCapacity) {
        this.customerHpiVehicleCapacity = customerHpiVehicleCapacity;
    }

    public String getCustomerHpiVehicleDoorplan() {
        return customerHpiVehicleDoorplan;
    }

    public void setCustomerHpiVehicleDoorplan(String customerHpiVehicleDoorplan) {
        this.customerHpiVehicleDoorplan = customerHpiVehicleDoorplan;
    }

    public String getCustomerHpiVehicleManufacturer() {
        return customerHpiVehicleManufacturer;
    }

    public void setCustomerHpiVehicleManufacturer(String customerHpiVehicleManufacturer) {
        this.customerHpiVehicleManufacturer = customerHpiVehicleManufacturer;
    }

    public String getCustomerHpiVehicleModel() {
        return customerHpiVehicleModel;
    }

    public void setCustomerHpiVehicleModel(String customerHpiVehicleModel) {
        this.customerHpiVehicleModel = customerHpiVehicleModel;
    }

    public String getCustomerHpiVehicleTransmission() {
        return customerHpiVehicleTransmission;
    }

    public void setCustomerHpiVehicleTransmission(String customerHpiVehicleTransmission) {
        this.customerHpiVehicleTransmission = customerHpiVehicleTransmission;
    }

    public String getCustomerHpiVehicleYear() {
        return customerHpiVehicleYear;
    }

    public void setCustomerHpiVehicleYear(String customerHpiVehicleYear) {
        this.customerHpiVehicleYear = customerHpiVehicleYear;
    }

    public String getHireVehicleHpiVehicleCapacity() {
        return hireVehicleHpiVehicleCapacity;
    }

    public void setHireVehicleHpiVehicleCapacity(String hireVehicleHpiVehicleCapacity) {
        this.hireVehicleHpiVehicleCapacity = hireVehicleHpiVehicleCapacity;
    }

    public String getHireVehicleHpiVehicleDoorplan() {
        return hireVehicleHpiVehicleDoorplan;
    }

    public void setHireVehicleHpiVehicleDoorplan(String hireVehicleHpiVehicleDoorplan) {
        this.hireVehicleHpiVehicleDoorplan = hireVehicleHpiVehicleDoorplan;
    }

    public String getHireVehicleHpiVehicleManufacturer() {
        return hireVehicleHpiVehicleManufacturer;
    }

    public void setHireVehicleHpiVehicleManufacturer(String hireVehicleHpiVehicleManufacturer) {
        this.hireVehicleHpiVehicleManufacturer = hireVehicleHpiVehicleManufacturer;
    }

    public String getHireVehicleHpiVehicleModel() {
        return hireVehicleHpiVehicleModel;
    }

    public void setHireVehicleHpiVehicleModel(String hireVehicleHpiVehicleModel) {
        this.hireVehicleHpiVehicleModel = hireVehicleHpiVehicleModel;
    }

    public String getHireVehicleHpiVehicleTransmission() {
        return hireVehicleHpiVehicleTransmission;
    }

    public void setHireVehicleHpiVehicleTransmission(String hireVehicleHpiVehicleTransmission) {
        this.hireVehicleHpiVehicleTransmission = hireVehicleHpiVehicleTransmission;
    }

    public String getHireVehicleHpiVehicleYear() {
        return hireVehicleHpiVehicleYear;
    }

    public void setHireVehicleHpiVehicleYear(String hireVehicleHpiVehicleYear) {
        this.hireVehicleHpiVehicleYear = hireVehicleHpiVehicleYear;
    }

    public String getCustomerHpiVehicleRegistrationDate() {
        return customerHpiVehicleRegistrationDate;
    }

    public void setCustomerHpiVehicleRegistrationDate(String customerHpiVehicleRegistrationDate) {
        this.customerHpiVehicleRegistrationDate = customerHpiVehicleRegistrationDate;
    }

    public String getHireVehicleHpiVehicleRegistrationDate() {
        return hireVehicleHpiVehicleRegistrationDate;
    }

    public void setHireVehicleHpiVehicleRegistrationDate(String hireVehicleHpiVehicleRegistrationDate) {
        this.hireVehicleHpiVehicleRegistrationDate = hireVehicleHpiVehicleRegistrationDate;
    }

    public BigDecimal getPaymentDetailsEngineerFeePaid() {
        return paymentDetailsEngineerFeePaid;
    }

    public void setPaymentDetailsEngineerFeePaid(BigDecimal paymentDetailsEngineerFeePaid) {
        this.paymentDetailsEngineerFeePaid = paymentDetailsEngineerFeePaid;
    }

    public BigDecimal getPaymentDetailsHirePaid() {
        return paymentDetailsHirePaid;
    }

    public void setPaymentDetailsHirePaid(BigDecimal paymentDetailsHirePaid) {
        this.paymentDetailsHirePaid = paymentDetailsHirePaid;
    }

    public BigDecimal getPaymentDetailsHirePenaltyPaid() {
        return paymentDetailsHirePenaltyPaid;
    }

    public void setPaymentDetailsHirePenaltyPaid(BigDecimal paymentDetailsHirePenaltyPaid) {
        this.paymentDetailsHirePenaltyPaid = paymentDetailsHirePenaltyPaid;
    }

    public BigDecimal getPaymentDetailsRepairPaid() {
        return paymentDetailsRepairPaid;
    }

    public void setPaymentDetailsRepairPaid(BigDecimal paymentDetailsRepairPaid) {
        this.paymentDetailsRepairPaid = paymentDetailsRepairPaid;
    }

    public BigDecimal getPaymentDetailsRepairPenaltyPaid() {
        return paymentDetailsRepairPenaltyPaid;
    }

    public void setPaymentDetailsRepairPenaltyPaid(BigDecimal paymentDetailsRepairPenaltyPaid) {
        this.paymentDetailsRepairPenaltyPaid = paymentDetailsRepairPenaltyPaid;
    }

    public BigDecimal getPaymentDetailsStorageRecoveryPaid() {
        return paymentDetailsStorageRecoveryPaid;
    }

    public void setPaymentDetailsStorageRecoveryPaid(BigDecimal paymentDetailsStorageRecoveryPaid) {
        this.paymentDetailsStorageRecoveryPaid = paymentDetailsStorageRecoveryPaid;
    }

    public BigDecimal getPaymentDetailsTotalLossPaid() {
        return paymentDetailsTotalLossPaid;
    }

    public void setPaymentDetailsTotalLossPaid(BigDecimal paymentDetailsTotalLossPaid) {
        this.paymentDetailsTotalLossPaid = paymentDetailsTotalLossPaid;
    }

	public BigDecimal getPaymentDetailsFinalPayment() {
		return paymentDetailsFinalPayment;
	}

	public void setPaymentDetailsFinalPayment(BigDecimal paymentDetailsFinalPayment) {
		this.paymentDetailsFinalPayment = paymentDetailsFinalPayment;
	}

    public BigDecimal getPaymentDetailsChoDiscountFeePaid() {
        return paymentDetailsChoDiscountFeePaid;
    }

    public void setPaymentDetailsChoDiscountFeePaid(BigDecimal paymentDetailsChoDiscountFeePaid) {
        this.paymentDetailsChoDiscountFeePaid = paymentDetailsChoDiscountFeePaid;
    }

    public BigDecimal getPaymentDetailsClaimHandlerChargePaid() {
        return paymentDetailsClaimHandlerChargePaid;
    }

    public void setPaymentDetailsClaimHandlerChargePaid(BigDecimal paymentDetailsClaimHandlerChargePaid) {
        this.paymentDetailsClaimHandlerChargePaid = paymentDetailsClaimHandlerChargePaid;
    }

    public BigDecimal getPaymentDetailsDeductionClaimHandlerFeePaid() {
        return paymentDetailsDeductionClaimHandlerFeePaid;
    }

    public void setPaymentDetailsDeductionClaimHandlerFeePaid(BigDecimal paymentDetailsDeductionClaimHandlerFeePaid) {
        this.paymentDetailsDeductionClaimHandlerFeePaid = paymentDetailsDeductionClaimHandlerFeePaid;
    }

    public BigDecimal getPaymentDetailsInsurerDiscountFeePaid() {
        return paymentDetailsInsurerDiscountFeePaid;
    }

    public void setPaymentDetailsInsurerDiscountFeePaid(BigDecimal paymentDetailsInsurerDiscountFeePaid) {
        this.paymentDetailsInsurerDiscountFeePaid = paymentDetailsInsurerDiscountFeePaid;
    }

    public String getSupplierClaimOwner() {
        return supplierClaimOwner;
    }

    public String getIsNFInsurerManagingRepair() {
        return isNFInsurerManagingRepair;
    }

    public void setIsNFInsurerManagingRepair(String isNFInsurerManagingRepair) {
        this.isNFInsurerManagingRepair = isNFInsurerManagingRepair;
    }

    public String getIsRepairOnlyCheck() {
        return isRepairOnlyCheck;
    }

    public void setIsRepairOnlyCheck(String isRepairOnlyCheck) {
        this.isRepairOnlyCheck = isRepairOnlyCheck;
    }

    public BigDecimal getOriginalInvoiceFullTotalToPay() {
        return originalInvoiceFullTotalToPay;
    }

    public void setOriginalInvoiceFullTotalToPay(BigDecimal originalInvoiceFullTotalToPay) {
        this.originalInvoiceFullTotalToPay = originalInvoiceFullTotalToPay;
    }

    public BigDecimal getOriginalInvoiceTotalToPay() {
        return originalInvoiceTotalToPay;
    }

    public void setOriginalInvoiceTotalToPay(BigDecimal originalInvoiceTotalToPay) {
        this.originalInvoiceTotalToPay = originalInvoiceTotalToPay;
    }

    public String getClientVatRegistered() {
        return clientVatRegistered;
    }
    
    public String getFinalReview() {
        return finalReview;
    }

}

