package idas.chox.web;

import idas.chox.core.model.Claim;
import idas.chox.core.model.LiabilityStatus;
import java.math.BigDecimal;
import java.util.Date;


public class ExcelClaim {
    private String claimStatus;
    private String claimChoReference;
    private String claimChorganisationName;
    private String claimWorkgroupName;
    private Date claimStatusModifiedDate;
    private BigDecimal claimIdemnity;
    private String claimLiabilityStatus;
    private BigDecimal claimPercentageLiabilityAccepted;
    private BigDecimal claimPercentageLiabilityCho;
    private String claimManagingRepair;
    private Date claimPolicyHolderContactDate;
    private Date claimCreditAgreementDate;
    private Date claimGtaNoticeDate;
    private String claimClaimNumber;
    private String claimClaimOwnerDisplayName;
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
    private String claimthirdPartyVehicleRegistration;
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
    private BigDecimal claimHireMonitoringDetailLabourCost;
    private String claimHireMonitoringDetailNonProvisionReason;
    private Date claimHireMonitoringDetailNextReviewDate;




    public void setClaim(Claim claim) {
        this.claimStatus = claim.getStatus();
        this.claimChoReference = claim.getChoReference();
        this.claimChorganisationName = claim.getChorganisation().getName();
        if (claim.getWorkgroup() != null)
            this.claimWorkgroupName = claim.getWorkgroup().getName();
        else
            this.claimWorkgroupName = "";
        this.claimStatusModifiedDate = claim.getStatusModifiedDate();
        this.claimIdemnity = claim.getIndemnityAmount();
        if(claim.getLiabilityStatus()!=LiabilityStatus.LIABILITY_NULL)
            this.claimLiabilityStatus = claim.getLiabilityStatus().toString();
        else
            this.claimLiabilityStatus = "";
        if(claim.getPercentageLiabilityAccepted()!=null)
            this.claimPercentageLiabilityAccepted = claim.getPercentageLiabilityAccepted();
        else
            this.claimPercentageLiabilityAccepted = new BigDecimal(0);
        if(claim.getPercentageLiabilityCho()!=null)
            this.claimPercentageLiabilityCho = claim.getPercentageLiabilityCho();
        else
            this.claimPercentageLiabilityCho = new BigDecimal(0);
        this.claimManagingRepair = claim.getIsManagingRepairDesc();
        this.claimPolicyHolderContactDate = claim.getPolicyHolderContactDate();
        this.claimCreditAgreementDate = claim.getCreditAgreementDate();
        this.claimGtaNoticeDate = claim.getGtaNoticeDate();
        this.claimClaimNumber = claim.getClaimNumber();
        if (claim.getClaimOwner() != null)
            this.claimClaimOwnerDisplayName = claim.getClaimOwner().getDisplayName();
        else
            this.claimClaimOwnerDisplayName = "";
        if (claim.getCustomer() != null) {
            this.claimCustomerTitle = claim.getCustomer().getTitle();
            this.claimCustomerFirstName = claim.getCustomer().getFirstName();
            this.claimCustomerLastName = claim.getCustomer().getLastName();
            this.claimCustomerAddress1 = claim.getCustomer().getAddress1();
            this.claimCustomerAddress2 = claim.getCustomer().getAddress2();
            this.claimCustomerAddress3 = claim.getCustomer().getAddress3();
            this.claimCustomerAddress4 = claim.getCustomer().getAddress4();
            this.claimCustomerAddress5 = claim.getCustomer().getAddress5();
            this.claimCustomerPostcode = claim.getCustomer().getPostcode();
            this.claimCustomerTelephoneDay = claim.getCustomer().getTelephoneDay();
            this.claimCustomerTelephoneEvening = claim.getCustomer().getTelephoneEvening();
            this.claimCustomerEmail = claim.getCustomer().getEmail();
            this.claimCustomerInsurerName = claim.getCustomer().getInsurerName();
            this.claimCustomerPolicyNumber = claim.getCustomer().getPolicyNumber();
            this.claimCustomerClaimReference = claim.getCustomer().getClaimReference();
            this.claimCustomerComprehensive = claim.getCustomer().getIsComprehensiveDesc();
            this.claimCustomerVehicleManufacturer = claim.getCustomer().getVehicleManufacturer();
            this.claimCustomerVehicleModel = claim.getCustomer().getVehicleModel();
            this.claimCustomerVehicleRegistration = claim.getCustomer().getVehicleRegistration();
            this.claimCustomerVehicleYear = claim.getCustomer().getVehicleYear();
            if (claim.getCustomer().getVehicleClass() != null)
                this.claimCustomerVehicleClassName = claim.getCustomer().getVehicleClass().getName();
            else
                this.claimCustomerVehicleClassName = "";
            this.claimCustomerLocation = claim.getCustomer().getLocation();
            this.claimCustomerHpiVehicleManufacturer = claim.getCustomer().getHpiVehicleManufacturer();
            this.claimCustomerHpiVehicleModel = claim.getCustomer().getHpiVehicleModel();
            this.claimCustomerHpiVehicleYear = claim.getCustomer().getHpiVehicleYear();
            this.claimCustomerHpiFirstRegistration = claim.getCustomer().getHpiFirstRegistration();
            this.claimCustomerHpiVehicleCapacity = claim.getCustomer().getHpiVehicleCapacity();
            this.claimCustomerHpiVehicleDoorplan = claim.getCustomer().getHpiVehicleDoorplan();
            this.claimCustomerHpiVehicleTransmission = claim.getCustomer().getHpiVehicleTransmission();
            this.claimCustomerCanAccessOtherVehicleDesc = claim.getCustomer().getCanAccessOtherVehicleDesc();
            this.claimCustomerOtherVehicleUsedDesc = claim.getCustomer().getOtherVehicleUsedDesc();
            this.claimCustomerOtherVehicle = claim.getCustomer().getOtherVehicle();
            this.claimCustomerCourtesyCarEntitledDesc = claim.getCustomer().getCourtesyCarEntitledDesc();
            this.claimCustomerSpecificVehicleRequiredDesc = claim.getCustomer().getSpecificVehicleRequiredDesc();
            this.claimCustomerSpecificVehicleReason = claim.getCustomer().getSpecificVehicleReason();
            this.claimCustomerTypeVehicleRequired = claim.getCustomer().getTypeVehicleRequired();
            this.claimCustomerSpecialRequirements = claim.getCustomer().getSpecialRequirements();
            this.claimCustomerAverageDailyMileage = claim.getCustomer().getAverageDailyMileage();
            this.claimCustomerDamage = claim.getCustomer().getDamage();
            this.claimCustomerIsUsable = claim.getCustomer().getIsUsableDesc();
            this.claimCustomerIsTotalLoss = claim.getCustomer().getIsTotalLossDesc();
            this.claimCustomerInitialECD = claim.getCustomer().getInitialECD();
        }
        else {
            this.claimCustomerTitle = "";
            this.claimCustomerFirstName = "";
            this.claimCustomerLastName = "";
            this.claimCustomerAddress1 = "";
            this.claimCustomerAddress2 = "";
            this.claimCustomerAddress3 = "";
            this.claimCustomerAddress4 = "";
            this.claimCustomerAddress5 = "";
            this.claimCustomerPostcode = "";
            this.claimCustomerTelephoneDay = "";
            this.claimCustomerTelephoneEvening = "";
            this.claimCustomerEmail = "";
            this.claimCustomerInsurerName = "";
            this.claimCustomerPolicyNumber = "";
            this.claimCustomerClaimReference = "";
            this.claimCustomerComprehensive = "";
            this.claimCustomerVehicleManufacturer = "";
            this.claimCustomerVehicleModel = "";
            this.claimCustomerVehicleRegistration = "";
            this.claimCustomerVehicleYear = "";
            this.claimCustomerVehicleClassName = "";
            this.claimCustomerLocation = "";
            this.claimCustomerHpiVehicleManufacturer = "";
            this.claimCustomerHpiVehicleModel = "";
            this.claimCustomerHpiVehicleYear = "";
            this.claimCustomerHpiFirstRegistration = null;
            this.claimCustomerHpiVehicleCapacity = "";
            this.claimCustomerHpiVehicleDoorplan = "";
            this.claimCustomerHpiVehicleTransmission = "";
            this.claimCustomerCanAccessOtherVehicleDesc = "";
            this.claimCustomerOtherVehicleUsedDesc = "";
            this.claimCustomerOtherVehicle = "";
            this.claimCustomerCourtesyCarEntitledDesc = "";
            this.claimCustomerSpecificVehicleRequiredDesc = "";
            this.claimCustomerSpecificVehicleReason = "";
            this.claimCustomerTypeVehicleRequired = "";
            this.claimCustomerSpecialRequirements = "";
            this.claimCustomerAverageDailyMileage = "";
            this.claimCustomerDamage = "";
            this.claimCustomerIsUsable = "";
            this.claimCustomerIsTotalLoss = "";
            this.claimCustomerInitialECD = null;
        }
        if (claim.getThirdParty() != null) {
            this.claimThirdPartyTitle = claim.getThirdParty().getTitle();
            this.claimThirdPartyFirstName = claim.getThirdParty().getFirstName();
            this.claimThirdPartyLastName = claim.getThirdParty().getLastName();
            this.claimThirdPartyAddress1 = claim.getThirdParty().getAddress1();
            this.claimThirdPartyAddress2 = claim.getThirdParty().getAddress2();
            this.claimThirdPartyAddress3 = claim.getThirdParty().getAddress3();
            this.claimThirdPartyAddress4 = claim.getThirdParty().getAddress4();
            this.claimThirdPartyAddress5 = claim.getThirdParty().getAddress5();
            this.claimThirdPartyPostcode = claim.getThirdParty().getPostcode();
            this.claimThirdPartyTelephoneDay = claim.getThirdParty().getTelephoneDay();
            this.claimThirdPartyTelephoneEvening = claim.getThirdParty().getTelephoneEvening();
            this.claimThirdPartyEmail = claim.getThirdParty().getEmail();
            if (claim.getThirdParty().getInsurer() != null)
                this.claimThirdPartyInsurerName = claim.getThirdParty().getInsurer().getName();
            else
                this.claimThirdPartyInsurerName = "";
            this.claimThirdPartyPolicyNumber = claim.getThirdParty().getPolicyNumber();
            this.claimThirdPartyVehicleManufacturer = claim.getThirdParty().getVehicleManufacturer();
            this.claimThirdPartyVehicleModel = claim.getThirdParty().getVehicleModel();
            this.claimthirdPartyVehicleRegistration = claim.getThirdParty().getVehicleRegistration();
            if (claim.getThirdParty().getVehicleClass() != null)
                this.claimThirdPartyVehicleClassName = claim.getThirdParty().getVehicleClass().getName();
            else
                this.claimThirdPartyVehicleClassName = "";
        }
        else {
            this.claimThirdPartyTitle = "";
            this.claimThirdPartyFirstName = "";
            this.claimThirdPartyLastName = "";
            this.claimThirdPartyAddress1 = "";
            this.claimThirdPartyAddress2 = "";
            this.claimThirdPartyAddress3 = "";
            this.claimThirdPartyAddress4 = "";
            this.claimThirdPartyAddress5 = "";
            this.claimThirdPartyPostcode = "";
            this.claimThirdPartyTelephoneDay = "";
            this.claimThirdPartyTelephoneEvening = "";
            this.claimThirdPartyEmail = "";
            this.claimThirdPartyInsurerName = "";
            this.claimThirdPartyPolicyNumber = "";
            this.claimThirdPartyVehicleManufacturer = "";
            this.claimThirdPartyVehicleModel = "";
            this.claimthirdPartyVehicleRegistration = "";
            this.claimThirdPartyVehicleClassName = "";
        }
        if (claim.getIncident() != null) {
            this.claimIncidentDate = claim.getIncident().getDate();
            this.claimIncidentLocation = claim.getIncident().getLocation();
            this.claimIncidentIsPoliceInvolved = claim.getIncident().getIsPoliceInvolvedDesc();
            this.claimIncidentIncidentDescription = claim.getIncident().getIncidentDescription();
            if (claim.getIncident().getWitness() != null) {
                this.claimWitnessName = claim.getIncident().getWitness().getName();
                this.claimWitnessAddress1 = claim.getIncident().getWitness().getAddress1();
                this.claimWitnessAddress2 = claim.getIncident().getWitness().getAddress2();
                this.claimWitnessAddress3 = claim.getIncident().getWitness().getAddress3();
                this.claimWitnessAddress4 = claim.getIncident().getWitness().getAddress4();
                this.claimWitnessAddress5 = claim.getIncident().getWitness().getAddress5();
                this.claimWitnessPostcode = claim.getIncident().getWitness().getPostcode();
                this.claimWitnessTelephoneDay = claim.getIncident().getWitness().getTelephoneDay();
                this.claimWitnessTelephoneEvening = claim.getIncident().getWitness().getTelephoneEvening();
                this.claimWitnessEmail = claim.getIncident().getWitness().getEmail();
            }
            else {
                this.claimWitnessName = "";
                this.claimWitnessAddress1 = "";
                this.claimWitnessAddress2 = "";
                this.claimWitnessAddress3 = "";
                this.claimWitnessAddress4 = "";
                this.claimWitnessAddress5 = "";
                this.claimWitnessPostcode = "";
                this.claimWitnessTelephoneDay = "";
                this.claimWitnessTelephoneEvening = "";
                this.claimWitnessEmail = "";
            }
            
            if (claim.getIncident().getInjury() != null) {
                this.claimInjuryName = claim.getIncident().getInjury().getName();
                this.claimInjuryAddress1 = claim.getIncident().getInjury().getAddress1();
                this.claimInjuryAddress2 = claim.getIncident().getInjury().getAddress2();
                this.claimInjuryAddress3 = claim.getIncident().getInjury().getAddress3();
                this.claimInjuryAddress4 = claim.getIncident().getInjury().getAddress4();
                this.claimInjuryAddress5 = claim.getIncident().getInjury().getAddress5();
                this.claimInjuryPostcode = claim.getIncident().getInjury().getPostcode();
                this.claimInjuryTelephoneDay = claim.getIncident().getInjury().getTelephoneDay();
                this.claimInjuryTelephoneEvening = claim.getIncident().getInjury().getTelephoneEvening();
                this.claimInjuryEmail = claim.getIncident().getInjury().getEmail();
                if (claim.getIncident().getInjury().getSolicitor() != null) {
                    this.claimSolicitorName = claim.getIncident().getInjury().getSolicitor().getName();
                    this.claimSolicitorAddress1 = claim.getIncident().getInjury().getSolicitor().getAddress1();
                    this.claimSolicitorAddress2 = claim.getIncident().getInjury().getSolicitor().getAddress2();
                    this.claimSolicitorAddress3 = claim.getIncident().getInjury().getSolicitor().getAddress3();
                    this.claimSolicitorAddress4 = claim.getIncident().getInjury().getSolicitor().getAddress4();
                    this.claimSolicitorAddress5 = claim.getIncident().getInjury().getSolicitor().getAddress5();
                    this.claimSolicitorPostcode = claim.getIncident().getInjury().getSolicitor().getPostcode();
                    this.claimSolicitorTelephone = claim.getIncident().getInjury().getSolicitor().getTelephone();
                    this.claimSolicitorEmail = claim.getIncident().getInjury().getSolicitor().getEmail();
                }
                else {
                    this.claimSolicitorName = "";
                    this.claimSolicitorAddress1 = "";
                    this.claimSolicitorAddress2 = "";
                    this.claimSolicitorAddress3 = "";
                    this.claimSolicitorAddress4 = "";
                    this.claimSolicitorAddress5 = "";
                    this.claimSolicitorPostcode = "";
                    this.claimSolicitorTelephone = "";
                    this.claimSolicitorEmail = "";

                }
            }
            else {
                this.claimInjuryName = "";
                this.claimInjuryAddress1 = "";
                this.claimInjuryAddress2 = "";
                this.claimInjuryAddress3 = "";
                this.claimInjuryAddress4 = "";
                this.claimInjuryAddress5 = "";
                this.claimInjuryPostcode = "";
                this.claimInjuryTelephoneDay = "";
                this.claimInjuryTelephoneEvening = "";
                this.claimInjuryEmail = "";
                this.claimSolicitorName = "";
                this.claimSolicitorAddress1 = "";
                this.claimSolicitorAddress2 = "";
                this.claimSolicitorAddress3 = "";
                this.claimSolicitorAddress4 = "";
                this.claimSolicitorAddress5 = "";
                this.claimSolicitorPostcode = "";
                this.claimSolicitorTelephone = "";
                this.claimSolicitorEmail = "";
            }
        }
        else {
            this.claimIncidentDate = null;
            this.claimIncidentLocation = "";
            this.claimIncidentIsPoliceInvolved = "";
            this.claimIncidentIncidentDescription = "";
            this.claimWitnessName = "";
            this.claimWitnessAddress1 = "";
            this.claimWitnessAddress2 = "";
            this.claimWitnessAddress3 = "";
            this.claimWitnessAddress4 = "";
            this.claimWitnessAddress5 = "";
            this.claimWitnessPostcode = "";
            this.claimWitnessTelephoneDay = "";
            this.claimWitnessTelephoneEvening = "";
            this.claimWitnessEmail = "";
            this.claimInjuryName = "";
            this.claimInjuryAddress1 = "";
            this.claimInjuryAddress2 = "";
            this.claimInjuryAddress3 = "";
            this.claimInjuryAddress4 = "";
            this.claimInjuryAddress5 = "";
            this.claimInjuryPostcode = "";
            this.claimInjuryTelephoneDay = "";
            this.claimInjuryTelephoneEvening = "";
            this.claimInjuryEmail = "";
            this.claimSolicitorName = "";
            this.claimSolicitorAddress1 = "";
            this.claimSolicitorAddress2 = "";
            this.claimSolicitorAddress3 = "";
            this.claimSolicitorAddress4 = "";
            this.claimSolicitorAddress5 = "";
            this.claimSolicitorPostcode = "";
            this.claimSolicitorTelephone = "";
            this.claimSolicitorEmail = "";
        }

        if (claim.getEngineerReport() != null) {
            this.claimEngineerReportLabourAmount = claim.getEngineerReport().getEstimatedLabourAmount();
            this.claimEngineerReportRepairAmount = claim.getEngineerReport().getEstimatedTotalRepairAmount();
            this.claimEngineerReportDays = claim.getEngineerReport().getDays();
            this.claimEngineerReportIsUsable = claim.getEngineerReport().getIsUsableDesc();
            this.claimEngineerReportName = claim.getEngineerReport().getName();
            this.claimEngineerReportCompany = claim.getEngineerReport().getCompany();
            this.claimEngineerReportAddress1 = claim.getEngineerReport().getAddress1();
            this.claimEngineerReportAddress2 = claim.getEngineerReport().getAddress2();
            this.claimEngineerReportAddress3 = claim.getEngineerReport().getAddress3();
            this.claimEngineerReportAddress4 = claim.getEngineerReport().getAddress4();
            this.claimEngineerReportAddress5 = claim.getEngineerReport().getAddress5();
            this.claimEngineerReportPostcode = claim.getEngineerReport().getPostcode();
            this.claimEngineerReportTelephone = claim.getEngineerReport().getTelephone();
            this.claimEngineerReportEmail = claim.getEngineerReport().getEmail();
        }
        else {
            this.claimEngineerReportLabourAmount = null;
            this.claimEngineerReportRepairAmount = null;
            this.claimEngineerReportDays = null;
            this.claimEngineerReportIsUsable = "";
            this.claimEngineerReportName = "";
            this.claimEngineerReportCompany = "";
            this.claimEngineerReportAddress1 = "";
            this.claimEngineerReportAddress2 = "";
            this.claimEngineerReportAddress3 = "";
            this.claimEngineerReportAddress4 = "";
            this.claimEngineerReportAddress5 = "";
            this.claimEngineerReportPostcode = "";
            this.claimEngineerReportTelephone = "";
            this.claimEngineerReportEmail = "";
        }

        if (claim.getVehicleHire() != null) {
            this.claimVehicleHireVehicleManufacturer = claim.getVehicleHire().getHpiVehicleManufacturer();
            this.claimVehicleHireVehicleModel = claim.getVehicleHire().getVehicleModel();
            this.claimVehicleHireVehicleRegistration = claim.getVehicleHire().getVehicleRegistration();
            if (claim.getVehicleHire().getVehicleClass() != null)
                this.claimVehicleHireVehicleClassName = claim.getVehicleHire().getVehicleClass().getName();
            else
                this.claimVehicleHireVehicleClassName = "";
            this.claimVehicleHireRentalStart = claim.getVehicleHire().getRentalStart();
            this.claimVehicleHireRentalEnd = claim.getVehicleHire().getRentalEnd();
            this.claimVehicleHireDays = claim.getVehicleHire().getDays();
            this.claimVehicleHireCollectionReason = claim.getVehicleHire().getCollectionReason();
            this.claimVehicleHireHpiVehicleManufacturer = claim.getVehicleHire().getHpiVehicleManufacturer();
            this.claimVehicleHireHpiVehicleModel = claim.getVehicleHire().getHpiVehicleModel();
            this.claimVehicleHireHpiVehicleYear = claim.getVehicleHire().getHpiVehicleYear();
            this.claimVehicleHireHpiFirstRegistration = claim.getVehicleHire().getHpiFirstRegistration();
            this.claimVehicleHireHpiVehicleCapacity = claim.getVehicleHire().getHpiVehicleCapacity();
            this.claimVehicleHireHpiVehicleDoorplan = claim.getVehicleHire().getHpiVehicleDoorplan();
            this.claimVehicleHireHpiVehicleTransmission = claim.getVehicleHire().getHpiVehicleTransmission();
        }
        else {
            this.claimVehicleHireVehicleManufacturer = "";
            this.claimVehicleHireVehicleModel = "";
            this.claimVehicleHireVehicleRegistration = "";
            this.claimVehicleHireVehicleClassName = "";
            this.claimVehicleHireRentalStart = null;
            this.claimVehicleHireRentalEnd = null;
            this.claimVehicleHireDays = null;
            this.claimVehicleHireCollectionReason = "";
            this.claimVehicleHireHpiVehicleManufacturer = "";
            this.claimVehicleHireHpiVehicleModel = "";
            this.claimVehicleHireHpiVehicleYear = "";
            this.claimVehicleHireHpiFirstRegistration = null;
            this.claimVehicleHireHpiVehicleCapacity = "";
            this.claimVehicleHireHpiVehicleDoorplan = "";
            this.claimVehicleHireHpiVehicleTransmission = "";
        }
        
        if (claim.getHireMonitoringDetail() != null) {
            this.claimHireMonitoringDetailNameOfRepairer = claim.getHireMonitoringDetail().getNameOfRepairer();
            this.claimHireMonitoringDetailRepairBookInDate = claim.getHireMonitoringDetail().getRepairBookInDate();
            this.claimHireMonitoringDetailRepairAuthorisedDate = claim.getHireMonitoringDetail().getRepairAuthorisedDate();
            this.claimHireMonitoringDetailRepairCommencedDate = claim.getHireMonitoringDetail().getRepairCommencedDate();
            this.claimHireMonitoringDetailInspectionBookedDate = claim.getHireMonitoringDetail().getInspectionBookedDate();
            this.claimHireMonitoringDetailInspectionDate = claim.getHireMonitoringDetail().getInspectionDate();
            this.claimHireMonitoringDetailNameOfIme = claim.getHireMonitoringDetail().getNameOfIme();
            this.claimHireMonitoringDetailRepairCompletionDate = claim.getHireMonitoringDetail().getRepairCompletionDate();
            this.claimHireMonitoringDetailIsTotalLostCheck = claim.getHireMonitoringDetail().getIsTotalLossDesc();
            this.claimHireMonitoringDetailTotalLossOfferMadeDate = claim.getHireMonitoringDetail().getTotalLossOfferMadeDate();
            this.claimHireMonitoringDetailTotalLossOfferAcceptedDate = claim.getHireMonitoringDetail().getTotalLossOfferAcceptedDate();
            this.claimHireMonitoringDetailTotalLossOfferCheckIssuedDate = claim.getHireMonitoringDetail().getTotalLossOfferCheckIssuedDate();
            this.claimHireMonitoringDetailTotalLossOfferCheckReceivedDate = claim.getHireMonitoringDetail().getTotalLossOfferCheckReceivedDate();
            this.claimHireMonitoringDetailLabourRate = claim.getHireMonitoringDetail().getLabourRate();
            this.claimHireMonitoringDetailLabourHour = claim.getHireMonitoringDetail().getLabourHour();
            this.claimHireMonitoringDetailLabourCost = claim.getHireMonitoringDetail().getLabourCost();
            this.claimHireMonitoringDetailNonProvisionReason = claim.getHireMonitoringDetail().getNonProvisionReason();
            this.claimHireMonitoringDetailNextReviewDate = claim.getHireMonitoringDetail().getNextReviewDate();
        }
        else {
            this.claimHireMonitoringDetailNameOfRepairer = "";
            this.claimHireMonitoringDetailRepairBookInDate = null;
            this.claimHireMonitoringDetailRepairAuthorisedDate = null;
            this.claimHireMonitoringDetailRepairCommencedDate = null;
            this.claimHireMonitoringDetailInspectionBookedDate = null;
            this.claimHireMonitoringDetailInspectionDate = null;
            this.claimHireMonitoringDetailNameOfIme = "";
            this.claimHireMonitoringDetailRepairCompletionDate = null;
            this.claimHireMonitoringDetailIsTotalLostCheck = "";
            this.claimHireMonitoringDetailTotalLossOfferMadeDate = null;
            this.claimHireMonitoringDetailTotalLossOfferAcceptedDate = null;
            this.claimHireMonitoringDetailTotalLossOfferCheckIssuedDate = null;
            this.claimHireMonitoringDetailTotalLossOfferCheckReceivedDate = null;
            this.claimHireMonitoringDetailLabourRate = null;
            this.claimHireMonitoringDetailLabourHour = null;
            this.claimHireMonitoringDetailLabourCost = null;
            this.claimHireMonitoringDetailNonProvisionReason = "";
            this.claimHireMonitoringDetailNextReviewDate = null;
        }
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

    public String getClaimManagingRepair() {
        return claimManagingRepair;
    }

    public BigDecimal getClaimPercentageLiabilityAccepted() {
        return claimPercentageLiabilityAccepted;
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

    public String getClaimthirdPartyVehicleRegistration() {
        return claimthirdPartyVehicleRegistration;
    }
}
