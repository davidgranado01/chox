package idas.chox.core.workflow;

import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.AutomaticRoutingService;
import idas.chox.core.services.BreBandService;
import idas.chox.core.services.BusinessRulesEngService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.DataService;

/**
 *
 * @author emmanuel
 */
public interface WorkflowContext {

    public DataService getDataService();

    public void setDataService(DataService dataService);

    public SecurityInfoProvider getSecurityInfoProvider();

    public void setSecurityInfoProvider(SecurityInfoProvider securityInfoProvider);

    public void setBusinessRulesEngService(BusinessRulesEngService businessRulesEngService);

    public BusinessRulesEngService getBusinessRulesEngService();

    AutomaticRoutingService getAutomaticRoutingService();

    void setAutomaticRoutingService(AutomaticRoutingService automaticRoutingService);
    
    public BreBandService getBreBandService();
    
    public ClaimService getClaimService();

}
