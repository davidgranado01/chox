package chox.web.actions;

import chox.services.ChoBandOrganisationService;
import chox.web.security.PermissionedUser;

public class UploadClaimsAction extends BaseAction {

    private ChoBandOrganisationService choBandOrganisationService;

    public void setChoBandOrganisationService(ChoBandOrganisationService choBandOrganisationService){
        this.choBandOrganisationService = choBandOrganisationService;
    }
            
    public UploadClaimsAction() {
    }
    
    private boolean uploadFlag;
    
    public boolean isUploadFlag() {
        
        PermissionedUser user = getAuthenticatedUser();
        
        uploadFlag = false;
        
        if(user.getIsCHO()){
            int chorgId = user.getUser().getChorganisation().getId();
            uploadFlag = choBandOrganisationService.isActiveChorganisationWithBand(chorgId);
        }
        
        return uploadFlag;
    }
    
    
    
    public String execute() throws Exception {
        return SUCCESS;
    }

}