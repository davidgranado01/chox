package idas.chox.core.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.util.TextHelper;

public class Insurer extends Entity implements Serializable {

    static final Logger LOG = LoggerFactory.getLogger(Insurer.class);
    private String name;
    private BigDecimal adminHandlingCharge;
    private boolean status;
    private String address1;
    private String address2;
    private String address3;
    private String address4;
    private String address5;
    private String postcode;
    private String vatNo;
    private String companyNo;
    private String phone;
    private String supportProcedure;
    private boolean workgroupEnable;
    private boolean fnolEnable;
    private boolean supervisorEnable;
    private boolean engineersEnable;
    private AutomaticRoutingStrategy automaticRoutingStrategy = AutomaticRoutingStrategy.NONE;
    private boolean claimOwnershipEnable;
    private boolean claimLocked;
    private boolean onlineSupportEnable;
    private boolean taskManagementEnable;
    private boolean gtaPaymentsTeamEnable;
    private boolean subscriberPaymentsTeamEnable;
    private boolean fixedFeePaymentsTeamEnable;
    private boolean insurerVsInsurerPaymentsTeamEnable;
    private boolean collaborationPaymentsTeamEnable;
    private boolean insurerManualPaymentsTeamEnable;
    private boolean tpiPaymentsTeamEnable;
    private List<VehicleClassCeiling> vehicleClassCeilings;
    private Insurer relatedInsurer;
    private boolean thirdPartyInterventionActivated;
    private String tpiIdentificationString;
    private Workgroup invoiceWorkgroup;
    private WebUser invoiceOwner;
    private String tpiRegexExpression;
    private String gtaRegexExpression;
    private String subscriberRegexExpression;
    private String fixedFeeRegexExpression;
    private String collaborationProtocolRegexExpression;
    private String insurerVsInsurerRegexExpression;
    private String insurerManualRegexExpression;
    private boolean tpiAutoRoutingEnable;
    private boolean gtaAutoRoutingEnable;
    private boolean subscriberAutoRoutingEnable;
    private boolean fixedFeeAutoRoutingEnable;
    private boolean colaborationProtocolAutoRoutingEnable;
    private boolean insurerVsInsurerAutoRoutingEnable;
    private boolean insurerManualAutoRoutingEnable;
    private int forcePasswordChange;
    private int uniquePasswordHistory;
    private boolean insurerDiscountEnable;
    private int minimumPasswordLength;
    private boolean invoiceUploadEnabled;
    private boolean claimUploadEnabled;
    private boolean allowSubscriberClaims;
    private boolean allowFixedFeeClaims;
    private boolean allowCollaborationProtocolClaims;
    private Integer timesInStatusContested;
    private Integer daysBeforeEscalated;
    private boolean enableIPWhitelist;
    private int maxLoginAttempts;
    private int blockTime;
    private boolean disablePrivateNotes;
    private String blockedMessage;
    private Integer ecdIncreaseTriggerPercentage;
    private boolean enableManualInvoiceWorkgroups;
    private boolean enableManualInvoiceOwnership;
    private boolean restrictExport;
    private Branding branding;
    private boolean paymentDisputesEnable;
    private boolean completeRoutingEnable;
    private boolean claimAuditReviewEnable;
    private boolean acceptanceReasonEnable;
    private boolean enableKbbsDashboard;
    private boolean enableLouDates;
    private String kbbsManagerPassword;
    private String kbbsOperativePassword;

    public boolean isEnableLouDates() {
        return enableLouDates;
    }

    public void setEnableLouDates(boolean enableLouDates) {
        this.enableLouDates = enableLouDates;
    }

    public boolean isEnableKbbsDashboard() {
        return enableKbbsDashboard;
    }

    public void setEnableKbbsDashboard(boolean enableKbbsDashboard) {
        this.enableKbbsDashboard = enableKbbsDashboard;
    }

    public String getKbbsManagerPassword() {
        return kbbsManagerPassword;
    }

    public void setKbbsManagerPassword(String kbbsManagerPassword) {
        this.kbbsManagerPassword = kbbsManagerPassword;
    }

    public String getKbbsOperativePassword() {
        return kbbsOperativePassword;
    }

    public void setKbbsOperativePassword(String kbbsOperativePassword) {
        this.kbbsOperativePassword = kbbsOperativePassword;
    }
    
    public Branding getBranding() {
        return branding == null ? Branding.NO_BRANDING : branding;
    }

    public void setBranding(Branding branding) {
        this.branding = branding;
    }

    public String getBlockedMessage() {
        return blockedMessage;
    }

