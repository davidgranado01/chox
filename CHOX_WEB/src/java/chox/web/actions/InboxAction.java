/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.actions;

import chox.services.ClaimService;
import chox.web.data.FilterRecordCounter;
import chox.web.security.FilterAccessibility;
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

    public void setClaimService(ClaimService service) {
        this.service = service;
    }

    @Override
    public String execute() throws Exception {

        session.remove("searchCriteria");
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
            filterAccessibility = new FilterAccessibility(super.getAuthenticatedUser().getAuthorities());
        }
        return filterAccessibility;
    }

    public void setSession(Map arg0) {
        this.session = arg0;
    }
}

