package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.annotation.Secured;


import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

import net.sf.json.JSONArray;

import idas.chox.core.model.ProtocolVehicleClassCeiling;
import idas.chox.core.model.VehicleClassCeiling;
import idas.chox.core.services.ProtocolVehicleClassCeilingService;
import idas.chox.service.admin.AdminInsurerService;
import idas.chox.web.viewdata.VehicleClassCeilingViewData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BreBandProtocolVehicleClassCeilingAction extends BaseAction implements ModelDriven<ProtocolVehicleClassCeiling>, Preparable {
    private static final Logger LOG = LoggerFactory.getLogger(BreBandProtocolVehicleClassCeilingAction.class);

    private int breBandId = -1;
    private int insurerId = -1;
    private int vehicleClassId = -1;
    private int protocolVehicleClassCeilingId = -1;
    private double hireNetCeiling = 0.00;
    private double repairNetCeiling = 0.00;
    private String objectId;
    private ProtocolVehicleClassCeiling model;
    private List<VehicleClassCeilingViewData> vehicleClassCeilingViewData = new ArrayList<VehicleClassCeilingViewData>();
    private AdminInsurerService adminInsurerService;
    private ProtocolVehicleClassCeilingService protocolVehicleClassCeilingService;

    public boolean getIsNew() {

        if (objectId != null && !objectId.equalsIgnoreCase("") && Integer.valueOf(objectId) <= 0) {
            return true;
        }
        return false;
    }

    @Override
    public ProtocolVehicleClassCeiling getModel() {
        return model;
    }

    public void setModel(ProtocolVehicleClassCeiling model) {
        this.model = model;
    }

    @Secured({"ROLE_CHOX_ADMIN", "ROLE_INS_ADMIN"})
    public String doRenderActionPage() {
        return SUCCESS;
    }

    @Override
    public void prepare() throws Exception {
        model = new ProtocolVehicleClassCeiling();
    }

    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.vehicleClassCeilingViewData);
        return "{totalCount:" + this.vehicleClassCeilingViewData.size() + ",results:" + jObject.toString() + "}";
    }

    // <editor-fold defaultstate="collapsed" desc="GET SET">

    public int getBreBandId() {
        return breBandId;
    }

    public void setBreBandId(int breBandId) {
        this.breBandId = breBandId;
    }

    public int getProtocolVehicleClassCeilingId() {
        return protocolVehicleClassCeilingId;
    }

    public void setProtocolVehicleClassCeilingId(int protocolVehicleClassCeilingId) {
        this.protocolVehicleClassCeilingId = protocolVehicleClassCeilingId;
    }

    public int getVehicleClassId() {
        return vehicleClassId;
    }

    public void setVehicleClassId(int vehicleClassId) {
        this.vehicleClassId = vehicleClassId;
    }

    public double getHireNetCeiling() {
        return hireNetCeiling;
    }

    public void setHireNetCeiling(double hireNetCeiling) {
        this.hireNetCeiling = hireNetCeiling;
    }

    public double getRepairNetCeiling() {
        return repairNetCeiling;
    }

    public void setRepairNetCeiling(double repairNetCeiling) {
        this.repairNetCeiling = repairNetCeiling;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="ACTIONS">
    @Secured({"ROLE_CHOX_ADMIN", "ROLE_INS_ADMIN"})
    public String getSelectedBreBandProtocolVehicleClassCeiling() {

        try {
            // get protocolVehicleClassCeiling for the existing breband.
            if (breBandId != -1) {
                List<ProtocolVehicleClassCeiling> protocolVehicleClassCeilings = protocolVehicleClassCeilingService.getSelectedProtocolVehicleClassCeilingByBreBand(breBandId);
                for (ProtocolVehicleClassCeiling vcc : protocolVehicleClassCeilings) {
                    vehicleClassCeilingViewData.add(new VehicleClassCeilingViewData(vcc));
                }
            } else if (breBandId == -1 && insurerId > 0) { // if it is new breband get it from vehicle class ceiling.
                List<VehicleClassCeiling> vehicleClassCeilings = adminInsurerService.getVehicleClassCeilingByInsurer(this.insurerId);

                for (VehicleClassCeiling vcc : vehicleClassCeilings) {
                    vehicleClassCeilingViewData.add(new VehicleClassCeilingViewData(vcc));
                }
            }
        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }
    /*
    @Secured ({"ROLE_CHOX_ADMIN", "ROLE_INS_ADMIN"})
    public String addNewProtocolVehicleClassCeiling() {

        try {

            List<ProtocolVehicleClassCeiling> protocolVehicleClassCeilings = protocolVehicleClassCeilingService.getSelectedProtocolVehicleClassCeilingByBreBand(breBandId);
            if (protocolVehicleClassCeilings.size() > 0) {
                for (ProtocolVehicleClassCeiling vcc : protocolVehicleClassCeilings) {
                    if (vcc.getVehicleClass().getId().compareTo(this.vehicleClassId) == 0) {
                        this.model = vcc;
                        throw new Exception("Record was updated by another transaction/user, please try again.",
                                new StaleObjectStateException(vcc.getClass().getSimpleName().concat("Version"), vcc.getId()));
                    }
                }
            }

            ActionResponse response;
            response = adminInsurerService.addNewProtocolVehicleClassCeiling(this.model, this.vehicleClassId, this.breBandId);
            setActionResponse(response);

        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }
    
    @Secured ({"ROLE_CHOX_ADMIN", "ROLE_INS_ADMIN"})
    public String removeProtocolVehicleClassCeiling() throws Exception {

        try {

            if (this.protocolVehicleClassCeilingId > 0) {
                ProtocolVehicleClassCeiling protocolVehicleClassCeiling = protocolVehicleClassCeilingService.getProtocolVehicleClassCeiling(protocolVehicleClassCeilingId);
                if (protocolVehicleClassCeiling != null)
                    protocolVehicleClassCeilingService.deleteProtocolVehicleClassCeiling(protocolVehicleClassCeiling);
            }

        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }
     
     @Secured ({"ROLE_CHOX_ADMIN", "ROLE_INS_ADMIN"})
     public String updateProtocolVehicleClassCeiling() {
     //        if (getUserOrganisationType() == 2 && this.insurerId != getUserOrganisationId()) {
     //            LOG.error("Illegal Access detected: InsurerId = {}, getUserOrganisationId={}", insurerId, getUserOrganisationId());
     //            throw new AccessDeniedException("Illegal access detected.");
     //        }

     try {

     ActionResponse response;
     response = adminInsurerService.updateProtocolVehicleClassCeiling(this.protocolVehicleClassCeilingId, this.hireNetCeiling, this.repairNetCeiling);
     setActionResponse(response);

     } catch (Exception ex) {
     handleException(ex);
     return ERROR;
     }

     return SUCCESS;
     } 
     */
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="SERVICES">

    public void setAdminInsurerService(AdminInsurerService adminInsurerService) {
        this.adminInsurerService = adminInsurerService;
    }


    // </editor-fold>

    public ProtocolVehicleClassCeilingService getProtocolVehicleClassCeilingService() {
        return protocolVehicleClassCeilingService;
    }

    public void setProtocolVehicleClassCeilingService(ProtocolVehicleClassCeilingService protocolVehicleClassCeilingService) {
        this.protocolVehicleClassCeilingService = protocolVehicleClassCeilingService;
    }


}
