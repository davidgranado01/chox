/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.core.model;

/**
 *
 * @author emmanuel
 */
public class Notification extends Entity {

    public static String TYPE_UPDATE = "Update";
    public static String TYPE_ANOMALOUS = "Anomalous";
    private String type;
    private String message;
    private boolean isacknowledged;
    private Claim claim;

    public Notification() {
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
    
    public NotificationType getNotificationType(){
    	return NotificationType.valueOf(getType());
    }

    /**
     * @return the isacknowledged
     */
    public boolean isIsacknowledged() {
        return isacknowledged;
    }

    /**
     * @param isacknowledged the isacknowledged to set
     */
    public void setIsacknowledged(boolean isacknowledged) {
        this.isacknowledged = isacknowledged;
    }
}
