/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.model;

import java.sql.Timestamp;
import java.math.BigDecimal;

/**
 *
 * @author Carlson
 */
public class RentalCost {

    private long ID = -1;
    private long rentalID = -1;
    private BigDecimal netRental;
    private BigDecimal rentalVAT;
    private BigDecimal grossRental;

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public BigDecimal getGrossRental() {
        return grossRental;
    }

    public void setGrossRental(BigDecimal grossRental) {
        this.grossRental = grossRental;
    }

    public BigDecimal getNetRental() {
        return netRental;
    }

    public void setNetRental(BigDecimal netRental) {
        this.netRental = netRental;
    }

    public long getRentalID() {
        return rentalID;
    }

    public void setRentalID(long rentalID) {
        this.rentalID = rentalID;
    }

    public BigDecimal getRentalVAT() {
        return rentalVAT;
    }

    public void setRentalVAT(BigDecimal rentalVAT) {
        this.rentalVAT = rentalVAT;
    }
    
    
}
