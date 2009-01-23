package scsbre.model;

import java.math.BigDecimal;
import java.util.Date;

public interface IInvoiceInfo {

    public BigDecimal getHireNet();

    public BigDecimal getHireVat();

    public BigDecimal getHireGross();

    public BigDecimal getRepairNet();

    public BigDecimal getRepairVat();

    public BigDecimal getRepairGross();

    public BigDecimal getEngineerFeeNet();

    public BigDecimal getEngineerFeeVat();

    public BigDecimal getEngineerFeeGross();

    public BigDecimal getStorageRecoveryNet();

    public BigDecimal getStorageRecoveryVat();

    public BigDecimal getStorageRecoveryGross();

    public BigDecimal getTotalNet();

    public BigDecimal getTotalVat();

    public BigDecimal getTotalGross();

    public Date getDateInvoiced();

    public BigDecimal getClaimsHandlingInvoiceAmount();

    public BigDecimal getDeductionForClaimsHandlingFee();

    public BigDecimal getDiscount();

    public BigDecimal getTotalToPay();
    
    public BigDecimal getPenaltyCharge();

}