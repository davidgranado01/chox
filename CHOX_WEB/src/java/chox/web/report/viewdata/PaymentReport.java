package chox.web.report.viewdata;

import java.math.BigDecimal;
import java.util.Map;


public class PaymentReport {

    private String supplierClaimInvoiceNo;
    private String claimNo;
    private String vehicleRegistrationNo;
    private String workgroup;
    private String policyHolderFirstname;
    private String policyHolderSurname;
    private BigDecimal hireNet = new BigDecimal(0.00);
    private BigDecimal repairNet = new BigDecimal(0.00);
    private BigDecimal engineerFeeNet = new BigDecimal(0.00);
    private BigDecimal storageRecoveryNet = new BigDecimal(0.00);
    private BigDecimal lessClaimHandlingFee = new BigDecimal(0.00);
    private BigDecimal totalNet = new BigDecimal(0.00);
    private BigDecimal totalVAT = new BigDecimal(0.00);
    private BigDecimal totalGross = new BigDecimal(0.00);
    private BigDecimal lessDiscount = new BigDecimal(0.00);
    private BigDecimal lessExcessCollected = new BigDecimal(0.00);
    private BigDecimal lessVATCollected = new BigDecimal(0.00);
    private BigDecimal totalHireandRepairServices = new BigDecimal(0.00);
    private BigDecimal additionalClaimsHandlingFee = new BigDecimal(0.00);
    private BigDecimal totaltoPay = new BigDecimal(0.00);
    
    public static PaymentReport getObject(Map data) {
        PaymentReport result = new PaymentReport();
        
        result.setSupplierClaimInvoiceNo((String)data.get("cho_reference".toLowerCase()));
        result.setClaimNo((String)data.get("claim_number".toLowerCase()));
        result.setVehicleRegistrationNo((String)data.get("vehicle_registration_number".toLowerCase()));
        result.setWorkgroup((String)data.get("workgroup".toLowerCase()));
        result.setPolicyHolderFirstname((String)data.get("policy_holder_first_name".toLowerCase()));
        result.setPolicyHolderSurname((String)data.get("policy_holder_surname_name".toLowerCase()));
        result.setHireNet((BigDecimal) data.get("hire_net".toLowerCase())); 
        result.setRepairNet((BigDecimal) data.get("repair_net".toLowerCase())); 
        result.setEngineerFeeNet((BigDecimal) data.get("engineer_fee_net".toLowerCase())); 
        result.setStorageRecoveryNet((BigDecimal) data.get("storage_recovery_net".toLowerCase())); 
        result.setLessClaimHandlingFee((BigDecimal) data.get("deduction_for_claims_handling_fee".toLowerCase())); 
        result.setTotalNet((BigDecimal) data.get("total_net".toLowerCase())); 
        result.setTotalVAT((BigDecimal) data.get("total_vat".toLowerCase())); 
        result.setTotalGross((BigDecimal) data.get("total_gross".toLowerCase())); 
        result.setLessDiscount((BigDecimal) data.get("discount".toLowerCase())); 
        result.setLessExcessCollected((BigDecimal) data.get("excess_amount_collected".toLowerCase())); 
        result.setLessVATCollected((BigDecimal) data.get("vat_amount_collected".toLowerCase())); 
        result.setAdditionalClaimsHandlingFee((BigDecimal) data.get("claims_handling_invoice_amount".toLowerCase())); 
        result.setTotaltoPay((BigDecimal) data.get("total_to_pay".toLowerCase())); 

        
        return result;
    }
    
    public BigDecimal getAdditionalClaimsHandlingFee() {
        return additionalClaimsHandlingFee;
    }

    public void setAdditionalClaimsHandlingFee(BigDecimal additionalClaimsHandlingFee) {
        this.additionalClaimsHandlingFee = additionalClaimsHandlingFee;
    }

    public String getClaimNo() {
        return claimNo;
    }

    public void setClaimNo(String claimNo) {
        this.claimNo = claimNo;
    }

    public BigDecimal getEngineerFeeNet() {
        return engineerFeeNet;
    }

