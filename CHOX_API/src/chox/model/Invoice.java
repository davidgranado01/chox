package chox.model;

import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;
import java.util.Date;

public class Invoice implements Serializable
{
	protected int id;
	protected Date dateInvoiced;
	protected double hireNet;
	protected double hireVat;
	protected double hireGross;
	protected double repairNet;
	protected double repairVat;
	protected double repairGross;
	protected double engineerFeeNet;
	protected double engineerFeeVat;
	protected double engineerFeeGross;
	protected double storageRecoveryNet;
	protected double storageRecoveryVat;
	protected double storageRecoveryGross;
	protected double totalNet;
	protected double totalVat;
	protected double totalGross;
	protected double claimsHandlingInvoiceAmount;
	protected double deductionForClaimsHandlingFee;
	protected double discount;
	protected double totaltoPay;
	protected String handlingInvoiceNo;
	protected String claimInvoiceNo;
	protected double cdwFee;
	protected Integer cdwQty;
	protected double automaticFee;
	protected Integer automaticQty;
	protected double satNavFee;
	protected Integer satNavQty;
	protected double estateFee;
	protected Integer estateQty;
	protected double babySeatFee;
	protected Integer babySeatQty;
	protected double towBarsFee;
	protected Integer towBarsQty;
	protected double nonStandardInsurancePremiumFee;
	protected Integer nonStandardInsurancePremiumQty;
	protected double adminFee;
	protected Integer adminQty;
	protected double roofRackFee;
	protected Integer roofRackQty;
	protected double dualControlFee;
	protected Integer dualControlQty;
	protected double deliveryCollectionFee;
	protected Integer deliveryCollectionQty;
	protected int createdBy;
	protected Date createdDate;
	protected int lastModifiedBy;
	protected Date lastModifiedDate;

    public double getAdminFee() {
        return adminFee;
    }

    public void setAdminFee(double adminFee) {
        this.adminFee = adminFee;
    }

    public Integer getAdminQty() {
        return adminQty;
    }

    public void setAdminQty(Integer adminQty) {
        this.adminQty = adminQty;
    }

    public double getAutomaticFee() {
        return automaticFee;
    }

    public void setAutomaticFee(double automaticFee) {
        this.automaticFee = automaticFee;
    }

    public Integer getAutomaticQty() {
        return automaticQty;
    }

    public void setAutomaticQty(Integer automaticQty) {
        this.automaticQty = automaticQty;
    }

    public double getBabySeatFee() {
        return babySeatFee;
    }

    public void setBabySeatFee(double babySeatFee) {
        this.babySeatFee = babySeatFee;
    }

    public Integer getBabySeatQty() {
        return babySeatQty;
    }

    public void setBabySeatQty(Integer babySeatQty) {
        this.babySeatQty = babySeatQty;
    }

    public double getCdwFee() {
        return cdwFee;
    }

    public void setCdwFee(double cdwFee) {
        this.cdwFee = cdwFee;
    }

    public Integer getCdwQty() {
        return cdwQty;
    }

    public void setCdwQty(Integer cdwQty) {
        this.cdwQty = cdwQty;
    }

    public String getClaimInvoiceNo() {
        return claimInvoiceNo;
    }

    public void setClaimInvoiceNo(String claimInvoiceNo) {
        this.claimInvoiceNo = claimInvoiceNo;
    }

    public double getClaimsHandlingInvoiceAmount() {
        return claimsHandlingInvoiceAmount;
    }

