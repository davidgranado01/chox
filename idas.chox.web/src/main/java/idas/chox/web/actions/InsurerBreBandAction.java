package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

import net.sf.json.JSONArray;

import idas.chox.core.model.BreBand;
import idas.chox.service.ActionResponse;
import idas.chox.service.admin.AdminInsurerService;
import idas.chox.web.viewdata.InsurerBreBandViewData;

public class InsurerBreBandAction extends BaseAction implements ModelDriven<BreBand>, Preparable {

    private static final Logger LOG = LoggerFactory.getLogger(InsurerBreBandAction.class);
    private String objectId;
    private int insurerId = -1;
    private BreBand model;
    private List<InsurerBreBandViewData> insurerBreBands;
    private AdminInsurerService adminInsurerService;

    @Secured({"ROLE_CHOX_ADMIN", "ROLE_INS_ADMIN"})
    public String doRenderActionPage() {
        updateModelInSession(Arrays.asList(model));
        return SUCCESS;
    }

    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.insurerBreBands);
        return "{totalCount:" + this.insurerBreBands.size() + ",results:" + jObject.toString() + "}";
    }

    public boolean getIsNew() {

        if (objectId != null && !objectId.equalsIgnoreCase("") && Integer.valueOf(objectId) <= 0) {
            return true;
        }
        return false;
    }

    @Override
    public BreBand getModel() {
        return model;
    }

    public void setModel(BreBand model) {
        this.model = model;
    }

    // <editor-fold defaultstate="collapsed" desc="GET SET">
    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="ACTIONS">

    @Override
    public void prepare() throws Exception {
        try {

            model = new BreBand();

            if (objectId != null && !objectId.equalsIgnoreCase("")) {
                if (Integer.valueOf(objectId) > 0) {
                    model = adminInsurerService.getBreBand(Integer.valueOf(this.objectId));
                    addModelToSession(Arrays.asList(model));
                }
            }

        } catch (Exception ex) {
            handleException(ex);
        }
    }

    public String getInsurerBreBands() {
        if (getUserOrganisationType() == 3 || (getUserOrganisationType() == 2 && this.insurerId != getUserOrganisationId())) {
            throw new AccessDeniedException("Trying to get the insurer BRE Bands for an insurer that isn't mine (POSSIBLE HACK ATTEMPT)");
        }

        try {

            List<BreBand> insurerBreBandData = adminInsurerService.getInsurerBreBands(this.insurerId);
            insurerBreBands = new ArrayList<InsurerBreBandViewData>();
            for (BreBand h : insurerBreBandData) {
                insurerBreBands.add(new InsurerBreBandViewData(h));
            }

        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }

    @Secured({"ROLE_CHOX_ADMIN", "ROLE_INS_ADMIN"})
    public String updateInsurerBreBand() {

        try {
            if (getUserOrganisationType() == 3 || (getUserOrganisationType() == 2 && this.insurerId != getUserOrganisationId())) {
                throw new AccessDeniedException("Trying to update an insurer BRE Band for an insurer that isn't mine (POSSIBLE HACK ATTEMPT)");
            }
            checkVersion(Arrays.asList(model));
            ActionResponse response;
            response = adminInsurerService.updateInsurerBreBand(model, this.insurerId, getIsNew());
            updateModelInSession(Arrays.asList(model));
            setActionResponse(response);
        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }

    @Secured({"ROLE_CHOX_ADMIN", "ROLE_INS_ADMIN"})
    public String deleteInsurerBreBand() {
        try {
            if (getUserOrganisationType() == 3 || (getUserOrganisationType() == 2 && model.getInsurer().getId().intValue() != getUserOrganisationId())) {
                throw new AccessDeniedException("Trying to delete an insurer BRE Band for an insurer that isn't mine (POSSIBLE HACK ATTEMPT)");
            }
            if (model != null) {
                checkVersion(Arrays.asList(model));
                ActionResponse response;
                response = adminInsurerService.deleteInsurerBreBand(model);
                setActionResponse(response);
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
    // </editor-fold>
    
    public boolean isSubscriberClaimsEnabled() {
        return adminInsurerService.getInsurer(insurerId).isAllowSubscriberClaims();
    }
    
    public boolean isFixedFeeClaimsEnabled() {
        return adminInsurerService.getInsurer(insurerId).isAllowFixedFeeClaims();
    }
    
    public boolean isTpiClaimsEnabled() {
        return adminInsurerService.getInsurer(insurerId).isThirdPartyInterventionActivated();
    }
    
    public boolean isInsurerUploadEnabled() {
        return adminInsurerService.getInsurer(insurerId).isClaimUploadEnabled()
                || adminInsurerService.getInsurer(insurerId).isInvoiceUploadEnabled();
    }
}

