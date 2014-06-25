package idas.chox.core.model;

import java.io.Serializable;

public class Workgroup extends Entity implements Serializable {

    private String name;
    private String site;
    private String team;
    private Insurer insurer;
    private boolean status;
    private boolean stpExcluded;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Insurer getInsurer() {
        return insurer;
    }

    public void setInsurer(Insurer insurer) {
        this.insurer = insurer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public boolean isStpExcluded() {
        return stpExcluded;
    }

    public void setStpExcluded(boolean stpExcluded) {
        this.stpExcluded = stpExcluded;
    }
}
