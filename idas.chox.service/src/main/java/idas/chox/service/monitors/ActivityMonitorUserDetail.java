package idas.chox.service.monitors;

public class ActivityMonitorUserDetail {
    private int id;
    private int orgId;
    private boolean insurer;
    private boolean cho;
    private String userName;
    private String orgName;

    public ActivityMonitorUserDetail(int id, int orgId, boolean isInsurer, boolean isCho, String userName, String orgName) {
        this.id = id;
        this.orgId = orgId;
        this.insurer = isInsurer;
        this.cho = isCho;
        this.userName = userName;
        this.orgName = orgName;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public boolean isInsurer() {
        return insurer;
    }

    public void setInsurer(boolean insurer) {
        this.insurer = insurer;
    }

    public boolean isCho() {
        return cho;
    }

    public void setCho(boolean cho) {
        this.cho = cho;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
    
    @Override
    public boolean equals(Object userInfo) {
        if (userInfo instanceof ActivityMonitorUserDetail && ((ActivityMonitorUserDetail) userInfo).getId() == this.id) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.id;
        return hash;
    }
    
    @Override
    public String toString() {
        String organisationName = String.format("(%1$s)", orgName);
        return String.format("%1$s %2$s", userName, organisationName);
    }
}
