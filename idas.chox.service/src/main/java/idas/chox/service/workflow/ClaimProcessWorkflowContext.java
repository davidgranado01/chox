/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.workflow;

import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.AutomaticRoutingService;
import idas.chox.core.services.BusinessRulesEngService;
import idas.chox.core.services.DataService;
import idas.chox.core.workflow.WorkflowContext;

public class ClaimProcessWorkflowContext implements WorkflowContext {

    private DataService dataService;
    private SecurityInfoProvider securityInfoProvider;
    private BusinessRulesEngService businessRulesEngService;
    private AutomaticRoutingService automaticRoutingService;

    @Override
    public DataService getDataService() {
        return dataService;
    }

    @Override
    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }

    @Override
    public SecurityInfoProvider getSecurityInfoProvider() {
        return securityInfoProvider;
    }

    @Override
    public void setSecurityInfoProvider(SecurityInfoProvider securityInfoProvider) {
        this.securityInfoProvider = securityInfoProvider;
    }

    @Override
    public void setBusinessRulesEngService(BusinessRulesEngService businessRulesEngService) {
        this.businessRulesEngService = businessRulesEngService;
    }

    @Override
    public BusinessRulesEngService getBusinessRulesEngService() {
        return businessRulesEngService;
    }

    public AutomaticRoutingService getAutomaticRoutingService() {
        return automaticRoutingService;
    }

    public void setAutomaticRoutingService(AutomaticRoutingService automaticRoutingService) {
        this.automaticRoutingService = automaticRoutingService;
    }
}
