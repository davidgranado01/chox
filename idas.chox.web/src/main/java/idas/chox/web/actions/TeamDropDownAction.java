package idas.chox.web.actions;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.services.LookupService;

public class TeamDropDownAction extends BaseAction {
    private static final Logger LOG = LoggerFactory.getLogger(TeamDropDownAction.class);
    private List<String> teams = null;
    private Integer insurerId;
    private String site;
    private LookupService service;

    public LookupService getService() {
        return service;
    }

    public void setService(LookupService service) {
        this.service = service;
    }

    public Integer getInsurerId() {
        if (getIsInsurer()) {
            insurerId = getAuthenticatedUser().getInsurer().getId();
        }
        return insurerId;
    }

    public void setInsurerId(Integer insurerId) {
        LOG.debug("insurerId set: {}", insurerId);
        this.insurerId = insurerId;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        if (site != null && site.equals("--- ALL ---"))
            site = null;
        this.site = site;
    }

    public void setLookupService(LookupService service) {
        this.service = service;
    }

    public List<String> getTeams() {
        return teams;
    }

    public void setTeams(List<String> teams) {
        LOG.debug("Teams set: {}", teams.size());
        this.teams = teams;
    }

    public String getJsonData() {
        LOG.debug("Returning json data from teams: {}", teams);

        StringBuilder jsonString = new StringBuilder();
        jsonString.append("[");
        for (String team : teams) {
            jsonString.append("{\"team\":\"").append(team).append("\"},");
        }
        jsonString.deleteCharAt(jsonString.length()-1); // remove trailing semi-colon
        jsonString.append("]");
        LOG.debug("Returning json data: {}", jsonString.toString());
        return "{totalCount:" + teams.size() + ",results:" + jsonString.toString() + "}";
    }

    @Override
    public String execute() throws Exception {
        LOG.debug("execute called in TeamDropDownAction.");
        teams = service.getTeamsBySite(getInsurerId(), site, true);
        return SUCCESS;
    }
}
