package chox.model;

import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

public class Invoice implements Serializable
{
	/** 
	 * This attribute maps to the column id in the Invoice table.
	 */
	protected int id;

	/** 
	 * This attribute maps to the column dateInvoiced in the Invoice table.
	 */
	protected Date dateInvoiced;

	/** 
	 * This attribute maps to the column hireNet in the Invoice table.
	 */
	protected BigDecimal hireNet;

	/** 
	 * This attribute maps to the column hireVat in the Invoice table.
	 */
	protected BigDecimal hireVat;

	/** 
	 * This attribute maps to the column hireGross in the Invoice table.
	 */
	protected BigDecimal hireGross;

	/** 
	 * This attribute maps to the column repairNet in the Invoice table.
	 */
	protected BigDecimal repairNet;

	/** 
	 * This attribute maps to the column repairVat in the Invoice table.
	 */
	protected BigDecimal repairVat;

	/** 
	 * This attribute maps to the column repairGross in the Invoice table.
	 */
	protected BigDecimal repairGross;

	/** 
	 * This attribute maps to the column engineerFeeNet in the Invoice table.
	 */
	protected BigDecimal engineerFeeNet;

	/** 
	 * This attribute maps to the column engineerFeeVat in the Invoice table.
	 */
	protected BigDecimal engineerFeeVat;

	/** 
	 * This attribute maps to the column engineerFeeGross in the Invoice table.
	 */
	protected BigDecimal engineerFeeGross;

	/** 
	 * This attribute maps to the column storageRecoveryNet in the Invoice table.
	 */
	protected BigDecimal storageRecoveryNet;

	/** 
	 * This attribute maps to the column storageRecoveryVat in the Invoice table.
	 */
	protected BigDecimal storageRecoveryVat;

	/** 
	 * This attribute maps to the column storageRecoveryGross in the Invoice table.
	 */
	protected BigDecimal storageRecoveryGross;

	/** 
	 * This attribute maps to the column totalNet in the Invoice table.
	 */
	protected BigDecimal totalNet;

	/** 
	 * This attribute maps to the column totalVat in the Invoice table.
	 */
	protected BigDecimal totalVat;

	/** 
	 * This attribute maps to the column totalGross in the Invoice table.
	 */
	protected BigDecimal totalGross;

	/** 
	 * This attribute maps to the column claimsHandlingInvoiceAmount in the Invoice table.
	 */
	protected BigDecimal claimsHandlingInvoiceAmount;

	/** 
	 * This attribute maps to the column deductionForClaimsHandlingFee in the Invoice table.
	 */
	protected BigDecimal deductionForClaimsHandlingFee;

	/** 
	 * This attribute maps to the column discount in the Invoice table.
	 */
	protected BigDecimal discount;

	/** 
	 * This attribute maps to the column totaltoPay in the Invoice table.
	 */
	protected BigDecimal totaltoPay;

	/** 
	 * This attribute maps to the column handlingInvoiceNo in the Invoice table.
	 */
	protected String handlingInvoiceNo;

	/** 
	 * This attribute maps to the column claimInvoiceNo in the Invoice table.
	 */
	protected String claimInvoiceNo;

	/** 
	 * This attribute maps to the column cdwFee in the Invoice table.
	 */
	protected BigDecimal cdwFee;

	/** 
	 * This attribute maps to the column cdwQty in the Invoice table.
	 */
	protected Integer cdwQty;

	/** 
	 * This attribute maps to the column automaticFee in the Invoice table.
	 */
	protected BigDecimal automaticFee;

	/** 
	 * This attribute maps to the column automaticQty in the Invoice table.
	 */
	protected Integer automaticQty;

	/** 
	 * This attribute maps to the column satNavFee in the Invoice table.
	 */
	protected BigDecimal satNavFee;

	/** 
	 * This attribute maps to the column satNavQty in the Invoice table.
	 */
	protected Integer satNavQty;

	/** 
	 * This attribute maps to the column estateFee in the Invoice table.
	 */
	protected BigDecimal estateFee;

	/** 
	 * This attribute maps to the column estateQty in the Invoice table.
	 */
	protected Integer estateQty;

	/** 
	 * This attribute maps to the column babySeatFee in the Invoice table.
	 */
	protected BigDecimal babySeatFee;

