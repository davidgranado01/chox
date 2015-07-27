package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.annotation.Secured;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

import net.sf.json.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.service.admin.AdminInsurerService;
import idas.chox.web.viewdata.BrePenaltyBandViewData;
import idas.chox.core.model.BrePenaltyBand;
import idas.chox.core.services.BrePenaltyBandService;


public class BreBandPenaltyBandAction extends BaseAction implements ModelDriven<BrePenaltyBand>, Preparable {
    private static final Logger LOG = LoggerFactory.getLogger(BreBandPenaltyBandAction.class);

    private int breBandId = -1;
    private int insurerId = -1;
    private int penaltyBandId = -1;
    private String objectId;
    private BrePenaltyBand model;
    private List<BrePenaltyBandViewData> brePenaltyBandViewData = new ArrayList<>();
    private AdminInsurerService adminInsurerService;
    private BrePenaltyBandService brePenaltyBandService;

    public boolean getIsNew() {
        return objectId != null && !objectId.equalsIgnoreCase("") && Integer.valueOf(objectId) <= 0;
    }

    @Override
    public BrePenaltyBand getModel() {
        return model;
    }

    public void setModel(BrePenaltyBand model) {
        this.model = model;
    }

    @Secured({"ROLE_CHOX_ADMIN", "ROLE_INS_ADMIN"})
    public String doRenderActionPage() {
        return SUCCESS;
    }

    @Override
    public void prepare() throws Exception {
        model = new BrePenaltyBand();
    }

    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.brePenaltyBandViewData);
        return "{totalCount:" + this.brePenaltyBandViewData.size() + ",results:" + jObject.toString() + "}";
    }

    // <editor-fold defaultstate="collapsed" desc="GET SET">

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

    public int getPenaltyBandId() {
        return penaltyBandId;
    }

    public void setPenaltyBandId(int penaltyBandId) {
        this.penaltyBandId = penaltyBandId;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="ACTIONS">
    @Secured({"ROLE_CHOX_ADMIN", "ROLE_INS_ADMIN"})
    public String getSelectedBreBandPenaltyBand() {

        try {
            // get penalty Bands for the existing breband.
            if (breBandId != -1) {
                List<BrePenaltyBand> brePenaltyBands = brePenaltyBandService.getBrePenaltyBands(breBandId);
                for (BrePenaltyBand bpb : brePenaltyBands) {
                    brePenaltyBandViewData.add(new BrePenaltyBandViewData(bpb));
                }
            } else if (breBandId == -1 && insurerId > 0) { // if it is new breband?
//                List<VehicleClassCeiling> vehicleClassCeilings = adminInsurerService.getVehicleClassCeilingByInsurer(this.insurerId);

//                for (VehicleClassCeiling vcc : vehicleClassCeilings) {
//                    vehicleClassCeilingViewData.add(new VehicleClassCeilingViewData(vcc));
//                }
            }
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

    public void setBrePenaltyBandService(BrePenaltyBandService brePenaltyBandService) {
        this.brePenaltyBandService = brePenaltyBandService;
    }

    // </editor-fold>

}
