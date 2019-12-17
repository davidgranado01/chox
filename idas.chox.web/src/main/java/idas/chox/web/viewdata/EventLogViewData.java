package idas.chox.web.viewdata;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.WebUser;
import idas.chox.core.util.DateHelper;
import idas.chox.events.BaseActivityEvent;

public class EventLogViewData {

    private int id;
    private final String createdBy;
    private final String createdDate;
    private String activityName;
    private String eventName;
    private String claimStatus;
    private Integer insurerId;
    private Integer choId;

    public EventLogViewData(BaseActivityEvent eventLog) {
        this.id = eventLog.getId();
        this.createdDate = DateHelper.getLocalDateTimeFormat().format(eventLog.getCreatedDate());
        this.activityName = eventLog.getActivityName();
        this.eventName = eventLog.getEventName();
        this.claimStatus = eventLog.getClaimStatus();
        this.insurerId = eventLog.getInsurerId();
        this.choId = eventLog.getChoId();

        String orgName = "";
        WebUser user = eventLog.getCreatedBy();
        if (user != null) {
            Chorganisation cho = user.getChorganisation();
            Insurer ins = user.getInsurer();

            if (ins != null) {
                orgName = String.format("(%1$s)", ins.getName());
            } else if (cho != null) {
                orgName = String.format("(%1$s)", cho.getName());
            }

            if ((user.getFirstName() != null && user.getFirstName().startsWith("~~"))
                    || (user.getLastName() != null && user.getLastName().startsWith("~~"))) {
                createdBy = String.format("GDPR: data demoved %1$s", orgName);
            } else {
                createdBy = String.format("%1$s %2$s %3$s", user.getFirstName(), user.getLastName(), orgName);
            }
        } else {
            createdBy = "unknown";
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(String claimStatus) {
        this.claimStatus = claimStatus;
    }

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
}
