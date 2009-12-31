/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

import idas.chox.core.model.BreBandOrganisation;
import idas.chox.core.services.BreBandOrganisationService;
import idas.chox.core.services.BreBandService;
import idas.chox.core.services.ChorganisationService;

public class doInsurerBreBandMappingAction extends BaseAction {

    private Integer objectId = -1;
    private Integer chorganisationId = -1;
    private Integer breBandId = -1;
    private String actionResult;
    private BreBandOrganisationService service;
    private BreBandService breBandService;
    private ChorganisationService chorganisationService;

    public void setBreBandOrganisationService(BreBandOrganisationService service) {
        this.service = service;
    }

    public void setBreBandService(BreBandService breBandService) {
        this.breBandService = breBandService;
    }

    public void setChorganisationService(ChorganisationService chorganisationService) {
        this.chorganisationService = chorganisationService;
    }

    public String getActionResult() {
        return actionResult;
    }

    public void setActionResult(String actionResult) {
        this.actionResult = actionResult;
    }

    public Integer getBreBandId() {
        return breBandId;
    }

    public void setBreBandId(Integer breBandId) {
        this.breBandId = breBandId;
    }

    public Integer getChorganisationId() {
        return chorganisationId;
    }

    public void setChorganisationId(Integer chorganisationId) {
        this.chorganisationId = chorganisationId;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String removeObject() {

        try {

            BreBandOrganisation object = service.getObject(objectId);
            service.deleteObject(object);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return SUCCESS;
    }

    public String addObject() {

        String sActionMsg = "";
        boolean bActionFlag = false;

        try {

            BreBandOrganisation object = new BreBandOrganisation();
            object.setBreBand(breBandService.getObject(breBandId));
            object.setChorganisation(chorganisationService.getChorganisation(chorganisationId));
            service.updateObject(object);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return SUCCESS;
    }
}
