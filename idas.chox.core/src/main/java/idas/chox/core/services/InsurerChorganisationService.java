package idas.chox.core.services;

import java.util.List;
import idas.chox.core.model.InsurerChorganisation;

public interface InsurerChorganisationService {

    void saveInsurerChorganisation(InsurerChorganisation object);

    void deleteInsurerChorganisation(InsurerChorganisation object);

    InsurerChorganisation getInsurerChorganisation(int insurerId, int chorganisationId);

    InsurerChorganisation getInsurerChorganisation(int insurerChorganisationId);

    List<InsurerChorganisation> getInsurerChorganisations(Integer insurerId, Integer chorganisationId);

    List<InsurerChorganisation> getTpiActivatedInsurerChorganisations(Integer insurerId, Integer chorganisationId);

    boolean isMapped(int insurerId, int chorganisationId);
}
