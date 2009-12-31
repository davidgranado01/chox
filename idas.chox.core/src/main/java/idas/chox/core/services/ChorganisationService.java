package idas.chox.core.services;

import idas.chox.core.model.Chorganisation;
import java.util.List;

public interface ChorganisationService {

    public List<Chorganisation> getObjectsWithoutInsurer(int insurerId);

    public List<Chorganisation> getObjectsByInsurerId(int insurerId);

    public Chorganisation getCurrentCHOrganisation();

    public List<Chorganisation> getChorganisation();

    public Chorganisation getChorganisation(int id);

    public Chorganisation updateChorganisation(Chorganisation object);

    public List<Chorganisation> getActiveChorganisation();

    public boolean isChorgNameExist(String s);

    public Chorganisation getChorgByName(String s);
}