	/** 
	 * This attribute maps to the column babySeatQty in the Invoice table.
	 */
	protected Integer babySeatQty;

	/** 
	 * This attribute maps to the column towBarsFee in the Invoice table.
	 */
	protected BigDecimal towBarsFee;

	/** 
	 * This attribute maps to the column towBarsQty in the Invoice table.
	 */
	protected Integer towBarsQty;

	/** 
	 * This attribute maps to the column nonStandardInsurancePremiumFee in the Invoice table.
	 */
	protected BigDecimal nonStandardInsurancePremiumFee;

	/** 
	 * This attribute maps to the column nonStandardInsurancePremiumQty in the Invoice table.
	 */
	protected Integer nonStandardInsurancePremiumQty;

	/** 
	 * This attribute maps to the column adminFee in the Invoice table.
	 */
	protected BigDecimal adminFee;

	/** 
	 * This attribute maps to the column adminQty in the Invoice table.
	 */
	protected Integer adminQty;

	/** 
	 * This attribute maps to the column roofRackFee in the Invoice table.
	 */
	protected BigDecimal roofRackFee;

	/** 
	 * This attribute maps to the column roofRackQty in the Invoice table.
	 */
	protected Integer roofRackQty;

	/** 
	 * This attribute maps to the column dualControlFee in the Invoice table.
	 */
	protected BigDecimal dualControlFee;

	/** 
	 * This attribute maps to the column dualControlQty in the Invoice table.
	 */
	protected Integer dualControlQty;

	/** 
	 * This attribute maps to the column deliveryCollectionFee in the Invoice table.
	 */
	protected BigDecimal deliveryCollectionFee;

	/** 
	 * This attribute maps to the column deliveryCollectionQty in the Invoice table.
	 */
	protected Integer deliveryCollectionQty;

	/** 
	 * This attribute maps to the column createdBy in the Invoice table.
	 */
	protected int createdBy;

	/** 
	 * This attribute maps to the column createdDate in the Invoice table.
	 */
	protected Date createdDate;

	/** 
	 * This attribute maps to the column lastModifiedBy in the Invoice table.
	 */
	protected int lastModifiedBy;

	/** 
	 * This attribute maps to the column lastModifiedDate in the Invoice table.
	 */
	protected Date lastModifiedDate;

	/**
	 * Method 'Invoice'
	 * 
	 */
	public Invoice()
	{
	}

	/**
	 * Method 'getId'
	 * 
	 * @return int
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * Method 'setId'
	 * 
	 * @param id
	 */
	public void setId(int id)
	{
		this.id = id;
	}

	/**
	 * Method 'getDateInvoiced'
	 * 
	 * @return java.util.Date
	 */
	public java.util.Date getDateInvoiced()
	{
		return dateInvoiced;
	}

	/**
	 * Method 'setDateInvoiced'
	 * 
	 * @param dateInvoiced
	 */
	public void setDateInvoiced(java.util.Date dateInvoiced)
	{
		this.dateInvoiced = dateInvoiced;
	}

	/**
	 * Method 'getHireNet'
	 * 
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getHireNet()
	{
		return hireNet;
	}

	/**
	 * Method 'setHireNet'
	 * 
	 * @param hireNet
	 */
	public void setHireNet(java.math.BigDecimal hireNet)
	{
		this.hireNet = hireNet;
	}

	/**
	 * Method 'getHireVat'
	 * 
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getHireVat()
	{
		return hireVat;
	}

	/**
	 * Method 'setHireVat'
	 * 
	 * @param hireVat
	 */
	public void setHireVat(java.math.BigDecimal hireVat)
	{
		this.hireVat = hireVat;
	}

	/**
	 * Method 'getHireGross'
	 * 
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getHireGross()
	{
		return hireGross;
	}

	/**
	 * Method 'setHireGross'
	 * 
	 * @param hireGross
	 */
	public void setHireGross(java.math.BigDecimal hireGross)
	{
		this.hireGross = hireGross;
	}

	/**
	 * Method 'getRepairNet'
	 * 
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getRepairNet()
	{
		return repairNet;
	}

	/**
	 * Method 'setRepairNet'
	 * 
	 * @param repairNet
	 */
	public void setRepairNet(java.math.BigDecimal repairNet)
	{
		this.repairNet = repairNet;
	}

