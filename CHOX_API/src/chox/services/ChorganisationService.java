package chox.services;

import chox.model.Chorganisation;

public interface ChorganisationService {
    public Chorganisation getCurrentCHOrganisation();
    public Chorganisation getObject(int id);
}
