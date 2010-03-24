/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
    private int countOfClaims;
    private double agreedBenefitValue;
    private double scsBenefitShare;
    private double totalAgreedBenefit;
    private double sumNetClaimCost;
    private double sumVatOnClaimCost;
    private double sumGrossClaimCost;

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

    /**
     * @return the sumNetClaimCost
     */
    public double getSumNetClaimCost() {
        return sumNetClaimCost;
    }

    /**
     * @param sumNetClaimCost the sumNetClaimCost to set
     */
    public void setSumNetClaimCost(double sumNetClaimCost) {
        this.sumNetClaimCost = sumNetClaimCost;
    }

    /**
     * @return the sumVatOnClaimCost
     */
    public double getSumVatOnClaimCost() {
        return sumVatOnClaimCost;
    }

    /**
     * @param sumVatOnClaimCost the sumVatOnClaimCost to set
     */
    public void setSumVatOnClaimCost(double sumVatOnClaimCost) {
        this.sumVatOnClaimCost = sumVatOnClaimCost;
    }

    /**
     * @return the sumGrossClaimCost
     */
    public double getSumGrossClaimCost() {
        return sumGrossClaimCost;
    }

    /**
     * @param sumGrossClaimCost the sumGrossClaimCost to set
     */
    public void setSumGrossClaimCost(double sumGrossClaimCost) {
        this.sumGrossClaimCost = sumGrossClaimCost;
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
