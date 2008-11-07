/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.model;

import java.math.BigDecimal;

public class StorageRecoveryCost {

    private long ID = -1;
    private long claimID = -1;
    private BigDecimal netStorageRecovery;
    private BigDecimal storageRecoveryVAT;
    private BigDecimal grossStorageRecovery;

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

    public BigDecimal getGrossStorageRecovery() {
        return grossStorageRecovery;
    }

    public void setGrossStorageRecovery(BigDecimal grossStorageRecovery) {
        this.grossStorageRecovery = grossStorageRecovery;
    }

    public BigDecimal getNetStorageRecovery() {
        return netStorageRecovery;
    }

    public void setNetStorageRecovery(BigDecimal netStorageRecovery) {
        this.netStorageRecovery = netStorageRecovery;
    }

    public BigDecimal getStorageRecoveryVAT() {
        return storageRecoveryVAT;
    }

    public void setStorageRecoveryVAT(BigDecimal storageRecoveryVAT) {
        this.storageRecoveryVAT = storageRecoveryVAT;
    }
    
}
