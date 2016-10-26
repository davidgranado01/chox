package idas.chox.core.model;

import java.io.Serializable;

import idas.chox.core.util.TextHelper;

public class Chorganisation extends Entity implements Serializable {

    /**
     * This attribute maps to the column name in the chorganisation table.
     */
    private String name;
    /**
     * This attribute maps to the column address1 in the chorganisation table.
     */
    private String address1;
    /**
     * This attribute maps to the column address2 in the chorganisation table.
     */
    private String address2;
    /**
     * This attribute maps to the column address3 in the chorganisation table.
     */
    private String address3;
    /**
     * This attribute maps to the column address4 in the chorganisation table.
     */
    protected String address4;
    /**
     * This attribute maps to the column address5 in the chorganisation table.
     */
    private String address5;
    /**
     * This attribute maps to the column postcode in the chorganisation table.
     */
    private String postcode;
    /**
     * This attribute maps to the column vat_no in the chorganisation table.
     */
    private String vatNo;
    /**
     * This attribute maps to the column company_no in the chorganisation table.
     */
    private String companyNo;
    /**
     * This attribute maps to the column is_delegated_authority in the chorganisation table.
     */
    private boolean delegatedAuthority;
    private boolean status;
    private String phone;
    private boolean claimOwnershipEnable;
    private boolean taskManagementEnable;
    private boolean adjustDailyRateCharge;
    private Integer dailyRateChargeLimit;
    private boolean thirdPartyInterventionActivated;
    private int forcePasswordChange;
    private int uniquePasswordHistory;;
    private int minimumPasswordLength;
    private boolean insurerUploadOnly;
    private boolean enableSubscriberClaims;
    private boolean enableFixedFeeClaims;
    private boolean enableCollaborationProtocolClaims;
    private boolean autoPenaltyChargeEnabled;
    private boolean enableIPWhitelist;
    private boolean allowEngineersInspectionTask = false;
    private int maxLoginAttempts;
    private int blockTime;
    private boolean disablePrivateNotes;
    private String blockedMessage;
    private boolean restrictExport;
    private int maxAllowedSlaExtForSubscriber;
    private int maxAllowedSlaExtForFixedFee;
    private Branding branding;
    private Chorganisation linkedCho;
    private Integer timesInStatusContested;
    private Integer daysBeforeEscalated;
    private boolean supervisorEnable;
    private boolean solicitorEnable;
    private boolean enableKbbsDashboard;
    private String kbbsManagerPassword;
    private String kbbsOperativePassword;

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

    public boolean isSupervisorEnable() {
        return supervisorEnable;
    }

    public void setSupervisorEnable(boolean supervisorEnable) {
        this.supervisorEnable = supervisorEnable;
    }

    public boolean isSolicitorEnable() {
        return solicitorEnable;
    }

    public void setSolicitorEnable(boolean solicitorEnable) {
        this.solicitorEnable = solicitorEnable;
    }

    public Chorganisation getLinkedCho() {
        return linkedCho;
    }

    public void setLinkedCho(Chorganisation linkedCho) {
        this.linkedCho = linkedCho;
    }

    public Branding getBranding() {
        return branding;
    }

    public void setBranding(Branding branding) {
        this.branding = branding;
    }

    public int getMaxAllowedSlaExtForSubscriber() {
        return maxAllowedSlaExtForSubscriber;
    }

    public void setMaxAllowedSlaExtForSubscriber(int maxAllowedSlaExtForSubscriber) {
        this.maxAllowedSlaExtForSubscriber = maxAllowedSlaExtForSubscriber;
    }

    public int getMaxAllowedSlaExtForFixedFee() {
        return maxAllowedSlaExtForFixedFee;
    }

    public void setMaxAllowedSlaExtForFixedFee(int maxAllowedSlaExtForFixedFee) {
        this.maxAllowedSlaExtForFixedFee = maxAllowedSlaExtForFixedFee;
    }

    public String getBlockedMessage() {
        return blockedMessage;
    }

    public void setBlockedMessage(String blockedMessage) {
        this.blockedMessage = blockedMessage;
    }

    public boolean isAutoPenaltyChargeEnabled() {
        return autoPenaltyChargeEnabled;
    }

    public void setAutoPenaltyChargeEnabled(boolean autoPenaltyChargeEnabled) {
        this.autoPenaltyChargeEnabled = autoPenaltyChargeEnabled;
    }

    public int getMinimumPasswordLength() {
        return minimumPasswordLength;
    }

    public void setMinimumPasswordLength(int minimumPasswordLength) {
        this.minimumPasswordLength = minimumPasswordLength;
    }

    public boolean isThirdPartyInterventionActivated() {
        return thirdPartyInterventionActivated;
    }

