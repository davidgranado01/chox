/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.model;

import java.math.BigDecimal;
import scsbre.model.IVehicleClassCellingInfo;

/**
 *
 * @author emmanuel
 */
public class VehicleClassCelling extends AuditableEntity implements IVehicleClassCellingInfo {

    private BigDecimal hireNetCelling;
    private BigDecimal repairNetCelling;
    private Insurer insurer;
    private VehicleClass vehicleClass;

    public VehicleClassCelling() {
    }

    public VehicleClassCelling(BigDecimal hireNetCelling, BigDecimal repairNetCelling) {
        setHireNetCelling(hireNetCelling);
        setRepairNetCelling(repairNetCelling);
    }

    public BigDecimal getHireNetCelling() {
        return hireNetCelling;
    }

    public void setHireNetCelling(BigDecimal hireNetCelling) {
        this.hireNetCelling = hireNetCelling;
    }

    public BigDecimal getRepairNetCelling() {
        return repairNetCelling;
    }

    public void setRepairNetCelling(BigDecimal repairNetCelling) {
        this.repairNetCelling = repairNetCelling;
    }

    public Insurer getInsurer() {
        return insurer;
    }

    public void setInsurer(Insurer insurer) {
        this.insurer = insurer;
    }

    public VehicleClass getVehicleClass() {
        return vehicleClass;
    }

    public void setVehicleClass(VehicleClass vehicleClass) {
        this.vehicleClass = vehicleClass;
    }
}