    public void setBlockedMessage(String blockedMessage) {
        this.blockedMessage = blockedMessage;
    }

    public int getMinimumPasswordLength() {
        return minimumPasswordLength;
    }

    public void setMinimumPasswordLength(int minimumPasswordLength) {
        this.minimumPasswordLength = minimumPasswordLength;
    }

    public boolean isInsurerDiscountEnable() {
        return insurerDiscountEnable;
    }

    public void setInsurerDiscountEnable(boolean insurerDiscountEnable) {
        this.insurerDiscountEnable = insurerDiscountEnable;
    }

    public String getTpiRegexExpression() {
        return tpiRegexExpression;
    }

    public void setTpiRegexExpression(String tpiRegexExpression) {
        this.tpiRegexExpression = tpiRegexExpression;
    }

    public int getForcePasswordChange() {
        return forcePasswordChange;
    }

    public void setForcePasswordChange(int forcePasswordChange) {
        this.forcePasswordChange = forcePasswordChange;
    }

    public int getUniquePasswordHistory() {
        return uniquePasswordHistory;
    }

    public void setUniquePasswordHistory(int uniquePasswordHistory) {
        this.uniquePasswordHistory = uniquePasswordHistory;
    }

    public Workgroup getInvoiceWorkgroup() {
        return invoiceWorkgroup;
    }

    public WebUser getInvoiceOwner() {
        return invoiceOwner;
    }

    public void setInvoiceWorkgroup(Workgroup workgroup) {
        this.invoiceWorkgroup = workgroup;
    }

    public void setInvoiceOwner(WebUser webUser) {
        this.invoiceOwner = webUser;
    }

    public String getTpiIdentificationString() {
        return tpiIdentificationString;
    }

    public void setTpiIdentificationString(String tpiIdentificationString) {
        this.tpiIdentificationString = tpiIdentificationString;
    }

    public boolean isThirdPartyInterventionActivated() {
        return thirdPartyInterventionActivated;
    }

    public void setThirdPartyInterventionActivated(boolean thirdPartyIntervention) {
        this.thirdPartyInterventionActivated = thirdPartyIntervention;
    }

    public Insurer getRelatedInsurer() {
        return relatedInsurer;
    }

    public void setRelatedInsurer(Insurer relatedInsurer) {
        this.relatedInsurer = relatedInsurer;
    }

    public Insurer() {
        vehicleClassCeilings = new ArrayList<>();
    }

    public BigDecimal getAdminHandlingCharge() {
        return adminHandlingCharge;
    }

