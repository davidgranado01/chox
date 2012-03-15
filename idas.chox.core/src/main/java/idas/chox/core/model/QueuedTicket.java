package idas.chox.core.model;

import java.io.Serializable;
import java.util.Date;

public class QueuedTicket implements Serializable {

    private int id;
    private String oldReference;
    private String newReference;
    private String sender;
    private Date createdDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getNewReference() {
        return newReference;
    }

    public void setNewReference(String newReference) {
        this.newReference = newReference;
    }

    public String getOldReference() {
        return oldReference;
    }

    public void setOldReference(String oldReference) {
        this.oldReference = oldReference;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

}
