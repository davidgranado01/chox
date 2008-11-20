package scsbre.sample;

import java.math.BigDecimal;
import java.util.Date;

import scsbre.model.IInvoiceInfo;

public class InvoiceInfo implements IInvoiceInfo {

    private BigDecimal hireNet;
    private BigDecimal hireVat;
    private BigDecimal hireGross;
    private BigDecimal repairNet;
    private BigDecimal repairVat;
    private BigDecimal repairGross;
    private BigDecimal engineerFeeNet;
    private BigDecimal engineerFeeVat;
    private BigDecimal engineerFeeGross;
    private BigDecimal storageRecoveryNet;
    private BigDecimal storageRecoveryVat;
    private BigDecimal storageRecoveryGross;
    private BigDecimal totalNet;
    private BigDecimal totalVat;
    private BigDecimal totalGross;
    private Date dateInvoiced;
    private BigDecimal claimsHandlingInvoiceAmount;
    private BigDecimal deductionForClaimsHandlingFee;
    private BigDecimal discount;
    private BigDecimal totalToPay;

    /* (non-Javadoc)
     * @see scsbre.model.IInvoiceInfo#getHireNet()
     */
    public BigDecimal getHireNet() {
        return hireNet;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IInvoiceInfo#setHireNet(java.math.BigDecimal)
     */

    public void setHireNet(BigDecimal hireNet) {
        this.hireNet = hireNet;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IInvoiceInfo#getHireVat()
     */

    public BigDecimal getHireVat() {
        return hireVat;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IInvoiceInfo#setHireVat(java.math.BigDecimal)
     */

    public void setHireVat(BigDecimal hireVat) {
        this.hireVat = hireVat;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IInvoiceInfo#getHireGross()
     */

    public BigDecimal getHireGross() {
        return hireGross;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IInvoiceInfo#setHireGross(java.math.BigDecimal)
     */

    public void setHireGross(BigDecimal hireGross) {
        this.hireGross = hireGross;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IInvoiceInfo#getRepairNet()
     */

    public BigDecimal getRepairNet() {
        return repairNet;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IInvoiceInfo#setRepairNet(java.math.BigDecimal)
     */

    public void setRepairNet(BigDecimal repairNet) {
        this.repairNet = repairNet;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IInvoiceInfo#getRepairVat()
     */

    public BigDecimal getRepairVat() {
        return repairVat;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IInvoiceInfo#setRepairVat(java.math.BigDecimal)
     */

    public void setRepairVat(BigDecimal repairVat) {
        this.repairVat = repairVat;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IInvoiceInfo#getRepairGross()
     */

    public BigDecimal getRepairGross() {
        return repairGross;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IInvoiceInfo#setRepairGross(java.math.BigDecimal)
     */

    public void setRepairGross(BigDecimal repairGross) {
        this.repairGross = repairGross;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IInvoiceInfo#getEngineerFeeNet()
     */

    public BigDecimal getEngineerFeeNet() {
        return engineerFeeNet;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IInvoiceInfo#setEngineerFeeNet(java.math.BigDecimal)
     */

    public void setEngineerFeeNet(BigDecimal engineerFeeNet) {
        this.engineerFeeNet = engineerFeeNet;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IInvoiceInfo#getEngineerFeeVat()
     */

    public BigDecimal getEngineerFeeVat() {
        return engineerFeeVat;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IInvoiceInfo#setEngineerFeeVat(java.math.BigDecimal)
     */

    public void setEngineerFeeVat(BigDecimal engineerFeeVat) {
        this.engineerFeeVat = engineerFeeVat;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IInvoiceInfo#getEngineerFeeGross()
     */

    public BigDecimal getEngineerFeeGross() {
        return engineerFeeGross;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IInvoiceInfo#setEngineerFeeGross(java.math.BigDecimal)
     */

    public void setEngineerFeeGross(BigDecimal engineerFeeGross) {
        this.engineerFeeGross = engineerFeeGross;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IInvoiceInfo#getStorageRecoveryNet()
     */

    public BigDecimal getStorageRecoveryNet() {
        return storageRecoveryNet;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IInvoiceInfo#setStorageRecoveryNet(java.math.BigDecimal)
     */

    public void setStorageRecoveryNet(BigDecimal storageRecoveryNet) {
        this.storageRecoveryNet = storageRecoveryNet;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IInvoiceInfo#getStorageRecoveryVat()
     */

    public BigDecimal getStorageRecoveryVat() {
        return storageRecoveryVat;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IInvoiceInfo#setStorageRecoveryVat(java.math.BigDecimal)
     */

    public void setStorageRecoveryVat(BigDecimal storageRecoveryVat) {
        this.storageRecoveryVat = storageRecoveryVat;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IInvoiceInfo#getStorageRecoveryGross()
     */

    public BigDecimal getStorageRecoveryGross() {
        return storageRecoveryGross;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IInvoiceInfo#setStorageRecoveryGross(java.math.BigDecimal)
     */

    public void setStorageRecoveryGross(BigDecimal storageRecoveryGross) {
        this.storageRecoveryGross = storageRecoveryGross;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IInvoiceInfo#getTotalNet()
     */

    public BigDecimal getTotalNet() {
        return totalNet;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IInvoiceInfo#setTotalNet(java.math.BigDecimal)
     */

    public void setTotalNet(BigDecimal totalNet) {
        this.totalNet = totalNet;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IInvoiceInfo#getTotalVat()
     */

    public BigDecimal getTotalVat() {
        return totalVat;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IInvoiceInfo#setTotalVat(java.math.BigDecimal)
     */

    public void setTotalVat(BigDecimal totalVat) {
        this.totalVat = totalVat;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IInvoiceInfo#getTotalGross()
     */

    public BigDecimal getTotalGross() {
        return totalGross;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IInvoiceInfo#setTotalGross(java.math.BigDecimal)
     */

    public void setTotalGross(BigDecimal totalGross) {
        this.totalGross = totalGross;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IInvoiceInfo#getDateInvoiced()
     */

    public Date getDateInvoiced() {
        return dateInvoiced;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IInvoiceInfo#setDateInvoiced(java.util.Date)
     */

    public void setDateInvoiced(Date dateInvoiced) {
        this.dateInvoiced = dateInvoiced;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IInvoiceInfo#getClaimsHandlingInvoiceAmount()
     */

    public BigDecimal getClaimsHandlingInvoiceAmount() {
        return claimsHandlingInvoiceAmount;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IInvoiceInfo#setClaimsHandlingInvoiceAmount(java.math.BigDecimal)
     */

    public void setClaimsHandlingInvoiceAmount(
            BigDecimal claimsHandlingInvoiceAmount) {
        this.claimsHandlingInvoiceAmount = claimsHandlingInvoiceAmount;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IInvoiceInfo#getDeductionForClaimsHandlingFee()
     */

    public BigDecimal getDeductionForClaimsHandlingFee() {
        return deductionForClaimsHandlingFee;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IInvoiceInfo#setDeductionForClaimsHandlingFee(java.math.BigDecimal)
     */

    public void setDeductionForClaimsHandlingFee(
            BigDecimal deductionForClaimsHandlingFee) {
        this.deductionForClaimsHandlingFee = deductionForClaimsHandlingFee;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IInvoiceInfo#getDiscount()
     */

    public BigDecimal getDiscount() {
        return discount;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IInvoiceInfo#setDiscount(java.math.BigDecimal)
     */

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IInvoiceInfo#getTotalToPay()
     */

    public BigDecimal getTotalToPay() {
        return totalToPay;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IInvoiceInfo#setTotalToPay(java.math.BigDecimal)
     */

    public void setTotalToPay(BigDecimal totalToPay) {
        this.totalToPay = totalToPay;
    }
}