	/**
	 * Method 'getRepairVat'
	 * 
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getRepairVat()
	{
		return repairVat;
	}

	/**
	 * Method 'setRepairVat'
	 * 
	 * @param repairVat
	 */
	public void setRepairVat(java.math.BigDecimal repairVat)
	{
		this.repairVat = repairVat;
	}

	/**
	 * Method 'getRepairGross'
	 * 
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getRepairGross()
	{
		return repairGross;
	}

	/**
	 * Method 'setRepairGross'
	 * 
	 * @param repairGross
	 */
	public void setRepairGross(java.math.BigDecimal repairGross)
	{
		this.repairGross = repairGross;
	}

	/**
	 * Method 'getEngineerFeeNet'
	 * 
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getEngineerFeeNet()
	{
		return engineerFeeNet;
	}

	/**
	 * Method 'setEngineerFeeNet'
	 * 
	 * @param engineerFeeNet
	 */
	public void setEngineerFeeNet(java.math.BigDecimal engineerFeeNet)
	{
		this.engineerFeeNet = engineerFeeNet;
	}

	/**
	 * Method 'getEngineerFeeVat'
	 * 
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getEngineerFeeVat()
	{
		return engineerFeeVat;
	}

	/**
	 * Method 'setEngineerFeeVat'
	 * 
	 * @param engineerFeeVat
	 */
	public void setEngineerFeeVat(java.math.BigDecimal engineerFeeVat)
	{
		this.engineerFeeVat = engineerFeeVat;
	}

	/**
	 * Method 'getEngineerFeeGross'
	 * 
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getEngineerFeeGross()
	{
		return engineerFeeGross;
	}

	/**
	 * Method 'setEngineerFeeGross'
	 * 
	 * @param engineerFeeGross
	 */
	public void setEngineerFeeGross(java.math.BigDecimal engineerFeeGross)
	{
		this.engineerFeeGross = engineerFeeGross;
	}

	/**
	 * Method 'getStorageRecoveryNet'
	 * 
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getStorageRecoveryNet()
	{
		return storageRecoveryNet;
	}

	/**
	 * Method 'setStorageRecoveryNet'
	 * 
	 * @param storageRecoveryNet
	 */
	public void setStorageRecoveryNet(java.math.BigDecimal storageRecoveryNet)
	{
		this.storageRecoveryNet = storageRecoveryNet;
	}

	/**
	 * Method 'getStorageRecoveryVat'
	 * 
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getStorageRecoveryVat()
	{
		return storageRecoveryVat;
	}

	/**
	 * Method 'setStorageRecoveryVat'
	 * 
	 * @param storageRecoveryVat
	 */
	public void setStorageRecoveryVat(java.math.BigDecimal storageRecoveryVat)
	{
		this.storageRecoveryVat = storageRecoveryVat;
	}

	/**
	 * Method 'getStorageRecoveryGross'
	 * 
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getStorageRecoveryGross()
	{
		return storageRecoveryGross;
	}

	/**
	 * Method 'setStorageRecoveryGross'
	 * 
	 * @param storageRecoveryGross
	 */
	public void setStorageRecoveryGross(java.math.BigDecimal storageRecoveryGross)
	{
		this.storageRecoveryGross = storageRecoveryGross;
	}

	/**
	 * Method 'getTotalNet'
	 * 
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getTotalNet()
	{
		return totalNet;
	}

	/**
	 * Method 'setTotalNet'
	 * 
	 * @param totalNet
	 */
	public void setTotalNet(java.math.BigDecimal totalNet)
	{
		this.totalNet = totalNet;
	}

	/**
	 * Method 'getTotalVat'
	 * 
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getTotalVat()
	{
		return totalVat;
	}

	/**
	 * Method 'setTotalVat'
	 * 
	 * @param totalVat
	 */
	public void setTotalVat(java.math.BigDecimal totalVat)
	{
		this.totalVat = totalVat;
	}

	/**
	 * Method 'getTotalGross'
	 * 
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getTotalGross()
	{
		return totalGross;
	}

	/**
	 * Method 'setTotalGross'
	 * 
	 * @param totalGross
	 */
	public void setTotalGross(java.math.BigDecimal totalGross)
	{
		this.totalGross = totalGross;
	}

