package idas.chox.service.xml;

import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.BreBandService;
import idas.chox.core.services.ChorganisationAliasService;
import idas.chox.core.services.ChorganisationService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.InsurerAliasService;
import idas.chox.core.services.InsurerChorganisationService;
import idas.chox.core.services.InsurerDiscountService;
import idas.chox.core.services.VehicleClassService;
import idas.chox.service.claim.ClaimObjectService;
import idas.chox.service.xml.validations.DataValidationParameter;

public class BordereauReaderContext {

    private ClaimService claimService;
    private ChorganisationService chorganisationService;
    private BreBandService breBandService;
    private VehicleClassService vehicleClassService;
    private InsurerAliasService insurerAliasService;
    private ChorganisationAliasService chorganisationAliasService;
    private InsurerChorganisationService insurerChorganisationService;
    private DataValidationParameter dataValidationParameter;
    private SecurityInfoProvider securityInfoProvider;
    private ClaimObjectService claimObjectService;
    private InsurerDiscountService insurerDiscountService;
    
    public InsurerDiscountService getInsurerDiscountService() {
        return insurerDiscountService;
    }

    public void setInsurerDiscountService(InsurerDiscountService insurerDiscountService) {
        this.insurerDiscountService = insurerDiscountService;
    }

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
        return insurerAliasService;
    }

    public void setInsurerAliasService(InsurerAliasService insurerAliasService) {
        this.insurerAliasService = insurerAliasService;
    }

    public ChorganisationAliasService getChorganisationAliasService() {
        return chorganisationAliasService;
    }

    public void setChorganisationAliasService(ChorganisationAliasService chorganisationAliasService) {
        this.chorganisationAliasService = chorganisationAliasService;
    }

    public InsurerChorganisationService getInsurerChorganisationService() {
        return insurerChorganisationService;
    }

    public void setInsurerChorganisationService(InsurerChorganisationService insurerChorganisationService) {
        this.insurerChorganisationService = insurerChorganisationService;
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
    
    public void setClaimObjectService(ClaimObjectService claimObjectService) {
        this.claimObjectService = claimObjectService;
    }

    public ClaimObjectService getClaimObjectService() {
        return claimObjectService;
    }
    
    
}
