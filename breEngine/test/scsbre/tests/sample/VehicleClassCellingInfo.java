/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package scsbre.tests.sample;

import java.math.BigDecimal;
import scsbre.model.IVehicleClassCellingInfo;

/**
 *
 * @author emmanuel
 */
public class VehicleClassCellingInfo implements IVehicleClassCellingInfo {
    private BigDecimal hireNetCelling;
    private BigDecimal repairNetCelling;

    /**
     * @return the hireNetCelling
     */
    public BigDecimal getHireNetCelling() {
        return hireNetCelling;
    }

    /**
     * @param hireNetCelling the hireNetCelling to set
     */
    public void setHireNetCelling(BigDecimal hireNetCelling) {
        this.hireNetCelling = hireNetCelling;
    }

    /**
     * @return the repairNetCelling
     */
    public BigDecimal getRepairNetCelling() {
        return repairNetCelling;
    }

    /**
     * @param repairNetCelling the repairNetCelling to set
     */
    public void setRepairNetCelling(BigDecimal repairNetCelling) {
        this.repairNetCelling = repairNetCelling;
    }



}