    public void setAdminHandlingCharge(BigDecimal adminHandlingCharge) {
        this.adminHandlingCharge = adminHandlingCharge;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getAddress4() {
        return address4;
    }

    public void setAddress4(String address4) {
        this.address4 = address4;
    }

    public String getAddress5() {
        return address5;
    }

    public void setAddress5(String address5) {
        this.address5 = address5;
    }

    public String getCompanyNo() {
        return companyNo;
    }

    public void setCompanyNo(String companyNo) {
        this.companyNo = companyNo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getVatNo() {
        return vatNo;
    }

    public void setVatNo(String vatNo) {
        this.vatNo = vatNo;
    }

    public boolean isFnolEnable() {
        return fnolEnable;
    }

    public void setFnolEnable(boolean fnolEnable) {
        this.fnolEnable = fnolEnable;
    }

    public boolean isEngineersEnable() {
        return engineersEnable;
    }

    public void setEngineersEnable(boolean engineersEnable) {
        this.engineersEnable = engineersEnable;
    }

    public boolean isWorkgroupEnable() {
        return workgroupEnable;
    }

    public void setWorkgroupEnable(boolean workgroupEnable) {
        this.workgroupEnable = workgroupEnable;
    }

    public List<VehicleClassCeiling> getVehicleClassCeilings() {
        return vehicleClassCeilings;
    }

    public void setVehicleClassCeilings(List<VehicleClassCeiling> vehicleClassCeilings) {
        this.vehicleClassCeilings = vehicleClassCeilings;
    }

    public void addVehicleClassCeiling(VehicleClassCeiling vehicleClassCeiling) {
        if (!this.vehicleClassCeilings.contains(vehicleClassCeiling)) {
            this.vehicleClassCeilings.add(vehicleClassCeiling);
        }
    }

    public boolean isOnlineSupportEnable() {
        return onlineSupportEnable;
    }

    public void setOnlineSupportEnable(boolean onlineSupportEnable) {
        this.onlineSupportEnable = onlineSupportEnable;
    }

    public boolean isClaimOwnershipEnable() {
        return claimOwnershipEnable;
    }

    public void setClaimOwnershipEnable(boolean claimOwnershipEnable) {
        this.claimOwnershipEnable = claimOwnershipEnable;
    }

    public boolean isClaimLocked() {
        return claimLocked;
    }

    public boolean isTaskManagementEnable() {
        return taskManagementEnable;
    }

    public void setTaskManagementEnable(boolean taskManagementEnable) {
        this.taskManagementEnable = taskManagementEnable;
    }

    public void setClaimLocked(boolean claimLocked) {
        this.claimLocked = claimLocked;
    }

    public String getSupportProcedure() {
        return supportProcedure;
    }

    public void setSupportProcedure(String supportProcedure) {
        this.supportProcedure = supportProcedure;
    }

    public String getDisplayAddress() {

        String strDelimiter = ", ";
        StringBuilder sb = new StringBuilder();

        if (TextHelper.isValidText(this.address1)) {
            if (TextHelper.isValidText(sb.toString())) {
                sb.append(strDelimiter);
            }
            sb.append(this.address1);
        }

        if (TextHelper.isValidText(this.address2)) {
            if (TextHelper.isValidText(sb.toString())) {
                sb.append(strDelimiter);
            }
            sb.append(this.address2);
        }

        if (TextHelper.isValidText(this.address3)) {
            if (TextHelper.isValidText(sb.toString())) {
                sb.append(strDelimiter);
            }
            sb.append(this.address3);
        }

        if (TextHelper.isValidText(this.postcode)) {
            if (TextHelper.isValidText(sb.toString())) {
                sb.append(strDelimiter);
            }
            sb.append(this.postcode);
        }


        if (TextHelper.isValidText(sb.toString())) {
            return sb.toString();
        } else {
            return "N/A";
        }
    }


    public boolean isInvoiceUploadEnabled() {
        return invoiceUploadEnabled;
    }

    public void setInvoiceUploadEnabled(boolean invoiceUploadEnabled) {
        this.invoiceUploadEnabled = invoiceUploadEnabled;
    }

    public boolean isClaimUploadEnabled() {
        return claimUploadEnabled;
    }

    public void setClaimUploadEnabled(boolean claimUploadEnabled) {
        this.claimUploadEnabled = claimUploadEnabled;
    }

    public boolean isAllowSubscriberClaims() {
        return allowSubscriberClaims;
    }

    public void setAllowSubscriberClaims(boolean allowSubscriberClaims) {
        this.allowSubscriberClaims = allowSubscriberClaims;
    }

    public boolean isAllowFixedFeeClaims() {
        return allowFixedFeeClaims;
    }

    public void setAllowFixedFeeClaims(boolean allowFixedFeeClaims) {
        this.allowFixedFeeClaims = allowFixedFeeClaims;
    }

    public boolean isAllowCollaborationProtocolClaims() {
        return allowCollaborationProtocolClaims;
    }

    public void setAllowCollaborationProtocolClaims(boolean allowCollaborationProtocolClaims) {
        this.allowCollaborationProtocolClaims = allowCollaborationProtocolClaims;
    }

    public Integer getTimesInStatusContested() {
        return timesInStatusContested;
    }

    public void setTimesInStatusContested(Integer timesInStatusContested) {
        this.timesInStatusContested = timesInStatusContested;
    }

    public Integer getDaysBeforeEscalated() {
        return daysBeforeEscalated;
    }

    public void setDaysBeforeEscalated(Integer daysBeforeEscalated) {
        this.daysBeforeEscalated = daysBeforeEscalated;
    }

    public boolean isEnableIPWhitelist() {
        return enableIPWhitelist;
    }

    public void setEnableIPWhitelist(boolean enableIPWhitelist) {
        this.enableIPWhitelist = enableIPWhitelist;
    }

    public boolean isSupervisorEnable() {
        return supervisorEnable;
    }

    public void setSupervisorEnable(boolean supervisorEnable) {
        this.supervisorEnable = supervisorEnable;
    }

    public int getBlockTime() {
        return blockTime;
    }

    public void setBlockTime(int blockTime) {
        this.blockTime = blockTime;
    }

    public int getMaxLoginAttempts() {
        return maxLoginAttempts;
    }

    public void setMaxLoginAttempts(int maxLoginAttempts) {
        this.maxLoginAttempts = maxLoginAttempts;
    }

    public boolean isDisablePrivateNotes() {
        return disablePrivateNotes;
    }

    public void setDisablePrivateNotes(boolean disablePrivateNotes) {
        this.disablePrivateNotes = disablePrivateNotes;
    }

    public Integer getEcdIncreaseTriggerPercentage() {
        return ecdIncreaseTriggerPercentage;
    }

    public void setEcdIncreaseTriggerPercentage(Integer ecdIncreaseTriggerPercentage) {
        this.ecdIncreaseTriggerPercentage = ecdIncreaseTriggerPercentage;
    }

    public boolean isEnableManualInvoiceWorkgroups() {
        return enableManualInvoiceWorkgroups;
    }

    public void setEnableManualInvoiceWorkgroups(
            boolean enableManualInvoiceWorkgroups) {
        this.enableManualInvoiceWorkgroups = enableManualInvoiceWorkgroups;
    }

    public boolean isEnableManualInvoiceOwnership() {
        return enableManualInvoiceOwnership;
    }

    public void setEnableManualInvoiceOwnership(boolean enableManualInvoiceOwnership) {
        this.enableManualInvoiceOwnership = enableManualInvoiceOwnership;
    }

    public boolean isRestrictExport() {
        return restrictExport;
    }

    public void setRestrictExport(boolean restrictExport) {
        this.restrictExport = restrictExport;
    }

    public String getGtaRegexExpression() {
        return gtaRegexExpression;
    }

    public void setGtaRegexExpression(String gtaRegexExpression) {
        this.gtaRegexExpression = gtaRegexExpression;
    }

    public String getSubscriberRegexExpression() {
        return subscriberRegexExpression;
    }

    public void setSubscriberRegexExpression(String subscriberRegexExpression) {
        this.subscriberRegexExpression = subscriberRegexExpression;
    }

    public String getFixedFeeRegexExpression() {
        return fixedFeeRegexExpression;
    }

    public void setFixedFeeRegexExpression(String fixedFeeRegexExpression) {
        this.fixedFeeRegexExpression = fixedFeeRegexExpression;
    }

    public String getCollaborationProtocolRegexExpression() {
        return collaborationProtocolRegexExpression;
    }

    public void setCollaborationProtocolRegexExpression(String collaborationProtocolRegexExpression) {
        this.collaborationProtocolRegexExpression = collaborationProtocolRegexExpression;
    }

    public String getInsurerVsInsurerRegexExpression() {
        return insurerVsInsurerRegexExpression;
    }

    public void setInsurerVsInsurerRegexExpression(
            String insurerVsInsurerRegexExpression) {
        this.insurerVsInsurerRegexExpression = insurerVsInsurerRegexExpression;
    }

    public String getInsurerManualRegexExpression() {
        return insurerManualRegexExpression;
    }

    public void setInsurerManualRegexExpression(String insurerManualRegexExpression) {
        this.insurerManualRegexExpression = insurerManualRegexExpression;
    }

    public boolean isTpiAutoRoutingEnable() {
        return tpiAutoRoutingEnable;
    }

    public void setTpiAutoRoutingEnable(boolean tpiAutoRoutingEnable) {
        this.tpiAutoRoutingEnable = tpiAutoRoutingEnable;
    }

    public boolean isGtaAutoRoutingEnable() {
        return gtaAutoRoutingEnable;
    }

    public void setGtaAutoRoutingEnable(boolean gtaAutoRoutingEnable) {
        this.gtaAutoRoutingEnable = gtaAutoRoutingEnable;
    }

    public boolean isSubscriberAutoRoutingEnable() {
        return subscriberAutoRoutingEnable;
    }

    public void setSubscriberAutoRoutingEnable(boolean subscriberAutoRoutingEnable) {
        this.subscriberAutoRoutingEnable = subscriberAutoRoutingEnable;
    }

    public boolean isFixedFeeAutoRoutingEnable() {
        return fixedFeeAutoRoutingEnable;
    }

    public void setFixedFeeAutoRoutingEnable(boolean fixedFeeAutoRoutingEnable) {
        this.fixedFeeAutoRoutingEnable = fixedFeeAutoRoutingEnable;
    }

    public boolean isColaborationProtocolAutoRoutingEnable() {
        return colaborationProtocolAutoRoutingEnable;
    }

    public void setColaborationProtocolAutoRoutingEnable(boolean colaborationProtocolAutoRoutingEnable) {
        this.colaborationProtocolAutoRoutingEnable = colaborationProtocolAutoRoutingEnable;
    }

    public boolean isInsurerVsInsurerAutoRoutingEnable() {
        return insurerVsInsurerAutoRoutingEnable;
    }

    public void setInsurerVsInsurerAutoRoutingEnable(
            boolean insurerVsInsurerAutoRoutingEnable) {
        this.insurerVsInsurerAutoRoutingEnable = insurerVsInsurerAutoRoutingEnable;
    }

    public boolean isInsurerManualAutoRoutingEnable() {
        return insurerManualAutoRoutingEnable;
    }

    public void setInsurerManualAutoRoutingEnable(
            boolean insurerManualAutoRoutingEnable) {
        this.insurerManualAutoRoutingEnable = insurerManualAutoRoutingEnable;
    }

    public boolean isGtaPaymentsTeamEnable() {
        return gtaPaymentsTeamEnable;
    }

    public void setGtaPaymentsTeamEnable(boolean gtaPaymentsTeamEnable) {
        this.gtaPaymentsTeamEnable = gtaPaymentsTeamEnable;
    }

    public boolean isSubscriberPaymentsTeamEnable() {
        return subscriberPaymentsTeamEnable;
    }

    public void setSubscriberPaymentsTeamEnable(boolean subscriberPaymentsTeamEnable) {
        this.subscriberPaymentsTeamEnable = subscriberPaymentsTeamEnable;
    }

    public boolean isFixedFeePaymentsTeamEnable() {
        return fixedFeePaymentsTeamEnable;
    }

    public void setFixedFeePaymentsTeamEnable(boolean fixedFeePaymentsTeamEnable) {
        this.fixedFeePaymentsTeamEnable = fixedFeePaymentsTeamEnable;
    }

    public boolean isInsurerVsInsurerPaymentsTeamEnable() {
        return insurerVsInsurerPaymentsTeamEnable;
    }

    public void setInsurerVsInsurerPaymentsTeamEnable(boolean insurerVsInsurerPaymentsTeamEnable) {
        this.insurerVsInsurerPaymentsTeamEnable = insurerVsInsurerPaymentsTeamEnable;
    }

    public boolean isCollaborationPaymentsTeamEnable() {
        return collaborationPaymentsTeamEnable;
    }

    public void setCollaborationPaymentsTeamEnable(boolean collaborationPaymentsTeamEnable) {
        this.collaborationPaymentsTeamEnable = collaborationPaymentsTeamEnable;
    }

    public boolean isInsurerManualPaymentsTeamEnable() {
        return insurerManualPaymentsTeamEnable;
    }

    public boolean isPaymentsTeamEnable() {
        return gtaPaymentsTeamEnable || subscriberPaymentsTeamEnable || fixedFeePaymentsTeamEnable || tpiPaymentsTeamEnable
                || insurerVsInsurerPaymentsTeamEnable || collaborationPaymentsTeamEnable || insurerManualPaymentsTeamEnable;
    }

    public void setInsurerManualPaymentsTeamEnable(boolean insurerManualPaymentsTeamEnable) {
        this.insurerManualPaymentsTeamEnable = insurerManualPaymentsTeamEnable;
    }

    public boolean isTpiPaymentsTeamEnable() {
        return tpiPaymentsTeamEnable;
    }

    public void setTpiPaymentsTeamEnable(boolean tpiPaymentsTeamEnable) {
        this.tpiPaymentsTeamEnable = tpiPaymentsTeamEnable;
    }

    public boolean isPaymentDisputesEnable() {
        return paymentDisputesEnable;
    }

    public void setPaymentDisputesEnable(boolean paymentDisputesEnable) {
        this.paymentDisputesEnable = paymentDisputesEnable;
    }

    public AutomaticRoutingStrategy getAutomaticRoutingStrategy() {
        return automaticRoutingStrategy;
    }

    public void setAutomaticRoutingStrategy(AutomaticRoutingStrategy automaticRoutingStrategy) {
        this.automaticRoutingStrategy = automaticRoutingStrategy;
    }

    public boolean isCompleteRoutingEnable() {
        return completeRoutingEnable;
    }

    public void setCompleteRoutingEnable(boolean completeRoutingEnable) {
        this.completeRoutingEnable = completeRoutingEnable;
    }

    public boolean isClaimAuditReviewEnable() {
        return claimAuditReviewEnable;
    }

    public void setClaimAuditReviewEnable(boolean claimAuditReviewEnable) {
        this.claimAuditReviewEnable = claimAuditReviewEnable;
    }

    public boolean isAcceptanceReasonEnable() {
        return acceptanceReasonEnable;
    }

    public void setAcceptanceReasonEnable(boolean acceptanceReasonEnable) {
        this.acceptanceReasonEnable = acceptanceReasonEnable;
    }

}
