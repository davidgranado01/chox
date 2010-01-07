package idas.chox.web.actions;

import idas.chox.core.services.ChorganisationService;



public class UploadClaimsAction extends BaseAction {

    private ChorganisationService chorganisationService;

    public void setChorganisationService(ChorganisationService chorganisationService) {
        this.chorganisationService = chorganisationService;
    }

    public UploadClaimsAction() {
    }
    private boolean uploadFlag;

    public boolean isUploadFlag() {

        uploadFlag = false;

        if (getIsCHO()) {
            int chorgId = getAuthenticatedUser().getChorganisation().getId();
            uploadFlag = chorganisationService.isCreditHireWithBreBand(chorgId);
        }

        return uploadFlag;
    }

    public String execute() throws Exception {
        return SUCCESS;
    }
}