    public void setThirdPartyInterventionActivated(boolean thirdPartyInterventionActivated) {
        this.thirdPartyInterventionActivated = thirdPartyInterventionActivated;
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
    

    public boolean isTaskManagementEnable() {
        return taskManagementEnable;
    }

    public void setTaskManagementEnable(boolean taskManagementEnable) {
        this.taskManagementEnable = taskManagementEnable;
    }

    /**
     * Method 'Chorganisation'
     *
     */
    public Chorganisation() {
        branding = Branding.NO_BRANDING;
    }

    /**
     * Method 'getName'
     *
     * @return java.lang.String
     */
    public java.lang.String getName() {
        return name;
    }

    /**
     * Method 'setName'
     *
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }

    /**
     * Method 'getAddress1'
     *
     * @return java.lang.String
     */
    public java.lang.String getAddress1() {
        return address1;
    }

    /**
     * Method 'setAddress1'
     *
     * @param address1
     */
    public void setAddress1(java.lang.String address1) {
        this.address1 = address1;
    }

    /**
     * Method 'getAddress2'
     *
     * @return java.lang.String
     */
    public java.lang.String getAddress2() {
        return address2;
    }

    /**
     * Method 'setAddress2'
     *
     * @param address2
     */
    public void setAddress2(java.lang.String address2) {
        this.address2 = address2;
    }

    /**
     * Method 'getAddress3'
     *
     * @return java.lang.String
     */
    public java.lang.String getAddress3() {
        return address3;
    }

    /**
     * Method 'setAddress3'
     *
     * @param address3
     */
    public void setAddress3(java.lang.String address3) {
        this.address3 = address3;
    }

    /**
     * Method 'getAddress4'
     *
     * @return java.lang.String
     */
    public java.lang.String getAddress4() {
        return address4;
    }

    /**
     * Method 'setAddress4'
     *
     * @param address4
     */
    public void setAddress4(java.lang.String address4) {
        this.address4 = address4;
    }

    /**
     * Method 'getAddress5'
     *
     * @return java.lang.String
     */
    public java.lang.String getAddress5() {
        return address5;
    }

    /**
     * Method 'setAddress5'
     *
     * @param address5
     */
    public void setAddress5(java.lang.String address5) {
        this.address5 = address5;
    }

    /**
     * Method 'getPostcode'
     *
     * @return java.lang.String
     */
    public java.lang.String getPostcode() {
        return postcode;
    }

    /**
     * Method 'setPostcode'
     *
     * @param postcode
     */
    public void setPostcode(java.lang.String postcode) {
        this.postcode = postcode;
    }

    /**
     * Method 'getVatNo'
     *
     * @return java.lang.String
     */
    public java.lang.String getVatNo() {
        return vatNo;
    }

    /**
     * Method 'setVatNo'
     *
     * @param vatNo
     */
    public void setVatNo(java.lang.String vatNo) {
        this.vatNo = vatNo;
    }

    /**
     * Method 'getCompanyNo'
     *
     * @return java.lang.String
     */
    public java.lang.String getCompanyNo() {
        return companyNo;
    }

    /**
     * Method 'setCompanyNo'
     *
     * @param companyNo
     */
    public void setCompanyNo(java.lang.String companyNo) {
        this.companyNo = companyNo;
    }

    /**
     * Method 'isIsDelegatedAuthority'
     *
     * @return boolean
     */
    public boolean isDelegatedAuthority() {
        return delegatedAuthority;
    }

    /**
     * Method 'setIsDelegatedAuthority'
     *
     * @param isDelegatedAuthority
     */
    public void setDelegatedAuthority(boolean isDelegatedAuthority) {
        this.delegatedAuthority = isDelegatedAuthority;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isClaimOwnershipEnable() {
        return claimOwnershipEnable;
    }

    public void setClaimOwnershipEnable(boolean claimOwnershipEnable) {
        this.claimOwnershipEnable = claimOwnershipEnable;
    }

    public boolean isAdjustDailyRateCharge() {
        return adjustDailyRateCharge;
    }

    public void setAdjustDailyRateCharge(boolean adjustDailyRateCharge) {
        this.adjustDailyRateCharge = adjustDailyRateCharge;
    }

    public Integer getDailyRateChargeLimit() {
        return dailyRateChargeLimit;
    }

    public void setDailyRateChargeLimit(Integer dailyRateChargeLimit) {
        this.dailyRateChargeLimit = dailyRateChargeLimit;
    }

    public boolean isInsurerUploadOnly() {
        return insurerUploadOnly;
    }

    public void setInsurerUploadOnly(boolean insurerUploadOnly) {
        this.insurerUploadOnly = insurerUploadOnly;
    }

    public boolean isEnableSubscriberClaims() {
        return enableSubscriberClaims;
    }

    public void setEnableSubscriberClaims(boolean enableSubscriberClaims) {
        this.enableSubscriberClaims = enableSubscriberClaims;
    }

    public boolean isEnableFixedFeeClaims() {
        return enableFixedFeeClaims;
    }

    public void setEnableFixedFeeClaims(boolean enableFixedFeeClaims) {
        this.enableFixedFeeClaims = enableFixedFeeClaims;
    }

    public boolean isEnableCollaborationProtocolClaims() {
        return enableCollaborationProtocolClaims;
    }

    public void setEnableCollaborationProtocolClaims(boolean enableCollaborationProtocolClaims) {
        this.enableCollaborationProtocolClaims = enableCollaborationProtocolClaims;
    }

    public boolean isEnableIPWhitelist() {
        return enableIPWhitelist;
    }

    public void setEnableIPWhitelist(boolean enableIPWhitelist) {
        this.enableIPWhitelist = enableIPWhitelist;
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

    public boolean isRestrictExport() {
        return restrictExport;
    }

    public void setRestrictExport(boolean restrictExport) {
        this.restrictExport = restrictExport;
    }

    public boolean isAllowEngineersInspectionTask() {
        return allowEngineersInspectionTask;
    }

    public void setAllowEngineersInspectionTask(boolean allowEngineersInspectionTask) {
        this.allowEngineersInspectionTask = allowEngineersInspectionTask;
    }
}
