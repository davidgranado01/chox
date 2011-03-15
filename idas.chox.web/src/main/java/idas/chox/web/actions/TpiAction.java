/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

/**
 *
 * @author seeni
 */
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.InsurerChorganisation;
import idas.chox.core.services.InsurerChorganisationService;
import idas.chox.core.services.LookupService;
import idas.chox.core.services.UserService;

public class TpiAction extends BaseAction implements ModelDriven<InsurerChorganisation>, Preparable {

    private int objectId;
    private boolean newTpiMapping;
    private InsurerChorganisation model;
    private InsurerChorganisationService insurerChorganisationService;
    private LookupService lkService;
    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setLookupService(LookupService service) {
        this.lkService = service;
    }

    @Override
    public InsurerChorganisation getModel() {
        return model;
    }

    public void setModel(InsurerChorganisation model) {
        this.model = model;
    }

    @Override
    public void prepare() throws Exception {

        if (isNewTpiMapping()) {
            objectId = insurerChorganisationService.getInsurerChorganisation(model.getChorganisation().getId(), model.getInsurer().getId()).getId();
        }

        try {

            if (Integer.valueOf(objectId) > 0) {
                model = insurerChorganisationService.getInsurerChorganisation(objectId);
            } else {
                model = new InsurerChorganisation();
            }

        } catch (Exception ex) {
            handleException(ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="GET SET">
    public boolean isNewTpiMapping() {
        return newTpiMapping;
    }

    public void setNewTpiMapping(boolean newTpiMapping) {
        this.newTpiMapping = newTpiMapping;
    }

    public int getObjectId() {
        return this.objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="ACTIONS">
    public String updateInsurerChorganisation() throws Exception {

        try {
            this.insurerChorganisationService.saveInsurerChorganisation(model);
            return SUCCESS;
        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="SERVICES">
    public void setInsurerChorganisationService(InsurerChorganisationService insurerChorganisationService) {
        this.insurerChorganisationService = insurerChorganisationService;
    }
    // </editor-fold>
}
