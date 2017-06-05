package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.List;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;

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
    private List<BrePenaltyBandViewData> brePenaltyBandViewData;;
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
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(brePenaltyBandViewData);
        } catch (JsonProcessingException ex) {
            LOG.error("Error converting brePenaltyBandViewData to json string.");
        }
        return "{totalCount:" + this.brePenaltyBandViewData.size() + ",results:" + jsonString + "}";
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

    public void setBrePenaltyBandService(BrePenaltyBandService brePenaltyBandService) {
        this.brePenaltyBandService = brePenaltyBandService;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="ACTIONS">
    @Secured({"ROLE_CHOX_ADMIN", "ROLE_INS_ADMIN"})
    public String getSelectedBreBandPenaltyBand() {

        try {
            // get penalty Bands for the existing breband.
            List<BrePenaltyBand> brePenaltyBands;
            if (breBandId != -1) {
                brePenaltyBands = brePenaltyBandService.getBrePenaltyBands(breBandId);
            } else if (breBandId == -1 && insurerId > 0) { // if it is new breband?
                brePenaltyBands = BrePenaltyBand.getDefaults();
            } else {
                brePenaltyBands = new ArrayList<>(0);
            }
            brePenaltyBandViewData = new ArrayList<>(brePenaltyBands.size());
            int id=0;
            for (BrePenaltyBand bpb : brePenaltyBands) {
                BrePenaltyBandViewData viewData = new BrePenaltyBandViewData(bpb);
                if (breBandId == -1) {
                    // We need unique ids when we have a new BRE Band, otherwise only one will be displayed in the grid
                    // This id will null when the actual BrePenaltyBand is created for persistance
                    viewData.setId(id++);
                }
                brePenaltyBandViewData.add(viewData);
            }
        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }

    // </editor-fold>

}
