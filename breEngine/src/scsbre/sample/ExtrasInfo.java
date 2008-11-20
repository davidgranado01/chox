package scsbre.sample;

import java.math.BigDecimal;

import scsbre.model.IExtrasInfo;

public class ExtrasInfo implements IExtrasInfo {

    private BigDecimal cdwFee;
    private int cdwQty;
    private BigDecimal automaticFee;
    private int automaticQty;
    private BigDecimal satNavFee;
    private int satNavQty;
    private BigDecimal estateFee;
    private int estateQty;
    private BigDecimal babySeatFee;
    private int babySeatQty;
    private BigDecimal towBarsFee;
    private int towBarsQty;
    private BigDecimal nonStandardInsurancePremiumFee;
    private int nonStandardInsurancePremiumQty;
    private BigDecimal adminFee;
    private int adminQty;
    private BigDecimal roofRackFee;
    private int roofRackQty;
    private BigDecimal dualControlFee;
    private int dualControlQty;
    private BigDecimal deliveryCollectionFee;
    private BigDecimal deliveryCollectionQty;

    /* standard accessors */
    /* (non-Javadoc)
     * @see scsbre.model.IExtrasInfo#getCdwFee()
     */
    public BigDecimal getCdwFee() {
        return cdwFee;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IExtrasInfo#setCdwFee(java.math.BigDecimal)
     */

    public void setCdwFee(BigDecimal cdwFee) {
        this.cdwFee = cdwFee;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IExtrasInfo#getCdwQty()
     */

    public int getCdwQty() {
        return cdwQty;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IExtrasInfo#setCdwQty(int)
     */

    public void setCdwQty(int cdwQty) {
        this.cdwQty = cdwQty;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IExtrasInfo#getAutomaticFee()
     */

    public BigDecimal getAutomaticFee() {
        return automaticFee;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IExtrasInfo#setAutomaticFee(java.math.BigDecimal)
     */

    public void setAutomaticFee(BigDecimal automaticFee) {
        this.automaticFee = automaticFee;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IExtrasInfo#getAutomaticQty()
     */

    public int getAutomaticQty() {
        return automaticQty;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IExtrasInfo#setAutomaticQty(int)
     */

    public void setAutomaticQty(int automaticQty) {
        this.automaticQty = automaticQty;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IExtrasInfo#getSatNavFee()
     */

    public BigDecimal getSatNavFee() {
        return satNavFee;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IExtrasInfo#setSatNavFee(java.math.BigDecimal)
     */

    public void setSatNavFee(BigDecimal satNavFee) {
        this.satNavFee = satNavFee;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IExtrasInfo#getSatNavQty()
     */

    public int getSatNavQty() {
        return satNavQty;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IExtrasInfo#setSatNavQty(int)
     */

    public void setSatNavQty(int satNavQty) {
        this.satNavQty = satNavQty;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IExtrasInfo#getEstateFee()
     */

    public BigDecimal getEstateFee() {
        return estateFee;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IExtrasInfo#setEstateFee(java.math.BigDecimal)
     */

    public void setEstateFee(BigDecimal estateFee) {
        this.estateFee = estateFee;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IExtrasInfo#getEstateQty()
     */

    public int getEstateQty() {
        return estateQty;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IExtrasInfo#setEstateQty(int)
     */

    public void setEstateQty(int estateQty) {
        this.estateQty = estateQty;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IExtrasInfo#getBabySeatFee()
     */

    public BigDecimal getBabySeatFee() {
        return babySeatFee;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IExtrasInfo#setBabySeatFee(java.math.BigDecimal)
     */

    public void setBabySeatFee(BigDecimal babySeatFee) {
        this.babySeatFee = babySeatFee;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IExtrasInfo#getBabySeatQty()
     */

    public int getBabySeatQty() {
        return babySeatQty;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IExtrasInfo#setBabySeatQty(int)
     */

    public void setBabySeatQty(int babySeatQty) {
        this.babySeatQty = babySeatQty;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IExtrasInfo#getTowBarsFee()
     */

    public BigDecimal getTowBarsFee() {
        return towBarsFee;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IExtrasInfo#setTowBarsFee(java.math.BigDecimal)
     */

    public void setTowBarsFee(BigDecimal towBarsFee) {
        this.towBarsFee = towBarsFee;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IExtrasInfo#getTowBarsQty()
     */

    public int getTowBarsQty() {
        return towBarsQty;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IExtrasInfo#setTowBarsQty(int)
     */

    public void setTowBarsQty(int towBarsQty) {
        this.towBarsQty = towBarsQty;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IExtrasInfo#getNonStandardInsurancePremiumFee()
     */

    public BigDecimal getNonStandardInsurancePremiumFee() {
        return nonStandardInsurancePremiumFee;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IExtrasInfo#setNonStandardInsurancePremiumFee(java.math.BigDecimal)
     */

    public void setNonStandardInsurancePremiumFee(
            BigDecimal nonStandardInsurancePremiumFee) {
        this.nonStandardInsurancePremiumFee = nonStandardInsurancePremiumFee;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IExtrasInfo#getNonStandardInsurancePremiumQty()
     */

    public int getNonStandardInsurancePremiumQty() {
        return nonStandardInsurancePremiumQty;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IExtrasInfo#setNonStandardInsurancePremiumQty(int)
     */

    public void setNonStandardInsurancePremiumQty(
            int nonStandardInsurancePremiumQty) {
        this.nonStandardInsurancePremiumQty = nonStandardInsurancePremiumQty;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IExtrasInfo#getAdminFee()
     */

    public BigDecimal getAdminFee() {
        return adminFee;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IExtrasInfo#setAdminFee(java.math.BigDecimal)
     */

    public void setAdminFee(BigDecimal adminFee) {
        this.adminFee = adminFee;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IExtrasInfo#getAdminQty()
     */

    public int getAdminQty() {
        return adminQty;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IExtrasInfo#setAdminQty(int)
     */

    public void setAdminQty(int adminQty) {
        this.adminQty = adminQty;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IExtrasInfo#getRoofRackFee()
     */

    public BigDecimal getRoofRackFee() {
        return roofRackFee;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IExtrasInfo#setRoofRackFee(java.math.BigDecimal)
     */

    public void setRoofRackFee(BigDecimal roofRackFee) {
        this.roofRackFee = roofRackFee;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IExtrasInfo#getRoofRackQty()
     */

    public int getRoofRackQty() {
        return roofRackQty;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IExtrasInfo#setRoofRackQty(int)
     */

    public void setRoofRackQty(int roofRackQty) {
        this.roofRackQty = roofRackQty;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IExtrasInfo#getDualControlFee()
     */

    public BigDecimal getDualControlFee() {
        return dualControlFee;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IExtrasInfo#setDualControlFee(java.math.BigDecimal)
     */

    public void setDualControlFee(BigDecimal dualControlFee) {
        this.dualControlFee = dualControlFee;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IExtrasInfo#getDualControlQty()
     */

    public int getDualControlQty() {
        return dualControlQty;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IExtrasInfo#setDualControlQty(int)
     */

    public void setDualControlQty(int dualControlQty) {
        this.dualControlQty = dualControlQty;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IExtrasInfo#getDeliveryCollectionFee()
     */

    public BigDecimal getDeliveryCollectionFee() {
        return deliveryCollectionFee;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IExtrasInfo#setDeliveryCollectionFee(java.math.BigDecimal)
     */

    public void setDeliveryCollectionFee(BigDecimal deliveryCollectionFee) {
        this.deliveryCollectionFee = deliveryCollectionFee;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IExtrasInfo#getDeliveryCollectionQty()
     */

    public BigDecimal getDeliveryCollectionQty() {
        return deliveryCollectionQty;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IExtrasInfo#setDeliveryCollectionQty(java.math.BigDecimal)
     */

    public void setDeliveryCollectionQty(BigDecimal deliveryCollectionQty) {
        this.deliveryCollectionQty = deliveryCollectionQty;
    }

    /*-------------- non-standard methods ----------------------------*/
    /* (non-Javadoc)
     * @see scsbre.model.IExtrasInfo#getTotalExtras()
     */
    public BigDecimal getTotalExtras() {


        BigDecimal total =BigDecimal.ZERO;


        total = total.add(cdwFee);
        total = total.add(automaticFee);
        total = total.add(satNavFee);
        total = total.add(estateFee);
        total = total.add(babySeatFee);
        total = total.add(towBarsFee);
        total = total.add(nonStandardInsurancePremiumFee);
        total = total.add(adminFee);
        total = total.add(roofRackFee);
        total = total.add(dualControlFee);
        total = total.add(deliveryCollectionFee);

        return total;


    }
}
