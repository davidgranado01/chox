package idas.chox.web.actions;

import idas.chox.core.services.BreBandOrganisationService;
import idas.chox.service.security.PermissionedUser;

public class UploadClaimsAction extends BaseAction {

    private BreBandOrganisationService breBandOrganisationService;

    public void setBreBandOrganisationService(BreBandOrganisationService breBandOrganisationService) {
        this.breBandOrganisationService = breBandOrganisationService;
    }

    public UploadClaimsAction() {
    }
    private boolean uploadFlag;

    public boolean isUploadFlag() {

        PermissionedUser user = getAuthenticatedUser();

        uploadFlag = false;

        if (user.getIsCHO()) {
            int chorgId = user.getUser().getChorganisation().getId();
            uploadFlag = breBandOrganisationService.isActiveChorganisationWithBand(chorgId);
        }

        return uploadFlag;
    }

    public String execute() throws Exception {
        return SUCCESS;
    }
}
