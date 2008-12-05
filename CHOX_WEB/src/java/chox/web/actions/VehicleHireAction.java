/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.web.actions;

import chox.Util.DateHelper;
import chox.model.Claim;
import chox.model.VehicleClass;
import chox.model.VehicleHire;
import chox.services.LookupService;
import chox.services.VehicleClassService;
import chox.services.VehicleHireService;
import chox.web.security.ApplicationAccessibility;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import java.util.List;
import net.sf.json.JSONObject;

/**
 *
 * @author Emmanuel
 */
public class VehicleHireAction extends BaseModelAction implements ModelDriven<VehicleHire>, Preparable {

    private VehicleHireService service;
    private LookupService lookupService;
    private VehicleClassService vehicleClassService;
    private VehicleHire model;
    private int vehicleClassId;

    public void setVehicleHireService(VehicleHireService service) {
        this.service = service;
    }
    
    public void setLookupService( LookupService lookupService)
    {
        this.lookupService = lookupService;
    }
    
    public void setVehicleClassService(VehicleClassService vehicleClassService)
    {
        this.vehicleClassService = vehicleClassService;
    }

    public VehicleHire getModel() {
        return model;
    }

    public void prepare() throws Exception {
        if (objectId <= 0) {
            model = new VehicleHire();
        } else {
            model = service.getObject(objectId);
        }  
    }
    
    public String updateModel() {
        
        if(vehicleClassId >= 0)
        {
           model.setVehicleClass(this.vehicleClassService.getObject(vehicleClassId));
        }
        
        try {
            if(model.getId() > 0)
            {
                this.service.updateObject(model);
            }
            else
            {
                Claim c = claimService.getClaim(getClaimId());
                c.setVehicleHire(model);
                this.claimService.updateClaim(c);
            }
            this.actionResult = "";
        } catch (Exception ex) {
            this.actionResult = "ERROR :" + ex.getMessage();
        }
        return SUCCESS;
    }
    
    public void setVehicleClassId(int vehicleClassId)
    {
        this.vehicleClassId = vehicleClassId;
    }
    
    public int getVehicleClassId()
    {
        return this.model.getVehicleClass() != null ? vehicleClassId = this.model.getVehicleClass().getId() : 0;
    }

    public String getJsonData() {
        JSONObject jObject = JSONObject.fromObject(this.model);
        return jObject.toString();
    }
    
    public List<VehicleClass> getVehicleClasses()
    {
        return this.lookupService.getVehicleClasses();
    }

    @Override
    String getTabName() {
        return ApplicationAccessibility.TAB_INVOICE_DETAIL;
    }
   
    
}
