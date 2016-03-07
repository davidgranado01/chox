package idas.chox.service.reports.viewdata;

import java.math.BigDecimal;
import java.util.Map;

public class InvoiceSavingSummaryReportViewData {

    private String supplierReferenceNumber;
    private String claimNumber;
    private String policyHolderVehicleRegisterationNumber;
    private String workgroupName;
    private BigDecimal originalInvoiceAmout;
    private BigDecimal AgreedSettlementValue;

    public static InvoiceSavingSummaryReportViewData getObject(Map data) {

        InvoiceSavingSummaryReportViewData result = new InvoiceSavingSummaryReportViewData();
        result.setSupplierReferenceNumber(data.get("supplier_reference_number".toLowerCase()).toString());
        result.setClaimNumber(data.get("claim_number".toLowerCase()).toString());
        result.setPolicyHolderVehicleRegisterationNumber(data.get("policy_holder_vehicle_registeration_number".toLowerCase()).toString());
        result.setWorkgroupName(data.get("workgroup_name".toLowerCase()).toString());
        result.setOriginalInvoiceAmout((BigDecimal)data.get("original_invoice_amount".toLowerCase()));
        result.setAgreedSettlementValue((BigDecimal)data.get("agreed_settlement_value".toLowerCase()));
        return result;
    }

    public BigDecimal getAgreedSettlementValue() {
        return AgreedSettlementValue;
    }

    public void setAgreedSettlementValue(BigDecimal agreedSettlementValue) {
        this.AgreedSettlementValue = agreedSettlementValue;
    }

    public String getClaimNumber() {
        return claimNumber;
    }

    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }

    public BigDecimal getOriginalInvoiceAmout() {
        return originalInvoiceAmout;
    }

    public void setOriginalInvoiceAmout(BigDecimal originalInvoiceAmout) {
        this.originalInvoiceAmout = originalInvoiceAmout;
    }

    public String getPolicyHolderVehicleRegisterationNumber() {
        return policyHolderVehicleRegisterationNumber;
    }

    public void setPolicyHolderVehicleRegisterationNumber(String policyHolderVehicleRegisterationNumber) {
        this.policyHolderVehicleRegisterationNumber = policyHolderVehicleRegisterationNumber;
    }

    public String getSupplierReferenceNumber() {
        return supplierReferenceNumber;
    }

    public void setSupplierReferenceNumber(String supplierReferenceNumber) {
        this.supplierReferenceNumber = supplierReferenceNumber;
    }

    public String getWorkgroupName() {
        return workgroupName;
    }

    public void setWorkgroupName(String workgroupName) {
        this.workgroupName = workgroupName;
    }

    
}
