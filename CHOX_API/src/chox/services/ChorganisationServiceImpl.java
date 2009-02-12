package chox.services;

import chox.model.*;

public class ChorganisationServiceImpl  extends SecureDataService implements ChorganisationService{

    public Chorganisation getCurrentCHOrganisation() {
        
        Chorganisation chorg = new Chorganisation();
        WebUser thisUser = getCurrentUser();
        chorg.setId(thisUser.getChorganisation().getId());
        return chorg;
        
    }

    public Chorganisation getObject(int id) {
        return (Chorganisation) get(Chorganisation.class, id);
    }
}