/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.model;

/**
 *
 * @author Carlson
 */

import java.math.BigDecimal;

public class Invoice {

    private long ID = -1;
    private long rentalID = -1;
    private BigDecimal net;
    private BigDecimal VAT;
    private BigDecimal gross;

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public BigDecimal getVAT() {
        return VAT;
    }

    public void setVAT(BigDecimal VAT) {
        this.VAT = VAT;
    }

    public BigDecimal getGross() {
        return gross;
    }

    public void setGross(BigDecimal gross) {
        this.gross = gross;
    }

    public BigDecimal getNet() {
        return net;
    }

    public void setNet(BigDecimal net) {
        this.net = net;
    }

    public long getRentalID() {
        return rentalID;
    }

    public void setRentalID(long rentalID) {
        this.rentalID = rentalID;
    }
    
    
}
