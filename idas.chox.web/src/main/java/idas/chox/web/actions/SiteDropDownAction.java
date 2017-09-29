package idas.chox.web.actions;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.services.LookupService;

public class SiteDropDownAction extends BaseAction {
    private static final Logger LOG = LoggerFactory.getLogger(SiteDropDownAction.class);

    private List<String> sites = null;
    private Integer insurerId;
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

    public void setLookupService(LookupService service) {
        this.service = service;
    }

    public List<String> getSites() {
        return sites;
    }

    public void setSites(List<String> sites) {
        LOG.debug("Sites set: {}", sites.size());
        this.sites = sites;
    }

    public String getJsonData() {
        LOG.debug("Returning json data from sites: {}", sites);

        StringBuilder jsonString = new StringBuilder();
        jsonString.append("[");
        for (String site : sites) {
            jsonString.append("{\"site\":\"").append(site).append("\"},");
        }
        jsonString.deleteCharAt(jsonString.length()-1); // remove trailing semi-colon
        jsonString.append("]");
        LOG.debug("Returning json data: {}", jsonString.toString());
        return "{totalCount:" + sites.size() + ",results:" + jsonString.toString() + "}";
    }


    @Override
    public String execute() throws Exception {
        LOG.debug("execute called in SiteDropDownAction.");
        sites = service.getSitesByInsurerId(getInsurerId(),true);
        return SUCCESS;
    }
}
