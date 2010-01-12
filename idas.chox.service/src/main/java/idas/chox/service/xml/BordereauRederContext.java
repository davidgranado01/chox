/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.xml;

import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.BreBandService;
import idas.chox.core.services.BusinessRulesEngService;
import idas.chox.core.services.ChorganisationService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.InsurerAliasService;
import idas.chox.core.services.InsurerChorganisationService;
import idas.chox.core.services.VehicleClassService;
import idas.chox.service.xml.validations.DataValidationParameter;

public class BordereauRederContext {

    private ClaimService claimService;
    private ChorganisationService chorganisationService;
    private BreBandService breBandService;
    private VehicleClassService vehicleClassService;
    private InsurerAliasService insurerAlliasService;
    private InsurerChorganisationService insurerChorganisationService;
    private BusinessRulesEngService businessRuleEngService;
    private DataValidationParameter dataValidationParameter;
    private SecurityInfoProvider securityInfoProvider;

    public ClaimService getClaimService() {
        return claimService;
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    public ChorganisationService getChorganisationService() {
        return chorganisationService;
    }

    public void setChorganisationService(ChorganisationService chorganisationService) {
        this.chorganisationService = chorganisationService;
    }

    public BreBandService getBreBandService() {
        return breBandService;
    }

    public void setBreBandService(BreBandService breBandService) {
        this.breBandService = breBandService;
    }

    public VehicleClassService getVehicleClassService() {
        return vehicleClassService;
    }

    public void setVehicleClassService(VehicleClassService vehicleClassService) {
        this.vehicleClassService = vehicleClassService;
    }

    public InsurerAliasService getInsurerAliasService() {
        return insurerAlliasService;
    }

    public void setInsurerAliasService(InsurerAliasService insurerAlliasService) {
        this.insurerAlliasService = insurerAlliasService;
    }

    public InsurerChorganisationService getInsurerChorganisationService() {
        return insurerChorganisationService;
    }

    public void setInsurerChorganisationService(InsurerChorganisationService insurerChorganisationService) {
        this.insurerChorganisationService = insurerChorganisationService;
    }

    public BusinessRulesEngService getBusinessRuleEngService() {
        return businessRuleEngService;
    }

    public void setBusinessRuleEngService(BusinessRulesEngService businessRuleEngService) {
        this.businessRuleEngService = businessRuleEngService;
    }

    public DataValidationParameter getDataValidationParameter() {
        return dataValidationParameter;
    }

    public void setDataValidationParameter(DataValidationParameter dataValidationParameter) {
        this.dataValidationParameter = dataValidationParameter;
    }

    /**
     * @return the securityInfoProvider
     */
    public SecurityInfoProvider getSecurityInfoProvider() {
        return securityInfoProvider;
    }

    /**
     * @param securityInfoProvider the securityInfoProvider to set
     */
    public void setSecurityInfoProvider(SecurityInfoProvider securityInfoProvider) {
        this.securityInfoProvider = securityInfoProvider;
    }
}
