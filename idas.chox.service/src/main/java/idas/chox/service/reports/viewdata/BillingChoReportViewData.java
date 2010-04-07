/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package idas.chox.service.reports.viewdata;

import idas.chox.service.bre.util.CalcHelper;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author abrar
 */
public class BillingChoReportViewData {
 private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private String supplierReference;
    private String claimNumber;
    private String thirdPartyPolicyNumber;
    private String thirdPartyVRN;
    private String thirdPartyName;
    private Date invoiceUploadDate;
    private BigDecimal totalToPay;
    private BigDecimal chargePercentageGrossAmount;
    private BigDecimal vatOnchargePercentageGrossAmount;
    private BigDecimal totalchargePercentageGrossAmount;
    public static BillingChoReportViewData getObject(Map data, BigDecimal chargeRate) {

        BillingChoReportViewData result = new BillingChoReportViewData();

        result.setSupplierReference((String)data.get("cho_reference"));
        result.setClaimNumber((String)data.get("claim_number"));
        result.setThirdPartyPolicyNumber((String)data.get("policy_number"));
        result.setThirdPartyVRN((String)data.get("vehicle_registration"));
        result.setThirdPartyName((String)data.get("thirdPartyName".toLowerCase()));
        result.setInvoiceUploadDate(getDate(data.get("created_date").toString()));
        BigDecimal ttp = (BigDecimal)data.get("total_to_pay");
        result.setTotalToPay((BigDecimal)data.get("total_to_pay"));
        BigDecimal charge = ttp.multiply(chargeRate).divide(new BigDecimal(100.00).setScale(2, BigDecimal.ROUND_HALF_UP));
        result.setChargePercentageGrossAmount(charge);
        BigDecimal vatOnCharge = charge.multiply(CalcHelper.VAT_RATE).setScale(2, BigDecimal.ROUND_HALF_UP);
        result.setVatOnchargePercentageGrossAmount(vatOnCharge);
        result.setTotalchargePercentageGrossAmount(vatOnCharge.add(charge));

        return result;
    }

    private static Date getDate(String dateStr) {
        Date date = null;

        try {
            date = df.parse(dateStr);
        }
        catch (ParseException e) {
            e.printStackTrace();
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

    public Date getInvoiceUploadDate() {
        return invoiceUploadDate;
    }

    public void setInvoiceUploadDate(Date invoiceUploadDate) {
        this.invoiceUploadDate = invoiceUploadDate;
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

    public String getThirdPartyName() {
        return thirdPartyName;
    }

    public void setThirdPartyName(String thirdPartyName) {
        this.thirdPartyName = thirdPartyName;
    }

    public String getThirdPartyPolicyNumber() {
        return thirdPartyPolicyNumber;
    }

    public void setThirdPartyPolicyNumber(String thirdPartyPolicyNumber) {
        this.thirdPartyPolicyNumber = thirdPartyPolicyNumber;
    }

    public String getThirdPartyVRN() {
        return thirdPartyVRN;
    }

    public void setThirdPartyVRN(String thirdPartyVRN) {
        this.thirdPartyVRN = thirdPartyVRN;
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
        return supplierReference + ", " +  claimNumber + ", " + thirdPartyPolicyNumber
                + ", " + thirdPartyVRN + ", " + thirdPartyName + ", "
                + invoiceUploadDate + ", " + totalToPay + ", "
                + chargePercentageGrossAmount + ", "
                + vatOnchargePercentageGrossAmount + ", "
                + totalchargePercentageGrossAmount;
    }
}
