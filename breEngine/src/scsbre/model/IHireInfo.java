package scsbre.model;

import java.util.Date;

public interface IHireInfo {

    public IVehicleClassInfo getVClass();
    public Date getRentalStart();
    public Date getRentalEnd();
    public int getDays();
    public boolean getIsTotalLoss();
    
}