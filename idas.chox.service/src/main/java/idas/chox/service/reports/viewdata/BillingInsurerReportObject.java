package idas.chox.service.reports.viewdata;

import java.util.Date;

/**
 *
 * @author abrar
 */
public class BillingInsurerReportObject {

    private Date currentDate;
    private Date claimUploadDateFrom;
    private Date claimUploadDateTo;
    private boolean isFixedTransactionFee;
    private int countOfClaims;
    private double agreedBenefitValue;
    private double scsBenefitShare;
    private double fixedTransactionFee;
    private double totalAgreedBenefit;
    private String scheduleName;
    private String insurerName;

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public String getInsurerName() {
        return insurerName;
    }

    public void setInsurerName(String insurerName) {
        this.insurerName = insurerName;
    }

    public double getFixedTransactionFee() {
        return fixedTransactionFee;
    }

    public void setFixedTransactionFee(double fixedTransactionFee) {
        this.fixedTransactionFee = fixedTransactionFee;
    }

    public boolean isIsFixedTransactionFee() {
        return isFixedTransactionFee;
    }

    public boolean getIsFixedTransactionFee() {
        return isFixedTransactionFee;
    }

    public void setIsFixedTransactionFee(boolean isFixedTransactionFee) {
        this.isFixedTransactionFee = isFixedTransactionFee;
    }

    /**
     * @return the currentDate
     */
    public Date getCurrentDate() {
        return currentDate;
    }

    /**
     * @param currentDate the currentDate to set
     */
    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    /**
     * @return the claimUploadDateFrom
     */
    public Date getClaimUploadDateFrom() {
        return claimUploadDateFrom;
    }

    /**
     * @param claimUploadDateFrom the claimUploadDateFrom to set
     */
    public void setClaimUploadDateFrom(Date claimUploadDateFrom) {
        this.claimUploadDateFrom = claimUploadDateFrom;
    }

    /**
     * @return the claimUploadDateTo
     */
    public Date getClaimUploadDateTo() {
        return claimUploadDateTo;
    }

    /**
     * @param claimUploadDateTo the claimUploadDateTo to set
     */
    public void setClaimUploadDateTo(Date claimUploadDateTo) {
        this.claimUploadDateTo = claimUploadDateTo;
    }

    /**
     * @return the countOfClaims
     */
    public int getCountOfClaims() {
        return countOfClaims;
    }

    /**
     * @param countOfClaims the countOfClaims to set
     */
    public void setCountOfClaims(int countOfClaims) {
        this.countOfClaims = countOfClaims;
    }

    /**
     * @return the agreedBenefitValue
     */
    public double getAgreedBenefitValue() {
        return agreedBenefitValue;
    }

    /**
     * @param agreedBenefitValue the agreedBenefitValue to set
     */
    public void setAgreedBenefitValue(double agreedBenefitValue) {
        this.agreedBenefitValue = agreedBenefitValue;
    }

    /**
     * @return the scsBenefitShare
     */
    public double getScsBenefitShare() {
        return scsBenefitShare;
    }

    /**
     * @param scsBenefitShare the scsBenefitShare to set
     */
    public void setScsBenefitShare(double scsBenefitShare) {
        this.scsBenefitShare = scsBenefitShare;
    }

    public void setTotalAgreedBenefit(double doubleValue) {
        this.totalAgreedBenefit = doubleValue;
    }

    /**
     * @return the totalAgreedBenefit
     */
    public double getTotalAgreedBenefit() {
        return totalAgreedBenefit;
    }
}
