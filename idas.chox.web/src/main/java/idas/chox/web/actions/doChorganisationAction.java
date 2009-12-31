package idas.chox.web.actions;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.Chorganisation;
import idas.chox.service.ActionResponse;
import idas.chox.service.admin.AdminChorganisationService;

public class doChorganisationAction extends BaseAction implements ModelDriven<Chorganisation>, Preparable {

    private AdminChorganisationService adminChorganisationService;
    private String objectId;
    private Chorganisation model;

    public void setAdminChorganisationService(AdminChorganisationService adminChorganisationService) {
        this.adminChorganisationService = adminChorganisationService;
    }

    // <editor-fold defaultstate="collapsed" desc="GET SET">
    
    public boolean getIsNew() {
        if (Integer.valueOf(objectId) <= 0) {
            return true;
        }
        return false;
    }

    public Chorganisation getModel() {
        return model;
    }

    public void setModel(Chorganisation model) {
        this.model = model;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="ACTION">

    public String doRenderActionPage() {
        return SUCCESS;
    }
    
    public String triggerStatus() throws Exception {

        try {

            ActionResponse response = adminChorganisationService.UpdateChorganisationStatus(this.objectId);
            setActionResponse(response);

        } catch (Exception ex) {
            handleException(this, ex);
            return ERROR;
        }

        return SUCCESS;
    }

    public String updateModel() throws Exception {

        try {

            if (getIsNew()) {

                if (this.adminChorganisationService.isChorganisationNameExist(model.getName())) {
                    this.getActionResponse().AddError("Insurer name already exist!");
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

    public void prepare() throws Exception {
        
        try {

            if (Integer.valueOf(objectId) <= 0) {
                model = new Chorganisation();
            } else {
                model = adminChorganisationService.getChorganisation(objectId);
            }

        } catch (Exception ex) {
            handleException(this, ex);
        }
    }
    
    // </editor-fold>
}
