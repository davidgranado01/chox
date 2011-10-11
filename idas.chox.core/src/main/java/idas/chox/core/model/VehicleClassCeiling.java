package idas.chox.core.model;

import java.math.BigDecimal;
/**
 *
 * @author emmanuel
 */
public class VehicleClassCeiling extends Entity {

    private BigDecimal hireNetCeiling;
    private BigDecimal repairNetCeiling;
    private Insurer insurer;
    private VehicleClass vehicleClass;

    public VehicleClassCeiling() {
    }

    public VehicleClassCeiling(BigDecimal hireNetCeiling, BigDecimal repairNetCeiling) {
        setHireNetCeiling(hireNetCeiling);
        setRepairNetCeiling(repairNetCeiling);
    }

    public BigDecimal getHireNetCeiling() {
        return hireNetCeiling;
    }

    public final void setHireNetCeiling(BigDecimal hireNetCeiling) {
        this.hireNetCeiling = hireNetCeiling;
    }

    public BigDecimal getRepairNetCeiling() {
        return repairNetCeiling;
    }

    public final void setRepairNetCeiling(BigDecimal repairNetCeiling) {
        this.repairNetCeiling = repairNetCeiling;
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
