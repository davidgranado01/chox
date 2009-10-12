package chox.web.actions;

import chox.model.ChoBandOrganisation;
import chox.model.VehicleClassCelling;
import chox.services.InsurerService;
import chox.services.VehicleClassCellingService;
import chox.web.viewdata.ChoBandChorganisationViewData;
import chox.web.viewdata.VehicleClassCellingViewData;
import java.util.ArrayList;
import net.sf.json.JSONArray;
import java.util.List;

public class VehicleClassCellingAction extends AdminBaseModelAction {

    private List<VehicleClassCellingViewData> vehicleClassCellingViewData;
    private InsurerService insurerService;
    private VehicleClassCellingService vehicleClassCellingService;
    private int insurerId = -1;

    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.vehicleClassCellingViewData);
        return "{totalCount:" + this.vehicleClassCellingViewData.size() + ",results:" + jObject.toString() + "}";
    }

    public void setInsurerService(InsurerService insurerService) {
        this.insurerService = insurerService;
    }
    
    public void setVehicleClassCellingService(VehicleClassCellingService vehicleClassCellingService)
    {
        this.vehicleClassCellingService = vehicleClassCellingService;
    }

    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }

    @Override
    public String execute() {
        return SUCCESS;
    }

    
    public String getSelectedInsurerVehicleClassCelling(){

        try{

            List<VehicleClassCelling> vehicleClassCellings = vehicleClassCellingService.getVehicleClassCellingByInsurer(insurerService.getObject(insurerId));
            vehicleClassCellingViewData = new ArrayList<VehicleClassCellingViewData>();

            for(VehicleClassCelling vcc : vehicleClassCellings){
                vehicleClassCellingViewData.add(new VehicleClassCellingViewData(vcc));
            }

        } catch (Exception ex) {

        }

        return SUCCESS;
    }
    
}
