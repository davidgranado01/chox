package idas.chox.service.reports.viewdata;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author abrar
 */
public class BillingInsurerReportViewData {
    private static final Logger LOG = LoggerFactory.getLogger(BillingInsurerReportViewData.class);
    private String supplierReferenceNumber;
    private String supplierName;
    private String claimNumber;
    private String thirdPartyPolicyNumber;
    private String thirdPartyVRN;
    private String thirdPartyName;
    private Date triggerDate;
    private String customerName;
    private String customerVRN;
    private Date incidentDate;
    private BigDecimal netClaimCost;
    private BigDecimal vatOnClaimCost;
    private BigDecimal grossClaimCost;

    public static BillingInsurerReportViewData getObject(Map data) {
        LOG.debug("Creating billing report view data entry...");
        BillingInsurerReportViewData result = new BillingInsurerReportViewData();
        result.supplierReferenceNumber = data.get("cho_reference").toString();
        result.claimNumber = data.get("claim_number").toString();
        result.thirdPartyPolicyNumber = data.get("policy_number").toString();
        result.thirdPartyVRN = data.get("vehicle_registration").toString();
        result.thirdPartyName = data.get("name").toString();
        result.triggerDate = (Date)data.get("trigger_date");
        result.supplierName = data.get("cho_name").toString();
        result.netClaimCost = (BigDecimal)data.get("net_claim_cost");
        result.vatOnClaimCost = (BigDecimal)data.get("vat_claim_cost");
        result.grossClaimCost = (BigDecimal)data.get("gross_claim_cost");
        result.customerName = data.get("customer_name").toString();
        result.customerVRN = data.get("customer_vrn").toString();
        result.incidentDate = (Date)data.get("incident_date");
        LOG.debug("Created billing insurer report entry for claim {}", result.getSupplierReferenceNumber());
        return result;
    }

    public String getSupplierName() {
        return supplierName;
    }

    /**
     * @return the supplierReferenceNumber
     */
    public String getSupplierReferenceNumber() {
        return supplierReferenceNumber;
    }

    /**
     * @return the claimNumber
     */
    public String getClaimNumber() {
        return claimNumber;
    }

    /**
     * @return the thirdPartyPolicyNumber
     */
    public String getThirdPartyPolicyNumber() {
        return thirdPartyPolicyNumber;
    }

    /**
     * @return the thirdPartyVRN
     */
    public String getThirdPartyVRN() {
        return thirdPartyVRN;
    }

    /**
     * @return the thirdPartyName
     */
    public String getThirdPartyName() {
        return thirdPartyName;
    }

    /**
     * @return the claimUploadDate
     */
    public Date getTriggerDate() {
        return triggerDate;
    }

    /**
     * @return the netClaimCost
     */
    public BigDecimal getNetClaimCost() {
        return netClaimCost;
    }

    /**
     * @return the vatOnClaimCost
     */
    public BigDecimal getVatOnClaimCost() {
        return vatOnClaimCost;
    }

    /**
     * @return the grossClaimCost
     */
    public BigDecimal getGrossClaimCost() {
        return grossClaimCost;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerVRN() {
        return customerVRN;
    }

    public Date getIncidentDate() {
        return incidentDate;
    }

}
