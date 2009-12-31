package idas.chox.service.admin;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.services.ChorganisationService;
import idas.chox.data.services.DataService;
import idas.chox.service.ActionResponse;


public class AdminChorganisationService extends DataService {

    private ActionResponse actionResponse;
    private ChorganisationService chorganisationService;

    public ActionResponse getActionResponse() {
        return actionResponse;
    }

    public void setActionResponse(ActionResponse actionResponse) {
        this.actionResponse = actionResponse;
    }

    public ChorganisationService getChorganisationService() {
        return chorganisationService;
    }

    public void setChorganisationService(ChorganisationService chorganisationService) {
        this.chorganisationService = chorganisationService;
    }

    public ActionResponse UpdateChorganisationSTatus(Chorganisation chorganisation){

        this.actionResponse = new ActionResponse();

        return getActionResponse();
        
    }

    public Chorganisation UpdateChorganisation(Chorganisation chorganisation){
        chorganisation = chorganisationService.updateChorganisation(chorganisation);        
        return chorganisation;
    }

    public boolean isChorganisationNameExist(String chorganisationName){
        return chorganisationService.isChorgNameExist(chorganisationName);
    }
    
    public ActionResponse UpdateChorganisationStatus(String chorganisationId){

        if(!chorganisationId.equalsIgnoreCase("")){
            
            this.actionResponse = new ActionResponse();

            Chorganisation chorganisation = null;
            chorganisation = chorganisationService.getChorganisation(Integer.valueOf(chorganisationId));

            if (chorganisation.isStatus()) {
                chorganisation.setStatus(false);
            } else {
                chorganisation.setStatus(true);
            }

            this.chorganisationService.updateChorganisation(chorganisation);

        }else{
            
            getActionResponse().AddError("Incorrect Credit Hire Organisation");
            
        }
        
        return getActionResponse();
        
    }

    public Chorganisation getChorganisation(String ChorganisationId) {

        Chorganisation chorganisation = null;
        
        if (!ChorganisationId.equalsIgnoreCase("")) {
            chorganisation = chorganisationService.getChorganisation(Integer.valueOf(ChorganisationId));
        }
        
        return chorganisation;
    }
    

}
