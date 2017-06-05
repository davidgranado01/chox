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

import idas.chox.core.model.ClaimMatchingBand;
import idas.chox.core.services.ClaimMatchingBandService;
import idas.chox.web.viewdata.ClaimMatchingBandViewData;

/**
 *
 * @author john
 */
public class ClaimMatchingBandAction extends BaseAction implements ModelDriven<ClaimMatchingBand>, Preparable {
    private static final Logger LOG = LoggerFactory.getLogger(ClaimMatchingBandAction.class);
    private int breBandId = -1;
    private int insurerId = -1;
    private int claimMatchingBandId = -1;
    private String objectId;
    private ClaimMatchingBand model;
    private List<ClaimMatchingBandViewData> claimMatchingBandViewData;;
    private ClaimMatchingBandService claimMatchingBandService;

    public boolean getIsNew() {
        return objectId != null && !objectId.equalsIgnoreCase("") && Integer.valueOf(objectId) <= 0;
    }

    @Override
    public ClaimMatchingBand getModel() {
        return model;
    }

    public void setModel(ClaimMatchingBand model) {
        this.model = model;
    }

    @Secured({"ROLE_CHOX_ADMIN"})
    public String doRenderActionPage() {
        return SUCCESS;
    }

    @Override
    public void prepare() throws Exception {
        model = new ClaimMatchingBand();
    }

    public String getJsonData() {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(claimMatchingBandViewData);
        } catch (JsonProcessingException ex) {
            LOG.error("Error converting claimMatchingBandViewData to json string.");
        }
        return "{totalCount:" + this.claimMatchingBandViewData.size() + ",results:" + jsonString + "}";
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

    public int getClaimMatchingBandId() {
        return claimMatchingBandId;
    }

    public void setClaimMatchingBandId(int claimMatchingBandId) {
        this.claimMatchingBandId = claimMatchingBandId;
    }

    public void setClaimMatchingBandService(ClaimMatchingBandService claimMatchingBandService) {
        this.claimMatchingBandService = claimMatchingBandService;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="ACTIONS">
    @Secured({"ROLE_CHOX_ADMIN"})
    public String getClaimMatchingBands() {

        try {
            // get penalty Bands for the existing breband.
            List<ClaimMatchingBand> claimMatchingBands;
            if (breBandId != -1) {
                claimMatchingBands = claimMatchingBandService.getClaimMatchingBands(breBandId);
            } else {
                claimMatchingBands = new ArrayList<>(0);
            }
            claimMatchingBandViewData = new ArrayList<>(claimMatchingBands.size());
            int id=0;
            for (ClaimMatchingBand bpb : claimMatchingBands) {
                ClaimMatchingBandViewData viewData = new ClaimMatchingBandViewData(bpb);
                claimMatchingBandViewData.add(viewData);
            }
        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }

    // </editor-fold>
}