	/**
	 * Method 'getClaimsHandlingInvoiceAmount'
	 * 
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getClaimsHandlingInvoiceAmount()
	{
		return claimsHandlingInvoiceAmount;
	}

	/**
	 * Method 'setClaimsHandlingInvoiceAmount'
	 * 
	 * @param claimsHandlingInvoiceAmount
	 */
	public void setClaimsHandlingInvoiceAmount(java.math.BigDecimal claimsHandlingInvoiceAmount)
	{
		this.claimsHandlingInvoiceAmount = claimsHandlingInvoiceAmount;
	}

	/**
	 * Method 'getDeductionForClaimsHandlingFee'
	 * 
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getDeductionForClaimsHandlingFee()
	{
		return deductionForClaimsHandlingFee;
	}

	/**
	 * Method 'setDeductionForClaimsHandlingFee'
	 * 
	 * @param deductionForClaimsHandlingFee
	 */
	public void setDeductionForClaimsHandlingFee(java.math.BigDecimal deductionForClaimsHandlingFee)
	{
		this.deductionForClaimsHandlingFee = deductionForClaimsHandlingFee;
	}

	/**
	 * Method 'getDiscount'
	 * 
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getDiscount()
	{
		return discount;
	}

	/**
	 * Method 'setDiscount'
	 * 
	 * @param discount
	 */
	public void setDiscount(java.math.BigDecimal discount)
	{
		this.discount = discount;
	}

	/**
	 * Method 'getTotaltoPay'
	 * 
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getTotaltoPay()
	{
		return totaltoPay;
	}

	/**
	 * Method 'setTotaltoPay'
	 * 
	 * @param totaltoPay
	 */
	public void setTotaltoPay(java.math.BigDecimal totaltoPay)
	{
		this.totaltoPay = totaltoPay;
	}

	/**
	 * Method 'getHandlingInvoiceNo'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getHandlingInvoiceNo()
	{
		return handlingInvoiceNo;
	}

	/**
	 * Method 'setHandlingInvoiceNo'
	 * 
	 * @param handlingInvoiceNo
	 */
	public void setHandlingInvoiceNo(java.lang.String handlingInvoiceNo)
	{
		this.handlingInvoiceNo = handlingInvoiceNo;
	}

	/**
	 * Method 'getClaimInvoiceNo'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getClaimInvoiceNo()
	{
		return claimInvoiceNo;
	}

	/**
	 * Method 'setClaimInvoiceNo'
	 * 
	 * @param claimInvoiceNo
	 */
	public void setClaimInvoiceNo(java.lang.String claimInvoiceNo)
	{
		this.claimInvoiceNo = claimInvoiceNo;
	}

	/**
	 * Method 'getCdwFee'
	 * 
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getCdwFee()
	{
		return cdwFee;
	}

	/**
	 * Method 'setCdwFee'
	 * 
	 * @param cdwFee
	 */
	public void setCdwFee(java.math.BigDecimal cdwFee)
	{
		this.cdwFee = cdwFee;
	}

	/**
	 * Method 'getCdwQty'
	 * 
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getCdwQty()
	{
		return cdwQty;
	}

	/**
	 * Method 'setCdwQty'
	 * 
	 * @param cdwQty
	 */
	public void setCdwQty(java.lang.Integer cdwQty)
	{
		this.cdwQty = cdwQty;
	}

	/**
	 * Method 'getAutomaticFee'
	 * 
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getAutomaticFee()
	{
		return automaticFee;
	}

	/**
	 * Method 'setAutomaticFee'
	 * 
	 * @param automaticFee
	 */
	public void setAutomaticFee(java.math.BigDecimal automaticFee)
	{
		this.automaticFee = automaticFee;
	}

	/**
	 * Method 'getAutomaticQty'
	 * 
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getAutomaticQty()
	{
		return automaticQty;
	}

	/**
	 * Method 'setAutomaticQty'
	 * 
	 * @param automaticQty
	 */
	public void setAutomaticQty(java.lang.Integer automaticQty)
	{
		this.automaticQty = automaticQty;
	}

