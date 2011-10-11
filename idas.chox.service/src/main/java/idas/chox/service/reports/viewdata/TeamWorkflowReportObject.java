package idas.chox.service.reports.viewdata;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author John
 */
public class TeamWorkflowReportObject {
    private String site;
    private List<TeamWorkflowLineItem> teams;

    public TeamWorkflowReportObject() {
        teams = new ArrayList<TeamWorkflowLineItem>();
    }

    public List<TeamWorkflowLineItem> getTeams() {
        return teams;
    }

    public void setTeam(List<TeamWorkflowLineItem> teams) {
        this.teams = teams;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }
}
