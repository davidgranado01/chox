package idas.chox.events;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Entity;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author john
 */
public class BaseActivityEvent extends Entity implements Serializable {
    Claim claim;
    private String activityName;
    private String eventName;
    private String claimStatus;
    private Integer insurerId;
    private Integer choId;
    private ClaimType claimType;
    private Map<String, String> attributes;
    
    public BaseActivityEvent() {};
    
    public BaseActivityEvent(Claim claim, String activityName) {
        this.claim = claim;
        this.claimStatus = claim.getStatus();
        this.eventName = this.getClass().getSimpleName();
        this.activityName = activityName;
        this.insurerId = claim.getInsurer().getId();
        this.choId = claim.getChorganisation().getId();
        this.claimType = claim.getClaimType();
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }
    
    public String getActivityName() {
        return activityName;
    }
    
    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Claim getClaim() {
        return claim;
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
    }
    
    public String getClaimStatus() { return claimStatus;}
    public void setClaimStatus(String claimStatus){ this.claimStatus=claimStatus;}

    public Integer getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(Integer insurerId) {
        this.insurerId = insurerId;
    }

    public Integer getChoId() {
        return choId;
    }

    public void setChoId(Integer choId) {
        this.choId = choId;
    }

    public ClaimType getClaimType() {
        return claimType;
    }

    public void setClaimType(ClaimType claimType) {
        this.claimType = claimType;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    @Override
    public String toString() {
        return activityName;
    }
    
    public final void addAttribute(String key, String value) {
        if (attributes == null) {
            attributes = new HashMap<>();
        }
        attributes.put(key, value);
    }
}