	/**
	 * Method 'getSatNavFee'
	 * 
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getSatNavFee()
	{
		return satNavFee;
	}

	/**
	 * Method 'setSatNavFee'
	 * 
	 * @param satNavFee
	 */
	public void setSatNavFee(java.math.BigDecimal satNavFee)
	{
		this.satNavFee = satNavFee;
	}

	/**
	 * Method 'getSatNavQty'
	 * 
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getSatNavQty()
	{
		return satNavQty;
	}

	/**
	 * Method 'setSatNavQty'
	 * 
	 * @param satNavQty
	 */
	public void setSatNavQty(java.lang.Integer satNavQty)
	{
		this.satNavQty = satNavQty;
	}

	/**
	 * Method 'getEstateFee'
	 * 
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getEstateFee()
	{
		return estateFee;
	}

	/**
	 * Method 'setEstateFee'
	 * 
	 * @param estateFee
	 */
	public void setEstateFee(java.math.BigDecimal estateFee)
	{
		this.estateFee = estateFee;
	}

	/**
	 * Method 'getEstateQty'
	 * 
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getEstateQty()
	{
		return estateQty;
	}

	/**
	 * Method 'setEstateQty'
	 * 
	 * @param estateQty
	 */
	public void setEstateQty(java.lang.Integer estateQty)
	{
		this.estateQty = estateQty;
	}

	/**
	 * Method 'getBabySeatFee'
	 * 
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getBabySeatFee()
	{
		return babySeatFee;
	}

	/**
	 * Method 'setBabySeatFee'
	 * 
	 * @param babySeatFee
	 */
	public void setBabySeatFee(java.math.BigDecimal babySeatFee)
	{
		this.babySeatFee = babySeatFee;
	}

	/**
	 * Method 'getBabySeatQty'
	 * 
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getBabySeatQty()
	{
		return babySeatQty;
	}

	/**
	 * Method 'setBabySeatQty'
	 * 
	 * @param babySeatQty
	 */
	public void setBabySeatQty(java.lang.Integer babySeatQty)
	{
		this.babySeatQty = babySeatQty;
	}

	/**
	 * Method 'getTowBarsFee'
	 * 
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getTowBarsFee()
	{
		return towBarsFee;
	}

	/**
	 * Method 'setTowBarsFee'
	 * 
	 * @param towBarsFee
	 */
	public void setTowBarsFee(java.math.BigDecimal towBarsFee)
	{
		this.towBarsFee = towBarsFee;
	}

	/**
	 * Method 'getTowBarsQty'
	 * 
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getTowBarsQty()
	{
		return towBarsQty;
	}

	/**
	 * Method 'setTowBarsQty'
	 * 
	 * @param towBarsQty
	 */
	public void setTowBarsQty(java.lang.Integer towBarsQty)
	{
		this.towBarsQty = towBarsQty;
	}

	/**
	 * Method 'getNonStandardInsurancePremiumFee'
	 * 
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getNonStandardInsurancePremiumFee()
	{
		return nonStandardInsurancePremiumFee;
	}

	/**
	 * Method 'setNonStandardInsurancePremiumFee'
	 * 
	 * @param nonStandardInsurancePremiumFee
	 */
	public void setNonStandardInsurancePremiumFee(java.math.BigDecimal nonStandardInsurancePremiumFee)
	{
		this.nonStandardInsurancePremiumFee = nonStandardInsurancePremiumFee;
	}

	/**
	 * Method 'getNonStandardInsurancePremiumQty'
	 * 
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getNonStandardInsurancePremiumQty()
	{
		return nonStandardInsurancePremiumQty;
	}

	/**
	 * Method 'setNonStandardInsurancePremiumQty'
	 * 
	 * @param nonStandardInsurancePremiumQty
	 */
	public void setNonStandardInsurancePremiumQty(java.lang.Integer nonStandardInsurancePremiumQty)
	{
		this.nonStandardInsurancePremiumQty = nonStandardInsurancePremiumQty;
	}

	/**
	 * Method 'getAdminFee'
	 * 
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getAdminFee()
	{
		return adminFee;
	}

	/**
	 * Method 'setAdminFee'
	 * 
	 * @param adminFee
	 */
	public void setAdminFee(java.math.BigDecimal adminFee)
	{
		this.adminFee = adminFee;
	}

