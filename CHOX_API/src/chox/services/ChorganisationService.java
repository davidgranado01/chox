package chox.services;

import chox.model.Chorganisation;
import java.util.List;

public interface ChorganisationService {
    public Chorganisation getCurrentCHOrganisation();
    public List<Chorganisation> getChorganisation();
    public Chorganisation getObject(int id);
    public void updateObject(Chorganisation object);
    public List<Chorganisation> getActiveChorganisation();
    public List<Chorganisation> getAvailableChorganisationByInsurer(int insurerId);
    
        
}
