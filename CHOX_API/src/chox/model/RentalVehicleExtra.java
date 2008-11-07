/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.model;

public class RentalVehicleExtra {

    private long ID = -1;
    private long rentalVehicleID = -1;
    private long extraID = -1;

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

    public long getRentalVehicleID() {
        return rentalVehicleID;
    }

    public void setRentalVehicleID(long rentalVehicleID) {
        this.rentalVehicleID = rentalVehicleID;
    }
    
    
}
