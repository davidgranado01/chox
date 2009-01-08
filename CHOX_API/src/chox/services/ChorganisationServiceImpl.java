package chox.services;

import chox.model.*;

public class ChorganisationServiceImpl  extends DataService implements ChorganisationService{

    public Chorganisation getCurrentCHOrganisation() {
        
        Chorganisation chorg = new Chorganisation();
        WebUser thisUser = getCurrentUser();
        chorg.setId(thisUser.getChorganisation().getId());
        return chorg;
        
    }
}