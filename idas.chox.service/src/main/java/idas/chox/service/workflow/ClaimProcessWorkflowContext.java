package idas.chox.service.workflow;

import net.engio.mbassy.bus.MBassador;

import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.AutomaticRoutingService;
import idas.chox.core.services.BreBandService;
import idas.chox.core.services.BusinessRulesEngService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.DataService;
import idas.chox.core.workflow.WorkflowContext;

public class ClaimProcessWorkflowContext implements WorkflowContext {

    private DataService dataService;
    private SecurityInfoProvider securityInfoProvider;
    private BusinessRulesEngService businessRulesEngService;
    private AutomaticRoutingService automaticRoutingService;
    private BreBandService breBandService;
    private ClaimService claimService;
    private MBassador mBassador;

    public MBassador getMBassador() {
        return mBassador;
    }

    public void setMBassador(MBassador mBassador) {
        this.mBassador = mBassador;
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
