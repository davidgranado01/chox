package scsbre.model;

import java.util.Date;

public interface IHireInfo {

    public IVehicleClassInfo getVClass();

    public Date getHireStart();

    public Date getHireEnd();

    public int getNumberOfHireDays();

    public boolean getIsTotalLoss();
}