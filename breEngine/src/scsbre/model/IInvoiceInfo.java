package scsbre.model;

import java.math.BigDecimal;
import java.util.Date;

public interface IInvoiceInfo {

    public BigDecimal getHireNet();

  //  public void setHireNet(BigDecimal hireNet);

    public BigDecimal getHireVat();

 //   public void setHireVat(BigDecimal hireVat);

    public BigDecimal getHireGross();

//    public void setHireGross(BigDecimal hireGross);

    public BigDecimal getRepairNet();

//    public void setRepairNet(BigDecimal repairNet);

    public BigDecimal getRepairVat();

//    public void setRepairVat(BigDecimal repairVat);

    public BigDecimal getRepairGross();

//    public void setRepairGross(BigDecimal repairGross);

    public BigDecimal getEngineerFeeNet();

//    public void setEngineerFeeNet(BigDecimal engineerFeeNet);

    public BigDecimal getEngineerFeeVat();

//    public void setEngineerFeeVat(BigDecimal engineerFeeVat);

    public BigDecimal getEngineerFeeGross();

//    public void setEngineerFeeGross(BigDecimal engineerFeeGross);

    public BigDecimal getStorageRecoveryNet();

//    public void setStorageRecoveryNet(BigDecimal storageRecoveryNet);

    public BigDecimal getStorageRecoveryVat();

//    public void setStorageRecoveryVat(BigDecimal storageRecoveryVat);

    public BigDecimal getStorageRecoveryGross();

//    public void setStorageRecoveryGross(BigDecimal storageRecoveryGross);

    public BigDecimal getTotalNet();

//    public void setTotalNet(BigDecimal totalNet);

    public BigDecimal getTotalVat();

//    public void setTotalVat(BigDecimal totalVat);

    public BigDecimal getTotalGross();

//    public void setTotalGross(BigDecimal totalGross);

    public Date getDateInvoiced();

//    public void setDateInvoiced(Date dateInvoiced);

    public BigDecimal getClaimsHandlingInvoiceAmount();

//    public void setClaimsHandlingInvoiceAmount(
//            BigDecimal claimsHandlingInvoiceAmount);

    public BigDecimal getDeductionForClaimsHandlingFee();

//    public void setDeductionForClaimsHandlingFee(
//            BigDecimal deductionForClaimsHandlingFee);

    public BigDecimal getDiscount();

//    public void setDiscount(BigDecimal discount);

    public BigDecimal getTotalToPay();

//    public void setTotalToPay(BigDecimal totalToPay);
}