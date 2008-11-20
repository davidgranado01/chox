package scsbre.model;

import java.math.BigDecimal;

public interface IExtrasInfo {

    public BigDecimal getCdwFee();

    public void setCdwFee(BigDecimal cdwFee);

    public int getCdwQty();

    public void setCdwQty(int cdwQty);

    public BigDecimal getAutomaticFee();

    public void setAutomaticFee(BigDecimal automaticFee);

    public int getAutomaticQty();

    public void setAutomaticQty(int automaticQty);

    public BigDecimal getSatNavFee();

    public void setSatNavFee(BigDecimal satNavFee);

    public int getSatNavQty();

    public void setSatNavQty(int satNavQty);

    public BigDecimal getEstateFee();

    public void setEstateFee(BigDecimal estateFee);

    public int getEstateQty();

    public void setEstateQty(int estateQty);

    public BigDecimal getBabySeatFee();

    public void setBabySeatFee(BigDecimal babySeatFee);

    public int getBabySeatQty();

    public void setBabySeatQty(int babySeatQty);

    public BigDecimal getTowBarsFee();

    public void setTowBarsFee(BigDecimal towBarsFee);

    public int getTowBarsQty();

    public void setTowBarsQty(int towBarsQty);

    public BigDecimal getNonStandardInsurancePremiumFee();

    public void setNonStandardInsurancePremiumFee(
            BigDecimal nonStandardInsurancePremiumFee);

    public int getNonStandardInsurancePremiumQty();

    public void setNonStandardInsurancePremiumQty(
            int nonStandardInsurancePremiumQty);

    public BigDecimal getAdminFee();

    public void setAdminFee(BigDecimal adminFee);

    public int getAdminQty();

    public void setAdminQty(int adminQty);

    public BigDecimal getRoofRackFee();

    public void setRoofRackFee(BigDecimal roofRackFee);

    public int getRoofRackQty();

    public void setRoofRackQty(int roofRackQty);

    public BigDecimal getDualControlFee();

    public void setDualControlFee(BigDecimal dualControlFee);

    public int getDualControlQty();

    public void setDualControlQty(int dualControlQty);

    public BigDecimal getDeliveryCollectionFee();

    public void setDeliveryCollectionFee(BigDecimal deliveryCollectionFee);

    public BigDecimal getDeliveryCollectionQty();

    public void setDeliveryCollectionQty(BigDecimal deliveryCollectionQty);

    public BigDecimal getTotalExtras();
}