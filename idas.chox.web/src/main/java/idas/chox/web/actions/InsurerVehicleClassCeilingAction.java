package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

import idas.chox.core.model.VehicleClassCeiling;
import idas.chox.core.services.VehicleClassCeilingService;
import idas.chox.service.ActionResponse;
import idas.chox.service.admin.AdminInsurerService;
import idas.chox.web.viewdata.VehicleClassCeilingViewData;
import org.hibernate.StaleObjectStateException;

public class InsurerVehicleClassCeilingAction extends BaseAction implements ModelDriven<VehicleClassCeiling>, Preparable {
    private static final Logger LOG = LoggerFactory.getLogger(InsurerVehicleClassCeilingAction.class);

    private int insurerId = -1;
    private int vehicleClassId = -1;
    private int vehicleClassCeilingId = -1;
    private double hireNetCeiling = 0.00;
    private double repairNetCeiling = 0.00;
    private String objectId;
    private VehicleClassCeiling model;
    private List<VehicleClassCeilingViewData> vehicleClassCeilingViewData = new ArrayList<>();
    private AdminInsurerService adminInsurerService;
    private VehicleClassCeilingService vehicleClassCeilingService;

    public boolean getIsNew() {
        return objectId != null && !objectId.equalsIgnoreCase("") && Integer.valueOf(objectId) <= 0;
    }

    @Override
    public VehicleClassCeiling getModel() {
        return model;
    }

    public void setModel(VehicleClassCeiling model) {
        this.model = model;
    }

    @Secured({"ROLE_CHOX_ADMIN", "ROLE_INS_ADMIN"})
    public String doRenderActionPage() {
        return SUCCESS;
    }

    @Override
    public void prepare() throws Exception {
        model = new VehicleClassCeiling();
    }

    public String getJsonData() {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(vehicleClassCeilingViewData);
        } catch (JsonProcessingException ex) {
            LOG.error("Error converting vehicleClassCeilingViewData to json string.");
        }
        return "{totalCount:" + this.vehicleClassCeilingViewData.size() + ",results:" + jsonString + "}";
    }

    // <editor-fold defaultstate="collapsed" desc="GET SET">
    public int getVehicleClassCeilingId() {
        return vehicleClassCeilingId;
    }

    public void setVehicleClassCeilingId(int vehicleClassCeilingId) {
        this.vehicleClassCeilingId = vehicleClassCeilingId;
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

    public String getSelectedInsurerVehicleClassCeiling() {
        if (getUserOrganisationType() == 3 || (getUserOrganisationType() == 2 && this.insurerId != getUserOrganisationId())) {
            throw new AccessDeniedException("Illegal access detected.");
        }

        try {

            List<VehicleClassCeiling> vehicleClassCeilings = adminInsurerService.getVehicleClassCeilingByInsurer(this.insurerId);

            for (VehicleClassCeiling vcc : vehicleClassCeilings) {
                vehicleClassCeilingViewData.add(new VehicleClassCeilingViewData(vcc));
            }

        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }

    @Secured ({"ROLE_CHOX_ADMIN", "ROLE_INS_ADMIN"})
    public String addNewVehicleClassCeiling() {

        try {
            if ( getUserOrganisationType() == 3 || (getUserOrganisationType() == 2 && this.insurerId != -1 && this.insurerId != getUserOrganisationId())
                        || (getUserOrganisationType() == 2 && this.insurerId == -1 && model.getInsurer().getId().intValue() != getUserOrganisationId())) {
                throw new AccessDeniedException("Trying to add a new Vehicle Class Ceiling for an insurer that isn't mine (POSSIBLE HACK ATTEMPT)");
            }
            List<VehicleClassCeiling> vehicleClassCeilings = adminInsurerService.getVehicleClassCeilingByInsurer(this.insurerId);
            if (vehicleClassCeilings.size() > 0) {
                for (VehicleClassCeiling vcc : vehicleClassCeilings) {
                    if (vcc.getVehicleClass().getId().compareTo(this.vehicleClassId) == 0) {
                        this.model = vcc;
                        throw new Exception("Record was updated by another transaction/user, please try again.",
                                new StaleObjectStateException(vcc.getClass().getSimpleName().concat("Version"), vcc.getId()));
                    }
                }
            }

            ActionResponse response;
            response = adminInsurerService.addNewVehicleClassCeiling(this.model, this.vehicleClassId, this.insurerId);
            setActionResponse(response);

        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }

    @Secured ({"ROLE_CHOX_ADMIN", "ROLE_INS_ADMIN"})
    public String removeVehicleClassCeiling() throws Exception {

        try {
            if (!getIsChoxAdmin() && !canRemoveVehicleClassCeiling(this.vehicleClassCeilingId)) {
                throw new AccessDeniedException("Trying to delete a Vehicle Class Ceiling for an insurer that isn't mine (POSSIBLE HACK ATTEMPT)");
            }
            if (this.vehicleClassCeilingId > 0) {
                VehicleClassCeiling vehicleClassCeiling = vehicleClassCeilingService.getVehicleClassCeiling(vehicleClassCeilingId);
                if (vehicleClassCeiling != null)
                    vehicleClassCeilingService.deleteVehicleClassCeiling(vehicleClassCeiling);
            }

        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }

    private boolean canRemoveVehicleClassCeiling(int vehicleClassCeilingId) {
        int insId = this.insurerId;
        if (insId == -1)
                insId = getUserOrganisationId();

        LOG.debug("Checking removal for insurerID={}, vehicleClassCeilingId={}", insId, vehicleClassCeilingId);
        List<VehicleClassCeiling> ceilingList = adminInsurerService.getVehicleClassCeilingByInsurer(insId);

        for (VehicleClassCeiling vcc : ceilingList) {
            if (vcc.getId() == vehicleClassCeilingId)
                return true;
            LOG.debug("No match with {}", vcc.getId());
        }
        return false;
    }

    @Secured ({"ROLE_CHOX_ADMIN", "ROLE_INS_ADMIN"})
    public String updateVehicleClassCeiling() {
//        if (getUserOrganisationType() == 2 && this.insurerId != getUserOrganisationId()) {
//            LOG.error("Illegal Access detected: InsurerId = {}, getUserOrganisationId={}", insurerId, getUserOrganisationId());
//            throw new AccessDeniedException("Illegal access detected.");
//        }

        try {

            if (!getIsChoxAdmin() && !canRemoveVehicleClassCeiling(this.vehicleClassCeilingId)) {
                LOG.error("Trying to update a Vehicle Class Ceiling for an insurer that isn't mine (POSSIBLE HACK ATTEMPT): {}");
                throw new AccessDeniedException("Trying to update a Vehicle Class Ceiling for an insurer that isn't mine (POSSIBLE HACK ATTEMPT)");
            }
            ActionResponse response;
            response = adminInsurerService.updateVehicleClassCeiling(this.vehicleClassCeilingId, this.hireNetCeiling, this.repairNetCeiling);
            setActionResponse(response);

        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="SERVICES">

    public void setAdminInsurerService(AdminInsurerService adminInsurerService) {
        this.adminInsurerService = adminInsurerService;
    }

    public VehicleClassCeilingService getVehicleClassCeilingService() {
        return vehicleClassCeilingService;
    }

    public void setVehicleClassCeilingService(VehicleClassCeilingService vehicleClassCeilingService) {
        this.vehicleClassCeilingService = vehicleClassCeilingService;
    }
    // </editor-fold>


}
