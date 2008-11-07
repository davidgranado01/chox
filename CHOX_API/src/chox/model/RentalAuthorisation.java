/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.model;

import java.sql.Timestamp;

/**
 *
 * @author Carlson
 */
public class RentalAuthorisation {

    public static final String RESET = "reset";
    public static final String AWAITING_AUTHORISATION = "awaiting authorisation";
    public static final String AUTHORISED = "authorised";
    public static final String SELF_AUTHORISED = "self authorised";
    public static final String IN_PROGRESS = "in progress";
    public static final String CANCELLED = "cancelled";
    public static final String DISPUTED = "disputed";
    public static final String RENTAL_ENDED="rental ended";
    public static final String INVOICED = "invoiced";
    public static final String INVOICE_DISPUTED = "invoice disputed";
    public static final String INVOICE_AUTHORISED = "invoice authorised";
    public static final String FINISHED="finished";
    private long ID = -1;
    private long rentalID = -1;
    private Timestamp updated = new Timestamp(System.currentTimeMillis());
    private long sessionID = -1;
    private String status;
    private long ackSessionID = -1;

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public long getAckSessionID() {
        return ackSessionID;
    }

    public void setAckSessionID(long ackSessionID) {
        this.ackSessionID = ackSessionID;
    }

    public long getRentalID() {
        return rentalID;
    }

    public void setRentalID(long rentalID) {
        this.rentalID = rentalID;
    }

    public long getSessionID() {
        return sessionID;
    }

    public void setSessionID(long sessionID) {
        this.sessionID = sessionID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }
    
    
}
