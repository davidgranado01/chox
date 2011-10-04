package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.List;
import org.springframework.security.annotation.Secured;
import org.springframework.security.AccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import net.sf.json.JSONArray;
import idas.chox.core.model.VehicleClassCeiling;
import idas.chox.service.ActionResponse;
import idas.chox.service.admin.AdminInsurerService;
import idas.chox.web.viewdata.VehicleClassCeilingViewData;
import java.util.Iterator;

public class InsurerVehicleClassCeiling extends BaseAction implements ModelDriven<VehicleClassCeiling>, Preparable {
    private static final Logger LOG = LoggerFactory.getLogger(InsurerVehicleClassCeiling.class);

    private int insurerId = -1;
    private int vehicleClassId = -1;
    private int vehicleClassCeilingId = -1;
    private double hireNetCeiling = 0.00;
    private double repairNetCeiling = 0.00;
    private String objectId;
    private VehicleClassCeiling model;
    private List<VehicleClassCeilingViewData> vehicleClassCeilingViewData = new ArrayList<VehicleClassCeilingViewData>();
    private AdminInsurerService adminInsurerService;

    public boolean getIsNew() {

        if (objectId != null && !objectId.equalsIgnoreCase("") && Integer.valueOf(objectId) <= 0) {
            return true;
        }
        return false;
    }

    @Override
    public VehicleClassCeiling getModel() {
        return model;
    }

    public void setModel(VehicleClassCeiling model) {
        this.model = model;
    }

    public String doRenderActionPage() {
        return SUCCESS;
    }

    @Override
    public void prepare() throws Exception {
        model = new VehicleClassCeiling();
    }

    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.vehicleClassCeilingViewData);
        return "{totalCount:" + this.vehicleClassCeilingViewData.size() + ",results:" + jObject.toString() + "}";
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

    @Secured ({"ROLE_CHOX_ADMIN", "ROLE_INS_MNG"})
    public String addNewVehicleClassCeiling() {

        try {
            if ( getUserOrganisationType() == 3 || (getUserOrganisationType() == 2 && this.insurerId != -1 && this.insurerId != getUserOrganisationId())
                        || (getUserOrganisationType() == 2 && this.insurerId == -1 && model.getInsurer().getId() != getUserOrganisationId())) {
                throw new AccessDeniedException("Trying to add a new Vehicle Class Ceiling for an insurer that isn't mine (POSSIBLE HACK ATTEMPT)");
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

    @Secured ({"ROLE_CHOX_ADMIN", "ROLE_INS_MNG"})
    public String removeVehicleClassCeiling() throws Exception {

        try {
            if (!getIsChoxAdmin() && !canRemoveVehicleClassCeiling(this.vehicleClassCeilingId)) {
                throw new AccessDeniedException("Trying to delete a Vehicle Class Ceiling for an insurer that isn't mine (POSSIBLE HACK ATTEMPT)");
            }
            if (this.vehicleClassCeilingId > 0) {
                ActionResponse response;
                response = adminInsurerService.removeVehicleClassCeiling(this.vehicleClassCeilingId);
                setActionResponse(response);
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

        for (Iterator<VehicleClassCeiling> i = ceilingList.iterator(); i.hasNext(); ) {
            VehicleClassCeiling vcc = i.next();
            if (vcc.getId() == vehicleClassCeilingId)
                return true;
            LOG.debug("No match with {}", vcc.getId());
        }
        return false;
    }

    @Secured ({"ROLE_CHOX_ADMIN", "ROLE_INS_MNG"})
    public String updateVehicleClassCeiling() {

        try {

            if (!getIsChoxAdmin() && !canRemoveVehicleClassCeiling(this.vehicleClassCeilingId)) {
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
    // </editor-fold>
}
