package idas.chox.service.reports.viewdata;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Customer;
import idas.chox.core.model.EngineerReport;
import idas.chox.core.model.HireMonitoringDetail;
import idas.chox.core.model.Incident;
import idas.chox.core.model.Injury;
import idas.chox.core.model.InsurerHireMonitoringDetail;
import idas.chox.core.model.InsurerVehicleHire;
import idas.chox.core.model.Invoice;
import idas.chox.core.model.LiabilityStatus;
import idas.chox.core.model.Solicitor;
import idas.chox.core.model.ThirdParty;
import idas.chox.core.model.VehicleHire;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.Witness;
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
    private String indemnityStance;
    private String contactDate;
    private String claimOwner;
    private String supplierClaimOwner;
    private String workgroup;
    private BigDecimal indemnityValue;
    private BigDecimal insurerLiabilityAgreed;
    private BigDecimal insurerLiabilityApplied;
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
    private BigDecimal invoiceGtaDiscount;
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
    private BigDecimal extrasCollaborationFee;
    private Integer extrasCollaborationQuantity;
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
    private BigDecimal extrasVedFee;
    private Integer extrasVedQuantity;
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
    private BigDecimal extrasRepairAdminFee;
    private BigDecimal extrasRepairAcquisitionFee;
    private BigDecimal extrasRepairParts;
    private BigDecimal extrasRepairLabour;
    private BigDecimal extrasRepairMaterials;
    private BigDecimal extrasRepairSpecialist;
    private boolean subscriberClaim;
    private boolean fixedFeeClaim;
    private boolean collaborationClaim;
    private String invoicePaymentsTeam;
    private boolean paymentsTeamActivated;
    private String remainingSlaDays;
    private boolean isInsurerOrAdmin;
    private boolean isInsurer;
    private boolean isInsurerHireMonitoring;
    private String fraudScore;
    private String fraudStatus;
    private String insurerHireMonBookedInDate;
    private String insurerHireMonAuthorisedDate;
    private String insurerHireMonCommencedDate;
    private String insurerHireMonInspectionBookedDate;
    private String insurerHireMonInspectionDate;
    private String insurerHireMonTotalLossOfferMadeDate;
    private String insurerHireMonTotalLossAcceptedDate;
    private String insurerHireMonTotalLossChequeIssuedDate;
    private String insurerHireMonTotalLossChequeReceivedDate;
    private String insurerHireMonRepairCompletionDate;
    private BigDecimal insurerHireMonLabourRate;
    private BigDecimal insurerHireMonLabourHours;
    private BigDecimal insurerHireMonTotalLabourCost;
    private String insurerHireMonClaimantImpecunious;
    private String insurerHireMonWhoManagedRepair;
    private String insurerHireMonReplacementVehicleClass;
    private String insurerHireMonHireStart;
    private boolean isCopleyOffer;
    private String copleyOfferMade;
    private String copleyOfferMadeDate;
    private String invoiceReviewReason;
    private boolean invoiceReviewReasonsEnabled;
    private String insurerHireMonTpReportedIncidentToTPIDate;
    private String insurerHireMonTotalLossReportSentToUsDate;
    private String insurerHireMonPartsReceivedDate;
    private String insurerHireMonHireEnd;

    public ClaimFileReportData(Claim claim, WebUser currentUser) {
        try {
            claimType = claim.getClaimType().toString();
            subscriberClaim = ClaimType.isSubscriber(claim.getClaimType());
            fixedFeeClaim = ClaimType.isFixedFee(claim.getClaimType());
            collaborationClaim = ClaimType.isCollaborationProtocol(claim.getClaimType());
            paymentsTeamActivated = claim.getInsurer().isPaymentsTeamEnable();
            if (claim.getChorganisation() != null) {
                choName = claim.getChorganisation().getName();
            }
            createdBy = claim.getCreatedBy().getFullName();
            createdOn = DateHelper.getLocalDateTimeFormat().format(claim.getCreatedDate());
            supplierReference = claim.getChoReference();
            insurerClaimNumber = claim.getClaimNumber();
            status = claim.getStatus();
            remainingSlaDays = claim.getRemainingSlaDays();

            if (claim.getLiabilityStatus() == LiabilityStatus.LIABILITY_NULL) {
                liabilityStatus = "";
            } else {
                liabilityStatus = claim.getLiabilityStatus().toString();
            }
            if (claim.getIndemnityStance() == null) {
                indemnityStance = "";
            } else {
                indemnityStance = claim.getIndemnityStance();
            }
            if (currentUser.isAnInsurer()) {
                isInsurerOrAdmin = true;
                isInsurer = true;
                finalReview = claim.isFinalReviewIns() ? "Yes" : "No";
                if (ClaimType.isInsurerUpload(claim.getClaimType()) && claim.getInsurer().isCopleyQuestion()) {
                    isCopleyOffer = true;
                    copleyOfferMade = claim.getCopleyOfferMadeDesc();
                    if (claim.getCopleyOfferMadeDate() != null) {
                        copleyOfferMadeDate = DateHelper.getLocalDateFormat().format(claim.getCopleyOfferMadeDate());
                    }
                }
                if ((ClaimType.isInsurerUpload(claim.getClaimType()) && currentUser.getInsurer().isEnableManualLouDates())
                        || (!ClaimType.isInsurerUpload(claim.getClaimType()) && currentUser.getInsurer().isEnableLouDates())) {
                    isInsurerHireMonitoring = true;
                    InsurerHireMonitoringDetail hmd = claim.getInsurerHireMonitoringDetail();
                    if (hmd != null) {
                        if (hmd.getRepairBookInDate() != null) {
                            insurerHireMonBookedInDate = DateHelper.getLocalDateFormat().format(hmd.getRepairBookInDate());
                        } else {
                            insurerHireMonBookedInDate = "";
                        }
                        if (hmd.getRepairAuthorisedDate() == null) {
                            insurerHireMonAuthorisedDate = "";
                        } else {
                            insurerHireMonAuthorisedDate = DateHelper.getLocalDateFormat().format(hmd.getRepairAuthorisedDate());
                        }
                        if (hmd.getRepairCommencedDate() == null) {
                            insurerHireMonCommencedDate = "";
                        } else {
                            insurerHireMonCommencedDate = DateHelper.getLocalDateFormat().format(hmd.getRepairCommencedDate());
                        }
                        if (hmd.getInspectionBookedDate() != null) {
                            insurerHireMonInspectionBookedDate = DateHelper.getLocalDateFormat().format(hmd.getInspectionBookedDate());
                        } else {
                            insurerHireMonInspectionBookedDate = "";
                        }
                        if (hmd.getInspectionDate() != null) {
                            insurerHireMonInspectionDate = DateHelper.getLocalDateFormat().format(hmd.getInspectionDate());
                        } else {
                            insurerHireMonInspectionDate = "";
                        }
                        if (hmd.getTotalLossOfferMadeDate() == null) {
                            insurerHireMonTotalLossOfferMadeDate = "";
                        } else {
                            insurerHireMonTotalLossOfferMadeDate = DateHelper.getLocalDateFormat().format(hmd.getTotalLossOfferMadeDate());
                        }
                        if (hmd.getTotalLossOfferAcceptedDate() == null) {
                            insurerHireMonTotalLossAcceptedDate = "";
                        } else {
                            insurerHireMonTotalLossAcceptedDate = DateHelper.getLocalDateFormat().format(hmd.getTotalLossOfferAcceptedDate());
                        }
                        if (hmd.getTotalLossOfferCheckIssuedDate() == null) {
                            insurerHireMonTotalLossChequeIssuedDate = "";
                        } else {
                            insurerHireMonTotalLossChequeIssuedDate = DateHelper.getLocalDateFormat().format(hmd.getTotalLossOfferCheckIssuedDate());
                        }
                        if (hmd.getTotalLossOfferCheckReceivedDate() == null) {
                            insurerHireMonTotalLossChequeReceivedDate = "";
                        } else {
                            insurerHireMonTotalLossChequeReceivedDate = DateHelper.getLocalDateFormat().format(hmd.getTotalLossOfferCheckReceivedDate());
                        }
                        if (hmd.getRepairCompletionDate() != null) {
                            insurerHireMonRepairCompletionDate = DateHelper.getLocalDateFormat().format(hmd.getRepairCompletionDate());
                        }
                        if (hmd.getTPReportedIncidentToTPIDate() == null){
                            insurerHireMonTpReportedIncidentToTPIDate = "";
                        } else {
                            insurerHireMonTpReportedIncidentToTPIDate = DateHelper.getLocalDateFormat().format(hmd.getTPReportedIncidentToTPIDate());
                        }
                        if (hmd.getTLReportSentToUsDate() == null){
                            insurerHireMonTotalLossReportSentToUsDate = "";
                        } else {
                            insurerHireMonTotalLossReportSentToUsDate = DateHelper.getLocalDateFormat().format(hmd.getTLReportSentToUsDate());
                        }
                        if (hmd.getPartsReceivedDate() == null){
                            insurerHireMonPartsReceivedDate = "";
                        } else {
                            insurerHireMonPartsReceivedDate = DateHelper.getLocalDateFormat().format(hmd.getPartsReceivedDate());
                        }
                        insurerHireMonLabourRate = hmd.getLabourRate();
                        insurerHireMonLabourHours = hmd.getLabourHour();
                        insurerHireMonTotalLabourCost = hmd.getLabourCost();
                        insurerHireMonClaimantImpecunious = hmd.getClaimantImpecuniousDesc();
                        if (hmd.getWhoManagedRepair() != null) {
                            insurerHireMonWhoManagedRepair = hmd.getWhoManagedRepair();
                        } else {
                            insurerHireMonWhoManagedRepair = "";
                        }
                    }

                    InsurerVehicleHire insVehicleHire = claim.getInsurerVehicleHire();
                    if (insVehicleHire != null) {
                        if (insVehicleHire.getVehicleClass() != null) {
                            insurerHireMonReplacementVehicleClass = insVehicleHire.getVehicleClass().getName();
                        } else {
                            insurerHireMonReplacementVehicleClass = "";
                        }
                        if (insVehicleHire.getRentalStart() != null) {
                            insurerHireMonHireStart = DateHelper.getLocalDateTimeFormat().format(insVehicleHire.getRentalStart());
                        } else {
                            insurerHireMonHireStart = "";
                        }
                        if (insVehicleHire.getRentalEnd() != null) {
                            insurerHireMonHireEnd = DateHelper.getLocalDateTimeFormat().format(insVehicleHire.getRentalEnd());
                        } else {
                            insurerHireMonHireEnd = "";
                        }
                    }
                }
            } else if (currentUser.isCHO()) {
                finalReview = claim.isFinalReviewCho() ? "Yes" : "No";
            } else if (currentUser.isCHOXAdmin()) {
                isInsurerOrAdmin = true;
                finalReview = (claim.isFinalReviewCho() ? "Yes (CHO), " : "No (CHO), ")
                        + (claim.isFinalReviewIns() ? "Yes (Ins)" : "No (Ins)");
            }
            if (isInsurer) {
                fraudScore = claim.getKeoghsRequest() == null ? "" : Integer.toString(claim.getKeoghsRequest().getTotalScore());
                fraudStatus = claim.getKeoghsRequest() == null ? "" : claim.getKeoghsRequest().getRagResult();
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
            if (claim.getAppliedLiability() != null) {
                insurerLiabilityApplied = claim.getAppliedLiability().divide(new BigDecimal("100.00"));
            }
            if (claim.getPercentageLiabilityCho() != null) {
                choLiabilityAgreed = claim.getPercentageLiabilityCho().divide(new BigDecimal("100.00"));
            }
            if (claim.getLiabilityAgreedDate() != null) {
                dateLiabilityAgreed = DateHelper.getLocalDateFormat().format(claim.getLiabilityAgreedDate());
            }
            Customer cust = claim.getCustomer();
            if (cust != null) {
                LOG.debug("Adding customer info.");
                customer = cust.getFormattedName();
                customerTitle = claim.isHashed() && (cust.getTitle() != null && !cust.getTitle().isEmpty()) ? "GDPR: data removed" : cust.getTitle();
                customerFirstName = claim.isHashed() && (cust.getFirstName() != null && !cust.getFirstName().isEmpty()) ? "GDPR: data removed" : cust.getFirstName();
                customerSurname = claim.isHashed() && (cust.getLastName() != null && !cust.getLastName().isEmpty()) ? "GDPR: data removed" : cust.getLastName();
                customerAddress1 = claim.isHashed() && (cust.getAddress1() != null && !cust.getAddress1().isEmpty()) ? "GDPR: data removed" : cust.getAddress1();
                customerAddress2 = claim.isHashed() && (cust.getAddress2() != null && !cust.getAddress2().isEmpty()) ? "GDPR: data removed" : cust.getAddress2();
                customerAddress3 = claim.isHashed() && (cust.getAddress3() != null && !cust.getAddress3().isEmpty()) ? "GDPR: data removed" : cust.getAddress3();
                customerAddress4 = claim.isHashed() && (cust.getAddress4() != null && !cust.getAddress4().isEmpty()) ? "GDPR: data removed" : cust.getAddress4();
                customerAddress5 = claim.isHashed() && (cust.getAddress5() != null && !cust.getAddress5().isEmpty()) ? "GDPR: data removed" : cust.getAddress5();
                customerPostcode = claim.isHashed() && (cust.getPostcode() != null && !cust.getPostcode().isEmpty()) ? "GDPR: data removed" : cust.getPostcode();
                customerTelephoneDay = claim.isHashed() && (cust.getTelephoneDay() != null && !cust.getTelephoneDay().isEmpty()) ? "GDPR: data removed" : cust.getTelephoneDay();
                customerTelephoneEvening = claim.isHashed() && (cust.getTelephoneEvening() != null && !cust.getTelephoneEvening().isEmpty()) ? "GDPR: data removed" : cust.getTelephoneEvening();
                customerEmail = claim.isHashed() && (cust.getEmail() != null && !cust.getEmail().isEmpty()) ? "GDPR: data removed" : cust.getEmail();
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
                customerVRN = claim.isHashed() && (cust.getVehicleRegistration() != null && !cust.getVehicleRegistration().isEmpty()) ? "GDPR: data removed" : cust.getVehicleRegistration();
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
                    averageDailyMileage = cust.getAverageDailyMileage();
                } else {
                    averageDailyMileage = "";
                }
                if (cust.getVehicleYear() != null) {
                    customerVehicleYear = cust.getVehicleYear();
                } else {
                    customerVehicleYear = "";
                }
            }
            ThirdParty thirdParty = claim.getThirdParty();
            if (thirdParty != null) {
                LOG.debug("Adding thirdparty info.");
                if (thirdParty.getInsurer() != null) {
                    thirdPartyInsurer = thirdParty.getInsurer().getName();
                }
                thirdPartyTitle = claim.isHashed() && (thirdParty.getTitle() != null && !thirdParty.getTitle().isEmpty()) ? "GDPR: data removed" : thirdParty.getTitle();
                thirdPartyFirstName = claim.isHashed() && (thirdParty.getFirstName() != null && !thirdParty.getFirstName().isEmpty()) ? "GDPR: data removed" : thirdParty.getFirstName();
                thirdPartySurname = claim.isHashed() && (thirdParty.getLastName() != null && !thirdParty.getLastName().isEmpty()) ? "GDPR: data removed" : thirdParty.getLastName();
                thirdPartyAddress1 = claim.isHashed() && (thirdParty.getAddress1() != null && !thirdParty.getAddress1().isEmpty()) ? "GDPR: data removed" : thirdParty.getAddress1();
                thirdPartyAddress2 = claim.isHashed() && (thirdParty.getAddress2() != null && !thirdParty.getAddress2().isEmpty()) ? "GDPR: data removed" : thirdParty.getAddress2();
                thirdPartyAddress3 = claim.isHashed() && (thirdParty.getAddress3() != null && !thirdParty.getAddress3().isEmpty()) ? "GDPR: data removed" : thirdParty.getAddress3();
                thirdPartyAddress4 = claim.isHashed() && (thirdParty.getAddress4() != null && !thirdParty.getAddress4().isEmpty()) ? "GDPR: data removed" : thirdParty.getAddress4();
                thirdPartyAddress5 = claim.isHashed() && (thirdParty.getAddress5() != null && !thirdParty.getAddress5().isEmpty()) ? "GDPR: data removed" : thirdParty.getAddress5();
                thirdPartyPostcode = claim.isHashed() && (thirdParty.getPostcode() != null && !thirdParty.getPostcode().isEmpty()) ? "GDPR: data removed" : thirdParty.getPostcode();
                thirdPartyTelephoneDay = claim.isHashed() && (thirdParty.getTelephoneDay() != null && !thirdParty.getTelephoneDay().isEmpty()) ? "GDPR: data removed" : thirdParty.getTelephoneDay();
                thirdPartyTelephoneEvening = claim.isHashed() && (thirdParty.getTelephoneEvening() != null && !thirdParty.getTelephoneEvening().isEmpty()) ? "GDPR: data removed" : thirdParty.getTelephoneEvening();
                thirdPartyEmail = claim.isHashed() && (thirdParty.getEmail() != null && !thirdParty.getEmail().isEmpty()) ? "GDPR: data removed" : thirdParty.getEmail();
                thirdPartyInsurer = thirdParty.getInsurer().getName();
                thirdPartyInsurerBrand = thirdParty.getInsurerBrand();
                thirdPartyPolicyNumber = thirdParty.getPolicyNumber();
                thirdPartyVehicleManufacturer = thirdParty.getVehicleManufacturer();
                thirdPartyVehicleModel = thirdParty.getVehicleModel();
                thirdPartyVRN = claim.isHashed() && (thirdParty.getVehicleRegistration() != null && !thirdParty.getVehicleRegistration().isEmpty()) ? "GDPR: data removed" : thirdParty.getVehicleRegistration();
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
            invoiceReviewReason = claim.getInvoiceReviewReason();
            invoiceReviewReasonsEnabled = claim.getInsurer().isInvoiceReviewEnable();
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
                    witnessName = claim.isHashed() && (witness.getName() != null && !witness.getName().isEmpty()) ? "GDPR: data removed" : witness.getName();
                    witnessAddress1 = claim.isHashed() && (witness.getAddress1() != null && !witness.getAddress1().isEmpty()) ? "GDPR: data removed" : witness.getAddress1();
                    witnessAddress2 = claim.isHashed() && (witness.getAddress2() != null && !witness.getAddress2().isEmpty()) ? "GDPR: data removed" : witness.getAddress2();
                    witnessAddress3 = claim.isHashed() && (witness.getAddress3() != null && !witness.getAddress3().isEmpty()) ? "GDPR: data removed" : witness.getAddress3();
                    witnessAddress4 = claim.isHashed() && (witness.getAddress4() != null && !witness.getAddress4().isEmpty()) ? "GDPR: data removed" : witness.getAddress4();
                    witnessAddress5 = claim.isHashed() && (witness.getAddress5() != null && !witness.getAddress5().isEmpty()) ? "GDPR: data removed" : witness.getAddress5();
                    witnessPostcode = claim.isHashed() && (witness.getPostcode() != null && !witness.getPostcode().isEmpty()) ? "GDPR: data removed" : witness.getPostcode();
                    witnessTelephoneDay = claim.isHashed() && (witness.getTelephoneDay() != null && !witness.getTelephoneDay().isEmpty()) ? "GDPR: data removed" : witness.getTelephoneDay();
                    witnessTelephoneEvening = claim.isHashed() && (witness.getTelephoneEvening() != null && !witness.getTelephoneEvening().isEmpty()) ? "GDPR: data removed" : witness.getTelephoneEvening();
                    witnessEmail = claim.isHashed() && (witness.getEmail() != null && !witness.getEmail().isEmpty()) ? "GDPR: data removed" : witness.getEmail();
                }
                Injury injury = incident.getInjury();
                if (injury != null) {
                    LOG.debug("Adding injury info.");
                    injuryName = claim.isHashed() && (injury.getName() != null && !injury.getName().isEmpty()) ? "GDPR: data removed" : injury.getName();
                    injuryAddress1 = claim.isHashed() && (injury.getAddress1() != null && !injury.getAddress1().isEmpty()) ? "GDPR: data removed" : injury.getAddress1();
                    injuryAddress2 = claim.isHashed() && (injury.getAddress2() != null && !injury.getAddress2().isEmpty()) ? "GDPR: data removed" : injury.getAddress2();
                    injuryAddress3 = claim.isHashed() && (injury.getAddress3() != null && !injury.getAddress3().isEmpty()) ? "GDPR: data removed" : injury.getAddress3();
                    injuryAddress4 = claim.isHashed() && (injury.getAddress4() != null && !injury.getAddress4().isEmpty()) ? "GDPR: data removed" : injury.getAddress4();
                    injuryAddress5 = claim.isHashed() && (injury.getAddress5() != null && !injury.getAddress5().isEmpty()) ? "GDPR: data removed" : injury.getAddress5();
                    injuryPostcode = claim.isHashed() && (injury.getPostcode() != null && !injury.getPostcode().isEmpty()) ? "GDPR: data removed" : injury.getPostcode();
                    injuryTelephoneDay = claim.isHashed() && (injury.getTelephoneDay() != null && !injury.getTelephoneDay().isEmpty()) ? "GDPR: data removed" : injury.getTelephoneDay();
                    injuryTelephoneEvening = claim.isHashed() && (injury.getTelephoneEvening() != null && !injury.getTelephoneEvening().isEmpty()) ? "GDPR: data removed" : injury.getTelephoneEvening();
                    injuryEmail = claim.isHashed() && (injury.getEmail() != null && !injury.getEmail().isEmpty()) ? "GDPR: data removed" : injury.getEmail();
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
                    hireMonBookedInDate = DateHelper.getLocalDateFormat().format(hireMonitoringDetail.getRepairBookInDate());
                }
                if (hireMonitoringDetail.getInspectionBookedDate() != null) {
                    hireMonInspectionBookedDate = DateHelper.getLocalDateFormat().format(hireMonitoringDetail.getInspectionBookedDate());
                }
                if (hireMonitoringDetail.getInspectionDate() != null) {
                    hireMonInspectionDate = DateHelper.getLocalDateFormat().format(hireMonitoringDetail.getInspectionDate());
                }
                hireMonTotalLoss = hireMonitoringDetail.getIsTotalLossDesc();
                if (hireMonitoringDetail.getRepairCompletionDate() != null) {
                    hireMonRepairCompletionDate = DateHelper.getLocalDateFormat().format(hireMonitoringDetail.getRepairCompletionDate());
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
                hireVehicleRegistration = claim.isHashed() && (vehicleHire.getVehicleRegistration() != null && !vehicleHire.getVehicleRegistration().isEmpty()) ? "GDPR: data removed" : vehicleHire.getVehicleRegistration();
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
            } else {
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
                invoiceGtaDiscount = invoice.getGtaDiscount();
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
                if (invoiceInterimPaymentAmount == null || invoiceInterimPaymentAmount.compareTo(BigDecimal.ZERO) == 0) {
                    invoiceInterimPayment = "";
                } else if (invoice.getInterimPaymentReceived() != null && invoice.getInterimPaymentReceived().compareTo(invoiceInterimPaymentAmount) >= 0) {
                    invoiceInterimPayment = "£" + invoiceInterimPaymentAmount.toString() + " (Received)";
                } else if (invoice.getInterimPaymentReceived() != null && invoice.getInterimPaymentReceived().compareTo(invoiceInterimPaymentAmount) < 0) {
                    invoiceInterimPayment = "£" + invoiceInterimPaymentAmount.toString() + " (Only £" + invoice.getInterimPaymentReceived() + " Received)";
                } else {
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
                extrasCollaborationFee = invoice.getCollaborationFee();
                extrasCollaborationQuantity = invoice.getCollaborationQty();
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
                extrasVedFee = invoice.getVedFee();
                extrasVedQuantity = invoice.getVedQty();
                extrasAdminFee = invoice.getAdminFee();
                extrasAdminQuantity = invoice.getAdminQty();
                extrasRoofRackFee = invoice.getRoofRackFee();
                extrasRoofRackQuantity = invoice.getRoofRackQty();
                extrasDualControlFee = invoice.getDualControlFee();
                extrasDualControlQuantity = invoice.getDualControlQty();
                extrasDeliveryCollectionFee = invoice.getDeliveryCollectionFee();
                extrasDeliveryCollectionQuantity = invoice.getDeliveryCollectionQty();
                extrasCoverNoteRequired = invoice.getCoverNoteRequiredDesc();

                extrasRepairAdminFee = invoice.getRepairAdminFee();
                extrasRepairAcquisitionFee = invoice.getRepairAcquisitionFee();
                extrasRepairParts = invoice.getRepairParts();
                extrasRepairLabour = invoice.getRepairLabour();
                extrasRepairMaterials = invoice.getRepairMaterials();
                extrasRepairSpecialist = invoice.getRepairSpecialist();

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
                invoicePaymentsTeam = invoice.getPaymentTeamDesc();
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
        } catch (Exception ex) {
            LOG.error("Error creating claim file report for claim '{}':\n", claim.getChoReference(), ex);
            if (ex.getCause() != null) {
                LOG.error("    Caused by: {}", ex.getCause().getMessage(), ex.getCause());
            }
        }
    }

    public String getClaimType() {
        return claimType;
    }

    public String getLiabilityStatus() {
        return liabilityStatus;
    }

    public String getIndemnityStance() {
        return indemnityStance;
    }

    public String getSolicitorEmail() {
        return solicitorEmail;
    }

    public String getWitnessEmail() {
        return witnessEmail;
    }

    public BigDecimal getChoLiabilityAgreed() {
        return choLiabilityAgreed;
    }

    public String getChoName() {
        return choName;
    }

    public String getClaimOwner() {
        return claimOwner;
    }

    public String getContactDate() {
        return contactDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public String getCreditAgreementSignedDate() {
        return creditAgreementSignedDate;
    }

    public String getCustomer() {
        return customer;
    }

    public String getCustomerAddress1() {
        return customerAddress1;
    }

    public String getCustomerAddress2() {
        return customerAddress2;
    }

    public String getCustomerAddress3() {
        return customerAddress3;
    }

    public String getCustomerAddress4() {
        return customerAddress4;
    }

    public String getCustomerAddress5() {
        return customerAddress5;
    }

    public Integer getCustomerAge() {
        return customerAge;
    }

    public String getCustomerClaimNumber() {
        return customerClaimNumber;
    }

    public String getCustomerComprehensive() {
        return customerComprehensive;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public String getCustomerInsurer() {
        return customerInsurer;
    }

    public String getCustomerOccupation() {
        return customerOccupation;
    }

    public String getCustomerPolicyNumber() {
        return customerPolicyNumber;
    }

    public String getCustomerPolicyUsage() {
        return customerPolicyUsage;
    }

    public String getCustomerPostcode() {
        return customerPostcode;
    }

    public String getCustomerSurname() {
        return customerSurname;
    }

    public String getCustomerTelephoneDay() {
        return customerTelephoneDay;
    }

    public String getCustomerTelephoneEvening() {
        return customerTelephoneEvening;
    }

    public String getCustomerTitle() {
        return customerTitle;
    }

    public String getCustomerVRN() {
        return customerVRN;
    }

    public String getCustomerVehicleClass() {
        return customerVehicleClass;
    }

    public String getCustomerVehicleLocation() {
        return customerVehicleLocation;
    }

    public String getCustomerVehicleManufacturer() {
        return customerVehicleManufacturer;
    }

    public String getCustomerVehicleModel() {
        return customerVehicleModel;
    }

    public String getDateLiabilityAgreed() {
        return dateLiabilityAgreed;
    }

    public String getDescription() {
        return description;
    }

    public String getEngReportAddress1() {
        return engReportAddress1;
    }

    public String getEngReportAddress2() {
        return engReportAddress2;
    }

    public String getEngReportAddress3() {
        return engReportAddress3;
    }

    public String getEngReportAddress4() {
        return engReportAddress4;
    }

    public String getEngReportAddress5() {
        return engReportAddress5;
    }

    public String getEngReportCompany() {
        return engReportCompany;
    }

    public String getEngReportEmail() {
        return engReportEmail;
    }

    public Integer getEngReportEstimatedDaysUnderRepair() {
        return engReportEstimatedDaysUnderRepair;
    }

    public BigDecimal getEngReportEstimatedLabourAmount() {
        return engReportEstimatedLabourAmount;
    }

    public BigDecimal getEngReportEstimatedTotalRepairAmount() {
        return engReportEstimatedTotalRepairAmount;
    }

    public String getEngReportName() {
        return engReportName;
    }

    public String getEngReportPostcode() {
        return engReportPostcode;
    }

    public String getEngReportTelephone() {
        return engReportTelephone;
    }

    public String getEngReportUsable() {
        return engReportUsable;
    }

    public BigDecimal getExtrasAdminFee() {
        return extrasAdminFee;
    }

    public Integer getExtrasAdminQuantity() {
        return extrasAdminQuantity;
    }

    public BigDecimal getExtrasAutomaticFee() {
        return extrasAutomaticFee;
    }

    public Integer getExtrasAutomaticQuantity() {
        return extrasAutomaticQuantity;
    }

    public BigDecimal getExtrasBabySeatFee() {
        return extrasBabySeatFee;
    }

    public Integer getExtrasBabySeatQuantity() {
        return extrasBabySeatQuantity;
    }

    public BigDecimal getExtrasMiscellaneousFee() {
        return extrasMiscellaneousFee;
    }

    public BigDecimal getExtrasCollaborationFee() {
        return extrasCollaborationFee;
    }

    public Integer getExtrasCollaborationQuantity() {
        return extrasCollaborationQuantity;
    }

    public String getExtrasMiscellaneousTitle() {
        return extrasMiscellaneousTitle;
    }

    public Integer getExtrasMiscellaneousQuantity() {
        return extrasMiscellaneousQuantity;
    }

    public BigDecimal getExtrasDeliveryCollectionFee() {
        return extrasDeliveryCollectionFee;
    }

    public Integer getExtrasDeliveryCollectionQuantity() {
        return extrasDeliveryCollectionQuantity;
    }

    public BigDecimal getExtrasDualControlFee() {
        return extrasDualControlFee;
    }

    public Integer getExtrasDualControlQuantity() {
        return extrasDualControlQuantity;
    }

    public BigDecimal getExtrasEstateFee() {
        return extrasEstateFee;
    }

    public Integer getExtrasEstateQuantity() {
        return extrasEstateQuantity;
    }

    public BigDecimal getExtrasNSRInsPremiumFee() {
        return extrasNSRInsPremiumFee;
    }

    public Integer getExtrasNSRInsPremiumQuantity() {
        return extrasNSRInsPremiumQuantity;
    }

    public BigDecimal getExtrasRoofRackFee() {
        return extrasRoofRackFee;
    }

    public Integer getExtrasRoofRackQuantity() {
        return extrasRoofRackQuantity;
    }

    public BigDecimal getExtrasSatNavFee() {
        return extrasSatNavFee;
    }

    public Integer getExtrasSatNavQuantity() {
        return extrasSatNavQuantity;
    }

    public BigDecimal getExtrasTowBarFee() {
        return extrasTowBarFee;
    }

    public Integer getExtrasTowBarQuantity() {
        return extrasTowBarQuantity;
    }

    public BigDecimal getExtrasVedFee() {
        return extrasVedFee;
    }

    public Integer getExtrasVedQuantity() {
        return extrasVedQuantity;
    }

    public String getHireMonBookedInDate() {
        return hireMonBookedInDate;
    }

    public String getHireMonIME() {
        return hireMonIME;
    }

    public String getHireMonInspectionBookedDate() {
        return hireMonInspectionBookedDate;
    }

    public String getHireMonInspectionDate() {
        return hireMonInspectionDate;
    }

    public BigDecimal getHireMonLabourHours() {
        return hireMonLabourHours;
    }

    public BigDecimal getHireMonLabourRate() {
        return hireMonLabourRate;
    }

    public String getHireMonNextReviewDate() {
        return hireMonNextReviewDate;
    }

    public String getHireMonNonProvisionReason() {
        return hireMonNonProvisionReason;
    }

    public String getHireMonOriginalECD() {
        return hireMonOriginalECD;
    }

    public String getHireMonRepairCompletionDate() {
        return hireMonRepairCompletionDate;
    }

    public String getHireMonRepairerName() {
        return hireMonRepairerName;
    }

    public BigDecimal getHireMonTotalLabourCost() {
        return hireMonTotalLabourCost;
    }

    public String getHireMonTotalLoss() {
        return hireMonTotalLoss;
    }

    public String getHireVehicleClass() {
        return hireVehicleClass;
    }

    public String getHireVehicleHireEnd() {
        return hireVehicleHireEnd;
    }

    public String getHireVehicleHireStart() {
        return hireVehicleHireStart;
    }

    public String getHireVehicleManufacturer() {
        return hireVehicleManufacturer;
    }

    public String getHireVehicleModel() {
        return hireVehicleModel;
    }

    public Integer getHireVehicleNoHireDays() {
        return hireVehicleNoHireDays;
    }

    public String getHireVehicleReasonForCollection() {
        return hireVehicleReasonForCollection;
    }

    public String getHireVehicleRegistration() {
        return hireVehicleRegistration;
    }

    public BigDecimal getIndemnityValue() {
        return indemnityValue;
    }

    public String getIncidentDate() {
        return incidentDate;
    }

    public String getIncidentDescription() {
        return incidentDescription;
    }

    public String getIncidentLocation() {
        return incidentLocation;
    }

    public String getIncidentPoliceInvolved() {
        return incidentPoliceInvolved;
    }

    public String getInjuryAddress1() {
        return injuryAddress1;
    }

    public String getInjuryAddress2() {
        return injuryAddress2;
    }

    public String getInjuryAddress3() {
        return injuryAddress3;
    }

    public String getInjuryAddress4() {
        return injuryAddress4;
    }

    public String getInjuryAddress5() {
        return injuryAddress5;
    }

    public String getInjuryEmail() {
        return injuryEmail;
    }

    public String getInjuryName() {
        return injuryName;
    }

    public String getInjuryPostcode() {
        return injuryPostcode;
    }

    public String getInjuryTelephoneDay() {
        return injuryTelephoneDay;
    }

    public String getInjuryTelephoneEvening() {
        return injuryTelephoneEvening;
    }

    public String getInsurerClaimNumber() {
        return insurerClaimNumber;
    }

    public BigDecimal getInsurerLiabilityAgreed() {
        return insurerLiabilityAgreed;
    }

    public BigDecimal getInsurerLiabilityApplied() {
        return insurerLiabilityApplied;
    }

    public BigDecimal getInvoiceClaimsHandlingAmount() {
        return invoiceClaimsHandlingAmount;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public BigDecimal getInvoiceDeductionHandlingFee() {
        return invoiceDeductionHandlingFee;
    }

    public BigDecimal getInvoiceInsurerDiscount() {
        return invoiceInsurerDiscount;
    }

    public BigDecimal getInvoiceGtaDiscount() {
        return invoiceGtaDiscount;
    }

    public BigDecimal getInvoiceDiscount() {
        return invoiceDiscount;
    }

    public BigDecimal getInvoiceEngineerFeeGross() {
        return invoiceEngineerFeeGross;
    }

    public BigDecimal getInvoiceEngineerFeeNet() {
        return invoiceEngineerFeeNet;
    }

    public BigDecimal getInvoiceEngineerFeeVat() {
        return invoiceEngineerFeeVat;
    }

    public BigDecimal getInvoiceExcessAmountCollected() {
        return invoiceExcessAmountCollected;
    }

    public BigDecimal getInvoiceFullTotalToPay() {
        return invoiceFullTotalToPay;
    }

    public BigDecimal getInvoiceHireGross() {
        return invoiceHireGross;
    }

    public BigDecimal getInvoiceHireNet() {
        return invoiceHireNet;
    }

    public BigDecimal getInvoiceHireRate() {
        return invoiceHireRate;
    }

    public BigDecimal getInvoiceHireVat() {
        return invoiceHireVat;
    }

    public BigDecimal getInvoiceHirePenaltyChargeAmount() {
        return invoiceHirePenaltyChargeAmount;
    }

    public String getInvoiceHirePenaltyChargePercentage() {
        return invoiceHirePenaltyChargePercentage;
    }

    public BigDecimal getInvoiceRepairPenaltyChargeAmount() {
        return invoiceRepairPenaltyChargeAmount;
    }

    public String getInvoiceRepairPenaltyChargePercentage() {
        return invoiceRepairPenaltyChargePercentage;
    }

    public String getInvoiceHirePenaltyChargePercentageApplied() {
        return invoiceHirePenaltyChargePercentageApplied;
    }

    public String getInvoiceRepairPenaltyChargePercentageApplied() {
        return invoiceRepairPenaltyChargePercentageApplied;
    }

    public BigDecimal getInvoiceTotalPenaltyCharge() {
        return invoiceTotalPenaltyCharge;
    }

    public BigDecimal getInvoiceRepairGross() {
        return invoiceRepairGross;
    }

    public BigDecimal getInvoiceRepairNet() {
        return invoiceRepairNet;
    }

    public BigDecimal getInvoiceRepairVat() {
        return invoiceRepairVat;
    }

    public String getInvoiceReviewRequired() {
        return invoiceReviewRequired;
    }

    public String getInvoiceReviewReason() {
        return invoiceReviewReason;
    }

    public BigDecimal getInvoiceStorageRecoveryGross() {
        return invoiceStorageRecoveryGross;
    }

    public BigDecimal getInvoiceStorageRecoveryNet() {
        return invoiceStorageRecoveryNet;
    }

    public BigDecimal getInvoiceStorageRecoveryVat() {
        return invoiceStorageRecoveryVat;
    }

    public String getInvoiceSupplierClaimInvoiceNo() {
        return invoiceSupplierClaimInvoiceNo;
    }

    public String getInvoiceSupplierClaimsHandlingNo() {
        return invoiceSupplierClaimsHandlingNo;
    }

    public BigDecimal getInvoiceTotalGross() {
        return invoiceTotalGross;
    }

    public BigDecimal getInvoiceTotalNet() {
        return invoiceTotalNet;
    }

    public BigDecimal getInvoiceTotalToPay() {
        return invoiceTotalToPay;
    }

    public BigDecimal getInvoiceTotalVat() {
        return invoiceTotalVat;
    }

    public String getInvoiceUploadedDate() {
        return invoiceUploadedDate;
    }

    public String getPenaltyStartDate() {
        return penaltyStartDate;
    }

    public BigDecimal getInvoiceVATAmountCollected() {
        return invoiceVATAmountCollected;
    }

    public String getManagingRepair() {
        return managingRepair;
    }

    public String getNoticeDate() {
        return noticeDate;
    }

    public String getSolicitorAddress1() {
        return solicitorAddress1;
    }

    public String getSolicitorAddress2() {
        return solicitorAddress2;
    }

    public String getSolicitorAddress3() {
        return solicitorAddress3;
    }

    public String getSolicitorAddress4() {
        return solicitorAddress4;
    }

    public String getSolicitorAddress5() {
        return solicitorAddress5;
    }

    public String getSolicitorName() {
        return solicitorName;
    }

    public String getSolicitorPostcode() {
        return solicitorPostcode;
    }

    public String getSolicitorTelephoneDay() {
        return solicitorTelephoneDay;
    }

    public String getStatus() {
        return status;
    }

    public String getSupplierReference() {
        return supplierReference;
    }

    public String getThirdPartyAddress1() {
        return thirdPartyAddress1;
    }

    public String getThirdPartyAddress2() {
        return thirdPartyAddress2;
    }

    public String getThirdPartyAddress3() {
        return thirdPartyAddress3;
    }

    public String getThirdPartyAddress4() {
        return thirdPartyAddress4;
    }

    public String getThirdPartyAddress5() {
        return thirdPartyAddress5;
    }

    public String getThirdPartyEmail() {
        return thirdPartyEmail;
    }

    public String getThirdPartyFirstName() {
        return thirdPartyFirstName;
    }

    public String getThirdPartyInsurer() {
        return thirdPartyInsurer;
    }

    public String getThirdPartyInsurerBrand() {
        return thirdPartyInsurerBrand;
    }

    public String getThirdPartyPolicyNumber() {
        return thirdPartyPolicyNumber;
    }

    public String getThirdPartyPostcode() {
        return thirdPartyPostcode;
    }

    public String getThirdPartySurname() {
        return thirdPartySurname;
    }

    public String getThirdPartyTelephoneDay() {
        return thirdPartyTelephoneDay;
    }

    public String getThirdPartyTelephoneEvening() {
        return thirdPartyTelephoneEvening;
    }

    public String getThirdPartyTitle() {
        return thirdPartyTitle;
    }

    public String getThirdPartyVRN() {
        return thirdPartyVRN;
    }

    public String getThirdPartyVehicleClass() {
        return thirdPartyVehicleClass;
    }

    public String getThirdPartyVehicleManufacturer() {
        return thirdPartyVehicleManufacturer;
    }

    public String getThirdPartyVehicleModel() {
        return thirdPartyVehicleModel;
    }

    public String getTotalLoss() {
        return totalLoss;
    }

    public String getUsable() {
        return usable;
    }

    public String getWitnessAddress1() {
        return witnessAddress1;
    }

    public String getWitnessAddress2() {
        return witnessAddress2;
    }

    public String getWitnessAddress3() {
        return witnessAddress3;
    }

    public String getWitnessAddress4() {
        return witnessAddress4;
    }

    public String getWitnessAddress5() {
        return witnessAddress5;
    }

    public String getWitnessName() {
        return witnessName;
    }

    public String getWitnessPostcode() {
        return witnessPostcode;
    }

    public String getWitnessTelephoneDay() {
        return witnessTelephoneDay;
    }

    public String getWitnessTelephoneEvening() {
        return witnessTelephoneEvening;
    }

    public String getWorkgroup() {
        return workgroup;
    }

    public String getAverageDailyMileage() {
        return averageDailyMileage;
    }

    public String getCourtesyCarEntitlement() {
        return courtesyCarEntitlement;
    }

    public String getCustomerVehicleYear() {
        return customerVehicleYear;
    }

    public BigDecimal getExtrasAdditionalDriverFee() {
        return extrasAdditionalDriverFee;
    }

    public Integer getExtrasAdditionalDriverQuantity() {
        return extrasAdditionalDriverQuantity;
    }

    public String getExtrasCoverNoteRequired() {
        return extrasCoverNoteRequired;
    }

    public String getHireMonAuthorisedDate() {
        return hireMonAuthorisedDate;
    }

    public String getHireMonCommencedDate() {
        return hireMonCommencedDate;
    }

    public String getHireMonTotalLossAcceptedDate() {
        return hireMonTotalLossAcceptedDate;
    }

    public String getHireMonTotalLossChequeIssuedDate() {
        return hireMonTotalLossChequeIssuedDate;
    }

    public String getHireMonTotalLossChequeReceivedDate() {
        return hireMonTotalLossChequeReceivedDate;
    }

    public String getHireMonTotalLossOfferMadeDate() {
        return hireMonTotalLossOfferMadeDate;
    }

    public BigDecimal getInvoiceTotalLossFeeGross() {
        return invoiceTotalLossFeeGross;
    }

    public BigDecimal getInvoiceTotalLossFeeNet() {
        return invoiceTotalLossFeeNet;
    }

    public BigDecimal getInvoiceTotalLossFeeVat() {
        return invoiceTotalLossFeeVat;
    }

    public String getOtherVehicleAccess() {
        return otherVehicleAccess;
    }

    public String getOtherVehicleType() {
        return otherVehicleType;
    }

    public String getOtherVehicleUsed() {
        return otherVehicleUsed;
    }

    public String getSpecialRequirements() {
        return specialRequirements;
    }

    public String getSpecificVehicleReason() {
        return specificVehicleReason;
    }

    public String getSpecificVehicleRequired() {
        return specificVehicleRequired;
    }

    public String getVehicleTypeRequired() {
        return vehicleTypeRequired;
    }

    public String getInvoiceInterimPayment() {
        return invoiceInterimPayment;
    }

    public BigDecimal getInvoiceInterimPaymentAmount() {
        return invoiceInterimPaymentAmount;
    }

    public String getCustomerHpiVehicleCapacity() {
        return customerHpiVehicleCapacity;
    }

    public String getCustomerHpiVehicleDoorplan() {
        return customerHpiVehicleDoorplan;
    }

    public String getCustomerHpiVehicleManufacturer() {
        return customerHpiVehicleManufacturer;
    }

    public String getCustomerHpiVehicleModel() {
        return customerHpiVehicleModel;
    }

    public String getCustomerHpiVehicleTransmission() {
        return customerHpiVehicleTransmission;
    }

    public String getCustomerHpiVehicleYear() {
        return customerHpiVehicleYear;
    }

    public String getHireVehicleHpiVehicleCapacity() {
        return hireVehicleHpiVehicleCapacity;
    }

    public String getHireVehicleHpiVehicleDoorplan() {
        return hireVehicleHpiVehicleDoorplan;
    }

    public String getHireVehicleHpiVehicleManufacturer() {
        return hireVehicleHpiVehicleManufacturer;
    }

    public String getHireVehicleHpiVehicleModel() {
        return hireVehicleHpiVehicleModel;
    }

    public String getHireVehicleHpiVehicleTransmission() {
        return hireVehicleHpiVehicleTransmission;
    }

    public String getHireVehicleHpiVehicleYear() {
        return hireVehicleHpiVehicleYear;
    }

    public String getCustomerHpiVehicleRegistrationDate() {
        return customerHpiVehicleRegistrationDate;
    }

    public String getHireVehicleHpiVehicleRegistrationDate() {
        return hireVehicleHpiVehicleRegistrationDate;
    }

    public BigDecimal getPaymentDetailsEngineerFeePaid() {
        return paymentDetailsEngineerFeePaid;
    }

    public BigDecimal getPaymentDetailsHirePaid() {
        return paymentDetailsHirePaid;
    }

    public BigDecimal getPaymentDetailsHirePenaltyPaid() {
        return paymentDetailsHirePenaltyPaid;
    }

    public BigDecimal getPaymentDetailsRepairPaid() {
        return paymentDetailsRepairPaid;
    }

    public BigDecimal getPaymentDetailsRepairPenaltyPaid() {
        return paymentDetailsRepairPenaltyPaid;
    }

    public BigDecimal getPaymentDetailsStorageRecoveryPaid() {
        return paymentDetailsStorageRecoveryPaid;
    }

    public BigDecimal getPaymentDetailsTotalLossPaid() {
        return paymentDetailsTotalLossPaid;
    }

    public BigDecimal getPaymentDetailsFinalPayment() {
        return paymentDetailsFinalPayment;
    }

    public BigDecimal getPaymentDetailsChoDiscountFeePaid() {
        return paymentDetailsChoDiscountFeePaid;
    }

    public BigDecimal getPaymentDetailsClaimHandlerChargePaid() {
        return paymentDetailsClaimHandlerChargePaid;
    }

    public BigDecimal getPaymentDetailsDeductionClaimHandlerFeePaid() {
        return paymentDetailsDeductionClaimHandlerFeePaid;
    }

    public BigDecimal getPaymentDetailsInsurerDiscountFeePaid() {
        return paymentDetailsInsurerDiscountFeePaid;
    }

    public String getSupplierClaimOwner() {
        return supplierClaimOwner;
    }

    public String getIsNFInsurerManagingRepair() {
        return isNFInsurerManagingRepair;
    }

    public String getIsRepairOnlyCheck() {
        return isRepairOnlyCheck;
    }

    public BigDecimal getOriginalInvoiceFullTotalToPay() {
        return originalInvoiceFullTotalToPay;
    }

    public BigDecimal getOriginalInvoiceTotalToPay() {
        return originalInvoiceTotalToPay;
    }

    public String getClientVatRegistered() {
        return clientVatRegistered;
    }

    public String getFinalReview() {
        return finalReview;
    }

    public BigDecimal getExtrasRepairAdminFee() {
        return extrasRepairAdminFee;
    }

    public BigDecimal getExtrasRepairAcquisitionFee() {
        return extrasRepairAcquisitionFee;
    }

    public BigDecimal getExtrasRepairParts() {
        return extrasRepairParts;
    }

    public BigDecimal getExtrasRepairLabour() {
        return extrasRepairLabour;
    }

    public BigDecimal getExtrasRepairMaterials() {
        return extrasRepairMaterials;
    }

    public BigDecimal getExtrasRepairSpecialist() {
        return extrasRepairSpecialist;
    }

    public boolean isSubscriberClaim() {
        return subscriberClaim;
    }

    public boolean isFixedFeeClaim() {
        return fixedFeeClaim;
    }

    public boolean isShowRemainingSlaDays() {
        boolean result = false;

        if ((subscriberClaim || fixedFeeClaim)
                && ("ClaimUnacknowledgedUnrouted".equals(status) || "ClaimUnacknowledgedRouted".equals(status)
                || "ClaimPending".equals(status) || "ClaimReferredToEngineer".equals(status)
                || "ClaimUpdatedByEngineer".equals(status) || "ClaimReferredToFNOL".equals(status)
                || "SubscriberClaimRejected".equals(status) || "ClaimRejected".equals(status)
                || "ClaimRejectionContested".equals(status) || "ClaimUnacknowledgedUnassigned".equals(status))
                && isInsurerOrAdmin) {
            result = true;
        }

        return result;
    }

    public boolean isCollaborationClaim() {
        return collaborationClaim;
    }

    public String getInvoicePaymentsTeam() {
        return invoicePaymentsTeam;
    }

    public boolean isPaymentsTeamActivated() {
        return paymentsTeamActivated;
    }

    public String getRemainingSlaDays() {
        return remainingSlaDays;
    }

    public String getFraudScore() {
        return fraudScore;
    }

    public String getFraudStatus() {
        return fraudStatus;
    }

    public boolean isIsInsurer() {
        return isInsurer;
    }

    public boolean isIsInsurerOrAdmin() {
        return isInsurerOrAdmin;
    }

    public boolean isIsInsurerHireMonitoring() {
        return isInsurerHireMonitoring;
    }

    public String getInsurerHireMonBookedInDate() {
        return insurerHireMonBookedInDate;
    }

    public String getInsurerHireMonAuthorisedDate() {
        return insurerHireMonAuthorisedDate;
    }

    public String getInsurerHireMonCommencedDate() {
        return insurerHireMonCommencedDate;
    }

    public String getInsurerHireMonInspectionBookedDate() {
        return insurerHireMonInspectionBookedDate;
    }

    public String getInsurerHireMonInspectionDate() {
        return insurerHireMonInspectionDate;
    }

    public String getInsurerHireMonTotalLossOfferMadeDate() {
        return insurerHireMonTotalLossOfferMadeDate;
    }

    public String getInsurerHireMonTotalLossAcceptedDate() {
        return insurerHireMonTotalLossAcceptedDate;
    }

    public String getInsurerHireMonTotalLossChequeIssuedDate() {
        return insurerHireMonTotalLossChequeIssuedDate;
    }

    public String getInsurerHireMonTotalLossChequeReceivedDate() {
        return insurerHireMonTotalLossChequeReceivedDate;
    }

    public String getInsurerHireMonRepairCompletionDate() {
        return insurerHireMonRepairCompletionDate;
    }

    public BigDecimal getInsurerHireMonLabourRate() {
        return insurerHireMonLabourRate;
    }

    public BigDecimal getInsurerHireMonLabourHours() {
        return insurerHireMonLabourHours;
    }

    public BigDecimal getInsurerHireMonTotalLabourCost() {
        return insurerHireMonTotalLabourCost;
    }

    public String getInsurerHireMonClaimantImpecunious() {
        return insurerHireMonClaimantImpecunious;
    }

    public String getInsurerHireMonWhoManagedRepair() {
        return insurerHireMonWhoManagedRepair;
    }

    public String getInsurerHireMonReplacementVehicleClass() {
        return insurerHireMonReplacementVehicleClass;
    }

    public String getInsurerHireMonHireStart() {
        return insurerHireMonHireStart;
    }

    public boolean isIsCopleyOffer() {
        return isCopleyOffer;
    }

    public boolean isHasReviewReason() {
        return invoiceReviewReasonsEnabled;
    }
    
    public boolean isHasAppliedliability() {
        return getInsurerLiabilityApplied()!= null && getInsurerLiabilityAgreed().compareTo(getInsurerLiabilityApplied()) != 0;
    }

    public String getCopleyOfferMade() {
        return copleyOfferMade;
    }

    public String getCopleyOfferMadeDate() {
        return copleyOfferMadeDate;
    }

    public String getInsurerHireMonTpReportedIncidentToTPIDate() {
        return insurerHireMonTpReportedIncidentToTPIDate;
    }

    public String getInsurerHireMonTotalLossReportSentToUsDate() {
        return insurerHireMonTotalLossReportSentToUsDate;
    }

    public String getInsurerHireMonPartsReceivedDate() {
        return insurerHireMonPartsReceivedDate;
    }

    public String getInsurerHireMonHireEnd() {
        return insurerHireMonHireEnd;
    }

}
