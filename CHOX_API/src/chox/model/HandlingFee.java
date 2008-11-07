/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.model;

import java.math.BigDecimal;
/**
 *
 * @author Carlson
 */
public class HandlingFee {

    private long ID = -1;
    private long rentalID = -1;
    private BigDecimal netFee;
    private BigDecimal feeVAT;
    private BigDecimal grossFee;

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public BigDecimal getFeeVAT() {
        return feeVAT;
    }

    public void setFeeVAT(BigDecimal feeVAT) {
        this.feeVAT = feeVAT;
    }

    public BigDecimal getGrossFee() {
        return grossFee;
    }

    public void setGrossFee(BigDecimal grossFee) {
        this.grossFee = grossFee;
    }

    public BigDecimal getNetFee() {
        return netFee;
    }

    public void setNetFee(BigDecimal netFee) {
        this.netFee = netFee;
    }

    public long getRentalID() {
        return rentalID;
    }

    public void setRentalID(long rentalID) {
        this.rentalID = rentalID;
    }
    
    
}
