/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package scsbre.tests.sample;

import java.math.BigDecimal;
import scsbre.model.IVehicleClassCeilingInfo;

/**
 *
 * @author emmanuel
 */
public class VehicleClassCeilingInfo implements IVehicleClassCeilingInfo {
    
    private BigDecimal hireNetCeiling;
    private BigDecimal repairNetCeiling;

   
    public BigDecimal getHireNetCeiling() {
        return hireNetCeiling;
    }

    public void setHireNetCeiling(BigDecimal hireNetCeiling) {
        this.hireNetCeiling = hireNetCeiling;
    }

    public BigDecimal getRepairNetCeiling() {
        return repairNetCeiling;
    }

    public void setRepairNetCeiling(BigDecimal repairNetCeiling) {
        this.repairNetCeiling = repairNetCeiling;
    }

}
