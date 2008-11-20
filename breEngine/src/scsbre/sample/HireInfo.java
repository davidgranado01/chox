package scsbre.sample;

import java.util.Date;

import scsbre.model.IHireInfo;
import scsbre.model.IVehicleClassInfo;

public class HireInfo implements IHireInfo {

    private IVehicleClassInfo vClass;
    private Date hireStart;
    private Date hireEnd;
    private int numberOfHireDays;
    private boolean isTotalLoss;

    /* (non-Javadoc)
     * @see scsbre.model.IHireInfo#getVClass()
     */
    public IVehicleClassInfo getVClass() {
        return vClass;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IHireInfo#setVClass(scsbre.model.VehicleClassInfo)
     */

    public void setVClass(IVehicleClassInfo class1) {
        vClass = class1;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IHireInfo#getHireStart()
     */

    public Date getHireStart() {
        return hireStart;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IHireInfo#setHireStart(java.util.Date)
     */

    public void setHireStart(Date hireStart) {
        this.hireStart = hireStart;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IHireInfo#getHireEnd()
     */

    public Date getHireEnd() {
        return hireEnd;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IHireInfo#setHireEnd(java.util.Date)
     */

    public void setHireEnd(Date hireEnd) {
        this.hireEnd = hireEnd;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IHireInfo#getNumberOfHireDays()
     */

    public int getNumberOfHireDays() {
        return numberOfHireDays;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IHireInfo#setNumberOfHireDays(int)
     */

    public void setNumberOfHireDays(int numberOfHireDays) {
        this.numberOfHireDays = numberOfHireDays;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IHireInfo#getIsTotalLoss()
     */

    public boolean getIsTotalLoss() {
        return isTotalLoss;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IHireInfo#setIsTotalLoss(java.lang.Boolean)
     */

    public void setIsTotalLoss(Boolean isTotalLoss) {
        this.isTotalLoss = isTotalLoss;
    }
}
