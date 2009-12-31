/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ThirdParty;
import idas.chox.core.services.InsurerService;
import idas.chox.core.services.VehicleClassService;
import idas.chox.web.security.ApplicationAccessibility;
import net.sf.json.JSONObject;

/**
 *
 * @author Emmanuel
 */
public class ThirdPartyAction extends BaseModelAction implements ModelDriven<ThirdParty>, Preparable {

    private VehicleClassService vehicleClassService;
    private InsurerService insurerService;
    private ThirdParty model;
    private int insurerId;
    private int vehicleClassId;

    public ThirdParty getModel() {
        return model;
    }

    public void prepare() throws Exception {
        Claim claim = getClaim();
        model = claim.getThirdParty();
        if (model == null) {
            model = new ThirdParty();
            claim.setThirdParty(model);
        }
    }

    public String updateModel() {

        if (vehicleClassId >= 0) {
            model.setVehicleClass(this.vehicleClassService.getObject(vehicleClassId));
        }

        if (insurerId >= 0) {
            model.setInsurer(this.insurerService.getInsurer(insurerId));
        }
        try {
            boolean isTransient = model.isTransient();
            Claim claim = getClaim();
            this.claimService.updateClaim(claim);
            if (isTransient) {
                this.getActionResponse().AssignNewIdResult(model.getId());
            }
        } catch (Exception ex) {
            this.actionResult = "ERROR :" + ex.getMessage();
        }
        return SUCCESS;
    }

    public String getJsonData() {
        JSONObject jObject = JSONObject.fromObject(this.model);
        return jObject.toString();
    }

    @Override
    String getTabName() {
        return ApplicationAccessibility.TAB_CLAIM_DETAIL;
    }

    public void setVehicleClassId(int vehicleClassId) {
        this.vehicleClassId = vehicleClassId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }

    public int getVehicleClassId() {
        return this.model.getVehicleClass() != null ? vehicleClassId = this.model.getVehicleClass().getId() : 0;
    }

    public int getInsurerId() {
        return this.model.getInsurer() != null ? insurerId = this.model.getInsurer().getId() : 0;
    }

    public void setVehicleClassService(VehicleClassService vehicleClassService) {
        this.vehicleClassService = vehicleClassService;
    }

    public void setInsurerService(InsurerService insurerService) {
        this.insurerService = insurerService;
    }
}
