package idas.chox.core.model;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 *
 * @author emmanuel
 */
public class VehicleClassCeiling extends Entity implements Serializable, FullAudit {

    private BigDecimal hireNetCeiling;
    private BigDecimal repairNetCeiling;
    private Insurer insurer;
    private VehicleClass vehicleClass;

    public VehicleClassCeiling() {
    }

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
