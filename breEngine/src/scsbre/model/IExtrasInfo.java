package scsbre.model;

import java.math.BigDecimal;

public interface IExtrasInfo {

    public BigDecimal getCdwFee();

    public void setCdwFee(BigDecimal cdwFee);

    public int getCdwQty();

    public BigDecimal getAutomaticFee();

    public int getAutomaticQty();

    public BigDecimal getSatNavFee();

    public int getSatNavQty();

    public BigDecimal getEstateFee();

    public int getEstateQty();

    public BigDecimal getBabySeatFee();

    public int getBabySeatQty();

    public BigDecimal getTowBarsFee();

    public int getTowBarsQty();

    public BigDecimal getNonStandardInsurancePremiumFee();

    public int getNonStandardInsurancePremiumQty();

    public BigDecimal getAdminFee();

    public int getAdminQty();

    public BigDecimal getRoofRackFee();

    public int getRoofRackQty();

    public BigDecimal getDualControlFee();

    public int getDualControlQty();

    public BigDecimal getDeliveryCollectionFee();

    public int getDeliveryCollectionQty();

}