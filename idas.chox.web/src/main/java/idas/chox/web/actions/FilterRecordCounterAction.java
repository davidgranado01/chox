/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

import idas.chox.core.services.ClaimService;
import idas.chox.web.FilterRecordCounter;
import idas.chox.web.security.ApplicationAccessibility;
import idas.chox.web.security.FilterAccessibility;

/**
 *
 * @author emmanuel
 */
public class FilterRecordCounterAction extends BaseAction {

    private ClaimService service;
    private FilterAccessibility filterAccessibility;
    private FilterRecordCounter filterRecordCounter;
    private ApplicationAccessibility applicationAccessibility;

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

    public ApplicationAccessibility getApplicationAccessibility() {
        return applicationAccessibility;
    }

    public void setApplicationAccessibility(ApplicationAccessibility applicationAccessibility) {
        this.applicationAccessibility = applicationAccessibility;
    }

    public void setClaimService(ClaimService service) {
        this.service = service;
    }
}
