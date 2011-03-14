    package idas.chox.web.actions;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.Workgroup;
import idas.chox.service.ActionResponse;
import idas.chox.service.admin.AdminChorganisationService;
import idas.chox.web.viewdata.ChorganisationViewData;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;

public class ChorganisationAction extends BaseAction implements ModelDriven<Chorganisation>, Preparable {

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

    public String updateChorganisation() throws Exception {

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
