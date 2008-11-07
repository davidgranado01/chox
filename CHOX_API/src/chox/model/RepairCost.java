/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.model;

import java.math.BigDecimal;

public class RepairCost {

    private long ID = -1;
    private long claimID = -1;
    private BigDecimal netRepair;
    private BigDecimal repairVAT;
    private BigDecimal grossRepair;

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public long getClaimID() {
        return claimID;
    }

    public void setClaimID(long claimID) {
        this.claimID = claimID;
    }

    public BigDecimal getGrossRepair() {
        return grossRepair;
    }

    public void setGrossRepair(BigDecimal grossRepair) {
        this.grossRepair = grossRepair;
    }

    public BigDecimal getNetRepair() {
        return netRepair;
    }

    public void setNetRepair(BigDecimal netRepair) {
        this.netRepair = netRepair;
    }

    public BigDecimal getRepairVAT() {
        return repairVAT;
    }

    public void setRepairVAT(BigDecimal repairVAT) {
        this.repairVAT = repairVAT;
    }
    
    
}
