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

    /**
     * @return the insurer
     */
    public Insurer getInsurer() {
        return insurer;
    }

    /**
     * @param insurer the insurer to set
     */
    public void setInsurer(Insurer insurer) {
        this.insurer = insurer;
    }

    /**
     * @return the vehicleClass
     */
    public VehicleClass getVehicleClass() {
        return vehicleClass;
    }

    /**
     * @param vehicleClass the vehicleClass to set
     */
    public void setVehicleClass(VehicleClass vehicleClass) {
        this.vehicleClass = vehicleClass;
    }
}
