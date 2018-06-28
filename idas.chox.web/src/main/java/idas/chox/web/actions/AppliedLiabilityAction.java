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

import idas.chox.core.model.BreAppliedLiability;
import idas.chox.core.services.AppliedLiabilityService;
import idas.chox.web.viewdata.AppliedLiabilityViewData;

/**
 *
 * @author john
 */
public class AppliedLiabilityAction extends BaseAction implements ModelDriven<BreAppliedLiability>, Preparable {
    private static final Logger LOG = LoggerFactory.getLogger(AppliedLiabilityAction.class);
    private int breBandId = -1;
    private int insurerId = -1;
    private int appliedLiabilityId = -1;
    private String objectId;
    private BreAppliedLiability model;
    private List<AppliedLiabilityViewData> appliedLiabilityViewData;
    private AppliedLiabilityService appliedLiabilityService;

    public boolean getIsNew() {
        return objectId != null && !objectId.equalsIgnoreCase("") && Integer.valueOf(objectId) <= 0;
    }

    @Override
    public BreAppliedLiability getModel() {
        return model;
    }

    public void setModel(BreAppliedLiability model) {
        this.model = model;
    }

    @Secured({"ROLE_CHOX_ADMIN"})
    public String doRenderActionPage() {
        return SUCCESS;
    }

    @Override
    public void prepare() throws Exception {
        model = new BreAppliedLiability();
    }

    public String getJsonData() {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(appliedLiabilityViewData);
        } catch (JsonProcessingException ex) {
            LOG.error("Error converting appliedLiabilityViewData to json string.");
        }
        return "{totalCount:" + this.appliedLiabilityViewData.size() + ",results:" + jsonString + "}";
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

    public int getAppliedLiabilityId() {
        return appliedLiabilityId;
    }

    public void setAppliedLiabilityId(int appliedLiabilityId) {
        this.appliedLiabilityId = appliedLiabilityId;
    }

    public void setAppliedLiabilityService(AppliedLiabilityService appliedLiabilityService) {
        this.appliedLiabilityService = appliedLiabilityService;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="ACTIONS">
    @Secured({"ROLE_CHOX_ADMIN"})
    public String getAppliedLiability() {

        try {
            // get penalty Bands for the existing breband.
            List<BreAppliedLiability> appliedLiability;
            if (breBandId != -1) {
                appliedLiability = appliedLiabilityService.getAppliedLiabilities(breBandId);
            } else {
                appliedLiability = new ArrayList<>(0);
            }
            appliedLiabilityViewData = new ArrayList<>(appliedLiability.size());
            int id=0;
            for (BreAppliedLiability bpb : appliedLiability) {
                AppliedLiabilityViewData viewData = new AppliedLiabilityViewData(bpb);
                appliedLiabilityViewData.add(viewData);
            }
        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }

    // </editor-fold>
}
