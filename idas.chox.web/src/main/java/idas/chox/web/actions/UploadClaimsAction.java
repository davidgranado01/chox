package idas.chox.web.actions;

import idas.chox.core.services.ChorganisationService;

public class UploadClaimsAction extends BaseAction {

    private ChorganisationService chorganisationService;
    private boolean uploadFlag;

    public void setChorganisationService(ChorganisationService chorganisationService) {
        this.chorganisationService = chorganisationService;
    }

    public boolean isUploadFlag() {

        uploadFlag = false;

        if (getIsCHO()) {
            int chorgId = getAuthenticatedUser().getChorganisation().getId();
            uploadFlag = chorganisationService.isCreditHireWithBreBand(chorgId);
        }

        return uploadFlag;
    }

    @Override
    public String execute() throws Exception {
        return SUCCESS;
    }
}
