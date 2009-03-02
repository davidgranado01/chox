package scsbre.tests.sample;

import java.util.Date;

import scsbre.model.ICustomerVehicleDamageInfo;

public class CustomerVehicleDamageInfo implements ICustomerVehicleDamageInfo {

    protected Date initialECD;
    protected boolean isUsable;

    /* (non-Javadoc)
     * @see scsbre.model.ICustomerVehicleDamageInfo#getIsUsable()
     */
    public boolean getIsUsable() {
        return isUsable;
    }

    /* (non-Javadoc)
     * @see scsbre.model.ICustomerVehicleDamageInfo#setIsUsable(boolean)
     */
    public void setIsUsable(boolean isUsable) {
        this.isUsable = isUsable;
    }

    /* (non-Javadoc)
     * @see scsbre.model.ICustomerVehicleDamageInfo#getInitialECD()
     */
    public Date getInitialECD() {
        return initialECD;
    }

    /* (non-Javadoc)
     * @see scsbre.model.ICustomerVehicleDamageInfo#setInitialECD(java.util.Date)
     */
    public void setInitialECD(Date initialECD) {
        this.initialECD = initialECD;
    }

    public String getVehicleRegistration() {
        return "ABC1234";
    }

    public Boolean isVehicleRegistrationExist() {
        return false;
    }
}
