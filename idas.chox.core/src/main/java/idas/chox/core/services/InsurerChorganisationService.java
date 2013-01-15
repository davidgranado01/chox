package idas.chox.core.services;

import idas.chox.core.model.InsurerChorganisation;
import java.util.List;

public interface InsurerChorganisationService {

    public void saveInsurerChorganisation(InsurerChorganisation object);

    public void deleteInsurerChorganisation(InsurerChorganisation object);

    public InsurerChorganisation getInsurerChorganisation(int insurerId, int chorganisationId);

    public InsurerChorganisation getInsurerChorganisation(int insurerChorganisationId);

    public List<InsurerChorganisation> getInsurerChorganisations(Integer insurerId, Integer chorganisationId);

    public List<InsurerChorganisation> getTpiActivatedInsurerChorganisations(Integer insurerId, Integer chorganisationId);

    public boolean isMapped(int insurerId, int chorganisationId);
}
