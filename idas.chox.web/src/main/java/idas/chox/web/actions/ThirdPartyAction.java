package idas.chox.web.actions;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.ThirdParty;
import idas.chox.core.services.InsurerService;
import idas.chox.core.services.LookupService;
import idas.chox.core.services.VehicleClassService;
import idas.chox.service.security.TabAccessibility;
import java.util.List;

/**
 *
 * @author Emmanuel
 */
public class ThirdPartyAction extends ClaimModelAction<ThirdParty> {

    private VehicleClassService vehicleClassService;
    private InsurerService insurerService;
    private LookupService lookupService;
  
    private int vehicleClassId;
    private List vehicleClasses;
    private List insurers;

    @Override
    public ThirdParty loadModel() {
        ThirdParty thirdParty = claim.getThirdParty();
        if (thirdParty != null) {
            return thirdParty;
        }
        return new ThirdParty();
    }

    @Override
    public String updateModel() {

        if (vehicleClassId >= 0) {
            model.setVehicleClass(this.vehicleClassService.getVehicleClass(vehicleClassId));
        }

        
        claim.setThirdParty(model);
        return super.updateModel();
    }

    @Override
    String getTabName() {
        return TabAccessibility.TAB_CLAIM_DETAIL;
    }

    public void setVehicleClassId(int vehicleClassId) {
        this.vehicleClassId = vehicleClassId;
    }

   

    public int getVehicleClassId() {
        return this.model.getVehicleClass() != null ? vehicleClassId = this.model.getVehicleClass().getId() : 0;
    }

   

    public void setVehicleClassService(VehicleClassService vehicleClassService) {
        this.vehicleClassService = vehicleClassService;
    }

    public void setInsurerService(InsurerService insurerService) {
        this.insurerService = insurerService;
    }

    public void setLookupService(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    public List getVehicleClasses() {
        if (vehicleClasses == null) {
            vehicleClasses = lookupService.getVehicleClasses();
        }
        return vehicleClasses;
    }

    public List getInsurers() {

        if (insurers == null) {

            if (this.getAuthenticatedUser().getChorganisation() != null) {
                Chorganisation currentCho = this.getAuthenticatedUser().getChorganisation();
                insurers = this.lookupService.getInsurers(currentCho.getId());
            } else {
                insurers = this.lookupService.getInsurers();
            }

        }

        return insurers;
    }

    public String getDefaultInsurerName(){

        return this.model.getInsurer().getName();
    }
}
