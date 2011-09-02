package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.List;
import org.springframework.security.annotation.Secured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.sf.json.JSONArray;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.Chorganisation;
import idas.chox.service.ActionResponse;
import idas.chox.service.admin.AdminChorganisationService;
import idas.chox.web.viewdata.ChorganisationViewData;
import org.springframework.security.AccessDeniedException;

public class ChorganisationAction extends BaseAction implements ModelDriven<Chorganisation>, Preparable {
    private static final Logger LOG = LoggerFactory.getLogger(ChorganisationAction.class);

    private AdminChorganisationService adminChorganisationService;
    private List<ChorganisationViewData> credithireorganisation;
    private int insurerId;
    private String objectId;
    private Chorganisation model;
    private boolean tpiActivated;
    private boolean tpiClaimOnly;
    private String tpiExceptionRegex;
    private String tpiClaimIdentifier;
    private int tpiInsurerId;
    private int tpiWorkgroupId;
    private int tpiClaimOwnerId;

    public boolean isTpiActivated() {
        return tpiActivated;
    }

    public void setTpiActivated(boolean tpiActivated) {
        this.tpiActivated = tpiActivated;
    }

    public String getTpiClaimIdentifier() {
        return tpiClaimIdentifier;
    }

    public void setTpiClaimIdentifier(String tpiClaimIdentifier) {
        this.tpiClaimIdentifier = tpiClaimIdentifier;
    }

    public boolean isTpiClaimOnly() {
        return tpiClaimOnly;
    }

    public void setTpiClaimOnly(boolean tpiClaimOnly) {
        this.tpiClaimOnly = tpiClaimOnly;
    }

    public int getTpiClaimOwnerId() {
        return tpiClaimOwnerId;
    }

    public void setTpiClaimOwnerId(int tpiClaimOwnerId) {
        this.tpiClaimOwnerId = tpiClaimOwnerId;
    }

    public String getTpiExceptionRegex() {
        return tpiExceptionRegex;
    }

    public void setTpiExceptionRegex(String tpiExceptionRegex) {
        this.tpiExceptionRegex = tpiExceptionRegex;
    }

    public int getTpiInsurerId() {
        return tpiInsurerId;
    }

    public void setTpiInsurerId(int tpiInsurerId) {
        this.tpiInsurerId = tpiInsurerId;
    }

    public int getTpiWorkgroupId() {
        return tpiWorkgroupId;
    }

    public void setTpiWorkgroupId(int tpiWorkgroupId) {
        this.tpiWorkgroupId = tpiWorkgroupId;
    }

    public String doRenderActionPage() {
        return SUCCESS;
    }

    public boolean getIsNew() {
        if (this.objectId != null && !objectId.equalsIgnoreCase("")) {
            if (Integer.valueOf(objectId) > 0) {
                return false;
            }
        }
        return true;
    }

    public Chorganisation getModel() {
        return model;
    }

    public void setModel(Chorganisation model) {
        this.model = model;
    }

    public void prepare() throws Exception {

        try {

            model = new Chorganisation();
            if (this.objectId != null && !objectId.equalsIgnoreCase("")) {
                if (Integer.valueOf(objectId) > 0) {
                    model = adminChorganisationService.getChorganisation(objectId);
                }
            }

        } catch (Exception ex) {
            handleException(ex);
        }
    }

    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.credithireorganisation);
        return "{totalCount:" + this.credithireorganisation.size() + ",results:" + jObject.toString() + "}";
    }

    // <editor-fold defaultstate="collapsed" desc="GET SET">
    public String getObjectId() {
        return this.objectId;
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

    @Secured({"ROLE_CHOX_ADMIN"})
    public String triggerChorganisationStatus() throws Exception {

        try {

            ActionResponse response = adminChorganisationService.UpdateChorganisationStatus(this.objectId);
            setActionResponse(response);

        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }

    @Secured({"ROLE_CHOX_ADMIN", "ROLE_CHO_MNG"})
    public String updateChorganisation() throws Exception {

        if ((getUserOrganisationType() == 2)
                || (getUserOrganisationType() == 3 && model.getId() != getUserOrganisationId())) {
            LOG.debug("Failed access validation in updateChorganisation() - throwing AccessDeniedException");
            throw new AccessDeniedException("Error trying to update a CHO to which I have no access (POSSIBLE HACK ATTEMPT)");
        }

        try {

            if (getIsNew()) {
                if (this.adminChorganisationService.isChorganisationNameExist(model.getName())) {
                    this.getActionResponse().AddError("Credit hire name already exist!");
                    return SUCCESS;
                }
            }

            model = adminChorganisationService.UpdateChorganisation(model);

            if (getIsNew()) {
                this.getActionResponse().AssignNewIdResult(model.getId());
            }

        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }

    public String renderTPIPage(){
        return SUCCESS;
    }

    public String getChorganisations() {
        try {
            credithireorganisation = new ArrayList<ChorganisationViewData>();
            List<Chorganisation> choData = this.adminChorganisationService.getAllChorganisations("name");
            for (Chorganisation h : choData) {
                credithireorganisation.add(new ChorganisationViewData(h));
            }

        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }
        return SUCCESS;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="SERVICES">

    public void setAdminChorganisationService(AdminChorganisationService adminChorganisationService) {
        this.adminChorganisationService = adminChorganisationService;
    }
    // </editor-fold>
}
