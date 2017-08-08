package idas.chox.core.model;

/**
 *
 * @author John
 */
public class Notification extends Entity {
    private String type;
    private String message;
    private boolean acknowledged;
    private boolean deleted;
    private Claim claim;

    public Notification() {}

    public Notification(Notification notification) {
        this.type = notification.getType();
        this.message = notification.getMessage();
        this.acknowledged = notification.isAcknowledged();
        this.deleted = notification.isDeleted();
        this.claim = notification.getClaim();
    }

    public Notification(String type, String message) {
        this.type = type;
        this.message = message;
    }

    /**
     * @return the type
     */
    public String getType() {
        if (type == null) {
            type = this.getClass().getSimpleName();
        }
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the claim
     */
    public Claim getClaim() {
        return claim;
    }

    /**
     * @param claim the claim to set
     */
    public void setClaim(Claim claim) {
        this.claim = claim;
    }
    
    /**
     * @return the acknowledged
     */
    public boolean isAcknowledged() {
        return acknowledged;
    }

    /**
     * @param acknowledged the iacknowledged to set
     */
    public void setAcknowledged(boolean acknowledged) {
        this.acknowledged = acknowledged;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
