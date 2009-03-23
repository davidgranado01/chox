/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.actions;

import chox.services.ClaimService;
import chox.web.data.FilterRecordCounter;
import chox.web.security.ApplicationAccessibility;
import chox.web.security.FilterAccessibility;
import chox.web.security.MenuAccessibility;
import chox.web.security.ReportAccessibility;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;

/**
 *
 * @author Emmanuel
 */
public class InboxAction extends BaseAction implements SessionAware {

    private Map session;
    private ClaimService service;
    private FilterRecordCounter filterRecordCounter;
    private FilterAccessibility filterAccessibility;
    private ApplicationAccessibility applicationAccessibility;
    private ReportAccessibility reportAccessibility;
    private MenuAccessibility menuAccessibility;

    public void setClaimService(ClaimService service) {
        this.service = service;
    }

    @Override
    public String execute() throws Exception {

        return SUCCESS;
    }

    public FilterRecordCounter getFilterRecordCounter() {

        if (filterRecordCounter == null) {
            filterRecordCounter = new FilterRecordCounter(service);
        }

        return filterRecordCounter;
    }

    public FilterAccessibility getFilterAccessibility() {
        if (filterAccessibility == null) {
            filterAccessibility = getApplicationAccessibility().getFilterAccessibility(super.getAuthenticatedUser().getAuthorities());
        }
        return filterAccessibility;
    }
    
    public ReportAccessibility getReportAccessibility() {
        
        if (reportAccessibility == null) {
            reportAccessibility = getApplicationAccessibility().getReportAccessibility(super.getAuthenticatedUser().getAuthorities());
        }
        return reportAccessibility;
    }
    
    public MenuAccessibility getMenuAccessibility() {
        if (menuAccessibility == null) {
            menuAccessibility = getApplicationAccessibility().getMenuAccessibility(super.getAuthenticatedUser().getAuthorities());
        }
        return menuAccessibility;
    }

    public void setSession(Map arg0) {
        this.session = arg0;
    }

    public ApplicationAccessibility getApplicationAccessibility() {
        return applicationAccessibility;
    }

    public void setApplicationAccessibility(ApplicationAccessibility applicationAccessibility) {
        this.applicationAccessibility = applicationAccessibility;
    }
}

