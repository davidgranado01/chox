package idas.chox.core.services;

import idas.chox.core.model.Chorganisation;
import java.util.List;

public interface ChorganisationService {

    public Chorganisation getChorganisation(int chorganisationId);

    public List<Chorganisation> getChorganisations(String order);

    public Chorganisation updateChorganisation(Chorganisation chorganisation);

    public List<Chorganisation> getAvailableChorganisationsByInsurer(int insurerId);

    public List<Chorganisation> getActiveChorganisationsByInsurerWithoutBreBand(int insurerId);
   
    public List<Chorganisation> getActiveChorganisation();

    public boolean isChorgNameExist(String chorganisationName);

    public Chorganisation getChorgByName(String chorganisationName);

    public Chorganisation getCurrentCHOrganisation();
}

