package scsbre.model;

import java.util.Date;

public interface ICustomerVehicleDamageInfo {

    public boolean getIsUsable();

    public Date getInitialECD();
    
    public String getVehicleRegistration();
    public Boolean isVehicleRegistrationExist();

}