package chox.services;

import chox.model.Chorganisation;
import java.util.List;

public interface ChorganisationService {
    public Chorganisation getCurrentCHOrganisation();
    public List<Chorganisation> getChorganisation();
    public Chorganisation getObject(int id);
    public Chorganisation updateObject(Chorganisation object);
    public List<Chorganisation> getActiveChorganisation();
    public List<Chorganisation> getAvailableChorganisationByInsurer(int insurerId);
    public boolean isChorgNameExist(String s);
    public Chorganisation getChorgByName(String s);
    
        
}
