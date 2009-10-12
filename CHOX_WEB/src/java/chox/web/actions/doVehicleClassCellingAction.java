/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.web.actions;

import chox.model.VehicleClass;
import chox.model.VehicleClassCelling;
import chox.services.InsurerService;
import chox.services.VehicleClassCellingService;
import chox.services.VehicleClassService;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

/**
 *
 * @author Carlson
 */
public class doVehicleClassCellingAction extends BaseAction implements ModelDriven<VehicleClassCelling>, Preparable {

    private String selectOrgId;
    private VehicleClassCelling model;
    private Integer vehicleClassId;
    private VehicleClassService vehicleClassService;
    private InsurerService insurerService;
    private VehicleClassCellingService vehicleClassCellingService;

    private String vehicleClassCellingId;

    public String getVehicleClassCellingId() {
        return vehicleClassCellingId;
    }

    public void setVehicleClassCellingId(String vehicleClassCellingId) {
        this.vehicleClassCellingId = vehicleClassCellingId;
    }
    
    public void setVehicleClassCellingService(VehicleClassCellingService vehicleClassCellingService) {
        this.vehicleClassCellingService = vehicleClassCellingService;
    }
        
    public void setVehicleClassService(VehicleClassService vehicleClassService) {
        this.vehicleClassService = vehicleClassService;
    }

    public void setInsurerService(InsurerService insurerService) {
        this.insurerService = insurerService;
    }
    
    public VehicleClassCelling getModel() {
        return model;
    }

    public void setModel(VehicleClassCelling model) {
        this.model = model;
    }

    public String getSelectOrgId() {
        return selectOrgId;
    }

    public void setSelectOrgId(String selectOrgId) {
        this.selectOrgId = selectOrgId;
    }

    public Integer getVehicleClassId() {
        return vehicleClassId;
    }

    public void setVehicleClassId(Integer vehicleClassId) {
        this.vehicleClassId = vehicleClassId;
    }

    public String updateModel() throws Exception {

        try {

            model.setVehicleClass(vehicleClassService.getObject(vehicleClassId));
            model.setInsurer(insurerService.getObject(Integer.parseInt(selectOrgId)));
            vehicleClassCellingService.updateObject(model);

        } catch (Exception ex) {
            ex.printStackTrace();
            this.getActionResponse().AddError(ex.getMessage());
        }

        return SUCCESS;
    }

    public void prepare() throws Exception {
        model = new VehicleClassCelling();
    }

    public String removeObject() throws Exception {

        try {
            
            if(!vehicleClassCellingId.equalsIgnoreCase("")){
                
                VehicleClassCelling vehicleClassCelling = vehicleClassCellingService.getObject(Integer.parseInt(vehicleClassCellingId));
                vehicleClassCellingService.deleteObject(vehicleClassCelling);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            this.getActionResponse().AddError(ex.getMessage());
        }

        return SUCCESS;
    }
}
