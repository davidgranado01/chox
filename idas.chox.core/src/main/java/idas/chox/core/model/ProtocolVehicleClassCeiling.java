package idas.chox.core.model;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 *
 * @author emmanuel
 */
public class ProtocolVehicleClassCeiling extends Entity implements Serializable, FullAudit {

    private BigDecimal hireNetCeiling;
    private BigDecimal repairNetCeiling;
    private BreBand breBand;
    private VehicleClass vehicleClass;

    public ProtocolVehicleClassCeiling() {
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

    public BreBand getBreBand() {
        return breBand;
    }

    public void setBreBand(BreBand breBand) {
        this.breBand = breBand;
    }

    public VehicleClass getVehicleClass() {
        return vehicleClass;
    }

    public void setVehicleClass(VehicleClass vehicleClass) {
        this.vehicleClass = vehicleClass;
    }
}
