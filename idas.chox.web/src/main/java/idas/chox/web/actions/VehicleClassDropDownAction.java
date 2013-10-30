package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;

import net.sf.json.JSONArray;

import idas.chox.core.model.LookupItem;
import idas.chox.core.model.VehicleClass;
import idas.chox.core.services.LookupService;
import idas.chox.core.services.ProtocolVehicleClassCeilingService;
import idas.chox.core.services.VehicleClassCeilingService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VehicleClassDropDownAction extends BaseAction {

    private static final Logger LOG = LoggerFactory.getLogger(VehicleClassDropDownAction.class);
    protected List<VehicleClass> vehicleClasses = new ArrayList<VehicleClass>();
    protected int insurerId;
    private int breBandId;
    protected VehicleClassCeilingService vehicleClassCeilingService;
    protected ProtocolVehicleClassCeilingService protocolVehicleClassCeilingService;
    private LookupService lookupService;

    public int getBreBandId() {
        return breBandId;
    }

    public void setBreBandId(int breBandId) {
        this.breBandId = breBandId;
    }

    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }

    public void setVehicleClassCeilingService(VehicleClassCeilingService vehicleClassCeilingService) {
        this.vehicleClassCeilingService = vehicleClassCeilingService;
    }

    public void setProtocolVehicleClassCeilingService(ProtocolVehicleClassCeilingService protocolVehicleClassCeilingService) {
        this.protocolVehicleClassCeilingService = protocolVehicleClassCeilingService;
    }

    public List getVehicleClasses() {
        return vehicleClasses;
    }

    @Override
    public String execute() throws Exception {
        if (getUserOrganisationType() == 3 || (getUserOrganisationType() == 2 && this.insurerId != getUserOrganisationId())) {
            throw new AccessDeniedException("Illegal access detected.");
        }
        
        try {
            
            vehicleClasses = vehicleClassCeilingService.getAvailableVehicleClassCeilingByInsurer(this.insurerId);

        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }
        return SUCCESS;
    }

    public String getAvailableVehicleClassByBreBand() throws Exception {

        try {
            if (breBandId != -1) { // get protocolVehicleClassCeiling for the existing breband.
                vehicleClasses = protocolVehicleClassCeilingService.getAvailableProtocolVehicleClassCeilingByBreBand(this.breBandId);
            } else if (breBandId == -1 && insurerId > 0) { // if it is new breband get it from vehicle class ceiling.
                vehicleClasses = vehicleClassCeilingService.getAvailableVehicleClassCeilingByInsurer(this.insurerId);
            }
        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }
        return SUCCESS;
    }

    public String getAvailableVehicleClasses() {
        try {
            vehicleClasses = this.lookupService.getVehicleClasses();
            return SUCCESS;
        } catch (Exception ex) {
            LOG.error("Exception creating jsonArray: {}", ex.getMessage());
            return SUCCESS;
        }
    }
    
    public String getJsonData() {
        JSONArray jsonArray;
        try {
            List<LookupItem> luItems = new ArrayList<LookupItem>(vehicleClasses.size());
            for (VehicleClass vehicleClass : vehicleClasses) {
                LOG.debug("Adding VehicleClass to Lookup: {}, {}", vehicleClass.getId().toString(), vehicleClass.getName());
                luItems.add(new LookupItem(vehicleClass.getId().toString(), vehicleClass.getName()));
            }
            jsonArray = JSONArray.fromObject(luItems);
        } catch (Exception ex) {
            LOG.error("Exception creating jsonArray: {}", ex.getMessage());
            return null;
        }
        LOG.debug("Returning json data: {}", jsonArray.toString());
        return "{totalCount:" + vehicleClasses.size() + ",results:" + jsonArray.toString() + "}";
    }
    
    public void setLookupService(LookupService lookupService) {
        this.lookupService = lookupService;
    }

}
