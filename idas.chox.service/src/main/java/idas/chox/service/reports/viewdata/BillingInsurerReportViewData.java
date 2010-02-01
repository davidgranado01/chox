/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package idas.chox.service.reports.viewdata;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author abrar
 */
public class BillingInsurerReportViewData {
    private String supplierReferenceNumber;
    private String claimNumber;
    private String thirdPartyPolicyNumber;
    private String thirdPartyVRN;
    private String thirdPartyName;
    private Date claimUploadDate;
    private BigDecimal netClaimCost;
    private BigDecimal vatOnClaimCost;
    private BigDecimal grossClaimCost;

    public static BillingInsurerReportViewData getObject(Map data) {

        BillingInsurerReportViewData result = new BillingInsurerReportViewData();
        result.setSupplierReferenceNumber(data.get("cho_reference".toLowerCase()).toString());
        result.setClaimNumber(data.get("claim_reference".toLowerCase()).toString());
        result.setThirdPartyPolicyNumber(data.get("policy_number".toLowerCase()).toString());
        result.setThirdPartyVRN(data.get("vehicle_registration".toLowerCase()).toString());
        result.setThirdPartyName(data.get("name".toLowerCase()).toString());
        result.setClaimUploadDate((Date)data.get("created_date".toLowerCase().toString()));
        result.setNetClaimCost((BigDecimal)data.get("net_claim_cost".toLowerCase().toString()));
        result.setVatOnClaimCost((BigDecimal)data.get("vat_on_claim_cost".toLowerCase()));
        result.setGrossClaimCost((BigDecimal)data.get("gross_claim_cost".toLowerCase()));
        return result;
    }

    /**
     * @return the supplierReferenceNumber
     */
    public String getSupplierReferenceNumber() {
        return supplierReferenceNumber;
    }

    /**
     * @param supplierReferenceNumber the supplierReferenceNumber to set
     */
    public void setSupplierReferenceNumber(String supplierReferenceNumber) {
        this.supplierReferenceNumber = supplierReferenceNumber;
    }

    /**
     * @return the claimNumber
     */
    public String getClaimNumber() {
        return claimNumber;
    }

    /**
     * @param claimNumber the claimNumber to set
     */
    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }

    /**
     * @return the thirdPartyPolicyNumber
     */
    public String getThirdPartyPolicyNumber() {
        return thirdPartyPolicyNumber;
    }

    /**
     * @param thirdPartyPolicyNumber the thirdPartyPolicyNumber to set
     */
    public void setThirdPartyPolicyNumber(String thirdPartyPolicyNumber) {
        this.thirdPartyPolicyNumber = thirdPartyPolicyNumber;
    }

    /**
     * @return the thirdPartyVRN
     */
    public String getThirdPartyVRN() {
        return thirdPartyVRN;
    }

    /**
     * @param thirdPartyVRN the thirdPartyVRN to set
     */
    public void setThirdPartyVRN(String thirdPartyVRN) {
        this.thirdPartyVRN = thirdPartyVRN;
    }

    /**
     * @return the thirdPartyName
     */
    public String getThirdPartyName() {
        return thirdPartyName;
    }

    /**
     * @param thirdPartyName the thirdPartyName to set
     */
    public void setThirdPartyName(String thirdPartyName) {
        this.thirdPartyName = thirdPartyName;
    }

    /**
     * @return the claimUploadDate
     */
    public Date getClaimUploadDate() {
        return claimUploadDate;
    }

    /**
     * @param claimUploadDate the claimUploadDate to set
     */
    public void setClaimUploadDate(Date claimUploadDate) {
        this.claimUploadDate = claimUploadDate;
    }

    /**
     * @return the netClaimCost
     */
    public BigDecimal getNetClaimCost() {
        return netClaimCost;
    }

    /**
     * @param netClaimCost the netClaimCost to set
     */
    public void setNetClaimCost(BigDecimal netClaimCost) {
        this.netClaimCost = netClaimCost;
    }

    /**
     * @return the vatOnClaimCost
     */
    public BigDecimal getVatOnClaimCost() {
        return vatOnClaimCost;
    }

    /**
     * @param vatOnClaimCost the vatOnClaimCost to set
     */
    public void setVatOnClaimCost(BigDecimal vatOnClaimCost) {
        this.vatOnClaimCost = vatOnClaimCost;
    }

    /**
     * @return the grossClaimCost
     */
    public BigDecimal getGrossClaimCost() {
        return grossClaimCost;
    }

    /**
     * @param grossClaimCost the grossClaimCost to set
     */
    public void setGrossClaimCost(BigDecimal grossClaimCost) {
        this.grossClaimCost = grossClaimCost;
    }
}
