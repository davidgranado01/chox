package idas.chox.service.reports.viewdata;


import java.util.ArrayList;
import java.util.List;



public class TeamPerformanceReportObject {

    private String site;
    private List<TeamPerformanceLineItem> teams;

    public TeamPerformanceReportObject() {
        teams = new ArrayList<TeamPerformanceLineItem>();
    }

    public List<TeamPerformanceLineItem> getTeams() {
        return teams;
    }

    public void setTeam(List<TeamPerformanceLineItem> teams) {
        this.teams = teams;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

}