	/**
	 * Method 'getAdminQty'
	 * 
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getAdminQty()
	{
		return adminQty;
	}

	/**
	 * Method 'setAdminQty'
	 * 
	 * @param adminQty
	 */
	public void setAdminQty(java.lang.Integer adminQty)
	{
		this.adminQty = adminQty;
	}

	/**
	 * Method 'getRoofRackFee'
	 * 
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getRoofRackFee()
	{
		return roofRackFee;
	}

	/**
	 * Method 'setRoofRackFee'
	 * 
	 * @param roofRackFee
	 */
	public void setRoofRackFee(java.math.BigDecimal roofRackFee)
	{
		this.roofRackFee = roofRackFee;
	}

	/**
	 * Method 'getRoofRackQty'
	 * 
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getRoofRackQty()
	{
		return roofRackQty;
	}

	/**
	 * Method 'setRoofRackQty'
	 * 
	 * @param roofRackQty
	 */
	public void setRoofRackQty(java.lang.Integer roofRackQty)
	{
		this.roofRackQty = roofRackQty;
	}

	/**
	 * Method 'getDualControlFee'
	 * 
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getDualControlFee()
	{
		return dualControlFee;
	}

	/**
	 * Method 'setDualControlFee'
	 * 
	 * @param dualControlFee
	 */
	public void setDualControlFee(java.math.BigDecimal dualControlFee)
	{
		this.dualControlFee = dualControlFee;
	}

	/**
	 * Method 'getDualControlQty'
	 * 
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getDualControlQty()
	{
		return dualControlQty;
	}

	/**
	 * Method 'setDualControlQty'
	 * 
	 * @param dualControlQty
	 */
	public void setDualControlQty(java.lang.Integer dualControlQty)
	{
		this.dualControlQty = dualControlQty;
	}

	/**
	 * Method 'getDeliveryCollectionFee'
	 * 
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getDeliveryCollectionFee()
	{
		return deliveryCollectionFee;
	}

	/**
	 * Method 'setDeliveryCollectionFee'
	 * 
	 * @param deliveryCollectionFee
	 */
	public void setDeliveryCollectionFee(java.math.BigDecimal deliveryCollectionFee)
	{
		this.deliveryCollectionFee = deliveryCollectionFee;
	}

	/**
	 * Method 'getDeliveryCollectionQty'
	 * 
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getDeliveryCollectionQty()
	{
		return deliveryCollectionQty;
	}

	/**
	 * Method 'setDeliveryCollectionQty'
	 * 
	 * @param deliveryCollectionQty
	 */
	public void setDeliveryCollectionQty(java.lang.Integer deliveryCollectionQty)
	{
		this.deliveryCollectionQty = deliveryCollectionQty;
	}

	/**
	 * Method 'getCreatedBy'
	 * 
	 * @return int
	 */
	public int getCreatedBy()
	{
		return createdBy;
	}

	/**
	 * Method 'setCreatedBy'
	 * 
	 * @param createdBy
	 */
	public void setCreatedBy(int createdBy)
	{
		this.createdBy = createdBy;
	}

	/**
	 * Method 'getCreatedDate'
	 * 
	 * @return java.util.Date
	 */
	public java.util.Date getCreatedDate()
	{
		return createdDate;
	}

	/**
	 * Method 'setCreatedDate'
	 * 
	 * @param createdDate
	 */
	public void setCreatedDate(java.util.Date createdDate)
	{
		this.createdDate = createdDate;
	}

	/**
	 * Method 'getLastModifiedBy'
	 * 
	 * @return int
	 */
	public int getLastModifiedBy()
	{
		return lastModifiedBy;
	}

	/**
	 * Method 'setLastModifiedBy'
	 * 
	 * @param lastModifiedBy
	 */
	public void setLastModifiedBy(int lastModifiedBy)
	{
		this.lastModifiedBy = lastModifiedBy;
	}

	/**
	 * Method 'getLastModifiedDate'
	 * 
	 * @return java.util.Date
	 */
	public java.util.Date getLastModifiedDate()
	{
		return lastModifiedDate;
	}

	/**
	 * Method 'setLastModifiedDate'
	 * 
	 * @param lastModifiedDate
	 */
	public void setLastModifiedDate(java.util.Date lastModifiedDate)
	{
		this.lastModifiedDate = lastModifiedDate;
	}

}
