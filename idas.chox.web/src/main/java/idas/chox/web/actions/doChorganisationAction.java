package idas.chox.web.actions;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.Chorganisation;
import idas.chox.core.services.ChorganisationService;

public class doChorganisationAction extends BaseAction implements ModelDriven<Chorganisation>, Preparable {

    private ChorganisationService service;
    private String objectId;
    private Chorganisation model;

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

    public void setChorganisationService(ChorganisationService service) {
        this.service = service;
    }
    
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ACTION">
    
    public String triggerStatus() throws Exception {

        Chorganisation thisObject = null;
        thisObject = this.service.getObject(Integer.valueOf(objectId));

        if (thisObject.isStatus()) {
            thisObject.setStatus(false);
        } else {
            thisObject.setStatus(true);
        }

        try {
            this.service.updateObject(thisObject);
        } catch (Exception ex) {
            throw ex;
        }

        return SUCCESS;
    }

    public String doRenderActionPage() {
        return SUCCESS;
    }

    public String updateModel() throws Exception {

        try {

            if (getIsNew()) {

                if (this.service.isChorgNameExist(model.getName())) {
                    this.getActionResponse().AddError("Insurer name already exist!");
                    return SUCCESS;
                }
            }

            model = this.service.updateObject(model);

            if (getIsNew()) {
                this.getActionResponse().AssignNewIdResult(model.getId());
            }


        } catch (Exception ex) {
            throw ex;
        }

        return SUCCESS;
    }

    public void prepare() throws Exception {
        if (Integer.valueOf(objectId) <= 0) {
            model = new Chorganisation();
        } else {
            model = service.getObject(Integer.valueOf(objectId));
        }
    }

     // </editor-fold>
}
