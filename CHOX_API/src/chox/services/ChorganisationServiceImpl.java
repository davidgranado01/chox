package chox.services;

import chox.model.*;

public class ChorganisationServiceImpl implements ChorganisationService{

    public Chorganisation getCurrentCHOrganisation() {
        
        Chorganisation chorg = new Chorganisation();
        chorg.setId(1006);
        return chorg;
    }
}