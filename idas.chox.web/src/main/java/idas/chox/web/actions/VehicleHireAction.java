/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.Claim;
import idas.chox.core.model.VehicleClass;
import idas.chox.core.model.VehicleHire;
import idas.chox.core.services.LookupService;
import idas.chox.core.services.VehicleClassService;
import idas.chox.core.util.DateHelper;
import idas.chox.web.security.ApplicationAccessibility;
import java.util.Date;
import java.util.List;
import net.sf.json.JSONObject;

/**
 *
 * @author Emmanuel
 */
public class VehicleHireAction extends BaseModelAction implements ModelDriven<VehicleHire>, Preparable {

    private LookupService lookupService;
    private VehicleClassService vehicleClassService;
    private VehicleHire model;
    private int vehicleClassId;

    public void setLookupService(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    public void setVehicleClassService(VehicleClassService vehicleClassService) {
        this.vehicleClassService = vehicleClassService;
    }

    public VehicleHire getModel() {
        return model;
    }

    public void prepare() throws Exception {
        Claim claim = getClaim();

        if (claim != null && claim.getVehicleHire() != null) {
            model = claim.getVehicleHire();
        } else {
            model = new VehicleHire();
        }
    }

    public String updateModel() {

        try {
            boolean isTransient = model.isTransient();
            Claim claim = getClaim();

            claim.setVehicleHire(model);

            this.claimService.updateClaim(claim);
            if (isTransient) {
                this.getActionResponse().AssignNewIdResult(model.getId());
            }
        } catch (Exception ex) {
            this.actionResult = "ERROR :" + ex.getMessage();
        }
        return SUCCESS;
    }

    public void setVehicleClassId(int vehicleClassId) {
        this.vehicleClassId = vehicleClassId;
    }

    public int getVehicleClassId() {
        return this.model.getVehicleClass() != null ? vehicleClassId = this.model.getVehicleClass().getId() : 0;
    }

    public String getJsonData() {
        JSONObject jObject = JSONObject.fromObject(this.model);
        return jObject.toString();
    }

    public List<VehicleClass> getVehicleClasses() {
        return this.lookupService.getVehicleClasses();
    }

    @Override
    String getTabName() {
        return ApplicationAccessibility.TAB_INVOICE_DETAIL;
    }

    public String getRentalStartTime() {
        return DateHelper.TimeFormat.format(model.getHireStart());
    }

    public void setRentalStartTime(String time) {
        if (model != null) {
            try {
                Date a = model.getHireStart();
                Date b = DateHelper.TimeFormat.parse(time);
                model.setHireStart(DateHelper.mergeTimeToDate(a, b));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public String getRentalEndTime() {
        return DateHelper.TimeFormat.format(model.getHireEnd());
    }

    public void setRentalEndTime(String time) {
        if (model != null) {
            try {
                Date a = model.getHireEnd();
                Date b = DateHelper.TimeFormat.parse(time);
                model.setHireStart(DateHelper.mergeTimeToDate(a, b));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }
}
