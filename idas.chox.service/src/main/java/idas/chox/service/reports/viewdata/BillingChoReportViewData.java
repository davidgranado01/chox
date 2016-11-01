package idas.chox.service.reports.viewdata;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author abrar
 */
public class BillingChoReportViewData {
    private static final Logger LOG = LoggerFactory.getLogger(BillingChoReportViewData.class);
    private String supplierReference;
    private String claimNumber;
    private String customerVRN;
    private String customerName;
    private Date triggerDate;
    private BigDecimal totalToPay;
    private BigDecimal chargePercentageGrossAmount;
    private BigDecimal vatOnchargePercentageGrossAmount;
    private BigDecimal totalchargePercentageGrossAmount;

    public static BillingChoReportViewData getObject(Map data) {

        BillingChoReportViewData result = new BillingChoReportViewData();

        result.setSupplierReference((String)data.get("cho_reference"));
        result.setClaimNumber((String)data.get("claim_number"));
        result.setCustomerVRN((String)data.get("vehicle_registration"));
        result.setCustomerName((String)data.get("name".toLowerCase()));
        result.setTriggerDate(getDate(data.get("trigger_date").toString()));
        result.setTotalToPay((BigDecimal)data.get("total_to_pay"));
        result.setChargePercentageGrossAmount((BigDecimal)data.get("net_claim_cost"));
        result.setVatOnchargePercentageGrossAmount((BigDecimal)data.get("vat_net_claim_cost"));
        result.setTotalchargePercentageGrossAmount((BigDecimal)data.get("gross_claim_cost"));

        return result;
    }

    private static Date getDate(String dateStr) {
        Date date = null;

        try {
            date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(dateStr);
        }
        catch (ParseException e) {
            LOG.error("Error getting date from string '{}': {}", dateStr, e.getMessage());
        }

        return date;
    }


    public BigDecimal getChargePercentageGrossAmount() {
        return chargePercentageGrossAmount;
    }

    public void setChargePercentageGrossAmount(BigDecimal chargePercentageGrossAmount) {
        this.chargePercentageGrossAmount = chargePercentageGrossAmount;
    }

    public String getClaimNumber() {
        return claimNumber;
    }

    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }

    public Date getTriggerDate() {
        return triggerDate;
    }

    public void setTriggerDate(Date triggerDate) {
        this.triggerDate = triggerDate;
    }

    public BigDecimal getTotalToPay() {
        return totalToPay;
    }

    public void setTotalToPay(BigDecimal totalToPay) {
        this.totalToPay = totalToPay;
    }

    public String getSupplierReference() {
        return supplierReference;
    }

    public void setSupplierReference(String supplierReference) {
        this.supplierReference = supplierReference;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerVRN() {
        return customerVRN;
    }

    public void setCustomerVRN(String customerVRN) {
        this.customerVRN = customerVRN;
    }

    public BigDecimal getTotalchargePercentageGrossAmount() {
        return totalchargePercentageGrossAmount;
    }

    public void setTotalchargePercentageGrossAmount(BigDecimal totalchargePercentageGrossAmount) {
        this.totalchargePercentageGrossAmount = totalchargePercentageGrossAmount;
    }

    public BigDecimal getVatOnchargePercentageGrossAmount() {
        return vatOnchargePercentageGrossAmount;
    }

    public void setVatOnchargePercentageGrossAmount(BigDecimal vatOnchargePercentageGrossAmount) {
        this.vatOnchargePercentageGrossAmount = vatOnchargePercentageGrossAmount;
    }

    @Override
    public String toString() {
        return supplierReference + ", " +  claimNumber
                + ", " + customerVRN + ", " + customerName + ", "
                + triggerDate + ", " + totalToPay + ", "
                + chargePercentageGrossAmount + ", "
                + vatOnchargePercentageGrossAmount + ", "
                + totalchargePercentageGrossAmount;
    }
}
