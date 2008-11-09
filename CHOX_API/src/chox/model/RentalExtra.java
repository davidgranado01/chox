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
public class RentalExtra {

    private long ID = -1;
    private long rentalID = -1;
    private long extraID = -1;
    private BigDecimal quantity;
    private BigDecimal itemAmount;

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public long getExtraID() {
        return extraID;
    }

    public void setExtraID(long extraID) {
        this.extraID = extraID;
    }

    public BigDecimal getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(BigDecimal itemAmount) {
        this.itemAmount = itemAmount;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public long getRentalID() {
        return rentalID;
    }

    public void setRentalID(long rentalID) {
        this.rentalID = rentalID;
    }
}
