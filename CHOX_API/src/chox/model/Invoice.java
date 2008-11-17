package chox.model;

import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;
import java.util.Date;

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
	protected double hireNet;

	/** 
	 * This attribute maps to the column hireVat in the Invoice table.
	 */
	protected double hireVat;

	/** 
	 * This attribute maps to the column hireGross in the Invoice table.
	 */
	protected double hireGross;

	/** 
	 * This attribute maps to the column repairNet in the Invoice table.
	 */
	protected double repairNet;

	/** 
	 * This attribute maps to the column repairVat in the Invoice table.
	 */
	protected double repairVat;

	/** 
	 * This attribute maps to the column repairGross in the Invoice table.
	 */
	protected double repairGross;

	/** 
	 * This attribute maps to the column engineerFeeNet in the Invoice table.
	 */
	protected double engineerFeeNet;

	/** 
	 * This attribute maps to the column engineerFeeVat in the Invoice table.
	 */
	protected double engineerFeeVat;

	/** 
	 * This attribute maps to the column engineerFeeGross in the Invoice table.
	 */
	protected double engineerFeeGross;

	/** 
	 * This attribute maps to the column storageRecoveryNet in the Invoice table.
	 */
	protected double storageRecoveryNet;

	/** 
	 * This attribute maps to the column storageRecoveryVat in the Invoice table.
	 */
	protected double storageRecoveryVat;

	/** 
	 * This attribute maps to the column storageRecoveryGross in the Invoice table.
	 */
	protected double storageRecoveryGross;

	/** 
	 * This attribute maps to the column totalNet in the Invoice table.
	 */
	protected double totalNet;

	/** 
	 * This attribute maps to the column totalVat in the Invoice table.
	 */
	protected double totalVat;

	/** 
	 * This attribute maps to the column totalGross in the Invoice table.
	 */
	protected double totalGross;

	/** 
	 * This attribute maps to the column claimsHandlingInvoiceAmount in the Invoice table.
	 */
	protected double claimsHandlingInvoiceAmount;

	/** 
	 * This attribute maps to the column deductionForClaimsHandlingFee in the Invoice table.
	 */
	protected double deductionForClaimsHandlingFee;

	/** 
	 * This attribute maps to the column discount in the Invoice table.
	 */
	protected double discount;

	/** 
	 * This attribute maps to the column totaltoPay in the Invoice table.
	 */
	protected double totaltoPay;

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
	protected double cdwFee;

	/** 
	 * This attribute represents whether the primitive attribute cdwFee is null.
	 */
	protected boolean cdwFeeNull = true;

	/** 
	 * This attribute maps to the column cdwQty in the Invoice table.
	 */
	protected short cdwQty;

	/** 
	 * This attribute represents whether the primitive attribute cdwQty is null.
	 */
	protected boolean cdwQtyNull = true;

	/** 
	 * This attribute maps to the column automaticFee in the Invoice table.
	 */
	protected double automaticFee;

	/** 
	 * This attribute represents whether the primitive attribute automaticFee is null.
	 */
	protected boolean automaticFeeNull = true;

	/** 
	 * This attribute maps to the column automaticQty in the Invoice table.
	 */
	protected short automaticQty;

	/** 
	 * This attribute represents whether the primitive attribute automaticQty is null.
	 */
	protected boolean automaticQtyNull = true;

	/** 
	 * This attribute maps to the column satNavFee in the Invoice table.
	 */
	protected double satNavFee;

	/** 
	 * This attribute represents whether the primitive attribute satNavFee is null.
	 */
	protected boolean satNavFeeNull = true;

	/** 
	 * This attribute maps to the column satNavQty in the Invoice table.
	 */
	protected short satNavQty;

	/** 
	 * This attribute represents whether the primitive attribute satNavQty is null.
	 */
	protected boolean satNavQtyNull = true;

	/** 
	 * This attribute maps to the column estateFee in the Invoice table.
	 */
	protected double estateFee;

	/** 
	 * This attribute represents whether the primitive attribute estateFee is null.
	 */
	protected boolean estateFeeNull = true;

	/** 
	 * This attribute maps to the column estateQty in the Invoice table.
	 */
	protected short estateQty;

	/** 
	 * This attribute represents whether the primitive attribute estateQty is null.
	 */
	protected boolean estateQtyNull = true;

	/** 
	 * This attribute maps to the column babySeatFee in the Invoice table.
	 */
	protected double babySeatFee;

	/** 
	 * This attribute represents whether the primitive attribute babySeatFee is null.
	 */
	protected boolean babySeatFeeNull = true;

	/** 
	 * This attribute maps to the column babySeatQty in the Invoice table.
	 */
	protected short babySeatQty;

	/** 
	 * This attribute represents whether the primitive attribute babySeatQty is null.
	 */
	protected boolean babySeatQtyNull = true;

	/** 
	 * This attribute maps to the column towBarsFee in the Invoice table.
	 */
	protected double towBarsFee;

	/** 
	 * This attribute represents whether the primitive attribute towBarsFee is null.
	 */
	protected boolean towBarsFeeNull = true;

	/** 
	 * This attribute maps to the column towBarsQty in the Invoice table.
	 */
	protected short towBarsQty;

	/** 
	 * This attribute represents whether the primitive attribute towBarsQty is null.
	 */
	protected boolean towBarsQtyNull = true;

	/** 
	 * This attribute maps to the column nonStandardInsurancePremiumFee in the Invoice table.
	 */
	protected double nonStandardInsurancePremiumFee;

	/** 
	 * This attribute represents whether the primitive attribute nonStandardInsurancePremiumFee is null.
	 */
	protected boolean nonStandardInsurancePremiumFeeNull = true;

	/** 
	 * This attribute maps to the column nonStandardInsurancePremiumQty in the Invoice table.
	 */
	protected short nonStandardInsurancePremiumQty;

	/** 
	 * This attribute represents whether the primitive attribute nonStandardInsurancePremiumQty is null.
	 */
	protected boolean nonStandardInsurancePremiumQtyNull = true;

	/** 
	 * This attribute maps to the column adminFee in the Invoice table.
	 */
	protected double adminFee;

	/** 
	 * This attribute represents whether the primitive attribute adminFee is null.
	 */
	protected boolean adminFeeNull = true;

	/** 
	 * This attribute maps to the column adminQty in the Invoice table.
	 */
	protected short adminQty;

	/** 
	 * This attribute represents whether the primitive attribute adminQty is null.
	 */
	protected boolean adminQtyNull = true;

	/** 
	 * This attribute maps to the column roofRackFee in the Invoice table.
	 */
	protected double roofRackFee;

	/** 
	 * This attribute represents whether the primitive attribute roofRackFee is null.
	 */
	protected boolean roofRackFeeNull = true;

	/** 
	 * This attribute maps to the column roofRackQty in the Invoice table.
	 */
	protected short roofRackQty;

	/** 
	 * This attribute represents whether the primitive attribute roofRackQty is null.
	 */
	protected boolean roofRackQtyNull = true;

	/** 
	 * This attribute maps to the column dualControlFee in the Invoice table.
	 */
	protected double dualControlFee;

	/** 
	 * This attribute represents whether the primitive attribute dualControlFee is null.
	 */
	protected boolean dualControlFeeNull = true;

	/** 
	 * This attribute maps to the column dualControlQty in the Invoice table.
	 */
	protected short dualControlQty;

	/** 
	 * This attribute represents whether the primitive attribute dualControlQty is null.
	 */
	protected boolean dualControlQtyNull = true;

	/** 
	 * This attribute maps to the column deliveryCollectionFee in the Invoice table.
	 */
	protected double deliveryCollectionFee;

	/** 
	 * This attribute represents whether the primitive attribute deliveryCollectionFee is null.
	 */
	protected boolean deliveryCollectionFeeNull = true;

	/** 
	 * This attribute maps to the column deliveryCollectionQty in the Invoice table.
	 */
	protected short deliveryCollectionQty;

	/** 
	 * This attribute represents whether the primitive attribute deliveryCollectionQty is null.
	 */
	protected boolean deliveryCollectionQtyNull = true;

	/** 
	 * This attribute maps to the column createdBy in the Invoice table.
	 */
	protected int createdBy;

	/** 
	 * This attribute represents whether the primitive attribute createdBy is null.
	 */
	protected boolean createdByNull = true;

	/** 
	 * This attribute maps to the column createdDate in the Invoice table.
	 */
	protected Date createdDate;

	/** 
	 * This attribute maps to the column lastModifiedBy in the Invoice table.
	 */
	protected int lastModifiedBy;

	/** 
	 * This attribute represents whether the primitive attribute lastModifiedBy is null.
	 */
	protected boolean lastModifiedByNull = true;

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
	 * @return double
	 */
	public double getHireNet()
	{
		return hireNet;
	}

	/**
	 * Method 'setHireNet'
	 * 
	 * @param hireNet
	 */
	public void setHireNet(double hireNet)
	{
		this.hireNet = hireNet;
	}

	/**
	 * Method 'getHireVat'
	 * 
	 * @return double
	 */
	public double getHireVat()
	{
		return hireVat;
	}

	/**
	 * Method 'setHireVat'
	 * 
	 * @param hireVat
	 */
	public void setHireVat(double hireVat)
	{
		this.hireVat = hireVat;
	}

	/**
	 * Method 'getHireGross'
	 * 
	 * @return double
	 */
	public double getHireGross()
	{
		return hireGross;
	}

	/**
	 * Method 'setHireGross'
	 * 
	 * @param hireGross
	 */
	public void setHireGross(double hireGross)
	{
		this.hireGross = hireGross;
	}

	/**
	 * Method 'getRepairNet'
	 * 
	 * @return double
	 */
	public double getRepairNet()
	{
		return repairNet;
	}

	/**
	 * Method 'setRepairNet'
	 * 
	 * @param repairNet
	 */
	public void setRepairNet(double repairNet)
	{
		this.repairNet = repairNet;
	}

	/**
	 * Method 'getRepairVat'
	 * 
	 * @return double
	 */
	public double getRepairVat()
	{
		return repairVat;
	}

	/**
	 * Method 'setRepairVat'
	 * 
	 * @param repairVat
	 */
	public void setRepairVat(double repairVat)
	{
		this.repairVat = repairVat;
	}

	/**
	 * Method 'getRepairGross'
	 * 
	 * @return double
	 */
	public double getRepairGross()
	{
		return repairGross;
	}

	/**
	 * Method 'setRepairGross'
	 * 
	 * @param repairGross
	 */
	public void setRepairGross(double repairGross)
	{
		this.repairGross = repairGross;
	}

	/**
	 * Method 'getEngineerFeeNet'
	 * 
	 * @return double
	 */
	public double getEngineerFeeNet()
	{
		return engineerFeeNet;
	}

	/**
	 * Method 'setEngineerFeeNet'
	 * 
	 * @param engineerFeeNet
	 */
	public void setEngineerFeeNet(double engineerFeeNet)
	{
		this.engineerFeeNet = engineerFeeNet;
	}

	/**
	 * Method 'getEngineerFeeVat'
	 * 
	 * @return double
	 */
	public double getEngineerFeeVat()
	{
		return engineerFeeVat;
	}

	/**
	 * Method 'setEngineerFeeVat'
	 * 
	 * @param engineerFeeVat
	 */
	public void setEngineerFeeVat(double engineerFeeVat)
	{
		this.engineerFeeVat = engineerFeeVat;
	}

	/**
	 * Method 'getEngineerFeeGross'
	 * 
	 * @return double
	 */
	public double getEngineerFeeGross()
	{
		return engineerFeeGross;
	}

	/**
	 * Method 'setEngineerFeeGross'
	 * 
	 * @param engineerFeeGross
	 */
	public void setEngineerFeeGross(double engineerFeeGross)
	{
		this.engineerFeeGross = engineerFeeGross;
	}

	/**
	 * Method 'getStorageRecoveryNet'
	 * 
	 * @return double
	 */
	public double getStorageRecoveryNet()
	{
		return storageRecoveryNet;
	}

	/**
	 * Method 'setStorageRecoveryNet'
	 * 
	 * @param storageRecoveryNet
	 */
	public void setStorageRecoveryNet(double storageRecoveryNet)
	{
		this.storageRecoveryNet = storageRecoveryNet;
	}

	/**
	 * Method 'getStorageRecoveryVat'
	 * 
	 * @return double
	 */
	public double getStorageRecoveryVat()
	{
		return storageRecoveryVat;
	}

	/**
	 * Method 'setStorageRecoveryVat'
	 * 
	 * @param storageRecoveryVat
	 */
	public void setStorageRecoveryVat(double storageRecoveryVat)
	{
		this.storageRecoveryVat = storageRecoveryVat;
	}

	/**
	 * Method 'getStorageRecoveryGross'
	 * 
	 * @return double
	 */
	public double getStorageRecoveryGross()
	{
		return storageRecoveryGross;
	}

	/**
	 * Method 'setStorageRecoveryGross'
	 * 
	 * @param storageRecoveryGross
	 */
	public void setStorageRecoveryGross(double storageRecoveryGross)
	{
		this.storageRecoveryGross = storageRecoveryGross;
	}

	/**
	 * Method 'getTotalNet'
	 * 
	 * @return double
	 */
	public double getTotalNet()
	{
		return totalNet;
	}

	/**
	 * Method 'setTotalNet'
	 * 
	 * @param totalNet
	 */
	public void setTotalNet(double totalNet)
	{
		this.totalNet = totalNet;
	}

	/**
	 * Method 'getTotalVat'
	 * 
	 * @return double
	 */
	public double getTotalVat()
	{
		return totalVat;
	}

	/**
	 * Method 'setTotalVat'
	 * 
	 * @param totalVat
	 */
	public void setTotalVat(double totalVat)
	{
		this.totalVat = totalVat;
	}

	/**
	 * Method 'getTotalGross'
	 * 
	 * @return double
	 */
	public double getTotalGross()
	{
		return totalGross;
	}

	/**
	 * Method 'setTotalGross'
	 * 
	 * @param totalGross
	 */
	public void setTotalGross(double totalGross)
	{
		this.totalGross = totalGross;
	}

	/**
	 * Method 'getClaimsHandlingInvoiceAmount'
	 * 
	 * @return double
	 */
	public double getClaimsHandlingInvoiceAmount()
	{
		return claimsHandlingInvoiceAmount;
	}

	/**
	 * Method 'setClaimsHandlingInvoiceAmount'
	 * 
	 * @param claimsHandlingInvoiceAmount
	 */
	public void setClaimsHandlingInvoiceAmount(double claimsHandlingInvoiceAmount)
	{
		this.claimsHandlingInvoiceAmount = claimsHandlingInvoiceAmount;
	}

	/**
	 * Method 'getDeductionForClaimsHandlingFee'
	 * 
	 * @return double
	 */
	public double getDeductionForClaimsHandlingFee()
	{
		return deductionForClaimsHandlingFee;
	}

	/**
	 * Method 'setDeductionForClaimsHandlingFee'
	 * 
	 * @param deductionForClaimsHandlingFee
	 */
	public void setDeductionForClaimsHandlingFee(double deductionForClaimsHandlingFee)
	{
		this.deductionForClaimsHandlingFee = deductionForClaimsHandlingFee;
	}

	/**
	 * Method 'getDiscount'
	 * 
	 * @return double
	 */
	public double getDiscount()
	{
		return discount;
	}

	/**
	 * Method 'setDiscount'
	 * 
	 * @param discount
	 */
	public void setDiscount(double discount)
	{
		this.discount = discount;
	}

	/**
	 * Method 'getTotaltoPay'
	 * 
	 * @return double
	 */
	public double getTotaltoPay()
	{
		return totaltoPay;
	}

	/**
	 * Method 'setTotaltoPay'
	 * 
	 * @param totaltoPay
	 */
	public void setTotaltoPay(double totaltoPay)
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
	 * @return double
	 */
	public double getCdwFee()
	{
		return cdwFee;
	}

	/**
	 * Method 'setCdwFee'
	 * 
	 * @param cdwFee
	 */
	public void setCdwFee(double cdwFee)
	{
		this.cdwFee = cdwFee;
		this.cdwFeeNull = false;
	}

	/** 
	 * Sets the value of cdwFeeNull
	 */
	public void setCdwFeeNull(boolean cdwFeeNull)
	{
		this.cdwFeeNull = cdwFeeNull;
	}

	/** 
	 * Gets the value of cdwFeeNull
	 */
	public boolean isCdwFeeNull()
	{
		return cdwFeeNull;
	}

	/**
	 * Method 'getCdwQty'
	 * 
	 * @return short
	 */
	public short getCdwQty()
	{
		return cdwQty;
	}

	/**
	 * Method 'setCdwQty'
	 * 
	 * @param cdwQty
	 */
	public void setCdwQty(short cdwQty)
	{
		this.cdwQty = cdwQty;
		this.cdwQtyNull = false;
	}

	/** 
	 * Sets the value of cdwQtyNull
	 */
	public void setCdwQtyNull(boolean cdwQtyNull)
	{
		this.cdwQtyNull = cdwQtyNull;
	}

	/** 
	 * Gets the value of cdwQtyNull
	 */
	public boolean isCdwQtyNull()
	{
		return cdwQtyNull;
	}

	/**
	 * Method 'getAutomaticFee'
	 * 
	 * @return double
	 */
	public double getAutomaticFee()
	{
		return automaticFee;
	}

	/**
	 * Method 'setAutomaticFee'
	 * 
	 * @param automaticFee
	 */
	public void setAutomaticFee(double automaticFee)
	{
		this.automaticFee = automaticFee;
		this.automaticFeeNull = false;
	}

	/** 
	 * Sets the value of automaticFeeNull
	 */
	public void setAutomaticFeeNull(boolean automaticFeeNull)
	{
		this.automaticFeeNull = automaticFeeNull;
	}

	/** 
	 * Gets the value of automaticFeeNull
	 */
	public boolean isAutomaticFeeNull()
	{
		return automaticFeeNull;
	}

	/**
	 * Method 'getAutomaticQty'
	 * 
	 * @return short
	 */
	public short getAutomaticQty()
	{
		return automaticQty;
	}

	/**
	 * Method 'setAutomaticQty'
	 * 
	 * @param automaticQty
	 */
	public void setAutomaticQty(short automaticQty)
	{
		this.automaticQty = automaticQty;
		this.automaticQtyNull = false;
	}

	/** 
	 * Sets the value of automaticQtyNull
	 */
	public void setAutomaticQtyNull(boolean automaticQtyNull)
	{
		this.automaticQtyNull = automaticQtyNull;
	}

	/** 
	 * Gets the value of automaticQtyNull
	 */
	public boolean isAutomaticQtyNull()
	{
		return automaticQtyNull;
	}

	/**
	 * Method 'getSatNavFee'
	 * 
	 * @return double
	 */
	public double getSatNavFee()
	{
		return satNavFee;
	}

	/**
	 * Method 'setSatNavFee'
	 * 
	 * @param satNavFee
	 */
	public void setSatNavFee(double satNavFee)
	{
		this.satNavFee = satNavFee;
		this.satNavFeeNull = false;
	}

	/** 
	 * Sets the value of satNavFeeNull
	 */
	public void setSatNavFeeNull(boolean satNavFeeNull)
	{
		this.satNavFeeNull = satNavFeeNull;
	}

	/** 
	 * Gets the value of satNavFeeNull
	 */
	public boolean isSatNavFeeNull()
	{
		return satNavFeeNull;
	}

	/**
	 * Method 'getSatNavQty'
	 * 
	 * @return short
	 */
	public short getSatNavQty()
	{
		return satNavQty;
	}

	/**
	 * Method 'setSatNavQty'
	 * 
	 * @param satNavQty
	 */
	public void setSatNavQty(short satNavQty)
	{
		this.satNavQty = satNavQty;
		this.satNavQtyNull = false;
	}

	/** 
	 * Sets the value of satNavQtyNull
	 */
	public void setSatNavQtyNull(boolean satNavQtyNull)
	{
		this.satNavQtyNull = satNavQtyNull;
	}

	/** 
	 * Gets the value of satNavQtyNull
	 */
	public boolean isSatNavQtyNull()
	{
		return satNavQtyNull;
	}

	/**
	 * Method 'getEstateFee'
	 * 
	 * @return double
	 */
	public double getEstateFee()
	{
		return estateFee;
	}

	/**
	 * Method 'setEstateFee'
	 * 
	 * @param estateFee
	 */
	public void setEstateFee(double estateFee)
	{
		this.estateFee = estateFee;
		this.estateFeeNull = false;
	}

	/** 
	 * Sets the value of estateFeeNull
	 */
	public void setEstateFeeNull(boolean estateFeeNull)
	{
		this.estateFeeNull = estateFeeNull;
	}

	/** 
	 * Gets the value of estateFeeNull
	 */
	public boolean isEstateFeeNull()
	{
		return estateFeeNull;
	}

	/**
	 * Method 'getEstateQty'
	 * 
	 * @return short
	 */
	public short getEstateQty()
	{
		return estateQty;
	}

	/**
	 * Method 'setEstateQty'
	 * 
	 * @param estateQty
	 */
	public void setEstateQty(short estateQty)
	{
		this.estateQty = estateQty;
		this.estateQtyNull = false;
	}

	/** 
	 * Sets the value of estateQtyNull
	 */
	public void setEstateQtyNull(boolean estateQtyNull)
	{
		this.estateQtyNull = estateQtyNull;
	}

	/** 
	 * Gets the value of estateQtyNull
	 */
	public boolean isEstateQtyNull()
	{
		return estateQtyNull;
	}

	/**
	 * Method 'getBabySeatFee'
	 * 
	 * @return double
	 */
	public double getBabySeatFee()
	{
		return babySeatFee;
	}

	/**
	 * Method 'setBabySeatFee'
	 * 
	 * @param babySeatFee
	 */
	public void setBabySeatFee(double babySeatFee)
	{
		this.babySeatFee = babySeatFee;
		this.babySeatFeeNull = false;
	}

	/** 
	 * Sets the value of babySeatFeeNull
	 */
	public void setBabySeatFeeNull(boolean babySeatFeeNull)
	{
		this.babySeatFeeNull = babySeatFeeNull;
	}

	/** 
	 * Gets the value of babySeatFeeNull
	 */
	public boolean isBabySeatFeeNull()
	{
		return babySeatFeeNull;
	}

	/**
	 * Method 'getBabySeatQty'
	 * 
	 * @return short
	 */
	public short getBabySeatQty()
	{
		return babySeatQty;
	}

	/**
	 * Method 'setBabySeatQty'
	 * 
	 * @param babySeatQty
	 */
	public void setBabySeatQty(short babySeatQty)
	{
		this.babySeatQty = babySeatQty;
		this.babySeatQtyNull = false;
	}

	/** 
	 * Sets the value of babySeatQtyNull
	 */
	public void setBabySeatQtyNull(boolean babySeatQtyNull)
	{
		this.babySeatQtyNull = babySeatQtyNull;
	}

	/** 
	 * Gets the value of babySeatQtyNull
	 */
	public boolean isBabySeatQtyNull()
	{
		return babySeatQtyNull;
	}

	/**
	 * Method 'getTowBarsFee'
	 * 
	 * @return double
	 */
	public double getTowBarsFee()
	{
		return towBarsFee;
	}

	/**
	 * Method 'setTowBarsFee'
	 * 
	 * @param towBarsFee
	 */
	public void setTowBarsFee(double towBarsFee)
	{
		this.towBarsFee = towBarsFee;
		this.towBarsFeeNull = false;
	}

	/** 
	 * Sets the value of towBarsFeeNull
	 */
	public void setTowBarsFeeNull(boolean towBarsFeeNull)
	{
		this.towBarsFeeNull = towBarsFeeNull;
	}

	/** 
	 * Gets the value of towBarsFeeNull
	 */
	public boolean isTowBarsFeeNull()
	{
		return towBarsFeeNull;
	}

	/**
	 * Method 'getTowBarsQty'
	 * 
	 * @return short
	 */
	public short getTowBarsQty()
	{
		return towBarsQty;
	}

	/**
	 * Method 'setTowBarsQty'
	 * 
	 * @param towBarsQty
	 */
	public void setTowBarsQty(short towBarsQty)
	{
		this.towBarsQty = towBarsQty;
		this.towBarsQtyNull = false;
	}

	/** 
	 * Sets the value of towBarsQtyNull
	 */
	public void setTowBarsQtyNull(boolean towBarsQtyNull)
	{
		this.towBarsQtyNull = towBarsQtyNull;
	}

	/** 
	 * Gets the value of towBarsQtyNull
	 */
	public boolean isTowBarsQtyNull()
	{
		return towBarsQtyNull;
	}

	/**
	 * Method 'getNonStandardInsurancePremiumFee'
	 * 
	 * @return double
	 */
	public double getNonStandardInsurancePremiumFee()
	{
		return nonStandardInsurancePremiumFee;
	}

	/**
	 * Method 'setNonStandardInsurancePremiumFee'
	 * 
	 * @param nonStandardInsurancePremiumFee
	 */
	public void setNonStandardInsurancePremiumFee(double nonStandardInsurancePremiumFee)
	{
		this.nonStandardInsurancePremiumFee = nonStandardInsurancePremiumFee;
		this.nonStandardInsurancePremiumFeeNull = false;
	}

	/** 
	 * Sets the value of nonStandardInsurancePremiumFeeNull
	 */
	public void setNonStandardInsurancePremiumFeeNull(boolean nonStandardInsurancePremiumFeeNull)
	{
		this.nonStandardInsurancePremiumFeeNull = nonStandardInsurancePremiumFeeNull;
	}

	/** 
	 * Gets the value of nonStandardInsurancePremiumFeeNull
	 */
	public boolean isNonStandardInsurancePremiumFeeNull()
	{
		return nonStandardInsurancePremiumFeeNull;
	}

	/**
	 * Method 'getNonStandardInsurancePremiumQty'
	 * 
	 * @return short
	 */
	public short getNonStandardInsurancePremiumQty()
	{
		return nonStandardInsurancePremiumQty;
	}

	/**
	 * Method 'setNonStandardInsurancePremiumQty'
	 * 
	 * @param nonStandardInsurancePremiumQty
	 */
	public void setNonStandardInsurancePremiumQty(short nonStandardInsurancePremiumQty)
	{
		this.nonStandardInsurancePremiumQty = nonStandardInsurancePremiumQty;
		this.nonStandardInsurancePremiumQtyNull = false;
	}

	/** 
	 * Sets the value of nonStandardInsurancePremiumQtyNull
	 */
	public void setNonStandardInsurancePremiumQtyNull(boolean nonStandardInsurancePremiumQtyNull)
	{
		this.nonStandardInsurancePremiumQtyNull = nonStandardInsurancePremiumQtyNull;
	}

	/** 
	 * Gets the value of nonStandardInsurancePremiumQtyNull
	 */
	public boolean isNonStandardInsurancePremiumQtyNull()
	{
		return nonStandardInsurancePremiumQtyNull;
	}

	/**
	 * Method 'getAdminFee'
	 * 
	 * @return double
	 */
	public double getAdminFee()
	{
		return adminFee;
	}

	/**
	 * Method 'setAdminFee'
	 * 
	 * @param adminFee
	 */
	public void setAdminFee(double adminFee)
	{
		this.adminFee = adminFee;
		this.adminFeeNull = false;
	}

	/** 
	 * Sets the value of adminFeeNull
	 */
	public void setAdminFeeNull(boolean adminFeeNull)
	{
		this.adminFeeNull = adminFeeNull;
	}

	/** 
	 * Gets the value of adminFeeNull
	 */
	public boolean isAdminFeeNull()
	{
		return adminFeeNull;
	}

	/**
	 * Method 'getAdminQty'
	 * 
	 * @return short
	 */
	public short getAdminQty()
	{
		return adminQty;
	}

	/**
	 * Method 'setAdminQty'
	 * 
	 * @param adminQty
	 */
	public void setAdminQty(short adminQty)
	{
		this.adminQty = adminQty;
		this.adminQtyNull = false;
	}

	/** 
	 * Sets the value of adminQtyNull
	 */
	public void setAdminQtyNull(boolean adminQtyNull)
	{
		this.adminQtyNull = adminQtyNull;
	}

	/** 
	 * Gets the value of adminQtyNull
	 */
	public boolean isAdminQtyNull()
	{
		return adminQtyNull;
	}

	/**
	 * Method 'getRoofRackFee'
	 * 
	 * @return double
	 */
	public double getRoofRackFee()
	{
		return roofRackFee;
	}

	/**
	 * Method 'setRoofRackFee'
	 * 
	 * @param roofRackFee
	 */
	public void setRoofRackFee(double roofRackFee)
	{
		this.roofRackFee = roofRackFee;
		this.roofRackFeeNull = false;
	}

	/** 
	 * Sets the value of roofRackFeeNull
	 */
	public void setRoofRackFeeNull(boolean roofRackFeeNull)
	{
		this.roofRackFeeNull = roofRackFeeNull;
	}

	/** 
	 * Gets the value of roofRackFeeNull
	 */
	public boolean isRoofRackFeeNull()
	{
		return roofRackFeeNull;
	}

	/**
	 * Method 'getRoofRackQty'
	 * 
	 * @return short
	 */
	public short getRoofRackQty()
	{
		return roofRackQty;
	}

	/**
	 * Method 'setRoofRackQty'
	 * 
	 * @param roofRackQty
	 */
	public void setRoofRackQty(short roofRackQty)
	{
		this.roofRackQty = roofRackQty;
		this.roofRackQtyNull = false;
	}

	/** 
	 * Sets the value of roofRackQtyNull
	 */
	public void setRoofRackQtyNull(boolean roofRackQtyNull)
	{
		this.roofRackQtyNull = roofRackQtyNull;
	}

	/** 
	 * Gets the value of roofRackQtyNull
	 */
	public boolean isRoofRackQtyNull()
	{
		return roofRackQtyNull;
	}

	/**
	 * Method 'getDualControlFee'
	 * 
	 * @return double
	 */
	public double getDualControlFee()
	{
		return dualControlFee;
	}

	/**
	 * Method 'setDualControlFee'
	 * 
	 * @param dualControlFee
	 */
	public void setDualControlFee(double dualControlFee)
	{
		this.dualControlFee = dualControlFee;
		this.dualControlFeeNull = false;
	}

	/** 
	 * Sets the value of dualControlFeeNull
	 */
	public void setDualControlFeeNull(boolean dualControlFeeNull)
	{
		this.dualControlFeeNull = dualControlFeeNull;
	}

	/** 
	 * Gets the value of dualControlFeeNull
	 */
	public boolean isDualControlFeeNull()
	{
		return dualControlFeeNull;
	}

	/**
	 * Method 'getDualControlQty'
	 * 
	 * @return short
	 */
	public short getDualControlQty()
	{
		return dualControlQty;
	}

	/**
	 * Method 'setDualControlQty'
	 * 
	 * @param dualControlQty
	 */
	public void setDualControlQty(short dualControlQty)
	{
		this.dualControlQty = dualControlQty;
		this.dualControlQtyNull = false;
	}

	/** 
	 * Sets the value of dualControlQtyNull
	 */
	public void setDualControlQtyNull(boolean dualControlQtyNull)
	{
		this.dualControlQtyNull = dualControlQtyNull;
	}

	/** 
	 * Gets the value of dualControlQtyNull
	 */
	public boolean isDualControlQtyNull()
	{
		return dualControlQtyNull;
	}

	/**
	 * Method 'getDeliveryCollectionFee'
	 * 
	 * @return double
	 */
	public double getDeliveryCollectionFee()
	{
		return deliveryCollectionFee;
	}

	/**
	 * Method 'setDeliveryCollectionFee'
	 * 
	 * @param deliveryCollectionFee
	 */
	public void setDeliveryCollectionFee(double deliveryCollectionFee)
	{
		this.deliveryCollectionFee = deliveryCollectionFee;
		this.deliveryCollectionFeeNull = false;
	}

	/** 
	 * Sets the value of deliveryCollectionFeeNull
	 */
	public void setDeliveryCollectionFeeNull(boolean deliveryCollectionFeeNull)
	{
		this.deliveryCollectionFeeNull = deliveryCollectionFeeNull;
	}

	/** 
	 * Gets the value of deliveryCollectionFeeNull
	 */
	public boolean isDeliveryCollectionFeeNull()
	{
		return deliveryCollectionFeeNull;
	}

	/**
	 * Method 'getDeliveryCollectionQty'
	 * 
	 * @return short
	 */
	public short getDeliveryCollectionQty()
	{
		return deliveryCollectionQty;
	}

	/**
	 * Method 'setDeliveryCollectionQty'
	 * 
	 * @param deliveryCollectionQty
	 */
	public void setDeliveryCollectionQty(short deliveryCollectionQty)
	{
		this.deliveryCollectionQty = deliveryCollectionQty;
		this.deliveryCollectionQtyNull = false;
	}

	/** 
	 * Sets the value of deliveryCollectionQtyNull
	 */
	public void setDeliveryCollectionQtyNull(boolean deliveryCollectionQtyNull)
	{
		this.deliveryCollectionQtyNull = deliveryCollectionQtyNull;
	}

	/** 
	 * Gets the value of deliveryCollectionQtyNull
	 */
	public boolean isDeliveryCollectionQtyNull()
	{
		return deliveryCollectionQtyNull;
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
		this.createdByNull = false;
	}

	/** 
	 * Sets the value of createdByNull
	 */
	public void setCreatedByNull(boolean createdByNull)
	{
		this.createdByNull = createdByNull;
	}

	/** 
	 * Gets the value of createdByNull
	 */
	public boolean isCreatedByNull()
	{
		return createdByNull;
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
		this.lastModifiedByNull = false;
	}

	/** 
	 * Sets the value of lastModifiedByNull
	 */
	public void setLastModifiedByNull(boolean lastModifiedByNull)
	{
		this.lastModifiedByNull = lastModifiedByNull;
	}

	/** 
	 * Gets the value of lastModifiedByNull
	 */
	public boolean isLastModifiedByNull()
	{
		return lastModifiedByNull;
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
