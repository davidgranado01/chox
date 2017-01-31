package idas.chox.service.workflow;

import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.AutomaticRoutingService;
import idas.chox.core.services.BreBandService;
import idas.chox.core.services.BusinessRulesEngService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.DataService;
import idas.chox.core.workflow.WorkflowContext;
import idas.chox.service.workflow.event.EventBusWrapper;

public class ClaimProcessWorkflowContext implements WorkflowContext {

    private DataService dataService;
    private SecurityInfoProvider securityInfoProvider;
    private BusinessRulesEngService businessRulesEngService;
    private AutomaticRoutingService automaticRoutingService;
    private BreBandService breBandService;
    private ClaimService claimService;
    private EventBusWrapper eventBus;

    public EventBusWrapper getEventBus() {
        return eventBus;
    }

    public void setEventBus(EventBusWrapper eventBus) {
        this.eventBus = eventBus;
    }
    
    @Override
    public ClaimService getClaimService() {
        return claimService;
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    public void setBreBandService(BreBandService breBandService) {
        this.breBandService = breBandService;
    }

    @Override
    public BreBandService getBreBandService() {
        return breBandService;
    }

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

    @Override
    public AutomaticRoutingService getAutomaticRoutingService() {
        return automaticRoutingService;
    }

    @Override
    public void setAutomaticRoutingService(AutomaticRoutingService automaticRoutingService) {
        this.automaticRoutingService = automaticRoutingService;
    }
}
