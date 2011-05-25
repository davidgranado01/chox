/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package idas.chox.service.reports.viewdata;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rajareddydodda
 */
public class TeamSiteBreInvoiceReportObject {




    private String site;
    private List<TeamSiteBreInvoiceLineItem> teams;

    public TeamSiteBreInvoiceReportObject() {
        teams = new ArrayList<TeamSiteBreInvoiceLineItem>();
    }

    public List<TeamSiteBreInvoiceLineItem> getTeams() {
        return teams;
    }

    public void setTeam(List<TeamSiteBreInvoiceLineItem> teams) {
        this.teams = teams;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

}
