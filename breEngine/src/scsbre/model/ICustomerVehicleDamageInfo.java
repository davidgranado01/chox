package scsbre.model;

import java.util.Date;

public interface ICustomerVehicleDamageInfo {

    public boolean getIsUsable();

    public void setIsUsable(boolean isUsable);

    public Date getInitialECD();

    public void setInitialECD(Date initialECD);
}