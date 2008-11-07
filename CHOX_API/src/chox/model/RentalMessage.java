/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.model;

/**
 *
 * @author Carlson
 */
public class RentalMessage {

    private long ID = -1;
    private long rentalID=-1;
    private long messageID=-1;

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public long getMessageID() {
        return messageID;
    }

    public void setMessageID(long messageID) {
        this.messageID = messageID;
    }

    public long getRentalID() {
        return rentalID;
    }

    public void setRentalID(long rentalID) {
        this.rentalID = rentalID;
    }
    
    
}
