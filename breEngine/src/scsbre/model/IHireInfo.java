package scsbre.model;

import java.util.Date;

public interface IHireInfo {

    public IVehicleClassInfo getVClass();

//    public void setVClass(IVehicleClassInfo class1);

    public Date getHireStart();

//    public void setHireStart(Date hireStart);

    public Date getHireEnd();

//    public void setHireEnd(Date hireEnd);

    public int getNumberOfHireDays();

//    public void setNumberOfHireDays(int numberOfHireDays);

    public boolean getIsTotalLoss();

//    public void setIsTotalLoss(Boolean isTotalLoss);
}