    public void setClaimsHandlingInvoiceAmount(double claimsHandlingInvoiceAmount) {
        this.claimsHandlingInvoiceAmount = claimsHandlingInvoiceAmount;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getDateInvoiced() {
        return dateInvoiced;
    }

    public void setDateInvoiced(Date dateInvoiced) {
        this.dateInvoiced = dateInvoiced;
    }

    public double getDeductionForClaimsHandlingFee() {
        return deductionForClaimsHandlingFee;
    }

    public void setDeductionForClaimsHandlingFee(double deductionForClaimsHandlingFee) {
        this.deductionForClaimsHandlingFee = deductionForClaimsHandlingFee;
    }

    public double getDeliveryCollectionFee() {
        return deliveryCollectionFee;
    }

    public void setDeliveryCollectionFee(double deliveryCollectionFee) {
        this.deliveryCollectionFee = deliveryCollectionFee;
    }

    public Integer getDeliveryCollectionQty() {
        return deliveryCollectionQty;
    }

    public void setDeliveryCollectionQty(Integer deliveryCollectionQty) {
        this.deliveryCollectionQty = deliveryCollectionQty;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getDualControlFee() {
        return dualControlFee;
    }

    public void setDualControlFee(double dualControlFee) {
        this.dualControlFee = dualControlFee;
    }

    public Integer getDualControlQty() {
        return dualControlQty;
    }

    public void setDualControlQty(Integer dualControlQty) {
        this.dualControlQty = dualControlQty;
    }

    public double getEngineerFeeGross() {
        return engineerFeeGross;
    }

    public void setEngineerFeeGross(double engineerFeeGross) {
        this.engineerFeeGross = engineerFeeGross;
    }

    public double getEngineerFeeNet() {
        return engineerFeeNet;
    }

    public void setEngineerFeeNet(double engineerFeeNet) {
        this.engineerFeeNet = engineerFeeNet;
    }

    public double getEngineerFeeVat() {
        return engineerFeeVat;
    }

    public void setEngineerFeeVat(double engineerFeeVat) {
        this.engineerFeeVat = engineerFeeVat;
    }

    public double getEstateFee() {
        return estateFee;
    }

    public void setEstateFee(double estateFee) {
        this.estateFee = estateFee;
    }

    public Integer getEstateQty() {
        return estateQty;
    }

    public void setEstateQty(Integer estateQty) {
        this.estateQty = estateQty;
    }

    public String getHandlingInvoiceNo() {
        return handlingInvoiceNo;
    }

    public void setHandlingInvoiceNo(String handlingInvoiceNo) {
        this.handlingInvoiceNo = handlingInvoiceNo;
    }

    public double getHireGross() {
        return hireGross;
    }

    public void setHireGross(double hireGross) {
        this.hireGross = hireGross;
    }

    public double getHireNet() {
        return hireNet;
    }

    public void setHireNet(double hireNet) {
        this.hireNet = hireNet;
    }

    public double getHireVat() {
        return hireVat;
    }

    public void setHireVat(double hireVat) {
        this.hireVat = hireVat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(int lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public double getNonStandardInsurancePremiumFee() {
        return nonStandardInsurancePremiumFee;
    }

    public void setNonStandardInsurancePremiumFee(double nonStandardInsurancePremiumFee) {
        this.nonStandardInsurancePremiumFee = nonStandardInsurancePremiumFee;
    }

    public Integer getNonStandardInsurancePremiumQty() {
        return nonStandardInsurancePremiumQty;
    }

    public void setNonStandardInsurancePremiumQty(Integer nonStandardInsurancePremiumQty) {
        this.nonStandardInsurancePremiumQty = nonStandardInsurancePremiumQty;
    }

    public double getRepairGross() {
        return repairGross;
    }

    public void setRepairGross(double repairGross) {
        this.repairGross = repairGross;
    }

    public double getRepairNet() {
        return repairNet;
    }

    public void setRepairNet(double repairNet) {
        this.repairNet = repairNet;
    }

    public double getRepairVat() {
        return repairVat;
    }

    public void setRepairVat(double repairVat) {
        this.repairVat = repairVat;
    }

    public double getRoofRackFee() {
        return roofRackFee;
    }

    public void setRoofRackFee(double roofRackFee) {
        this.roofRackFee = roofRackFee;
    }

    public Integer getRoofRackQty() {
        return roofRackQty;
    }

    public void setRoofRackQty(Integer roofRackQty) {
        this.roofRackQty = roofRackQty;
    }

    public double getSatNavFee() {
        return satNavFee;
    }

    public void setSatNavFee(double satNavFee) {
        this.satNavFee = satNavFee;
    }

    public Integer getSatNavQty() {
        return satNavQty;
    }

    public void setSatNavQty(Integer satNavQty) {
        this.satNavQty = satNavQty;
    }

    public double getStorageRecoveryGross() {
        return storageRecoveryGross;
    }

    public void setStorageRecoveryGross(double storageRecoveryGross) {
        this.storageRecoveryGross = storageRecoveryGross;
    }

    public double getStorageRecoveryNet() {
        return storageRecoveryNet;
    }

    public void setStorageRecoveryNet(double storageRecoveryNet) {
        this.storageRecoveryNet = storageRecoveryNet;
    }

    public double getStorageRecoveryVat() {
        return storageRecoveryVat;
    }

    public void setStorageRecoveryVat(double storageRecoveryVat) {
        this.storageRecoveryVat = storageRecoveryVat;
    }

    public double getTotalGross() {
        return totalGross;
    }

    public void setTotalGross(double totalGross) {
        this.totalGross = totalGross;
    }

    public double getTotalNet() {
        return totalNet;
    }

    public void setTotalNet(double totalNet) {
        this.totalNet = totalNet;
    }

    public double getTotalVat() {
        return totalVat;
    }

    public void setTotalVat(double totalVat) {
        this.totalVat = totalVat;
    }

    public double getTotaltoPay() {
        return totaltoPay;
    }

    public void setTotaltoPay(double totaltoPay) {
        this.totaltoPay = totaltoPay;
    }

    public double getTowBarsFee() {
        return towBarsFee;
    }

    public void setTowBarsFee(double towBarsFee) {
        this.towBarsFee = towBarsFee;
    }

    public Integer getTowBarsQty() {
        return towBarsQty;
    }

    public void setTowBarsQty(Integer towBarsQty) {
        this.towBarsQty = towBarsQty;
    }

	

}
