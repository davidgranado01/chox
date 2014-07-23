package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

import net.sf.json.JSONArray;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.services.ChorganisationAliasService;
import idas.chox.service.ActionResponse;
import idas.chox.service.admin.AdminChorganisationService;
import idas.chox.web.viewdata.ChorganisationViewData;

public class ChorganisationAction extends BaseAction implements ModelDriven<Chorganisation>, Preparable {
    private static final Logger LOG = LoggerFactory.getLogger(ChorganisationAction.class);

    private AdminChorganisationService adminChorganisationService;
    private ChorganisationAliasService chorganisationAliasService;
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
    private String originalName;

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

    @Secured({"ROLE_CHOX_ADMIN"})
    public String doRenderActionPage() {
        updateModelInSession(Arrays.asList(model));
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

    @Override
    public Chorganisation getModel() {
        return model;
    }

    public void setModel(Chorganisation model) {
        this.model = model;
    }

    @Override
    public void prepare() throws Exception {

        try {

            model = new Chorganisation();
            if (this.objectId != null && !objectId.equalsIgnoreCase("")) {
                if (Integer.valueOf(objectId) > 0) {
                    model = adminChorganisationService.getChorganisation(objectId);
                    addModelToSession(Arrays.asList(model));
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

    @Secured({"ROLE_CHOX_ADMIN"})
    public String triggerChorganisationStatus() throws Exception {

        try {

            ActionResponse response = adminChorganisationService.updateChorganisationStatus(this.objectId);
            setActionResponse(response);

        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }

    @Secured({"ROLE_CHOX_ADMIN"})
    public String updateChorganisation() throws Exception {
        try {

            if (getIsNew()) {
                if (this.adminChorganisationService.isChorganisationNameExist(model.getName())) {
                    this.getActionResponse().AddError("Credit hire name already exists!");
                    return SUCCESS;
                }
            } else {
                if (!originalName.equals(model.getName())) {
                    if (this.adminChorganisationService.isChorganisationNameExist(model.getName())) {
                        this.getActionResponse().AddError("Credit hire name already exists!");
                        return SUCCESS;
                    }
                }
            }
            checkVersion(Arrays.asList(model));

            model = adminChorganisationService.updateChorganisation(model);
            updateModelInSession(Arrays.asList(model));
            if (getIsNew()) {
                this.getActionResponse().AssignNewIdResult(model.getId());
                chorganisationAliasService.createDefaultRecord(model);
            }

        } catch (Exception ex) {
            handleException(ex);
            return SUCCESS;
        }

        return SUCCESS;
    }

    public String renderTPIPage(){
        return SUCCESS;
    }

    @Secured({"ROLE_CHOX_ADMIN"})
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

    public void setAdminChorganisationService(AdminChorganisationService adminChorganisationService) {
        this.adminChorganisationService = adminChorganisationService;
    }

    public void setChorganisationAliasService(ChorganisationAliasService chorganisationAliasService) {
        this.chorganisationAliasService = chorganisationAliasService;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }
}