    public void setEngineerFeeNet(BigDecimal engineerFeeNet) {
        this.engineerFeeNet = engineerFeeNet;
    }

    public BigDecimal getHireNet() {
        return hireNet;
    }

    public void setHireNet(BigDecimal hireNet) {
        this.hireNet = hireNet;
    }

    public BigDecimal getLessClaimHandlingFee() {
        return lessClaimHandlingFee;
    }

    public void setLessClaimHandlingFee(BigDecimal lessClaimHandlingFee) {
        this.lessClaimHandlingFee = lessClaimHandlingFee;
    }

    public BigDecimal getLessDiscount() {
        return lessDiscount;
    }

    public void setLessDiscount(BigDecimal lessDiscount) {
        this.lessDiscount = lessDiscount;
    }

    public BigDecimal getLessExcessCollected() {
        return lessExcessCollected;
    }

    public void setLessExcessCollected(BigDecimal lessExcessCollected) {
        this.lessExcessCollected = lessExcessCollected;
    }

    public BigDecimal getLessVATCollected() {
        return lessVATCollected;
    }

    public void setLessVATCollected(BigDecimal lessVATCollected) {
        this.lessVATCollected = lessVATCollected;
    }

    public String getWorkgroup() {
        return workgroup;
    }

    public void setWorkgroup(String workgroup) {
        this.workgroup = workgroup;
    }
    
    public String getPolicyHolderFirstname() {
        return policyHolderFirstname;
    }

    public void setPolicyHolderFirstname(String policyHolderFirstname) {
        this.policyHolderFirstname = policyHolderFirstname;
    }

    public String getPolicyHolderSurname() {
        return policyHolderSurname;
    }

    public void setPolicyHolderSurname(String policyHolderSurname) {
        this.policyHolderSurname = policyHolderSurname;
    }

    public BigDecimal getRepairNet() {
        return repairNet;
    }

    public void setRepairNet(BigDecimal repairNet) {
        this.repairNet = repairNet;
    }

    public BigDecimal getStorageRecoveryNet() {
        return storageRecoveryNet;
    }

    public void setStorageRecoveryNet(BigDecimal storageRecoveryNet) {
        this.storageRecoveryNet = storageRecoveryNet;
    }

    public String getSupplierClaimInvoiceNo() {
        return supplierClaimInvoiceNo;
    }

    public void setSupplierClaimInvoiceNo(String supplierClaimInvoiceNo) {
        this.supplierClaimInvoiceNo = supplierClaimInvoiceNo;
    }

    public BigDecimal getTotalGross() {
        return totalGross;
    }

    public void setTotalGross(BigDecimal totalGross) {
        this.totalGross = totalGross;
    }

    public BigDecimal getTotalHireandRepairServices() {
        BigDecimal totalDiscount = (lessDiscount.add(lessExcessCollected).add(lessVATCollected)).multiply(new BigDecimal("-1"));
        totalHireandRepairServices = totalGross.add(totalDiscount);
        return totalHireandRepairServices;
    }

    public void setTotalHireandRepairServices(BigDecimal totalHireandRepairServices) {
        this.totalHireandRepairServices = totalHireandRepairServices;
    }

    public BigDecimal getTotalNet() {
        return totalNet;
    }

    public void setTotalNet(BigDecimal totalNet) {
        this.totalNet = totalNet;
    }

    public BigDecimal getTotalVAT() {
        return totalVAT;
    }

    public void setTotalVAT(BigDecimal totalVAT) {
        this.totalVAT = totalVAT;
    }

    public BigDecimal getTotaltoPay() {
        return totaltoPay;
    }

    public void setTotaltoPay(BigDecimal totaltoPay) {
        this.totaltoPay = totaltoPay;
    }

    public String getVehicleRegistrationNo() {
        return vehicleRegistrationNo;
    }

    public void setVehicleRegistrationNo(String vehicleRegistrationNo) {
        this.vehicleRegistrationNo = vehicleRegistrationNo;
    }
    
    
}
