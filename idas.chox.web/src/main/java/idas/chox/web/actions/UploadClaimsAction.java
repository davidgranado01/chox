package idas.chox.web.actions;

import idas.chox.core.services.BreBandOrganisationService;

public class UploadClaimsAction extends BaseAction {

    private BreBandOrganisationService breBandOrganisationService;

    public void setBreBandOrganisationService(BreBandOrganisationService breBandOrganisationService) {
        this.breBandOrganisationService = breBandOrganisationService;
    }

    public UploadClaimsAction() {
    }
    private boolean uploadFlag;

    public boolean isUploadFlag() {

        uploadFlag = false;

        if (getIsCHO()) {
            int chorgId = getAuthenticatedUser().getChorganisation().getId();
            uploadFlag = breBandOrganisationService.isActiveChorganisationWithBand(chorgId);
        }

        return uploadFlag;
    }

    public String execute() throws Exception {
        return SUCCESS;
    }
}
