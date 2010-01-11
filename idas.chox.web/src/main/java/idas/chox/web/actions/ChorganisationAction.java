package idas.chox.web.actions;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.Chorganisation;
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
            handleException(this, ex);
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
            handleException(this, ex);
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
            handleException(this, ex);
            return ERROR;
        }

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
            handleException(this, ex);
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
