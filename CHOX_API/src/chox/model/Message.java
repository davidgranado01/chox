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
public class Message {

    private long ID = -1;
    private String message;
    private Timestamp received = new Timestamp(System.currentTimeMillis());
    private long sessionID = -1;
    private int statusID = 3;

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setReceived(Timestamp received) {
        this.received = received;
    }

    public void setSessionID(long sessionID) {
        this.sessionID = sessionID;
    }

    public void setStatusID(int statusID) {
        this.statusID = statusID;
    }

    public String getMessage() {
        return message;
    }

    public Timestamp getReceived() {
        return received;
    }

    public long getSessionID() {
        return sessionID;
    }

    public int getStatusID() {
        return statusID;
    }
}
